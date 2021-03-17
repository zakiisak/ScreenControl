package ftp;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class FtpServer {
	
	private ServerSocket socket;
	private boolean running = true;
	private byte[] dataToSend;
	
	public FtpServer(byte[] dataToSend, int port) throws IOException
	{
		this.dataToSend = dataToSend;
		socket = new ServerSocket(port);
	}
	
	public void clientSend(Socket socket) throws IOException
	{
		OutputStream stream = socket.getOutputStream();
		stream.write(dataToSend);
		stream.flush();
	}
	
	
	public void listen() throws IOException
	{
		running = true;
		while(running)
		{
			try {
				Socket socket = this.socket.accept();
				System.out.println("client accepted");
				while(socket.isConnected())
				{
					clientSend(socket);
					
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
				socket.close();
				running = false;
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		socket.close();
	}
	
	
}
