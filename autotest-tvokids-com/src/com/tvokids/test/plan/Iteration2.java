package com.tvokids.test.plan;


import java.io.IOException;
import java.net.MalformedURLException;

import com.tvokids.test.helper.UtilitiesTestHelper;
import com.tvokids.common.Locators;
import com.tvokids.common.DrupalLocators;

import org.openqa.selenium.By;
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
           helper.getUrlWaitUntil(driver, 10, DrupalLocators.customBrand);
           
     	   // ASSERT EXISTANCE:
           helper.assertWebElementExist(driver, DrupalLocators.title, new RuntimeException().getStackTrace()[0]);
           helper.assertWebElementExist(driver, DrupalLocators.brandPageDescriptionID, new RuntimeException().getStackTrace()[0]);
           helper.assertWebElementExist(driver, DrupalLocators.ageGroup5, new RuntimeException().getStackTrace()[0]);
           helper.assertWebElementExist(driver, DrupalLocators.ageGroup6, new RuntimeException().getStackTrace()[0]);
           helper.assertWebElementExist(driver, DrupalLocators.keywords, new RuntimeException().getStackTrace()[0]);
    	  
           } catch(Exception e) { UtilitiesTestHelper.getExceptionDescriptive(e, new Exception().getStackTrace()[0], driver); }
   }
	
	/**
	 * Test Custom Brand Meta-Data Attributes - Title field content is limited
	 * <p>Date Created: 2016-07-06</p>
	 * <p>Date Modified: 2016-07-06<p>
	 * <p>Original Version: V1</p>
	 * <p>Modified Version: </p>
	 * <p>Xpath: 1</p>
	 * <p>Test Cases: 35131</p>
	 */
	@Test(groups = {"TC-35131","BUG-35502","OPEN"}, enabled = true)
    public void testCustomBrandTitleFieldContentLimit() throws IOException, IllegalArgumentException, MalformedURLException {
       try{
    	   // INITIALISATION:
           helper.printXmlPath(new RuntimeException().getStackTrace()[0]);
           driver = helper.getServerName(driver);

           // DECLARATION:
           int actual, expected, number;
           String actualText, expectedText, expectedURL, title;
           
           // LOGIN TO DRUPAL AS A CONTENT EDITOR:
           helper.logIn(driver,"content_editor","changeme");
           
           // CLEAN-UP:
           helper.deleteAllContent(driver, "Custom Brand", "", "content_editor", new RuntimeException().getStackTrace()[0]);
           
           // NAVIGATE TO A NEW CUSTOM BRAND PAGE:
           helper.getUrlWaitUntil(driver, 10, DrupalLocators.customBrand);
           driver.manage().window().maximize();
           
     	   // ASSERT EMPTY TITLE FIELD:
           driver.findElement(By.id(DrupalLocators.title)).clear();
           actual = Integer.valueOf( driver.findElement(By.xpath(DrupalLocators.titleRemainCharsNumber)).getText() );
           expected = DrupalLocators.titleMaxCharsNumber;
           helper.assertEquals(driver, new Exception().getStackTrace()[0], actual, expected);
           
           // ASSERT TITLE FIELD CAPACITY COUNTER:          
           number = helper.randomInt(1, (DrupalLocators.titleMaxCharsNumber -1));
           title = helper.randomEnglishText(number);
           driver.findElement(By.id(DrupalLocators.title)).clear();
           driver.findElement(By.id(DrupalLocators.title)).sendKeys(title);
           actual = Integer.valueOf( driver.findElement(By.xpath(DrupalLocators.titleRemainCharsNumber)).getText() );
           expected = DrupalLocators.titleMaxCharsNumber - number;
           helper.assertEquals(driver, new Exception().getStackTrace()[0], actual, expected);
           
           // ASSERT TITLE FIELD MAXIMUM CAPACITY:           
           title = helper.randomEnglishText(DrupalLocators.titleMaxCharsNumber);
           driver.findElement(By.id(DrupalLocators.title)).clear();
           driver.findElement(By.id(DrupalLocators.title)).sendKeys(title);
           actual = Integer.valueOf( driver.findElement(By.xpath(DrupalLocators.titleRemainCharsNumber)).getText() );
           expected = 0;
           helper.assertEquals(driver, new Exception().getStackTrace()[0], actual, expected);
           
           // ASSERT TITLE FIELD OVERLOAD ENTRY:
           driver.findElement(By.id(DrupalLocators.title)).clear();
           
           // CREATE TITLE FOR CONTENT:
//           text = helper.randomEnglishText(DrupalAddContentLocators.titleMaxCharsNumber + 10);
//           text = helper.randomWord(DrupalAddContentLocators.titleMaxCharsNumber + 10);
           long fingerprint = System.currentTimeMillis();
           title = String.valueOf(fingerprint) + " " +  helper.randomText(DrupalLocators.titleMaxCharsNumber + 10);
             
           driver.findElement(By.id(DrupalLocators.title)).sendKeys(title);
           actual = Integer.valueOf( driver.findElement(By.xpath(DrupalLocators.titleRemainCharsNumber)).getText() );
           expected = 0;
           helper.assertEquals(driver, new Exception().getStackTrace()[0], actual, expected);

           // CREATE CONTENT:
//           Robot robot = new Robot();
//           helper.createCustomBrand(driver, text, true, true, robot, fingerprint);
           helper.createCustomBrand(driver, title, true, true, fingerprint);
          
           // ACCEPTANCE CRITERIA:
           expectedText = title.substring(0, DrupalLocators.titleMaxCharsNumber);
           expectedURL = Locators.homeURL + "/" + expectedText.toLowerCase().replace(" ", "-");

           // ASSERT CONTENT URL USES A LIMITED TITLE:
           helper.checkCurrentURL(driver, new Exception().getStackTrace()[0], expectedURL); 
          
           // ASSERT TITLE FIELD OVERLOAD CONTENT IS LIMITED:
           actualText = driver.findElement(By.xpath(Locators.title)).getText();
           helper.assertEquals(driver, new Exception().getStackTrace()[0], actualText, expectedText);
           
//         helper.getUrlWaitUntil(driver, 15, Locators.adminContentURL);          
//		   WebElement dropwDownListBox = driver.findElement(By.id("edit-type"));
//		   Select clickThis = new Select(dropwDownListBox);
//		   Thread.sleep(2000);
//		   clickThis.selectByVisibleText("Custom Brand");
//		   Thread.sleep(2000);		   
//		   driver.findElement(By.id("edit-author")).clear();
//		   driver.findElement(By.id("edit-author")).sendKeys("content_editor");
//		   int size = helper.waitUntilElementList(driver, 5, Locators.autoComplete, "auto-complete").size();
//         if (size == 1) { try { driver.findElement(By.xpath(Locators.autoComplete)).click(); } catch(Exception e) { } }
//         helper.waitUntilElementInvisibility(driver, 15, Locators.autoComplete, "auto-complete", new Exception().getStackTrace()[0]);
//		   driver.findElement(By.id("edit-submit-admin-views-node")).click();
//		   helper.waitUntilElementInvisibility(driver, 30, Locators.ajaxThrobber, "Throbber", new Exception().getStackTrace()[0]);          
//		   xpath = DrupalAddContentLocators.adminContentRowFirst + DrupalAddContentLocators.adminContentFieldTitles;
//		   actualText = driver.findElement(By.xpath(xpath)).getText();
//         helper.assertEquals(driver, new Exception().getStackTrace()[0], actualText, expectedText);  
           
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
           helper.getUrlWaitUntil(driver, 10, DrupalLocators.customBrand);
           driver.manage().window().maximize();
           
           // CREATE TITLE FOR CONTENT:
           long fingerprint = System.currentTimeMillis();
           title = String.valueOf(fingerprint) + " " +  helper.randomText(DrupalLocators.titleMaxCharsNumber + 10);

           // CREATE CONTENT WITH NO DESCRIPTION:
           helper.createCustomBrand(driver, title, "", true, true, true);
          
           // ASSERT CONTENT URL DID NOT CHANGE:
           expectedURL = DrupalLocators.customBrand;
           helper.checkCurrentURL(driver, new Exception().getStackTrace()[0], expectedURL);
           
           // ASSERT ERROR MESSAGE APPEARS:
           helper.assertWebElementExist(driver, new Exception().getStackTrace()[0], DrupalLocators.errorDescription);
           
           } catch(Exception e) { UtilitiesTestHelper.getExceptionDescriptive(e, new Exception().getStackTrace()[0], driver); }
       }
	
	/**
	 * Test attempt to create a Brand Page Description that is longer than 135 characters is rejected
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
	           helper.getUrlWaitUntil(driver, 10, DrupalLocators.customBrand);
	           
	           // DECLARATION:
	           int actual, expected, number;
	           String description = "";
	    
	           // ASSERT EMPTY DESCRIPTION FIELD:
	           driver.findElement(By.id(DrupalLocators.brandPageDescriptionID)).clear();
	           helper.fileWriterPrinter();
	           helper.fileWriterPrinter("DESCRIPTION: " + description);
	           helper.fileWriterPrinter("     LENGTH: " + description.length());
	           actual = Integer.valueOf( driver.findElement(By.cssSelector(DrupalLocators.brandPageDescriptionCounterCSS)).getText() );
	           expected = DrupalLocators.descriptionMaxCharsNumber;
	           helper.assertEquals(driver, new Exception().getStackTrace()[0], actual, expected);
	           
	           // ASSERT TITLE FIELD CAPACITY COUNTER:	           
	           number = helper.randomInt(1, (DrupalLocators.descriptionMaxCharsNumber -1));
	           description = helper.randomEnglishText(number);
	           helper.fileWriterPrinter("DESCRIPTION: " + description);
	           helper.fileWriterPrinter("     LENGTH: " + description.length());
	           driver.findElement(By.id(DrupalLocators.brandPageDescriptionID)).clear();
	           driver.findElement(By.id(DrupalLocators.brandPageDescriptionID)).sendKeys(description);
	           actual = Integer.valueOf( driver.findElement(By.cssSelector(DrupalLocators.brandPageDescriptionCounterCSS)).getText() );
	           expected = DrupalLocators.descriptionMaxCharsNumber - number;
	           helper.assertEquals(driver, new Exception().getStackTrace()[0], actual, expected);
	           
	           // ASSERT TITLE FIELD MAXIMUM CAPACITY:           
	           description = helper.randomEnglishText(DrupalLocators.descriptionMaxCharsNumber);
	           helper.fileWriterPrinter("DESCRIPTION: " + description);
	           helper.fileWriterPrinter("     LENGTH: " + description.length());
	           driver.findElement(By.id(DrupalLocators.brandPageDescriptionID)).clear();
	           driver.findElement(By.id(DrupalLocators.brandPageDescriptionID)).sendKeys(description);
	           actual = Integer.valueOf( driver.findElement(By.cssSelector(DrupalLocators.brandPageDescriptionCounterCSS)).getText() );
	           expected = 0;
	           helper.assertEquals(driver, new Exception().getStackTrace()[0], actual, expected);
	           
	           // ASSERT TITLE FIELD OVERLOAD ENTRY:
	           description = helper.randomEnglishText(DrupalLocators.descriptionMaxCharsNumber + 10);
	           helper.fileWriterPrinter("DESCRIPTION: " + description);
	           helper.fileWriterPrinter("     LENGTH: " + description.length());
	           driver.findElement(By.id(DrupalLocators.brandPageDescriptionID)).clear();
	           driver.findElement(By.id(DrupalLocators.brandPageDescriptionID)).sendKeys(description);
	           actual = Integer.valueOf( driver.findElement(By.cssSelector(DrupalLocators.brandPageDescriptionCounterCSS)).getText() );
	           expected = 0;
	           helper.assertEquals(driver, new Exception().getStackTrace()[0], actual, expected);

	           } catch(Exception e) { UtilitiesTestHelper.getExceptionDescriptive(e, new Exception().getStackTrace()[0], driver); }
	       }
	
    @BeforeMethod public static void startTime() throws IOException { new UtilitiesTestHelper().startTime(); } 
    @AfterMethod  public static void endTime() throws IOException { new UtilitiesTestHelper().endTime(); }
    @AfterMethod  @AfterClass   public static void closeBrowsers() { driver.quit(); }
}
