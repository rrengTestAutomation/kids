package com.tvokids.test.retry;
/**
 * Logger class, responsible to log data to file
 * @author 
 *
 */
public class Logger {
	private static org.apache.log4j.Logger getLogger() {
		StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
		String funName = stackTraceElements[3].getMethodName();
		int lineNumber = stackTraceElements[3].getLineNumber();
		String[] className = stackTraceElements[3].getFileName().split("\\.");
		return org.apache.log4j.Logger.getLogger(className[0] + ":" + funName
				+ ":" + lineNumber);
	}
	
	
	public static void writeInfoLogEntry(String message) {
		org.apache.log4j.Logger logger = getLogger();
		// System.out.println(message);
		logger.info(message);
	}
	
	
	public static void writeErrorLogEntry(String message) {
		org.apache.log4j.Logger logger = getLogger();
		// System.out.println(message);
		logger.error(message);
	}
	
	public static void writeWarmLogEntry(String message) {
		org.apache.log4j.Logger logger = getLogger();
		// System.out.println(message);
		logger.warn(message);
	}


}
