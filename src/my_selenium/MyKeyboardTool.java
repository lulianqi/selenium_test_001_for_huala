package my_selenium;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;


public class MyKeyboardTool {

	static Robot rb = null;
	
	public static Boolean initializationKeyboadTool() {
		try {
			rb = new Robot();
			return true;
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	
	public static void Test() {
		rb.keyPress(KeyEvent.VK_F12);
		rb.keyRelease(KeyEvent.VK_F12);
	}
	
}
