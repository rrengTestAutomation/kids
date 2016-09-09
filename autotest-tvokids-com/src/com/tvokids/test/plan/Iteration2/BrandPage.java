package com.tvokids.test.plan.Iteration2;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
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
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
*/
import com.tvokids.locator.Common;
import com.tvokids.locator.Drupal;
import com.tvokids.test.helper.UtilitiesTestHelper;

@SuppressWarnings("static-access")
public class BrandPage {
	static WebDriver driver;
	UtilitiesTestHelper helper = new UtilitiesTestHelper();

    @BeforeMethod public static void startTime(Method method) throws IOException { new UtilitiesTestHelper().startTime(method); }   
    @AfterMethod  public static void endTime() throws IOException { new UtilitiesTestHelper().endTime(); }
    @AfterMethod  @AfterClass   public static void closeBrowsers() { driver.quit(); }
    
	/**
	 * Test Custom Brand Meta-Data Attributes - elements exist
	 * <p>Date Created: 2016-07-06</p>
	 * <p>Date Modified: 2016-07-06</p>
	 * <p>Original Version: V1</p>
	 * <p>Modified Version: </p>
	 * <p>Xpath: 1</p>
	 * <p>Test Cases: 35131 3229</p> 
	 */
	@Test(groups = {"TC-35131","US-3229"}, priority = 1)
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
	 * <p>Date Modified: 2016-07-20</p>
	 * <p>Original Version: V1</p>
	 * <p>Modified Version: </p>
	 * <p>Xpath: 1</p>
	 * <p>Test Cases: 35131 35153 3229</p>
	 */
	@Test(groups = {"TC-35131","TC-35153","US-3229"}, priority = 2)
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
	 * <p>Date Modified: 2016-07-22</p>
	 * <p>Original Version: V1</p>
	 * <p>Modified Version: V3</p>
	 * <p>Xpath: 3</p>
	 * <p>Test Cases: 35131 35153 3229 524 528 529</p>
	 */
	@Test(groups = {"TC-35131","TC-35153","US-3229","BUG-35502","BUG-524","BUG-528","BUG-529","CLOSED"}, priority = 3)
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
           helper.deleteAllContent(driver, "", "", "dev, content_editor", new RuntimeException().getStackTrace()[0]);
           
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
	 * <p>Date Modified: 2016-07-20</p>
	 * <p>Original Version: V1</p>
	 * <p>Modified Version: </p>
	 * <p>Xpath: 1</p>
	 * <p>Test Cases: 35153 35131 3229</p>
	 */
	@Test(groups = {"TC-35153","TC-35131","US-3229"}, priority = 4)
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
	 * <p>Date Modified: 2016-07-20</p>
	 * <p>Original Version: V1</p>
	 * <p>Modified Version: </p>
	 * <p>Xpath: 1</p>
	 * <p>Test Cases: 35153 35131 3229</p>
	 */
	@Test(groups = {"TC-35153","TC-35131","US-3229"}, priority = 5)
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
	 * <p>Date Modified: 2016-07-22</p>
	 * <p>Original Version: V1</p>
	 * <p>Modified Version: </p>
	 * <p>Xpath: 1</p>
	 * <p>Test Cases: 35153 3229 650</p>
	 */
	@Test(groups = {"TC-35153","US-3229","BUG-650","NEW"}, enabled = false, priority = 6)
    public void testCustomBrandBothAgesFrontEndLocationAndLinkAreCorrect() throws IOException, IllegalArgumentException, MalformedURLException {
	       try{
	    	   // INITIALISATION:
	           helper.printXmlPath(new RuntimeException().getStackTrace()[0]);
	           driver = helper.getServerName(driver);
	           
	           // LOGIN TO DRUPAL AS A CONTENT EDITOR:
	           helper.logIn(driver,"content_editor","changeme");
	           
	           // CLEAN-UP:
	           helper.deleteAllContent(driver, "", "", "dev, content_editor", new RuntimeException().getStackTrace()[0]);
	           
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
	 * <p>Date Modified: 2016-08-18</p>
	 * <p>Original Version: V1</p>
	 * <p>Modified Version: </p>
	 * <p>Xpath: 1</p>
	 * <p>Test Cases: 34558 3461</p>
	 */
	@Test(groups = {"TC-34558","US-3461"}, priority = 23)
    public void testBrandPageBrowserTitle() throws IOException, IllegalArgumentException, MalformedURLException {
	       try{
	    	   // INITIALISATION:
	           helper.printXmlPath(new RuntimeException().getStackTrace()[0]);
	           driver = helper.getServerName(driver);
	           
	           // LOGIN TO DRUPAL AS AN ADMIN:
	           helper.logIn(driver);
	           
	           // CLEAN-UP:
	           helper.deleteAllContent(driver, "", "", "dev, content_editor", new RuntimeException().getStackTrace()[0]);
	           
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
	
	/**
	 * Test Brand Page Upload Image for Hero Box only jpg and jpeg and png files are allowed
	 * <p>Date Created: 2016-08-24</p>
	 * <p>Date Modified: 2016-08-24</p>
	 * <p>Original Version: V1</p>
	 * <p>Modified Version: </p>
	 * <p>Xpath: 1</p>
	 * <p>Test Cases: 35154 3311</p>
	 */
	@Test(groups = {"TC-35154","US-3311"}, priority = 24)
    public void testBrandPageHeroBoxImageUploadOnlyJpgJpegPngFilesAllowed() throws IOException, IllegalArgumentException, MalformedURLException {
	       try{
	    	   // INITIALISATION:
	           helper.printXmlPath(new RuntimeException().getStackTrace()[0]);
	           driver = helper.getServerName(driver);
	           
	           // LOGIN TO DRUPAL AS A CONTENT EDITOR:
	           helper.logIn(driver,"content_editor","changeme");

	           // DECLARATION:
	           String actual, expected;
	           String browse = Drupal.heroBoxBrowse, error = Drupal.errorBrowse; 
			   String imageDir = Common.localImageDir, imagePath, image = "hero.gif";
			   
	           // CREATE CONTENT WITH BOTH AGES SELECTED:
	           helper.getUrlWaitUntil(driver, 15, Drupal.customBrand);
	           driver.findElement(By.xpath(Drupal.heroBoxVerticalTab)).click();
	           
			   // ASSERT GIF FILE NOT ALLOWED:
			   imagePath = imageDir + File.separator + image;			   
		       driver.findElement(By.xpath(browse)).sendKeys(imagePath);
		       actual = driver.findElement(By.xpath(error)).getText();
		       expected = Drupal.errorMessageHeroBoxWrongImageFormat(image);
		       helper.assertEquals(driver, new RuntimeException().getStackTrace()[0], actual, expected);

	           // ASSERT JPG FILE IS ALLOWED:
		       driver.navigate().refresh();
	           imagePath = imageDir + File.separator + "hero.jpg";
			   error     = Drupal.errorBrowse;
		       driver.findElement(By.xpath(browse)).sendKeys(imagePath);
	           helper.assertWebElementNotExist(driver,  new Exception().getStackTrace()[0], error);           
	           
	           } catch(Exception e) { helper.getExceptionDescriptive(e, new Exception().getStackTrace()[0], driver); }
	       }
	
	/**
	 * Test Brand Page Upload Image for Hero Box less then minimum dimensions not allowed
	 * <p>Date Created: 2016-08-24</p>
	 * <p>Date Modified: 2016-08-24</p>
	 * <p>Original Version: V1</p>
	 * <p>Modified Version: </p>
	 * <p>Xpath: 1</p>
	 * <p>Test Cases: 35154 3311</p>
	 */
	@Test(groups = {"TC-35154","US-3311"}, priority = 25)
    public void testBrandPageHeroBoxImageUploadLessThenMinDimensionsNotAllowed() throws IOException, IllegalArgumentException, MalformedURLException {
	       try{	    	   
	    	   // INITIALISATION:
	           helper.printXmlPath(new RuntimeException().getStackTrace()[0]);
	           driver = helper.getServerName(driver);
	           
	           // LOGIN TO DRUPAL AS CONTENT-EDITOR:
	           helper.logIn(driver,"content_editor","changeme");
	           
	           // DECLARATION:
	           String title, titleURL, description, actual, expected;
	           String browse = Drupal.heroBoxBrowse, upload = Drupal.heroBoxUpload, remove = Drupal.heroBoxRemove;
			   String imageDir = Common.localImageDir, imagePath;
			   String image = "hero 707x397.jpg", imageMin = "hero 708x398.jpg", imageOverMin = "hero 709x399.jpg";
	           
	           
	           // CREATE TITLE FOR CONTENT:
	           long fingerprint = System.currentTimeMillis();
	           title = String.valueOf(fingerprint) + " " +  helper.randomWord(Drupal.titleMaxCharsNumber);
	           titleURL = helper.reFormatStringForURL(title, Drupal.titleMaxCharsNumber);
	           
	           // CREATE DESCRIPTION FOR CONTENT:
	           description = helper.randomEnglishText(helper.randomInt((Drupal.descriptionMaxCharsNumber - 30), Drupal.descriptionMaxCharsNumber));
	           
	           // CREATE CONTENT WITH BOTH AGES SELECTED:	           
	           helper.createCustomBrand(driver, titleURL, description, true, true, false, new RuntimeException().getStackTrace()[0], "bubble.jpg", image, "", "");
	           
	           // ASSERT LESS THEN MINIMUM DIMENSIONS IMAGE NOT ALLOWED:
	           helper.assertWebElementExist(driver, new RuntimeException().getStackTrace()[0], Drupal.errorUpload);
	           actual = driver.findElement(By.xpath( Drupal.errorUpload)).getText();
	           expected = Drupal.errorMessageHeroBoxWrongImagePixels(image);
	           helper.assertEquals(driver, new RuntimeException().getStackTrace()[0], actual, expected);
		       		       
	           // ASSERT MINIMUM DIMENSIONS IMAGE IS ALLOWED:
	           driver.navigate().refresh();
	           helper.waitUntilElementPresence(driver, 10, By.id(Drupal.title), "Title", new Exception().getStackTrace()[0]);
	           driver.findElement(By.xpath(Drupal.heroBoxVerticalTab)).click();
	           imagePath = imageDir + File.separator + imageMin;
	           driver.findElement(By.xpath(browse)).sendKeys(imagePath);
	           Thread.sleep(1000);
	           helper.ajaxProtectedClick(driver, upload, "Upload", true, Common.ajaxThrobber, true, 5, false);	       
	           helper.assertWebElementNotExist(driver,  new Exception().getStackTrace()[0], Drupal.errorUpload);

	           // ASSERT MORE THEN MINIMUM DIMENSIONS IMAGE IS ALLOWED:
	           driver.findElement(By.xpath(remove)).click();
	           helper.waitUntilElementInvisibility(driver, 10, Common.ajaxThrobber, "Throbber", new Exception().getStackTrace()[0]);
	           imagePath = imageDir + File.separator + imageOverMin;
	           driver.findElement(By.xpath(browse)).sendKeys(imagePath);
	           Thread.sleep(1000);
	           helper.ajaxProtectedClick(driver, upload, "Upload", true, Common.ajaxThrobber, true, 5, false);		       
	           helper.assertWebElementNotExist(driver,  new Exception().getStackTrace()[0], Drupal.errorUpload);
	           
	           } catch(Exception e) { helper.getExceptionDescriptive(e, new Exception().getStackTrace()[0], driver); }
	       }
	
	/**
	 * Test Brand Page Upload Image for Hero Box larger than 75kb not allowed
	 * <p>Date Created: 2016-08-24</p>
	 * <p>Date Modified: 2016-08-24</p>
	 * <p>Original Version: V1</p>
	 * <p>Modified Version: </p>
	 * <p>Xpath: 1</p>
	 * <p>Test Cases: 35154 3311</p>
	 */
	@Test(groups = {"TC-35154","US-3311"}, priority = 26)
    public void testBrandPageHeroBoxImageUploadLargerThenMaxSizeNotAllowed() throws IOException, IllegalArgumentException, MalformedURLException {
	       try{
	    	   // INITIALISATION:
	           helper.printXmlPath(new RuntimeException().getStackTrace()[0]);
	           driver = helper.getServerName(driver);
	           
	           // LOGIN TO DRUPAL AS CONTENT-EDITOR:
	           helper.logIn(driver,"content_editor","changeme");
	           
	           // DECLARATION:
	           String title, titleURL, description, actual, expected, image = "hero more then 75Kb.png";
	           
	           // CREATE TITLE FOR CONTENT:
	           long fingerprint = System.currentTimeMillis();
	           title = String.valueOf(fingerprint) + " " +  helper.randomWord(Drupal.titleMaxCharsNumber);
	           titleURL = helper.reFormatStringForURL(title, Drupal.titleMaxCharsNumber);
	           
	           // CREATE DESCRIPTION FOR CONTENT:
	           description = helper.randomEnglishText(helper.randomInt((Drupal.descriptionMaxCharsNumber - 30), Drupal.descriptionMaxCharsNumber));
	           
	           // CREATE CONTENT WITH BOTH AGES SELECTED:	           
	           helper.createCustomBrand(driver, titleURL, description, true, true, false, new RuntimeException().getStackTrace()[0], "bubble.jpg", image, "", "");
	           
	           // ASSERT ERROR MESSAGE APPEARS (MORE THEN MAXIMUM SIZE IMAGE NOT ALLOWED):	   
	           helper.assertWebElementExist(driver, new RuntimeException().getStackTrace()[0], Drupal.errorUpload);
	           actual = driver.findElement(By.xpath( Drupal.errorUpload)).getText();
	           expected = helper.getFileSpaceSize(Common.localImageDir + File.separator + image, "Kbit", 2);
	           expected = Drupal.errorMessageHeroBoxWrongUploadSize(image, expected);
	           helper.assertEquals(driver, new RuntimeException().getStackTrace()[0], actual, expected);
	           
	           } catch(Exception e) { helper.getExceptionDescriptive(e, new Exception().getStackTrace()[0], driver); }
	       }
	
	/**
	 * Test Brand Page Upload Image for Hero Box Hero image has no alternate text
	 * <p>Date Created: 2016-08-24</p>
	 * <p>Date Modified: 2016-08-24</p>
	 * <p>Original Version: V1</p>
	 * <p>Modified Version: </p>
	 * <p>Xpath: 1</p>
	 * <p>Test Cases: 35154 3311 35220 3188 3996</p>
	 */
	@Test(groups = {"TC-35154","US-3311","TC-35220","US-3188","US-3996"}, priority = 27)
    public void  testBrandPageHeroBoxImageUploadAlternativeTextNotExist() throws IOException, IllegalArgumentException, MalformedURLException {
	       try{
	    	   // INITIALISATION:
	           helper.printXmlPath(new RuntimeException().getStackTrace()[0]);
	           driver = helper.getServerName(driver);
	           
	           // LOGIN TO DRUPAL AS A CONTENT EDITOR:
	           helper.logIn(driver,"content_editor","changeme");
	           
	           // CLEAN-UP:
	           helper.deleteAllContent(driver, "", "", "dev, content_editor", new RuntimeException().getStackTrace()[0]);
	           
	           // NAVIGATE TO A NEW CUSTOM BRAND PAGE:
	           helper.getUrlWaitUntil(driver, 10, Drupal.customBrand);
	           
	           // DECLARATION:
	           String title, titleURL, description, xpath, alternate;
	           
	           // CREATE TITLE FOR CONTENT:
	           long fingerprint = System.currentTimeMillis();
	           title = String.valueOf(fingerprint) + " " +  helper.randomWord(Drupal.titleMaxCharsNumber);
	           titleURL = helper.reFormatStringForURL(title, Drupal.titleMaxCharsNumber);
	           
	           // CREATE DESCRIPTION FOR CONTENT:
	           description = helper.randomEnglishText(helper.randomInt((Drupal.descriptionMaxCharsNumber - 30), Drupal.descriptionMaxCharsNumber));
	           
	           // CREATE CONTENT WITH BOTH AGES SELECTED:
// helper.createCustomBrand(driver, title, description, true, true, true, true, new Exception().getStackTrace()[0]);
	           helper.createCustomBrand(driver, titleURL, description, true, true, true, true, new Exception().getStackTrace()[0],
	        		                    "bubble.jpg", "hero.jpg", "small.jpg", "large.jpg", "Age Landing Page"
	        		                    );
	           
	           // LINK GENERIC XPATH:
	           xpath = "//a[contains(@href,'" + titleURL +  Common.XpathContainsEnd;
	           helper.fileWriterPrinter("\n" + "LINK GENERIC XPATH = " + xpath + "\n");
	           
	           // AGE 5 AND UNDER TEST:
	           helper.fileWriterPrinter("\n" + "AGE 5 AND UNDER TEST:");
	           helper.getUrlWaitUntil(driver, 10, Common.fiveAndUnderURL);
	           helper.assertWebElementExist(driver, new Exception().getStackTrace()[0], xpath);
	           // ASSERT ALERNATE IS NOT SHOWN:
	           alternate = Common.XpathContainsStart + Drupal.alternateSmallText + Common.XpathContainsEnd;
               helper.assertWebElementNotExist(driver, new Exception().getStackTrace()[0], alternate);
               alternate = Common.XpathContainsStart + Drupal.alternateLargeText + Common.XpathContainsEnd;
               helper.assertWebElementNotExist(driver, new Exception().getStackTrace()[0], alternate);
	           
	           // AGE 6 AND OVER TEST:
	           helper.fileWriterPrinter("\n" + "AGE 6 AND OVER TEST:");
	           helper.getUrlWaitUntil(driver, 10, Common.sixAndOverURL);
	           helper.assertWebElementExist(driver, new Exception().getStackTrace()[0], xpath);
	           // ASSERT ALERNATE IS NOT SHOWN:
	           alternate = Common.XpathContainsStart + Drupal.alternateSmallText + Common.XpathContainsEnd;
               helper.assertWebElementNotExist(driver, new Exception().getStackTrace()[0], alternate);
               alternate = Common.XpathContainsStart + Drupal.alternateLargeText + Common.XpathContainsEnd;
               helper.assertWebElementNotExist(driver, new Exception().getStackTrace()[0], alternate);
	           
	           } catch(Exception e) { helper.getExceptionDescriptive(e, new Exception().getStackTrace()[0], driver); }
	       }
	
	/**
	 * Test create Brand Tile and check the Small Tile image is mandatory
	 * <p>Date Created: 2016-08-29</p>
	 * <p>Date Modified: 2016-08-29</p>
	 * <p>Original Version: V1</p>
	 * <p>Modified Version: </p>
	 * <p>Xpath: 1</p>
	 * <p>Test Cases: 35220 3188 3996 707</p>
	 */
	@Test(groups = {"TC-35220","US-3188","US-3996","BUG-707","CLOSED"}, priority = 28)
    public void testBrandPageSmallTileImageIsMandatory() throws IOException, IllegalArgumentException, MalformedURLException {
	       try{
	    	   // INITIALISATION:
	           helper.printXmlPath(new RuntimeException().getStackTrace()[0]);
	           driver = helper.getServerName(driver);
	           
	           // LOGIN TO DRUPAL AS CONTENT-EDITOR:
	           helper.logIn(driver,"content_editor","changeme");
	           
	           // DECLARATION:
	           String title, titleURL, description, actual, expected;
	           
	           // CREATE TITLE FOR CONTENT:
	           long fingerprint = System.currentTimeMillis();
	           title = String.valueOf(fingerprint) + " " +  helper.randomWord(Drupal.titleMaxCharsNumber);
	           titleURL = helper.reFormatStringForURL(title, Drupal.titleMaxCharsNumber);
	           
	           // CREATE DESCRIPTION FOR CONTENT:
	           description = helper.randomEnglishText(helper.randomInt((Drupal.descriptionMaxCharsNumber - 30), Drupal.descriptionMaxCharsNumber));
	           
	           // CREATE CONTENT WITH BOTH AGES SELECTED:	           
	           helper.createCustomBrand(driver, titleURL, description, true, true, true, new RuntimeException().getStackTrace()[0], "bubble.jpg", "hero.jpg", "", "");
	           
	           // ASSERT ERROR MESSAGE APPEARS:
	           helper.assertWebElementExist(driver, new RuntimeException().getStackTrace()[0], Drupal.errorMessage);
	           actual = driver.findElement(By.xpath( Drupal.error)).getText();
	           expected = Drupal.errorMessageSmallTileImageRequired;
	           helper.assertEquals(driver, new RuntimeException().getStackTrace()[0], actual, expected);

	           } catch(Exception e) { helper.getExceptionDescriptive(e, new Exception().getStackTrace()[0], driver); }
	       }

	/**
	 * Test create Brand Tile and check the Small Tile image upload larger than 75 KB not allowed
	 * <p>Date Created: 2016-08-29</p>
	 * <p>Date Modified: 2016-08-29</p>
	 * <p>Original Version: V1</p>
	 * <p>Modified Version: </p>
	 * <p>Xpath: 1</p>
	 * <p>Test Cases: 35220 3188 3996</p>
	 */
	@Test(groups = {"TC-35220","US-3188","US-3996"}, priority = 29)
    public void testBrandPageSmallTileImageUploadLargerThenMaxSizeNotAllowed() throws IOException, IllegalArgumentException, MalformedURLException {
	       try{
	    	   // INITIALISATION:
	           helper.printXmlPath(new RuntimeException().getStackTrace()[0]);
	           driver = helper.getServerName(driver);
	           
	           // LOGIN TO DRUPAL AS CONTENT-EDITOR:
	           helper.logIn(driver,"content_editor","changeme");
	           
	           // DECLARATION:
	           String title, titleURL, description, actual, expected, image = "small more then 75Kb.jpg";
	           
	           // CREATE TITLE FOR CONTENT:
	           long fingerprint = System.currentTimeMillis();
	           title = String.valueOf(fingerprint) + " " +  helper.randomWord(Drupal.titleMaxCharsNumber);
	           titleURL = helper.reFormatStringForURL(title, Drupal.titleMaxCharsNumber);
	           
	           // CREATE DESCRIPTION FOR CONTENT:
	           description = helper.randomEnglishText(helper.randomInt((Drupal.descriptionMaxCharsNumber - 30), Drupal.descriptionMaxCharsNumber));
	           
	           // CREATE CONTENT WITH BOTH AGES SELECTED:	           
	           helper.createCustomBrand(driver, titleURL, description, true, true, false, new RuntimeException().getStackTrace()[0], "bubble.jpg", "hero.jpg", image, "");
	           
	           // ASSERT ERROR MESSAGE APPEARS (MORE THEN MAXIMUM SIZE IMAGE NOT ALLOWED):   
	           helper.assertWebElementExist(driver, new RuntimeException().getStackTrace()[0], Drupal.errorUpload);
	           actual = driver.findElement(By.xpath( Drupal.errorUpload)).getText();
	           expected = helper.getFileSpaceSize(Common.localImageDir + File.separator + image, "Kbit", 2);
	           expected = Drupal.errorMessageSmallTileWrongUploadSize(image, expected);
	           helper.assertEquals(driver, new RuntimeException().getStackTrace()[0], actual, expected);

	           } catch(Exception e) { helper.getExceptionDescriptive(e, new Exception().getStackTrace()[0], driver); }
	       }
	
	/**
	 * Test create Brand Tile and check the Small Tile image upload only jpg and jpeg and png files are allowed
	 * <p>Date Created: 2016-08-24</p>
	 * <p>Date Modified: 2016-08-24</p>
	 * <p>Original Version: V1</p>
	 * <p>Modified Version: </p>
	 * <p>Xpath: 1</p>
	 * <p>Test Cases: 35220 3188 3996</p>
	 */
	@Test(groups = {"TC-35220","US-3188","US-3996"}, priority = 30)
    public void testBrandPageSmallTileImageUploadOnlyJpgJpegPngFilesAllowed() throws IOException, IllegalArgumentException, MalformedURLException {
	       try{
	    	   // INITIALISATION:
	           helper.printXmlPath(new RuntimeException().getStackTrace()[0]);
	           driver = helper.getServerName(driver);
	           
	           // LOGIN TO DRUPAL AS CONTENT-EDITOR:
	           helper.logIn(driver,"content_editor","changeme");
	           
	           // DECLARATION:
	           String actual, expected;
	           String browse = Drupal.tileSmallBrowse, error = Drupal.errorBrowse; 
			   String imageDir = Common.localImageDir, imagePath, image = "small 708x398.gif";
			   
	           // CREATE CONTENT WITH BOTH AGES SELECTED:
	           helper.getUrlWaitUntil(driver, 15, Drupal.customBrand);
	           driver.findElement(By.xpath(Drupal.tileVerticalTab)).click();
	           
			   // ASSERT GIF FILE NOT ALLOWED:
			   imagePath = imageDir + File.separator + image;			   
		       driver.findElement(By.xpath(browse)).sendKeys(imagePath);
		       actual = driver.findElement(By.xpath(error)).getText();
		       expected = Drupal.errorMessageSmallTileWrongImageFormat(image);
		       helper.assertEquals(driver, new RuntimeException().getStackTrace()[0], actual, expected);		       

	           // ASSERT JPG FILE IS ALLOWED:
		       driver.navigate().refresh();
		       helper.waitUntilElementPresence(driver, 10, By.id(Drupal.title), "Title", new Exception().getStackTrace()[0]);
	           imagePath = imageDir + File.separator + "small.jpg";
			   error     = Drupal.errorBrowse;
		       driver.findElement(By.xpath(browse)).sendKeys(imagePath);
	           helper.assertWebElementNotExist(driver,  new Exception().getStackTrace()[0], error);  

	           } catch(Exception e) { helper.getExceptionDescriptive(e, new Exception().getStackTrace()[0], driver); }
	       }
	
	/**
	 * Test create Brand Tile and check the Small Tile image upload not exact dimensions not allowed
	 * <p>Date Created: 2016-08-29</p>
	 * <p>Date Modified: 2016-08-29</p>
	 * <p>Original Version: V1</p>
	 * <p>Modified Version: </p>
	 * <p>Xpath: 1</p>
	 * <p>Test Cases: 35220 3188 3996 699</p>
	 */
	@Test(groups = {"TC-35220","US-3188","US-3996","BUG-699","OPEN"}, enabled = true, priority = 31)
    public void testBrandPageSmallTileImageUploadNotExactDimensionsNotAllowed() throws IOException, IllegalArgumentException, MalformedURLException {
	       try{
	    	   // INITIALISATION:
	           helper.printXmlPath(new RuntimeException().getStackTrace()[0]);
	           driver = helper.getServerName(driver);
	           
	           // LOGIN TO DRUPAL AS CONTENT-EDITOR:
	           helper.logIn(driver,"content_editor","changeme");
	           
	           // DECLARATION:
	           String title, titleURL, description, actual, expected;
	           String browse = Drupal.tileSmallBrowse, upload = Drupal.tileSmallUpload, remove = Drupal.tileSmallRemove;
			   String imageDir = Common.localImageDir, imagePath;
			   String image = "small 707x397.jpg", imageMin = "small 708x398.jpg", imageOverMin = "small 709x399.jpg";
	           
	           
	           // CREATE TITLE FOR CONTENT:
	           long fingerprint = System.currentTimeMillis();
	           title = String.valueOf(fingerprint) + " " +  helper.randomWord(Drupal.titleMaxCharsNumber);
	           titleURL = helper.reFormatStringForURL(title, Drupal.titleMaxCharsNumber);
	           
	           // CREATE DESCRIPTION FOR CONTENT:
	           description = helper.randomEnglishText(helper.randomInt((Drupal.descriptionMaxCharsNumber - 30), Drupal.descriptionMaxCharsNumber));
	           
	           // CREATE CONTENT WITH BOTH AGES SELECTED:	           
	           helper.createCustomBrand(driver, titleURL, description, true, true, false, new RuntimeException().getStackTrace()[0], "bubble.jpg", "hero.jpg", image, "");
	           
	           // ASSERT LESS THEN MINIMUM DIMENSIONS IMAGE NOT ALLOWED:
	           helper.assertWebElementExist(driver, new RuntimeException().getStackTrace()[0], Drupal.errorUpload);
	           actual = driver.findElement(By.xpath( Drupal.errorUpload)).getText();
	           expected = Drupal.errorMessageSmallTileWrongImagePixels(image);
	           helper.assertEquals(driver, new RuntimeException().getStackTrace()[0], actual, expected);
		       		       
	           // ASSERT MINIMUM DIMENSIONS IMAGE IS ALLOWED:
	           driver.navigate().refresh();
	           helper.waitUntilElementPresence(driver, 10, By.id(Drupal.title), "Title", new Exception().getStackTrace()[0]);
	           driver.findElement(By.xpath(Drupal.tileVerticalTab)).click();
	           imagePath = imageDir + File.separator + imageMin;
	           driver.findElement(By.xpath(browse)).sendKeys(imagePath);
	           Thread.sleep(1000);
	           helper.ajaxProtectedClick(driver, upload, "Upload", true, Common.ajaxThrobber, true, 5, false);	       
	           helper.assertWebElementNotExist(driver,  new Exception().getStackTrace()[0], Drupal.errorUpload);

	           // ASSERT MORE THEN MINIMUM DIMENSIONS IMAGE IS NOT ALLOWED:
	           driver.findElement(By.xpath(remove)).click();
	           helper.waitUntilElementInvisibility(driver, 10, Common.ajaxThrobber, "Throbber", new Exception().getStackTrace()[0]);
	           imagePath = imageDir + File.separator + imageOverMin;
	           driver.findElement(By.xpath(browse)).sendKeys(imagePath);
	           Thread.sleep(1000);
	           helper.ajaxProtectedClick(driver, upload, "Upload", true, Common.ajaxThrobber, true, 5, false);		       
	           helper.assertWebElementNotExist(driver,  new Exception().getStackTrace()[0], Drupal.errorUpload);

	           } catch(Exception e) { helper.getExceptionDescriptive(e, new Exception().getStackTrace()[0], driver); }
	       }
	
	/**
	 * Test create Brand Tile and check the Large Tile image upload larger than 75 KB not allowed
	 * <p>Date Created: 2016-08-29</p>
	 * <p>Date Modified: 2016-08-29</p>
	 * <p>Original Version: V1</p>
	 * <p>Modified Version: </p>
	 * <p>Xpath: 1</p>
	 * <p>Test Cases: 35220 3188 3996</p>
	 */
	@Test(groups = {"TC-35220","US-3188","US-3996"}, priority = 32)
    public void testBrandPageLargeTileImageUploadLargerThenMaxSizeNotAllowed() throws IOException, IllegalArgumentException, MalformedURLException {
	       try{
	    	   // INITIALISATION:
	           helper.printXmlPath(new RuntimeException().getStackTrace()[0]);
	           driver = helper.getServerName(driver);
	           
	           // LOGIN TO DRUPAL AS CONTENT-EDITOR:
	           helper.logIn(driver,"content_editor","changeme");
	           
	           // DECLARATION:
	           String title, titleURL, description, actual, expected, image = "large more then 100Kb.jpg";
	           
	           // CREATE TITLE FOR CONTENT:
	           long fingerprint = System.currentTimeMillis();
	           title = String.valueOf(fingerprint) + " " +  helper.randomWord(Drupal.titleMaxCharsNumber);
	           titleURL = helper.reFormatStringForURL(title, Drupal.titleMaxCharsNumber);
	           
	           // CREATE DESCRIPTION FOR CONTENT:
	           description = helper.randomEnglishText(helper.randomInt((Drupal.descriptionMaxCharsNumber - 30), Drupal.descriptionMaxCharsNumber));
	           
	           // CREATE CONTENT WITH BOTH AGES SELECTED:	           
	           helper.createCustomBrand(driver, titleURL, description, true, true, false, new RuntimeException().getStackTrace()[0], "bubble.jpg", "hero.jpg", "small.jpg", image);
	           
	           // ASSERT ERROR MESSAGE APPEARS (MORE THEN MAXIMUM SIZE IMAGE NOT ALLOWED):   
	           helper.assertWebElementExist(driver, new RuntimeException().getStackTrace()[0], Drupal.errorUpload);
	           actual = driver.findElement(By.xpath( Drupal.errorUpload)).getText();
	           expected = helper.getFileSpaceSize(Common.localImageDir + File.separator + image, "Kbit", 2);
	           expected = Drupal.errorMessageLargeTileWrongUploadSize(image, expected);
	           helper.assertEquals(driver, new RuntimeException().getStackTrace()[0], actual, expected);

	           } catch(Exception e) { helper.getExceptionDescriptive(e, new Exception().getStackTrace()[0], driver); }
	       }
	
	/**
	 * Test create Brand Tile and check the Large Tile image upload only jpg and jpeg and png files are allowed
	 * <p>Date Created: 2016-08-24</p>
	 * <p>Date Modified: 2016-08-24</p>
	 * <p>Original Version: V1</p>
	 * <p>Modified Version: </p>
	 * <p>Xpath: 1</p>
	 * <p>Test Cases: 35220 3188 3996</p>
	 */
	@Test(groups = {"TC-35220","US-3188","US-3996"}, priority = 33)
    public void testBrandPageLargeTileImageUploadOnlyJpgJpegPngFilesAllowed() throws IOException, IllegalArgumentException, MalformedURLException {
	       try{
	    	   // INITIALISATION:
	           helper.printXmlPath(new RuntimeException().getStackTrace()[0]);
	           driver = helper.getServerName(driver);
	           
	           // LOGIN TO DRUPAL AS CONTENT-EDITOR:
	           helper.logIn(driver,"content_editor","changeme");
	           
	           // DECLARATION:
	           String actual, expected;
	           String browse = Drupal.tileLargeBrowse, error = Drupal.errorBrowse; 
			   String imageDir = Common.localImageDir, imagePath, image = "large 708x836.gif";
			   
	           // CREATE CONTENT WITH BOTH AGES SELECTED:
	           helper.getUrlWaitUntil(driver, 15, Drupal.customBrand);
	           driver.findElement(By.xpath(Drupal.tileVerticalTab)).click();
	           
			   // ASSERT GIF FILE NOT ALLOWED:
			   imagePath = imageDir + File.separator + image;			   
		       driver.findElement(By.xpath(browse)).sendKeys(imagePath);
		       actual = driver.findElement(By.xpath(error)).getText();
		       expected = Drupal.errorMessageLargeTileWrongImageFormat(image);
		       helper.assertEquals(driver, new RuntimeException().getStackTrace()[0], actual, expected);		       

	           // ASSERT JPG FILE IS ALLOWED:
		       driver.navigate().refresh();
		       helper.waitUntilElementPresence(driver, 10, By.id(Drupal.title), "Title", new Exception().getStackTrace()[0]);
	           imagePath = imageDir + File.separator + "large.jpg";
			   error     = Drupal.errorBrowse;
		       driver.findElement(By.xpath(browse)).sendKeys(imagePath);
	           helper.assertWebElementNotExist(driver,  new Exception().getStackTrace()[0], error);  

	           } catch(Exception e) { helper.getExceptionDescriptive(e, new Exception().getStackTrace()[0], driver); }
	       }
	
	/**
	 * Test create Brand Tile and check the Large Tile image upload not exact dimensions not allowed
	 * <p>Date Created: 2016-08-29</p>
	 * <p>Date Modified: 2016-08-29</p>
	 * <p>Original Version: V1</p>
	 * <p>Modified Version: </p>
	 * <p>Xpath: 1</p>
	 * <p>Test Cases: 35220 3188 3996 708</p>
	 */
	@Test(groups = {"TC-35220","US-3188","US-3996","BUG-708","NEW"}, enabled = true, priority = 34)
    public void testBrandPageLargeTileImageUploadNotExactDimensionsNotAllowed() throws IOException, IllegalArgumentException, MalformedURLException {
	       try{
	    	   // INITIALISATION:
	           helper.printXmlPath(new RuntimeException().getStackTrace()[0]);
	           driver = helper.getServerName(driver);
	           
	           // LOGIN TO DRUPAL AS CONTENT-EDITOR:
	           helper.logIn(driver,"content_editor","changeme");
	           
	           // DECLARATION:
	           String title, titleURL, description, actual, expected;
	           String browse = Drupal.tileLargeBrowse, upload = Drupal.tileLargeUpload, remove = Drupal.tileLargeRemove;
			   String imageDir = Common.localImageDir, imagePath;
			   String image = "large 707x835.jpg", imageMin = "large 708x836.jpg", imageOverMin = "large 709x837.jpg";
	            
	           // CREATE TITLE FOR CONTENT:
	           long fingerprint = System.currentTimeMillis();
	           title = String.valueOf(fingerprint) + " " +  helper.randomWord(Drupal.titleMaxCharsNumber);
	           titleURL = helper.reFormatStringForURL(title, Drupal.titleMaxCharsNumber);
	           
	           // CREATE DESCRIPTION FOR CONTENT:
	           description = helper.randomEnglishText(helper.randomInt((Drupal.descriptionMaxCharsNumber - 30), Drupal.descriptionMaxCharsNumber));
	           
	           // CREATE CONTENT WITH BOTH AGES SELECTED:	           
	           helper.createCustomBrand(driver, titleURL, description, true, true, false, new RuntimeException().getStackTrace()[0], "bubble.jpg", "hero.jpg", "small.jpg", image);
	           
	           // ASSERT LESS THEN MINIMUM DIMENSIONS IMAGE NOT ALLOWED:
	           helper.assertWebElementExist(driver, new RuntimeException().getStackTrace()[0], Drupal.errorUpload);
	           actual = driver.findElement(By.xpath( Drupal.errorUpload)).getText();
	           expected = Drupal.errorMessageLargeTileWrongImagePixels(image);
	           helper.assertEquals(driver, new RuntimeException().getStackTrace()[0], actual, expected);
		       		       
	           // ASSERT MINIMUM DIMENSIONS IMAGE IS ALLOWED:
	           driver.navigate().refresh();
	           helper.waitUntilElementPresence(driver, 10, By.id(Drupal.title), "Title", new Exception().getStackTrace()[0]);
	           driver.findElement(By.xpath(Drupal.tileVerticalTab)).click();
	           imagePath = imageDir + File.separator + imageMin;
	           driver.findElement(By.xpath(browse)).sendKeys(imagePath);
	           Thread.sleep(1000);
	           helper.ajaxProtectedClick(driver, upload, "Upload", true, Common.ajaxThrobber, true, 5, false);	       
	           helper.assertWebElementNotExist(driver,  new Exception().getStackTrace()[0], Drupal.errorUpload);

	           // ASSERT MORE THEN MINIMUM DIMENSIONS IMAGE IS NOT ALLOWED:
	           driver.findElement(By.xpath(remove)).click();
	           helper.waitUntilElementInvisibility(driver, 10, Common.ajaxThrobber, "Throbber", new Exception().getStackTrace()[0]);
	           imagePath = imageDir + File.separator + imageOverMin;
	           driver.findElement(By.xpath(browse)).sendKeys(imagePath);
	           Thread.sleep(1000);
	           helper.ajaxProtectedClick(driver, upload, "Upload", true, Common.ajaxThrobber, true, 5, false);
               helper.moveToElement(driver, "//span[@class='fieldset-legend'][text()='Add Tile Placements']");	       
	           helper.assertWebElementNotExist(driver,  new Exception().getStackTrace()[0], Drupal.errorUpload);

	           } catch(Exception e) { helper.getExceptionDescriptive(e, new Exception().getStackTrace()[0], driver); }
	       }
	
	/**
	 * Test the ability to create a custom URL Redirect for a particular Content type - 5 and Under
	 * <p>Date Created: 2016-09-06</p>
	 * <p>Date Modified: 2016-09-06</p>
	 * <p>Original Version: V1</p>
	 * <p>Modified Version: </p>
	 * <p>Xpath: 1</p>
	 * <p>Test Cases: 35158 3610</p>
	 */
	@Test(groups = {"US-35158","US-3610"}, priority = 35)
    public void testUrlRedirectAgeFiveAndUnder() throws IOException, IllegalArgumentException, MalformedURLException {
	       try{
	    	   // INITIALISATION:
	           helper.printXmlPath(new RuntimeException().getStackTrace()[0]);
	           driver = helper.getServerName(driver);
	           
	           // DECLARATION:
	           String redirectURL = "http://www.veoh.com";
	        		   
	           // LOGIN TO DRUPAL AS CONTENT-EDITOR:
	           helper.logIn(driver,"content_editor","changeme");
	           
	           // ADD REEDIRECT:
	           helper.createUrlRedirect(driver, Common.fiveAndUnderURL, redirectURL);
	           
	           // LOG-OUT:
	           helper.logOut(driver);
	           
	           // ASSERT REDIRECT:
	           helper.getUrlWaitUntil(driver, 10, Common.homeURL);
	           helper.clickLinkAndCheckURL(driver, new RuntimeException().getStackTrace()[0], Common.homePageFiveAndUnderTitle, redirectURL, false, false);
	           
	           // DELETE REDIRECT:
	           helper.logIn(driver,"content_editor","changeme");
	           helper.deleteUrlRedirect(driver, redirectURL);
	           helper.logOut(driver);
	           
	           // ASSERT REDIRECT DELETED:
	           helper.getUrlWaitUntil(driver, 10, Common.homeURL);
	           helper.clickLinkAndCheckURL(driver, new RuntimeException().getStackTrace()[0], Common.homePageFiveAndUnderTitle, Common.fiveAndUnderURL, false, false);
	           
	           } catch(Exception e) { helper.getExceptionDescriptive(e, new Exception().getStackTrace()[0], driver); }
	       }
	
	/**
	 * Test the ability to create a custom URL Redirect for a particular Content type - 6 and Over
	 * <p>Date Created: 2016-09-08</p>
	 * <p>Date Modified: 2016-09-08</p>
	 * <p>Original Version: V1</p>
	 * <p>Modified Version: </p>
	 * <p>Xpath: 1</p>
	 * <p>Test Cases: 35160 3610</p>
	 */
	@Test(groups = {"US-35160","US-3610"}, priority = 36)
    public void testUrlRedirectAgeSixAndOver() throws IOException, IllegalArgumentException, MalformedURLException {
	       try{
	    	   // INITIALISATION:
	           helper.printXmlPath(new RuntimeException().getStackTrace()[0]);
	           driver = helper.getServerName(driver);
	           
	           // DECLARATION:
	           String redirectURL = "http://www.veoh.com";
	        		   
	           // LOGIN TO DRUPAL AS CONTENT-EDITOR:
	           helper.logIn(driver,"content_editor","changeme");
	           
	           // ADD REEDIRECT:
	           helper.createUrlRedirect(driver, Common.sixAndOverURL, redirectURL);
	           
	           // LOG-OUT:
	           helper.logOut(driver);
	           
	           // ASSERT REDIRECT:
	           helper.getUrlWaitUntil(driver, 10, Common.homeURL);
	           helper.clickLinkAndCheckURL(driver, new RuntimeException().getStackTrace()[0], Common.homePageSixAndOverTitle, redirectURL, false, false);
	           
	           // DELETE REDIRECT:
	           helper.logIn(driver,"content_editor","changeme");
	           helper.deleteUrlRedirect(driver, redirectURL);
	           helper.logOut(driver);
	           
	           // ASSERT REDIRECT DELETED:
	           helper.getUrlWaitUntil(driver, 10, Common.homeURL);
	           helper.clickLinkAndCheckURL(driver, new RuntimeException().getStackTrace()[0], Common.homePageSixAndOverTitle, Common.sixAndOverURL, false, false);
	           
	           } catch(Exception e) { helper.getExceptionDescriptive(e, new Exception().getStackTrace()[0], driver); }
	       }
	
	/**
	 * Test the ability to create a custom URL Redirect for a particular Content type - Custom Brand Page
	 * <p>Date Created: 2016-09-08</p>
	 * <p>Date Modified: 2016-09-08</p>
	 * <p>Original Version: V1</p>
	 * <p>Modified Version: </p>
	 * <p>Xpath: 1</p>
	 * <p>Test Cases: 35161 3610</p>
	 */
	@Test(groups = {"US-35161","US-3610"}, priority = 37)
    public void testUrlRedirectCustomBrandPage() throws IOException, IllegalArgumentException, MalformedURLException {
	       try{
	    	   // DECLARATION:
	           String title, titleURL, description, xpath, BrandPageUrlAgeFive, BrandPageUrlAgeSix, redirectURL = "http://www.veoh.com";
	    	   
	           // INITIALISATION:
	           helper.printXmlPath(new RuntimeException().getStackTrace()[0]);
	           driver = helper.getServerName(driver);
	           
	           // LOGIN TO DRUPAL AS A CONTENT EDITOR:
	           helper.logIn(driver,"content_editor","changeme");
	           
	           // CLEAN-UP:
	           helper.deleteAllContent(driver, "", "", "dev, content_editor", new RuntimeException().getStackTrace()[0]);
	           helper.deleteUrlRedirect(driver, redirectURL);
	           
	           // NAVIGATE TO A NEW CUSTOM BRAND PAGE:
	           helper.getUrlWaitUntil(driver, 10, Drupal.customBrand);
	           
	           // CREATE TITLE FOR CONTENT:
	           long fingerprint = System.currentTimeMillis();
	           title = String.valueOf(fingerprint) + " " +  helper.randomWord(Drupal.titleMaxCharsNumber);
	           titleURL = helper.reFormatStringForURL(title, Drupal.titleMaxCharsNumber);
	           BrandPageUrlAgeFive = Common.fiveAndUnderURL + "/" + titleURL;
	           BrandPageUrlAgeSix  = Common.sixAndOverURL + "/" + titleURL;
	           
	           // CREATE DESCRIPTION FOR CONTENT:
	           description = helper.randomEnglishText(helper.randomInt((Drupal.descriptionMaxCharsNumber - 30), Drupal.descriptionMaxCharsNumber));
	           
	           // CREATE CONTENT WITH BOTH AGES SELECTED:
	           helper.createCustomBrand(driver, title, description, true, true, true, new Exception().getStackTrace()[0]);
	           
	           // LINK GENERIC XPATH:
	           xpath = "//a[contains(@href,'" + titleURL +  Common.XpathContainsEnd;
	           helper.fileWriterPrinter("\n" + "LINK GENERIC XPATH = " + xpath + "\n");
	           
	           // ADD REEDIRECTS:
	           helper.createUrlRedirect(driver, BrandPageUrlAgeFive, redirectURL);
	           helper.createUrlRedirect(driver, BrandPageUrlAgeSix,  redirectURL);
	           
	           // AGE 5 AND UNDER REDIRECT TEST:	           
	           helper.fileWriterPrinter("\n" + "AGE 5 AND UNDER REDIRECT TEST:");  
	           helper.getUrlWaitUntil(driver, 15, Common.fiveAndUnderURL);
	           helper.assertWebElementExist(driver, new Exception().getStackTrace()[0], xpath);
	           helper.clickToAppear(driver, Common.charBannerButtonLeft, Common.charBannerButtonRight, xpath, false, false);
	           // ASSERT REDIRECT IS CORRECT:
	           helper.clickLinkAndCheckURL(driver, new RuntimeException().getStackTrace()[0], xpath, redirectURL, false, false);
	           
	           // AGE 6 AND OVER REDIRECT TEST:
	           helper.fileWriterPrinter("\n" + "AGE 6 AND OVER REDIRECT TEST:");	           	           
	           helper.getUrlWaitUntil(driver, 15, Common.sixAndOverURL);
	           helper.assertWebElementExist(driver, new Exception().getStackTrace()[0], xpath);
	           helper.clickToAppear(driver, Common.charBannerButtonLeft, Common.charBannerButtonRight, xpath, false, false);
	           // ASSERT REDIRECT IS CORRECT:
	           helper.clickLinkAndCheckURL(driver, new RuntimeException().getStackTrace()[0], xpath, redirectURL, false, false);
	           
	           // DELETE REDIRECT:
	           helper.deleteUrlRedirect(driver, redirectURL);
	           
	           // AGE 5 AND UNDER REDIRECT DELETED TEST:	           
	           helper.fileWriterPrinter("\n" + "AGE 5 AND UNDER REDIRECT DELETED TEST:");  
	           helper.getUrlWaitUntil(driver, 15, Common.fiveAndUnderURL);
	           helper.assertWebElementExist(driver, new Exception().getStackTrace()[0], xpath);
	           helper.clickToAppear(driver, Common.charBannerButtonLeft, Common.charBannerButtonRight, xpath, false, false);
	           // ASSERT REDIRECT DELETED:
	           helper.clickLinkAndCheckURL(driver, new RuntimeException().getStackTrace()[0], xpath, BrandPageUrlAgeFive, false, false);
	           
	           // AGE 6 AND OVER REDIRECT DELETED TEST:
	           helper.fileWriterPrinter("\n" + "AGE 6 AND OVER REDIRECT DELETED TEST:");
	           helper.getUrlWaitUntil(driver, 15, Common.sixAndOverURL);
	           helper.assertWebElementExist(driver, new Exception().getStackTrace()[0], xpath);
	           helper.clickToAppear(driver, Common.charBannerButtonLeft, Common.charBannerButtonRight, xpath, false, false);
	           // ASSERT REDIRECT DELETED:
	           helper.clickLinkAndCheckURL(driver, new RuntimeException().getStackTrace()[0], xpath, BrandPageUrlAgeSix, false, false);
	           
	           } catch(Exception e) { helper.getExceptionDescriptive(e, new Exception().getStackTrace()[0], driver); }
	       }
	
	/**
	 * Test sort Tiles on the reorder interface
	 * <p>Date Created: 2016-09-09</p>
	 * <p>Date Modified: 2016-09-09</p>
	 * <p>Original Version: V1</p>
	 * <p>Modified Version: </p>
	 * <p>Xpath: 1</p>
	 * <p>Test Cases: 35404 3522 3202</p>
	 */
	@Test(groups = {"US-35404","US-3522","US-3202"}, priority = 38)
    public void testSortTilesOnReorderInterface() throws IOException, IllegalArgumentException, MalformedURLException {
	       try{
	           
	           } catch(Exception e) { helper.getExceptionDescriptive(e, new Exception().getStackTrace()[0], driver); }

	       }
	
}