package com.tvokids.test.plan.Iteration3;

import java.awt.AWTException;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
//import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
//import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
//import org.openqa.selenium.support.ui.Select;
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
import com.tvokids.utilities.*;

@SuppressWarnings("static-access")
public class BrandPage {
	static WebDriver driver;
	UtilitiesTestHelper helper = new UtilitiesTestHelper();

    @BeforeMethod public static void startTime(Method method) throws IOException { new UtilitiesTestHelper().startTime(method); }   
    @AfterMethod  public static void endTime() throws IOException { new UtilitiesTestHelper().endTime(); }
    @AfterMethod  @AfterClass   public static void closeBrowsers() { driver.quit(); }

	/**
	 * Test sort Tiles on the reorder interface
	 * <p>Date Created: 2016-09-09</p>
	 * <p>Date Modified: 2016-09-09</p>
	 * <p>Original Version: V1</p>
	 * <p>Modified Version: </p>
	 * <p>Xpath: 1</p>
	 * <p>Test Cases: 35404 3522 3202</p>
	 * @throws InterruptedException 
	 * @throws AWTException 
	 */
	@Test(groups = {"TC-35404","US-3522","US-3202"}, enabled = false, priority = 38)
    public void testSortTilesOnReorderInterface() throws IOException, IllegalArgumentException, MalformedURLException, InterruptedException, AWTException {
	   try{
    	   // INITIALISATION:
           helper.printXmlPath(new RuntimeException().getStackTrace()[0]);
           driver = helper.getServerName(driver);
           
           // LOGIN TO DRUPAL AS A CONTENT EDITOR:
           helper.logIn(driver,"content_editor","changeme");
           
           // CLEAN-UP:
           helper.deleteAllContent(driver, "", "", "dev, content_editor", new RuntimeException().getStackTrace()[0]);
           
           // DECLARATION:
           int total = 3;
           String[] title = new String[total];
           String[] titleURL = new String[total];
           String[] description = new String[total];
           String[] xpath = new String[total];
           String[] tileXpath = new String[total];
           long[] fingerprint = new long[total];
           String tile = "";
           
           for (int i = 0; i < total; i++) {
        	   // CREATE TITLES FOR CONTENTS:
        	   fingerprint[i] = System.currentTimeMillis();
        	   title[i] = String.valueOf(fingerprint[i]) + " " +  helper.randomWord(Drupal.titleMaxCharsNumber);
        	   title[i] = helper.getStringBeginning(title[i], Drupal.titleMaxCharsNumber);
        	   titleURL[i] = helper.reFormatStringForURL(title[i], Drupal.titleMaxCharsNumber);
        	   // CREATE DESCRIPTION FOR CONTENT:
        	   description[i] = helper.randomEnglishText(helper.randomInt((Drupal.descriptionMaxCharsNumber - 30), Drupal.descriptionMaxCharsNumber));
        	   // LINK GENERIC XPATH:
	           xpath[i] = "//a[contains(@href,'" + titleURL[i] +  Common.XpathContainsEnd;
	           helper.fileWriterPrinter("\n\n" + "LINK GENERIC XPATH = " + xpath[i]);
    		   // CREATE A CUSTOM BRAND CONTENT WITH BOTH AGES SELECTED:
	           if(i > 0) { tile = title[0]; }
    		   helper.createCustomBrand(driver, title[i], description[i], true, true, true, true, true, new RuntimeException().getStackTrace()[0],
    				                   "bubble.jpg", "hero.jpg", "small.jpg", "", tile, true
    				                   );
    		   tileXpath[i] = Common.TextEntireToXpath(title[i]) + "/ancestor::a";
    		   helper.fileWriterPrinter("\n" + (i + 1) + " OF " + total + ": CREATED!\n  TYPE: CUSTOM BRAND\n TITLE: " + title[i] + "\n  TILE: " + tile + "\n");    
    		   }
       
           // AGE 5 AND UNDER SORTED AS-IS TEST:
           helper.fileWriterPrinter("\n" + "AGE 5 AND UNDER REDIRECT TEST:");  
           helper.getUrlWaitUntil(driver, 15, Common.fiveAndUnderURL);
//         helper.assertWebElementExist(driver, new Exception().getStackTrace()[0], xpath[0]);
//         Thread.sleep(1000);
           helper.clickToAppear(driver, Common.charBannerButtonLeft, Common.charBannerButtonRight, xpath[0], false, false);	           
           // NAVIGATE TO BRAND PAGE:
           helper.clickLinkUrlWaitUntil(driver, 15, xpath[0], new Exception().getStackTrace()[0]);
           // ASSERT AS-IS TILES SORTING:
           for (int i = total - 1; i > 1; i--) {
        	   int bottomUpper = helper.getElementLocationY(driver, tileXpath[i]) + helper.getElementHeight(driver, tileXpath[i]);
        	   int topOfNext = helper.getElementLocationY(driver, tileXpath[i - 1]) + helper.getElementHeight(driver, tileXpath[i - 1]);
        	   helper.assertBooleanTrue(driver, new Exception().getStackTrace()[0], bottomUpper < topOfNext);
        	   }
           
           // SEND TILES TO SORTED LIST:
           helper.sendTilesToSortedList(driver, "", tile, "", true, true, new RuntimeException().getStackTrace()[0]);
           // NAVIGATE TO SORTING LIST:
           helper.sendTilesToUnSortedList(driver, "", tile, false, new RuntimeException().getStackTrace()[0]);
           // DRAG UP:
           for (int i = total - 1; i > 1; i--) {
//        	   String topXpath = Drupal.reorderTileHandle(i - 1);
        	   String bottomXpath = Drupal.reorderTileHandle(i);
        	   helper.fileWriterPrinter(bottomXpath);
        	   
//               int dragUP = helper.getElementLocationY(driver, bottomXpath) - helper.getElementLocationY(driver, 
//            		   Drupal.selectAllCheckBox
//            		   );
//               helper.fileWriterPrinter("DRUGGING Y CALCULATED = " + dragUP); 
//               helper.dragAndDrop(driver, bottomXpath, 0, -300, true, 500);
               
               driver.manage().timeouts().implicitlyWait(10000, TimeUnit.MILLISECONDS);
               WebElement From = driver.findElement(By.xpath(bottomXpath)); 
//             WebElement To = driver.findElement(By.xpath(Drupal.selectAllCheckBox));
               
               Actions builder = new Actions(driver);
//             Action dragAndDrop = builder.moveToElement(From).clickAndHold(From).moveToElement(To).release(To).build();
//             dragAndDrop.perform(); Thread.sleep(1000);
               builder.dragAndDropBy(From, 0, -50); Thread.sleep(1000);
               
               driver.findElement(By.id("edit-save-order")).click();
               Thread.sleep(1000);
               
//               (new Actions(driver)).dragAndDrop(
//            		   driver.findElement(By.xpath(bottomXpath)),
//            		   driver.findElement(By.xpath("//th[@class='views-field views-field-views-bulk-operations']"))
//            		   ).perform();
               }
           
           // AGE 5 AND UNDER TEST RE-SORTED ORDER:
           helper.fileWriterPrinter("\n" + "AGE 5 AND UNDER REDIRECT TEST:");  
           helper.getUrlWaitUntil(driver, 15, Common.fiveAndUnderURL);
//         helper.assertWebElementExist(driver, new Exception().getStackTrace()[0], xpath[0]);
//         Thread.sleep(1000);
           helper.clickToAppear(driver, Common.charBannerButtonLeft, Common.charBannerButtonRight, xpath[0], false, false);	           
           // NAVIGATE TO BRAND PAGE:
           helper.clickLinkUrlWaitUntil(driver, 15, xpath[0], new Exception().getStackTrace()[0]);
           // ASSERT AS-IS TILES SORTING:
           for (int i = 1; i < total - 1; i++) {
        	   int bottomUpper = helper.getElementLocationY(driver, tileXpath[i]) + helper.getElementHeight(driver, tileXpath[i]);
        	   int topOfNext = helper.getElementLocationY(driver, tileXpath[i - 1]) + helper.getElementHeight(driver, tileXpath[i - 1]);
        	   helper.assertBooleanTrue(driver, new Exception().getStackTrace()[0], bottomUpper < topOfNext);
        	   }
           
           
	   } catch(IOException | InterruptedException /*| AWTException*/ e) { helper.getExceptionDescriptive(e, new Exception().getStackTrace()[0], driver); } 
	}
	
