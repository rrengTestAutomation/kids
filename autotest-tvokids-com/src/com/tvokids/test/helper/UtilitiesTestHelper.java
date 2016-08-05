package com.tvokids.test.helper;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;
import java.util.zip.ZipOutputStream;

import javax.swing.JTextField;

import com.tvokids.locator.Dictionary;
import com.tvokids.locator.Drupal;
import com.tvokids.locator.Common;

import org.apache.commons.io.FileUtils;
import org.apache.tools.zip.ZipEntry;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.*;
import org.openqa.selenium.interactions.internal.Coordinates;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.pagefactory.ByAll;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

public class UtilitiesTestHelper{
	WebDriver driverHelper;
	
	public WebDriver getServerName(WebDriver driver) throws IllegalArgumentException, MalformedURLException{
		try{
			String remoteOrLocal = System.getProperty("Server");
			String browser = System.getProperty("Browser");
			if (remoteOrLocal.equalsIgnoreCase("local") && browser.equalsIgnoreCase("firefox")){
				driver = new FirefoxDriver();
			}
			else if (remoteOrLocal.equalsIgnoreCase("remote") && browser.equalsIgnoreCase("firefox")){
				driver = new RemoteWebDriver(new URL("http://127.0.0.1:4444/wd/hub"), DesiredCapabilities.firefox());
			}
			else if (remoteOrLocal.equalsIgnoreCase("local") && browser.equalsIgnoreCase("chrome")){
				System.setProperty("webdriver.chrome.driver", Common.localDriversDir + "chromedriver.exe");
				driver = new ChromeDriver();
			}
			else if (remoteOrLocal.equalsIgnoreCase("remote") && browser.equalsIgnoreCase("chrome")){
				driver = new RemoteWebDriver(new URL("http://127.0.0.1:4444/wd/hub"), DesiredCapabilities.chrome());
			}
			else
				throw new IllegalArgumentException("input type not supported! ");
			return driver;
		}
		catch(WebDriverException e){
			String browser = System.getProperty("Browser");
			if (browser.equalsIgnoreCase("firefox")){
				driver = new RemoteWebDriver(new URL("http://127.0.0.1:4444/wd/hub"), DesiredCapabilities.firefox());
			}
			else if (browser.equalsIgnoreCase("chrome")){
				driver = new RemoteWebDriver(new URL("http://127.0.0.1:4444/wd/hub"), DesiredCapabilities.chrome());
			}
			return driver;
		}
	}
	
	/**
	 * @throws IOException
	 * @throws InterruptedException 
	 */
	public void logIn(WebDriver driver) throws IOException, InterruptedException {
		getUrlWaitUntil(driver, 15, Common.userLoginPage);		
		waitUntilElementPresence(driver, 60, By.id("edit-name"), "\"Username\" ", new RuntimeException().getStackTrace()[0]);		
		driver.findElement(By.id("edit-name")).sendKeys(Common.adminUsername);
		driver.findElement(By.id("edit-pass")).sendKeys(Common.adminPassword);
		driver.findElement(By.id(Drupal.submit)).click();
		Thread.sleep(500);
        if (driver.findElements(By.xpath("//*[text()='Have you forgotten your password?']")).size() == 1   ) {
        	throw new IOException("Sorry, unrecognized username or password");
        }
		waitUntilElementPresence(driver, 30, By.xpath(Drupal.drupalHomeIcon), "\"Home\" icon", new RuntimeException().getStackTrace()[0]);
	}
	
	/**
	 * @throws IOException
	 * @throws InterruptedException 
	 */
	public void logIn(WebDriver driver, String username, String password) throws IOException, InterruptedException {
		getUrlWaitUntil(driver, 15, Common.userLoginPage);		
		waitUntilElementPresence(driver, 60, By.id("edit-name"), "\"Username\" ", new RuntimeException().getStackTrace()[0]);		
		driver.findElement(By.id("edit-name")).sendKeys(username);
		driver.findElement(By.id("edit-pass")).sendKeys(password);
		driver.findElement(By.id(Drupal.submit)).click();
		Thread.sleep(500);
        if (driver.findElements(By.xpath("//*[text()='Have you forgotten your password?']")).size() == 1   ) {
        	throw new IOException("Sorry, unrecognized username or password");
        }
		waitUntilElementPresence(driver, 30, By.xpath(Drupal.drupalHomeIcon), "\"Home\" icon", new RuntimeException().getStackTrace()[0]);
	}
	
	/**
	 * @throws IOException
	 */
	public void logOut(WebDriver driver) throws IOException {		
		while ( (driver.findElements(By.xpath(Common.logout)).size() > 0) || (driver.findElements(By.xpath(Common.notFoundError)).size() > 0) ) {
			getUrlWaitUntil(driver, 15, Common.homeURL);
			driver.findElement(By.xpath(Common.logout)).click();
			waitUntilElementInvisibility(driver, 15, By.xpath(Common.logout), "\"Log out\" Button", new RuntimeException().getStackTrace()[0]);
			}
		}
	
	   /**
		* Deletes all the Contents by Content type ("" for all types) on user demand.
		* @throws IOException
		*/
		public void deleteAllContent(WebDriver driver, String type, String title, String user, StackTraceElement t) throws InterruptedException, IOException{
			try {
				if (type.length() == 0) { type = "- Any -"; }
				fileWriterPrinter("\n" + "Delete Content Type:   " + type.replace("- ", "").replace(" -", ""));
				fileWriterPrinter("Delete Content Title:  " + title);
				fileWriterPrinter("Delete Content Author: " + user);
				getUrlWaitUntil(driver, 15, Common.adminContentURL);
				
				WebElement dropwDownListBox = driver.findElement(By.id("edit-type"));
				Select clickThis = new Select(dropwDownListBox);
				Thread.sleep(2000);
				clickThis.selectByVisibleText(type);
				Thread.sleep(2000);
							
				driver.findElement(By.id("edit-title")).clear();          //pre-clear the Title filter field
				if(title.length() > 0) { driver.findElement(By.id("edit-title")).sendKeys(title); }  //typing selected title name - full or partial
				
				driver.findElement(By.id("edit-author")).clear();         //pre-clear the Author filter field
				if(user.length() > 0) { 
					driver.findElement(By.id("edit-author")).sendKeys(user); 
					int size = waitUntilElementList(driver, 5, Common.autoComplete, "auto-complete").size();
		            if (size == 1) { try { driver.findElement(By.xpath(Common.autoComplete)).click(); } catch(Exception e) { } }
		            waitUntilElementInvisibility(driver, 15, Common.autoComplete, "auto-complete", new Exception().getStackTrace()[0]);
		            } //typing the Author name filter as User Name

				driver.findElement(By.id("edit-submit-admin-views-node")).click(); //apply button;
	            waitUntilElementInvisibility(driver, 30, Common.ajaxThrobber, "Throbber", new Exception().getStackTrace()[0]);
				
				List<WebElement> list = driver.findElements(By.xpath("//*[@id='views-form-admin-views-node-system-1']//*[contains(text(),'No content available.')]"));

				int i = 1;
				while ((list.size() == 0) && (driver.findElements(By.xpath(Drupal.errorAjax)).size() < 1)) {
				fileWriterPrinter("\nPAGE-" + i + ": DELETING...");
				waitUntilElementVisibility(driver, 30, Drupal.selectAllCheckBox, "\"Select All\"", new Exception().getStackTrace()[0]);
				driver.findElement(By.xpath(Drupal.selectAllCheckBox)).click();  //check "Select All"
				
				// all rows selection			
				List<WebElement> elements = driver.findElements(By.xpath(Drupal.selectAllRowsButton));
				if (elements.size() > 0) {
				   WebElement element = driver.findElement(By.xpath(Drupal.selectAllRowsButton));
				   fileWriterPrinter(element.getAttribute("value"));
				   ((JavascriptExecutor)driver).executeScript("arguments[0].click();", element);
				}
				
				dropwDownListBox = driver.findElement(By.id("edit-operation"));
				clickThis = new Select(dropwDownListBox);
				Thread.sleep(2000);
				clickThis.selectByVisibleText("Delete");
				Thread.sleep(2000);	
	
				driver.findElement(By.id("edit-submit--2")).click(); // 'Execute' button;
				waitUntilElementVisibility(driver, 30, "//*[contains(text(),'You selected the following')]", "\"You selected the following\"", new Exception().getStackTrace()[0]);
				driver.findElement(By.id(Drupal.submit)).click(); // 'Confirm' button
				waitUntilElementInvisibility(driver, 5, By.id(Drupal.submit), "\"Save\" Button", new Exception().getStackTrace()[0]);
				waitUntilElementInvisibility(driver, 600, By.id(Drupal.progress), "Progress Bar", new Exception().getStackTrace()[0]);
				
				if(driver.findElements(By.xpath(Drupal.errorAjax)).size() > 0) { assertWebElementNotExist(driver, t, Drupal.errorAjax); }
				    
				waitUntilElementVisibility(driver, 30, Drupal.statusPerformedDelete, "\"Performed Delete\"", new Exception().getStackTrace()[0]);
				By message = By.xpath(Drupal.statusPerformedMessage);
				if (driver.findElements(message).size() > 0) { fileWriterPrinter(driver.findElement(message).getText()); }
				list = driver.findElements(By.xpath("//*[@id='views-form-admin-views-node-system-1']//*[contains(text(),'No content available.')]"));
				i++;
				}
			    } catch(Exception e) { getExceptionDescriptive(e, t, driver); }
		  }
		
	/**
	 * Submits Content and reports result (if final URL doesn't end with fingerprint - failure condition)
	 * @throws IOException
	 * @throws NumberFormatException
	 * @throws InterruptedException
	 */
	public int contentSubmit(WebDriver driver, int iteration, long fingerprint) throws IOException, NumberFormatException, InterruptedException {
	   String type = driver.findElement(By.xpath("//h1[@class='page-title']")).getText();
	   String previousURL = driver.getCurrentUrl();
	   driver.findElement(By.id(Drupal.submit)).click();
	   waitUntilUrl(driver, 15, previousURL);
	   iteration++;
	   String suffix = "-" + getNumberSuffix(iteration);
	   String success = "Successful \"" + type + "\" process!"; 
	   String   issue = "Not a successful \"" + type + "\" process...will try again..." + "(attempt #" + iteration + ")";
	   if (iteration > 1) { success = success + " (on " + iteration + suffix + " attempt)"; }
	   if (! driver.getCurrentUrl().endsWith(String.valueOf(fingerprint))) { 
	   	fileWriterPrinter(issue);
	   	if( driver.findElements(By.xpath(Drupal.errorMessage)).size() > 0 ) {
	   		String text = driver.findElement(By.xpath(Drupal.errorConsole)).getText();
	   		String[] error = text.split("\\n");
	   		String message, prompt;
	   		if( error.length > 1) { 
	   			message = error[1];
	   			prompt = error[0] + ": " + error[1];
	   			} else { message = error[0]; prompt = message; }
	   				fileWriterPrinter(prompt);
	   		if( iteration == 1 ) { getScreenShot(new RuntimeException().getStackTrace()[0], message, driver); }
	   	}
	   	} else { fileWriterPrinter(success); }
	   return iteration;
		  }
		
	/**
	 * Submits Content and reports result (if final URL doesn't contain fingerprint - failure condition)
	 * @throws IOException
	 * @throws NumberFormatException
	 * @throws InterruptedException
	 */
	public int contentSubmit(WebDriver driver, long fingerprint, int iteration) throws IOException, NumberFormatException, InterruptedException {
	   String type = driver.findElement(By.xpath("//h1[@class='page-title']")).getText();
	   String previousURL = driver.getCurrentUrl();
	   driver.findElement(By.id(Drupal.submit)).click();
	   waitUntilUrl(driver, 15, previousURL);
	   iteration++;
	   String suffix = "-" + getNumberSuffix(iteration);
	   String success = "Successful \"" + type + "\" process!"; 
	   String   issue = "Not a successful \"" + type + "\" process...will try again..." + "(attempt #" + iteration + ")";
	   if (iteration > 1) { success = success + " (on " + iteration + suffix + " attempt)"; }
	   if (! driver.getCurrentUrl().contains(String.valueOf(fingerprint))) { 
	   	fileWriterPrinter(issue);
	   	if( driver.findElements(By.xpath(Drupal.errorMessage)).size() > 0 ) {
	   		String text = driver.findElement(By.xpath(Drupal.errorConsole)).getText();
	   		String[] error = text.split("\\n");
	   		String message, prompt;
	   		if( error.length > 1) { 
	   			message = error[1];
	   			prompt = error[0] + ": " + error[1];
	   			} else { message = error[0]; prompt = message; }
	   				fileWriterPrinter(prompt);
	   		if( iteration == 1 ) { getScreenShot(new RuntimeException().getStackTrace()[0], message, driver); }
	   	}
	   	} else { fileWriterPrinter(success); }
	   return iteration;
	   }
	
	/**
	 * Create a Custom Brand
	 * @throws AWTException 
	 * @throws IOException
	 */
	@SuppressWarnings("finally")
	public long createCustomBrand(WebDriver driver, String title, String description, Boolean ifAgeUnder, Boolean ifAgeOver, Boolean ifSubmit, StackTraceElement t) throws AWTException, InterruptedException, IOException
	  {
	   long fingerprint = System.currentTimeMillis();
	   By browse, upload;
       try {
            getUrlWaitUntil(driver, 15, Drupal.customBrand);
			waitUntilElementPresence(driver, 15, By.id(Drupal.title), "Title", new Exception().getStackTrace()[0]);
			
			driver.findElement(By.id(Drupal.title)).clear();
			driver.findElement(By.id(Drupal.title)).sendKeys(title);
			
			driver.findElement(By.xpath(Drupal.description)).clear();
			driver.findElement(By.xpath(Drupal.description)).sendKeys(description);

			if (ifAgeUnder) { driver.findElement(By.id("edit-field-age-group-und-1")).click(); }
			if (ifAgeOver)  { driver.findElement(By.id("edit-field-age-group-und-2")).click(); }
			
			driver.findElement(By.id(Drupal.keywords)).clear();
			driver.findElement(By.id(Drupal.keywords)).sendKeys(title + " (keywords)");
			
			driver.findElement(By.xpath(Drupal.characterBannerVerticalTab)).click();
			browse = By.xpath(Drupal.characterBannerBrowse);
			upload = By.xpath(Drupal.characterBannerUpload);					
			uploader(driver, "bubble.jpg", browse, upload, "thumbnail", t);
			
		    driver.findElement(By.xpath(Drupal.heroBoxVerticalTab)).click();
			browse = By.xpath(Drupal.heroBoxBrowse);
			upload = By.xpath(Drupal.heroBoxUpload);
			uploader(driver, "hero.jpg", browse, upload, "image", t);
		    
		    driver.findElement(By.xpath(Drupal.tileVerticalTab)).click();
			browse = By.xpath(Drupal.tileSmallBrowse);
			upload = By.xpath(Drupal.tileSmallUpload);
			uploader(driver, "small.jpg", browse, upload, "image", t);
		    
		    driver.findElement(By.xpath(Drupal.tileVerticalTab)).click();
			browse = By.xpath(Drupal.tileLargeBrowse);
			upload = By.xpath(Drupal.tileLargeUpload);
			uploader(driver, "large.jpg", browse, upload, "image", t);

			if(ifSubmit) { driver.findElement(By.id(Drupal.submit)).click(); }
			
		    } catch(Exception e) { getScreenShot(new Exception().getStackTrace()[0], e, driver); } finally { return fingerprint; }	
	  }
	
	/**
	 * Create a Custom Brand
	 * @throws AWTException 
	 * @throws IOException
	 */
	@SuppressWarnings("finally")
	public long createCustomBrand(WebDriver driver, String title, Boolean ifAgeUnder, Boolean ifAgeOver, StackTraceElement t) throws AWTException, InterruptedException, IOException
	  {
	   long fingerprint = System.currentTimeMillis();
	   By browse, upload;
	   try {
	    	int i = 0;
	    	while (((! driver.getCurrentUrl().endsWith(String.valueOf(fingerprint))) || (i == 0)) && (i < 25)) {
            getUrlWaitUntil(driver, 15, Drupal.customBrand);
			waitUntilElementPresence(driver, 15, By.id(Drupal.title), "Title", new Exception().getStackTrace()[0]);
			
			driver.findElement(By.id(Drupal.title)).clear();
			driver.findElement(By.id(Drupal.title)).sendKeys(title);
			
			driver.findElement(By.xpath(Drupal.description)).clear();
			driver.findElement(By.xpath(Drupal.description)).sendKeys("This is \"" + title + "\" Description");

			if (ifAgeUnder) { driver.findElement(By.id("edit-field-age-group-und-1")).click(); }
			if (ifAgeOver)  { driver.findElement(By.id("edit-field-age-group-und-2")).click(); }
			
			driver.findElement(By.id(Drupal.keywords)).clear();
			driver.findElement(By.id(Drupal.keywords)).sendKeys(title + " (keywords)");
			
			driver.findElement(By.xpath(Drupal.characterBannerVerticalTab)).click();
			browse = By.xpath(Drupal.characterBannerBrowse);
			upload = By.xpath(Drupal.characterBannerUpload);					
			uploader(driver, "bubble.jpg", browse, upload, "thumbnail", t);
			
		    driver.findElement(By.xpath(Drupal.heroBoxVerticalTab)).click();
			browse = By.xpath(Drupal.heroBoxBrowse);
			upload = By.xpath(Drupal.heroBoxUpload);
			uploader(driver, "hero.jpg", browse, upload, "image", t);
		    
		    driver.findElement(By.xpath(Drupal.tileVerticalTab)).click();
			browse = By.xpath(Drupal.tileSmallBrowse);
			upload = By.xpath(Drupal.tileSmallUpload);
			uploader(driver, "small.jpg", browse, upload, "image", t);
		    
		    driver.findElement(By.xpath(Drupal.tileVerticalTab)).click();
			browse = By.xpath(Drupal.tileLargeBrowse);
			upload = By.xpath(Drupal.tileLargeUpload);
			uploader(driver, "large.jpg", browse, upload, "image", t);

			i = contentSubmit(driver, i, fingerprint);			
            }
		    } catch(Exception e) { getScreenShot(new Exception().getStackTrace()[0], e, driver); } finally { return fingerprint; }	
	  }
	
	/**
	 * Create a Custom Brand
	 * @throws InterruptedException 
	 * @throws NumberFormatException 
	 * @throws AWTException 
	 * @throws IOException
	 */
	public long createCustomBrand(WebDriver driver, String title, Boolean ifAgeUnder, Boolean ifAgeOver, long fingerprint, StackTraceElement t)
    throws NumberFormatException, InterruptedException, IOException	{
	   By browse, upload;
	    	int i = 0;
	    	while (((! driver.getCurrentUrl().contains(String.valueOf(fingerprint))) || (i == 0)) && (i < 25)) {
            getUrlWaitUntil(driver, 15, Drupal.customBrand);
			waitUntilElementPresence(driver, 15, By.id(Drupal.title), "Title", new Exception().getStackTrace()[0]);
			
			driver.findElement(By.id(Drupal.title)).clear();
			driver.findElement(By.id(Drupal.title)).sendKeys(title);
			
			driver.findElement(By.xpath(Drupal.description)).clear();
			driver.findElement(By.xpath(Drupal.description)).sendKeys("This is \"" + title + "\" Description");

			if (ifAgeUnder) { driver.findElement(By.id("edit-field-age-group-und-1")).click(); }
			if (ifAgeOver)  { driver.findElement(By.id("edit-field-age-group-und-2")).click(); }
			
			driver.findElement(By.id(Drupal.keywords)).clear();
			driver.findElement(By.id(Drupal.keywords)).sendKeys(title + " (keywords)");
			
			driver.findElement(By.xpath(Drupal.characterBannerVerticalTab)).click();
			browse = By.xpath(Drupal.characterBannerBrowse);
			upload = By.xpath(Drupal.characterBannerUpload);					
			uploader(driver, "bubble.jpg", browse, upload, "thumbnail", t);
			
		    driver.findElement(By.xpath(Drupal.heroBoxVerticalTab)).click();
			browse = By.xpath(Drupal.heroBoxBrowse);
			upload = By.xpath(Drupal.heroBoxUpload);
			uploader(driver, "hero.jpg", browse, upload, "image", t);
		    
		    driver.findElement(By.xpath(Drupal.tileVerticalTab)).click();
			browse = By.xpath(Drupal.tileSmallBrowse);
			upload = By.xpath(Drupal.tileSmallUpload);
			uploader(driver, "small.jpg", browse, upload, "image", t);
		    
		    driver.findElement(By.xpath(Drupal.tileVerticalTab)).click();
			browse = By.xpath(Drupal.tileLargeBrowse);
			upload = By.xpath(Drupal.tileLargeUpload);
			uploader(driver, "large.jpg", browse, upload, "image", t);

			i = contentSubmit(driver, fingerprint, i);			
            }
	    	return fingerprint;
	  }
	
	/**
	 * Create a Custom Brand using Robot
	 * @throws AWTException 
	 * @throws IOException
	 */
	@SuppressWarnings("finally")
	public long createCustomBrand(WebDriver driver, String title, Boolean ifAgeUnder, Boolean ifAgeOver, Robot robot) throws AWTException, InterruptedException, IOException
	  {
	   long fingerprint = System.currentTimeMillis();
	   By browse, upload;
       try {
    	    int i = 0;
    	    while (((! driver.getCurrentUrl().endsWith(String.valueOf(fingerprint))) || (i == 0)) && (i < 25)) {
            getUrlWaitUntil(driver, 15, Drupal.customBrand);
			waitUntilElementPresence(driver, 15, By.id(Drupal.title), "Title", new Exception().getStackTrace()[0]);
			
			driver.findElement(By.id(Drupal.title)).clear();
			driver.findElement(By.id(Drupal.title)).sendKeys(title);
			
			driver.findElement(By.xpath(Drupal.description)).clear();
			driver.findElement(By.xpath(Drupal.description)).sendKeys("This is \"" + title + "\" Description");

			if (ifAgeUnder) { driver.findElement(By.id("edit-field-age-group-und-1")).click(); }
			if (ifAgeOver)  { driver.findElement(By.id("edit-field-age-group-und-2")).click(); }
			
			driver.findElement(By.id(Drupal.keywords)).clear();
			driver.findElement(By.id(Drupal.keywords)).sendKeys(title + " (keywords)");
			
			driver.findElement(By.xpath(Drupal.characterBannerVerticalTab)).click();
			browse = By.xpath(Drupal.characterBannerBrowse);
			upload = By.xpath(Drupal.characterBannerUpload);					
			uploader(driver, "bubble.jpg", browse, upload, robot);
			
		    driver.findElement(By.xpath(Drupal.heroBoxVerticalTab)).click();
			browse = By.xpath(Drupal.heroBoxBrowse);
			upload = By.xpath(Drupal.heroBoxUpload);
			uploader(driver, "hero.jpg", browse, upload, robot);
		    
		    driver.findElement(By.xpath(Drupal.tileVerticalTab)).click();
			browse = By.xpath(Drupal.tileSmallBrowse);
			upload = By.xpath(Drupal.tileSmallUpload);
			uploader(driver, "small.jpg", browse, upload, robot);
		    
		    driver.findElement(By.xpath(Drupal.tileVerticalTab)).click();
			browse = By.xpath(Drupal.tileLargeBrowse);
			upload = By.xpath(Drupal.tileLargeUpload);
			uploader(driver, "large.jpg", browse, upload, robot);

			i = contentSubmit(driver, i, fingerprint);			
            }
		    } catch(Exception e) { getScreenShot(new Exception().getStackTrace()[0], e, driver); } finally { return fingerprint; }	
	  }
	
	/**
	 * Create a Custom Brand using Robot
	 * @throws AWTException 
	 * @throws IOException
	 */
	@SuppressWarnings("finally")
	public long createCustomBrand(WebDriver driver, String title, Boolean ifAgeUnder, Boolean ifAgeOver, Robot robot, long fingerprint) throws AWTException, InterruptedException, IOException
	  {
	   By browse, upload;
       try {
    	    int i = 0;
    	    while (((! driver.getCurrentUrl().contains(String.valueOf(fingerprint))) || (i == 0)) && (i < 25)) {
            getUrlWaitUntil(driver, 15, Drupal.customBrand);
			waitUntilElementPresence(driver, 15, By.id(Drupal.title), "Title", new Exception().getStackTrace()[0]);
			
			driver.findElement(By.id(Drupal.title)).clear();
			driver.findElement(By.id(Drupal.title)).sendKeys(title);
			
			driver.findElement(By.xpath(Drupal.description)).clear();
			driver.findElement(By.xpath(Drupal.description)).sendKeys("This is \"" + title + "\" Description");

			if (ifAgeUnder) { driver.findElement(By.id("edit-field-age-group-und-1")).click(); }
			if (ifAgeOver)  { driver.findElement(By.id("edit-field-age-group-und-2")).click(); }
			
			driver.findElement(By.id(Drupal.keywords)).clear();
			driver.findElement(By.id(Drupal.keywords)).sendKeys(title + " (keywords)");
			
			driver.findElement(By.xpath(Drupal.characterBannerVerticalTab)).click();
			browse = By.xpath(Drupal.characterBannerBrowse);
			upload = By.xpath(Drupal.characterBannerUpload);					
			uploader(driver, "bubble.jpg", browse, upload, robot, "thumbnail");
			
		    driver.findElement(By.xpath(Drupal.heroBoxVerticalTab)).click();
			browse = By.xpath(Drupal.heroBoxBrowse);
			upload = By.xpath(Drupal.heroBoxUpload);
			uploader(driver, "hero.jpg", browse, upload, robot, "image");
		    
		    driver.findElement(By.xpath(Drupal.tileVerticalTab)).click();
			browse = By.xpath(Drupal.tileSmallBrowse);
			upload = By.xpath(Drupal.tileSmallUpload);
			uploader(driver, "small.jpg", browse, upload, robot, "image");
		    
		    driver.findElement(By.xpath(Drupal.tileVerticalTab)).click();
			browse = By.xpath(Drupal.tileLargeBrowse);
			upload = By.xpath(Drupal.tileLargeUpload);
			uploader(driver, "large.jpg", browse, upload, robot, "image");

			i = contentSubmit(driver, fingerprint, i);			
            }
		    } catch(Exception e) { getScreenShot(new Exception().getStackTrace()[0], e, driver); } finally { return fingerprint; }	
	  }
	
	 public static void setClipboardData(String string) throws NumberFormatException, IOException {
			StringSelection stringSelection = new StringSelection(string);
			fileWriterPrinter("selection" + stringSelection);
   		    Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
			fileWriterPrinter("selection" + stringSelection);
	  }
	  
	 /**
	   * Re-formats a String as per syntacsis requirements of page URL (un-limited length)
	   */
	  public String reFormatStringForURL(String string) {
		  string = string.toLowerCase().replaceAll(" ", "-").replaceAll("--", "-");
		  if(string.endsWith("-")) { string = string.substring(0, (string.length() - 1)); }
		  return string;
		  }
	  
	 /**
	   * Re-formats a String as per syntacsis requirements of page URL (limited length)
	   */
	  public String reFormatStringForURL(String string, int length) {
		  string = string.substring(0, length);
		  return reFormatStringForURL(string);
		  }
	 
	  /**
	   * Verify the page URL is as expected.
	   * StackTraceElement "t" = to be delivered from external method using "t"-variable;
	   * @throws IOException
	   */
	  public void checkCurrentURL(WebDriver driver, StackTraceElement t, String expectedURL) throws IOException {
			  String actualURL = driver.getCurrentUrl();
			  if (actualURL.endsWith("/")) {
				  if (! expectedURL.endsWith("/")) { expectedURL += "/"; }
				  }
			  if (expectedURL.endsWith("/")) {
				  if (! actualURL.endsWith("/")) { actualURL += "/"; }
				  }
			  Assert.assertEquals(actualURL.toLowerCase(), expectedURL.toLowerCase(),
		                          getAssertEquals(t, driver, "Wrong URL!",
		                          actualURL.toLowerCase(), expectedURL.toLowerCase()));
			  }
	  
	  /**
	   * Verify the page URL ends as expected.
	   * StackTraceElement "t" = to be delivered from external method using "t"-variable;
	   * @throws IOException
	   */
	  public void checkCurrentURLendsWith(WebDriver driver, StackTraceElement t, String expectedURL) throws IOException {
			  String actualURL = driver.getCurrentUrl();
			  if (actualURL.endsWith("/")) {
				  if (! expectedURL.endsWith("/")) { expectedURL += "/"; }
				  }
			  if (expectedURL.endsWith("/")) {
				  if (! actualURL.endsWith("/")) { actualURL += "/"; }
				  }
			  actualURL = actualURL.substring(actualURL.lastIndexOf("/"), actualURL.length());
			  Assert.assertEquals(actualURL.toLowerCase(), expectedURL.toLowerCase(),
		                          getAssertEquals(t, driver, "Wrong URL ending!",
		                          actualURL.toLowerCase(), expectedURL.toLowerCase()));
			  }
	  
	  /**
	   * In current page, check a link/tab/locator by verifying that it links to the expected URL.
	   * @throws IOException
	   */
	  public void checkLinkURL(WebDriver driver, String locator, String expectedLinkURL) throws IOException{
		  try {
			if ( (!locator.contains("/a")) &&
				   (!locator.contains("@href")) &&
				   (!locator.contains("text()")) &&
				   (!locator.contains("/descendant::a")) &&
				   (!locator.contains("/ancestor::a")) &&
				   (!locator.contains("/parent::a"))
				 )
			  { if(!driver.findElement(By.xpath(locator + "/descendant::a")).getAttribute("href").equals(null)) { locator = locator + "/descendant::a"; } }			
		  } catch(Exception e) {}
		  
		  try {
			if ( (!locator.contains("/a")) &&
				   (!locator.contains("@href")) &&
				   (!locator.contains("text()")) &&
				   (!locator.contains("/descendant::a")) &&
				   (!locator.contains("/ancestor::a")) &&
				   (!locator.contains("/parent::a"))
				 )
		      { if(!driver.findElement(By.xpath(locator + "/ancestor::a")).getAttribute("href").equals(null)) { locator = locator + "/ancestor::a"; } }
		  } catch(Exception e) {}
		  
	      String actualLinkURL = driver.findElement(By.xpath(locator)).getAttribute("href");
	      fileWriterPrinter("\n Current: " + driver.getCurrentUrl());
		  fileWriterPrinter(  "    Link: " + actualLinkURL);	      
	      int lengthActual = actualLinkURL.length();
	      int lengthExpected = expectedLinkURL.length();	      
	      if (lengthActual > lengthExpected) { actualLinkURL = actualLinkURL.substring(0, lengthExpected); }	      
	      if (actualLinkURL.endsWith("/")) { if (! expectedLinkURL.endsWith("/")) { expectedLinkURL += "/"; } }
	      if (expectedLinkURL.endsWith("/")) { if (! actualLinkURL.endsWith("/")) { actualLinkURL += "/"; } }	      
	      Assert.assertEquals(actualLinkURL.toLowerCase(), expectedLinkURL.toLowerCase(),
			                  getAssertEquals(new RuntimeException().getStackTrace()[0],
                              driver, "URL are not the same!",
                              actualLinkURL.toLowerCase(), expectedLinkURL.toLowerCase()));
	  }
	  
