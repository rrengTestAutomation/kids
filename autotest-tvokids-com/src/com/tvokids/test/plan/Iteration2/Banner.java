package com.tvokids.test.plan.Iteration2;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.lang.reflect.Method;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;
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
public class Banner {
	static WebDriver driver;
	UtilitiesTestHelper helper = new UtilitiesTestHelper();

    @BeforeMethod public void startTime(Method method) throws IOException { helper.startTime(method); }   
    @AfterMethod  public void endTime() throws IOException { helper.endTime(); }
    @AfterMethod  @AfterClass   public void closeBrowsers() { driver.quit(); }
    
	/**
	 * Test Desktop - Character banner rotation using Banner Arrows
	 * <p>Date Created: 2016-07-30</p>
	 * <p>Date Modified: 2016-07-30</p>
	 * <p>Original Version: V1</p>
	 * <p>Modified Version: </p>
	 * <p>Xpath: 1</p>
	 * <p>Test Cases: 34144 2025 3655 972</p>
	 */
	@Test(groups = {"TC-34144","US-2025","US-3655","BUG-972","CLOSED"}, priority = 15)
	public void testDesktopCharacterBannerNavigationButtons() throws IOException, IllegalArgumentException, MalformedURLException {
	       try{
	    	   // INITIALISATION:
	           helper.printXmlPath(new RuntimeException().getStackTrace()[0]);
	           driver = helper.getServerName(driver);
	           
	           // CLEAN-UP:
	           helper.deleteAllContent(driver, "147", "", "", new RuntimeException().getStackTrace()[0]);
			   
	           // LOGIN TO DRUPAL AS A CONTENT EDITOR:
	           helper.logIn(driver, Common.contentEditorUsername, Common.userPassword(Common.contentEditorUsername));
	           
	           // NAVIGATE TO A NEW CUSTOM BRAND PAGE:
	           helper.getUrlWaitUntil(driver, 10, Drupal.customBrand);
	           
	           // DECLARATION:
	           String title, titleURL, description, xpath;
	           int defaultCoordinateX, movedCoordinateX, backCoordinateX, characterWidth;
	           double DefaultCoordinateX, MovedCoordinateX, CharacterWidth, MovementRatio, BackCoordinateX, ReturntRatio;
		       DecimalFormat df = new DecimalFormat("#");
		       
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
	           xpath = "//img[@alt=\"" + title + "\"]/parent::a";
	           helper.fileWriterPrinter("\n" + "BANNER BUBBLE LINK XPATH = " + xpath);
	           
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
		       helper.assertBooleanTrue(driver, new Exception().getStackTrace()[0], Math.abs(MovementRatio) > 0 );
		       helper.assertBooleanTrue(driver, new Exception().getStackTrace()[0], Math.abs(ReturntRatio) > 0 );
	
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
		       helper.assertBooleanTrue(driver, new Exception().getStackTrace()[0], Math.abs(MovementRatio) > 0 );
		       helper.assertBooleanTrue(driver, new Exception().getStackTrace()[0], Math.abs(ReturntRatio) > 0 );
		       
	       } catch(Exception e) { helper.getExceptionDescriptive(e, new Exception().getStackTrace()[0], driver); }
	    }
	
