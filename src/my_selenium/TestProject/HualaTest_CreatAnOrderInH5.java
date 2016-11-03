package my_selenium.TestProject;

import static org.junit.Assert.*;

import java.util.concurrent.TimeUnit;

import my_selenium.MySeleniumTool;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class HualaTest_CreatAnOrderInH5 {

	  private WebDriver driver;
	  private String baseUrl;
	  private boolean acceptNextAlert = true;
	  private StringBuffer verificationErrors = new StringBuffer();
	
	
	 @Before
	  public void setUp() throws Exception {
		  
		System.setProperty("webdriver.chrome.driver", "D:\\selenium\\chromedriver.exe");
		driver = new ChromeDriver();

		//IE
//		System.setProperty("webdriver.ie.driver", "D:\\selenium\\IEDriverServer.exe");
//	    driver = new InternetExplorerDriver ();	
		
		//火狐
//		driver = new FirefoxDriver();
		
		//baseUrl = "http://xp.xiaxiaw.com";
		baseUrl = "http://xp.liuxia8.cn/";

		// 定位对象时给3s的时间
		// 如果3s内还定位不到则抛出异常
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		// 页面加载超时时间设置为5s
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		// 异步脚本的超时时间设置成3s 
		driver.manage().timeouts().setScriptTimeout(10, TimeUnit.SECONDS);
		  
		driver.manage().window().setPosition(new Point(0, 0));
		driver.manage().window().setSize(new Dimension(560, 1000));
//		driver.manage().window().maximize();

		MySeleniumTool.setGlobalTimeOut(500);
	    MySeleniumTool.setDefaultWebElement(driver);
		
		HualaCommonHandle.setBaseUrl(baseUrl);
	  }

	
	@Test
	public void test() {
		HualaCommonHandle.hualaLogin(driver, "15158155511");
		//HualaCommonHandle.hualaCreatAddress(driver, "15158155511");
		HualaCommonHandle.hualaGotoShop(driver, "562");
		HualaCommonHandle.hualaSelectAddress(driver);
		//HualaCommonHandle.hualaCollectShop(driver);
		for(int i=1;i<100;i++){
			HualaCommonHandle.hualaCreatOrder(driver);
			HualaCommonHandle.hualaGotoShop(driver, "562");
		}
		
	}
	
	@After
	  public void tearDown() throws Exception {
		  MySeleniumTool.releaseDefaultWebElement();
	    //driver.quit();
	    String verificationErrorString = verificationErrors.toString();
	    if (!"".equals(verificationErrorString)) {
	      fail(verificationErrorString);
	    }
	  }

}
