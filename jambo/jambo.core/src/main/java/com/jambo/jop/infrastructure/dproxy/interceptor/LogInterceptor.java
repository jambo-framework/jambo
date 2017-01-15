package com.jambo.jop.infrastructure.dproxy.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jambo.jop.infrastructure.dproxy.AbstractInterceptor;
import com.jambo.jop.infrastructure.dproxy.InvokeInfo;

public class LogInterceptor extends AbstractInterceptor {
	
	private static Logger log = LoggerFactory.getLogger(LogInterceptor.class);
	/**
	 * 
	 */
	public void before(InvokeInfo info) throws Throwable {		
		if(log.isDebugEnabled())
			log.debug("记录日志：调用前" + info.getMethod().getName());
		
	}
	
	
	public void after(InvokeInfo info) throws Throwable {
		// TODO Auto-generated method stub
		if(log.isDebugEnabled())
			log.debug("记录日志：调用后" + info.getMethod().getName());
	}
}
