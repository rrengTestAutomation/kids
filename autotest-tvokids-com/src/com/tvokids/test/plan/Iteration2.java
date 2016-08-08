package com.tvokids.test.plan;


import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DecimalFormat;

import com.tvokids.locator.Drupal;
import com.tvokids.locator.Common;
import com.tvokids.test.helper.UtilitiesTestHelper;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.*;
/*
import java.awt.Robot;
import java.io.File;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
*/

//@SuppressWarnings("unused")
public class Iteration2 {
	static WebDriver driver;
	UtilitiesTestHelper helper = new UtilitiesTestHelper();
	
	/**
	 * Test Custom Brand Meta-Data Attributes - elements exist
	 * <p>Date Created: 2016-07-06</p>
	 * <p>Date Modified: 2016-07-06<p>
	 * <p>Original Version: V1</p>
	 * <p>Modified Version: </p>
	 * <p>Xpath: 1</p>
	 * <p>Test Cases: 35131</p>
	 */
	@Test(groups = {"TC-35131"}, priority = 1)
    public void testCustomBrandFieldsExist() throws IOException, IllegalArgumentException, MalformedURLException {
       try{
    	   // INITIALISATION:
           helper.printXmlPath(new RuntimeException().getStackTrace()[0]);
           driver = helper.getServerName(driver);                
           
           // LOGIN TO DRUPAL AS A CONTENT EDITOR:
           helper.logIn(driver,"content_editor","changeme");
           
           // NAVIGATE TO A NEW CUSTOM BRAND PAGE:
           helper.getUrlWaitUntil(driver, 10, Drupal.customBrand);
           
     	   // ASSERT EXISTANCE:
           helper.assertWebElementExist(driver, Drupal.title, new RuntimeException().getStackTrace()[0]);
           helper.assertWebElementExist(driver, Drupal.brandPageDescriptionID, new RuntimeException().getStackTrace()[0]);
           helper.assertWebElementExist(driver, Drupal.ageGroup5, new RuntimeException().getStackTrace()[0]);
           helper.assertWebElementExist(driver, Drupal.ageGroup6, new RuntimeException().getStackTrace()[0]);
           helper.assertWebElementExist(driver, Drupal.keywords, new RuntimeException().getStackTrace()[0]);
    	  
           } catch(Exception e) { UtilitiesTestHelper.getExceptionDescriptive(e, new Exception().getStackTrace()[0], driver); }
   }
	
	/**
	 * Test attempt to create a Custom Brand without a title is rejected
	 * <p>Date Created: 2016-07-20</p>
	 * <p>Date Modified: 2016-07-20<p>
	 * <p>Original Version: V1</p>
	 * <p>Modified Version: </p>
	 * <p>Xpath: 1</p>
	 * <p>Test Cases: 35131</p>
	 */
	@Test(groups = {"TC-35131","TC-35153"}, priority = 2)
    public void testCustomBrandTitleIsMandatory() throws IOException, IllegalArgumentException, MalformedURLException {
       try{
    	   // INITIALISATION:
           helper.printXmlPath(new RuntimeException().getStackTrace()[0]);
           driver = helper.getServerName(driver);

           // DECLARATION:
           String expectedURL, title, description;
           
           // LOGIN TO DRUPAL AS A CONTENT EDITOR:
           helper.logIn(driver,"content_editor","changeme");
           
           // NAVIGATE TO A NEW CUSTOM BRAND PAGE:
           helper.getUrlWaitUntil(driver, 10, Drupal.customBrand);
           driver.manage().window().maximize();
           
           // PREPARE CONTENT ENTRY:
           title = "";
           description = helper.randomEnglishText(helper.randomInt(125, (Drupal.descriptionMaxCharsNumber - 1)));
           
           // CREATE CONTENT WITH NO DESCRIPTION:
           helper.createCustomBrand(driver, title, description, true, true, true, new Exception().getStackTrace()[0]);
          
           // ASSERT CONTENT URL DID NOT CHANGE:
           expectedURL = Drupal.customBrand;
           helper.checkCurrentURL(driver, new Exception().getStackTrace()[0], expectedURL);
           
           // ASSERT ERROR MESSAGE APPEARS:
           driver.findElement(By.id(Drupal.submit)).click();
           helper.assertWebElementExist(driver, new Exception().getStackTrace()[0], Drupal.errorTitle);
           
           } catch(Exception e) { UtilitiesTestHelper.getExceptionDescriptive(e, new Exception().getStackTrace()[0], driver); }
       }
	
