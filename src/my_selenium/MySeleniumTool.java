package my_selenium;

import org.apache.commons.lang3.ObjectUtils.Null;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

public final class MySeleniumTool {
	
	/**
     * Don't let anyone instantiate this class.
     */
    private MySeleniumTool() {}

    private static int globalTimeOut = 0;
    
    private static int retryTime=0;
    
    private static WebDriver defaultWebDriver = null;
    
    public static void setGlobalTimeOut(int timeOut)
    {
    	globalTimeOut=timeOut;
    }
    
    public static void setDefaultWebElement(WebDriver webDriver)
    {
    	defaultWebDriver=webDriver;
    }
    
    public static void setReTryTime(int times) {
    	retryTime=times;
	}
    
    public static void releaseDefaultWebElement()
    {
    	defaultWebDriver=null;
    }
    
    
    public static boolean dealTheAlert(WebDriver driver ,boolean option) {
    	//是否存在
    	boolean flag = false;
    	//异常捕获
    	try {
    	    Alert alert = driver.switchTo().alert();
    	    //判断是否存在alert弹框
    	    if (null == alert){
    	        //throw new NoAlertPresentException();
    	    	showSeleniumMes("can not find the Alert");
    	    	return false;
    	    }
    	    //异常捕获
    	    try {
    	        //确认or取消
    	        if (option) {
    	            //确认
    	            alert.accept();
    	            showSeleniumMes("Accept the alert ");
    	        } else {
    	            //取消
    	            alert.dismiss();
    	            showSeleniumMes("Dismiss the alert ");
    	        }
    	        flag = true;
    	    } 
    	    catch (WebDriverException e) {
    	        if (e.getMessage().startsWith("Could not find")){
    	        	showSeleniumMes("There is no alert appear!");
    	        }else{
    	        	showSeleniumMes(e.toString());
    	        }
    	    }
    	} catch (NoAlertPresentException e) {
    		showSeleniumMes("There is no alert appear!");
    	}
    	return flag;
    }
    
    
    public static WebElement pickOutElement(WebDriver driver, String vaule ,int timeOut ,FindElementType findType) 
    {
    	mySeleniumSleep(timeOut);
    	WebElement myElement=null;
    	switch (findType) {
		case Id:
			myElement = driver.findElement(By.id(vaule));
			break;
		case LinkText:
			myElement = driver.findElement(By.linkText(vaule));
		case PartialLinkText:
			myElement = driver.findElement(By.partialLinkText(vaule));
			break;
		case Name:
			myElement = driver.findElement(By.name(vaule));
			break;
		case TagName:
			myElement = driver.findElement(By.tagName(vaule));
			break;
		case CssSelector:
			myElement = driver.findElement(By.cssSelector(vaule));
			break;
		case Xpath:
			myElement = driver.findElement(By.xpath(vaule));
			break;
		default:
			break;
		}
    	if(myElement==null)
    	{
    		showSeleniumMes("can not find the Element with "+vaule);
    		return null;
    	}
    	return myElement;
    }
    
    public static void clickElement(WebDriver driver, String vaule ,int timeOut ,FindElementType findType ,String remarkMes) 
    {
    	if(remarkMes!=null)
    	{
    		showSeleniumMes(remarkMes);
    	}
    	mySeleniumSleep(timeOut);
    	WebElement myElement=null;
    	switch (findType) {
		case Id:
			myElement = driver.findElement(By.id(vaule));
			break;
		case LinkText:
			myElement = driver.findElement(By.linkText(vaule));
		case PartialLinkText:
			myElement = driver.findElement(By.partialLinkText(vaule));
			break;
		case Name:
			myElement = driver.findElement(By.name(vaule));
			break;
		case TagName:
			myElement = driver.findElement(By.tagName(vaule));
			break;
		case CssSelector:
			myElement = driver.findElement(By.cssSelector(vaule));
			break;
		case Xpath:
			myElement = driver.findElement(By.xpath(vaule));
			break;
		default:
			break;
		}
    	if(myElement==null)
    	{
    		showSeleniumMes("can not find the Element with "+vaule);
    		return;
    	}
    	myElement.click();
    }
    
    public static void clickElement(WebDriver driver, String vaule)
    {
    	clickElement(driver,vaule,globalTimeOut,FindElementType.Xpath,null);
    }
    
    public static void clickElement(String vaule ,FindElementType findType ,String remarkMes)
    {
    	clickElement(defaultWebDriver,vaule,globalTimeOut,findType,remarkMes);
    }
    
    public static void clickElement(String vaule)
    {
    	clickElement(defaultWebDriver,vaule,globalTimeOut,FindElementType.Xpath,null);
    }
    
    public static void clickElement(String vaule,int timeOut)
    {
    	clickElement(defaultWebDriver,vaule,timeOut,FindElementType.Xpath,null);
    }
    
    private static void showSeleniumMes(String yourMes) {
		  System.out.println(String.format("MySeleniumTool Message ==>%s", yourMes));
	  }
    
    private static void mySeleniumSleep(int time) {
		  try {
				Thread.sleep(time);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	  }
   
    
}

enum FindElementType {
    Id, LinkText, PartialLinkText, Name, TagName, CssSelector,Xpath;
}