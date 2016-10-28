package com.tvokids.utilities;

import java.awt.AWTException;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
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
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.*;
import java.util.zip.*;

import javax.imageio.ImageIO;
import javax.swing.JTextField;

import org.apache.commons.io.FileUtils;
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
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
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
import org.testng.annotations.Test;

import com.tvokids.locator.Common;
import com.tvokids.locator.Dictionary;
import com.tvokids.locator.Drupal;
import com.tvokids.retry.*;

public class UtilitiesTestHelper {
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
			 // driver = new ChromeDriver();
				ChromeOptions options = new ChromeOptions();
				options.addArguments("--disable-extensions");
				driver = new ChromeDriver(options);	
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
	 * Zoom browser window IN or OUT
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws NumberFormatException 
	 */
	public void windowZoom(WebDriver driver, double zoom, int pauseSECONDS) throws InterruptedException, NumberFormatException, IOException {
		DecimalFormat f = new DecimalFormat("#.#");
		String Zoom = f.format(zoom);
		String zoomType = "OUT";
		if (zoom > 1) { zoomType = "IN"; }
		String script = "document.body.style.zoom=" + Zoom + ";this.blur();";
		((JavascriptExecutor) driver).executeScript(script);
		fileWriterPrinter("\nWINDOW ZOOM " + zoomType + " = " + ((int) (Double.valueOf(Zoom) * 100)) + "%\n");
		Thread.sleep(pauseSECONDS * 1000);
		}
	
	/**
	 * @throws IOException
	 * @throws InterruptedException 
	 */
	public void logIn(WebDriver driver) throws IOException, InterruptedException {
		if((driver.findElements(By.xpath(Common.logout)).size() > 0)) { logOut(driver); }
		getUrlWaitUntil(driver, 15, Common.userLoginPage);		
		waitUntilElementPresence(driver, 60, By.id("edit-name"), "\"Username\" ", new RuntimeException().getStackTrace()[0]);		
		driver.findElement(By.id("edit-name")).sendKeys(Common.adminUsername());
		driver.findElement(By.id("edit-pass")).sendKeys(Common.userPassword(Common.adminUsername()));
		driver.findElement(By.id(Drupal.submit)).click();
		Thread.sleep(500);
        if (driver.findElements(By.xpath("//*[text()='Have you forgotten your password?']")).size() == 1   ) {
        	throw new IOException("Sorry, unrecognized username or password");
        }
		waitUntilElementPresence(driver, 10, By.xpath(Drupal.drupalHomeIcon), "\"Home\" icon", new RuntimeException().getStackTrace()[0]);
	}
	
	/**
	 * @throws IOException
	 * @throws InterruptedException 
	 */
	public void logIn(WebDriver driver, String username, String password) throws IOException, InterruptedException {
		if((driver.findElements(By.xpath(Common.logout)).size() > 0)) { logOut(driver); }
		getUrlWaitUntil(driver, 15, Common.userLoginPage);
		waitUntilElementPresence(driver, 60, By.id("edit-name"), "\"Username\" ", new RuntimeException().getStackTrace()[0]);		
		driver.findElement(By.id("edit-name")).sendKeys(username);
		driver.findElement(By.id("edit-pass")).sendKeys(password);
		driver.findElement(By.id(Drupal.submit)).click();
		Thread.sleep(500);
        if (driver.findElements(By.xpath("//*[text()='Have you forgotten your password?']")).size() == 1   ) {
        	throw new IOException("Sorry, unrecognized username or password");
        }
		waitUntilElementPresence(driver, 10, By.xpath(Drupal.drupalHomeIcon), "\"Home\" icon", new RuntimeException().getStackTrace()[0]);
	}
	
	/**
	 * @throws IOException
	 * @throws InterruptedException 
	 */
	public void smartLogIn(WebDriver driver) throws IOException, InterruptedException {
		if((driver.findElements(By.xpath(Common.logout)).size() > 0)) { logOut(driver); }
		getUrlWaitUntil(driver, 10, Common.userLoginPage);		
		waitUntilElementPresence(driver, 60, By.id("edit-name"), "\"Username\" ", new RuntimeException().getStackTrace()[0]);		
		driver.findElement(By.id("edit-name")).sendKeys(Common.adminUsername());
		driver.findElement(By.id("edit-pass")).sendKeys(Common.userPassword(Common.adminUsername()));
		driver.findElement(By.id(Drupal.submit)).click();
		Thread.sleep(500);
        if ( driver.findElements(By.xpath("//*[text()='Have you forgotten your password?']")).size() == 1 ) {
        	logIn(driver, Common.contentEditorUsername, Common.userPassword(Common.contentEditorUsername));       	
        } else { waitUntilElementPresence(driver, 10, By.xpath(Drupal.drupalHomeIcon), "\"Home\" icon", new RuntimeException().getStackTrace()[0]); }
	}
	
	/**
	 * @throws IOException
	 */
	public void logOut(WebDriver driver) throws IOException {		
		while ( (driver.findElements(By.xpath(Common.logout)).size() > 0) || (driver.findElements(By.xpath(Common.notFoundError)).size() > 0) ) {
			try {
				driver.findElement(By.xpath(Common.logout)).click();
				waitUntilElementInvisibility(driver, 15, By.xpath(Common.logout), "\"Log out\" Button", new RuntimeException().getStackTrace()[0]);
			} catch (Exception e) { e.printStackTrace(); }
		}
	}
	
	/** Converts empty String to "N/A" */
	public String  convertEmptyStringToNotAvailable(String s) { if( (s.length() == 0 ) || (s.equals(null)) ) { return "N/A"; } else { return s; }
	}
	
	   
	/**
	 * Filters all the Contents by any selected cathegory ("" for "any") on user demand
	 * @throws IOException
	 */
		
	public void filterAllContent(WebDriver driver, String title, String type, String author, String published, Boolean ifAgeUnder, Boolean ifAgeOver, StackTraceElement t) throws InterruptedException, IOException{
		try {
			String ageGroup = "- Any -";
			if (type.length() == 0) { type = "- Any -"; }
			if (published.length() == 0) { published = "- Any -"; }
			if ( (ifAgeUnder) && (!ifAgeOver) ) { ageGroup = "5 and Under"; }
			if ( (!ifAgeUnder) && (ifAgeOver) ) { ageGroup = "6 and Over"; }

			fileWriterPrinter("\n" + "Title     FILTER:  " + convertEmptyStringToNotAvailable(title));
			fileWriterPrinter(       "Type      FILTER:  " + type.replace("- ", "").replace(" -", ""));
			fileWriterPrinter(       "Author    FILTER:  " + convertEmptyStringToNotAvailable(author));
			fileWriterPrinter(       "Published FILTER:  " + published.replace("- ", "").replace(" -", ""));
			fileWriterPrinter(       "Age Group FILTER:  " + ageGroup.replace("- ", "").replace(" -", "") + "\n");
			
			getUrlWaitUntil(driver, 15, Common.adminContentURL);

			// TITLE FILTER:
			driver.findElement(By.id("edit-title")).clear();
			if(title.length() > 0) { driver.findElement(By.id("edit-title")).sendKeys(title); }
			
			// TYPE FILTER:
			WebElement dropwDownListBox = driver.findElement(By.id("edit-type"));
			Select clickThis = new Select(dropwDownListBox);
			Thread.sleep(2000);
			clickThis.selectByVisibleText(type);
			Thread.sleep(2000);
			
			// TYPE AUTHOR (USER):
			driver.findElement(By.id("edit-author")).clear();         //pre-clear the Author filter field
			if(author.length() > 0) { 
				driver.findElement(By.id("edit-author")).sendKeys(author); 
				int size = waitUntilElementList(driver, 5, Common.autoComplete, "auto-complete").size();
	            if (size == 1) { try { driver.findElement(By.xpath(Common.autoComplete)).click(); } catch(Exception e) { } }
	            waitUntilElementInvisibility(driver, 15, Common.autoComplete, "auto-complete", new Exception().getStackTrace()[0]);
	            }
			
			// PUBLISHED FILTER:
			dropwDownListBox = driver.findElement(By.id("edit-status"));
			clickThis = new Select(dropwDownListBox);
			Thread.sleep(2000);
			clickThis.selectByVisibleText(published);
			Thread.sleep(2000);
			
			// AGE GROUP FILTER:
			dropwDownListBox = driver.findElement(By.id("edit-field-age-group-tid"));
			clickThis = new Select(dropwDownListBox);
			Thread.sleep(2000);
			clickThis.selectByVisibleText(ageGroup);
			Thread.sleep(2000);
			
         // APPLY:
			driver.findElement(By.id("edit-submit-admin-views-node")).click();
            waitUntilElementInvisibility(driver, 30, Common.ajaxThrobber, "Throbber", new Exception().getStackTrace()[0]);
			
			String result = driver.findElement(By.xpath("//tr[contains(@class,'views-row-first')]/td[1]")).getText();
			if (result.length() == 0) { result = "Content available."; }
			fileWriterPrinter("\n" + "          RESULT:  " + result + "\n");

		    } catch(Exception e) { getExceptionDescriptive(e, t, driver); }
	}
		
	/**
	 * Modifies all the Contents pre-selected by "Filter" function
	 * @throws IOException
	 */
	public void operateOnContent(WebDriver driver, String operation, Boolean ifAll, StackTraceElement t) throws InterruptedException, IOException{
		try {
			if (operation.length() == 0) { operation = "- Choose an operation -"; }
			int quantity = 0;

			int i = 1;
			while ((quantity == 0) && (driver.findElements(By.xpath(Drupal.errorAjax)).size() < 1)) {
			fileWriterPrinter("\nPAGE-" + i + ": PERFORMING " + operation.toUpperCase() + "...");
			waitUntilElementVisibility(driver, 30, Drupal.allInOneCheckBox, "\"Select All\"", new Exception().getStackTrace()[0]);
			
			
			if(ifAll) {
				// all rows selection:
				ajaxProtectedClick(driver, Drupal.allInOneCheckBox, "Select All", false, "", true, false); //checks the "Select All" check-box
				List<WebElement> elements = driver.findElements(By.xpath(Drupal.allRowsSelectorButton));
				if (elements.size() > 0) {
				   WebElement element = driver.findElement(By.xpath(Drupal.allRowsSelectorButton));
				   fileWriterPrinter(element.getAttribute("value"));
				   ((JavascriptExecutor)driver).executeScript("arguments[0].click();", element);
				}
			} else {
				// first row selection:
				ajaxProtectedClick(driver, Drupal.firstRowCheckBox, "Select First", false, "", true, false); //checks the "First Row" check-box
			}

			new Select(driver.findElement(By.id("edit-operation"))).selectByVisibleText(operation);
			Thread.sleep(1000);	
			driver.findElement(By.xpath(Drupal.executeButton)).click(); // 'Execute' button;
			
            if(operation.equals("Delete")) {
			waitUntilElementVisibility(driver, 30, "//*[contains(text(),'You selected the following')]", "\"You selected the following\"", new Exception().getStackTrace()[0]);
			driver.findElement(By.id(Drupal.submit)).click(); // 'Confirm' button
			waitUntilElementInvisibility(driver, 5, By.id(Drupal.submit), "\"Save\" Button", new Exception().getStackTrace()[0]);
            }
            
			waitUntilElementInvisibility(driver, 600, By.id(Drupal.progress), "Progress Bar", new Exception().getStackTrace()[0]);
			if(driver.findElements(By.xpath(Drupal.errorAjax)).size() > 0) { assertWebElementNotExist(driver, t, Drupal.errorAjax); }   
			waitUntilElementVisibility(driver, 30, Drupal.statusPerformed, "\"Performed " + operation + "\"", new Exception().getStackTrace()[0]);
			
			By message = By.xpath(Drupal.statusPerformedMessage);
			if (driver.findElements(message).size() > 0) { fileWriterPrinter(driver.findElement(message).getText()); }
			
			if(ifAll) { quantity = driver.findElements(By.xpath(Drupal.messageNoContentAvailable)).size(); } else { quantity = 1; }
			i++;
			}
		    } catch(Exception e) { getExceptionDescriptive(e, t, driver); }
	}
	
	/**
	 * Re-oprens (edit) any Contents using built-in "Filter" function
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public Boolean reopenContent(WebDriver driver, String title, String type, String author, String published, Boolean ifAgeUnder, Boolean ifAgeOver, StackTraceElement t) throws InterruptedException, IOException{
		return reopenContent(driver, title, type, author, published, ifAgeUnder, ifAgeOver, true, t);
	}
	
	/**
	 * Re-oprens (edit) any Contents using built-in "Filter" function
	 * @throws IOException
	 * @throws InterruptedException
	 */
	@SuppressWarnings("finally")
	public Boolean reopenContent(WebDriver driver, String title, String type, String author, String published, Boolean ifAgeUnder, Boolean ifAgeOver, Boolean ifPrompt, StackTraceElement t) throws InterruptedException, IOException{
		Boolean ifContent = false;
		try {
			getUrlWaitUntil(driver, 15, Common.adminContentURL);
			filterAllContent(driver, title, type, author, published, ifAgeUnder, ifAgeOver, t);
			ifContent = (driver.findElements(By.xpath(Drupal.messageNoContentAvailable)).size() == 0);
			if(ifContent) {
			waitUntilElementPresence(driver, 15, Drupal.adminContentRowFirstEdit, "First Row To Edit", t, ifPrompt);
			driver.findElement(By.xpath(Drupal.adminContentRowFirstEdit)).click();
			waitUntilElementPresence(driver, 15, By.id(Drupal.title), "Title", t, ifPrompt);
			}
		} catch(Exception e) { if(ifPrompt) { getExceptionDescriptive(e, t, driver); } } finally { return ifContent; }
	}
	
	/**
	 * Searches and Re-oprens (edit) any Brand using built-in "Filter" function and filtered out based on single user-defined acceptance criteria
	 * @throws IOException
	 * @throws InterruptedException
	 */
	@SuppressWarnings("finally")
	public Boolean reopenBrand(WebDriver driver, String title, String type, String author, String published, Boolean ifAgeUnder, Boolean ifAgeOver, Boolean ifPrompt, StackTraceElement t,
			int acceptanceItem, String acceptanceValue) throws InterruptedException, IOException{
		Boolean ifContent = false;
		try {
			int i = 0;
			Boolean ifExceptance = false;
			while ((i < 25) && !ifExceptance) {
				getUrlWaitUntil(driver, 15, Common.adminContentURL);
				filterAllContent(driver, title, type, author, published, ifAgeUnder, ifAgeOver, t);
				ifContent = (driver.findElements(By.xpath(Drupal.messageNoContentAvailable)).size() == 0) &&
						    (driver.findElements(By.xpath(Drupal.adminContentRowEdit(i))).size() == 1) ;
				if(ifContent) {
					waitUntilElementPresence(driver, 15, Drupal.adminContentRowFirstEdit, "First Row To Edit", t, ifPrompt);
					driver.findElement(By.xpath(Drupal.adminContentRowEdit(i))).click();
					waitUntilElementPresence(driver, 15, By.id(Drupal.title), "Title", t, ifPrompt);
					String[] content = readContent(driver, new RuntimeException().getStackTrace()[0], ifPrompt);
					ifExceptance = content[acceptanceItem].equals(acceptanceValue);
					i++;
				} else { ifExceptance = true; }
			}
		} catch(Exception e) { if(ifPrompt) { getExceptionDescriptive(e, t, driver); } } finally { return ifContent; }
	}
	
	/**
	 * Searches and Re-oprens (edit) any Video using built-in "Filter" function and filtered out based on user-defined Publishing criteria
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public String reopenVideo(WebDriver driver, String title, Boolean ifPublished, Boolean ifAgeUnder, Boolean ifAgeOver, StackTraceElement t) throws InterruptedException, IOException{
	   return reopenVideo(driver, title, ifPublished, true, ifAgeUnder, ifAgeOver, t);
	}
	
	/**
	 * Searches and Re-oprens (edit) any Video using built-in "Filter" function and filtered out based on user-defined Publishing criteria
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public String reopenVideo(WebDriver driver, String title, Boolean ifPublished, Boolean ifRepublish, Boolean ifAgeUnder, Boolean ifAgeOver, StackTraceElement t) throws InterruptedException, IOException{
	       // FILTER AND EDIT THE CONTENT BY "VIDEO" AND "PUBLISH" AS "YES":
		   String videoTitle = "";
		   String published = "Yes", unpublished = "No";
		   String publish = "Publish", unpublish = "Unpublish";
		   if(!ifPublished) { unpublished = "Yes"; published = "No"; publish = "Unpublish"; unpublish = "Publish"; }
		   Boolean acception = false;
		   int i = 1;
		   while (!acception) {
			   if(i == 1) { filterAllContent(driver, title, "Video", "", published, ifAgeUnder, ifAgeOver, t); }
		 	   if( driver.findElements(By.xpath(Drupal.messageNoContentAvailable)).size() == 1 ) {
		 		   filterAllContent(driver, title, "Video", "", unpublished, ifAgeUnder, ifAgeOver, t);
			       operateOnContent(driver, publish, !ifRepublish, t);
			       }
		 	   try {
		 		   // (RE)OPEN AND CHECK:
		 		   if(ifRepublish) { i = 1; }
		 		   getUrlWaitUntil(driver, 15, Common.adminContentURL);
		 		   waitUntilElementPresence(driver, 15, Drupal.adminContentRowFirstEdit, "First Row To Edit", t, false);
				   driver.findElement(By.xpath(Drupal.adminContentRowEdit(i))).click();
			       waitUntilElementPresence(driver, 5, Drupal.tileVerticalTab, "Tile Vertical Tab (Video)", t, false);
			 	   // PLACE VIDEO TO TILE WITH TILE PLACEMENT ASSERTION:
			       videoTitle = driver.findElement(By.id(Drupal.title)).getAttribute("value");
			       Boolean five = checkBoxStatus(driver, By.id(Drupal.ageGroup5), false, false, t).equals(ifAgeUnder);
			       Boolean six = checkBoxStatus(driver, By.id(Drupal.ageGroup6), false, false, t).equals(ifAgeOver);
			       acception = five && six;
			       if(ifAgeUnder && !ifAgeOver) { acception = five; }
			       if(!ifAgeUnder && ifAgeOver) { acception = six;  }
			       } catch (Exception e) {}
		       if(!acception) {
		    	   if(ifRepublish){ operateOnContent(driver, unpublish, false, t); }
		    	   i++;
		       }
	       }
		   return videoTitle;
		}
	
	/**
	 * Deletes all the Contents by Content type ("" for all types) on user demand
	 * @throws IOException
	 */
	public void deleteAllContent(WebDriver driver, String title, String type, String user, StackTraceElement t) throws InterruptedException, IOException{
		try {
			smartLogIn(driver);
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
			
			List<WebElement> list = driver.findElements(By.xpath(Drupal.messageNoContentAvailable));

			int i = 1;
			while ((list.size() == 0) && (driver.findElements(By.xpath(Drupal.errorAjax)).size() < 1)) {
			fileWriterPrinter("\nPAGE-" + i + ": DELETING...");
			waitUntilElementVisibility(driver, 30, Drupal.allInOneCheckBox, "\"Select All\"", new Exception().getStackTrace()[0]);
			ajaxProtectedClick(driver, Drupal.allInOneCheckBox, "Select All", false, "", true, false); //checks the "Select All" check-box
			
			// all rows selection			
			List<WebElement> elements = driver.findElements(By.xpath(Drupal.allRowsSelectorButton));
			if (elements.size() > 0) {
			   WebElement element = driver.findElement(By.xpath(Drupal.allRowsSelectorButton));
			   fileWriterPrinter(element.getAttribute("value"));
			   ((JavascriptExecutor)driver).executeScript("arguments[0].click();", element);
			}
			
			dropwDownListBox = driver.findElement(By.id("edit-operation"));
			clickThis = new Select(dropwDownListBox);
			Thread.sleep(2000);
			clickThis.selectByVisibleText("Delete");
			Thread.sleep(2000);	

			driver.findElement(By.xpath(Drupal.executeButton)).click(); // 'Execute' button;
			waitUntilElementVisibility(driver, 30, "//*[contains(text(),'You selected the following')]", "\"You selected the following\"", new Exception().getStackTrace()[0]);
			driver.findElement(By.id(Drupal.submit)).click(); // 'Confirm' button
			waitUntilElementInvisibility(driver, 5, By.id(Drupal.submit), "\"Save\" Button", new Exception().getStackTrace()[0]);
			waitUntilElementInvisibility(driver, 600, By.id(Drupal.progress), "Progress Bar", new Exception().getStackTrace()[0]);
			
			if(driver.findElements(By.xpath(Drupal.errorAjax)).size() > 0) { assertWebElementNotExist(driver, t, Drupal.errorAjax); }
			    
			waitUntilElementVisibility(driver, 30, Drupal.statusPerformedDelete, "\"Performed Delete\"", new Exception().getStackTrace()[0]);
			By message = By.xpath(Drupal.statusPerformedMessage);
			if (driver.findElements(message).size() > 0) { fileWriterPrinter(driver.findElement(message).getText()); }
			list = driver.findElements(By.xpath(Drupal.messageNoContentAvailable));
			i++;
			}
		    } catch(Exception e) { getExceptionDescriptive(e, t, driver); } finally { logOut(driver); }
	}
				
