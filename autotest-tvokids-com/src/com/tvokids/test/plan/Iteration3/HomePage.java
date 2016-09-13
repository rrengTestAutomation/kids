package com.tvokids.test.plan.Iteration3;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


/*
import java.awt.Robot;
import java.io.File;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
*/
import com.tvokids.locator.Common;
import com.tvokids.test.helper.UtilitiesTestHelper;

@SuppressWarnings("static-access")
public class HomePage {
	static WebDriver driver;
	UtilitiesTestHelper helper = new UtilitiesTestHelper();

    @BeforeMethod public static void startTime(Method method) throws IOException { new UtilitiesTestHelper().startTime(method); }   
    @AfterMethod  public static void endTime() throws IOException { new UtilitiesTestHelper().endTime(); }
    @AfterMethod  @AfterClass   public static void closeBrowsers() { driver.quit(); }
    
    /**
     * Test Home page Browser Title
     * <p>Date Created: 2016-09-13</p>
     * <p>Date Modified: 2016-09-13</p>
     * <p>Original Version: V1</p>
     * <p>Modified Version: </p>
     * <p>Xpath: 1</p>
     * <p>Test Cases: 36104 3852</p>
     */
	@Test(groups = {"TC-36104","US-3852"}, priority = 40)
	public void testHomePageBrowserTitle() throws IOException, IllegalArgumentException, MalformedURLException {
	       try{
	    	   // DECLARATION:
	    	   String expected, actual;
	    	   
	    	   // INITIALISATION:
	           helper.printXmlPath(new RuntimeException().getStackTrace()[0]);
	           driver = helper.getServerName(driver);
	           
	           // NAVIGATE TO HOME PAGE:
		       helper.getUrlWaitUntil(driver, 10, Common.homeURL);
		       
		       // ASSERT BROWSER TITLE:
		       expected = Common.homePageBrowserTitle;
		       actual   = driver.getTitle();
		       helper.assertEquals(driver, new Exception().getStackTrace()[0], actual, expected);
		       
	       } catch(Exception e) { helper.getExceptionDescriptive(e, new Exception().getStackTrace()[0], driver); }
	   }

}
