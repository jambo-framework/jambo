package com.jambo.jop.ui.tag.component;

public class CheckboxList {//extends org.apache.struts2.components.CheckboxList {

	protected String randomCode;
	
	public String getRandomCode() {
		return randomCode;
	}

	public void setRandomCode(String randomCode) {
		this.randomCode = randomCode;
	}
                     /*
	public CheckboxList(ValueStack stack, HttpServletRequest request,
			HttpServletResponse response) {
		super(stack, request, response);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected String getDefaultTemplate() {
		// TODO Auto-generated method stub
		return "checkboxlistwithcss";
	}
	
	@Override
	public void evaluateExtraParams() {
		// TODO Auto-generated method stub
		super.evaluateExtraParams();
		StringBuffer sb = new StringBuffer();
		Random r = new Random();
		for(int i=0;i<10;i++){
			sb.append(r.nextInt(10));
		}
		addParameter("randomCode", sb.toString());
	}
	*/
}
