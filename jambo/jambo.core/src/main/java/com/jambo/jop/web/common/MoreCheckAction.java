package com.jambo.jop.web.common;

import com.jambo.jop.common.utils.lang.Code2NameUtils;
import com.jambo.jop.common.utils.lang.InterfaceUtils;
import com.jambo.jop.infrastructure.component.Code2Name;
import com.jambo.jop.infrastructure.component.impl.Node;
import com.jambo.jop.infrastructure.db.DBQueryParam;
import com.jambo.jop.infrastructure.db.DataPackage;
import com.jambo.jop.ui.spring.BaseAction;
import org.apache.commons.lang.StringUtils;

import java.util.Iterator;

/**
 * MoreCheck多选框,用来同时选择多个code2name
 * 没用起来，作废
 * @author Canigar
 * 
 */
public class MoreCheckAction extends BaseAction{

	public String doList() throws Exception {
		try{
//			if(param== null) {
//				param = new DBQueryParam();
//				param.set_pageno("1");
//				param.set_pagesize("15");
//			}
			
			/*** 用于记录下是否是翻页选择（是否是第一次弹出框） ***/
			if(!isFirst()){
				/** 设置标志 **/
				setFirst(true);
				
				/*** 记录下初始的code 和 name ***/
				setInitcode(getCode());
				setInitname(getName());
				
				setInitcondition(getCondition());
				/*** FullCode跟FullName填充 ***/
				getFull();
			}
			
			//使用Cache拦截器管理缓存，拦截器配置在 WebViewInterceptorHandler 中，参见 /jop-aop.xml 
			Code2Name comp = (Code2Name)InterfaceUtils.getInstance().createObject(Code2Name.class); //主要针对单表型的编码列表		
//			DBAccessUser user = getDBAccessUser();
			String queryDBFlag = getDBAccessUser().getDbFlag();
			if(StringUtils.isNotBlank(dbFlag)) {
				queryDBFlag = dbFlag;
			}
			if(definition.startsWith("$")){
				;	//直接将字典表用picker形式展现
			}else if(definition.startsWith("#")){
				;
			}else{
				definition = "#"+definition;
			}
			
			condition = initcondition;
			if(StringUtils.isNotBlank(queryCode))	//添加用户输入的code，name作为新的查询条件。
				condition = condition + ";" + "CODE:" + queryCode;
			if(StringUtils.isNotBlank(queryName))
				condition = condition + ";" + "NAME:" + queryName;
			if(condition.startsWith(";"))
				condition = condition.substring(1);
//			DataPackage dp = comp.valueListPackage(definition, condition, param, queryDBFlag);
//			setDp(dp);
				
		}catch (Exception e) {
			e.printStackTrace();
		}
		return "morecheck";
	}
	
	/*** 获取多选中 每一个code和code对用的name 。例如： 1，2，3 和 A，B，C， ***/
	private void getFull() throws Exception{
		
		StringBuffer fullcode = new StringBuffer("");
		StringBuffer fullname = new StringBuffer("");
		// TODO Auto-generated method stub
		DBQueryParam param = new DBQueryParam();
		param.set_pageno("1");
		param.set_pagesize("10000");
		
//		DBAccessUser user = getDBAccessUser();
		String queryDBFlag = getDBAccessUser().getDbFlag();
		if(StringUtils.isNotBlank(dbFlag)) {
			queryDBFlag = dbFlag;
		}
		
		DataPackage dp = Code2NameUtils.valueListPackage(definition, condition, param, queryDBFlag);
		
		if(dp.getDatas() != null && dp.getDatas().size() != 0){
			Iterator itt = dp.getDatas().iterator();
			while(itt.hasNext()){
				Node node = (Node)itt.next();
				fullcode.append(node.getCode()).append(",");
				fullname.append(node.getName()).append(",");
			}
		}
		
		setFullcode(fullcode.toString());
		setFullname(fullname.toString());
		
		setAllcode(getFullcode());
		setAllname(getFullname());
	}
	
	
	

	private String queryCode;
	private String queryName;

	private String code ;	
	private String name ;
	private String definition;	
	private String condition ;	
	private String initcondition;
	private String property;
	private String dbFlag;
	private String allname;
	private String allcode;
	private String fullname;
	private String fullcode;
	private String initcode;
	private String initname;
	private String uncheckcode;
	private String uncheckname;
	private boolean first;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
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
	public String getProperty() {
		return property;
	}
	public void setProperty(String property) {
		this.property = property;
	}
	public String getDbFlag() {
		return dbFlag;
	}
	public void setDbFlag(String dbFlag) {
		this.dbFlag = dbFlag;
	}
	public String getAllname() {
		return allname;
	}
	public void setAllname(String allname) {
		this.allname = allname;
	}
	public String getAllcode() {
		return allcode;
	}
	public void setAllcode(String allcode) {
		this.allcode = allcode;
	}
	public String getFullname() {
		return fullname;
	}
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
	public String getFullcode() {
		return fullcode;
	}
	public void setFullcode(String fullcode) {
		this.fullcode = fullcode;
	}
	public String getInitcode() {
		return initcode;
	}
	public void setInitcode(String initcode) {
		this.initcode = initcode;
	}
	public String getInitname() {
		return initname;
	}
	public void setInitname(String initname) {
		this.initname = initname;
	}
	public String getUncheckcode() {
		return uncheckcode;
	}
	public void setUncheckcode(String uncheckcode) {
		this.uncheckcode = uncheckcode;
	}
	public String getUncheckname() {
		return uncheckname;
	}
	public void setUncheckname(String uncheckname) {
		this.uncheckname = uncheckname;
	}
	public boolean isFirst() {
		return first;
	}
	public void setFirst(boolean first) {
		this.first = first;
	}

	public String getQueryCode() {
		return queryCode;
	}

	public void setQueryCode(String queryCode) {
		this.queryCode = queryCode;
	}

	public String getQueryName() {
		return queryName;
	}

	public void setQueryName(String queryName) {
		this.queryName = queryName;
	}

	public String getInitcondition() {
		return initcondition;
	}

	public void setInitcondition(String initcondition) {
		this.initcondition = initcondition;
	}
}
