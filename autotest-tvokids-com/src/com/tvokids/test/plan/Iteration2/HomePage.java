package com.tvokids.test.plan.Iteration2;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
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
import com.tvokids.test.helper.*;
import com.tvokids.test.retry.*;

@SuppressWarnings("static-access")
public class HomePage {
	static WebDriver driver;
	UtilitiesTestHelper helper = new UtilitiesTestHelper();

    @BeforeMethod public static void startTime(Method method) throws IOException { new UtilitiesTestHelper().startTime(method); }   
    @AfterMethod  public static void endTime() throws IOException { new UtilitiesTestHelper().endTime(); }
    @AfterMethod  @AfterClass   public static void closeBrowsers() { driver.quit(); }
    
	/**
	 * Test Home Page elements exist
	 * <p>Date Created: 2016-07-25</p>
	 * <p>Date Modified: 2016-07-25</p>
	 * <p>Original Version: V1</p>
	 * <p>Modified Version: </p>
	 * <p>Xpath: 1</p>
	 * <p>Test Cases: 34908 3214</p>
	 */
	@Test(retryAnalyzer = RetryOnFail.class, /*dataProvider = "numberOfTimesToRun", dataProviderClass = DataProviderForTest.class,*/
    groups = {"TC-34908","US-3214"}, priority = 7)
	public void testHomePageElementsExist() throws IOException, IllegalArgumentException, MalformedURLException {
	       try{
	    	   // INITIALISATION:
	           helper.printXmlPath(new RuntimeException().getStackTrace()[0]);
	           driver = helper.getServerName(driver);
	           
	           // NAVIGATE TO HOME PAGE:
		       helper.getUrlWaitUntil(driver, 10, Common.homeURL);
		          
		       // ASSERT EXISTANCE:
		       helper.assertWebElementExist(driver, new RuntimeException().getStackTrace()[0], Common.homePageLogo);
		       helper.assertWebElementExist(driver, new RuntimeException().getStackTrace()[0], Common.homePageFiveAndUnderBlock);
		       helper.assertWebElementExist(driver, new RuntimeException().getStackTrace()[0], Common.homePageFiveAndUnderTitle);
		       helper.assertWebElementExist(driver, new RuntimeException().getStackTrace()[0], Common.homePageSixAndOverBlock);
		       helper.assertWebElementExist(driver, new RuntimeException().getStackTrace()[0], Common.homePageSixAndOverTitle);      
		          
	       } catch(Exception e) { helper.getExceptionDescriptive(e, new Exception().getStackTrace()[0], driver); }
	   }
	
	/**
	 * Test Home page by click on the specific age image takes under to corresponding age Landing page
	 * <p>Date Created: 2016-07-25</p>
	 * <p>Date Modified: 2016-07-25</p>
	 * <p>Original Version: V1</p>
	 * <p>Modified Version: </p>
	 * <p>Xpath: 1</p>
	 * <p>Test Cases: 34908 3214</p>
	 */
	@Test(retryAnalyzer = RetryOnFail.class, /*dataProvider = "numberOfTimesToRun", dataProviderClass = DataProviderForTest.class,*/
    groups = {"TC-34908","US-3214"}, priority = 8)
	public void testHomePageClickAgeImageNavigatesToCorrectAgeLandingPage() throws IOException, IllegalArgumentException, MalformedURLException {
	       try{
	    	   // INITIALISATION:
	           helper.printXmlPath(new RuntimeException().getStackTrace()[0]);
	           driver = helper.getServerName(driver);
	           
	           // NAVIGATE TO HOME PAGE:
		       helper.getUrlWaitUntil(driver, 10, Common.homeURL);
		          
		       // ASSERT EXISTANCE:
		       helper.clickLinkAndCheckURL(driver, new RuntimeException().getStackTrace()[0], Common.homePageFiveAndUnderBlock, Common.fiveAndUnderURL, true, false);
		       helper.clickLinkAndCheckURL(driver, new RuntimeException().getStackTrace()[0], Common.homePageFiveAndUnderTitle, Common.fiveAndUnderURL, true, false);
		       helper.clickLinkAndCheckURL(driver, new RuntimeException().getStackTrace()[0], Common.homePageSixAndOverBlock, Common.sixAndOverURL, true, false);
		       helper.clickLinkAndCheckURL(driver, new RuntimeException().getStackTrace()[0], Common.homePageSixAndOverTitle, Common.sixAndOverURL,  true, false);     
		          
	       } catch(Exception e) { helper.getExceptionDescriptive(e, new Exception().getStackTrace()[0], driver); }
	   }
	