	  /**
	   * In current page, check a link/tab/locator by verifying that it links to the expected URL.
	   * StackTraceElement = internal "Assert.assertEquals"
	   * @throws IOException
	   */
	  public void checkLinkURL(WebDriver driver, StackTraceElement t, String locator, String expectedLinkURL) throws IOException{
		  try {
			if ( (!locator.contains("/a")) &&
				   (!locator.contains("@href")) &&
				   (!locator.contains("text()")) &&
				   (!locator.contains("/descendant::a")) &&
				   (!locator.contains("/ancestor::a")) &&
				   (!locator.contains("/parent::a"))
				 )
			  { if(!driver.findElement(By.xpath(locator + "/descendant::a")).getAttribute("href").equals(null)) { locator = locator + "/descendant::a"; } }			
		  } catch(Exception e) {}
		  
		  try {
			if ( (!locator.contains("/a")) &&
				   (!locator.contains("@href")) &&
				   (!locator.contains("text()")) &&
				   (!locator.contains("/descendant::a")) &&
				   (!locator.contains("/ancestor::a")) &&
				   (!locator.contains("/parent::a"))
				 )
		      { if(!driver.findElement(By.xpath(locator + "/ancestor::a")).getAttribute("href").equals(null)) { locator = locator + "/ancestor::a"; } }
		  } catch(Exception e) {}
		  
		  String actualLinkURL = driver.findElement(By.xpath(locator)).getAttribute("href");
		  fileWriterPrinter("\n Current: " + driver.getCurrentUrl());
		  fileWriterPrinter(  "    Link: " + actualLinkURL);		  
		  int lengthActual = actualLinkURL.length();
		  int lengthExpected = expectedLinkURL.length();		  
		  if (lengthActual > lengthExpected) { actualLinkURL = actualLinkURL.substring(0, lengthExpected); }		  
		  if (actualLinkURL.endsWith("/")) { if (! expectedLinkURL.endsWith("/")) { expectedLinkURL += "/"; } }
		  if (expectedLinkURL.endsWith("/")) { if (! actualLinkURL.endsWith("/")) { actualLinkURL += "/"; } }		  
		  Assert.assertEquals(actualLinkURL.toLowerCase(), expectedLinkURL.toLowerCase(),
		                      getAssertEquals(t, driver, "URL are not the same!",
                              actualLinkURL.toLowerCase(), expectedLinkURL.toLowerCase()));
	  }
	  
	  /**
	   * In current page, click a link/tab/locator, verify the page goes to an URL as expected.
 	   * StackTraceElement = internal "Assert.assertEquals"
	   * @throws IOException
	   */
	  public void clickLinkAndCheckURL(WebDriver driver, String locator, String expectedURL) throws IOException{ 
			  String previousURL = driver.getCurrentUrl();
			  driver.findElement(By.xpath(locator)).click();
			  waitUntilUrl(driver, 10, previousURL);
			  String actualURL = driver.getCurrentUrl();
			  if (actualURL.endsWith("/")) {
				  if (! expectedURL.endsWith("/")) { expectedURL += "/"; }
			  }
			  if (expectedURL.endsWith("/")) {
				  if (! actualURL.endsWith("/")) { actualURL += "/"; }
			  }
			  Assert.assertEquals(actualURL.toLowerCase(), expectedURL.toLowerCase(),
					              getAssertEquals(new RuntimeException().getStackTrace()[0],
                                  driver, "URL are not the same!",
                                  actualURL.toLowerCase(), expectedURL.toLowerCase()));
	  }
	  
	  /**
	   * In current page, click a link/tab/locator, verify the page goes to an URL as expected.
	   * StackTraceElement = internal "Assert.assertEquals"
	   * Returns back to initial URL as per user request (ifBack = true);
	   * @throws IOException
	   */	  
	  public void clickLinkAndCheckURL(WebDriver driver, String locator, String expectedURL, Boolean ifBack) throws IOException{
			  String previousURL = driver.getCurrentUrl();
			  driver.findElement(By.xpath(locator)).click();
			  waitUntilUrl(driver, 10, previousURL);
			  String actualURL = driver.getCurrentUrl();
			  if (actualURL.endsWith("/")) {
				  if (! expectedURL.endsWith("/")) { expectedURL += "/"; }
			  }
			  if (expectedURL.endsWith("/")) {
				  if (! actualURL.endsWith("/")) { actualURL += "/"; }
			  }
			  Assert.assertEquals(actualURL.toLowerCase(), expectedURL.toLowerCase(),
					              getAssertEquals(new RuntimeException().getStackTrace()[0],
                                  driver, "URL are not the same!",
                                  actualURL.toLowerCase(), expectedURL.toLowerCase()));
			  if (ifBack) {
				  driver.navigate().back();
				  waitUntilUrl(driver, 10, actualURL, false);
			  }		  
	  }
	  
	  /**
	   * In current page, click a link/tab/locator, verify the page goes to an URL as expected.
	   * StackTraceElement = internal "Assert.assertEquals"
	   * Returns back to initial URL as per user request (ifBack = true);
	   * Prompts URL navigation as per user request (ifPrompt = true);
	   * @throws IOException
	   */	  
	  public void clickLinkAndCheckURL(WebDriver driver, String locator, String expectedURL, Boolean ifBack, Boolean ifPrompt) throws IOException{
			  String previousURL = driver.getCurrentUrl();
			  driver.findElement(By.xpath(locator)).click();
			  waitUntilUrl(driver, 15, previousURL, ifPrompt);
			  String actualURL = driver.getCurrentUrl();
			  if (actualURL.endsWith("/")) {
				  if (! expectedURL.endsWith("/")) { expectedURL += "/"; }
			  }
			  if (expectedURL.endsWith("/")) {
				  if (! actualURL.endsWith("/")) { actualURL += "/"; }
			  }
			  Assert.assertEquals(actualURL.toLowerCase(), expectedURL.toLowerCase(),
					              getAssertEquals(new RuntimeException().getStackTrace()[0],
                                  driver, "URL are not the same!",
                                  actualURL.toLowerCase(), expectedURL.toLowerCase()));
			  if (ifBack) {
				  driver.navigate().back();
				  waitUntilUrl(driver, 15, actualURL, ifPrompt);
			  }		  
	  }
	  
	  /**
	   * In current page, click a link/tab/locator, verify the page goes to an URL as expected.
	   * StackTraceElement "t" = to be delivered from external method using "t"-variable;
	   * @throws IOException
	   */
	  public void clickLinkAndCheckURL(WebDriver driver, StackTraceElement t, String locator, String expectedURL) throws IOException{
			  String previousURL = driver.getCurrentUrl();
			  driver.findElement(By.xpath(locator)).click();
			  waitUntilUrl(driver, 10, previousURL);
			  String actualURL = driver.getCurrentUrl();
			  if (actualURL.endsWith("/")) {
				  if (! expectedURL.endsWith("/")) { expectedURL += "/"; }
			  }
			  if (expectedURL.endsWith("/")) {
				  if (! actualURL.endsWith("/")) { actualURL += "/"; }
			  }
			  Assert.assertEquals(actualURL.toLowerCase(), expectedURL.toLowerCase(),
		                          getAssertEquals(t, driver, "URL are not the same!",
		                          actualURL.toLowerCase(), expectedURL.toLowerCase()));	
	  }
	  
	  /**
	   * In current page, click a link/tab/locator, verify the page goes to an URL as expected.
	   * StackTraceElement "t" = to be delivered from external method using "t"-variable;
	   * @throws IOException
	   */
	  public void clickLinkAndCheckURL(WebDriver driver, StackTraceElement t, By by, String expectedURL) throws IOException{
			  String previousURL = driver.getCurrentUrl();
			  driver.findElement(by).click();
			  waitUntilUrl(driver, 10, previousURL);
			  String actualURL = driver.getCurrentUrl();
			  if (actualURL.endsWith("/")) {
				  if (! expectedURL.endsWith("/")) { expectedURL += "/"; }
			  }
			  if (expectedURL.endsWith("/")) {
				  if (! actualURL.endsWith("/")) { actualURL += "/"; }
			  }
			  Assert.assertEquals(actualURL.toLowerCase(), expectedURL.toLowerCase(),
		                          getAssertEquals(t, driver, "URL are not the same!",
		                          actualURL.toLowerCase(), expectedURL.toLowerCase()));	
	  }
	  
	  /**
	   * In current page, click a link/tab/locator, verify the page goes to an URL as expected.
	   * StackTraceElement "t" = to be delivered from external method using "t"-variable;
	   * Returns back to initial URL as per user request (ifBack = true);
	   * @throws IOException
	   */
	  public void clickLinkAndCheckURL(WebDriver driver, StackTraceElement t, String locator, String expectedURL, Boolean ifBack) throws IOException{
			  String previousURL = driver.getCurrentUrl();
			  driver.findElement(By.xpath(locator)).click();
			  waitUntilUrl(driver, 10, previousURL);
			  String actualURL = driver.getCurrentUrl();
			  if (actualURL.endsWith("/")) {
				  if (! expectedURL.endsWith("/")) { expectedURL += "/"; }
			  }
			  if (expectedURL.endsWith("/")) {
				  if (! actualURL.endsWith("/")) { actualURL += "/"; }
			  }
			  Assert.assertEquals(actualURL.toLowerCase(), expectedURL.toLowerCase(),
		              getAssertEquals(t, driver, "URL are not the same!",
		              actualURL.toLowerCase(), expectedURL.toLowerCase()));
			  if (ifBack) { getUrlWaitUntil(driver, 15, previousURL); }		  
	  }
	  
	  /**
	   * In current page, click a link/tab/locator, verify the page goes to an URL as expected.
	   * StackTraceElement "t" = to be delivered from external method using "t"-variable;
	   * Returns back to initial URL as per user request (ifBack = true);
	   * @throws IOException
	   */
	  public void clickLinkAndCheckURL(WebDriver driver, StackTraceElement t, By by, String expectedURL, Boolean ifBack) throws IOException{
			  String previousURL = driver.getCurrentUrl();
			  driver.findElement(by).click();
			  waitUntilUrl(driver, 10, previousURL);
			  String actualURL = driver.getCurrentUrl();
			  if (actualURL.endsWith("/")) {
				  if (! expectedURL.endsWith("/")) { expectedURL += "/"; }
			  }
			  if (expectedURL.endsWith("/")) {
				  if (! actualURL.endsWith("/")) { actualURL += "/"; }
			  }
			  Assert.assertEquals(actualURL.toLowerCase(), expectedURL.toLowerCase(),
		              getAssertEquals(t, driver, "URL are not the same!",
		              actualURL.toLowerCase(), expectedURL.toLowerCase()));
			  if (ifBack) { getUrlWaitUntil(driver, 15, previousURL); }		  
	  }
	  
	  /**
	   * In current page, click a link/tab/locator, verify the page goes to an URL as expected.
	   * StackTraceElement = internal "Assert.assertEquals"
	   * Returns back to initial URL as per user request (ifBack = true);
	   * Prompts URL navigation as per user request (ifPrompt = true);
       * @throws IOException
	   */
	  public void clickLinkAndCheckURL(WebDriver driver, StackTraceElement t, String locator, String expectedURL, Boolean ifBack, Boolean ifPrompt) throws IOException{
			  String previousURL = driver.getCurrentUrl();
			  driver.findElement(By.xpath(locator)).click();
			  waitUntilUrl(driver, 15, previousURL, ifPrompt);
			  String actualURL = driver.getCurrentUrl();
			  if (actualURL.endsWith("/")) {
				  if (! expectedURL.endsWith("/")) { expectedURL += "/"; }
			  }
			  if (expectedURL.endsWith("/")) {
				  if (! actualURL.endsWith("/")) { actualURL += "/"; }
			  }
			  Assert.assertEquals(actualURL.toLowerCase(), expectedURL.toLowerCase(),
		              getAssertEquals(t, driver, "URL are not the same!",
		              actualURL.toLowerCase(), expectedURL.toLowerCase()));
			  if (ifBack) { getUrlWaitUntil(driver, 15, previousURL, ifPrompt); }		  
	  }
	  
	  /**
	   * In current page, click a link/tab/locator, verify the page goes to an URL as expected.
	   * StackTraceElement = internal "Assert.assertEquals"
	   * Returns back to initial URL as per user request (ifBack = true);
	   * Prompts URL navigation as per user request (ifPrompt = true);
       * @throws IOException
	   */
	  public void clickLinkAndCheckURL(WebDriver driver, StackTraceElement t, By by, String expectedURL, Boolean ifBack, Boolean ifPrompt) throws IOException{
			  String previousURL = driver.getCurrentUrl();
			  driver.findElement(by).click();
			  waitUntilUrl(driver, 15, previousURL, ifPrompt);
			  String actualURL = driver.getCurrentUrl();
			  if (actualURL.endsWith("/")) {
				  if (! expectedURL.endsWith("/")) { expectedURL += "/"; }
			  }
			  if (expectedURL.endsWith("/")) {
				  if (! actualURL.endsWith("/")) { actualURL += "/"; }
			  }
			  Assert.assertEquals(actualURL.toLowerCase(), expectedURL.toLowerCase(),
		              getAssertEquals(t, driver, "URL are not the same!",
		              actualURL.toLowerCase(), expectedURL.toLowerCase()));
			  if (ifBack) { getUrlWaitUntil(driver, 15, previousURL, ifPrompt); }		  
	  }
	  
	  /**
	   * In current page, click a link/tab/locator, verify the page goes to an URL which contains the expected.
	   * StackTraceElement = internal "Assert.assertEquals"
	   * Returns back to initial URL as per user request (ifBack = true);
	   * Prompts URL navigation as per user request (ifPrompt = true);
       * @throws IOException
	   */
	  public void clickLinkAndCheckURLstartsWith(WebDriver driver, StackTraceElement t, String locator, String expectedURLstartsWith, Boolean ifBack, Boolean ifPrompt) throws IOException{
			  String previousURL = driver.getCurrentUrl();
			  driver.findElement(By.xpath(locator)).click();
			  waitUntilUrl(driver, 15, previousURL, ifPrompt);
			  String actualURL = driver.getCurrentUrl();		  		  
			  // ASSERTION:
			  fileWriterPrinter("\nExpected URL: " + expectedURLstartsWith + "\n  Actual URL: " + actualURL);
			  if (actualURL.startsWith(expectedURLstartsWith) == false) {
				       fileWriterPrinter("      Result: FAILED! Actual URL does not start with the Expected...");
			  } else { fileWriterPrinter("      Result: OK (Actual URL starts with the Expected)"); }
			  Assert.assertTrue((actualURL.toLowerCase()).startsWith(expectedURLstartsWith.toLowerCase()),
                                 UtilitiesTestHelper.getAssertTrue(t, driver, "URL not found!",
				                (actualURL.toLowerCase()).startsWith(expectedURLstartsWith.toLowerCase())));		  			  
			  if (ifBack) {
				  driver.navigate().back();
				  waitUntilUrl(driver, 15, actualURL, ifPrompt);
			  }		  
	  }
	  
	  /**
	   * From current page, go to expected URL, and verify the page goes to an URL as expected.
	   * StackTraceElement = internal "Assert.assertEquals"
	   * Returns back to initial URL as per user request (ifBack = true);
	   * Prompts URL navigation as per user request (ifPrompt = true);
       * @throws IOException
	   */
	  public void gotoURLandCheckURL(WebDriver driver, StackTraceElement t, String url, String expectedURL, Boolean ifBack, Boolean ifPrompt) throws IOException{			  
			  getUrlWaitUntil(driver, 15, url);			  
			  String actualURL = driver.getCurrentUrl();
			  if (actualURL.endsWith("/")) {
				  if (! expectedURL.endsWith("/")) { expectedURL += "/"; }
			  }
			  if (expectedURL.endsWith("/")) {
				  if (! actualURL.endsWith("/")) { actualURL += "/"; }
			  }
			  Assert.assertEquals(actualURL.toLowerCase(), expectedURL.toLowerCase(),
		              getAssertEquals(t, driver, "URL are not the same!",
		              actualURL.toLowerCase(), expectedURL.toLowerCase()));
			  if (ifBack) {
				  driver.navigate().back();
				  waitUntilUrl(driver, 15, actualURL, ifPrompt);
			  }			  
	  }
	  
	  /**
	   * In current page, click a link/tab/locator, verify the page goes to an URL as expected.
	   * StackTraceElement "t" = to be delivered from external method using "t"-variable;
	   * @throws IOException
	   */
	  public void clickLinkHandleTabAndCheckURL(WebDriver driver, StackTraceElement t, String locator, String expectedURL) throws IOException {  
	          // CLICK AND GO TO NEW TAB:
		      String handle = driver.getWindowHandle();
		      clickHandleTab(driver, handle, locator);
				
			  // ASSERTION:
			  String actualURL = driver.getCurrentUrl();
			  if (actualURL.endsWith("/")) {
				  if (! expectedURL.endsWith("/")) { expectedURL += "/"; }
			  }
			  if (expectedURL.endsWith("/")) {
				  if (! actualURL.endsWith("/")) { actualURL += "/"; }
			  }
			  Assert.assertEquals(actualURL.toLowerCase(), expectedURL.toLowerCase(),
					              getAssertEquals(t, driver, "URL are not the same!",
					              actualURL.toLowerCase(), expectedURL.toLowerCase()));
		      
			  // SWITCHING BACK TO INITIAL TAB:
	          driver.switchTo().window(handle);
	  }
	  
	  /**
	   * In current page, click a link/tab/locator, verify the page goes to an URL which contains the expected.
	   * StackTraceElement "t" = to be delivered from external method using "t"-variable;
	   * @throws IOException
	   */
	  public void clickLinkHandleTabAndCheckURLstartsWith(WebDriver driver, StackTraceElement t, String locator, String expectedURLstartsWith) throws IOException {
		      // CLICK AND GO TO NEW TAB:
			  String handle = driver.getWindowHandle();
			  clickHandleTab(driver, handle, locator);
				
			  // ASSERTION:
			  String actualURL = driver.getCurrentUrl();
			  fileWriterPrinter("\nExpected URL: " + expectedURLstartsWith + "\n  Actual URL: " + actualURL);
			  if (actualURL.startsWith(expectedURLstartsWith) == false) {
				       fileWriterPrinter("      Result: FAILED! Actual URL does not start with the Expected...");
			  } else { fileWriterPrinter("      Result: OK (Actual URL starts with the Expected)"); }
			  Assert.assertTrue((actualURL.toLowerCase()).startsWith(expectedURLstartsWith.toLowerCase()),
                                 UtilitiesTestHelper.getAssertTrue(t, driver, "URL not found!",
				                (actualURL.toLowerCase()).startsWith(expectedURLstartsWith.toLowerCase())));
			  
		      // SWITCHING BACK TO INITIAL TAB:
	          driver.switchTo().window(handle);
	  }
	  
	  /**
	   * In current page, click a link/tab/locator, verify the page goes to an URL as expected.
	   * StackTraceElement "t" = to be delivered from external method using "t"-variable;
	   * @throws IOException
	   */
	  public void clickLinkHandleWindowAndCheckURL(WebDriver driver, StackTraceElement t, String locator, String expectedURL) throws IOException {  
	          // CLICK AND GO TO NEW TAB:
		      String handle = driver.getWindowHandle();
		      clickHandleWindow(driver, handle, locator);
				
			  // ASSERTION:
			  String actualURL = driver.getCurrentUrl();
			  if (actualURL.endsWith("/")) {
				  if (! expectedURL.endsWith("/")) { expectedURL += "/"; }
			  }
			  if (expectedURL.endsWith("/")) {
				  if (! actualURL.endsWith("/")) { actualURL += "/"; }
			  }
			  Assert.assertEquals(actualURL.toLowerCase(), expectedURL.toLowerCase(), getAssertEquals(t, driver, "URL are not the same!", actualURL.toLowerCase(), expectedURL.toLowerCase()));
		      
			  // SWITCHING BACK TO INITIAL TAB:
	          driver.switchTo().window(handle);
	  }
	  
	  /**
	   * In current page, click a link/tab/locator, verify the page goes to an URL which contains the expected.
	   * StackTraceElement "t" = to be delivered from external method using "t"-variable;
	   * @throws IOException
	   */
	  public void clickLinkHandleWindowAndCheckURLstartsWith(WebDriver driver, StackTraceElement t, String locator, String expectedURLstartsWith) throws IOException {
		      // CLICK AND GO TO NEW TAB:
			  String handle = driver.getWindowHandle();
			  clickHandleWindow(driver, handle, locator);
				
			  // ASSERTION:
			  String actualURL = driver.getCurrentUrl();
			  fileWriterPrinter("\nExpected URL: " + expectedURLstartsWith + "\n  Actual URL: " + actualURL);
			  if (actualURL.startsWith(expectedURLstartsWith) == false) {
				       fileWriterPrinter("      Result: FAILED! Actual URL does not start with the Expected...");
			  } else { fileWriterPrinter("      Result: OK (Actual URL starts with the Expected)"); }
			  Assert.assertTrue((actualURL.toLowerCase()).startsWith(expectedURLstartsWith.toLowerCase()),
			                     UtilitiesTestHelper.getAssertTrue(t, driver, "URL not found!",
			                    (actualURL.toLowerCase()).startsWith(expectedURLstartsWith.toLowerCase())));
			  
		      // SWITCHING BACK TO INITIAL TAB:
	          driver.switchTo().window(handle);
	  }
	  
	  public int getFileSize(URL url) {
		    HttpURLConnection conn = null;
		    try {
		        conn = (HttpURLConnection) url.openConnection();
		        conn.setRequestMethod("HEAD");
		        conn.getInputStream();
		        return conn.getContentLength();
		    } catch(IOException e) { return -1; } finally { conn.disconnect(); }
	  }

	  /**
	   * Select from Any Selection List
	   * @throws InterruptedException 
	   */
	  public void selectOption(WebDriver driver, Object selectionListXpath, String visibleText) throws InterruptedException{
			WebElement dropwDownListBox = driver.findElement(By.xpath(String.valueOf(selectionListXpath)));
			Select clickThis = new Select(dropwDownListBox);
			Thread.sleep(1000);
		    clickThis.selectByVisibleText(visibleText); 
			Thread.sleep(1000);
	  }
	  
	  /**
	   * Select from Any Selection List
	   * @throws InterruptedException 
	   */
	  public void selectOption(WebDriver driver, String secondaryPartialSelectionListID, String visibleText) throws InterruptedException{
			WebElement dropwDownListBox = driver.findElement(By.xpath(
					   "//*[contains(@id,'edit-field') and contains(@id,'" +
			           secondaryPartialSelectionListID + 
			           "')]"
			           ));
			Select clickThis = new Select(dropwDownListBox);
			Thread.sleep(1000);
		    clickThis.selectByVisibleText(visibleText); 
			Thread.sleep(1000);
	  }
	  
	  /**
	   * Select a content category.
	   * @throws InterruptedException 
	   */
	  public void selectContentCategory(WebDriver driver, String contentType) throws InterruptedException{
			WebElement dropwDownListBox = driver.findElement(By.xpath("//*[contains(@id,'edit-field') and contains(@id,'-content-category-und-hierarchical-select-selects-0')]"));
			Select clickThis = new Select(dropwDownListBox);
			Thread.sleep(1000);
		    clickThis.selectByVisibleText(contentType); 
			Thread.sleep(1000);
	  }
	  
	  /**
	   * Image direct upload engine
	   * @param driver
	   * @param image
	   * @param browse
	   * @param upload
	   * @throws InterruptedException
	   */
      public void uploader(WebDriver driver, String image, By browse, By upload) throws InterruptedException {
		  String imageDir = Common.localImageDir;
		  String imagePath = imageDir + File.separator + image;
		  try{
        	  driver.findElement(browse).sendKeys(imagePath);
        	  Thread.sleep(1000);
        	  driver.findElement(upload).click();
        	  waitUntilElementInvisibility(driver, 10, Common.ajaxThrobber, "Throbber", new Exception().getStackTrace()[0]);
          }catch(Throwable e) { e.printStackTrace(); }
      }
	  
	  /**
	   * Image direct upload engine with image appplication notification
	   * @param driver
	   * @param image
	   * @param browse
	   * @param upload
	   * @throws InterruptedException
	   * @throws IOException 
	   * @throws NumberFormatException 
	   */
      public void uploader(WebDriver driver, String image, By browse, By upload, String name, StackTraceElement t) throws InterruptedException, NumberFormatException, IOException {
		  String imageDir = Common.localImageDir;
		  String imagePath = imageDir + File.separator + image;
		  
		  int i = 0;
		  String xpath = "//a[contains(@type,'image/jpeg;')][text()='" + image + "']";
		  By element = By.xpath(xpath);
		  int size = 0;
		  int errors = 0;
		  while ((size == 0) && (errors == 0)) {			  		        	 
		        	 if (i > 0) { fileWriterPrinter("Not a successful \"" + image + "\" " + name + " upload...will try again..." + "[Attempt #" + (i+1) + "]"); }
		        	 driver.findElement(browse).sendKeys(imagePath);
		        	 Thread.sleep(1000);
		        	 driver.findElement(upload).click();
		        	 waitUntilElementInvisibility(driver, 10, Common.ajaxThrobber, "Throbber", new Exception().getStackTrace()[0]);
		        	 if (i > 1) { errors = driver.findElements(By.xpath(Drupal.errorUpload)).size(); }
		        	 if (errors > 0) { 
		        		              fileWriterPrinter("\n" + "ERROR! The file could not be uploaded...");
		        		              moveToElement(driver, Drupal.confirmButton);
		        		              }
		        	 if (errors > 0) { assertWebElementNotExist(driver, t, By.xpath(Drupal.errorUpload)); }	         
		         i++;
		         Thread.sleep(1000);
		         waitUntilElementPresence(driver, 2, element, "\"" + image + "\"", t, false);
		         size = driver.findElements(element).size();		         
		         }
		  if (size == 1) { fileWriterPrinter("Successful \"" + image + "\" " + name + " upload!"); }
		  Thread.sleep(1000);
	  }

	  /**
	   * image upload engine using Robot
	   * @param driver
	   * @param image
	   * @param element
	   * @param name
	   * @throws IOException
	   */
	  public void uploader(WebDriver driver, String image, By browse, By upload, Robot robot) throws NumberFormatException, IOException {
		  String parentWindowHandle = driver.getWindowHandle();
		  int i = 0;
		  String xpath = "//a[contains(@type,'image/jpeg;')][text()='" + image + "']";
		  By element = By.xpath(xpath);
		  int size = driver.findElements(element).size();		  
		  while (size == 0) {
		         try {
		        	 String previousURL = driver.getCurrentUrl();
		             if (i > 0) { fileWriterPrinter("Not a successful \"" + image + "\" image upload...will try again..."); }
			   
		             robot.keyPress(KeyEvent.VK_CANCEL);    // close previous in the beginning
		             robot.keyRelease(KeyEvent.VK_CANCEL);
		             driver.findElement(browse).click();
			   
		             waitUntilUrl(driver, previousURL); 
		             chooseImageRobot(driver, image, robot);
		             alertHandler(driver);
		             closeAllOtherWindows(driver, parentWindowHandle);// driver.switchTo().window(parentWindowHandle);
		             
			         driver.findElement(upload).click();
			         waitUntilElementInvisibility(driver, 10, Common.ajaxThrobber, "Throbber", new Exception().getStackTrace()[0]);
		         } catch(Exception e) {}
		         i++;
		         size = driver.findElements(element).size();         
	      }
		  if (size == 1) { fileWriterPrinter("Successful \"" + image + "\" image upload!"); }
	  }
	  
	  /**
	   * image upload engine using Robot with image appplication notification
	   * @param driver
	   * @param image
	   * @param element
	   * @param name
	   * @throws IOException
	   */
	  public void uploader(WebDriver driver, String image, By browse, By upload, Robot robot, String name) throws NumberFormatException, IOException {
		  String parentWindowHandle = driver.getWindowHandle();
		  int i = 0;
		  String xpath = "//a[contains(@type,'image/jpeg;')][text()='" + image + "']";
		  By element = By.xpath(xpath);
		  int size = driver.findElements(element).size();		  
		  while (size == 0) {
		         try {
		        	 String previousURL = driver.getCurrentUrl();
		             if (i > 0) { fileWriterPrinter("Not a successful \"" + image + "\" " + name + " upload...will try again..."); }
			   
		             robot.keyPress(KeyEvent.VK_CANCEL);    // close previous in the beginning
		             robot.keyRelease(KeyEvent.VK_CANCEL);
		             driver.findElement(browse).click();
			   
		             waitUntilUrl(driver, previousURL); 
		             chooseImageRobot(driver, image, robot);
		             alertHandler(driver);
		             closeAllOtherWindows(driver, parentWindowHandle);// driver.switchTo().window(parentWindowHandle);
		             
			         driver.findElement(upload).click();
			         waitUntilElementInvisibility(driver, 10, Common.ajaxThrobber, "Throbber", new Exception().getStackTrace()[0]);
		         } catch(Exception e) {}
		         i++;
		         size = driver.findElements(element).size();         
	      }
		  if (size == 1) { fileWriterPrinter("Successful \"" + image + "\" " + name + " upload!"); }
	  }
	  
