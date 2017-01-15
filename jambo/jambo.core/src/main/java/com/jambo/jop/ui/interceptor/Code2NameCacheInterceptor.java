package com.jambo.jop.ui.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jambo.jop.infrastructure.dproxy.interceptor.CacheInterceptor;

public class Code2NameCacheInterceptor extends CacheInterceptor {
	
	private static Logger log = LoggerFactory.getLogger(Code2NameCacheInterceptor.class);
	
	public static final String DATA_DICT_CACHE = "DATA_DICT_CACHE";
	public static final String SINGLE_TABLE_CACHE = "SINGLE_TABLE_CACHE";
	
	public Code2NameCacheInterceptor() {		
	}
	
	/**
	 * 获取cache名，返回的cache名必须在ehcache中配置
	 */
	protected String getCacheName(String clazz, String method, Object[] args) {
		
		try {
			if(args!= null && args.length > 2)
			if("code2Name".equals(method) || "valueList".equals(method)) {
				String definition = (String)args[0];
				
				if(definition.startsWith("$")) {
					return DATA_DICT_CACHE;
				}else if(definition.startsWith("#")) {
					return SINGLE_TABLE_CACHE;
				}
			}
		}catch(Exception e) {
			if(log.isWarnEnabled()) 
				log.warn("获取code2Name cache 名称时发生错误, 将将使用默认缓存." + e.getMessage());			
		}
		return super.getCacheName(clazz, method, args);
		
	}
	
	/**
	 * 根据code2name情况重构key生成方式
	 * 只需要采用 args 的第一二个参数: code,dbFlag
	 */
	protected String buildKey(String clazz, String method, Object[] args) {
		
		StringBuffer key = new StringBuffer(16);
		for (int i = 0; i < args.length; i++) {
			if(args[i] == null)
				key.append("null");
			else 
				key.append( args[i]);
			if( i != args.length -1 )
				key.append("-");
		}
		return key.toString();
	}
	
}
