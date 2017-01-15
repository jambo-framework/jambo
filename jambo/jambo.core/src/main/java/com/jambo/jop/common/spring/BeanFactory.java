/**
 * 
 */
package com.jambo.jop.common.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;

/**
 * @author He Kun
 *
 */
public class BeanFactory {
	
	private static Logger log = LoggerFactory.getLogger(BeanFactory.class);
	/**
	 * 从Spring容器中获取bean
	 * @param name
	 * @return
	 */
	public static Object getBean(String name) {
		
		if(SpringContextManager.containsBean(name))
			return SpringContextManager.getBean(name);
		else {
			throw new BeanNotFoundException(name);
		}
	}
	
	public static Object getBean(Class beanClass) {
		return getBean(beanClass.getName());
	}
	
	/**
	 * 通过Spring容器创建bean
	 * 其中会检查bean的scope ，避免冲突。bean的scope取决与第一次创建时定义的scope。
	 * @param name
	 * @param beanClass
	 * @param isSingleton
	 * @return
	 */
	public static Object createBean(String name,Class beanClass,boolean isSingleton ) {
		
		if(!SpringContextManager.containsBean(name)) {
			
//			if(log.isDebugEnabled())
//				log.debug("bean dynamic register " + name +",class:" + beanClass +",scope(singleton?)" + isSingleton );
			SpringContextManager.registerBean(name, beanClass, isSingleton);
		}else {  //检查bean的scope ，避免冲突。bean的scope取决与第一次创建时定义的scope。
			BeanDefinition def = SpringContextManager.getBeanDefinition(name);
			if(isSingleton != def.isSingleton()) {
				throw new IllegalArgumentException("bean with name '" + name +"' already registered as "
							+ (def.isSingleton() ? "singleton" : "prototype") +", but a prototype scope when create it this time.");
			}
		}
		return getBean(name);	
	}
	
	/**
	 * 通过Spring容器创建bean，bean类型prototype
	 * @param name
	 * @param beanClass
	 * @return
	 */
	public static Object createBean(String name,Class beanClass ) {

		return createBean(name, beanClass,false);

		
	}
	
	/**
	 * 通过Spring容器创建bean，bean类型prototype
	 * @param name
	 * @param beanClass
	 * @return
	 */
	public static Object createBean(Class beanClass ) {
		return createBean(beanClass.getName(), beanClass);
		
	}
	
	

	/**
	 * 通过Spring容器创建bean，bean类型prototype
	 * @param name
	 * @param beanClass
	 * @param isSingleton
	 * @return
	 */
	public static Object createBean(Class beanClass,boolean isSingleton ) {
		return createBean(beanClass.getName(), beanClass, isSingleton);
		
	}
}