	  /**
	   * Typing Robot
	   * @param robot
	   * @param text
	   * @throws IOException
	   * @throws IllegalAccessException 
	   * @throws IllegalArgumentException 
	   * @throws InterruptedException 
	   * @throws SecurityException  
	   * @throws NoSuchFieldException 
	   */
	  public void keyTypeRobot(Robot robot, String text) throws IOException, IllegalArgumentException, IllegalAccessException, InterruptedException, NoSuchFieldException, SecurityException {
			boolean shiftOn = false;
			String key = null;
			char[] character = new char[text.length()];
			
			int keyCode, max = 100, min = 80;
			Thread.sleep(randomInt(min, max));
			
			for (int index = 0; index < text.length(); index++) { character[index] = text.charAt(index); }
			
			for (int index = 0; index < character.length; index++) {				
				switch (character[index])
				{
				case ' ': key = "VK_SPACE"; shiftOn = false; break;
				case 'a': key = "VK_A"; shiftOn = false;	break;
				case 'b': key = "VK_B";	shiftOn = false;	break;
				case 'c': key = "VK_C"; shiftOn = false;	break;
				case 'd': key = "VK_D"; shiftOn = false;	break;
				case 'e': key = "VK_E"; shiftOn = false;	break;
				case 'f': key = "VK_F"; shiftOn = false;	break;
				case 'g': key = "VK_G"; shiftOn = false;	break;
				case 'h': key = "VK_H"; shiftOn = false;	break;
				case 'i': key = "VK_I"; shiftOn = false;	break;
				case 'j': key = "VK_J"; shiftOn = false;	break;
				case 'k': key = "VK_K"; shiftOn = false;	break;
				case 'l': key = "VK_L"; shiftOn = false;	break;
				case 'm': key = "VK_M"; shiftOn = false;	break;
				case 'n': key = "VK_N"; shiftOn = false;	break;
				case 'o': key = "VK_O"; shiftOn = false;	break;
				case 'p': key = "VK_P"; shiftOn = false;	break;
				case 'q': key = "VK_Q"; shiftOn = false;	break;
				case 'r': key = "VK_R"; shiftOn = false;	break;
				case 's': key = "VK_S"; shiftOn = false;	break;
				case 't': key = "VK_T"; shiftOn = false;	break;
				case 'u': key = "VK_U"; shiftOn = false;	break;
				case 'v': key = "VK_V"; shiftOn = false;	break;
				case 'w': key = "VK_W"; shiftOn = false;	break;
				case 'x': key = "VK_X"; shiftOn = false;	break;
				case 'y': key = "VK_Y";	shiftOn = false;	break;
				case 'z': key = "VK_Z"; shiftOn = false;	break;
				case 'A': key = "VK_A"; shiftOn = true;		break;
				case 'B': key = "VK_B";	shiftOn = true;		break;
				case 'C': key = "VK_C"; shiftOn = true;		break;
				case 'D': key = "VK_D"; shiftOn = true;		break;
				case 'E': key = "VK_E"; shiftOn = true;		break;
				case 'F': key = "VK_F"; shiftOn = true;		break;
				case 'G': key = "VK_G"; shiftOn = true;		break;
				case 'H': key = "VK_H"; shiftOn = true;		break;
				case 'I': key = "VK_I"; shiftOn = true;		break;
				case 'J': key = "VK_J"; shiftOn = true;		break;
				case 'K': key = "VK_K"; shiftOn = true;		break;
				case 'L': key = "VK_L"; shiftOn = true;		break;
				case 'M': key = "VK_M"; shiftOn = true;		break;
				case 'N': key = "VK_N"; shiftOn = true;		break;
				case 'O': key = "VK_O"; shiftOn = true;		break;
				case 'P': key = "VK_P"; shiftOn = true;		break;
				case 'Q': key = "VK_Q"; shiftOn = true;		break;
				case 'R': key = "VK_R"; shiftOn = true;		break;
				case 'S': key = "VK_S"; shiftOn = true;		break;
				case 'T': key = "VK_T"; shiftOn = true;		break;
				case 'U': key = "VK_U"; shiftOn = true;		break;
				case 'V': key = "VK_V"; shiftOn = true;		break;
				case 'W': key = "VK_W"; shiftOn = true;		break;
				case 'X': key = "VK_X"; shiftOn = true;		break;
				case 'Y': key = "VK_Y";	shiftOn = true;		break;
				case 'Z': key = "VK_Z"; shiftOn = true;		break;
				case '0': key = "VK_0"; shiftOn = false;	break;
				case '1': key = "VK_1";	shiftOn = false;	break;
				case '2': key = "VK_2"; shiftOn = false;	break;
				case '3': key = "VK_3"; shiftOn = false;	break;
				case '4': key = "VK_4"; shiftOn = false;	break;
				case '5': key = "VK_5"; shiftOn = false;	break;
				case '6': key = "VK_6"; shiftOn = false;	break;
				case '7': key = "VK_7"; shiftOn = false;	break;
				case '8': key = "VK_8"; shiftOn = false;	break;
				case '9': key = "VK_9"; shiftOn = false;	break;
				case ')': key = "VK_0"; shiftOn = true;		break;
				case '!': key = "VK_1";	shiftOn = true;		break;
				case '@': key = "VK_2"; shiftOn = true;		break;
				case '#': key = "VK_3"; shiftOn = true;		break;
				case '$': key = "VK_4"; shiftOn = true;		break;
				case '%': key = "VK_5"; shiftOn = true;		break;
				case '^': key = "VK_6"; shiftOn = true;		break;
				case '&': key = "VK_7"; shiftOn = true;		break;
				case '*': key = "VK_8"; shiftOn = true;		break;
				case '(': key = "VK_9"; shiftOn = true;		break;
				case '-': key = "VK_OEM_MINUS"; shiftOn = false;break;
				case '_': key = "VK_OEM_MINUS"; shiftOn = true;	break;
				case '[': key = "VK_OEM_4"; shiftOn = false;	break;
				case '{': key = "VK_OEM_4"; shiftOn = true;		break;
				case ']': key = "VK_OEM_6"; shiftOn = false;	break;
				case '}': key = "VK_OEM_6"; shiftOn = true;		break;
				case '|': key = "VK_OEM_102"; shiftOn = true;	break;
				case ';': key = "VK_SEMICOLON"; shiftOn = false;break;
				case ':': key = "VK_SEMICOLON"; shiftOn = true;	break;
				case '"': key = "VK_OEM_7"; shiftOn = true;		break;
				case ',': key = "VK_COMMA"; shiftOn = false;break;
				case '<': key = "VK_COMMA"; shiftOn = true;	break;
				case '.': key = "VK_PERIOD"; shiftOn = false;   break;
				case '>': key = "VK_PERIOD"; shiftOn = true;	break;
				case '/': key = "VK_SLASH"; shiftOn = false;   break;
				case '\\': key = "VK_BACK_SLASH"; shiftOn = false;	break;
				case '?': key = "VK_SLASH"; shiftOn = true;	break;
				
				default: fileWriterPrinter("Keyboard: Invalid Value"); 	break;
				}
				
	            KeyEvent ke = new KeyEvent(new JTextField(), 0, 0, 0, 0, ' ');  
	            @SuppressWarnings("rawtypes") Class clazz = ke.getClass();  
	            Field field = clazz.getField( key );  
	            
	            keyCode = field.getInt(ke);
				if (shiftOn == true) { robot.keyPress(KeyEvent.VK_SHIFT); }
				
				robot.keyPress(keyCode);
				if (shiftOn == true) { robot.keyRelease(KeyEvent.VK_SHIFT); }
				
				Thread.sleep(randomInt(min, max));
				}
		}
	  
      /**
	   * chooses an image/portrait to upload by Robot
	   * @param driver
	   * @param robot
	   * @param image
	   * @throws IOException 
       * @throws AWTException 
       * @throws InterruptedException 
       * @throws IllegalAccessException 
       * @throws IllegalArgumentException 
       * @throws NoSuchFieldException 
       * @throws SecurityException 
	   */
	  public void chooseImageRobot(WebDriver driver, String image, Robot robot) throws IOException, SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InterruptedException, AWTException {
			String parentWindowHandle = driver.getWindowHandle();
		    
			for(String winHandle : driver.getWindowHandles()) {
		           driver.switchTo().window(winHandle);
		           }

            String previousURL = driver.getCurrentUrl();
		    alertHandler(driver);

		    String imageDir = Common.localImageDir;
		  	String imagePath = imageDir + File.separator + image;
		  	
		  	keyTypeRobot(robot, " ");             // pressing Space in the beginning
		  	keyTypeRobot(robot, imagePath);
		  	
		  	robot.keyPress(KeyEvent.VK_ENTER);    // confirm by pressing Enter in the end
		  	robot.keyRelease(KeyEvent.VK_ENTER);
		  	
		  	robot.keyPress(KeyEvent.VK_ENTER);    // confirm by pressing Enter in the end
		  	robot.keyRelease(KeyEvent.VK_ENTER);

		    closeAllOtherWindows(driver, parentWindowHandle); // driver.switchTo().window(parentWindowHandle);
		    waitUntilUrl(driver, previousURL); 
		    alertHandler(driver, parentWindowHandle);
	  }
	   
	  /**
	   * Switch window
	   * @param driver 
       * @throws InterruptedException
	   */
	  public void switchWindow(WebDriver driver) throws InterruptedException{
	     for(String winHandle : driver.getWindowHandles()) { driver.switchTo().window(winHandle); }
	     Thread.sleep(3000);
	  }
	  
	  /**
	   * select content category and sub category in blog/article
	   * @param driver
	   * @param mainCat
	   * @param subCat
 	  */
	  public void selectContentCategory(WebDriver driver, String mainCat, String subCat){
		  WebElement mainCategory = driver.findElement(By.id("edit-field-content-category-und-hierarchical-select-selects-0"));
		  Select clickDropDown = new Select(mainCategory);
		  clickDropDown.selectByVisibleText(mainCat);
		  
		  if (! subCat.equals("")) {
			   WebDriverWait wait = new WebDriverWait(driver, 15);
			   wait.until(ExpectedConditions.presenceOfElementLocated(By.id("edit-field-content-category-und-hierarchical-select-selects-1")));
			   WebElement subCategory = driver.findElement(By.id("edit-field-content-category-und-hierarchical-select-selects-1"));
			   clickDropDown = new Select(subCategory);
			   clickDropDown.selectByVisibleText(subCat);
		  }

	  }
	    
	/**
	   * Convert Content Title based on established Alphabetical Title-Pattern
	   */
	  public String alphabeticalTitle(String title){
          if ( title.toLowerCase().startsWith("the ") ) {
        	 String titlePrefix = title.substring(0, 3);
        	 String titleSuffix = title.substring(4, title.length());
        	 title = titleSuffix + ", " + titlePrefix;
        	 }
		  return title;
	  }
	  
	  /**
	   * Extract Content Title first Alphabetical Letter based on Established Alphabetical Title-Pattern
	   */
	  public String firstAlphabeticalTitleLetter(String title){
		  int first = 0;
          if ( title.toLowerCase().startsWith("the ") ) { first = 4; }
		  return title.substring(first, (first + 1));
	  }
	  
	  /**
	   * Extract Content Title first Alphabetical Letter based on Pseudo-Established Alphabetical Title-Pattern
	   */
	  public String firstAlphabeticalTitlePseudoLetter(String title){
		  int first = 0;
          if ( title.toLowerCase().startsWith("the") && !title.toLowerCase().startsWith("the ") ) { first = 3; }
		  return title.substring(first, (first + 1));
	  }
 
	// ################# DESCRIPTIVE BEGIN ##############################  
		  /**Gets the first (main) line of any thrown Exception Message
		   * Regardless it is single or multi-line Prompt
		   * @param e
		   */
		   public static void getExceptionDescriptive(Exception e, StackTraceElement l, WebDriver driver) throws IOException {
			 String message1 = null;					
			 try{
				 message1 = e.getCause().toString();
			 } 
			 catch(NullPointerException e1) {
			 message1 = ".getCause() by NullPointerException:";
			 }
			 finally {					
			 String message2 = e.getMessage();
			 String [] multiline1 = message1.replaceAll("\\r", "").split("\\n");
			 String [] multiline2 = message2.replaceAll("\\r", "").split("\\n");
			 String firstLine = multiline1[0];
			 String secondLine = multiline2[0];
			 String errorCause = firstLine.substring(0,firstLine.indexOf(":"));
			 String exceptionThrown = errorCause.substring(1 + errorCause.lastIndexOf("."), errorCause.length());
			 String packageNameOnly = l.getClassName().substring(0, l.getClassName().lastIndexOf("."));
			 String classNameOnly = l.getClassName().substring(1 + l.getClassName().lastIndexOf("."), l.getClassName().length());
			 String location = packageNameOnly + File.separator + classNameOnly + File.separator + l.getMethodName() + ", line # " + l.getLineNumber();
		     String xml = "<class name=\"" + packageNameOnly + "." + classNameOnly + "\"><methods><include name=\"" + l.getMethodName() + "\"/></methods></class>";
			 String description = exceptionThrown;
		     String detected = getCurrentDateTimeFull();
		     String runtime  = testRunTime("start.time", System.currentTimeMillis());
		     String subtotal = testRunTime("ini.time",   System.currentTimeMillis());
			 fileWriterPrinter("\nError Cause: ---> " + errorCause + "\nDescription: ---> " + secondLine + "\n   Location: ---> " + location);
			 getScreenShot(l, description, driver);
		  // Creating New or Updating existing Failed Counter record:  
			 counter("failed.num");
		  // Append a New Log record:
		     if (fileExist("run.log", false)) {
			     fileWriter("run.log", "Error Cause: ---> " + errorCause);
			     fileWriter("run.log", "Description: ---> " + secondLine);
			     fileWriter("run.log", "   Location: ---> " + location);
	   		  // fileWriter("run.log", "   Detected: ---> " + detected);
	   		  // fileWriter("run.log", "    Runtime: ---> " + runtime);
	   		  // fileWriter("run.log", "   Subtotal: ---> " + subtotal);	    	        
		     }
		  // Append an Error record:
			   fileWriter("failed.log", "    Failure: #" + fileScanner("failed.num"));
			   fileWriter("failed.log", "       Test: #" + fileScanner("test.num"));
			   fileWriter("failed.log", "      Start: "  + convertCalendarMillisecondsAsStringToDateTimeHourMinSec(fileScanner("start.time")));
               fileWriter("failed.log", "   XML Path: "  + xml);
			   fileWriter("failed.log", "Error Cause: ---> " + errorCause);
			   fileWriter("failed.log", "Description: ---> " + secondLine);
			   fileWriter("failed.log", "   Location: ---> " + location);
			   fileWriter("failed.log", "   Detected: " + detected);
		   	   fileWriter("failed.log", "    Runtime: " + runtime);
		   	   fileWriter("failed.log", "   Subtotal: " + subtotal);
		   	   fileWriter("failed.log", "");
		  // Append Descriptive record:
			 Assert.assertFalse(true, "\n  Error Cause: ---> " + errorCause
					                + "\n  Description: ---> " + secondLine
					                + "\n     Location: ---> " + location
									+ "\n     Detected: ---> " + detected
									+ "\n      Runtime: ---> " + runtime
									+ "\n     Subtotal: ---> " + subtotal
					                + "\n"
							        + xml
							        + "\n"
				                	+ "\nStack Traces:");
			 }		
		   }
	// ################# DESCRIPTIVE END ##############################

	// ################# SMART ASSERTIONS BEGIN #######################
		   public void assertWebElementsExist(WebDriver driver, StackTraceElement t, By by) throws IOException {
			   List <WebElement> list = driver.findElements(by);
			   Assert.assertTrue(list.size() > 0, getAssertTrue(t, driver, "Element not found!", list.size() > 0));
			   }
		   
		   public void assertWebElementsExist(WebDriver driver, StackTraceElement t, String xpath) throws IOException {
			   List <WebElement> list = driver.findElements(By.xpath(xpath));
			   Assert.assertTrue(list.size() > 0, getAssertTrue(t, driver, "Element not found!", list.size() > 0));
			   }
		   
		   public void assertWebElementsExist(WebDriver driver, String id, StackTraceElement t) throws IOException {
			   List <WebElement> list = driver.findElements(By.id(id));
			   Assert.assertTrue(list.size() > 0, getAssertTrue(t, driver, "Element not found!", list.size() > 0));
			   }
		   
		   public void assertWebElementExist(WebDriver driver, StackTraceElement t, By by) throws IOException {
			   List <WebElement> list = driver.findElements(by);
			   Assert.assertTrue(list.size() == 1, getAssertTrue(t, driver, "Element not found!", list.size() == 1));
			   }
		   
		   public void assertWebElementExist(WebDriver driver, StackTraceElement t, String xpath) throws IOException {
			   List <WebElement> list = driver.findElements(By.xpath(xpath));
			   Assert.assertTrue(list.size() == 1, getAssertTrue(t, driver, "Element not found!", list.size() == 1));
			   }
		   
		   public void assertWebElementExist(WebDriver driver, String id, StackTraceElement t) throws IOException {
			   List <WebElement> list = driver.findElements(By.id(id));
			   Assert.assertTrue(list.size() == 1, getAssertTrue(t, driver, "Element not found!", list.size() == 1));
			   }
		   
		   public void assertWebElementNotExist(WebDriver driver, StackTraceElement t, By by) throws IOException {
			   List <WebElement> list = driver.findElements(by);
			   Assert.assertFalse(list.size() > 0, getAssertFalse(t, driver, "Un-Expected Element found!", list.size() > 0));
			   }
		   
		   public void assertWebElementNotExist(WebDriver driver, StackTraceElement t, String xpath) throws IOException {
			   List <WebElement> list = driver.findElements(By.xpath(xpath));
			   Assert.assertFalse(list.size() > 0, getAssertFalse(t, driver, "Un-Expected Element found!", list.size() > 0));
			   }
		   
		   public void assertWebElementNotExist(WebDriver driver, String id, StackTraceElement t) throws IOException {
			   List <WebElement> list = driver.findElements(By.id(id));
			   Assert.assertFalse(list.size() > 0, getAssertFalse(t, driver, "Un-Expected Element found!", list.size() > 0));
			   }
		   
		   public void assertBooleanTrue(WebDriver driver, StackTraceElement t, Boolean b) throws IOException {
			   Assert.assertTrue(b, getAssertTrue(t, driver, "Not as expected!", b));
			   }
		   
		   public void assertBooleanFalse(WebDriver driver, StackTraceElement t, Boolean b) throws IOException {
			   Assert.assertFalse(b, getAssertFalse(t, driver, "Not as expected!", b));
			   }
		   
		   public void assertEquals(WebDriver driver, StackTraceElement t, int actual, int expected) throws IOException {
			   Assert.assertEquals(actual, expected, getAssertEquals(t, driver, "Not the same!", actual, expected));
			   }
		   
		   public void assertEquals(WebDriver driver, StackTraceElement t, String actual, String expected) throws IOException {
			   Assert.assertEquals(actual, expected, getAssertEquals(t, driver, "Not the same!", actual, expected));
			   }
		   
		   public static String getAssertTrue(StackTraceElement l, WebDriver driver, String description, Boolean b) throws IOException {
		       String packageNameOnly = l.getClassName().substring(0, l.getClassName().lastIndexOf("."));
		       String classNameOnly = l.getClassName().substring(1 + l.getClassName().lastIndexOf("."), l.getClassName().length());
		       String location = packageNameOnly + File.separator + classNameOnly + File.separator + l.getMethodName() + ", line # " + l.getLineNumber();
		       String xml = "<class name=\"" + packageNameOnly + "." + classNameOnly + "\"><methods><include name=\"" + l.getMethodName() + "\"/></methods></class>";
		       String detected = getCurrentDateTimeFull();
			   String runtime  = testRunTime("start.time", System.currentTimeMillis());
			   String subtotal = testRunTime("ini.time",   System.currentTimeMillis());
		   if (b == false) {
		      fileWriterPrinter("\nError Cause: ---> " + description + "\n   Location: ---> " + location + "\n   Expected: ---> " + "true" + "\n     Actual: ---> " + b + "\n");
		  	  getScreenShot(l, description, driver);
			  // Creating New or Updating existing Failed Counter record:  
				 counter("failed.num");
			  // Append a New Log record:
			     if (fileExist("run.log", false)) {
				     fileWriter("run.log", "Error Cause: ---> " + description);
				     fileWriter("run.log", "   Location: ---> " + location);
				     fileWriter("run.log", "   Expected: ---> " + "true");
				     fileWriter("run.log", "     Actual: ---> " + b);
	   		      // fileWriter("run.log", "   Detected: ---> " + detected);
		   		  // fileWriter("run.log", "    Runtime: ---> " + runtime);
		   		  // fileWriter("run.log", "   Subtotal: ---> " + subtotal);
				 }
			  // Append an Error record:
			       fileWriter("failed.log", "    Failure: #" + fileScanner("failed.num"));
			       fileWriter("failed.log", "       Test: #" + fileScanner("test.num"));
			       fileWriter("failed.log", "      Start: "  + convertCalendarMillisecondsAsStringToDateTimeHourMinSec(fileScanner("start.time")));
                   fileWriter("failed.log", "   XML Path: "  + xml);
				   fileWriter("failed.log", "Error Cause: ---> " + description);
				   fileWriter("failed.log", "   Location: ---> " + location);
				   fileWriter("failed.log", "   Expected: ---> " + "true");
				   fileWriter("failed.log", "     Actual: ---> " + b);
				   fileWriter("failed.log", "   Detected: " + detected);
		   		   fileWriter("failed.log", "    Runtime: " + runtime);
		   		   fileWriter("failed.log", "   Subtotal: " + subtotal);
		   		   fileWriter("failed.log", "");
		      } else {
			  fileWriterPrinter("\nExpected: " + true + "\n  Actual: " + b + "\n  Result: OK\n");
			  }
		   // Descriptive record output:
		      return "\nError Cause: ---> " + description
				   + "\n   Location: ---> " + location
				   + "\n   Expected: ---> " + "true"
				   + "\n     Actual: ---> " + b
				   + "\n   Detected: ---> " + detected
				   + "\n    Runtime: ---> " + runtime
				   + "\n   Subtotal: ---> " + subtotal
				   + "\n"
			       + xml
			       + "\n"
				+ "\nStack Traces:";
		      }

		   public static String getAssertEquals(StackTraceElement l, WebDriver driver, String description, Object actual, Object expected) throws IOException {
			   String packageNameOnly = l.getClassName().substring(0, l.getClassName().lastIndexOf("."));
			   String classNameOnly = l.getClassName().substring(1 + l.getClassName().lastIndexOf("."), l.getClassName().length());
			   String location = packageNameOnly + File.separator + classNameOnly + File.separator + l.getMethodName() + ", line # " + l.getLineNumber();
			   String xml = "<class name=\"" + packageNameOnly + "." + classNameOnly + "\"><methods><include name=\"" + l.getMethodName() + "\"/></methods></class>";
		       String detected = getCurrentDateTimeFull();
			   String runtime  = testRunTime("start.time", System.currentTimeMillis());
			   String subtotal = testRunTime("ini.time",   System.currentTimeMillis());
		   if (actual.equals(expected) == false) {
		      fileWriterPrinter("\nError Cause: ---> " + description + "\n   Location: ---> " + location + "\n   Expected: ---> " + expected + "\n     Actual: ---> " + actual + "\n");
		  	  getScreenShot(l, description, driver);
			  // Creating New or Updating existing Failed Counter record:  
				 counter("failed.num");
			  // Append a New Log record:
			     if (fileExist("run.log", false)) {
				     fileWriter("run.log", "Error Cause: ---> " + description);
				     fileWriter("run.log", "   Location: ---> " + location);
				     fileWriter("run.log", "   Expected: ---> " + expected);
				     fileWriter("run.log", "     Actual: ---> " + actual);
	   		      // fileWriter("run.log", "   Detected: ---> " + detected);
	   		      // fileWriter("run.log", "    Runtime: ---> " + runtime);
	   		      // fileWriter("run.log", "   Subtotal: ---> " + subtotal);
				 }
			  // Append an Error record:
			       fileWriter("failed.log", "    Failure: #" + fileScanner("failed.num"));
			       fileWriter("failed.log", "       Test: #" + fileScanner("test.num"));
			       fileWriter("failed.log", "      Start: "  + convertCalendarMillisecondsAsStringToDateTimeHourMinSec(fileScanner("start.time")));
                   fileWriter("failed.log", "   XML Path: "  + xml);
				   fileWriter("failed.log", "Error Cause: ---> " + description);
				   fileWriter("failed.log", "   Location: ---> " + location);
				   fileWriter("failed.log", "   Expected: ---> " + expected);
				   fileWriter("failed.log", "     Actual: ---> " + actual);
				   fileWriter("failed.log", "   Detected: " + detected);
		   		   fileWriter("failed.log", "    Runtime: " + runtime);
		   		   fileWriter("failed.log", "   Subtotal: " + subtotal);
		   		   fileWriter("failed.log", "");	    	        
		      } else {
		      fileWriterPrinter("\nExpected: " + expected + "\n  Actual: " + actual + "\n  Result: OK\n");
		      }
		   // Descriptive record output:
		      return "\nError Cause: ---> " + description
		    	   + "\n   Location: ---> " + location
				   + "\n   Expected: ---> " + expected
				   + "\n     Actual: ---> " + actual
				   + "\n   Detected: ---> " + detected
				   + "\n    Runtime: ---> " + runtime
				   + "\n   Subtotal: ---> " + subtotal
				   + "\n"
				   + xml
				   + "\n"
		    	   + "\nStack Traces:";
		      }
		   
		   public static String getAssertFalse(StackTraceElement l, WebDriver driver, String description, Boolean b) throws IOException {
		       String packageNameOnly = l.getClassName().substring(0, l.getClassName().lastIndexOf("."));
		       String classNameOnly = l.getClassName().substring(1 + l.getClassName().lastIndexOf("."), l.getClassName().length());
		       String location = packageNameOnly + File.separator + classNameOnly + File.separator + l.getMethodName() + ", line # " + l.getLineNumber();
		       String xml = "<class name=\"" + packageNameOnly + "." + classNameOnly + "\"><methods><include name=\"" + l.getMethodName() + "\"/></methods></class>";
		       String detected = getCurrentDateTimeFull();
			   String runtime  = testRunTime("start.time", System.currentTimeMillis());
			   String subtotal = testRunTime("ini.time",   System.currentTimeMillis());
		   if (b == true) {			  
		      fileWriterPrinter("\nError Cause: ---> " + description + "\n   Location: ---> " + location + "\n   Expected: ---> " + "false" + "\n     Actual: ---> " + b + "\n");
		  	  getScreenShot(l, description, driver);
			  // Creating New or Updating existing Failed Counter record:  
				 counter("failed.num");
			  // Append a New Log record:
			     if (fileExist("run.log", false)) {
				     fileWriter("run.log", "Error Cause: ---> " + description);
				     fileWriter("run.log", "   Location: ---> " + location);
				     fileWriter("run.log", "   Expected: ---> " + "false");
				     fileWriter("run.log", "     Actual: ---> " + b);
   		          // fileWriter("run.log", "   Detected: ---> " + detected);
   		          // fileWriter("run.log", "    Runtime: ---> " + runtime);
   		          // fileWriter("run.log", "   Subtotal: ---> " + subtotal);
		         }
			  // Append an Error record:
			       fileWriter("failed.log", "    Failure: #" + fileScanner("failed.num"));
			       fileWriter("failed.log", "       Test: #" + fileScanner("test.num"));
			       fileWriter("failed.log", "      Start: "  + convertCalendarMillisecondsAsStringToDateTimeHourMinSec(fileScanner("start.time")));
                   fileWriter("failed.log", "   XML Path: "  + xml);
				   fileWriter("failed.log", "Error Cause: ---> " + description);
				   fileWriter("failed.log", "   Location: ---> " + location);
				   fileWriter("failed.log", "   Expected: ---> " + "false");
				   fileWriter("failed.log", "     Actual: ---> " + b);
				   fileWriter("failed.log", "   Detected: " + detected);
		   		   fileWriter("failed.log", "    Runtime: " + runtime);
		   		   fileWriter("failed.log", "   Subtotal: " + subtotal);
		   		   fileWriter("failed.log", "");	    	        
		      } else {
			  fileWriterPrinter("\nExpected: " + false + "\n  Actual: " + b + "\n  Result: OK\n");
			  }
		   // Descriptive record output:
		      return "\nError Cause: ---> " + description
			       + "\n   Location: ---> " + location
				   + "\n   Expected: ---> " + "false"
				   + "\n     Actual: ---> " + b
				   + "\n   Detected: ---> " + detected
				   + "\n    Runtime: ---> " + runtime
				   + "\n   Subtotal: ---> " + subtotal
				   + "\n"
				   + xml
				   + "\n"
		    	   + "\nStack Traces:";
		      }
	// ################# SMART ASSERTIONS END ###########################
		   
	// ################# SCREEN-SHOT BEGIN ##############################
		  /**
		   * Takes screenshot when step fails. Works only with Selenium2Driver.
		   * Screenshot is saved at:  [workspace]/[project]/
		   * Screenshot file name is: [class].[method],[description] (date, time).png
		   */
		   public static void getScreenShot(String description, WebDriver driver) throws IOException {
		   DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd, HH.mm.ss");
		   File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		   String outputFile = Common.outputFileDir + description + " (" + dateFormat.format(new Date()) + ").png";
		   fileWriterPrinter(outputFile);
		   FileUtils.copyFile(scrFile, new File(outputFile));
		  }
		   
		  /** 
		   * Takes screenshot when step fails. Works only with Selenium2Driver.
		   * Screenshot is saved at:  [workspace]/[project]/
		   * Screenshot file name is: [class].[method],[description] (date, time).png
		   */
		   public static void getScreenShot(String description, WebDriver driver, long milliseconds) throws IOException {
		   DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd, HH.mm.ss");
		   File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		   String outputFile = Common.outputFileDir + description + " (" + dateFormat.format(milliseconds) + ").png";
		   fileWriterPrinter(outputFile);
		   FileUtils.copyFile(scrFile, new File(outputFile));
		  } 
		   
		  /**
		   * Takes screenshot when step fails. Works only with Selenium2Driver.
		   * Screenshot is saved at:  [workspace]/[project]/[package]/[class]/
		   * Screenshot file name is: [class].[method],[description],[line #](date, time).png
		   */
		   public static void getScreenShot(StackTraceElement l, String description, WebDriver driver) throws IOException {
		   DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd, HH.mm.ss");
		   File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		   String packageNameOnly = l.getClassName().substring(0, l.getClassName().lastIndexOf("."));
		   String classNameOnly = l.getClassName().substring(1 + l.getClassName().lastIndexOf("."), l.getClassName().length());
		   String screenshotName = classNameOnly + "." + l.getMethodName() + ", " + description +", line # " + l.getLineNumber();
		   String outputFile = Common.outputFileDir + packageNameOnly + File.separator + classNameOnly + File.separator + screenshotName + " (" + dateFormat.format(new Date()) + ").png";
		   fileWriterPrinter(outputFile);
		   FileUtils.copyFile(scrFile, new File(outputFile));
		   }
		   
		   /**
			* Takes screenshot when step fails. Works only with Selenium2Driver.
		    * Screenshot is saved at:  [workspace]/[project]/[package]/[class]/
			* Screenshot file name is: [class].[method],[description],[line #](date, time).png
			*/
			public void getScreenShot(StackTraceElement l, Exception e, WebDriver driver) throws IOException {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd, HH.mm.ss");
			File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			String message1 = null;					
			try{
				 message1 = e.getCause().toString();
			} 
			catch(NullPointerException e1) {
			message1 = ".getCause() by NullPointerException:";
			}
			finally {
			String [] multiline1 = message1.replaceAll("\\r", "").split("\\n");
			String firstLine = multiline1[0];
			String errorCause = firstLine.substring(0,firstLine.indexOf(":"));
			String exceptionThrown = errorCause.substring(1 + errorCause.lastIndexOf("."), errorCause.length());
			 
			String packageNameOnly = l.getClassName().substring(0, l.getClassName().lastIndexOf("."));
			String classNameOnly = l.getClassName().substring(1 + l.getClassName().lastIndexOf("."), l.getClassName().length());
			String description = exceptionThrown;
			String screenshotName = classNameOnly + "." + l.getMethodName() + ", " + description +", line # " + l.getLineNumber();

			String outputFile = Common.outputFileDir + packageNameOnly + File.separator + classNameOnly + File.separator + screenshotName + " (" + dateFormat.format(new Date()) + ").png";
			fileWriterPrinter(outputFile);
			FileUtils.copyFile(scrFile, new File(outputFile));
			}
			}
			
			   /**
				* Takes screenshot when step fails. Works only with Selenium2Driver.
			    * Screenshot is saved at:  [workspace]/[project]/[package]/[class]/
				* Screenshot file name is: [class].[method],[description],[line #](date, time).png
				*/
				public void getScreenShot(StackTraceElement l, Exception e, String description, WebDriver driver) throws IOException {
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd, HH.mm.ss");
				File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
				String message1 = null;					
				try{
					 message1 = e.getCause().toString();
				} 
				catch(NullPointerException e1) {
				message1 = ".getCause() by NullPointerException:";
				}
				finally {
				String [] multiline1 = message1.replaceAll("\\r", "").split("\\n");
				String firstLine = multiline1[0];
				String errorCause = firstLine.substring(0,firstLine.indexOf(":"));
				String exceptionThrown = errorCause.substring(1 + errorCause.lastIndexOf("."), errorCause.length());
				 
				String packageNameOnly = l.getClassName().substring(0, l.getClassName().lastIndexOf("."));
				String classNameOnly = l.getClassName().substring(1 + l.getClassName().lastIndexOf("."), l.getClassName().length());
				String exception = exceptionThrown;
				String screenshotName = classNameOnly + "." + l.getMethodName() + ", " + exception + ", " + description + ", line # " + l.getLineNumber();

				String outputFile = Common.outputFileDir + packageNameOnly + File.separator + classNameOnly + File.separator + screenshotName + " (" + dateFormat.format(new Date()) + ").png";
				fileWriterPrinter(outputFile);
				FileUtils.copyFile(scrFile, new File(outputFile));
				}
				}
	// ################# SCREEN-SHOT END ##############################