	/**
	 * Test responsive Desktop and Tablet and Mobile Home page Age Titles position is compatible to the platform being used
	 * <p>Date Created: 2016-07-26</p>
	 * <p>Date Modified: 2016-07-26</p>
	 * <p>Original Version: V1</p>
	 * <p>Modified Version: </p>
	 * <p>Xpath: 1</p>
	 * <p>Test Cases: 34960 3267</p>
	 */
	@Test(retryAnalyzer = RetryOnFail.class, /*dataProvider = "numberOfTimesToRun", dataProviderClass = DataProviderForTest.class,*/
    groups = {"TC-34960","US-3267"}, priority = 9)
	public void testResponsiveDesktopAndMobileDevicesHomePageAgeTitlesPosition() throws IOException, IllegalArgumentException, MalformedURLException {
	       try{
	    	   // INITIALISATION:
	           helper.printXmlPath(new RuntimeException().getStackTrace()[0]);
	           driver = helper.getServerName(driver);
	           
	           // DECLARATION:
	           int alignment;
	           
	           // NAVIGATE TO HOME PAGE:
		       helper.getUrlWaitUntil(driver, 10, Common.homeURL);
		       
		       // DESKTOP:
		       helper.fileWriterPrinter("\n" + "DESKTOP:");
		       driver.manage().window().maximize();		       
		       // ASSERT DESKTOP AGE 5 AND 6 TITLES ARE ALIGNED VERTICALLY AT THEIR TOP:
		       alignment = helper.getElementVerticalAlignmentTop(driver, Common.homePageFiveAndUnderTitle, Common.homePageSixAndOverTitle);
		       helper.assertBooleanTrue(driver, new RuntimeException().getStackTrace()[0], alignment <= 1);
		       // ASSERT DESKTOP AGE 5 TITLE AND AGE 5 BLOCK ARE ALIGNED HORIZONTALLY AT THEIR LEFT:
		       alignment = helper.getElementHorizontalalAlignmentLeft(driver, Common.homePageFiveAndUnderTitle, Common.homePageFiveAndUnderBlock);
		       helper.assertBooleanTrue(driver, new RuntimeException().getStackTrace()[0], alignment <= 1);
		       // ASSERT DESKTOP AGE 6 TITLE AND AGE 6 BLOCK ARE ALIGNED HORIZONTALLY AT THEIR RIGHT:
		       alignment = helper.getElementHorizontalalAlignmentRight(driver, Common.homePageSixAndOverTitle, Common.homePageSixAndOverBlock);
		       helper.assertBooleanTrue(driver, new RuntimeException().getStackTrace()[0], alignment <= 1);
	
		       // TABLET LANDSCAPE:
		       helper.switchWindowSizeToTabletLandscape(driver);
		       // ASSERT TABLET LANDSCAPE AGE 5 AND 6 TITLES ARE ALIGNED VERTICALLY AT THEIR TOP:
		       alignment = helper.getElementVerticalAlignmentTop(driver, Common.homePageFiveAndUnderTitle, Common.homePageSixAndOverTitle);
		       helper.assertBooleanTrue(driver, new RuntimeException().getStackTrace()[0], alignment <= 1);
		       // ASSERT TABLET LANDSCAPE AGE 5 TITLE AND AGE 5 BLOCK ARE ALIGNED HORIZONTALLY AT THEIR LEFT:
		       alignment = helper.getElementHorizontalalAlignmentLeft(driver, Common.homePageFiveAndUnderTitle, Common.homePageFiveAndUnderBlock);
		       helper.assertBooleanTrue(driver, new RuntimeException().getStackTrace()[0], alignment <= 1);
		       // ASSERT TABLET LANDSCAPE AGE 6 TITLE AND AGE 6 BLOCK ARE ALIGNED HORIZONTALLY AT THEIR RIGHT:
		       alignment = helper.getElementHorizontalalAlignmentRight(driver, Common.homePageSixAndOverTitle, Common.homePageSixAndOverBlock);
		       helper.assertBooleanTrue(driver, new RuntimeException().getStackTrace()[0], alignment <= 1);
	
		       // TABLET PORTRAIT:
		       helper.switchWindowSizeToTabletPortrait(driver);
		       // ASSERT TABLET PORTRAIT AGE 5 AND 6 TITLES ARE ALIGNED VERTICALLY AT THEIR TOP:
		       alignment = helper.getElementVerticalAlignmentTop(driver, Common.homePageFiveAndUnderTitle, Common.homePageSixAndOverTitle);
		       helper.assertBooleanTrue(driver, new RuntimeException().getStackTrace()[0], alignment <= 1);
		       // ASSERT TABLET PORTRAIT AGE 5 TITLE AND AGE 5 BLOCK ARE ALIGNED HORIZONTALLY AT THEIR LEFT:
		       alignment = helper.getElementHorizontalalAlignmentLeft(driver, Common.homePageFiveAndUnderTitle, Common.homePageFiveAndUnderBlock);
		       helper.assertBooleanTrue(driver, new RuntimeException().getStackTrace()[0], alignment <= 1);
		       // ASSERT TABLET PORTRAIT AGE 6 TITLE AND AGE 6 BLOCK ARE ALIGNED HORIZONTALLY AT THEIR RIGHT:
		       alignment = helper.getElementHorizontalalAlignmentRight(driver, Common.homePageSixAndOverTitle, Common.homePageSixAndOverBlock);
		       helper.assertBooleanTrue(driver, new RuntimeException().getStackTrace()[0], alignment <= 1);
		       
		       // MOBILE LANDSCAPE:
		       helper.switchWindowSizeToMobileLandscape(driver);
		       // ASSERT MOBILE LANDSCAPE AGE 5 AND 6 TITLES ARE ALIGNED VERTICALLY AT THEIR TOP:
		       alignment = helper.getElementVerticalAlignmentTop(driver, Common.homePageFiveAndUnderTitle, Common.homePageSixAndOverTitle);
		       helper.assertBooleanTrue(driver, new RuntimeException().getStackTrace()[0], alignment <= 1);
		       // ASSERT MOBILE LANDSCAPE AGE 5 TITLE AND AGE 5 BLOCK ARE ALIGNED HORIZONTALLY AT THEIR LEFT:
		       alignment = helper.getElementHorizontalalAlignmentLeft(driver, Common.homePageFiveAndUnderTitle, Common.homePageFiveAndUnderBlock);
		       helper.assertBooleanTrue(driver, new RuntimeException().getStackTrace()[0], alignment <= 1);
		       // ASSERT MOBILE LANDSCAPE AGE 6 TITLE AND AGE 6 BLOCK ARE ALIGNED HORIZONTALLY AT THEIR RIGHT:
		       alignment = helper.getElementHorizontalalAlignmentRight(driver, Common.homePageSixAndOverTitle, Common.homePageSixAndOverBlock);
		       helper.assertBooleanTrue(driver, new RuntimeException().getStackTrace()[0], alignment <= 1);
		       
		       // MOBILE PORTRAIT:
		       helper.switchWindowSizeToMobilePortrait(driver);
		       // ASSERT MOBILE PORTRAIT AGE 5 AND 6 TITLES ARE ALIGNED HORIZONTALLY AT THEIR LEFT:
		       alignment = helper.getElementHorizontalalAlignmentLeft(driver, Common.homePageFiveAndUnderTitle, Common.homePageSixAndOverTitle);
		       helper.assertBooleanTrue(driver, new RuntimeException().getStackTrace()[0], alignment <= 1);
		       // ASSERT MOBILE PORTRAIT AGE 5 TITLE AND AGE 5 BLOCK ARE ALIGNED HORIZONTALLY AT THEIR LEFT:
		       alignment = helper.getElementHorizontalalAlignmentLeft(driver, Common.homePageFiveAndUnderTitle, Common.homePageFiveAndUnderBlock);
		       helper.assertBooleanTrue(driver, new RuntimeException().getStackTrace()[0], alignment <= 1);
		       // ASSERT MOBILE PORTRAIT AGE 6 TITLE AND AGE 6 BLOCK ARE ALIGNED HORIZONTALLY AT THEIR LEFT:
		       alignment = helper.getElementHorizontalalAlignmentLeft(driver, Common.homePageSixAndOverTitle, Common.homePageSixAndOverBlock);
		       helper.assertBooleanTrue(driver, new RuntimeException().getStackTrace()[0], alignment <= 1);
		       // ASSERT DESKTOP AGE 5 TITLE AND AGE 5 BLOCK ARE ALIGNED VERTICALLY AT THEIR TOP:
		       alignment = helper.getElementVerticalAlignmentTop(driver, Common.homePageFiveAndUnderTitle, Common.homePageFiveAndUnderBlock);
		       helper.assertBooleanTrue(driver, new RuntimeException().getStackTrace()[0], alignment <= 1);
		       // ASSERT DESKTOP AGE 6 TITLE AND AGE 6 BLOCK ARE ALIGNED VERTICALLY AT THEIR TOP:
		       alignment = helper.getElementVerticalAlignmentTop(driver, Common.homePageSixAndOverTitle, Common.homePageSixAndOverBlock);
		       helper.assertBooleanTrue(driver, new RuntimeException().getStackTrace()[0], alignment <= 1);		       
		       
	    } catch(Exception e) { helper.getExceptionDescriptive(e, new Exception().getStackTrace()[0], driver); }
	}
	
