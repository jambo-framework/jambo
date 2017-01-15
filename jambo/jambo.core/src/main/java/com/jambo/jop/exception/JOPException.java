package com.jambo.jop.exception;

import com.jambo.jop.common.utils.i18n.I18nMessage;
import com.jambo.jop.infrastructure.config.CoreConfigInfo;
import org.apache.commons.lang.ClassUtils;

/**
 * $Id: JOPException.java JinBo Exp at 2007-1-18 13:51:37 $<br/>
 * 这种异常主要是指框架本身导致的错误，和一些内层错误引起的异常
 * 系统异常,运行时异常,因为i18n的原因，所有的系统异常使用代码标记<br/> 
 * 错误代码进行分段管理,格式如下:NET-10001,其中前段为分类代码，后五位为数字.<br/> 
 * 为了便于区分后面5位的数字不重复,需要规划分段.前两位用于分段.<br/>
 * 如:DB-00001 :DB表示数据库相关异常 DB用于公共.
 * @author $Author:JinBo $</br>
 * @version $ReVersion: 1.0$</br>
 * @since  1.0
 * @see com.jambo.jop.BusinessException
 */
public class JOPException extends RuntimeException {
	/* 异常唯一标记 */
//	private String sid = "";
	private String errorCode = ERROR_CODE_JOP_COMMON;

	public static final String ERROR_CODE_JOP_COMMON = "jop.common";
	/**
	 * @param errorCode  错误代码
	 */
	
	public JOPException(String errorCode) {
		super (toMessage(JOPException.class, checkErrorCode(errorCode), ""));
		setErrorCode(errorCode);
	}
	
	public JOPException(String message, Throwable cause) {
		super(message, cause);
		setErrorCode(ERROR_CODE_JOP_COMMON);
	}

	public JOPException(String errorCode, String[] msgParam, Throwable cause) {
		super (toMessage(JOPException.class, checkErrorCode(errorCode),msgParam), cause);
		setErrorCode(errorCode);
	}

	public JOPException(String errorCode, String msgParam, Throwable cause) {
		super (toMessage(JOPException.class, checkErrorCode(errorCode), msgParam), cause);
		setErrorCode(errorCode);
	}

	public JOPException(Exception ex) {
		// TODO Auto-generated constructor stub
		super(ex);
	}

	protected static String checkErrorCode(String errorCode){
		return (errorCode==null)?ERROR_CODE_JOP_COMMON:errorCode;
	}
	
	/**
	 * @return 错误代码
	 */
	public String getCode() {
		return errorCode;
	}

	/**
	 * 
	 * @return 异常唯一标记 使用系统时间加errorCode
	 */
//	public String Sid() {
//		return sid;
//	}
	
	protected void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
//		this.sid = errorCode + "-" + System.currentTimeMillis();
	}
	
	public String getErrorCode() {
		return errorCode;
	}

	public static String toMessage(Class cls, String errorCode, String[] msgParam){
		if(errorCode == null) return null;
		String[] param = new String[msgParam.length + 1];
		System.arraycopy(msgParam, 0, param, 1, msgParam.length);
		param[0] = errorCode;
		StringBuffer resname = new StringBuffer(CoreConfigInfo.I18N_ROOT_PATH).append(".exception.").append(ClassUtils.getShortClassName(cls));
		return I18nMessage.getString(resname.toString(), errorCode, param);
	}
	
	public static String toMessage(Class cls, String errorCode, String msgParam){
		String[] param = {msgParam}; 
		return toMessage(cls, errorCode, param);
	}
	
}