package com.jambo.jop.ui.interceptor;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 给所有应用页面、action提供Contextpath
 * @author jinbo
 */
public class SessionInterceptor extends HandlerInterceptorAdapter {
    protected Logger logger = Logger.getLogger(this.getClass());
    
    public static final String SESSION_CONSTANT_CONTEXTPATH = "contextPath";

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        request.setAttribute(SESSION_CONSTANT_CONTEXTPATH, request.getContextPath());
        return true;
	}

}
