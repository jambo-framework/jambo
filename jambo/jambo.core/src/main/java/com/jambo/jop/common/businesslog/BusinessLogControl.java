package com.jambo.jop.common.businesslog;

public interface BusinessLogControl {
	
	public abstract Class getVoClass();

    public abstract void setVoClass(Class class1);
	
	public abstract Object doLogCreate(Object obj)
    	throws Exception;
	
}
