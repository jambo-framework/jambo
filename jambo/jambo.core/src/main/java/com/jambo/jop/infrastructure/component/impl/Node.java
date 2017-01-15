package com.jambo.jop.infrastructure.component.impl;

import java.io.Serializable;

/**
 * 
 * @author He Kun
 *
 */
public class Node implements Serializable {

	private String valueObject;

	private String code;

	private String name;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValueObject() {
		return valueObject;
	}

	public void setValueObject(String valueObject) {
		this.valueObject = valueObject;
	}
	
	public String toString() {
//		ToStringStyle style =new ManageLogToStringStyle();
//		return ReflectionToStringBuilder.toString(this, style);		
		StringBuffer buffer = new StringBuffer(32);
		buffer.append("[Code2Name Node:")
				.append("code=")
				.append(code)
				.append(",")
				.append("name=")
				.append(name)				
				.append("]");
		return buffer.toString();
		
	}
}
