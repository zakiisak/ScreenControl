package screenSender;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.UnknownHostException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import screenSender.VideoClient.OnImageReceived;

public class Main {
	
	public static void main(String[] args)
	{
		final int port = 8695;
		
		JFrame frame = new JFrame();
		frame.setSize(640, 480);
		frame.setLocation(1920, 0);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("JPG Net Stream");
		frame.setVisible(true);
		
		frame.setLayout(new BorderLayout());
		JLabel label = new JLabel(new ImageIcon("forest.jpg"));
		frame.setContentPane(label);
		frame.setLayout(new FlowLayout());
		
		frame.pack();
		
		
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				VideoServer server = null;
				try {
					server = new VideoServer(port);
					server.listen();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}).start();
		
		try {
			VideoClient client = new VideoClient("localhost", port, new OnImageReceived() {
				
				@Override
				public void onReceived(BufferedImage image) {
					if(image != null)
					{
						System.out.println("received image: " + image.getWidth() + "x" + image.getHeight());
						label.setIcon(new ImageIcon(image));
						frame.pack();
					}
				}
			});
			client.listen();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