	/**
	 * Test Custom Brand Meta-Data Attributes - Title field content is limited
	 * <p>Date Created: 2016-07-06</p>
	 * <p>Date Modified: 2016-07-22<p>
	 * <p>Original Version: V1</p>
	 * <p>Modified Version: V3</p>
	 * <p>Xpath: 3</p>
	 * <p>Test Cases: 35131</p>
	 */
	@Test(groups = {"TC-35131","TC-35153","BUG-35502","BUG-528","BUG-529","OPEN"}, enabled = true, priority = 3)
    public void testCustomBrandTitleFieldContentLimit() throws IOException, IllegalArgumentException, MalformedURLException {
       try{
    	   // INITIALISATION:
           helper.printXmlPath(new RuntimeException().getStackTrace()[0]);
           driver = helper.getServerName(driver);

           // DECLARATION:
           int actual, expected, number;
           String actualText, expectedText, expectedURLend, title, xpath;
           
           // LOGIN TO DRUPAL AS A CONTENT EDITOR:
           helper.logIn(driver,"content_editor","changeme");
           
           // CLEAN-UP:
           helper.deleteAllContent(driver, "Custom Brand", "14", "content_editor", new RuntimeException().getStackTrace()[0]);
           
           // NAVIGATE TO A NEW CUSTOM BRAND PAGE:
           helper.getUrlWaitUntil(driver, 10, Drupal.customBrand);
           driver.manage().window().maximize();
           
     	   // ASSERT EMPTY TITLE FIELD:
           driver.findElement(By.id(Drupal.title)).clear();
           actual = Integer.valueOf( driver.findElement(By.xpath(Drupal.titleRemainCharsNumber)).getText() );
           expected = Drupal.titleMaxCharsNumber;
           helper.assertEquals(driver, new Exception().getStackTrace()[0], actual, expected);
           
           // ASSERT TITLE FIELD CAPACITY COUNTER:          
           number = helper.randomInt(1, (Drupal.titleMaxCharsNumber - 1));
           title = helper.randomEnglishText(number);
           driver.findElement(By.id(Drupal.title)).clear();
           driver.findElement(By.id(Drupal.title)).sendKeys(title);
           actual = Integer.valueOf( driver.findElement(By.xpath(Drupal.titleRemainCharsNumber)).getText() );
           expected = Drupal.titleMaxCharsNumber - number;
           helper.assertEquals(driver, new Exception().getStackTrace()[0], actual, expected);
           
           // ASSERT TITLE FIELD MAXIMUM CAPACITY:           
           title = helper.randomEnglishText(Drupal.titleMaxCharsNumber);
           driver.findElement(By.id(Drupal.title)).clear();
           driver.findElement(By.id(Drupal.title)).sendKeys(title);
           actual = Integer.valueOf( driver.findElement(By.xpath(Drupal.titleRemainCharsNumber)).getText() );
           expected = 0;
           helper.assertEquals(driver, new Exception().getStackTrace()[0], actual, expected);
           
           // ASSERT TITLE FIELD OVERLOAD ENTRY:
           driver.findElement(By.id(Drupal.title)).clear();
           
           // CREATE TITLE FOR CONTENT:
//            text = helper.randomEnglishText(DrupalAddContentLocators.titleMaxCharsNumber + 10);
//            text = helper.randomWord(DrupalAddContentLocators.titleMaxCharsNumber + 10);
           long fingerprint = System.currentTimeMillis();
           title = String.valueOf(fingerprint) + " " +  helper.randomText(Drupal.titleMaxCharsNumber + 10);
             
           driver.findElement(By.id(Drupal.title)).sendKeys(title);
           actual = Integer.valueOf( driver.findElement(By.xpath(Drupal.titleRemainCharsNumber)).getText() );
           expected = 0;
           helper.assertEquals(driver, new Exception().getStackTrace()[0], actual, expected);

           // CREATE CONTENT:
//            Robot robot = new Robot();
//            helper.createCustomBrand(driver, text, true, true, robot, fingerprint);
           helper.createCustomBrand(driver, title, true, true, fingerprint, new Exception().getStackTrace()[0]);
          
           // ASSERT CONTENT URL USES A LIMITED TITLE:
           expectedText = title.substring(0, Drupal.titleMaxCharsNumber);
           expectedURLend = "/" + helper.reFormatStringForURL(title, Drupal.titleMaxCharsNumber);
           helper.checkCurrentURLendsWith(driver, new Exception().getStackTrace()[0], expectedURLend);
           
           // ASSERT TITLE FIELD OVERLOAD CONTENT IS LIMITED:
           actualText = driver.findElement(By.xpath(Common.title)).getText();
           helper.assertEquals(driver, new Exception().getStackTrace()[0], actualText, expectedText);
           
           // ASSERT TITLE FIELD OVERLOAD ON CONTENT ADMIN IS LIMITED:
           helper.getUrlWaitUntil(driver, 15, Common.adminContentURL);          
		   WebElement dropwDownListBox = driver.findElement(By.id("edit-type"));
		   Select clickThis = new Select(dropwDownListBox);
		   Thread.sleep(2000);
		   clickThis.selectByVisibleText("Custom Brand");
		   Thread.sleep(2000);
		   driver.findElement(By.id("edit-author")).clear();
		   driver.findElement(By.id("edit-author")).sendKeys("content_editor");
		   int size = helper.waitUntilElementList(driver, 5, Common.autoComplete, "auto-complete").size();
           if (size == 1) { try { driver.findElement(By.xpath(Common.autoComplete)).click(); } catch(Exception e) { } }
           helper.waitUntilElementInvisibility(driver, 15, Common.autoComplete, "auto-complete", new Exception().getStackTrace()[0]);
		   driver.findElement(By.id("edit-submit-admin-views-node")).click();
		   helper.waitUntilElementInvisibility(driver, 30, Common.ajaxThrobber, "Throbber", new Exception().getStackTrace()[0]);          
		   xpath = Drupal.adminContentRowFirst + Drupal.adminContentFieldTitles;
		   actualText = driver.findElement(By.xpath(xpath)).getText();
           helper.assertEquals(driver, new Exception().getStackTrace()[0], actualText, expectedText);  
           
           } catch(Exception e) { UtilitiesTestHelper.getExceptionDescriptive(e, new Exception().getStackTrace()[0], driver); }
       }

	/**
	 * Test attempt to create a Custom Brand without a description is rejected
	 * <p>Date Created: 2016-07-20</p>
	 * <p>Date Modified: 2016-07-20<p>
	 * <p>Original Version: V1</p>
	 * <p>Modified Version: </p>
	 * <p>Xpath: 1</p>
	 * <p>Test Cases: 35153</p>
	 */
	@Test(groups = {"TC-35131","TC-35153"}, priority = 4)
    public void testCustomBrandDescriptionIsMandatory() throws IOException, IllegalArgumentException, MalformedURLException {
       try{
    	   // INITIALISATION:
           helper.printXmlPath(new RuntimeException().getStackTrace()[0]);
           driver = helper.getServerName(driver);

           // DECLARATION:
           String expectedURL, title;
           
           // LOGIN TO DRUPAL AS A CONTENT EDITOR:
           helper.logIn(driver,"content_editor","changeme");
           
           // NAVIGATE TO A NEW CUSTOM BRAND PAGE:
           helper.getUrlWaitUntil(driver, 10, Drupal.customBrand);
           driver.manage().window().maximize();
           
           // CREATE TITLE FOR CONTENT:
           long fingerprint = System.currentTimeMillis();
           title = String.valueOf(fingerprint) + " " +  helper.randomText(Drupal.titleMaxCharsNumber + 10);

           // CREATE CONTENT WITH NO DESCRIPTION:
           helper.createCustomBrand(driver, title, "", true, true, true, new Exception().getStackTrace()[0]);
          
           // ASSERT CONTENT URL DID NOT CHANGE:
           expectedURL = Drupal.customBrand;
           helper.checkCurrentURL(driver, new Exception().getStackTrace()[0], expectedURL);
           
           // ASSERT ERROR MESSAGE APPEARS:
           driver.findElement(By.id(Drupal.submit)).click();
           helper.assertWebElementExist(driver, new Exception().getStackTrace()[0], Drupal.errorDescription);
           
           } catch(Exception e) { UtilitiesTestHelper.getExceptionDescriptive(e, new Exception().getStackTrace()[0], driver); }
       }
	