	/**
	 * Test responsive Desktop and Tablet and Mobile Home page Age Images position is compatible to the platform being used
	 * <p>Date Created: 2016-07-26</p>
	 * <p>Date Modified: 2016-07-26</p>
	 * <p>Original Version: V1</p>
	 * <p>Modified Version: </p>
	 * <p>Xpath: 1</p>
	 * <p>Test Cases: 34960 3267</p>
	 */
	@Test(retryAnalyzer = RetryOnFail.class, /*dataProvider = "numberOfTimesToRun", dataProviderClass = DataProviderForTest.class,*/
    groups = {"TC-34960","US-3267"}, priority = 10)
	public void testResponsiveDesktopAndMobileDevicesHomePageAgeImagesPosition() throws IOException, IllegalArgumentException, MalformedURLException {
	       try{
	    	   // INITIALISATION:
	           helper.printXmlPath(new RuntimeException().getStackTrace()[0]);
	           driver = helper.getServerName(driver);
	           
	           // DECLARATION:
	           int alignment, distance, symmetryRatio;
	           
	           // NAVIGATE TO HOME PAGE:
		       helper.getUrlWaitUntil(driver, 10, Common.homeURL);
		       
		       // DESKTOP:
		       helper.fileWriterPrinter("\n" + "DESKTOP:");
		       driver.manage().window().maximize();			       
		       // ASSERT DESKTOP AGE 5 TITLE IS MORE LEFT THEN AGE 5 IMAGE:
		       distance = helper.getHorizontalDistanceBetweenTwoElements(driver, Common.homePageFiveAndUnderTitle, Common.homePageFiveAndUnderImage);		       
		       helper.assertBooleanTrue(driver, new RuntimeException().getStackTrace()[0], distance > 0);		       
		       // ASSERT DESKTOP AGE 6 TITLE IS MORE RIGHT THEN AGE 6 IMAGE:
		       distance = helper.getHorizontalDistanceBetweenTwoElements(driver, Common.homePageSixAndOverImage, Common.homePageSixAndOverTitle);		       
		       helper.assertBooleanTrue(driver, new RuntimeException().getStackTrace()[0], distance > 0);		       
		       // ASSERT DESKTOP AGE 5 IMAGE AND AGE 5 BLOCK ARE ALIGNED HORIZONTALLY AT THEIR RIGHT:
		       alignment = helper.getElementHorizontalalAlignmentRight(driver, Common.homePageFiveAndUnderImage, Common.homePageFiveAndUnderBlock);
		       helper.assertBooleanTrue(driver, new RuntimeException().getStackTrace()[0], alignment <= 1);		       
		       // ASSERT DESKTOP AGE 6 IMAGE AND AGE 6 BLOCK ARE ALIGNED HORIZONTALLY AT THEIR LEFT:
		       alignment = helper.getElementHorizontalalAlignmentLeft(driver, Common.homePageSixAndOverImage, Common.homePageSixAndOverBlock);
		       helper.assertBooleanTrue(driver, new RuntimeException().getStackTrace()[0], alignment <= 1);
	
		       // TABLET LANDSCAPE:
		       helper.switchWindowSizeToTabletLandscape(driver);
		       
		       // ASSERT TABLET LANDSCAPE AGE 5 TITLE IS SIMMETRIC TO AGE 5 IMAGE:
		       symmetryRatio = helper.getInternalElementHorizontalalSymmetry(driver, Common.homePageFiveAndUnderImage, Common.homePageFiveAndUnderTitle);		       
		       helper.assertBooleanTrue(driver, new RuntimeException().getStackTrace()[0], (symmetryRatio >= 75) && (symmetryRatio <= 100) );		       
		       // ASSERT TABLET LANDSCAPE AGE 6 TITLE IS SIMMETRIC TO AGE 6 IMAGE:
		       symmetryRatio = helper.getInternalElementHorizontalalSymmetry(driver, Common.homePageSixAndOverTitle, Common.homePageSixAndOverImage);		       
		       helper.assertBooleanTrue(driver, new RuntimeException().getStackTrace()[0], (symmetryRatio >= 75) && (symmetryRatio <= 100));		       
		       // ASSERT TABLET LANDSCAPE AGE 5 IMAGE AND AGE 5 BLOCK ARE ALIGNED HORIZONTALLY AT THEIR RIGHT:
		       alignment = helper.getElementHorizontalalAlignmentRight(driver, Common.homePageFiveAndUnderImage, Common.homePageFiveAndUnderBlock);
		       helper.assertBooleanTrue(driver, new RuntimeException().getStackTrace()[0], alignment <= 1);		       
		       // ASSERT TABLET LANDSCAPE AGE 6 IMAGE AND AGE 6 BLOCK ARE ALIGNED HORIZONTALLY AT THEIR LEFT:
		       alignment = helper.getElementHorizontalalAlignmentLeft(driver, Common.homePageSixAndOverImage, Common.homePageSixAndOverBlock);
		       helper.assertBooleanTrue(driver, new RuntimeException().getStackTrace()[0], alignment <= 1);
	
		       // TABLET PORTRAIT:
		       helper.switchWindowSizeToTabletPortrait(driver);
		       // ASSERT TABLET PORTRAIT AGE 5 TITLE IS SIMMETRIC TO AGE 5 IMAGE:
		       symmetryRatio = helper.getInternalElementHorizontalalSymmetry(driver, Common.homePageFiveAndUnderImage, Common.homePageFiveAndUnderTitle);		       
		       helper.assertBooleanTrue(driver, new RuntimeException().getStackTrace()[0], (symmetryRatio >= 75) && (symmetryRatio <= 100));		       
		       // ASSERT TABLET PORTRAIT AGE 6 TITLE IS SIMMETRIC TO AGE 6 IMAGE:
		       symmetryRatio = helper.getInternalElementHorizontalalSymmetry(driver, Common.homePageSixAndOverTitle, Common.homePageSixAndOverImage);		       
		       helper.assertBooleanTrue(driver, new RuntimeException().getStackTrace()[0], (symmetryRatio >= 75) && (symmetryRatio <= 100));	       
		       // ASSERT TABLET PORTRAIT AGE 5 IMAGE AND AGE 5 BLOCK ARE ALIGNED HORIZONTALLY AT THEIR RIGHT:
		       alignment = helper.getElementHorizontalalAlignmentRight(driver, Common.homePageFiveAndUnderImage, Common.homePageFiveAndUnderBlock);
		       helper.assertBooleanTrue(driver, new RuntimeException().getStackTrace()[0], alignment <= 1);		       
		       // ASSERT TABLET PORTRAIT AGE 6 IMAGE AND AGE 6 BLOCK ARE ALIGNED HORIZONTALLY AT THEIR LEFT:
		       alignment = helper.getElementHorizontalalAlignmentLeft(driver, Common.homePageSixAndOverImage, Common.homePageSixAndOverBlock);
		       helper.assertBooleanTrue(driver, new RuntimeException().getStackTrace()[0], alignment <= 1);
		       
		       // MOBILE LANDSCAPE:
		       helper.switchWindowSizeToMobileLandscape(driver);
		       // ASSERT MOBILE LANDSCAPE AGE 5 TITLE IS SIMMETRIC TO AGE 5 IMAGE:
		       symmetryRatio = helper.getInternalElementHorizontalalSymmetry(driver, Common.homePageFiveAndUnderImage, Common.homePageFiveAndUnderTitle);		       
		       helper.assertBooleanTrue(driver, new RuntimeException().getStackTrace()[0], (symmetryRatio >= 75) && (symmetryRatio <= 100));		       
		       // ASSERT MOBILE LANDSCAPE AGE 6 TITLE IS SIMMETRIC TO AGE 6 IMAGE:
		       symmetryRatio = helper.getInternalElementHorizontalalSymmetry(driver, Common.homePageSixAndOverTitle, Common.homePageSixAndOverImage);		       
		       helper.assertBooleanTrue(driver, new RuntimeException().getStackTrace()[0], (symmetryRatio >= 75) && (symmetryRatio <= 100));	       
		       // ASSERT MOBILE LANDSCAPE AGE 5 IMAGE AND AGE 5 BLOCK ARE ALIGNED HORIZONTALLY AT THEIR RIGHT:
		       alignment = helper.getElementHorizontalalAlignmentRight(driver, Common.homePageFiveAndUnderImage, Common.homePageFiveAndUnderBlock);
		       helper.assertBooleanTrue(driver, new RuntimeException().getStackTrace()[0], alignment <= 1);		       
		       // ASSERT MOBILE LANDSCAPE AGE 6 IMAGE AND AGE 6 BLOCK ARE ALIGNED HORIZONTALLY AT THEIR LEFT:
		       alignment = helper.getElementHorizontalalAlignmentLeft(driver, Common.homePageSixAndOverImage, Common.homePageSixAndOverBlock);
		       helper.assertBooleanTrue(driver, new RuntimeException().getStackTrace()[0], alignment <= 1);
	
		       // MOBILE PORTRAIT:
		       helper.switchWindowSizeToMobilePortrait(driver);		       
		       // ASSERT MOBILE PORTRAIT AGE 5 TITLE AND AGE 5 IMAGE ARE ALIGNED HORIZONTALLY AT THEIR LEFT:
		       alignment = helper.getElementHorizontalalAlignmentLeft(driver, Common.homePageFiveAndUnderTitle, Common.homePageFiveAndUnderImage);		       
		       helper.assertBooleanTrue(driver, new RuntimeException().getStackTrace()[0], alignment <= 1);		       
		       // ASSERT MOBILE PORTRAIT AGE 6 TITLE AND AGE 6 IMAGE ARE ALIGNED HORIZONTALLY AT THEIR LEFT:
		       alignment = helper.getElementHorizontalalAlignmentLeft(driver, Common.homePageSixAndOverImage, Common.homePageSixAndOverTitle);
		       helper.assertBooleanTrue(driver, new RuntimeException().getStackTrace()[0], alignment <= 1);		       
		       // ASSERT MOBILE PORTRAIT AGE 5 TITLE AND AGE 5 IMAGE ARE ALIGNED VERTICALLY AT THEIR TOP:
		       alignment = helper.getElementVerticalAlignmentTop(driver, Common.homePageFiveAndUnderTitle, Common.homePageFiveAndUnderImage);		       
		       helper.assertBooleanTrue(driver, new RuntimeException().getStackTrace()[0], alignment <= 1);		       
		       // ASSERT MOBILE PORTRAIT AGE 6 TITLE AND AGE 6 IMAGE ARE ALIGNED VERTICALLY AT THEIR TOP:
		       alignment = helper.getElementVerticalAlignmentTop(driver, Common.homePageSixAndOverImage, Common.homePageSixAndOverTitle);
		       helper.assertBooleanTrue(driver, new RuntimeException().getStackTrace()[0], alignment <= 1);
	     
	    } catch(Exception e) { helper.getExceptionDescriptive(e, new Exception().getStackTrace()[0], driver); }
	}
	
