/**
 * @author He Kun (Henry He), Guangzhou, China
 * 2006-4-19
 */
package com.jambo.jop.infrastructure.dproxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * InterceptorHandler
 * <br> Description: class InterceptorHandler
 * <br> Company: ericcson</br>
 * @author He Kun
 * @since 1.0
 * @version 1.0
 * 2006-4-19
 */
public class InterceptorHandler implements MethodInterceptor {
	private static Logger log = LoggerFactory.getLogger(InterceptorHandler.class);
	
	private String name ;
	private Class clazz ;
	private String description ;
	
	private List interceptors = new ArrayList(3);
	
	private Map includeMethods = new HashMap(4); //被代理类需要被拦截的方法名称集合, 需要在创建对象时添加进来，否则任何方法都不会被拦截
	
	public InterceptorHandler() {
		registerInterceptors();
	}
	/**
	 * 
	 */
	public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
		Object result = null;	
		
		if(getInterceptors().size() == 0)
			if(log.isWarnEnabled()) log.warn("InterceptorHandler " + name +" 没有注册任何拦截器，如需注册，请在 /jop-aop.xml 中配置。");
		InvokeInfo invokeInfo = new InvokeInfo(obj,proxy,method,args,null,null);
		
		try {
			//before			
			if(log.isDebugEnabled()) log.debug(" 方法调用前: "  + invokeInfo.getMethod() +",  Handler:" + name);
			invokeInterceptorBefore( invokeInfo );

			//			execute the method
			if(!invokeInfo.isStopInvoke()) {  //没有禁止方法调用，则执行被代理对象的方法

				if(log.isDebugEnabled()) log.debug(" 调用方法: " + invokeInfo.getMethod().getName() +",  Handler:" + name);
				result = proxy.invokeSuper( obj , args);			
				invokeInfo.setResult(result);
				
				if(invokeInfo.isStopProxy()) 
					return invokeInfo.getResult();
			}
			
			//after
			if(log.isDebugEnabled()) log.debug(" 方法调用后: " + invokeInfo.getMethod() +",  Handler:" + name);
			invokeInterceptorAfter( invokeInfo );
			
		}catch(Throwable e) {
			Throwable t = e instanceof InvocationTargetException ? e.getCause() : e;
			invokeInfo.setException(t);
			if(log.isDebugEnabled()) log.debug( " 方法调用异常: " + invokeInfo.getMethod() +",  Handler:" + name);
			invokeInterceptorExceptionThrow(invokeInfo);
		}		
		return invokeInfo.getResult();
	}
		
	/**
	 * 子类可继承，以便手工注册不通的拦截器
	 */
	protected void registerInterceptors() {
		//TODO:子类可继承
	}
	
	public List getInterceptors() {	
		if(interceptors==null) interceptors = new ArrayList(3);
		return interceptors;
	}
	public Map getIncludeMethods() {
		return includeMethods;
	}
	
	public void setIncludeMethods(Map includeMethods) {
		this.includeMethods = includeMethods;
	}		
	
	public void setIncludeMethods(String[] methods) {
		if(methods!=null)
		for (int i = 0; i < methods.length; i++) {
			includeMethods.put(methods[i], methods[i]);
		}
	}
	
	public void registerInterceptor(Class clazz) {
		if(!Interceptor.class.isAssignableFrom(clazz))
			throw new IllegalArgumentException("Interceptor class must implement interface " + Interceptor.class.getName() +" . But it's " + clazz.getName());
		List tors = getInterceptors();
		tors.add(clazz);
	}
	
	public void removeInterceptor(Class clazz) {
		List tors = getInterceptors();
		tors.remove(clazz);
	}

	private void invokeInterceptorBefore(InvokeInfo invokeInfo) {
		List interceptors = getInterceptors();
		
		for (Iterator iter = interceptors.iterator(); iter.hasNext();) {			
			try {
				Class clazz = (Class) iter.next();			
				Interceptor interceptor = (Interceptor)clazz.newInstance();
//				只有部分方法才需要被拦截
				if(interceptor.isIncludeMethod(invokeInfo) 
						&& getIncludeMethods().containsKey(invokeInfo.getMethod().getName() )) 
					interceptor.before(invokeInfo);
			} catch (Throwable e) {				
				e.printStackTrace();
			}
		}
	}

	private void invokeInterceptorAfter(InvokeInfo invokeInfo) {
		List interceptors = getInterceptors();
		
		for (Iterator iter = interceptors.iterator(); iter.hasNext();) {			
			try {
				Class clazz = (Class) iter.next();			
				Interceptor interceptor = (Interceptor)clazz.newInstance();
				if(interceptor.isIncludeMethod(invokeInfo) 
						&& getIncludeMethods().containsKey(invokeInfo.getMethod().getName() )) 
					interceptor.after(invokeInfo);
			} catch (Throwable e) {				
				e.printStackTrace();
			}
		}
	}
	
	private void invokeInterceptorExceptionThrow(InvokeInfo invokeInfo) throws Throwable {
		List interceptors = getInterceptors();
		
		for (Iterator iter = interceptors.iterator(); iter.hasNext();) {			
			try {
				Class clazz = (Class) iter.next();			
				Interceptor interceptor = (Interceptor)clazz.newInstance();
				if(interceptor.isIncludeMethod(invokeInfo) 
						&& getIncludeMethods().containsKey(invokeInfo.getMethod().getName() )) 
					interceptor.exceptionThrow(invokeInfo);
			} catch (Throwable e) {				
				e.printStackTrace();
				throw e;
			}
		}
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Class getClazz() {
		return clazz;
	}
	public void setClazz(Class clazz) {
		this.clazz = clazz;
	}
	public void setInterceptors(List interceptors) {
		this.interceptors = interceptors;
	}

}
