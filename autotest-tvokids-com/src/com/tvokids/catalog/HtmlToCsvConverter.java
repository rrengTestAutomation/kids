package com.tvokids.catalog;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.tvokids.common.Locators;


/**
 * Converts HTML into CSV
 */
public class HtmlToCsvConverter {
	
	protected static String url = "file:///C:/Users/rweinbrand/workspace/tvo/tvo-d7/auto-java-test/doc";
	public static  WebDriver driver = new FirefoxDriver();
	protected static String[] testDescription;
	protected static String [] dateCreated;
	protected static String [] dateModified;
	protected static String [] originalVersion;
	protected static String [] modifiedVersion;
	protected static String [] xpath;
	protected static String [] userStories;
	protected static BufferedWriter bw;
	public static String currentPackage = "";
	
    /**
     * @throws InterruptedException
     */
    public static void main(String[] args) {
		try{
			String filePath = System.getProperty("user.dir") + Locators.localFileDir + File.separator + "testList.csv";
			File file = new File(filePath);

		 // if file doesnt exists, then create it:
			if (!file.exists()) { file.createNewFile(); }

			FileWriter fw = new FileWriter(file.getAbsoluteFile());

			bw = new BufferedWriter(fw);
			bw.write("Date Created");
			bw.write(",");
			bw.write("Date Modified");
			bw.write(",");
			bw.write("Original Version");
			bw.write(",");
			bw.write("Modified Version");
			bw.write(",");
			bw.write("XPATH");
			bw.write(",");
			bw.write("User Stories");
			bw.write(",");
			bw.write("Group");
			bw.write(",");
			bw.write("Class");
			bw.write(",");
			bw.write("Test");
			bw.write(",");
			bw.write("Description");
			bw.write("\n");
			System.out.println(filePath);			
			writeFinal("C:/Users/rweinbrand/workspace/tvo/tvo-d7/auto-java-test/doc", fw);
			bw.close();
		} catch (Exception e) {  e.printStackTrace(); } finally { driver.close(); }	
    }

    public static String[] parseHTML(String url) throws InterruptedException {
		driver.get(url);
		Thread.sleep(2000);
		
		List <WebElement> list  = driver.findElements(By.xpath("//*[@class='colLast']/descendant::a[contains(text(), 'test')]"));
		List <WebElement> list1 = driver.findElements(By.xpath("//*[@class='colLast']/descendant::div"));
		
		String[] testname = new String[list.size()];
		testDescription   = new String[list1.size()];
		dateCreated       = new String[list1.size()];
		dateModified      = new String[list1.size()];
		originalVersion   = new String[list1.size()];
		modifiedVersion   = new String[list1.size()];
		xpath             = new String[list1.size()];
		userStories       = new String[list1.size()];
		
		for(int i = 0; i < list.size(); i++) {
			testname[i] = list.get(i).getText();
			testDescription[i] = list1.get(i).getText();
			System.out.println(testname);

			String dateCreatedXPath     = "//*[@class='details']/descendant::h4[text()='" + testname[i] +"']/following-sibling::div/p[1]";
			String dateModifiedXPath    = "//*[@class='details']/descendant::h4[text()='" + testname[i] +"']/following-sibling::div/p[2]";
			String originalVersionXPath = "//*[@class='details']/descendant::h4[text()='" + testname[i] +"']/following-sibling::div/p[3]";
			String modifiedVersionXPath = "//*[@class='details']/descendant::h4[text()='" + testname[i] +"']/following-sibling::div/p[4]";
			String xpathPath            = "//*[@class='details']/descendant::h4[text()='" + testname[i] +"']/following-sibling::div/p[5]";
			String userStoriesPath      = "//*[@class='details']/descendant::h4[text()='" + testname[i] +"']/following-sibling::div/p[6]";
			
			List <WebElement> element = driver.findElements(By.xpath(dateCreatedXPath));
			if (element.size() > 0) {
				dateCreated[i] = element.get(0).getText();
				dateCreated[i] = dateCreated[i].substring(dateCreated[i].indexOf(":") + 1);
			}
			else dateCreated[i] = "";
			
			element = driver.findElements(By.xpath(dateModifiedXPath));
			if (element.size() > 0) {
				dateModified[i] = element.get(0).getText();
				dateModified[i] = dateModified[i].substring(dateModified[i].indexOf(":") + 1 );
			}
			else dateModified[i] = "";
			
			element = driver.findElements(By.xpath(originalVersionXPath));
			if (element.size() > 0) {
				originalVersion[i] = element.get(0).getText();
				originalVersion[i] = originalVersion[i].substring(originalVersion[i].indexOf(":") + 1);
			}
			else originalVersion[i] = "";
			
			element = driver.findElements(By.xpath(modifiedVersionXPath));
			if (element.size() > 0) {
				modifiedVersion[i] = element.get(0).getText();
				modifiedVersion[i] = modifiedVersion[i].substring(modifiedVersion[i].indexOf(":") + 1);
			}
			else modifiedVersion[i] = "";
			
			element = driver.findElements(By.xpath(xpathPath));
			if (element.size() > 0) {
				xpath[i] = element.get(0).getText();
				xpath[i] = xpath[i].substring(xpath[i].indexOf(":") + 1);
			}
			else xpath[i] = "";
			
			element = driver.findElements(By.xpath(userStoriesPath));
			if (element.size() > 0) {
				userStories[i] = element.get(0).getText();
				userStories[i] = userStories[i].substring(userStories[i].indexOf(":") + 1);
			}
			else userStories[i] = "";
		}
		return testname;
    }

    public static void writeToCSV(String currentPackage, String currentFile, String[] testnames, FileWriter fw) throws IOException {
		for(int i = 0; i < testnames.length; i++) {
				bw.write(dateCreated[i]);
				bw.write(",");
				bw.write(dateModified[i]);
				bw.write(",");
				bw.write(originalVersion[i]);
				bw.write(",");
				bw.write(modifiedVersion[i]);
				bw.write(",");
				bw.write(xpath[i]);
				bw.write(",");
				bw.write(userStories[i]);
				bw.write(",");
				bw.write(currentPackage);
				bw.write(",");
				bw.write(currentFile);
				bw.write(",");
				bw.write(testnames[i]);
				bw.write(",");
				bw.write(testDescription[i]);
				bw.write("\n");
		}
		System.out.println("Done");
    }
 
    public static void writeFinal(String dir, FileWriter fw) throws InterruptedException, IOException {
		File[] javadocFolder = new File(dir).listFiles();
		String currentFile = "";
		for (File javadoc : javadocFolder) {
			if (!javadoc.toString().contains("package") && !javadoc.toString().contains("class-use")) {
				if (javadoc.isDirectory()) {
					currentPackage = javadoc.toString().substring(javadoc.toString().lastIndexOf(File.separator) + 1);
					writeFinal(javadoc.toString(), fw);
				}
				String []testnames = parseHTML(javadoc.toString());				
				if(javadoc.toString().endsWith(".html")){
					currentFile = javadoc.toString().substring(javadoc.toString().lastIndexOf(File.separator) + 1, javadoc.toString().indexOf(".html"));
				}			
				writeToCSV(currentPackage, currentFile, testnames, fw);
			}	
		}
	}
}