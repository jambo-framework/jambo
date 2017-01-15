package com.jambo.jop.common.spring;

import com.jambo.jop.exception.JOPException;
import com.jambo.jop.infrastructure.config.CoreConfigInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Spring 上下文管理器，ContextManager
 * @author He Kun
 *
 */
public class SpringContextManager implements ApplicationContextAware {
	static private String SPRING_CONFIG = "jop.spring.config";
	static private boolean initFlag = false;
    // Spring应用上下文环境,web环境中用的
    private static ApplicationContext applicationContext = null;


    public static boolean isInitFlag() {
		return initFlag;
	}

	public static final String CONFIG_NAME = "applicationContext.xml";
	public static final Logger log = LogManager.getLogger(SpringContextManager.class);

    // Spring应用上下文环境,独立程序环境中用的，以applicationContext为优先
	private static Map contextCache = new HashMap();
	
	public static void init() {
		init(CONFIG_NAME);
	}
	
	public static void init(String resouce) {
        if (!isInitFlag() && SpringContextManager.applicationContext == null) {
            //2011-8-24 jinbo 允许由外部提供spring配置文件所在路径
            // 读取部分参数
            String path = CoreConfigInfo.getRuntimeParam(SPRING_CONFIG);
            try {
                if (path != null){
                    init(new String[]{path});
                }else{
                    init(new String[]{resouce });
                }
            } catch (Exception e) {
                log.error("Spring init error.");
                log.catching(e);
            }
        }
	}
	
	private static void init(String[] resouces) {
        if (!isInitFlag() && SpringContextManager.applicationContext == null) {
            for (int i = 0; i < resouces.length; i++) {
                File f = new File(resouces[i]);
                ApplicationContext context = null;
                if (f.exists()){
                    context = new FileSystemXmlApplicationContext(resouces[i]);
                } else{
                    context = new ClassPathXmlApplicationContext(resouces[i]);
                }

                contextCache.put("context" + ( i + 1), context);
            }
            initFlag = true;
        }
	}
	
	/**
	 * 获取缺省配置（第一个配置）对应的context
	 * @return
	 */
	public static ApplicationContext getDefaultContext() {
        if (SpringContextManager.applicationContext == null) {
            return (ApplicationContext)contextCache.get("context1");
        }else{
            return SpringContextManager.applicationContext;
        }
	}
	
	/**
	 * 获取缺省配置（第一个配置）对应的context
	 * @return
	 */
	public static DefaultListableBeanFactory getDefaultBeanFactory() {
        DefaultListableBeanFactory bf;
        if (SpringContextManager.applicationContext == null) {
            AbstractXmlApplicationContext context = (AbstractXmlApplicationContext)contextCache.get("context1");
            if (context == null)
                throw new JOPException("SpringContextManager initial error!");
            bf =  (DefaultListableBeanFactory)context.getBeanFactory();
        } else {
            if (SpringContextManager.applicationContext instanceof XmlWebApplicationContext){
                XmlWebApplicationContext context = (XmlWebApplicationContext) SpringContextManager.applicationContext;
                bf =  (DefaultListableBeanFactory)context.getBeanFactory();
            } else if (SpringContextManager.applicationContext instanceof AbstractXmlApplicationContext){
                AbstractXmlApplicationContext context = (AbstractXmlApplicationContext)SpringContextManager.applicationContext;
                bf =  (DefaultListableBeanFactory)context.getBeanFactory();
            } else {
                throw new JOPException("applicationContext not support!");
            }
        }
        return  bf;
	}
	
	/**
	 * 从缺省的bean工厂中获取指定名称的bean实例
	 * @param name
	 * @return
	 */
	public static Object getBean(String name) {
		return getBean(name,null);
	}
	
	/**
	 * 判断缺省bean容器中是否包含指定的Bean
	 * @param beanName
	 * @return
	 */
	public static boolean containsBean(String beanName) {
		return getDefaultBeanFactory().containsBean(beanName);
	}
	/**
	 * 从缺省的bean工厂中获取指定名称的bean实例
	 * @param name
	 * @param args
	 * @return
	 */
	public static Object getBean(String name,Object[] args) {
		DefaultListableBeanFactory beanFactory = getDefaultBeanFactory();
		if(args == null) 
			return beanFactory.getBean(name,args);
		else
			return beanFactory.getBean(name,args); //这个方法有问题，Spring不支持构造函数传参，将来修改
	}
	
	
	/**
	 * 注册bean定义,使用protype
	 * @param idOrName
	 * @param beanClass
	 */
	public static void registerBean(String idOrName,Class beanClass) {
		registerBean(idOrName, beanClass,false);
	}
	
	/**
	 * 注册bean定义
	 * @param idOrName
	 * @param beanClass
	 * @param isSingleton
	 */
	public static void registerBean(String idOrName,Class beanClass,boolean isSingleton) {
		DefaultListableBeanFactory beanFactory = getDefaultBeanFactory();
		if(!beanFactory.containsBean( idOrName )){
			
			RootBeanDefinition beanDefinition = new RootBeanDefinition();
			beanDefinition.setBeanClassName(beanClass.getName());
            beanDefinition.setScope(AbstractBeanDefinition.SCOPE_SINGLETON);
//			beanDefinition.setSingleton(isSingleton); //默认为true, 需要增加可配置性
			beanFactory.registerBeanDefinition( idOrName , beanDefinition);
			
			if(log.isDebugEnabled()) log.debug("注册bean: {} singleton?:{}",  idOrName, isSingleton);
		}
		
	}
	
	/**
	 * 获取bean定义
	 * @param beanName
	 */
	public static BeanDefinition getBeanDefinition(String beanName) {
		DefaultListableBeanFactory beanFactory = getDefaultBeanFactory();
		BeanDefinition def = beanFactory.getBeanDefinition(beanName);
		return def;
	}

    /**
     * 实现ApplicationContextAware接口的回调方法，设置上下文环境
     *
     * @param applicationContext
     */
    public void setApplicationContext(ApplicationContext applicationContext) {
        SpringContextManager.applicationContext = applicationContext;
        initFlag = true;
    }

    /**
     * @return ApplicationContext
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static String getI18nMessage(String key){
        String msg = getApplicationContext().getMessage(key, null, Locale.getDefault());
        return  msg;
    }

	/**
	 * demo
	 * @param args
	 */
	public static void main(String[] args) {
		SpringContextManager.registerBean(HashMap.class.getName(), HashMap.class);
		Map hashMap = (Map)SpringContextManager.getBean(HashMap.class.getName());
		System.out.println("bean:" + hashMap);
	}
}