	/**
	 * Test Desktop - Character banner rotation using Click and Drug
	 * <p>Date Created: 2016-07-30</p>
	 * <p>Date Modified: 2016-07-30</p>
	 * <p>Original Version: V1</p>
	 * <p>Modified Version: </p>
	 * <p>Xpath: 1</p>
	 * <p>Test Cases: 34144 2025 3655 972</p>
	 */
	@Test(groups = {"TC-34144","US-2025","US-3655","BUG-972","CLOSED"}, priority = 16)
	public void testDesktopCharacterBannerClickAndDrugRotation() throws IOException, IllegalArgumentException, MalformedURLException {
	       try{
	    	   // INITIALISATION:
	           helper.printXmlPath(new RuntimeException().getStackTrace()[0]);
	           driver = helper.getServerName(driver);
	           driver.manage().window().maximize();
	           
	           // CLEAN-UP:
	           helper.deleteAllContent(driver, "147", "", "", new RuntimeException().getStackTrace()[0]);
			   
	           // LOGIN TO DRUPAL AS A CONTENT EDITOR:
	           helper.logIn(driver, Common.contentEditorUsername, Common.userPassword(Common.contentEditorUsername));
	           
	           // NAVIGATE TO A NEW CUSTOM BRAND PAGE:
	           helper.getUrlWaitUntil(driver, 10, Drupal.customBrand);
	           
	           // DECLARATION:
	           String title, titleURL, description, xpath;
	           double MovementRatioForward, MovementRatioBack;
		       
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
	           xpath = "//img[@alt=\"" + title + "\"]/parent::a";
	           helper.fileWriterPrinter("\n" + "BANNER BUBBLE LINK XPATH = " + xpath);
	           
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
		       	       
	       } catch(Exception e) { helper.getExceptionDescriptive(e, new Exception().getStackTrace()[0], driver); }
	    }
	
	/**
	 * Test the banner is Infinite looped
	 * <p>Date Created: 2016-08-08</p>
	 * <p>Date Modified: 2016-08-08</p>
	 * <p>Original Version: V1</p>
	 * <p>Modified Version: </p>
	 * <p>Xpath: 1</p>
	 * <p>Test Cases: 34147 2024 3656 972</p>
	 */
	@Test(groups = {"TC-34147","US-2024","US-3656","BUG-972","CLOSED"}, priority = 17)
	public void testCharacterBannerIsInfiniteLooped() throws IOException, IllegalArgumentException, MalformedURLException {
	       try{
	    	   // INITIALISATION:
	           helper.printXmlPath(new RuntimeException().getStackTrace()[0]);
	           driver = helper.getServerName(driver);
	           
	           // CLEAN-UP:
	           helper.deleteAllContent(driver, "147", "", "", new RuntimeException().getStackTrace()[0]);
			   
	           // LOGIN TO DRUPAL AS A CONTENT EDITOR:
	           helper.logIn(driver, Common.contentEditorUsername, Common.userPassword(Common.contentEditorUsername));
	           
	           // NAVIGATE TO A NEW CUSTOM BRAND PAGE:
	           helper.getUrlWaitUntil(driver, 10, Drupal.customBrand);
	           
	           // DECLARATION:
	           String title, titleURL, description, xpath;
	           int i = 0, countDisappear = i, countAppear = i;
	           
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
	           xpath = "//img[@alt=\"" + title + "\"]/parent::a";
	           helper.fileWriterPrinter("\n" + "BANNER BUBBLE LINK XPATH = " + xpath);
	           
	           // AGE 5 AND UNDER TEST:
	              helper.fileWriterPrinter("\n\n" + "AGE 5 AND UNDER TEST:");
	              helper.getUrlWaitUntil(driver, 10, Common.fiveAndUnderURL);
           
	           // CLICK LEFT UNTIL DISAPPEAR:
	           countDisappear = helper.clickToDisAppear(driver, Common.charBannerButtonLeft, Common.charBannerButtonRight, xpath, false, true);		       
	           // CLICK LEFT UNTIL APPEAR:
	           countAppear = countDisappear + helper.clickToAppear(driver, Common.charBannerButtonLeft, Common.charBannerButtonRight, xpath, false, true);		       
	           // ASSERT 5 AND UNDER CHARACTER LEFT LOOPING:
	           helper.assertBooleanTrue(driver, new Exception().getStackTrace()[0], (countAppear >= countDisappear) && (countDisappear >= 0) );
	           
	           // CLICK RIGHT UNTIL DISAPPEAR:
	           countDisappear = helper.clickToDisAppear(driver, Common.charBannerButtonLeft, Common.charBannerButtonRight, xpath, true, true);		       
	           // CLICK RIGHT UNTIL APPEAR:
	           countAppear = countDisappear + helper.clickToAppear(driver, Common.charBannerButtonLeft, Common.charBannerButtonRight, xpath, true, true);   
	           // ASSERT 5 AND UNDER CHARACTER RIGHT LOOPING:
	           helper.assertBooleanTrue(driver, new Exception().getStackTrace()[0], (countAppear >= countDisappear) && (countDisappear >= 0) );
	           
	           // AGE 6 AND OVER TEST:
	              helper.fileWriterPrinter("\n\n" + "AGE 6 AND OVER TEST:");
	              helper.getUrlWaitUntil(driver, 10, Common.sixAndOverURL);
	           
	           // CLICK LEFT UNTIL DISAPPEAR:
		       countDisappear = helper.clickToDisAppear(driver, Common.charBannerButtonLeft, Common.charBannerButtonRight, xpath, false, true);		      
		       // CLICK LEFT UNTIL APPEAR:        
		       countAppear = countDisappear + helper.clickToAppear(driver, Common.charBannerButtonLeft, Common.charBannerButtonRight, xpath, false, true);		       
		       // ASSERT 6 AND OVER CHARACTER LEFT LOOPING:
	           helper.assertBooleanTrue(driver, new Exception().getStackTrace()[0], (countAppear >= countDisappear) && (countDisappear >= 0) );
	           
	           // CLICK RIGHT UNTIL DISAPPEAR:
	           countDisappear = helper.clickToDisAppear(driver, Common.charBannerButtonLeft, Common.charBannerButtonRight, xpath, true, true);	           
		       // CLICK RIGHT UNTIL APPEAR:
	           countAppear = countDisappear + helper.clickToAppear(driver, Common.charBannerButtonLeft, Common.charBannerButtonRight, xpath, true, true);	           
		       // ASSERT 6 AND OVER CHARACTER RIGHT LOOPING:
	           helper.assertBooleanTrue(driver, new Exception().getStackTrace()[0], (countAppear >= countDisappear) && (countDisappear >= 0) );
	           
	       } catch(Exception e) { helper.getExceptionDescriptive(e, new Exception().getStackTrace()[0], driver); }
	    }
	
