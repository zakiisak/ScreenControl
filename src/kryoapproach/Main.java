package kryoapproach;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.esotericsoftware.minlog.Log;

import kryoapproach.VideoClient.OnImageReceived;

public class Main {
	
	private static final int port = 5478;
	
	private static void dialog()
	{
		final JFrame frame = new JFrame();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setTitle("Connect or Host?");
		
		
		frame.setLayout(new FlowLayout());
		JButton server = new JButton("Server");
		server.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(false);
				server();
			}
		});
		JButton client = new JButton("Client");
		client.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(false);
				client();
			}
		});
		
		frame.add(client);
		frame.add(server);
		frame.pack();
		frame.setSize(frame.getWidth() + 150, frame.getHeight() + 50);
	}
	
	private static void server()
	{
		JFrame frame = new JFrame();
		frame.setSize(320, 240);
		frame.add(new JLabel("Server running..."));
		frame.pack();
		frame.setVisible(true);
		frame.setTitle("Screen Capturer");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					new VideoServer(port);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	private static void client()
	{
		JFrame frame = new JFrame();
		frame.setSize(640, 480);
		frame.setLocation(1920, 0);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Image Kryo Net Stream");
		frame.setVisible(true);
		
		
		JLabel label = new JLabel(new ImageIcon("forest.jpg"));
		frame.setContentPane(label);
		frame.setLayout(new FlowLayout());
		
		frame.pack();
		
		Log.set(Log.LEVEL_TRACE);
		
		String title = frame.getTitle();
		
		try {
			final VideoClient client = new VideoClient("localhost", port, new OnImageReceived() {
				
				long lastFrame = System.currentTimeMillis();
				long counter = 0;
				
				@Override
				public void onReceived(BufferedImage image) {
					label.setIcon(new ImageIcon(image));
					frame.pack();
					
					double fps = 1000D / (double) (System.currentTimeMillis() - lastFrame);
					if(System.currentTimeMillis() - counter > 1000) {
						frame.setTitle(title + " | " + String.format("%.2f", fps) + " fps");
						counter = System.currentTimeMillis();
					}
					
					lastFrame = System.currentTimeMillis();
				}
			});
			
			
			frame.addMouseMotionListener(new MouseMotionListener() {
				@Override
				public void mouseMoved(MouseEvent e) {
					MouseMove packet = new MouseMove();
					packet.x = e.getX();
					packet.y = e.getY();
					client.sendMovePacket(packet);
				}
				
				@Override
				public void mouseDragged(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
			});
			
			frame.addKeyListener(new KeyListener() {
				
				@Override
				public void keyTyped(KeyEvent e) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void keyReleased(KeyEvent e) {
					KeyboardInput packet = new KeyboardInput();
					packet.key = e.getKeyCode();
					packet.pressed = false;
					client.sendKeyPacket(packet);
					
				}
				
				@Override
				public void keyPressed(KeyEvent e) {
					KeyboardInput packet = new KeyboardInput();
					packet.key = e.getKeyCode();
					packet.pressed = true;
					client.sendKeyPacket(packet);
				}
			});
			
			frame.addMouseListener(new MouseListener() {
				
				@Override
				public void mouseReleased(MouseEvent e) {
					MouseInput packet = new MouseInput();
					packet.button = e.getButton();
					packet.pressed = false;
					client.sendMouseInputPacket(packet);
					
				}
				
				@Override
				public void mousePressed(MouseEvent e) {
					MouseInput packet = new MouseInput();
					packet.button = e.getButton();
					packet.pressed = true;
					client.sendMouseInputPacket(packet);
				}
				
				@Override
				public void mouseExited(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void mouseEntered(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void mouseClicked(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
			});
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		

	}
	
	public static void main(String[] args)
	{
		dialog();
	}
	
}