	/**
	 * Test attempt to create a Custom Brand Page Description that is longer than 135 characters is rejected
	 * <p>Date Created: 2016-07-20</p>
	 * <p>Date Modified: 2016-07-20<p>
	 * <p>Original Version: V1</p>
	 * <p>Modified Version: </p>
	 * <p>Xpath: 1</p>
	 * <p>Test Cases: 35153</p>
	 */
	@SuppressWarnings("static-access")
	@Test(groups = {"TC-35131","TC-35153"}, priority = 5)
    public void testCustomBrandDescriptionIsLimited() throws IOException, IllegalArgumentException, MalformedURLException {
	       try{
	    	   // INITIALISATION:
	           helper.printXmlPath(new RuntimeException().getStackTrace()[0]);
	           driver = helper.getServerName(driver);
	           
	           // LOGIN TO DRUPAL AS A CONTENT EDITOR:
	           helper.logIn(driver,"content_editor","changeme");
	           
	           // NAVIGATE TO A NEW CUSTOM BRAND PAGE:
	           helper.getUrlWaitUntil(driver, 10, Drupal.customBrand);
	           
	           // DECLARATION:
	           int actual, expected, number;
	           String description = "";
	    
	           // ASSERT EMPTY DESCRIPTION FIELD:
	           driver.findElement(By.id(Drupal.brandPageDescriptionID)).clear();
	           helper.fileWriterPrinter();
	           helper.fileWriterPrinter("DESCRIPTION: " + description);
	           helper.fileWriterPrinter("     LENGTH: " + description.length());
	           actual = Integer.valueOf( driver.findElement(By.cssSelector(Drupal.brandPageDescriptionCounterCSS)).getText() );
	           expected = Drupal.descriptionMaxCharsNumber;
	           helper.assertEquals(driver, new Exception().getStackTrace()[0], actual, expected);
	           
	           // ASSERT DESCRIPTION FIELD CAPACITY COUNTER:	           
	           number = helper.randomInt(1, (Drupal.descriptionMaxCharsNumber - 1));
	           description = helper.randomEnglishText(number);
	           helper.fileWriterPrinter("DESCRIPTION: " + description);
	           helper.fileWriterPrinter("     LENGTH: " + description.length());
	           driver.findElement(By.id(Drupal.brandPageDescriptionID)).clear();
	           driver.findElement(By.id(Drupal.brandPageDescriptionID)).sendKeys(description);
	           actual = Integer.valueOf( driver.findElement(By.cssSelector(Drupal.brandPageDescriptionCounterCSS)).getText() );
	           expected = Drupal.descriptionMaxCharsNumber - number;
	           helper.assertEquals(driver, new Exception().getStackTrace()[0], actual, expected);
	           
	           // ASSERT DESCRIPTION FIELD MAXIMUM CAPACITY:           
	           description = helper.randomEnglishText(Drupal.descriptionMaxCharsNumber);
	           helper.fileWriterPrinter("DESCRIPTION: " + description);
	           helper.fileWriterPrinter("     LENGTH: " + description.length());
	           driver.findElement(By.id(Drupal.brandPageDescriptionID)).clear();
	           driver.findElement(By.id(Drupal.brandPageDescriptionID)).sendKeys(description);
	           actual = Integer.valueOf( driver.findElement(By.cssSelector(Drupal.brandPageDescriptionCounterCSS)).getText() );
	           expected = 0;
	           helper.assertEquals(driver, new Exception().getStackTrace()[0], actual, expected);
	           
	           // ASSERT DESCRIPTION FIELD OVERLOAD ENTRY:
	           description = helper.randomEnglishText(Drupal.descriptionMaxCharsNumber + 10);
	           helper.fileWriterPrinter("DESCRIPTION: " + description);
	           helper.fileWriterPrinter("     LENGTH: " + description.length());
	           driver.findElement(By.id(Drupal.brandPageDescriptionID)).clear();
	           driver.findElement(By.id(Drupal.brandPageDescriptionID)).sendKeys(description);
	           actual = Integer.valueOf( driver.findElement(By.cssSelector(Drupal.brandPageDescriptionCounterCSS)).getText() );
	           expected = 0;
	           helper.assertEquals(driver, new Exception().getStackTrace()[0], actual, expected);

	           } catch(Exception e) { UtilitiesTestHelper.getExceptionDescriptive(e, new Exception().getStackTrace()[0], driver); }
	       }
	
