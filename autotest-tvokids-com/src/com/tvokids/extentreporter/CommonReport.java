package com.tvokids.extentreporter;

import java.util.ArrayList;
import java.util.List;
import org.testng.IResultMap;
import org.testng.ITestResult;
import com.relevantcodes.extentreports.LogStatus;


public class CommonReport {
	
	private static int testFailed = 0;
	private static int testPassed = 0;
	private static int testSkipped = 0;
	static List<String> failedNameList = new ArrayList<String>();
	
	public static int getTestFailed() {
		return testFailed;
	}
	public static void setTestFailed(int test) {
		testFailed += test;
	}
	public static  int getTestPassed() {
		return testPassed;
	}
	public static void setTestPassed(int test) {
		testPassed += test;
	}
	public static int getTestSkipped() {
		return testSkipped;
	}
	public static void setTestSkipped(int test) {
		testSkipped += test;
	}

    public static List<String> GetFailedMethodsNames(){
    	return failedNameList;
    }
	
	List<String> GetFailedList(){
		return failedNameList;
	}
	
	@SuppressWarnings("static-access")
	public static void BuildReport(IResultMap tests, LogStatus status) {
		
		if(status == status.PASS)
			setTestPassed(tests.getAllResults().size());
		else if (status == status.FAIL){
			setTestFailed(tests.getAllResults().size());
			for (ITestResult tResult : tests.getAllResults()) {
				failedNameList.add(tResult.getMethod().getMethodName() + "\n");
	        } 
		}
		else if(status == status.SKIP)
			setTestSkipped(tests.getAllResults().size());
		
	}
}
