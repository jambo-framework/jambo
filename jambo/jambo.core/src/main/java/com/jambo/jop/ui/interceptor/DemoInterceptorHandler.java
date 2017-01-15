package com.jambo.jop.ui.interceptor;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jambo.jop.infrastructure.dproxy.InterceptorHandler;

/**
 * 
 * @author He Kun
 *
 */
public class DemoInterceptorHandler extends InterceptorHandler {
	
	private static final Logger log = LoggerFactory.getLogger(DemoInterceptorHandler.class);
	
    private static List interceptors = new ArrayList(3);
    
    public void registerInterceptors() {
    	//registerInterceptor(Code2NameCacheInterceptor.class);
    }
    
    /**
     * @return List interceptors
     */
    public List getInterceptors()  {    	
    	if(log.isDebugEnabled()) log.debug("DemoInterceptorHandler getInterceptors...");
    	return interceptors;
    }
}
