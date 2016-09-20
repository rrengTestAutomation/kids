package com.tvokids.extentreporter;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/*
import java.io.IOException;
import javax.mail.MessagingException;
*/
import org.testng.IReporter;
import org.testng.IResultMap;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.xml.XmlSuite;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.tvokids.send.Mail;
import com.tvokids.email.All;
import com.tvokids.utilities.*;
 
public class ExtentReporterNG implements IReporter {
    private ExtentReports extent;
   
    @SuppressWarnings("static-access")
	@Override
    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
    	UtilitiesTestHelper helper = new UtilitiesTestHelper();
        extent = new ExtentReports(outputDirectory + File.separator + "extent-test-report.html", true);

        ITestContext context = null;
        for (ISuite suite : suites) {
            Map<String, ISuiteResult> result = suite.getResults();
            System.out.println("Extent Test Report has been created (result size: " + result.size() + "1)\n");
            for (ISuiteResult r : result.values()) {
                 context = r.getTestContext();
                buildTestNodes(context.getPassedTests(), LogStatus.PASS);
                buildTestNodes(context.getFailedTests(), LogStatus.FAIL);
                buildTestNodes(context.getSkippedTests(), LogStatus.SKIP);
            }
        }
       
        extent.flush();
        extent.close(); 

        try {
        	// SEND E-MAIL:
        	if( helper.fileExist("email.all", false) && Boolean.valueOf(helper.fileScanner("email.all"))  ) { new All().finish(); }
        	if( helper.fileExist("email.all", false) && !Boolean.valueOf(helper.fileScanner("email.all")) ) { new Mail().finish(); }
        	// CREATING TEST-FAILED.XML:
        	String reporterClass = "<class name=\"com.tvokids.email.All\"></class>";
        	helper.testLogToXmlCreator("test-failed", "test-failed", "failed.log", "test-failed.xml", reporterClass);
        	// AFTER CLEANING:
        	helper.afterCleaner();
        	}
        catch ( Exception e) { /* e.printStackTrace(); */ }
        
    }
 
    private void buildTestNodes(IResultMap tests, LogStatus status) {
        ExtentTest test;

        CommonReport.BuildReport( tests, status);
        if (tests.size() > 0) {
            for (ITestResult result : tests.getAllResults()) {
                 test = extent.startTest(result.getMethod().getMethodName());
 
                test.setStartedTime(getTime(result.getStartMillis()));
                test.setEndedTime(getTime(result.getEndMillis()));
 
                for (String group : result.getMethod().getGroups())
                    test.assignCategory(group);
 
                if (result.getThrowable() != null) {
                    test.log(status, result.getThrowable());
                }
                else {
                    test.log(status, "Test " + status.toString().toLowerCase() + "ed");
                }
 
                extent.endTest(test);
            }
        }
    }
 
    private Date getTime(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        return calendar.getTime();        
    }
}
