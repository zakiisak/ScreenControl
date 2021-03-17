import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class UDPWay {
	
	private static final int PORT = 8894;
	
	private static void server() throws SocketException
	{
		boolean running = true;
		
		DatagramSocket socket = new DatagramSocket(PORT);
		
		byte[] buf = new byte[256];
		DatagramPacket packet = new DatagramPacket(buf, buf.length);
		try {
			//the accept packet
			socket.receive(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		while(running)
		{
			
		}
		socket.close();
	}
	
	public static void main(String[] args)
	{
		
	}
	
}
