package com.tvokids.catalog;

import java.io.IOException;

import com.tvokids.utilities.*;

/**
 * Converts "failed.log" into "test-failed.xml"
 */
public class FailedLogToXmlConverter {
	static UtilitiesTestHelper helper = new UtilitiesTestHelper();
	
	public static void main(String[] args) throws IOException {
		String reporterClass = "<class name=\"com.tvokids.email.All\"></class>";		
		helper.testLogToXmlCreator("test-failed", "test-failed", "failed.log", "test-failed.xml", reporterClass);
	}

}