	// ################# STRING CONVERTER START #######################   
	/* @throws IOException
	 * @throws NumberFormatException */	   
			public int convertStringToInt(String value) throws NumberFormatException, IOException {
				try {
				      return Integer.parseInt(value);
				    } catch(NumberFormatException e) {
				    	fileWriterPrinter("NullPointerException:\nString '" + value + "is not convertable to Integer...");
				    	return 0;
				    }
				}
			
		public int convertStringToInt(String value, WebDriver driver) throws IOException {
			try {
			      return Integer.parseInt(value);
			    } catch(NumberFormatException exception) {
			    	getExceptionDescriptive(exception, new Exception().getStackTrace()[0], driver);
			    	return 0;
			    }
			}
		   
		/* @throws IOException
		 * @throws NumberFormatException */
		public static long convertStringToLong(String value) throws NumberFormatException, IOException {
			try {
				return Long.parseLong(value);
			} catch(NumberFormatException exception) {
				fileWriterPrinter("\"NullPointerException\" thrown:\nString '" + value
						+ "' is not convertable to Long...");
				return 0;
			}
		}
		
		/* @throws IOException
		public long convertStringToLong(String value, WebDriver driver) throws IOException {
			try {
				return Long.parseLong(value);
			} catch(NumberFormatException exception) {
			  // takeScreenshotJU.takeScreenshotHelper.getExceptionDescriptive(exception, new Exception().getStackTrace()[0], driver);
				 getExceptionDescriptive(exception, new Exception().getStackTrace()[0], driver);
				return 0;
			}
		}
		
		/* @throws IOException
		 * @throws NumberFormatException */
		public String convertLongToString(long value) throws NumberFormatException, IOException {
			try {
				return String.valueOf(value);
			} catch(NumberFormatException exception) {
				fileWriterPrinter("\"NullPointerException\" thrown:\nString '" + value
						+ "' is not convertable to Long...");
				return null;
			}
		}
		
		/* @throws IOException
		public String convertLongToString(long value, WebDriver driver) throws IOException {
			try {
				return String.valueOf(value);
			} catch(NumberFormatException exception) {
				  // takeScreenshotJU.takeScreenshotHelper.getExceptionDescriptive(exception, new Exception().getStackTrace()[0], driver);
					 getExceptionDescriptive(exception, new Exception().getStackTrace()[0], driver);
				return null;
			}
		}
		
		/** Convert  Long to Integer */
		public int convertLongToInteger(long value) {
		    int result = (int) (long) value;
		    if (result != value) {
		      throw new IllegalArgumentException("Out of range: " + value);
		    }
		    return result;
		}
		
		/** Roundout Double to 3-decimals return String */
		public String roundoutNumberToThree(double number) {
			DecimalFormat f = new DecimalFormat("#.###");
			return f.format(number); // this is String
		}
		
		/** Roundout Division to 3-decimals return String */
		public String roundoutRatioToThree(double numerator, double denominator ) {
			DecimalFormat f = new DecimalFormat("#.###");
			return f.format(numerator/denominator); // this is String
		}
		
		/** Convert String to Float */
		public Float convertStringToFloat(String s ) {
			return Float.valueOf(s);
		}
		
		/** Convert String to Double */
		public Double convertStringToDouble(String s ) {
			return Double.valueOf(s);
		}
	// ################# STRING CONVERTER END #######################	
		
	    /** Print XML path 
	     * @throws IOException
	     */
		public void printXmlPath(StackTraceElement l) throws IOException {
		       String packageNameOnly = l.getClassName().substring(0, l.getClassName().lastIndexOf("."));
		       String classNameOnly = l.getClassName().substring(1 + l.getClassName().lastIndexOf("."), l.getClassName().length());
		       String xml = "<class name=\"" + packageNameOnly + "." + classNameOnly + "\"><methods><include name=\"" + l.getMethodName() + "\"/></methods></class>";
			   fileWriterPrinter("   XML Path: " + xml);
			// Renew XML record:
			   fileCleaner("xml.path");
			   fileWriter( "xml.path", xml);
			// Renew Stack Trace Element record:
			   fileCleaner("stack.trace");
			   fileWriter( "stack.trace", l);
			// Append a New Log record:
			   if (fileExist("run.log", false)) { fileWriter("run.log", "   XML Path: " + xml); }      
		}
		
	/** Get XML path */
		public String gettXmlPath(StackTraceElement l) {
		       String packageNameOnly = l.getClassName().substring(0, l.getClassName().lastIndexOf("."));
		       String classNameOnly = l.getClassName().substring(1 + l.getClassName().lastIndexOf("."), l.getClassName().length());
			   return("<class name=\"" + packageNameOnly + "." + classNameOnly + "\"><methods><include name=\"" + l.getMethodName() + "\"/></methods></class>");
		}
		
	/** Percentage Calculator */
		 public int percentageCalc(int score, int total) {  
		        int percentage; percentage = (score * 100/ total);		         
		 return percentage;
		 }

    /** Dot Spacer */
		 public String dotSpacer(int argument) {  
		        String dotSpacer = new String();		         
		        if (argument < 10) {
		        	dotSpacer = ".....";      
		        } else if (argument < 100) {
		          dotSpacer = "....";
		        } else if (argument < 1000) {
		          dotSpacer = "...";
		        } else if (argument >= 1000) {
			          dotSpacer = "..";
			    }	         
		  return dotSpacer;
		  } 
		 	
// ################# CALENDAR START ###########################
			/** Returns Current Time (hour, min)*/
			public String getCurrentTimeHourMin() {
				// DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				// need to change after the date format is decided
				DateFormat dateFormat = new SimpleDateFormat("HH:mm");
				Date date = new Date();
				return dateFormat.format(date);
			}
			
			/** Returns Current Time (hour, min, sec)*/
			public String getCurrentTimeHourMinSec() {
				// DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				// need to change after the date format is decided
				DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
				Date date = new Date();
				return dateFormat.format(date);
			}
			
			/** Returns Current Date */
			public String getCurrentDateYearMonthDay() {
				// DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				// need to change after the date format is decided
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				Date date = new Date();
				return dateFormat.format(date);
			}
		 
			/** Returns Current Date and Time (hour, min)*/
			public String getCurrentDateTimeHourMin() {
				// DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				// need to change after the date format is decided
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				Date date = new Date();
				return dateFormat.format(date);
			}
			
			/** Returns Current Date and Time (hour, min, sec)*/
			public String getCurrentDateTimeHourMinSec() {
				// DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				// need to change after the date format is decided
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date date = new Date();
				return dateFormat.format(date);
			}
			
			/** Returns Current Date and Time */
			public static String getCurrentDateTimeFull() {
				// DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				// need to change after the date format is decided
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date date = new Date();
				return dateFormat.format(date);
			}

			/**
			 * Creates a new Test Log record as a text file named "run.log" create file
			 * example: File f = new File(<full path string>); f.createNewFile();
			 * 
			 * @throws IOException
			 */
			// @BeforeSuite
			public void logOpen() throws IOException {
				// Initialization:
				fileCleaner("failed.log");
				fileCleaner("failed.num");
				fileCleaner("finish.time");
				fileCleaner("ini.time");
				fileCleaner("run.log");
				fileCleaner("print.log");
				fileCleaner("start.time");
				fileCleaner("stack.trace");
				fileCleaner("test.num");
				fileCleaner("wait.log");
				fileCleaner("xml.path");
				fileCleaner("source.html");
				String time = getCurrentDateTimeFull(); // System.out.print(" TEST START: " + time + "\n");
				fileWriter("ini.time", convertLongToString(System.currentTimeMillis()));
				// Initial Log record:
				fileWriter("run.log", " TEST START: " + time);
				fileWriter("run.log", "");
			}

			/**
			 * Closes the Test Log record text file named "run.log"
			 * 
			 * @throws IOException
			 * @throws Exception
			 */
			// @AfterSuite
			public void logClose() throws IOException {
				long finish = System.currentTimeMillis();
				String time = getCurrentDateTimeFull();
				// Scanning Failure Counter record:
				int failed = 0;
				if (fileExist("failed.num", false)) { failed = Integer.valueOf(fileScanner("failed.num")); }
				// Scanning Test Counter record:
				int n = 1;
				if (fileExist("test.num", false)) {
					if (!fileScanner("test.num").equals(null)) { n = Integer.valueOf(fileScanner("test.num")); }
				}
				if (n > 1) {
					// Scanning Initialization record:
					String startingTime = fileScanner("ini.time");
					long start = convertStringToLong(startingTime);
					fileWriterPrinter("TOTAL TESTS: " + Integer.valueOf(fileScanner("test.num")));
					fileWriterPrinter("     FAILED: " + failed);
					fileWriterPrinter("TEST  START: " + convertCalendarMillisecondsAsLongToDateTimeHourMinSec(start));
					fileWriterPrinter("TEST FINISH: " + time);
					fileWriterPrinter("TOTAL  TIME: " + convertTimeMillisecondsAsLongToDuration(finish - start));
					fileWriterPrinter();
					// Final Log record:
					if (fileExist("run.log")) { 
						fileWriter("run.log", "");
						fileWriter("run.log", "TOTAL TESTS: " + Integer.valueOf(fileScanner("test.num")));
						fileWriter("run.log", "     FAILED: " + failed);
						fileWriter("run.log", "TEST  START: " + convertCalendarMillisecondsAsLongToDateTimeHourMinSec(start));
						fileWriter("run.log", "TEST FINISH: " + time);
						fileWriter("run.log", "TOTAL  TIME: " + convertTimeMillisecondsAsLongToDuration(finish - start));
						fileWriter("run.log", "");
					}
				}
				// Clean-up unnecessary file(s)
				fileCleaner("failed.num");
				fileCleaner("finish.time");
				fileCleaner("ini.time");
				fileCleaner("start.time");
				fileCleaner("stack.trace");
				fileCleaner("test.num");
				fileCleaner("xml.path");
			}
			
			/**
			 * @throws IOException
			 */
			public void startTime() throws IOException {
			   String date = getCurrentDateTimeFull();

			// Cleaning:
			   fileCleaner("match.log");
			   fileCleaner("max.log");
			   fileCleaner("order.log");
			   fileCleaner("xml.log");
			   fileCleaner("error.log");
			   fileCleaner("reason.log");
			   fileCleaner("start.time");
			   
			   fileWriter("start.time", convertLongToString(System.currentTimeMillis()));
			// Creating New or Updating existing Test Counter record:  
			   int n = counter("test.num");
		    // Print-out information:
		       fileWriterPrinter("\n       Test: #" + n);
			   fileWriterPrinter(  "      Start: "  + date);
			// Append a Start Log record:
			   if (fileExist("run.log", false)) {
			       fileWriter("run.log", "");
			       fileWriter("run.log", "       Test: #" + n);
			       fileWriter("run.log", "      Start: "  + date);	    	        
		    	}		    
			}
			
			/** Prints Test End and Sub-Total Time */
			public void endTime() throws IOException {
			   long finish = System.currentTimeMillis();
			   
			// Cleaning:
			   fileCleaner("match.log");
			   fileCleaner("max.log");
			   fileCleaner("order.log");
			   fileCleaner("xml.log");
			   fileCleaner("error.log");
			   fileCleaner("reason.log");
			   fileCleaner("finish.time");
			   
			   fileWriter("finish.time", convertLongToString(finish));
			// Scanning Test Counter record:
			   int n = 1;
			   if (fileExist("test.num", false)) { 
				   if (! fileScanner("test.num").equals(null)) { n = Integer.valueOf(fileScanner("test.num")); }
			   }
			   fileWriterPrinter("\n     Finish: " + getCurrentDateTimeFull());
			   fileWriterPrinter(  "   Duration: " + testRunTime("start.time", finish)); 
			   if (n > 1) { fileWriterPrinter("   Subtotal: " + testRunTime("ini.time", finish) + "\n"); }
			   else { fileWriterPrinter(); }
			// Append an End Log record:
		       if (fileExist("run.log", false)) {
		    	   fileWriter("run.log", "\n     Finish: " + getCurrentDateTimeFull());
		    	   fileWriter("run.log",   "   Duration: " + testRunTime("start.time", finish));
        	       if (n > 1) { fileWriter("run.log", "   Subtotal: " + testRunTime("ini.time", finish)); }
		       }
    }
        
    /** Outputs Test Run Time as HH:MM:SS */
			public static String testRunTime(String startFileName) throws IOException {
			   long finish = System.currentTimeMillis();
			   String startingTime = fileScanner(startFileName);
			   long start = convertStringToLong(startingTime);
			   return convertTimeMillisecondsAsLongToDuration(finish - start);
			}
			
			/** Outputs Test Run Time as HH:MM:SS, finish input required */
			public static String testRunTime(String startFileName, long finishTimeMilliseconds) throws IOException {
			   long finish = finishTimeMilliseconds;
			   String startingTime = fileScanner(startFileName);
			   long start = convertStringToLong(startingTime);
			   return convertTimeMillisecondsAsLongToDuration(finish - start);
			}
			
			/* @throws IOException
			 * @throws NumberFormatException */
			public static String fileScanner(String fileName) throws NumberFormatException, IOException {				
				String n = null;
				if (fileExist(fileName, false)) {
				   File f = new File(Common.testOutputFileDir + fileName);
				   scanner = new Scanner(f);
				   n = scanner.useDelimiter("\\Z").next();
				   scanner.close();
			    }
				return n;				
			}
			
			/* @throws IOException
			 * @throws NumberFormatException */
			public static void fileCleaner(String fileName) throws NumberFormatException, IOException {
				if (fileExist(fileName, false))
				 { (new File(Common.testOutputFileDir + fileName)).delete(); }
			}
			
			/* @throws IOException
			 * @throws NumberFormatException */
			public static void fileCleaner(String path, String fileName) throws NumberFormatException, IOException {
				if (fileExist(path, fileName, false))
				 { (new File(path  + File.separator + fileName)).delete(); }
			}
			
			/** Counter: Will renew counting starting with "1" if the Counter File is currently missing; Returns new iteration value; 
			 * @throws IOException
			 */
            public static int counter(String counterFileName) throws NumberFormatException, IOException {
			   // if Counter File does not exist - create new it with counter "1";
               //                      otherwise - update existing by increasing the counter by "1";
		       int n = 1;
		       File f = new File(Common.testOutputFileDir + counterFileName);
		       if (f.exists() && f.isFile()) { n = Integer.valueOf(fileScanner(counterFileName)) + 1; }
		       FileUtils.writeStringToFile(f, String.valueOf(n));
		       return n;
            }
            
			/**
			 * @throws IOException
			 * @throws NumberFormatException
			 */ 
			public static Boolean fileExist(String fileName) throws NumberFormatException, IOException {
				File f = new File(Common.testOutputFileDir + fileName);
				if (! (f.exists() && f.isFile()) ) { fileWriterPrinter(f + " is missing..."); }
				return (f.exists() && f.isFile());
			}
			
			/**
			 * @throws IOException
			 * @throws NumberFormatException
			 */ 
			public static Boolean fileExist(String path, String fileName) throws NumberFormatException, IOException {
				File f = new File(path + File.separator + fileName);
				if (! (f.exists() && f.isFile()) ) { fileWriterPrinter(f + " is missing..."); }
				return (f.exists() && f.isFile());
			}
			
			/**
			 * @throws IOException
			 * @throws NumberFormatException
			 */ 
			public static Boolean fileExist(String fileName, Boolean silentMode) throws NumberFormatException, IOException {
				File f = new File(Common.testOutputFileDir + fileName);
				if (! (f.exists() && f.isFile()) ) { if (silentMode) { fileWriterPrinter(f + " is missing..."); } }
				return (f.exists() && f.isFile());
			}
			
			/**
			 * @throws IOException
			 * @throws NumberFormatException
			 */ 
			public static Boolean fileExist(String path, String fileName, Boolean silentMode) throws NumberFormatException, IOException {
				File f = new File(path + File.separator + fileName);
				if (! (f.exists() && f.isFile()) ) { if (silentMode) { fileWriterPrinter(f + " is missing..."); } }
				return (f.exists() && f.isFile());
			}
			
			/** @throws IOException */ 
			public static void fileCopy(String fileSource, String fileDest) throws IOException {
				File s = new File(Common.testOutputFileDir + fileSource);
				File d = new File(Common.testOutputFileDir + fileDest);
				if (s.exists() && s.isFile()) { FileUtils.copyFile(s, d); }
			}
			
			/** Edit the target file by searching content throug all lines, replacing them, and overwriting that target file */
			public static void fileEditor(String path, String search, String replace) {	    	
			try {
				 File log= new File(path);	    	
			     FileReader fr = new FileReader(log);
			     String s;
			     String totalStr = "";
			     try (BufferedReader br = new BufferedReader(fr)) {
			          while ((s = br.readLine()) != null) { totalStr += s + "\n";  }	            
			          totalStr = totalStr.replaceAll(search, replace);
			          FileWriter fw = new FileWriter(log);
			          fw.write(totalStr);
			          fw.close();
			          }
			     } catch(Exception e) { System.out.println("Problem reading file."); }
			}
		
			/** Searching content throug all lines of source file, replacing them, and saving as new target file */
			public static void fileEditor(String sourcePath, String targetPath, String search, String replace) {	    	
			try {
				 File log= new File(sourcePath);
				 File tar= new File(targetPath);	    	
			     FileReader fr = new FileReader(log);
			     String s;
			     String totalStr = "";
			     try (BufferedReader br = new BufferedReader(fr)) {
			          while ((s = br.readLine()) != null) { totalStr += s + "\n";  }	            
			          totalStr = totalStr.replaceAll(search, replace);
			          FileWriter fw = new FileWriter(tar);
			          fw.write(totalStr);
			          fw.close();
			          }
			     } catch(Exception e) { System.out.println("Problem reading file."); }
			}			
			   
			/** Writes a String line into File */
            public static void fileWriter(String fileName, Object printLine) throws NumberFormatException, IOException {
             // Create File:
				File f = new File(Common.testOutputFileDir + fileName);				                                                                      
			 // Write or add a String line into File:	
			    FileWriter fw = new FileWriter(f,true);
			    PrintWriter pw = new PrintWriter(fw);
			    pw.println(printLine);
			    pw.close();
			}
            
			/** Writes a String line into File */
            public static void fileWriter(String path, String fileName, Object printLine) throws NumberFormatException, IOException {
             // Create File:
				File f = new File(path + File.separator + fileName);				                                                                      
			 // Write or add a String line into File:	
			    FileWriter fw = new FileWriter(f,true);
			    PrintWriter pw = new PrintWriter(fw);
			    pw.println(printLine);
			    pw.close();
			}
            
			/** Writes an empty line into "print.log" File, as well as through System Out Print Line */
            public static void fileWriterPrinter() throws NumberFormatException, IOException {
             // Create File:
				File f = new File(Common.testOutputFileDir + "print.log");				                                                                      
			 // Write or add a String line into File:	
			    FileWriter fw = new FileWriter(f,true);
			    PrintWriter pw = new PrintWriter(fw);
			    pw.println();
			    pw.close();
			 // System Out Print Line:
			    // if (printLine instanceof String) {}
			    // if (printLine instanceof Integer) {}
			    // if (printLine instanceof Long) {}
			    // if (printLine instanceof Boolean) {}
			    // if (printLine instanceof Double) {}
			    System.out.print("\n");		    
			}
            
			/** Writes an Object line into "print.log" File, as well as through System Out Print Line */
            public static void fileWriterPrinter(String printLine) throws NumberFormatException, IOException {
             // Create File:
				File f = new File(Common.testOutputFileDir + "print.log");				                                                                      
			 // Write or add a String line into File:	
			    FileWriter fw = new FileWriter(f,true);
			    PrintWriter pw = new PrintWriter(fw);
			    pw.println(printLine);
			    pw.close();
			 // System Out Print Line:
			    // if (printLine instanceof String) {}
			    // if (printLine instanceof Integer) {}
			    // if (printLine instanceof Long) {}
			    // if (printLine instanceof Boolean) {}
			    // if (printLine instanceof Double) {}
			    System.out.print(printLine + "\n");		    
			}
            
			/** Writes an Object line into "print.log" File, as well as through System Out Print Line */
            public static void fileWriterPrinter(Object printLine) throws NumberFormatException, IOException {
             // Create File:
				File f = new File(Common.testOutputFileDir + "print.log");				                                                                      
			 // Write or add a String line into File:	
			    FileWriter fw = new FileWriter(f,true);
			    PrintWriter pw = new PrintWriter(fw);
			    pw.println(printLine);
			    pw.close();
			 // System Out Print Line:
			    // if (printLine instanceof String) {}
			    // if (printLine instanceof Integer) {}
			    // if (printLine instanceof Long) {}
			    // if (printLine instanceof Boolean) {}
			    // if (printLine instanceof Double) {}
			    System.out.print(printLine + "\n");		    
			}
            
			/** Writes an Object line into File, as well as through System Out Print Line */
            public static void fileWriterPrinter(String fileName, Object printLine) throws NumberFormatException, IOException {
             // Create File:
				File f = new File(Common.testOutputFileDir + fileName);				                                                                      
			 // Write or add a String line into File:	
			    FileWriter fw = new FileWriter(f,true);
			    PrintWriter pw = new PrintWriter(fw);
			    pw.println(printLine);
			    pw.close();
			 // System Out Print Line:
			    fileWriterPrinter(printLine);
			} 
            
			/** Writes an Object line into File, as well as through System Out Print Line */
            public static void fileWriterPrinter(String path, String fileName, Object printLine) throws NumberFormatException, IOException {
             // Create File:
				File f = new File(path + fileName);		                                                                      
			 // Write or add a String line into File:	
			    FileWriter fw = new FileWriter(f,true);
			    PrintWriter pw = new PrintWriter(fw);
			    pw.println(printLine);
			    pw.close();
			 // System Out Print Line:
			    fileWriterPrinter(printLine);
			}
            
        // ####################### TESTNG-FAILED XML CONVERTER START #######################
            /**
        	 * This METHOD converts Testng-Failed XML file into pure Test-Failed XML
        	 */
            public void testNgFailedToTestFailedConverter(String suiteName, String testName, String xmlOutputFileName, String reporterClass ) throws IOException {
           		// DECLARATION:
				String sourceFileName = "testng-failed.xml";
    		
        	    // PRE-CLEAN:
        	    if (fileExist(System.getProperty("user.dir"), xmlOutputFileName, false)) { fileCleaner(System.getProperty("user.dir"), xmlOutputFileName);}
        	
        	    // READER-WRITER:     		
    		    String[] string = addReporterClassToTestNgXmlLinesArray(sourceFileName, reporterClass); // READS TESTNG XML
    		    for (String s : string) { fileWriter(System.getProperty("user.dir"), xmlOutputFileName, s); }
        	
        	    // OPTIONAL FAILURES NUMBER OUTPUT:
    		    int failed = 0;
    		    for (int i = 0; i < string.length; i++) {
				    if ( string[i].contains("<include name=\"") ) { failed++; }
			    }
    		    
        		// UPDATE FAILED TEST NUMBER AS PER FAILED:
        		if (fileExist("failed.num", false)){
        			fileCleaner("failed.num");
        			fileWriter("failed.num", failed);
        		}
        		
    		    if (fileExist("last.num", false)) {
    		    	if (Integer.valueOf(fileScanner("last.num")) == 1 ) {
    			    if (failed > 0) { fileWriterPrinter("FAILED..."); } else { fileWriterPrinter("PASSED!"); }
    			    }   			
    			if (Integer.valueOf(fileScanner("last.num")) > 1 ) {
    			   if (failed > 0) {
    				   if (failed == Integer.valueOf(fileScanner("last.num")) ) { fileWriterPrinter("ALL FAILED..."); }
    				   else { fileWriterPrinter("FAILED " + failed  + " OF " + Integer.valueOf(fileScanner("last.num"))); }
    			   }
      			   if (failed == 0) { fileWriterPrinter("ALL PASSED!"); }
      			   }			
    			} 		
            }

        	/**
        	 * This METHOD adds Reporter Class to TestNG File
        	 */
        	public String[] addReporterClassToTestNgXmlLinesArray(String fileName, String reporterClass) throws IOException{
        		String[] string = cleanTestNgXmlLinesArray(fileName);
        	    
        	    ArrayList<String> lines = new ArrayList<String>();
        	    for (int i = 0; i < string.length; i++) {
        	    	lines.add(string[i]);
					if ( string[i].contains("<classes>") ) { lines.add("      " + reporterClass); }
				}
        	    
        	    String[] linesArray = lines.toArray(new String[lines.size()]);
        	    return linesArray;
        	}
            
        	/**
        	 * This METHOD reads TestNG Files, deletes empty XML CLASS PATH(s)
        	 */
        	public String[] cleanTestNgXmlLinesArray(String fileName) throws IOException{
        		String[] string = readTestNgXmlFileOutputLinesArray(fileName);
        		ArrayList<String> noEmptyMethods = new ArrayList<String>();
        		ArrayList<String> noEmptyClass = new ArrayList<String>();
        		
                // EMTY METHODS CLEANER:
        		for (int i = 0; i < string.length; i++) {
					if ( (i < (string.length - 1))
					   && string[i].contains("<methods>")
					   && string[i + 1].contains("</methods>")
					   ) 
					{ i++; } else { noEmptyMethods.add(string[i]); }
				}
        		
        		String[] noEmptyMethodsArray = noEmptyMethods.toArray(new String[noEmptyMethods.size()]);
        		
                // EMTY CLASS CLEANER:
        		for (int i = 0; i < noEmptyMethodsArray.length; i++) {
					if ( (i < (noEmptyMethodsArray.length - 1))
					   && noEmptyMethodsArray[i].contains("<class name=")
					   && noEmptyMethodsArray[i + 1].contains("</class>")
					   ) 
					{ i++; } else { noEmptyClass.add(noEmptyMethodsArray[i]); }
				}

        		String[] noEmptyClassArray = noEmptyClass.toArray(new String[noEmptyClass.size()]);
				return noEmptyClassArray;
        	}
        	
        	/**
        	 * This METHOD reads any Text File,
        	 * converts and outputs all the text lines as a String Array
        	 */
        	@SuppressWarnings("resource")
        	public String[] readTestNgXmlFileOutputLinesArray(String fileName) throws IOException{
        	    BufferedReader in = new BufferedReader(new FileReader(Common.testOutputFileDir + fileName));
        	    String str=null;
        	    ArrayList<String> lines = new ArrayList<String>();
        	    while ((str = in.readLine()) != null) {
        	        if (!str.contains("helper")       	        		
        	        && (!str.contains("startTime"))
        	        && (!str.contains("closeBrowsers"))
        	        && (!str.contains("endTime"))
        	        && (!str.contains("start"))
        	        && (!str.contains("finish"))        	        
        	        && (str.length() != 0)       	        
        	        	) { lines.add(str); }
        	        }
        	    String[] linesArray = lines.toArray(new String[lines.size()]);
        	    return linesArray;
        	}
        // ######################## TESTNG-FAILED XML CONVERTER END ########################
            
        // ####################### TEST-NG XML EXTRACTOR-CREATER START #######################
            /**
        	 * This METHOD created TestNG XML file based on Source File which contains (or not) XML CLASS PATH(s)
        	 */
        	public void testLogToXmlCreator(String suiteName, String testName, String sourceFileName, String xmlOutputFileName, String reporterClass) throws IOException {
        		// OPTIONAL FAILURES NUMBER OUTPUT:
        		if (fileExist("last.num", false)) {
        			if (fileExist(sourceFileName, false)) {
        				if (Integer.valueOf(fileScanner("last.num")) == 1 ) { fileWriterPrinter("FAILED..."); }
        				else { 
        					if (convertXmlArrayToTestNG(readLogOutputXmlLinesArray(sourceFileName)).length == Integer.valueOf(fileScanner("last.num")))
        					     { fileWriterPrinter("ALL " + convertXmlArrayToTestNG(readLogOutputXmlLinesArray(sourceFileName)).length + " FAILED!" + "\n"); }
        					else { fileWriterPrinter("FAILED: " + convertXmlArrayToTestNG(readLogOutputXmlLinesArray(sourceFileName)).length + " OF " + fileScanner("last.num") + "\n"); }
        				}
        			} else { 
        				    if (Integer.valueOf(fileScanner("last.num")) == 1 ) { fileWriterPrinter("PASSED!"); } 
        				    else { fileWriterPrinter("ALL PASSED!\n"); }
        				}  		
        	    } else {
            		    if(fileExist(sourceFileName, false)) {
       			        System.out.println("FAILED: " + convertXmlArrayToTestNG(readLogOutputXmlLinesArray(sourceFileName)).length);	
       		            } else { System.out.print("ALL PASSED!"); }        	    	
        	    }

        		// PRE-CLEAN:
        		if (fileExist(System.getProperty("user.dir"), xmlOutputFileName, false)) { fileCleaner(System.getProperty("user.dir"), xmlOutputFileName);}		
        		
        		// FAILED SUITE CREATES XML, OTHERWISE NOTHING:
        		if (fileExist(sourceFileName, false)) {
        			
//        		// UPDATE PREVIOUS TEST NUMBER AS PER FAILED:
//        		if (fileExist("prev.num", false)){
//        			fileCleaner("prev.num");
//        			fileWriter("prev.num", convertXmlArrayToTestNG(readLogOutputXmlLinesArray(sourceFileName)).length);
//        		}
        			
//        		// UPDATE LAST TEST NUMBER AS PER FAILED:
//        		if (fileExist("last.num", false)){
//        			fileCleaner("last.num");
//        			fileWriter("last.num", convertXmlArrayToTestNG(readLogOutputXmlLinesArray(sourceFileName)).length);
//        		}
        			
            	// UPDATE FAILED TEST NUMBER AS PER FAILED:
            	if (fileExist("failed.num", false)){
            		fileCleaner("failed.num");
            		fileWriter("failed.num", convertXmlArrayToTestNG(readLogOutputXmlLinesArray(sourceFileName)).length);
            	}
        		
        		// HEADER:
        		fileWriter(System.getProperty("user.dir"), xmlOutputFileName, "<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        		fileWriter(System.getProperty("user.dir"), xmlOutputFileName, "<!DOCTYPE suite SYSTEM \"http://testng.org/testng-1.0.dtd\">");
        		fileWriter(System.getProperty("user.dir"), xmlOutputFileName, "<suite name=\"" + suiteName + "\">");
        		
        		// LISTENERS:
				String extentReporterClassPath; 
        		extentReporterClassPath = new Object(){}.getClass().getPackage().getName();
        		extentReporterClassPath = extentReporterClassPath.substring(0, extentReporterClassPath.lastIndexOf("."));
        		extentReporterClassPath = extentReporterClassPath.substring(0, extentReporterClassPath.lastIndexOf("."));
        		extentReporterClassPath = extentReporterClassPath + ".extentreporter.ExtentReporterNG";               
        		fileWriter(System.getProperty("user.dir"), xmlOutputFileName, "  <listeners>");
        		fileWriter(System.getProperty("user.dir"), xmlOutputFileName, "    <listener class-name=\"" + extentReporterClassPath + "\"/>");
        		fileWriter(System.getProperty("user.dir"), xmlOutputFileName, "  </listeners>");
        		
        		// TEST:
        		fileWriter(System.getProperty("user.dir"), xmlOutputFileName, "  <test name=\"" + testName + "\">");
        		fileWriter(System.getProperty("user.dir"), xmlOutputFileName, "    <classes>");
        						
        		// BODY:
        		fileWriter(System.getProperty("user.dir"), xmlOutputFileName, "      " + reporterClass);       		
        		String[] string = convertXmlArrayToTestNG(readLogOutputXmlLinesArray(sourceFileName)); // READS FAILURES
        		/** WILL PRINT XML CLASSES-BODY */ // for (String s : string) { System.out.println(s); }
        		for (String s : string) { fileWriter(System.getProperty("user.dir"), xmlOutputFileName, s); }
        		
        		// FOOTER:
        		fileWriter(System.getProperty("user.dir"), xmlOutputFileName, "    </classes>");
        		fileWriter(System.getProperty("user.dir"), xmlOutputFileName, "  </test>");
        		fileWriter(System.getProperty("user.dir"), xmlOutputFileName, "</suite>");
        		
        		}	
        	}
		// ####################### TEST-NG XML EXTRACTOR-CREATER END #######################
        	
