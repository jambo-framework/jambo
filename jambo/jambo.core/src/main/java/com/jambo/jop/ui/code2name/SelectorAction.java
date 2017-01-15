package com.jambo.jop.ui.code2name;

import com.jambo.jop.common.utils.lang.Code2NameUtils;
import com.jambo.jop.common.utils.lang.PublicUtils;
import com.jambo.jop.infrastructure.config.CoreConfigInfo;
import com.jambo.jop.infrastructure.db.DBQueryParam;
import com.jambo.jop.infrastructure.db.DataPackage;
import com.jambo.jop.ui.spring.BaseAction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Code2Name控制层
 * @author jinbo
 */
@Controller
@RequestMapping("/code2name/selector/")
public class SelectorAction extends BaseAction {
	private final static String PAGE_URL = "/code2name/selector/qryList.do";

	@RequestMapping("/code2name")
	@ResponseBody
	public Map<String, String>  code2name(String def, String code, HttpServletRequest request){
		Map<String, String> map = new HashMap();
		String name = Code2NameUtils.code2Name(def, code, CoreConfigInfo.COMMON_DB_NAME);
		if(!PublicUtils.isBlankString(name)){
			map.put("namevalue", name);
		}
		return map;
	}

	/**
	 * 根据def查询所有的字典数据，用于下拉框，限制数量为50
	 * @param def 字典组id
	 * @param request 请求对象
	 * @return 返回MAP
	 */
	@RequestMapping("/qrymap")
	@ResponseBody
	public Map<String, String> qryMap(String def, String condition, HttpServletRequest request){
		return Code2NameUtils.valueList(def, condition);
	}

	@RequestMapping("/qryList")
	@ResponseBody
	public DataPackage queryList(HttpServletRequest request, HttpServletResponse response) {
		String code2namePageNo = request.getParameter("code2namePageNo");
		String code2nameQryCode = request.getParameter("code2nameQryCode");
		String code2nameQryName = request.getParameter("code2nameQryName");
		String code2nameQryGroup = request.getParameter("code2nameQryGroup");
		String code2nameCondition = request.getParameter("code2nameCondition");

		DBQueryParam param = new DBQueryParam();

		if (!PublicUtils.isInteger(code2namePageNo)) {
			param.set_pageno("1");
		} else{
			param.set_pageno(code2namePageNo);
		}

		String condition = Code2NameUtils.castConditionValue(code2nameQryGroup, code2nameCondition, code2nameQryCode, code2nameQryName);

		return Code2NameUtils.valueListPackage(code2nameQryGroup, condition, param, CoreConfigInfo.COMMON_DB_NAME);
	}
}
