package com.tvokids.test.retry;

import java.io.IOException;
import java.util.Map;

import com.tvokids.test.helper.*;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * This class is responsible for how many times the test will be run on fail
 */
public class RetryOnFail implements IRetryAnalyzer {
    private int retryCount = 0;
    private int maxRetryCount = 1;
	
    /**
     * Retreaving Max Retry Count parameter from Configuration File (EXTERNAL)
     * @throws Exception 
     */
	public int maxRetryCount() throws Exception {
	  Map<String, String> configs;
	  configs = xmlParser.parseConfigFileXMLforVariables("config.xml","setupvariables");
	  return Integer.parseInt(configs.get("RunOnFail"));
     }
  
    /**
     * Activating Max Retry Count parameter from Configuration File using Constructor
     * @throws Exception
     */
	public RetryOnFail() throws Exception {
		super();
	
	 // INTERNAL RETREAVING (OPTIONAL ALL-IN-ONE)
     // Map<String, String> configs;
     // configs = xmlParser.parseConfigFileXMLforVariables("config.xml","setupvariables");
	 // maxRetryCount = Integer.parseInt(configs.get("RunOnFail"));
		
		maxRetryCount = maxRetryCount();
		}
    
    /**
     * Below method returns 'true' if the test method has to be retried else 'false'
     * and it takes the 'Result' as parameter of the test method that just ran
     */
//    public boolean retry(ITestResult result) {
//      String[] time = {"st","nd","rd","th","th","th","th","th","th","th","th","th","th","th"};
//      if (retryCount < maxRetryCount) {
//    	  
////    	  System.out.println("\n\"" + result.getName() + "\" test re-trying, with a status of " +
////                  getResultStatusName(result.getStatus()) + 
////                  ", for " + (retryCount + 1) + "-" + time[retryCount] + " time:"
////                  ); // where: result.getName() = result.getMethod().getMethodName()
//    	  
//    	  System.out.println("\n\"" + result.getTestClass().getName() +"."+ result.getMethod().getMethodName() + "\" test re-trying, with a status of " +
//                  getResultStatusName(result.getStatus()) + 
//                  ", for " + (retryCount + 1) + "-" + time[retryCount] + " time:"
//                  );
//      
//    	  retryCount++;
//      
//    	  return true;
//    	  }     
//      return false;
//    } 
    
    @SuppressWarnings("static-access")
	public boolean retry(ITestResult result) {
    	UtilitiesTestHelper helper = new UtilitiesTestHelper();
    	String[] time = {"st","nd","rd","th","th","th","th","th","th","th","th","th","th","th"};
        if (retryCount < maxRetryCount) {
            String err = result.getThrowable().getMessage().toString().replace("\n\n", "\n");
            try {
				helper.fileWriter("run.log", "  Retrying test with status of "  + getResultStatusName(result.getStatus()));
				
// helper.getExceptionDescriptive(result.getThrowable(), result.getThrowable().getStackTrace()[2]);
				
				System.out.println("   Executed: ---> " +  (retryCount + 1)  + "-" + time[retryCount] + " time");
				System.out.println("     Result: ---> "  + getResultStatusName(result.getStatus()));
				System.out.println("    Details: "  + err);
				System.out.println("\n\n" + "Retrying the test \"" + result.getTestClass().getName() +"."+ result.getMethod().getMethodName() + "\" with status of " + getResultStatusName(result.getStatus()) + " for the " + (retryCount + 2)  + "-" + time[retryCount + 1] + " time:");
			} catch (NumberFormatException | IOException e) { /* TODO Auto-generated catch block */ e.printStackTrace(); }
            retryCount++;
            return true;
        }
        return false;
    }
    
    public String getResultStatusName(int status) {
    	String resultName = null;
    	if(status==1)
    		resultName = "SUCCESS";
    	if(status==2)
    		resultName = "FAILURE";
    	if(status==3)
    		resultName = "SKIP";
		return resultName;
    }
}