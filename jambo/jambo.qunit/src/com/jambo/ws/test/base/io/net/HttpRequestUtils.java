package com.jambo.ws.test.base.io.net;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

/**
 * <p>Execute simple HTTP request, support Get、Post&Head Method</p>
 * <p>Once you need manage cookie, you should use HttpClient, not this Class</p>
 * @author Lucky
 *
 */
public class HttpRequestUtils {
	
	//default time out setting , half minute
	private static final int defaultTimeOut = 30*1000;
	
	private static final String defaultCharset = "UTF-8";

    /**
	 * <p>Do HTTP GET Request</p>
	 * @param url the HTTP URL for Request
	 * @param charSetName char set name etc. UTF_8,GB3212
	 * @param timeOut time out setting
	 * @return response stream
	 * @throws RequestException
	 */
	public static String doGet(String url, String charSetName, int timeOut) throws RequestException{
		try{
			URL ur = new URL(url);
			URLConnection con = ur.openConnection();
			con.setConnectTimeout(timeOut);
			con.setReadTimeout(timeOut);
			BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream(),charSetName));
			StringBuilder sb = new StringBuilder();
			try{			
				int k = rd.read();
				while(k!=-1){
					sb.append((char)k);
					k = rd.read();				
				}			
			}catch(Exception ee){
			}finally{
				if(rd != null){
					rd.close();
				}
			}
			return sb.toString();	
		}catch(Exception e){
			throw new RequestException(e);
		}
	}
	
	/**
	 * <p>Do HTTP GET Request, user default timeout setting</p>
	 * @param url the HTTP URL for Request
	 * @param charSetName char set name etc. UTF_8,GB3212
	 * @return response stream
	 * @throws RequestException
	 */
	public static String doGet(String url, String charSetName) throws RequestException{
		return doGet(url, charSetName, defaultTimeOut);
	}
	
	/**
	 * <p>Do HTTP GET Request, use default timeout setting & charSetName</p>
	 * @param url the HTTP URL for Request
	 * @return response stream
	 * @throws RequestException
	 */
	public static String doGet(String url) throws RequestException{
		return doGet(url,defaultCharset,defaultTimeOut);
	}
	
	/**
	 * <p>Get file From remote URL and store in disk</p>
	 * @param url the HTTP URL for Request
	 * @param timeOut time out setting
	 * @param fullFileName ect. c:/a.jar
	 * @throws RequestException
	 */
	public static void doGetFile(String url, int timeOut, String fullFileName) throws RequestException{
		InputStream is = null;
		OutputStream os = null;
		try{
			URL ur = new URL(url);
			URLConnection con = ur.openConnection();
			con.setConnectTimeout(timeOut);
			con.setReadTimeout(timeOut);		
			      
		    is = con.getInputStream();        
		       
		    // 1K cache     
		    byte[] bs = new byte[1024];        
		    // length      
		    int len; 
		    
		    os = new FileOutputStream(fullFileName);                
		    while ((len = is.read(bs)) != -1) {        
		      os.write(bs, 0, len);        
		    }            
		}catch(Exception e){
			throw new RequestException(e);
		}finally{
			if(os != null){
				try {
					os.close();
				} catch (IOException e) {
				}
			}
			if(is != null){
				try {
					is.close();
				} catch (IOException e) {
				}
			}
		}
	}
	
	/**
	 * <p>Get InputStream from URL</p>
	 * @param url the HTTP URL for Request
	 * @param timeOut time out setting
	 * @return response stream
	 * @throws RequestException
	 */
	public static InputStream doGetStream(String url, int timeOut) throws RequestException{
		InputStream is = null;
		try{
			URL ur = new URL(url);
			URLConnection con = ur.openConnection();
			con.setConnectTimeout(timeOut);
			con.setReadTimeout(timeOut);					      
		    is = con.getInputStream();     
		    return is;
		}catch(Exception e){
			throw new RequestException(e);
		}finally{
			if(is != null){
				try {
					is.close();
				} catch (Exception unusede) {
				}
			}
		}
	}
	
	/**
	 * <p>Do HTTP POST request</p>
	 * @param url the HTTP URL for Request
	 * @param parameters the posting parameter
	 * @param timeOut Timeout Setting
	 * @param charSetName char set name etc. UTF_8,GB3212
	 * @return response stream
	 */
	public static String doPost(String url, Map<String, String> parameters, int timeOut, String charSetName) throws RequestException{
		//generate post data form parameters
		StringBuilder sb= new StringBuilder();
		for(Map.Entry<String, String> kv: parameters.entrySet()){
            if(!"".equals(kv.getKey())){
                sb.append(kv.getKey());
                sb.append("=");
                sb.append(URLUtils.decode(kv.getValue()));
            }else{
                sb.append(kv.getValue());
            }
			sb.append("&");
		}
		if(sb.length() > 0){
			sb.deleteCharAt(sb.length() - 1);
		}
		byte[] postData = BytesUtils.toBytes(sb);	
		try{
			URL ur = new URL(url);
			URLConnection con = ur.openConnection();
			
			//setting
			con.setConnectTimeout(timeOut);
			con.setReadTimeout(timeOut);			
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setUseCaches(false);
			con.setDefaultUseCaches(false);
			
			con.setRequestProperty("Content-Length", postData.length+"");
			OutputStream os = con.getOutputStream();
			
			os.write(postData);
			os.flush();
			os.close();		
			BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream(),charSetName));
			StringBuilder rsb = new StringBuilder();
			try{			
				int k = rd.read();
				while(k!=-1){
					rsb.append((char)k);
					k = rd.read();				
				}			
			}catch(Exception ee){}
			finally{
				try{
					rd.close();
				}catch(Exception e){
					
				}
			}
			return rsb.toString();	
		}catch(Exception e){
			throw new RequestException(e);
		}
	}
	
	/**
	 * <p>Do HTTP POST request, Use default char set</p>
	 * @param url the HTTP URL for Request
	 * @param parameters the posting parameter
	 * @param timeOut Timeout Setting
	 * @return response stream
	 */
	public static String doPost(String url, Map<String, String> parameters, int timeOut) throws RequestException{
		return HttpRequestUtils.doPost(url, parameters, timeOut, defaultCharset);
	}
	
	/**
	 * <p>Do HTTP POST request, User default time out setting & char set</p>
	 * @param url the HTTP URL for Request
	 * @param parameters the posting parameter
	 * @return response stream
	 */
	public static String doPost(String url, Map<String, String> parameters)  throws RequestException{
		return HttpRequestUtils.doPost(url, parameters, defaultTimeOut, defaultCharset);
	}
	
	/**
	 * <p>Do HTTP HEAD Request</p>
	 * @param url  the HTTP URL for Request
	 * @param timeOut timeout setting
	 * @return state code
	 * @throws RequestException
	 */
	public static int doHead(String url, int timeOut) throws RequestException{
		try{
			URL ur = new URL(url);
			HttpURLConnection con = (HttpURLConnection)ur.openConnection();
			con.setConnectTimeout(timeOut);		
			return con.getResponseCode();
		}catch(Exception e){
			throw new RequestException(e);
		}
	}

	/**
	 * <p>Do HTTP HEAD Request, user default timeout setting</p>
	 * @param url  the HTTP URL for Request
	 * @return state code
	 * @throws RequestException
	 */
	public static int doHead(String url) throws RequestException{
		return doHead(url, defaultTimeOut);
	}

}
