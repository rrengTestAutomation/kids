package com.tvokids.test.plan.Iteration2;

import java.io.IOException;
import java.net.MalformedURLException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.tvokids.locator.Drupal;
import com.tvokids.test.helper.UtilitiesTestHelper;
/*
import java.awt.Robot;
import java.io.File;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
*/
import com.tvokids.locator.Common;

@SuppressWarnings("static-access")
public class BrandPage {
	static WebDriver driver;
	UtilitiesTestHelper helper = new UtilitiesTestHelper();

    @BeforeMethod public static void startTime() throws IOException { new UtilitiesTestHelper().startTime(); } 
    @AfterMethod  public static void endTime() throws IOException { new UtilitiesTestHelper().endTime(); }
    @AfterMethod  @AfterClass   public static void closeBrowsers() { driver.quit(); }
    
	/**
	 * Test Custom Brand Meta-Data Attributes - elements exist
	 * <p>Date Created: 2016-07-06</p>
	 * <p>Date Modified: 2016-07-06</p>
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
    	  
           } catch(Exception e) { helper.getExceptionDescriptive(e, new Exception().getStackTrace()[0], driver); }
       }
	
	/**
	 * Test attempt to create a Custom Brand without a title is rejected
	 * <p>Date Created: 2016-07-20</p>
	 * <p>Date Modified: 2016-07-20<p>
	 * <p>Original Version: V1</p>
	 * <p>Modified Version: </p>
	 * <p>Xpath: 1</p>
	 * <p>Test Cases: 35131 35153</p>
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
	       
	       } catch(Exception e) { helper.getExceptionDescriptive(e, new Exception().getStackTrace()[0], driver); }
	   }
	
	/**
	 * Test Custom Brand Meta-Data Attributes - Title field content is limited
	 * <p>Date Created: 2016-07-06</p>
	 * <p>Date Modified: 2016-07-22<p>
	 * <p>Original Version: V1</p>
	 * <p>Modified Version: V3</p>
	 * <p>Xpath: 3</p>
	 * <p>Test Cases: 35131 35153</p>
	 */
	@Test(groups = {"TC-35131","TC-35153","BUG-35502","BUG-528","BUG-529","CLOSED"}, priority = 3)
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
           helper.deleteAllContent(driver, "Custom Brand", "", "content_editor", new RuntimeException().getStackTrace()[0]);
           
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
           
           } catch(Exception e) { helper.getExceptionDescriptive(e, new Exception().getStackTrace()[0], driver); }
       }
	
	/**
	 * Test attempt to create a Custom Brand without a description is rejected
	 * <p>Date Created: 2016-07-20</p>
	 * <p>Date Modified: 2016-07-20<p>
	 * <p>Original Version: V1</p>
	 * <p>Modified Version: </p>
	 * <p>Xpath: 1</p>
	 * <p>Test Cases: 35153 35131</p>
	 */
	@Test(groups = {"TC-35153","TC-35131"}, priority = 4)
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
	       
	       } catch(Exception e) { helper.getExceptionDescriptive(e, new Exception().getStackTrace()[0], driver); }
	   }
	
	/**
	 * Test attempt to create a Custom Brand Page Description that is longer than 135 characters is rejected
	 * <p>Date Created: 2016-07-20</p>
	 * <p>Date Modified: 2016-07-20<p>
	 * <p>Original Version: V1</p>
	 * <p>Modified Version: </p>
	 * <p>Xpath: 1</p>
	 * <p>Test Cases: 35153 35131</p>
	 */
	@Test(groups = {"TC-35153","TC-35131"}, priority = 5)
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
	
	           } catch(Exception e) { helper.getExceptionDescriptive(e, new Exception().getStackTrace()[0], driver); }
	       }

	/**
	 * Test create a Custom Brand Page on both 5 and Under & 6 and Over is located on the appropriate front end age page and navigatation link is correct
	 * <p>Date Created: 2016-07-22</p>
	 * <p>Date Modified: 2016-07-22<p>
	 * <p>Original Version: V1</p>
	 * <p>Modified Version: </p>
	 * <p>Xpath: 1</p>
	 * <p>Test Cases: 35153</p>
	 */
	@Test(groups = {"TC-35153","BUG-650","OPEN"}, enabled = false, priority = 6)
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
	           helper.deleteAllContent(driver, "Custom Brand", "", "content_editor", new RuntimeException().getStackTrace()[0]);
	           
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
	           
	           // LINK GENERIC XPATH:
	           xpath = "//a[contains(@href,'" + titleURL +  Common.XpathContainsEnd;
	           helper.fileWriterPrinter("\n" + "LINK GENERIC XPATH = " + xpath + "\n");
	           
	           // AGE 5 AND UNDER TEST:
	           helper.fileWriterPrinter("\n" + "AGE 5 AND UNDER TEST:");
	           helper.getUrlWaitUntil(driver, 10, Common.fiveAndUnderURL);
	           helper.assertWebElementExist(driver, new Exception().getStackTrace()[0], xpath);
	           // ASSERT LINK IS CORRECT:
	           expectedURL = Common.fiveAndUnderURL + "/" + titleURL;
	           helper.clickToAppear(driver, Common.charBannerButtonLeft, Common.charBannerButtonRight, xpath, false, false);
	           helper.clickLinkAndCheckURL(driver, new Exception().getStackTrace()[0], xpath, expectedURL, false, true);
	           helper.assertFont(driver, new Exception().getStackTrace()[0], Common.brandTitle,
	        		   Common.brandTitleFontName, "font-family",
	        		   Common.brandTitleFontSize, "font-size",
	        		   Common.brandTitleFontColour, "color");
	           helper.assertFont(driver, new Exception().getStackTrace()[0], Common.brandDescription, 
	        		   Common.brandDescriptionFontName, "font-family", 
	        		   "", "font-size", 
	        		   Common.brandDescriptionFontColour, "color");
	           
	           // AGE 6 AND OVER TEST:
	           helper.fileWriterPrinter("\n" + "AGE 6 AND OVER TEST:");
	           helper.getUrlWaitUntil(driver, 10, Common.sixAndOverURL);
	           helper.assertWebElementExist(driver, new Exception().getStackTrace()[0], xpath);
	           // ASSERT LINK IS CORRECT:
	           expectedURL = Common.sixAndOverURL + "/" + titleURL;
	           driver.findElement(By.xpath(Common.charBannerButtonLeft)).click();
	           helper.clickToAppear(driver, Common.charBannerButtonLeft, Common.charBannerButtonRight, xpath, false, false);
	           helper.clickLinkAndCheckURL(driver, new Exception().getStackTrace()[0], xpath, expectedURL, false, true);
	           helper.assertFont(driver, new Exception().getStackTrace()[0], Common.brandTitle,
	        		   Common.brandTitleFontName, "font-family",
	        		   Common.brandTitleFontSize, "font-size",
	        		   Common.brandTitleFontColour, "color");
	           helper.assertFont(driver, new Exception().getStackTrace()[0], Common.brandDescription,
	        		   Common.brandDescriptionFontName, "font-family",
	        		   "", "font-size",
	        		   Common.brandDescriptionFontColour, "color");
	           
	           } catch(Exception e) { helper.getExceptionDescriptive(e, new Exception().getStackTrace()[0], driver); }
	       }
	
	/**
	 * Test Brand Page Browser Title 
	 * <p>Date Created: 2016-08-18</p>
	 * <p>Date Modified: 2016-08-18<p>
	 * <p>Original Version: V1</p>
	 * <p>Modified Version: </p>
	 * <p>Xpath: 1</p>
	 * <p>Test Cases: 34558</p>
	 */
	@Test(groups = {"TC-34558"}, priority = 23)
    public void testBrandPageBrowserTitle() throws IOException, IllegalArgumentException, MalformedURLException {
	       try{
	    	   // INITIALISATION:
	           helper.printXmlPath(new RuntimeException().getStackTrace()[0]);
	           driver = helper.getServerName(driver);
	           
	           // LOGIN TO DRUPAL AS A CONTENT EDITOR:
	           helper.logIn(driver);
	           
	           // CLEAN-UP:
	           helper.deleteAllContent(driver, "Character Brand", "", "dev", new RuntimeException().getStackTrace()[0]);
	           
	           // DECLARATION:
	           String title, titleURL, description, xpath, actual, expected;
	           
	           // CREATE TITLE FOR CONTENT:
	           long fingerprint = System.currentTimeMillis();
	           title = String.valueOf(fingerprint) + " " +  helper.randomWord(Drupal.titleMaxCharsNumber);
	           titleURL = helper.reFormatStringForURL(title, Drupal.titleMaxCharsNumber);
	           
	           // CREATE DESCRIPTION FOR CONTENT:
	           description = helper.randomEnglishText(helper.randomInt((Drupal.descriptionMaxCharsNumber - 30), Drupal.descriptionMaxCharsNumber));
	           
	           // CREATE CONTENT WITH BOTH AGES SELECTED:
	           helper.createCharacterBrand(driver, title, description, 281374, true, true, true, new Exception().getStackTrace()[0]);
	           
	           // LINK GENERIC XPATH:
	           xpath = "//a[contains(@href,'" + titleURL +  Common.XpathContainsEnd;
	           helper.fileWriterPrinter("\n" + "LINK GENERIC XPATH = " + xpath + "\n");
	           
	           // AGE 5 AND UNDER TEST:
	           helper.fileWriterPrinter("\n" + "AGE 5 AND UNDER TEST:");
	           helper.getUrlWaitUntil(driver, 10, Common.fiveAndUnderURL);
	           helper.clickToAppear(driver, Common.charBannerButtonLeft, Common.charBannerButtonRight, xpath, false, false);
               helper.clickLinkUrlWaitUntil(driver, 10, xpath, new RuntimeException().getStackTrace()[0]);
               // ASSERT:
               actual = driver.findElement(By.xpath(Common.brandTitle)).getText();
	           expected = title;
	           helper.assertEquals(driver, new RuntimeException().getStackTrace()[0], actual, expected);

	           // AGE 6 AND OVER TEST:
	           helper.fileWriterPrinter("\n" + "AGE 6 AND OVER TEST:");
	           helper.getUrlWaitUntil(driver, 10, Common.sixAndOverURL);
	           helper.clickToAppear(driver, Common.charBannerButtonLeft, Common.charBannerButtonRight, xpath, false, false);
               helper.clickLinkUrlWaitUntil(driver, 10, xpath, new RuntimeException().getStackTrace()[0]);
               // ASSERT:
               actual = driver.findElement(By.xpath(Common.brandTitle)).getText();
	           expected = title;
	           helper.assertEquals(driver, new RuntimeException().getStackTrace()[0], actual, expected);
	           
	           } catch(Exception e) { helper.getExceptionDescriptive(e, new Exception().getStackTrace()[0], driver); }
	       }

}