    /**
     * Test Age group filters on the "ALL Content" page in Drupal
     * <p>Date Created: 2016-09-12</p>
     * <p>Date Modified: 2016-09-12</p>
     * <p>Original Version: V1</p>
     * <p>Modified Version: </p>
     * <p>Xpath: 1</p>
     * <p>Test Cases: 36101 3758</p>
     */
	@Test(groups = {"TC-36101","US-3758"}, priority = 39)
    public void testAgeGroupFiltersOnAllContentPage() throws IOException, IllegalArgumentException, MalformedURLException {
       try{
    	   // INITIALISATION:
           helper.printXmlPath(new RuntimeException().getStackTrace()[0]);
           driver = helper.getServerName(driver);
           
           // CLEAN-UP:
           helper.logIn(driver);
           helper.deleteAllContent(driver, "", "", "dev, content_editor", new RuntimeException().getStackTrace()[0]);
           helper.logOut(driver);
           
           // DECLARATION:
           int total = 3;
           String[] title = new String[total];
           String[] titleURL = new String[total];
           String[] description = new String[total];
           String[] xpath = new String[total];
           long[] fingerprint = new long[total];
           
           for (int i = 0; i < total; i++) {
        	   // CREATE TITLES FOR CONTENTS:
        	   fingerprint[i] = System.currentTimeMillis();
        	   title[i] = String.valueOf(fingerprint[i]) + " " +  helper.randomWord(Drupal.titleMaxCharsNumber);
               if (i > 0) { title[i] = title[i].substring(0, Drupal.titleMaxCharsNumber); }
        	   titleURL[i] = helper.reFormatStringForURL(title[i], Drupal.titleMaxCharsNumber);
        	   // CREATE DESCRIPTION FOR CONTENT:
        	   description[i] = helper.randomEnglishText(helper.randomInt((Drupal.descriptionMaxCharsNumber - 30), Drupal.descriptionMaxCharsNumber));
        	   // LINK GENERIC XPATH:
	           xpath[i] = "//a[contains(@href,'" + titleURL[i] +  Common.XpathContainsEnd;
	           helper.fileWriterPrinter("\n\n" + "LINK GENERIC XPATH = " + xpath[i]);
        	   
	           if (i == 0) {
	        	// CREATE A CHARACTER BRAND CONTENT WITH BOTH AGES SELECTED:
        		   helper.logIn(driver);  // LOGIN TO DRUPAL AS AN ADMIN
    	           helper.createCharacterBrand(driver, title[i], description[i], 281374, true, true, false, true, true, new Exception().getStackTrace()[0]);
    	           helper.logOut(driver);
    	           helper.fileWriterPrinter("\n" + (i + 1) + " OF " + total + ": CREATED!\n  TYPE: CHARACTER BRAND\n TITLE: " + title[i] + "\n");
	           } else {
	        	   helper.logIn(driver,"content_editor","changeme"); // LOGIN TO DRUPAL AS A CONTENT EDITOR
		           if (helper.isOdd(i)) {
		        	// CREATE A CUSTOM BRAND CONTENT WITH AGE 5 AND UNDER ONLY SELECTED:	        		   
	        		   helper.createCustomBrand(driver, title[i], description[i], true, false, false, true, true, new Exception().getStackTrace()[0]);
	    	           helper.fileWriterPrinter("\n" + (i + 1) + " OF " + total + ": CREATED!\n  TYPE: CHARACTER BRAND\n TITLE: " + title[i] + "\n");
	        	   } else {
	        		   // CREATE A CUSTOM BRAND CONTENT WITH AGE 6 AND OVER ONLY SELECTED:
	        		   helper.createCustomBrand(driver, title[i], description[i], false, true, false, true, true, new Exception().getStackTrace()[0]);
	        		   helper.fileWriterPrinter("\n" + (i + 1) + " OF " + total + ": CREATED!\n  TYPE: CUSTOM BRAND\n TITLE: " + title[i] + "\n");
	        	   }
    	           helper.logOut(driver);
	           }
	           
           }
           
           // LOGIN TO DRUPAL AS A CONTENT EDITOR:
           helper.logIn(driver,"content_editor","changeme");
           driver.manage().window().maximize();
           
           // FILTER BOTH AGES VISIBLE (ANY AGE VISIBLE):
           helper.filterAllContent(driver, "", "", "dev, content_editor", "", true, true, new Exception().getStackTrace()[0]);
           for (int i = 0; i < total; i++) {
        	   helper.assertWebElementExist(driver, new Exception().getStackTrace()[0], Common.ContentEntireToXpath(title[i]));
           }
           
           // FILTER AGE 5 AND UNDER ONLY VISIBLE:
           helper.filterAllContent(driver, "", "", "dev, content_editor", "", true, false, new Exception().getStackTrace()[0]);
           for (int i = 0; i < total; i++) {
        	   if(i == 0) { helper.assertWebElementExist(driver, new Exception().getStackTrace()[0], Common.ContentEntireToXpath(title[i])); }
        	   else {
        		   if (helper.isOdd(i)) { helper.assertWebElementExist(driver, new Exception().getStackTrace()[0], Common.ContentEntireToXpath(title[i])); }
        		   else { helper.assertWebElementNotExist(driver, new Exception().getStackTrace()[0], Common.ContentEntireToXpath(title[i])); }
        	   }
           }
           
           // FILTER AGE 6 AND OVER ONLY VISIBLE:
           helper.filterAllContent(driver, "", "", "dev, content_editor", "", false, true, new Exception().getStackTrace()[0]);
           for (int i = 0; i < total; i++) {
        	   if(i == 0) { helper.assertWebElementExist(driver, new Exception().getStackTrace()[0], Common.ContentEntireToXpath(title[i])); }
        	   else {
        		   if (helper.isOdd(i)) { helper.assertWebElementNotExist(driver, new Exception().getStackTrace()[0], Common.ContentEntireToXpath(title[i])); }
        		   else { helper.assertWebElementExist(driver, new Exception().getStackTrace()[0], Common.ContentEntireToXpath(title[i])); }
        	   }
           }
    	  
           } catch(Exception e) { helper.getExceptionDescriptive(e, new Exception().getStackTrace()[0], driver); }
       }
	
