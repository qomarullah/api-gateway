package tsel.proxy.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Log
{
	public static Logger logger = LogManager.getLogger(Log.class);
	
	public static Logger getLogger() {
		return LogManager.getLogger(Log.class);
	}
	
	public static void info(String value)
	{
		logger.info(value);
	}
	
	public static void debug(String value)
	{
		logger.debug(value);
	}
	
	public static void error(String value)
	{
		logger.error(value);
	}
	
	public static void fatal(String value, Exception e)
	{
		logger.fatal(value, e);
	}
}