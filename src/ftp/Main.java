package ftp;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Random;

public class Main {
	
	public static void main(String[] args)
	{
		Random random = new Random();
		
		//1 gb of data
		byte[] randomData = new byte[1024 * 1024 * 512];
		
		random.nextBytes(randomData);
			
		
		
		final int port = 7542;
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					FtpServer server = new FtpServer(randomData, port);
					server.listen();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		}).start();
		
		
		
		try {
			FtpClient client = new FtpClient("localhost", port);
			client.receiveArray();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