	/**
	 * Test New size of Tile Image
	 * <p>Date Created: 2016-09-13</p>
	 * <p>Date Modified: 2016-09-13</p>
	 * <p>Original Version: V1</p>
	 * <p>Modified Version: </p>
	 * <p>Xpath: 1</p>
	 * <p>Test Cases: 36113 3996</p>
	 */
	@Test(groups = {"TC-36113","US-3996"}, priority = 41)
	public void testNewSizeOfTileImageDescription() throws IOException, IllegalArgumentException, MalformedURLException {
	       try{
	    	   // DECLARATION:
	    	   String expected, actual;
	    	   
	    	   // INITIALISATION:
	           helper.printXmlPath(new RuntimeException().getStackTrace()[0]);
	           driver = helper.getServerName(driver);
	           
	           // LOGIN TO DRUPAL AS CONTENT-EDITOR:
	           helper.logIn(driver,"content_editor","changeme");
	           
	           // NAVIGATE TO CONTENT ADD CUSTOM BRAND:
		       helper.getUrlWaitUntil(driver, 10, Drupal.customBrand);
		       
		       // CLICK ON TILE:
		       helper.ajaxProtectedClick(driver, Drupal.tileVerticalTab, "Tile Vertical Tab", false, "", true, false);
		       
		       // ASSERT SMALL TITLE IMAGE SIZE DESCRIPTION:
		       actual = driver.findElement(By.xpath(Drupal.tileSmallDescription)).getText();
		       actual = helper.getTextLine(actual, 1);
		       expected = Drupal.tileSmallDescriptionOfSize;
		       helper.assertEquals(driver, new Exception().getStackTrace()[0], actual, expected);
		    
		       // ASSERT LARGE TITLE IMAGE SIZE DESCRIPTION:
		       actual = driver.findElement(By.xpath(Drupal.tileLargeDescription)).getText();
		       actual = helper.getTextLine(actual, 1);
		       expected = Drupal.tileLargeDescriptionOfSize;
		       helper.assertEquals(driver, new Exception().getStackTrace()[0], actual, expected);
		       
	       } catch(Exception e) { helper.getExceptionDescriptive(e, new Exception().getStackTrace()[0], driver); }
	   }
	
