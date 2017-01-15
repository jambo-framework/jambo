package com.jambo.ws.test.base.io.net;

import java.io.UnsupportedEncodingException;

/**
 * <p>byte[] operation</p>
 * @author Lucky
 */
public class BytesUtils {

	private static final String defaultCharset = "UTF-8";
	
	 	/**
	     * <p>Convert string to byte[]</p>
	     * @param str the string to convert
	     * @param charSetName
	     * @return byte[]
	     */
	    public static byte[] toBytes(String str, String charSetName){
	    	if(str != null){
	    		try {
					return str.getBytes(charSetName);
				} catch (UnsupportedEncodingException e) {
					throw new RuntimeException(e);
				}
	    	}else{
	    		throw new IllegalArgumentException("input string must not be null");
	    	}
	    }
	    
	    /**
	     * <p>Convert string to bytes, user default charsetName</p>
	     * @param str the string to convert 
	     * @return byte[]
	     */
	    public static byte[] toBytes(String str){
	    	return toBytes(str, defaultCharset);
	    }
	    
	    /**
	     * <p>Convert StringBuilder to bytes, user default charsetName</p>
	     * @param sb the StringBuilder to convert 
	     * @return byte[]
	     */
	    public static byte[] toBytes(StringBuilder sb, String charSetName){
	    	return toBytes(sb.toString(), charSetName);
	    }
	    
	    /**
	     * <p>Convert StringBuilder to bytes, user default charsetName</p>
	     * @param sb the StringBuilder to convert 
	     * @return byte[]
	     */
	    public static byte[] toBytes(StringBuilder sb){
	    	return toBytes(sb, defaultCharset);
	    }
}
