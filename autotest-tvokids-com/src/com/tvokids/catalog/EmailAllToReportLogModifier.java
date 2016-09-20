package com.tvokids.catalog;

import java.io.IOException;

import com.tvokids.utilities.*;

public class EmailAllToReportLogModifier {
	static UtilitiesTestHelper helper = new UtilitiesTestHelper();
	
	public static void main(String[] args) throws IOException {
		String reporterClass = "<class name=\"com.tvokids.report.Log\"></class>";		
		helper.testFailedToTestFailedModifier("test-failed", "test-failed", "test-failed.xml", reporterClass);
	}

}