	/**
	 * Test TVOKIDS Logo displaying
	 * <p>Date Created: 2016-08-09</p>
	 * <p>Date Modified: 2016-08-09</p>
	 * <p>Original Version: V1</p>
	 * <p>Modified Version: </p>
	 * <p>Xpath: 1</p>
	 * <p>Test Cases: 34169 2769</p>
	 */
	@Test(groups = {"TC-34169","US-2769"}, priority = 18)
	public void testTVOKidsLogoIsAnimated() throws IOException, IllegalArgumentException, MalformedURLException {
	       try{
	    	   // INITIALISATION:
	           helper.printXmlPath(new RuntimeException().getStackTrace()[0]);
	           driver = helper.getServerName(driver);
	           
	           // DECLARATION:
	           String expected, actual;
	           Integer[] seconds = {1, 10, 4};
	           
	           // AGE 5 AND UNDER TEST:
		       helper.fileWriterPrinter("\n" + "AGE 5 AND UNDER TEST:");
		       helper.getUrlWaitUntil(driver, 10, Common.fiveAndUnderURL);
		       
		       for (int i = 0; i < seconds.length; i++) {
		    	   if(helper.determineEvenOrOdd(i).equals("even")) { expected = "animated-logo animating"; }
		    	   else { expected = "animated-logo"; }
		    	   Thread.sleep(seconds[i]*1000);
		    	   actual = driver.findElement(By.xpath(Common.kidsPageLogoCanvas)).getAttribute("class");		    	   
		    	   // ASSERT:
		    	   helper.assertEquals(driver, new RuntimeException().getStackTrace()[0], actual, expected);
		    	   }
		         
		       // AGE 6 AND OVER TEST:
		       helper.fileWriterPrinter("\n" + "AGE 6 AND OVER TEST:");
		       helper.getUrlWaitUntil(driver, 10, Common.sixAndOverURL);
		       
		       for (int i = 0; i < seconds.length; i++) {
		    	   if(helper.determineEvenOrOdd(i).equals("even")) { expected = "animated-logo animating"; }
		    	   else { expected = "animated-logo"; }
		    	   Thread.sleep(seconds[i]*1000);
		    	   actual = driver.findElement(By.xpath(Common.kidsPageLogoCanvas)).getAttribute("class");		    	   
		    	   // ASSERT:
		    	   helper.assertEquals(driver, new RuntimeException().getStackTrace()[0], actual, expected);
		    	   }
		       
	       } catch(Exception e) { helper.getExceptionDescriptive(e, new Exception().getStackTrace()[0], driver); }
	   }
	
