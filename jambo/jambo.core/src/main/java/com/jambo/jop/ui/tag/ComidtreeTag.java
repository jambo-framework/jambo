package com.jambo.jop.ui.tag;

//extends AbstractUITag
public class ComidtreeTag  {

	private String definition;

	private String condition;

	private boolean readonly;

	private boolean disabled;

	private String comtype;

	private String onchange;

	private String nameOnly = "true";

	public String getNameOnly() {
		return nameOnly;
	}

	public void setNameOnly(String nameOnly) {
		this.nameOnly = nameOnly;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getDefinition() {
		return definition;
	}

	public void setDefinition(String definition) {
		this.definition = definition;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public String getOnchange() {
		return onchange;
	}

	public void setOnchange(String onchange) {
		this.onchange = onchange;
	}

	public ComidtreeTag() {
		super();
	}

	public boolean isReadonly() {
		return readonly;
	}

	public void setReadonly(boolean readonly) {
		this.readonly = readonly;
	}
                               /*
	public int doStartTag() throws JspException {
		return SKIP_BODY;
	}

	public int doEndTag() throws JspTagException, JspException {
		try {
			Object value = pageContext.getRequest().getAttribute(name);
			if (value == null) {
				value = "";
			}
			StringBuffer input = new StringBuffer();
			input.append("<input type=\"text\" name=\"").append(name).append("\" value=\"").append(value).append("\" readonly=\"true\" class=\"form_input_1x\" />");

			input.append("<input type=\"button\" name=\"treebt\" title=\"点击选择商品标识\" class='picker_button' value=\"...\" onclick=\"showtree").append("()\" ");
			if (this.isDisabled()) {
				input.append("disabled=\"true\"");
			}
			input.append(" />");
			pageContext.getOut().write(input.toString());
			pageContext.getOut().write(javaScript());
		} catch (JspTagException ex) {
			ex.printStackTrace();
			throw ex;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return EVAL_PAGE;
	}

	private String javaScript() throws Exception {
		StringBuffer buff = new StringBuffer();
		buff.append("<script language=\"JavaScript\" type=\"text/javascript\"> \n");
		// 使用该标签的页面必须要在<head>处导入rescommon.js文件
		buff.append("function showtree").append("() { \n");
		buff.append("  var array = new Array();\n");
		if (this.getCondition() != null && this.getCondition().trim().length() > 0) {
			buff.append("  var oldCondition=\"").append(this.getCondition()).append("\";\n");
			buff.append("  array[0] = oldCondition;\n");
		}
		buff.append("\n  var returnvalue = window.showModalDialog(contextPath+\"").append("/resource/com/selectcomidtree.jsp");
		buff.append("\",array,\"dialogWidth:675px;dialogHeight:455px;status:no;resizable:no;\");");

		buff.append(" \n  var oldvalue = document.all('").append(name).append("')").append(".value; ");
		buff.append("\n  if (returnvalue != null && returnvalue.length){\ndocument.all('").append(name).append("')").append(".value=returnvalue[0]");
		if ("false".equals(nameOnly)) {
			buff.append("\"  \"+returnvalue[1];");
		} else {
			buff.append(";");
		}

		if (this.getOnchange() != null) {
			buff.append(" \n  if ( oldvalue != document.all('").append(name).append("')").append(name).append(".value ){ ");
			buff.append("   \n  document.all('").append(name).append("')").append(".fireEvent(\"onchange\");");
			buff.append(" } ");
		}
		buff.append("\n } \n}");
		buff.append("</script>");
		return buff.toString();
	}

	public String getComtype() {
		return comtype;
	}

	public void setComtype(String comtype) {
		this.comtype = comtype;
	}

	@Override
	public Component getBean(ValueStack stack, HttpServletRequest req, HttpServletResponse res) {
		ValueStack stack1 = (ValueStack) req.getAttribute(ServletActionContext.STRUTS_VALUESTACK_KEY);

		return (Component) stack1;
	}
          */
}
