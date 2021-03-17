package ftp;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class FtpClient {
	
	private Socket socket;
	
	public FtpClient(String host, int port) throws UnknownHostException, IOException
	{
		socket = new Socket(host, port);
	}
	
	public void receive(InputStream stream)
	{
		
	}
	
	public void listen() throws IOException
	{
		final InputStream stream = socket.getInputStream();
		while(socket.isConnected())
		{
			receive(stream);
			
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void receiveArray() throws IOException
	{
		final InputStream stream = socket.getInputStream();
		
		System.out.println("receiving...");
		long timeBefore = System.currentTimeMillis();
		byte[] bytes = stream.readAllBytes();
		System.out.println("took " + (System.currentTimeMillis() - timeBefore) + "ms to receive byte of array size: " + Utils.formatSize(bytes.length));
		
		System.out.println("first 10 bytes: ");
		for(int i = 0; i < Math.min(bytes.length, 10); i++)
		{
			System.out.print(bytes[i] + " ");
		}
		System.out.println();
	}
	
}