	/**
	 * Test Search Icon - Desktop
	 * <p>Date Created: 2016-08-10</p>
	 * <p>Date Modified: 2016-08-10</p>
	 * <p>Original Version: V1</p>
	 * <p>Modified Version: </p>
	 * <p>Xpath: 1</p>
	 * <p>Test Cases: 34460 2829 4489 1021</p>
	 */
	@Test(groups = {"TC-34460","US-2829","US-4489","BUG-1021","CLOSED"}, priority = 19)
	public void testSearchIcon() throws IOException, IllegalArgumentException, MalformedURLException {
	       try{
	    	   // INITIALISATION:
	           helper.printXmlPath(new RuntimeException().getStackTrace()[0]);
	           driver = helper.getServerName(driver);
	           
	           // AGE 5 AND UNDER TEST:
		       helper.fileWriterPrinter("\n" + "AGE 5 AND UNDER TEST:");
		       helper.getUrlWaitUntil(driver, 10, Common.fiveAndUnderURL);		       
		       // ASSERT:
		       helper.assertWebElementNotExist(driver, new RuntimeException().getStackTrace()[0], Common.searchIcon);
	
		       // AGE 6 AND OVER TEST:
		       helper.fileWriterPrinter("\n" + "AGE 6 AND OVER TEST:");
		       helper.getUrlWaitUntil(driver, 10, Common.sixAndOverURL);
		       // ASSERT:
		       helper.assertWebElementNotExist(driver, new RuntimeException().getStackTrace()[0], Common.searchIcon);
	
	       } catch(Exception e) { helper.getExceptionDescriptive(e, new Exception().getStackTrace()[0], driver); }
	   }
	
