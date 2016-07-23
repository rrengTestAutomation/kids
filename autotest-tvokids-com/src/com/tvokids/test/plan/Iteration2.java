package com.tvokids.test.plan;


import java.io.IOException;
import java.net.MalformedURLException;

import com.tvokids.locator.Drupal;
import com.tvokids.locator.Common;
import com.tvokids.test.helper.UtilitiesTestHelper;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/*
import java.awt.Robot;
import java.io.File;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
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
	@Test(groups = {"TC-35131"})
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
	 * Test Custom Brand Meta-Data Attributes - Title field content is limited
	 * <p>Date Created: 2016-07-06</p>
	 * <p>Date Modified: 2016-07-22<p>
	 * <p>Original Version: V1</p>
	 * <p>Modified Version: V3</p>
	 * <p>Xpath: 3</p>
	 * <p>Test Cases: 35131</p>
	 */
	@Test(groups = {"TC-35131","BUG-35502","BUG-528","OPEN"}, enabled = true)
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
           expectedURLend = "/" + expectedText.toLowerCase().replace(" ", "-"); 
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
	@Test(groups = {"TC-35153"})
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
	@Test(groups = {"TC-35153"})
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
	@Test(groups = {"TC-35153"})
    public void testCustomBrandBothAgesFrontEndLocationAndLinkAreCorrect() throws IOException, IllegalArgumentException, MalformedURLException {
	       try{
	    	   // INITIALISATION:
	           helper.printXmlPath(new RuntimeException().getStackTrace()[0]);
	           driver = helper.getServerName(driver);
	           
	           // LOGIN TO DRUPAL AS A CONTENT EDITOR:
	           helper.logIn(driver,"content_editor","changeme");
	           
	           // CLEAN-UP:
	           helper.deleteAllContent(driver, "", "", "", new RuntimeException().getStackTrace()[0]);
	           
	           // NAVIGATE TO A NEW CUSTOM BRAND PAGE:
	           helper.getUrlWaitUntil(driver, 10, Drupal.customBrand);
	           
	           // DECLARATION:
	           String title, titleURL, description, xpath, expectedURL;
	           
	           // CREATE TITLE FOR CONTENT:
	           long fingerprint = System.currentTimeMillis();
	           title = String.valueOf(fingerprint) + " " +  helper.randomWord(Drupal.titleMaxCharsNumber);
	           titleURL = title.replace(" ", "-").toLowerCase().substring(0, Drupal.titleMaxCharsNumber);
	           
	           // CREATE DESCRIPTION FOR CONTENT:
	           description = helper.randomEnglishText(helper.randomInt((Drupal.descriptionMaxCharsNumber - 30), Drupal.descriptionMaxCharsNumber));
	           
	           // CREATE CONTENT WITH BOTH AGES SELECTED:
	           helper.createCustomBrand(driver, title, description, true, true, true, new Exception().getStackTrace()[0]);
	           
	           // NAVIGATE TO "AGE 5 AND UNDER":
	           xpath = Common.fiveAndUnderLinkBase + titleURL + Common.XpathEqualsEnd;
	           helper.fileWriterPrinter(xpath);
	           helper.getUrlWaitUntil(driver, 10, Common.fiveAndUnderURL);
	           helper.assertWebElementExist(driver, new Exception().getStackTrace()[0], xpath);
	           
	           // ASSERT LINK IS CORRECT:
	           expectedURL = Common.fiveAndUnderURL + "/" + titleURL;
	           helper.clickLinkAndCheckURL(driver, xpath, expectedURL);
	           
	           // NAVIGATE TO "AGE 6 AND OVER":
	           xpath = Common.sixAndOvererLinkBase + titleURL + Common.XpathEqualsEnd;
	           helper.fileWriterPrinter(xpath);
	           helper.getUrlWaitUntil(driver, 10, Common.sixAndOvererURL);
	           helper.assertWebElementExist(driver, new Exception().getStackTrace()[0], xpath);
	           
	           // ASSERT LINK IS CORRECT:
	           expectedURL = Common.sixAndOvererURL + "/" + titleURL;
	           helper.clickLinkAndCheckURL(driver, xpath, expectedURL);
	           
	           } catch(Exception e) { UtilitiesTestHelper.getExceptionDescriptive(e, new Exception().getStackTrace()[0], driver); }
	       }
	
    @BeforeMethod public static void startTime() throws IOException { new UtilitiesTestHelper().startTime(); } 
    @AfterMethod  public static void endTime() throws IOException { new UtilitiesTestHelper().endTime(); }
    @AfterMethod  @AfterClass   public static void closeBrowsers() { driver.quit(); }
}
