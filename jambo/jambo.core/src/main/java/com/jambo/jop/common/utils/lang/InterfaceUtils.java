package com.jambo.jop.common.utils.lang;

import com.jambo.jop.common.spring.SpringContextManager;

import java.util.*;

/**
 * 注册接口，实现类的对应关系。
 * InterfaceUtils 将根据这个对应关系，来创建接口的默认实现类的实例。
 * 可以在Spring配置中进行注册。实现接口和实现类的完全解耦。
 * 
 * @author He Kun (Sunrise,Guangzhou, CN)
 *
 */
public class InterfaceUtils {
	
	private Map interfaceClassMapping = new HashMap();
	private static InterfaceUtils interfaceUtils = new InterfaceUtils();
	
	public InterfaceUtils(){
	}
	
	public static InterfaceUtils getInstance() {
		if(SpringContextManager.containsBean((InterfaceUtils.class.getName()))) {
			InterfaceUtils utils = (InterfaceUtils)SpringContextManager.getBean(InterfaceUtils.class.getName());
			return utils;
		}else			
			return interfaceUtils;
	}
	
	public Map getInterfaceClassMapping() {
		return interfaceClassMapping;
	}
	
	/**
	 * 注册接口，实现类的对应关系。InterfaceUtils 将根据这个对应关系，来创建接口的默认实现类的实例。
	 * 可以在Spring配置中进行注册。实现接口和实现类的完全解耦。
	 * @param interfaceClassMapping
	 */
	public void setInterfaceClassMapping(Map interfaceClassMapping) {
		this.interfaceClassMapping = interfaceClassMapping;
	}
	
	public Class  getImplClass(Class interfaceClass) {
		Class clazz = null;
		
		if(interfaceClass.isInterface()) { //如果是接口，则创建默认的实现类的实例
			Object o =interfaceClassMapping.get(interfaceClass.getName());			
			if(o==null) {				
				throw new IllegalArgumentException("No impl class is registered for  " + interfaceClass);
			}
			
			try {
				if(o instanceof String) {
					String clazzName = (String)o;
					clazz = Class.forName(clazzName);
                }else if(o instanceof Class) {
					clazz = (Class)o;
                } else
					throw new IllegalArgumentException("Only String and Class is allowed for InterfaceUtils's key and value! But found:" + o.getClass());
				
			} catch (ClassNotFoundException e) {
				throw new IllegalArgumentException(e.getMessage());
			}
		}else {
			clazz = interfaceClass;
		}
		return clazz;
	}
	
	/**
	 * 传入一个接口， 根据配置的接口实现类创建一个对象。
	 * @param interfaceClass
	 * @return
	 * @throws IllegalArgumentException
	 */
	public Object createImplObject(Class interfaceClass)   {
		Object object = null;
		Class clazz = getImplClass(interfaceClass);

		try {
//            object = clazz.newInstance();
            SpringContextManager.registerBean(clazz.getName(), clazz);
            object = SpringContextManager.getBean(clazz.getName());
		}catch(Exception e) {
			throw new IllegalArgumentException(e.getMessage());
		}
		return object;
	}
	
	/**
	 * 传入一个接口， 根据配置的接口实现类创建一个对象。
	 * 与实例方法createImplObject作用相同。
	 * @param interfaceClass
	 * @return
	 * @throws IllegalArgumentException 
	 */
	public Object createObject(Class interfaceClass)   {
		InterfaceUtils util = null;
		
		if( SpringContextManager.getDefaultBeanFactory().containsBean(InterfaceUtils.class.getName())) {
			util = (InterfaceUtils) SpringContextManager.getBean(InterfaceUtils.class.getName());
		}else
			util = interfaceUtils;
		
		return util.createImplObject(interfaceClass);		
	}
	
	public static void main(String[] args) {
		Map map = new HashMap();
		map.put(List.class.getName(), LinkedList.class.getName());
		map.put(Map.class.getName(), LinkedHashMap.class.getName());
		
		InterfaceUtils interfaceUtils = new InterfaceUtils();
		interfaceUtils.setInterfaceClassMapping(map);
		
		Object o1 = interfaceUtils.createImplObject(List.class);
		Object o2 = interfaceUtils.createImplObject(Map.class);
		
		System.out.println("o1 " + o1.getClass().getName());
		System.out.println("o2 " + o2.getClass().getName());		
	}
}
