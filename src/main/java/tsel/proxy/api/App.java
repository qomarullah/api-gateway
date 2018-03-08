package tsel.proxy.api;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import tsel.proxy.api.Log;

/**
 * Hello world!
 *
 */
public class App 
{
	
	
	public static int port;
	
	public static int minThreads;
	public static int maxThreads;
	public static int timeOutMillis;
	
	public static String url;
	
	public static Properties prop = new Properties();
	static InputStream input = null;
	
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        
        
        //System.setProperty("log4j.configurationFile",args[1]);
	 	Log.debug("starting..");
	    
	 	try {
	 		input = new FileInputStream(args[0]);

			// load a properties file
			prop.load(input);
			port=Integer.parseInt(prop.getProperty("server.port","7000"));
			minThreads=Integer.parseInt(prop.getProperty("server.minthread","2"));
			maxThreads=Integer.parseInt(prop.getProperty("server.maxthread","8"));
			timeOutMillis=Integer.parseInt(prop.getProperty("server.timeout","30000"));
			url=prop.getProperty("server.url","http://localhost");
			
			
			//Start Router
			new Router(port, minThreads, maxThreads, timeOutMillis);
			
			
	 	}catch (Exception e) {
			// TODO: handle exception
		}
			
        
    }
}
