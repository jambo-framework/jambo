package com.jambo.jop.infrastructure.dproxy.interceptor;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jambo.jop.infrastructure.cache.CacheFactory;
import com.jambo.jop.infrastructure.dproxy.AbstractInterceptor;
import com.jambo.jop.infrastructure.dproxy.InvokeInfo;
/**
 * CacheInterceptor 是个通用的缓存管理器，采用缺省的缓存配置管理数据。
 * 在 ehcache-config.xml 中必须有一个缺省的缓存项配置：defaultCache
 * @author He Kun
 *
 */
public class CacheInterceptor extends AbstractInterceptor {
	
	private static Logger log = LoggerFactory.getLogger(CacheInterceptor.class);
	
	public static final String defaultCacheName = "DEFAULT_CACHE";
	
	public CacheInterceptor() {		
	}

	/**
	 * 采用缺省的缓存配置管理数据。
	 * 检查缓存,查询，及放回缓存
	 */
	public void before(InvokeInfo info) throws Throwable {			
		Object[] args = info.getArgs();
		String clazz = info.getObj().getClass().getName();
		String method = info.getMethod().getName();
		String key = buildKey(clazz, method, args);
		
		String cacheName = getCacheName(clazz, method, args);
		Cache defaultCache = CacheFactory.getCacheManager().getCache( cacheName );
		
		if(defaultCache ==null) {  //使用default配置 
			 CacheFactory.getCacheManager().addCache( cacheName );
			 defaultCache = CacheFactory.getCacheManager().getCache( cacheName );
		}
		
		if(defaultCache!=null) {
			Element element = defaultCache.get(key);
			if(element!=null) {
				Object value = (Object)element.getObjectValue();
//				Object value = (Object)element.getValue();
				info.setResult(value);
				
				if(log.isDebugEnabled()) log.debug("缓存命中" + cacheName + "，找到数据：key:" + key + ",Value:" + info.getResult());
				info.setStopInvoke(true);
				info.setStopProxy(true);  //在缓存中找到数据，不再需要执被调方法
			}else {
				if(log.isDebugEnabled()) log.debug("缓存未命中" + cacheName + "，未找到数据：key:" + key + ",Value:" + info.getResult());
			}
		}else {
			cacheNotConfiged(cacheName);			
			//info.setStopProxy(true);
		}	
	}
	
	/**
	 * 
	 */
	public void after(InvokeInfo info) throws Throwable {		
		Object[] args = info.getArgs();
		String clazz = info.getObj().getClass().getName();
		String method = info.getMethod().getName();
		String key = buildKey(clazz, method, args);
		
		String cacheName = getCacheName(clazz, method, args);
		Cache defaultCache = CacheFactory.getCacheManager().getCache( cacheName );
		
		if(info.getResult()!=null) {			
			if(defaultCache!=null) {
				Element element = defaultCache.get(key);
				//element是为空时，创建缓存项， 不为空时，更新缓存项。
				if(info.getResult()!=null) {
					if(log.isDebugEnabled()) log.debug(cacheName + " 缓存数据：key:" + key + ",Value:" + info.getResult());
					
					element = new Element(key,info.getResult());
					defaultCache.put(element);
				}					
				
			}else {
				cacheNotConfiged(cacheName);
			}
		}
	}
	
	private void cacheNotConfiged(String cacheName) {
		if(log.isWarnEnabled()) 
			log.warn("默认缓存项 " +cacheName + " 没有找到! 请在 /ehcache.xml 文件中进行定义！");
	}
	/**
	 * 获取配置的cache名称，缺省为 defaultCache。 子类可覆写此方法，针对不同情况，使用不同的缓存来管理数据。
	 * @return
	 */
	protected String getCacheName(String clazz, String method, Object[] args) {
		return defaultCacheName;
	}
	/**
	 * 构造被拦截方法的缓存Key值。 由类名，方法名和参数构成。
	 * @param clazz
	 * @param method
	 * @param args
	 * @return
	 */
	protected String buildKey(String clazz,String method, Object[] args) {
		StringBuffer buffer = new StringBuffer(120);
		buffer.append(clazz).append("#")
				.append(method).append("#");
		
		for (int i = 0; i < args.length; i++) {
			if(args[i] == null)
				buffer.append("null").append("_");
			else 
				buffer.append( args[i]).append("_");
		}
		return buffer.toString();
	}
}
