package com.jambo.jop.exception.system;

import com.jambo.jop.exception.JOPException;

/**
 * <p>Description: BaseAction抛出的异常</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: jambo-framework Tech Ltd.</p>
 *
 * @author JinBo
 *
 * @version 1.0
 */
public class ActionException extends JOPException {
	public static final String ERROR_CODE_ACTION_COMMON = "action.common";
	/**
	 * @param errorCode  错误代码
	 */
	public ActionException(String errorCode) {
		super (toMessage(JOPException.class, checkErrorCode(errorCode), ""));
		setErrorCode(errorCode);
	}
	
	public ActionException(String message, Throwable cause) {
		super(message, cause);
	}

	public ActionException(String errorCode, String[] msgParam, Throwable cause) {
		super (toMessage(ActionException.class, checkErrorCode(errorCode),msgParam), cause);
		setErrorCode(errorCode);
	}

	public ActionException(String errorCode, String msgParam, Throwable cause) {
		super (toMessage(ActionException.class, checkErrorCode(errorCode), msgParam), cause);
		setErrorCode(errorCode);
	}

	protected static String checkErrorCode(String errorCode){
		return (errorCode==null)?ERROR_CODE_ACTION_COMMON:errorCode;
	}
	
}
