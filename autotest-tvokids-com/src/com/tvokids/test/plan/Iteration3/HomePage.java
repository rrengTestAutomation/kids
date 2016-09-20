package com.tvokids.test.plan.Iteration3;

import java.io.IOException;
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
import java.lang.reflect.Method;
import com.tvokids.locator.Common;
import com.tvokids.logger.DataProviderForTest;
import com.tvokids.test.helper.*;
import com.tvokids.rerun.*;

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
    @Test(retryAnalyzer = RetryOnFail.class, /*dataProvider = "numberOfTimesToRun", dataProviderClass = DataProviderForTest.class,*/
    groups = {"TC-36104","US-3852"}, priority = 40)
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

	/**
	 * Test Background color for age groups
	 * <p>Date Created: 2016-09-15</p>
	 * <p>Date Modified: 2016-09-15</p>
	 * <p>Original Version: V1</p>
	 * <p>Modified Version: </p>
	 * <p>Xpath: 1</p>
	 * <p>Test Cases: 36144 3414 742</p>
	 */
	@Test(retryAnalyzer = RetryOnFail.class, dataProvider = "numberOfTimesToRun", dataProviderClass = DataProviderForTest.class,
    groups = {"US-36144","US-3414","BUG-742","NEW"}, enabled = false, priority = 43)
	public void testBackgroundColorForAgeGroups() throws IOException, IllegalArgumentException, MalformedURLException {
	       try{
	    	   // INITIALISATION:
	           helper.printXmlPath(new RuntimeException().getStackTrace()[0]);
	           driver = helper.getServerName(driver);
	           
	           // DECLARATION:
	           String colorDefaultBackground;
	           
	           // NAVIGATE TO HOME PAGE:
		       helper.getUrlWaitUntil(driver, 10, Common.homeURL);
		       
		       // AGE 5 AND UNDER:
		       helper.fileWriterPrinter("\n" + "AGE 5 AND UNDER TEST:");		       
		       // AGE 5 AND UNDER DEFAULT COLOR:
		       colorDefaultBackground = helper.getColorHEX(driver, Common.homePageFiveAndUnderBlock, "background", true, "AGE 5 AND UNDER DEFAULT BACKGROUND COLOR");		       
		       // ASSERT AGE 5 AND UNDER BACKGROUND COLOR:
		       helper.assertEquals(driver, new Exception().getStackTrace()[0], colorDefaultBackground, Common.homePageFiveAndUnderBlockBackgroundColorDefault);
		       
		       // AGE 6 AND OVER:
		       helper.fileWriterPrinter("\n" + "AGE 6 AND OVER TEST:");		       
		       // AGE 6 AND OVER DEFAULT COLOR:
		       colorDefaultBackground = helper.getColorHEX(driver, Common.homePageSixAndOverBlock, "background", true, "AGE 6 AND OVER DEFAULT BACKGROUND COLOR");		       
		       // ASSERT 6 AND OVER BACKGROUND COLOR:
		       helper.assertEquals(driver, new Exception().getStackTrace()[0], colorDefaultBackground, Common.homePageSixAndOverBlockBackgroundColorDefault);
		       
	        } catch(Exception e) { helper.getExceptionDescriptive(e, new Exception().getStackTrace()[0], driver); }
	    }
	
}
