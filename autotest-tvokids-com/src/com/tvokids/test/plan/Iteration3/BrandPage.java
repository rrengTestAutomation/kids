package com.tvokids.test.plan.Iteration3;

import java.awt.AWTException;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.*;

/*
import java.awt.Robot;
import java.io.File;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.interactions.Actions;
*/
import com.tvokids.locator.Common;
import com.tvokids.locator.Drupal;
import com.tvokids.utilities.*;

@SuppressWarnings("static-access")
public class BrandPage {
	static WebDriver driver;
	UtilitiesTestHelper helper = new UtilitiesTestHelper();

    @BeforeMethod public void startTime(Method method) throws IOException { helper.startTime(method); }   
    @AfterMethod  public void endTime() throws IOException { helper.endTime(); }
    @AfterMethod  @AfterClass   public void closeBrowsers() { driver.quit(); }

	/**
	 * Test sort Tiles on the reorder interface
	 * <p>Date Created: 2016-09-09</p>
	 * <p>Date Modified: 2016-09-09</p>
	 * <p>Original Version: V1</p>
	 * <p>Modified Version: </p>
	 * <p>Xpath: 1</p>
	 * <p>Test Cases: 35404 3522 3202 972</p>
	 * @throws IOException 
	 * @throws AWTException 
	 */
	@Test(groups = {"TC-35404","US-3522","US-3202","BUG-972","NEW"}, enabled = true, priority = 38)
    public void testSortTilesOnReorderInterface() throws IOException, AWTException {
	   try{
    	   // INITIALISATION:
           helper.printXmlPath(new RuntimeException().getStackTrace()[0]);
           driver = helper.getServerName(driver);
           
           // CLEAN-UP:
           helper.deleteAllContent(driver, "147", "", "", new RuntimeException().getStackTrace()[0]);
         
           // LOGIN TO DRUPAL AS A CONTENT EDITOR:
           helper.logIn(driver, Common.contentEditorUsername, Common.userPassword(Common.contentEditorUsername));
           
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
        	   title[i] = String.valueOf(fingerprint[i]) + " " + (i + 1) + "-" 
      	                + helper.randomWord(Drupal.titleMaxCharsNumber - String.valueOf(fingerprint[i]).length() - (" " + (i + 1) + "-").length());
        	   titleURL[i] = helper.reFormatStringForURL(title[i], Drupal.titleMaxCharsNumber);
        	   // CREATE DESCRIPTION FOR CONTENT:
        	   description[i] = helper.randomEnglishText(helper.randomInt((Drupal.descriptionMaxCharsNumber - 30), Drupal.descriptionMaxCharsNumber));
        	   // BANNER BUBBLE XPATH:
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
       
           // AGE 5 AND UNDER BEFORE-REORDER TEST (SORTED AS-IS):
           helper.fileWriterPrinter("\n" + "AGE 5 AND UNDER BEFORE-REORDER TEST (SORTED AS-IS):");  
           helper.getUrlWaitUntil(driver, 15, Common.fiveAndUnderURL);
           helper.assertWebElementExist(driver, new Exception().getStackTrace()[0], xpath[0]);
           Thread.sleep(1000);
           helper.clickToAppear(driver, Common.charBannerButtonLeft, Common.charBannerButtonRight, xpath[0], false, false);	           
           // NAVIGATE TO BRAND PAGE:
           helper.clickLinkUrlWaitUntil(driver, 15, xpath[0], new Exception().getStackTrace()[0]);
           // ASSERT AS-IS TILES SORTING:
           for (int i = total - 1; i > 1; i--) {
        	   int bottomUpper = helper.getElementLocationY(driver, tileXpath[i]) + helper.getElementHeight(driver, tileXpath[i]);
        	   int topOfNext = helper.getElementLocationY(driver, tileXpath[i - 1]) + helper.getElementHeight(driver, tileXpath[i - 1]);
        	   helper.assertBooleanTrue(driver, new Exception().getStackTrace()[0], bottomUpper < topOfNext);
        	   }
           
           // AGE 6 AND OVER BEFORE-REORDER TEST (SORTED AS-IS):
           helper.fileWriterPrinter("\n" + "AGE 6 AND OVER BEFORE-REORDER TEST (SORTED AS-IS):");  
           helper.getUrlWaitUntil(driver, 15, Common.sixAndOverURL);
           helper.assertWebElementExist(driver, new Exception().getStackTrace()[0], xpath[0]);
           Thread.sleep(1000);
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
           // ASSERT IMAGE SIZE:
           for (int i = total - 1; i > 0; i--) {
        	   String Xpath = Drupal.reorderTileImageSize(i);
        	   String expected = "Small";
        	   String actual = driver.findElement(By.xpath(Xpath)).getText();
        	   helper.assertEquals(driver, new Exception().getStackTrace()[0], actual, expected);
        	   }
           
           // DRAG UP:
           for (int i = total - 1; i > 1; i--) {
        	   String topXpath = Drupal.reorderTileHandle(i - 1);
        	   String bottomXpath = Drupal.reorderTileHandle(i);
        	   helper.fileWriterPrinter("\nABOVE THE LAST XPATH: " + topXpath);
        	   helper.fileWriterPrinter(  "          LAST XPATH: " + bottomXpath);
        	   
               int drag = helper.getElementLocationY(driver, topXpath) - helper.getElementLocationY(driver, bottomXpath);
               String Drag = " N/A";
               if(drag < 0) { Drag = Math.abs(drag) + " pixels UP"; }
               if(drag > 0) { Drag = Math.abs(drag) + " pixels DOWN"; }
               helper.fileWriterPrinter("\nDRUG-AND-DROP Y-DIRECTION MOVEMENT REQUIRED: " + Drag);

//             (new Actions(driver)).dragAndDrop(driver.findElement(By.xpath(bottomXpath)), driver.findElement(By.xpath(topXpath))).perform(); 
               (new Actions(driver)).dragAndDropBy(driver.findElement(By.xpath(bottomXpath)), 0, drag).perform();  
               Thread.sleep(1000);
               
               driver.findElement(By.id("edit-save-order")).click();
               Thread.sleep(1000);
               }
           
           // AGE 5 AND UNDER AFTER-REORDER TEST:
           helper.fileWriterPrinter("\n" + "AGE 5 AND UNDER AFTER-REORDER TEST:");  
           helper.getUrlWaitUntil(driver, 15, Common.fiveAndUnderURL);
           helper.assertWebElementExist(driver, new Exception().getStackTrace()[0], xpath[0]);
           Thread.sleep(1000);
           helper.clickToAppear(driver, Common.charBannerButtonLeft, Common.charBannerButtonRight, xpath[0], false, false);	           
           // NAVIGATE TO BRAND PAGE:
           helper.clickLinkUrlWaitUntil(driver, 15, xpath[0], new Exception().getStackTrace()[0]);
           // ASSERT AFTER-REORDER TILES SORTING:
           for (int i = total - 1; i > 1; i--) {
        	   int bottomUpper = helper.getElementLocationY(driver, tileXpath[i]) + helper.getElementHeight(driver, tileXpath[i]);
        	   int topOfNext = helper.getElementLocationY(driver, tileXpath[i - 1]) + helper.getElementHeight(driver, tileXpath[i - 1]);
        	   
        	   if(bottomUpper > topOfNext) {
        		   helper.fileWriterPrinter("\"" + title[i - 1] + "\" is above \"" + title[i] + "\"" );
        		   helper.assertBooleanTrue(driver, new Exception().getStackTrace()[0], bottomUpper > topOfNext);
        		   }
        	   
        	   if(bottomUpper < topOfNext) {
        		   helper.fileWriterPrinter("\"" + title[i] + "\" is above \"" + title[i - 1] + "\"" );
        		   helper.assertBooleanTrue(driver, new Exception().getStackTrace()[0], bottomUpper < topOfNext);
        		   }
        	   }
           
           // AGE 6 AND OVER AFTER-REORDER TEST:
           helper.fileWriterPrinter("\n" + "AGE 6 AND OVER AFTER-REORDER TEST:");  
           helper.getUrlWaitUntil(driver, 15, Common.sixAndOverURL);
           helper.assertWebElementExist(driver, new Exception().getStackTrace()[0], xpath[0]);
           Thread.sleep(1000);
           helper.clickToAppear(driver, Common.charBannerButtonLeft, Common.charBannerButtonRight, xpath[0], false, false);	           
           // NAVIGATE TO BRAND PAGE:
           helper.clickLinkUrlWaitUntil(driver, 15, xpath[0], new Exception().getStackTrace()[0]);
           // ASSERT AFTER-REORDER TILES SORTING:
           for (int i = total - 1; i > 1; i--) {
        	   int bottomUpper = helper.getElementLocationY(driver, tileXpath[i]) + helper.getElementHeight(driver, tileXpath[i]);
        	   int topOfNext = helper.getElementLocationY(driver, tileXpath[i - 1]) + helper.getElementHeight(driver, tileXpath[i - 1]);
        	   
        	   if(bottomUpper > topOfNext) {
        		   helper.fileWriterPrinter("\"" + title[i - 1] + "\" is above \"" + title[i] + "\"" );
        		   helper.assertBooleanTrue(driver, new Exception().getStackTrace()[0], bottomUpper > topOfNext);
        		   }
        	   
        	   if(bottomUpper < topOfNext) {
        		   helper.fileWriterPrinter("\"" + title[i] + "\" is above \"" + title[i - 1] + "\"" );
        		   helper.assertBooleanTrue(driver, new Exception().getStackTrace()[0], bottomUpper < topOfNext);
        		   }
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
           helper.deleteAllContent(driver, "147", "", "", new RuntimeException().getStackTrace()[0]);
           
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
        	   title[i] = String.valueOf(fingerprint[i]) + " " +  helper.randomWord(Drupal.titleMaxCharsNumber - String.valueOf(fingerprint[i]).length() - 1);
               if (i > 0) { title[i] = title[i].substring(0, Drupal.titleMaxCharsNumber); }
        	   titleURL[i] = helper.reFormatStringForURL(title[i], Drupal.titleMaxCharsNumber);
        	   // CREATE DESCRIPTION FOR CONTENT:
        	   description[i] = helper.randomEnglishText(helper.randomInt((Drupal.descriptionMaxCharsNumber - 30), Drupal.descriptionMaxCharsNumber));
        	   // BANNER BUBBLE XPATH:
	           xpath[i] = "//a[contains(@href,'" + titleURL[i] +  Common.XpathContainsEnd;
	           helper.fileWriterPrinter("\n\n" + "LINK GENERIC XPATH = " + xpath[i]);
        	   
	           if (i == 0) {
	        	// CREATE A CHARACTER BRAND CONTENT WITH BOTH AGES SELECTED:
        		   helper.logIn(driver);  // LOGIN TO DRUPAL AS AN ADMIN
    	           helper.createCharacterBrand(driver, title[i], description[i], "281374", false, true, true, false, true, true, new Exception().getStackTrace()[0]);
    	           helper.logOut(driver);
    	           helper.fileWriterPrinter("\n" + (i + 1) + " OF " + total + ": CREATED!\n  TYPE: CHARACTER BRAND\n TITLE: " + title[i] + "\n");
	           } else {
	        	   helper.logIn(driver, Common.contentEditorUsername, Common.userPassword(Common.contentEditorUsername)); // LOGIN TO DRUPAL AS A CONTENT EDITOR
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
           helper.logIn(driver, Common.contentEditorUsername, Common.userPassword(Common.contentEditorUsername));
           driver.manage().window().maximize();
           
           // FILTER BOTH AGES VISIBLE (ANY AGE VISIBLE):
           helper.filterAllContent(driver, "147", "", "", "", true, true, new Exception().getStackTrace()[0]);
           for (int i = 0; i < total; i++) {
        	   helper.assertWebElementExist(driver, new Exception().getStackTrace()[0], Common.ContentEntireToXpath(title[i]));
           }
           
           // FILTER AGE 5 AND UNDER ONLY VISIBLE:
           helper.filterAllContent(driver, "147", "", "", "", true, false, new Exception().getStackTrace()[0]);
           for (int i = 0; i < total; i++) {
        	   if(i == 0) { helper.assertWebElementExist(driver, new Exception().getStackTrace()[0], Common.ContentEntireToXpath(title[i])); }
        	   else {
        		   if (helper.isOdd(i)) { helper.assertWebElementExist(driver, new Exception().getStackTrace()[0], Common.ContentEntireToXpath(title[i])); }
        		   else { helper.assertWebElementNotExist(driver, new Exception().getStackTrace()[0], Common.ContentEntireToXpath(title[i])); }
        	   }
           }
           
           // FILTER AGE 6 AND OVER ONLY VISIBLE:
           helper.filterAllContent(driver, "147", "", "", "", false, true, new Exception().getStackTrace()[0]);
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
	 * <p>Date Modified: 2016-11-01</p>
	 * <p>Original Version: V1</p>
	 * <p>Modified Version: V2</p>
	 * <p>Xpath: 1</p>
	 * <p>Test Cases: 36113 3996 4452 972</p>
	 */
	@Test(groups = {"TC-36113","US-3996","US-4452","BUG-972","NEW"}, priority = 41)
	public void testNewSizeOfTileImageDescription() throws IOException, IllegalArgumentException, MalformedURLException {
	       try{
	    	   // DECLARATION:
	    	   String expected, actual;
	    	   
	    	   // INITIALISATION:
	           helper.printXmlPath(new RuntimeException().getStackTrace()[0]);
	           driver = helper.getServerName(driver);
	           
	           // LOGIN TO DRUPAL AS CONTENT-EDITOR:
	           helper.logIn(driver, Common.contentEditorUsername, Common.userPassword(Common.contentEditorUsername));
	           
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
	           helper.logIn(driver, Common.contentEditorUsername, Common.userPassword(Common.contentEditorUsername));
	           
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
	 * <p>Test Cases: 36146 3496 972</p>
	 */
	@Test(groups = {"TC-36146","US-3496","BUG-972","NEW"}, priority = 44)
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
	           
	           // CLEAN-UP:
	           helper.deleteAllContent(driver, "147", "", "", new RuntimeException().getStackTrace()[0]);
			   
	           // LOGIN TO DRUPAL AS A CONTENT EDITOR:
	           helper.logIn(driver, Common.contentEditorUsername, Common.userPassword(Common.contentEditorUsername));
	           
	           // NAVIGATE TO A NEW CUSTOM BRAND PAGE:
	           helper.getUrlWaitUntil(driver, 10, Drupal.customBrand);
	           
	           // CREATE TITLE FOR CONTENT:
	           long fingerprint = System.currentTimeMillis();
	           title = String.valueOf(fingerprint) + " " +  helper.randomWord(Drupal.titleMaxCharsNumber - String.valueOf(fingerprint).length() - 1);
	           titleURL = helper.reFormatStringForURL(title, Drupal.titleMaxCharsNumber);
	           helper.fileWriterPrinter("\n" + "BRAND PAGE URL END:  " + titleURL);
	           
	           // CREATE DESCRIPTION FOR CONTENT:
	           description = helper.randomEnglishText(helper.randomInt((Drupal.descriptionMaxCharsNumber - 30), Drupal.descriptionMaxCharsNumber));
	           
	           // CREATE CONTENT WITH BOTH AGES SELECTED:
	           helper.createCustomBrand(driver, title, description, true, true, false, true, true, new Exception().getStackTrace()[0]);
	           
	           // BANNER BUBBLE XPATH:
	           xpath = "//img[@alt='" + title + "']/parent::a";
	           xpathCharTitle = Common.title + Common.TextEntireAddToXpath(title);
	           xpathPageTitle = Common.titleBrandPage + Common.TextEntireAddToXpath(title);
	           helper.fileWriterPrinter("\n" + "BANNER BUBBLE LINK XPATH = " + xpath);
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
		       
	   } catch(Exception e) { helper.getExceptionDescriptive(e, new Exception().getStackTrace()[0], driver); }
	}
	
	/**
	 * Test New Character Banner Visibility
	 * <p>Date Created: 2016-09-28</p>
	 * <p>Date Modified: 2016-09-28</p>
	 * <p>Original Version: V1</p>
	 * <p>Modified Version: </p>
	 * <p>Xpath: 1</p>
	 * <p>Test Cases: 36414 4097 798 972</p>
	 */
	@Test(groups = {"TC-36414","US-4097","BUG-798","BUG-972","NEW"}, enabled = true, priority = 45)
	public void testNewCharacterBannerVisibility() throws IOException, IllegalArgumentException, MalformedURLException {
	       try{
	    	   // DECLARATION:
	           String title, titleURL, description, xpath;
	    	   
	           // INITIALISATION:
	           helper.printXmlPath(new RuntimeException().getStackTrace()[0]);
	           driver = helper.getServerName(driver);
	           
	           // CLEAN-UP:
	           helper.deleteAllContent(driver, "147", "", "", new RuntimeException().getStackTrace()[0]);
			   
	           // LOGIN TO DRUPAL AS A CONTENT EDITOR:
	           helper.logIn(driver, Common.contentEditorUsername, Common.userPassword(Common.contentEditorUsername));
	           
	           // NAVIGATE TO A NEW CUSTOM BRAND PAGE:
	           helper.getUrlWaitUntil(driver, 10, Drupal.customBrand);
	           
	           // ASSERT "VISIBLE ON CHARACTER BANNER" DEFAULT OPTION:
	           helper.assertBooleanFalse(driver, new Exception().getStackTrace()[0], helper.checkVisibleOnCharacterBanner(driver, false, new RuntimeException().getStackTrace()[0]));
	           
	           // CREATE TITLE FOR CONTENT:
	           long fingerprint = System.currentTimeMillis();
	           title = String.valueOf(fingerprint) + " " +  helper.randomWord(Drupal.titleMaxCharsNumber - String.valueOf(fingerprint).length() - 1);
	           titleURL = helper.reFormatStringForURL(title, Drupal.titleMaxCharsNumber);
	           helper.fileWriterPrinter("\n" + "BRAND PAGE URL END:  " + titleURL);
	           
	           // CREATE DESCRIPTION FOR CONTENT:
	           description = helper.randomEnglishText(helper.randomInt((Drupal.descriptionMaxCharsNumber - 30), Drupal.descriptionMaxCharsNumber));
	           
	           // CREATE CONTENT WITH BOTH AGES SELECTED:
	           helper.createCustomBrand(driver, title, description, true, true, false, true, true, new RuntimeException().getStackTrace()[0],
	                                   "bubble.jpg", "hero.jpg", "small.jpg", "", "", false);
	           // BANNER BUBBLE XPATH:
	           xpath = "//img[@alt='" + title + "']/parent::a";
	           helper.fileWriterPrinter("\n" + "BANNER BUBBLE LINK XPATH = " + xpath);
	           
	           // AGE 5 AND UNDER EXIST TEST:
	           helper.fileWriterPrinter("\n" + "AGE 5 AND UNDER REDIRECT TEST:");  
	           helper.getUrlWaitUntil(driver, 15, Common.fiveAndUnderURL);
	           helper.assertWebElementExist(driver, new Exception().getStackTrace()[0], xpath);
		       
	           // AGE 6 AND OVER EXIST TEST:
	           helper.fileWriterPrinter("\n" + "AGE 6 AND OVER REDIRECT TEST:");	           	           
	           helper.getUrlWaitUntil(driver, 15, Common.sixAndOverURL);
	           helper.assertWebElementExist(driver, new Exception().getStackTrace()[0], xpath);
	           
	           // EDIT:
	           int i = 0;
	    	   Boolean ifTitle = true;
	    	   while ( (ifTitle || (i == 0)) && (i < 25) ) {
		           helper.getUrlWaitUntil(driver, 15, Common.adminContentURL);
		           helper.filterAllContent(driver, title, "", "", "", false, false, new Exception().getStackTrace()[0]);
		           driver.findElement(By.xpath(Drupal.adminContentRowFirstEdit)).click();
		           Thread.sleep(1000);
		           helper.waitUntilElementPresence(driver, 15, By.id(Drupal.title), "Title", new Exception().getStackTrace()[0]);
		           Boolean checked = Boolean.valueOf(driver.findElement(By.id(Drupal.characterBannerVisibleOn)).getAttribute("checked"));
		           helper.fileWriterPrinter("\nCURRENT CHARACTER VISIBILITY CHECK-BOX STATUS: " + helper.checkBoxStatus(checked));
		           if (checked) { driver.findElement(By.id(Drupal.characterBannerVisibleOn)).click(); Thread.sleep(1000); }
		           checked = Boolean.valueOf(driver.findElement(By.id(Drupal.characterBannerVisibleOn)).getAttribute("checked"));
		           helper.fileWriterPrinter("    NEW CHARACTER VISIBILITY CHECK-BOX STATUS: " + helper.checkBoxStatus(checked));
				   i = helper.contentSubmit(Common.adminContentURL, driver, i);
				   if(title.length() > 0) { ifTitle = (! driver.getCurrentUrl().startsWith(Common.adminContentURL)); }
				   }
	    	   
	           // AGE 5 AND UNDER NOT EXIST TEST:
	           helper.fileWriterPrinter("\n" + "AGE 5 AND UNDER REDIRECT TEST:");  
	           helper.getUrlWaitUntil(driver, 15, Common.fiveAndUnderURL);
	           helper.assertWebElementNotExist(driver, new Exception().getStackTrace()[0], xpath);
		       
	           // AGE 6 AND OVER NOT EXIST TEST:
	           helper.fileWriterPrinter("\n" + "AGE 6 AND OVER REDIRECT TEST:");	           	           
	           helper.getUrlWaitUntil(driver, 15, Common.sixAndOverURL);
	           helper.assertWebElementNotExist(driver, new Exception().getStackTrace()[0], xpath);
	           
	   } catch(Exception e) { helper.getExceptionDescriptive(e, new Exception().getStackTrace()[0], driver); }
	}
	
	/**
	 * Test the create video Tile operation on Custom Brand Page for the age group of 5 and under
	 * <p>Date Created: 2016-10-03</p>
	 * <p>Date Modified: 2016-10-03</p>
	 * <p>Original Version: V1</p>
	 * <p>Modified Version: </p>
	 * <p>Xpath: 1</p>
	 * <p>Test Cases: 36413 3550 4271 832 972</p>
	 */
	@Test(groups = {"TC-36413","US-3550","US-4271","BUG-832","BUG-972","NEW"}, enabled = true, priority = 46)
	public void testCreateVideoTileOnCustomBrandPageForFiveAndUnder() throws IOException, IllegalArgumentException, MalformedURLException {
	       try{
	    	   // DECLARATION:
	           String title, titleURL, description, xpath, videoTitle, actual, expected;
	    	   
	           // INITIALISATION:
	           helper.printXmlPath(new RuntimeException().getStackTrace()[0]);
	           driver = helper.getServerName(driver);
	           
	           // CLEAN-UP:
	           helper.deleteAllContent(driver, "147", "", "", new RuntimeException().getStackTrace()[0]);
			   
	           // LOGIN TO DRUPAL AS A CONTENT EDITOR:
	           helper.logIn(driver, Common.contentEditorUsername, Common.userPassword(Common.contentEditorUsername));
	           
	           // NAVIGATE TO A NEW CUSTOM BRAND PAGE:
	           helper.getUrlWaitUntil(driver, 10, Drupal.customBrand);
	           
	           // CREATE TITLE FOR CONTENT:
	           long fingerprint = System.currentTimeMillis();
	           title = String.valueOf(fingerprint) + " " +  helper.randomWord(Drupal.titleMaxCharsNumber - String.valueOf(fingerprint).length() - 1);
	           titleURL = helper.reFormatStringForURL(title, Drupal.titleMaxCharsNumber);
	           helper.fileWriterPrinter("\n" + "BRAND PAGE URL END:  " + titleURL);
	           
	           // CREATE DESCRIPTION FOR CONTENT:
	           description = helper.randomEnglishText(helper.randomInt((Drupal.descriptionMaxCharsNumber - 30), Drupal.descriptionMaxCharsNumber));
	           
	           // CREATE CONTENT WITH BOTH AGES SELECTED:
	           helper.createCustomBrand(driver, title, description, true, false, false, true, true, new RuntimeException().getStackTrace()[0],
	                                   "bubble.jpg", "hero.jpg", "small.jpg", "", "", false);
	           // BANNER BUBBLE XPATH:
	           xpath = "//img[@alt='" + title + "']/parent::a";
	           helper.fileWriterPrinter("\n" + "BANNER BUBBLE LINK XPATH = " + xpath);

	           int i = 0;
	           Boolean ifTitle = true;
	           while ( (ifTitle || (i == 0)) && (i < 25) ) {
		           // FILTER AND EDIT THE CONTENT BY "VIDEO" AND "PUBLISH" AS "YES":
		           videoTitle = helper.reopenVideo(driver, "", true, false, true, false, new Exception().getStackTrace()[0]);
		           // PLACE VIDEO TO TILE WITH TILE PLACEMENT ASSERTION:
		           helper.addTilePlacement(driver, Drupal.tileVerticalTab, title, true, true, false, new Exception().getStackTrace()[0]);
		           // SUBMIT:
		           i = helper.contentSubmit(Common.adminContentURL, driver, i);
				   if(videoTitle.length() > 0) { ifTitle = (! driver.getCurrentUrl().startsWith(Common.adminContentURL)); }
				   if(! ifTitle) { helper.filterAllContent(driver, videoTitle, "Video", "", "Yes", false, false, new RuntimeException().getStackTrace()[0]); }
		           }
	           
	           videoTitle = driver.findElement(By.xpath(Drupal.adminContentRowFirstTitle)).getText();
	           expected = videoTitle;
	           
	           // AGE 5 AND UNDER TEST:
	           helper.fileWriterPrinter("\n" + "AGE 5 AND UNDER TEST:");  
	           helper.getUrlWaitUntil(driver, 15, Common.fiveAndUnderURL);
	           helper.assertWebElementExist(driver, new Exception().getStackTrace()[0], xpath);
	           Thread.sleep(1000);
	           helper.clickToAppear(driver, Common.charBannerButtonLeft, Common.charBannerButtonRight, xpath, false, false);	
	           // NAVIGATE TO BRAND PAGE:
	           helper.clickLinkUrlWaitUntil(driver, 15, xpath, new Exception().getStackTrace()[0]);
	           // ASSERT VIDEO ON CUSTOM BRAND PAGE EXIST:
	           actual = driver.findElement(By.xpath(Common.brandVideoTile(1))).getText();
	           if(expected.length() > Common.brandVideoTileMaxCharsNumber){
	        	   if(actual.endsWith("...")){ actual = actual.substring(0,actual.length() - 3); }
	        	   expected = expected.substring(0,actual.length());
	        	   }
	           helper.assertEquals(driver, new Exception().getStackTrace()[0], actual, expected);
	           
	   } catch(Exception e) { helper.getExceptionDescriptive(e, new Exception().getStackTrace()[0], driver); }
	}
	
	/**
	 * Test the create video Tile operation on Custom Brand Page for the age group of 6 and over
	 * <p>Date Created: 2016-10-03</p>
	 * <p>Date Modified: 2016-10-03</p>
	 * <p>Original Version: V1</p>
	 * <p>Modified Version: </p>
	 * <p>Xpath: 1</p>
	 * <p>Test Cases: 36413 3550 4271 832 972</p>
	 */
	@Test(groups = {"TC-36413","US-3550","US-4271","BUG-832","BUG-972","NEW"}, enabled = true, priority = 47)
	public void testCreateVideoTileOnCustomBrandPageForSixAndOver() throws IOException, IllegalArgumentException, MalformedURLException {
	       try{
	    	   // DECLARATION:
	           String title, titleURL, description, xpath, videoTitle, actual, expected;
	    	   
	           // INITIALISATION:
	           helper.printXmlPath(new RuntimeException().getStackTrace()[0]);
	           driver = helper.getServerName(driver);
	           
	           // CLEAN-UP:
	           helper.deleteAllContent(driver, "147", "", "", new RuntimeException().getStackTrace()[0]);
			   
	           // LOGIN TO DRUPAL AS A CONTENT EDITOR:
	           helper.logIn(driver, Common.contentEditorUsername, Common.userPassword(Common.contentEditorUsername));
	           
	           // NAVIGATE TO A NEW CUSTOM BRAND PAGE:
	           helper.getUrlWaitUntil(driver, 10, Drupal.customBrand);
	           
	           // CREATE TITLE FOR CONTENT:
	           long fingerprint = System.currentTimeMillis();
	           title = String.valueOf(fingerprint) + " " +  helper.randomWord(Drupal.titleMaxCharsNumber - String.valueOf(fingerprint).length() - 1);
	           titleURL = helper.reFormatStringForURL(title, Drupal.titleMaxCharsNumber);
	           helper.fileWriterPrinter("\n" + "BRAND PAGE URL END:  " + titleURL);
	           
	           // CREATE DESCRIPTION FOR CONTENT:
	           description = helper.randomEnglishText(helper.randomInt((Drupal.descriptionMaxCharsNumber - 30), Drupal.descriptionMaxCharsNumber));
	           
	           // CREATE CONTENT WITH BOTH AGES SELECTED:
	           helper.createCustomBrand(driver, title, description, false, true, false, true, true, new RuntimeException().getStackTrace()[0],
	                                   "bubble.jpg", "hero.jpg", "small.jpg", "", "", false);
	           // BANNER BUBBLE XPATH:
	           xpath = "//img[@alt='" + title + "']/parent::a";
	           helper.fileWriterPrinter("\n" + "BANNER BUBBLE LINK XPATH = " + xpath);

	           int i = 0;
	           Boolean ifTitle = true;
	           while ( (ifTitle || (i == 0)) && (i < 25) ) {
		           // FILTER AND EDIT THE CONTENT BY "VIDEO" AND "PUBLISH" AS "YES":
		           videoTitle = helper.reopenVideo(driver, "", true, false, false, true, new Exception().getStackTrace()[0]);
		           // PLACE VIDEO TO TILE WITH TILE PLACEMENT ASSERTION:
		           helper.addTilePlacement(driver, Drupal.tileVerticalTab, title, true, true, false, new Exception().getStackTrace()[0]);
		           // SUBMIT:
		           i = helper.contentSubmit(Common.adminContentURL, driver, i);
				   if(videoTitle.length() > 0) { ifTitle = (! driver.getCurrentUrl().startsWith(Common.adminContentURL)); }
				   if(! ifTitle) { helper.filterAllContent(driver, videoTitle, "Video", "", "Yes", false, false, new RuntimeException().getStackTrace()[0]); }
		           }
	           
	           videoTitle = driver.findElement(By.xpath(Drupal.adminContentRowFirstTitle)).getText();
	           expected = videoTitle;
	           
	           // AGE 6 AND OVER TEST:
	           helper.fileWriterPrinter("\n" + "AGE 6 AND OVER TEST:");  
	           helper.getUrlWaitUntil(driver, 15, Common.sixAndOverURL);
	           helper.assertWebElementExist(driver, new Exception().getStackTrace()[0], xpath);
	           Thread.sleep(1000);
	           helper.clickToAppear(driver, Common.charBannerButtonLeft, Common.charBannerButtonRight, xpath, false, false);	           
	           // NAVIGATE TO BRAND PAGE:
	           helper.clickLinkUrlWaitUntil(driver, 15, xpath, new Exception().getStackTrace()[0]);
	           // ASSERT VIDEO ON CUSTOM BRAND PAGE EXIST:
	           actual = driver.findElement(By.xpath(Common.brandVideoTile(1))).getText();
	           if(expected.length() > Common.brandVideoTileMaxCharsNumber){
	        	   if(actual.endsWith("...")){ actual = actual.substring(0,actual.length() - 3); }
	        	   expected = expected.substring(0,actual.length());
	        	   }
	           helper.assertEquals(driver, new Exception().getStackTrace()[0], actual, expected);
	           
	   } catch(Exception e) { helper.getExceptionDescriptive(e, new Exception().getStackTrace()[0], driver); }
	}
	
    /**
	 * Test the create video Tile operation on Character Brand Page for the age group of 5 and under
	 * <p>Date Created: 2016-10-05</p>
	 * <p>Date Modified: 2016-10-05</p>
	 * <p>Original Version: V1</p>
	 * <p>Modified Version: </p>
	 * <p>Xpath: 1</p>
	 * <p>Test Cases: 36413 3550 4271 854 972</p>
	 */
	@Test(groups = {"TC-36413","US-3550","US-4271","BUG-854","BUG-972","NEW"}, enabled = true, priority = 48)
	public void testCreateVideoTileOnCharacterBrandPageForFiveAndUnder() throws IOException, IllegalArgumentException, MalformedURLException {
	       try{
	    	   // DECLARATION:
	           String title, titleURL, description, xpath, videoTitle, actual, expected;
	    	   
	           // INITIALISATION:
	           helper.printXmlPath(new RuntimeException().getStackTrace()[0]);
	           driver = helper.getServerName(driver);
	           
               // CLEAN-UP:
               helper.deleteAllContent(driver, "147", "", "", new RuntimeException().getStackTrace()[0]);

               // LOGIN TO DRUPAL AS AN ADMIN:
               helper.logIn(driver);
	           
	           // CREATE TITLE FOR CONTENT:
	           long fingerprint = System.currentTimeMillis();
	           title = String.valueOf(fingerprint) + " " +  helper.randomWord(Drupal.titleMaxCharsNumber - String.valueOf(fingerprint).length() - 1);
	           titleURL = helper.reFormatStringForURL(title, Drupal.titleMaxCharsNumber);
	           helper.fileWriterPrinter("\n" + "BRAND PAGE URL END:  " + titleURL);
	           
	           // CREATE DESCRIPTION FOR CONTENT:
	           description = helper.randomEnglishText(helper.randomInt((Drupal.descriptionMaxCharsNumber - 30), Drupal.descriptionMaxCharsNumber));
	           
	           // CREATE CONTENT WITH AGE 5 SELECTED:
	           helper.createCharacterBrand(driver, title, description, "2654", true, true, false, false, true, true, new Exception().getStackTrace()[0]);
	           
	           // BANNER BUBBLE XPATH:
	           xpath = "//img[@alt='" + title + "']/parent::a";
	           helper.fileWriterPrinter("\n" + "BANNER BUBBLE LINK XPATH = " + xpath);
	           
	           // CREATE VIDEO WITH AGE 5 SELECTED:
	           videoTitle = String.valueOf(fingerprint) + " " +  helper.randomWord(Drupal.titleMaxCharsNumber - String.valueOf(fingerprint).length() - 1);
	           helper.createVideo(driver, videoTitle, "Short Description", "Long Description", title, "3022354586001", "2468", true, false, true, true, true, new Exception().getStackTrace()[0]);
	           helper.logOut(driver);
	           
	           // LOGIN TO DRUPAL AS A CONTENT EDITOR:
	           helper.logIn(driver, Common.contentEditorUsername, Common.userPassword(Common.contentEditorUsername));
	           
	           int i = 0;
	           Boolean ifTitle = true;
	           while ( (ifTitle || (i == 0)) && (i < 25) ) {
		           // REOPEN VIDEO BY TITLE:
		           helper.reopenContent(driver, videoTitle, "Video", "", "", true, false, true, new RuntimeException().getStackTrace()[0]);
		           // PLACE VIDEO TO TILE WITH TILE PLACEMENT ASSERTION:
		           helper.addTilePlacement(driver, Drupal.tileVerticalTab, title, true, true, false, new Exception().getStackTrace()[0]);
		           // SUBMIT:
		           i = helper.contentSubmit(Common.adminContentURL, driver, i);
				   ifTitle = (! driver.getCurrentUrl().startsWith(Common.adminContentURL));
		           }
	           
	           expected = videoTitle;
	           
	           // AGE 5 AND UNDER TEST:
	           helper.fileWriterPrinter("\n" + "AGE 5 AND UNDER TEST:");  
	           helper.getUrlWaitUntil(driver, 15, Common.fiveAndUnderURL);
	           helper.assertWebElementExist(driver, new Exception().getStackTrace()[0], xpath);
	           Thread.sleep(1000);
	           helper.clickToAppear(driver, Common.charBannerButtonLeft, Common.charBannerButtonRight, xpath, false, false);	
	           // NAVIGATE TO BRAND PAGE:
	           helper.clickLinkUrlWaitUntil(driver, 15, xpath, new Exception().getStackTrace()[0]);
	           // ASSERT VIDEO ON CUSTOM BRAND PAGE EXIST:
	           actual = driver.findElement(By.xpath(Common.brandVideoTile(1))).getText();
	           if(expected.length() > Common.brandVideoTileMaxCharsNumber){
	        	   if(actual.endsWith("...")){ actual = actual.substring(0,actual.length() - 3); }
	        	   expected = expected.substring(0,actual.length());
	        	   }
	           helper.assertEquals(driver, new Exception().getStackTrace()[0], actual, expected);
	           
	   } catch(Exception e) { helper.getExceptionDescriptive(e, new Exception().getStackTrace()[0], driver); }
}
	
    /**
	 * Test the create video Tile operation on Character Brand Page for the age group of 6 and over
	 * <p>Date Created: 2016-10-05</p>
	 * <p>Date Modified: 2016-10-05</p>
	 * <p>Original Version: V1</p>
	 * <p>Modified Version: </p>
	 * <p>Xpath: 1</p>
	 * <p>Test Cases: 36413 3550 4271 854 972</p>
	 */
	@Test(groups = {"TC-36413","US-3550","US-4271","BUG-854","BUG-972","NEW"}, enabled = true, priority = 49)
	public void testCreateVideoTileOnCharacterBrandPageForSixAndOver() throws IOException, IllegalArgumentException, MalformedURLException {
	       try{
	    	   // DECLARATION:
	           String title, titleURL, description, xpath, videoTitle, actual, expected;
	    	   
	           // INITIALISATION:
	           helper.printXmlPath(new RuntimeException().getStackTrace()[0]);
	           driver = helper.getServerName(driver);
	           
               // CLEAN-UP:
               helper.deleteAllContent(driver, "147", "", "", new RuntimeException().getStackTrace()[0]);

               // LOGIN TO DRUPAL AS AN ADMIN:
               helper.logIn(driver);
	           
	           // CREATE TITLE FOR CONTENT:
	           long fingerprint = System.currentTimeMillis();
	           title = String.valueOf(fingerprint) + " " +  helper.randomWord(Drupal.titleMaxCharsNumber - String.valueOf(fingerprint).length() - 1);
	           titleURL = helper.reFormatStringForURL(title, Drupal.titleMaxCharsNumber);
	           helper.fileWriterPrinter("\n" + "BRAND PAGE URL END:  " + titleURL);
	           
	           // CREATE DESCRIPTION FOR CONTENT:
	           description = helper.randomEnglishText(helper.randomInt((Drupal.descriptionMaxCharsNumber - 30), Drupal.descriptionMaxCharsNumber));
	           
	           // CREATE CONTENT WITH AGE 5 SELECTED:
	           helper.createCharacterBrand(driver, title, description, "2654", true, false, true, false, true, true, new Exception().getStackTrace()[0]);
	           
	           // BANNER BUBBLE XPATH:
	           xpath = "//img[@alt='" + title + "']/parent::a";
	           helper.fileWriterPrinter("\n" + "BANNER BUBBLE LINK XPATH = " + xpath);
	           
	           // CREATE VIDEO WITH AGE 5 SELECTED:
	           videoTitle = String.valueOf(fingerprint) + " " +  helper.randomWord(Drupal.titleMaxCharsNumber - String.valueOf(fingerprint).length() - 1);
	           helper.createVideo(driver, videoTitle, "Short Description", "Long Description", title, "3022354586001", "2468", false, true, true, true, true, new Exception().getStackTrace()[0]);
	           helper.logOut(driver);
	           
	           // LOGIN TO DRUPAL AS A CONTENT EDITOR:
	           helper.logIn(driver, Common.contentEditorUsername, Common.userPassword(Common.contentEditorUsername));
	           
	           int i = 0;
	           Boolean ifTitle = true;
	           while ( (ifTitle || (i == 0)) && (i < 25) ) {
		           // REOPEN VIDEO BY TITLE:
		           helper.reopenContent(driver, videoTitle, "Video", "", "", false, true, true, new RuntimeException().getStackTrace()[0]);
		           // PLACE VIDEO TO TILE WITH TILE PLACEMENT ASSERTION:
		           helper.addTilePlacement(driver, Drupal.tileVerticalTab, title, true, true, false, new Exception().getStackTrace()[0]);
		           // SUBMIT:
		           i = helper.contentSubmit(Common.adminContentURL, driver, i);
				   ifTitle = (! driver.getCurrentUrl().startsWith(Common.adminContentURL));
		           }
	           
	           expected = videoTitle;
	           
	           // AGE 6 AND OVER TEST:
	           helper.fileWriterPrinter("\n" + "AGE 6 AND OVER TEST:");  
	           helper.getUrlWaitUntil(driver, 15, Common.sixAndOverURL);
	           helper.assertWebElementExist(driver, new Exception().getStackTrace()[0], xpath);
	           Thread.sleep(1000);
	           helper.clickToAppear(driver, Common.charBannerButtonLeft, Common.charBannerButtonRight, xpath, false, false);	           
	           // NAVIGATE TO BRAND PAGE:
	           helper.clickLinkUrlWaitUntil(driver, 15, xpath, new Exception().getStackTrace()[0]);
	           // ASSERT VIDEO ON CUSTOM BRAND PAGE EXIST:
	           actual = driver.findElement(By.xpath(Common.brandVideoTile(1))).getText();
	           if(expected.length() > Common.brandVideoTileMaxCharsNumber){
	        	   if(actual.endsWith("...")){ actual = actual.substring(0,actual.length() - 3); }
	        	   expected = expected.substring(0,actual.length());
	        	   }
	           helper.assertEquals(driver, new Exception().getStackTrace()[0], actual, expected);
	           
	   } catch(Exception e) { helper.getExceptionDescriptive(e, new Exception().getStackTrace()[0], driver); }
	}
	
	/**
	 * Test Badge for the Tile
	 * <p>Date Created: 2016-10-19</p>
	 * <p>Date Modified: 2016-10-19</p>
	 * <p>Original Version: V1</p>
	 * <p>Modified Version: </p>
	 * <p>Xpath: 1</p>
	 * <p>Test Cases: 35413 3309 35459 3183 972</p>
	 */
	@Test(groups = {"TC-35413","US-3309","TC-35459","US-3183","BUG-972","NEW"}, enabled = true, priority = 50)
	public void testBadgeForTileOnCustomBrandPage() throws IOException, IllegalArgumentException, MalformedURLException {
	       try{	    	   
	           // INITIALISATION:
	           helper.printXmlPath(new RuntimeException().getStackTrace()[0]);
	           driver = helper.getServerName(driver);
	           
	           // CLEAN-UP:
	           helper.deleteAllContent(driver, "147", "", "", new RuntimeException().getStackTrace()[0]);

	           // LOGIN TO DRUPAL AS A CONTENT EDITOR:
	           helper.logIn(driver, Common.contentEditorUsername, Common.userPassword(Common.contentEditorUsername));
	           
	           // DECLARATION:
	           int total = 3;
	           String[] title = new String[total];
	           String[] titleURL = new String[total];
	           String[] description = new String[total];
	           String[] xpath = new String[total];
	           String[] tileXpath = new String[total];
	           long[] fingerprint = new long[total];
	           
	           String  badge = "", tile = "", actual, expected = "", svg;
	           Boolean ifPublish = false;
	           
	           for (int i = 0; i < total; i++) {
	        	   // CREATE TITLES FOR CONTENTS:
	        	   fingerprint[i] = System.currentTimeMillis();
	        	   title[i] = String.valueOf(fingerprint[i]) + " " + (i + 1) + "-" 
	      	                + helper.randomWord(Drupal.titleMaxCharsNumber - String.valueOf(fingerprint[i]).length() - (" " + (i + 1) + "-").length());
	        	   titleURL[i] = helper.reFormatStringForURL(title[i], Drupal.titleMaxCharsNumber);
	        	   // CREATE DESCRIPTION FOR CONTENT:
	        	   description[i] = helper.randomEnglishText(helper.randomInt((Drupal.descriptionMaxCharsNumber - 30), Drupal.descriptionMaxCharsNumber));
	        	   // BANNER BUBBLE XPATH:
		           xpath[i] = "//a[contains(@href,'" + titleURL[i] +  Common.XpathContainsEnd;
		           helper.fileWriterPrinter("\n\n" + "LINK GENERIC XPATH = " + xpath[i]);
		           // PUBLISH ON FIRST BRAND TILES OF OTHER BRANDS WITH BADGES:
		           if(i > 0) { badge = "new.svg"; tile = title[0]; ifPublish = true; }
		           // CREATE A CUSTOM BRAND CONTENT WITH BOTH AGES SELECTED:
	    		   helper.createCustomBrand(driver, title[i], description[i], true, true, false, true, true, new RuntimeException().getStackTrace()[0],
                                           "bubble.jpg", "hero.jpg", "small.jpg", "", badge, tile, ifPublish);
	    		   tileXpath[i] = Common.TextEntireToXpath(title[i]) + "/ancestor::a";
	    		   helper.fileWriterPrinter("\n" + (i + 1) + " OF " + total + ": CREATED!\n  TYPE: CUSTOM BRAND\n TITLE: " + title[i] + "\n  TILE: " + tile + "\n");
	    		   
	    		   if(i > 0) {
	    			   int j = 0;
			           Boolean ifTitle = true;
			           while ( (ifTitle || (j == 0)) && (j < 25) ) {
					           // FILTER BY FIRST TITLE AND EDIT THE CONTENT:
					           helper.reopenContent(driver, title[0], "", "", "", false, false, true, new Exception().getStackTrace()[0]);
					           // PUBLISH CURRENT TITLE ON FIRST BRAND WITH TILE PLACEMENT ASSERTION:
					           helper.addTilePlacement(driver, Drupal.tileVerticalTab, title[i], true, true, false, new Exception().getStackTrace()[0]);
					           // SUBMIT:
					           j = helper.contentSubmit(Common.adminContentURL, driver, j);
							   ifTitle = (! driver.getCurrentUrl().startsWith(Common.adminContentURL));
							   }
			           }
	    		   }
	           
	           // AGE 5 AND UNDER TEST:
	           helper.fileWriterPrinter("\n" + "AGE 5 AND UNDER TEST:");
	           for (int i = 0; i < total; i++) {
		           helper.getUrlWaitUntil(driver, 15, Common.fiveAndUnderURL);
		           helper.assertWebElementExist(driver, new Exception().getStackTrace()[0], xpath[i]);
		           Thread.sleep(1000);
		           helper.clickToAppear(driver, Common.charBannerButtonLeft, Common.charBannerButtonRight, xpath[i], false, false);	
		           // NAVIGATE TO BRAND PAGE:
		           helper.clickLinkUrlWaitUntil(driver, 15, xpath[i], new Exception().getStackTrace()[0]);
		           // ASSERTIONS:
	        	   if(i == 0) {
	        		   // ASSERT BADGE IS EXIST:
	        		   for (int j = 1; j < total; j++) {
	        			   expected = title[total - j];
	        			   actual   = driver.findElement(By.xpath(Common.brandTile("", j))).getText();
		        		   helper.assertEquals(driver, new Exception().getStackTrace()[0], actual, expected);
		        		   helper.assertWebElementExist(driver, new Exception().getStackTrace()[0], Common.brandTile("", j) + Common.brandBadgeParticle);
		        		   svg = driver.findElement(By.xpath(Common.brandTile("", j) + Common.brandBadgeParticle)).getAttribute("src");
		        		   svg = svg.substring(svg.lastIndexOf("/") + 1, svg.length());
		        		   helper.assertEquals(driver, new Exception().getStackTrace()[0], svg, badge);
		        		   }
	        	   } else { 
	        		   // ASSERT BADGE IS NOT EXIST:
	        		   expected = title[0];
	        		   actual   = driver.findElement(By.xpath(Common.brandTile("", 1))).getText();
	        		   helper.assertEquals(driver, new Exception().getStackTrace()[0], actual, expected);
	        		   helper.assertWebElementNotExist(driver, new Exception().getStackTrace()[0], Common.brandTile("", 1) + Common.brandBadgeParticle);
	        		   }
		           }
	           
	           // AGE 6 AND OVER TEST:
	           helper.fileWriterPrinter("\n" + "AGE 6 AND OVER TEST:");
	           for (int i = 0; i < total; i++) {
		           helper.getUrlWaitUntil(driver, 15, Common.sixAndOverURL);
		           helper.assertWebElementExist(driver, new Exception().getStackTrace()[0], xpath[i]);
		           Thread.sleep(1000);
		           helper.clickToAppear(driver, Common.charBannerButtonLeft, Common.charBannerButtonRight, xpath[i], false, false);	
		           // NAVIGATE TO BRAND PAGE:
		           helper.clickLinkUrlWaitUntil(driver, 15, xpath[i], new Exception().getStackTrace()[0]);
		           // ASSERTIONS:
	        	   if(i == 0) {
	        		   for (int j = 1; j < total; j++) {
	        			   // ASSERT BADGE IS EXIST:
	        			   expected = title[total - j];
	        			   actual   = driver.findElement(By.xpath(Common.brandTile("", j))).getText();
		        		   helper.assertEquals(driver, new Exception().getStackTrace()[0], actual, expected);
		        		   helper.assertWebElementExist(driver, new Exception().getStackTrace()[0], Common.brandTile("", j) + Common.brandBadgeParticle);
		        		   svg = driver.findElement(By.xpath(Common.brandTile("", j) + Common.brandBadgeParticle)).getAttribute("src");
		        		   svg = svg.substring(svg.lastIndexOf("/") + 1, svg.length());
		        		   helper.assertEquals(driver, new Exception().getStackTrace()[0], svg, badge);
		        		   }
	        	   } else {
	        		   // ASSERT BADGE IS NOT EXIST:
	        		   expected = title[0];
	        		   actual   = driver.findElement(By.xpath(Common.brandTile("", 1))).getText();
	        		   helper.assertEquals(driver, new Exception().getStackTrace()[0], actual, expected);
	        		   helper.assertWebElementNotExist(driver, new Exception().getStackTrace()[0], Common.brandTile("", 1) + Common.brandBadgeParticle);
	        		   }
		           }
	           
	   } catch(Exception e) { helper.getExceptionDescriptive(e, new Exception().getStackTrace()[0], driver); }
	}
	
	/**
	 * Test Brand Tile Structure
	 * <p>Date Created: 2016-10-21</p>
	 * <p>Date Modified: 2016-10-21</p>
	 * <p>Original Version: V1</p>
	 * <p>Modified Version: </p>
	 * <p>Xpath: 1</p>
	 * <p>Test Cases: 35459 3183 878 972</p>
	 */
	@Test(groups = {"TC-35459","US-3183","BUG-878","BUG-972","NEW"}, enabled = true, priority = 51)
	public void testBrandTileStructureOnCustomBrandPage() throws IOException, IllegalArgumentException, MalformedURLException {
	       try{	    	   
	           // INITIALISATION:
	           helper.printXmlPath(new RuntimeException().getStackTrace()[0]);
	           driver = helper.getServerName(driver);
	           
	           // CLEAN-UP:
	           helper.deleteAllContent(driver, "147", "", "", new RuntimeException().getStackTrace()[0]);

	           // LOGIN TO DRUPAL AS A CONTENT EDITOR:
	           helper.logIn(driver, Common.contentEditorUsername, Common.userPassword(Common.contentEditorUsername));
	           
	           // DECLARATION:
	           int total = 3;
	           String[] title = new String[total];
	           String[] titleURL = new String[total];
	           String[] description = new String[total];
	           String[] xpath = new String[total];
	           String[] tileXpath = new String[total];
	           long[] fingerprint = new long[total];
	           
	           String  tile = "", large = "", actual, expected = "";
	           Boolean ifPublish = false;
	           
	           for (int i = 0; i < total; i++) {
	        	   // CREATE TITLES FOR CONTENTS:
	        	   fingerprint[i] = System.currentTimeMillis();
	        	   title[i] = String.valueOf(fingerprint[i]) + " " + (i + 1) + "-" 
	      	                + helper.randomWord(Drupal.titleMaxCharsNumber - String.valueOf(fingerprint[i]).length() - (" " + (i + 1) + "-").length());
	        	   titleURL[i] = helper.reFormatStringForURL(title[i], Drupal.titleMaxCharsNumber);
	        	   // CREATE DESCRIPTION FOR CONTENT:
	        	   description[i] = helper.randomEnglishText(helper.randomInt((Drupal.descriptionMaxCharsNumber - 30), Drupal.descriptionMaxCharsNumber));
	        	   // BANNER BUBBLE XPATH:
		           xpath[i] = "//a[contains(@href,'" + titleURL[i] +  Common.XpathContainsEnd;
		           helper.fileWriterPrinter("\n\n" + "LINK GENERIC XPATH = " + xpath[i]);
		           // PUBLISH ON FIRST BRAND TILES OF OTHER BRANDS:
		           if(i > 0) { tile = title[0]; ifPublish = true; }
		           // USE LARGE SIZE TILE FOR ALL BRANDS STARTING FROM SECOND:
		           if(i > 1) { large = "large.jpg"; }
		           // CREATE A CUSTOM BRAND CONTENT WITH BOTH AGES SELECTED:
	    		   helper.createCustomBrand(driver, title[i], description[i], true, true, true, true, true, new RuntimeException().getStackTrace()[0],
                                        "bubble.jpg", "hero.jpg", "small.jpg", large, "", tile, ifPublish);
	    		   tileXpath[i] = Common.TextEntireToXpath(title[i]) + "/ancestor::a";
	    		   helper.fileWriterPrinter("\n" + (i + 1) + " OF " + total + ": CREATED!\n  TYPE: CUSTOM BRAND\n TITLE: " + title[i] + "\n  TILE: " + tile + "\n");
	    		   }
	           
	           // AGE 5 AND UNDER TEST:
	           helper.fileWriterPrinter("\n" + "AGE 5 AND UNDER TEST:");
	           // NAVIGATE TO AGE PAGE:
	           helper.getUrlWaitUntil(driver, 15, Common.fiveAndUnderURL);
	           helper.assertWebElementExist(driver, new Exception().getStackTrace()[0], xpath[0]);
	           Thread.sleep(1000);
	           helper.clickToAppear(driver, Common.charBannerButtonLeft, Common.charBannerButtonRight, xpath[0], false, false);	
	           // NAVIGATE TO BRAND PAGE:
	           helper.clickLinkUrlWaitUntil(driver, 15, xpath[0], new Exception().getStackTrace()[0]);
	           // ASSERTIONS:
	           for (int i = 1; i < total; i++) {
	        	   if (i == 1) { helper.fileWriterPrinter(); }
	        	   helper.fileWriterPrinter("TILE TITLE UNDER TEST: \"" + title[i] + "\"");
		           // ASSERT TILE SIZE IS CORRECT:
		           helper.assertWebElementExist(driver, new Exception().getStackTrace()[0], Common.brandTile((Object) title[i]));
		           // ASSERT TILE EXIST:
		           expected = "This is an Alternate Text of Small Tile";
		           if (i > 1) { expected = "This is an Alternate Text of Large Tile"; } 
		           actual = driver.findElement(By.xpath(Common.brandTileImage(title[i]))).getAttribute("alt");
		           helper.assertEquals(driver, new Exception().getStackTrace()[0], actual, expected);
		           }

	           // AGE 6 AND OVER TEST:
	           helper.fileWriterPrinter("\n" + "AGE 6 AND OVER TEST:");
	           // NAVIGATE TO AGE PAGE:
	           helper.getUrlWaitUntil(driver, 15, Common.sixAndOverURL);
	           helper.assertWebElementExist(driver, new Exception().getStackTrace()[0], xpath[0]);
	           Thread.sleep(1000);
	           helper.clickToAppear(driver, Common.charBannerButtonLeft, Common.charBannerButtonRight, xpath[0], false, false);	
	           // NAVIGATE TO BRAND PAGE:
	           helper.clickLinkUrlWaitUntil(driver, 15, xpath[0], new Exception().getStackTrace()[0]);
	           // ASSERTIONS:
	           for (int i = 1; i < total; i++) {
	        	   if (i == 1) { helper.fileWriterPrinter(); }
	        	   helper.fileWriterPrinter("TILE TITLE UNDER TEST: \"" + title[i] + "\"");
		           // ASSERT TILE SIZE IS CORRECT:
		           helper.assertWebElementExist(driver, new Exception().getStackTrace()[0], Common.brandTile((Object) title[i]));
		           // ASSERT TILE EXIST:
		           expected = "This is an Alternate Text of Small Tile";
		           if (i > 1) { expected = "This is an Alternate Text of Large Tile"; } 
		           actual = driver.findElement(By.xpath(Common.brandTileImage(title[i]))).getAttribute("alt");
		           helper.assertEquals(driver, new Exception().getStackTrace()[0], actual, expected);
		           }
	           
	   } catch(Exception e) { helper.getExceptionDescriptive(e, new Exception().getStackTrace()[0], driver); }
	}
	
	/**
	 * Test Hero Box on New Custom Brand page
	 * <p>Date Created: 2016-10-25</p>
	 * <p>Date Modified: 2016-10-25</p>
	 * <p>Original Version: V1</p>
	 * <p>Modified Version: </p>
	 * <p>Xpath: 1</p>
	 * <p>Test Cases: 35474 2079 972</p>
	 */
	@Test(groups = {"TC-35474","US-2079","BUG-972","NEW"}, priority = 52)
	public void testHeroBoxOnNewCustomBrandPage() throws IOException {
		   try{
			   // INITIALISATION:
		       helper.printXmlPath(new RuntimeException().getStackTrace()[0]);
		       driver = helper.getServerName(driver);
		
		       // DECLARATION:
		       String title, titleURL, xpath, description, actual, expected;
		       int i;
		       
	           // CLEAN-UP:
	           helper.deleteAllContent(driver, "147", "", "", new RuntimeException().getStackTrace()[0]);
			   
	           // LOGIN TO DRUPAL AS A CONTENT EDITOR:
	           helper.logIn(driver, Common.contentEditorUsername, Common.userPassword(Common.contentEditorUsername));
	           
	           // CREATE TITLE FOR CONTENT:
	           long fingerprint = System.currentTimeMillis();
	           title = String.valueOf(fingerprint) + " " +  helper.randomWord(Drupal.titleMaxCharsNumber - String.valueOf(fingerprint).length() - 1);
	           titleURL = helper.reFormatStringForURL(title, Drupal.titleMaxCharsNumber);
	           helper.fileWriterPrinter("\n" + "BRAND PAGE URL END:  " + titleURL);
	           
	           // CREATE DESCRIPTION FOR CONTENT:
	           description = helper.randomEnglishText(helper.randomInt((Drupal.descriptionMaxCharsNumber - 30), Drupal.descriptionMaxCharsNumber));
	           
	           // CREATE CONTENT WITH BOTH AGES SELECTED:
	           helper.createCustomBrand(driver, title, description, true, true, false, true, true, new RuntimeException().getStackTrace()[0],
	                                   "bubble.jpg", "hero.jpg", "small.jpg", "", "", false);
	           helper.logOut(driver);
	           
	           // BANNER BUBBLE XPATH:
	           xpath = "//img[@alt='" + title + "']/parent::a";
	           helper.fileWriterPrinter("\n" + "BANNER BUBBLE LINK XPATH = " + xpath);
	           
	           // AGE 5 AND UNDER TEST:
	           helper.fileWriterPrinter("\n" + "AGE 5 AND UNDER TEST:");  
	           helper.getUrlWaitUntil(driver, 15, Common.fiveAndUnderURL);
	           helper.assertWebElementExist(driver, new Exception().getStackTrace()[0], xpath);
	           Thread.sleep(1000);
	           helper.clickToAppear(driver, Common.charBannerButtonLeft, Common.charBannerButtonRight, xpath, false, false);	
	           // NAVIGATE TO BRAND PAGE:
	           helper.clickLinkUrlWaitUntil(driver, 15, xpath, new Exception().getStackTrace()[0]);
	           // ASSERTIONS:
	           // ASSERT CHARACTER BUBBLE IS THE SAME BUBBLE DISPLAYED ON BANNER:
	           helper.fileWriterPrinter("\n" + "ASSERT CHARACTER BUBBLE IS THE SAME BUBBLE DISPLAYED ON BANNER:");
	           i = driver.findElements(By.xpath(Common.charBannerThumbnails)).size();
	           helper.fileWriterPrinter("CURRENT TOTAL NUMBER OF BANNER BUBBLES: " + i);
	           actual = driver.findElement(By.xpath(Common.brandBubble)).getAttribute("src");
	           actual = actual.substring(actual.lastIndexOf("/") + 1, actual.length());
	           expected = driver.findElement(By.xpath(xpath + "/descendant::img[@src]")).getAttribute("src");
	           expected = expected.substring(expected.lastIndexOf("/") + 1 , expected.length());
	           helper.assertEquals(driver, new Exception().getStackTrace()[0], actual, expected);
	           // ASSERT CHARACTER TITLE IS THE NAME OF THE BRAND PAGE:
	           helper.fileWriterPrinter("\n" + "ASSERT CHARACTER TITLE IS THE NAME OF THE BRAND PAGE:");
	           actual = driver.findElement(By.xpath(Common.brandTitle)).getText();
	           expected = title;
	           helper.assertEquals(driver, new Exception().getStackTrace()[0], actual, expected);
	           // ASSERT SCHEDULE INFORMATION: N/A FOR CUSTOM BRAND
	           // ASSERT THUMBNAIL:
	           helper.fileWriterPrinter("\n" + "ASSERT THUMBNAIL:");
	           expected = "hero.jpg";
	           actual = driver.findElement(By.xpath(Common.brandHeroBoxImage)).getAttribute("src");
	           actual = helper.trimLongerFilenameAsPerShorter(actual, expected);
	           helper.assertEquals(driver, new Exception().getStackTrace()[0], actual, expected);          
	           // ASSERT DESCRIPTION IS DESCRIPTION OF THE BRAND PAGE:
	           helper.fileWriterPrinter("ASSERT DESCRIPTION IS DESCRIPTION OF THE BRAND PAGE:");
	           actual = helper.getText(driver, Common.brandDescription);
	           expected = description;
	           helper.assertEquals(driver, new Exception().getStackTrace()[0], actual, expected);
	           
	           // AGE 6 AND OVER TEST:
	           helper.fileWriterPrinter("\n" + "AGE 6 AND OVER TEST:");  
	           helper.getUrlWaitUntil(driver, 15, Common.sixAndOverURL);
	           helper.assertWebElementExist(driver, new Exception().getStackTrace()[0], xpath);
	           Thread.sleep(1000);
	           helper.clickToAppear(driver, Common.charBannerButtonLeft, Common.charBannerButtonRight, xpath, false, false);	
	           // NAVIGATE TO BRAND PAGE:
	           helper.clickLinkUrlWaitUntil(driver, 15, xpath, new Exception().getStackTrace()[0]);	     
	           helper.assertWebElementExist(driver, new Exception().getStackTrace()[0], xpath);
	           Thread.sleep(1000);
	           helper.clickToAppear(driver, Common.charBannerButtonLeft, Common.charBannerButtonRight, xpath, false, false);	
	           // NAVIGATE TO BRAND PAGE:
	           helper.clickLinkUrlWaitUntil(driver, 15, xpath, new Exception().getStackTrace()[0]);
	           // ASSERTIONS:
	           // ASSERT CHARACTER BUBBLE IS THE SAME BUBBLE DISPLAYED ON BANNER:
	           helper.fileWriterPrinter("\n" + "ASSERT CHARACTER BUBBLE IS THE SAME BUBBLE DISPLAYED ON BANNER:");
	           i = driver.findElements(By.xpath(Common.charBannerThumbnails)).size();
	           helper.fileWriterPrinter("CURRENT TOTAL NUMBER OF BANNER BUBBLES: " + i);
	           actual = driver.findElement(By.xpath(Common.brandBubble)).getAttribute("src");
	           actual = actual.substring(actual.lastIndexOf("/") + 1, actual.length());
	           expected = driver.findElement(By.xpath(xpath + "/descendant::img[@src]")).getAttribute("src");
	           expected = expected.substring(expected.lastIndexOf("/") + 1 , expected.length());
	           helper.assertEquals(driver, new Exception().getStackTrace()[0], actual, expected);
	           // ASSERT CHARACTER TITLE IS THE NAME OF THE BRAND PAGE:
	           helper.fileWriterPrinter("\n" + "ASSERT CHARACTER TITLE IS THE NAME OF THE BRAND PAGE:");
	           actual = driver.findElement(By.xpath(Common.brandTitle)).getText();
	           expected = title;
	           helper.assertEquals(driver, new Exception().getStackTrace()[0], actual, expected);
	           // ASSERT SCHEDULE INFORMATION: N/A FOR CUSTOM BRAND
	           // ASSERT THUMBNAIL:
	           helper.fileWriterPrinter("\n" + "ASSERT THUMBNAIL:");
	           expected = "hero.jpg";
	           actual = driver.findElement(By.xpath(Common.brandHeroBoxImage)).getAttribute("src");
	           actual = helper.trimLongerFilenameAsPerShorter(actual, expected);
	           helper.assertEquals(driver, new Exception().getStackTrace()[0], actual, expected);          
	           // ASSERT DESCRIPTION IS DESCRIPTION OF THE BRAND PAGE:
	           helper.fileWriterPrinter("ASSERT DESCRIPTION IS DESCRIPTION OF THE BRAND PAGE:");
	           actual = helper.getText(driver, Common.brandDescription);
	           expected = description;
	           helper.assertEquals(driver, new Exception().getStackTrace()[0], actual, expected); 
		    		   
	   } catch(Exception e) { helper.getExceptionDescriptive(e, new Exception().getStackTrace()[0], driver); }
	}
	
	/**
	 * Test Hero Box on New Character Brand page
	 * <p>Date Created: 2016-10-25</p>
	 * <p>Date Modified: 2016-10-25</p>
	 * <p>Original Version: V1</p>
	 * <p>Modified Version: </p>
	 * <p>Xpath: 1</p>
	 * <p>Test Cases: 35474 2079 972</p>
	 */
	@Test(groups = {"TC-35474","US-2079","BUG-972","NEW"}, priority = 53)
	public void testHeroBoxOnNewCharacterBrandPage() throws IOException {
		   try{
			   // INITIALISATION:
		       helper.printXmlPath(new RuntimeException().getStackTrace()[0]);
		       driver = helper.getServerName(driver);
		
		       // DECLARATION:
		       String title, titleURL, xpath, description, actual, expected;
		       int i;
		       
	           // CLEAN-UP:
	           helper.deleteAllContent(driver, "147", "", "", new RuntimeException().getStackTrace()[0]);
			   
	           // LOGIN TO DRUPAL AS AN ADMIN:
	           helper.logIn(driver);
	           
	           // CREATE TITLE FOR CONTENT:
	           long fingerprint = System.currentTimeMillis();
	           title = String.valueOf(fingerprint) + " " +  helper.randomWord(Drupal.titleMaxCharsNumber - String.valueOf(fingerprint).length() - 1);
	           titleURL = helper.reFormatStringForURL(title, Drupal.titleMaxCharsNumber);
	           helper.fileWriterPrinter("\n" + "BRAND PAGE URL END:  " + titleURL);
	           
	           // CREATE DESCRIPTION FOR CONTENT:
	           description = helper.randomEnglishText(helper.randomInt((Drupal.descriptionMaxCharsNumber - 30), Drupal.descriptionMaxCharsNumber));
	           
	           // CREATE CONTENT WITH BOTH AGES SELECTED:
	           helper.createCharacterBrand(driver, title, description, "2654", false, true, true, true, true, true, new RuntimeException().getStackTrace()[0]);
	           helper.logOut(driver);
	           
	           // BANNER BUBBLE XPATH:
	           xpath = "//img[@alt='" + title + "']/parent::a";
	           helper.fileWriterPrinter("\n" + "BANNER BUBBLE LINK XPATH = " + xpath);
	           
	           // AGE 5 AND UNDER TEST:
	           helper.fileWriterPrinter("\n" + "AGE 5 AND UNDER TEST:");  
	           helper.getUrlWaitUntil(driver, 15, Common.fiveAndUnderURL);
	           helper.assertWebElementExist(driver, new Exception().getStackTrace()[0], xpath);
	           Thread.sleep(1000);
	           helper.clickToAppear(driver, Common.charBannerButtonLeft, Common.charBannerButtonRight, xpath, false, false);	
	           // NAVIGATE TO BRAND PAGE:
	           helper.clickLinkUrlWaitUntil(driver, 15, xpath, new Exception().getStackTrace()[0]);
	           // ASSERTIONS:
	           // ASSERT CHARACTER BUBBLE IS THE SAME BUBBLE DISPLAYED ON BANNER:
	           helper.fileWriterPrinter("\n" + "ASSERT CHARACTER BUBBLE IS THE SAME BUBBLE DISPLAYED ON BANNER:");
	           i = driver.findElements(By.xpath(Common.charBannerThumbnails)).size();
	           helper.fileWriterPrinter("CURRENT TOTAL NUMBER OF BANNER BUBBLES: " + i); 
	           actual = driver.findElement(By.xpath(Common.brandBubble)).getAttribute("src");
	           actual = actual.substring(actual.lastIndexOf("/") + 1, actual.length());
	           expected = driver.findElement(By.xpath(xpath + "/descendant::img[@src]")).getAttribute("src");
	           expected = expected.substring(expected.lastIndexOf("/") + 1 , expected.length());
	           helper.assertEquals(driver, new Exception().getStackTrace()[0], actual, expected);
	           // ASSERT CHARACTER TITLE IS THE NAME OF THE BRAND PAGE:
	           helper.fileWriterPrinter("\n" + "ASSERT CHARACTER TITLE IS THE NAME OF THE BRAND PAGE:");
	           actual = driver.findElement(By.xpath(Common.brandTitle)).getText();
	           expected = title;
	           helper.assertEquals(driver, new Exception().getStackTrace()[0], actual, expected);
	           // ASSERT SCHEDULE INFORMATION: N/A FOR CUSTOM BRAND
	           // ASSERT THUMBNAIL:
	           helper.fileWriterPrinter("\n" + "ASSERT THUMBNAIL:");
	           expected = "hero.jpg";
	           actual = driver.findElement(By.xpath(Common.brandHeroBoxImage)).getAttribute("src");
	           actual = helper.trimLongerFilenameAsPerShorter(actual, expected);
	           helper.assertEquals(driver, new Exception().getStackTrace()[0], actual, expected);          
	           // ASSERT DESCRIPTION IS DESCRIPTION OF THE BRAND PAGE:
	           helper.fileWriterPrinter("ASSERT DESCRIPTION IS DESCRIPTION OF THE BRAND PAGE:");
	           actual = helper.getText(driver, Common.brandDescription);
	           expected = description;
	           helper.assertEquals(driver, new Exception().getStackTrace()[0], actual, expected);
	           
	           // AGE 6 AND OVER TEST:
	           helper.fileWriterPrinter("\n" + "AGE 6 AND OVER TEST:");  
	           helper.getUrlWaitUntil(driver, 15, Common.sixAndOverURL);
	           helper.assertWebElementExist(driver, new Exception().getStackTrace()[0], xpath);
	           Thread.sleep(1000);
	           helper.clickToAppear(driver, Common.charBannerButtonLeft, Common.charBannerButtonRight, xpath, false, false);	
	           // NAVIGATE TO BRAND PAGE:
	           helper.clickLinkUrlWaitUntil(driver, 15, xpath, new Exception().getStackTrace()[0]);
	           // ASSERTIONS:
	           // ASSERT CHARACTER BUBBLE IS THE SAME BUBBLE DISPLAYED ON BANNER:
	           helper.fileWriterPrinter("\n" + "ASSERT CHARACTER BUBBLE IS THE SAME BUBBLE DISPLAYED ON BANNER:");
	           i = driver.findElements(By.xpath(Common.charBannerThumbnails)).size();
	           helper.fileWriterPrinter("CURRENT TOTAL NUMBER OF BANNER BUBBLES: " + i); 
	           actual = driver.findElement(By.xpath(Common.brandBubble)).getAttribute("src");
	           actual = actual.substring(actual.lastIndexOf("/") + 1, actual.length());
	           expected = driver.findElement(By.xpath(xpath + "/descendant::img[@src]")).getAttribute("src");
	           expected = expected.substring(expected.lastIndexOf("/") + 1 , expected.length());
	           helper.assertEquals(driver, new Exception().getStackTrace()[0], actual, expected);
	           // ASSERT CHARACTER TITLE IS THE NAME OF THE BRAND PAGE:
	           helper.fileWriterPrinter("\n" + "ASSERT CHARACTER TITLE IS THE NAME OF THE BRAND PAGE:");
	           actual = driver.findElement(By.xpath(Common.brandTitle)).getText();
	           expected = title;
	           helper.assertEquals(driver, new Exception().getStackTrace()[0], actual, expected);
	           // ASSERT SCHEDULE INFORMATION: N/A FOR CUSTOM BRAND
	           // ASSERT THUMBNAIL:
	           helper.fileWriterPrinter("\n" + "ASSERT THUMBNAIL:");
	           expected = "hero.jpg";
	           actual = driver.findElement(By.xpath(Common.brandHeroBoxImage)).getAttribute("src");
	           actual = helper.trimLongerFilenameAsPerShorter(actual, expected);
	           helper.assertEquals(driver, new Exception().getStackTrace()[0], actual, expected);          
	           // ASSERT DESCRIPTION IS DESCRIPTION OF THE BRAND PAGE:
	           helper.fileWriterPrinter("ASSERT DESCRIPTION IS DESCRIPTION OF THE BRAND PAGE:");
	           actual = helper.getText(driver, Common.brandDescription);
	           expected = description;
	           helper.assertEquals(driver, new Exception().getStackTrace()[0], actual, expected);
		    		   
	   } catch(Exception e) { helper.getExceptionDescriptive(e, new Exception().getStackTrace()[0], driver); }
	}
	
	/**
	 * Test Hero Box on Existing Custom Brand page
	 * <p>Date Created: 2016-10-25</p>
	 * <p>Date Modified: 2016-10-25</p>
	 * <p>Original Version: V1</p>
	 * <p>Modified Version: </p>
	 * <p>Xpath: 1</p>
	 * <p>Test Cases: 35474 2079 972</p>
	 */
	@Test(groups = {"TC-35474","US-2079","BUG-972","NEW"}, priority = 54)
	public void testHeroBoxOnExistingCustomBrandPage() throws IOException {
		   try{
			   // INITIALISATION:
		       helper.printXmlPath(new RuntimeException().getStackTrace()[0]);
		       driver = helper.getServerName(driver);
		
		       // DECLARATION:
		       String title, titleURL, xpath, description, hero, actual, expected;
		       String[] content;
		       Boolean ifContent;
		       int i;
		       
	           // CLEAN-UP:
	           helper.deleteAllContent(driver, "147", "", "", new RuntimeException().getStackTrace()[0]);
	           
	           // AGE 5 AND UNDER TEST:
	           helper.fileWriterPrinter("\n" + "AGE 5 AND UNDER TEST:");
	           // LOGIN TO DRUPAL AS A CONTENT EDITOR:
	           helper.logIn(driver, Common.contentEditorUsername, Common.userPassword(Common.contentEditorUsername));
	           // REOPEN EXISTING CUSTOM BRAND:
	           ifContent = helper.reopenBrand(driver, "", "Custom Brand", "", "Yes", true, false, false, new RuntimeException().getStackTrace()[0], 7, "CHECKED");
	           // CHECK IF AGE 5 AND UNDER CUSTOM BRAND PAGES EXIST:
	           if(ifContent) {   
	           // READ BRAND DATA:
	           content = helper.readContent(driver, new RuntimeException().getStackTrace()[0], true);
	           helper.logOut(driver);
	           title = content[0];
	           description = content[1];
	           hero = content[8];
	           // BANNER BUBBLE XPATH:
	           titleURL = helper.reFormatStringForURL(title, Drupal.titleMaxCharsNumber);
	           helper.fileWriterPrinter("\n" + "BRAND PAGE URL END:  " + titleURL);
	           // NAVIGATE TO AGE 5 AND UNDER PAGE:
	           helper.getUrlWaitUntil(driver, 15, Common.fiveAndUnderURL);
	           Thread.sleep(1000);
	           // BANNER BUBBLE XPATH:
	           xpath = "//img[@alt='" + title + "']/parent::a";
	           helper.fileWriterPrinter("\n" + "BANNER BUBBLE LINK XPATH = " + xpath);
	           helper.clickToAppear(driver, Common.charBannerButtonLeft, Common.charBannerButtonRight, xpath, false, false);	
	           // NAVIGATE TO BRAND PAGE:
	           helper.clickLinkUrlWaitUntil(driver, 15, xpath, new Exception().getStackTrace()[0]);
	           // ASSERTIONS:
	           // ASSERT CHARACTER BUBBLE IS THE SAME BUBBLE DISPLAYED ON BANNER:
	           helper.fileWriterPrinter("\n" + "ASSERT CHARACTER BUBBLE IS THE SAME BUBBLE DISPLAYED ON BANNER:");
	           i = driver.findElements(By.xpath(Common.charBannerThumbnails)).size();
	           helper.fileWriterPrinter("CURRENT TOTAL NUMBER OF BANNER BUBBLES: " + i);
	           actual = driver.findElement(By.xpath(Common.brandBubble)).getAttribute("src");
	           actual = actual.substring(actual.lastIndexOf("/") + 1, actual.length());
	           expected = driver.findElement(By.xpath(xpath + "/descendant::img[@src]")).getAttribute("src");
	           expected = expected.substring(expected.lastIndexOf("/") + 1 , expected.length());
	           helper.assertEquals(driver, new Exception().getStackTrace()[0], actual, expected);
	           // ASSERT CHARACTER TITLE IS THE NAME OF THE BRAND PAGE:
	           helper.fileWriterPrinter("\n" + "ASSERT CHARACTER TITLE IS THE NAME OF THE BRAND PAGE:");
	           actual = helper.getText(driver, Common.brandTitle);
	           expected = title;
	           helper.assertEquals(driver, new Exception().getStackTrace()[0], actual, expected);
	           // ASSERT SCHEDULE INFORMATION: N/A FOR CUSTOM BRAND
	           // ASSERT THUMBNAIL:
	           helper.fileWriterPrinter("\n" + "ASSERT THUMBNAIL:");
	           expected = hero;
	           actual = driver.findElement(By.xpath(Common.brandHeroBoxImage)).getAttribute("src");
	           actual = helper.trimLongerFilenameAsPerShorter(actual, expected);
	           helper.assertEquals(driver, new Exception().getStackTrace()[0], actual, expected);          
	           // ASSERT DESCRIPTION IS DESCRIPTION OF THE BRAND PAGE:
	           helper.fileWriterPrinter("ASSERT DESCRIPTION IS DESCRIPTION OF THE BRAND PAGE:");
	           actual = helper.getText(driver, Common.brandDescription);
	           expected = description;
	           helper.assertEquals(driver, new Exception().getStackTrace()[0], actual, expected);
	           }
	           
	           // AGE 6 AND OVER TEST:
	           helper.fileWriterPrinter("\n" + "AGE 6 AND OVER TEST:");
	           // LOGIN TO DRUPAL AS A CONTENT EDITOR:
	           helper.logIn(driver, Common.contentEditorUsername, Common.userPassword(Common.contentEditorUsername));
	           // REOPEN EXISTING CUSTOM BRAND:
	           ifContent = helper.reopenBrand(driver, "", "Custom Brand", "", "Yes", false, true, false, new RuntimeException().getStackTrace()[0], 7, "CHECKED");
	           // CHECK IF AGE 6 AND OVER CUSTOM BRAND PAGES EXIST:
	           if(ifContent) {   
	           // READ BRAND DATA:
	           content = helper.readContent(driver, new RuntimeException().getStackTrace()[0], true);
	           helper.logOut(driver);
	           title = content[0];
	           description = content[1];
	           hero = content[8];
	           // BANNER BUBBLE XPATH:
	           titleURL = helper.reFormatStringForURL(title, Drupal.titleMaxCharsNumber);
	           helper.fileWriterPrinter("\n" + "BRAND PAGE URL END:  " + titleURL);
	           // NAVIGATE TO AGE 6 AND OVER PAGE:
	           helper.getUrlWaitUntil(driver, 15, Common.sixAndOverURL);
	           Thread.sleep(1000);
	           // BANNER BUBBLE XPATH:
	           xpath = "//img[@alt='" + title + "']/parent::a";
	           helper.fileWriterPrinter("\n" + "BANNER BUBBLE LINK XPATH = " + xpath);
	           helper.clickToAppear(driver, Common.charBannerButtonLeft, Common.charBannerButtonRight, xpath, false, false);	
	           // NAVIGATE TO BRAND PAGE:
	           helper.clickLinkUrlWaitUntil(driver, 15, xpath, new Exception().getStackTrace()[0]);
	           // ASSERTIONS:
	           // ASSERT CHARACTER BUBBLE IS THE SAME BUBBLE DISPLAYED ON BANNER:
	           helper.fileWriterPrinter("\n" + "ASSERT CHARACTER BUBBLE IS THE SAME BUBBLE DISPLAYED ON BANNER:");
	           i = driver.findElements(By.xpath(Common.charBannerThumbnails)).size();
	           helper.fileWriterPrinter("CURRENT TOTAL NUMBER OF BANNER BUBBLES: " + i);
	           actual = driver.findElement(By.xpath(Common.brandBubble)).getAttribute("src");
	           actual = actual.substring(actual.lastIndexOf("/") + 1, actual.length());
	           expected = driver.findElement(By.xpath(xpath + "/descendant::img[@src]")).getAttribute("src");
	           expected = expected.substring(expected.lastIndexOf("/") + 1 , expected.length());
	           helper.assertEquals(driver, new Exception().getStackTrace()[0], actual, expected);
	           // ASSERT CHARACTER TITLE IS THE NAME OF THE BRAND PAGE:
	           helper.fileWriterPrinter("\n" + "ASSERT CHARACTER TITLE IS THE NAME OF THE BRAND PAGE:");
	           actual = helper.getText(driver, Common.brandTitle);
	           expected = title;
	           helper.assertEquals(driver, new Exception().getStackTrace()[0], actual, expected);
	           // ASSERT SCHEDULE INFORMATION: N/A FOR CUSTOM BRAND
	           // ASSERT THUMBNAIL:
	           helper.fileWriterPrinter("\n" + "ASSERT THUMBNAIL:");
	           expected = hero;
	           actual = driver.findElement(By.xpath(Common.brandHeroBoxImage)).getAttribute("src");
	           actual = helper.trimLongerFilenameAsPerShorter(actual, expected);
	           helper.assertEquals(driver, new Exception().getStackTrace()[0], actual, expected);          
	           // ASSERT DESCRIPTION IS DESCRIPTION OF THE BRAND PAGE:
	           helper.fileWriterPrinter("ASSERT DESCRIPTION IS DESCRIPTION OF THE BRAND PAGE:");
	           actual = helper.getText(driver, Common.brandDescription);
	           expected = description;
	           helper.assertEquals(driver, new Exception().getStackTrace()[0], actual, expected);
	           } 
	           
	   } catch(Exception e) { helper.getExceptionDescriptive(e, new Exception().getStackTrace()[0], driver); }
	}
	
	/**
	 * Test Hero Box on Existing Character Brand page
	 * <p>Date Created: 2016-10-25</p>
	 * <p>Date Modified: 2016-10-25</p>
	 * <p>Original Version: V1</p>
	 * <p>Modified Version: </p>
	 * <p>Xpath: 1</p>
	 * <p>Test Cases: 35474 2079 972</p>
	 */
	@Test(groups = {"TC-35474","US-2079","BUG-972","NEW"}, priority = 55)
	public void testHeroBoxOnExistingCharacterBrandPage() throws IOException {
		   try{
			   // INITIALISATION:
		       helper.printXmlPath(new RuntimeException().getStackTrace()[0]);
		       driver = helper.getServerName(driver);
		
		       // DECLARATION:
		       String title, titleURL, xpath, description, hero, actual, expected;
		       String[] content;
		       Boolean ifContent;
		       int i;
		       
	           // CLEAN-UP:
	           helper.deleteAllContent(driver, "147", "", "", new RuntimeException().getStackTrace()[0]);
	           
	           // AGE 5 AND UNDER TEST:
	           helper.fileWriterPrinter("\n" + "AGE 5 AND UNDER TEST:");
	           // LOGIN TO DRUPAL AS A CONTENT EDITOR:
	           helper.logIn(driver, Common.contentEditorUsername, Common.userPassword(Common.contentEditorUsername));
	           // REOPEN EXISTING CHARACTER BRAND:
	           ifContent = helper.reopenBrand(driver, "", "Character Brand", "", "Yes", true, false, false, new RuntimeException().getStackTrace()[0], 7, "CHECKED");
	           // CHECK IF AGE 5 AND UNDER CHARACTER BRAND PAGES EXIST:
	           if(ifContent) {   
	           // READ BRAND DATA:
	           content = helper.readContent(driver, new RuntimeException().getStackTrace()[0], true);
	           helper.logOut(driver);
	           title = content[0];
	           description = content[1];
	           hero = content[8];
	           // BANNER BUBBLE XPATH:
	           titleURL = helper.reFormatStringForURL(title, Drupal.titleMaxCharsNumber);
	           helper.fileWriterPrinter("\n" + "BRAND PAGE URL END:  " + titleURL);
	           xpath = "//img[@alt='" + title + "']/parent::a";
	           helper.fileWriterPrinter("\n" + "BANNER BUBBLE LINK XPATH = " + xpath);	           
	           // NAVIGATE TO AGE 5 AND UNDER PAGE:
	           helper.getUrlWaitUntil(driver, 15, Common.fiveAndUnderURL);
	           Thread.sleep(1000);
	           helper.clickToAppear(driver, Common.charBannerButtonLeft, Common.charBannerButtonRight, xpath, false, false);	
	           // NAVIGATE TO BRAND PAGE:
	           helper.clickLinkUrlWaitUntil(driver, 15, xpath, new Exception().getStackTrace()[0]);	
	           // ASSERTIONS:
	           // ASSERT CHARACTER BUBBLE IS THE SAME BUBBLE DISPLAYED ON BANNER:
	           helper.fileWriterPrinter("\n" + "ASSERT CHARACTER BUBBLE IS THE SAME BUBBLE DISPLAYED ON BANNER:");
	           i = driver.findElements(By.xpath(Common.charBannerThumbnails)).size();
	           helper.fileWriterPrinter("CURRENT TOTAL NUMBER OF BANNER BUBBLES: " + i); 
	           actual = driver.findElement(By.xpath(Common.brandBubble)).getAttribute("src");
	           actual = actual.substring(actual.lastIndexOf("/") + 1, actual.length());
	           expected = driver.findElement(By.xpath(xpath + "/descendant::img[@src]")).getAttribute("src");
	           expected = expected.substring(expected.lastIndexOf("/") + 1 , expected.length());
	           helper.assertEquals(driver, new Exception().getStackTrace()[0], actual, expected);
	           // ASSERT CHARACTER TITLE IS THE NAME OF THE BRAND PAGE:
	           helper.fileWriterPrinter("\n" + "ASSERT CHARACTER TITLE IS THE NAME OF THE BRAND PAGE:");
	           actual = helper.getText(driver, Common.brandTitle);
	           expected = title;
	           helper.assertEquals(driver, new Exception().getStackTrace()[0], actual, expected);
	           // ASSERT SCHEDULE INFORMATION: N/A FOR CUSTOM BRAND
	           // ASSERT THUMBNAIL:
	           helper.fileWriterPrinter("\n" + "ASSERT THUMBNAIL:");
	           expected = hero;
	           actual = driver.findElement(By.xpath(Common.brandHeroBoxImage)).getAttribute("src");
	           actual = helper.trimLongerFilenameAsPerShorter(actual, expected);
	           helper.assertEquals(driver, new Exception().getStackTrace()[0], actual, expected);          
	           // ASSERT DESCRIPTION IS DESCRIPTION OF THE BRAND PAGE:
	           helper.fileWriterPrinter("ASSERT DESCRIPTION IS DESCRIPTION OF THE BRAND PAGE:");
	           actual = helper.getText(driver, Common.brandDescription);
	           expected = description;
	           helper.assertEquals(driver, new Exception().getStackTrace()[0], actual, expected);
	           }
	           
	           // AGE 6 AND OVER TEST:
	           helper.fileWriterPrinter("\n" + "AGE 6 AND OVER TEST:");
	           // LOGIN TO DRUPAL AS A CONTENT EDITOR:
	           helper.logIn(driver, Common.contentEditorUsername, Common.userPassword(Common.contentEditorUsername));
	           // REOPEN EXISTING CHARACTER BRAND:
	           ifContent = helper.reopenBrand(  driver, "", "Character Brand", "", "Yes", false, true, false, new RuntimeException().getStackTrace()[0], 7, "CHECKED");
	           // CHECK IF AGE 6 AND OVER CHARACTER BRAND PAGES EXIST:
	           if(ifContent) {   
	           // READ BRAND DATA:
	           content = helper.readContent(driver, new RuntimeException().getStackTrace()[0], true);
	           helper.logOut(driver);
	           title = content[0];
	           description = content[1];
	           hero = content[8];
	           // BANNER BUBBLE XPATH:
	           titleURL = helper.reFormatStringForURL(title, Drupal.titleMaxCharsNumber);
	           helper.fileWriterPrinter("\n" + "BRAND PAGE URL END:  " + titleURL);
	           xpath = "//img[@alt='" + title + "']/parent::a";
	           helper.fileWriterPrinter("\n" + "BANNER BUBBLE LINK XPATH = " + xpath);	           
	           // NAVIGATE TO AGE 6 AND OVER PAGE:
	           helper.getUrlWaitUntil(driver, 15, Common.sixAndOverURL);
	           Thread.sleep(1000);
	           helper.clickToAppear(driver, Common.charBannerButtonLeft, Common.charBannerButtonRight, xpath, false, false);	
	           // NAVIGATE TO BRAND PAGE:
	           helper.clickLinkUrlWaitUntil(driver, 15, xpath, new Exception().getStackTrace()[0]);	
	           // ASSERTIONS:
	           // ASSERT CHARACTER BUBBLE IS THE SAME BUBBLE DISPLAYED ON BANNER:
	           helper.fileWriterPrinter("\n" + "ASSERT CHARACTER BUBBLE IS THE SAME BUBBLE DISPLAYED ON BANNER:");
	           i = driver.findElements(By.xpath(Common.charBannerThumbnails)).size();
	           helper.fileWriterPrinter("CURRENT TOTAL NUMBER OF BANNER BUBBLES: " + i); 
	           actual = driver.findElement(By.xpath(Common.brandBubble)).getAttribute("src");
	           actual = actual.substring(actual.lastIndexOf("/") + 1, actual.length());
	           expected = driver.findElement(By.xpath(xpath + "/descendant::img[@src]")).getAttribute("src");
	           expected = expected.substring(expected.lastIndexOf("/") + 1 , expected.length());
	           helper.assertEquals(driver, new Exception().getStackTrace()[0], actual, expected);
	           // ASSERT CHARACTER TITLE IS THE NAME OF THE BRAND PAGE:
	           helper.fileWriterPrinter("\n" + "ASSERT CHARACTER TITLE IS THE NAME OF THE BRAND PAGE:");
	           actual = helper.getText(driver, Common.brandTitle);
	           expected = title;
	           helper.assertEquals(driver, new Exception().getStackTrace()[0], actual, expected);
	           // ASSERT SCHEDULE INFORMATION: N/A FOR CUSTOM BRAND
	           // ASSERT THUMBNAIL:
	           helper.fileWriterPrinter("\n" + "ASSERT THUMBNAIL:");
	           expected = hero;
	           actual = driver.findElement(By.xpath(Common.brandHeroBoxImage)).getAttribute("src");
	           actual = helper.trimLongerFilenameAsPerShorter(actual, expected);
	           helper.assertEquals(driver, new Exception().getStackTrace()[0], actual, expected);          
	           // ASSERT DESCRIPTION IS DESCRIPTION OF THE BRAND PAGE:
	           helper.fileWriterPrinter("ASSERT DESCRIPTION IS DESCRIPTION OF THE BRAND PAGE:");
	           actual = helper.getText(driver, Common.brandDescription);
	           expected = description;
	           helper.assertEquals(driver, new Exception().getStackTrace()[0], actual, expected);
	           } 
	           
	   } catch(Exception e) { helper.getExceptionDescriptive(e, new Exception().getStackTrace()[0], driver); }
	}
	
	/**
	 * Test interactive Tile-Hover on Desktop
	 * <p>Date Created: 2016-10-31</p>
	 * <p>Date Modified: 2016-10-31</p>
	 * <p>Original Version: V1</p>
	 * <p>Modified Version: </p>
	 * <p>Xpath: 1</p>
	 * <p>Test Cases: 36143 3258</p>
	 */
	@Test(groups = {"TC-36143","US-3258"}, priority = 56)
    public void testInteractiveTileHoverOnDesktop() throws IOException, AWTException {
	   try{
    	   // INITIALISATION:
           helper.printXmlPath(new RuntimeException().getStackTrace()[0]);
           driver = helper.getServerName(driver);
           
           // CLEAN-UP:
           helper.deleteAllContent(driver, "147", "", "", new RuntimeException().getStackTrace()[0]);
         
           // LOGIN TO DRUPAL AS A CONTENT EDITOR:
           helper.logIn(driver, Common.contentEditorUsername, Common.userPassword(Common.contentEditorUsername));
           
           // DECLARATION:
           int total = 2;
           String[] title = new String[total];
           String[] titleURL = new String[total];
           String[] description = new String[total];
           String[] xpath = new String[total];
           String[] tileXpath = new String[total];
           long[] fingerprint = new long[total];
           String tile = "", scale, Scale;
           
           for (int i = 0; i < total; i++) {
        	   // CREATE TITLES FOR CONTENTS:
        	   fingerprint[i] = System.currentTimeMillis();
        	   title[i] = String.valueOf(fingerprint[i]) + " " + (i + 1) + "-" 
      	                + helper.randomWord(Drupal.titleMaxCharsNumber - String.valueOf(fingerprint[i]).length() - (" " + (i + 1) + "-").length());
        	   titleURL[i] = helper.reFormatStringForURL(title[i], Drupal.titleMaxCharsNumber);
        	   // CREATE DESCRIPTION FOR CONTENT:
        	   description[i] = helper.randomEnglishText(helper.randomInt((Drupal.descriptionMaxCharsNumber - 30), Drupal.descriptionMaxCharsNumber));
        	   // BANNER BUBBLE XPATH:
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
       
           // AGE 5 AND UNDER BEFORE-AFTER TILE-HOVER TEST:
           helper.fileWriterPrinter("\n" + "AGE 5 AND UNDER BEFORE-AFTER TILE-HOVER TEST:");  
           helper.getUrlWaitUntil(driver, 15, Common.fiveAndUnderURL);
           helper.clickToAppear(driver, Common.charBannerButtonLeft, Common.charBannerButtonRight, xpath[0], false, false);	           
           // NAVIGATE TO BRAND PAGE:
           helper.clickLinkUrlWaitUntil(driver, 15, xpath[0], new Exception().getStackTrace()[0]);
           // ASSERT TILE(S) EXIST:
           for (int i = total - 1; i > 0; i--) {
        	   // ASSERT TILE EXIST:
        	   helper.assertWebElementExist(driver, new Exception().getStackTrace()[0], tileXpath[i]);
        	   // MEASURE TILE SCALE BEFORE HOVER:
        	   scale = driver.findElement(By.xpath(tileXpath[i])).getCssValue("transform");
        	   helper.fileWriterPrinter("\nBEFORE HOWER: SCALE = " + scale);
        	   // MEASURE TILE SCALE AFTER HOVER:
        	   helper.hoverElement(driver, tileXpath[i]);
        	   Thread.sleep(1000);
        	   Scale = driver.findElement(By.xpath(tileXpath[i])).getCssValue("transform");
        	   helper.fileWriterPrinter(" AFTER HOWER: SCALE = " + Scale);
        	   // ASSERT TILE BEFORE AND AFTER HOVER:
        	   helper.assertBooleanFalse(driver, new Exception().getStackTrace()[0], scale.equals(Scale));
        	   }
           
           // AGE 6 AND OVER BEFORE-AFTER TILE-HOVER TEST:
           helper.fileWriterPrinter("\n" + "AGE 6 AND OVER BEFORE-AFTER TILE-HOVER TEST:");  
           helper.getUrlWaitUntil(driver, 15, Common.sixAndOverURL);
           helper.clickToAppear(driver, Common.charBannerButtonLeft, Common.charBannerButtonRight, xpath[0], false, false);	           
           // NAVIGATE TO BRAND PAGE:
           helper.clickLinkUrlWaitUntil(driver, 15, xpath[0], new Exception().getStackTrace()[0]);
           // ASSERT TILE(S) EXIST:
           for (int i = total - 1; i > 0; i--) {
        	   // ASSERT TILE EXIST:
        	   helper.assertWebElementExist(driver, new Exception().getStackTrace()[0], tileXpath[i]);
        	   // MEASURE TILE SCALE BEFORE HOVER:
        	   scale = driver.findElement(By.xpath(tileXpath[i])).getCssValue("transform");
        	   helper.fileWriterPrinter("\nBEFORE HOWER: SCALE = " + scale);
        	   // MEASURE TILE SCALE AFTER HOVER:
        	   helper.hoverElement(driver, tileXpath[i]);
        	   Thread.sleep(1000);
        	   Scale = driver.findElement(By.xpath(tileXpath[i])).getCssValue("transform");
        	   helper.fileWriterPrinter(" AFTER HOWER: SCALE = " + Scale);
        	   // ASSERT TILE BEFORE AND AFTER HOVER:
        	   helper.assertBooleanFalse(driver, new Exception().getStackTrace()[0], scale.equals(Scale));
        	   }
           
	   } catch(IOException | InterruptedException /*| AWTException*/ e) { helper.getExceptionDescriptive(e, new Exception().getStackTrace()[0], driver); }
	}
	
	/**
	 * Test view Transcript page (click on video player "View Transcript" link opens "View transcript" page where user can see transcript)
	 * <p>Date Created: 2016-11-02</p>
	 * <p>Date Modified: 2016-11-02</p>
	 * <p>Original Version: V1</p>
	 * <p>Modified Version: </p>
	 * <p>Xpath: 1</p>
	 * <p>Test Cases: 36309 3964</p>
	 */
	@Test(groups = {"TC-36309","US-3964"}, priority = 57)
    public void testViewTranscriptPage() throws IOException{
	   try{
    	   // DECLARATION:
           String title, titleURL, brightcoveRefID, telescopeAssetId, description, xpath, videoTitle, actual, expected, expectedURL;
    	   By locator;
    	   
           // INITIALISATION:
           helper.printXmlPath(new RuntimeException().getStackTrace()[0]);
           driver = helper.getServerName(driver);
           
           // CLEAN-UP:
           helper.deleteAllContent(driver, "147", "", "", new RuntimeException().getStackTrace()[0]);

           // LOGIN TO DRUPAL AS AN ADMIN:
           helper.logIn(driver);
           
           // CREATE TITLE FOR CONTENT:
           long fingerprint = System.currentTimeMillis();
           title = String.valueOf(fingerprint) + " " +  helper.randomWord(Drupal.titleMaxCharsNumber - String.valueOf(fingerprint).length() - 1);
           titleURL = helper.reFormatStringForURL(title, Drupal.titleMaxCharsNumber);
           helper.fileWriterPrinter("\n" + "BRAND PAGE URL END:  " + titleURL);
           
           // THIS IS SUPPLIED BY CPAD (IN ORDER FOR THE VIDEO TO PLAY):
           brightcoveRefID = "3022354586001";
           telescopeAssetId = "120141X";
           
           // CREATE DESCRIPTION FOR CONTENT:
           description = helper.randomEnglishText(helper.randomInt((Drupal.descriptionMaxCharsNumber - 30), Drupal.descriptionMaxCharsNumber));
           
           // CREATE CONTENT WITH AGE 5 SELECTED:
           helper.createCharacterBrand(driver, title, description, telescopeAssetId, true, true, false, false, true, true, new Exception().getStackTrace()[0]);
           
           // BANNER BUBBLE XPATH:
           xpath = "//img[@alt='" + title + "']/parent::a";
           helper.fileWriterPrinter("\n" + "BANNER BUBBLE LINK XPATH = " + xpath);
           
           // AGE 5 AND UNDER TRANSCRIPT PAGE TEST:
           helper.fileWriterPrinter("\n" + "AGE 5 AND UNDER TRANSCRIPT PAGE TEST:"); 
           // CREATE VIDEO WITH AGE 5 SELECTED:
           fingerprint = System.currentTimeMillis();
           videoTitle = String.valueOf(fingerprint) + " " +  helper.randomWord(Drupal.titleMaxCharsNumber - String.valueOf(fingerprint).length() - 1);
           helper.createVideo(driver, videoTitle, "Short Description", "Long Description", title, brightcoveRefID, telescopeAssetId, true, false, true, true, true, new Exception().getStackTrace()[0]);
           // ASSERT VIDEO URL:
           expectedURL = Common.fiveAndUnderVideoURL(title, videoTitle);
           helper.checkCurrentURL(driver, new Exception().getStackTrace()[0], expectedURL);
           // LOG-OUT:
           helper.logOut(driver);
           helper.getUrlWaitUntil(driver, 10, expectedURL, false);
           // ASSERT TRANSCRIPT LINK:
           helper.assertWebElementExist(driver, new Exception().getStackTrace()[0], By.linkText(Common.videoTranscriptLinkText));
           // ASSERT TRANSCRIPT LINK URL:
           expectedURL = Common.fiveAndUnderTranscriptURL(title, videoTitle, telescopeAssetId);
           helper.checkLinkURL(driver, new Exception().getStackTrace()[0], Common.videoTranscriptLinkXpath, expectedURL);
           // ASSERT CLICK ON TRANSCRIPT LINK OPENS TRANSCRIPT PAGE:
           locator = By.linkText(Common.videoTranscriptLinkText);
           helper.clickLinkAndCheckURL(driver, new Exception().getStackTrace()[0], locator, expectedURL, false, false);
           // ASSERT TRANSCRIPT PAGE KEY-ELEMENTS APPEAR:
           xpath = Common.transcriptPageTitleXpath;
           helper.assertWebElementExist(driver, new Exception().getStackTrace()[0], xpath);
           expected = Common.transcriptPageTitleText;
           actual = driver.findElement(By.xpath(xpath)).getText();
           actual = helper.getStringBeginning(actual, expected.length());
           helper.assertEquals(driver, new Exception().getStackTrace()[0], actual, expected);
        
           // AGE 6 AND OVER TRANSCRIPT PAGE TEST:
           helper.fileWriterPrinter("\n" + "AGE 6 AND OVER TRANSCRIPT PAGE TEST:");
           // LOGIN TO DRUPAL AS AN ADMIN:
           helper.logIn(driver);
           // CREATE VIDEO WITH AGE 6 SELECTED:
           fingerprint = System.currentTimeMillis();
           videoTitle = String.valueOf(fingerprint) + " " +  helper.randomWord(Drupal.titleMaxCharsNumber - String.valueOf(fingerprint).length() - 1);
           helper.createVideo(driver, videoTitle, "Short Description", "Long Description", title, brightcoveRefID, telescopeAssetId, false, true, true, true, true, new Exception().getStackTrace()[0]);
           // ASSERT VIDEO URL:
           expectedURL = Common.sixAndOverVideoURL(title, videoTitle);
           helper.checkCurrentURL(driver, new Exception().getStackTrace()[0], expectedURL);
           // LOG-OUT:
           helper.logOut(driver);
           helper.getUrlWaitUntil(driver, 10, expectedURL, false);
           // ASSERT TRANSCRIPT LINK:
           helper.assertWebElementExist(driver, new Exception().getStackTrace()[0], By.linkText(Common.videoTranscriptLinkText));
           // ASSERT TRANSCRIPT LINK URL:
           expectedURL = Common.sixAndOverTranscriptURL(title, videoTitle, telescopeAssetId);
           helper.checkLinkURL(driver, new Exception().getStackTrace()[0], Common.videoTranscriptLinkXpath, expectedURL);
           // ASSERT CLICK ON TRANSCRIPT LINK OPENS TRANSCRIPT PAGE:
           locator = By.linkText(Common.videoTranscriptLinkText);
           helper.clickLinkAndCheckURL(driver, new Exception().getStackTrace()[0], locator, expectedURL, false, false);
           // ASSERT TRANSCRIPT PAGE KEY-ELEMENTS APPEAR:
           xpath = Common.transcriptPageTitleXpath;
           helper.assertWebElementExist(driver, new Exception().getStackTrace()[0], xpath);
           expected = Common.transcriptPageTitleText;
           actual = driver.findElement(By.xpath(xpath)).getText();
           actual = helper.getStringBeginning(actual, expected.length());
           helper.assertEquals(driver, new Exception().getStackTrace()[0], actual, expected);
           
	   } catch(Exception e) { helper.getExceptionDescriptive(e, new Exception().getStackTrace()[0], driver); }
	}
	
	/**
	 * Test "The Space" Character Brand front-end
	 * <p>Date Created: 2016-11-03</p>
	 * <p>Date Modified: 2016-11-03</p>
	 * <p>Original Version: V1</p>
	 * <p>Modified Version: </p>
	 * <p>Xpath: 1</p>
	 * <p>Test Cases: 36418 3711</p>
	 */
	@Test(groups = {"TC-36418","US-3711"}, priority = 58)
    public void testTheSpaceFrontEnd() throws IOException{
	   try{
    	   // DECLARATION:
           String title, titleURL, xpath, tab, browse, upload, url, actual, expected;
    	   Boolean ifContent, ifVisible, ifPublished;
    	   Boolean ifBubble = false, ifHero = false, ifSmallTile = false;
    	   int i;
    	   
           // INITIALISATION:
           helper.printXmlPath(new RuntimeException().getStackTrace()[0]);
           driver = helper.getServerName(driver);
           
           // CLEAN-UP:
           helper.deleteAllContent(driver, "147", "", "", new RuntimeException().getStackTrace()[0]);
           
           // ASSIGN CONTENT TITLE:
           title = "The Space";
           
           // STEP-1:
           // LOGIN TO DRUPAL AS AN ADMIN:
           helper.fileWriterPrinter("\n" + "STEP-1\n" + "LOGIN TO DRUPAL AS AN ADMIN:");
           helper.logIn(driver);
           
           // STEP-2:
           // RE-OPEN THE SPACE BRAND PAGE BACK-END:
           helper.fileWriterPrinter("\n" + "STEP-2\n" + "RE-OPEN THE SPACE BRAND PAGE BACK-END:");
           ifContent = helper.reopenBrand( driver, title, "Character Brand", "", "", false, true, false, new RuntimeException().getStackTrace()[0], 3, "CHECKED");
           // CHECK IF AGE 6 AND OVER CHARACTER BRAND PAGES EXIST:
           if(ifContent) {   
           // READ BRAND DATA:
           String[] content = helper.readContent(driver, new RuntimeException().getStackTrace()[0], false);
           // ASSERT TITLE:
           String actualTitle = content[0];
           helper.assertBooleanTrue(driver, new RuntimeException().getStackTrace()[0], actualTitle.contains(title));
           title = actualTitle;
           // ASSERT AGE GROUP:
           String expectedAgeGroup5 = "UN-CHECKED";
           String expectedAgeGroup6 = "CHECKED";
           String actualAgeGroup5= content[2];
           String actualAgeGroup6= content[3];
           helper.assertEquals(driver, new Exception().getStackTrace()[0], actualAgeGroup5, expectedAgeGroup5);
           helper.assertEquals(driver, new Exception().getStackTrace()[0], actualAgeGroup6, expectedAgeGroup6);         
           
           // CHECKING VISIBLE ON CHARACTER BANNER CHECK BOX AND CLCK IF REQUIRED:
           ifVisible = content[7].equals("UN-CHECKED");
           tab = Drupal.characterBannerVerticalTab;
   		   helper.uploadReader(driver, tab, "");
           helper.checkVisibleOnCharacterBanner(driver, ifVisible, new Exception().getStackTrace()[0]);
        
           // CHECKING BUBBLE IMAGE EXIST AND UPLOADING IT IF REQUIRED:
           if(content[6].length() == 0) {
        	   tab    = Drupal.characterBannerVerticalTab;
        	   browse = Drupal.characterBannerBrowse;
               upload = Drupal.characterBannerUpload;
               ifBubble = helper.upload(driver, "bubble.jpg", tab, browse, upload, "thumbnail", new Exception().getStackTrace()[0]);
               }
           
           // CHECKING HERO IMAGE EXIST AND UPLOADING IT IF REQUIRED:
           if(content[8].length() == 0) {
        	   tab    = Drupal.heroBoxVerticalTab;
        	   browse = Drupal.heroBoxBrowse;
               upload = Drupal.heroBoxUpload;
               ifHero = helper.upload(driver, "hero.jpg", tab, browse, upload, "image", new Exception().getStackTrace()[0]);
               }
 		  
           // CHECKING SMALL TILE IMAGE EXIST AND UPLOADING IT IF REQUIRED:
           if(content[9].length() == 0) {
        	   tab    = Drupal.tileVerticalTab;
        	   browse = Drupal.tileSmallBrowse;
               upload = Drupal.tileSmallUpload;
               ifSmallTile = helper.upload(driver, "small.jpg", tab, browse, upload, "image", new Exception().getStackTrace()[0]);
               }
 		  
           // CHECKING PUBLISHED ON CHARACTER BANNER CHECK BOX AND CLCK IF REQUIRED:
 		   ifPublished = content[14].equals("UN-CHECKED");
           tab = Drupal.publishingOptionsVerticalTab;
   		   helper.uploadReader(driver, tab, "");
   		   helper.checkBoxStatus(driver, By.id(Drupal.publishingOptionsPublishedCheckBoxId), ifPublished, false, new Exception().getStackTrace()[0]);          
           
   		   // SUBMIT (IF REQUIRED):
   		   if(ifVisible || ifPublished || ifBubble || ifHero || ifSmallTile) {
   	   		   i = 0;
   	           Boolean ifTitle = true;
   	           while ( (ifTitle || (i == 0)) && (i < 5) ) {
   	        	   i = helper.contentSubmit(driver, i, helper.reFormatStringForURL(title), false);
   	               if(title.length() > 0) { ifTitle = (! driver.getCurrentUrl().startsWith(Common.adminContentURL)); }
   	               }
   	           }
   		   
           helper.logOut(driver);
           } else { helper.assertBooleanTrue(driver, new RuntimeException().getStackTrace()[0], ifContent); }
        
           // CONTENT URL:
           titleURL = helper.reFormatStringForURL(title, Drupal.titleMaxCharsNumber);
           
           // BANNER BUBBLE XPATH:
           xpath = "//img[@alt='" + title + "']/parent::a";
           url = Common.sixAndOverURL + "/" + titleURL;
           helper.fileWriterPrinter("\n" + "BANNER BUBBLE LINK XPATH = " + xpath);
           helper.fileWriterPrinter(       "               URL = " + url  );
           
           // STEPS-3,4:
           // THE SPACE BUBBLE TEST:
           helper.fileWriterPrinter("\n" + "STEPS-3,4\n" + "THE SPACE BUBBLE TEST:");
           // NAVIGATE TO BRAND PAGE BUBBLE EXIST:
           helper.getUrlWaitUntil(driver, 15, Common.sixAndOverURL);
           Thread.sleep(1000);
           helper.clickToAppear(driver, Common.charBannerButtonLeft, Common.charBannerButtonRight, xpath, false, false);
           // ASSERT BRAND PAGE BUBBLE EXIST:
           helper.assertWebElementExist(driver, new Exception().getStackTrace()[0], xpath);
           helper.clickLinkUrlWaitUntil(driver, 15, xpath, new Exception().getStackTrace()[0]);
//         // OPEN BRAND PAGE URL:
//         helper.getUrlWaitUntil(driver, 15, url);
           // ASSERT BRAND PAGE URL IS CORRECT:
           helper.assertEquals(driver, new Exception().getStackTrace()[0], driver.getCurrentUrl(), url);
           
           // STEP-5:
           // THE SPACE HERO BO TEST:
           helper.fileWriterPrinter("\n" + "STEP-5\n" + "THE SPACE HERO BOX TEST:");
           // ASSERT CHARACTER BUBBLE IS THE SAME BUBBLE DISPLAYED ON BANNER:
           helper.fileWriterPrinter("\n" + "ASSERT CHARACTER BUBBLE IS THE SAME BUBBLE DISPLAYED ON BANNER:");
           i = driver.findElements(By.xpath(Common.charBannerThumbnails)).size();
           helper.fileWriterPrinter("CURRENT TOTAL NUMBER OF BANNER BUBBLES: " + i); 
           actual = driver.findElement(By.xpath(Common.brandBubble)).getAttribute("src");
           actual = actual.substring(actual.lastIndexOf("/") + 1, actual.length());
           expected = driver.findElement(By.xpath(xpath + "/descendant::img[@src]")).getAttribute("src");
           expected = expected.substring(expected.lastIndexOf("/") + 1 , expected.length());
           helper.assertEquals(driver, new Exception().getStackTrace()[0], actual, expected);
           // ASSERT CHARACTER TITLE IS THE NAME OF THE BRAND PAGE:
           helper.fileWriterPrinter("ASSERT CHARACTER TITLE IS THE NAME OF THE BRAND PAGE:");
           actual = helper.getText(driver, Common.brandTitle);
           expected = title;
           helper.assertEquals(driver, new Exception().getStackTrace()[0], actual, expected);
           
           // STEP-6:
           // ABILITY TO SEE TILES TEST:
           helper.fileWriterPrinter("\n" + "STEP-6\n" + "ABILITY TO SEE TILES TEST:");
           helper.assertWebElementExist(driver, new Exception().getStackTrace()[0], Common.brandTile);
           i = driver.findElements(By.xpath(Common.brandTile)).size();
           if(i > 0) {
        	   helper.fileWriterPrinter("TOTAL NUMBER OF TILES FOUND: " + i);
        	   for (int j = 0; j < i; j++) { 
        		   helper.fileWriterPrinter(
        				                   "                     " + (j + 1) + " OF " + i + ": " +
        	                               driver.findElement(By.xpath(Common.brandTile("", j + 1))).getText());
        		   }
        	   }
                     
	   } catch(Exception e) { helper.getExceptionDescriptive(e, new Exception().getStackTrace()[0], driver); }
	}
	
	/**
	 * Test "GBB" Character Brand front-end
	 * <p>Date Created: 2016-11-08</p>
	 * <p>Date Modified: 2016-11-08</p>
	 * <p>Original Version: V1</p>
	 * <p>Modified Version: </p>
	 * <p>Xpath: 1</p>
	 * <p>Test Cases: 36419 3966</p>
	 */
	@Test(groups = {"TC-36419","US-3966","BUG-972","NEW"}, priority = 59)
    public void testBddFrontEnd() throws IOException{
	   try{
    	   // DECLARATION:
           String title, titleURL, xpath, tab, browse, upload, url, actual, expected, bubble = "";
    	   Boolean ifContent = false, ifVisible = false, ifUnVisible, ifPublished = false, ifUnPublished;
    	   Boolean ifBubble = false, ifNoBubble, ifHero = false, ifNoHero, ifSmallTile = false, ifNoSmallTile;
    	   int i;
    	   
           // INITIALISATION:
           helper.printXmlPath(new RuntimeException().getStackTrace()[0]);
           driver = helper.getServerName(driver);
           
           // CLEAN-UP:
           helper.deleteAllContent(driver, "147", "", "", new RuntimeException().getStackTrace()[0]);
           
           // ASSIGN CONTENT TITLE:
           title = "GBB";
           
           // STEP-1:
           // LOGIN TO DRUPAL AS AN ADMIN:
           helper.fileWriterPrinter("\n" + "STEP-1\n" + "LOGIN TO DRUPAL AS AN ADMIN:");
           helper.logIn(driver);
           
           // STEP-2:
           // RE-OPEN THE SPACE BRAND PAGE BACK-END:
           helper.fileWriterPrinter("\n" + "STEP-2\n" + "RE-OPEN THE SPACE BRAND PAGE BACK-END:");
           ifContent = helper.reopenBrand( driver, title, "Character Brand", "", "", true, false, false, new RuntimeException().getStackTrace()[0], 2, "CHECKED");
           // CHECK IF AGE 6 AND OVER CHARACTER BRAND PAGES EXIST:
           if(ifContent) {   
           // READ BRAND DATA:
           String[] content = helper.readContent(driver, new RuntimeException().getStackTrace()[0], false);
           helper.fileWriterPrinterArray(content);
		   // ASSERT TITLE:
           String actualTitle = content[0];
           helper.assertBooleanTrue(driver, new RuntimeException().getStackTrace()[0], actualTitle.contains(title));
           title = actualTitle;
           // ASSERT AGE GROUP:
           String expectedAgeGroup5 = "CHECKED";
           String expectedAgeGroup6 = "UN-CHECKED";
           String actualAgeGroup5= content[2];
           String actualAgeGroup6= content[3];
           helper.assertEquals(driver, new Exception().getStackTrace()[0], actualAgeGroup5, expectedAgeGroup5);
           helper.assertEquals(driver, new Exception().getStackTrace()[0], actualAgeGroup6, expectedAgeGroup6);         
           
           // CHECKING VISIBLE ON CHARACTER BANNER CHECK BOX AND CLCK IF REQUIRED:
           ifUnVisible = content[7].equals("UN-CHECKED");
           ifVisible = !ifUnVisible;
           helper.fileWriterPrinter("\n" + 
                                    "VISIBLE ON CHAR BUNNER: " + helper.convertBoolanToYesNo(ifVisible));
           if(ifUnVisible){
               tab = Drupal.characterBannerVerticalTab;
       		   helper.uploadReader(driver, tab, "");
               helper.checkVisibleOnCharacterBanner(driver, ifUnVisible, new Exception().getStackTrace()[0]);
               }
           
           // CHECKING BUBBLE IMAGE EXIST AND UPLOADING IT IF REQUIRED:
           ifNoBubble = (content[6].length() == 0);
           ifBubble = !ifNoBubble;
           helper.fileWriterPrinter("         BUBBLE EXISTS: " + helper.convertBoolanToYesNo(ifBubble));
           if(ifNoBubble) {
        	   tab    = Drupal.characterBannerVerticalTab;
        	   browse = Drupal.characterBannerBrowse;
               upload = Drupal.characterBannerUpload;
               helper.upload(driver, "bubble.jpg", tab, browse, upload, "thumbnail", new Exception().getStackTrace()[0]);
               bubble = "bubble";
           } else { bubble = content[6].substring(0, content[6].indexOf("."));}
           
           // CHECKING HERO IMAGE EXIST AND UPLOADING IT IF REQUIRED:
           ifNoHero = (content[8].length() == 0);
           ifHero = !ifNoHero;
           helper.fileWriterPrinter("           HERO EXISTS: " + helper.convertBoolanToYesNo(ifHero));
           if(ifNoHero) {
        	   tab    = Drupal.heroBoxVerticalTab;
        	   browse = Drupal.heroBoxBrowse;
               upload = Drupal.heroBoxUpload;
               helper.upload(driver, "hero.jpg", tab, browse, upload, "image", new Exception().getStackTrace()[0]);
               }
 		  
           // CHECKING SMALL TILE IMAGE EXIST AND UPLOADING IT IF REQUIRED:
           ifNoSmallTile = (content[9].length() == 0);
           ifSmallTile = !ifNoSmallTile;
           helper.fileWriterPrinter("    SMALL TITLE EXISTS: " + helper.convertBoolanToYesNo(ifSmallTile));
           if(ifNoSmallTile) {
        	   tab    = Drupal.tileVerticalTab;
        	   browse = Drupal.tileSmallBrowse;
               upload = Drupal.tileSmallUpload;
               ifSmallTile = helper.upload(driver, "small.jpg", tab, browse, upload, "image", new Exception().getStackTrace()[0]);
               }
 		  
           // CHECKING PUBLISHED ON CHARACTER BANNER CHECK BOX AND CLCK IF REQUIRED: 		   
 		   ifUnPublished = (content[14].equals("UN-CHECKED"));
 		   ifPublished = !ifUnPublished;

 		   helper.fileWriterPrinter("            PUBLISHED?: " + helper.convertBoolanToYesNo(ifPublished));
           if(ifNoSmallTile) {
           tab = Drupal.publishingOptionsVerticalTab;
   		   helper.uploadReader(driver, tab, "");
   		   helper.checkBoxStatus(driver, By.id(Drupal.publishingOptionsPublishedCheckBoxId), ifPublished, false, new Exception().getStackTrace()[0]);          
           }
           
   		   // SUBMIT (IF REQUIRED):
   		   helper.fileWriterPrinter("\n" +
   		                            "       SUBMIT REQUIRED: " + 
   		                            helper.convertBoolanToYesNo(ifUnVisible || ifUnPublished || ifNoBubble || ifNoHero || ifNoSmallTile) +
   		                            "\n"); 		
   		   if(ifUnVisible || ifUnPublished || ifNoBubble || ifNoHero || ifNoSmallTile) {
   	   		   i = 0;
   	           Boolean ifTitle = true;
   	           while ( (ifTitle || (i == 0)) && (i < 5) ) {
   	        	   i = helper.contentSubmit(driver, i, true, false, Common.adminContentURL, false);
   	               if(title.length() > 0) { ifTitle = (! driver.getCurrentUrl().startsWith(Common.adminContentURL)); }
   	               }
   	           }
   		   
           helper.logOut(driver);
           
           } else { helper.assertBooleanTrue(driver, new RuntimeException().getStackTrace()[0], ifContent); }
        
           // CONTENT URL:
           titleURL = helper.reFormatStringForURL(title, Drupal.titleMaxCharsNumber); 
           // BANNER BUBBLE XPATH:
           xpath = "//img[@alt='" + title + "']/parent::a";
           url = Common.fiveAndUnderURL + "/" + titleURL;
           helper.fileWriterPrinter("\n" + "BANNER BUBBLE LINK XPATH = " + xpath);
           helper.fileWriterPrinter(       "               URL = " + url  );
           
           // STEPS-3,4:
           // THE SPACE BUBBLE TEST:
           helper.fileWriterPrinter("\n" + "STEPS-3,4\n" + "THE SPACE BUBBLE TEST:");
//           // ASSERT BRAND PAGE BUBBLE EXIST:
//           helper.getUrlWaitUntil(driver, 15, Common.fiveAndUnderURL);
//           helper.assertWebElementExist(driver, new Exception().getStackTrace()[0], xpath);
//           Thread.sleep(1000);
//           helper.clickToAppear(driver, Common.charBannerButtonLeft, Common.charBannerButtonRight, xpath, false, false);
           // OPEN BRAND PAGE URL:
           helper.getUrlWaitUntil(driver, 15, url);

           // ASSERT BRAND PAGE URL IS CORRECT:
           helper.assertEquals(driver, new Exception().getStackTrace()[0], driver.getCurrentUrl(), url);
           
           // STEP-5:
           // THE SPACE HERO BO TEST:
           helper.fileWriterPrinter("\n" + "STEP-5\n" + "THE SPACE HERO BOX TEST:");
           // ASSERT CHARACTER BUBBLE IS THE SAME BUBBLE DISPLAYED ON BANNER:
           helper.fileWriterPrinter("\n" + "ASSERT CHARACTER BUBBLE IS THE SAME BUBBLE DISPLAYED ON BANNER:");
           i = driver.findElements(By.xpath(Common.charBannerThumbnails)).size();
           helper.fileWriterPrinter("CURRENT TOTAL NUMBER OF BANNER BUBBLES: " + i);
           actual = driver.findElement(By.xpath(Common.brandBubble)).getAttribute("src");
           actual = actual.substring(actual.lastIndexOf("/") + 1, actual.length());
//         expected = driver.findElement(By.xpath(xpath + "/descendant::img[@src]")).getAttribute("src");
//         expected = expected.substring(expected.lastIndexOf("/") + 1 , expected.length());
           expected = bubble;
//         helper.assertEquals(driver, new Exception().getStackTrace()[0], actual, expected);
           helper.assertBooleanTrue(driver, new Exception().getStackTrace()[0], actual.contains(expected));

           // ASSERT CHARACTER TITLE IS THE NAME OF THE BRAND PAGE:
           helper.fileWriterPrinter("ASSERT CHARACTER TITLE IS THE NAME OF THE BRAND PAGE:");
           actual = helper.getText(driver, Common.brandTitle);
           expected = title;
           helper.assertEquals(driver, new Exception().getStackTrace()[0], actual, expected);
           
//         // STEP-6:
//         // ABILITY TO SEE TILES TEST:
//         helper.fileWriterPrinter("\n" + "STEP-6\n" + "ABILITY TO SEE TILES TEST:");
//         helper.assertWebElementExist(driver, new Exception().getStackTrace()[0], Common.brandTile);
//         i = driver.findElements(By.xpath(Common.brandTile)).size();
//         if(i > 0) {
//      	 helper.fileWriterPrinter("TOTAL NUMBER OF TILES FOUND: " + i);
//      	 for (int j = 0; j < i; j++) { 
//      		   helper.fileWriterPrinter(
//      				                    "                     " + (j + 1) + " OF " + i + ": " +
//      	                                driver.findElement(By.xpath(Common.brandTile("", j + 1))).getText());
//      		   }
//      	 }
           
	   } catch(Exception e) { helper.getExceptionDescriptive(e, new Exception().getStackTrace()[0], driver); }
	}
	
}