        // ####################### LOG FILES HANDLER START #######################
    		/** 
    		 * Cleans all the Log records left from previous test executions
    		 * @throws NumberFormatException 
    		 * @throws IOException 
    		 */
    		public void beforeCleaner() throws NumberFormatException, IOException{
    		    // PRE-CLEANING:
                fileCleaner("email.all"  );
                fileCleaner("email.cont" );
                fileCleaner("email.subj" );
                fileCleaner("failed.log" );		
                fileCleaner("finish.time");
                fileCleaner("ini.time"   );
                fileCleaner("print.log"  );
                fileCleaner("run.log"    );
                fileCleaner("stack.trace");
                fileCleaner("start.time" );
                fileCleaner("wait.log"   );
                fileCleaner("xml.path"   );
                fileCleaner("source.html");
                fileCleaner("test.num"   );
                fileCleaner("failed.num" );
                fileCleaner("emailable-report.html");
                fileCleaner("extent-test-report.html");
    		}
        		
        		/** 
        		 * Cleans all the Log records after current test executions
        		 * @throws NumberFormatException 
        		 * @throws IOException 
        		 */
        		public void afterCleaner() throws NumberFormatException, IOException{
    			   fileCleaner("ini.time"   );
    			   fileCleaner("failed.num" );
    			   fileCleaner("test.num"   );
    			   fileCleaner("add.num"    );
//    			   fileCleaner("prev.num"   );
    			   fileCleaner("server.info");
    			   fileCleaner("email.opt"  );
    			   fileCleaner("email.all"  );
    			   fileCleaner("email.cont" );
    			   fileCleaner("email.subj" );
    			   fileCleaner("add.show"   );
    			   fileCleaner("screen-shots.zip");
    			   fileCleaner("screen-shots.renameToZip");
        		}
        // ####################### LOG FILES HANDLER END #######################
        	
        	/**
        	 * This METHOD reads any Text File,
        	 * converts and outputs all the text lines as an ASC sorted String Array
        	 */
        	@SuppressWarnings("resource")
        	public String[] readTextFileOutputLinesArray(String fileName) throws IOException{
        	    BufferedReader in = new BufferedReader(new FileReader(Common.testOutputFileDir + fileName));
        	    String str=null;
        	    ArrayList<String> lines = new ArrayList<String>();
        	    while ((str = in.readLine()) != null) {
        	        if (!str.contains("helper") && (str.length() != 0)) { lines.add(str); }
        	        }
        	    String[] linesArray = lines.toArray(new String[lines.size()]);
        	    return linesArray;
        	}
        	
        	/**
        	 * This METHOD reads any Text File,
        	 * converts and outputs all the text lines as an ASC sorted String Array
        	 */
        	@SuppressWarnings("resource")
        	public String[] readTextFileOutputLinesArray(String path, String fileName) throws IOException{
        	    BufferedReader in = new BufferedReader(new FileReader(path + File.separator + fileName));
        	    String str=null;
        	    ArrayList<String> lines = new ArrayList<String>();
        	    while ((str = in.readLine()) != null) {
        	        if (!str.contains("helper") && (str.length() != 0)) { lines.add(str); }
        	        }
        	    String[] linesArray = lines.toArray(new String[lines.size()]);
        	    return linesArray;
        	}
        	
        	/**
        	 * This METHOD gets a String Array and sorts it as per ASC order
        	 */
            public String[] orderedStringArrayAsc(String[] string) {
                Arrays.sort(string);
                return string;
            }

        	/**
        	 * This METHOD reads Log Files, extracts XML CLASS PATH(s),
        	 * converts and outputs them as an ASC sorted String Array
        	 */
        	public String[] readLogOutputXmlLinesArray(String fileName) throws IOException{
        		String[] string = readTextFileOutputLinesArray(fileName);
                Pattern p = Pattern.compile("<class name=\"");
                
                // FAILURES COUNTER
        		int i = 0;
        		for (String s : string) {
        			Matcher m = p.matcher(s); Boolean found = m.find();
        			if ( found && !s.contains("helper") && (s.length() != 0) ){ i++; }
        			}
        		
        		// CLASS LINE EXTRACTION
        		String[] linesArray = new String[i];		
        		int j = 0;
        		for (String s : string) {
        			Matcher m = p.matcher(s); Boolean found = m.find();
        			if ( found && !s.contains("helper") && (s.length() != 0) ) {
        		    	linesArray[j] = s.replace(s.substring(0, s.indexOf("<class name=\"")),"      ");
        		    	j++;
        		    	}
        		}
        		
        		return orderedStringArrayAsc(linesArray);
        	}  
        	
        	/**
        	 * This METHOD reads Log Files, extracts XML CLASS PATH(s),
        	 * converts and outputs them as an ASC sorted String Array
        	 */
        	public String[] readLogOutputXmlLinesArray(String path, String fileName) throws IOException{
        		String[] string = readTextFileOutputLinesArray(path, fileName);
                Pattern p = Pattern.compile("<class name=\"");
                
                // FAILURES COUNTER
        		int i = 0;
        		for (String s : string) {
        			Matcher m = p.matcher(s); Boolean found = m.find();
        			if ( found && !s.contains("helper") && (s.length() != 0) ){ i++; }
        			}
        		
        		// CLASS LINE EXTRACTION
        		String[] linesArray = new String[i];		
        		int j = 0;
        		for (String s : string) {
        			Matcher m = p.matcher(s); Boolean found = m.find();
        			if ( found && !s.contains("helper") && (s.length() != 0) ) {
        		    	linesArray[j] = s.replace(s.substring(0, s.indexOf("<class name=\"")),"      ");
        		    	j++;
        		    	}
        		}       		
        		return orderedStringArrayAsc(linesArray);
        	}
        	
        	/**
        	 * This METHOD converts Sorted XML CLASS PATH(s) as per TestNG XML Format
        	 */
        	public String[] convertXmlArrayToTestNG(String[] string) {
        		for (int i = 0; i < string.length-1; i++) {
        			if ( string[i].substring(0, string[i].indexOf("<methods>")).equals(
        			     string[i+1].substring(0, string[i+1].indexOf("<methods>"))) )
        			{ string[i] = string[i].replace("</methods></class>", "");}
        		}		
        		for (int j = string.length-1; j > 0; j--) {
        			if ( string[j].substring(0, string[j].indexOf("<methods>")).equals(
        			     string[j-1].substring(0, string[j-1].indexOf("<methods>"))) )
        			{ string[j] = string[j].replace(string[j].substring(0, string[j].indexOf("<include")), "             ");}
        		}		
        	return string;
        	}

            /** Returns Suffix based on Number */
			public String getNumberSuffix(int num) {
				String s = String.valueOf(num);
				if (s.endsWith("0")) { return "st"; }
				else if (s.endsWith("1"))  { return "st"; }
				else if (s.endsWith("2"))  { return "nd"; }
				else if (s.endsWith("3"))  { return "rd"; }
				else if (s.endsWith("10")) { return "th"; }
				else if (s.endsWith("11")) { return "th"; }
				else if (s.endsWith("12")) { return "th"; }
				else if (s.endsWith("13")) { return "th"; }
				else { return "th"; }
            }
			
			/** Returns Current Time */
			public String getCurrentTime() {
				// DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				// need to change after the date format is decided
				DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
				Date date = new Date();
				return dateFormat.format(date);
			}
			
			/** Returns Current Time, Hours only */
			public String getCurrentHours() {
				// DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				// need to change after the date format is decided
				DateFormat dateFormat = new SimpleDateFormat("HH");
				Date date = new Date();
				return dateFormat.format(date);
			}
			
			/** Returns Current Time, Minutes only */
			public String getCurrentMinutes() {
				// DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				// need to change after the date format is decided
				DateFormat dateFormat = new SimpleDateFormat("mm");
				Date date = new Date();
				return dateFormat.format(date);
			}
			
			/** Returns Current Time, Seconds only */
			public String getCurrentSeconds() {
				// DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				// need to change after the date format is decided
				DateFormat dateFormat = new SimpleDateFormat("ss");
				Date date = new Date();
				return dateFormat.format(date);
			}

			public String getCurrentDate() {
				// DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				// need to change after the date format is decided
				DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
				Date date = new Date();
				return dateFormat.format(date);
			}

			public String getArticleDate() {
				// DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				// needs to be change after the date format is decided
				DateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
				Date date = new Date();
				return dateFormat.format(date);
			}
			
			public String getDefaultDate() {
				// DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				// needs to be change after the date format is decided
				DateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy");
				Date date = new Date();
				return dateFormat.format(date);
			}
			
			/** Get Current dayOfMonth as Number*/
			public int getCurrentDayOfMonthAsNum() {
				int dayOfMonth = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
				return dayOfMonth;
			}
			
			/** Get Current dayOfMonth as String*/
			public String getCurrentDayOfMonthAsString() {
				DateFormat dateFormat = new SimpleDateFormat("dd");
				Date date = new Date();
				return dateFormat.format(date);
			}

			/** Get Current dayOfWeek Number */
			public int getCurrentDayOfWeekAsNum() {
				int dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
				return dayOfWeek;
			}

			/** Get Current dayOfWeek Name */
			public String getCurrentDayOfWeekAsShortName() {
				DateFormat format = new SimpleDateFormat("EEE");
				Date date = new Date(System.currentTimeMillis());
				String dayOfWeek = format.format(date);
				return dayOfWeek;
			}

			/** Get Current dayOfWeek Full Name */
			public String getCurrentDayOfWeekAsFullName() {
				return new DateFormatSymbols().getWeekdays()[Calendar.getInstance()
						.get(Calendar.DAY_OF_WEEK)];
			}

			/**
			 * Convert Integer to localized month name
			 * 
			 * @param month
			 * @return Integer month
			 */
			public String getMonth(int month) {
				return new DateFormatSymbols().getMonths()[month];
			}

			public int getCurrentMonthNumber() {
				Calendar cal = Calendar.getInstance();
				String month = new SimpleDateFormat("MM").format(cal.getTime());
				return Integer.valueOf(month);
			}
			
			public String getCurrentMonthNameShort() {
				Calendar cal = Calendar.getInstance();
				String month = new SimpleDateFormat("MMM").format(cal.getTime());
				return month;
			}
			
			public String getCurrentMonthNameFull() {
				Calendar cal = Calendar.getInstance();
				String month = new SimpleDateFormat("MMMM").format(cal.getTime());
				return month;
			}

			public String getCurrentYear() {
				Integer year = Calendar.getInstance().get(Calendar.YEAR);
				return year.toString();
			}
			
			/** Date Calculator per days step: adding or reducing number of days */
			public long dateAddDaysToCurrentTimeMilliseconds(int addDays) {
				long mills = System.currentTimeMillis() + addDays * 24 * 3600 * 1000; // add "days";
				return mills;
			}

			/** Date Calculator per days step: adding or reducing number of days */
			public String dateAddDaysToCurrentDate(int addDays) {
				return convertCalendarMillisecondsAsLongToDate(dateAddDaysToCurrentTimeMilliseconds(addDays));
			}
			
			/**
			 * Date Calculator per days step: adding or reducing number of days from/to System Date
			 * @throws IOException
			 * @throws NumberFormatException 
			 */
			public String dateAddDaysToCurrentSystemDate(int addDays) throws NumberFormatException, IOException {
				try{
					long fingerprint = dateAddDaysToCurrentTimeMilliseconds(addDays);				
					DateFormat dateFormat = new SimpleDateFormat("M/dd/yyyy");
					fileWriterPrinter("\nCurrent System Date: " + dateFormat.format(new Date()));				
					String dateToSet = dateFormat.format(new Date(fingerprint));					
					fileWriterPrinter("Changed System Date: " + dateToSet + "\n");
					Runtime.getRuntime().exec("cmd /C date " + dateToSet);  //  M/dd/yyyy
					return dateToSet;
				} catch(Exception e) { fileWriterPrinter(e); return null;}				
			}
			
			/** Convert Date Format from "yyyy-MM-dd" to "MMM d, yyyy" */
			public String convertCalendarDateFormatFromNumbersToMonthDayComaYear(String stringDate) throws ParseException {
				DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				Date date = formatter.parse(stringDate);
				long mills = date.getTime();
				formatter = new SimpleDateFormat("MMM dd, yyyy");
				date = new Date(mills);
				return formatter.format(date);
			}
			
			/** Convert Date to dayOfWeek Number */
			public int convertCalendarDateToDayOfWeekAsNum(String stringDate) throws ParseException {
				DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				Date date = formatter.parse(stringDate);
				Calendar c = Calendar.getInstance();
				c.setTime(date);
				int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
				return dayOfWeek;
			}
			
			/** Convert Date and Time list year, month, day, hours, minutes, seconds to long Milliseconds */
			public long convertCalendarIntDateTimeListToMillisecondsAsLong(
					int year, int month, int day, int hours, int min, int sec)
					throws ParseException {

				return convertCalendarDateToMillisecondsAsLong(
						String.valueOf(year)
						+ "-" + String.valueOf(month)
						+ "-" + String.valueOf(day))
						+ ((hours * 3600) + (min * 60) + sec) * 1000
						;
			}
			
			/** Convert Date and Time list year, month, day, hours, minutes, seconds to String Milliseconds */
			public String convertCalendarIntDateTimeListToMillisecondsAsString(
					int year, int month, int day, int hours, int min, int sec)
					throws ParseException {

				return String.valueOf(convertCalendarDateToMillisecondsAsLong(
						String.valueOf(year)
						+ "-"
						+ String.valueOf(month)
						+ "-"
						+ String.valueOf(day))
						+ ((hours * 3600) + (min * 60) + sec) * 1000
						);
			}

			/** Convert Date to long Milliseconds */
			public long convertCalendarDateToMillisecondsAsLong(String stringDate) throws ParseException {
				DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				Date date = formatter.parse(stringDate);
				long mills = date.getTime();
				return mills;
			}

			/** Convert Date to String Milliseconds */
			public String convertCalendarDateToMillisecondsAsString(String stringDate) throws ParseException {
				DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				Date date = formatter.parse(stringDate);
				long mills = date.getTime();
				return String.valueOf(mills);
			}
			
			/** Convert Date and Time (Hour:Min) to long Milliseconds */
			public long convertCalendarDateTimeHourMinToMillisecondsAsLong(String stringDate) throws ParseException {
				DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				Date date = formatter.parse(stringDate);
				long mills = date.getTime();
				return mills;
			}
			
			/** Convert Date and Time (Hour:Min) to String Milliseconds */
			public String convertCalendarDateTimeHourMinToMillisecondsAsString(String stringDate) throws ParseException {
				DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				Date date = formatter.parse(stringDate);
				long mills = date.getTime();
				return String.valueOf(mills);
			}
			
			/** Convert Date and Time (Hour:Min:Sec) to long Milliseconds */
			public long convertCalendarDateTimeHourMinSecToMillisecondsAsLong(String stringDate) throws ParseException {
				DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date date = formatter.parse(stringDate);
				long mills = date.getTime();
				return mills;
			}
			
			/** Convert Date and Time (Hour:Min:Sec) to String Milliseconds */
			public String convertCalendarDateTimeHourMinSecToMillisecondsAsString(String stringDate) throws ParseException {
				DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date date = formatter.parse(stringDate);
				long mills = date.getTime();
				return String.valueOf(mills);
			}
				
			/** Convert long Milliseconds to Date and Time (Hour:Min) */
			public String convertCalendarMillisecondsAsLongToDateTimeHourMin(long fingerprint) {
				DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm"); // not "yyyy-MM-dd HH:mm:ss"
				Date date = new Date(fingerprint);
				return format.format(date);
			}
			
			/** Convert long Milliseconds to Date and Time (Hour:Min:Sec) */
			public String convertCalendarMillisecondsAsLongToDateTimeHourMinSec(long fingerprint) {
				DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date date = new Date(fingerprint);
				return format.format(date);
			}
			
			/* @throws IOException
			 * @throws NumberFormatException */
			public String convertCalendarMillisecondsAsStringToDateTimeHourMin(String s) throws NumberFormatException, IOException {
				try {
					long l = Long.parseLong(s);
					DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm"); // not "yyyy-MM-dd HH:mm:ss"
					Date date = new Date(l);
					String d = format.format(date);
					return d;
				} catch(NumberFormatException exception) {
					fileWriterPrinter("\"NumberFormatException\" thrown "
							+ exception.getMessage());
					return ("\"NumberFormatException\" thrown " + exception
							.getMessage());
				}
			}
			
			/* @throws IOException
			 * @throws NumberFormatException */
			public static String convertCalendarMillisecondsAsStringToDateTimeHourMinSec(String s) throws NumberFormatException, IOException {
				try {
					long l = Long.parseLong(s);
					DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date date = new Date(l);
					String d = format.format(date);
					return d;
				} catch(NumberFormatException exception) {
					fileWriterPrinter("\"NumberFormatException\" thrown "
							+ exception.getMessage());
					return ("\"NumberFormatException\" thrown " + exception
							.getMessage());
				}
			}
			
			/** Convert long Milliseconds to Date */
			public String convertCalendarMillisecondsAsLongToDate(long fingerprint) {
				DateFormat format = new SimpleDateFormat("yyyy-MM-dd"); // not "yyyy-MM-dd HH:mm:ss"
				Date date = new Date(fingerprint);
				return format.format(date);
			}

			/* @throws IOException
			 * @throws NumberFormatException */
			public String convertCalendarMillisecondsAsStringToDate(String s) throws NumberFormatException, IOException {
				try {
					long l = Long.parseLong(s);
					DateFormat format = new SimpleDateFormat("yyyy-MM-dd"); // not "yyyy-MM-dd HH:mm:ss"
					Date date = new Date(l);
					String d = format.format(date);
					return d;
				} catch(NumberFormatException exception) {
					fileWriterPrinter("\"NumberFormatException\" thrown "
							+ exception.getMessage());
					return ("\"NumberFormatException\" thrown " + exception
							.getMessage());
				}
			}
			
			/** Convert long Milliseconds to Year as String */
			public String convertCalendarMillisecondsAsLongToYearAsString(long fingerprint) {
				DateFormat format = new SimpleDateFormat("yyyy"); // not "yyyy-MM-dd HH:mm:ss"
				Date date = new Date(fingerprint);
				return format.format(date);
			}

			/* @throws IOException
			 * @throws NumberFormatException */
			public String convertCalendarMillisecondsAsStringToYearAsString(String s) throws NumberFormatException, IOException {
				try {
					long l = Long.parseLong(s);
					DateFormat format = new SimpleDateFormat("yyyy"); // not "yyyy-MM-dd HH:mm:ss"
					Date date = new Date(l);
					String d = format.format(date);
					return d;
				} catch(NumberFormatException exception) {
					fileWriterPrinter("\"NumberFormatException\" thrown "
							+ exception.getMessage());
					return ("\"NumberFormatException\" thrown " + exception
							.getMessage());
				}
			}
			
			/** Convert long Milliseconds to Month Number as String */
			public String convertCalendarMillisecondsAsLongToMonthNumAsString(long fingerprint) {
				DateFormat format = new SimpleDateFormat("MM"); // not "yyyy-MM-dd HH:mm:ss"
				Date date = new Date(fingerprint);
				return format.format(date);
			}

			/* @throws IOException
			 * @throws NumberFormatException */
			public static String convertCalendarMillisecondsAsStringToMonthNumAsString(String s) throws NumberFormatException, IOException {
				try {
					long l = Long.parseLong(s);
					DateFormat format = new SimpleDateFormat("MM"); // not "yyyy-MM-dd HH:mm:ss"
					Date date = new Date(l);
					String d = format.format(date);
					return d;
				} catch(NumberFormatException exception) {
					fileWriterPrinter("\"NumberFormatException\" thrown "
							+ exception.getMessage());
					return ("\"NumberFormatException\" thrown " + exception
							.getMessage());
				}
			}
			
			/** Convert long Milliseconds to Month Name (Short) */
			public String convertCalendarMillisecondsAsLongToMonthNameShort(long fingerprint) {
				DateFormat format = new SimpleDateFormat("MMM"); // not "yyyy-MM-dd HH:mm:ss"
				Date date = new Date(fingerprint);
				return format.format(date);
			}

			/* @throws IOException
			 * @throws NumberFormatException */
			public String convertCalendarMillisecondsAsStringToMonthNameShort(String s) throws NumberFormatException, IOException {
				try {
					long l = Long.parseLong(s);
					DateFormat format = new SimpleDateFormat("MMM"); // not "yyyy-MM-dd HH:mm:ss"
					Date date = new Date(l);
					String d = format.format(date);
					return d;
				} catch(NumberFormatException exception) {
					fileWriterPrinter("\"NumberFormatException\" thrown "
							+ exception.getMessage());
					return ("\"NumberFormatException\" thrown " + exception
							.getMessage());
				}
			}
			
			/** Convert long Milliseconds to Publication Date */
			public String convertCalendarMillisecondsAsLongToDatePublication(long fingerprint) {
			DateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
		    Date date = new Date(fingerprint);
		    return dateFormat.format(date);
			}
			
			/** Convert Long Milliseconds to Day of Month as Integer*/
			public int convertCalendarMillisecondsAsLongToDayOfMonthInt(long fingerprint) {
			DateFormat format = new SimpleDateFormat("dd");
			Date date = new Date(fingerprint);
			return Integer.parseInt(format.format(date));
			}
			
			/** Convert Long Milliseconds to Day of Month as String*/
			public String convertCalendarMillisecondsAsLongToDayOfMonthString(long fingerprint) {
			DateFormat format = new SimpleDateFormat("dd");
			Date date = new Date(fingerprint);
			return format.format(date);
			}
			
			/** Convert String Milliseconds to Day of Month as Integer*/
			public int convertCalendarMillisecondsAsStringToDayOfMonthInt(String s) {
			DateFormat format = new SimpleDateFormat("dd");
			Date date = new Date(Long.parseLong(s));
			return Integer.parseInt(format.format(date));
			}
			
			/** Convert String Milliseconds to Day of Month as String*/
			public String convertCalendarMillisecondsAsStringToDayOfMonthString(String s) {
			DateFormat format = new SimpleDateFormat("dd");
			Date date = new Date(Long.parseLong(s));
			return format.format(date);
			}

			/**
			 * Convert long Milliseconds to dayOfWeek Number long fingerprint can be
			 * limited when using typing for numbers. Can be allowed up to SEC. Requires
			 * multiplication x 1000, or expression
			 */
			public int convertCalendarMillisecondsAsLongToDayOfWeekAsNum(long fingerprint) {
				Date date = new Date(fingerprint);
				Calendar cal = Calendar.getInstance();
				cal.setTime(date);
				int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
				return dayOfWeek;
			}

			/**
			 * Convert String Milliseconds to dayOfWeek Number long fingerprint can be
			 * limited when using typing for numbers. Can be allowed up to SEC. Requires
			 * @throws IOException
			 * @throws NumberFormatException 
			 */
			public static int convertCalendarMillisecondsAsStringToDayOfWeekAsNum(String s) throws NumberFormatException, IOException {
				try {
					long l = Long.parseLong(s);
					Date date = new Date(l);
					Calendar cal = Calendar.getInstance();
					cal.setTime(date);
					int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
					return dayOfWeek;
				} catch(NumberFormatException exception) {
					fileWriterPrinter("\"NumberFormatException\" thrown "
							+ exception.getMessage());
					return -1;
				}
			}

			/**
			 * Convert long Milliseconds to dayOfWeek Name long fingerprint can be
			 * limited when using typing for numbers. Can be allowed up to SEC. Requires
			 * multiplication x 1000, or expression
			 */
			public String convertCalendarMillisecondsAsLongToDayOfWeekAsShortName(long fingerprint) {
				DateFormat format = new SimpleDateFormat("EEE");
				Date date = new Date(fingerprint);
				String dayOfWeek = format.format(date);
				return dayOfWeek;
			}

			/**
			 * Convert String Milliseconds to dayOfWeek Name long fingerprint can be
			 * limited when using typing for numbers. Can be allowed up to SEC. Requires
			 * @throws IOException
			 * @throws NumberFormatException 
			 */
			public String convertCalendarMillisecondsAsStringToDayOfWeekAsShortName(String s) throws NumberFormatException, IOException {
				try {
					long l = Long.parseLong(s);
					DateFormat format = new SimpleDateFormat("EEE");
					Date date = new Date(l);
					String dayOfWeek = format.format(date);
					return dayOfWeek;
				} catch(NumberFormatException exception) {
					fileWriterPrinter("\"NumberFormatException\" thrown "
							+ exception.getMessage());
					return ("\"NumberFormatException\" thrown " + exception
							.getMessage());
				}
			}

			/** Convert dayOfWeek Number to dayOfWeek Name */
			public String convertDayOfWeekAsNumToName(int dayOfWeek) {
				String[] DayOfWeekAsShortName = { "Sun","Mon","Tue","Wed","Thu","Fri","Sat" };
				return DayOfWeekAsShortName[dayOfWeek - 1];
			}
			
			/** dayOfWeek Number Array */
			public int[] dayOfWeekAsNumArray = { 2, 3, 4, 5, 6, 7, 1 };
			private static Scanner scanner;

			/** Convert dayOfWeek Number to dayOfWeek Full Name */
			public String convertDayOfWeekAsNumToFullName(int dayOfWeek) {
				return new DateFormatSymbols().getWeekdays()[dayOfWeek];
			}
			
			/** Convert dayOfWeek Number to dayOfWeek Full Name */
			public int convertDayOfWeekAsNameToNum(String dayOfWeekName) {
				int dayOfWeek = 0;
				for (int i = 1; i <= 7; i++) {
					if ((new DateFormatSymbols().getWeekdays()[i]).toLowerCase().contains(dayOfWeekName.toLowerCase())){
						dayOfWeek = i;
					}
				}
				return dayOfWeek;
			}
			
			/** Convert Month MM Name to Integer Number */		
			public int convertMonthNameToNumAsInt(String name) {
		        int month = 0;
		        name = name.substring(0,3);
		        if (name.equals("Jan")) { month = 1; }
		        if (name.equals("Feb")) { month = 2; }
		        if (name.equals("Mar")) { month = 3; }
		        if (name.equals("Apr")) { month = 4; }
		        if (name.equals("May")) { month = 5; }
		        if (name.equals("Jun")) { month = 6; }
		        if (name.equals("Jul")) { month = 7; }
		        if (name.equals("Aug")) { month = 8; }
		        if (name.equals("Sep")) { month = 9; }
		        if (name.equals("Oct")) { month = 10; }
		        if (name.equals("Nov")) { month = 11; }
		        if (name.equals("Dec")) { month = 12; }
		    	return month;
			}
			
			/** Convert Month MM Name to Integer Number */		
			public String convertMonthNameToNumAsString(String name) {
		        String month = null;
		        name = name.substring(0,3);
		        if (name.equals("Jan")) { month = "01"; }
		        if (name.equals("Feb")) { month = "02"; }
		        if (name.equals("Mar")) { month = "03"; }
		        if (name.equals("Apr")) { month = "04"; }
		        if (name.equals("May")) { month = "05"; }
		        if (name.equals("Jun")) { month = "06"; }
		        if (name.equals("Jul")) { month = "07"; }
		        if (name.equals("Aug")) { month = "08"; }
		        if (name.equals("Sep")) { month = "09"; }
		        if (name.equals("Oct")) { month = "10"; }
		        if (name.equals("Nov")) { month = "11"; }
		        if (name.equals("Dec")) { month = "12"; }
		    	return month;
			}
			
			/** Convert Month Number to Name */
			public String convertMonthNumToName(int month) {
				return new DateFormatSymbols().getMonths()[month-1];
			}
			
			/** Convert Number of Years ago or in future from Now into Millisecond Time */
			public long convertYearsFromNowToMilliseconds(int years) throws ParseException{
            	long Years = Long.valueOf(String.valueOf(years));            	
            	long twoThousandYearAsMilliseconds = (convertCalendarDateToMillisecondsAsLong("2000-01-01") - convertCalendarDateToMillisecondsAsLong("0-01-01"));
            	long oneYearAverageAsMilliseconds = twoThousandYearAsMilliseconds/2000;         	
            	return (System.currentTimeMillis() + oneYearAverageAsMilliseconds*Years);
			}
			
			/** Convert Number of Months ago or in future from Now into Milliseconds Time */
			public long convertMonthsFromNowToMilliseconds(int months) throws ParseException{
            	long Months = Long.valueOf(String.valueOf(months));
            	long twoThousandYearAsMilliseconds = (convertCalendarDateToMillisecondsAsLong("2000-01-01") - convertCalendarDateToMillisecondsAsLong("0-01-01"));           	
          	    long oneMonthAverageAsMilliseconds = twoThousandYearAsMilliseconds/(2000*12);   	
            	return (System.currentTimeMillis() + oneMonthAverageAsMilliseconds*Months);
			}

			/** Convert Number of Years, Months and Days ago or in future from Now into Milliseconds Time */
			public long convertYearsMonthsDaysFromNowToMilliseconds(int years, int months, int days) throws ParseException{
				long Years = Long.valueOf(String.valueOf(years));
				long Months = Long.valueOf(String.valueOf(months));
				long Days = Long.valueOf(String.valueOf(days));
            	long twoThousandYearAsMilliseconds = (convertCalendarDateToMillisecondsAsLong("2000-01-01") - convertCalendarDateToMillisecondsAsLong("0-01-01"));           	
            	long oneYearAverageAsMilliseconds = twoThousandYearAsMilliseconds/2000;
            	long oneMonthAverageAsMilliseconds = twoThousandYearAsMilliseconds/(2000*12);
            	long oneDayAsMilliseconds = 24*3600*1000;
            	return (System.currentTimeMillis() + 
            			oneYearAverageAsMilliseconds*Years + 
            			oneMonthAverageAsMilliseconds*Months + 
            			oneDayAsMilliseconds*Days);
			}
			
			/** Convert Number of Years, Months and Days ago or in future from Date into Milliseconds Time */
			public long convertYearsMonthsDaysFromDateToMilliseconds(String fromDate, int years, int months, int days) throws ParseException{
				long millisecondsFrom = convertCalendarDateToMillisecondsAsLong(fromDate);
				long Years = Long.valueOf(String.valueOf(years));
				long Months = Long.valueOf(String.valueOf(months));
				long Days = Long.valueOf(String.valueOf(days));
            	long twoThousandYearAsMilliseconds = (convertCalendarDateToMillisecondsAsLong("2000-01-01") - convertCalendarDateToMillisecondsAsLong("0-01-01"));           	
            	long oneYearAverageAsMilliseconds = twoThousandYearAsMilliseconds/2000;
            	long oneMonthAverageAsMilliseconds = twoThousandYearAsMilliseconds/(2000*12);
            	long oneDayAsMilliseconds = 24*3600*1000;
            	return (millisecondsFrom + 
            			oneYearAverageAsMilliseconds*Years + 
            			oneMonthAverageAsMilliseconds*Months + 
            			oneDayAsMilliseconds*Days);
			}
			