	/**
	 * Submits Content and reports result (if final URL doesn't start with String - failure condition)
	 * @throws IOException
	 * @throws NumberFormatException
	 * @throws InterruptedException
	 */
	public int contentSubmit(String URLstartsWith, WebDriver driver, int iteration) throws IOException, NumberFormatException, InterruptedException {
	   String type = driver.findElement(By.xpath("//h1[@class='page-title']")).getText();
	   String previousURL = driver.getCurrentUrl();
	   driver.findElement(By.id(Drupal.submit)).click();
	   waitUntilUrl(driver, 15, previousURL);
	   iteration++;
	   String suffix = "-" + getNumberSuffix(iteration);
	   String success = "Successful \"" + type + "\" process!"; 
	   String   issue = "Not a successful \"" + type + "\" process...will try again..." + "(attempt #" + iteration + ")";
	   if (iteration > 1) { success = success + " (on " + iteration + suffix + " attempt)"; }
	   if (! driver.getCurrentUrl().startsWith(URLstartsWith)) { 
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
	 * Submits Content and reports result (if final URL doesn't contain String - failure condition)
	 * @throws IOException
	 * @throws NumberFormatException
	 * @throws InterruptedException
	 */
	public int contentSubmit(WebDriver driver, String URLcontains, int iteration) throws IOException, NumberFormatException, InterruptedException {
	   String type = driver.findElement(By.xpath("//h1[@class='page-title']")).getText();
	   String previousURL = driver.getCurrentUrl();
	   driver.findElement(By.id(Drupal.submit)).click();
	   waitUntilUrl(driver, 15, previousURL);
	   iteration++;
	   String suffix = "-" + getNumberSuffix(iteration);
	   String success = "Successful \"" + type + "\" process!"; 
	   String   issue = "Not a successful \"" + type + "\" process...will try again..." + "(attempt #" + iteration + ")";
	   if (iteration > 1) { success = success + " (on " + iteration + suffix + " attempt)"; }
	   if (! driver.getCurrentUrl().contains(URLcontains)) { 
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
	 * Submits Content and reports result (if final URL doesn't end with String - failure condition)
	 * @throws IOException
	 * @throws NumberFormatException
	 * @throws InterruptedException
	 */
	public int contentSubmit(WebDriver driver, int iteration, String URLendsWith, Boolean ifRetry) throws IOException, NumberFormatException, InterruptedException {
	   String type = driver.findElement(By.xpath("//h1[@class='page-title']")).getText();
	   String previousURL = driver.getCurrentUrl();
	   driver.findElement(By.id(Drupal.submit)).click();
	   waitUntilUrl(driver, 15, previousURL);
	   iteration++;
	   String suffix = "-" + getNumberSuffix(iteration);
	   String success = "Successful \"" + type + "\" process!"; 
	   String   issue = "Not a successful \"" + type + "\" process...";
	   String attempt =  "will try again..." + "(attempt #" + iteration + ")";
	   if(! ifRetry ) { attempt = ""; }
	   if (iteration > 1) { success = success + " (on " + iteration + suffix + " attempt)"; }   
	   Boolean error = false;	   
	   if ( URLendsWith.length() == 0 ) { error =   driver.getCurrentUrl().equals(previousURL); }
	   else                             { error = ! driver.getCurrentUrl().endsWith(URLendsWith); }
	   if ( error ) {
		   fileWriterPrinter(issue + attempt);
		   if( driver.findElements(By.xpath(Drupal.errorMessage)).size() > 0 ) {
			   String text = driver.findElement(By.xpath(Drupal.errorConsole)).getText();			   
			   String message=getTextLine(text, 1), prompt=getTextLine(text, 2), space = "";
			   if(prompt.length() > 0) { space = ": "; }
			   fileWriterPrinter(message + space + prompt);	   		
	   		if( (iteration == 1) && ifRetry ) { getScreenShot(new RuntimeException().getStackTrace()[0], message, driver); }
	   		}	   
	   } else { fileWriterPrinter(success); }
	   return iteration;	   
	}
	
	/** Converts Boolean status value into user-friendly message */
	public String checkBoxStatus(Boolean ifChecked) { if(ifChecked) { return "CHECKED"; } else { return "UN-CHECKED"; } }
	
	/**
	 * Detects and Enforces the Check-Box to be checked
	 * @throws IOException 
	 * @throws NumberFormatException 
	 * @throws InterruptedException 
	 */
	public Boolean checkBoxStatus(WebDriver driver, By element, Boolean ifCheckOn, Boolean ifAssert, StackTraceElement t) throws NumberFormatException, IOException, InterruptedException {
		Boolean status = false;
		String name = "";
		if ( driver.findElements(element).size() == 1) {
			String id = driver.findElement(element).getAttribute("id");
			if( (id.length() == 0) || (id.equals(null)) ){ name = " "; } 
			else { 
				  name = Common.IdToXpath(id) + "/following-sibling::label";
				  name = " \"" + driver.findElement(By.xpath(name)).getText() + "\" ";
				  }
			status = Boolean.valueOf(driver.findElement(element).getAttribute("checked"));
			fileWriterPrinter("Check-Box" + padRight(name, 36 - name.length()) + "     status:   " + checkBoxStatus(status)); 
			if ( (!status) && ifCheckOn ) { 
				driver.findElement(element).click(); Thread.sleep(1000);
				status = Boolean.valueOf(driver.findElement(element).getAttribute("checked"));
				fileWriterPrinter("Check-Box" + padRight(name, 36 - name.length()) + " new status:   " + checkBoxStatus(status));
				}
			} else { if(ifAssert) { assertWebElementExist(driver, t, element); } }
		return status;
	}
	
	/**
	 * Detects and Enforces the "Visible on character banner" Check-Box to be checked
	 * @throws IOException 
	 * @throws NumberFormatException 
	 * @throws InterruptedException 
	 */
	public Boolean checkVisibleOnCharacterBanner(WebDriver driver, Boolean ifCheckOn, StackTraceElement t) throws NumberFormatException, IOException, InterruptedException {
		return checkBoxStatus(driver, By.id(Drupal.characterBannerVisibleOn), ifCheckOn, true, t);
	}
	
	/**
	 * Enforces the "Visible on character banner" Check-Box to be checked
	 * @throws IOException 
	 * @throws NumberFormatException 
	 * @throws InterruptedException 
	 */
	public Boolean checkVisibleOnCharacterBanner(WebDriver driver) throws NumberFormatException, IOException, InterruptedException {
		return checkVisibleOnCharacterBanner(driver, true, new RuntimeException().getStackTrace()[0]);
	}
	
	/**
	 * Create a Character Brand
	 * @throws AWTException 
	 * @throws IOException
	 */
//	@SuppressWarnings("finally")
	public long createCharacterBrand(WebDriver driver, String title, String description, int assetID, Boolean ifNoAutoVideoTiles, Boolean ifAgeUnder, Boolean ifAgeOver, Boolean ifAlternateText, Boolean ifSubmit, Boolean ifRetry, StackTraceElement t) throws AWTException, InterruptedException, IOException
	  {
	   long fingerprint = System.currentTimeMillis();
	   String tab, browse, upload;
//     try {
	   int i = 0;
	   Boolean ifTitle = true;
	   while ( (ifTitle || (i == 0)) && (i < 5) ) {
            getUrlWaitUntil(driver, 15, Drupal.characterBrand);
			waitUntilElementPresence(driver, 15, By.id(Drupal.title), "Title", new Exception().getStackTrace()[0]);
			
			driver.findElement(By.id(Drupal.title)).clear();
			driver.findElement(By.id(Drupal.title)).sendKeys(title);
			
			driver.findElement(By.xpath(Drupal.description)).clear();
			driver.findElement(By.xpath(Drupal.description)).sendKeys(description);

			if (ifAgeUnder) { driver.findElement(By.id(Drupal.ageGroup5)).click(); }
			if (ifAgeOver)  { driver.findElement(By.id(Drupal.ageGroup6)).click(); }
			
			driver.findElement(By.id(Drupal.keywords)).clear();
			driver.findElement(By.id(Drupal.keywords)).sendKeys(title + " (keywords)");

			if (assetID > 0) { driver.findElement(By.id(Drupal.programTelescopeAssetId)).sendKeys(String.valueOf(assetID)); }
			
			checkBoxStatus(driver, By.id(Drupal.noAutoVideoTiles), ifNoAutoVideoTiles, false, new Exception().getStackTrace()[0]);
			
			tab    = Drupal.characterBannerVerticalTab;
			browse = Drupal.characterBannerBrowse;
			upload = Drupal.characterBannerUpload;
			upload(driver, "bubble.jpg", tab, browse, upload, "thumbnail", t);
			checkVisibleOnCharacterBanner(driver);
			
		    tab    = Drupal.heroBoxVerticalTab;
			browse = Drupal.heroBoxBrowse;
			upload = Drupal.heroBoxUpload;
			upload(driver, "hero.jpg", tab, browse, upload, "image", t);

			tab    = Drupal.tileVerticalTab;
			browse = Drupal.tileSmallBrowse;
			upload = Drupal.tileSmallUpload;
			upload(driver, "small.jpg", tab, browse, upload, "image", t);
			if(ifAlternateText) { driver.findElement(By.xpath(Drupal.alternateSmall)).clear(); driver.findElement(By.xpath(Drupal.alternateSmall)).sendKeys(Drupal.alternateSmallText); }
			
			if(ifSubmit) { i = contentSubmit(driver, i, reFormatStringForURL(title), ifRetry); }
			if(ifRetry)  { if(title.length() > 0) { ifTitle = (! driver.getCurrentUrl().endsWith(reFormatStringForURL(title))); } }
			if( (!ifSubmit) || (!ifRetry) ) { i = 5; }
			}
			
	   if(ifSubmit && ifRetry) {
		   String url = driver.getCurrentUrl();
		   String expected = reFormatStringForURL(title.substring(0, Drupal.titleMaxCharsNumber));
		   String actual = url.substring( (url.length() - expected.length()), url.length() );
		   assertEquals(driver, t, actual, expected);
		   }
	   
//	   } catch(Exception e) { getScreenShot(new Exception().getStackTrace()[0], e, driver); } finally { return fingerprint; }    
	   return fingerprint;
	}
	
	/**
	 * Read Brand Data
	 * @throws NumberFormatException
	 * @throws IOException
	 * @throws InterruptedException 
	 */
	public String[] readContent(WebDriver driver, StackTraceElement t, Boolean ifPrompt) throws NumberFormatException, IOException, InterruptedException {
		// DECLARATION:
		String name, tab, thumbnail;
		String title, description, ageGroup5, ageGroup6, assetID, noAutoVideoTiles, bannerImage, visibleOnCharacterBanner, heroImage, tileSmallImage, alternateSmall, tileLargeImage, alternateLarge, badge;
		// TYPE:
		String contentTypeXpath = "//h1[@class='page-title']/em[text()]";
		String contentType = getText(driver, contentTypeXpath).replace("Edit ", "");
		name = "CONTENT  TYPE: ";
		if(ifPrompt) { fileWriterPrinter("\n" + name + padSpace(50 - name.length()) + contentType); }
		// TITLE:
		title = driver.findElement(By.id(Drupal.title)).getAttribute("value");
		name = "TITLE: ";
		if(ifPrompt) { fileWriterPrinter(name + padSpace(50 - name.length()) + title); }
		// DESCRIPTION:
		description = getText(driver, Drupal.description);
		name = "DESCRIPTION: ";
		if(ifPrompt) { fileWriterPrinter(name + padSpace(50 - name.length()) + description); }
		// AGE 5 AND UNDER:
		ageGroup5 = checkBoxStatus(checkBoxStatus(driver, By.id(Drupal.ageGroup5), false, false, t));
		// 6 AND OVER:
		ageGroup6 = checkBoxStatus(checkBoxStatus(driver, By.id(Drupal.ageGroup6), false, false, t));
		// ASSET ID:
		assetID = uploadReader("", Drupal.programTelescopeAssetId, driver);
		name = "ASSET ID: ";
		if(ifPrompt) { fileWriterPrinter(name + padSpace(50 - name.length()) + assetID); }
		// NO AUTO VIDEO TILES:
		noAutoVideoTiles = checkBoxStatus(checkBoxStatus(driver, By.id(Drupal.noAutoVideoTiles), false, false, t));
		// BANNER IMAGE:
		tab    = Drupal.characterBannerVerticalTab;
		thumbnail = Drupal.characterBannerThumbnail;
		bannerImage = uploadReader(driver, tab, thumbnail);
		name = "BANNER IMAGE: ";
		if(ifPrompt) { fileWriterPrinter(name + padSpace(50 - name.length()) + bannerImage); }
		// VISIBLE ON CHARACTER BANNER:
		visibleOnCharacterBanner = checkBoxStatus(checkVisibleOnCharacterBanner(driver, false, t));
		// HERO IMAGE:
		tab    = Drupal.heroBoxVerticalTab;
		thumbnail = Drupal.heroBoxThumbnail;
		heroImage = uploadReader(driver, tab, thumbnail);
		name = "HERO IMAGE: ";
		if(ifPrompt) { fileWriterPrinter(name + padSpace(50 - name.length()) + heroImage); }
		// TILE SMALL IMAGE:
		tab    = Drupal.tileVerticalTab;
		thumbnail = Drupal.tileSmallThumbnail;
		tileSmallImage = uploadReader(driver, tab, thumbnail);
		name = "TILE SMALL IMAGE: ";
		if(ifPrompt) { fileWriterPrinter(name + padSpace(50 - name.length()) + tileSmallImage); }
		// ALTERNATE TEXT (SMALL):
		alternateSmall = uploadReader(driver, tab, Drupal.alternateSmall);
		name = "ALTERNATE TEXT (SMALL): ";
		if(ifPrompt) { fileWriterPrinter(name + padSpace(50 - name.length()) + alternateSmall); }
		// TILE LARGE IMAGE:
		tab    = Drupal.tileVerticalTab;
		thumbnail = Drupal.tileLargeThumbnail;
		tileLargeImage = uploadReader(driver, tab, thumbnail);
		name = "TILE LARGE IMAGE: ";
		if(ifPrompt) { fileWriterPrinter(name + padSpace(50 - name.length()) + tileLargeImage); }
		// ALTERNATE TEXT (LARGE):
		alternateLarge = uploadReader(driver, tab, Drupal.alternateLarge);
		name = "ALTERNATE TEXT (LARGE): ";
		if(ifPrompt) { fileWriterPrinter(name + padSpace(50 - name.length()) + alternateLarge); }
		// BADGE:
		tab    = Drupal.tileVerticalTab;
		thumbnail = Drupal.badgeThumbnail;
		badge = uploadReader(driver, tab, thumbnail);
		name = "BADGE: ";
		if(ifPrompt) { fileWriterPrinter(name + padSpace(50 - name.length()) + badge + "\n"); }

		String[] s = {
				title,                    // [0]
				description,              // [1]
				ageGroup5,                // [2]
				ageGroup6,                // [3]
				assetID,                  // [4]
				noAutoVideoTiles,         // [5]
				bannerImage,              // [6]
				visibleOnCharacterBanner, // [7]
				heroImage,                // [8]
				tileSmallImage,           // [9]
				alternateSmall,           // [10]
				tileLargeImage,           // [11]
				alternateLarge,           // [12]
				badge,                    // [13]
				};
		return s;
	}
	
	/**
	 * Create a Custom Brand
	 * @throws AWTException 
	 * @throws IOException
	 */
//	@SuppressWarnings("finally")
	public long createCustomBrand(WebDriver driver, String title, String description, Boolean ifAgeUnder, Boolean ifAgeOver, Boolean ifAlternateText,
			                      Boolean ifSubmit, Boolean ifRetry, StackTraceElement t,
			                      String bannerImage, String heroImage, String tileSmallImage, String tileLargeImage, String badge, String tile, Boolean ifPublish
			                     ) throws AWTException, InterruptedException, IOException
	  {
	   long fingerprint = System.currentTimeMillis();
	   String tab, browse, upload;
//     try {
	   int i = 0;
	   Boolean ifTitle = true;
	   while ( (ifTitle || (i == 0)) && (i < 5) ) {
            getUrlWaitUntil(driver, 15, Drupal.customBrand);
			waitUntilElementPresence(driver, 15, By.id(Drupal.title), "Title", new Exception().getStackTrace()[0]);
			if(title.length() > 0) {
				driver.findElement(By.id(Drupal.title)).clear();
				driver.findElement(By.id(Drupal.title)).sendKeys(title);
				}
			
			if(description.length() > 0) {
				driver.findElement(By.xpath(Drupal.description)).clear();
				driver.findElement(By.xpath(Drupal.description)).sendKeys(description);
				}
			
			if (ifAgeUnder) { driver.findElement(By.id(Drupal.ageGroup5)).click(); }
			if (ifAgeOver)  { driver.findElement(By.id(Drupal.ageGroup6)).click(); }
			
			if(title.length() > 0) {
				driver.findElement(By.id(Drupal.keywords)).clear();
				driver.findElement(By.id(Drupal.keywords)).sendKeys(title + " (keywords)");
				}
			
			if(bannerImage.length() > 0) {
				tab    = Drupal.characterBannerVerticalTab;
				browse = Drupal.characterBannerBrowse;
				upload = Drupal.characterBannerUpload;
				upload(driver, bannerImage, tab, browse, upload);
				checkVisibleOnCharacterBanner(driver);
				}

			if(heroImage.length() > 0) {
			    tab    = Drupal.heroBoxVerticalTab;
				browse = Drupal.heroBoxBrowse;
				upload = Drupal.heroBoxUpload;
				upload(driver, heroImage, tab, browse, upload);
				}

			if(tileSmallImage.length() > 0) {
				tab    = Drupal.tileVerticalTab;
				browse = Drupal.tileSmallBrowse;
				upload = Drupal.tileSmallUpload;
				upload(driver, tileSmallImage, tab, browse, upload);
				if(ifAlternateText) { driver.findElement(By.xpath(Drupal.alternateSmall)).clear(); driver.findElement(By.xpath(Drupal.alternateSmall)).sendKeys(Drupal.alternateSmallText); }
				}

			if(tileLargeImage.length() > 0) {
				tab    = Drupal.tileVerticalTab;
				browse = Drupal.tileLargeBrowse;
				upload = Drupal.tileLargeUpload;
				upload(driver, tileLargeImage, tab, browse, upload);
				if(ifAlternateText) { driver.findElement(By.xpath(Drupal.alternateLarge)).clear(); driver.findElement(By.xpath(Drupal.alternateLarge)).sendKeys(Drupal.alternateLargeText); }
				}
			
			if(badge.length() > 0) {
				tab    = Drupal.tileVerticalTab;
				browse = Drupal.badgeBrowse;
				upload = Drupal.badgeUpload;
				upload(driver, badge, tab, browse, upload);
				if(ifAlternateText) { driver.findElement(By.xpath(Drupal.alternateLarge)).clear(); driver.findElement(By.xpath(Drupal.alternateLarge)).sendKeys(Drupal.alternateLargeText); }
				}
			
			if(tile.length() > 0) { addTilePlacement(driver, tile, ifPublish, false);}

			if(ifSubmit) { i = contentSubmit(driver, i, reFormatStringForURL(title, Drupal.titleMaxCharsNumber), ifRetry); }
			if(ifRetry)  { if(title.length() > 0) { ifTitle = (! driver.getCurrentUrl().endsWith(reFormatStringForURL(title, Drupal.titleMaxCharsNumber))); } }
			if( (!ifSubmit) || (!ifRetry) ) { i = 5; }
			}
	   
	   if(ifSubmit && ifRetry) {
		   String url = driver.getCurrentUrl();
		   String expected = reFormatStringForURL(title.substring(0, Drupal.titleMaxCharsNumber));
		   String actual = url.substring( (url.length() - expected.length()), url.length() );
		   assertEquals(driver, t, actual, expected);
		   }	

//	   } catch(Exception e) { getScreenShot(new Exception().getStackTrace()[0], e, driver); } finally { return fingerprint; }    
	   return fingerprint;
	}
	
	/**
	 * Create a Custom Brand
	 * @throws AWTException 
	 * @throws IOException
	 */
//	@SuppressWarnings("finally")
	public long createCustomBrand(WebDriver driver, String title, String description, Boolean ifAgeUnder, Boolean ifAgeOver, Boolean ifAlternateText,
			                      Boolean ifSubmit, Boolean ifRetry, StackTraceElement t,
			                      String bannerImage, String heroImage, String titleSmallImage, String titleLargeImage, String tile, Boolean ifPublish
			                     ) throws AWTException, InterruptedException, IOException
	  {
		return createCustomBrand(driver, title, description, ifAgeUnder, ifAgeOver, ifAlternateText, ifSubmit, ifRetry, t, bannerImage, heroImage, titleSmallImage, titleLargeImage, "", tile, ifPublish);
	  }
	
	/**
	 * Create a Custom Brand
	 * @throws AWTException 
	 * @throws IOException
	 */
//	@SuppressWarnings("finally")
	public long createCustomBrand(WebDriver driver, String title, String description, Boolean ifAgeUnder, Boolean ifAgeOver, Boolean ifAlternateText,
			                      Boolean ifSubmit, Boolean ifRetry, StackTraceElement t,
			                      String bannerImage, String heroImage, String titleSmallImage, String titleLargeImage, String tile
			                     ) throws AWTException, InterruptedException, IOException
	  {
		return createCustomBrand(driver, title, description, ifAgeUnder, ifAgeOver, ifAlternateText, ifSubmit, ifRetry, t, bannerImage, heroImage, titleSmallImage, titleLargeImage, tile, true);
	  }
	
	/**
	 * Create a Custom Brand
	 * @throws AWTException 
	 * @throws IOException
	 */
	public long createCustomBrand(WebDriver driver, String title, String description, Boolean ifAgeUnder, Boolean ifAgeOver, Boolean ifSubmit, Boolean ifRetry, StackTraceElement t,
			                      String bannerImage, String heroImage, String titleSmallImage, String titleLargeImage
			                     ) throws AWTException, InterruptedException, IOException
	  { 
		return createCustomBrand(driver, title, description, ifAgeUnder, ifAgeOver, false, ifSubmit, ifRetry, t, bannerImage, heroImage, titleSmallImage, titleLargeImage, "");	  
	  }
	
	/**
	 * Create a Custom Brand
	 * @throws AWTException 
	 * @throws IOException
	 */
//	@SuppressWarnings("finally")
	public long createCustomBrand(WebDriver driver, String title, String description, Boolean ifAgeUnder, Boolean ifAgeOver, Boolean ifAlternateText, Boolean ifSubmit, Boolean ifRetry, StackTraceElement t) throws AWTException, InterruptedException, IOException
	  {
	   long fingerprint = System.currentTimeMillis();
	   String tab, browse, upload;
//     try {
	   int i = 0;
	   Boolean ifTitle = true;
	   while ( (ifTitle || (i == 0)) && (i < 5) ) {
            getUrlWaitUntil(driver, 15, Drupal.customBrand);
			waitUntilElementPresence(driver, 15, By.id(Drupal.title), "Title", new Exception().getStackTrace()[0]);
			
			driver.findElement(By.id(Drupal.title)).clear();
			if(title.length() > 0) { driver.findElement(By.id(Drupal.title)).sendKeys(title); }
			
			driver.findElement(By.xpath(Drupal.description)).clear();
			driver.findElement(By.xpath(Drupal.description)).sendKeys(description);

			if (ifAgeUnder) { driver.findElement(By.id(Drupal.ageGroup5)).click(); }
			if (ifAgeOver)  { driver.findElement(By.id(Drupal.ageGroup6)).click(); }
			
			driver.findElement(By.id(Drupal.keywords)).clear();
			driver.findElement(By.id(Drupal.keywords)).sendKeys(title + " (keywords)");

			tab    = Drupal.characterBannerVerticalTab;
			browse = Drupal.characterBannerBrowse;
			upload = Drupal.characterBannerUpload;
			upload(driver, "bubble.jpg", tab, browse, upload, "thumbnail", t);
			checkVisibleOnCharacterBanner(driver);

		    tab    = Drupal.heroBoxVerticalTab;
			browse = Drupal.heroBoxBrowse;
			upload = Drupal.heroBoxUpload;
			upload(driver, "hero.jpg", tab, browse, upload, "image", t);

			tab    = Drupal.tileVerticalTab;
			browse = Drupal.tileSmallBrowse;
			upload = Drupal.tileSmallUpload;
			upload(driver, "small.jpg", tab, browse, upload, "image", t);

			tab    = Drupal.tileVerticalTab;
			browse = Drupal.tileLargeBrowse;
			upload = Drupal.tileLargeUpload;
			upload(driver, "large.jpg", tab, browse, upload, "image", t);
			
			if(ifAlternateText) { 
				driver.findElement(By.xpath(Drupal.alternateSmall)).clear();
				driver.findElement(By.xpath(Drupal.alternateSmall)).sendKeys(Drupal.alternateSmallText);
				driver.findElement(By.xpath(Drupal.alternateLarge)).clear();
				driver.findElement(By.xpath(Drupal.alternateLarge)).sendKeys(Drupal.alternateLargeText);
				}
		
			if(ifSubmit) { i = contentSubmit(driver, i, reFormatStringForURL(title, Drupal.titleMaxCharsNumber), ifRetry); }
			if(ifRetry)  { if(title.length() > 0) { ifTitle = (! driver.getCurrentUrl().endsWith(reFormatStringForURL(title, Drupal.titleMaxCharsNumber))); } }
			if( (!ifSubmit) || (!ifRetry) ) { i = 5; }
			}
						
	   if(ifSubmit && ifRetry) {
		   String url = driver.getCurrentUrl();
		   String expected = reFormatStringForURL(title.substring(0, Drupal.titleMaxCharsNumber));
		   String actual = url.substring( (url.length() - expected.length()), url.length() );
		   assertEquals(driver, t, actual, expected);
		   }
	   
//	   } catch(Exception e) { getScreenShot(new Exception().getStackTrace()[0], e, driver); } finally { return fingerprint; }    
	   return fingerprint;
	}
	
//	/**
//	 * Create a Custom Brand using Robot
//	 * @throws AWTException 
//	 * @throws IOException
//	 */
////	@SuppressWarnings("finally")
//	public long createCustomBrand(WebDriver driver, String title, Boolean ifAgeUnder, Boolean ifAgeOver, Robot robot, long fingerprint) throws AWTException, InterruptedException, IOException
//	  {
//	   By browse, upload;
////     try {
//    	    int i = 0;
//    	    while (((! driver.getCurrentUrl().contains(String.valueOf(fingerprint))) || (i == 0)) && (i < 5)) {
//    	    getUrlWaitUntil(driver, 15, Drupal.customBrand);
//			waitUntilElementPresence(driver, 15, By.id(Drupal.title), "Title", new Exception().getStackTrace()[0]);
//			
//			driver.findElement(By.id(Drupal.title)).clear();
//			driver.findElement(By.id(Drupal.title)).sendKeys(title);
//			
//			driver.findElement(By.xpath(Drupal.description)).clear();
//          driver.findElement(By.xpath(Drupal.description)).sendKeys("This is \"" + title + "\" Description");
//
//			if (ifAgeUnder) { driver.findElement(By.id(Drupal.ageGroup5)).click(); }
//			if (ifAgeOver)  { driver.findElement(By.id(Drupal.ageGroup6)).click(); }
//			
//			driver.findElement(By.id(Drupal.keywords)).clear();
//			driver.findElement(By.id(Drupal.keywords)).sendKeys(title + " (keywords)");
//			
//			driver.findElement(By.xpath(Drupal.characterBannerVerticalTab)).click();
//			browse = By.xpath(Drupal.characterBannerBrowse);
//			upload = By.xpath(Drupal.characterBannerUpload);					
//			uploader(driver, "bubble.jpg", browse, upload, robot, "thumbnail");
//			checkVisibleOnCharacterBanner(driver);
//	
//		    driver.findElement(By.xpath(Drupal.heroBoxVerticalTab)).click();
//			browse = By.xpath(Drupal.heroBoxBrowse);
//			upload = By.xpath(Drupal.heroBoxUpload);
//			uploader(driver, "hero.jpg", browse, upload, robot, "image");
//		    
//		    driver.findElement(By.xpath(Drupal.tileVerticalTab)).click();
//			browse = By.xpath(Drupal.tileSmallBrowse);
//			upload = By.xpath(Drupal.tileSmallUpload);
//			uploader(driver, "small.jpg", browse, upload, robot, "image");
//		    
//		    driver.findElement(By.xpath(Drupal.tileVerticalTab)).click();
//			browse = By.xpath(Drupal.tileLargeBrowse);
//			upload = By.xpath(Drupal.tileLargeUpload);
//			uploader(driver, "large.jpg", browse, upload, robot, "image");
//
//			i = contentSubmit(driver, fingerprint, i);			
//          }
//	   String url = driver.getCurrentUrl();
//	   String expected = reFormatStringForURL(title.substring(0, Drupal.titleMaxCharsNumber));
//	   String actual = url.substring( (url.length() - expected.length()), url.length() );
//	   assertEquals(driver, t, actual, expected);
//	   
////   } catch(Exception e) { getScreenShot(new Exception().getStackTrace()[0], e, driver); } finally { return fingerprint; }    
//	   return fingerprint;
//	}
//	
//	/**
//	 * Create a Custom Brand using Robot
//	 * @throws AWTException 
//	 * @throws IOException
//	 */
////	@SuppressWarnings("finally")
//	public long createCustomBrand(WebDriver driver, String title, Boolean ifAgeUnder, Boolean ifAgeOver, Robot robot) throws AWTException, InterruptedException, IOException
//	  {
//	   long fingerprint = System.currentTimeMillis();
//	   return createCustomBrand(driver, title, ifAgeUnder, ifAgeOver, robot, fingerprint);
//	  }
	
	/**
	 * Create URL Redirect
	 * @throws IOException
	 */
	public void createUrlRedirect(WebDriver driver, String sourceURL, String destinationURL) throws IOException, InterruptedException {
     	getUrlWaitUntil(driver, 15, Drupal.urlRedirectsAdd);
		waitUntilElementPresence(driver, 15, By.id(Drupal.urlRedirectsFrom), "From", new Exception().getStackTrace()[0]);
		driver.findElement(By.id(Drupal.urlRedirectsFrom)).clear();
		Thread.sleep(1000);
	    driver.findElement(By.id(Drupal.urlRedirectsFrom)).sendKeys(sourceURL);
		driver.findElement(By.id(Drupal.urlRedirectsTo)).clear();
		Thread.sleep(1000);
	    driver.findElement(By.id(Drupal.urlRedirectsTo)).sendKeys(destinationURL);
	    driver.findElement(By.id(Drupal.submit)).click();
	    Thread.sleep(1000);
	    fileWriterPrinter("\n" + "URL REDIRECT ADDED!");
	    fileWriterPrinter("FROM: " + sourceURL);
	    fileWriterPrinter("  TO: " + destinationURL + "\n");
	    }
	
	/**
	 * Delete URL Redirect
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public void deleteUrlRedirect(WebDriver driver, String filter) throws IOException, InterruptedException {
		getUrlWaitUntil(driver, 10, Drupal.urlRedirects);	
		waitUntilElementPresence(driver, 5, By.id(Drupal.filterRedirects), "Filter", new Exception().getStackTrace()[0], false);
		if( driver.findElements(By.xpath(Drupal.selectAllRedirectsCheckBox)).size() > 0) {
			if(filter.length() > 0) {
				driver.findElement(By.id(Drupal.filterRedirects)).clear();
			    driver.findElement(By.id(Drupal.filterRedirects)).sendKeys(filter);
			    driver.findElement(By.id(Drupal.filterSubmit)).click();
			    waitUntilElementPresence(driver, 15, By.id(Drupal.filterReset), "Reset", new Exception().getStackTrace()[0], false);
			    }
			if( driver.findElements(By.xpath(Drupal.noUrlRedirects)).size() == 0) {
			    driver.findElement(By.xpath(Drupal.selectAllRedirectsCheckBox)).click();
			    waitUntilElementPresence(driver, 15, By.id(Drupal.redirectUpdateSubmit), "Update", new Exception().getStackTrace()[0], false);
			    driver.findElement(By.id(Drupal.redirectUpdateSubmit)).click();
			    waitUntilElementPresence(driver, 15, By.xpath(Drupal.redirectUpdateWarning), "Warning", new Exception().getStackTrace()[0], false);
			    driver.findElement(By.id(Drupal.submit)).click();
			    waitUntilElementInvisibility(driver, 15, By.xpath(Drupal.redirectUpdateWarning), "Warning", false, new Exception().getStackTrace()[0]);
			    }
			}
		}
	
	/**
	 * Create a Video
	 * @throws AWTException 
	 * @throws IOException
	 */
//	@SuppressWarnings("finally")
	public long createVideo(WebDriver driver, String title, String shortDescription, String longDescription, String brandTitle, 
			                  String brightcoveRefID, String telescopeAssetId,
			                  Boolean ifAgeUnder, Boolean ifAgeOver, Boolean ifPublish, 
			                  Boolean ifSubmit, Boolean ifRetry, StackTraceElement t) throws Exception {
		long fingerprint = System.currentTimeMillis();
        // try {
		int i = 0;
		Boolean ifTitle = true;
		while ( (ifTitle || (i == 0)) && (i < 5) ) {
			getUrlWaitUntil(driver, 10, Drupal.video, false);
			waitUntilElementPresence(driver, 10, By.id(Drupal.title), "Title", t);

		    driver.findElement(By.id(Drupal.title)).clear();
		    driver.findElement(By.id(Drupal.title)).sendKeys(title);
		    
            driver.findElement(By.id(Drupal.editShortDescription)).clear();
            driver.findElement(By.id(Drupal.editShortDescription)).sendKeys(shortDescription);
            driver.findElement(By.id(Drupal.editLongDescription)).clear();
            driver.findElement(By.id(Drupal.editLongDescription)).sendKeys(longDescription);

			if (ifAgeUnder) { driver.findElement(By.id(Drupal.ageGroup5)).click(); }
			if (ifAgeOver)  { driver.findElement(By.id(Drupal.ageGroup6)).click(); }

		    driver.findElement(By.id("edit-field-program-und-0-target-id")).clear();
		    driver.findElement(By.id("edit-field-program-und-0-target-id")).sendKeys(brandTitle);
		    int size = waitUntilElementList(driver, 5, Common.autoComplete, "auto-complete").size();
            if (size == 1) { try { driver.findElement(By.xpath(Common.autoComplete)).click(); } catch(Exception e) { } }
            waitUntilElementInvisibility(driver, 15, Common.autoComplete, "auto-complete", new Exception().getStackTrace()[0]);
              
		    driver.findElement(By.id(Drupal.brightcoveRefID)).clear();
		    driver.findElement(By.id(Drupal.brightcoveRefID)).sendKeys(brightcoveRefID);
		    
		    driver.findElement(By.id(Drupal.telescopeAssetId)).clear();
		    driver.findElement(By.id(Drupal.telescopeAssetId)).sendKeys(telescopeAssetId);
		    
		    if(ifPublish){
		    	driver.findElement(By.xpath(Drupal.publishingOptionsVerticalTab)).click();
		    	waitUntilElementPresence(driver, 10, By.id(Drupal.publishingOptionsPublishedCheckBoxId), "Published Check Box", t);
		    	driver.findElement(By.id(Drupal.publishingOptionsPublishedCheckBoxId)).click();
		    	}
		    
			if(ifSubmit) { i = contentSubmit(driver, i, reFormatStringForURL(title), ifRetry); }
			if(ifRetry)  { if(title.length() > 0) { ifTitle = (! driver.getCurrentUrl().endsWith(reFormatStringForURL(title))); } }
			if( (!ifSubmit) || (!ifRetry) ) { i = 5; }
			}
			
	   if(ifSubmit && ifRetry) {
		   String url = driver.getCurrentUrl();
		   String expected = reFormatStringForURL(title);
		   String actual = url.substring( (url.length() - expected.length()), url.length() );
		   assertEquals(driver, t, actual, expected);
		   }
	   
//	   } catch(Exception e) { getScreenShot(new Exception().getStackTrace()[0], e, driver); } finally { return fingerprint; }    
	   return fingerprint;
	}
	
	/**
	 * Navigates to Drupal "Manage Tile" with option to perform or not the operation of sending tiles to sorted list
	 * @throws NumberFormatException 
	 * @throws IOException 
	 */
	public void sendTilesToSortedList(
			WebDriver driver, String ageGroup, String landingPage, String contentType, Boolean ifPublished, Boolean ifSentToSortedList, StackTraceElement t
			) throws NumberFormatException, IOException {	  
	  // DECLARATION:
	  String published;
	  // MANAGE TILE CLICK:
      driver.findElement(By.linkText("Manage Tile")).click();
      waitUntilElement(driver, Common.TextEntireToXpath("Manage Content Tiles"));
      // AGE GROUP:
      if(ageGroup.length() > 0) {
    	  new Select(driver.findElement(By.id("edit-term-node-tid-depth-1"))).selectByVisibleText(ageGroup);
    	  waitUntilElementInvisibility(driver, 10, Common.throbber, "Throbber", new Exception().getStackTrace()[0]);
    	  }
      // LANDING PAGE:
      if(landingPage.length() > 0) {
          new Select(driver.findElement(By.id("edit-nid"))).selectByVisibleText(landingPage);
          waitUntilElementInvisibility(driver, 10, Common.throbber, "Throbber", new Exception().getStackTrace()[0]);
          }
      // TYPE:
      if(contentType.length() > 0) {
          new Select(driver.findElement(By.id("edit-type"))).selectByVisibleText(contentType);
          waitUntilElementInvisibility(driver, 10, Common.throbber, "Throbber", new Exception().getStackTrace()[0]);
          }
      // PUBLISHED/SCHEDULED:
      if(ifPublished) { published = "Yes"; } else { published = "No"; }
      new Select(driver.findElement(By.id("edit-published"))).selectByVisibleText(published);
      waitUntilElementInvisibility(driver, 10, Common.throbber, "Throbber", new Exception().getStackTrace()[0]);
      // EXECUTION:
      if(ifSentToSortedList) { 
    	  List<WebElement> list = driver.findElements(By.xpath(Drupal.messageNoContentAvailable));
    	  int i = 1;
    	  while ((list.size() == 0) && (driver.findElements(By.xpath(Drupal.errorAjax)).size() < 1)) {			
    		  // SENDING TILES TO SORTED LIST:
    		  fileWriterPrinter("\nPAGE-" + i + ": SENDING TILES TO SORTED LIST...");
    		  waitUntilElementVisibility(driver, 30, Drupal.allInOneCheckBox, "\"Select All\"", new Exception().getStackTrace()[0]);
    		  ajaxProtectedClick(driver, Drupal.allInOneCheckBox, "Select All", false, "", true, false); //checks the "Select All" check-box
    		  // OPERATIONS:
    		  new Select(driver.findElement(By.id("edit-operation"))).selectByVisibleText("Send Tiles to Sorted List");
              waitUntilElementInvisibility(driver, 10, Common.throbber, "Throbber", new Exception().getStackTrace()[0]);
              // EXECUTE:
              driver.findElement(By.xpath(Drupal.executeButton)).click(); // 'Execute' button;
			  // ARE YOU SURE?
              waitUntilElementVisibility(driver, 30, "//*[contains(text(),'You selected the following')]", "\"You selected the following\"", new Exception().getStackTrace()[0]);
              driver.findElement(By.id(Drupal.submit)).click(); // 'Confirm' button
              waitUntilElementInvisibility(driver, 5, By.id(Drupal.submit), "\"Save\" Button", new Exception().getStackTrace()[0]);
              waitUntilElementInvisibility(driver, 600, By.id(Drupal.progress), "Progress Bar", new Exception().getStackTrace()[0]);
              if(driver.findElements(By.xpath(Drupal.errorAjax)).size() > 0) { assertWebElementNotExist(driver, t, Drupal.errorAjax); }
              // STATUS CHECK:
              waitUntilElementVisibility(driver, 30, Drupal.statusPerformedSend, "\"Performed Send\"", new Exception().getStackTrace()[0]);
              By message = By.xpath(Drupal.statusPerformedMessage);
              if (driver.findElements(message).size() > 0) { fileWriterPrinter(driver.findElement(message).getText()); }
              list = driver.findElements(By.xpath(Drupal.messageNoContentAvailable));
              i++;
              }
          }
	}
	
	/**
	 * Navigates to Drupal "Reorder Tiles" with option to perform or not the operation of sending tiles to un-sorted list
	 * @throws NumberFormatException 
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public void sendTilesToUnSortedList(WebDriver driver, String ageGroup, String landingPage, Boolean ifSentToUnSortedList, StackTraceElement t) throws NumberFormatException, IOException, InterruptedException {
	  // MANAGE TILE CLICK:
      hoverElement(driver, By.linkText("Manage Tile"));
      driver.findElement(By.linkText("Reorder Tiles")).click();
      waitUntilElement(driver, Common.TextEntireToXpath("Reorder Content Tiles"));
      // AGE GROUP:
      if(ageGroup.length() > 0) {
    	  new Select(driver.findElement(By.id("edit-term-node-tid-depth-1"))).selectByVisibleText(ageGroup);
    	  waitUntilElementInvisibility(driver, 10, Common.throbber, "Throbber", new Exception().getStackTrace()[0]);
    	  }
      // LANDING PAGE:
      if(landingPage.length() > 0) {
          new Select(driver.findElement(By.id("edit-nid"))).selectByVisibleText(landingPage);
          waitUntilElementInvisibility(driver, 10, Common.throbber, "Throbber", new Exception().getStackTrace()[0]);
          }
      driver.navigate().refresh();
      Thread.sleep(3000);
      // EXECUTION:
      if(ifSentToUnSortedList) { 
    	  List<WebElement> list = driver.findElements(By.xpath(Drupal.messageNoContentAvailable));
    	  int i = 1;
    	  while ((list.size() == 0) && (driver.findElements(By.xpath(Drupal.errorAjax)).size() < 1)) {			
    		  // SENDING TILES TO SORTED LIST:
    		  fileWriterPrinter("\nPAGE-" + i + ": SENDING TILES TO UN-SORTED LIST...");
    		  waitUntilElementVisibility(driver, 30, Drupal.allInOneCheckBox, "\"Select All\"", new Exception().getStackTrace()[0]);
    		  ajaxProtectedClick(driver, Drupal.allInOneCheckBox, "Select All", false, "", true, false); //checks the "Select All" check-box
    		  // OPERATIONS:
    		  new Select(driver.findElement(By.id("edit-operation"))).selectByVisibleText("Send Tiles to UnSorted List");
              waitUntilElementInvisibility(driver, 10, Common.throbber, "Throbber", new Exception().getStackTrace()[0]);
              // EXECUTE:
              driver.findElement(By.xpath(Drupal.executeButton)).click(); // 'Execute' button;
			  // ARE YOU SURE?
              waitUntilElementVisibility(driver, 30, "//*[contains(text(),'You selected the following')]", "\"You selected the following\"", new Exception().getStackTrace()[0]);
              driver.findElement(By.id(Drupal.submit)).click(); // 'Confirm' button
              waitUntilElementInvisibility(driver, 5, By.id(Drupal.submit), "\"Save\" Button", new Exception().getStackTrace()[0]);
              waitUntilElementInvisibility(driver, 600, By.id(Drupal.progress), "Progress Bar", new Exception().getStackTrace()[0]);
              if(driver.findElements(By.xpath(Drupal.errorAjax)).size() > 0) { assertWebElementNotExist(driver, t, Drupal.errorAjax); }
              // STATUS CHECK:
              waitUntilElementVisibility(driver, 30, Drupal.statusPerformedSend, "\"Performed Send\"", new Exception().getStackTrace()[0]);
              By message = By.xpath(Drupal.statusPerformedMessage);
              if (driver.findElements(message).size() > 0) { fileWriterPrinter(driver.findElement(message).getText()); }
              list = driver.findElements(By.xpath(Drupal.messageNoContentAvailable));
              i++;
              }
          }    
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
		  if(string.length() > 0) {
			  string = string.toLowerCase().replaceAll(":", "").replaceAll("'", "").replaceAll(",", "").replaceAll("&", "");
			  string = string.replaceAll(" the ", " ").replaceAll(" on ", " ").replaceAll(" of ", " ");
			  string = string.replaceAll("The ", "").replaceAll("On ", "").replaceAll("Of ", "");
			  string = string.replaceAll(" in ", " ").replaceAll("In ", ""); 
			  string = string.replaceAll(" a ", " ").replaceAll("A ", "");
			  string = string.replaceAll(" for ", " ").replaceAll("For ", ""); 
			  string = string.replaceAll(" ", "-").replaceAll("--", "-");
			  if(string.endsWith("-")) { string = string.substring(0, (string.length() - 1)); }
			  }
		  return string;
		  }
	  
	/**
	 * Re-formats a String as per syntacsis requirements of page URL (limited length)
	 */
	public String reFormatStringForURL(String string, int length) {
		  if(string.length() < length) { length = string.length(); }
		  if(string.length() > 0) { string = string.substring(0, length); }
		  return reFormatStringForURL(string);
		  }
	
	/**
	 * Get the beginning of a String as per given length
	 */
	public String getStringBeginning(String string, int length) {
		  if(string.length() < length) { length = string.length(); }
		  if(string.length() > 0) { string = string.substring(0, length); }
		  return string;
		  }
	
	/**
	 * Get the filename without extention from path
	 */
	public String getFilenameFromPath(String path) {
		if(path.contains("/")) { path = path.substring(path.lastIndexOf("/") + 1, path.length()); } 
		return path.substring(0, path.lastIndexOf("."));
	}
	
	/**
	 * Get the dot-extention from path
	 */
	public String getExtentionFromPath(String path) {
		return path.substring(path.lastIndexOf("."), path.length());
	}
	
	/**
	 * Trims a longer filename as per given shorter one
	 */
	public String trimLongerFilenameAsPerShorter(String pathOfLong, String pathOfShort) {
		String  filename1 = getFilenameFromPath(pathOfLong);
		String extention1 = getExtentionFromPath(pathOfLong);
		String  filename2 = getFilenameFromPath(pathOfShort);
		if(filename1.length() > filename2.length()) { filename1 = filename1.substring(0,filename2.length()); }
	return filename1 + extention1;
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
	  
      /**
       * In current page it will smart-click (by scrolling) a link/tab/locator and wait until url opens with exception trace control
       * @throws IOException 
       * @throws NumberFormatException 
       * @throws InterruptedException 
       */
		public void smartClickLinkUrlWaitUntil(WebDriver driver, int seconds, By element, Boolean ifPromptResult, Boolean ifPromptUrl) 
		throws IOException, NumberFormatException, InterruptedException {
			Dimension size = driver.manage().window().getSize();
			if(ifPromptResult) { driver.manage().window().maximize(); }
			int iteration = 0;
			Boolean ifRetry = true;
			String type = "click";
			String text = driver.findElement(element).getText();
			if(text.length() > 0) { type = type + " on \"" + text + "\""; }
			while( (iteration < 3) && ifRetry ){
				try {
					iteration++;
					clickLinkUrlWaitUntil(driver, seconds, element, ifPromptUrl);
					String success = "Successful " + type + " action!";
					String suffix = "-" + getNumberSuffix(iteration);
					if (iteration > 1) { success = success + " (on " + iteration + suffix + " attempt)"; }
					if ((ifPromptUrl) && (iteration > 1)) { success = "\n" +success; }
					if(ifPromptResult) { fileWriterPrinter(success); }
					ifRetry = false;
				} catch (Exception e) {
					if(ifPromptResult) { fileWriterPrinter("Not a successful " + type + " action...will try again..." + "(attempt #" + iteration + ")"); }
		            if(iteration == 1) { scrollToElementCenter(driver, element, ifPromptResult, false); }
		            if(iteration == 2) { scrollToElementBottom(driver, element, ifPromptResult, false);
		            }
		            }
				}
			driver.manage().window().setSize(size);
			}
		
      /**
       * In current page it will smart-click (by scrolling) a link/tab/locator and wait until url opens with exception trace control
       * @throws IOException 
       * @throws NumberFormatException 
       * @throws InterruptedException 
       */
		public void smartClickLinkUrlWaitUntil(WebDriver driver, int seconds, String xpath, Boolean ifPrompt, Boolean ifPromptUrl) 
		throws IOException, NumberFormatException, InterruptedException {
			smartClickLinkUrlWaitUntil(driver, seconds, By.xpath(xpath), ifPrompt, ifPromptUrl);
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
	    * Add Tile Placement
		* @throws AWTException 
	    * @throws IOException
	    * @throws InterruptedException 
	    */
	  public void addTilePlacement(WebDriver driver, String tileTextSelection) throws IOException, InterruptedException {
		  addTilePlacement(driver, tileTextSelection, true, true);
		  }
	  
	   /**
	    * Add Tile Placement
		* @throws AWTException 
	    * @throws IOException
	    * @throws InterruptedException 
	    */
	  public void addTilePlacement(WebDriver driver, String tileTextSelection, Boolean ifPublish, Boolean ifAdd) throws IOException, InterruptedException {
		  addTilePlacement(driver, Drupal.tileVerticalTab + Drupal.verticalTabActive, tileTextSelection, ifPublish, ifAdd);
		  }
	  
	   /**
	    * Add Tile Placement
		* @throws AWTException 
	    * @throws IOException
	    * @throws InterruptedException 
	    */
	  public void addTilePlacement(WebDriver driver, String tab, String tileTextSelection, Boolean ifPublish, Boolean ifAdd) throws IOException, InterruptedException {
		  addTilePlacement(driver, tab, tileTextSelection, true, ifPublish, ifAdd, null);
		  }
	  
	   /**
	    * Add Tile Placement
		* @throws AWTException 
	    * @throws IOException
	    * @throws InterruptedException 
	    */
	  public void addTilePlacement(WebDriver driver, String tab, String tileTextSelection, Boolean ifAssert, Boolean ifPublish, Boolean ifAdd, StackTraceElement t) throws IOException, InterruptedException {
		  if( tab.length() > 0 ) { scrollToElementCenter(driver, tab, false); ajaxProtectedClick(driver, tab, "", false, "", true, false); }
		  if( (t != null) && ifAssert ) { 
			  assertWebElementExist(driver, Drupal.tilePlacementSelection, t);
			  scrollToElementCenter(driver, By.id(Drupal.tilePlacementSelection), false, false);
			  // ASSERT IF TEXT SELECTION IS MISSING:
			  if(driver.findElements(By.xpath(Common.IdToXpath(Drupal.tilePlacementSelection) + Common.TextEntireAddDescToXpath(tileTextSelection))).size() == 0) {
				  driver.findElement(By.id(Drupal.tilePlacementSelection)).click();
				  waitUntilElementPresence(driver, 1, Common.IdToXpath(Drupal.tilePlacementSelection) + Common.selectionListOptionTwo, "Selection List Option", t, false);
				  assertWebElementsExist(driver, t, Common.IdToXpath(Drupal.tilePlacementSelection) + Common.TextEntireAddDescToXpath(tileTextSelection), true);
				  }
			  }
		  new Select(driver.findElement(By.id(Drupal.tilePlacementSelection))).selectByVisibleText(tileTextSelection);
		  if(ifPublish) { 
			  Boolean status = false;
			  while (!status) {
				  ajaxProtectedClick(driver, By.id(Drupal.tilePlacementPublished), "Published", true, Common.ajaxThrobber, true, -1, false);
				  status = checkBoxStatus(driver, By.id(Drupal.tilePlacementPublished), false, false, t);
				  }
			  }
		  if(ifAdd) { ajaxProtectedClick(driver, By.id(Drupal.tilePlacementAdd), "Add", true, Common.ajaxThrobber, true, -1, false); }
		  }
	  
	  /**
	   * Image tab-clicked reader
	   * @throws IOException 
	   * @throws NumberFormatException 
	   * @throws InterruptedException 
	   */
	  public String uploadReader(WebDriver driver, String xpathTab, By byThumbnail) throws NumberFormatException, IOException, InterruptedException {
		  String thumbnail = "";
		  if(xpathTab.length() > 0) {
			  if(driver.findElements(By.xpath(xpathTab)).size() == 1) {
				  String tabActive = xpathTab  + Drupal.verticalTabActive;
				  By Tab = By.xpath(tabActive);		  
				  // TAB CLICK WITH XPATH CHANGE CONTROLLER AND AJAX ERROR HANDLER:
				  int i = 0;
				  int size = driver.findElements(Tab).size();
				  while ((size == 0) && (i < 5)) {
					     // TAB CLICK WITH AJAX ERROR HANDLER:
					     ajaxProtectedClick(driver, xpathTab, "", false, "", true, false);		     
				         i++;		         
				         size = driver.findElements(Tab).size();
				         }
				  if (size == 1) { fileWriterPrinter("\n" + "Successful \"" + driver.findElement(By.xpath(xpathTab)).getText().replace("(", "").replace(")", "") + "\" tab click!"); }
			  }
		  }
		  // FINAL READER:
		  if(driver.findElements(byThumbnail).size() == 1) { thumbnail = getText(driver, byThumbnail); }
		  return thumbnail;
      }
	  
	  public String uploadReader(WebDriver driver, String xpathTab, String xpathThumbnail) throws NumberFormatException, IOException, InterruptedException {
		  return uploadReader(driver, xpathTab, By.xpath(xpathThumbnail));
		  }
	  
	  public String uploadReader(String xpathTab, String idThumbnail, WebDriver driver) throws NumberFormatException, IOException, InterruptedException {
		  return uploadReader(driver, xpathTab, By.xpath(idThumbnail));
		  }
	  
	  /**
	   * Image tab-clicked upload
	   * @throws IOException 
	   * @throws NumberFormatException 
	   * @throws InterruptedException 
	   */
	  public void upload(WebDriver driver, String image, String tab, String browse, String upload) throws NumberFormatException, IOException, InterruptedException {
	  //  String parentWindowHandle = driver.getWindowHandle();
		  String tabActive = tab  + Drupal.verticalTabActive;
		  By Tab = By.xpath(tabActive);
		  By Browse = By.xpath(browse);
		  By Upload = By.xpath(upload);
		  
		  // TAB CLICK WITH XPATH CHANGE CONTROLLER AND AJAX ERROR HANDLER:
		  int i = 0;
		  int size = driver.findElements(Tab).size();
		  while ((size == 0) && (i < 5)) {
			     // TAB CLICK WITH AJAX ERROR HANDLER:
			     ajaxProtectedClick(driver, tab, "", false, "", true, false);		     
		         i++;		         
		         size = driver.findElements(Tab).size();
		         }
		  if (size == 1) { fileWriterPrinter("\n" + "Successful \"" + driver.findElement(By.xpath(tab)).getText().replace("(", "").replace(")", "") + "\" tab click!"); }
		  
		  // FINAL UPLOADER:
		  String browseText = driver.findElement(Browse).getText();
		  if(browseText.equals(Drupal.badgeBrowseText)) { uploader(driver, image, Browse); }
		  else { uploader(driver, image, Browse, Upload); }
      }
	  
	  /**
	   * Image tab-clicked upload
	   * @throws IOException 
	   * @throws NumberFormatException 
	   * @throws InterruptedException 
	   */
	  public void upload(WebDriver driver, String image, String tab, String browse, String upload, String name, StackTraceElement t) throws NumberFormatException, IOException, InterruptedException {
//		  String parentWindowHandle = driver.getWindowHandle();
		  String tabActive = tab  + Drupal.verticalTabActive;
		  By Tab = By.xpath(tabActive);
		  By Browse = By.xpath(browse);
		  By Upload = By.xpath(upload);
		  
		  // TAB CLICK WITH XPATH CHANGE CONTROLLER AND AJAX ERROR HANDLER:
		  int i = 0;
		  int size = driver.findElements(Tab).size();
		  while ((size == 0) && (i < 5)) {
			     // TAB CLICK WITH AJAX ERROR HANDLER:
			     ajaxProtectedClick(driver, tab, "", false, "", true, -1, false); //(driver, tab, "", false, "", true, false);		     
		         i++;		         
		         size = driver.findElements(Tab).size();
		         }
		  if (size == 1) { fileWriterPrinter("\n" + "Successful \"" + driver.findElement(By.xpath(tab)).getText().replace("(", "").replace(")", "") + "\" tab click!"); }
		  
		  // FINAL UPLOADER:
		  String browseText = driver.findElement(Browse).getText();
		  if(browseText.equals(Drupal.badgeBrowseText)) { uploader(driver, image, Browse); }
		  else { uploader(driver, image, Browse, Upload, name, t); }
      }
	  
	  /**
	   * file upload engine (with new WINDOW handler)
	   * @param driver
	   * @param image
	   * @param element
	   * @param name
	   * @throws IOException
	   * @throws InterruptedException 
	   */
	  public void uploader(WebDriver driver, String file, By browse) throws IOException, InterruptedException {
		  String parentWindowHandle = driver.getWindowHandle();
		  int i = 0;
		  int size = driver.findElements(browse).size();
		  while (size == 1) {
		         try {
		              size = driver.findElements(browse).size();
		              if ((i > 0) && (size == 1)) { fileWriterPrinter("Not a successful " + file + " upload...will try again..."); }
		              smartClickLinkUrlWaitUntil(driver, 5, browse, false, false);
		    	      for(String winHandle : driver.getWindowHandles()) { driver.switchTo().window(winHandle); }
		              driver.findElement(By.xpath("//span" + Common.TextEntireAddToXpath(file))).click();
	                  driver.findElement(By.xpath(Drupal.badgeSelect)).click();
	             alertHandler(driver);
	             closeAllOtherWindows(driver, parentWindowHandle); // driver.switchTo().window(parentWindowHandle);
		         } catch (Exception e) {}
		         i++;
		         size = driver.findElements(browse).size();         
	      }
		  if (size == 0) { fileWriterPrinter("Successful " + file + " upload!"); }
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
        	  ajaxProtectedClick(driver, upload, image + "\" \"Upload", true, Common.ajaxThrobber, true, 5, false);
          } catch(Throwable e) { e.printStackTrace(); }
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
		  
		  // UPLOADER WITH UPLOAD ACTION ERROR HANDLER:
		  int i = 0;
		  String xpath = "//a[contains(@type,'image/jpeg;')][text()='" + image + "']";
		  By element = By.xpath(xpath);
		  int size = 0;
		  int errors = 0;
		  while ((size == 0) && (errors == 0)) {			  		        	 
		        	 if (i > 0) { fileWriterPrinter("Not a successful \"" + image + "\" " + name + " upload...will try again..." + "[Attempt #" + (i+1) + "]"); }

			         try {
			        	  size = driver.findElements(element).size();
				          if ((i > 0) && (size == 0) && (errors == 0)) { fileWriterPrinter("AJAX error during " + name + " upload..."); }				            
				          // IMAGE PATH ENTRY WITH AJAX ERROR HANDLER:
				          ajaxProtectedSendKeys(driver, browse, image, imagePath, false, "", true, false); 
				          // UPLOAD CLICK WITH AJAX ERROR HANDLER:
				          ajaxProtectedClick(driver, upload, "Upload", true, Common.ajaxThrobber, true, -1, false);  
			         } catch (Exception e) {}
		        	 
		        	 // UPLOAD ACTION ERROR HANDLER:
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
	  
	  /**
	   * Determines the given number is even or oddn
	   */
	  public String determineEvenOrOdd(int number){
		  if((number%2)==0) { return "even"; } else { return "odd"; }
		  }
	  
	  /** Determines the given number is even */
	  public Boolean isEven(int number){ return ((number%2)==0); }
	  
	  /** Determines the given number is odd */
	  public Boolean isOdd(int number){ return ((number%2)!=0); }
 
	// ################# DESCRIPTIVE BEGIN ##############################  
		  /**
		   * Gets the first (main) line of any thrown Exception Message
		   * Regardless it is single or multi-line Prompt
		   * @param e
		   * @throws IOException 
		   * @throws NumberFormatException 
		   */
	  public static void getExceptionDescriptive(Exception e, StackTraceElement l, WebDriver driver) throws IOException{
		  String message1 = "", message2 = "", firstLine = "", secondLine = "";
		  String errorCause = "", location = "", exceptionThrown = "";
		  String packageNameOnly = "", classNameOnly = "", xml = "";
		  String description = "", detected = "", runtime = "", subtotal = "";
		  try{			  		 
			  if( (e.getCause().toString().length() > 0) && (e.getMessage().length() > 0) ) { 
				  message1 = e.getCause().toString();
				  message2 = e.getMessage();
				  if(message1.contains("\\r") && message2.contains("\\r")) { 
					  String [] multiline1 = message1.replaceAll("\\r", "").split("\\n");			
					  String [] multiline2 = message2.replaceAll("\\r", "").split("\\n");
					  if(multiline1.length > 0) { firstLine  = multiline1[0]; } else { firstLine  = "Unknown";  }
					  if(multiline2.length > 0) { secondLine = multiline2[0]; } else { secondLine = "Unknown";  }
					  errorCause = firstLine;
					  if(errorCause.contains(":")) { errorCause = errorCause.substring(0,firstLine.indexOf(":")); }
					  exceptionThrown = errorCause;
					  if(exceptionThrown.contains(".")) { exceptionThrown = exceptionThrown.substring(1 + exceptionThrown.lastIndexOf("."), exceptionThrown.length()); }
					  }
				  }

			  packageNameOnly = l.getClassName().substring(0, l.getClassName().lastIndexOf("."));
			  classNameOnly = l.getClassName().substring(1 + l.getClassName().lastIndexOf("."), l.getClassName().length());
			  location = packageNameOnly + File.separator + classNameOnly + File.separator + l.getMethodName() + ", line # " + l.getLineNumber();
		      xml = "<class name=\"" + packageNameOnly + "." + classNameOnly + "\"><methods><include name=\"" + l.getMethodName() + "\"/></methods></class>";
			  description = exceptionThrown;
		      detected = getCurrentDateTimeFull();
		      runtime  = testRunTime("start.time", System.currentTimeMillis());
		      subtotal = testRunTime("ini.time",   System.currentTimeMillis());
		     
		      // ERROR MESSAGE:
			  fileWriterPrinter("\nError Cause: ---> " + errorCause + "\nDescription: ---> " + secondLine + "\n   Location: ---> " + location);
			  
		      // APPEND A NEW LOG RECORD:
		      if (fileExist("run.log", false)) {
			      fileWriter("run.log", "Error Cause: ---> " + errorCause);
			      fileWriter("run.log", "Description: ---> " + secondLine);
			      fileWriter("run.log", "   Location: ---> " + location);
	   		  //  fileWriter("run.log", "   Detected: ---> " + detected);
	   		  //  fileWriter("run.log", "    Runtime: ---> " + runtime);
	   		  //  fileWriter("run.log", "   Subtotal: ---> " + subtotal);	    	        
		      }
		      
		      // UPDATING THE FAILED COUNTER, SCREEN-SHOT AND APPEND AN ERROR RECORD:
		      if( !RetryOnFail.retryOnFail() || Boolean.valueOf(fileScanner("failed.temp")) ) {
		    	  counter("failed.num");
		    	  getScreenShot(l, description, driver);
		    	  fileWriter("failed.log", "    Failure: #" + fileScanner("failed.num"));
				  fileWriter("failed.log", "       Test: #" + fileScanner("test.num"));
				  if(fileExist("failed.try",false)) {  
				  fileWriter("failed.log", "     Re-Run: " + fileScanner("failed.try") + "-" + getNumberSuffix(Integer.valueOf(fileScanner("failed.try"))) + " time"); }
				  fileWriter("failed.log", "      Start: "  + convertCalendarMillisecondsAsStringToDateTimeHourMinSec(fileScanner("start.time")));
	              fileWriter("failed.log", "   Coverage: " + fileScanner("coverage.info"));
	              fileWriter("failed.log", "   XML Path: "  + xml);
				  fileWriter("failed.log", "Error Cause: ---> " + errorCause);
				  fileWriter("failed.log", "Description: ---> " + secondLine);
				  fileWriter("failed.log", "   Location: ---> " + location);
				  fileWriter("failed.log", "   Detected: " + detected);
			   	  fileWriter("failed.log", "    Runtime: " + runtime);
			   	  fileWriter("failed.log", "   Subtotal: " + subtotal);
			   	  fileWriter("failed.log", "");
		      }
		      
		      // APPEND DESCRIPTIVE RECORD:
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
			  
			  } catch(Exception e1) { getExceptionDescriptive(e, l); /*getExceptionDescriptive(getExceptionErrorCause(e), l);*/ }
	  }

	  /**
	   * The function gets the exception error cause
	   * @param Throwable e
	   * @param StackTraceElement l
	   * @throws IOException
	   */
	  public static String getExceptionErrorCause(Exception e) throws IOException{
          String s = e.toString();
          if( (e.getMessage().length() == 0) && (s.contains(".") && s.contains("Exception")) ) {
         	 s = s.substring(s.lastIndexOf(".") + 1, s.lastIndexOf("Exception"));
         	 s = ".getCause() by \"" + s.substring(0, 72) + "\" Exception";
         	 }
          else {
         	 s = e.getMessage().replace("!", "");
         	 s = ".getCause() by \"" + s.substring(0, 72) + "\" Exception";
         	 }
          return s.replaceAll("  ", " ").replaceAll(" \" Exception","\" Exception");    
	  }
	  
		/**
		 * The function create fail logs
		 * @param Throwable e
		 * @param StackTraceElement l
		 * @param WebDriver driver
		 * @throws IOException
		 */
		public static void getExceptionDescriptive(Throwable e, StackTraceElement l, Boolean ifCount) throws IOException {
		   String message1 = "", message2 = "", firstLine = "", secondLine = "";
		   String errorCause = "", location = "", exceptionThrown = "";
		   String packageNameOnly = "", classNameOnly = "", xml = "";  
		   String description = "", detected = "", runtime = "", subtotal = "";
			  
		   try{ message1 = e.getCause().toString(); } 
		   catch (NullPointerException e1) { message1 = ".getCause() by NullPointerException:"; }
		   finally {
			  	 message2 = e.getMessage();
			  	 String [] multiline1 = message1.replaceAll("\\r", "").split("\\n");
			  	 String [] multiline2 = message2.replaceAll("\\r", "").split("\\n");
				 if(multiline1.length > 0) { firstLine  = multiline1[0]; } else { firstLine  = "Unknown";  }
				 if(multiline2.length > 0) { secondLine = multiline2[0]; } else { secondLine = "Unknown";  }
			  	 errorCause = firstLine;
			  	 if(errorCause.contains(":")) { errorCause = errorCause.substring(0,firstLine.indexOf(":")); }
			  	 exceptionThrown = errorCause;
				 if(exceptionThrown.contains(".")) { exceptionThrown = exceptionThrown.substring(1 + exceptionThrown.lastIndexOf("."), exceptionThrown.length()); }
			  	 packageNameOnly = l.getClassName().substring(0, l.getClassName().lastIndexOf("."));
			  	 classNameOnly = l.getClassName().substring(1 + l.getClassName().lastIndexOf("."), l.getClassName().length());
			  	 location = packageNameOnly + File.separator + classNameOnly + File.separator + l.getMethodName() + ", line # " + l.getLineNumber();
			  	 xml = "<class name=\"" + packageNameOnly + "." + classNameOnly + "\"><methods><include name=\"" + l.getMethodName() + "\"/></methods></class>";
			  	 description = exceptionThrown;
			  	 detected = getCurrentDateTimeFull();
			  	 runtime  = testRunTime("start.time", System.currentTimeMillis());
			  	 subtotal = testRunTime("ini.time",   System.currentTimeMillis());

			  	 // ERROR MESSAGE:
				 fileWriterPrinter("\nError Cause: ---> " + errorCause + "\nDescription: ---> " + secondLine + "\n   Location: ---> " + location);
		   
			     // APPEND A NEW LOG RECORD:		      
			  	 if (fileExist("run.log", false)) {
			  	     fileWriter("run.log", "Error Cause: ---> " + errorCause);
			  	     fileWriter("run.log", "Description: ---> " + secondLine);
			  	     fileWriter("run.log", "   Location: ---> " + location);
		  		  // fileWriter("run.log", "   Detected: ---> " + detected);
		  		  // fileWriter("run.log", "    Runtime: ---> " + runtime);
		  		  // fileWriter("run.log", "   Subtotal: ---> " + subtotal);
			  	     }
			  	   
			  	 // UPDATING THE FAILED COUNTER, SCREEN-SHOT AND APPEND AN ERROR RECORD:
			  	 if( !RetryOnFail.retryOnFail() || Boolean.valueOf(fileScanner("failed.temp")) ) {
					 if(ifCount) { counter("failed.num"); }
					 getScreenShotOfDesktop(l, description, false);
				  	 fileWriter("failed.log", "    Failure: #" + fileScanner("failed.num"));
				  	 fileWriter("failed.log", "       Test: #" + fileScanner("test.num"));
				  	 if(fileExist("failed.try",false)) {  
					 fileWriter("failed.log", "     Re-Run: " + fileScanner("failed.try") + "-" + getNumberSuffix(Integer.valueOf(fileScanner("failed.try"))) + " time"); }
				  	 fileWriter("failed.log", "      Start: "  + convertCalendarMillisecondsAsStringToDateTimeHourMinSec(fileScanner("start.time")));
				  	 fileWriter("failed.log", "   XML Path: "  + xml);			  	   
				  	 fileWriter("failed.log", "Error Cause: ---> " + errorCause);			  	   
				  	 fileWriter("failed.log", "Description: ---> " + secondLine);			  	   
				  	 fileWriter("failed.log", "   Location: ---> " + location);			  	   
				  	 fileWriter("failed.log", "   Detected: " + detected);			  	   
				  	 fileWriter("failed.log", "    Runtime: " + runtime);
				  	 fileWriter("failed.log", "   Subtotal: " + subtotal);
				  	 fileWriter("failed.log", "");
			  	 }
			      
			     // APPEND DESCRIPTIVE RECORD:
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

		/**
		 * The function create fail logs
		 * @param Throwable e
		 * @param StackTraceElement l
		 * @throws IOException
		 */
		public static void getExceptionDescriptive(Throwable e, StackTraceElement l) throws IOException {
			getExceptionDescriptive(e, l, true);
		}
		
		/**
		 * The function create fail logs from the secondary Exception Error Cause entry
		 * Regardless it is single or multi-line Prompt
		 * @param e
		 * @throws IOException
		 */
		   
		public static void getExceptionDescriptive(String errorCause, StackTraceElement l) throws IOException {
			  String secondLine = errorCause.replace(".getCause() by ", "").replaceAll("\"", "").replace(" Exception", "") + "!";
			  String description = secondLine.substring(0,secondLine.indexOf(":"));
			  String location = "";
			  String packageNameOnly, classNameOnly, xml, detected, runtime, subtotal;

			  packageNameOnly = l.getClassName().substring(0, l.getClassName().lastIndexOf("."));
			  classNameOnly = l.getClassName().substring(1 + l.getClassName().lastIndexOf("."), l.getClassName().length());
			  location = packageNameOnly + File.separator + classNameOnly + File.separator + l.getMethodName() + ", line # " + l.getLineNumber();
		      xml = "<class name=\"" + packageNameOnly + "." + classNameOnly + "\"><methods><include name=\"" + l.getMethodName() + "\"/></methods></class>";
		      detected = getCurrentDateTimeFull();
		      runtime  = testRunTime("start.time", System.currentTimeMillis());
		      subtotal = testRunTime("ini.time",   System.currentTimeMillis());
		      	
		      // ERROR MESSAGE:
		      fileWriterPrinter("\nError Cause: ---> " + errorCause + "\nDescription: ---> " + secondLine + "\n   Location: ---> " + location);
		      
		      // APPEND A NEW LOG RECORDS:
		      if (fileExist("run.log", false)) {
			      fileWriter("run.log", "Error Cause: ---> " + errorCause);
			      fileWriter("run.log", "Description: ---> " + secondLine);
			      fileWriter("run.log", "   Location: ---> " + location);	    	        
		      }
		      
		      // UPDATING THE FAILED COUNTER, SCREEN-SHOT AND APPEND AN ERROR RECORD:
		      if( !RetryOnFail.retryOnFail() || Boolean.valueOf(fileScanner("failed.temp")) ) {
		    	  counter("failed.num");
		    	  getScreenShotOfDesktop(l, description, false);
		    	  fileWriter("failed.log", "    Failure: #" + fileScanner("failed.num"));
				  fileWriter("failed.log", "       Test: #" + fileScanner("test.num"));
				  if(fileExist("failed.try",false)) {  
				  fileWriter("failed.log", "     Re-Run: " + fileScanner("failed.try") + "-" + getNumberSuffix(Integer.valueOf(fileScanner("failed.try"))) + " time"); }
				  fileWriter("failed.log", "      Start: "  + convertCalendarMillisecondsAsStringToDateTimeHourMinSec(fileScanner("start.time")));
				  fileWriter("failed.log", "   Coverage: " + fileScanner("coverage.info"));
				  fileWriter("failed.log", "   XML Path: "  + xml);
				  fileWriter("failed.log", "Error Cause: ---> " + errorCause);
				  fileWriter("failed.log", "Description: ---> " + secondLine);
				  fileWriter("failed.log", "   Location: ---> " + location);
				  fileWriter("failed.log", "   Detected: " + detected);
			   	  fileWriter("failed.log", "    Runtime: " + runtime);
			   	  fileWriter("failed.log", "   Subtotal: " + subtotal);
			   	  fileWriter("failed.log", "");
		      }
		      
		      // APPEND DESCRIPTIVE RECORD:
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
	// ################# DESCRIPTIVE END ##############################
		   
	// ################# SMART THROW EXCEPTION BEGIN #######################
		   public void throwNotEquals(String actual, String expected) throws IOException {
			   if(expected.contains("\n")) { expected = "\n" + expected + "\n"; actual = "\n" + actual + "\n"; }
			   fileWriterPrinter("\nExpected: " + expected + "\n  Actual: " + actual);
			   if(actual.equals(expected)) { fileWriterPrinter("  Result: OK\n"); }
			   else { fileWriterPrinter("  Result: Not the same!\n"); throw new IOException("Not the same!"); }
			   }
	// ################# SMART THROW EXCEPTIO END #######################
		   
	// ################# SMART ASSERTIONS BEGIN #######################
		   public void assertWebElementsExist(WebDriver driver, StackTraceElement t, By by, Boolean ifScreenShotFull) throws IOException {
			   List <WebElement> list = driver.findElements(by);
			   Assert.assertTrue(list.size() > 0, getAssertTrue(t, driver, "Element not found!", list.size() > 0, ifScreenShotFull));
			   }
		   
		   public void assertWebElementsExist(WebDriver driver, StackTraceElement t, By by) throws IOException {
			   assertWebElementsExist(driver, t, by, false);
			   }
		   
		   public void assertWebElementsExist(WebDriver driver, StackTraceElement t, String xpath, Boolean ifScreenShotFull) throws IOException {
			   List <WebElement> list = driver.findElements(By.xpath(xpath));
			   if (list.size() == 0) { fileWriterPrinter("\n      XPATH: ---> " + xpath); }
			   Assert.assertTrue(list.size() > 0, getAssertTrue(t, driver, "Element not found!", list.size() > 0, ifScreenShotFull));
			   }
		   
		   public void assertWebElementsExist(WebDriver driver, StackTraceElement t, String xpath) throws IOException {
			   assertWebElementsExist(driver, t, xpath, false);
			   }
		   
		   public void assertWebElementsExist(WebDriver driver, String id, StackTraceElement t, Boolean ifScreenShotFull) throws IOException {
			   List <WebElement> list = driver.findElements(By.id(id));
			   if (list.size() == 0) { fileWriterPrinter("\n         ID: ---> " + id); }
			   Assert.assertTrue(list.size() > 0, getAssertTrue(t, driver, "Element not found!", list.size() > 0, ifScreenShotFull));
			   }
		   
		   public void assertWebElementsExist(WebDriver driver, String id, StackTraceElement t) throws IOException {
			   assertWebElementsExist(driver, id, t, false);
			   }
		   
		   public void assertWebElementExist(WebDriver driver, StackTraceElement t, By by) throws IOException {
			   List <WebElement> list = driver.findElements(by);
			   Assert.assertTrue(list.size() == 1, getAssertTrue(t, driver, "Element not found!", list.size() == 1));
			   }
		   
		   public void assertWebElementExist(WebDriver driver, StackTraceElement t, String xpath) throws IOException {
			   List <WebElement> list = driver.findElements(By.xpath(xpath));
			   if (list.size() != 1) { fileWriterPrinter("\n      XPATH: ---> " + xpath); }
			   Assert.assertTrue(list.size() == 1, getAssertTrue(t, driver, "Element not found!", list.size() == 1));
			   }
		   
		   public void assertWebElementExist(WebDriver driver, String id, StackTraceElement t) throws IOException {
			   List <WebElement> list = driver.findElements(By.id(id));
			   if (list.size() != 1) { fileWriterPrinter("\n         ID: ---> " + id); }
			   Assert.assertTrue(list.size() == 1, getAssertTrue(t, driver, "Element not found!", list.size() == 1));
			   }
		   
		   public void assertWebElementNotExist(WebDriver driver, StackTraceElement t, By by) throws IOException {
			   List <WebElement> list = driver.findElements(by);
			   Assert.assertFalse(list.size() > 0, getAssertFalse(t, driver, "Un-Expected Element found!", list.size() > 0));
			   }
		   
		   public void assertWebElementNotExist(WebDriver driver, StackTraceElement t, String xpath) throws IOException {
			   List <WebElement> list = driver.findElements(By.xpath(xpath));
			   if (list.size() > 0) { fileWriterPrinter("\n      XPATH: ---> " + xpath); }
			   Assert.assertFalse(list.size() > 0, getAssertFalse(t, driver, "Un-Expected Element found!", list.size() > 0));
			   }
		   
		   public void assertWebElementNotExist(WebDriver driver, String id, StackTraceElement t) throws IOException {
			   List <WebElement> list = driver.findElements(By.id(id));
			   if (list.size() > 0) { fileWriterPrinter("\n         ID: ---> " + id); }
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
			   if(expected.contains("\n")) { expected = "\n" + expected + "\n"; actual = "\n" + actual + "\n"; }
			   Assert.assertEquals(actual, expected, getAssertEquals(t, driver, "Not the same!", actual, expected));
			   }
		   
		   public void assertFont(WebDriver driver, StackTraceElement t, By by,
				                  String expectedFontName,  String fontNameCssValue,
				                  String expectedFontSize,  String fontSizeCssValue,
				                  String expectedFontColour, String fontColourCssValue
				                  ) throws IOException {
			   
			   String actualFontName = null, actualFontColour = null, actualFontSize = null;
			   if( (expectedFontName.length() > 0) && (fontNameCssValue.length() > 0) ) {
				   actualFontName = driver.findElement(by).getCssValue(fontNameCssValue).toLowerCase().replace("'", "").replace("_", "").replace("-","").replace(" ", "");
				   if(actualFontName.contains(",")) { actualFontName = actualFontName.substring(0, actualFontName.indexOf(",")); }
				   expectedFontName = expectedFontName.toLowerCase().replace("_", "").replace("'", "").replace("-","").replace(" ", "");
				   assertEquals(driver, t, actualFontName, expectedFontName);
				   }
			   if( (expectedFontSize.length() > 0) && (fontSizeCssValue.length() > 0) ) { 
				   actualFontSize = driver.findElement(by).getCssValue(fontSizeCssValue);
				   assertEquals(driver, t, actualFontSize, expectedFontSize);
				   }
			   if( (expectedFontColour.length() > 0) && (fontColourCssValue.length() > 0) ) {
				   actualFontColour = getColorHEX(driver, by, fontColourCssValue, true, "DETECTED COLOUR: "); // driver.findElement(by).getCssValue(fontColourCssValue);
				   assertEquals(driver, t, actualFontColour, expectedFontColour);
				   }		   
			   }
		   
		   public void assertFont(WebDriver driver, StackTraceElement t, String xpath,
	                  String expectedFontName,  String fontNameCssValue,
	                  String expectedFontSize,  String fontSizeCssValue,
	                  String expectedFontColour, String fontColourCssValue
	                  ) throws IOException {
			   assertFont(driver, t, By.xpath(xpath), expectedFontName, fontNameCssValue, expectedFontSize, fontSizeCssValue, expectedFontColour, fontColourCssValue);
			   }
		   
		   public static String getAssertTrue(StackTraceElement l, WebDriver driver, String description, Boolean b, Boolean ifScreenShotFull) throws IOException {
		       String packageNameOnly = l.getClassName().substring(0, l.getClassName().lastIndexOf("."));
		       String classNameOnly = l.getClassName().substring(1 + l.getClassName().lastIndexOf("."), l.getClassName().length());
		       String location = packageNameOnly + File.separator + classNameOnly + File.separator + l.getMethodName() + ", line # " + l.getLineNumber();
		       String xml = "<class name=\"" + packageNameOnly + "." + classNameOnly + "\"><methods><include name=\"" + l.getMethodName() + "\"/></methods></class>";
		       String detected = getCurrentDateTimeFull();
			   String runtime  = testRunTime("start.time", System.currentTimeMillis());
			   String subtotal = testRunTime("ini.time",   System.currentTimeMillis());
		   if (b == false) {
		      fileWriterPrinter("\nError Cause: ---> " + description + "\n   Location: ---> " + location + "\n   Expected: ---> " + "true" + "\n     Actual: ---> " + b + "\n");				 
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
			  // Appending an Error record and updating existing Failed Counter record:
			     if( !RetryOnFail.retryOnFail() || Boolean.valueOf(fileScanner("failed.temp")) ) {
			    	 counter("failed.num");
			    	 if(ifScreenShotFull) { getScreenShotOfDesktop(l, description, false); } else { getScreenShot(l, description, driver); } 
			    	 fileWriter("failed.log", "    Failure: #" + fileScanner("failed.num"));
			    	 fileWriter("failed.log", "       Test: #" + fileScanner("test.num"));
					 if(fileExist("failed.try",false)) { fileWriter("failed.log", "     Re-Run: " + fileScanner("failed.try") + "-" + getNumberSuffix(Integer.valueOf(fileScanner("failed.try"))) + " time"); }   
					 fileWriter("failed.log", "      Start: "  + convertCalendarMillisecondsAsStringToDateTimeHourMinSec(fileScanner("start.time")));  
					 fileWriter("failed.log", "   Coverage: " + fileScanner("coverage.info"));
					 fileWriter("failed.log", "   XML Path: "  + xml);
					 fileWriter("failed.log", "Error Cause: ---> " + description);
					 fileWriter("failed.log", "   Location: ---> " + location);
					 fileWriter("failed.log", "   Expected: ---> " + "true");
					 fileWriter("failed.log", "     Actual: ---> " + b);
					 fileWriter("failed.log", "   Detected: " + detected);
					 fileWriter("failed.log", "    Runtime: " + runtime);
					 fileWriter("failed.log", "   Subtotal: " + subtotal);
					 fileWriter("failed.log", "");
			    	 }
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
		   
		   public static String getAssertTrue(StackTraceElement l, WebDriver driver, String description, Boolean b) throws IOException {
		       return getAssertTrue(l, driver, description, b, false);
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
			     
			  // Appending an Error record and updating existing Failed Counter record:
			     if( !RetryOnFail.retryOnFail() || Boolean.valueOf(fileScanner("failed.temp")) ) {
			    	 counter("failed.num");
			    	 getScreenShot(l, description, driver);
			    	 fileWriter("failed.log", "    Failure: #" + fileScanner("failed.num"));
			    	 fileWriter("failed.log", "       Test: #" + fileScanner("test.num"));
					 if(fileExist("failed.try",false)) { fileWriter("failed.log", "     Re-Run: " + fileScanner("failed.try") + "-" + getNumberSuffix(Integer.valueOf(fileScanner("failed.try"))) + " time"); }
					 fileWriter("failed.log", "      Start: "  + convertCalendarMillisecondsAsStringToDateTimeHourMinSec(fileScanner("start.time")));
					 fileWriter("failed.log", "   Coverage: " + fileScanner("coverage.info"));
					 fileWriter("failed.log", "   XML Path: "  + xml);
					 fileWriter("failed.log", "Error Cause: ---> " + description);
					 fileWriter("failed.log", "   Location: ---> " + location);
					 fileWriter("failed.log", "   Expected: ---> " + expected);
					 fileWriter("failed.log", "     Actual: ---> " + actual);
					 fileWriter("failed.log", "   Detected: " + detected);
					 fileWriter("failed.log", "    Runtime: " + runtime);
					 fileWriter("failed.log", "   Subtotal: " + subtotal);
					 fileWriter("failed.log", "");
					 }
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
				 
			  // Appending an Error record and updating existing Failed Counter record:
			     if( !RetryOnFail.retryOnFail() || Boolean.valueOf(fileScanner("failed.temp")) ) {
			    	 counter("failed.num");
			    	 getScreenShot(l, description, driver);
			    	 fileWriter("failed.log", "    Failure: #" + fileScanner("failed.num"));
			    	 fileWriter("failed.log", "       Test: #" + fileScanner("test.num"));
			    	 if(fileExist("failed.try",false)) { fileWriter("failed.log", "     Re-Run: " + fileScanner("failed.try") + "-" + getNumberSuffix(Integer.valueOf(fileScanner("failed.try"))) + " time"); }
			    	 fileWriter("failed.log", "      Start: "  + convertCalendarMillisecondsAsStringToDateTimeHourMinSec(fileScanner("start.time"))); 
			    	 fileWriter("failed.log", "   Coverage: " + fileScanner("coverage.info"));
			    	 fileWriter("failed.log", "   XML Path: "  + xml);
			    	 fileWriter("failed.log", "Error Cause: ---> " + description);
			    	 fileWriter("failed.log", "   Location: ---> " + location);
			    	 fileWriter("failed.log", "   Expected: ---> " + "false");
			    	 fileWriter("failed.log", "     Actual: ---> " + b);
			    	 fileWriter("failed.log", "   Detected: " + detected);
			    	 fileWriter("failed.log", "    Runtime: " + runtime);
			    	 fileWriter("failed.log", "   Subtotal: " + subtotal);
			    	 fileWriter("failed.log", "");
			       }
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
				
				/**
				 * Creates Screen-Shot png-files as per user-defined path;
				 * (one is for primary and the rest are for the secondary screens)
				 * Also prints the Bounds information about the Displays;
				 * For All-In-One Display image: add the width of all monitors;
				 * @throws IOException 
				 * @throws NumberFormatException 
				 */
				public void getScreenShotOfScreens(StackTraceElement l, Exception e, String description) throws NumberFormatException, IOException {
					DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd, HH.mm.ss");
					String message1 = null;
					String exception = "";
					if(description.length() > 0) { description = ", " + description; }
					try{ message1 = e.getCause().toString(); }
					catch(NullPointerException e1) { message1 = ".getCause() by NullPointerException:"; }
					finally {
							String [] multiline1 = message1.replaceAll("\\r", "").split("\\n");
							String firstLine = multiline1[0];
							String errorCause = firstLine.substring(0,firstLine.indexOf(":"));
							String exceptionThrown = errorCause.substring(1 + errorCause.lastIndexOf("."), errorCause.length());
							exception = ", " + exceptionThrown;

							String packageNameOnly = l.getClassName().substring(0, l.getClassName().lastIndexOf("."));
							String classNameOnly = l.getClassName().substring(1 + l.getClassName().lastIndexOf("."), l.getClassName().length());
							
							String screenshot = classNameOnly + "." + l.getMethodName() + exception + description + ", line # " + l.getLineNumber();
							String screenshotName = screenshot + " (" + dateFormat.format(new Date()) + ") [";
							String outputDir = Common.outputFileDir + packageNameOnly + File.separator + classNameOnly + File.separator;
							
						    GraphicsEnvironment gEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
						    GraphicsDevice[] gDevs = gEnv.getScreenDevices();

						    for (GraphicsDevice gDev : gDevs) {
						        DisplayMode mode   = gDev.getDisplayMode();
						        Rectangle bounds   = gDev.getDefaultConfiguration().getBounds();
						        String displayName = gDev.getIDstring().replace("\\","");
						        fileWriterPrinter("\n" + displayName + ": ");
						        fileWriterPrinter( "Min: (" + (int)bounds.getMinX() + ", " + (int)bounds.getMinY() + "); "
				        		         + "Max: (" + (int)bounds.getMaxX() + ", " + (int)bounds.getMaxY() + "); "
						        		 + "Width: " + mode.getWidth() + "; Height:" + mode.getHeight());
						        try {
						            Robot robot = new Robot();
						            BufferedImage image = robot.createScreenCapture(new Rectangle((int) bounds.getMinX(),
						                                  (int) bounds.getMinY(), (int) bounds.getWidth(), (int) bounds.getHeight()));
						            String displayFileName = displayName + ".png";
						            String displayshotName = screenshotName + displayFileName.replace(".png", "].png");
						            ImageIO.write(image, "png", new File(Common.outputDir + displayFileName));				            
						            fileWriterPrinter(outputDir + displayshotName);
									fileCopy(Common.outputFileDir, displayFileName, outputDir, displayshotName);
									fileCleaner(Common.outputFileDir, displayFileName);			
						        } catch (AWTException | IOException E) {  E.printStackTrace(); }
						    }					    
					}
				}
				
				/**
				 * Creates Screen-Shot png-files as per user-defined path;
				 * (one is for primary and the rest are for the secondary screens)
				 * Also prints the Bounds information about the Displays;
				 * For All-In-One Display image: add the width of all monitors;
				 * @throws IOException 
				 * @throws NumberFormatException 
				 */
				public void getScreenShotOfScreens(StackTraceElement l, String description, Boolean ifDisplay) throws NumberFormatException, IOException {
					DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd, HH.mm.ss");
					String packageNameOnly = l.getClassName().substring(0, l.getClassName().lastIndexOf("."));
					String classNameOnly = l.getClassName().substring(1 + l.getClassName().lastIndexOf("."), l.getClassName().length());
					String screenshot = classNameOnly + "." + l.getMethodName() + ", " + description + ", line # " + l.getLineNumber();
					String screenshotName = screenshot + " (" + dateFormat.format(new Date()) + ") [";
					String outputDir = Common.outputFileDir + packageNameOnly + File.separator + classNameOnly + File.separator;
					
				    GraphicsEnvironment gEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
				    GraphicsDevice[] gDevs = gEnv.getScreenDevices();

				    for (GraphicsDevice gDev : gDevs) {
				        DisplayMode mode   = gDev.getDisplayMode();
				        Rectangle bounds   = gDev.getDefaultConfiguration().getBounds();
				        String displayName = gDev.getIDstring().replace("\\","");
				        fileWriterPrinter("\n" + displayName + ": ");
				        fileWriterPrinter( "Min: (" + (int)bounds.getMinX() + ", " + (int)bounds.getMinY() + "); "
		        		         + "Max: (" + (int)bounds.getMaxX() + ", " + (int)bounds.getMaxY() + "); "
				        		 + "Width: " + mode.getWidth() + "; Height:" + mode.getHeight());				        
				        try {
				            Robot robot = new Robot();
				            BufferedImage image = robot.createScreenCapture(new Rectangle((int) bounds.getMinX(),
				                                  (int) bounds.getMinY(), (int) bounds.getWidth(), (int) bounds.getHeight()));
				            String displayFileName = displayName + ".png";
				            String displayshotName = screenshotName + displayFileName.replace(".png", "].png");
				            if(!ifDisplay) { displayshotName.replace("[Display", "["); }
				            ImageIO.write(image, "png", new File(Common.outputDir + displayFileName));				            
				            fileWriterPrinter(outputDir + displayshotName);
							fileCopy(Common.outputFileDir, displayFileName, outputDir, displayshotName);
							fileCleaner(Common.outputFileDir, displayFileName);			
				        } catch (AWTException | IOException E) {  E.printStackTrace(); }
				    }
				}
				
				/**
				 * Creates a full-desktop Screen-Shot png-file (All-In-One Display image) as per user-defined path;
				 * (for any number of screens)
				 * Also prints the Bounds information about the Display;
				 * @throws IOException 
				 * @throws NumberFormatException 
				 */
				public static void getScreenShotOfDesktop(StackTraceElement l, String description, Boolean ifDesktop) throws NumberFormatException, IOException {
					DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd, HH.mm.ss");
					String packageNameOnly = l.getClassName().substring(0, l.getClassName().lastIndexOf("."));
					String classNameOnly = l.getClassName().substring(1 + l.getClassName().lastIndexOf("."), l.getClassName().length());
					String screenshot = classNameOnly + "." + l.getMethodName() + ", " + description + ", line # " + l.getLineNumber() + " (" + dateFormat.format(new Date()) + ").png";
					String outputDir = Common.outputFileDir + packageNameOnly + File.separator + classNameOnly + File.separator;					
				    
					GraphicsEnvironment gEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
				    GraphicsDevice[] gDevs = gEnv.getScreenDevices();
			        
				    Rectangle bounds;
			        int maxX = 0;
			        int maxY = 0;
					for (int i = 0; i < gDevs.length; i++) {
						bounds = gDevs[i].getDefaultConfiguration().getBounds();
						if((int)bounds.getMaxX() > maxX) { maxX = (int)bounds.getMaxX(); }
						if((int)bounds.getMaxY() > maxY) { maxY = (int)bounds.getMaxY(); }			
					}						
			        String display = "Display";
			        fileWriterPrinter("\n" + display + ": ");
			        fileWriterPrinter( "Width: " + maxX + "; Height:" + maxY);				        
			        try {
			            Robot robot = new Robot();
			            BufferedImage image = robot.createScreenCapture(new Rectangle(0, 0, maxX, maxY));		            
			            if(ifDesktop) { screenshot.replace(".png"," [" + display + "].png"); } 
			            
			            ImageIO.write(image, "png", new File(Common.outputDir + screenshot));				            
			            fileWriterPrinter(outputDir + screenshot);
						fileCopy(Common.outputFileDir, screenshot, outputDir, screenshot);
						fileCleaner(Common.outputFileDir, screenshot);			
			        } catch (AWTException | IOException E) {  E.printStackTrace(); }			    
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
			   fileOverWriter("xml.path", xml); 
			// Append Test Coverage:
			   if( fileExist("test.type", false) && fileExist("coverage.info", false) ) {				   
			       if( fileScanner("test.type").contains("Regression Test") ) {
			    	   // Test Coverage recorder:
			    	   if( Integer.valueOf(fileScanner("test.num")) == 1 ) { fileOverWriter("coverage.csv", "ID,PACKAGE,CLASS,TEST,COVERAGE"); }
			    	   String record = fileScanner("test.num") + "," + packageNameOnly + "," + classNameOnly + "," + l.getMethodName() + "," + fileScanner("coverage.info").replaceAll(", ", ",");
			    	   String last = readFileOutputLastLine("coverage.csv");
			    	   if( ! record.equals(last) ) {fileWriter("coverage.csv", record);}
			    	   }
			       }			   
			// Renew Stack Trace Element record:
			   fileOverWriter("stack.trace", l); 
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
			   fileCleaner("match.log" );
			   fileCleaner("max.log"   );
			   fileCleaner("order.log" );
			   fileCleaner("xml.log"   );
			   fileCleaner("error.log" );
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
			
			/**
			 * startTime with Anotation Output
			 * @throws IOException
			 */
			public void startTime(Method method) throws IOException {
			   String date = getCurrentDateTimeFull();
			   
			// Cleaning:
			   fileCleaner("match.log" );
			   fileCleaner("max.log"   );
			   fileCleaner("order.log" );
			   fileCleaner("xml.log"   );
			   fileCleaner("error.log" );
			   fileCleaner("reason.log");
			   fileCleaner("start.time");
			   
			   fileWriter("start.time", convertLongToString(System.currentTimeMillis()));
			// Creating New or Updating existing Test Counter record:  
			   int n = counter("test.num");
		    // Print-out information:
		       fileWriterPrinter("\n       Test: #" + n);
			   fileWriterPrinter(  "      Start: "  + date);
			// Anotation output (coverage groups):
			   fileWriterPrinter(  "   Coverage: " + printAnnotationGroups(method));
			   fileOverWriter("coverage.info", printAnnotationGroups(method));   
			// Append a Start Log record with Annotation:
			   if (fileExist("run.log", false)) {
			       fileWriter("run.log", "");
			       fileWriter("run.log", "       Test: #" + n);
			       fileWriter("run.log", "      Start: "  + date);
			       fileWriter("run.log", "   Coverage: " + printAnnotationGroups(method));
		    	}	            
			}
			
//		    @BeforeMethod
		    public void printMethodName(Method method) throws IOException { fileWriterPrinter(method.getName()); }

//		    @BeforeMethod
		    public String printAnnotationGroups(Method method) throws IOException{
		            Test test = method.getAnnotation(Test.class);
		            String s = "", c = ", ";
		            try {
						for (int i = 0; i < test.groups().length; i++) {
							if (  test.groups()[i].startsWith("US-" )
							   || test.groups()[i].startsWith("TC-" )
							   || test.groups()[i].startsWith("TP-" )
							   || test.groups()[i].startsWith("BUG-")
							   )
							   { if((i == 0) || (s.length() == 0)) { s = s + test.groups()[i]; } else { s = s + c + test.groups()[i]; } }
						//  if( i == (test.groups().length - 1) ) { fileWriterPrinter(s); }
							}
					} catch (Exception e) {}
		            if(s.length() == 0 ) { s = "N/A"; }
		            return s;
		            }
		    
		    /** 
		     * Performes user defined AfterTest activities (if required) 
			 * @throws IOException
			 */
			public void endTest() throws IOException {
			    try {
					if(Common.homeURL.contains("qa-kids.tvokids.com")){
						WebDriver driver = getServerName(driverHelper);
					    deleteAllContent(driver, "147", "", "", new RuntimeException().getStackTrace()[0]);
					    driver.quit();
					    }
					} catch (IllegalArgumentException | InterruptedException e) { e.printStackTrace(); }
			    }
				
			
			/** Prints Test End and Sub-Total Time 
			 * @throws IOException
			 */
			public void endTime() throws IOException {
			   long finish = System.currentTimeMillis();
			   
			// Cleaning:
			   fileCleaner("match.log"  );
			   fileCleaner("max.log"    );
			   fileCleaner("order.log"  );
			   fileCleaner("xml.log"    );
			   fileCleaner("error.log"  );
			   fileCleaner("reason.log" );
			   fileCleaner("failed.try" );
			   fileCleaner("failed.temp"); // fileOverWriter("failed.temp", "false");
			   fileOverWriter("finish.time", convertLongToString(finish));
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
			
            /** Cleans "test-output" Directory 
             * @throws IOException */
            public boolean testOutputCleaner(Boolean ifPrompt) throws IOException {
                return folderCleaner(Common.testOutputFileDir, true, false, false, ifPrompt);
                }
            
            /** Cleans "test-output" Directory 
             * @throws IOException 
             * @throws ParseException */
            public boolean testOutputTimeReset(Boolean ifPrompt) throws IOException, ParseException {
                return changeTimeAttribute("1999-12-31", "23:00:00", Common.testOutputFileDir, false, true, false, ifPrompt);
                }
            
            /** Cleans "output" Directory 
             * @throws IOException */
            public boolean outputCleaner(Boolean ifPrompt) throws IOException {
                return folderCleaner(Common.outputFileDir, true, true, false, ifPrompt);
                }
            
            /** 
             * Cleans any User-Defined Directory 
             * @throws IOException
             */
            public boolean folderCleaner(String folderPath, Boolean ifDeleteSubdirs, Boolean ifDeleteFiles, Boolean ifDeleteRoot, Boolean ifPrompt) throws IOException {                
                folderPath = folderPath.replace("\\", "/");
                if(folderPath.endsWith("/")) { folderPath = folderPath.substring(0, folderPath.length() - 1); }
                File dir = new File(folderPath);
                String how = "", action = null;
                Boolean success = false;
                Boolean current = false;
                if (dir.exists() && dir.isDirectory()) {                    
                    try{                    
                        success = true;
                        String[] children = dir.list();
                        if(children.length > 0) {
                            action = "CLEANED";
                            for (int i = 0; i < children.length; i++) {
                                File child = new File(dir, children[i]);
                                if(child.isDirectory()){  if(ifDeleteSubdirs) { FileUtils.forceDelete(child); } if(ifPrompt && ifDeleteSubdirs) {System.out.print("DIRECTORY ");} }
                                else                   {  if(ifDeleteFiles)  { FileUtils.forceDelete(child); } if(ifPrompt && ifDeleteFiles)  {System.out.print("     FILE ");} }
                                
                                current = !child.exists();
                                success = success && current;
                                if(current) { how = "    DELETED: \""; } else { how = "NOT DELETED: \""; }
                                if(ifPrompt && current) System.out.print(how + child.getAbsolutePath() + "\"\n");
                                }
                            }
                        // THE DIRECTORY IS EMPTY - DELETE IT IF REQUIRED
                        if (ifDeleteRoot) { FileUtils.forceDelete(dir); success = success && !dir.exists(); action = "DELETED"; }
                        if(ifPrompt && (!action.equals(null))) {
                        if (success) { 
                              System.out.print("\n" + "COMPLETELY " + action + " DIRECTORY: \"" + folderPath.substring(folderPath.lastIndexOf("/") + 1, folderPath.length()) + "\"\n\n");
                            } else {
                              System.out.print("\n" + "NOT COMPLETELY " + action + " DIRECTORY: \"" + folderPath.substring(folderPath.lastIndexOf("/") + 1, folderPath.length()) + "\"\n\n");
                            }
                        }
                    } catch (Exception e) {}                    
                }
                return success;
            }
            
            /** 
             * Changes Sub-Directories and/or Files Time-Attributes located in User-Defined Directory 
             * @throws IOException
             * @throws ParseException 
             */
            public boolean changeTimeAttribute(String date, String time, String folderPath, Boolean ifChangeSubdirs, Boolean ifChangeFiles, Boolean ifChangeRoot, Boolean ifPrompt) throws IOException, ParseException {                
                long milliseconds = convertCalendarDateTimeHourMinSecToMillisecondsAsLong(date + " " + time);
                folderPath = folderPath.replace("\\", "/");
                if(folderPath.endsWith("/")) { folderPath = folderPath.substring(0, folderPath.length() - 1); }
                File dir = new File(folderPath);
                String dirTimeStamp = getLastModifiedTimeStamp(dir);
                String how = "", action = null;
                Boolean success = false;
                Boolean current = false;
                if (dir.exists() && dir.isDirectory()) {                    
                    try{                    
                        success = true;
                        String[] children = dir.list();
                        if(children.length > 0) {
                            action = "TIME RESET";
                            for (int i = 0; i < children.length; i++) {
                                File child = new File(dir, children[i]);
                                String childTimeStamp = getLastModifiedTimeStamp(child);
                                if(child.isDirectory()){  if(ifChangeSubdirs) { child.setLastModified(milliseconds); } if(ifPrompt && ifChangeSubdirs) {System.out.print("DIRECTORY ");} }
                                else                   {  if(ifChangeFiles)   { child.setLastModified(milliseconds); } if(ifPrompt && ifChangeFiles)  {System.out.print("     FILE ");} }
                                
                                current = child.exists() && !getLastModifiedTimeStamp(child).equals(childTimeStamp);
                                success = success && current;
                                if(current) { how = "TIME     CHANGED: \""; } else { how = "TIME NOT CHANGED: \""; }
                                if(ifPrompt) System.out.print(how + child.getAbsolutePath() + "\"\n");
                                }
                            }
                        // DIRECTORY TIME RESET IF REQUIRED:
                        if (ifChangeRoot) { dir.setLastModified(milliseconds); success = success && dir.exists() && !getLastModifiedTimeStamp(dir).equals(dirTimeStamp); action = "TIME CHANGED"; }
                        if(ifPrompt && (!action.equals(null))) {
                        if (success) { 
                              System.out.print("\n" + "COMPLETE " + action + " DIRECTORY: \"" + folderPath.substring(folderPath.lastIndexOf("/") + 1, folderPath.length()) + "\"\n\n");
                            } else {
                              System.out.print("\n" + "NOT A COMPLETE " + action + " DIRECTORY: \"" + folderPath.substring(folderPath.lastIndexOf("/") + 1, folderPath.length()) + "\"\n\n");
                            }
                        }
                    } catch (Exception e) {}                    
                }
                return success;
            }
            
            public String getCreationTimeStamp(File file) throws IOException {
                return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(getCreationTime(file).toMillis());
                }
            
            public String getLastModifiedTimeStamp(File file) throws IOException {
                return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(getLastModifiedTime(file).toMillis());
                }
            
            public String getLastAccessTimeStamp(File file) throws IOException {
                return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(getLastAccessTime(file).toMillis());
                }
            
            public FileTime getCreationTime(File file) throws IOException {
                Path p = Paths.get(file.getAbsolutePath());
                BasicFileAttributes view = Files.getFileAttributeView(p, BasicFileAttributeView.class).readAttributes();
                FileTime fileTime = view.creationTime();
                return fileTime;
                }
            
            public FileTime getLastModifiedTime(File file) throws IOException {
                Path p = Paths.get(file.getAbsolutePath());
                BasicFileAttributes view = Files.getFileAttributeView(p, BasicFileAttributeView.class).readAttributes();
                FileTime fileTime = view.lastModifiedTime();
                return fileTime;
                }
            
            public FileTime getLastAccessTime(File file) throws IOException {
                Path p = Paths.get(file.getAbsolutePath());
                BasicFileAttributes view = Files.getFileAttributeView(p, BasicFileAttributeView.class).readAttributes();
                FileTime fileTime = view.lastAccessTime();
                return fileTime;
                }
			
			/** Counter: Will renew counting starting with "1" if the Counter File is currently missing; Returns new iteration value; 
			 * @throws IOException
			 */
            public static int counter(String counterFileName, int counterStep) throws NumberFormatException, IOException {
			   // if Counter File does not exist - create new it with counter "1";
               //                      otherwise - update existing by increasing the counter by value of "counterStep";
		       int n = 1;
		       File f = new File(Common.testOutputFileDir + counterFileName);
		       if (f.exists() && f.isFile()) { n = Integer.valueOf(fileScanner(counterFileName)) + counterStep; }
		       FileUtils.writeStringToFile(f, String.valueOf(n));
		       return n;
            }
            
			/** Counter: Will renew counting starting with "1" if the Counter File is currently missing; Returns new iteration value; 
			 * @throws IOException
			 */
            public static int counter(String counterFileName) throws NumberFormatException, IOException {
			   // if Counter File does not exist - create new it with counter "1";
               //                      otherwise - update existing by increasing the counter by "1";
		       return counter(counterFileName, 1);
            }
            
			/**
			 * @throws IOException
			 * @throws NumberFormatException
			 */ 
			public static Boolean fileExist(String fileName) throws NumberFormatException, IOException {
				return fileExist(Common.testOutputFileDir, fileName, true);			
			}
			
			/**
			 * @throws IOException
			 * @throws NumberFormatException
			 */ 
			public static Boolean fileExist(String fileName, Boolean silentMode) throws NumberFormatException, IOException {
				return fileExist(Common.testOutputFileDir, fileName, silentMode);			
			}
			
			/**
			 * @throws IOException
			 * @throws NumberFormatException
			 */ 
			public static Boolean fileExist(String path, String fileName) throws NumberFormatException, IOException {
				return fileExist(path, fileName, true);			
			}
			
			/**
			 * @throws IOException
			 * @throws NumberFormatException
			 */ 
			public static Boolean fileExist(String path, String fileName, Boolean silentMode) throws NumberFormatException, IOException {
				if(path.endsWith(File.separator)) { path = path.substring(0, path.length() - 1); }
				File f = new File((path + File.separator + fileName).replace(File.separator + File.separator, File.separator));
				if (! (f.exists() && f.isFile()) ) { if (silentMode) { fileWriterPrinter(f + " is missing..."); } }
				return (f.exists() && f.isFile());
			}
			
			/** @throws IOException */ 
			public static void fileCopy(String fileSource, String fileDest) throws IOException {
				fileCopy(Common.testOutputFileDir, fileSource, Common.testOutputFileDir, fileDest);
			}
			
			/** @throws IOException */ 
			public static void fileCopy(String pathSource, String fileSource, String pathDest, String fileDest) throws IOException {
				if(pathSource.endsWith(File.separator)) { pathSource = pathSource.substring(0, pathSource.length() - 1); }
				if(pathDest.endsWith(File.separator))   { pathDest = pathDest.substring(0, pathDest.length() - 1); }
				File s = new File((pathSource + File.separator + fileSource).replace(File.separator + File.separator, File.separator));
				File d = new File((pathDest   + File.separator + fileDest  ).replace(File.separator + File.separator, File.separator));
				if (s.exists() && s.isFile()) { FileUtils.copyFile(s, d); }
			}
			
			/** Edit the target file by searching content throug all lines, replacing them, and overwriting that target file */
			public static void fileEditor(String path, String search, String replace) {	    	
			try {
				 if(path.endsWith(File.separator)) { path = path.substring(0, path.length() - 1); }
				 File log = new File(path);	    	
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
				 if(sourcePath.endsWith(File.separator)) { sourcePath = sourcePath.substring(0,sourcePath.length() - 1); }
				 if(targetPath.endsWith(File.separator)) { targetPath = targetPath.substring(0,targetPath.length() - 1); }
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
            	if(path.endsWith(File.separator)) { path = path.substring(0, path.length() - 1); }
				File f = new File(path + File.separator + fileName);				                                                                      
			 // Write or add a String line into File:	
			    FileWriter fw = new FileWriter(f,true);
			    PrintWriter pw = new PrintWriter(fw);
			    pw.println(printLine);
			    pw.close();
			}
            
            /** Over-Writes a File with a String line */
            public static void fileOverWriter(String fileName, Object printLine) throws NumberFormatException, IOException {
            	fileCleaner(fileName); fileWriter(fileName, printLine); 
            }
            
            /** Over-Writes a File with a String line */
            public static void fileOverWriter(String path, String fileName, Object printLine) throws NumberFormatException, IOException {
            	fileCleaner(path, fileName); fileWriter(path, fileName, printLine); 
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
        		if (fileExist("failed.num", false)){ fileOverWriter("failed.num", failed); }
        		
    		    if (fileExist("test.num", false)) {
    		    	if (Integer.valueOf(fileScanner("test.num")) == 1 ) {
    			    if (failed > 0) { fileWriterPrinter("FAILED..."); } else { fileWriterPrinter("PASSED!"); }
    			    }   			
    			if (Integer.valueOf(fileScanner("test.num")) > 1 ) {
    			   if (failed > 0) {
    				   if (failed == Integer.valueOf(fileScanner("test.num")) ) { fileWriterPrinter("ALL FAILED..."); }
    				   else { fileWriterPrinter("FAILED " + failed  + " OF " + Integer.valueOf(fileScanner("test.num"))); }
    			   }
      			   if (failed == 0) { fileWriterPrinter("ALL PASSED!"); }
      			   }			
    			} 		
            }

        	/**
        	 * This METHOD adds Reporter Class to TestNG-Failed XML File
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
        	 * This METHOD reads TestNG-Failed XML File and deletes empty XML CLASS PATH(s) if any
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
        	        if  ((!str.contains("email.All"))
                    && (!str.contains("send.Mail"))
                    && (!str.contains("report.Log"))     	        
            	    && (!str.contains(".helper"))
        	        && (!str.contains("\"logOpen\""))
        	        && (!str.contains("\"startTime\""))
        	        && (!str.contains("\"closeBrowsers\""))
        	        && (!str.contains("\"endTime\""))
        	        && (!str.contains("\"start\""))
        	        && (!str.contains("\"finish\""))      	        
        	        && (!str.contains("groups>")) 
        	        && (!str.contains("run>"))
        	        && (!str.contains("\"TC-"))
        	        && (!str.contains("\"US-"))
        	        && (!str.contains("\"BUG-"))
        	        && (!str.contains("\"CLOSED\""))
        	        && (!str.contains("\"OPEN\""))
        	        && (!str.contains("\"REJECTED\""))    	        
        	        && (str.length() != 0)       	        
        	        	) { lines.add(str); }
        	        }
        	    String[] linesArray = lines.toArray(new String[lines.size()]);
        	    return linesArray;
        	}
        // ####################### TESTNG-FAILED XML CONVERTER END #######################
        	
        // ####################### TEST-FAILED XML MODIFIER START #######################
            /**
         	 * This METHOD modifies Test-Failed XML file into Test-Failed XML of user-selected reporting option
         	 */
             public void testFailedToTestFailedModifier(String suiteName, String testName, String xmlOutputFileName, String reporterClass ) throws IOException {
            	// DECLARATION:
 				String sourceFileName = "test-failed.xml";
     		
         	    // PRE-CLEAN:
         	    if (fileExist(System.getProperty("user.dir"), xmlOutputFileName, false)) {
         	    	fileCopy(System.getProperty("user.dir"), xmlOutputFileName, Common.testOutputFileDir, sourceFileName);
         	    	fileCleaner(System.getProperty("user.dir"), xmlOutputFileName);
         	
         	    // READER-WRITER:     		
     		    String[] string = addReporterClassToTestFailedXmlLinesArray(sourceFileName, reporterClass); // READS TESTNG XML
     		    for (String s : string) { fileWriter(System.getProperty("user.dir"), xmlOutputFileName, s); }

         	    // OPTIONAL FAILURES NUMBER OUTPUT:
     		    int failed = 0;
     		    for (int i = 0; i < string.length; i++) {
 				    if ( string[i].contains("<include name=\"") ) { failed++; }
 			    }
     		    
         		// UPDATE FAILED TEST NUMBER AS PER FAILED:
         		if (fileExist("failed.num", false)){ fileOverWriter("failed.num", failed); }
         		
     		    if (fileExist("test.num", false)) {
     		    	if (Integer.valueOf(fileScanner("test.num")) == 1 ) {
     			    if (failed > 0) { fileWriterPrinter("FAILED..."); } else { fileWriterPrinter("PASSED!"); }
     			    }   			
     			if (Integer.valueOf(fileScanner("test.num")) > 1 ) {
     			   if (failed > 0) {
     				   if (failed == Integer.valueOf(fileScanner("test.num")) ) { fileWriterPrinter("ALL FAILED..."); }
     				   else { fileWriterPrinter("FAILED " + failed  + " OF " + Integer.valueOf(fileScanner("test.num"))); }
     			   }
       			   if (failed == 0) { fileWriterPrinter("ALL PASSED!"); }
       			   }			
     			}
     		    
         	    }
             }
        	
        	/**
        	 * This METHOD adds Reporter Class to Test-Failed XML File
        	 */
        	public String[] addReporterClassToTestFailedXmlLinesArray(String fileName, String reporterClass) throws IOException{
        		String[] string = cleanTestFailedXmlLinesArray(fileName);
        	    
        	    ArrayList<String> lines = new ArrayList<String>();
        	    for (int i = 0; i < string.length; i++) {
        	    	lines.add(string[i]);
					if ( string[i].contains("<classes>") ) { lines.add("      " + reporterClass); }
				}
        	    
        	    String[] linesArray = lines.toArray(new String[lines.size()]);
        	    return linesArray;
        	}
        	
        	/**
        	 * This METHOD reads Test-Failed XML File and deletes empty XML CLASS PATH(s) if any
        	 */
        	public String[] cleanTestFailedXmlLinesArray(String fileName) throws IOException{
        		String[] string = readTestFailedXmlFileOutputLinesArray(fileName);
        		ArrayList<String> noEmptyMethods = new ArrayList<String>();
        		ArrayList<String> noEmptyClass = new ArrayList<String>();
        		
                // EMTY METHODS CLEANER:
        		for (int i = 0; i < string.length; i++) { noEmptyMethods.add(string[i]); }
        		
        		String[] noEmptyMethodsArray = noEmptyMethods.toArray(new String[noEmptyMethods.size()]);
        		
                // EMTY CLASS CLEANER:
				for (int i = 0; i < noEmptyMethodsArray.length; i++) { noEmptyClass.add(noEmptyMethodsArray[i]); }

        		String[] noEmptyClassArray = noEmptyClass.toArray(new String[noEmptyClass.size()]);
				return noEmptyClassArray;
        	}
        	
        	/**
        	 * This METHOD reads any Text File,
        	 * converts and outputs all the text lines as a String Array
        	 */
        	@SuppressWarnings("resource")
        	public String[] readTestFailedXmlFileOutputLinesArray(String fileName) throws IOException{
        	    BufferedReader in = new BufferedReader(new FileReader(Common.testOutputFileDir + fileName));
        	    String str=null;
        	    ArrayList<String> lines = new ArrayList<String>();
        	    while ((str = in.readLine()) != null) {
        	        if  ((!str.contains("email.All"))
                    && (!str.contains("send.Mail"))
                    && (!str.contains("report.Log"))     	        
            	    && (!str.contains(".helper"))
            	    && (!str.contains("\"logOpen\""))
        	        && (!str.contains("\"startTime\""))
        	        && (!str.contains("\"closeBrowsers\""))
        	        && (!str.contains("\"endTime\""))
        	        && (!str.contains("\"start\""))
        	        && (!str.contains("\"finish\""))
                    && (str.length() != 0)       	        
        	        	) { lines.add(str); }
        	        }
        	    String[] linesArray = lines.toArray(new String[lines.size()]);
        	    return linesArray;
        	}
        // ####################### TEST-FAILED XML MODIFIER END #######################
            
        // ####################### TEST-FAILED XML CREATER START #######################
            /**
        	 * This METHOD created Test-Failed XML file based on Source File which contains (or not) XML CLASS PATH(s)
        	 */
        	public void testLogToXmlCreator(String suiteName, String testName, String sourceFileName, String xmlOutputFileName, String reporterClass) throws IOException {
        		// OPTIONAL FAILURES NUMBER OUTPUT:
        		if (fileExist("test.num", false)) {
        			if (fileExist(sourceFileName, false)) {
        				if (Integer.valueOf(fileScanner("test.num")) == 1 ) { fileWriterPrinter("FAILED..."); }
        				else { 
        					if (convertXmlArrayToTestNG(readLogOutputXmlLinesArray(sourceFileName)).length == Integer.valueOf(fileScanner("test.num")))
        					     { fileWriterPrinter("ALL " + convertXmlArrayToTestNG(readLogOutputXmlLinesArray(sourceFileName)).length + " FAILED!" + "\n"); }
        					else { fileWriterPrinter("FAILED: " + convertXmlArrayToTestNG(readLogOutputXmlLinesArray(sourceFileName)).length + " OF " + fileScanner("test.num") + "\n"); }
        				}
        			} else { 
        				    if (Integer.valueOf(fileScanner("test.num")) == 1 ) { fileWriterPrinter("PASSED!"); } 
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
//        			fileOverWriter("prev.num", convertXmlArrayToTestNG(readLogOutputXmlLinesArray(sourceFileName)).length);
//        		}
        			
//        		// UPDATE LAST TEST NUMBER AS PER FAILED:
//        		if (fileExist("last.num", false)){
//        			fileOverWriter("last.num", convertXmlArrayToTestNG(readLogOutputXmlLinesArray(sourceFileName)).length);
//        		}
        			
            	// UPDATE FAILED TEST NUMBER AS PER FAILED:
            	if (fileExist("failed.num", false)){
            		fileOverWriter("failed.num", convertXmlArrayToTestNG(readLogOutputXmlLinesArray(sourceFileName)).length);
            	}
        		
        		// HEADER:
        		fileWriter(System.getProperty("user.dir"), xmlOutputFileName, "<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        		fileWriter(System.getProperty("user.dir"), xmlOutputFileName, "<!DOCTYPE suite SYSTEM \"http://testng.org/testng-1.0.dtd\">");
        		fileWriter(System.getProperty("user.dir"), xmlOutputFileName, "<suite name=\"" + suiteName + "\">");
        		
        		// LISTENERS:
				String packagePath = new Object(){}.getClass().getPackage().getName();
				packagePath = packagePath.substring(0, packagePath.lastIndexOf("."));
				// EXTENT-REPORT XML LISTENER-LINE:
				String extentReporterClassPath = packagePath + ".extentreporter.ExtentReporterNG";			
        		// RE-TRY XML LISTENER-LINE:
				String retryListenerClassPath = packagePath + ".retry.RetryListener";
				// LISTENERS-BLOCK XML-WRITER :
        		fileWriter(System.getProperty("user.dir"), xmlOutputFileName, "  <listeners>");
        		fileWriter(System.getProperty("user.dir"), xmlOutputFileName, "    <listener class-name=\"" + extentReporterClassPath + "\"/>");
				fileWriter(System.getProperty("user.dir"), xmlOutputFileName, "    <listener class-name=\"" + retryListenerClassPath + "\"/>");
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
		// ####################### TEST-FAILED XML CREATER END #######################
        		
        // ####################### TEST-NG XML READER-EXTRACTOR-CREATER START #######################
        	/**
        	 * Extracts Hidden Text from WebElement using jQuery;
        	 */
        	public static String getText(WebDriver driver, WebElement element){
        	    return (String) ((JavascriptExecutor) driver).executeScript("return jQuery(arguments[0]).text();", element); 
        	    }
        	public static String getText(WebDriver driver, By by)         { return getText(driver, driver.findElement(by)); }
        	public static String getText(WebDriver driver, String xpath)  { return getText(driver, By.xpath(xpath)); }
        	public static String getText(String id, WebDriver driver)     { return getText(driver, By.id(id)); }
        	
        	/**
        	 * Extracts selected text line number (1, 2, 3, etc.) from multi-line text
        	 */
        	public String getTextLine(String text, int line) {
        		String[] string = text.split("\\n");
           		if( (string.length >= line) && (line > 0) ) { return string[line - 1]; } 
           		else { return ""; }
           		}

        	/**
        	 * This METHOD reads any Text File and extracts last text line
        	 */
        	public String readFileOutputLastLine(String fileName) throws IOException{
        		String[] string = readFileOutputLinesArray(fileName);
        		return string[string.length - 1];
        		}

        	/**
        	 * This METHOD reads any Text File and extracts last text line
        	 */
        	public String readFileOutputLastLine(String path, String fileName) throws IOException{
        		String[] string = readFileOutputLinesArray(path, fileName);
        		return string[string.length - 1];
        		}
        	
        	/**
        	 * This METHOD reads any Text File and extracts selected text line number (1, 2, 3, etc.)
        	 */
        	public String readFileOutputLine(String fileName, int line) throws IOException{
        		String Line = "";
        		String[] string = readFileOutputLinesArray(fileName);
        		if(line >= 1) {
            		if(line <= string.length) { Line = string[line - 1]; }
            		else { Line = string[string.length - 1]; }
            		}
        		return Line;
        		}
        	
        	/**
        	 * This METHOD reads any Text File and extracts selected text line number (1, 2, 3, etc.)
        	 */
        	public String readFileOutputLine(String path, String fileName, int line) throws IOException{
        		String Line = "";
        		String[] string = readFileOutputLinesArray(path, fileName);
        		if(line >= 1) {
            		if(line <= string.length) { Line = string[line - 1]; }
            		else { Line = string[string.length - 1]; }
            		}
        		return Line;
        		}

        	/**
        	 * This METHOD reads any Text File,
        	 * converts and outputs all the text lines as an ASC sorted String Array
        	 */
        	public String[] readFileOutputLinesArray(String fileName) throws IOException{
        		return readFileOutputLinesArray(Common.testOutputFileDir, fileName);
        		}
        	
        	/**
        	 * This METHOD reads any Text File,
        	 * converts and outputs all the text lines as an ASC sorted String Array
        	 */
        	@SuppressWarnings("resource")
        	public String[] readFileOutputLinesArray(String path, String fileName) throws IOException{
        	    BufferedReader in = new BufferedReader(new FileReader((path + File.separator + fileName).replace(File.separator + File.separator, File.separator)));
        	    String str=null;
        	    ArrayList<String> lines = new ArrayList<String>();
        	    while ((str = in.readLine()) != null) {
        	        if (str.length() != 0) { lines.add(str); }
        	        }
        	    String[] linesArray = lines.toArray(new String[lines.size()]);
        	    return linesArray;
        	}

        	/**
        	 * This METHOD reads any Text File,
        	 * converts and outputs all the text lines as an ASC sorted String Array
        	 */
        	public String[] readTextFileOutputLinesArray(String fileName) throws IOException{
        		return readTextFileOutputLinesArray(Common.testOutputFileDir, fileName);
        		}
        	
        	/**
        	 * This METHOD reads any Text File,
        	 * converts and outputs all the text lines as an ASC sorted String Array
        	 */
        	@SuppressWarnings("resource")
        	public String[] readTextFileOutputLinesArray(String path, String fileName) throws IOException{
        	    BufferedReader in = new BufferedReader(new FileReader((path + File.separator + fileName).replace(File.separator + File.separator, File.separator)));
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
        		return readLogOutputXmlLinesArray(Common.testOutputFileDir, fileName);
        		}  
        	
        	/**
        	 * This METHOD reads Log Files, extracts XML CLASS PATH(s),
        	 * converts and outputs them as an ASC sorted String Array
        	 */
        	public String[] readLogOutputXmlLinesArray(String path, String fileName) throws IOException{
        		String[] string = readTextFileOutputLinesArray(path, fileName);
                Pattern p = Pattern.compile("<class name=\"");
                int i, j; 
                String current, previous;
                
                // FAILURES COUNTER
        		i = 0; j = 0; previous = null; current = null;
        		for (String s : string) {
        			Matcher m = p.matcher(s); Boolean found = m.find();
        			if ( found ) { current = s.toString().substring(s.indexOf("<include name=\"") + 15, s.indexOf("\"/>")); }
        			if ( found && !s.contains(".helper") && (s.length() != 0) && !current.equals(previous) ) { i++; } 
        			if ( found ) { j++; }
        			if ( found && (j > 0)) { previous = s.toString().substring(s.indexOf("<include name=\"") + 15, s.indexOf("\"/>")); }		
        		}
        		
        		// CLASS LINE EXTRACTION
        		String[] linesArray = new String[i];		
        		i = 0; j = 0; previous = null; current = null;
        		for (String s : string) {
        			Matcher m = p.matcher(s); Boolean found = m.find();
        			if ( found ) { current = s.toString().substring(s.indexOf("<include name=\"") + 15, s.indexOf("\"/>")); }
        			if ( found && !s.contains(".helper") && (s.length() != 0) && !current.equals(previous) ) {
        		    	linesArray[i] = s.replace(s.substring(0, s.indexOf("<class name=\"")),"      ");
        		    	i++;
        		    	}
        			if ( found ) { j++; }
        			if ( found && (j > 0)) { previous = s.toString().substring(s.indexOf("<include name=\"") + 15, s.indexOf("\"/>")); }
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
        // ####################### TEST-NG XML READER-EXTRACTOR-CREATER END #######################
        	
        // ####################### LOG FILES HANDLER START #######################
    		/** 
    		 * Cleans all the Log records left from previous test executions
    		 * @throws NumberFormatException 
    		 * @throws IOException 
    		 * @throws ParseException 
    		 */
    		public void beforeCleaner() throws NumberFormatException, IOException, ParseException{
    		    // PRE-CLEANING:
    			testOutputCleaner(true);
    			testOutputTimeReset(true);
    			outputCleaner(true);
                fileCleaner("email.all"  );
                fileCleaner("email.cont" );
                fileCleaner("email.subj" );	
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
                fileCleaner("failed.log" );	
                fileCleaner("failed.num" );
                fileCleaner("failed.try" );
			    fileCleaner("coverage.csv");
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
    			   fileCleaner("failed.try" );
//    			   fileCleaner("test.num"   );
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

            /** Returns Suffix based on Number */
			public static String getNumberSuffix(int num) {
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
				
				public Boolean waitUntilElementPresence(WebDriver driver, int seconds, final String xpath, String elementName, StackTraceElement t, Boolean ifScreenshot) throws IOException {
					return waitUntilElementPresence(driver, seconds, By.xpath(xpath), elementName, t, ifScreenshot);
				}
				
				public Boolean waitUntilElementPresence(WebDriver driver, int seconds, final By element, String elementName, StackTraceElement t) throws IOException {
					return waitUntilElementPresence(driver, seconds, element, elementName, t, true);
				}
							
				public Boolean waitUntilElementPresence(WebDriver driver, int seconds, final String xpath, String elementName, StackTraceElement t) throws IOException {
					return waitUntilElementPresence(driver, seconds, By.xpath(xpath), elementName, t);
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
					waitUntilElementInvisibility(driver, seconds, By.xpath(xpath), elementName, true, t);
					}
				
				public void waitUntilElementInvisibility(WebDriver driver, int seconds, final String xpath, String elementName, Boolean ifScreenshot, StackTraceElement t) throws IOException {
					waitUntilElementInvisibility(driver, seconds, By.xpath(xpath), elementName, ifScreenshot, t);
					}

				public void waitUntilElementInvisibility(WebDriver driver, int seconds, final By element, String elementName, StackTraceElement t) throws IOException {
					waitUntilElementInvisibility(driver, seconds, element, elementName, true, t);
					}
				
				public void waitUntilElementInvisibility(WebDriver driver, int seconds, final By element, String elementName, Boolean ifScreenshot, StackTraceElement t) throws IOException {
					long start = System.currentTimeMillis();
					// SOURCE EXAMPLE of "element" VARIABLE ENTRY:     By element = By.xpath("xpath string");     By element = By.id("id string")   etc.
					WebDriverWait wait = new WebDriverWait(driver, seconds);
					try { wait.until(ExpectedConditions.invisibilityOfElementLocated(element)); }
					catch(Exception e) {
						if (driver.findElements(element).size() > 0) { 
							fileWriterPrinter("\n" + elementName + " is still visible!\n\"By\" ELEMENT: " + element);
							if(ifScreenshot) { getScreenShot(t, elementName.replace("\"", "''") + " invisibility Time-Out", driver); }
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
				public void clickLinkUrlWaitUntil(WebDriver driver, int seconds, By element, Boolean ifPrompt) throws IOException{    	
					final String previousURL = driver.getCurrentUrl();	  	
					driver.findElement(element).click();		  	
					waitUntilUrl(driver, seconds, previousURL, ifPrompt);
					}
				
	            /**
	             * In current page it will click a link/tab/locator and wait until url opens with exception trace control
	             * @throws IOException
	             */
				public void clickLinkUrlWaitUntil(WebDriver driver, int seconds, By by, StackTraceElement t) throws IOException{    
					try {	
						final String previousURL = driver.getCurrentUrl();	  
						driver.findElement(by).click();		  
						waitUntilUrl(driver, seconds, previousURL);					
					} catch(Exception e) { getExceptionDescriptive(e, t, driver); }
					}
				
	            /**
	             * In current page it will click a link/tab/locator and wait until url opens
	             * @throws IOException
	             */
				public void clickLinkUrlWaitUntil(WebDriver driver, int seconds, String locator) throws IOException{    	
					clickLinkUrlWaitUntil(driver, seconds, By.xpath(locator), new Exception().getStackTrace()[0]);
					}
				
	            /**
	             * In current page it will click a link/tab/locator and wait until url opens with exception trace control
	             * @throws IOException
	             */
				public void clickLinkUrlWaitUntil(WebDriver driver, int seconds, String locator, StackTraceElement t) throws IOException{    
					clickLinkUrlWaitUntil(driver, seconds, By.xpath(locator), t);
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
				
	/* ##### NAVIGATION START ##### */		
	public void moveToElement(WebDriver driver, String xpath) throws InterruptedException{
        WebElement element = driver.findElement(By.xpath(xpath));
        Coordinates coordinate = ((Locatable)element).getCoordinates(); 
        coordinate.onPage(); 
        coordinate.inViewPort();
	    Thread.sleep(1000);
	    }
	
	public void moveToElement(WebDriver driver, By element) throws InterruptedException{
        WebElement e = driver.findElement(element);
        Coordinates coordinate = ((Locatable)e).getCoordinates(); 
        coordinate.onPage(); 
        coordinate.inViewPort();
	    Thread.sleep(1000);
	    }
	
	public void moveToElementCenter(WebDriver driver, By element, Boolean ifPrompt) throws InterruptedException, IOException{
        WebElement e = driver.findElement(element);
        String name = ""; 
        int windowCenterX = driver.manage().window().getSize().getWidth()/2;
        int windowCenterY = driver.manage().window().getSize().getHeight()/2;	        
        int elementX = getElementLocationX(driver, e) + getElementWidth(driver, e)/2;
        int elementY = getElementLocationY(driver, e) + getElementHeight(driver, e)/2;	        
        String dirX = "NOT REQUIRED", dirY = "NOT REQUIRED";        
        int scrollX  = windowCenterX - elementX; if(scrollX < 0) {dirX = "LEFT";} else if(scrollX > 0) {dirX = "RIGHT";}
        int scrollY  = windowCenterY - elementY; if(scrollY < 0) {dirY = "UP";  } else if(scrollY > 0) {dirY = "DOWN"; }	        
        if(!e.getText().isEmpty()) { name = "\"" + e.getText() + "\" "; }
        if(ifPrompt) {
        	fileWriterPrinter("\nELEMENT " + name + "WILL BE PLACED INTO CENTER OF SCREEN BY:");
            fileWriterPrinter("SCROLLING (HORIZONTAL): " + Math.abs(scrollX) + " pixels " + dirX);
            }
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(" + scrollX + ",0)", "");
        Thread.sleep(1000);	        
        if(ifPrompt) { fileWriterPrinter("SCROLLING   (VERTICAL): " + Math.abs(scrollY) + " pixels " + dirY + "\n"); }
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0," + scrollY + ")", "");
        Thread.sleep(1000);
        }

	public void moveToWindowTopLeft(WebDriver driver, Boolean ifPrompt) throws InterruptedException, IOException{
        Long valueX = (Long) ((JavascriptExecutor) driver).executeScript("return window.pageXOffset;");
        Long valueY = (Long) ((JavascriptExecutor) driver).executeScript("return window.pageYOffset;");	        
        int x = Integer.valueOf(String.valueOf(valueX));
        int y = Integer.valueOf(String.valueOf(valueY));
        String dirX = "NOT REQUIRED", dirY = "NOT REQUIRED";	        
        int scrollX  = -x; if(scrollX < 0) {dirX = "LEFT";} else if(scrollX > 0) {dirX = "RIGHT";}
        int scrollY  = -y; if(scrollY < 0) {dirY = "UP";  } else if(scrollY > 0) {dirY = "DOWN"; }
        if(ifPrompt) {
        	fileWriterPrinter("\nWINDOW WILL BE NAVIGATED TO TOP-LEFT BY:");
        	fileWriterPrinter("SCROLLING (HORIZONTAL): " + Math.abs(scrollX) + " pixels " + dirX);
        	}
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(" + scrollX + ",0)", "");
        Thread.sleep(1000);
        if(ifPrompt) { fileWriterPrinter("SCROLLING   (VERTICAL): " + Math.abs(scrollY) + " pixels " + dirY + "\n"); }
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0," + scrollY + ")", "");
        Thread.sleep(1000);
        }
	
	public void moveToWindowTop(WebDriver driver, Boolean ifPrompt) throws InterruptedException, IOException{
        Long valueY = (Long) ((JavascriptExecutor) driver).executeScript("return window.pageYOffset;");
        int y = Integer.valueOf(String.valueOf(valueY));
        String dirY = "NOT REQUIRED";
        int scrollY  = -y; if(scrollY < 0) {dirY = "UP";  } else if(scrollY > 0) {dirY = "DOWN"; }
        if(ifPrompt) { 
        	fileWriterPrinter("\nWINDOW WILL BE NAVIGATED TO TOP BY:");
        	fileWriterPrinter("SCROLLING   (VERTICAL): " + Math.abs(scrollY) + " pixels " + dirY + "\n");
        	}
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0," + scrollY + ")", "");
        Thread.sleep(1000);
	    }

	public void scrollVerticalUntilWindowPercent(WebDriver driver, int percentOfWindowToStop, int percentOfWindowToStep, int stepTimeMilliseconds, Boolean ifPrompt, Boolean ifFromTop) throws IOException, InterruptedException {
		if(ifFromTop) { moveToWindowTop(driver, ifPrompt); }
		int Y = driver.manage().window().getSize().getHeight();
	    int y = 0;
	    while ( y < (Y*percentOfWindowToStop/100)) {
	    	    if(ifPrompt) { fileWriterPrinter("Current scroll-bar position (coordinate Y) = " + y); }
				((JavascriptExecutor) driver).executeScript("window.scrollBy(0," + (Y*percentOfWindowToStep/100) + ")", "");
				Thread.sleep(stepTimeMilliseconds);
				Long value = (Long) ((JavascriptExecutor) driver).executeScript("return window.pageYOffset;");
				y = Integer.valueOf(String.valueOf(value));
				}
	    }
	
	public void scrollVerticalUntilWindowPercent(WebDriver driver, int percentOfWindowToStop, int percentOfWindowToStep, int stepTimeMilliseconds, Boolean ifPrompt) throws IOException, InterruptedException {
		scrollVerticalUntilWindowPercent(driver, percentOfWindowToStop, percentOfWindowToStep, stepTimeMilliseconds, ifPrompt, true);
	    }
	
	// SCROLL TO ELEMENT BOTTOM:
	public void scrollToElementBottom(WebDriver driver, By element, Boolean ifPrompt, Boolean ifFromTop) throws NumberFormatException, IOException, InterruptedException{
		int Y = getElementLocationY(driver, element) + getElementHeight(driver, element);
		int H = driver.manage().window().getSize().getHeight();
		double x = Y - H;
		float p = (float) ( ((double)x)/((double)H) * 100 );
		int P = (int)p;
		if(ifPrompt){ fileWriterPrinter("\nBROWSER WINDOW WILL BE SCROLLED DOWN BY: " + P + " %"); }
		scrollVerticalUntilWindowPercent(driver, P, 10, 200, ifPrompt, ifFromTop);
		}
	
	public void scrollToElementBottom(WebDriver driver, String xpath, Boolean ifPrompt) throws NumberFormatException, IOException, InterruptedException{
		scrollToElementBottom(driver, By.xpath(xpath), ifPrompt, true);
		}

	// SCROLL TO ELEMENT CENTER:
	public void scrollToElementCenter(WebDriver driver, By element, Boolean ifPrompt, Boolean ifFromTop) throws NumberFormatException, IOException, InterruptedException{
		double Y = (double)getElementLocationY(driver, element) + (double)getElementHeight(driver, element)/2;
		int H = driver.manage().window().getSize().getHeight();
		double x = (double)Y - ((double)H/2);
		float p = (float) ( x/((double)H) * 100 );
		int P = (int)p;
		if(ifPrompt){ fileWriterPrinter("\nBROWSER WINDOW WILL BE SCROLLED DOWN BY: " + P + " %"); }
		scrollVerticalUntilWindowPercent(driver, P, 10, 200, ifPrompt, ifFromTop);
		}
	
	public void scrollToElementCenter(WebDriver driver, By element, Boolean ifPrompt) throws NumberFormatException, IOException, InterruptedException{
		scrollToElementCenter(driver, element, ifPrompt, true);
		}
	
	public void scrollToElementCenter(WebDriver driver, String xpath, Boolean ifPrompt) throws NumberFormatException, IOException, InterruptedException{
		scrollToElementCenter(driver, By.xpath(xpath), ifPrompt);
		}
	/* ##### NAVIGATION END ##### */
	
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
//		if (n < 0) { n = 0; }
		while (n < 0) { n++; }
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
	
    // ################ AJAX RECOVERY SCENARIOS START ###################
    /**
	 * Performes SendKeys on By-identified Web-Element protected with AJAX Recovery Scenario
     * @throws IOException 
     * @throws NumberFormatException 
	 */
    public void ajaxProtectedSendKeys(WebDriver driver, By element, String name, String sendKeys, Boolean ifThrobber, String throbberXPATH, Boolean ifAttempt, Boolean ifScreenshot ) throws NumberFormatException, IOException{
    	String parentWindowHandle = driver.getWindowHandle();
    	String attempt = "";
        boolean ifAjax = true;
        if(name.length() == 0) { name = driver.findElement(element).getText().replace("(", "").replace(")", ""); }
        int i = 0;
        boolean reason = true;
        while (reason) {
			     try {
			    	 driver.findElement(element).sendKeys(sendKeys);
			    	 Thread.sleep(1000);
			    	 if(ifAttempt && (i > 0)) { attempt = " [Attempt #" + (i+1) + "]"; }
			    	 ifAjax = ifAlertHandler(driver, ifScreenshot, new Exception().getStackTrace()[0], "''" + name + "'' entry" + attempt);
				     if(ifAjax) { closeAllOtherWindows(driver, parentWindowHandle); }
				     if(ifAjax) { fileWriterPrinter("Not a successful \"" + name + "\" entry...will try again..." + attempt); }
			     } catch (Exception e) {}
			     i++;
			     reason = ((i > 0) && (i < 5)) && ifAjax;
        }        
        if(!ifAjax) { fileWriterPrinter("Successful \"" + name + "\" entry!" + attempt); }
        if(!ifAjax && (ifThrobber && (throbberXPATH.length() != 0))) { waitUntilElementInvisibility(driver, 10, throbberXPATH, "Throbber", new Exception().getStackTrace()[0]); } 
    }
    
    /**
	 * Performes Click on By-identified Web-Element protected with AJAX Recovery Scenario with custom maximum number of attempts
	 * If custom maximum number of attempts is negative - will work like maximum number of attempts is un-limited
     * @throws IOException 
     * @throws NumberFormatException 
	 */
    public void ajaxProtectedClick(WebDriver driver, By element, String name, Boolean ifThrobber, String throbberXPATH, Boolean ifAttempt, int maxAttempt, Boolean ifScreenshot ) throws NumberFormatException, IOException{
    	String parentWindowHandle = driver.getWindowHandle();
    	String attempt = "";
        boolean ifAjax = true;
        if(name.length() == 0) { name = driver.findElement(element).getText().replace("(", "").replace(")", ""); }
        int i = 0;
        boolean reason = true;
        while (reason) {
			     try {
			    	 driver.findElement(element).click();
			    	 Thread.sleep(1000);
			    	 if(ifAttempt && (i > 0)) { attempt = " [Attempt #" + (i+1) + "]"; }
			    	 ifAjax = ifAlertHandler(driver, ifScreenshot, new Exception().getStackTrace()[0], "''" + name + "'' click" + attempt);
				     if(ifAjax) { closeAllOtherWindows(driver, parentWindowHandle); }
				     if(ifAjax) { fileWriterPrinter("Not a successful \"" + name + "\" click...will try again..." + attempt); }
			     } catch (Exception e) {}
			     i++;
			     reason = ( (i > 0) && ((i < maxAttempt) || (maxAttempt < 0)) ) && ifAjax;
        }        
        if(!ifAjax) { fileWriterPrinter("Successful \"" + name + "\" click!" + attempt); }
        if(!ifAjax && (ifThrobber && (throbberXPATH.length() != 0))) { waitUntilElementInvisibility(driver, 10, throbberXPATH, "Throbber", new Exception().getStackTrace()[0]); }    
    }
    
    /**
	 * Performes Click on XPATH-identified Web-Element protected with AJAX Recovery Scenario
     * @throws IOException 
     * @throws NumberFormatException 
	 */
    public void ajaxProtectedClick(WebDriver driver, String locator, String name, Boolean ifThrobber, String throbberXPATH, Boolean ifAttempt, int maxAttempt, Boolean ifScreenshot ) throws NumberFormatException, IOException{
    	ajaxProtectedClick(driver, By.xpath(locator), name, ifThrobber, throbberXPATH, ifAttempt, maxAttempt, ifScreenshot);
    }
    
    /**
	 * Performes Click on By-identified Web-Element protected with AJAX Recovery Scenario with maximum 5 attempts
     * @throws IOException 
     * @throws NumberFormatException 
	 */
    public void ajaxProtectedClick(WebDriver driver, By element, String name, Boolean ifThrobber, String throbberXPATH, Boolean ifAttempt, Boolean ifScreenshot ) throws NumberFormatException, IOException{
    	ajaxProtectedClick(driver, element, name, ifThrobber, throbberXPATH, ifAttempt, 5, ifScreenshot);
    }
    
    /**
	 * Performes Click on XPATH-identified Web-Element protected with AJAX Recovery Scenario
     * @throws IOException 
     * @throws NumberFormatException 
	 */
    public void ajaxProtectedClick(WebDriver driver, String locator, String name, Boolean ifThrobber, String throbberXPATH, Boolean ifAttempt, Boolean ifScreenshot ) throws NumberFormatException, IOException{
    	ajaxProtectedClick(driver, By.xpath(locator), name, ifThrobber, throbberXPATH, ifAttempt, ifScreenshot);
    }
	// ################ AJAX RECOVERY SCENARIOS END ###################
	
	
	public boolean isAlertPresent(WebDriver driver){
        try{ driver.switchTo().alert(); return true; }
		catch(Exception e){ return false; }
    }
	
	public void alertHandler(WebDriver driver) throws NumberFormatException, IOException, InterruptedException{
	  if(isAlertPresent(driver)){
        driver.switchTo().alert();
        String text = driver.switchTo().alert().getText();
        fileWriterPrinter(text);
        driver.switchTo().alert().accept();
        waitUntilElementInvisibility(driver, 5, Common.TextEntireToXpath(text), text.substring(0, 21), new Exception().getStackTrace()[0]);
        driver.switchTo().defaultContent();
      }
	}
	
	public boolean ifAlertHandler(WebDriver driver) throws NumberFormatException, IOException, InterruptedException{
		boolean ifAlert = isAlertPresent(driver);
		if(isAlertPresent(driver)){
	        driver.switchTo().alert();
	        String text = driver.switchTo().alert().getText();
	        fileWriterPrinter(text);
	        driver.switchTo().alert().accept();
	        waitUntilElementInvisibility(driver, 5, Common.TextEntireToXpath(text), text.substring(0, 21), new Exception().getStackTrace()[0]);
	        driver.switchTo().defaultContent();
	      }
		return ifAlert;
		}
	
	public boolean ifAlertHandler(WebDriver driver, Boolean ifScreenshot, StackTraceElement t, String comment) throws NumberFormatException, IOException, InterruptedException{
		boolean ifAlert = isAlertPresent(driver);
		if(isAlertPresent(driver)){
	        driver.switchTo().alert();
	        String text = driver.switchTo().alert().getText();
	        fileWriterPrinter(text);
	        driver.switchTo().alert().accept();
	        if(!ifScreenshot) { waitUntilElementInvisibility(driver, 5, Common.TextEntireToXpath(text), text.substring(0, 21), ifScreenshot, t); }
	        driver.switchTo().defaultContent();
	        if(ifScreenshot) { getScreenShot(new Exception().getStackTrace()[0], text.substring(0, 21) + " " + comment, driver); }
	      }
		return ifAlert;
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
		if(ifPrint){ fileWriterPrinter("\n" + comment + space + Color.fromString(color).asHex().toUpperCase()); }
		return Color.fromString(color).asHex().toUpperCase();		
	}
	
	/** 
	 * Output HEX-color of Element identified by Xpath
	 * @throws IOException 
	 * @throws NumberFormatException 
	 */
	public String getColorHEX(WebDriver driver, String xpath, String css, Boolean ifPrint, String comment) throws NumberFormatException, IOException {
		return getColorHEX(driver, By.xpath(xpath), css, ifPrint, comment);		
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
	
	/** Outputs element X-Location */
	public int getElementLocationX(WebDriver driver, WebElement element) { return element.getLocation().getX(); }
	public int getElementLocationX(WebDriver driver, String xpath) { return getElementLocationX(driver, driver.findElement(By.xpath(xpath))); }
	
	/** Outputs element Y-Location */
	public int getElementLocationY(WebDriver driver, WebElement element) { return element.getLocation().getY(); }
	public int getElementLocationY(WebDriver driver, By element) { return getElementLocationY(driver, driver.findElement(element)); }
	public int getElementLocationY(WebDriver driver, String xpath) { return getElementLocationY(driver, driver.findElement(By.xpath(xpath))); }
	
	/** Outputs element Hight */
	public int getElementHeight(WebDriver driver, WebElement element) { return element.getSize().getHeight(); }
	public int getElementHeight(WebDriver driver, By element) { return getElementHeight(driver, driver.findElement(element)); }
	public int getElementHeight(WebDriver driver, String xpath) { return getElementHeight(driver, driver.findElement(By.xpath(xpath))); }
	
	/** Outputs element Width */
	public int getElementWidth(WebDriver driver, WebElement element) { return element.getSize().getWidth(); }
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

	// ################ FILE SIZE START ####################
	/**
     * This class gets file size
     * @throws IOException 
     * @throws NumberFormatException 
     */
    public double fileSizeBit(String filePath) throws NumberFormatException, IOException {
    	File file = new File(filePath.replace("\\", "/"));
        if(file.exists()) {
            fileWriterPrinter("FILE SIZE: " + file.length() + " Bit");
         // fileWriterPrinter("FILE SIZE: " + file.length()/1024 + " Kb");
         // fileWriterPrinter("FILE SIZE: " + ((double) file.length()/(1024*1024)) + " Mb");
            return (double) file.length();
        } else { fileWriterPrinter("File doesn't exist"); return 0; }
    }
    
	/**
     * This class gets file size
     * @throws IOException 
     * @throws NumberFormatException 
     */
    public double fileSizeKB(String filePath) throws NumberFormatException, IOException {
    	File file = new File(filePath.replace("\\", "/"));
        if(file.exists()) {
         // fileWriterPrinter("FILE SIZE: " + file.length() + " Bit");
            fileWriterPrinter("FILE SIZE: " + file.length()/1024 + " Kb");
         // fileWriterPrinter("FILE SIZE: " + ((double) file.length()/(1024*1024)) + " Mb");
            return (double) file.length()/1024;
        } else { fileWriterPrinter("File doesn't exist"); return 0; }
    }
    
	/**
     * This class gets file size MB
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
     * This class gets file size Bit, KB, MB
     * @throws IOException 
     * @throws NumberFormatException 
     */
    public double fileSize(String filePath, String units, Boolean ifPrompt) throws NumberFormatException, IOException {
    	File file = new File(filePath.replace("\\", "/"));
        String Units = "";
    	if(file.exists()) {
        	if(units.toUpperCase().substring(0,1).equals("B")) { Units = " Bit"; }
        	if(units.toUpperCase().substring(0,1).equals("K")) { Units = " KB";  }
        	if(units.toUpperCase().substring(0,1).equals("M")) { Units = " MB";  }
        	
        	if(ifPrompt) { 
        	    if(units.toUpperCase().substring(0,1).equals("B")) { fileWriterPrinter("FILE SIZE: " + (double) file.length() + Units); } else
            	if(units.toUpperCase().substring(0,1).equals("K")) { fileWriterPrinter("FILE SIZE: " + (double) file.length()/1024 + Units); } else
                if(units.toUpperCase().substring(0,1).equals("M")) { fileWriterPrinter("FILE SIZE: " + (double) file.length()/(1024*1024) + Units); }
        	}
        	
        	if(units.toUpperCase().substring(0,1).equals("B")) { return (double) file.length(); } else
        	if(units.toUpperCase().substring(0,1).equals("K")) { return (double) file.length()/1024; } else
            if(units.toUpperCase().substring(0,1).equals("M")) { return (double) file.length()/(1024*1024); } else return 0;
            
        } else { fileWriterPrinter("File doesn't exist"); return 0; }
    }
    
	/**
     * This class gets file size as String
     * @throws IOException
     */
	public String getFileSpaceSize(String filePath, String units, int decimals) throws IOException {
		DecimalFormat f = new DecimalFormat(decimalsNumberToFormat(decimals));
		return f.format(fileSize(filePath, units, false));
		}
	
	/**
     * This class generates format String
     */
	public String decimalsNumberToFormat(int decimals) {
		String format = "#";
		if(decimals > 0) { 
			format = format + ".";
			for (int i = 0; i < decimals; i++) {
				format = format + "#";
			}
			}	
		return format;
		}
	// ################ FILE SIZE END ####################
    
	// ################ ZIP METHODS START #########################
	/**
	 * Create a Zip File from given Directory using ZipOutputStream CLASS with Path Structure and Protection choise
	 */
	 public void zipDirectory(String sourceDirectoryPath, String zipOutputPath, Boolean ifAbsolutePath, Boolean ifProtect, int maxZipMbSizeToProtect) throws Exception {		  
		   if(ifAbsolutePath) { zipOutputPath = zipDirectoryFullPath(sourceDirectoryPath, zipOutputPath); }
		   else { zipOutputPath = zipDirectoryInternalPath(sourceDirectoryPath, zipOutputPath); }		   
		   if( (fileSizeMB(zipOutputPath) < maxZipMbSizeToProtect) &&  (fileSizeBit(zipOutputPath) > 22) ) {
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
	     * @throws IOException 
	     * @throws NumberFormatException 
	     */
	    public void fileRename(String sourcePath, String targetPath) {
	    	File file = new File(sourcePath); 
	    	file.renameTo(new File(targetPath));
	    	}
		// ################# ZIP METHODS END ##########################
	       
		/**
	     * This Method generates JavaScript Alert shown during user difined time in seconds
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
	    // ########### DRAG AND DROP END ############
	    
	    // ########### ELEMENT APPEARER START ############
	    /** This Method forces to move selected element between left and right arrow scroller-buttons
	     * @throws InterruptedException 
	     * @throws IOException 
	     * @throws NumberFormatException 
	     */
	    public int clickToAppear(WebDriver driver, String buttonLeft, String buttonRight, String elementXpath, Boolean ifRight, Boolean ifPrompt) throws InterruptedException, NumberFormatException, IOException {
	    	// LEFT-RIGHT coordinates
	        int buttonLeftX = driver.findElement(By.xpath(buttonLeft)).getLocation().getX();
	        int buttonRightX = driver.findElement(By.xpath(buttonRight)).getLocation().getX();

            // MEASURE THE WIDTH:
            int leftWidth = getElementWidth(driver, buttonLeft);
            int elementWidth = getElementWidth(driver, elementXpath);
            
	        // MEASURE THE ECURRENT LOCATION:
	        int elementCurrentCoordinateX = driver.findElement(By.xpath(elementXpath)).getLocation().getX();
           
            // APEARER:
	        int i = 0;
	        String buttonClick = buttonLeft;
	        String arrow = "LEFT  ";
	        if(ifRight) { buttonClick = buttonRight; arrow = "RIGHT "; }
	        while( (driver.findElement(By.xpath(elementXpath)).getLocation().getX() < (buttonLeftX + leftWidth + 1))
	        	   || 
	        	   (driver.findElement(By.xpath(elementXpath)).getLocation().getX() > (buttonRightX - elementWidth - 1))
	        	 ) { driver.findElement(By.xpath(buttonClick)).click(); Thread.sleep(1000); i++; }

            // MEASURE NEW LOCATION:
	        if(ifPrompt) { fileWriterPrinter("\n" + "APPEARS!"); }
		    int elementMovedCoordinateX = driver.findElement(By.xpath(elementXpath)).getLocation().getX();
		    int diff = elementMovedCoordinateX - elementCurrentCoordinateX;
		    if(i == 0) { if(ifPrompt) { fileWriterPrinter("RE-LOCATION:   NOT REQUIRED"); } }
		    else       { if(ifPrompt) { 
		    	                        fileWriterPrinter("ELEMENT     APPEARED  AFTER " + i + " CLICKS ON " + arrow + "ARROW");
		    	                        fileWriterPrinter("ELEMENT HORIZONTAL MOVEMENT: " + diff + " UNITS");
		    	                      }
		               }
		    Thread.sleep(1000);
		    return i;
		    }

	    /** This Method forces to move selected element out of between left and right arrow scroller-buttons
	     * @throws InterruptedException 
	     * @throws IOException 
	     * @throws NumberFormatException 
	     */
	    public int clickToDisAppear(WebDriver driver, String buttonLeft, String buttonRight, String elementXpath, Boolean ifRight, Boolean ifPrompt) throws InterruptedException, NumberFormatException, IOException {
	    	// LEFT-RIGHT coordinates
	        int buttonLeftX = driver.findElement(By.xpath(buttonLeft)).getLocation().getX();
	        int buttonRightX = driver.findElement(By.xpath(buttonRight)).getLocation().getX();

            // MEASURE THE WIDTH:
            int leftWidth = getElementWidth(driver, buttonLeft);
            int elementWidth = getElementWidth(driver, elementXpath);
            
	        // MEASURE THE ECURRENT LOCATION:
	        int elementCurrentX = driver.findElement(By.xpath(elementXpath)).getLocation().getX();
           
	     // APEARER:
	        int i = 0;
	        String buttonClick = buttonLeft;
	        String arrow = "LEFT  ";
	        if(ifRight) { buttonClick = buttonRight; arrow = "RIGHT "; }
	        while( (driver.findElement(By.xpath(elementXpath)).getLocation().getX() > (buttonLeftX + leftWidth - elementWidth - 1))
	        	   && 
	        	   (driver.findElement(By.xpath(elementXpath)).getLocation().getX() < (buttonRightX + 1))
	        	 ) { driver.findElement(By.xpath(buttonClick)).click(); Thread.sleep(1000); i++; }

            // MEASURE NEW LOCATION:
	        if(ifPrompt) { fileWriterPrinter("\n" + "DISAPPEAR!"); }
		    int elementMovedX = driver.findElement(By.xpath(elementXpath)).getLocation().getX();
		    int diff = elementMovedX - elementCurrentX;
		    if(i == 0) { if(ifPrompt) { fileWriterPrinter("RE-LOCATION:   NOT REQUIRED"); } }
		    else       { if(ifPrompt) { 
                                        fileWriterPrinter("ELEMENT  DISAPPEARED  AFTER " + i + " CLICKS ON " + arrow + "ARROW");
		    	                        fileWriterPrinter("ELEMENT HORIZONTAL MOVEMENT: " + diff + " UNITS");
		    	                      }
		               }
		    Thread.sleep(1000);
		    return i;
		    }
	    // ########### ELEMENT APPEARER END ############ 
}