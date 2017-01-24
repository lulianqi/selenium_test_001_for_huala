package my_selenium.TestProject;

import static org.junit.Assert.*;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import my_selenium.MySeleniumTool;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class HualaNewUserInShop {

	  private static WebDriver driver;
	  private static String baseUrl;
	  private static boolean acceptNextAlert = true;
	  private static StringBuffer verificationErrors = new StringBuffer();
	
	  private static List<String> newUsers=null;
	
	 @BeforeClass
	  public static void enterClass() throws Exception {
		  
		 HualaCommonHandle.ShowMes("@BeforeClass");
		System.setProperty("webdriver.chrome.driver", "D:\\selenium\\chromedriver.exe");
		driver = new ChromeDriver();

		//IE
//		System.setProperty("webdriver.ie.driver", "D:\\selenium\\IEDriverServer.exe");
//	    driver = new InternetExplorerDriver ();	
		
		//火狐
//		driver = new FirefoxDriver();
		
		//baseUrl = "http://xp.xiaxiaw.com";
		baseUrl = "http://wxtest.huala.com";

		// 定位对象时给3s的时间
		// 如果3s内还定位不到则抛出异常
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
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
		HualaCommonHandle.setDbTool("jdbc:mysql://192.168.200.152:3306/huala_test", "root", "xpsh");
		
	  }

	 
	  @Before
	  public void setUp() throws Exception {
		  HualaCommonHandle.ShowMes("@Before");
	  }
	 
	 
	 @Test
	public void prepareData() {
		 String souceDate="13538947486,13538947363,13539490295,13539474507,13539431483,13539409167,13539767459,13539460249,13538975440,13539467165,13539427681,13539468774,13539479144,13538927254,13538927254";
		 souceDate="13538947342,13539489582,13539429047";
		 souceDate="15118985501,15220670065,15220570980,15220574104,15119053015,15018085622,15220578924,15014942981,13620498267,15119017903";
		 souceDate="13538947486,13538947363,13539490295,13539474507,13539431483,13539409167,13539767459,13539460249,13538975440,13539467165,13539427681,13539468774,13539479144,13538927254,13538927254";
		 String[] phoneStrings= souceDate.split(",");
		 newUsers=Arrays.asList(phoneStrings);
	}
	
    //@Ignore
	@Test
	public void newUser() {
		if(newUsers==null)
		{
			HualaCommonHandle.ShowMes("NULL DATA");
			return;
		}
		for(String nowUser :newUsers )
		{
			HualaCommonHandle.ShowMes(nowUser);
			HualaCommonHandle.hualaLogin(driver, nowUser);
			//HualaCommonHandle.hualaCreatAddress(driver, nowUser);
			HualaCommonHandle.hualaGotoShop(driver, "584"); //584
			//HualaCommonHandle.hualaGetRedPackets(driver);
			HualaCommonHandle.hualaCreatOrder(driver);
			HualaCommonHandle.hualaPayLastOrder(nowUser);
			HualaCommonHandle.hualaUserSureOrder(driver,"AutoShop002"); //AutoShop002
			HualaCommonHandle.hualaLoginOut(driver);
			
			/*
			HualaCommonHandle.ShowMes(nowUser);
			HualaCommonHandle.hualaLogin(driver, nowUser);
			HualaCommonHandle.hualaCreatAddress(driver, nowUser);
			HualaCommonHandle.hualaGotoShop(driver, "562");
			HualaCommonHandle.hualaCollectShop(driver);
			HualaCommonHandle.hualaGetRedPackets(driver);
			HualaCommonHandle.hualaCreatOrder(driver);
			HualaCommonHandle.hualaPayLastOrder(nowUser);
			HualaCommonHandle.hualaUserSureOrder(driver,"Shop001");
			HualaCommonHandle.hualaLoginOut(driver);
			*/
		}
	}
	
	@Ignore
	@Test
	public void newLogin() {
		//13023700122   13534718310
	    String tempUserId="13825472587";
		HualaCommonHandle.hualaLogin(driver, tempUserId);
	    HualaCommonHandle.hualaCreatAddress(driver, tempUserId);
	    HualaCommonHandle.hualaGotoShop(driver, "562");
//		HualaCommonHandle.hualaGetRedPackets(driver);
//		HualaCommonHandle.hualaCreatOrder(driver);
//		HualaCommonHandle.hualaPayLastOrder(tempUserId);
//		HualaCommonHandle.hualaUserSureOrder(driver,"Shop001");
	}
	
	@After
	  public void tearDown() throws Exception {
		HualaCommonHandle.ShowMes("@After");
	  }
	
	@AfterClass
	  public static void leaveClass() throws Exception {
		
		 HualaCommonHandle.ShowMes("@AfterClass");
		  MySeleniumTool.releaseDefaultWebElement();
	    //driver.quit();
	    String verificationErrorString = verificationErrors.toString();
	    if (!"".equals(verificationErrorString)) {
	      fail(verificationErrorString);
	    }
	  }

}
