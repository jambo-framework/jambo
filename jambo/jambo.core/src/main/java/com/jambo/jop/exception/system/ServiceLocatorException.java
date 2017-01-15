package com.jambo.jop.exception.system;

import com.jambo.jop.exception.JOPException;

/**
 * <p>Title: GDIBOSS</p>
 *
 * <p>Description: 服务定位器ServiceLocator抛出的异常</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: jambo-framework Tech Ltd.</p>
 *
 * @author HuangBaiming
 *
 * @version 1.0
 */
public class ServiceLocatorException extends JOPException {
	public static final String ERROR_CODE_LOCATOR_COMMON = "locator.common";
//	private String errorCode = ERROR_CODE_LOCATOR_COMMON;
	/**
	 * @param errorCode  错误代码
	 */
	public ServiceLocatorException(String errorCode) {
		super (toMessage(JOPException.class, checkErrorCode(errorCode), ""));
		setErrorCode(errorCode);
	}
	
	public ServiceLocatorException(String message, Throwable cause) {
		super(message, cause);
	}

	public ServiceLocatorException(String errorCode, String[] msgParam, Throwable cause) {
		super (toMessage(ServiceLocatorException.class, checkErrorCode(errorCode),msgParam), cause);
		setErrorCode(errorCode);
	}

	public ServiceLocatorException(String errorCode, String msgParam, Throwable cause) {
		super (toMessage(ServiceLocatorException.class, checkErrorCode(errorCode), msgParam), cause);
		setErrorCode(errorCode);
	}

	protected static String checkErrorCode(String errorCode){
		return (errorCode==null)?ERROR_CODE_LOCATOR_COMMON:errorCode;
	}
	
}
