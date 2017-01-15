package com.jambo.jop.exception;

/**
 * 业务异常,因为i18n的原因，所有的业务异常使用代码标记<br/>
 * 错误代码进行分段管理,格式如下:BUSI-10001,其中前四位为业务模块代码，后五位为数字.<br/>
 * 为了便于区分后面５位的数字不重复,需要规划分段.前两位用于分段.具体规划交由业务组来规划<br/>
 * 如:COM-100001 : COM表示公共 10用于公共.
 *    RES-100001 :　RES表示资源，10为内部规范编号。
 * @author $Author:JinBo $</br>
 * @version $ReVersion: 1.0 JinBo Exp at 2007-1-18 9:44:11 $</br>
 * @since 1.0
 * @see com.jambo.jop.JopException
 */
public class BusinessException extends RuntimeException {
	private String errorCode = ERROR_CODE_BUSINESS_COMMON;

	public static final String ERROR_CODE_BUSINESS_COMMON = "business.common";
	/* 异常唯一标记 */
//	private String sid = "";

	/**
	 * @param errorCode  错误代码
	 */
	public BusinessException(String errorCode) {
		super (JOPException.toMessage(BusinessException.class, checkErrorCode(errorCode), ""));
		setErrorCode(errorCode);
	}
	
	public BusinessException(String message, Throwable cause) {
		super(message, cause);
		setErrorCode(ERROR_CODE_BUSINESS_COMMON);
	}

	public BusinessException(String errorCode, String[] msgParam, Throwable cause) {
		super (JOPException.toMessage(BusinessException.class, checkErrorCode(errorCode),msgParam), cause);
		setErrorCode(errorCode);
	}

	public BusinessException(String errorCode, String msgParam, Throwable cause) {
		super (JOPException.toMessage(BusinessException.class, checkErrorCode(errorCode), msgParam), cause);
		setErrorCode(errorCode);
	}

	protected static String checkErrorCode(String errorCode){
		return (errorCode==null)?ERROR_CODE_BUSINESS_COMMON:errorCode;
	}
	

	public String getCode() {
		return errorCode;
	}

	/**
	 * 异常唯一标记 使用系统时间加errorCode
	 * @return
	 */
//	public String Sid() {
//		return sid;
//	}

	public String getErrorCode() {
		return errorCode;
	}

	protected void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
//		this.sid = System.currentTimeMillis() + "-" + errorCode;
	}
}