	/**
	 * Test TVOKids logo is redirecting to age specific landing page
	 * <p>Date Created: 2016-08-11</p>
	 * <p>Date Modified: 2016-08-11</p>
	 * <p>Original Version: V1</p>
	 * <p>Modified Version: </p>
	 * <p>Xpath: 1</p>
	 * <p>Test Cases: 34140 1990 972</p>
	 */
	@Test(groups = {"TC-34140","US-1990","BUG-972","CLOSED"}, priority = 20)
	public void testTVOKidsLogoRedirectsToCorrectAgeLandingPage() throws IOException, IllegalArgumentException, MalformedURLException {
	       try{
	    	   // INITIALISATION:
	           helper.printXmlPath(new RuntimeException().getStackTrace()[0]);
	           driver = helper.getServerName(driver);
	           
	           // CLEAN-UP:
	           helper.deleteAllContent(driver, "147", "", "", new RuntimeException().getStackTrace()[0]);	           
	           
	           // DECLARATION:
	           int total = 8;
	           String[] title = new String[total];
	           String[] titleURL = new String[total];
	           String[] description = new String[total];
	           String[] xpath = new String[total];
	           long[] fingerprint = new long[total];
	           
	           for (int i = 0; i < total; i++) {
	        	   // CREATE TITLES FOR CONTENTS:
	        	   fingerprint[i] = System.currentTimeMillis();
	        	   title[i] = String.valueOf(fingerprint[i]) + " " +  helper.randomWord(Drupal.titleMaxCharsNumber - String.valueOf(fingerprint[i]).length() - 1);
	        	   titleURL[i] = helper.reFormatStringForURL(title[i], Drupal.titleMaxCharsNumber);
	        	   // CREATE DESCRIPTION FOR CONTENT:
	        	   description[i] = helper.randomEnglishText(helper.randomInt((Drupal.descriptionMaxCharsNumber - 30), Drupal.descriptionMaxCharsNumber));
	        	   // BANNER BUBBLE XPATH:
		           xpath[i] = "//a[contains(@href,'" + titleURL[i] +  Common.XpathContainsEnd;
		           helper.fileWriterPrinter("\n\n" + "LINK GENERIC XPATH = " + xpath[i]);
	        	   if (helper.isEven(i)) {
	        		   // CREATE A CHARACTER BRAND CONTENT WITH BOTH AGES SELECTED:
	        		   helper.logIn(driver);  // LOGIN TO DRUPAL AS AN ADMIN
	    	           helper.createCharacterBrand(driver, title[i], description[i], "281374", false, true, true, false, true, true, new Exception().getStackTrace()[0]);
	    	           helper.logOut(driver);
	    	           helper.fileWriterPrinter("\n" + (i + 1) + " OF " + total + ": CREATED!\n  TYPE: CHARACTER BRAND\n TITLE: " + title[i] + "\n");
	        	   } else {
	        		   // CREATE A CUSTOM BRAND CONTENT WITH BOTH AGES SELECTED:
	        		   helper.logIn(driver, Common.contentEditorUsername, Common.userPassword(Common.contentEditorUsername)); // LOGIN TO DRUPAL AS A CONTENT EDITOR
	        		   helper.createCustomBrand(driver, title[i], description[i], true, true, false, true, true, new Exception().getStackTrace()[0]);
	        		   helper.logOut(driver);
	        		   helper.fileWriterPrinter("\n" + (i + 1) + " OF " + total + ": CREATED!\n  TYPE: CUSTOM BRAND\n TITLE: " + title[i] + "\n");
	        	   }
	           }
	           
	           // AGE 5 AND UNDER TEST:
	           helper.fileWriterPrinter("\n\n" + "AGE 5 AND UNDER TEST:");
	           for (int i = 0; i < total; i++) {
	        	   helper.fileWriterPrinter("\nAGE 5 AND UNDER TEST: " + (i + 1) + " OF " + total);
	        	   helper.fileWriterPrinter(  "LINK   GENERIC XPATH: " + xpath[i] + "\n");
	        	   helper.getUrlWaitUntil(driver, 10, Common.fiveAndUnderURL);
	        	   helper.assertWebElementExist(driver, new Exception().getStackTrace()[0], xpath[i]);
	        	   helper.clickToAppear(driver, Common.charBannerButtonLeft, Common.charBannerButtonRight, xpath[i], false, false);
	        	   helper.clickLinkUrlWaitUntil(driver, 10, xpath[i]); // URL = Common.fiveAndUnderURL + "/" + titleURL[i];
	        	   // ASSERT:
	        	   helper.checkLinkURL(driver, new RuntimeException().getStackTrace()[0], Common.kidsPageLogo, Common.fiveAndUnderURL);
	        	   helper.clickLinkAndCheckURL(driver, new RuntimeException().getStackTrace()[0], Common.kidsPageLogo, Common.fiveAndUnderURL, true, false);
	        	   }
	           
	           // AGE 6 AND OVER TEST:
	           helper.fileWriterPrinter("\n\n" + "AGE 6 AND OVER TEST:");
	           for (int i = 0; i < total; i++) {
	        	   helper.fileWriterPrinter("\nAGE 6 AND OVER TEST: " + (i + 1) + " OF " + total);
	        	   helper.fileWriterPrinter(  "LINK  GENERIC XPATH: " + xpath[i] + "\n");
	        	   helper.getUrlWaitUntil(driver, 10, Common.sixAndOverURL);
	        	   helper.assertWebElementExist(driver, new Exception().getStackTrace()[0], xpath[i]);
	        	   helper.clickToAppear(driver, Common.charBannerButtonLeft, Common.charBannerButtonRight, xpath[i], false, false);
	        	   helper.clickLinkUrlWaitUntil(driver, 10, xpath[i]); // URL = Common.sixAndOverURL + "/" + titleURL[i];
	        	   // ASSERT:
	        	   helper.checkLinkURL(driver, new RuntimeException().getStackTrace()[0], Common.kidsPageLogo, Common.sixAndOverURL);
	        	   helper.clickLinkAndCheckURL(driver, new RuntimeException().getStackTrace()[0], Common.kidsPageLogo, Common.sixAndOverURL, true, false);
	        	   }
	           
	           } catch(Exception e) { helper.getExceptionDescriptive(e, new Exception().getStackTrace()[0], driver); }
	       }
	/**
	 * Test character banner images Prioritization Business Rule
	 * <p>Date Created: 2016-08-12</p>
	 * <p>Date Modified: 2016-08-12</p>
	 * <p>Original Version: V1</p>
	 * <p>Modified Version: </p>
	 * <p>Xpath: 1</p>
	 * <p>Test Cases: 34143 2023 972</p>
	 */
	@Test(groups = {"TC-34143","US-2023","BUG-972","CLOSED"}, priority = 21)
	public void testOnNowBubbleIsNotMovingBackAfterRotation() throws IOException, IllegalArgumentException, MalformedURLException {
	       try{
	    	   // INITIALISATION:
	           helper.printXmlPath(new RuntimeException().getStackTrace()[0]);
	           driver = helper.getServerName(driver);
	           
	           // CLEAN-UP:
	           helper.deleteAllContent(driver, "147", "", "", new RuntimeException().getStackTrace()[0]);
			   
	           // LOGIN TO DRUPAL AS A CONTENT EDITOR:
	           helper.logIn(driver, Common.contentEditorUsername, Common.userPassword(Common.contentEditorUsername));
	           
	           // NAVIGATE TO A NEW CUSTOM BRAND PAGE:
	           helper.getUrlWaitUntil(driver, 10, Drupal.customBrand);
	           
	           // DECLARATION:
	           String title, titleURL, description, xpath;
	           int defaultCoordinateX, movedCoordinateX, backCoordinateX, characterWidth, windowWidth , left, right;
	           
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
	           xpath = "//img[@alt=\"" + title + "\"]/parent::a";
	           helper.fileWriterPrinter("\n" + "BANNER BUBBLE LINK XPATH = " + xpath);
	           
	           // AGE 5 AND UNDER TEST:
	           helper.fileWriterPrinter("\n\n" + "AGE 5 AND UNDER TEST:");
	           helper.getUrlWaitUntil(driver, 10, Common.fiveAndUnderURL);
	           
	           // MEASURE THE CHARACTER BUBBLE EXISTING (DEFAULT) LOCATION:
	           defaultCoordinateX = driver.findElement(By.xpath(xpath)).getLocation().getX();	           
	           characterWidth = helper.getElementWidth(driver, xpath);
	           windowWidth = driver.manage().window().getSize().getWidth();
               left = driver.findElement(By.xpath(Common.charBannerButtonLeft)).getLocation().getX();
               right = driver.findElement(By.xpath(Common.charBannerButtonRight)).getLocation().getX();
	           
               // CLICK LEFT UNTIL DISAPPEAR:
               helper.clickToDisAppear(driver, Common.charBannerButtonLeft, Common.charBannerButtonRight, xpath, false, true);
	           
	           // MEASURE THE CHARACTER BUBBLE NEW LOCATION:
               Thread.sleep(1000);
		       movedCoordinateX = driver.findElement(By.xpath(xpath)).getLocation().getX();
		       
		       // MEASUREMENT REPORT:
	           helper.fileWriterPrinter("\n" + "CHARACTER              WIDTH = " + characterWidth);
	           helper.fileWriterPrinter("CHARACTER DEFAULT  COORDINATE = " + defaultCoordinateX);
		       helper.fileWriterPrinter("CHARACTER   MOVED  COORDINATE = " + movedCoordinateX);
		       helper.fileWriterPrinter("LEFT  ARROW      X-COORDINATE = " + left);
		       helper.fileWriterPrinter("RIGHT ARROW      X-COORDINATE = " + right);
		       helper.fileWriterPrinter("CURRENT BROWSER  WINDOW WIDTH = " + windowWidth);
		       
		       // ASSERT CHARACTER BUBBLE DID NOT MOVE BACK:
	           Thread.sleep(3000);
	           backCoordinateX = driver.findElement(By.xpath(xpath)).getLocation().getX();
	           helper.assertEquals(driver, new RuntimeException().getStackTrace()[0], backCoordinateX, movedCoordinateX);
	           
	           // AGE 6 AND OVER TEST:
	           helper.fileWriterPrinter("\n\n" + "AGE 6 AND OVER TEST:");
	           helper.getUrlWaitUntil(driver, 10, Common.sixAndOverURL);
	           
	           // MEASURE THE CHARACTER BUBBLE EXISTING (DEFAULT) LOCATION:
	           defaultCoordinateX = driver.findElement(By.xpath(xpath)).getLocation().getX();	           
	           characterWidth = helper.getElementWidth(driver, xpath);
	           windowWidth = driver.manage().window().getSize().getWidth();
	           left = driver.findElement(By.xpath(Common.charBannerButtonLeft)).getLocation().getX();
	           right = driver.findElement(By.xpath(Common.charBannerButtonRight)).getLocation().getX();
	           
	           // CLICK RIGHT UNTIL DISAPPEAR:
	           helper.clickToDisAppear(driver, Common.charBannerButtonLeft, Common.charBannerButtonRight, xpath, true, true);
	           
	           // MEASURE THE CHARACTER BUBBLE NEW LOCATION:
	           Thread.sleep(1000);
		       movedCoordinateX = driver.findElement(By.xpath(xpath)).getLocation().getX();
		       
		       // MEASUREMENT REPORT:
	           helper.fileWriterPrinter("\n" + "CHARACTER              WIDTH = " + characterWidth);
	           helper.fileWriterPrinter("CHARACTER DEFAULT  COORDINATE = " + defaultCoordinateX);
		       helper.fileWriterPrinter("CHARACTER   MOVED  COORDINATE = " + movedCoordinateX);
		       helper.fileWriterPrinter("LEFT  ARROW      X-COORDINATE = " + left);
		       helper.fileWriterPrinter("RIGHT ARROW      X-COORDINATE = " + right);
		       helper.fileWriterPrinter("CURRENT BROWSER  WINDOW WIDTH = " + windowWidth);
		       
		       // ASSERT CHARACTER BUBBLE DID NOT MOVE BACK:
	           Thread.sleep(3000);
	           backCoordinateX = driver.findElement(By.xpath(xpath)).getLocation().getX();
	           helper.assertEquals(driver, new RuntimeException().getStackTrace()[0], backCoordinateX, movedCoordinateX);
	           
	       } catch(Exception e) { helper.getExceptionDescriptive(e, new Exception().getStackTrace()[0], driver); }
	    }
	
