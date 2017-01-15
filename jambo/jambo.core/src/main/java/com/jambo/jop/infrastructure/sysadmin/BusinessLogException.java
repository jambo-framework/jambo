package com.jambo.jop.infrastructure.sysadmin;

/**
 * 
 * @author He Kun (Sunrise,Guangzhou, CN)
 *
 */
public class BusinessLogException extends RuntimeException {

	public BusinessLogException() {
	}

	public BusinessLogException(String message) {
		super(message);

	}

	public BusinessLogException(Throwable cause) {
		super(cause);
	}

	public BusinessLogException(String message, Throwable cause) {
		super(message, cause);		
	}

}