	// Navigate to the appropriate page (on both 5 and Under & 6 and Over) and locate your newly created Custom Brand.
	/**
	 * Test create a Custom Brand Page on both 5 and Under & 6 and Over is located on the appropriate front end age page and navigatation link is correct
	 * <p>Date Created: 2016-07-22</p>
	 * <p>Date Modified: 2016-07-22<p>
	 * <p>Original Version: V1</p>
	 * <p>Modified Version: </p>
	 * <p>Xpath: 1</p>
	 * <p>Test Cases: 35153</p>
	 */
	@SuppressWarnings("static-access")
	@Test(groups = {"TC-35153"}, priority = 6)
    public void testCustomBrandBothAgesFrontEndLocationAndLinkAreCorrect() throws IOException, IllegalArgumentException, MalformedURLException {
	       try{
	    	   // INITIALISATION:
	           helper.printXmlPath(new RuntimeException().getStackTrace()[0]);
	           driver = helper.getServerName(driver);
	           
	           // LOGIN TO DRUPAL AS A CONTENT EDITOR:
	           helper.logIn(driver,"content_editor","changeme");
	           
	           // CLEAN-UP:
//	           helper.deleteAllContent(driver, "", "", "", new RuntimeException().getStackTrace()[0]);
//	           helper.deleteAllContent(driver, "Custom Brand", "", "", new RuntimeException().getStackTrace()[0]);
//	           helper.deleteAllContent(driver, "Custom Brand", "", "content_editor", new RuntimeException().getStackTrace()[0]);
	           helper.deleteAllContent(driver, "Custom Brand", "14", "content_editor", new RuntimeException().getStackTrace()[0]);
	           
	           // NAVIGATE TO A NEW CUSTOM BRAND PAGE:
	           helper.getUrlWaitUntil(driver, 10, Drupal.customBrand);
	           
	           // DECLARATION:
	           String title, titleURL, description, xpath, expectedURL;
	           
	           // CREATE TITLE FOR CONTENT:
	           long fingerprint = System.currentTimeMillis();
	           title = String.valueOf(fingerprint) + " " +  helper.randomWord(Drupal.titleMaxCharsNumber);
	           titleURL = helper.reFormatStringForURL(title, Drupal.titleMaxCharsNumber);
	           
	           // CREATE DESCRIPTION FOR CONTENT:
	           description = helper.randomEnglishText(helper.randomInt((Drupal.descriptionMaxCharsNumber - 30), Drupal.descriptionMaxCharsNumber));
	           
	           // CREATE CONTENT WITH BOTH AGES SELECTED:
	           helper.createCustomBrand(driver, title, description, true, true, true, new Exception().getStackTrace()[0]);
	           
	           // LINK GENERIC XPATH
	           xpath = "//a[contains(@href,'" + titleURL +  Common.XpathContainsEnd;
	           helper.fileWriterPrinter("\n" + "LINK GENERIC XPATH = " + xpath + "\n");
	           
	           // NAVIGATE TO "AGE 5 AND UNDER":
	           helper.getUrlWaitUntil(driver, 10, Common.fiveAndUnderURL);
//	           xpath = Common.fiveAndUnderLinkBase + titleURL + Common.XpathEqualsEnd;
	           helper.assertWebElementExist(driver, new Exception().getStackTrace()[0], xpath);
	           
	           // ASSERT LINK IS CORRECT:
	           expectedURL = Common.fiveAndUnderURL + "/" + titleURL;
	           helper.moveToElement(driver, xpath);
	           helper.clickLinkAndCheckURL(driver, new Exception().getStackTrace()[0], xpath, expectedURL, false, true);
	           
	           // NAVIGATE TO "AGE 6 AND OVER":
	           helper.getUrlWaitUntil(driver, 10, Common.sixAndOverURL);
//	           xpath = Common.sixAndOverLinkBase + titleURL + Common.XpathEqualsEnd;
	           helper.assertWebElementExist(driver, new Exception().getStackTrace()[0], xpath);
	           
	           // ASSERT LINK IS CORRECT:
	           expectedURL = Common.sixAndOverURL + "/" + titleURL;
	           helper.moveToElement(driver, xpath);
	           helper.clickLinkAndCheckURL(driver, new Exception().getStackTrace()[0], xpath, expectedURL, false, true);
	           
	           } catch(Exception e) { UtilitiesTestHelper.getExceptionDescriptive(e, new Exception().getStackTrace()[0], driver); }
	       }
	
