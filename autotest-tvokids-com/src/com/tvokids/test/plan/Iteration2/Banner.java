package com.tvokids.test.plan.Iteration2;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DecimalFormat;

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
public class Banner {
	static WebDriver driver;
	UtilitiesTestHelper helper = new UtilitiesTestHelper();

    @BeforeMethod public static void startTime() throws IOException { new UtilitiesTestHelper().startTime(); } 
    @AfterMethod  public static void endTime() throws IOException { new UtilitiesTestHelper().endTime(); }
    @AfterMethod  @AfterClass   public static void closeBrowsers() { driver.quit(); }
    
	/**
	 * Test Desktop - Character banner rotation using Banner Arrows
	 * <p>Date Created: 2016-07-30</p>
	 * <p>Date Modified: 2016-07-30<p>
	 * <p>Original Version: V1</p>
	 * <p>Modified Version: </p>
	 * <p>Xpath: 1</p>
	 * <p>Test Cases: 34144</p>
	 */
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
	           
	           // LINK GENERIC XPATH:
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
	 * <p>Date Modified: 2016-07-30<p>
	 * <p>Original Version: V1</p>
	 * <p>Modified Version: </p>
	 * <p>Xpath: 1</p>
	 * <p>Test Cases: 34144</p>
	 */
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
	           
