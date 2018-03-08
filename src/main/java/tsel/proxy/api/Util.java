package tsel.proxy.api;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.SecureProtocolSocketFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Util {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	

	public Logger logger = LogManager.getLogger(Router.class);
	
	
	public String get(String url, int timeout) {
		return get(url, timeout, null,null);
	}
	
	public String getHttps(String url, int timeout) {
		return getHttps(url, timeout, null,null);
	}
	public String get(String url, int timeout, String proxyHost,
			String proxyPort) {
		long start = System.currentTimeMillis();
		url = url.replaceAll(" ", "+");
		String response = null;
		HttpClient http = new HttpClient();
		http.getParams().setSoTimeout(timeout);
		if (proxyHost != null && proxyPort != null) {
			logger.info("Using proxy = " + proxyHost + ":" + proxyPort);
			http.getHostConfiguration().setProxy(proxyHost,
					Integer.parseInt(proxyPort));
		}
		HttpMethod method = new GetMethod(url);
		http.getParams().setParameter("http.socket.timeout", timeout);
		http.getParams().setParameter("http.connection.timeout", timeout);
		method.getParams().setParameter("http.socket.timeout", timeout);
		method.getParams().setParameter("http.connection.timeout", timeout);
		try {
			int statusCode = http.executeMethod(method);
			if (statusCode != HttpStatus.SC_OK && statusCode != 202) {
				response = "HTTP_FAILED";
			} else {
				response = method.getResponseBodyAsString().trim();
			}
		} catch (Exception e) {
			// e.printStackTrace();
			logger.error("Error in httpGet", e);
			response = "FAILED";
		} finally {
			method.releaseConnection();
		}
		
		logger.info("HTTP-GET|" + url + "|Response|" + response + "|"+ (System.currentTimeMillis() - start) + "ms");

		//System.out.println("HTTP-GET|" + url + "|Response|" + response + "|"+ (System.currentTimeMillis() - start) + "ms");

		return response;
	}

	public String getHttps(String url, int timeout, String proxyHost,
			String proxyPort) {
		long start = System.currentTimeMillis();
		url = url.replaceAll(" ", "+");
		String response = null;
		HttpClient http = new HttpClient();
		http.getParams().setSoTimeout(timeout);
		if (proxyHost != null && proxyPort != null) {
			logger.info("Using proxy = " + proxyHost + ":" + proxyPort);
			http.getHostConfiguration().setProxy(proxyHost,
					Integer.parseInt(proxyPort));
		}
		HttpMethod method = new GetMethod(url);
		http.getParams().setParameter("http.socket.timeout", timeout);
		http.getParams().setParameter("http.connection.timeout", timeout);
		method.getParams().setParameter("http.socket.timeout", timeout);
		method.getParams().setParameter("http.connection.timeout", timeout);
		try {
			int statusCode = http.executeMethod(method);
			if (statusCode != HttpStatus.SC_OK && statusCode != 202) {
				response = "HTTP_FAILED";
			} else {
				response = method.getResponseBodyAsString().trim();
			}
		} catch (Exception e) {
			// e.printStackTrace();
			logger.error("Error in httpGet", e);
			response = "FAILED";
		} finally {
			method.releaseConnection();
		}
		
		logger.info("HTTP-GET|" + url + "|Response|" + response + "|"+ (System.currentTimeMillis() - start) + "ms");

		//System.out.println("HTTP-GET|" + url + "|Response|" + response + "|"+ (System.currentTimeMillis() - start) + "ms");

		return response;
	}


	public String postHttps(String url, String remFile, int timeout){
		// Create a trust manager that does not validate certificate chains
        try {
			
		TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }
        };
 
        // Install the all-trusting trust manager
        SSLContext sc;
		try {
			sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
		    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		 
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      
        // Create all-trusting host name verifier
        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
 
        // Install the all-trusting host verifier
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
 
        String resp=null;
        StringBuilder builder = new StringBuilder();
        
        
        URL urlx = new URL(url);
        URLConnection con = urlx.openConnection();
        Reader reader = new InputStreamReader(con.getInputStream());
        while (true) {
            int ch = reader.read();
            if (ch==-1) {
                break;
            }
            builder.append((char)ch);
           // System.out.print((char)ch);
        }
        	return builder.toString();
        
		} catch (Exception e) {
			// TODO: handle exception
			return e.getMessage();
		}

	}
	
	 public String postx(String url, String remFile, int timeout){

			String respXML="";
			String PN = "HTTP-REQUEST";
			String inputLine;
			StringBuffer resp = new StringBuffer();
			
			try{
					
				URL obj = new URL(url);
				HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
				con.setRequestMethod("POST");
				con.setRequestProperty("Content-Type","application/json"); 
				con.setDoOutput(true);
			
				DataOutputStream wr = new DataOutputStream(con.getOutputStream());
				wr.writeBytes(remFile);
				wr.flush();
				wr.close();
				
				int responseCode = con.getResponseCode();
				//System.out.println("\nSending 'POST' request to URL : " + url);
				//System.out.println("Response Code : " + responseCode);
				if (con.getResponseCode() >= 400) {
					//    is = httpConn.getErrorStrieam();
					BufferedReader in2 = new BufferedReader(new InputStreamReader(con.getErrorStream()));
					while ((inputLine = in2.readLine()) != null) {
					        resp.append(inputLine);
					}
					in2.close();
				
					//print result
					logger.info(PN + "|HTTP|POST(" + url + "," + remFile + "," + timeout + ")=" + resp.toString());
				
				} else {
				    //is = httpConn.getInputStream();
					BufferedReader in2 = new BufferedReader(new InputStreamReader(con.getInputStream()));
					while ((inputLine = in2.readLine()) != null) {
					        resp.append(inputLine);
					}
					in2.close();
				}
				
				
				logger.info(PN + "|HTTP|POST(" + url + "," + remFile + "," + timeout + ")=" + resp.toString());
				//System.out.println(PN + "|HTTP|POST(" + url + "," + remFile + "," + timeout + ")=" + resp.toString());
				
				respXML=resp.toString();
				return respXML;
			
				
		} catch(Exception i)
		{
	      logger.fatal(PN + "|HTTP|POST(" + url + "," + remFile + "," + timeout + ")=" + i.getMessage(), i);
	      return "FAILED" ;

	  	}
	 }
	 public String post(String url, String remFile, int timeout){
			
			String respXML="";
			String PN = "HTTP-REQUEST";
			String inputLine;
			StringBuffer resp = new StringBuffer();
			
			try{
					
				URL obj = new URL(url);
				HttpURLConnection con = (HttpURLConnection) obj.openConnection();
				con.setRequestMethod("POST");
				con.setRequestProperty("Content-Type","application/json"); 
				con.setDoOutput(true);
			
				DataOutputStream wr = new DataOutputStream(con.getOutputStream());
				wr.writeBytes(remFile);
				wr.flush();
				wr.close();
				
				int responseCode = con.getResponseCode();
				//System.out.println("\nSending 'POST' request to URL : " + url);
				//System.out.println("Response Code : " + responseCode);
				if (con.getResponseCode() >= 400) {
					//    is = httpConn.getErrorStrieam();
					BufferedReader in2 = new BufferedReader(new InputStreamReader(con.getErrorStream()));
					while ((inputLine = in2.readLine()) != null) {
					        resp.append(inputLine);
					}
					in2.close();
				
					//print result
					logger.info(PN + "|HTTP|POST(" + url + "," + remFile + "," + timeout + ")=" + resp.toString());
				
				} else {
				    //is = httpConn.getInputStream();
					BufferedReader in2 = new BufferedReader(new InputStreamReader(con.getInputStream()));
					while ((inputLine = in2.readLine()) != null) {
					        resp.append(inputLine);
					}
					in2.close();
				}
				
				
				logger.info(PN + "|HTTP|POST(" + url + "," + remFile + "," + timeout + ")=" + resp.toString());
				//System.out.println(PN + "|HTTP|POST(" + url + "," + remFile + "," + timeout + ")=" + resp.toString());
				
				respXML=resp.toString();
				return respXML;
			
				
		} catch(Exception i)
		{
	      logger.fatal(PN + "|HTTP|POST(" + url + "," + remFile + "," + timeout + ")=" + i.getMessage(), i);
	      return "FAILED" ;

	  	}
	 }
}