	/**
	 * Test New size of Character bubble Image ()
	 * <p>Date Created: 2016-09-14</p>
	 * <p>Date Modified: 2016-09-14</p>
	 * <p>Original Version: V1</p>
	 * <p>Modified Version: </p>
	 * <p>Xpath: 1</p>
	 * <p>Test Cases: 36114 3997 699</p>
	 */
	@Test(groups = {"TC-36114","US-3997","BUG-699","OPEN"}, enabled = true, priority = 42)
	public void testNewSizeOfCharacterBubbleImageDescription() throws IOException, IllegalArgumentException, MalformedURLException {
	       try{
	    	   // DECLARATION:
	    	   String expected, actual, image;
	    	   
	    	   // INITIALISATION:
	           helper.printXmlPath(new RuntimeException().getStackTrace()[0]);
	           driver = helper.getServerName(driver);
	           
	           // LOGIN TO DRUPAL AS CONTENT-EDITOR:
	           helper.logIn(driver,"content_editor","changeme");
	           
	           // NAVIGATE TO CONTENT ADD CUSTOM BRAND:
		       helper.getUrlWaitUntil(driver, 10, Drupal.customBrand);
		       
		       // CLICK ON CHARACTER BANNER:
		       helper.ajaxProtectedClick(driver, Drupal.characterBannerVerticalTab, "Character Banner Vertical Tab", false, "", true, false);
		       
		       // ASSERT CHARACTER BANNER IMAGE SIZE DESCRIPTION:
		       actual = driver.findElement(By.xpath(Drupal.characterBannerDescription)).getText();
		       actual = helper.getTextLine(actual, 1);
		       expected = Drupal.characterBannerDescriptionOfSize;
		       helper.assertEquals(driver, new Exception().getStackTrace()[0], actual, expected);
		       
		       // ASSERT CHARACTER BANNER IMAGE IS ACCEPTED IF RESTRICTED BY SIZE, FORMAT AND DIMENSIONS:
		       image = "bubble.jpg";
	           helper.createCustomBrand(driver, "", "", false, false, false, false, new Exception().getStackTrace()[0], image, "", "", "");
	           helper.assertWebElementNotExist(driver, new Exception().getStackTrace()[0], Drupal.characterBannerUpload);
	           helper.assertWebElementExist(driver, new Exception().getStackTrace()[0], Drupal.characterBannerRemove);
	           
		       // ASSERT CHARACTER BANNER IMAGE NOT AS RESTRICTED SIZE IS REJECTED:
	           image = "bubble more then 25Kb.jpg";
	           helper.ajaxProtectedClick(driver, Drupal.characterBannerRemove, "Remove", true, Common.ajaxThrobber, true, 5, false);
	           helper.createCustomBrand(driver, "", "", false, false, false, false, new Exception().getStackTrace()[0], image, "", "", "");
	           helper.assertWebElementExist(driver, new RuntimeException().getStackTrace()[0], Drupal.errorUpload);
	           actual = driver.findElement(By.xpath( Drupal.errorUpload)).getText();
	           expected = helper.getFileSpaceSize(Common.localImageDir + File.separator + image, "Kbit", 2);
	           expected = Drupal.errorMessageCharacterBannerWrongUploadSize(image, expected);
	           helper.assertEquals(driver, new RuntimeException().getStackTrace()[0], actual, expected);
	           
	           // ASSERT PNG FILE IS NOT ALLOWED:
	           image = "bubble.png";
	           driver.navigate().refresh();
	           helper.waitUntilElementPresence(driver, 5, Drupal.characterBannerBrowse, "Bubble Thumbnail Browse", new RuntimeException().getStackTrace()[0]);
	           driver.findElement(By.xpath(Drupal.characterBannerBrowse)).sendKeys(Common.localImageDir + File.separator + image);
		       actual = driver.findElement(By.xpath(Drupal.errorBrowse)).getText();
		       expected = Drupal.errorMessageCharacterBannerWrongImageFormat(image);
		       helper.assertEquals(driver, new RuntimeException().getStackTrace()[0], actual, expected);
		       
	           // ASSERT GIF FILE IS NOT ALLOWED:
	           image = "bubble.gif";
	           driver.navigate().refresh();
	           helper.waitUntilElementPresence(driver, 5, Drupal.characterBannerBrowse, "Bubble Thumbnail Browse", new RuntimeException().getStackTrace()[0]);
	           driver.findElement(By.xpath(Drupal.characterBannerBrowse)).sendKeys(Common.localImageDir + File.separator + image);
		       actual = driver.findElement(By.xpath(Drupal.errorBrowse)).getText();
		       expected = Drupal.errorMessageCharacterBannerWrongImageFormat(image);
		       helper.assertEquals(driver, new RuntimeException().getStackTrace()[0], actual, expected);
		       
	           // ASSERT JPEG FILE IS NOT ALLOWED:
	           image = "bubble.jpeg";
	           driver.navigate().refresh();
	           helper.waitUntilElementPresence(driver, 5, Drupal.characterBannerBrowse, "Bubble Thumbnail Browse", new RuntimeException().getStackTrace()[0]);
	           driver.findElement(By.xpath(Drupal.characterBannerBrowse)).sendKeys(Common.localImageDir + File.separator + image);
		       actual = driver.findElement(By.xpath(Drupal.errorBrowse)).getText();
		       expected = Drupal.errorMessageCharacterBannerWrongImageFormat(image);
		       helper.assertEquals(driver, new RuntimeException().getStackTrace()[0], actual, expected);
		       
	           // ASSERT BMP FILE IS NOT ALLOWED:
	           image = "bubble.bmp";
	           driver.navigate().refresh();
	           helper.waitUntilElementPresence(driver, 5, Drupal.characterBannerBrowse, "Bubble Thumbnail Browse", new RuntimeException().getStackTrace()[0]);
	           driver.findElement(By.xpath(Drupal.characterBannerBrowse)).sendKeys(Common.localImageDir + File.separator + image);
		       actual = driver.findElement(By.xpath(Drupal.errorBrowse)).getText();
		       expected = Drupal.errorMessageCharacterBannerWrongImageFormat(image);
		       helper.assertEquals(driver, new RuntimeException().getStackTrace()[0], actual, expected);
		    
		       // ASSERT LESS THEN MINIMUM DIMENSIONS IMAGE NOT ALLOWED:
		       image = "bubble 199x199.jpg";
	           driver.navigate().refresh();
	           helper.createCustomBrand(driver, "", "", false, false, false, false, new Exception().getStackTrace()[0], image, "", "", "");
	           helper.assertWebElementExist(driver, new RuntimeException().getStackTrace()[0], Drupal.errorUpload);
	           actual = driver.findElement(By.xpath( Drupal.errorUpload)).getText();
	           expected = Drupal.errorMessageCharacterBannerWrongImagePixels(image);
	           helper.assertEquals(driver, new RuntimeException().getStackTrace()[0], actual, expected);

	           // ASSERT MORE THEN MINIMUM DIMENSIONS IMAGE IS NOT ALLOWED:
	           image = "bubble 201x201.jpg";
	           driver.navigate().refresh();
	           helper.createCustomBrand(driver, "", "", false, false, false, false, new Exception().getStackTrace()[0], image, "", "", "");		       
	           helper.assertWebElementNotExist(driver,  new Exception().getStackTrace()[0], Drupal.errorUpload);
		       
	       } catch(Exception e) { helper.getExceptionDescriptive(e, new Exception().getStackTrace()[0], driver); }
	   }
	
