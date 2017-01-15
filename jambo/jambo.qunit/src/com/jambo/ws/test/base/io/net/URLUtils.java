/**
 * 
 */
package com.jambo.ws.test.base.io.net;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * <p>URL String Operation</p>
 * @author Lucky
 *
 */
public class URLUtils {

	//default charset name
	private static final String defaultCharSet = "UTF-8";
	
	private static String httpProtocolStr = "http";
	private static String httpsProtocolStr = "https";
	private static String protocolSuffix = "://";
	
	/**
	 * <p>eq URLEncoder.encode(String url, String charSetName), Exception insensitive</p>
	 * @param url
	 * @param charSetName
	 * @return
	 */
	public static String encode(String url, String charSetName){
		try {
			String str = URLEncoder.encode(url, charSetName);
			return str;
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * <p>eq URLEncoder.encode(String url, "utf-8"), Exception insensitive</p>
	 * @param url
	 * @param charSetName
	 * @return
	 */ 
	public static String encode(String url){
		return encode(url , defaultCharSet);
	}
	
	/**
	 * <p>eq URLDecoder.decode(String url, String charSetName), Exception insensitive</p>
	 * @param url
	 * @param charSetName
	 * @return
	 */
	public static String decode(String url, String charSetName){
		try {
			return URLDecoder.decode(url, charSetName);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * <p>eq UrlEncoder.encode(String url, "utf-8"), Exception insensitive</p>
	 * @param url
	 * @param charSetName
	 * @return
	 */
	public static String decode(String url){
		return decode(url , defaultCharSet);
	}
	
	/**
	 * String fromUrlBase = "http://www.mozat.com.cn";
	 *	String toUrlBase = "http://www.mozat.com";
	 *	String fromUrl = "http://www.mozat.com.cn/abc/zz.jsp";
	 *	System.out.println(absoluteUrl(fromUrlBase,toUrlBase, fromUrl));
	 *	
	 *	result: http://www.mozat.com/abc/zz.jsp
	 * @param fromBaseUrl
	 * @param toBaseUrl
	 * @param fromUrl
	 * @return
	 */
	public static String absoluteUrl(String fromBaseUrl, String toBaseUrl, String fromUrl){
		int index = fromUrl.length() - fromBaseUrl.length();
		if(index == -1){
			return fromUrl;
		}else{
			return toBaseUrl + fromUrl.substring(fromUrl.length() - index);
		}
	}

}
