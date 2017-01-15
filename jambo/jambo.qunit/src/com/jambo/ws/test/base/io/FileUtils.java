/**
 * 
 */
package com.jambo.ws.test.base.io;

import java.io.*;

/**
 * <p>File Operation</p>
 * @author Lucky
 *
 */
public class FileUtils {
	
	public static final int BUFFER_SIZE = 4096;

	/**
	 * <p>Read from text file, return the file content as a StringBuilder</p>
	 * @param path
	 * @return StringBuilder
	 */
	public static StringBuilder readFileToString(String path){
		BufferedReader reader = null;
		StringBuilder sb = null; 
		try{
			reader = new BufferedReader(new FileReader(path));
			sb = new StringBuilder();
			int c;
			c = reader.read();			
			while(c!=-1){
				sb.append((char)c);
				c = reader.read();				
			}	
		}catch(Exception e){
			throw new RuntimeException(e);
		}finally{
			IOUtils.closeQuietly(reader);
		}
		return sb;
	}
	
	/**
	 * <p>Read from text file, return the file content as a StringBuilder</p>
	 * @param path
	 * @param encoding
	 * @return
	 */
	public static StringBuilder readFileToString(String path, String encoding){
		BufferedReader reader = null;
		StringBuilder sb = null; 
		try{
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(path),encoding));
			sb = new StringBuilder();
			int c;
			c = reader.read();			
			while(c!=-1){
				sb.append((char)c);
				c = reader.read();				
			}	
		}catch(Exception e){
			throw new RuntimeException(e);
		}finally{
			IOUtils.closeQuietly(reader);
		}
		return sb;
	}
}