			/** Convert Number of Years, Months and Days ago or in future from Date into Milliseconds Time */
			public long convertYearsMonthsDaysFromDateTimeHourMinSecToMilliseconds(String fromDateTimeHourMinSec, int years, int months, int days) throws ParseException{
				long millisecondsFrom = convertCalendarDateTimeHourMinSecToMillisecondsAsLong(fromDateTimeHourMinSec);
				long Years = Long.valueOf(String.valueOf(years));
				long Months = Long.valueOf(String.valueOf(months));
				long Days = Long.valueOf(String.valueOf(days));
            	long twoThousandYearAsMilliseconds = (convertCalendarDateToMillisecondsAsLong("2000-01-01") - convertCalendarDateToMillisecondsAsLong("0-01-01"));           	
            	long oneYearAverageAsMilliseconds = twoThousandYearAsMilliseconds/2000;
            	long oneMonthAverageAsMilliseconds = twoThousandYearAsMilliseconds/(2000*12);
            	long oneDayAsMilliseconds = 24*3600*1000;
            	return (millisecondsFrom + 
            			oneYearAverageAsMilliseconds*Years + 
            			oneMonthAverageAsMilliseconds*Months + 
            			oneDayAsMilliseconds*Days);
			}
     // ################# CALENDAR END ###########################

	 // ################# TIME CALCULATOR START ##################		
			/** Convert Seconds to Hours, Minutes, Seconds */
			public String convertTimeSecondsToHoursMinSeconds(int totalSeconds) {
				int MINUTES_IN_AN_HOUR = 60;
				int SECONDS_IN_A_MINUTE = 60;
				int hours = totalSeconds / MINUTES_IN_AN_HOUR / SECONDS_IN_A_MINUTE;
				int minutes = (totalSeconds - (convertTimeHoursToSeconds(hours))) / SECONDS_IN_A_MINUTE;
				int seconds = totalSeconds
						- ((convertTimeHoursToSeconds(hours)) + (convertTimeMinutesToSeconds(minutes)));
				return hours + " hours " + minutes + " minutes " + seconds + " seconds";
			}

			/** Convert Hours to Seconds */
			public static int convertTimeHoursToSeconds(int hours) {
				int SECONDS_IN_A_MINUTE = 60;
				int MINUTES_IN_AN_HOUR = 60;
				return hours * MINUTES_IN_AN_HOUR * SECONDS_IN_A_MINUTE;
			}

			/** Convert SMinutes to Seconds */
			public static int convertTimeMinutesToSeconds(int minutes) {
				int SECONDS_IN_A_MINUTE = 60;
				return minutes * SECONDS_IN_A_MINUTE;
			}

			/** Convert long Milliseconds to Seconds */
			public static long convertTimeMillisecondsAsLongToSeconds(long milliseconds) {
				int MILLISECONDS_IN_A_SECOND = 1000;
				Double d = Double.valueOf(milliseconds) / Double.valueOf(MILLISECONDS_IN_A_SECOND);
				return Math.round(d);
			}

			/** Convert String Milliseconds to Seconds */	
			public static long convertTimeMillisecondsAsStringToSeconds(String milliseconds) {
				int MILLISECONDS_IN_A_SECOND = 1000;
				Double d = Double.valueOf(milliseconds) / Double.valueOf(MILLISECONDS_IN_A_SECOND);
				return Math.round(d);
			}
			
