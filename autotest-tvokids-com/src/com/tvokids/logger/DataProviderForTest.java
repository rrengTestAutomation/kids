package com.tvokids.logger;

import java.util.Map;

import org.testng.annotations.DataProvider;

import com.tvokids.rerun.xmlParser;

/**
 * Data provider class, provide times to run the test.
 * @author best
 *
 */
public class DataProviderForTest {
	
	
	@DataProvider(name = "numberOfTimesToRun")
	public static Object[][] getData() throws NumberFormatException, Exception {
		Map<String, String> configs = xmlParser.parseConfigFileXMLforVariables("config.xml","setupvariables");

		int n = Integer.parseInt(configs.get("numberOfTimesToRun"));
		Object[][] obj = new Object[n][1];
		for (int i = 0; i < n; i++) {
			obj[i][0] = i + 1;
		}
		return obj;
	}

	@DataProvider(name = "RunOnFail")
	public static Object[][] getValue() throws NumberFormatException, Exception {
		Map<String, String> configs = xmlParser.parseConfigFileXMLforVariables("config.xml","setupvariables");

		int n = Integer.parseInt(configs.get("RunOnFail"));
		Object[][] obj = new Object[n][1];
		for (int i = 0; i < n; i++) {
			obj[i][0] = i + 1;
		}
		return obj;
	}
	

}
