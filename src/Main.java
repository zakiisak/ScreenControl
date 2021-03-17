import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;



public class Main {
public static final int PORT = 9006;
	

	private static final int BUFFER_SIZE = 1024 * 1024 * 10;

	private static byte[] jpgInBytes(BufferedImage image) throws IOException
	{
		byte[] imageInByte = null;
	    try {
	        // convert BufferedImage to byte array
	        ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        ImageIO.write(image, "png", baos);
	        baos.flush();
	        imageInByte = baos.toByteArray();
	        baos.close();
	    } catch (IOException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    }

	    return imageInByte;
	}

	public static void server() throws IOException, AWTException
	{
		Robot robot = new Robot();
		//BufferedImage image = ImageIO.read(new File("forest.jpg"));
		
		ServerSocket socket = new ServerSocket(PORT);
		System.out.println("server started! waiting for clients...");
		Socket client = socket.accept();
		
		
		UDPOutputStream stream = new UDPOutputStream(client.getInetAddress(), PORT);
		
		System.out.println("accepted client " + client.getInetAddress());
		
		
		Rectangle bounds = new Rectangle(320, 240);
		
		for(int i = 0; i < 180000; i++)
		{
			//byte[] data = jpgInBytes(JNAScreenShot.getScreenshot(bounds));
			byte[] data = jpgInBytes(robot.createScreenCapture(bounds));
			System.out.println("sending " + data.length + " bytes");
			//stream.write(data);
			//stream.flush();
			client.getOutputStream().write(data);
			client.getOutputStream().flush();
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("finished writing jpg");
		
		client.close();
		socket.close();
		
	}
	
	public static void client() throws UnknownHostException, SocketException
	{
		JFrame frame = new JFrame();
		frame.setSize(640, 480);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("JPG Net Stream");
		frame.setVisible(true);
		
		frame.setLayout(new BorderLayout());
		JLabel label = new JLabel(new ImageIcon("forest.jpg"));
		frame.setContentPane(label);
		frame.setLayout(new FlowLayout());
		
		frame.pack();
		
		
		Socket socket = null;
		try {
			socket = new Socket("localhost", PORT);
		} catch (UnknownHostException e2) {
			e2.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		
		
		UDPInputStream stream = new UDPInputStream("localhost", PORT);
		
		for(int i = 0; i < 108000; i++)
		{
			long millis = System.currentTimeMillis();
			BufferedImage image = null;
			try {
				image = ImageIO.read(socket.getInputStream());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			if(image != null)
			{
				label.setIcon(new ImageIcon(image));
				frame.pack();
				System.out.println("took " + (System.currentTimeMillis() - millis) + "ms to receive image " + i);
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		if(socket != null) 
		{
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args)
	{
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					server();
				} catch (IOException | AWTException e) {
					e.printStackTrace();
				}
				
			}
		}).start();
		try {
			Thread.sleep(100);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		try {
			client();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
