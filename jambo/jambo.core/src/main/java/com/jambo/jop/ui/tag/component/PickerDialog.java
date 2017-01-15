package com.jambo.jop.ui.tag.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author He Kun
 *
 */
public class PickerDialog {//extends UIBean {
	
	private static Logger log = LoggerFactory.getLogger(PickerDialog.class);
	
	 final public static String TEMPLATE = "picker";
	
	protected String definition;    
	protected String condition;  //过滤器,只保留指定条件的值    
	protected String  dbFlag;    
	protected String readonly ;
    
//	public PickerDialog(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
//		super(stack, request, response);
//	}

	protected String getDefaultTemplate() {
		return TEMPLATE;
	}

	public static String getTEMPLATE() {
		return TEMPLATE;
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

	public String getDefinition() {
		return definition;
	}

	public void setDefinition(String definition) {
		this.definition = definition;
	}

	public String getReadonly() {
		return readonly;
	}

	public void setReadonly(String readonly) {
		this.readonly = readonly;
	}

    /*
	public void evaluateExtraParams() {	
		super.evaluateExtraParams();
		
		addParameter("definition",getDefinition());
		//计算自定义表达式的值//以下参数不需要添加到参数包中
		if(condition!=null) {
			String con = (String) getStack().findString( condition);
			if(con == null)
				con = condition;
			addParameter("condition", con);
		}else
			addParameter("condition", "");
		
		if(dbFlag!=null) {
			String dbFlag0 = (String)getStack().findValue( dbFlag ,String.class);
			if( dbFlag0 == null)
				dbFlag0 = dbFlag;
			addParameter("dbFlag", dbFlag0);
		}else
			addParameter("dbFlag", "");
		
		//以下参数需要添加到参数包中，控制输出效果
		if(readonly!=null) {
			addParameter("readonly", getStack().findValue( readonly ,Boolean.class));
		}
	    
		if(title!=null){
			String str = (String) getStack().findString(title);
			if(str==null){
				str = title;
			}
			addParameter("title", str);
		}else{
			addParameter("title", "");
		}
		
		evalueValue();
		
	}
	
	protected void evalueValue() {
		String valueValue = null;
		if(value == null) {
			//尝试根据 name属性计算值
			String name0 = (String)parameters.get("name");
			if( name0 !=null) {
				String vvv = (String)getStack().findString( name);
				if( !name0.equals(vvv)) { //如果能够根据名称计算绑定的属性值，则作为value的默认值
					valueValue = vvv;
				}
			}
				
		}else  {
//			将编码检索出来， nameValue 显示在hidden中
			valueValue = getStack().findValue(value)!=null ? String.valueOf(getStack().findValue(value)) : null;
		}
		
		if( valueValue !=null) {
			addParameter("nameValue", valueValue);
			
			//将编码转换为名称， 作为 nameText 显示在文本框中
			try {
				//使用Cache拦截器管理缓存，拦截器配置在 WebViewInterceptorHandler 中，参见 /jop-aop.xml 
				if(dbFlag == null) dbFlag = CoreConfigInfo.COMMON_DB_NAME;
				String codeName = Code2NameUtils.code2Name(definition,  valueValue , dbFlag);
				
				addParameter("nameText", codeName);  //添加到参数包中，以便jsp使用			 
			} catch (Exception e) {			 
				if( log.isErrorEnabled())
					log.error("无法转换 picker " + name + " 的值：" + value +" value:" + valueValue);
				addParameter("nameText", value);  //添加到参数包中，以便jsp使用	
			}		
		}
		
	}
	*/
}
