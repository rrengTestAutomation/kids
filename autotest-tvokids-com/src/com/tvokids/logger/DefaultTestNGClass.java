package com.tvokids.logger;


import java.lang.reflect.Method;

//import mpower.tvo.org.pages.TvoPages;
//import mpower.tvo.org.pages.WebPage;
import org.testng.ITestResult;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Parameters;

/**
 * This class contain methods for testNG running.
 * @author best
 *
 */
public class DefaultTestNGClass {
    
//  public DriverManager DriverManager = new DriverManager();
//  public TvoPages TvoPages = null;
//  private WebPage WebPage = null;
	
  @BeforeMethod
  public void beforeMethod(Method method) {
	  Logger.writeInfoLogEntry("Method started : " + method.getName());
//	  WebPage.goToWebPage();
//	  WebPage.MaximizeWindow();
  }
  
	@AfterMethod
	public void afterMethod(ITestResult result) {
		Logger.writeInfoLogEntry("Method fineshed : " + result.getMethod().getMethodName());
	}
	  
  	
  @BeforeClass
  @Parameters("browser")
  public void beforeClass(String browser) {	  
//	  TvoPages = new TvoPages(DriverManager.setDriver(browser));
//	  WebPage = new WebPage(DriverManager.getDriver());
	  Logger.writeInfoLogEntry("Start Test on browser = " + browser);
  }

  @AfterClass
  @Parameters("browser")
  public void afterClass(String browser) {
//	  DriverManager.closeDriver();
//	  DriverManager.quitDriver();
	  Logger.writeInfoLogEntry("Fineshed Test on browser = " + browser);
  }

  
}
