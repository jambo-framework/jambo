package com.jambo.jop.ui.tag.component;

import com.jambo.jop.common.utils.lang.Code2NameUtils;
import com.jambo.jop.infrastructure.config.CoreConfigInfo;
import com.jambo.jop.infrastructure.db.DBQueryParam;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 
 * @author He Kun
 * @version 2010-10-13 添加selectorMaxNum，orderby属性
 * 
 */
public class Selector {//extends Select {

	protected String definition;
	protected String condition; // 过滤器,只保留指定条件的值
	protected String dbFlag;
	protected String selectorMaxNum;// 下拉框最大数量
	protected String showonly;
	protected String nameOnly;
	protected String readonly = "true";
	protected String headOption; // 下拉框的第一个Option <optionvalue="">headOption</option>
	protected String orderby; // 排序属性，对应DBParam里面的_orderby

//	public Selector(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
//		super(stack, request, response);
//	}

	public String getOrderby() {
		return orderby;
	}

	public void setOrderby(String orderby) {
		this.orderby = orderby;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getSelectorMaxNum() {
		return selectorMaxNum;
	}

	public void setSelectorMaxNum(String selectorMaxNum) {
		this.selectorMaxNum = selectorMaxNum;
	}

	public String getDbFlag() {
		return dbFlag;
	}

	public void setDbFlag(String dbFlag) {
		this.dbFlag = dbFlag;
	}

	public String getDefinition() {
		return definition;
	}

	public void setDefinition(String definition) {
		this.definition = definition;
	}

	public String getNameOnly() {
		return nameOnly;
	}

	public void setNameOnly(String nameOnly) {
		this.nameOnly = nameOnly;
	}

	public String getShowonly() {
		return showonly;
	}

	public void setShowonly(String showonly) {
		this.showonly = showonly;
	}

	public String getReadonly() {
		return readonly;
	}

	public void setReadonly(String readonly) {
		this.readonly = readonly;
	}

	public String getHeadOption() {
		return headOption;
	}

	public void setHeadOption(String headOption) {
		this.headOption = headOption;
	}

    /*
	public void evaluateExtraParams() {
		super.evaluateExtraParams();

		if (readonly != null) {
			addParameter("readonly", getStack().findValue(readonly, Boolean.class));
		}
		if (listKey == null) {
			addParameter("listKey", "key");
		}

		if (listValue == null) {
			addParameter("listValue", "value");
		}
	}

	public void evaluateParams() {
		// 先计算 list 属性的值，及下拉框的内容，再调用super的evaluateParams方法

		setList(buildMapList());

		super.evaluateParams();
	}
	*/

	private String conditionValue; // 过滤器,只保留指定条件的值
	private String dbFlagValue;
//	private String showonlyValue; // 控制显示方式时使用
//	private String nameOnlyValue;
//	private String readonlyValue = "true";
	private String selectorMaxNumValue = "30";
	private String orderbyValue;

	/**
	 * 计算主要参数的值，以便根据其查询数据
	 * 
	 */
	protected void evaluateQueryParams() {
		// 先按表达式来查询，无结果时沿用当前值

		if (condition != null) {
//			conditionValue = getStack().findString(condition);
			if (conditionValue == null) {
				conditionValue = condition;
			}
		}

		if (dbFlag != null) {
//			dbFlagValue = getStack().findString(dbFlag);
			if (dbFlagValue == null) {
				dbFlagValue = dbFlag;
			}
		} else {
			dbFlagValue = CoreConfigInfo.COMMON_DB_NAME;
		}

//		if (showonly != null) {
//			showonlyValue = getStack().findString(showonly);
//			if (showonlyValue == null) {
//				showonlyValue = showonly;
//			}
//		}
//
//		if (nameOnly != null) {
//			nameOnlyValue = getStack().findString(nameOnly);
//			if (nameOnlyValue == null) {
//				nameOnlyValue = nameOnly;
//			}
//		}
//
//		if (readonly != null) {
//			readonlyValue = getStack().findString(readonly);
//			if (readonlyValue == null) {
//				readonlyValue = readonly;
//			}
//		}

		if (selectorMaxNum != null) {
//			selectorMaxNumValue = getStack().findString(selectorMaxNum);
			if (selectorMaxNumValue == null) {
				selectorMaxNumValue = selectorMaxNum;
			}
		}

		if (orderby != null) {
//			orderbyValue = getStack().findString(orderby);
			if (orderbyValue == null) {
				orderbyValue = orderby;
			}
		}
	}

	/**
	 * 根据 definition，condition等参数计算list数据
	 * 
	 * @return
	 */
	protected Map buildMapList() {
		evaluateQueryParams();

		try {
			// 使用Cache拦截器管理缓存，拦截器配置在 WebViewInterceptorHandler 中，参见 /jop-aop.xml
			DBQueryParam param = new DBQueryParam();
			param.set_pagesize(selectorMaxNumValue);
			if (orderbyValue != null) {
				param.set_orderby(orderbyValue);
			}

			Map map = Code2NameUtils.valueList(definition, conditionValue, param, dbFlagValue);
			Map newMap = new LinkedHashMap();
			if (headOption == null) {
				newMap.put(null, null); // 加一个空白项
			} else {
				newMap.put(null, headOption);// 添加一个默认的Option：如，请选择....
			}
			newMap.putAll(map);
			return newMap;
		} catch (Exception e) {
			e.printStackTrace();
			return new HashMap();
		}
	}

}
