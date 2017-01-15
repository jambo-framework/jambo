package com.jambo.jop.exception.system;

import com.jambo.jop.exception.JOPException;

/**
 * <p>Title: GDIBOSS</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: jambo-framework Tech Ltd.</p>
 *
 * @author HuangBaiming
 *
 * @version 1.0
 */
public class ControlException extends JOPException {
	public static final String ERROR_CODE_CONTROL_COMMON = "business.common";
	/**
	 * @param errorCode  错误代码
	 */
	public ControlException(String errorCode) {
		super (toMessage(JOPException.class, checkErrorCode(errorCode), ""));
		setErrorCode(errorCode);
	}
	
	public ControlException(String message, Throwable cause) {
		super(message, cause);
	}

	public ControlException(String errorCode, String[] msgParam, Throwable cause) {
		super (toMessage(ServiceLocatorException.class, checkErrorCode(errorCode),msgParam), cause);
		setErrorCode(errorCode);
	}

	public ControlException(String errorCode, String msgParam, Throwable cause) {
		super (toMessage(ServiceLocatorException.class, checkErrorCode(errorCode), msgParam), cause);
		setErrorCode(errorCode);
	}

	protected static String checkErrorCode(String errorCode){
		return (errorCode==null)?ERROR_CODE_CONTROL_COMMON:errorCode;
	}

}
