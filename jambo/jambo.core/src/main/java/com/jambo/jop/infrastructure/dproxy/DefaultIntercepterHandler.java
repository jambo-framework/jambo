package com.jambo.jop.infrastructure.dproxy;

import com.jambo.jop.infrastructure.dproxy.interceptor.LogInterceptor;

/**
 * 缺省的拦截处理器。只注册日志拦截器，适合与通用用途
 * @author He Kun
 *
 */
public class DefaultIntercepterHandler extends InterceptorHandler {	
	/**
	 * 
	 */
	public void registerInterceptors() {			
		registerInterceptor(LogInterceptor.class);
		
	}	
}
