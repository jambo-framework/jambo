package com.jambo.jop.ui.tag.component;

public class OrderByImg{//} extends ClosingUIBean{

	protected String href;
	
	public static final String OPEN_TEMPLATE = "orderbyimg";
	public static final String TEMPLATE = "orderbyimg-close";
	
//	public OrderByImg(ValueStack stack, HttpServletRequest request,
//			HttpServletResponse response) {
//		super(stack, request, response);
		// TODO Auto-generated constructor stub
	
//	}
	
	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}
                                    /*
	@Override
	public void evaluateExtraParams(){
		// TODO Auto-generated method stub
		super.evaluateExtraParams();
		addParameter("href", href);
		addParameter("contextPath", ServletActionContext.getRequest().getContextPath());
		try{
			if(href.contains("doOrderby")){
				String field = href.substring(href.indexOf("'")+1, href.lastIndexOf("'"));
				DBQueryParam param = (DBQueryParam) ServletActionContext.getRequest().getAttribute("param");
				
			    String orderby = (String)PropertyUtils.getProperty(param, "_orderby");
			    String desc = (String)PropertyUtils.getProperty(param, "_desc");
			    
				if(StringUtils.isNotEmpty(field) && field.equals(orderby)){
					
					if (desc.length() == 0) {
						addParameter("showImg", "up");
			        }else {
			        	addParameter("showImg", "down");
			        }
				}
			}
		}catch (Exception e) {
			// do nothing
		}
	}
	                */
//	@Override
	protected String getDefaultTemplate() {
		// TODO Auto-generated method stub
		return TEMPLATE;
	}
	
//	@Override
	public String getDefaultOpenTemplate() {
		// TODO Auto-generated method stub
		return OPEN_TEMPLATE;
	}

}
