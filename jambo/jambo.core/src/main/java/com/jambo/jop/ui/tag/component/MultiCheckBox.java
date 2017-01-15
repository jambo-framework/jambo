package com.jambo.jop.ui.tag.component;

import com.jambo.jop.common.utils.lang.Code2NameUtils;
import com.jambo.jop.infrastructure.db.BaseDAO;
import com.jambo.jop.infrastructure.db.DBQueryParam;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * FreeMaker File: multicheckbox.ftl
 * @author Canigar
 *
 */
public class MultiCheckBox extends CheckboxList{

	protected String randomCode; //生成页面selectAll等js随机码
	protected String definition;
	protected String condition; // 过滤器,只保留指定条件的值
	protected String dbFlag;
	
	private String conditionValue; // 过滤器,只保留指定条件的值
	private String dbFlagValue;

    /*
	public MultiCheckBox(ValueStack stack, HttpServletRequest request,
			HttpServletResponse response) {
		super(stack, request, response);
	}
	
	public void evaluateParams() {
		setList(buildMapList());
		super.evaluateParams();
	
	}
	
	public void evaluateExtraParams() {
		super.evaluateExtraParams();
		if (listKey == null) {
			addParameter("listKey", "key");
		}
		if (listValue == null) {
			addParameter("listValue", "value");
		}
		StringBuffer sb = new StringBuffer();
		Random r = new Random();
		for(int i=0;i<10;i++){
			sb.append(r.nextInt(10));
		}
		addParameter("randomCode", sb.toString());
	}
	*/
	
	/**
	 * 根据 definition，condition等参数计算list数据
	 * 
	 * @return
	 */
	protected Map buildMapList() {
//		evaluateQueryParams();
		try {
			// 使用Cache拦截器管理缓存，拦截器配置在 WebViewInterceptorHandler 中，参见 /jop-aop.xml
			DBQueryParam param = new DBQueryParam();
			param.setQueryType(BaseDAO.QUERY_TYPE_COUNT_AND_DATA);
			Map map = Code2NameUtils.valueList(definition, conditionValue, param, dbFlagValue);
			Map newMap = new LinkedHashMap();
			newMap.putAll(map);
			return newMap;
		} catch (Exception e) {
			e.printStackTrace();
			return new HashMap();
		}
	}
	
	/**
	 * 计算主要参数的值，以便根据其查询数据
	 * 
	 */
    /*
	protected void evaluateQueryParams() {
		if (condition != null) {
			conditionValue = getStack().findString(condition);
			if(conditionValue == null)
				conditionValue = condition;
		}
			
		if (dbFlag != null) {
			dbFlagValue = getStack().findString(dbFlag);
			if (dbFlagValue == null) {
				dbFlagValue = dbFlag;
			}
		} else {
			dbFlagValue = CoreConfigInfo.COMMON_DB_NAME;
		}
			
	}
	@Override
	protected String getDefaultTemplate() {
		return "multicheckbox";
	}
	                  */

	public String getDefinition() {
		return definition;
	}

	public void setDefinition(String definition) {
		this.definition = definition;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getDbFlag() {
		return dbFlag;
	}

	public void setDbFlag(String dbFlag) {
		this.dbFlag = dbFlag;
	}

	public String getRandomCode() {
		return randomCode;
	}

	public void setRandomCode(String randomCode) {
		this.randomCode = randomCode;
	}

}
