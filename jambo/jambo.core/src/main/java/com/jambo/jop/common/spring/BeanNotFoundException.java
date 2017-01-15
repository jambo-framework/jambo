/**
 * 
 */
package com.jambo.jop.common.spring;

/**
 * Thrown when no bean found in spring config.
 * @author He Kun
 *
 */
public class BeanNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	public BeanNotFoundException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 */
	public BeanNotFoundException(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 */
	public BeanNotFoundException(Throwable arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public BeanNotFoundException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

}