	           // LINK GENERIC XPATH:
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
		       	       
	       } catch(Exception e) { helper.getExceptionDescriptive(e, new Exception().getStackTrace()[0], driver); }
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
	           
	           // LINK GENERIC XPATH:
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
	           
	       } catch(Exception e) { helper.getExceptionDescriptive(e, new Exception().getStackTrace()[0], driver); }
	    }
	
	/**
	 * Test TVOKIDS Logo displaying
	 * <p>Date Created: 2016-08-09</p>
	 * <p>Date Modified: 2016-08-09<p>
	 * <p>Original Version: V1</p>
	 * <p>Modified Version: </p>
	 * <p>Xpath: 1</p>
	 * <p>Test Cases: 34169</p>
	 */
	@Test(groups = {"TC-34169"}, priority = 18)
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
	 * <p>Date Modified: 2016-08-10<p>
	 * <p>Original Version: V1</p>
	 * <p>Modified Version: </p>
	 * <p>Xpath: 1</p>
	 * <p>Test Cases: 34460</p>
	 */
	@Test(groups = {"TC-34460"}, priority = 19)
	public void testSearchIcon() throws IOException, IllegalArgumentException, MalformedURLException {
	       try{
	    	   // INITIALISATION:
	           helper.printXmlPath(new RuntimeException().getStackTrace()[0]);
	           driver = helper.getServerName(driver);
	           
	           // AGE 5 AND UNDER TEST:
		       helper.fileWriterPrinter("\n" + "AGE 5 AND UNDER TEST:");
		       helper.getUrlWaitUntil(driver, 10, Common.fiveAndUnderURL);		       
		       // ASSERT:
		       helper.assertWebElementExist(driver, new RuntimeException().getStackTrace()[0], Common.searchIcon);
		       helper.checkLinkURL(driver, new RuntimeException().getStackTrace()[0], Common.searchIcon, Common.fiveAndUnderSearchURL);
		       helper.clickLinkAndCheckURL(driver, new RuntimeException().getStackTrace()[0], Common.searchIcon, Common.fiveAndUnderSearchURL, true, false);
	
		       // AGE 6 AND OVER TEST:
		       helper.fileWriterPrinter("\n" + "AGE 6 AND OVER TEST:");
		       helper.getUrlWaitUntil(driver, 10, Common.sixAndOverURL);
		       // ASSERT:
		       helper.assertWebElementExist(driver, new RuntimeException().getStackTrace()[0], Common.searchIcon);
		       helper.checkLinkURL(driver, new RuntimeException().getStackTrace()[0], Common.searchIcon, Common.sixAndOverSearchURL);
		       helper.clickLinkAndCheckURL(driver, new RuntimeException().getStackTrace()[0], Common.searchIcon, Common.sixAndOverSearchURL, true, false);
	
	       } catch(Exception e) { helper.getExceptionDescriptive(e, new Exception().getStackTrace()[0], driver); }
	   }
	
	/**
	 * Test TVOKids logo is redirecting to age specific landing page
	 * <p>Date Created: 2016-08-11</p>
	 * <p>Date Modified: 2016-08-11<p>
	 * <p>Original Version: V1</p>
	 * <p>Modified Version: </p>
	 * <p>Xpath: 1</p>
	 * <p>Test Cases: 34140</p>
	 */
	@Test(groups = {"TC-34140"}, priority = 20)
	public void testTVOKidsLogoRedirectsToCorrectAgeLandingPage() throws IOException, IllegalArgumentException, MalformedURLException {
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
	           helper.clickLinkUrlWaitUntil(driver, 10, xpath); // URL = Common.fiveAndUnderURL + "/" + titleURL;
	           // ASSERT:
	           helper.checkLinkURL(driver, new RuntimeException().getStackTrace()[0], Common.kidsPageLogo, Common.fiveAndUnderURL);
	           helper.clickLinkAndCheckURL(driver, new RuntimeException().getStackTrace()[0], Common.kidsPageLogo, Common.fiveAndUnderURL, true, false);
	           
	           // AGE 6 AND OVER TEST:
	           helper.fileWriterPrinter("\n" + "AGE 6 AND OVER TEST:");
		       helper.getUrlWaitUntil(driver, 10, Common.sixAndOverURL);
	           helper.clickLinkUrlWaitUntil(driver, 10, xpath); // URL = Common.fiveAndUnderURL + "/" + titleURL;
	           // ASSERT:
	           helper.checkLinkURL(driver, new RuntimeException().getStackTrace()[0], Common.kidsPageLogo, Common.sixAndOverURL);
	           helper.clickLinkAndCheckURL(driver, new RuntimeException().getStackTrace()[0], Common.kidsPageLogo, Common.sixAndOverURL, true, false);
	           
	           } catch(Exception e) { helper.getExceptionDescriptive(e, new Exception().getStackTrace()[0], driver); }
	       }
	/**
	 * Test character banner images Prioritization Business Rule
	 * <p>Date Created: 2016-08-12</p>
	 * <p>Date Modified: 2016-08-12<p>
	 * <p>Original Version: V1</p>
	 * <p>Modified Version: </p>
	 * <p>Xpath: 1</p>
	 * <p>Test Cases: 34143</p>
	 */
	@Test(groups = {"TC-34143"}, priority = 21)
	public void testOnNowBubbleDoesIsNotMovingBackAfterRotation() throws IOException, IllegalArgumentException, MalformedURLException {
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
	           int defaultCoordinateX, movedCoordinateX, backCoordinateX, characterWidth, windowWidth;
	           
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
	           helper.fileWriterPrinter("\n" + "LINK GENERIC XPATH = " + xpath);
	           
	           // AGE 5 AND UNDER TEST:
	           helper.fileWriterPrinter("\n" + "AGE 5 AND UNDER TEST:");
	           helper.getUrlWaitUntil(driver, 10, Common.fiveAndUnderURL);
	           // MEASURE THE CHARACTER BUBBLE EXISTING (DEFAULT) LOCATION:
	           defaultCoordinateX = driver.findElement(By.xpath(xpath)).getLocation().getX();	           
	           characterWidth = helper.getElementWidth(driver, xpath);
	           windowWidth = driver.manage().window().getSize().getWidth();
	           // CLICK LEFT UNTIL DISAPPEAR:
	           while( driver.findElement(By.xpath(xpath)).getLocation().getX() < windowWidth )
	           { driver.findElement(By.xpath(Common.charBannerButtonLeft)).click(); Thread.sleep(1000); }		      
	           // MEASURE THE CHARACTER BUBBLE NEW LOCATION:
		       movedCoordinateX = driver.findElement(By.xpath(xpath)).getLocation().getX();
		       // MEASUREMENT REPORT:
	           helper.fileWriterPrinter("\n" + "CHARACTER              WIDTH = " + characterWidth);
	           helper.fileWriterPrinter("CHARACTER DEFAULT  COORDINATE = " + defaultCoordinateX);
		       helper.fileWriterPrinter("CHARACTER   MOVED  COORDINATE = " + movedCoordinateX);
		       helper.fileWriterPrinter("CURRENT BROWSER  WINDOW WIDTH = " + windowWidth);	           
		       // ASSERT CHARACTER BUBBLE DID NOT MOVE BACK:
	           Thread.sleep(3000);
	           backCoordinateX = driver.findElement(By.xpath(xpath)).getLocation().getX();
	           helper.assertEquals(driver, new RuntimeException().getStackTrace()[0], backCoordinateX, movedCoordinateX);
	           
	           // AGE 6 AND OVER TEST:
	           helper.fileWriterPrinter("\n" + "AGE 6 AND OVER TEST:");
	           helper.getUrlWaitUntil(driver, 10, Common.sixAndOverURL);
	           // MEASURE THE CHARACTER BUBBLE EXISTING (DEFAULT) LOCATION:
	           defaultCoordinateX = driver.findElement(By.xpath(xpath)).getLocation().getX();	           
	           characterWidth = helper.getElementWidth(driver, xpath);
	           windowWidth = driver.manage().window().getSize().getWidth();
	           // CLICK RIGHT UNTIL DISAPPEAR:
	           while( driver.findElement(By.xpath(xpath)).getLocation().getX() > (0 - characterWidth) )
	           { driver.findElement(By.xpath(Common.charBannerButtonRight)).click(); Thread.sleep(1000); }		      
	           // MEASURE THE CHARACTER BUBBLE NEW LOCATION:
		       movedCoordinateX = driver.findElement(By.xpath(xpath)).getLocation().getX();
		       // MEASUREMENT REPORT:
	           helper.fileWriterPrinter("\n" + "CHARACTER              WIDTH = " + characterWidth);
	           helper.fileWriterPrinter("CHARACTER DEFAULT  COORDINATE = " + defaultCoordinateX);
		       helper.fileWriterPrinter("CHARACTER   MOVED  COORDINATE = " + movedCoordinateX);
		       helper.fileWriterPrinter("CURRENT BROWSER  WINDOW WIDTH = " + windowWidth);	           
		       // ASSERT CHARACTER BUBBLE DID NOT MOVE BACK:
	           Thread.sleep(3000);
	           backCoordinateX = driver.findElement(By.xpath(xpath)).getLocation().getX();
	           helper.assertEquals(driver, new RuntimeException().getStackTrace()[0], backCoordinateX, movedCoordinateX);
	           
	       } catch(Exception e) { helper.getExceptionDescriptive(e, new Exception().getStackTrace()[0], driver); }
	    }
    
}