	/**
	 * Test responsive Desktop and Tablet and Mobile Home page Age Blocks sections are 50% of the browser width
	 * <p>Date Created: 2016-07-26</p>
	 * <p>Date Modified: 2016-07-26</p>
	 * <p>Original Version: V1</p>
	 * <p>Modified Version: </p>
	 * <p>Xpath: 1</p>
	 * <p>Test Cases: 34960 3267</p>
	 */
	@Test(retryAnalyzer = RetryOnFail.class, /*dataProvider = "numberOfTimesToRun", dataProviderClass = DataProviderForTest.class,*/
    groups = {"TC-34960","US-3267"}, priority = 11)
	public void testResponsiveDesktopAndMobileDevicesHomePageAgeBlocksOccupyHalfBrowser() throws IOException, IllegalArgumentException, MalformedURLException {
	       try{
	    	   // INITIALISATION:
	           helper.printXmlPath(new RuntimeException().getStackTrace()[0]);
	           driver = helper.getServerName(driver);
	           
	           // DECLARATION:
	           double ageFiveWidth, ageSixWidth, ageFiveWidthPercents, ageSixWidthPercents;
	           double ageFiveHeight, ageSixHeight, ageFiveHeightPercents, ageSixHeightPercents;
	           DecimalFormat df = new DecimalFormat("#.###");
	           Boolean acceptanceCriteria;
	           
	           // NAVIGATE TO HOME PAGE:
		       helper.getUrlWaitUntil(driver, 10, Common.homeURL);
		       
		       // DESKTOP:
		       helper.fileWriterPrinter("\n" + "DESKTOP:");
		       driver.manage().window().maximize();
		       // ASSERT DESKTOP SCREEN DIVISION BY AGES IS HALF-HALF:
		       ageFiveWidth = helper.getElementWidth(driver, Common.homePageFiveAndUnderBlock);
		       ageSixWidth  = helper.getElementWidth(driver, Common.homePageSixAndOverBlock);
		       helper.fileWriterPrinter("\nDESKTOP SCREEN AGE 5 BLOCK WIDTH (PIXELS) = " + Integer.valueOf(df.format(ageFiveWidth)));
		       helper.fileWriterPrinter(  "DESKTOP SCREEN AGE 6 BLOCK WIDTH (PIXELS) = " + Integer.valueOf(df.format(ageSixWidth)));
		       ageFiveWidthPercents = (double) ( (ageFiveWidth*100/(ageFiveWidth + ageSixWidth))*1000/1000.000   );
		       ageSixWidthPercents  = (double) (  (ageSixWidth*100/(ageFiveWidth + ageSixWidth))*1000/1000.000   );
		       helper.fileWriterPrinter("DESKTOP SCREEN  - COVERAGE BY AGE 5 BLOCK = " + df.format(ageFiveWidthPercents) + " %");
		       helper.fileWriterPrinter("DESKTOP SCREEN  - COVERAGE BY AGE 6 BLOCK = " + df.format(ageSixWidthPercents ) + " %");
		       acceptanceCriteria = (ageFiveWidthPercents >= 49) && (ageFiveWidthPercents <= 51);
		       helper.assertBooleanTrue(driver, new Exception().getStackTrace()[0], acceptanceCriteria);
		       acceptanceCriteria = (ageSixWidthPercents >= 49) && (ageSixWidthPercents <= 51);
		       helper.assertBooleanTrue(driver, new Exception().getStackTrace()[0], acceptanceCriteria);
		       
		       // TABLET LANDSCAPE:
		       helper.switchWindowSizeToTabletLandscape(driver);
		       // ASSERT TABLET LANDSCAPE SCREEN DIVISION BY AGES IS HALF-HALF:
		       ageFiveWidth = helper.getElementWidth(driver, Common.homePageFiveAndUnderBlock);
		       ageSixWidth  = helper.getElementWidth(driver, Common.homePageSixAndOverBlock);	       
		       helper.fileWriterPrinter("\nTABLET LANDSCAPE SCREEN AGE 5 BLOCK WIDTH (PIXELS) = " + Integer.valueOf(df.format(ageFiveWidth)));
		       helper.fileWriterPrinter(  "TABLET LANDSCAPE SCREEN AGE 6 BLOCK WIDTH (PIXELS) = " + Integer.valueOf(df.format(ageSixWidth)));	       
		       ageFiveWidthPercents = (double) ( (ageFiveWidth*100/(ageFiveWidth + ageSixWidth))*1000/1000.000   );
		       ageSixWidthPercents  = (double) (  (ageSixWidth*100/(ageFiveWidth + ageSixWidth))*1000/1000.000   );	       
		       helper.fileWriterPrinter("TABLET LANDSCAPE SCREEN  - COVERAGE BY AGE 5 BLOCK = " + df.format(ageFiveWidthPercents) + " %");
		       helper.fileWriterPrinter("TABLET LANDSCAPE SCREEN  - COVERAGE BY AGE 6 BLOCK = " + df.format(ageSixWidthPercents ) + " %");	       
		       acceptanceCriteria = (ageFiveWidthPercents >= 49) && (ageFiveWidthPercents <= 51);
		       helper.assertBooleanTrue(driver, new Exception().getStackTrace()[0], acceptanceCriteria);	       
		       acceptanceCriteria = (ageSixWidthPercents >= 49) && (ageSixWidthPercents <= 51);
		       helper.assertBooleanTrue(driver, new Exception().getStackTrace()[0], acceptanceCriteria);
		       
		       // TABLET PORTRAIT:
		       helper.switchWindowSizeToTabletPortrait(driver);
		       // ASSERT TABLET PORTRAIT SCREEN DIVISION BY AGES IS HALF-HALF:
		       ageFiveWidth = helper.getElementWidth(driver, Common.homePageFiveAndUnderBlock);
		       ageSixWidth  = helper.getElementWidth(driver, Common.homePageSixAndOverBlock);	       
		       helper.fileWriterPrinter("\nTABLET PORTRAIT SCREEN AGE 5 BLOCK WIDTH (PIXELS) = " + Integer.valueOf(df.format(ageFiveWidth)));
		       helper.fileWriterPrinter(  "TABLET PORTRAIT SCREEN AGE 6 BLOCK WIDTH (PIXELS) = " + Integer.valueOf(df.format(ageSixWidth)));	       
		       ageFiveWidthPercents = (double) ( (ageFiveWidth*100/(ageFiveWidth + ageSixWidth))*1000/1000.000   );
		       ageSixWidthPercents  = (double) (  (ageSixWidth*100/(ageFiveWidth + ageSixWidth))*1000/1000.000   );	       
		       helper.fileWriterPrinter("TABLET PORTRAIT SCREEN  - COVERAGE BY AGE 5 BLOCK = " + df.format(ageFiveWidthPercents) + " %");
		       helper.fileWriterPrinter("TABLET PORTRAIT SCREEN  - COVERAGE BY AGE 6 BLOCK = " + df.format(ageSixWidthPercents ) + " %");	       
		       acceptanceCriteria = (ageFiveWidthPercents >= 49) && (ageFiveWidthPercents <= 51);
		       helper.assertBooleanTrue(driver, new Exception().getStackTrace()[0], acceptanceCriteria);	       
		       acceptanceCriteria = (ageSixWidthPercents >= 49) && (ageSixWidthPercents <= 51);
		       helper.assertBooleanTrue(driver, new Exception().getStackTrace()[0], acceptanceCriteria);
		       
		       // MOBILE LANDSCAPE:
		       helper.switchWindowSizeToMobileLandscape(driver);
		       // ASSERT MOBILE LANDSCAPE SCREEN DIVISION BY AGES IS HALF-HALF:
		       ageFiveWidth = helper.getElementWidth(driver, Common.homePageFiveAndUnderBlock);
		       ageSixWidth  = helper.getElementWidth(driver, Common.homePageSixAndOverBlock);	       
		       helper.fileWriterPrinter("\nMOBILE LANDSCAPE SCREEN AGE 5 BLOCK WIDTH (PIXELS) = " + Integer.valueOf(df.format(ageFiveWidth)));
		       helper.fileWriterPrinter(  "MOBILE LANDSCAPE SCREEN AGE 6 BLOCK WIDTH (PIXELS) = " + Integer.valueOf(df.format(ageSixWidth)));	       
		       ageFiveWidthPercents = (double) ( (ageFiveWidth*100/(ageFiveWidth + ageSixWidth))*1000/1000.000   );
		       ageSixWidthPercents  = (double) (  (ageSixWidth*100/(ageFiveWidth + ageSixWidth))*1000/1000.000   );	       
		       helper.fileWriterPrinter("MOBILE LANDSCAPE SCREEN  - COVERAGE BY AGE 5 BLOCK = " + df.format(ageFiveWidthPercents) + " %");
		       helper.fileWriterPrinter("MOBILE LANDSCAPE SCREEN  - COVERAGE BY AGE 6 BLOCK = " + df.format(ageSixWidthPercents ) + " %");	       
		       acceptanceCriteria = (ageFiveWidthPercents >= 49) && (ageFiveWidthPercents <= 51);
		       helper.assertBooleanTrue(driver, new Exception().getStackTrace()[0], acceptanceCriteria);	       
		       acceptanceCriteria = (ageSixWidthPercents >= 49) && (ageSixWidthPercents <= 51);
		       helper.assertBooleanTrue(driver, new Exception().getStackTrace()[0], acceptanceCriteria);
		       
		       // MOBILE PORTRAIT:
		       helper.switchWindowSizeToMobilePortrait(driver);
		       // ASSERT MOBILE PORTRAIT SCREEN DIVISION BY AGES IS HALF-HALF:
		       ageFiveHeight = helper.getElementHeight(driver, Common.homePageFiveAndUnderBlock);
		       ageSixHeight  = helper.getElementHeight(driver, Common.homePageSixAndOverBlock);	       
		       helper.fileWriterPrinter("\nMOBILE PORTRAIT SCREEN AGE 5 HEIGHT (PIXELS) = " + Integer.valueOf(df.format(ageFiveHeight)));
		       helper.fileWriterPrinter(  "MOBILE PORTRAIT SCREEN AGE 6 BLOCK HEIGHT (PIXELS) = " + Integer.valueOf(df.format(ageSixHeight)));	       
		       ageFiveHeightPercents = (double) ( (ageFiveHeight*100/(ageFiveHeight + ageSixHeight))*1000/1000.000   );
		       ageSixHeightPercents  = (double) (  (ageSixHeight*100/(ageFiveHeight + ageSixHeight))*1000/1000.000   );	       
		       helper.fileWriterPrinter("MOBILE PORTRAIT SCREEN  - COVERAGE BY AGE 5 = " + df.format(ageFiveHeightPercents) + " %");
		       helper.fileWriterPrinter("MOBILE PORTRAIT SCREEN  - COVERAGE BY AGE 6 BLOCK = " + df.format(ageSixHeightPercents ) + " %");	       
		       acceptanceCriteria = (ageFiveHeightPercents >= 49) && (ageFiveHeightPercents <= 51);
		       helper.assertBooleanTrue(driver, new Exception().getStackTrace()[0], acceptanceCriteria);	       
		       acceptanceCriteria = (ageSixHeightPercents >= 49) && (ageSixHeightPercents <= 51);
		       helper.assertBooleanTrue(driver, new Exception().getStackTrace()[0], acceptanceCriteria);
		       
	    } catch(Exception e) { helper.getExceptionDescriptive(e, new Exception().getStackTrace()[0], driver); }
	}
	
