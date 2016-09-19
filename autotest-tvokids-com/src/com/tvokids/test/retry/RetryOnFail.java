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

	private int retryCount, maxRetryCount;
	Boolean retryOnFail;
	
    /**
     * Retreaving Retry On Fail parameter from Configuration File (EXTERNAL)
     * @throws Exception 
     */
	public Boolean retryOnFail() throws Exception {
	  Map<String, String> configs;
	  configs = xmlParser.parseConfigFileXMLforVariables("config.xml","setupvariables");
	  return Boolean.parseBoolean(configs.get("retryOnFail"));
     } 
	
    /**
     * Retreaving Start Retry Count parameter from Configuration File (EXTERNAL)
     * @throws Exception 
     */
	public int retryCountStart() throws Exception {
	  Map<String, String> configs;
	  configs = xmlParser.parseConfigFileXMLforVariables("config.xml","setupvariables");
	  return Integer.parseInt(configs.get("retryCountStart"));
     }    
	    
    /**
     * Retreaving Max Retry Count parameter from Configuration File (EXTERNAL)
     * @throws Exception 
     */
	public int maxRetryCount() throws Exception {
	  Map<String, String> configs;
	  configs = xmlParser.parseConfigFileXMLforVariables("config.xml","setupvariables");
	  return Integer.parseInt(configs.get("maxRetryCount"));
     }
  
    /**
     * Activating Max Retry Count parameter from Configuration File using Constructor
     * @throws Exception
     */
	public RetryOnFail() throws Exception {
		super();		
//	    // INTERNAL RETREAVING (OPTIONAL ALL-IN-ONE):
//		Map<String, String> configs;
//		configs = xmlParser.parseConfigFileXMLforVariables("config.xml","setupvariables");
//		retryOnFail   = Boolean.parseBoolean(configs.get("retryOnFail"));
//		retryCount    = Integer.parseInt(configs.get("retryCountStart"));
//		maxRetryCount = Integer.parseInt(configs.get("maxRetryCount"));		
		retryOnFail   = retryOnFail();
		retryCount    = retryCountStart();
		maxRetryCount = maxRetryCount();
		}
    
//    /**
//     * Below method returns 'true' if the test method has to be retried else 'false'
//     * and it takes the 'Result' as parameter of the test method that just ran
//     */
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
	
    /**
     * Below method returns 'true' if the test method has to be retried else 'false'
     * and it takes the 'Result' as parameter of the test method that just ran
     */
    @SuppressWarnings("static-access")
	public boolean retry(ITestResult result) {
    	UtilitiesTestHelper helper = new UtilitiesTestHelper();
    	String[] time = {"st","nd","rd","th","th","th","th","th","th","th","th","th","th","th"};
        if ( (retryCount < maxRetryCount) && (retryOnFail) ) {
            String err = result.getThrowable().getMessage().toString().replace("\n\n", "\n");
            try {
            	helper.counter("test.num", -1);
            	helper.counter("failed.num", -1);
				helper.fileWriter(   "run.log", "\n   Retrying test with status of " + getResultStatusName(result.getStatus()) + " for the " + (retryCount + 2)  + "-" + time[retryCount + 1] + " time:");
				helper.fileWriter("failed.log",   "   Retrying: " + (retryCount + 2) + "-" + time[retryCount + 1] + " time...\n");
//				helper.getExceptionDescriptive(result.getThrowable(), result.getThrowable().getStackTrace()[2]);				
				helper.fileWriterPrinter("   Executed: ---> " +  (retryCount + 1)  + "-" + time[retryCount] + " time");
				helper.fileWriterPrinter("     Result: ---> "  + getResultStatusName(result.getStatus()));
				helper.fileWriterPrinter("    Details: "  + err);
				helper.fileWriterPrinter("\n" + "Retrying the test \"" + result.getTestClass().getName() +"."+ result.getMethod().getMethodName() + "\" with status of " + getResultStatusName(result.getStatus()) + " for the " + (retryCount + 2)  + "-" + time[retryCount + 1] + " time:");
			} catch (NumberFormatException | IOException e) { /* TODO Auto-generated catch block */ e.printStackTrace(); }
            retryCount++;
            return true;
        }
        return false;
    }
    
    public String getResultStatusName(int status) {
    	String resultName = null;
    	if(status==1) resultName = "SUCCESS";
    	if(status==2) resultName = "FAILURE";
    	if(status==3) resultName = "SKIP";
		return resultName;
    }
}