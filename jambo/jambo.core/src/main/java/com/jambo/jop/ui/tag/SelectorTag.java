package com.jambo.jop.ui.tag;

import com.jambo.jop.common.utils.i18n.I18nMessage;
import com.jambo.jop.common.utils.lang.Code2NameUtils;
import com.jambo.jop.infrastructure.db.DBQueryParam;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * User: He Kun, jinbo
 * Date: 2006-8-18
 * Time: 15:34:32
 * @version 2010-10-13 添加selectorMaxNum和orderby属性
 * 
 */
public class SelectorTag  extends BaseTag {
    private static Logger log = LogManager.getLogger(Code2NameTag.class);

	protected String definition;    
	protected String condition;  //过滤器,只保留指定条件的值    
	protected String dbFlag;    
    protected String showonly;    
    protected String nameOnly;    
    protected String readonly ;
    protected String headOption; // 下拉框的第一个Option <option value="">headOption</option>
    protected String mode;  // 选择模式 picker为弹出框 selector为下拉框 morecheck为多选框
    
    protected String selectorMaxNum; //查询最大值
    protected String orderby; //排序属性，对应DBParam里面的orderby

    protected String btnClass;

    public String getBtnClass() {
        return btnClass;
    }

    public void setBtnClass(String btnClass) {
        this.btnClass = btnClass;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    protected String value;

    public void setOrderby(String orderby) {
		this.orderby = orderby;
	}

	public void setCondition(String condition) {
		this.condition = condition;		
	}

	public void setDbFlag(String dbFlag) {
		this.dbFlag = dbFlag;
	}

	public void setSelectorMaxNum(String selectorMaxNum) {
		this.selectorMaxNum = selectorMaxNum;
	}

	public void setDefinition(String definition) {
		this.definition = definition;
	}

	public void setNameOnly(String nameOnly) {
		this.nameOnly = nameOnly;
	}

	public void setReadonly(String readonly) {
		this.readonly = readonly;
	}

	public void setShowonly(String showonly) {
		this.showonly = showonly;
	}
	
	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public void setHeadOption(String headOption) {
		this.headOption = headOption;
	}

    public int doEndTag() throws JspException {
        try {
            if (mode == null) {
                //动态数据，使用对话框
                if (definition.startsWith("#")) {
                    doPickerDialog();
                } else { //数据字段和固定参数 使用 select 控件
                    doSelector();
                }
            } else if (mode.equalsIgnoreCase("picker")) {
                doPickerDialog();
            } else if (mode.equalsIgnoreCase("selector")) {
                doSelector();
            } else if (mode.equalsIgnoreCase("morecheck")) {
                doMoreCheckDialog();
            } else if (mode.equalsIgnoreCase("multicheckbox")) {
                doMultiCheckBox();
            }
        } catch (IOException e) {
            log.catching(e);
            throw  new JspException(e);
        }

        return  EVAL_PAGE;
    }

    private void doSelector() throws JspTagException, IOException {

        results = new StringBuffer("<select");
        addParamterValue("name", name, null);
        addParamterValue("id", name, null);
        addParamterValue("class", cssClass, null);
        addParamterValue("style", style, null);
        results.append(">");

        getTagWriter().println(results);
        Map map = buildMapList();
        for(Object dataKey : map.keySet()) {
            boolean matched = false;
            if (dataKey != null){
                matched = dataKey.equals(value);
            }
            writeOption(dataKey, map.get(dataKey), matched);
        }
        getTagWriter().println("</select>");
        results = null;
    }

    private void writeOption(Object code, Object ob, boolean ismatch)
            throws JspTagException, IOException {
        StringBuffer sb = new StringBuffer();
        String name = "";
        if (ob != null) {
            name = ob.toString();
        }
        if (code == null)
            sb.append("<option value=\"\" ").append(">");
        else
            sb.append("<option value=\"").append(code).append("\" ").append(
                    (ismatch ? "selected" : "")).append(">");
        if (this.nameOnly != null && this.nameOnly.equalsIgnoreCase("false")) {
            sb.append(code).append("\t");
        }
        sb.append(name).append("</option>");
        getTagWriter().println(sb.toString());
    }

    /**
     * 根据 definition，condition等参数计算list数据
     *
     * @return
     */
    protected Map buildMapList() {
        try {
            // 使用Cache拦截器管理缓存，拦截器配置在 WebViewInterceptorHandler 中，参见 /jop-aop.xml
            DBQueryParam param = new DBQueryParam();
            if (selectorMaxNum == null){
                selectorMaxNum = "30";
            }
            param.set_pagesize(selectorMaxNum);
            if (orderby != null) {
                param.set_orderby(orderby);
            }

            Map map = Code2NameUtils.valueList(definition, condition, param, getDbFlag());
            Map newMap = new LinkedHashMap();
            if (headOption == null) {
                newMap.put(null, ""); // 加一个空白项
            } else {
                newMap.put(null, headOption);// 添加一个默认的Option：如，请选择....
            }
            newMap.putAll(map);
            return newMap;
        } catch (Exception e) {
            log.catching(e);
            return new HashMap();
        }
    }

    protected void addParamterValue(String field, String value, String suffix){
        if(value != null){
            results.append(" ").append(field).append("=\"").append(value);
            if (suffix != null){
                results.append(suffix);
            }
            results.append("\" ");
        }
    }

    private void doPickerDialog() throws IOException {
        //隐藏框,用来保存CODE
        results = new StringBuffer("<input type=\"hidden\" ");
        addParamterValue("name", name, null);
        addParamterValue("id", name, null);
        addParamterValue("value", value, null);
        results.append("/>");

        //文本框,用来显示NAME
        results.append("<input type=\"text\"");
        addParamterValue("name", name, "_text");
        addParamterValue("id", name, "_text");
        addParamterValue("style", style, null);
        addParamterValue("class", cssClass, null);
        addParamterValue("value", evalueValue(), null);

        if(readonly != null && readonly.toLowerCase().equals("true")){
            addParamterValue("readonly", readonly, null);
        }
        results.append("/>");

        //选择按钮
        results.append("<input type=\"button\"");
        addParamterValue("name", name, "_button");
        addParamterValue("id", name, "_button");
        addParamterValue("class", btnClass, null);
        if(readonly != null && readonly.toLowerCase().equals("true")){
            addParamterValue("disabled", readonly, null);
        }

        results.append(" value=\"").append(I18nMessage.getPublicString("button_select")).append("\" onclick=\"javascript:openPicker(this,'")
               .append(definition).append("','").append(condition).append("'");

        if (dbFlag != null) {
            results.append(" ,dbFlag='").append(dbFlag).append("'");
        }
        results.append(");\"/>");

        getTagWriter().println(results.toString());
        results = null;
    }

    protected String evalueValue() {
        String codeName = "";

        if( value !=null) {
            //将编码转换为名称， 作为 nameText 显示在文本框中
            try {
                //使用Cache拦截器管理缓存，拦截器配置在 WebViewInterceptorHandler 中，参见 /jop-aop.xml
//                if(dbFlag == null) dbFlag = CoreConfigInfo.COMMON_DB_NAME;
                codeName = Code2NameUtils.code2Name(definition, value, getDbFlag());
            } catch (Exception e) {
                log.error("picker [" + name + "] value text ：" + value);
                log.catching(e);
            }
        }
        return codeName;
    }

    private void doMultiCheckBox() {
        //To change body of created methods use File | Settings | File Templates.
    }

    private void doMoreCheckDialog() {
        //To change body of created methods use File | Settings | File Templates.
    }
}
