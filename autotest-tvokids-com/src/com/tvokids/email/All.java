package com.tvokids.email;

import java.io.IOException;
import java.text.ParseException;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;

import org.testng.annotations.*;

import com.tvokids.locator.Email;
import com.tvokids.locator.Common;
import com.tvokids.test.helper.*;

@SuppressWarnings({ "static-access" })
public class All {
	UtilitiesTestHelper helper = new UtilitiesTestHelper();
	
	/** 
	 * Test Start-Up management;
	 * @throws IOException 
	 * @throws ParseException
	 * @throws InterruptedException
	 * @throws NumberFormatException 
	 */
	@BeforeSuite /** (groups = {"START",}) */
	public void start() throws IOException, ParseException, InterruptedException, NumberFormatException {
		// ERASING PREVIOUS TEST LOGS AND RECORDS:
		// beforeCleaner();
		helper.beforeCleaner();

		// PREVIOUS NUMBER OF TESTS MANAGEMENT:
		lastToPrev();
		
		// TEST TYPE MANAGEMENT:
		String test = testType();
		if(helper.fileExist("test.type", false)) { helper.fileCleaner("test.type"); }
		helper.fileWriter("test.type", test);
		
		// SHOW TEST NUMBER DIFFERENCE MANAGEMENT:
		Boolean show = addTestOption();
		if(helper.fileExist("add.show", false)) { helper.fileCleaner("add.show"); }
		helper.fileWriter("add.show", show);
		if( helper.fileExist("add.show", false) && Boolean.valueOf(helper.fileScanner("add.show")) ) {
			     System.out.println("Will show the difference between quantity of Tests executed during the last and previous runs!\n");
		} else { System.out.println("Won't show the difference between quantity of Tests executed during the last and previous runs...\n"); }

		// TEST DATE AND TIME MANAGEMENT:
		String current = helper.getCurrentDateTimeHourMinSec();
		System.out.println(current + "\n");
		long currTime = helper.convertCalendarDateTimeHourMinSecToMillisecondsAsLong(current);
		long startTime = helper.convertCalendarDateTimeHourMinSecToMillisecondsAsLong(dateBox());	
		long updateDelay = (startTime - currTime);
		int sec = (int) updateDelay/1000;
		
		// TEST HOST APPLICATION SERVER MANAGEMENT:
		@SuppressWarnings("unused") String server = devServer();
		
		// GIT BRANCH MANAGEMENT:
		String branch = gitBranch();
		System.out.println("Update will be started on: \"" + branch + "\" GiT Branch");
		
		// E-MAIL PERMEATION MANAGEMENT
		boolean send = emailOptionDouble();
		if(helper.fileExist("email.opt", false)) { helper.fileCleaner("email.opt"); }
		helper.fileWriter("email.opt", send);
		if( helper.fileExist("email.opt", false) && Boolean.valueOf(helper.fileScanner("email.opt")) ) {
			   System.out.println("\nWill send Automated E-Mail notification about Test Results!\n");
			   
			   // E-MAIL ADDRESSES SELECTION MANAGEMENT:
			   boolean all = emailAddresses();
			   if(helper.fileExist("email.all", false)) { helper.fileCleaner("email.all"); }
			   helper.fileWriter("email.all", all);
			   if( helper.fileExist("email.all", false) && Boolean.valueOf(helper.fileScanner("email.all")) ) {
				   System.out.println("E-Mail will be sent to All assigned Recepients!\n");
			   } else { System.out.println("E-Mail will be sent to Automation Tester Only...\n"); }
			   
		} else { System.out.println("Will not send any E-Mail notification...\n"); }
		
		// TEST DELAY MANAGEMENT:
		int testDelay = minBox();
		if (testDelay != 0 ) {
		System.out.println("  Test will be started in: " + helper.convertTimeSecondsToHoursMinSeconds(sec + testDelay*60));
		System.out.println("  Test will be started at: " + helper.convertCalendarMillisecondsAsLongToDateTimeHourMinSec(currTime + updateDelay + testDelay*60*1000));
		System.out.println("\nwait please...\n");
		}
		
		long sleep = (helper.convertCalendarDateTimeHourMinSecToMillisecondsAsLong(helper.convertCalendarMillisecondsAsLongToDateTimeHourMinSec(currTime + updateDelay + testDelay*60*1000)) -
				      helper.convertCalendarDateTimeHourMinSecToMillisecondsAsLong(helper.getCurrentDateTimeHourMinSec()));
				
		if (testDelay != 0 ) { Thread.sleep(sleep); }
		System.out.println(helper.getCurrentDateTimeHourMinSec());
		System.out.println("Starting...\n");
		
		// CREATE NEW TEST LOG RECORD:
		logOpen();
		}
	