	/**
	 * Test responsive Desktop and Tablet and Mobile Home page TVOKids logo is not a hyperlink
	 * <p>Date Created: 2016-07-26</p>
	 * <p>Date Modified: 2016-07-26</p>
	 * <p>Original Version: V1</p>
	 * <p>Modified Version: </p>
	 * <p>Xpath: 1</p>
	 * <p>Test Cases: 34960 3267</p>
	 */
	@Test(retryAnalyzer = RetryOnFail.class, /*dataProvider = "numberOfTimesToRun", dataProviderClass = DataProviderForTest.class,*/
    groups = {"TC-34960","US-3267"}, priority = 12)
	public void testResponsiveDesktopAndMobileDevicesHomePageAgeLogoIsNotHyperlink() throws IOException, IllegalArgumentException, MalformedURLException {
	       try{
	    	   // INITIALISATION:
	           helper.printXmlPath(new RuntimeException().getStackTrace()[0]);
	           driver = helper.getServerName(driver);
	           
	           // NAVIGATE TO HOME PAGE:
		       helper.getUrlWaitUntil(driver, 10, Common.homeURL);
		       
		       // DESKTOP:
		       helper.fileWriterPrinter("\n" + "DESKTOP:");
		       driver.manage().window().maximize();
		       // ASSERT DESKTOP SCREEN HOME PAGE LOGO IS NOT CLICKABLE:
		       helper.clickLinkAndCheckURL(driver, Common.homePageLogo, Common.homeURL, false, false);
		       
		       // TABLET LANDSCAPE:
		       helper.switchWindowSizeToTabletLandscape(driver);
		       // ASSERT TABLET LANDSCAPE SCREEN HOME PAGE LOGO IS NOT CLICKABLE:
		       helper.clickLinkAndCheckURL(driver, Common.homePageLogo, Common.homeURL, false, false);
		       
		       // TABLET PORTRAIT:
		       helper.switchWindowSizeToTabletPortrait(driver);
		       // ASSERT TABLET PORTRAIT SCREEN HOME PAGE LOGO IS NOT CLICKABLE:
		       helper.clickLinkAndCheckURL(driver, Common.homePageLogo, Common.homeURL, false, false);
		       
		       // MOBILE LANDSCAPE:
		       helper.switchWindowSizeToMobileLandscape(driver);
		       // ASSERT MOBILE LANDSCAPE SCREEN HOME PAGE LOGO IS NOT CLICKABLE:
		       helper.clickLinkAndCheckURL(driver, Common.homePageLogo, Common.homeURL, false, false);
		       
		       // MOBILE PORTRAIT:
		       helper.switchWindowSizeToMobilePortrait(driver);
		       // ASSERT MOBILE PORTRAIT SCREEN HOME PAGE LOGO IS NOT CLICKABLE:
		       helper.clickLinkAndCheckURL(driver, Common.homePageLogo, Common.homeURL, false, false);
		       
	    } catch(Exception e) { helper.getExceptionDescriptive(e, new Exception().getStackTrace()[0], driver); }
	}
	
