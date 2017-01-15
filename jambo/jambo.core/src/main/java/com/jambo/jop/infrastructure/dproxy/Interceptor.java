/**
 * @author He Kun (Henry He), Guangzhou, China
 * 2006-4-19
 */
package com.jambo.jop.infrastructure.dproxy;

/**
 * Interceptor
 * <br> Description: class Interceptor
 * <br> Company: ericcson</br>
 * @author He Kun
 * @since 1.0
 * @version 1.0
 * 2006-4-19
 */
public interface Interceptor {
	
	/**
	 * 判断当前被代理的方法是否需要排除，默认为false
	 * @param info
	 * @return
	 */
	boolean isIncludeMethod(InvokeInfo info);
	
	void before(InvokeInfo info) throws Throwable;
	
	void after(InvokeInfo info) throws Throwable;
	
	void exceptionThrow(InvokeInfo info) throws Throwable;
}
