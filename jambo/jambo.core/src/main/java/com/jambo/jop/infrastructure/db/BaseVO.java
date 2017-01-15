package com.jambo.jop.infrastructure.db;

import java.io.Serializable;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 在Struts2里，值对象如果没个基类，则页面转换Form对象时，不方便
 * @author bo
 *
 */
public class BaseVO implements Serializable{
	
	public String toString() {		
		return ReflectionToStringBuilder.toString(this);
	}
	
}
