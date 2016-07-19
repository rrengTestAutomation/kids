package com.tvokids.catalog;

import java.io.IOException;
import com.tvokids.test.helper.UtilitiesTestHelper;

/**
 * Converts "failed.log" into "test-failed.xml"
 */
public class TestNgFailedToTestFailedConvertor {
	static UtilitiesTestHelper helper = new UtilitiesTestHelper();
	
	public static void main(String[] args) throws IOException {
	String reporterClass = "<class name=\"kids.rebuild.email.All\"></class>";		
	helper.testNgFailedToTestFailedConvertor("test-failed", "test-failed", "test-failed.xml", reporterClass);
	}

}
