package com.tvokids.locator;

import java.io.File;
import java.io.IOException;

import com.tvokids.utilities.*;

@SuppressWarnings("static-access")
public class Email {
	  static UtilitiesTestHelper helper = new UtilitiesTestHelper();
	
	  /**********common credentiald***********/
	  public static String autoTesterEmail    = "selenium.automated.test.report@gmail.com";
	  public static String autoTesterPassword = "!Puna36Rinta48!";
	  public static String autoTesterUsername = autoTesterEmail.substring(0, autoTesterEmail.indexOf("@"));
	  
	  /**********common email addresses***********/
	  // TESTERS E-MAIL ADDRESSES
	  public static String testerHomeEmail = "roman.weinbrand@gmail.com";
	  public static String testerWorkEmail = "rweinbrand@tvo.org";
	  
	  // MANAGERS E-MAIL ADDRESSES  			  
	  public static String managerEmail = managerEmail();
	  public static String managerEmail() {
		  String managerEmail = testerWorkEmail;
		  if(System.getProperty("URL").contains("dev30.tvo.org"))      { managerEmail = "ukhan@tvo.org"; }
		  if(System.getProperty("URL").contains("qa-kids.tvokids.com")){ managerEmail = "ckatz@tvo.org"; }
		  return managerEmail;
		  }
			  
	  // PRODUCTION E-MAIL ADDRESSES
	  public static String[] toAll = { managerEmail, };
	  
	  public static String[] ccAll() { 
		  String[] ccAll = {""};
		  if(System.getProperty("URL").contains("dev30.tvo.org"))      { ccAll = ccDEV; }
		  if(System.getProperty("URL").contains("qa-kids.tvokids.com")){ ccAll = ccQA; }
		  return ccAll;
		  }
	  public static String[] ccDEV = { testerWorkEmail,"aporretta@tvo.org","lkumar@tvo.org","sreeves@echidna.ca" };
	  public static String[] ccQA =  { testerWorkEmail,"ssrinivasan@tvo.org","pdrinkwater@tvo.org","ltrotsky@tvo.org","jvijaya@tvo.org","ahanda@tvo.org" };
	  
	  public static String[] bccAll = { /** autoTesterEmail,*/ testerHomeEmail };
	  
//	  // UNIT TEST E-MAIL ADDRESSES
//	  public static String[] toAll = { testerWorkEmail };
//	  public static String[] ccAll = { "123@abc.org" };	  
//	  public static String[] bccAll = {"456@abc.org" };
	  
      // DEBUGGING E-MAIL ADDRESSES
	  public static String[] toTester  = { testerWorkEmail };
	  public static String[] ccTester  = { testerHomeEmail };
	  public static String[] bccTester = { autoTesterEmail };
	  
	  // "TO" E-MAIL ADDRESSES LOGIC
	  public static String[] to() throws NumberFormatException, IOException{
		String[] to = toTester;
		if( helper.fileExist("email.all", false) && Boolean.valueOf(helper.fileScanner("email.all")) ) { to = toAll; }
	    return to;
	  }
	  
	  // "CC" E-MAIL ADDRESSES LOGIC
	  public static String[] cc() throws NumberFormatException, IOException{
		String[] cc = ccTester;
		if( helper.fileExist("email.all", false) && Boolean.valueOf(helper.fileScanner("email.all")) ) { cc = ccAll(); }
	    return cc;
	  }
	  
	  // "BCC" E-MAIL ADDRESSES LOGIC
	public static String[] bcc() throws NumberFormatException, IOException{
		String[] bcc = bccTester;
		if( helper.fileExist("email.all", false) && Boolean.valueOf(helper.fileScanner("email.all")) ) { bcc = bccAll; }
	    return bcc;
	  }
	  
	  public static String subject = "This is subject";
	  public static String text = "This is content";
	  
	  /**********common email single attachment***********/
	  public static String attachmentFullPath = Common.testOutputFileDir + "emailable-report.html";
	  public static String attachmentFileName = attachmentFullPath.substring(
				                                attachmentFullPath.lastIndexOf("\\") + 1,
				                                attachmentFullPath.length()
				                                );
	  
	  /**********common email multi attachments***********/
	  public static String[] attachments = { 
		                                     Common.testOutputFileDir + "emailable-report.html",
		                                     Common.testOutputFileDir + "extent-test-report.html",
		                                     Common.testOutputFileDir + "run.log",
		                                     Common.testOutputFileDir + "failed.log",
		                                     Common.testOutputFileDir + "coverage.csv",
		                                     Common.testOutputFileDir + "screen-shots.renameToZip"
		                                    };
	  
	  // ATTACHMENT PATHS VALIDATION
	  public static String[] attachmentFullPaths() {
			int j = 0;
			for (int i = 0; i < Email.attachments.length; i++) {		  
				  File f = new File(Email.attachments[i]);
				  if ((f.exists() && f.isFile()) ) { j++;}
				  }
			  String[] paths = new String [j];
			  int n = 0;	  
			  for (int i = 0; i < Email.attachments.length; i++) {
				  File f = new File(Email.attachments[i]);
				  if ((f.exists() && f.isFile()) ) {
					  paths[n] = Email.attachments[i];
				  n++;
				  }
				  } 
			  return paths;
	  }

	  public static String[] attachmentFileNames() {
		  String[] names = new String [attachmentFullPaths().length];
		  for (int i = 0; i < attachmentFullPaths().length; i++) {
			  names[i] = attachmentFullPaths()[i].substring(
  				         attachmentFullPaths()[i].lastIndexOf("\\") + 1,
  				         attachmentFullPaths()[i].length()
  				         );
			  }
		  return names;
	  }
	  
	  /**********common gmail parameters***********/
	  public static String gmailDomain             = "gmail.com";
	  public static String gmailHost               = "smtp.gmail.com";
	  public static String gmailPort               = "465";
	  public static String gmailStarttls           = "true";
	  public static String gmailAuth               = "true";
	  public static boolean gmailDebug             = true;
	  public static String gmailSocketFactoryClass = "javax.net.ssl.SSLSocketFactory";
	  public static String gmailFallback           = "false";
	  
	  /**********common email content***********/
	  public static String whoSent     = "This e-mail has been sent automatically by \"Selenium WebDriver\" Auto-Testing Tool combined with \"TestNG\" framework";
	  public static String aboutTool   = "For more details and most up-to-date information please visit our website at http://www.seleniumhq.org and http://testng.org";
	  public static String doNotReply  = "Please do not reply to this email, as we are unable to respond from this address";
	  public static String unsubscribe = "Don't want to receive this notification anymore? Send email to \"rweinbrand@tvo.org\" with subject \"unsubscribe\"";
	  
	  public static String[] header = { whoSent, aboutTool, doNotReply, unsubscribe };
	  public static String[] footer = { whoSent, aboutTool, doNotReply, unsubscribe };
		
}
