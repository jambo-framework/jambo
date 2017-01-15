/**
 * 
 */
package com.jambo.ws.test.base.io.net;

/**
 * <p>Exception when execute Request</p>
 * @author Lucky
 *
 */
public class RequestException extends Exception {

	private static final long serialVersionUID = 3812105322628942359L;
	
	public RequestException(){
		super();
	}
	
	public RequestException(String msg){
		super(msg);
	}
	
	public RequestException(Exception e){
		super(e);
	}

}
