package com.tvokids.report;

import java.io.IOException;
import java.text.ParseException;

import org.testng.annotations.*;

import com.tvokids.utilities.*;

@SuppressWarnings("static-access")
public class Log {
	UtilitiesTestHelper helper = new UtilitiesTestHelper();
		
	/**
	 * Creates a new Test Log record as a text file named "run.log" create file
	 * example: File f = new File(<full path string>); f.createNewFile();
	 * @throws IOException
	 * @throws ParseException 
	 * @throws NumberFormatException 
	 */
	@BeforeSuite
	public void logOpen() throws IOException, NumberFormatException, ParseException {
		// INITIALIZATION:
		helper.beforeCleaner();	
		String time = helper.getCurrentDateTimeFull(); // System.out.print(" TEST START: " + time + "\n");
		helper.fileWriter("ini.time", helper.convertLongToString(System.currentTimeMillis()));
		// INITIAL TEST TYPE RECORD:
		helper.fileOverWriter("test.type", "Single Test");
		// INITIAL LOG RECORD:
		helper.fileWriter("run.log", " TEST START: " + time);
		helper.fileWriter("run.log", "");
	}

	/**
	 * Closes the Test Log record text file named "run.log"
	 * @throws IOException
	 * @throws Exception
	 */
	//@AfterSuite /** (groups = {"FINISH",}) */
	public void logClose() throws IOException {
		long finish = System.currentTimeMillis();
		String time = helper.getCurrentDateTimeFull();
		// SCANNING FAILURE COUNTER RECORD:
		int failed = 0;
		if (helper.fileExist("failed.num", false)) { failed = Integer.valueOf(helper.fileScanner("failed.num")); }
		// SCANNING TEST COUNTER RECORD:
		int n = 1;
		if (helper.fileExist("test.num", false)) {
			if (!helper.fileScanner("test.num").equals(null)) { n = Integer.valueOf(helper.fileScanner("test.num")); }
		}
		if (n > 1) {
			// SCANNING INITIALIZATION RECORD:
			String startingTime = helper.fileScanner("ini.time");
			long start = helper.convertStringToLong(startingTime);
			helper.fileWriterPrinter("TOTAL TESTS: " + Integer.valueOf(helper.fileScanner("test.num")));
			helper.fileWriterPrinter("     FAILED: " + failed);
			helper.fileWriterPrinter("TEST  START: " + helper.convertCalendarMillisecondsAsLongToDateTimeHourMinSec(start));
			helper.fileWriterPrinter("TEST FINISH: " + time);
			helper.fileWriterPrinter("TOTAL  TIME: " + helper.convertTimeMillisecondsAsLongToDuration(finish - start));
			helper.fileWriterPrinter();
			// FINAL LOG RECORD:
			if (helper.fileExist("run.log")) {
				helper.fileWriter("run.log", "");
				helper.fileWriter("run.log", "TOTAL TESTS: " + Integer.valueOf(helper.fileScanner("test.num")));
				helper.fileWriter("run.log", "     FAILED: " + failed);
				helper.fileWriter("run.log", "TEST  START: " + helper.convertCalendarMillisecondsAsLongToDateTimeHourMinSec(start));
				helper.fileWriter("run.log", "TEST FINISH: " + time);
				helper.fileWriter("run.log", "TOTAL  TIME: " + helper.convertTimeMillisecondsAsLongToDuration(finish - start));
				helper.fileWriter("run.log", "");
			}
		}
		
		// CREATING TEST-FAILED.XML:
		/*
		testFailedXML();
		*/
		
		// CLEAN-UP UNNECESSARY FILE(S):
		/*
		helper.fileCleaner("failed.num");
		helper.fileCleaner("finish.time");
		helper.fileCleaner("ini.time");
		helper.fileCleaner("start.time");
		helper.fileCleaner("stack.trace");
		helper.fileCleaner("test.num");
		helper.fileCleaner("xml.path");	
		*/	
	}
	
}
