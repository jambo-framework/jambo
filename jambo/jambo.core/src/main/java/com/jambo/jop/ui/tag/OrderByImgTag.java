package com.jambo.jop.ui.tag;

//extends AbstractClosingTag
public class OrderByImgTag {
	
	protected String href;
	
    public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}
                  /*
	@Override
    public Component getBean(ValueStack stack, HttpServletRequest req,
    		HttpServletResponse res) {
    	// TODO Auto-generated method stub
    	return new OrderByImg(stack, req, res);
    }
    
    @Override
    protected void populateParams() {
    	// TODO Auto-generated method stub
    	super.populateParams();
    	OrderByImg tag = (OrderByImg)this.component;
    	tag.setHref(href);
    }
                */
}
