package com.jambo.jop.infrastructure.dproxy;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.proxy.Enhancer;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 代理工厂，用于创建一个aop的对象实例， 该实例的方法被调用时，对应的拦截器会自动调用
 * @author He Kun
 *
 */
public class ProxyFactory {
	
	private static Logger LOG = LoggerFactory.getLogger(ProxyFactory.class);
	
	private static String configName = "/jop-aop.xml";
	
	private static Map handlerMap = new HashMap(5);
	
	static {
		loadConfig(configName);
	}
	
	/**
	 * 加载配置
	 * @param configName
	 */
	private static void loadConfig(String configName) {
		SAXReader saxReader = new SAXReader();
		URL url = Thread.currentThread().getContextClassLoader().getResource(configName);
		
		if(url == null) {
			url = ProxyFactory.class.getResource(configName);
		}
		
		if(url== null) {
			throw new RuntimeException("无法找到配置文件" + configName);
		}
			
		InputStream in = null;
		Document document;
		try {
			 in = url.openStream();
			document = saxReader.read(in);
			Element root = document.getRootElement();
			List handlers = root.selectNodes("interceptorHandlers/interceptorHandler");
			
			Iterator iter = handlers.iterator();
			while (iter.hasNext()) {
				Element handlerEle = (Element) iter.next();
				String handlerName = handlerEle.attributeValue("name"); //注意，要去除空格
				String handlerDescription = handlerEle.attributeValue("description"); //注意，要去除空格
				String handlerClass = handlerEle.attributeValue("class"); //注意，要去除空格
				
				if(handlerName==null || "".equals(handlerName)) {
					if(LOG.isErrorEnabled()) LOG.error("拦截处理器元素 name属性 必须设置!");
				}
				
				if(handlerClass==null || "".equals(handlerClass)) {
					handlerClass = InterceptorHandler.class.getName(); //如果没有定义class，则采用默认的。
					if(LOG.isErrorEnabled()) LOG.error("拦截处理器 " + handlerName + " 未定义class  采用默认值" + InterceptorHandler.class.getName());
				}
				InterceptorHandler handler = new InterceptorHandler();
				handler.setName(handlerName);
				handler.setDescription(handlerDescription);
				
				
				try {
					Class clazz = Class.forName(handlerClass);
					if(!InterceptorHandler.class.isAssignableFrom(clazz)) {
						if(LOG.isErrorEnabled()) LOG.error("拦截处理器 class " + handlerClass +" must implement interface " + InterceptorHandler.class.getName());
					}else {
						if(LOG.isInfoEnabled()) LOG.info("加载拦截处理器 " + handlerName );
						handler.setClazz(clazz);
						loadHandler(handlerEle,handler);
					}
						
				} catch (ClassNotFoundException e) {
					if(LOG.isErrorEnabled()) LOG.error("拦截处理器 class " + handlerClass +" not found");
				}
				
				
			}
		} catch (DocumentException e) {
			if(LOG.isErrorEnabled()) LOG.error("Can't locad config " + configName + " " + e.getMessage());
		} catch (IOException e) {
				if(LOG.isErrorEnabled()) LOG.error("Can't locad config " + configName + " " + e.getMessage()) ;
		}
		finally {
			 if (in!=null) 
				 try { in.close();	 }catch(IOException e) { }
		}
	}
	
	private static void loadHandler(Element handlerEle, InterceptorHandler handler ) {
		Iterator interceptors = handlerEle.elementIterator("interceptor");
		while (interceptors.hasNext()) {
			Element interceptorEle = (Element) interceptors.next();
			String interceptorClass = interceptorEle.attributeValue("class").trim(); //注意，要去除空格
			try {
				Class clazz = Class.forName(interceptorClass);
				if(!Interceptor.class.isAssignableFrom(clazz)) {
					if(LOG.isErrorEnabled()) LOG.error("Interceptor class " + interceptorClass +" must implement interface " + Interceptor.class.getName() +" OR extends class " + AbstractInterceptor.class.getName());
				}else {
					if(LOG.isInfoEnabled()) LOG.info("注册拦截器 " + interceptorClass +" to InterceptorHandler " + handler.getName());
					handler.registerInterceptor(clazz);
				}
					
			} catch (ClassNotFoundException e) {
				if(LOG.isErrorEnabled()) LOG.error("Interceptor class " + interceptorClass +" not found");
			}
			
		}
		handlerMap.put(handler.getName(), handler);	
	}
	
	/**
	 * 根据配置的handlerName创建一个指定对象的代理对象，对includeMethods中要求的方法进行拦截
	 * @param proxyClass
	 * @param includeMethods
	 * @param handlerName
	 * @return
	 * @throws InstantiationException
	 */
	public static Object createObject(Class proxyClass,String[] includeMethods,String handlerName) throws InstantiationException {
		InterceptorHandler handler = (InterceptorHandler) handlerMap.get(handlerName);
		if(handler == null) {
			throw new InstantiationException("拦截处理器 not found name:" + handlerName);
		}
		
		return createObject(proxyClass, includeMethods, handler);
	}
	
	public static Object createObject(Class proxyClass,InterceptorHandler handler) throws InstantiationException {
		return createObject(proxyClass, null, handler);
	}
	
	public static Object createObject(Class proxyClass,String[] includeMethods,InterceptorHandler handler) throws InstantiationException {
		 Enhancer enhancer = new Enhancer();
		 enhancer.setSuperclass(proxyClass);  //被代理的类
		 
		 InterceptorHandler interceptorHandler = null;
		try {
			Class interceptorHandlerClass = handler.getClazz();
			interceptorHandler = (InterceptorHandler)interceptorHandlerClass.newInstance();
			interceptorHandler.setIncludeMethods(includeMethods);
			
			interceptorHandler.setName(handler.getName());
			interceptorHandler.setDescription(handler.getDescription());
			interceptorHandler.setClazz(handler.getClazz());
					
			interceptorHandler.setInterceptors(handler.getInterceptors());
			
			enhancer.setCallback( interceptorHandler );
			Object proxyObject = enhancer.create();
			return proxyObject;
			 
		} catch (InstantiationException e) {
			
			throw new InstantiationException("Can't create instance of class " + proxyClass  +" with interceptorHandler " + handler.getName() + ". Cause:" + e); 
		} catch (IllegalAccessException e) {
			throw new InstantiationException("Can't create instance of class " + proxyClass  +" with interceptorHandler " + handler.getName() +". Cause:" + e);
		}
	}
	
	public static void main(String[] args) {
		try {
			Map map = (Map) ProxyFactory.createObject(HashMap.class, new String[] {"get"}, "DAOInterceptorHandler");
			map.put("1111111", "AAAAAAAA");
			System.out.println("1st get " + map.get("1111111"));
			System.out.println("2rd get " + map.get("1111111"));
			System.out.println("2rd get " + map.get("1111111"));
			System.out.println("2rd get " + map.get("1111111"));
			System.out.println("2rd get " + map.get("1111111"));
			System.out.println("3rd get " + map);
			
			map.clear();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
