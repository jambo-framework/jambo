package com.jambo.jop.exception.business;

import com.jambo.jop.exception.JOPException;

/**
 * <p>
 * Title: 分销业务错误信息
 * </p>
 * <p>
 * Description: 分销业务办理失败情况下的错误信息
 * </p>
 * <p>
 * Copyright: Copyright (c) 2009
 * </p>
 * <p>
 * Company: jambo-framework Tech Ltd.
 * </p>
 * 
 * @author Zhang Fengchao
 * @version 1.0
 */
public class SaleException extends RuntimeException {
	private static final long serialVersionUID = 4800793721453063275L;

	public static final String ERROR_CODE_SALE = "business.sale";

	private String errorCode = ERROR_CODE_SALE;

	private String timesect; // 订购时间段

	private String comcode; // 商品种类编码

	private String brandcode; // 品牌编码

	private String brandname; // 品牌名称

	private Long orderamt; // 订购套数

	private Long unitage; // 订购基数

	private Long maxamt; // 订购上限

	private String compname; // 分公司名称

	private String cooptypename; // 合作类型名称

	/**
	 * 构造方法 简单异常提示，只提供错误码即可
	 * 
	 * @param errorCode
	 *            错误码
	 */
	public SaleException(String errorCode) {
		super(JOPException.toMessage(SaleException.class,
				checkErrorCode(errorCode), ""));
		setErrorCode(errorCode);
	}

	/**
	 * 构造方法 针对需要替换内容的异常提示，需要填写错误码和错误参数（单个）
	 * 
	 * @param errorCode
	 *            错误码
	 * @param msgParam
	 *            错误参数值，即待替换内容
	 */
	public SaleException(String errorCode, String msgParam) {
		super(JOPException.toMessage(SaleException.class,
				checkErrorCode(errorCode), msgParam));
		setErrorCode(errorCode);
	}

	/**
	 * 构造方法 针对需要替换内容的异常提示，需要填写错误码和错误参数（多个）
	 * 
	 * @param errorCode
	 *            错误码
	 * @param msgParam
	 *            错误参数值，即待替换内容
	 */
	public SaleException(String errorCode, String[] msgParam) {
		super(JOPException.toMessage(SaleException.class,
				checkErrorCode(errorCode), msgParam));
		setErrorCode(errorCode);
	}

	/**
	 * 错误码检查
	 * 
	 * @param errorCode
	 *            错误码
	 * @return
	 */
	protected static String checkErrorCode(String errorCode) {
		return (errorCode == null) ? ERROR_CODE_SALE : errorCode;
	}

	public Long getUnitage() {
		return unitage;
	}

	public void setUnitage(Long unitage) {
		this.unitage = unitage;
	}

	public String getBrandcode() {
		return brandcode;
	}

	public void setBrandcode(String brandcode) {
		this.brandcode = brandcode;
	}

	public String getBrandname() {
		return brandname;
	}

	public void setBrandname(String brandname) {
		this.brandname = brandname;
	}

	public String getComcode() {
		return comcode;
	}

	public void setComcode(String comcode) {
		this.comcode = comcode;
	}

	public String getCompname() {
		return compname;
	}

	public void setCompname(String compname) {
		this.compname = compname;
	}

	public String getCooptypename() {
		return cooptypename;
	}

	public void setCooptypename(String cooptypename) {
		this.cooptypename = cooptypename;
	}

	public Long getMaxamt() {
		return maxamt;
	}

	public void setMaxamt(Long maxamt) {
		this.maxamt = maxamt;
	}

	public Long getOrderamt() {
		return orderamt;
	}

	public void setOrderamt(Long orderamt) {
		this.orderamt = orderamt;
	}

	public String getTimesect() {
		return timesect;
	}

	public void setTimesect(String timesect) {
		this.timesect = timesect;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
}