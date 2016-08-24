package com.tvokids.send;

import java.awt.Component;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
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
import javax.swing.UIManager;

import org.testng.annotations.*;

import com.tvokids.locator.Common;
import com.tvokids.locator.Email;
import com.tvokids.test.helper.UtilitiesTestHelper;

@SuppressWarnings("static-access")
public class Mail {
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
		} else { System.out.println("Won't show the difference between quantity of Tests executed during the last and previous runs...\n");                    }

		// TEST DATE AND TIME MANAGEMENT:
		String current = helper.getCurrentDateTimeHourMinSec();
		System.out.println(current + "\n");		
		StringSelection stringSelection = new StringSelection(current);
		Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
		clpbrd.setContents(stringSelection, null);
		long currTime = helper.convertCalendarDateTimeHourMinSecToMillisecondsAsLong(current);
		long startTime = helper.convertCalendarDateTimeHourMinSecToMillisecondsAsLong(dateBox());
		String start = helper.convertCalendarMillisecondsAsLongToDateTimeHourMinSec(startTime);		
		long updateDelay = (startTime - currTime);
		int sec = (int) updateDelay/1000;
		
		// TEST HOST APPLICATION SERVER MANAGEMENT:
	 // int server = devBox();
		String server = devServer();
		System.out.println("Update will be started in: " + helper.convertTimeSecondsToHoursMinSeconds(sec));
		System.out.println("Update will be started at: " + helper.convertCalendarMillisecondsAsLongToDateTimeHourMinSec(currTime + updateDelay));
		System.out.println("Update will be started on: " + server);
		
		// GIT BRANCH MANAGEMENT
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
//		if (testDelay != 0 ) {
//		System.out.println("  Test will be started in: " + helper.convertTimeSecondsToHoursMinSeconds(sec + testDelay*60));
//		System.out.println("  Test will be started at: " + helper.convertCalendarMillisecondsAsLongToDateTimeHourMinSec(currTime + updateDelay + testDelay*60*1000));
//		System.out.println("\nwait please...\n");
//		}
		if ( (testDelay >= 0 ) && (currTime + updateDelay + testDelay*60*1000 > System.currentTimeMillis()) ){    // USED TO BE: if (testDelay != 0 ) {
		System.out.println("  Test additional delay is: " + helper.convertTimeSecondsToHoursMinSeconds(testDelay*60));
		System.out.println("  Test will be  started at: " + helper.convertCalendarMillisecondsAsLongToDateTimeHourMinSec(currTime + updateDelay + testDelay*60*1000));
		System.out.println("\nwait please...\n");
		} else { System.out.println("starting now...\n"); }
		
		String date = start.substring(0,start.indexOf(" "));
		String time = start.substring(start.indexOf(" ") + 1, start.length());
		
		Boolean ifRefresh = false;
		String refresh = "";
        if(ifRefresh) { refresh = "refresh-dev "; }

		String command = 
				"clear;CURRENT=$(date +%s);echo;echo $(date +%Y-%m-%d)\" \"$(date +%H\":\"%M\":\"%S);echo;TESTSTARTDATE=\"" +
                date +
                "\";TESTSTARTTIME=\"" +
                time +
                "\";TESTSTART=$(date --date=\"$TESTSTARTDATE $TESTSTARTTIME\" +%s);DIFF=$(( $TESTSTART - $CURRENT ));HOURS=$(( $DIFF/3600 ));MINUTES=$(( $(( $DIFF%3600 ))/60 ));SECONDS=$(( $DIFF%60 ));SLEEPTIME=$(( $HOURS*3600 + $MINUTES*60 + $SECONDS ));echo;echo \"Sleep time seconds: \"$SLEEPTIME\" seconds\";echo;echo \"Update Start is expected at:\";date \"+%Y-%m-%d\" -d @$(( $(date +%s) + $SLEEPTIME));date \"+%T\" -d @$(( $(date +%s) + $SLEEPTIME));echo;echo;sleep $SLEEPTIME;echo;echo;echo;cd /data/WebSites/" +
                server +
                "/website;git pull;git checkout " + branch + ";git pull origin " + branch + ";sh sites/all/scripts/d7-rebuild.sh " + refresh + branch + ";echo;echo;FINISH=$(date +%s);DIFF=$(( $FINISH - $TESTSTART ));HOURS=$(( $DIFF/3600 ));MINUTES=$(( $(( $DIFF%3600 ))/60 ));SECONDS=$(( $DIFF%60 ));echo;echo;echo $(date +%Y-%m-%d)\" \"$(date +%H\":\"%M\":\"%S);echo \"Update duration: \"$HOURS\" hours \"$MINUTES\" minutes \"$SECONDS\" seconds\";echo;echo;"
                ;
	 // System.out.println("\n" + date + "\n" + time + "\n");	
	 // System.out.println(command);		
		stringSelection = new StringSelection(command);
		clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
		clpbrd.setContents(stringSelection, null);
		
		long sleep = (helper.convertCalendarDateTimeHourMinSecToMillisecondsAsLong(helper.convertCalendarMillisecondsAsLongToDateTimeHourMinSec(currTime + updateDelay + testDelay*60*1000)) -
				      helper.convertCalendarDateTimeHourMinSecToMillisecondsAsLong(helper.getCurrentDateTimeHourMinSec()));
				
		if (testDelay >= 0 ) { Thread.sleep(sleep); }   // USED TO BE: if (testDelay != 0 ) { Thread.sleep(sleep); }
		
		System.out.println(helper.getCurrentDateTimeHourMinSec());
		System.out.println("Starting...\n");
		
		// CREATE NEW TEST LOG RECORD:
		logOpen();
		}
	
	/**
	 * Creates a new Test Log record as a text file named "run.log"
	 * create file example:
	 * File f = new File(<full path string>); f.createNewFile();
	 * @throws IOException
	 */
	public void logOpen() throws IOException {		
		String time = helper.getCurrentDateTimeFull(); // System.out.print(" TEST START: " + time + "\n");
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
    		helper.fileCleaner("start.time");
    		helper.fileCleaner("stack.trace");
    		helper.fileCleaner("xml.path");
		
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
			
			// E-MAIL SUBJECT: 
			if (helper.fileExist("email.subj", false)) { helper.fileCleaner("email.subj"); }
			String result = "ALL PASSED!";
			if ( Integer.valueOf(lastTestNum()) == 1 ) { result = "PASSED!"; }
			if (helper.fileExist("failed.log", false)) { 
				if (helper.fileExist("failed.num", false)) { 
					result = "FAILED: " + helper.fileScanner("failed.num");
					if ( Integer.valueOf(lastTestNum()) == Integer.valueOf(helper.fileScanner("failed.num")) ) { result = "ALL FAILED..."; }
					if ( Integer.valueOf(lastTestNum()) == 1 ) { result = "FAILED..."; }
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
			for (int i = 0; i < EmailLocators.header.length; i++) { helper.fileWriter("email.cont", "* " + EmailLocators.header[i]); }
			for (int i = 0; i < 2; i++) { helper.fileWriter("email.cont", ""); }
			*/
			
			// E-MAIL CONTENT BODY:
			helper.fileWriter("email.cont", "Hi,");
			helper.fileWriter("email.cont", "");
			helper.fileWriter("email.cont", "FYI:"); 
			helper.fileWriter("email.cont", "TVOKids.com - AUTOMATED " + helper.fileScanner("test.type").toUpperCase() + " RESULT");
			helper.fileWriter("email.cont", "");
			helper.fileWriter("email.cont", "     APP SERVER: " + helper.fileScanner("server.info"));
			helper.fileWriter("email.cont", "     GiT BRANCH: " + helper.fileScanner("branch.info"));
			helper.fileWriter("email.cont", "");

			// E-MAIL CONTENT TOTAL TESTS NUMBER:
			helper.fileWriter("email.cont", "    TOTAL TESTS: " + lastTestNum());
		    
		    // E-MAIL CONTENT RESULTS:
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
		 
		// RECORD HIGHEST TEST NUMBER:
		if(helper.fileExist("test.num", false)) {			
		   if(helper.fileExist("test.max", false)){ 
			   if( Integer.valueOf(helper.fileScanner("test.max")) < Integer.valueOf(helper.fileScanner("test.num")) ) {
				   helper.fileCleaner("test.max");
				   helper.fileWriter("test.max", helper.fileScanner("test.num"));
				   }
			    } else {
			       helper.fileWriter("test.max", helper.fileScanner("test.num"));
			       }		   
		    } else {		    	
		    	if(helper.fileExist("prev.num", false)) {
				   if(helper.fileExist("test.max", false)){ 
					   if( Integer.valueOf(helper.fileScanner("test.max")) < Integer.valueOf(helper.fileScanner("prev.num")) ) {
						   helper.fileCleaner("test.max");
						   helper.fileWriter("test.max", helper.fileScanner("prev.num"));
						   }
					    } else {
					       helper.fileWriter("test.max", helper.fileScanner("prev.num"));					       
					    }
				   }
		    }
		}		
		
		/** Test Number dialoge */
		public int testNum(){
			 ImageIcon icon = new ImageIcon(Common.testIconFileDir + "tests.number.1-9.png");
			 String Default = "0";
			 String number = null;
			 boolean isTrue = false;
			 while(isTrue == false) {
				 number = (String) JOptionPane.showInputDialog(
						 null, 
						 "Enter desired Number of Test \n(integer positive value)\n\nor click \"CANCEL\" for " + Default + "\n ",
						 "Test Number",
					     1,
					     icon, null, 1
						 );	
				 
//				 final icon = new ImageIcon("home/user/Pictures/default.jpg"));
//				 showMessageDialog(dialog, "Blah blah blah", "About", JOptionPane.INFORMATION_MESSAGE, icon);
				 
				 if(number == null){ number = Default; }
				 isTrue = helper.isInteger(number);
				 if (isTrue == true) { isTrue = (Integer.valueOf(number) >= 0); }
				 }
			 return Integer.valueOf(number);
			 }
			
		/** Test Type dialoge 
		 * @throws IOException 
		 * @throws NumberFormatException
		 */
		public String testType() throws NumberFormatException, IOException {		  
			  Component frame = null;
		      Icon icon = UIManager.getIcon("OptionPane.questionIcon");
			  icon = new ImageIcon(Common.testIconFileDir + "question.round.png");
			      Object[] possibilities = { "As Previous",
	                                         "Regression Test", 
	                                         "Test Failures Re-Run",
	                                         "Test Execution",
	                                         "New Test Execution"
	                                        };
			      String number = "";
			      if(!helper.fileExist("prev.num", false)){
			       // helper.fileWriter("prev.num", 0);
			    	  helper.fileWriter("prev.num", testNum());
			    	  }
			      if(helper.fileExist("prev.num", false)){
			    	  int num = Integer.valueOf(helper.fileScanner("prev.num"));
			    	  if (num == 0) {number = "( number of tests executed during last run is missing... )";}
			    	  if (num == 1) {number = "( " + helper.fileScanner("prev.num") + " test executed during last run )";}
			    	  if (num > 1)  {number = "( " + helper.fileScanner("prev.num") + " tests executed during last run )";}
			    	  }
			      String s = null;		      
			      while((s == null) || (s.length() == 0)){
			      s = (String)JOptionPane.showInputDialog(frame,"Select Test Type:\n" + number,"Test Type Selection List",JOptionPane.PLAIN_MESSAGE,icon,possibilities,"");
		          if(s.equals("As Previous")){	        	  
		        	  if( helper.fileExist("prev.num", false) && helper.fileExist("test.max", false) ) {
		        		  if( Integer.valueOf(helper.fileScanner("prev.num")) == Integer.valueOf(helper.fileScanner("test.max")) ) {
		        			  s = "Regression Test";
		        		  } else {
		        			  if(helper.fileExist("test.type", false)) { s = helper.fileScanner("test.type"); } }
		        		  }
		        	  }	          
			      }	
			      System.out.println("You selected to run " + s.toUpperCase() + "\n");
			      return s;
			}
		
		 /** Date and Time entry dialogue */
		 public String dateBox(){
			 ImageIcon icon = new ImageIcon(Common.testIconFileDir + "shedule.date.time.png");
			 String str = null;
			 boolean isDate = false;
			 while(isDate == false) {
				 
				 str = (String) JOptionPane.showInputDialog(
						 null, 
						 "Enter Date & Time \n(like \"" + helper.getCurrentDateTimeHourMinSec() + "\")\n\nor paste,\nor click \"CANCEL\" for default current date and time\n ",
						 "Date & Time",
						 1,
						 icon, null, helper.getCurrentDateTimeHourMinSec()
						 );
				 
				 if(str != null){
					 // JOptionPane.showMessageDialog(null, "You entered: " + str, "Date & Time", 1);
					 // System.out.println("\nYou entered: " + str + "\n");
					 }
				 else {
					    str = helper.getCurrentDateTimeHourMinSec();
					 // JOptionPane.showMessageDialog(null, "You pressed cancel button...", "Date & Time", 1);
					 // System.out.println("\nYou pressed cancel button...\n");
					 }
				 String datePattern = "\\d{4}-\\d{1,2}-\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}";
				 isDate = str.matches(datePattern);
			  // System.out.println("Is the entry of \"" + str + "\" matching with this date Pattern: \"" + datePattern + "\"?\nAnswer: " + isDate + "\n");
				 }     
			 return str;				 
			 }
		 
//		 /**
//		  * 'dev' Box number reader
//		  * @throws IOException 
//		  * @throws NumberFormatException 
//		  */
//		 public int devBox() throws NumberFormatException, IOException {
//		 	 // DETECTING DEFAULT SERVER:
//		 	 String server, dev;
//		 	 server = Locators.homeURL;
//		 	 server = server.substring(server.indexOf(":") + 3, server.length());
//		 	 if(server.contains("dev")) { dev = server.substring(server.indexOf("dev") + 3, server.indexOf(".")); } else { dev = "0"; }
//		 	 return Integer.valueOf(dev);
//		 }

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
		  * GiT Branch entry dialogue 
		  * @throws IOException 
		  * @throws NumberFormatException
		  */
		 public String gitBranch() throws NumberFormatException, IOException{
		 	 ImageIcon icon = new ImageIcon(Common.testIconFileDir + "web.server.pc.png");
		 	 
		 	 // DETECTING DEFAULT SERVER:
		 	 String branch = null, Default = null;
		 	 if(helper.fileExist("branch.info", false)) { Default = helper.fileScanner("branch.info"); }
		 	 else { Default = "develop"; }
		 	 
		 		StringSelection stringSelection = new StringSelection(Default);
		 		Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
		 		clpbrd.setContents(stringSelection, null);
		 	 boolean isCorrect = false;
		 	 while(isCorrect == false) {
		 		 branch = (String) JOptionPane.showInputDialog(
		 				 null, 
		 				 "Enter GiT Branch \n(\"" + Default + "\")\n\nor paste,\nor click \"CANCEL\" for \"develop\" as a default\n ",
		 				 "\"" + Default + "\" GiT Branch",
		 				 1,
		 				 icon, null, Default
		 				 );
		 		 if(branch != null){
		 		       // JOptionPane.showMessageDialog(null, "You entered: " + branch, "\"develop\" Server", 1);
		 		       // System.out.println("\nYou entered: " + branch + "\n");
		 			 }
		 		 else {
		 			   branch = "develop";
		 		       // JOptionPane.showMessageDialog(null, "You pressed cancel button...", "\"branch\" Server", 1);
		 		       // System.out.println("\nYou pressed cancel button...\n");
		 			 }
		 		 isCorrect = ( !helper.isInteger(branch) );
		 	  // System.out.println("Is the entry of \"" + branch + "\" satisfying the acceptance criteria? \nAnswer: " + isCorrect + "\n");
		 		 } 
		 	 if(helper.fileExist("branch.info", false)) { helper.fileCleaner("branch.info"); }
		 	 helper.fileWriter("branch.info", branch);
		 	 return branch;
		 	 }
		 
		 /** Test Delay entry dialogue */
		 public int minBox(){		 
		  // Icon questionIcon = UIManager.getIcon("OptionPane.questionIcon");
			 Icon icon = new ImageIcon(Common.testIconFileDir + "timer.watch.when.png");
			 String min = null;
			 
//			 String message = "When do you want to run your test?";	
//			 boolean now = false;
//			 JCheckBox checkboxYes = new JCheckBox("Start immediatelly !");
//			 Object[] params = { message, checkboxYes };
//			 JOptionPane.showConfirmDialog( null, params, null, JOptionPane.CLOSED_OPTION, 0, questionIcon );		 
//			 now = checkboxYes.isSelected();

			 String message = "Do you want to run your test right now?";		 
			 Object[] params = {message};
		  // int n = JOptionPane.showConfirmDialog(null, params, "Notification Option", JOptionPane.INFORMATION_MESSAGE, 0, icon);
			 String[] buttons = { "Now !", "Later", "Cancel" };
			 int n = JOptionPane.showOptionDialog(null, params, "Notification Option", JOptionPane.INFORMATION_MESSAGE, 0, icon, buttons, buttons[1]);
			 boolean now = (n == 0);
			 
			 if (now) { min = "-1"; }
			 else {
				    String Default = "45";
					StringSelection stringSelection = new StringSelection(Default);
					Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
					clpbrd.setContents(stringSelection, null);
					
				    boolean isTrue = false;
			        while(isTrue == false) {
			        icon = new ImageIcon(Common.testIconFileDir + "timer.min.png");
				    min = (String) JOptionPane.showInputDialog(
						  null, 
						  "Enter Test Delay, minutes \n(integer positive value only)\n\nor paste,\nor click \"CANCEL\" for " + Default + " min as a default delay\n ",
						  "Test Delay",
						  1,
						  icon, null, Default
						  );
				    if (min == null) { min = Default; }
				    isTrue = helper.isInteger(min);
				    if (isTrue == true) { isTrue = (Integer.valueOf(min) >= 0); }
				    }
			        }
			 return Integer.valueOf(min);
			 }
		 
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
		 public boolean emailOptionDouble(){
			 Icon icon = new ImageIcon(Common.testIconFileDir + "envelope.open.email.png");
		     JCheckBox checkboxYes = new JCheckBox("Yes !");
		     JCheckBox checkboxNo = new JCheckBox("No !");
		     checkboxNo.setSelected(true);
		     String message = "Do you want to send automated E-Mail notification about Test Results?";
		     Object[] params = { message, checkboxYes, checkboxNo };
		     boolean send = false;
		     boolean dont = false;   
		     while(send == dont) {
		        JOptionPane.showConfirmDialog(null, params, "E-Mail Notification Option", JOptionPane.CLOSED_OPTION, 0, icon);
		        send = checkboxYes.isSelected();
		        dont = checkboxNo.isSelected();
		      }
			 return send; 
		 }
		 
		 /** Test E-mail Addresses Option dialogue with "All" and "Tester" smart-checkbox */
		 public boolean emailAddresses(){
			 Icon icon = new ImageIcon(Common.testIconFileDir + "envelope.front.address.png");
		     JCheckBox checkboxAll = new JCheckBox("Send E-Mail to All !");
		     JCheckBox checkboxTester = new JCheckBox("Send E-Mail to Tester Only !");
		     checkboxTester.setSelected(true);
		     String message = "Who do you want to send the automated E-Mail notification about Test Results?";
		     Object[] params = { message, checkboxAll, checkboxTester };
		     boolean all = false;
		     boolean tester = false;   
		     while(all == tester) {
		        JOptionPane.showConfirmDialog(null, params, "E-Mail Addresses Option", JOptionPane.CLOSED_OPTION, 0, icon);
		        all = checkboxAll.isSelected();
		        tester = checkboxTester.isSelected();
		      }
			 return all; 
		 }
		 
		/** Reads the previous (last) Test total test number and records it as a previous and erases the last total 
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
		 
		/** Reads the previous Test total test number
		  * @throws IOException 
		  * @throws NumberFormatException 
		  */
		 public String prevTestNum() throws NumberFormatException, IOException{
			 String prev = "N/A";
			 if (helper.fileExist("prev.num", false)) { prev = helper.fileScanner("prev.num"); }
			 return prev; 
		 }
		 
		/** Reads the last (just finished) Test final test number
		  * @throws IOException 
		  * @throws NumberFormatException
		  */
		 public String lastTestNum() throws NumberFormatException, IOException{
			 String last = "N/A";
		     if (helper.fileExist("last.num", false)) { last = helper.fileScanner("last.num"); }
			 return last; 
		 }
		 
		/** Calculates the number of tests difference between last (just finished) and previous Tests   
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
		 public boolean addTestOption(){
		  // Icon icon = UIManager.getIcon("OptionPane.questionIcon");
			 Icon icon = new ImageIcon(Common.testIconFileDir + "question.round.png");
			 JCheckBox checkbox = new JCheckBox("Yes!");
			 String message = "Do you want to show the number of difference between last and previous Tests?";
			 Object[] params = { message, checkbox };
			 JOptionPane.showConfirmDialog(null, params, "Show Tests Number change Option", JOptionPane.CLOSED_OPTION, 0, icon);
			 boolean show = checkbox.isSelected();
			 return show;
		 }
		 
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

				    // Sets text message part:
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