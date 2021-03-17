import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.peer.RobotPeer;
import java.lang.reflect.Field;


public class RobotPeerTest {
	
	public static void main(String[] args)
	{
		Robot robot;
		try {
			robot = new Robot();
			
			Field privateField = Robot.class.getDeclaredField("peer");
			privateField.setAccessible(true);
			RobotPeer peer = (RobotPeer) privateField.get(robot);
			System.out.println(peer);
			Rectangle area = new Rectangle(1920, 1080);
			for(int i = 0; i < 60; i++)
			{
				long ms = System.currentTimeMillis();
				int[] pixels = peer.getRGBPixels(area);
				//robot.createScreenCapture(area);
				System.out.println("it took " + (System.currentTimeMillis() - ms) + " ms to fetch 1 frame");
			}
			
		} catch (AWTException | NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e1) {
			e1.printStackTrace();
		}

        
	}
	
}
