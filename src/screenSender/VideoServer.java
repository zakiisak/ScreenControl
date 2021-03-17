package screenSender;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;

import javax.imageio.ImageIO;

import ftp.FtpServer;

public class VideoServer extends FtpServer{
	
	public VideoServer(int port) throws IOException {
		super(null, port);
	}
	
	@Override
	public void clientSend(Socket socket) throws IOException {
		BufferedImage image = JNAScreenShot.getScreenshot(new Rectangle(1920, 1080));
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		ImageIO.write(image, "jpg", byteStream);
		byteStream.write(0xFFFFFFFF);
		byteStream.write(0xFFFFFFFF);
		byte[] data = byteStream.toByteArray();
		socket.getOutputStream().write(data);
		socket.getOutputStream().flush();
	}

}