	/** 
	 * Creates a new Test Log record as a text file named "run.log"
	 * 
	 * create file example:
	 * File f = new File(<full path string>);
	 * f.createNewFile();
	 * 
	 * @throws IOException 
	 * @throws NumberFormatException 
	 *  
	 */	
	public void logOpen() throws NumberFormatException, IOException{		
		String time = helper.getCurrentDateTimeFull();  // System.out.print(" TEST START: " + time + "\n");
		helper.fileWriter("ini.time", helper.convertLongToString(System.currentTimeMillis()));     
		// INITIAL LOG RECORD:
		helper.fileWriter("run.log", " TEST START: " + time);
	    helper.fileWriter("run.log", "");
	}
	
	/**
	 * Closes the Test Log record text file named "run.log"
	 * @throws IOException
	 * @throws Exception 
     */
	public void logClose() throws IOException {
		long finish = System.currentTimeMillis();
		String time = helper.getCurrentDateTimeFull();
		
	 // SCANNING FAILURE COUNTER RECORD:
		int failed = 0;
		if (helper.fileExist("failed.num", false)) { failed = Integer.valueOf(helper.fileScanner("failed.num")); }
		
	 // SCANNING TEST COUNTER RECORD:
		int n = 1;
		if (helper.fileExist("test.num", false)) { 
			if (! helper.fileScanner("test.num").equals(null)) { n = Integer.valueOf(helper.fileScanner("test.num")); }
			}
		String startingTime = helper.fileScanner("ini.time");
		long start = helper.convertStringToLong(startingTime);
				
	 // SCANNING INITIALIZATION RECORD:
		if (n > 1) {
		    helper.fileWriterPrinter("TOTAL TESTS: " + Integer.valueOf(helper.fileScanner("test.num")));
			helper.fileWriterPrinter("     FAILED: " + failed);
			helper.fileWriterPrinter("TEST  START: " + helper.convertCalendarMillisecondsAsLongToDateTimeHourMinSec(start));
			helper.fileWriterPrinter("TEST FINISH: " + time);
			helper.fileWriterPrinter("TOTAL  TIME: " + helper.convertTimeMillisecondsAsLongToDuration(finish - start));
			helper.fileWriterPrinter();
		}
		
	 // FINAL LOG RECORD:
	    if (helper.fileExist("run.log", false)) {
	    	
	    	if (n > 1) {
	    	helper.fileWriter("run.log", "");		    	
            helper.fileWriter("run.log", "TOTAL TESTS: " + Integer.valueOf(helper.fileScanner("test.num")));
	    	}
                        
            if (n == 1) { helper.fileWriter("run.log", ""); }
            if (n >= 1) { helper.fileWriter("run.log", "     FAILED: " + failed); }
	    		    	
	        if (n > 1) {	
		    helper.fileWriter("run.log", "TEST  START: " + helper.convertCalendarMillisecondsAsLongToDateTimeHourMinSec(start));
		    helper.fileWriter("run.log", "TEST FINISH: " + time);
		    helper.fileWriter("run.log", "TOTAL  TIME: " + helper.convertTimeMillisecondsAsLongToDuration(finish - start));
		    helper.fileWriter("run.log", "");
		    }
	    }

	 // FINAL LOG EMAIL:
	    if (helper.fileExist("email.cont", false)) { helper.fileCleaner("email.cont"); }
	    	helper.fileWriter("email.cont", "         FAILED: " + failed);
    	    helper.fileWriter("email.cont", "    TEST  START: " + helper.convertCalendarMillisecondsAsLongToDateTimeHourMinSec(start));
    	    helper.fileWriter("email.cont", "    TEST FINISH: " + time);
    	    helper.fileWriter("email.cont", "    TOTAL  TIME: " + helper.convertTimeMillisecondsAsLongToDuration(finish - start));
    	    helper.fileWriter("email.cont", "");
        
     // CLEAN-UP UNNECESSARY FILE(S):
		helper.fileCleaner("finish.time");
		helper.fileCleaner("ini.time"   );
		helper.fileCleaner("start.time" );
		helper.fileCleaner("stack.trace");				
		helper.fileCleaner("xml.path"   );
		
	 // STORE LAST TEST NUMBER:
		helper.fileCopy("test.num", "last.num");
//		helper.fileCleaner("test.num");
	}	

/** 
 * Test Report management;
 * @throws Exception 
 */
//@AfterSuite /** (groups = {"FINISH",}) */
public void finish() throws Exception {
	// CLOSE TEST LOG RECORD:
	logClose();
	
	// E-MAIL SUBJECT 
	if (helper.fileExist("email.subj", false)) { helper.fileCleaner("email.subj"); }
	String result = "ALL PASSED!";
	if ( Integer.valueOf(lastTestNum()) == 1 ) { result = "PASSED!"; }
	
	if (helper.fileExist("failed.log", false)) { 
		if (helper.fileExist("failed.num", false)) { 
			if (Integer.valueOf(helper.fileScanner("failed.num")) > 0) {
				result = "FAILED: " + helper.fileScanner("failed.num");
				if ( Integer.valueOf(lastTestNum()) == Integer.valueOf(helper.fileScanner("failed.num")) ) { result = "ALL FAILED..."; }
				if ( Integer.valueOf(lastTestNum()) == 1 ) { result = "FAILED..."; }
				}
			}
		}
	
	String subject = "TVOKids.com ---> Automated " + helper.fileScanner("test.type") + 
			         " Result ---> Total tests run: " + lastTestNum() + 
			         " ---> " + result + "  [ " + helper.getCurrentDateYearMonthDay() + "   " + helper.getCurrentTimeHourMin() + " ]";
	helper.fileWriter("email.subj", subject);
	
    // E-MAIL CONTENT:
    if (helper.fileExist("email.cont", false)) {
	    String email = helper.fileScanner("email.cont");
	    helper.fileCleaner("email.cont");
	
	// E-MAIL CONTENT HEADER:
	/*
	for (int i = 0; i < Email.header.length; i++) { helper.fileWriter("email.cont", "* " + Email.header[i]); }
	for (int i = 0; i < 2; i++) { helper.fileWriter("email.cont", ""); }
	*/
	
	// E-MAIL CONTENT BODY:
	helper.fileWriter("email.cont", "Hi,");
	helper.fileWriter("email.cont", "");
	helper.fileWriter("email.cont", "FYI:"); 
	helper.fileWriter("email.cont", "TVOKids.com - AUTOMATED " + helper.fileScanner("test.type").toUpperCase() + " RESULT");
	helper.fileWriter("email.cont", "");
	helper.fileWriter("email.cont", "     APP  SERVER: " + helper.fileScanner("server.info"));
	helper.fileWriter("email.cont", "     WEB BROWSER: " + System.getProperty("Browser"));
	helper.fileWriter("email.cont", "     GiT  BRANCH: " + helper.fileScanner("branch.info"));	
	helper.fileWriter("email.cont", "");

	// E-MAIL CONTENT TOTAL TESTS NUMBER:
	helper.fileWriter("email.cont", "    TOTAL TESTS: " + lastTestNum());
    
    // E-MAIL CONTENT RESULTS
    helper.fileWriter("email.cont", email);
    
    // E-MAIL CONTENT NEW TESTS ADDED NUMBER, IF ANY:
    if ( (Integer.valueOf(helper.fileScanner("last.num")) > 1) && (Integer.valueOf(helper.fileScanner("prev.num")) > 0) ) {
    if ( helper.isInteger(addTestNum()) && Boolean.valueOf(helper.fileScanner("add.show")) ) {
    	if ( Integer.valueOf(addTestNum()) > 0 ) {
    		helper.fileWriterPrinter(" TOTAL PREVIOUS: " + prevTestNum());
    		helper.fileWriterPrinter("NEW TESTS ADDED: " + addTestNum());
    		
    		helper.fileWriter("run.log", " TOTAL PREVIOUS: " + prevTestNum());
    		helper.fileWriter("run.log", "NEW TESTS ADDED: " + addTestNum());
    		
    		if (helper.fileExist("email.cont", false)) {
    			helper.fileWriter("email.cont", " TOTAL PREVIOUS: " + prevTestNum());
    			helper.fileWriter("email.cont", "NEW TESTS ADDED: " + addTestNum());
    		    helper.fileWriter("email.cont", "");
    			}
    		}
    	}
    }
    
    // E-MAIL CONTENT FINAL RESULT STATEMENT:
    helper.fileWriter("email.cont", "   FINAL RESULT: " + result);
    
    // E-MAIL CONTENT ATTACHMENT INFO:
    helper.fileWriter("email.cont", "");
    helper.fileWriter("email.cont", "       ATTACHED: reference log(s)");

    // E-MAIL CONTENT FAILURE INFO, IF ANY:
    if (helper.fileExist("failed.log", false)) {
    	for (int i = 0; i < 3; i++) { helper.fileWriter("email.cont", ""); }
		helper.fileWriter("email.cont", "    LIST OF FAILED TESTS:");
	    helper.fileWriter("email.cont", "");
		String failed = helper.fileScanner("failed.log");
		helper.fileWriter("email.cont", failed);
		}
    
    // E-MAIL CONTENT FOOTER, OTHER INFO, ETC. (OPTIONAL):
    /*
    helper.fileWriter("email.cont", "");
    */	    
    for (int i = 0; i < 8; i++) { helper.fileWriter("email.cont", ""); }
    for (int i = 0; i < Email.footer.length; i++) { helper.fileWriter("email.cont", "* " + Email.footer[i]); }
    }
		
// E-MAIL SENDING
if( helper.fileExist("email.opt", false) && Boolean.valueOf(helper.fileScanner("email.opt")) ) {
	// ZIP SCREEN-SHOTS:
	if (helper.fileExist("failed.log", false)) {
		helper.zipDirectory(Common.outputFileDir, Common.testOutputFileDir + "screen-shots.zip", false, true, 5);
		}
	while( !helper.fileExist("emailable-report.html", false) &&
		   !helper.fileExist("extent-test-report.html", false) &&
		   !(helper.fileExist("screen-shots.zip", false) || helper.fileExist("screen-shots.renameToZip", false))
		   ) { Thread.sleep(1000); }
	// SENDING:
	sendEmail(
		Email.autoTesterUsername,
		Email.autoTesterPassword,
		Email.gmailHost,
		Email.gmailPort,
		Email.gmailStarttls,
		Email.gmailAuth,
		Email.gmailDebug,
		Email.gmailSocketFactoryClass,
		Email.gmailFallback,
		Email.to(),
		Email.cc(),
		Email.bcc(),
		subject,
		helper.fileScanner("email.cont"),
		Email.attachmentFullPaths(),
		Email.attachmentFileNames()
        );  
	}

}

/**
 * Test Number dialoge 
 * @throws IOException 
 * @throws NumberFormatException
 */
public int testNum() throws NumberFormatException, IOException{
	 String Default = "0";
	 String number = null;
	 if(!helper.fileExist("prev.num", false)){ number = helper.fileScanner("prev.num"); }
	 if(number == null){ number = Default; }		 
	 return Integer.valueOf(number);
	 }

/**
 * Test Type dialoge 
 * @throws IOException 
 * @throws NumberFormatException */
public String testType() throws NumberFormatException, IOException {
	      if(!helper.fileExist("prev.num", false)){
	       // helper.fileWriter("prev.num", 0);
	    	  helper.fileWriter("prev.num", testNum());
	    	  }
	      String s = null;		      
	      	        	  
    	  if ( helper.fileExist("prev.num", false)){   		   
    		  if ( Integer.valueOf(helper.fileScanner("prev.num"))  >  250 ) { s = "Regression Test (Part 2 of 2)"; }	    		   
    		  if ( Integer.valueOf(helper.fileScanner("prev.num"))  >  650 ) { s = "Regression Test (Part 1 of 2)"; }	    		   
    		  if ( Integer.valueOf(helper.fileScanner("prev.num"))  > 1000 ) { s = "Regression Test";          }		   
    		  if ( Integer.valueOf(helper.fileScanner("prev.num")) <=  100 ) { s = "Test Failures Re-Run";     }
    		  }
    	  
  		   if ( helper.fileExist("test.type", false) ) {
  			    if ( helper.fileScanner("test.type").equals("Regression Test (Part 1 of 2)")      ) { s = "Test Failures Re-Run (Part 1 of 2)"; }	  
  			    if ( helper.fileScanner("test.type").equals("Regression Test (Part 2 of 2)")      ) { s = "Test Failures Re-Run (Part 2 of 2)"; }
  			    if ( helper.fileScanner("test.type").equals("Test Failures Re-Run (Part 1 of 2)") ) { s = "Test Failures Re-Run (Part 1 of 2)"; }	  
  			    if ( helper.fileScanner("test.type").equals("Test Failures Re-Run (Part 2 of 2)") ) { s = "Test Failures Re-Run (Part 2 of 2)"; }
  			    if ( helper.fileScanner("test.type").equals("Regression Test")      )               { s = "Test Failures Re-Run";     }	
  			    if ( helper.fileScanner("test.type").equals("Test Failures Re-Run") )               { s = "Test Failures Re-Run";     }
  			    helper.fileCleaner("test.type");
				helper.fileWriter("test.type", s);
				} 
	      
	      System.out.println("You selected to run " + s.toUpperCase() + "\n");
	      return s;
	}

/** Date and Time entry dialogue */
public String dateBox(){ return helper.getCurrentDateTimeHourMinSec(); }

/**
 * 'dev' Server log recorder
 * @throws IOException 
 * @throws NumberFormatException 
 */
public String devServer() throws NumberFormatException, IOException {
	 // DETECTING DEFAULT SERVER:
	 String server = Common.homeURL.substring(Common.homeURL.indexOf(":") + 3, Common.homeURL.length());
	 if(helper.fileExist("server.info", false)) { helper.fileCleaner("server.info"); }
	 helper.fileWriter("server.info", server);
	 return server;
}

/**
 * GiT Branch log recorder
 * @throws IOException 
 * @throws NumberFormatException 
 */
public String gitBranch() throws NumberFormatException, IOException {
	 // DETECTING DEFAULT SERVER:
	 String branch = "develop";
	 if(helper.fileExist("branch.info", false)) { branch = helper.fileScanner("branch.info"); }
	 return branch;
}

/** Test Delay entry dialogue */
public static int minBox(){	return 0; }

/** Test E-mail Notification Option dialogue with "Yes" checkbox */
public boolean emailOption(){
	 Icon icon = new ImageIcon(Common.testIconFileDir + "envelope.open.letter.png");
	 JCheckBox checkbox = new JCheckBox("Yes!");
	 String message = "Do you want to send automated E-Mail notification about Test Results?";
	 Object[] params = { message, checkbox };
	 JOptionPane.showConfirmDialog(null, params, "E-Mail Notification Option", JOptionPane.CLOSED_OPTION, 0, icon);
	 boolean send = checkbox.isSelected();
	 return send;
}

/** Test E-mail Notification Option dialogue with "Yes" and "No" smart-checkbox */
public boolean emailOptionDouble(){ return true; }

/** Test E-mail Addresses Option dialogue with "All" and "Tester" smart-checkbox */
public boolean emailAddresses(){ return true; }

/**
 * Reads the previous (last) Test total test number and records it as a previous and erases the last total 
 * @throws IOException 
 * @throws NumberFormatException 
 */
public void lastToPrev() throws NumberFormatException, IOException{	
	 if (helper.fileExist("test.num", false)) {
		 helper.fileCopy("test.num", "last.num");
		 helper.fileCleaner("test.num");
		 }
	 
	 if (helper.fileExist("prev.num", false)) { helper.fileCleaner("prev.num"); }
	 
     if (helper.fileExist("last.num", false)) {
   	     helper.fileCopy("last.num", "prev.num");
         helper.fileCleaner("last.num");
     }    
}

/**
 * Reads the previous Test total test number
 * @throws IOException 
 * @throws NumberFormatException 
 */
public String prevTestNum() throws NumberFormatException, IOException{
	 String prev = "N/A";
	 if (helper.fileExist("prev.num", false)) { prev = helper.fileScanner("prev.num"); }
	 return prev; 
}

/**
 * Reads the last (just finished) Test final test number
 * @throws IOException 
 * @throws NumberFormatException
 */
public String lastTestNum() throws NumberFormatException, IOException{
	 String last = "N/A";
    if (helper.fileExist("last.num", false)) { last = helper.fileScanner("last.num"); }
	 return last; 
}

/**
 * Calculates the number of tests difference between last (just finished) and previous Tests   
 * @throws IOException 
 * @throws NumberFormatException
 */
public String addTestNum() throws NumberFormatException, IOException{
	 String added = "N/A";
	 if (helper.fileExist("add.num", false)) { helper.fileCleaner("add.num"); }
	 if ( helper.fileExist("prev.num", false) && helper.fileExist("last.num", false) ) {
		 String last = helper.fileScanner("last.num");
		 String prev  = helper.fileScanner("prev.num");
		 if ( helper.isInteger(last) && helper.isInteger(prev) ) {
			 added = String.valueOf((Integer.valueOf(last) - Integer.valueOf(prev)));
			 helper.fileWriter("add.num", added);
			 }
    }
	 return added; 
}

/** Test E-mail Notification Option dialogue with "Yes" checkbox */
public boolean addTestOption(){ return false; }

/** Sends e-mail with single-file attachment */
public boolean sendEmail(
			String userName,
			String passWord,
			String host,
			String port,
			String starttls,
			String auth,
			boolean debug,
			String socketFactoryClass,
			String fallback,
			String[] to,
			String[] cc,
			String[] bcc,
			String subject,
			String text,
			String attachmentFullPath,
			String attachmentName
			) throws AddressException, MessagingException{

		    // OBJECT INSTANTIATION OF A PROPERTIES FILE (SETS SMTP SERVER PROPERTIES):
		    Properties properties = new Properties();
		    properties.put("mail.smtp.user", userName);
		    properties.put("mail.smtp.host", host);
		    if(!"".equals(port)){ properties.put("mail.smtp.port", port); }
		    if(!"".equals(starttls)){
		        properties.put("mail.smtp.starttls.enable",starttls);
		        properties.put("mail.smtp.auth", auth);
		        }
		    if(debug){ properties.put("mail.smtp.debug", "true"); }
		    else     { properties.put("mail.smtp.debug", "false"); }
		    if(!"".equals(port)){ properties.put("mail.smtp.socketFactory.port", port); }
		    if(!"".equals(socketFactoryClass)){ properties.put("mail.smtp.socketFactory.class",socketFactoryClass); }
		    if(!"".equals(fallback)){ properties.put("mail.smtp.socketFactory.fallback", fallback); }

		try{
		    // CREATES A NEW SESSION:
		    Session session = Session.getDefaultInstance(properties, null);
		    session.setDebug(debug);

		    // CREATES A NEW E-MAIL MESSAGE PART:
		    MimeMessage msg = new MimeMessage(session);
		    msg.setSubject(subject);
		 // msg.setSentDate(new Date());


		    // CREATES THE MESSAGE PART:
		    BodyPart messageBodyPart = new MimeBodyPart();

		    // FILL THE MESSAGE:
		    messageBodyPart.setText(text);

		    // CREATE A MULTI-PART MESSAGE:
		    Multipart multipart = new MimeMultipart();

		    // SETS TEXT MESSAGE PART:
		    multipart.addBodyPart(messageBodyPart);

		    /** For attaching a single document */
		    if(!"".equals(attachmentFullPath)){ 
		        // ADDING ATTACHMENT:
		        messageBodyPart = new MimeBodyPart();
			    DataSource source = new FileDataSource(attachmentFullPath);
			    messageBodyPart.setDataHandler(new DataHandler(source));
			 // messageBodyPart.setDisposition(Part.ATTACHMENT);
			    messageBodyPart.setFileName(attachmentName);
			    multipart.addBodyPart(messageBodyPart);
			    }
		    
		    // SETS THE COMPLETE MULTI-PART MESSAGE AS E-MAIL:
		    msg.setContent(multipart);

		    msg.setFrom(new InternetAddress(userName));

		    for(int i=0;i<to.length;i++){ msg.addRecipient(Message.RecipientType.TO, new InternetAddress(to[i])); }
		    for(int i=0;i<cc.length;i++){ msg.addRecipient(Message.RecipientType.CC, new InternetAddress(cc[i])); }
		    for(int i=0;i<bcc.length;i++){ msg.addRecipient(Message.RecipientType.BCC, new InternetAddress(bcc[i])); }

		    msg.saveChanges();

		    Transport transport = session.getTransport("smtp");
		    transport.connect(host, userName, passWord);
		    transport.sendMessage(msg, msg.getAllRecipients());
		    transport.close();

		    System.out.println("\nE-Mail sent sucessfully!");
		    return true;
		    } catch (Exception mex){ System.out.println("\nE-Mail sending failed...\nException:\n" + mex); return false; }

		}

/** Sends e-mail with multi-file attachment */
public boolean sendEmail(
			String userName,
			String passWord,
			String host,
			String port,
			String starttls,
			String auth,
			boolean debug,
			String socketFactoryClass,
			String fallback,
			String[] to,
			String[] cc,
			String[] bcc,
			String subject,
			String text,
			String[] attachmentFullPath,
			String[] attachmentName
			) throws AddressException, MessagingException{

		    // OBJECT INSTANTIATION OF A PROPERTIES FILE (SETS SMTP SERVER PROPERTIES):
		    Properties properties = new Properties();
		    properties.put("mail.smtp.user", userName);
		    properties.put("mail.smtp.host", host);
		    if(!"".equals(port)){ properties.put("mail.smtp.port", port); }
		    if(!"".equals(starttls)){
		        properties.put("mail.smtp.starttls.enable",starttls);
		        properties.put("mail.smtp.auth", auth);
		        }
		    if(debug){ properties.put("mail.smtp.debug", "true"); }
		    else     { properties.put("mail.smtp.debug", "false"); }
		    if(!"".equals(port)){ properties.put("mail.smtp.socketFactory.port", port); }
		    if(!"".equals(socketFactoryClass)){ properties.put("mail.smtp.socketFactory.class",socketFactoryClass); }
		    if(!"".equals(fallback)){ properties.put("mail.smtp.socketFactory.fallback", fallback); }

		try{
		    // CREATES A NEW SESSION:
		    Session session = Session.getDefaultInstance(properties, null);
		    session.setDebug(debug);

		    // CREATES A NEW E-MAIL MESSAGE PART:
		    MimeMessage msg = new MimeMessage(session);
		    msg.setSubject(subject);
		 // msg.setSentDate(new Date());


		    // CREATES THE MESSAGE PART:
		    BodyPart messageBodyPart = new MimeBodyPart();

		    // FILL THE MESSAGE:
		    messageBodyPart.setText(text);

		    // CREATE A MULTI-PART MESSAGE:
		    Multipart multipart = new MimeMultipart();

		    // SETS TEXT MESSAGE PART:
		    multipart.addBodyPart(messageBodyPart);

		    /** For attaching a multi documents */
		    for (int i = 0; i < attachmentName.length; i++) {
		    	if (!"".equals(attachmentFullPath[i])) {
		    		// ADDING INDIVIDUAL ATTACHMENT:
		            messageBodyPart = new MimeBodyPart();
			        DataSource source = new FileDataSource(attachmentFullPath[i]);
			        messageBodyPart.setDataHandler(new DataHandler(source));
			     // messageBodyPart.setDisposition(Part.ATTACHMENT);
			        messageBodyPart.setFileName(attachmentName[i]);
			        multipart.addBodyPart(messageBodyPart);
			        }
		    	}
		    
		    // SETS THE COMPLETE MULTI-PART MESSAGE AS E-MAIL:
		    msg.setContent(multipart);

		    msg.setFrom(new InternetAddress(userName));

		    for(int i=0;i<to.length;i++){ msg.addRecipient(Message.RecipientType.TO, new InternetAddress(to[i])); }
		    for(int i=0;i<cc.length;i++){ msg.addRecipient(Message.RecipientType.CC, new InternetAddress(cc[i])); }
		    for(int i=0;i<bcc.length;i++){ msg.addRecipient(Message.RecipientType.BCC, new InternetAddress(bcc[i])); }

		    msg.saveChanges();

		    Transport transport = session.getTransport("smtp");
		    transport.connect(host, userName, passWord);
		    transport.sendMessage(msg, msg.getAllRecipients());
		    transport.close();

		    System.out.println("\nE-Mail sent sucessfully!");
		    return true;
		    } catch (Exception mex){ System.out.println("\nE-Mail sending failed...\nException:\n" + mex); return false; }

		}

}
