/**
 * 
 */
package com.jambo.jop.common.utils.lang;

import java.util.HashMap;
import java.util.Map;

/**
 * 线程变量容器。可以简单的方式设置和取出线程级变量。并以map方式进行操作。
 * @author He Kun
 * Aug 19, 2008
 *
 */
public class TheadLocalContainer {
	
	private static ThreadLocal threadLocal = new ThreadLocal();
	
	
	public static void set(Object key,Object value) {
		Map storeMap = (Map)threadLocal.get();
		if(storeMap == null) {
			storeMap = new HashMap();
		}
		synchronized (storeMap) {
			storeMap.put(key, value);
		}
		
		threadLocal.set(storeMap);
	}
	
	public static Object get(Object key) {
		Map storeMap = (Map)threadLocal.get();
		if(storeMap == null) {
			return null;
		}else {
			Object value = null;
			synchronized (storeMap) {
				value = storeMap.get(key);
			}
			return value;
		}	
	}
}