			/** Convert long Milliseconds to Duration "Hours:Min:Sec" auto-format */
			public static String convertTimeMillisecondsAsLongToDuration(long milliseconds) {
				String hours = String.format("%02d", TimeUnit.MILLISECONDS.toHours(milliseconds));
				String minutes = String.format("%02d",
					   TimeUnit.MILLISECONDS.toMinutes(milliseconds) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milliseconds)));
			    String seconds = String.format("%02d",
			           TimeUnit.MILLISECONDS.toSeconds(milliseconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));
			    String duration = Integer.valueOf(hours) + ":" + minutes + ":" + seconds;
			    return duration;       
			}
			
			/** Convert long Milliseconds to Duration "Min:Sec" or "Hours:Min:Sec" auto-format */
			public String convertTimeMillisecondsAsLongToDurationAuto(long milliseconds) {
				String hours = String.format("%02d", TimeUnit.MILLISECONDS.toHours(milliseconds));
				String minutes = String.format("%02d",
					   TimeUnit.MILLISECONDS.toMinutes(milliseconds) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milliseconds)));
			    String seconds = String.format("%02d",
			           TimeUnit.MILLISECONDS.toSeconds(milliseconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));
			if (Integer.valueOf(hours) == 0) {
				String duration = minutes + ":" + seconds;
			    return duration;
			} else {
			    String duration = hours + ":" + minutes + ":" + seconds;
			    return duration;
			}          
			}
			
			/** Convert long Milliseconds to Duration "Min:Sec" or "Hours:Min:Sec" auto-format, with no Lead Zero */
			public String convertTimeMillisecondsAsLongToDurationNoLeadZero(long milliseconds) {
				String hours = String.format("%02d", TimeUnit.MILLISECONDS.toHours(milliseconds));
				String minutes = String.format("%02d",
					   TimeUnit.MILLISECONDS.toMinutes(milliseconds) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milliseconds)));
			    String seconds = String.format("%02d",
			           TimeUnit.MILLISECONDS.toSeconds(milliseconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));
			if (Integer.valueOf(hours) == 0) {
				int min = Integer.valueOf(minutes);
			    if (min != 0) { minutes = String.valueOf(min); }
			    String duration = minutes + ":" + seconds;
			    return duration;
			} else {
			    hours = String.valueOf(Integer.valueOf(hours));
			    String duration = hours + ":" + minutes + ":" + seconds;
			    return duration;
			}          
			}
			
			/** 
			 * Calculates waiting time from "START" till now;
			 * Returnd difference as Double Milliseconds;
			 */
			public Double waitTimeCalculatior(long start) {
				double difference = (System.currentTimeMillis() - start);
				return difference;
			}
			
			/** 
			 * Calculates waiting time from "START" till now;
			 * Returnd difference as Double Seconds;
			 * @throws IOException
			 * @throws NumberFormatException 
			 */
			public String waitTimeConroller(long start) throws NumberFormatException, IOException {
				double sec = waitTimeCalculatior(start)/1000;
				long testStart = convertStringToLong(fileScanner("start.time"));
				int limit = 0;
				
				if ((sec >= 15) && (sec < 30)) { limit = 15; }
				if ((sec >= 30) && (sec < 60)) { limit = 30; }
				if  (sec >= 60)                { limit = 60; }
				
				if (sec >= 15) {
				fileWriterPrinter("Waiting time exceeded limit of " + new DecimalFormat("0.000").format(limit) + " seconds!");
				fileWriter("wait.log", "       Test: #" + fileScanner("test.num"));	
				fileWriter("wait.log", "    Started: "  + convertCalendarMillisecondsAsLongToDateTimeHourMinSec(testStart));
				fileWriter("wait.log", "      Event: "  + convertCalendarMillisecondsAsLongToDateTimeHourMinSec(start));
				fileWriter("wait.log", "   XML Path: "  + fileScanner("xml.path"));
				fileWriter("wait.log", "             Waiting time is " + sec + " sec, which exceeds limit of " + new DecimalFormat("0.000").format(limit) + " seconds!");
				fileWriterPrinter("wait.log", "");
				}
				return padNum(sec);
			}
			
			/** 
			 * Calculates waiting time from "START" till now;
			 * Returnd difference as Double Seconds;
			 * @throws IOException
			 * @throws NumberFormatException 
			 */
			public String waitTimeConroller(long start, String elementName) throws NumberFormatException, IOException {
				double sec = waitTimeCalculatior(start)/1000;
				long testStart = convertStringToLong(fileScanner("start.time"));
				int limit = 0;
				
				if ((sec >= 15) && (sec < 30)) { limit = 15; }
				if ((sec >= 30) && (sec < 60)) { limit = 30; }
				if  (sec >= 60)                { limit = 60; }
				
				if (sec >= 15) {
				fileWriterPrinter("Waiting time exceeded limit of " + new DecimalFormat("0.000").format(limit) + " seconds!");
				fileWriter("wait.log", "       Test: #" + fileScanner("test.num"));	
				fileWriter("wait.log", "    Started: "  + convertCalendarMillisecondsAsLongToDateTimeHourMinSec(testStart));
				fileWriter("wait.log", "      Event: "  + convertCalendarMillisecondsAsLongToDateTimeHourMinSec(start));
				fileWriter("wait.log", "   XML Path: "  + fileScanner("xml.path"));
				fileWriter("wait.log", "             Waiting " + sec + " sec for " + elementName + ", which exceeds limit of " + new DecimalFormat("0.000").format(limit) + " seconds!");
				fileWriterPrinter("wait.log", "");
				}
				return padNum(sec);
			}
			
			/** 
			 * Calculates waiting time from "START" till now;
			 * Returnd difference as Double Seconds;
			 * @throws IOException
			 * @throws NumberFormatException 
			 */
			public String waitTimeConroller(long start, int limit, String elementName) throws NumberFormatException, IOException {
				double sec = waitTimeCalculatior(start)/1000;
				long testStart = convertStringToLong(fileScanner("start.time"));
				
				if ((sec >= limit) && (sec >= 15)) {
				fileWriterPrinter("Waiting time exceeded limit of " + new DecimalFormat("0.000").format(limit) + " seconds!");
				fileWriter("wait.log", "       Test: #" + fileScanner("test.num"));	
				fileWriter("wait.log", "    Started: "  + convertCalendarMillisecondsAsLongToDateTimeHourMinSec(testStart));
				fileWriter("wait.log", "      Event: "  + convertCalendarMillisecondsAsLongToDateTimeHourMinSec(start));
				fileWriter("wait.log", "   XML Path: "  + fileScanner("xml.path"));
				fileWriter("wait.log", "             Waiting " + sec + " sec for " + elementName + ", which exceeds limit of " + new DecimalFormat("0.000").format(limit) + " seconds!");
				fileWriterPrinter("wait.log", "");
				}
				return padNum(sec);
			}
	// ################# TIME CALCULATOR END ##########################	
	
	// ################# RANDOM GENERATOR START #######################
			/** Generate random English Sentence */
			public String randomEnglishText(int Length) throws IOException {
				String s = "", w, space;	
				int length = 0, m;
						while (length < Length) {
							m = randomInt(1, 5);
							w = Dictionary.englishWords[randomInt(0, (Dictionary.englishWords).length - 1)];
							m = randomInt(1, 17);
							if ( ((m/2)*2 == m) && (m > 9) && (m < 13) ) { w = w.substring(0,1).toUpperCase() + w.substring(1, w.length()); }
							if ( m == 13 ) { w = String.valueOf(randomNumber(randomInt(1, 6))); }
							m = randomInt(1, 13);
							space = " ";
							if ( length < (9*(Length/10f)) ) {
							if (m == 3) { space = ", "; }
							if (m == 7) { space = " - "; }
							if (m == 13) { space = ". "; }
							}
							s = s + w + space;
							length = s.length();
							}
						s = s.substring(0, (Length));
		                s = smartDot(s);
		                s = smartUpperCase(s);
		                
						return s;	
					}
			
			    /** Capitalises all letters after 'DOT' following by 'SPASE' */
			    public String smartUpperCase(String s) {
			    	s = s.substring(0, 1).toUpperCase() + s.substring(1, (s.length()));
	                s = s.replace(".", "_");
	                while (s.contains("_ ")) {
	                	s = s.replace( s.substring(s.indexOf("_ "), s.indexOf("_ ") + 3),
	                		           ". " + (s.substring(s.indexOf("_ ") + 2, s.indexOf("_ ") + 3)).toUpperCase()
	                		          );
	                	}
	                s = s.replace("_", ".");
	                return s;
			    }
			    
			    /** Places 'DOT' at the text end */
			    public String smartDot(String s) {
			    	s = s.substring(0, (s.length() - 1)) + "_";
			    	s = s.replace("._", "s.");
	                s = s.replace(" _", "s.");
	                s = s.replace("_", ".");
	                return s;
			    }
			
			    /** Generate random Text (with Word length limitation between minimum and maximum number of chars) */
				public String randomText(int total, int min, int max) {
					int Length, remaining = total;
					String s = null, a = null;
					while (remaining > 0) {
						Length = randomInt(min, max);
						if (max == min) { 
							if (max == 1) { a = "."; }
							if (max > 1) { a = randomString(max - 1) + "."; }
							}
						else { a = randomText(Length) + " "; }
						s = s + a;
						remaining = total - s.length();
						if (remaining < max) { max = remaining; }
						}
                    s = s.substring(0, (total));
                    if ( s.substring((total - 1), (total)).equals(" ") ) {
                    	 s = s.substring(0, (total - 1)) + ".";
                    	 }
                    return s.replaceAll("  ", (" " + randomWord(1))).replace(" .", (randomWord(1) + "."));	
				}
				
				/** Generate random Text (without Word length limitation)*/
				public String randomText(int Length) {
					String s = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz ";
					Random rand = new Random();
					StringBuilder sb = new StringBuilder(Length);
					for (int i = 0; i < Length; i++) {
                         char a = s.charAt(rand.nextInt(s.length()));
                         String c = String.valueOf(a);
						 if ((i == (Length - 1)) && c.equals(" ")) { sb.append("."); } 
						 else  { sb.append(a); }
						 }
					return sb.toString();
				}
				
			    /** Generate random String */
				public String randomString(int Length) {
					String s = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
					Random rand = new Random();
					StringBuilder sb = new StringBuilder(Length);
					for (int i = 0; i < Length; i++) { sb.append(s.charAt(rand.nextInt(s.length()))); }
					return sb.toString();
				}
				
				/** Generate random String from given String */
				public String randomString(int Length, String s) {
					Random rand = new Random();
					StringBuilder sb = new StringBuilder(Length);
					for (int i = 0; i < Length; i++) { sb.append(s.charAt(rand.nextInt(s.length()))); }
					return sb.toString();
				}
				
				/** Generate random Word */
				public String randomWord(int Length) {
					String s = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
					Random rand = new Random();
					StringBuilder sb = new StringBuilder(Length);
					for (int i = 0; i < Length; i++) { sb.append(s.charAt(rand.nextInt(s.length()))); }
					return sb.toString();
				}
				
			    /** Generate random letter */	
				public String randomLetter() {
					   Random rand = new Random();
					   StringBuilder sb = new StringBuilder();
					   char tmp = (char) ('a' + rand.nextInt('z' - 'a'));
					   sb.append(tmp);
					   return sb.toString();
					   }
				
				/** Generate random Number of cert. length */
				public int randomNumber(int Length) {
					String n = "0123456789";
					Random rnd = new Random();
					StringBuilder sb = new StringBuilder(Length);
					for (int i = 0; i < Length; i++) { sb.append(n.charAt(rnd.nextInt(n.length()))); }
					return Integer.valueOf(sb.toString());
				}
				
				/**
				 * Returns a Random Int Number.
				 * @return Integer, a random one, more then 0.
				 */
				public int randomInt() {
				    // NOTE: Usually this should be a field rather than a method
				    // variable so that it is not re-seeded every call.
				    Random rand = new Random();
				    // nextInt is normally exclusive of the top value,
				    // so add 1 to make it inclusive
				    int randomNum = rand.nextInt();
				    return randomNum;
				}
				
				/**
				 * Returns a Random Int Number less then max, inclusive.
				 * The difference between min and max can be at most <code>Integer.MAX_VALUE - 1</code>.
				 *
				 * @param max Maximum value.  Must be greater than 0.
				 * @return Integer between 0 and max, inclusive.
				 */
				public int randomInt(int max) {
				    // NOTE: Usually this should be a field rather than a method
				    // variable so that it is not re-seeded every call.
				    Random rand = new Random();
				    // nextInt is normally exclusive of the top value,
				    // so add 1 to make it inclusive
				    int randomNum = rand.nextInt(max + 1);
				    return randomNum;
				}
				
				/**
				 * Returns a Random Int Number between min and max, inclusive.
				 * The difference between min and max can be at most <code>Integer.MAX_VALUE - 1</code>.
				 *
				 * @param min Minimum value
				 * @param max Maximum value.  Must be greater than min.
				 * @return Integer between min and max, inclusive.
				 */
				public int randomInt(int min, int max) {
				    // NOTE: Usually this should be a field rather than a method
				    // variable so that it is not re-seeded every call.
				    Random rand = new Random();
				    // nextInt is normally exclusive of the top value,
				    // so add 1 to make it inclusive
				    int randomNum = rand.nextInt((max - min) + 1) + min;
				    return randomNum;
				}
				
				public int getRandomNumber(int max){
					Random rnd = new Random();
					return rnd.nextInt(max - 1);	
				}

				public int getRandomNumber(int start, int end){
					Random rnd = new Random();
					int num = rnd.nextInt(end) + start;
					if(num == end) { return (num - 1); }
					return num;	
				}
	// ################# RANDOM GENERATOR END #######################
				
    // ################# WAIT UNTIL ELEMENT START ###################
				public void waitUntilElement(WebDriver driver, final String xpath) throws NumberFormatException, IOException {
					long start = System.currentTimeMillis();
					WebDriverWait wait = new WebDriverWait(driver, 30);
					try {
					ExpectedCondition<Boolean> e = new ExpectedCondition<Boolean>() {
				        public Boolean apply(WebDriver driver) {
				        return (driver.findElements(By.xpath(xpath)).size() > 0);
				        }
				    };
				    wait.until(e);}
					catch(Exception e) {
					if (driver.findElements(By.xpath(xpath)).size() == 0) { fileWriterPrinter("\nElement not found!\nXPATH: " + xpath); }
					}
				    fileWriterPrinter("Waiting time for Element: " + waitTimeConroller(start, 30, xpath) + " sec\n");
				}
	
				public void waitUntilElement(WebDriver driver, int seconds, final String xpath) throws NumberFormatException, IOException {
					long start = System.currentTimeMillis();
					WebDriverWait wait = new WebDriverWait(driver, seconds);
					try {
					ExpectedCondition<Boolean> e = new ExpectedCondition<Boolean>() {
				        public Boolean apply(WebDriver driver) {
				        return (driver.findElements(By.xpath(xpath)).size() > 0);
				        }
				    };
				    wait.until(e);}
					catch(Exception e) {
					if (driver.findElements(By.xpath(xpath)).size() == 0) { fileWriterPrinter("\nElement not found!\nXPATH: " + xpath); }
					}
				    fileWriterPrinter("Waiting time for Element: " + waitTimeConroller(start, seconds, xpath) + " sec\n");
				}

				public void waitUntilElement(WebDriver driver, int seconds, final String xpath, String elementName) throws NumberFormatException, IOException {
					long start = System.currentTimeMillis();
					WebDriverWait wait = new WebDriverWait(driver, seconds);
					try {
					ExpectedCondition<Boolean> e = new ExpectedCondition<Boolean>() {
				        public Boolean apply(WebDriver driver) {
				        return (driver.findElements(By.xpath(xpath)).size() > 0);
				        }
				    };
				    wait.until(e);}
					catch(Exception e) {
					if (driver.findElements(By.xpath(xpath)).size() == 0) {
					    fileWriterPrinter("\n" + elementName + " not found!\nXPATH: " + xpath);
					   }
					}
				    fileWriterPrinter("Waiting time for " + elementName + ": " + waitTimeConroller(start, seconds, elementName) + " sec\n");
				}
	// ################# WAIT UNTIL ELEMENT END #####################
				
    // ################# WAIT UNTIL ELEMENT APPEARS START ###################
				public Boolean waitUntilElementAppears(WebDriver driver, final String xpath) throws NumberFormatException, IOException {
					long start = System.currentTimeMillis();
					WebDriverWait wait = new WebDriverWait(driver, 30);
					try {
					ExpectedCondition<Boolean> e = new ExpectedCondition<Boolean>() {
				        public Boolean apply(WebDriver driver) {				        	
				        return (driver.findElements(By.xpath(xpath)).size() == 0);
				        }
				    };
				    wait.until(e);}
					catch(Exception e) {
					if (driver.findElements(By.xpath(xpath)).size() == 0) { fileWriterPrinter("\nElement not found!\nXPATH: " + xpath); }
					}
				    fileWriterPrinter("Waiting time for Element    appearence: " + waitTimeConroller(start, 30, xpath) + " sec");
				    return (driver.findElements(By.xpath(xpath)).size() == 0);
				}
				
				public Boolean waitUntilElementAppears(WebDriver driver, int seconds, final String xpath) throws NumberFormatException, IOException {
					long start = System.currentTimeMillis();
					WebDriverWait wait = new WebDriverWait(driver, seconds);
					try {
					ExpectedCondition<Boolean> e = new ExpectedCondition<Boolean>() {
				        public Boolean apply(WebDriver driver) {
				        return (driver.findElements(By.xpath(xpath)).size() == 0);
				        }
				    };
				    wait.until(e);}
					catch(Exception e) {
					if (driver.findElements(By.xpath(xpath)).size() == 0) { fileWriterPrinter("\nElement not found!\nXPATH: " + xpath); }
					}
				    fileWriterPrinter("Waiting time for Element    appearence: " + waitTimeConroller(start, seconds, xpath) + " sec");
				    return (driver.findElements(By.xpath(xpath)).size() > 0);
				}

				public Boolean waitUntilElementAppears(WebDriver driver, int seconds, final String xpath, String elementName) throws NumberFormatException, IOException {
					long start = System.currentTimeMillis();
					WebDriverWait wait = new WebDriverWait(driver, seconds);
					try {
					ExpectedCondition<Boolean> e = new ExpectedCondition<Boolean>() {
				        public Boolean apply(WebDriver driver) {
				        return (driver.findElements(By.xpath(xpath)).size() == 0);
				        }
				    };
				    wait.until(e); }
					catch(Exception e) {
					if (driver.findElements(By.xpath(xpath)).size() == 0) { fileWriterPrinter("\n" + elementName + " not found!\nXPATH: " + xpath); }
					}
				    fileWriterPrinter("Waiting time for " + elementName + "    appearence: " + waitTimeConroller(start, seconds, elementName) + " sec");
				    return (driver.findElements(By.xpath(xpath)).size() > 0);
				}
	// ################# WAIT UNTIL ELEMENT APPEARS END #####################
	
	// ################# WAIT UNTIL ELEMENT DISAPPEARS START ###################
				public void waitUntilElementDisappears(WebDriver driver, final String xpath) throws NumberFormatException, IOException {
					long start = System.currentTimeMillis();
					WebDriverWait wait = new WebDriverWait(driver, 30);
					try {
					ExpectedCondition<Boolean> e = new ExpectedCondition<Boolean>() {
				        public Boolean apply(WebDriver driver) {
				        return (driver.findElements(By.xpath(xpath)).size() > 0);
				        }
				    };
				    wait.until(e);}
					catch(Exception e) {
					if (driver.findElements(By.xpath(xpath)).size() > 0) { fileWriterPrinter("\nElement is still visible!\nXPATH: " + xpath); }
					}
				    fileWriterPrinter("Waiting time for Element disappearence: " + waitTimeConroller(start, 30, xpath) + " sec\n");
				}
	
				public void waitUntilElementDisappears(WebDriver driver, int seconds, final String xpath) throws NumberFormatException, IOException {
					long start = System.currentTimeMillis();
					WebDriverWait wait = new WebDriverWait(driver, seconds);
					try {
					ExpectedCondition<Boolean> e = new ExpectedCondition<Boolean>() {
				        public Boolean apply(WebDriver driver) {
				        return (driver.findElements(By.xpath(xpath)).size() > 0);
				        }
				    };
				    wait.until(e);}
					catch(Exception e) {
					if (driver.findElements(By.xpath(xpath)).size() > 0) { fileWriterPrinter("\nElement is still visible!\nXPATH: " + xpath); }
					}
				    fileWriterPrinter("Waiting time for Element disappearence: " + waitTimeConroller(start, seconds, xpath) + " sec\n");
				}
				
				public void waitUntilElementDisappears(WebDriver driver, int seconds, final String xpath, String elementName) throws NumberFormatException, IOException {
					long start = System.currentTimeMillis();
					WebDriverWait wait = new WebDriverWait(driver, seconds);
					try {
					ExpectedCondition<Boolean> e = new ExpectedCondition<Boolean>() {
				        public Boolean apply(WebDriver driver) {
				        return (driver.findElements(By.xpath(xpath)).size() > 0);
				        }
				    };
				    wait.until(e);}
					catch(Exception e) {
					if (driver.findElements(By.xpath(xpath)).size() > 0) { fileWriterPrinter("\n" + elementName + " is still visible!\nXPATH: " + xpath); }
					}
				    fileWriterPrinter("Waiting time for " + elementName + " disappearence: " + waitTimeConroller(start, seconds, elementName) + " sec\n");
				}

				public void waitUntilElementDisappears(WebDriver driver, int seconds, final String xpath, String elementName, StackTraceElement t) throws IOException {
					long start = System.currentTimeMillis();
					WebDriverWait wait = new WebDriverWait(driver, seconds);
					try {
					ExpectedCondition<Boolean> e = new ExpectedCondition<Boolean>() {
				        public Boolean apply(WebDriver driver) {
				        return (driver.findElements(By.xpath(xpath)).size() > 0);
				        }
				    };
				    wait.until(e);}
					catch(Exception e) {
					if (driver.findElements(By.xpath(xpath)).size() > 0) { 
						fileWriterPrinter("\n" + elementName + " is still visible!\nXPATH: " + xpath);
						getScreenShot(t, elementName.replace("\"", "''") + " visibility Time-Out", driver);
						}
					}
				    fileWriterPrinter("Waiting time for " + elementName + " disappearence: " + waitTimeConroller(start, seconds, elementName) + " sec\n");
				}
	// ################# WAIT UNTIL ELEMENT DISAPPEARS END #####################

	// ################# WAIT UNTIL ELEMENT PRESENTS START #####################
				public Boolean waitUntilElementPresence(WebDriver driver, int seconds, final String xpath, String elementName, StackTraceElement t) throws IOException {
					long start = System.currentTimeMillis();
					WebDriverWait wait = new WebDriverWait(driver, seconds);
					try { wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath))); }
					catch(Exception e) {
					if (driver.findElements(By.xpath(xpath)).size() == 0) { fileWriterPrinter("\n" + elementName + " not found!\nXPATH: " + xpath); }
					getScreenShot(t, elementName.replace("\"", "''") + " presence Time-Out", driver);
					}
				    fileWriterPrinter("Waiting time for " + padRight(elementName, 30 - elementName.length()) + " presence: " + waitTimeConroller(start, seconds, elementName) + " sec");
				    return (driver.findElements(By.xpath(xpath)).size() > 0);
				}
				
				public Boolean waitUntilElementPresence(WebDriver driver, int seconds, final By element, String elementName, StackTraceElement t) throws IOException {
					long start = System.currentTimeMillis();					
					// SOURCE EXAMPLE of "element" VARIABLE ENTRY:     By element = By.xpath("xpath string");     By element = By.id("id string")   etc.	
					WebDriverWait wait = new WebDriverWait(driver, seconds);
					List<WebElement> list = driver.findElements(element);					
					try { wait.until(ExpectedConditions.presenceOfElementLocated(element)); }										
					catch(Exception e) {
					if (list.size() == 0) { fileWriterPrinter("\n" + elementName + " not found!\n\"By\" ELEMENT: " + element); }
					getScreenShot(t, elementName.replace("\"", "''") + " presence Time-Out", driver);
					}
				    fileWriterPrinter("Waiting time for " + padRight(elementName, 30 - elementName.length()) + " presence: " + waitTimeConroller(start, seconds, elementName) + " sec");
				    return (list.size() > 0);
				}
				
				public Boolean waitUntilElementPresence(WebDriver driver, int seconds, final By element, String elementName, StackTraceElement t, Boolean ifScreenshot) throws IOException {
					long start = System.currentTimeMillis();					
					// SOURCE EXAMPLE of "element" VARIABLE ENTRY:     By element = By.xpath("xpath string");     By element = By.id("id string")   etc.	
					WebDriverWait wait = new WebDriverWait(driver, seconds);
					List<WebElement> list = driver.findElements(element);					
					try { wait.until(ExpectedConditions.presenceOfElementLocated(element)); }										
					catch(Exception e) {
					if (list.size() == 0) { fileWriterPrinter("\n" + elementName + " not found!\n\"By\" ELEMENT: " + element); }
					if (ifScreenshot) { getScreenShot(t, elementName.replace("\"", "''") + " presence Time-Out", driver); }
					}
				    fileWriterPrinter("Waiting time for " + padRight(elementName, 30 - elementName.length()) + " presence: " + waitTimeConroller(start, seconds, elementName) + " sec");
				    return (list.size() > 0);
				}
				
				public Boolean waitUntilElementPresence(WebDriver driver, int seconds, final ByAll locator, String elementName, StackTraceElement t) throws IOException {
					long start = System.currentTimeMillis();
					// SOURCE EXAMPLE of "locator" VARIABLE ENTRY:     ByAll locator = (ByAll) By.xpath("xpath string")     ByAll locator = (ByAll) By.id("id string")   etc.					
					WebDriverWait wait = new WebDriverWait(driver, seconds);
					List<WebElement> list = driver.findElements(locator);					
					try { wait.until(ExpectedConditions.presenceOfElementLocated(locator)); }										
					catch(Exception e) {
					if (list.size() == 0) { fileWriterPrinter("\n" + elementName + " not found!\n\"ByAll\" LOCATOR: " + locator); }
					getScreenShot(t, elementName.replace("\"", "''") + " presence Time-Out", driver);
					}
				    fileWriterPrinter("Waiting time for " + padRight(elementName, 30 - elementName.length()) + " presence: " + waitTimeConroller(start, seconds, elementName) + " sec");
				    return (list.size() > 0);
				}
	// ################# WAIT UNTIL ELEMENT PRESENTS END #######################
				
	// ################# WAIT UNTIL ELEMENT VISIBLE START ######################
				public void waitUntilElementVisibility(WebDriver driver, int seconds, final String xpath, String elementName, StackTraceElement t) throws IOException {
					long start = System.currentTimeMillis();
					WebDriverWait wait = new WebDriverWait(driver, seconds);
					try { wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath))); }
					catch(Exception e) {
						if (driver.findElements(By.xpath(xpath)).size() == 0) { 
							fileWriterPrinter("\n" + elementName + " is still not visible!\nXPATH: " + xpath);
							getScreenShot(t, elementName.replace("\"", "''") + " visibility Time-Out", driver);
							}
						}
					    fileWriterPrinter("Waiting time for " + padRight(elementName, 28 - elementName.length()) + " visibility: " + waitTimeConroller(start, seconds, elementName) + " sec");
					}

				public void waitUntilElementVisibility(WebDriver driver, int seconds, final By element, String elementName, StackTraceElement t) throws IOException {
					long start = System.currentTimeMillis();
					// SOURCE EXAMPLE of "element" VARIABLE ENTRY:     By element = By.xpath("xpath string");     By element = By.id("id string")   etc.
					WebDriverWait wait = new WebDriverWait(driver, seconds);
					try { wait.until(ExpectedConditions.visibilityOfElementLocated(element)); }
					catch(Exception e) {
						if (driver.findElements(element).size() == 0) { 
							fileWriterPrinter("\n" + elementName + " is still not visible!\n\"By\" ELEMENT: " + element);
							getScreenShot(t, elementName.replace("\"", "''") + " visibility Time-Out", driver);
							}
						}
					    fileWriterPrinter("Waiting time for " + padRight(elementName, 28 - elementName.length()) + " visibility: " + waitTimeConroller(start, seconds, elementName) + " sec");
					}
				
				public void waitUntilElementVisibility(WebDriver driver, int seconds, final ByAll locator, String elementName, StackTraceElement t) throws IOException {
					long start = System.currentTimeMillis();
					// SOURCE EXAMPLE of "locator" VARIABLE ENTRY:     ByAll locator = (ByAll) By.xpath("xpath string")     ByAll locator = (ByAll) By.id("id string")   etc.
					WebDriverWait wait = new WebDriverWait(driver, seconds);
					try { wait.until(ExpectedConditions.visibilityOfElementLocated(locator)); }
					catch(Exception e) {
						if (driver.findElements(locator).size() == 0) { 
							fileWriterPrinter("\n" + elementName + " is still not visible!\n\"ByAll\" LOCATOR: " + locator);
							getScreenShot(t, elementName.replace("\"", "''") + " visibility Time-Out", driver);
							}
						}
					    fileWriterPrinter("Waiting time for " + padRight(elementName, 28 - elementName.length()) + " visibility: " + waitTimeConroller(start, seconds, elementName) + " sec");
					}
	// ################# WAIT UNTIL ELEMENT VISIBLE END #########################
				
	// ################# WAIT UNTIL ELEMENT INVISIBLE START #####################
				public void waitUntilElementInvisibility(WebDriver driver, int seconds, final String xpath, String elementName, StackTraceElement t) throws IOException {
					long start = System.currentTimeMillis();
					WebDriverWait wait = new WebDriverWait(driver, seconds);
					try { wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(xpath))); }
					catch(Exception e) {
						if (driver.findElements(By.xpath(xpath)).size() > 0) { 
							fileWriterPrinter("\n" + elementName + " is still visible!\nXPATH: " + xpath);
							getScreenShot(t, elementName.replace("\"", "''") + " invisibility Time-Out", driver);
							}
						}
					    fileWriterPrinter("Waiting time for " + padRight(elementName, 26 - elementName.length()) + " invisibility: " + waitTimeConroller(start, seconds, elementName) + " sec");
					}

				public void waitUntilElementInvisibility(WebDriver driver, int seconds, final By element, String elementName, StackTraceElement t) throws IOException {
					long start = System.currentTimeMillis();
					// SOURCE EXAMPLE of "element" VARIABLE ENTRY:     By element = By.xpath("xpath string");     By element = By.id("id string")   etc.
					WebDriverWait wait = new WebDriverWait(driver, seconds);
					try { wait.until(ExpectedConditions.invisibilityOfElementLocated(element)); }
					catch(Exception e) {
						if (driver.findElements(element).size() > 0) { 
							fileWriterPrinter("\n" + elementName + " is still visible!\n\"By\" ELEMENT: " + element);
							getScreenShot(t, elementName.replace("\"", "''") + " invisibility Time-Out", driver);
							}
						}
					    fileWriterPrinter("Waiting time for " + padRight(elementName, 26 - elementName.length()) + " invisibility: " + waitTimeConroller(start, seconds, elementName) + " sec");
					}
				
				public void waitUntilElementInvisibility(WebDriver driver, int seconds, final ByAll locator, String elementName, StackTraceElement t) throws IOException {
					long start = System.currentTimeMillis();
					// SOURCE EXAMPLE of "locator" VARIABLE ENTRY:     ByAll locator = (ByAll) By.xpath("xpath string")     ByAll locator = (ByAll) By.id("id string")   etc.
					WebDriverWait wait = new WebDriverWait(driver, seconds);
					try { wait.until(ExpectedConditions.invisibilityOfElementLocated(locator)); }
					catch(Exception e) {
						if (driver.findElements(locator).size() > 0) { 
							fileWriterPrinter("\n" + elementName + " is still visible!\n\"ByAll\" LOCATOR: " + locator);
							getScreenShot(t, elementName.replace("\"", "''") + " invisibility Time-Out", driver);
							}
						}
					    fileWriterPrinter("Waiting time for " + padRight(elementName, 26 - elementName.length()) + " invisibility: " + waitTimeConroller(start, seconds, elementName) + " sec");
					}
	// ################# WAIT UNTIL ELEMENT INVISIBLE END #######################
				
	// ################# WAIT UNTIL ELEMENT APPEARS AND DISAPPEAR START #####################
				public void waitUntilElementAppearsAndDisappears(WebDriver driver, int secondsApp, int secondsDis, final String xpath, String elementName, StackTraceElement t) throws IOException {
					Boolean a = waitUntilElementAppears(driver, secondsApp, xpath, elementName);
					if (a) { waitUntilElementDisappears(driver, secondsDis, xpath, elementName, t); }
				}				
	// ################# WAIT UNTIL ELEMENT APPEARS AND DISAPPEAR END #######################
				
	// ################# WAIT UNTIL ELEMENT PRESENTS AND INVISIBLE START #####################
				public void waitUntilElementPresenceAndInvisible(WebDriver driver, int secondsPre, int secondsInv, final String xpath, String elementName, StackTraceElement t) throws IOException {
					Boolean p = waitUntilElementPresence(driver, secondsPre, xpath, elementName, t);
					if (p) { waitUntilElementInvisibility(driver, secondsInv, xpath, elementName, t); }
				}				
	// ################# WAIT UNTIL ELEMENT PRESENTS AND INVISIBLE END #######################				

	// ################# WAIT UNTIL ELEMENT LIST START #################
				public List<WebElement>  waitUntilElementList(WebDriver driver, final String xpath) throws NumberFormatException, IOException, InterruptedException {
					long start = System.currentTimeMillis();
					if (driver.findElements(By.xpath(xpath)).size() == 0) {
					WebDriverWait wait = new WebDriverWait(driver, 30);					
					try { wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath))); }
					catch(Exception e) {
					if (driver.findElements(By.xpath(xpath)).size() == 0) { fileWriterPrinter("\nList of Elements is empty!\nXPATH: " + xpath); }
					}
					}
				    fileWriterPrinter("Waiting time for List of Elements:" + padSpace(54 - "Waiting time for List of Elements:".length()) + waitTimeConroller(start, 30, xpath) + " sec");
				    return (driver.findElements(By.xpath(xpath)));
				}
	
				public List<WebElement>  waitUntilElementList(WebDriver driver, int seconds, final String xpath) throws NumberFormatException, IOException, InterruptedException {
					long start = System.currentTimeMillis();
					if (driver.findElements(By.xpath(xpath)).size() == 0) {
					WebDriverWait wait = new WebDriverWait(driver, seconds);					
					try { wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath))); }
					catch(Exception e) {
					if (driver.findElements(By.xpath(xpath)).size() == 0) { fileWriterPrinter("\nList of Elements is empty!\nXPATH: " + xpath); }
					}
					}
					fileWriterPrinter("Waiting time for List of Elements:" + padSpace(54 - "Waiting time for List of Elements:".length()) + waitTimeConroller(start, seconds, xpath) + " sec");
				    return (driver.findElements(By.xpath(xpath)));
				}
				
				public List<WebElement>  waitUntilElementList(WebDriver driver, int seconds, final String xpath, String elementListName) throws NumberFormatException, IOException, InterruptedException {
					long start = System.currentTimeMillis();
					if (driver.findElements(By.xpath(xpath)).size() == 0) {
					WebDriverWait wait = new WebDriverWait(driver, seconds);					
					try { wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath))); }
					catch(Exception e) {
					if (driver.findElements(By.xpath(xpath)).size() == 0) { fileWriterPrinter("\nList of " + elementListName + " is empty!\nXPATH: " + xpath); }
					}
					}
				    fileWriterPrinter("Waiting time for List of " + elementListName + ":" + padSpace(32 - elementListName.length()) + waitTimeConroller(start, seconds, elementListName) + " sec");
				    return (driver.findElements(By.xpath(xpath)));				    
				}
				
				public List<WebElement>  waitUntilElementList(WebDriver driver, int seconds, final String xpath, String elementListName, StackTraceElement t) throws NumberFormatException, IOException, InterruptedException {
					long start = System.currentTimeMillis();
					if (driver.findElements(By.xpath(xpath)).size() == 0) {
					WebDriverWait wait = new WebDriverWait(driver, seconds);					
					try { wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath))); }
					catch(Exception e) {
					if (driver.findElements(By.xpath(xpath)).size() == 0) {
						fileWriterPrinter("\nList of " + elementListName + " is empty!\nXPATH: " + xpath);
						getScreenShot(t, "List of " + elementListName.replace("\"", "''") + " is empty!", driver);
						}
					}
					}
				    fileWriterPrinter("Waiting time for List of " + elementListName + "s:" + padSpace(31 - elementListName.length()) + waitTimeConroller(start, seconds, elementListName) + " sec");
				    return (driver.findElements(By.xpath(xpath)));				    
				}
	// ################# WAIT UNTIL ELEMENT LIST END ###################
		
    // ################# WAIT UNTIL URL CHANGE START #################
				public void waitUntilUrl(WebDriver driver, String previousURL) throws NumberFormatException, IOException {
					long start = System.currentTimeMillis();
					final String oldURL = previousURL.toLowerCase();
					WebDriverWait wait = new WebDriverWait(driver, 30);
					ExpectedCondition<Boolean> e = new ExpectedCondition<Boolean>() {
				        public Boolean apply(WebDriver driver) {
				        return (driver.getCurrentUrl() != oldURL);
				        }
				    };
				    wait.until(e);
				    fileWriterPrinter("Waiting time for New URL:" + padSpace(58 - "Waiting time for New URL:".length()) + waitTimeConroller(start, 30, driver.getCurrentUrl()) + " sec");
				}
				
				public void waitUntilUrl(WebDriver driver, int seconds, String previousURL) throws NumberFormatException, IOException {
					long start = System.currentTimeMillis();
					final String oldURL = previousURL.toLowerCase();
					fileWriterPrinter("\nOld URL: " + oldURL);
					WebDriverWait wait = new WebDriverWait(driver, seconds);
					ExpectedCondition<Boolean> e = new ExpectedCondition<Boolean>() {
				        public Boolean apply(WebDriver driver) {
				        return (driver.getCurrentUrl() != oldURL);
				        }
				    };
				    wait.until(e);
				    fileWriterPrinter("New URL: " + driver.getCurrentUrl());
				    fileWriterPrinter("Waiting time for New URL:" + padSpace(58 - "Waiting time for New URL:".length()) + waitTimeConroller(start, seconds, driver.getCurrentUrl()) + " sec");
				}
				
				public void waitUntilUrl(WebDriver driver, int seconds, String previousURL, Boolean ifPrompt) throws NumberFormatException, IOException {
					long start = System.currentTimeMillis();
					final String oldURL = previousURL.toLowerCase();
					if (ifPrompt) { fileWriterPrinter("\nOld URL: " + oldURL); }
					WebDriverWait wait = new WebDriverWait(driver, seconds);
					ExpectedCondition<Boolean> e = new ExpectedCondition<Boolean>() {
				        public Boolean apply(WebDriver driver) {
				        return (driver.getCurrentUrl() != oldURL);
				        }
				    };
				    wait.until(e);
				    if (ifPrompt) { 
				        fileWriterPrinter("New URL: " + driver.getCurrentUrl());
				        fileWriterPrinter("Waiting time for New URL:" + padSpace(58 - "Waiting time for New URL:".length()) + waitTimeConroller(start, seconds, driver.getCurrentUrl()) + " sec");
				    }
				}
	// ################# WAIT UNTIL URL CHANGE END #################
				
	// ################# GET WEB PAGE SOURCE CODE START ############
				public String getUrlSourcePage(String url) throws IOException {
			        URL URL = new URL(url);
			        URLConnection uc = URL.openConnection();
			        
			     // allow GZip encodings  
			     // the encoding type
			        BufferedReader in = null;
			        if (uc.getHeaderField("Content-Encoding") != null && uc.getHeaderField("Content-Encoding").equals("gzip")) {
			            in = new BufferedReader(new InputStreamReader(new GZIPInputStream(uc.getInputStream())));
			        } else { in = new BufferedReader(new InputStreamReader(uc.getInputStream())); }
			        
			        String inputLine;
			        StringBuilder sb = new StringBuilder();
			        while ((inputLine = in.readLine()) != null)
			        sb.append(inputLine);
			        in.close();
			        
			        return sb.toString();
			    }

				@SuppressWarnings("static-access")
				public String getUrlSourcePagePrint(String url) throws IOException {
				        URL URL = new URL(url);				        			        
				        HttpURLConnection uc = (HttpURLConnection) URL.openConnection(); // Cast shouldn't fail
				        uc.setFollowRedirects(true);
				        
				     // allow both GZip and Deflate (ZLib) encodings
				        uc.setRequestProperty("Accept-Encoding", "gzip, deflate");
				        String encoding = uc.getContentEncoding();
				        
				     // the encoding type
				        BufferedReader in = null;
				        if (encoding != null && encoding.equalsIgnoreCase("gzip")) {
				            in = new BufferedReader(new InputStreamReader(new GZIPInputStream(uc.getInputStream())));
				        } else if (encoding != null && encoding.equalsIgnoreCase("deflate")) {
				            in = new BufferedReader(new InputStreamReader(new InflaterInputStream(uc.getInputStream(), new Inflater(true))));
				        } else { in = new BufferedReader(new InputStreamReader(uc.getInputStream())); }
				        
				        String inputLine;
				        StringBuilder sb = new StringBuilder();
				        while ((inputLine = in.readLine()) != null)
				        sb.append(inputLine);
				        in.close();
				        
			            fileCleaner("source.html");
			            fileWriterPrinter("source.html", sb.toString());
			            
				        return sb.toString();
				}
				
				@SuppressWarnings("static-access")
				public String getUrlSourcePagePrint(String url, String filename) throws IOException {
				        URL URL = new URL(url);				        			        
				        HttpURLConnection uc = (HttpURLConnection) URL.openConnection(); // Cast shouldn't fail
				        uc.setFollowRedirects(true);
				        
				     // allow both GZip and Deflate (ZLib) encodings
				        uc.setRequestProperty("Accept-Encoding", "gzip, deflate");
				        String encoding = uc.getContentEncoding();
				        
				     // the encoding type
				        BufferedReader in = null;
				        if (encoding != null && encoding.equalsIgnoreCase("gzip")) {
				            in = new BufferedReader(new InputStreamReader(new GZIPInputStream(uc.getInputStream())));
				        } else if (encoding != null && encoding.equalsIgnoreCase("deflate")) {
				            in = new BufferedReader(new InputStreamReader(new InflaterInputStream(uc.getInputStream(), new Inflater(true))));
				        } else { in = new BufferedReader(new InputStreamReader(uc.getInputStream())); }
				        
				        String inputLine;
				        StringBuilder sb = new StringBuilder();
				        while ((inputLine = in.readLine()) != null)
				        sb.append(inputLine);
				        in.close();
				        
			            fileCleaner(filename);
			            fileWriterPrinter(filename, sb.toString());
			            
				        return sb.toString();
				}
				
				@SuppressWarnings("static-access")
				public String getUrlSourcePagePrint(String url, String path, String fileName) throws IOException {
				        URL URL = new URL(url);				        			        
				        HttpURLConnection uc = (HttpURLConnection) URL.openConnection(); // Cast shouldn't fail
				        uc.setFollowRedirects(true);
				        
				     // allow both GZip and Deflate (ZLib) encodings
				        uc.setRequestProperty("Accept-Encoding", "gzip, deflate");
				        String encoding = uc.getContentEncoding();
				        
				     // the encoding type
				        BufferedReader in = null;
				        if (encoding != null && encoding.equalsIgnoreCase("gzip")) {
				            in = new BufferedReader(new InputStreamReader(new GZIPInputStream(uc.getInputStream())));
				        } else if (encoding != null && encoding.equalsIgnoreCase("deflate")) {
				            in = new BufferedReader(new InputStreamReader(new InflaterInputStream(uc.getInputStream(), new Inflater(true))));
				        } else { in = new BufferedReader(new InputStreamReader(uc.getInputStream())); }
				        
				        String inputLine;
				        StringBuilder sb = new StringBuilder();
				        while ((inputLine = in.readLine()) != null)
				        sb.append(inputLine);
				        in.close();
				        
			            fileCleaner(fileName);
			            fileWriterPrinter(path, fileName, sb.toString());
			            
				        return sb.toString();
				}
				
				@SuppressWarnings("static-access")
				public String getUrlSourcePagePrint(String url, String path, String fileName, String extention) throws IOException {
				        URL URL = new URL(url);				        			        
				        HttpURLConnection uc = (HttpURLConnection) URL.openConnection(); // Cast shouldn't fail
				        uc.setFollowRedirects(true);
				        
				     // allow both GZip and Deflate (ZLib) encodings
				        uc.setRequestProperty("Accept-Encoding", "gzip, deflate");
				        String encoding = uc.getContentEncoding();
				        
				     // the encoding type
				        BufferedReader in = null;
				        if (encoding != null && encoding.equalsIgnoreCase("gzip")) {
				            in = new BufferedReader(new InputStreamReader(new GZIPInputStream(uc.getInputStream())));
				        } else if (encoding != null && encoding.equalsIgnoreCase("deflate")) {
				            in = new BufferedReader(new InputStreamReader(new InflaterInputStream(uc.getInputStream(), new Inflater(true))));
				        } else { in = new BufferedReader(new InputStreamReader(uc.getInputStream())); }
				        
				        String inputLine;
				        StringBuilder sb = new StringBuilder();
				        while ((inputLine = in.readLine()) != null)
				        sb.append(inputLine);
				        in.close();
				        
			            fileCleaner(fileName);
			            fileWriterPrinter(path, (fileName + "." + extention), sb.toString());
			            
				        return sb.toString();
				}
	// ################# GET WEB PAGE SOURCE CODE END ###################
				
	// ################# CLICK LINK AND WAIT UNTIL URL CHANGE START ################
	            /**
	             * In current page it will click a link/tab/locator and wait until url opens
	             * @throws IOException
	             */
				public void clickLinkUrlWaitUntil(WebDriver driver, int seconds, String locator) throws IOException{    	
					final String previousURL = driver.getCurrentUrl();	  	
					driver.findElement(By.xpath(locator)).click();		  	
					waitUntilUrl(driver, seconds, previousURL);
					}
				
	            /**
	             * In current page it will click a link/tab/locator and wait until url opens with exception trace control
	             * @throws IOException
	             */
				public void clickLinkUrlWaitUntil(WebDriver driver, int seconds, String locator, StackTraceElement t) throws IOException{    
					try {	
						final String previousURL = driver.getCurrentUrl();	  
						driver.findElement(By.xpath(locator)).click();		  
						waitUntilUrl(driver, seconds, previousURL);					
					} catch(Exception e) { getExceptionDescriptive(e, t, driver); }
				}			
    // ################# CLICK LINK AND WAIT UNTIL URL CHANGE END ################



	// ################# GET URL WAIT UNTIL CHANGE START ################							
				public void getUrlWaitUntil(WebDriver driver, int seconds, String newURL) throws NumberFormatException, IOException {
					final String previousURL = driver.getCurrentUrl();
					final String URL = newURL.toLowerCase();
					
				// First (initial) connection:
					int i = 1;
					int s = 1;
				    // driver.get(URL);
				    // waitUntilUrl(driver, seconds, previousURL);
					long start = System.currentTimeMillis();
					
				 // "Try Again" connection manager, if required (up to 10 times):					
					String xpathTryAgainButton = "//*[contains(@id,'error')][@id='errorTryAgain'][text()='Try Again']";					
					// List<WebElement> list; int s = list.size(); int s = driver.findElements(By.xpath(xpathTryAgainButton)).size();					
					while ((s > 0) && (i <= 10)) {
						   driver.get(URL);
						   waitUntilUrl(driver, seconds, previousURL);					   
						   s = driver.findElements(By.xpath(xpathTryAgainButton)).size();
						   if (s > 0) { i = i + 1; }
					}	
					if (i > 1) {
					            fileWriterPrinter("Try Again   attempts: " + i);
				                fileWriterPrinter("Try Again time spent: " + waitTimeConroller(start, seconds, newURL) + " sec\n");
					            }	
				}
				
				public void getUrlWaitUntil(WebDriver driver, int seconds, String newURL, Boolean ifPrompt) throws NumberFormatException, IOException {
					final String previousURL = driver.getCurrentUrl();
					final String URL = newURL.toLowerCase();
					
				// First (initial) connection:
					int i = 1;
					int s = 1;
				    // driver.get(URL);
				    // waitUntilUrl(driver, seconds, previousURL);
					long start = System.currentTimeMillis();
					
				 // "Try Again" connection manager, if required (up to 10 times):					
					String xpathTryAgainButton = "//*[contains(@id,'error')][@id='errorTryAgain'][text()='Try Again']";					
					// List<WebElement> list; int s = list.size(); int s = driver.findElements(By.xpath(xpathTryAgainButton)).size();					
					while ((s > 0) && (i <= 10)) {
						   driver.get(URL);
						   waitUntilUrl(driver, seconds, previousURL, ifPrompt);					   
						   s = driver.findElements(By.xpath(xpathTryAgainButton)).size();
						   if (s > 0) { i = i + 1; }
					}	
					if (i > 1) {
					            fileWriterPrinter("Try Again   attempts: " + i);
				                fileWriterPrinter("Try Again time spent: " + waitTimeConroller(start, seconds, newURL) + " sec\n");
					            }	
				}
    // ################# GET URL WAIT UNTIL CHANGE END #################			
				
	/* ##### WAIT ATTRIBUTE START ##### */
	public void waitForAttributeChanged(WebDriver driver, By locator, String attr, String initialValue) {
		WebDriverWait wait = new WebDriverWait(driver, 15);

		wait.until(new ExpectedCondition<Boolean>() {
			private By locator;
			private String attr;
			private String initialValue;

			private ExpectedCondition<Boolean> init(By locator, String attr, String initialValue) {
				this.locator = locator;
				this.attr = attr;
				this.initialValue = initialValue;
				return this;
			}

			public Boolean apply(WebDriver driver) {
				// WebElement button = driver.findElement(this.locator);
				// String enabled = button.getAttribute(this.attr);
				String enabled = driver.findElement(this.locator).getAttribute(
						this.attr);
				if (enabled.equals(this.initialValue))
					return false;
				else
					return true;
			}
		}.init(locator, attr, initialValue));
	}
	/* ##### WAIT ATTRIBUTE END ##### */

	/* ##### DOUBLE-CLICK START ##### */
	public void doubleClick(WebDriver driver, WebElement element) throws NumberFormatException, IOException {
		try {
			Actions action = new Actions(driver).doubleClick(element);
			action.build().perform();
			fileWriterPrinter("Double-Clicked the element: " + "\n" +
			                   "                      Type: " + element.getAttribute("type") + "\n" +
			                   "                      Name: \"" + element.getText() + "\"");
			
		} catch(StaleElementReferenceException e) {
			fileWriterPrinter("Element is not attached to the page document " + e.getStackTrace());
			
		} catch(NoSuchElementException e) {
			fileWriterPrinter("Element " + element + " was not found in DOM " + e.getStackTrace());
			
		} catch(Exception e) {
			fileWriterPrinter("Element " + element + " was not clickable "
					+ e.getStackTrace());
		}
	}
	/* ##### DOUBLE-CLICK END ##### */
	
	/* ##### WINDOW SWITCH HANDLER WITH CLICK START ##### */
	public String clickHandleWindow(WebDriver driver, String handle, String xpath) throws NumberFormatException, IOException {
     // Accessing current web address:
		String currentURL = driver.getCurrentUrl();				
	 // If the current window handle belongs to main window (use "String handle = driver.getWindowHandle();" coomand):
		if (handle.equals(driver.getWindowHandle()) == true) {			
	         // Performing the click operation that opens a new window:
			    driver.findElement(By.xpath(xpath)).click();	   
	         // Switch to new opened window:
	            for(String winHandle : driver.getWindowHandles()){
	                driver.switchTo().window(winHandle);
	            }				
	         // Waiting for switch to be complete:
	            waitUntilUrl(driver, 15, currentURL);	            
		     // Performing the actions on the new window:
	            /** external operation already done */		
		} else {
	         // Closing the new window (that window is no longer required):
	            driver.close();
	         // Switching back to the main initial window:
	            driver.switchTo().window(handle);	
		     // Waiting for switch to be complete:
		        waitUntilUrl(driver, 15, currentURL);            
	         // Continuing with the initial window of the original browser:
	            /** external operation */
		}		
		return driver.getWindowHandle();
	}
	/* ##### WINDOW SWITCH HANDLER WITH CLICK END ##### */
	
	/* ##### WINDOW SWITCH HANDLER WITH DOUBLE-CLICK START ##### */
	public String doubleClickHandleWindow(WebDriver driver, String handle, String xpath) throws NumberFormatException, IOException {
     // Accessing current web address:
		String currentURL = driver.getCurrentUrl();				
	 // If the current window handle belongs to main window (use "String handle = driver.getWindowHandle();" coomand):
		if (handle.equals(driver.getWindowHandle()) == true) {			
	         // Performing the double-click operation that opens a new window:
			    doubleClick(driver, driver.findElement(By.xpath(xpath)));	   
	         // Switch to new opened window:
	            for(String winHandle : driver.getWindowHandles()){
	                driver.switchTo().window(winHandle);
	            }				
	         // Waiting for switch to be complete:
	            waitUntilUrl(driver, 15, currentURL);	            
		     // Performing the actions on the new window:
	            /** external operation already done */		
		} else {
	         // Closing the new window (that window is no longer required):
	            driver.close();
	         // Switching back to the main initial window:
	            driver.switchTo().window(handle);	
		     // Waiting for switch to be complete:
		        waitUntilUrl(driver, 15, currentURL);            
	         // Continuing with the initial window of the original browser:
	            /** external operation */
		}		
		return driver.getWindowHandle();
	}
	/* ##### WINDOW SWITCH HANDLER WITH DOUBLE-CLICK END ##### */
	
	/* ##### TAB SWITCH HANDLER WITH CLICK START ##### */
	public String clickHandleTab(WebDriver driver, String handle, String xpath) throws NumberFormatException, IOException {
     // Accessing current web address:
		String currentURL = driver.getCurrentUrl();				
	 // If the current tab handle belongs to main tab (use "String handle = driver.getWindowHandle();" coomand):
		if (handle.equals(driver.getWindowHandle()) == true) {			
	         // Performing the click operation that opens a new tab:
			    driver.findElement(By.xpath(xpath)).click();	   
	         // Switch to new opened tab:
                                        // for(String winHandle : driver.getWindowHandles()){
			    ArrayList<String> tabs2 = new ArrayList<String> (driver.getWindowHandles());
			    driver.switchTo().window(tabs2.get(1));
 	                                    // }				
	         // Waiting for switch to be complete:
	            waitUntilUrl(driver, 15, currentURL);	            
		     // Performing the actions on the new tab:
	            /** external operation already done */		
		} else {
	         // Closing the new tab (that tab is no longer required):
	            driver.close();
	         // Switching back to the main initial tab:
	            driver.switchTo().window(handle);	
		     // Waiting for switch to be complete:
		        waitUntilUrl(driver, 15, currentURL);            
	         // Continuing with the initial tab of the original browser:
	            /** external operation */
		}		
		return driver.getWindowHandle();
	}
	/* ##### TAB SWITCH HANDLER WITH CLICK END ##### */
				
	public void moveToElement(WebDriver driver, String scrollTo) throws InterruptedException{
        WebElement element = driver.findElement(By.xpath(scrollTo));
        Coordinates coordinate = ((Locatable)element).getCoordinates(); 
        coordinate.onPage(); 
        coordinate.inViewPort();
	    Thread.sleep(1000);
	}
	
	public String stringsDifference(String str1, String str2) {
		int INDEX_NOT_FOUND = -1;
		String EMPTY = "";
		String message;
	    if ( ((str1 == null)||(str1.equals(EMPTY))) && ((str2 != null)&&(!str2.equals(EMPTY))) ) { message = str2;  }
	    if ( ((str2 == null)||(str2.equals(EMPTY))) && ((str1 != null)&&(!str1.equals(EMPTY))) ) { message = str1;  }
	    if ( ((str1 == null)||(str1.equals(EMPTY))) && ((str2 == null)||( str2.equals(EMPTY))) ) { message = EMPTY; }
	    int at = indexOfDifference(str1, str2);
	    if (at == INDEX_NOT_FOUND) { message = EMPTY; } else {
	    message = "\"" + str1.substring(at) + "\" vs. \"" + str2.substring(at) +"\"";
	    }
	    return message;
	}

	public int indexOfDifference(CharSequence cs1, CharSequence cs2) {
		int INDEX_NOT_FOUND = -1;
	    if (cs1 == cs2) { return INDEX_NOT_FOUND; }
	    if (cs1 == null || cs2 == null) { return 0; }
	    int i;
	    for (i = 0; i < cs1.length() && i < cs2.length(); ++i) {
	        if (cs1.charAt(i) != cs2.charAt(i)) { break; }
	    }
	    if (i < cs2.length() || i < cs1.length()) { return i; }
	    return INDEX_NOT_FOUND;
	}
	
    /** Returns a String of n spaces long */
	public String padSpace(int n) {
		if (n < 0) { n = 0; }
		String s = "";
		for (int i = 0; i < n; i++) {
			s = s + " ";
		}
		return s;
	}

    /** Gets a String and returs it with added n leading spaces (to left) */
	public String padLeft(String s, int n) {
			if (n < 0) { n = 0; }
			if (s.equals(null)) { s = ""; }
			for (int i = 0; i < n; i++) {
				s = " " + s;
			}
		return s;
		}
	
    /** Gets a String and returs it with added n spaces to right */
	public String padRight(String s, int n) {
		if (n < 0) { n = 0; }
		if (s.equals(null)) { s = ""; }
		for (int i = 0; i < n; i++) {
			s = s + " ";
		}
		return s;
	}
	
	/** Detects if the entered String is Integer */
	public static boolean isInteger(String s) {
	    try { Integer.parseInt(s); }
	    catch(NumberFormatException e) { return false; }
	    catch(NullPointerException  e) { return false; }
	 // only gets here if the entered String is Integer:
	    return true;
	}

    /**
	 * Detects if the entered Object is numeric, and if yes: 
	 * ---> returns String value of #.### Format;
	 * ---> adds leading Spaces to match in-row of "###.###";
	 */
	public String padNum(Object o) {
	double d = 0;
	if (o instanceof Double)  { d = Double.parseDouble((String) o.toString()); }
	if (o instanceof String)  { try { d = Double.parseDouble((String) o); }  catch(NumberFormatException nfe) { } }
	if (o instanceof Integer) { d = Double.parseDouble((String) o.toString()); }
	if (o instanceof Float)   { d = new Double(o.toString()); }
	String pad = "";
	if (d < 100) { pad = " ";  }
	if (d < 10 ) { pad = "  "; }
	String s = pad + (new DecimalFormat("0.000").format(d)).toString();
	return s;
	}
	
	public boolean isAlertPresent(WebDriver driver){
        try{ driver.switchTo().alert(); return true; }
		catch(Exception e){ return false; }
    }
	
	public void alertHandler(WebDriver driver) throws NumberFormatException, IOException{
	  if(isAlertPresent(driver)){
        driver.switchTo().alert();
        fileWriterPrinter(driver.switchTo().alert().getText());
        driver.switchTo().alert().accept();   
        driver.switchTo().defaultContent();
      }
	}
	
	public String alertContentScanner(WebDriver driver) throws NumberFormatException, IOException{
	    String content = null;
		if (isAlertPresent(driver)) {
			driver.switchTo().alert();
			content = driver.switchTo().alert().getText();
	        driver.switchTo().alert().accept();   
	        driver.switchTo().defaultContent();
			}
	return content;	
	}
	
	public void alertHandler(WebDriver driver, String handle) throws NumberFormatException, IOException{
		  if(isAlertPresent(driver)){
	        driver.switchTo().alert();
	        fileWriterPrinter(driver.switchTo().alert().getText());
	        driver.switchTo().alert().accept();
	        closeAllOtherWindows(driver, handle); // driver.switchTo().window(handle);
	      }
		}
	
	/** 
	 * Close all the other windows except of selected (Main) Window,
	 * identified by Handle parameter, entered as "mainWindowHandle" string.
	 * Can be used instead of "driver.switchTo().window(parentWindowHandle);" 
	 */
	public boolean closeAllOtherWindows(WebDriver driver, String mainWindowHandle) {
		Set<String> allWindowHandles = driver.getWindowHandles();
		for (String currentWindowHandle : allWindowHandles) {
			if (!currentWindowHandle.equals(mainWindowHandle)) {
				driver.switchTo().window(currentWindowHandle);
				driver.close();
			}
		}		
		driver.switchTo().window(mainWindowHandle);
		if (driver.getWindowHandles().size() == 1) { return true; }
		else { return false; }
	}
	
	/** 
	 * Navigate to Element identified by "By", using JavaScript 
	 */
	public void navigateToElement(WebDriver driver, By element) {
	    if (driver instanceof JavascriptExecutor) {
		   ((JavascriptExecutor) driver).executeScript("window.scrollBy(0," + (
				                                       - driver.findElement(element).getLocation().getY()
				                                       + driver.manage().window().getSize().height/2
				                                       ) + ")", "");
	    }	    
	}
	
	/** 
	 * Hover to Element identified as WebElement
	 */
	public void hoverElement(WebDriver driver, WebElement Element) {
        Actions builder = new Actions(driver);
        builder.moveToElement(Element).build().perform();    
	}
	
	/** 
	 * Hover to Element identified by "By"
	 */
	public void hoverElement(WebDriver driver, By element) {
		WebElement Element = driver.findElement(element);
        Actions builder = new Actions(driver);
        builder.moveToElement(Element).build().perform();    
	}
	
	/** 
	 * Hover to Element identified by Xpath
	 */
	public void hoverElement(WebDriver driver, String xpath) {		
		WebElement Element = driver.findElement(By.xpath(xpath));
        Actions builder = new Actions(driver);
        builder.moveToElement(Element).build().perform();    
	}
	
	/** 
	 * Output RGB-color of Element identified by Xpath
	 * @throws IOException 
	 * @throws NumberFormatException 
	 */
	public String getColorRGB(WebDriver driver, String xpath, String css, Boolean ifPrint) throws NumberFormatException, IOException {		
		if(ifPrint){ fileWriterPrinter(driver.findElement(By.xpath(xpath)).getCssValue(css)); }
		return driver.findElement(By.xpath(xpath)).getCssValue("color");		
	}
	
	/** 
	 * Output HEX-color of Element identified by Element
	 * @throws IOException 
	 * @throws NumberFormatException 
	 */
	public String getColorHEX(WebDriver driver, By element, String css, Boolean ifPrint, String comment) throws NumberFormatException, IOException {
		String color = driver.findElement(element).getCssValue(css), space = "";
		       color = color.substring(0, color.indexOf(")") + 1);
		if(comment.length() > 0) { space = ": "; }
		if(ifPrint){ fileWriterPrinter("\n" + comment + space + Color.fromString(color).asHex()); }
		return Color.fromString(color).asHex();		
	}
	
	/** 
	 * Output HEX-color of Element identified by Xpath
	 * @throws IOException 
	 * @throws NumberFormatException 
	 */
	public String getColorHEX(WebDriver driver, String xpath, String css, Boolean ifPrint, String comment) throws NumberFormatException, IOException {
		String color = driver.findElement(By.xpath(xpath)).getCssValue(css), space = "";
		       color = color.substring(0, color.indexOf(")") + 1);
		if(comment.length() > 0) { space = ": "; }
		if(ifPrint){ fileWriterPrinter("\n" + comment + space + Color.fromString(color).asHex()); }
		return Color.fromString(color).asHex();		
	}
	
	/** 
	 * Output integer array of RGB-color values of Element identified by Element
	 * @throws IOException 
	 * @throws NumberFormatException 
	 */
	public int[] getColorRGBarray(WebDriver driver, By element, String css, Boolean ifPrint) throws NumberFormatException, IOException {
		String color = driver.findElement(element).getCssValue(css);
		       color = color.substring(0, color.indexOf(")") + 1);
		String colorAfterRed = color.substring(color.indexOf(",") + 2, color.length());
		String colorAfterGreen = colorAfterRed.substring(colorAfterRed.indexOf(",") + 2, colorAfterRed.length());		
		int red   = Integer.valueOf(color.substring(color.indexOf("(") + 1, color.indexOf(",")));
		int green = Integer.valueOf(colorAfterRed.substring(0, colorAfterRed.indexOf(",")));
		int blue  = Integer.valueOf(colorAfterGreen.substring(0, colorAfterGreen.indexOf(",")));
		String[] Name = {"RED = ", "GREEN = ", "BLUE = "};
		int[] Color = {red, green, blue};
		if(ifPrint){
			for (int i = 0; i < Color.length; i++) {
				fileWriterPrinter(Name[0] + Color[0]);
			}
		}
		return Color;
	}
	
	/** 
	 * Output integer array of RGB-color values of Element identified by Xpath
	 * @throws IOException 
	 * @throws NumberFormatException 
	 */
	public int[] getColorRGBarray(WebDriver driver, String xpath, String css, Boolean ifPrint) throws NumberFormatException, IOException {
		String color = driver.findElement(By.xpath(xpath)).getCssValue(css);
		       color = color.substring(0, color.indexOf(")") + 1);
		String colorAfterRed = color.substring(color.indexOf(",") + 2, color.length());
		String colorAfterGreen = colorAfterRed.substring(colorAfterRed.indexOf(",") + 2, colorAfterRed.length());		
		int red   = Integer.valueOf(color.substring(color.indexOf("(") + 1, color.indexOf(",")));
		int green = Integer.valueOf(colorAfterRed.substring(0, colorAfterRed.indexOf(",")));
		int blue  = Integer.valueOf(colorAfterGreen.substring(0, colorAfterGreen.indexOf(",")));
		String[] Name = {"RED = ", "GREEN = ", "BLUE = "};
		int[] Color = {red, green, blue};
		if(ifPrint){
			for (int i = 0; i < Color.length; i++) {
				fileWriterPrinter(Name[0] + Color[0]);
			}
		}
		return Color;
	}
	
	/** 
	 * Output RED-color value of Element identified by Xpath
	 * @throws IOException 
	 * @throws NumberFormatException 
	 */
	public int getColorRED(WebDriver driver, String xpath, String css, Boolean ifPrint) throws NumberFormatException, IOException {
		if(ifPrint){ fileWriterPrinter("RED = " + getColorRGBarray(driver, xpath, css, false)[0]); }
		return getColorRGBarray(driver, xpath, css, false)[0];
	}
	
	/** 
	 * Output GREEN-color value of Element identified by Xpath
	 * @throws IOException 
	 * @throws NumberFormatException 
	 */
	public int getColorGREEN(WebDriver driver, String xpath, String css, Boolean ifPrint) throws NumberFormatException, IOException {
		if(ifPrint){ fileWriterPrinter("GREEN = " + getColorRGBarray(driver, xpath, css, false)[1]); }
		return getColorRGBarray(driver, xpath, css, false)[1];
	}
	
	/** 
	 * Output BLUE-color value of Element identified by Xpath
	 * @throws IOException 
	 * @throws NumberFormatException 
	 */
	public int getColorBLUE(WebDriver driver, String xpath, String css, Boolean ifPrint) throws NumberFormatException, IOException {
		if(ifPrint){ fileWriterPrinter("BLUE = " + getColorRGBarray(driver, xpath, css, false)[2]); }
		return getColorRGBarray(driver, xpath, css, false)[2];
	}

	/**  
	 * Switch window size to Mobile-Portrait
	 * @throws InterruptedException 
	 * @throws IOException
	 */
	public void switchWindowSizeToMobilePortrait(WebDriver driver) throws InterruptedException, IOException {
    driver.manage().window().setSize(new Dimension(430,857));
    fileWriterPrinter("\n" + "CONVERTED TO WINDOW SIZE: MOBILE (PORTRAIT) = 430 x 857");
    Thread.sleep(1000);
	}
	
    /** 
	 * Switch window size to Mobile-Landscape
	 * @throws InterruptedException 
	 * @throws IOException
	 */
	public void switchWindowSizeToMobileLandscape(WebDriver driver) throws InterruptedException, IOException {
	driver.manage().window().setSize(new Dimension(857,430));
	fileWriterPrinter("\n" + "CONVERTED TO WINDOW SIZE: MOBILE (LANDSCAPE) = 857 x 430");
	Thread.sleep(1000);
	}

	/**  
	 * Switch window size to Tablet-Portrait
	 * @throws InterruptedException 
	 * @throws IOException
	 */
	public void switchWindowSizeToTabletPortrait(WebDriver driver) throws InterruptedException, IOException {
	driver.manage().window().setSize(new Dimension(768, 1024));
	fileWriterPrinter("\n" + "CONVERTED TO WINDOW SIZE: TABLET (PORTRAIT) = 768 x 1024");
	Thread.sleep(1000);
	}

	/** 
	 * Switch window size to Tablet-Landscape
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	public void switchWindowSizeToTabletLandscape(WebDriver driver) throws InterruptedException, IOException {
	driver.manage().window().setSize(new Dimension(1024,768));
	fileWriterPrinter("\n" + "CONVERTED TO WINDOW SIZE: TABLET (LANDSCAPE) = 1024 x 768");
	Thread.sleep(1000);
	}
	
	/** 
	 * Outputs internal Element vertical location symmetry ratio in percents (ideal is 100%)
	 * @throws IOException 
	 * @throws NumberFormatException 
	 */
	public int getInternalElementVerticalSymmetry(WebDriver driver, String xpathExternal, String xpathInternal) throws NumberFormatException, IOException {
		int Y = driver.findElement(By.xpath(xpathExternal)).getLocation().getY();
		int H = driver.findElement(By.xpath(xpathExternal)).getSize().getHeight();
		int y = driver.findElement(By.xpath(xpathInternal)).getLocation().getY();
		int h = driver.findElement(By.xpath(xpathInternal)).getSize().getHeight();
		int symmetryRatio = 0;
		int upperSpace = Math.abs(Y - y);
		int bottomSpace = Math.abs(Y + H - y - h);
		if((upperSpace != 0) && (bottomSpace != 0)) { symmetryRatio = (bottomSpace/upperSpace)*100; }		
		if((upperSpace == 0) || (bottomSpace == 0)) { 
			if(H > h) { symmetryRatio = 100*(H - Math.abs((Y+H/2)-(y+h/2)))/H; }
			else      { symmetryRatio = 100*(h - Math.abs((Y+H/2)-(y+h/2)))/h; }
			}		
		if((upperSpace == 0) && (bottomSpace == 0)) { symmetryRatio = 100; }
		fileWriterPrinter("\n" + "VERTICAL SIMMETRY = " + symmetryRatio + "%" + "\n");
		return symmetryRatio;
	}
	
	/** 
	 * Outputs internal Element horizontal location symmetry ratio in percents (ideal is 100%)
	 * @throws IOException 
	 * @throws NumberFormatException 
	 */
	public int getInternalElementHorizontalalSymmetry(WebDriver driver, String xpathExternal, String xpathInternal) throws NumberFormatException, IOException {
		int X = driver.findElement(By.xpath(xpathExternal)).getLocation().getX();
		int W = driver.findElement(By.xpath(xpathExternal)).getSize().getWidth();
		int x = driver.findElement(By.xpath(xpathInternal)).getLocation().getX();
		int w = driver.findElement(By.xpath(xpathInternal)).getSize().getWidth();		
		int symmetryRatio = 0;
		int leftSpace = Math.abs(X - x);
		int rightSpace = Math.abs(X + W - x - w);
		if((leftSpace != 0) && (rightSpace != 0)) { symmetryRatio = (rightSpace/leftSpace)*100; }		
		if((leftSpace == 0) || (rightSpace == 0)) { 
			if(W > w) { symmetryRatio = 100*(W - Math.abs((X+W/2)-(x+w/2)))/W; }
			else      { symmetryRatio = 100*(w - Math.abs((X+W/2)-(x+w/2)))/w; }
			}		
		if((leftSpace == 0) && (rightSpace == 0)) { symmetryRatio = 100; }
		fileWriterPrinter("\n" + "HORIZONTAL SIMMETRY = " + symmetryRatio + "%" + "\n");
		return symmetryRatio;
	}
	
	/** 
	 * Outputs Elements Horizontal-Left borders alignment
	 * @throws IOException 
	 * @throws NumberFormatException 
	 */
	public int getElementHorizontalalAlignmentLeft(WebDriver driver, String xpathUpper, String xpathLower) throws NumberFormatException, IOException {
		int X = driver.findElement(By.xpath(xpathUpper)).getLocation().getX();
		int x = driver.findElement(By.xpath(xpathLower)).getLocation().getX();
		int alignment = Math.abs(X - x);
		fileWriterPrinter("\n" + "HORIZONTAL ALIGNMENT = " + alignment);
		return alignment;
		}
	
	/** 
	 * Outputs Elements Horizontal-Right borders alignment
	 * @throws IOException 
	 * @throws NumberFormatException 
	 */
	public int getElementHorizontalalAlignmentRight(WebDriver driver, String xpathUpper, String xpathLower) throws NumberFormatException, IOException {
		int X = driver.findElement(By.xpath(xpathUpper)).getLocation().getX() + driver.findElement(By.xpath(xpathUpper)).getSize().getWidth();
		int x = driver.findElement(By.xpath(xpathLower)).getLocation().getX() + driver.findElement(By.xpath(xpathLower)).getSize().getWidth();
		int alignment = Math.abs(X - x);
		fileWriterPrinter("\n" + "HORIZONTAL ALIGNMENT = " + alignment);
		return alignment;
		}
	
	/** 
	 * Outputs Elements Vertical-Top borders Alignment
	 * @throws IOException 
	 * @throws NumberFormatException 
	 */
	public int getElementVerticalAlignmentTop(WebDriver driver, String xpathLeft, String xpathRight) throws NumberFormatException, IOException {
		int Y = driver.findElement(By.xpath(xpathLeft)).getLocation().getY();
		int y = driver.findElement(By.xpath(xpathRight)).getLocation().getY();
		int alignment = Math.abs(Y - y);
		fileWriterPrinter("\n" + "VERTICAL ALIGNMENT = " + alignment);
		return alignment;
		}
	
	/** 
	 * Outputs Elements Vertical-Bottom borders Alignment
	 * @throws IOException 
	 * @throws NumberFormatException 
	 */
	public int getElementVerticalAlignmentBottom(WebDriver driver, String xpathLeft, String xpathRight) throws NumberFormatException, IOException {
		int Y = driver.findElement(By.xpath(xpathLeft)).getLocation().getY() + driver.findElement(By.xpath(xpathLeft)).getSize().getHeight();
		int y = driver.findElement(By.xpath(xpathRight)).getLocation().getY() + driver.findElement(By.xpath(xpathRight)).getSize().getHeight();
		int alignment = Math.abs(Y - y);
		fileWriterPrinter("\n" + "VERTICAL ALIGNMENT = " + alignment);
		return alignment;
		}
	
	/** Outputs element Hight */
	public int getElementHeight(WebDriver driver, WebElement element) { return element.getSize().getHeight(); }
	
	/** Outputs element Hight */
	public int getElementHeight(WebDriver driver, String xpath) { return getElementHeight(driver, driver.findElement(By.xpath(xpath))); }
	
	/** Outputs element Width */
	public int getElementWidth(WebDriver driver, WebElement element) { return element.getSize().getWidth(); }
	
	/** Outputs element Width */
	public int getElementWidth(WebDriver driver, String xpath) { return getElementWidth(driver, driver.findElement(By.xpath(xpath))); }
	
	/** 
	 * Outputs horizontal space between two elements
	 * @throws IOException 
	 * @throws NumberFormatException 
	 */
	public int getHorizontalSpaceBetweenTwoElements(WebDriver driver, String xpathLeft, String xpathRight) throws NumberFormatException, IOException {		
		int x = driver.findElement(By.xpath(xpathLeft)).getLocation().getX();
		int w = driver.findElement(By.xpath(xpathLeft)).getSize().getWidth();
		int X = driver.findElement(By.xpath(xpathRight)).getLocation().getX();
		int space = X - x - w;
		fileWriterPrinter("\n" + "HORIZONTAL SPACE = " + space);
		return space;
	}
	
	/** 
	 * Outputs vertical space between two elements
	 * @throws IOException 
	 * @throws NumberFormatException 
	 */
	public int getVerticalSpaceBetweenTwoElements(WebDriver driver, String xpathUpper, String xpathLower) throws NumberFormatException, IOException {
		int Y = driver.findElement(By.xpath(xpathLower)).getLocation().getY();
		int y = driver.findElement(By.xpath(xpathUpper)).getLocation().getY();
		int h = driver.findElement(By.xpath(xpathUpper)).getSize().getHeight();
		int space = Y - y - h;
		fileWriterPrinter("\n" + "VERTICAL SPACE = " + space);
		return space;
	}
	
	/** 
	 * Outputs horizontal distance between two elements
	 * @throws IOException 
	 * @throws NumberFormatException 
	 */
	public int getHorizontalDistanceBetweenTwoElements(WebDriver driver, String xpathLeft, String xpathRight) throws NumberFormatException, IOException {
		int x = driver.findElement(By.xpath(xpathLeft)).getLocation().getX();
		int X = driver.findElement(By.xpath(xpathRight)).getLocation().getX();
		int distance = X - x;
		fileWriterPrinter("\n" + "HORIZONTAL DISTANCE = " + distance);
		return distance;
	}
	
	/** 
	 * Outputs vertical distance between two elements
	 * @throws IOException 
	 * @throws NumberFormatException 
	 */
	public int getVerticalDistanceBetweenTwoElements(WebDriver driver, String xpathUpper, String xpathLower) throws NumberFormatException, IOException {
		int Y = driver.findElement(By.xpath(xpathLower)).getLocation().getY();
		int y = driver.findElement(By.xpath(xpathUpper)).getLocation().getY();
		int distance = Y - y;
		fileWriterPrinter("\n" + "VERTICAL DISTANCE = " + distance);
		return distance;
	}
	
	/**
     * This class gets file size
     * @param args
     * @throws IOException 
     * @throws NumberFormatException 
     */
    public double fileSizeMB(String filePath) throws NumberFormatException, IOException {
    	File file = new File(filePath.replace("\\", "/"));
        if(file.exists()) {
         // fileWriterPrinter("FILE SIZE: " + file.length() + " Bit");
         // fileWriterPrinter("FILE SIZE: " + file.length()/1024 + " Kb");
            fileWriterPrinter("FILE SIZE: " + ((double) file.length()/(1024*1024)) + " Mb");
            return (double) file.length()/(1024*1024);
        } else { fileWriterPrinter("File doesn't exist"); return 0; }
         
    }
    
	/**
	 * Create a Zip File from given Directory using ZipOutputStream CLASS with Path Structure and Protection choise
	 */
	 public void zipDirectory(String sourceDirectoryPath, String zipOutputPath, Boolean ifAbsolutePath, Boolean ifProtect, int maxZipMbSizeToProtect) throws Exception {		  
		   if(ifAbsolutePath) { zipOutputPath = zipDirectoryFullPath(sourceDirectoryPath, zipOutputPath); }
		   else { zipOutputPath = zipDirectoryInternalPath(sourceDirectoryPath, zipOutputPath); }		   
		   if(fileSizeMB(zipOutputPath) < maxZipMbSizeToProtect) {
			   if(ifProtect){ fileRename(zipOutputPath,zipOutputPath.replace(".zip",".renameToZip")); }
			   }
		  }
	
	/**
	 * Create a Zip File from given Directory using ZipOutputStream CLASS with keeping Full Path Structure
	 */
	 public String zipDirectoryFullPath(String sourceDirectoryPath, String zipOutputPath) throws Exception {
     	   // DECLARATION:
     	   if(sourceDirectoryPath.endsWith("\\")) { sourceDirectoryPath = sourceDirectoryPath.substring(0, sourceDirectoryPath.length() - 1); }
     	   sourceDirectoryPath = sourceDirectoryPath.replace("\\", "/");
     	   zipOutputPath = zipOutputPath.replace("\\", "/");
     	   // MESSAGE:
     	   fileWriterPrinter("\nSOURCE DIRECTORY: " + sourceDirectoryPath);
     	   fileWriterPrinter(" ZIP OUTPUT PATH: " + zipOutputPath + "\n");
		  
		   File dirObj = new File(sourceDirectoryPath);
		   ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipOutputPath));
		   System.out.println("Creating : " + zipOutputPath);
		   addDir(dirObj, out);
		   out.close();
		   return zipOutputPath;
		  }
	 
	/**
	 * Add File to Zip
	 */
	 public void addDir(File dirObj, ZipOutputStream out) throws IOException {
		    File[] files = dirObj.listFiles();
		    byte[] tmpBuf = new byte[1024];
		    for (int i = 0; i < files.length; i++) {
		      if (files[i].isDirectory()) {
		        addDir(files[i], out);
		        continue;
		      }
	    	  FileInputStream in = new FileInputStream(files[i].getAbsolutePath());
		      System.out.println(" Adding: " + files[i].getAbsolutePath());
		      out.putNextEntry(new ZipEntry(files[i].getAbsolutePath()));
		      int len;
		      while ((len = in.read(tmpBuf)) > 0) { out.write(tmpBuf, 0, len); }
		      out.closeEntry();
		      in.close();
		      }
		  }

	    /**
	     * Create a Zip File from given Directory with keeping Internal Path Structure only
	     */
		public String zipDirectoryInternalPath(String sourceDirectoryPath, String zipOutputPath) throws IOException {
		  	   // DECLARATION:
		  	   if(sourceDirectoryPath.endsWith("\\")) { sourceDirectoryPath = sourceDirectoryPath.substring(0, sourceDirectoryPath.length() - 1); }
		  	   sourceDirectoryPath = sourceDirectoryPath.replace("\\", "/");
		  	   zipOutputPath = zipOutputPath.replace("\\", "/");
		  	   // MESSAGE:
		  	   fileWriterPrinter("\nSOURCE DIRECTORY: " + sourceDirectoryPath);
		  	   fileWriterPrinter(" ZIP OUTPUT PATH: " + zipOutputPath + "\n");
		  	   
		       File dir = new File(sourceDirectoryPath);
		       File file = new File(zipOutputPath);
		       // System.out.println(mydir.toURI().relativize(myfile.toURI()).getPath());	
		       zip(dir, file);
		       return zipOutputPath;
			}

			@SuppressWarnings("resource")
			public static void zip(File directory, File zipfile) throws IOException {
			    URI base = directory.toURI();
			    Deque<File> queue = new LinkedList<File>();
			    queue.push(directory);
			    OutputStream out = new FileOutputStream(zipfile);
			    Closeable res = out;
			    try {
			    	ZipOutputStream zout = new ZipOutputStream(out);
			    	res = zout;
			    	while (!queue.isEmpty()) {
			    		directory = queue.pop();
			    		for (File kid : directory.listFiles()) {
			    			String name = base.relativize(kid.toURI()).getPath();
			    			if (kid.isDirectory()) {
			    				queue.push(kid);
			    				name = name.endsWith("/") ? name : name + "/";
			    				zout.putNextEntry(new ZipEntry(name));
			    			} else {
			    				zout.putNextEntry(new ZipEntry(name));
			    				copy(kid, zout);
			    				zout.closeEntry();
			    				}
			    			}
			    		}
			    	} finally { res.close(); }
			    }
			  	  
			private static void copy(InputStream in, OutputStream out) throws IOException { 
				byte[] buffer = new byte[1024];
				while (true) {
				      int readCount = in.read(buffer);
				      if (readCount < 0) {  break; }
				      out.write(buffer, 0, readCount);
				      }
				}
		  
			private static void copy(File file, OutputStream out) throws IOException {  
				InputStream in = new FileInputStream(file);    
				try { copy(in, out); } finally { in.close(); }
				}

			@SuppressWarnings("unused")	
			private static void copy(InputStream in, File file) throws IOException {   
				OutputStream out = new FileOutputStream(file);
				try { copy(in, out); } finally { out.close(); }
				}
			
		/**
	     * This Method renames file
	     * @param args
	     * @throws IOException 
	     * @throws NumberFormatException 
	     */
	    public void fileRename(String sourcePath, String targetPath) {
	    	File file = new File(sourcePath); 
	    	file.renameTo(new File(targetPath));
	    	}
	       
		/**
	     * This Method generates JavaScript Alert shown during user difined time in seconds
	     * @param args
	     * @throws InterruptedException 
	     */
	    public void javaScriptAlert(WebDriver driver, String alert, int seconds) throws InterruptedException {
			  String handle = driver.getWindowHandle();		      
	    	  JavascriptExecutor javascript = (JavascriptExecutor) driver;
	    	  javascript.executeScript("alert('" + alert + "');");
	    	  // SWITCHING BACK TO INITIAL TAB:
	    	  Thread.sleep(seconds * 1000);
	    	  driver.switchTo().alert().accept();
	          driver.switchTo().window(handle);
	    	  }
	    
		// ########### DRAG AND DROP START ############
	    public String dragAndDropMessage(int X, int Y) {
	    	// DECLARATION:
	    	String alertX, alertY, and, alert = "", dir;
	    	// AFTER DRAG AND DROP ALERT:
  		    alertX = " by " + X + " pixel offset In horizontal";
  		    alertY = " by " + Y + " pixel offset In vertical";
  		    and = "";
  		    alert = "";
  		    dir = " direction";
  		    if (X == 0) { alertX = ""; }
  		    if (Y == 0) { alertY = ""; }
  		    if ((X != 0) && (Y != 0)) { and = " and"; dir = " directions";}
  		    if ((X == 0) && (Y == 0)) { and = " by nothing"; dir = "";}
  		    alert = "Element has been Drag-And-Dropped:" + alertX + and + alertY + dir;
	    	return alert;
	    	}
	    
	    public double[] dragAndDrop(WebDriver driver, String xpath, int X, int Y, Boolean ifPrompt, int waitMilliSeconds) throws InterruptedException, NumberFormatException, IOException {
	    	// LOCATING ELEMENT TO DRAG:	    	  
	    	WebElement element = driver.findElement(By.xpath(xpath));
	    	// DRAG AND DROP ELEMENT BY X PIXEL OFFSET IN HORIZONTAL DIRECTION X AND Y PIXEL IN VERTICAL DIRECTION:
	    	return dragAndDrop(driver, element, X, Y, ifPrompt, waitMilliSeconds);
	    	}
	    
	    public double[] dragAndDrop(WebDriver driver, By by, int X, int Y, Boolean ifPrompt, int waitMilliSeconds) throws InterruptedException, NumberFormatException, IOException {
	    	// LOCATING AN ELEMENT TO DRAG:
	    	WebElement element = driver.findElement(by);
	    	// DRAG AND DROP ELEMENT BY X PIXEL OFFSET IN HORIZONTAL DIRECTION X AND Y PIXEL IN VERTICAL DIRECTION:
	    	return dragAndDrop(driver, element, X, Y, ifPrompt, waitMilliSeconds);
	    	}
	    
	    public double[] dragAndDrop(WebDriver driver, WebElement element, int X, int Y, Boolean ifPrompt, int waitMilliSeconds) throws InterruptedException, NumberFormatException, IOException {	
	    	// DECLARATION:
	    	int defaultCoordinateX, defaultCoordinateY, movedCoordinateX, movedCoordinateY, elementWidth, elementHeight;
	    	double DefaultCoordinateX,  DefaultCoordinateY, MovedCoordinateX, MovedCoordinateY, ElementWidth, ElementHeight, MovementRatioX, MovementRatioY;
	    	DecimalFormat df = new DecimalFormat("#");
	    	
	    	// BEFORE PARAMETERS:
	        defaultCoordinateX = element.getLocation().getX();
	        defaultCoordinateY = element.getLocation().getY();
	        elementWidth = getElementWidth(driver, element);
	        elementHeight = getElementHeight(driver, element);
	        movedCoordinateX = defaultCoordinateX;
	        movedCoordinateY = defaultCoordinateY;
	        
	    	// DRAG AND DROP ELEMENT BY X PIXEL OFFSET IN HORIZONTAL DIRECTION X AND Y PIXEL IN VERTICAL DIRECTION:   	
	    	int i = 0;
	        while ( (((X != 0) && (movedCoordinateX == defaultCoordinateX)) || ((Y != 0) && (movedCoordinateY == defaultCoordinateY))) && (i < 3) ) {
	        	new Actions(driver).dragAndDropBy(element, X, Y).release(element).build().perform();
	        	Thread.sleep(waitMilliSeconds);
	        	movedCoordinateX = element.getLocation().getX();
	        	movedCoordinateY = element.getLocation().getY();
	        	i++;
	        	}
	        
//		    if( (X != 0) && (movedCoordinateX == defaultCoordinateX) ) { Boolean reRun = true; }
//		    if( (Y != 0) && (movedCoordinateY == defaultCoordinateY) ) { Boolean reRun = true; }
		    
	    	// AFTER DRAG AND DROP MESSAGE:	
	    	if (ifPrompt) { 
	    		fileWriterPrinter(dragAndDropMessage(X, Y));
	    		fileWriterPrinter("ELEMENT                 WIDTH = " + elementWidth);
	            fileWriterPrinter("ELEMENT                HEIGHT = " + elementWidth);
	            fileWriterPrinter("ELEMENT  DEFAULT X-COORDINATE = " + defaultCoordinateX);
	            fileWriterPrinter("ELEMENT  DEFAULT Y-COORDINATE = " + defaultCoordinateY);
		        fileWriterPrinter("ELEMENT MOVED TO X-COORDINATE = " + movedCoordinateX);		      
		        fileWriterPrinter("ELEMENT MOVED TO Y-COORDINATE = " + movedCoordinateY);
		        fileWriterPrinter("ELEMENT  MOVE ATTEMPTS NUMBER = " + i);
		        }
	    	DefaultCoordinateX = Double.valueOf(defaultCoordinateX);
	    	DefaultCoordinateY = Double.valueOf(defaultCoordinateY);
	    	MovedCoordinateX = Double.valueOf(movedCoordinateX);
	    	MovedCoordinateY = Double.valueOf(movedCoordinateY);
	    	ElementWidth = Double.valueOf(elementWidth);
	    	ElementHeight = Double.valueOf(elementHeight);
	    	MovementRatioX  = (double) ( ((MovedCoordinateX - DefaultCoordinateX)*100/ElementWidth)*1000/1000.000 );
	    	MovementRatioY  = (double) ( ((MovedCoordinateY - DefaultCoordinateY)*100/ElementHeight)*1000/1000.000 );
	    	if (ifPrompt) {
	    		fileWriterPrinter("ELEMENT SIZE X-MOVEMENT RATIO = " + df.format(MovementRatioX) + " %");
	    		fileWriterPrinter("ELEMENT SIZE Y-MOVEMENT RATIO = " + df.format(MovementRatioY) + " %\n");
	    		}
	    	double[] array = {MovementRatioX, MovementRatioY};
	    	return array;
	    	}
	    // ########### DRAG AND DROP START ############
	        
}