	/**
	 * Test responsive Desktop and Tablet and Mobile Home page both ages group blocks are hyperlinked to the corresponding landing page
	 * <p>Date Created: 2016-07-26</p>
	 * <p>Date Modified: 2016-07-26</p>
	 * <p>Original Version: V1</p>
	 * <p>Modified Version: </p>
	 * <p>Xpath: 1</p>
	 * <p>Test Cases: 34960 3267</p>
	 */
	@Test(retryAnalyzer = RetryOnFail.class, /*dataProvider = "numberOfTimesToRun", dataProviderClass = DataProviderForTest.class,*/
    groups = {"TC-34960","US-3267"}, priority = 13)
	public void testResponsiveDesktopAndMobileDevicesHomePageClickAgeBlocksOpensCorrectLandingPage() throws IOException, IllegalArgumentException, MalformedURLException {
	       try{
	    	   // INITIALISATION:
	           helper.printXmlPath(new RuntimeException().getStackTrace()[0]);
	           driver = helper.getServerName(driver);
	           
	           // NAVIGATE TO HOME PAGE:
		       helper.getUrlWaitUntil(driver, 10, Common.homeURL);
		       
		       // DESKTOP:
		       helper.fileWriterPrinter("\n" + "DESKTOP:");
		       driver.manage().window().maximize();
		       // ASSERT DESKTOP SCREEN CLICK ON AGE 5 BLOCK:
		       helper.clickLinkAndCheckURL(driver, Common.homePageFiveAndUnderBlock, Common.fiveAndUnderURL, true, false);
		       // ASSERT DESKTOP SCREEN CLICK ON AGE 6 BLOCK:
		       helper.clickLinkAndCheckURL(driver, Common.homePageSixAndOverBlock, Common.sixAndOverURL, true, false);
		       
		       // TABLET LANDSCAPE:
		       helper.switchWindowSizeToTabletLandscape(driver);
		       // ASSERT TABLET LANDSCAPE SCREEN CLICK ON AGE 5 BLOCK:
		       helper.clickLinkAndCheckURL(driver, Common.homePageFiveAndUnderBlock, Common.fiveAndUnderURL, true, false);
		       // ASSERT TABLET LANDSCAPE SCREEN CLICK ON AGE 6 BLOCK:
		       helper.clickLinkAndCheckURL(driver, Common.homePageSixAndOverBlock, Common.sixAndOverURL, true, false);
		       
		       // TABLET PORTRAIT:
		       helper.switchWindowSizeToTabletPortrait(driver);
		       // ASSERT TABLET PORTRAIT SCREEN CLICK ON AGE 5 BLOCK:
		       helper.clickLinkAndCheckURL(driver, Common.homePageFiveAndUnderBlock, Common.fiveAndUnderURL, true, false);
		       // ASSERT TABLET PORTRAIT SCREEN CLICK ON AGE 6 BLOCK:
		       helper.clickLinkAndCheckURL(driver, Common.homePageSixAndOverBlock, Common.sixAndOverURL, true, false);
		       
		       // MOBILE LANDSCAPE:
		       helper.switchWindowSizeToMobileLandscape(driver);
		       // ASSERT MOBILE LANDSCAPE SCREEN CLICK ON AGE 5 BLOCK:
		       helper.clickLinkAndCheckURL(driver, Common.homePageFiveAndUnderBlock, Common.fiveAndUnderURL, true, false);
		       // ASSERT MOBILE LANDSCAPE SCREEN CLICK ON AGE 6 BLOCK:
		       helper.clickLinkAndCheckURL(driver, Common.homePageSixAndOverBlock, Common.sixAndOverURL, true, false);
		       
		       // MOBILE PORTRAIT:
		       helper.switchWindowSizeToMobilePortrait(driver);
		       // ASSERT MOBILE PORTRAIT SCREEN CLICK ON AGE 5 BLOCK:
		       helper.clickLinkAndCheckURL(driver, Common.homePageFiveAndUnderBlock, Common.fiveAndUnderURL, true, false);
		       // ASSERT MOBILE PORTRAIT SCREEN CLICK ON AGE 6 BLOCK:
		       helper.clickLinkAndCheckURL(driver, Common.homePageSixAndOverBlock, Common.sixAndOverURL, true, false);
		       
	    } catch(Exception e) { helper.getExceptionDescriptive(e, new Exception().getStackTrace()[0], driver); }
	}
	
