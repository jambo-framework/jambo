/**
 * 
 */
package com.jambo.ws.test.base.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

/**
 * <p>Some common IO close operation</p>
 * @author Lucky
 *
 */
public class IOUtils {
	
	private IOUtils(){		
	}

	/**
     * Unconditionally close an <code>Reader</code>.
     * <p>
     * Equivalent to {@link java.io.Reader#close()}, except any exceptions will be ignored.
     * This is typically used in finally blocks.
     *
     * @param input  the Reader to close, may be null or already closed
     */
    public static void closeQuietly(Reader input) {
        try {
            if (input != null) {
                input.close();
            }
        } catch (IOException ioe) {
            // ignore
        }
    }

    /**
     * Unconditionally close a <code>Writer</code>.
     * <p>
     * Equivalent to {@link java.io.Writer#close()}, except any exceptions will be ignored.
     * This is typically used in finally blocks.
     *
     * @param output  the Writer to close, may be null or already closed
     */
    public static void closeQuietly(Writer output) {
        try {
            if (output != null) {
                output.close();
            }
        } catch (IOException ioe) {
            // ignore
        }
    }

    /**
     * Unconditionally close an <code>InputStream</code>.
     * <p>
     * Equivalent to {@link java.io.InputStream#close()}, except any exceptions will be ignored.
     * This is typically used in finally blocks.
     *
     * @param input  the InputStream to close, may be null or already closed
     */
    public static void closeQuietly(InputStream input) {
        try {
            if (input != null) {
                input.close();
            }
        } catch (IOException ioe) {
            // ignore
        }
    }

    /**
     * Unconditionally close an <code>OutputStream</code>.
     * <p>
     * Equivalent to {@link java.io.OutputStream#close()}, except any exceptions will be ignored.
     * This is typically used in finally blocks.
     *
     * @param output  the OutputStream to close, may be null or already closed
     */
    public static void closeQuietly(OutputStream output) {
        try {
            if (output != null) {
                output.close();
            }
        } catch (IOException ioe) {
            // ignore
        }
    }
    
    /**
     * Get String form appointing InputStream
     * @param in InputStream
     * @param encoding
     * @return
     */
	public static String toString(InputStream in, String encoding){
		BufferedReader reader = null;
		StringBuilder sb = null; 
		try{
			reader = new BufferedReader(new InputStreamReader(in,encoding));
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
		return sb.toString();
	}
}
