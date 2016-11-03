package my_selenium;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;

import org.junit.*;

import com.gargoylesoftware.htmlunit.javascript.host.Set;

import static org.junit.Assert.*;

//调试用，可删除
public class MyMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//myInerTest();
		
		MyTestTast.OpenChrome();
	}
	
	public static void myInerTest() {
		MyhttpTool.setProperty();
		MyhttpTool.sendPost("http://xp.xiaxiaw.com/huala/v2/ws/s/562", "key1=value1&key2=value2",null);
		MyDbTool dbTool;
	 	dbTool=new MyDbTool("jdbc:mysql://192.168.200.152:3306/huala_test", "root", "xpsh");
	 	if(dbTool.connectDb())	{
	 		dbTool.monitorAdd("myP","SELECT * from h_sms WHERE phone ='15158155511'");
	 		if(dbTool.monitorListen("myP", 1))
	 		{
	 			System.out.println("1");
	 		}
	 		else {
	 			System.out.println("2");
			}
	 		dbTool.dbRunSql("SELECT * from h_seller_order WHERE seller_id=562 and (order_status ='shipping')");
		 	System.out.println(dbTool.dbRunSql("SELECT * from h_seller_order WHERE seller_id=562 and (order_status ='shipping')",1,1));
		 	System.out.println(dbTool.dbRunSql("SELECT * from h_seller_order WHERE seller_id=562 and (order_status ='shipping')","id",1));
	 	}

	}
	
	
	static class MyInerClass{
		
	}

}



 class MyTestTast{
	 
	 public static void OpenFirefox() {
		 WebDriver myDriver = new FirefoxDriver();
			//WebDriver myDriver = new InternetExplorerDriver ();
			myDriver.get("http://www.baidu.com");
			myDriver.manage().window().maximize();
			WebElement tb_1=myDriver.findElement(By.name("wd"));
			tb_1.sendKeys("lulianqi15");
			WebElement bt_1=myDriver.findElement(By.id("su"));
			bt_1.click();
	}
	 
	 
	public static void OpenIe() {
		System.setProperty("webdriver.ie.driver", "D:\\selenium\\IEDriverServer.exe");
		WebDriver driver = new InternetExplorerDriver ();	
		driver.manage().window().maximize();
		driver.get("http://www.baidu.com");
		driver.navigate().to("http://www.baidu.com");
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		driver.close();
	}
	
	public static void OpenChrome2() {
		System.setProperty("webdriver.chrome.driver", "D:\\selenium\\chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		//driver.manage() //配置
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);  
		driver.manage().window().maximize();
		driver.get("http://xp.xiaxiaw.com/huala/v2/ws/s/562");
		//driver.navigate().to("http://xp.xiaxiaw.com/huala/v2/ws/s/562");
		//((JavascriptExecutor)driver).executeScript("alert(\"hello,this is a alert!\")");
		try {
			Thread.sleep(20000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		
		 Actions action = new Actions(driver);
		
	    action.click(driver.findElement(By.cssSelector("em.iconfont.icon-location")));
	    action.click(driver.findElement(By.cssSelector("div.s_newaddress > a")));
	    driver.findElement(By.id("phone")).clear();
	    driver.findElement(By.id("phone")).sendKeys("15158155511");
	    
	}
	
	
	public static void OpenChrome() {
		System.setProperty("webdriver.chrome.driver", "D:\\selenium\\chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		//driver.manage() //配置
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);  
		driver.manage().window().maximize();
		driver.get("http://xp.xiaxiaw.com/huala/v2/ws/s/562");
		//driver.navigate().to("http://xp.xiaxiaw.com/huala/v2/ws/s/562");
		//((JavascriptExecutor)driver).executeScript("alert(\"hello,this is a alert!\")");
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		
		/*
		if(MyKeyboardTool.initializationKeyboadTool()){
			MyKeyboardTool.Test();
		}
		else {
			System.out.print("initializationKeyboadTool fial");
		}
		*/
		
		String testPhoneNumString="15158155511";
		String phoneCode="";

	    driver.findElement(By.cssSelector("em.iconfont.icon-location")).click();
	    driver.findElement(By.cssSelector("div.s_newaddress > a")).click();
	    driver.findElement(By.id("phone")).clear();
	    driver.findElement(By.id("phone")).sendKeys(testPhoneNumString);
	    
	    System.out.println("start send phone code");
	    ((JavascriptExecutor)driver).executeScript("sendCode()");
	    
		MyDbTool dbTool;
	 	dbTool=new MyDbTool("jdbc:mysql://192.168.200.152:3306/huala_test", "root", "xpsh");
	 	if(dbTool.connectDb()){
	 		dbTool.monitorAdd("phoneCodeMonitor", String.format("SELECT * from h_sms WHERE phone ='%s'", testPhoneNumString));
	 		if(dbTool.monitorListen("phoneCodeMonitor", 15))
	 		{
	 			phoneCode = (dbTool.dbRunSql(String.format("SELECT * from h_sms WHERE phone ='%s'", testPhoneNumString),3,-1));
			 	System.out.print("phone Code is" +phoneCode);
	 		}
	 		else
	 		{
	 			System.out.print("get phone Code time out");
	 		}
	 	}
	 	else {
			System.out.println("sql error");
		}
	    
	    driver.findElement(By.id("code")).sendKeys(phoneCode);
	    
	    System.out.println(driver.manage().getCookies());
	    driver.findElement(By.xpath("//*[@id=\"loginform\"]/div[3]/div")).click();
	    System.out.println(driver.manage().getCookies());

	    driver.findElement(By.id("consignee")).clear();
	    driver.findElement(By.id("consignee")).sendKeys("lijie_xx");
	    driver.findElement(By.id("mobile")).clear();
	    driver.findElement(By.id("mobile")).sendKeys("15158155511");
	    driver.findElement(By.id("address")).clear();
	    driver.findElement(By.id("address")).sendKeys("no way");
	    ((JavascriptExecutor)driver).executeScript(" $(\"#s-page\").removeClass(\"hidden\").addClass(\"animated slideInFromRight\");$(\"#search\").focus();getSearch('', lat, lng)");
	    driver.findElement(By.id("search")).clear();
	    driver.findElement(By.id("search")).sendKeys("双城国际");
	    System.out.println(driver.manage().getCookies());
	    
	    String myPostCookie="";
	    if(!driver.manage().getCookies().isEmpty())
	    {
	    	myPostCookie="";
	    	Iterator<Cookie> it=driver.manage().getCookies().iterator();
	        while(it.hasNext())
	        {
	        	Cookie ck=it.next();
	        	myPostCookie +=String.format("%s=%s",ck.getName(),ck.getValue()+"; ");
//	            if(ck.getName().endsWith("hltoken")){
//	            }
	        }
	        if(myPostCookie.endsWith("; ")){
	        	myPostCookie=myPostCookie.substring(0,myPostCookie.length()-2);
	        }
	    }
	    System.out.println(myPostCookie);
	    
	    HashMap<String, String> myHeadsHashMap=new HashMap<String, String>();
	    myHeadsHashMap.put("Cookie", myPostCookie);
	    
	    
	    //MyhttpTool.sendPost("http://xp.xiaxiaw.com/huala/v2/wu/address/add?backUrl=/v2/wm/relocation", "consignee=lijie_wwww&mobile="+testPhoneNumString+"&signBuilding=%E5%8F%8C%E5%9F%8E%E5%9B%BD%E9%99%85&address=no+way&lat=30.213331&lng=120.206865&id=&district=%E6%BB%A8%E6%B1%9F%E5%8C%BA",myHeadsHashMap );
	    
	    //driver.findElement(By.cssSelector("input.choosedate_ok")).click();
	    driver.navigate().back();
	    driver.navigate().back();
	    driver.navigate().back();
	    driver.findElement(By.xpath("//*[@id=\"category_sel\"]/a    ")).click();
	    driver.findElement(By.xpath("//*[@id=\"goodsList_sel\"]/li[1]/a    ")).click();
	    //driver.findElement(By.xpath("//*[@id=\"addtocart\"]   ")).click();
	    ((JavascriptExecutor)driver).executeScript("var data = $(\".proinfo\");        var html = '<p class=\"cart_success j-cart_success\">已加入购物车</p>';     	var num = data.find(\".goodsNum\").val();     	num = Number(num)+1;     	if(Cart.cal(sellerId, {skuId:data.find(\".skuId\").val(),goodsNum:num,salePrice:data.find(\".salePrice\").val()})){     		data.find(\".goodsNum\").val(num);	     	$(\"body\").append(html);	     	initCartNum();	     	setTimeout(function(){	      	  $(\".j-cart_success\").remove();	      	},1000);     	}");
	    driver.findElement(By.xpath("//*[@id=\"gd_footer_sel\"]/ul/li[3]/a")).click();
	    driver.findElement(By.xpath("//*[@id=\"cart_sel\"]/article:cartmodule/header/span")).click();
	  
	    return;
		
	    /*
		//WebElement element = driver.findElement(By.id("shownav"));
		WebElement element = driver.findElement(By.xpath("//*[@id=\"header_sel\"]/p[1]/a"));
		element.click();
		
		element = driver.findElement(By.xpath("//*[@id=\"sellerList_sel\"]/div/div[2]/a"));
		element.click();
		
		element = driver.findElement(By.xpath("//*[@id=\"phone\"]"));
		element.sendKeys("15158155511");
		
		driver.findElement(By.id("sendercode")).click();
		((JavascriptExecutor)driver).executeScript("sendCode()");
		 ((JavascriptExecutor)driver).executeScript("alert(\"hello,this is a alert!\")");
		//element.click();
		//element.
		 
		 */
	}
}
 
class MyTesterBak {
	  private WebDriver driver;
	  private String baseUrl;
	  private boolean acceptNextAlert = true;
	  private StringBuffer verificationErrors = new StringBuffer();

	  @Before
	  public void setUp() throws Exception {
	    driver = new FirefoxDriver();
	    baseUrl = "http://xp.xiaxiaw.com/";
	    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	  }

	  @Test
	  public void test14()  {
	    driver.get(baseUrl + "/huala/v2/ws/s/562");
	    driver.findElement(By.cssSelector("em.iconfont.icon-location")).click();
	    driver.findElement(By.cssSelector("div.s_newaddress > a")).click();
	    driver.findElement(By.id("phone")).clear();
	    driver.findElement(By.id("phone")).sendKeys("15158155511");
	    
	    System.out.print("start send phone code");
	    ((JavascriptExecutor)driver).executeScript("sendCode()");
	    
	  }

	  
	  
	  @After
	  public void tearDown() throws Exception {
	    driver.quit();
	    String verificationErrorString = verificationErrors.toString();
	    if (!"".equals(verificationErrorString)) {
	      fail(verificationErrorString);
	    }
	  }

	  private boolean isElementPresent(By by) {
	    try {
	      driver.findElement(by);
	      return true;
	    } catch (NoSuchElementException e) {
	      return false;
	    }
	  }

	  private boolean isAlertPresent() {
	    try {
	      driver.switchTo().alert();
	      return true;
	    } catch (NoAlertPresentException e) {
	      return false;
	    }
	  }

	  private String closeAlertAndGetItsText() {
	    try {
	      Alert alert = driver.switchTo().alert();
	      String alertText = alert.getText();
	      if (acceptNextAlert) {
	        alert.accept();
	      } else {
	        alert.dismiss();
	      }
	      return alertText;
	    } finally {
	      acceptNextAlert = true;
	    }
	  }
	}