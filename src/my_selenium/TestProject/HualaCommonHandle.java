package my_selenium.TestProject;

import static org.junit.Assert.*;
import my_selenium.MyDbTool;
import my_selenium.MySeleniumTool;
import my_selenium.MyhttpTool;

import org.eclipse.jetty.servlet.listener.ELContextCleaner;
import org.junit.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public final class HualaCommonHandle {

	private static String baseUrl;
	private static MyDbTool hualaDbTool;
	
	
	public static void setBaseUrl(String url) {
		baseUrl =url;
	}
	
	public static void setDbTool(String connectString,String userNmae,String pwd) {
		if(hualaDbTool!=null){
			hualaDbTool.closeDb();
			hualaDbTool=null;
		}
		hualaDbTool=new MyDbTool(connectString,userNmae,pwd);
	}
	
	public static MyDbTool getDbTool(){
		if(hualaDbTool!=null)
		{
			if(!hualaDbTool.getIsConnected()){
				hualaDbTool.connectDb();
			}
		}
		return hualaDbTool;
	}
	
	/**
	 * 登录
	 * @param driver
	 * @param testPhoneNumString
	 */
	public static void hualaLogin(WebDriver driver ,String testPhoneNumString) {
		
		//http://xp.xiaxiaw.com/huala/v3#/seller/562
		String phoneCode ="";
	    driver.get(baseUrl + "/huala/v3#/seller/562");
	    MySleep(5000);
	    driver.findElement(By.xpath("//*[@id=\"ng-view\"]/header/form/p[4]/em")).click();
	    driver.findElement(By.xpath("//*[@id=\"ng-view\"]/header/form/nav/ul/li[3]/a")).click();
	    
	    ShowMes("开始登录");
	    driver.findElement(By.xpath("//*[@id=\"loginForm\"]/div[2]/div/form/div[1]/input")).clear();
	    driver.findElement(By.xpath("//*[@id=\"loginForm\"]/div[2]/div/form/div[1]/input")).sendKeys(testPhoneNumString);
	    
	    ShowMes("获取验证码");
	    MyDbTool dbTool;
	 	dbTool=new MyDbTool("jdbc:mysql://192.168.200.152:3306/huala_test", "root", "xpsh");
	 	if(dbTool.connectDb()){
	 		dbTool.monitorAdd("phoneCodeMonitor", String.format("SELECT * from h_sms WHERE phone ='%s'", testPhoneNumString));
	 		ShowMes("触发验证码发送");
	 		driver.findElement(By.xpath("//*[@id=\"loginForm\"]/div[2]/div/form/div[2]/div[2]/button")).click();
	 		if(dbTool.monitorListen("phoneCodeMonitor", 15))
	 		{
	 			phoneCode = (dbTool.dbRunSql(String.format("SELECT * from h_sms WHERE phone ='%s'", testPhoneNumString),3,-1));
			 	System.out.print("phone Code is" +phoneCode);
	 		}
	 		else
	 		{
	 			System.out.print("get phone Code time out");
	 		}
	 		dbTool.monitorRemove("phoneCodeMonitor");
	 		dbTool.closeDb();
	 	}
	 	else {
			System.out.println("sql error");
		}
	    driver.findElement(By.xpath("//*[@id=\"loginForm\"]/div[2]/div/form/div[2]/div[1]/input")).clear();
	    driver.findElement(By.xpath("//*[@id=\"loginForm\"]/div[2]/div/form/div[2]/div[1]/input")).sendKeys(phoneCode);
	    MySleep(1000);
	    ShowMes("登录");
	    driver.findElement(By.xpath("//*[@id=\"loginForm\"]/div[2]/div/form/div[4]/input")).click();
	}
	
	/**
	 * 退出登录
	 * @param driver
	 */
	public static void hualaLoginOut(WebDriver driver )
	{
		ShowMes("退出登录");
		try {
			MySeleniumTool.clickElement("//*[@id=\"ng-view\"]/header/form/p[4]");
		} catch (Exception e) {
			ShowMes("该页未能进入个人这些，转入主页");
			hualaGotoShop(driver,"562");
			MySeleniumTool.clickElement("//*[@id=\"ng-view\"]/header/form/p[4]");
		}
		
		MySeleniumTool.clickElement("//*[@id=\"ng-view\"]/header/form/nav/ul/li[3]/a",2000);
		MySeleniumTool.clickElement("//*[@id=\"ng-view\"]/div[1]/div[3]",1000);
	}
	
	/**
	 * 创建地址
	 * @param driver
	 * @param testPhoneNumString
	 */
	public static void hualaCreatAddress(WebDriver driver,String testPhoneNumString) {
		ShowMes("创建地址");
	    //driver.get(baseUrl + "/huala/v3#/seller");
	    //driver.navigate().to(baseUrl + "/huala/v3#/seller");
	    MySleep(1000);
	    driver.findElement(By.xpath("//*[@id=\"ng-view\"]/header/form/p[2]/a")).click();
	    MySleep(1000);
	    driver.findElement(By.xpath("//*[@id=\"ng-view\"]/a")).click();
	    MySleep(1000);
	    driver.findElement(By.name("consignee")).clear();
	    driver.findElement(By.name("consignee")).sendKeys("sele"+testPhoneNumString);
	    driver.findElement(By.name("mobile")).clear();
	    driver.findElement(By.name("mobile")).sendKeys(testPhoneNumString);
	    driver.findElement(By.cssSelector("em.iconfont")).click();
	    
	    MySleep(2000);
	    driver.findElement(By.xpath("(//input[@type='text'])[5]")).clear();
	    driver.findElement(By.xpath("(//input[@type='text'])[5]")).sendKeys("双城国际");
	    
	    MySleep(1000);
	    driver.findElement(By.cssSelector("p.greyclr.ng-binding")).click();
	    driver.findElement(By.cssSelector("input.choosedate_ok")).click();
	    driver.findElement(By.xpath("//div[@id='ng-view']/div/div[2]/section/div/p[2]")).click();
	}
	
	/**
	 * 选择地址
	 * @param driver
	 */
	public static void hualaSelectAddress(WebDriver driver) {
		ShowMes("选择地址");
		MySleep(1000);
		MySeleniumTool.clickElement("//*[@id=\"ng-view\"]/header/form/p[2]/a");
		MySleep(1000);
		driver.findElement(By.cssSelector("p.ng-binding")).click();
	}
	
	/**
	 * 进入店铺
	 * @param driver
	 * @param shopId
	 */
	public static void hualaGotoShop(WebDriver driver,String shopId) { 
		ShowMes("进入"+shopId);
	    MySleep(1000);
	    driver.navigate().to(baseUrl + "/huala/v3#/seller/"+shopId);
	    driver.navigate().refresh();
	    MySleep(5000);
		
	}
	
	/**
	 * 领取红包
	 */
	public static void hualaGetRedPackets(WebDriver driver) {
		ShowMes("领取红包");
		MySleep(1000);
		try {
			//MySeleniumTool.clickElement( driver ,"//*[@id=\"ng-view\"]/div[2]/div");
			//MySeleniumTool.clickElement( driver ,"//*[@id=\"ng-view\"]/div[2]/div[2]/a");
			//driver.navigate().back();           
			MySeleniumTool.clickElement( driver ,"//*[@id=\"ng-view\"]/div[4]/div");
			MySleep(8000);
			driver.navigate().refresh();
		} catch (Exception e) {
			ShowMes("无法领取红包");
			e.printStackTrace();
		}

	}
	
	/**
	 * 收藏店铺
	 * @param driver
	 */
	public static void hualaCollectShop(WebDriver driver) {
		 ShowMes("收藏店铺");
		 MySleep(1000);
		 driver.findElement(By.cssSelector("div.collect")).click();
	}
	
	/**
	 * 创建订单
	 * @param driver
	 */
	public static void hualaCreatOrder(WebDriver driver) {
		 ShowMes("开始下单");
		 MySleep(1500);
		 driver.findElement(By.linkText("查看全部")).click(); 
		    MySleep(2000);
		    try {
		    	driver.findElement(By.cssSelector("img.ng-scope")).click();
			} catch (Exception e) {
				 e.printStackTrace();
				 ShowMes("重新定位");
				 driver.navigate().refresh();
				 MySleep(5000);
				 driver.findElement(By.cssSelector("img.ng-scope")).click();
			}
		    
		    //driver.findElement(By.xpath("//*[@id=\"ng-view\"]/article:prolist/ul:prolist/li[1]/a")).click();
		    MySleep(1000);
		    driver.findElement(By.cssSelector("div.addtocart_deitail.ng-isolate-scope")).click();
		    MySleep(1000);
		    driver.findElement(By.cssSelector("em.iconfont.icon-big")).click();
		    MySleep(1000);
		    driver.findElement(By.xpath("//footer/div")).click();
		    MySleep(1000);
		    driver.findElement(By.cssSelector("div.pick_up_ps > span.changeaddressico")).click();
		    MySleep(1000);
		    driver.findElement(By.cssSelector("button.gotobuy")).click();
		    
		    ShowMes("确认订单");
		    MySleep(5000);
		    /*Alert alert = driver.switchTo().alert();
		    alert.accept();*/
		    MySeleniumTool.dealTheAlert(driver, true);
		    MySleep(1000);
	}
	
	
	/**
	 * 付款
	 * @param yourPhone
	 * @return
	 */
	public static boolean hualaPayLastOrder(String yourPhone){
		ShowMes(String.format("用户%s开始付款", yourPhone));
		if(getDbTool()==null){
			ShowMes("hualaDbTool未设置");
			return false;
		}
		else {
			String tempOderSn= hualaDbTool.dbRunSql(String.format("select * from h_order  where mobile ='%s' and order_status='no_pay' order by add_time desc", yourPhone), 2, 1);
			if(tempOderSn==null){
				ShowMes(String.format("未发现可妇科订单  电话%s", yourPhone));
				return false;
			}
			if(MyhttpTool.sendGet(baseUrl+"/huala/paytest/"+tempOderSn+ "/0", null, null).equals("{\"success\":true}"))
			{
				ShowMes(String.format("订单%s付款完成", tempOderSn));
				return true;
			}
			else {
				ShowMes(String.format("订单%s付款失败", tempOderSn));
				return false;
			}
		}
	}

	
	/**
	 * 用户确认收货
	 * @param driver
	 */
	public static void hualaUserSureOrder(WebDriver driver , String shopNmae) {
		    ShowMes("确认收货");
		 	driver.navigate().back();
		    MySleep(1000);
		    driver.findElement(By.cssSelector("p.icon-nav > em.iconfont")).click();
		    MySleep(1000);
		    driver.findElement(By.linkText("订单中心")).click();
		    MySleep(1000);
		    driver.findElement(By.linkText(shopNmae)).click();
		    MySleep(1000);
		    driver.findElement(By.xpath("//footer/div/div")).click();
	}
	
	
	/**
	 * 确认收货   (与hualaUserSureOrder功能一致)
	 * @param driver
	 */
	public static void hualaChekInOrder(WebDriver driver , String shopNmae) {
		ShowMes("确认收货");
		//driver.navigate().back();
	    MySleep(1000);
		try {
			driver.findElement(By.cssSelector("p.icon-nav > em.iconfont")).click();
		} catch (Exception e) {
			ShowMes("该页未能进入订单中心，转入主页");
			hualaGotoShop(driver,"562");
			driver.findElement(By.cssSelector("p.icon-nav > em.iconfont")).click();
		}
		
	    MySleep(1000);
	    driver.findElement(By.linkText("订单中心")).click();
	    MySleep(1000);
	    driver.findElement(By.linkText(shopNmae)).click(); 
	    MySleep(1000);
	    driver.findElement(By.xpath("//footer/div/div")).click();
	}
	
	
	
	public static void ShowMes(String yourMes) {
		  System.out.println(String.format("==>%s", yourMes));
	  }
	  
	public static void MySleep(int time) {
		  try {
				Thread.sleep(time);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	  }

}
