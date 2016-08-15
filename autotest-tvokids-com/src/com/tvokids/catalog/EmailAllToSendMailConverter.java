package com.tvokids.catalog;

import java.io.IOException;
import com.tvokids.test.helper.UtilitiesTestHelper;

public class EmailAllToSendMailConverter {
	static UtilitiesTestHelper helper = new UtilitiesTestHelper();
	
	public static void main(String[] args) throws IOException {
		String reporterClass = "<class name=\"com.tvokids.send.Mail\"></class>";		
		helper.testFailedToTestFailedConverter("test-failed", "test-failed", "test-failed.xml", reporterClass);
	}

}
