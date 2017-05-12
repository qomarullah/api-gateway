package tsel.proxy.api;

import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;
import static spark.Spark.delete;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import static spark.Spark.port;

import spark.Request;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import spark.Filter;
import spark.Request;
import spark.Response;
import spark.Spark;

import org.eclipse.jetty.server.ServerConnector;

public class Router {

	public Logger logger = LogManager.getLogger(Router.class);
	String exceptionCode = "99";

	private static final HashMap<String, String> corsHeaders = new HashMap<String, String>();

	Util util;
	int timeout;
	String path;
	String body;
	
	
	static {
		corsHeaders.put("Access-Control-Allow-Methods", "GET,PUT,POST,DELETE,OPTIONS");
		corsHeaders.put("Access-Control-Allow-Origin", "*");
		corsHeaders.put("Access-Control-Allow-Headers",
				"Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin,");
		corsHeaders.put("Access-Control-Allow-Credentials", "true");
	}

	public final static void apply() {
		Filter filter = new Filter() {
			@Override
			public void handle(Request request, Response response) throws Exception {
				corsHeaders.forEach((key, value) -> {
					response.header(key, value);
				});
				response.type("application/json");

			}
		};
		Spark.after(filter);

	}

	public String getUrl(String path){
		String url=App.url;
		int num=Integer.parseInt(App.prop.getProperty("num","0"));
		for(int i=0;i<num;i++){
			String[] filter=App.prop.getProperty(i+".proxy.filter","").split(",");
			for(int j=0;j<filter.length;j++){
				if(path.contains(filter[j])){
					url=App.prop.getProperty(i+".proxy.url","");
					return url;
				}
			}
			
		}
		
		return url;
	}
	public Router(int port, int minThreads, int maxThreads, int timeOutMillis) {
		util = new Util();
		
		
		Spark.port(port);
		Spark.threadPool(maxThreads, minThreads, timeOutMillis);
		
		apply(); // Call this before mapping thy routes

		//////////////////////////////////////////////////
		// ROUTING START
		/////////////////////////////////////////////////
		
		get("/bismillah", (req, res) -> "Alhamdulillah");
		get("/status", "application/json", (request, response) -> {
			String resp="OK";
			return  resp;
		});
		
		
		get("/*", "application/json", (request, response) -> {

			String resp = "Bad Request";
			
			String path=request.pathInfo()+"?"+request.queryString();
			String url=getUrl(path)+path;
			resp=util.get(url, timeout);
			
			Log.info(url+"=>"+resp.replaceAll("\r", "").replaceAll("\n", ""));
			
			return resp;
			
		});
		
		post("/*", "application/json", (request, response) -> {

			String resp = "Bad Request";
			
			String path=request.pathInfo()+"?"+request.queryString();
			String url=getUrl(path)+path;
			
			resp=util.post(url, request.body(), timeout);
			Log.info(url+"=>"+resp.replaceAll("\r", "").replaceAll("\n", ""));
			
			return resp;
			
		});
		
		
	}
}