	/**
	 * Test interactivity Home Page age block's background colour changes if mouse hover
	 * <p>Date Created: 2016-07-29</p>
	 * <p>Date Modified: 2016-07-29</p>
	 * <p>Original Version: V1</p>
	 * <p>Modified Version: </p>
	 * <p>Xpath: 1</p>
	 * <p>Test Cases: 34969 3316</p>
	 */
	@Test(retryAnalyzer = RetryOnFail.class, /*dataProvider = "numberOfTimesToRun", dataProviderClass = DataProviderForTest.class,*/
    groups = {"TC-34969","US-3316"}, priority = 14)
	public void testInteractivityHomePageAgeBlockMouseHoverColourChanges() throws IOException, IllegalArgumentException, MalformedURLException {
	       try{
	    	   // INITIALISATION:
	           helper.printXmlPath(new RuntimeException().getStackTrace()[0]);
	           driver = helper.getServerName(driver);
	           
	           // DECLARATION:
	           String colorDefaultBackground, colorDefaultTitle, colorHoverBackground, colorHoverTitleText;
	           
	           // NAVIGATE TO HOME PAGE:
		       helper.getUrlWaitUntil(driver, 10, Common.homeURL);
		       
		       // AGE 5 AND UNDER:
		       helper.fileWriterPrinter("\n" + "AGE 5 AND UNDER TEST:");
		       
		       // AGE 5 AND UNDER DEFAULT COLOR:
		       colorDefaultBackground = helper.getColorHEX(driver, Common.homePageFiveAndUnderBlock, "background", true, "AGE 5 AND UNDER DEFAULT BACKGROUND COLOR");
		       colorDefaultTitle = helper.getColorHEX(driver, Common.homePageFiveAndUnderTitle, "color", true, "AGE 5 AND UNDER DEFAULT TITLE TEXT COLOR"); 
		       
		       // AGE 5 AND UNDER MOUSE HOVER ACTION:
		       helper.hoverElement(driver, Common.homePageFiveAndUnderBlock);
		       Thread.sleep(1000);
		       
		       // AGE 5 AND UNDER AFTER HOVER COLOR:
		       colorHoverBackground = helper.getColorHEX(driver, Common.homePageFiveAndUnderBlock, "background", true, "AGE 5 AND UNDER AFTER HOVER BACKGROUND COLOR");
		       colorHoverTitleText = helper.getColorHEX(driver, Common.homePageFiveAndUnderTitle, "color", true, "AGE 5 AND UNDER AFTER HOVER TITLE TEXT COLOR");
		       
		       // ASSERT AGE 5 AND UNDER:
		       helper.assertEquals(driver, new Exception().getStackTrace()[0], colorDefaultBackground, colorHoverTitleText);
		       helper.assertEquals(driver, new Exception().getStackTrace()[0], colorDefaultTitle, colorHoverBackground);
		       
		       // AGE 6 AND OVER:
		       helper.fileWriterPrinter("\n" + "AGE 6 AND OVER TEST:");
		       
		       // AGE 6 AND OVER DEFAULT COLOR:
		       colorDefaultBackground = helper.getColorHEX(driver, Common.homePageSixAndOverBlock, "background", true, "AGE 6 AND OVER DEFAULT BACKGROUND COLOR");
		       colorDefaultTitle = helper.getColorHEX(driver, Common.homePageSixAndOverTitle, "color", true, "AGE 6 AND OVER DEFAULT TITLE TEXT COLOR"); 
		       
		       // AGE 6 AND OVER MOUSE HOVER ACTION:
		       helper.hoverElement(driver, Common.homePageSixAndOverBlock);
		       Thread.sleep(1000);
		       
		       // AGE 6 AND OVER AFTER HOVER COLOR:
		       colorHoverBackground = helper.getColorHEX(driver, Common.homePageSixAndOverBlock, "background", true, "AGE 6 AND OVER AFTER HOVER BACKGROUND COLOR");
		       colorHoverTitleText = helper.getColorHEX(driver, Common.homePageSixAndOverTitle, "color", true, "AGE 6 AND OVER AFTER HOVER TITLE TEXT COLOR");
		       
		       // ASSERT 6 AND OVER:
		       helper.assertEquals(driver, new Exception().getStackTrace()[0], colorDefaultBackground, colorHoverTitleText);
		       helper.assertEquals(driver, new Exception().getStackTrace()[0], colorDefaultTitle, colorHoverBackground);
		       
	        } catch(Exception e) { helper.getExceptionDescriptive(e, new Exception().getStackTrace()[0], driver); }
	    }
	
}
