package com.jambo.jop.infrastructure.cache;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CacheFactory {
	
	private static Logger log = LoggerFactory.getLogger(CacheFactory.class);
	public static CacheManager cacheManager;
	static {		
		try {
			cacheManager = CacheManager.create( CacheFactory.class.getResource("/ehcache.xml"))	;
			
		} catch (CacheException e) {
			e.printStackTrace();
		}
	}
		
	public  static CacheManager getCacheManager() {
		return cacheManager;
	}
	
	public static void logStatistics() {
		if(log.isDebugEnabled()) 
			log.debug("************ Cache Report start*******************************");
		String[] cacheNames = CacheFactory.getCacheManager().getCacheNames();
		for(int i=0 ; i < cacheNames.length ; i++) {
			Cache cache =  CacheFactory.getCacheManager().getCache(cacheNames[i]);
			
			if(log.isDebugEnabled()) 
				log.debug(cacheNames[i] + ":  " + cache.getStatistics());
		}
		if(log.isDebugEnabled()) 
			log.debug("************ Cache Report end *******************************");
	}
}