	/**
	 * Test display Title while scrolling
	 * <p>Date Created: 2016-09-21</p>
	 * <p>Date Modified: 2016-09-21</p>
	 * <p>Original Version: V1</p>
	 * <p>Modified Version: </p>
	 * <p>Xpath: 1</p>
	 * <p>Test Cases: 36146 3496</p>
	 */
	@Test(groups = {"TC-36146","US-3496"}, priority = 44)
	public void testDisplayTitleWhileScrolling() throws IOException, IllegalArgumentException, MalformedURLException {
	       try{
	    	   // DECLARATION:
	           String title, titleURL, description, xpath, xpathCharTitle, xpathPageTitle;
	    	   int Y, y;
	    	   int charTitleTopY, charTitleH, charTitleBottomY;
	    	   int pageTitleTopY, pageTitleH, pageTitleBottomY;
	    	   Boolean charTitleVisible, pageTitleVisible;
	    	   Long value;
	    	   
	           // INITIALISATION:
	           helper.printXmlPath(new RuntimeException().getStackTrace()[0]);
	           driver = helper.getServerName(driver);
	           
	           // LOGIN TO DRUPAL AS A CONTENT EDITOR:
	           helper.logIn(driver,"content_editor","changeme");
	           
	           // CLEAN-UP:
	           helper.deleteAllContent(driver, "", "", "dev, content_editor", new RuntimeException().getStackTrace()[0]);
	           
	           // NAVIGATE TO A NEW CUSTOM BRAND PAGE:
	           helper.getUrlWaitUntil(driver, 10, Drupal.customBrand);
	           
	           // CREATE TITLE FOR CONTENT:
	           long fingerprint = System.currentTimeMillis();
	           title = String.valueOf(fingerprint) + " " +  helper.randomWord(Drupal.titleMaxCharsNumber);
	           title = helper.getStringBeginning(title, Drupal.titleMaxCharsNumber);
	           titleURL = helper.reFormatStringForURL(title, Drupal.titleMaxCharsNumber);
	           
	           // CREATE DESCRIPTION FOR CONTENT:
	           description = helper.randomEnglishText(helper.randomInt((Drupal.descriptionMaxCharsNumber - 30), Drupal.descriptionMaxCharsNumber));
	           
	           // CREATE CONTENT WITH BOTH AGES SELECTED:
	           helper.createCustomBrand(driver, title, description, true, true, false, true, true, new Exception().getStackTrace()[0]);
	           
	           // LINK GENERIC XPATH:
	           xpath = "//a[contains(@href,'" + titleURL +  Common.XpathContainsEnd;
	           xpathCharTitle = Common.title + Common.TextEntireAddToXpath(title);
	           xpathPageTitle = Common.titleBrandPage + Common.TextEntireAddToXpath(title);
	           helper.fileWriterPrinter("\n" + "LINK GENERIC XPATH = " + xpath);
	           helper.fileWriterPrinter(       "CHAR - TITLE XPATH = " + xpathCharTitle);
	           helper.fileWriterPrinter(       "PAGE - TITLE XPATH = " + xpathPageTitle + "\n");
	           
	           // AGE 5 AND UNDER REDIRECT TEST:
	           driver.manage().window().maximize();
	           helper.fileWriterPrinter("\n" + "AGE 5 AND UNDER REDIRECT TEST:");  
	           helper.getUrlWaitUntil(driver, 15, Common.fiveAndUnderURL);
	           helper.assertWebElementExist(driver, new Exception().getStackTrace()[0], xpath);
	           Thread.sleep(1000);
	           helper.clickToAppear(driver, Common.charBannerButtonLeft, Common.charBannerButtonRight, xpath, false, false);	           
	           // NAVIGATE TO BRAND PAGE:
	           helper.clickLinkUrlWaitUntil(driver, 15, xpath, new Exception().getStackTrace()[0]);	           	              
		       // MOBILE LANDSCAPE:
		       helper.switchWindowSizeToMobileLandscape(driver);
		       // ASSERT TITLES EXIST:
		       helper.fileWriterPrinter("\nSCROLLING DOWN:\n");
		       Y = driver.manage().window().getSize().getHeight();
		       y = 0;
		       while (y < Y) {
		    	   	// MEASUREMENT LOGIC OF SWITHING TITLES VISIBILITY:				
					charTitleTopY    = helper.getElementLocationY(driver, xpathCharTitle) - y;
					charTitleH       = helper.getElementHeight(driver, xpathCharTitle);
					charTitleBottomY = charTitleTopY + charTitleH;
					charTitleVisible  = ( (charTitleH > 0) && (charTitleBottomY > charTitleH) );
					pageTitleTopY    = helper.getElementLocationY(driver, xpathPageTitle) - y;
					pageTitleH       = helper.getElementHeight(driver, xpathPageTitle);
					pageTitleBottomY = pageTitleTopY + pageTitleH;
					pageTitleVisible  = ( (pageTitleH > 0) && (pageTitleBottomY > pageTitleH) );					
					helper.fileWriterPrinter("Current Scroll-Bar Y-position (screen coordinate) = " + y);
					helper.fileWriterPrinter("Current Char-Title (VISIBLE, HEIGHT, TOP, BOTTOM) = " + charTitleVisible + ", " + charTitleH + ", " + charTitleTopY + ", " + charTitleBottomY);
					helper.fileWriterPrinter("Current Page-Title (VISIBLE, HEIGHT, TOP, BOTTOM) = " + pageTitleVisible + ", " + pageTitleH + ", " + pageTitleTopY + ", " + pageTitleBottomY);
					
					// ASSERT SWITHING TITLES VISIBILITY:
					helper.assertBooleanTrue(driver, new Exception().getStackTrace()[0], (charTitleVisible || pageTitleVisible));
					
					// ONE-MORE DOWN-STEP SCROLL:
					((JavascriptExecutor) driver).executeScript("window.scrollBy(0,50)", "");
					Thread.sleep(500);
					value = (Long) ((JavascriptExecutor) driver).executeScript("return window.pageYOffset;");
					y = Integer.valueOf(String.valueOf(value));
					}
		       
	           // AGE 6 AND OVER REDIRECT TEST:
		       driver.manage().window().maximize();
	           helper.fileWriterPrinter("\n" + "AGE 6 AND OVER REDIRECT TEST:");	           	           
	           helper.getUrlWaitUntil(driver, 15, Common.sixAndOverURL);
	           Thread.sleep(1000);
	           helper.assertWebElementExist(driver, new Exception().getStackTrace()[0], xpath);
	           
	           helper.clickToAppear(driver, Common.charBannerButtonLeft, Common.charBannerButtonRight, xpath, false, false);
	           // NAVIGATE TO BRAND PAGE:
	           helper.clickLinkUrlWaitUntil(driver, 15, xpath, new Exception().getStackTrace()[0]);
		       // MOBILE LANDSCAPE:
		       helper.switchWindowSizeToMobileLandscape(driver);
		       // ASSERT TITLES EXIST:
		       helper.fileWriterPrinter("\nSCROLLING DOWN:\n");
		       Y = driver.manage().window().getSize().getHeight();
		       y = 0;
		       while (y < Y) {
		    	   	// MEASUREMENT LOGIC OF SWITHING TITLES VISIBILITY:				
					charTitleTopY    = helper.getElementLocationY(driver, xpathCharTitle) - y;
					charTitleH       = helper.getElementHeight(driver, xpathCharTitle);
					charTitleBottomY = charTitleTopY + charTitleH;
					charTitleVisible  = ( (charTitleH > 0) && (charTitleBottomY > charTitleH) );
					pageTitleTopY    = helper.getElementLocationY(driver, xpathPageTitle) - y;
					pageTitleH       = helper.getElementHeight(driver, xpathPageTitle);
					pageTitleBottomY = pageTitleTopY + pageTitleH;
					pageTitleVisible  = ( (pageTitleH > 0) && (pageTitleBottomY > pageTitleH) );					
					helper.fileWriterPrinter("Current Scroll-Bar Y-position (screen coordinate) = " + y);
					helper.fileWriterPrinter("Current Char-Title (VISIBLE, HEIGHT, TOP, BOTTOM) = " + charTitleVisible + ", " + charTitleH + ", " + charTitleTopY + ", " + charTitleBottomY);
					helper.fileWriterPrinter("Current Page-Title (VISIBLE, HEIGHT, TOP, BOTTOM) = " + pageTitleVisible + ", " + pageTitleH + ", " + pageTitleTopY + ", " + pageTitleBottomY);
					
					// ASSERT SWITHING TITLES VISIBILITY:
					helper.assertBooleanTrue(driver, new Exception().getStackTrace()[0], (charTitleVisible || pageTitleVisible));
					
					// ONE-MORE DOWN-STEP SCROLL:
					((JavascriptExecutor) driver).executeScript("window.scrollBy(0,50)", "");
					Thread.sleep(500);
					value = (Long) ((JavascriptExecutor) driver).executeScript("return window.pageYOffset;");
					y = Integer.valueOf(String.valueOf(value));
					}
		       
	    } catch(Exception e) { helper.getExceptionDescriptive(e, new Exception().getStackTrace()[0], driver); }
	}
	
}