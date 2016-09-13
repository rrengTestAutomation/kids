package com.tvokids.test.plan.Iteration3;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;

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
//	    	   String expected, actual;
	    	   
	    	   // INITIALISATION:
	           helper.printXmlPath(new RuntimeException().getStackTrace()[0]);
	           driver = helper.getServerName(driver);
	           
	        // LOGIN TO DRUPAL AS CONTENT-EDITOR:
	           helper.logIn(driver,"content_editor","changeme");
	           
	           // NAVIGATE TO HOME PAGE:
		       helper.getUrlWaitUntil(driver, 10, Drupal.customBrand);
		       
		       // ASSERT BROWSER TITLE:
		       helper.ajaxProtectedClick(driver, Drupal.tileVerticalTab, "tile Vertical Tab", false, "", true, false);
		       helper.fileWriterPrinter(driver.findElement(By.xpath(Drupal.tileSmallSizeDescription)).getText());
		       helper.fileWriterPrinter(driver.findElement(By.xpath(Drupal.tileLargeSizeDescription)).getText());
		       
	       } catch(Exception e) { helper.getExceptionDescriptive(e, new Exception().getStackTrace()[0], driver); }
	   }
	
}