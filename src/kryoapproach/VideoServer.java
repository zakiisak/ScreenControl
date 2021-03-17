package kryoapproach;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.image.Raster;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.esotericsoftware.kryonet.Connection;

import screenSender.JNAScreenShot;

public class VideoServer extends KryoServer {
	public static final int BUFFER_SIZE = 1024 * 1024 * 5;
	
	private static Map<Integer, User> connectedUsers = new HashMap<Integer, User>();
	private Robot robot;
	
	@Override
	protected void registerClasses() {
		Net.register(getKryo());
	}
	
	
	public VideoServer(int port) throws IOException {
		super(BUFFER_SIZE, BUFFER_SIZE, port);
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void disconnected(Connection c) {
		connectedUsers.remove(c.getID());
	}
	
	
	@Override
	protected void received(Connection c, Object data) {
		if(data instanceof MouseMove)
		{
			MouseMove mouse = (MouseMove) data;
			if(robot != null)
			{
				robot.mouseMove(mouse.x, mouse.y);
			}
			
		}
		else if(data instanceof MouseInput)
		{
			MouseInput input = (MouseInput) data;
			if(robot != null)
			{
				if(input.pressed)
					robot.mousePress(InputEvent.getMaskForButton(input.button));
				else robot.mouseRelease(InputEvent.getMaskForButton(input.button));
			}
		}
		else if(data instanceof KeyboardInput)
		{
			KeyboardInput input = (KeyboardInput) data;
			if(robot != null)
			{
				if(input.pressed)
					robot.keyPress(input.key);
				else robot.keyRelease(input.key);
			}
		}
	}
	
	@Override
	protected void connected(Connection c) {
		super.connected(c);
		System.out.println("c is " + c);
		if(c != null)
		{
			User newUser = new User(c);
			connectedUsers.put(c.getID(), newUser);
			newUser.startSendingImage();
		}
	}
	
	public static class User implements Runnable 
	{
		private final Connection c;
		
		public User(Connection c)
		{
			this.c = c;
		}
		
		public void startSendingImage()
		{
			new Thread(this).start();
		}

		@Override
		public void run() {
			Robot robot = null;
			try {
				robot = new Robot();
			} catch (AWTException e3) {
				// TODO Auto-generated catch block
				e3.printStackTrace();
			}
			while(c.isConnected())
			{
				long before = System.currentTimeMillis();
				
				long time = System.currentTimeMillis();
				/*
				BufferedImage image = JNAScreenShot.getScreenshot(new Rectangle(1920, 1080));
				System.out.println("it took " + (System.currentTimeMillis() - time) + "ms to take screenshot");
				time = System.currentTimeMillis();
				ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
				try {
					ImageIO.write(image, "png", byteStream);
				} catch (IOException e2) {
					e2.printStackTrace();
				}
				*/
				time = System.currentTimeMillis();
				Raster raster = JNAScreenShot.getScreenshotData(new Rectangle(1920, 1080));
				System.out.println("it took " + (System.currentTimeMillis() - time) + " ms to write raster");
				
				System.out.println("it took " + (System.currentTimeMillis() - time) + "ms to write image");
				time = System.currentTimeMillis();
				byte[] finalArray = new byte[] {}; //byteStream.toByteArray();
				System.out.println("it took " + (System.currentTimeMillis() - time) + "ms to get array");
				
				
				try {

					c.sendTCP(finalArray);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				
				long after = System.currentTimeMillis();
				long diff = after - before;
				long amountOfSleep = 16 - diff; //16 * 60 fps = 1000
				System.out.println("ms to sleep " + amountOfSleep);
				if(amountOfSleep > 0)
				{
					try {
						Thread.sleep(amountOfSleep);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

}