	/**
	 * Test Character Banner Interactivity Bubbles-animation behaves exactly as required
	 * <p>Date Created: 2016-08-18</p>
	 * <p>Date Modified: 2016-08-18</p>
	 * <p>Original Version: V1</p>
	 * <p>Modified Version: </p>
	 * <p>Xpath: 1</p>
	 * <p>Test Cases: 34462 2831 972</p>
	 */
	@Test(groups = {"TC-34462","US-2831","BUG-972","CLOSED"}, priority = 22)
    public void testCharacterBannerInteractivityBubblesAnimationIsCorrect() throws IOException, IllegalArgumentException, MalformedURLException {
	       try{
	    	   // INITIALISATION:
	           helper.printXmlPath(new RuntimeException().getStackTrace()[0]);
	           driver = helper.getServerName(driver);
	           
	           // CLEAN-UP:
	           helper.deleteAllContent(driver, "147", "", "", new RuntimeException().getStackTrace()[0]);
			   
	           // LOGIN TO DRUPAL AS A CONTENT EDITOR:
	           helper.logIn(driver, Common.contentEditorUsername, Common.userPassword(Common.contentEditorUsername));
	           
	           // NAVIGATE TO A NEW CUSTOM BRAND PAGE:
	           helper.getUrlWaitUntil(driver, 10, Drupal.customBrand);
	           
	           // DECLARATION:
	           String title, titleURL, description, xpath;
	           int x, y, X, Y;
	           
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
	           xpath = "//img[@alt='" + title + "']/parent::a" + Common.imageParticle;
	           helper.fileWriterPrinter("\n" + "LINK GENERIC XPATH = " + xpath + "\n");
	           
	           // AGE 5 AND UNDER TEST:
	           helper.fileWriterPrinter("\n" + "AGE 5 AND UNDER TEST:");
	           helper.getUrlWaitUntil(driver, 10, Common.fiveAndUnderURL);
	           helper.assertWebElementExist(driver, new Exception().getStackTrace()[0], xpath);
	           helper.clickToAppear(driver, Common.charBannerButtonLeft, Common.charBannerButtonRight, xpath, false, false);
	           Thread.sleep(1000);
	           // DETECT SIZES BEFORE AND AFTER MOUSE HOVER:
	           X = driver.findElement(By.xpath(xpath)).getLocation().getX();
	           Y = driver.findElement(By.xpath(xpath)).getLocation().getY();
	           helper.hoverElement(driver, xpath);
	           Thread.sleep(1000);
	           x = driver.findElement(By.xpath(xpath)).getLocation().getX();
	           y = driver.findElement(By.xpath(xpath)).getLocation().getY();
	           // ASSERT:
	           helper.assertBooleanTrue(driver, new Exception().getStackTrace()[0], X > x);
	           helper.assertBooleanTrue(driver, new Exception().getStackTrace()[0], Y > y);

	           // AGE 6 AND OVER TEST:
	           helper.fileWriterPrinter("\n" + "AGE 6 AND OVER TEST:");
	           helper.getUrlWaitUntil(driver, 10, Common.sixAndOverURL);
	           helper.assertWebElementExist(driver, new Exception().getStackTrace()[0], xpath);
	           helper.clickToAppear(driver, Common.charBannerButtonLeft, Common.charBannerButtonRight, xpath, false, false);
	           Thread.sleep(1000);
	           // DETECT SIZES BEFORE AND AFTER MOUSE HOVER:
	           X = driver.findElement(By.xpath(xpath)).getLocation().getX();
	           Y = driver.findElement(By.xpath(xpath)).getLocation().getY();
	           helper.hoverElement(driver, xpath);
	           Thread.sleep(1000);
	           x = driver.findElement(By.xpath(xpath)).getLocation().getX();
	           y = driver.findElement(By.xpath(xpath)).getLocation().getY();
	           // ASSERT:
	           helper.assertBooleanTrue(driver, new Exception().getStackTrace()[0], X > x);
	           helper.assertBooleanTrue(driver, new Exception().getStackTrace()[0], Y > y);
	           
	           } catch(Exception e) { helper.getExceptionDescriptive(e, new Exception().getStackTrace()[0], driver); }
	       }	
	
}
