package com.jambo.jop.exception.business;

import com.jambo.jop.exception.BusinessException;
import com.jambo.jop.exception.JOPException;

/**
 * <p>Title: 非法主键</p>
 * <p>Description: 即主键对应的记录不存在或无效</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: jambo-framework Tech Ltd. </p>
 * @author jambo-framework
 * @version 1.0
 */
public class InvalidIdException extends BusinessException {
	public static final String ERROR_CODE_DB_INVALIDID = "db.invalidid";
	/**
	 * @param errorCode  错误代码
	 */
	public InvalidIdException(String errorCode) {
		super (JOPException.toMessage(BusinessException.class, checkErrorCode(errorCode), ""));
		setErrorCode(errorCode);
	}
	
	public InvalidIdException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidIdException(String errorCode, String[] msgParam, Throwable cause) {
		super (JOPException.toMessage(BusinessException.class, checkErrorCode(errorCode),msgParam), cause);
		setErrorCode(errorCode);
	}

	public InvalidIdException(String errorCode, String msgParam, Throwable cause) {
		super (JOPException.toMessage(BusinessException.class, checkErrorCode(errorCode), msgParam), cause);
		setErrorCode(errorCode);
	}

	protected static String checkErrorCode(String errorCode){
		return (errorCode==null)?ERROR_CODE_DB_INVALIDID:errorCode;
	}
}
