package screenSender;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import ftp.FtpClient;

public class VideoClient extends FtpClient {

	private final OnImageReceived onImageReceived;
	
	public VideoClient(String host, int port, OnImageReceived onImageReceived) throws UnknownHostException, IOException {
		super(host, port);
		this.onImageReceived = onImageReceived;
	}
	
	@Override
	public void receive(InputStream stream) {
		int nextByte = 0;
		java.nio.ByteBuffer buffer = ByteBuffer.allocate(1024 * 512);
		int counter = 0;
		byte[] last4 = new byte[4];
		try {
			while((nextByte = stream.read()) > -1)
			{
				buffer.put((byte) nextByte);
				
				last4[counter % 4] = (byte) nextByte;
				
				if(last4[0] == -1 && last4[1] == -39 && last4[2] == -1 && last4[3] == -1)
				{
					break;
				}
				counter++;
				if(counter >= buffer.capacity())
					return;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("read " + buffer.position() + " bytes");

		
		ByteArrayInputStream byteStream = new ByteArrayInputStream(buffer.array());
		try {
			BufferedImage outputImage = ImageIO.read(byteStream);
			onImageReceived.onReceived(outputImage);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static interface OnImageReceived
	{
		public void onReceived(BufferedImage image);
	}

}
