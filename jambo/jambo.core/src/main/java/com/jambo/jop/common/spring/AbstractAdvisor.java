package com.jambo.jop.common.spring;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.ThrowsAdvice;

/**
 * 集成Spring的多个Advisor接口
 * @author He Kun (Sunrise,Guangzhou, CN)
 *
 */
public class AbstractAdvisor implements MethodBeforeAdvice,AfterReturningAdvice,ThrowsAdvice, MethodInterceptor  {
	
	public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
	}
	
	public void before(Method method, Object[] args, Object target) throws Throwable {
	}
	
	public void afterThrowing(Method method, Object[] args, Object target, Throwable t) {
	}
	
	public Object invoke(MethodInvocation invocation) throws Throwable {
		Object result = null;
		
		try {
			result = invocation.proceed();
		} finally  {			
		}
		return result;
	}
}
