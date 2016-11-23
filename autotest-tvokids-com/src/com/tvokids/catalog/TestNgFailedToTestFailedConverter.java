package com.tvokids.catalog;

import java.io.IOException;

import com.tvokids.utilities.*;

/**
 * Converts "failed.log" into "test-failed.xml"
 */
public class TestNgFailedToTestFailedConverter {
	static UtilitiesTestHelper helper = new UtilitiesTestHelper();
	
	public static void main(String[] args) throws IOException {
	String reporterClass = "<class name=\"com.tvokids.email.All\"></class>";		
	helper.testNgFailedToTestFailedConverter("test-failed", "test-failed", "test-failed.xml", reporterClass);
	}

}
