package com.jambo.jop.infrastructure.sysadmin;

import org.apache.commons.lang.builder.*;

/**
 * 字符串风格
 * @author He Kun (Sunrise,Guangzhou, CN)
 *
 */
public class ManageLogToStringStyle extends ToStringStyle {
	
	public static final ToStringStyle MANAGE_LOG_STYLE = new ManageLogToStringStyle();

	public ManageLogToStringStyle() {
		super();
		this.setUseClassName(false);
		this.setUseIdentityHashCode(false);
	}
}
