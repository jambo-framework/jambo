package com.jambo.jop.ui.tag;


import com.jambo.jop.common.utils.lang.Code2NameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

/**
 * Title:自定义标签
 * 
 * Description: 翻译标签,从标准标签改到struts2,再改回来
 * Copyright: Copyright (c) 2006
 * Company: jambo-framework Tech Ltd.
 * @author Huang BaiMing， He Kun, JinBo
 * @version 3.0
 */
public class Code2NameTag extends BaseTag {

	private static final long serialVersionUID = 5307581419270193600L;

	private static Logger log = LogManager.getLogger(Code2NameTag.class);

	static public final String SYSCODE_FLAG = "$";

	static public final String CONFIG_FLAG = "#";

	protected String definition;
    protected String code;
    protected String split;

    //    public Component getBean(ValueStack stack, HttpServletRequest req, HttpServletResponse res) {
//    	Code2NameCom code2NameCom =  new Code2NameCom(stack);
//    	Class clazz = InterfaceUtils.getInstance().getImplClass(UserProvider.class);
//    	UserProvider provider;
//		try {
//			provider = (UserProvider)clazz.newInstance();
//			DBAccessUser user = (DBAccessUser)provider.getUser();// req.getSession().getAttribute(WebConstant.SESSION_ATTRIBUTE_USER);
//	    	code2NameCom.setUser(user);
//	    	return code2NameCom;
//		} catch (Exception e) {
//			throw new IllegalArgumentException("Can't find or instant UserProvider's impl class! ");
//		}
//    }
//
//    /**
//     * 填充主要属性
//     */
//    protected void populateParams() {
//    	super.populateParams();
//    	Code2NameCom d = (Code2NameCom)component;
//    	d.setDefinition(definition);
//    	d.setCode(code);
//    	d.setDbFlag(dbFlag);
//    	d.setSplit(split);
//    }

	public void setDefinition(String definition) {
		this.definition = definition;
	}

	public void setCode(String code) {
		this.code = code;
	}

    public String getSplit() {
		return split;
	}

	public void setSplit(String split) {
		this.split = split;
	}

    public int doStartTag() throws JspException{
        if (definition != null && definition.trim().length() > 0 && code != null && code.trim().length() > 0) {

//            String codeValue = (String) getStack().findValue(code, String.class);

            // 如果没找到codeValue就没必要进行转换
            if (code == null) {
                return EVAL_PAGE;
            }

            String dbFlagValue = getDbFlag();

            String[] codeValues;
            try {
                // 添加了split字段对codeValue进行分割,对分割的字符进行code2name
                if (split != null) {
                    codeValues = StringUtils.split(code, split);
                } else {
                    codeValues = new String[] { code };
                }
                StringBuffer codeNames = new StringBuffer();
                for (int i = 0; i < codeValues.length; i++) {
                    // 做request生命周期内的缓存--start-------
                    HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
                    String codeName = (String)request.getAttribute("JOPCode2Name:" + definition + codeValues[i]);
                    if (codeName == null) {
                        codeName = Code2NameUtils.code2Name(definition, codeValues[i], dbFlagValue);
                        request.setAttribute("JOPCode2Name:" + definition + codeValues[i], codeName);
                    }
                    // 做request生命周期内的缓存--end---------

                    if (codeName == null) {
                        codeName = codeValues[i];
                    }
                    codeNames.append(codeName);
                    if (split != null && i != codeValues.length - 1) {
                        codeNames.append(split);
                    }
                }

                getTagWriter().print(codeNames.toString());
            } catch (Exception e) {
                if (log.isErrorEnabled())
                    log.catching(e);
                return EVAL_PAGE;
            }
        }
        return EVAL_PAGE;
    }

    @Override
    public int doEndTag() throws JspException {
        return EVAL_PAGE;

    }}
