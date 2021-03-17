package kryoapproach;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.esotericsoftware.kryonet.Connection;

public class VideoClient extends KryoClient {

	private final OnImageReceived onImageReceived;
	
	@Override
	protected void registerClasses() {
		Net.register(getKryo());
	}
	
	public VideoClient(String host, int port, OnImageReceived onImageReceived) throws IOException {
		super(VideoServer.BUFFER_SIZE, VideoServer.BUFFER_SIZE, host, port);
		this.onImageReceived = onImageReceived;
	}
	
	public void sendMovePacket(MouseMove mouseMovePacket)
	{
		sendTCP(mouseMovePacket);
	}
	
	public void sendKeyPacket(KeyboardInput keyboardPacket)
	{
		sendTCP(keyboardPacket);
	}
	
	public void sendMouseInputPacket(MouseInput mousePacket)
	{
		sendTCP(mousePacket);
	}
	
	
	@Override
	protected void received(Connection c, Object data) {
		if(data instanceof byte[])
		{
			ByteArrayInputStream byteStream = new ByteArrayInputStream((byte[]) data);
			try {
				BufferedImage outputImage = ImageIO.read(byteStream);
				onImageReceived.onReceived(outputImage);
				
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static interface OnImageReceived
	{
		public void onReceived(BufferedImage image);
	}

}