	/**
	 * Test Home Page elements exist
	 * <p>Date Created: 2016-07-25</p>
	 * <p>Date Modified: 2016-07-25<p>
	 * <p>Original Version: V1</p>
	 * <p>Modified Version: </p>
	 * <p>Xpath: 1</p>
	 * <p>Test Cases: 34908</p>
	 */
	@Test(groups = {"TC-34908"}, priority = 7)
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
		          
           } catch(Exception e) { UtilitiesTestHelper.getExceptionDescriptive(e, new Exception().getStackTrace()[0], driver); }
       }
	
	/**
	 * Test Home page by click on the specific age image takes under to corresponding age Landing page
	 * <p>Date Created: 2016-07-25</p>
	 * <p>Date Modified: 2016-07-25<p>
	 * <p>Original Version: V1</p>
	 * <p>Modified Version: </p>
	 * <p>Xpath: 1</p>
	 * <p>Test Cases: 34908</p>
	 */
	@Test(groups = {"TC-34908"}, priority = 8)
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
		          
           } catch(Exception e) { UtilitiesTestHelper.getExceptionDescriptive(e, new Exception().getStackTrace()[0], driver); }
       }
	
	/**
	 * Test responsive Desktop and Tablet and Mobile Home page Age Titles position is compatible to the platform being used
	 * <p>Date Created: 2016-07-26</p>
	 * <p>Date Modified: 2016-07-26<p>
	 * <p>Original Version: V1</p>
	 * <p>Modified Version: </p>
	 * <p>Xpath: 1</p>
	 * <p>Test Cases: 34960</p>
	 */
	@SuppressWarnings("static-access")
	@Test(groups = {"TC-34960"}, priority = 9)
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
		       
        } catch(Exception e) { UtilitiesTestHelper.getExceptionDescriptive(e, new Exception().getStackTrace()[0], driver); }
    }
	
	/**
	 * Test responsive Desktop and Tablet and Mobile Home page Age Images position is compatible to the platform being used
	 * <p>Date Created: 2016-07-26</p>
	 * <p>Date Modified: 2016-07-26<p>
	 * <p>Original Version: V1</p>
	 * <p>Modified Version: </p>
	 * <p>Xpath: 1</p>
	 * <p>Test Cases: 34960</p>
	 */
	@SuppressWarnings("static-access")
	@Test(groups = {"TC-34960"}, priority = 10)
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
	     
        } catch(Exception e) { UtilitiesTestHelper.getExceptionDescriptive(e, new Exception().getStackTrace()[0], driver); }
    }
	
	/**
	 * Test responsive Desktop and Tablet and Mobile Home page Age Blocks sections are 50% of the browser width
	 * <p>Date Created: 2016-07-26</p>
	 * <p>Date Modified: 2016-07-26<p>
	 * <p>Original Version: V1</p>
	 * <p>Modified Version: </p>
	 * <p>Xpath: 1</p>
	 * <p>Test Cases: 34960</p>
	 */
	@SuppressWarnings("static-access")
	@Test(groups = {"TC-34960"}, priority = 11)
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
		       
        } catch(Exception e) { UtilitiesTestHelper.getExceptionDescriptive(e, new Exception().getStackTrace()[0], driver); }
    }
	
	/**
	 * Test responsive Desktop and Tablet and Mobile Home page TVOKids logo is not a hyperlink
	 * <p>Date Created: 2016-07-26</p>
	 * <p>Date Modified: 2016-07-26<p>
	 * <p>Original Version: V1</p>
	 * <p>Modified Version: </p>
	 * <p>Xpath: 1</p>
	 * <p>Test Cases: 34960</p>
	 */
	@SuppressWarnings("static-access")
	@Test(groups = {"TC-34960"}, priority = 12)
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
		       
        } catch(Exception e) { UtilitiesTestHelper.getExceptionDescriptive(e, new Exception().getStackTrace()[0], driver); }
    }
	
	/**
	 * Test responsive Desktop and Tablet and Mobile Home page both ages group blocks are hyperlinked to the corresponding landing page
	 * <p>Date Created: 2016-07-26</p>
	 * <p>Date Modified: 2016-07-26<p>
	 * <p>Original Version: V1</p>
	 * <p>Modified Version: </p>
	 * <p>Xpath: 1</p>
	 * <p>Test Cases: 34960</p>
	 */
	@SuppressWarnings("static-access")
	@Test(groups = {"TC-34960"}, priority = 13)
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
		       
        } catch(Exception e) { UtilitiesTestHelper.getExceptionDescriptive(e, new Exception().getStackTrace()[0], driver); }
    }
		
	/**
	 * Test interactivity Home Page age block's background colour changes if mouse hover
	 * <p>Date Created: 2016-07-29</p>
	 * <p>Date Modified: 2016-07-29<p>
	 * <p>Original Version: V1</p>
	 * <p>Modified Version: </p>
	 * <p>Xpath: 1</p>
	 * <p>Test Cases: 34969</p>
	 */
	@SuppressWarnings("static-access")
	@Test(groups = {"TC-34969"}, priority = 14)
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
		       
	        } catch(Exception e) { UtilitiesTestHelper.getExceptionDescriptive(e, new Exception().getStackTrace()[0], driver); }
	    }
	
	/**
	 * Test Desktop - Character banner rotation using Banner Arrows
	 * <p>Date Created: 2016-07-30</p>
	 * <p>Date Modified: 2016-07-30<p>
	 * <p>Original Version: V1</p>
	 * <p>Modified Version: </p>
	 * <p>Xpath: 1</p>
	 * <p>Test Cases: 34144</p>
	 */
	@SuppressWarnings("static-access")
	@Test(groups = {"TC-34144"}, priority = 15)
    public void testDesktopCharacterBannerNavigationButtons() throws IOException, IllegalArgumentException, MalformedURLException {
	       try{
	    	   // INITIALISATION:
	           helper.printXmlPath(new RuntimeException().getStackTrace()[0]);
	           driver = helper.getServerName(driver);
	           
	           // LOGIN TO DRUPAL AS A CONTENT EDITOR:
	           helper.logIn(driver,"content_editor","changeme");
	           
	           // CLEAN-UP:
	           helper.deleteAllContent(driver, "Custom Brand", "14", "content_editor", new RuntimeException().getStackTrace()[0]);
	           
	           // NAVIGATE TO A NEW CUSTOM BRAND PAGE:
	           helper.getUrlWaitUntil(driver, 10, Drupal.customBrand);
	           
	           // DECLARATION:
	           String title, titleURL, description, xpath;
	           int defaultCoordinateX, movedCoordinateX, backCoordinateX, characterWidth;
	           double DefaultCoordinateX, MovedCoordinateX, CharacterWidth, MovementRatio, BackCoordinateX, ReturntRatio;
		       DecimalFormat df = new DecimalFormat("#");
		       
	           // CREATE TITLE FOR CONTENT:
	           long fingerprint = System.currentTimeMillis();
	           title = String.valueOf(fingerprint) + " " +  helper.randomWord(Drupal.titleMaxCharsNumber);
	           titleURL = helper.reFormatStringForURL(title, Drupal.titleMaxCharsNumber);
	           
	           // CREATE DESCRIPTION FOR CONTENT:
	           description = helper.randomEnglishText(helper.randomInt((Drupal.descriptionMaxCharsNumber - 30), Drupal.descriptionMaxCharsNumber));
	           
	           // CREATE CONTENT WITH BOTH AGES SELECTED:
	           helper.createCustomBrand(driver, title, description, true, true, true, new Exception().getStackTrace()[0]);
	           
	           // LINK GENERIC XPATH
	           xpath = "//a[contains(@href,'" + titleURL +  Common.XpathContainsEnd;
	           helper.fileWriterPrinter("\n" + "LINK GENERIC XPATH = " + xpath);
	           
	           // AGE 5 AND UNDER TEST:
	           helper.fileWriterPrinter("\n" + "AGE 5 AND UNDER TEST:");
	           helper.getUrlWaitUntil(driver, 10, Common.fiveAndUnderURL);
	           
	           defaultCoordinateX = driver.findElement(By.xpath(xpath)).getLocation().getX();	           
	           characterWidth = helper.getElementWidth(driver, xpath);	           
		       driver.findElement(By.xpath(Common.charBannerButtonLeft)).click();
		       Thread.sleep(1000);		       
		       movedCoordinateX = driver.findElement(By.xpath(xpath)).getLocation().getX();
		       
	           helper.fileWriterPrinter("\n" + "CHARACTER              WIDTH = " + characterWidth);
	           helper.fileWriterPrinter("CHARACTER DEFAULT  COORDINATE = " + defaultCoordinateX);
		       helper.fileWriterPrinter("CHARACTER   MOVED  COORDINATE = " + movedCoordinateX);		      
		       
		       DefaultCoordinateX = Double.valueOf(defaultCoordinateX);
		       MovedCoordinateX = Double.valueOf(movedCoordinateX);
		       CharacterWidth = Double.valueOf(characterWidth);
		       MovementRatio  = (double) ( ((MovedCoordinateX - DefaultCoordinateX)*100/CharacterWidth)*1000/1000.000 );
		       helper.fileWriterPrinter("CHARACTER SIZE MOVEMENT RATIO = " + df.format(MovementRatio) + " %\n");

		       driver.findElement(By.xpath(Common.charBannerButtonRight)).click();
		       Thread.sleep(1000);
		       
		       backCoordinateX = driver.findElement(By.xpath(xpath)).getLocation().getX();
		       helper.fileWriterPrinter("CHARACTER BACK   COORDINATE =  " + backCoordinateX);
		       BackCoordinateX = Double.valueOf(backCoordinateX);
		       ReturntRatio  = (double) ( ((BackCoordinateX - MovedCoordinateX)*100/CharacterWidth)*1000/1000.000 );
		       helper.fileWriterPrinter("CHARACTER SIZE RETURN RATIO = " + df.format(ReturntRatio) + " %");
		       
		       // ASSERT 5 AND UNDER CHARACTER MOVEMENT AND RETURN:
		       helper.assertBooleanTrue(driver, new Exception().getStackTrace()[0], Math.abs(MovementRatio) > 300 );
		       helper.assertBooleanTrue(driver, new Exception().getStackTrace()[0], Math.abs(ReturntRatio) > 300 );

	           // AGE 6 AND OVER TEST:
	           helper.fileWriterPrinter("\n" + "AGE 6 AND OVER TEST:");
	           helper.getUrlWaitUntil(driver, 10, Common.sixAndOverURL);
	           
	           defaultCoordinateX = driver.findElement(By.xpath(xpath)).getLocation().getX();	           
	           characterWidth = helper.getElementWidth(driver, xpath);
	           
		       driver.findElement(By.xpath(Common.charBannerButtonLeft)).click();
		       Thread.sleep(1000);		       
		       movedCoordinateX = driver.findElement(By.xpath(xpath)).getLocation().getX();
		       
	           helper.fileWriterPrinter("\n" + "CHARACTER              WIDTH = " + characterWidth);
	           helper.fileWriterPrinter("CHARACTER DEFAULT  COORDINATE = " + defaultCoordinateX);
		       helper.fileWriterPrinter("CHARACTER   MOVED  COORDINATE = " + movedCoordinateX);		      
		       
		       DefaultCoordinateX = Double.valueOf(defaultCoordinateX);
		       MovedCoordinateX = Double.valueOf(movedCoordinateX);
		       CharacterWidth = Double.valueOf(characterWidth);
		       MovementRatio  = (double) ( ((MovedCoordinateX - DefaultCoordinateX)*100/CharacterWidth)*1000/1000.000 );
		       helper.fileWriterPrinter("CHARACTER SIZE MOVEMENT RATIO = " + df.format(MovementRatio) + " %\n");

		       driver.findElement(By.xpath(Common.charBannerButtonRight)).click();
		       Thread.sleep(1000);
		       
		       backCoordinateX = driver.findElement(By.xpath(xpath)).getLocation().getX();
		       helper.fileWriterPrinter("CHARACTER BACK   COORDINATE =  " + backCoordinateX);
		       BackCoordinateX = Double.valueOf(backCoordinateX);
		       ReturntRatio  = (double) ( ((BackCoordinateX - MovedCoordinateX)*100/CharacterWidth)*1000/1000.000 );
		       helper.fileWriterPrinter("CHARACTER SIZE RETURN RATIO = " + df.format(ReturntRatio) + " %");
		       
		       // ASSERT 6 AND OVER CHARACTER MOVEMENT AND RETURN:
		       helper.assertBooleanTrue(driver, new Exception().getStackTrace()[0], Math.abs(MovementRatio) > 300 );
		       helper.assertBooleanTrue(driver, new Exception().getStackTrace()[0], Math.abs(ReturntRatio) > 300 );
		       
	       } catch(Exception e) { UtilitiesTestHelper.getExceptionDescriptive(e, new Exception().getStackTrace()[0], driver); }
	    }
	
	/**
	 * Test Desktop - Character banner rotation using Click and Drug
	 * <p>Date Created: 2016-07-30</p>
	 * <p>Date Modified: 2016-07-30<p>
	 * <p>Original Version: V1</p>
	 * <p>Modified Version: </p>
	 * <p>Xpath: 1</p>
	 * <p>Test Cases: 34144</p>
	 */
	@SuppressWarnings("static-access")
	@Test(groups = {"TC-34144"}, priority = 16)
    public void testDesktopCharacterBannerClickAndDrugRotation() throws IOException, IllegalArgumentException, MalformedURLException {
	       try{
	    	   // INITIALISATION:
	           helper.printXmlPath(new RuntimeException().getStackTrace()[0]);
	           driver = helper.getServerName(driver);
	           driver.manage().window().maximize();
	           
	           // LOGIN TO DRUPAL AS A CONTENT EDITOR:
	           helper.logIn(driver,"content_editor","changeme");
	           
	           // CLEAN-UP:
	           helper.deleteAllContent(driver, "Custom Brand", "14", "content_editor", new RuntimeException().getStackTrace()[0]);
	           
	           // NAVIGATE TO A NEW CUSTOM BRAND PAGE:
	           helper.getUrlWaitUntil(driver, 10, Drupal.customBrand);
	           
	           // DECLARATION:
	           String title, titleURL, description, xpath;
	           double MovementRatioForward, MovementRatioBack;
		       
	           // CREATE TITLE FOR CONTENT:
	           long fingerprint = System.currentTimeMillis();
	           title = String.valueOf(fingerprint) + " " +  helper.randomWord(Drupal.titleMaxCharsNumber);
	           titleURL = helper.reFormatStringForURL(title, Drupal.titleMaxCharsNumber);
	           
	           // CREATE DESCRIPTION FOR CONTENT:
	           description = helper.randomEnglishText(helper.randomInt((Drupal.descriptionMaxCharsNumber - 30), Drupal.descriptionMaxCharsNumber));
	           
	           // CREATE CONTENT WITH BOTH AGES SELECTED:
	           helper.createCustomBrand(driver, title, description, true, true, true, new Exception().getStackTrace()[0]);
	           
	           // LINK GENERIC XPATH
	           xpath = "//a[contains(@href,'" + titleURL +  Common.XpathContainsEnd; // + "/ancestor::li";
	           helper.fileWriterPrinter("\n" + "LINK GENERIC XPATH = " + xpath);
	           
	           // AGE 5 AND UNDER TEST:
	           helper.fileWriterPrinter("\n" + "AGE 5 AND UNDER TEST:");
	           helper.getUrlWaitUntil(driver, 10, Common.fiveAndUnderURL);
	           // DRAGGING RIGHT:
	           helper.fileWriterPrinter("\n" + "DRAGGING RIGHT:");	           
	           MovementRatioForward = helper.dragAndDrop(driver, xpath, 250, 0, true, 1000)[0];
	           // DRAGGING LEFT:
		       helper.fileWriterPrinter("DRAGGING LEFT TEST:");		       
		       MovementRatioBack = helper.dragAndDrop(driver, xpath, -250, 0, true, 500)[0];		       
		       // ASSERT 5 AND UNDER CHARACTER MOVEMENT AND RETURN:
		       helper.assertBooleanTrue(driver, new Exception().getStackTrace()[0], Math.abs(MovementRatioForward) > 0 );
		       helper.assertBooleanTrue(driver, new Exception().getStackTrace()[0], Math.abs(MovementRatioBack) > 0 );
		       
	           // AGE 6 AND OVER TEST:
	           helper.fileWriterPrinter("\n" + "AGE 6 AND OVER TEST:");
	           helper.getUrlWaitUntil(driver, 10, Common.sixAndOverURL);
	           // DRAGGING RIGHT:
	           helper.fileWriterPrinter("\n" + "DRAGGING RIGHT:");	           
	           MovementRatioForward = helper.dragAndDrop(driver, xpath, 250, 0, true, 1000)[0];
	           // DRAGGING LEFT:
		       helper.fileWriterPrinter("DRAGGING LEFT TEST:");		       
		       MovementRatioBack = helper.dragAndDrop(driver, xpath, -250, 0, true, 500)[0];		       
		       // ASSERT 6 AND OVER CHARACTER MOVEMENT AND RETURN:
		       helper.assertBooleanTrue(driver, new Exception().getStackTrace()[0], Math.abs(MovementRatioForward) > 0 );
		       helper.assertBooleanTrue(driver, new Exception().getStackTrace()[0], Math.abs(MovementRatioBack) > 0 );
		       	       
	       } catch(Exception e) { UtilitiesTestHelper.getExceptionDescriptive(e, new Exception().getStackTrace()[0], driver); }
	    }
	
	/**
	 * Test the banner is Infinite looped
	 * <p>Date Created: 2016-08-08</p>
	 * <p>Date Modified: 2016-08-08<p>
	 * <p>Original Version: V1</p>
	 * <p>Modified Version: </p>
	 * <p>Xpath: 1</p>
	 * <p>Test Cases: 34147</p>
	 */
	@SuppressWarnings("static-access")
	@Test(groups = {"TC-34147"}, priority = 17)
    public void testCharacterBannerIsInfiniteLooped() throws IOException, IllegalArgumentException, MalformedURLException {
	       try{
	    	   // INITIALISATION:
	           helper.printXmlPath(new RuntimeException().getStackTrace()[0]);
	           driver = helper.getServerName(driver);
	           
	           // LOGIN TO DRUPAL AS A CONTENT EDITOR:
	           helper.logIn(driver,"content_editor","changeme");
	           
	           // CLEAN-UP:
	           helper.deleteAllContent(driver, "Custom Brand", "14", "content_editor", new RuntimeException().getStackTrace()[0]);
	           
	           // NAVIGATE TO A NEW CUSTOM BRAND PAGE:
	           helper.getUrlWaitUntil(driver, 10, Drupal.customBrand);
	           
	           // DECLARATION:
	           String title, titleURL, description, xpath;
	           int i = 0, countDisappear = i, countAppear = i;
	           
	           // CREATE TITLE FOR CONTENT:
	           long fingerprint = System.currentTimeMillis();
	           title = String.valueOf(fingerprint) + " " +  helper.randomWord(Drupal.titleMaxCharsNumber);
	           titleURL = helper.reFormatStringForURL(title, Drupal.titleMaxCharsNumber);
	           
	           // CREATE DESCRIPTION FOR CONTENT:
	           description = helper.randomEnglishText(helper.randomInt((Drupal.descriptionMaxCharsNumber - 30), Drupal.descriptionMaxCharsNumber));
	           
	           // CREATE CONTENT WITH BOTH AGES SELECTED:
	           helper.createCustomBrand(driver, title, description, true, true, true, new Exception().getStackTrace()[0]);
	           // LINK GENERIC XPATH
	           xpath = "//a[contains(@href,'" + titleURL +  Common.XpathContainsEnd;
	           helper.fileWriterPrinter("\n" + "LINK GENERIC XPATH = " + xpath);
	           
	           // AGE 5 AND UNDER TEST:
	           helper.fileWriterPrinter("\n" + "AGE 5 AND UNDER TEST:");
	           helper.getUrlWaitUntil(driver, 10, Common.fiveAndUnderURL);
	           helper.assertWebElementExist(driver, new Exception().getStackTrace()[0], xpath);
	           
	           // CLICK LEFT UNTIL DISAPPEAR:
	           while( driver.findElement(By.xpath(xpath)).getLocation().getX() > 0 )
	           {
			       driver.findElement(By.xpath(Common.charBannerButtonLeft)).click();
			       Thread.sleep(1000);
			       i++;
			       countDisappear = i;
			       }
	           helper.fileWriterPrinter("CHARACTER DISAPPEARED AFTER " + countDisappear + " CLICKS ON LEFT ARROW");
		       // CLICK LEFT UNTIL APPEAR:
	           while( driver.findElement(By.xpath(xpath)).getLocation().getX() <= 0 )
	           {
			       driver.findElement(By.xpath(Common.charBannerButtonLeft)).click();
			       Thread.sleep(1000);
			       i++;
			       countAppear = i;
			       }
               helper.fileWriterPrinter("CHARACTER APPEARED AGAIN AFTER " + countAppear + " CLICKS ON LEFT ARROW" + "\n");         
		       // ASSERT 5 AND UNDER CHARACTER LEFT LOOPING:
               helper.assertBooleanTrue(driver, new Exception().getStackTrace()[0], (countAppear > countDisappear) && (countDisappear > 0) );
               
	           // CLICK RIGHT UNTIL DISAPPEAR:
               i = 0;
               while( driver.findElement(By.xpath(xpath)).getLocation().getX() > 0 )
	           {
			       driver.findElement(By.xpath(Common.charBannerButtonRight)).click();
			       Thread.sleep(1000);
			       i++;
			       countDisappear = i;
			       }
	           helper.fileWriterPrinter("CHARACTER DISAPPEARED AFTER " + countDisappear + " CLICKS ON RIGHT ARROW");
		       // CLICK RIGHT UNTIL APPEAR:
	           while( driver.findElement(By.xpath(xpath)).getLocation().getX() <= 0 )
	           {
			       driver.findElement(By.xpath(Common.charBannerButtonRight)).click();
			       Thread.sleep(1000);
			       i++;
			       countAppear = i;
			       }
               helper.fileWriterPrinter("CHARACTER APPEARED AGAIN AFTER " + countAppear + " CLICKS ON RIGHT ARROW" + "\n");         
		       // ASSERT 5 AND UNDER CHARACTER RIGHT LOOPING:
               helper.assertBooleanTrue(driver, new Exception().getStackTrace()[0], (countAppear > countDisappear) && (countDisappear > 0) );
               
	           // AGE 6 AND OVER TEST:
               i = 0;
	           helper.fileWriterPrinter("\n" + "AGE 6 AND OVER TEST:");
	           helper.getUrlWaitUntil(driver, 10, Common.sixAndOverURL);
	           helper.assertWebElementExist(driver, new Exception().getStackTrace()[0], xpath);
	           
	           // CLICK LEFT UNTIL DISAPPEAR:
	           while( driver.findElement(By.xpath(xpath)).getLocation().getX() > 0 )
	           {
			       driver.findElement(By.xpath(Common.charBannerButtonLeft)).click();
			       Thread.sleep(1000);
			       i++;
			       countDisappear = i;
			       }
	           helper.fileWriterPrinter("CHARACTER DISAPPEARED AFTER " + countDisappear + " CLICKS ON LEFT ARROW");
		       // CLICK LEFT UNTIL APPEAR:
	           while( driver.findElement(By.xpath(xpath)).getLocation().getX() <= 0 )
	           {
			       driver.findElement(By.xpath(Common.charBannerButtonLeft)).click();
			       Thread.sleep(1000);
			       i++;
			       countAppear = i;
			       }
               helper.fileWriterPrinter("CHARACTER APPEARED AGAIN AFTER " + countAppear + " CLICKS ON LEFT ARROW" + "\n");         
		       // ASSERT 6 AND OVER CHARACTER LEFT LOOPING:
               helper.assertBooleanTrue(driver, new Exception().getStackTrace()[0], (countAppear > countDisappear) && (countDisappear > 0) );
               
	           // CLICK RIGHT UNTIL DISAPPEAR:
               i = 0;
               while( driver.findElement(By.xpath(xpath)).getLocation().getX() > 0 )
	           {
			       driver.findElement(By.xpath(Common.charBannerButtonRight)).click();
			       Thread.sleep(1000);
			       i++;
			       countDisappear = i;
			       }
	           helper.fileWriterPrinter("CHARACTER DISAPPEARED AFTER " + countDisappear + " CLICKS ON RIGHT ARROW");
		       // CLICK RIGHT UNTIL APPEAR:
	           while( driver.findElement(By.xpath(xpath)).getLocation().getX() <= 0 )
	           {
			       driver.findElement(By.xpath(Common.charBannerButtonRight)).click();
			       Thread.sleep(1000);
			       i++;
			       countAppear = i;
			       }
               helper.fileWriterPrinter("CHARACTER APPEARED AGAIN AFTER " + countAppear + " CLICKS ON RIGHT ARROW" + "\n");         
		       // ASSERT 6 AND OVER CHARACTER RIGHT LOOPING:
               helper.assertBooleanTrue(driver, new Exception().getStackTrace()[0], (countAppear > countDisappear) && (countDisappear > 0) );
               
	       } catch(Exception e) { UtilitiesTestHelper.getExceptionDescriptive(e, new Exception().getStackTrace()[0], driver); }
	    }
	
    @BeforeMethod public static void startTime() throws IOException { new UtilitiesTestHelper().startTime(); } 
    @AfterMethod  public static void endTime() throws IOException { new UtilitiesTestHelper().endTime(); }
    @AfterMethod  @AfterClass   public static void closeBrowsers() { driver.quit(); }
}
