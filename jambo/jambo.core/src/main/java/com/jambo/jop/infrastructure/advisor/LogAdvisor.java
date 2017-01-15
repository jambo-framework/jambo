/**
 * 
 */
package com.jambo.jop.infrastructure.advisor;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jambo.jop.common.spring.AbstractAdvisor;

/**
 * 日志拦截器， 实用意义不大，主要作为 Advisor开发的范例。
 * @author He Kun
 *
 */
public class LogAdvisor extends AbstractAdvisor { 
//implements MethodBeforeAdvice,AfterReturningAdvice,ThrowsAdvice, MethodInterceptor  {

	private static Logger log = LoggerFactory.getLogger(LogAdvisor.class);


	/**
	 * 
	 */
	public void before(Method method, Object[] args, Object target)
			throws Throwable {
		if(log.isDebugEnabled())
			log.debug("记录日志(前):" + target +", 方法:" + method.getName());
	}
	
	/**
	 * 
	 */
	public void afterReturning(Object object, Method method, Object[] args, Object target) throws Throwable {		
		if(log.isDebugEnabled())
			log.debug("记录日志(后):" + target +", 方法:" + method.getName());
	}
	
	public void afterThrowing(Method method, Object[] args, Object target, Throwable t) {
		if(log.isDebugEnabled())
			log.debug("记录日志(发生异常):" + target +", 方法:" + method.getName() +",异常:" + t.getMessage());
	}
	
	public Object invoke(MethodInvocation invocation) throws Throwable {
		if(log.isDebugEnabled())
			log.debug("记录日志(Around before):" + invocation.getThis() +", 方法:" + invocation.getMethod().getName());
		
		Object result = null;
		
		try {
			result = invocation.proceed();
		} finally  {
			if(log.isDebugEnabled())
				log.debug("记录日志(Around after):" + invocation.getThis() +", 方法:" + invocation.getMethod().getName());
		}
		return result;
	}
}
