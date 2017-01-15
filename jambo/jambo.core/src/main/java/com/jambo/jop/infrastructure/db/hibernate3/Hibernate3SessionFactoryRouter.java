package com.jambo.jop.infrastructure.db.hibernate3;

import com.jambo.jop.common.spring.SpringContextManager;
import com.jambo.jop.exception.JOPException;
import com.jambo.jop.infrastructure.config.CoreConfigInfo;
import com.jambo.jop.infrastructure.db.SessionFactoryRouter;
import com.jambo.jop.infrastructure.db.SessionManager;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;

import javax.transaction.TransactionManager;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;


public class Hibernate3SessionFactoryRouter implements SessionFactoryRouter {
	private static String HIBERNATE_CONFIG = "jop.hibernate.basepath";
	
	private static Logger log = LoggerFactory.getLogger(Hibernate3SessionFactoryRouter.class);
	
	private static SessionFactoryRouter sessionFactoryRouter ;  //本类的单例
		
	private String sessionFactoryConfig = "data/sessionFactory.properties";
	private boolean hasInit; 
	private String defaultDataSource = CoreConfigInfo.COMMON_DB_NAME;
	private String multiDBMode = "COMMON_CITY_MODE";  //取值:公共+地市库模式 COMMON_CITY_MODE， 平行库模式:BROTHERS_MODE
	
	static Properties dbMap;

	public Hibernate3SessionFactoryRouter() {		
	}
	
	/**
	 * 获取该类的唯一单例
	 * @return
	 */
	public static SessionFactoryRouter getInstance() {
		if( SpringContextManager.containsBean(SessionFactoryRouter.class.getName())) {
			sessionFactoryRouter = (SessionFactoryRouter)SpringContextManager.getBean(SessionFactoryRouter.class.getName());
		}else {
			if(log.isWarnEnabled()) log.warn("Bean can't be found! It must be required! Name: " + SessionFactoryRouter.class.getName());
			sessionFactoryRouter = new Hibernate3SessionFactoryRouter();
		}
			
		return sessionFactoryRouter;
	}
	
	public String getSessionFactoryConfig() {
		return sessionFactoryConfig;
	}

	public void setSessionFactoryConfig(String sessionFactoryConfig) {
		this.sessionFactoryConfig = sessionFactoryConfig;
	}
	
	/**
	 * 加载配置
	 *
	 */
	private  void loadSessionFactoryConfig() {
//		加载配置
		InputStream in = null;
		try {
			dbMap = new Properties();
			synchronized (dbMap) {
				 in = Hibernate3SessionFactoryRouter.class.getResourceAsStream("/" + sessionFactoryConfig);
				if(in == null) {
					if(log.isErrorEnabled())
						log.error("sessionFactoryConfig:" + sessionFactoryConfig + " not found!" );
					throw new JOPException("sessionFactoryConfig: " + sessionFactoryConfig + " not found!");
				}
					
				
				dbMap.load(in);
	
				if(log.isDebugEnabled()) log.debug("加载SessionFactory 配置: " + sessionFactoryConfig  +", Content:" + dbMap);
				
				//根据配置， 运用Spring bean 容器构造sessionFactory
				Iterator keys = dbMap.keySet().iterator();
				while(keys.hasNext()) {
					String dbName = (String)keys.next();
					String configFile = dbMap.getProperty(dbName);
					
					registerSessionFactoryBean(dbName, configFile);
				}
			}
			
		}catch(IOException e) {
			throw new JOPException("Can't load sessionFactoryConfig from location: " + sessionFactoryConfig);
		}finally {
			if(in!=null)
				try {	in.close();	} catch (IOException e) { }
		}
	}
	
	private void registerSessionFactoryBean(String dbName,String configFile) {
		DefaultListableBeanFactory beanFactory = SpringContextManager.getDefaultBeanFactory();
		String beanName =  "sessionFactory_" + dbName;
		if(!beanFactory.containsBean( beanName )){
			
			RootBeanDefinition beanDefinition = new RootBeanDefinition();
			
			beanDefinition.setBeanClassName( LocalSessionFactoryBean.class.getName());
            beanDefinition.setScope(AbstractBeanDefinition.SCOPE_SINGLETON);
//			beanDefinition.setSingleton( true ); //默认为true, 需要增加可配置性
			
			MutablePropertyValues propertyValues = new MutablePropertyValues();
			
			String[] configpaths;
			if(configFile.indexOf(",")>0){
				configpaths = configFile.split(",");
			}else{
				configpaths = new String[]{configFile};
			}
			
			//2011-8-24 jinbo 允许由外部提供Hibernate配置文件所在路径
			String path = CoreConfigInfo.getRuntimeParam(HIBERNATE_CONFIG);
			if (path != null){
				for (int i=0; i<configpaths.length; i++){
					configpaths[i] = path + configpaths[i]; 
				}
			}
			//2011-8-24 jinbo 允许由外部提供Hibernate配置文件所在路径
			
			
//			String configpath = configFile.startsWith("classpath:") ? configFile : "classpath:" + configFile;
			PropertyValue propertyValue1 = new PropertyValue("configLocations",configpaths);	
			
			propertyValues.addPropertyValue(propertyValue1);
			
			beanDefinition.setPropertyValues(propertyValues);
			beanFactory.registerBeanDefinition( beanName , beanDefinition);
			
			if(log.isDebugEnabled()) log.debug("注册 SessionFactory bean: " + beanName );
		}
	}
	
	/**
	 * 根据voClass,数据源标识获取SessionManager
	 */
	public SessionManager getSessionManager(Class voClass,String dbFlag) {
//		dbFlag = Hibernate3RouterMap.getRouter(dbFlag);//做一层名字与dbFlag的装换
		if(dbFlag == null){
			throw new IllegalArgumentException("dbFlag is missing when getSessionManager(Class voClass,String dbFlag). ");
		}
        //2015.3.25 jinbo 已经不再需要传入voClass
        //以前的版本用传进来的clazz识别是不是在公共库，后来已经废掉这个机制，这个参数已经不需要
//		if(voClass == null) {
//			throw new IllegalArgumentException("voClass is missing when getSessionManager(Class voClass,String dbFlag). ");
//		}

		//如果未配置common数据源，并且没有初始化，则按多数据源处理，加载 多数据源配置。
		//否则，按单数据源处理
		if(!hasInit && !SpringContextManager.containsBean("sessionFactory_" + defaultDataSource)) {
			loadSessionFactoryConfig();		
			hasInit = true;
		}
		
		SessionFactory sessionFactory;
		if( CoreConfigInfo.COMMON_CITY_MODE.equals( getMultiDBMode() )) { //公共+地市库模式, 2013-5-28 jinbo 这个模式重定义为多数据库模式
//			if(Hibernate3RouterMap.containsCommonVO(voClass.getName())){
//				sessionFactory = (SessionFactory) SpringContextManager.getBean("sessionFactory_" + defaultDataSource);
//			}else{
				sessionFactory = (SessionFactory) SpringContextManager.getBean("sessionFactory_DB_" + dbFlag);
//			}
		}else{ //平行数据库模式, 2013-5-28 jinbo 重定义为单数据库模式
//			sessionFactory = (SessionFactory) SpringContextManager.getBean("sessionFactory_DB_" + dbFlag);
			sessionFactory = (SessionFactory) SpringContextManager.getBean("sessionFactory_" + defaultDataSource);
		}
		
		Hibernate3SessionManager sessionManager = new Hibernate3SessionManager();
		sessionManager.setSessionFactoryRouter(this);
		sessionManager.setSessionFactory(sessionFactory);
		return sessionManager;
	}

    /**
     * 根据dbFlag,数据源标识获取TransactionManager
     * 2015.3.30 jinbo
     */
    public TransactionManager getTransactionManager(String dbFlag) {
        if(dbFlag == null){
            throw new IllegalArgumentException("dbFlag is missing when getTransactionManager(String dbFlag). ");
        }
        if(!hasInit && !SpringContextManager.containsBean("sessionFactory_" + defaultDataSource)) {
            loadSessionFactoryConfig();
            hasInit = true;
        }

        TransactionManager tm;
        if( CoreConfigInfo.COMMON_CITY_MODE.equals( getMultiDBMode() )) { //这个模式重定义为多数据库模式
            tm = (TransactionManager) SpringContextManager.getBean("transactionManager_DB_" + dbFlag);
        }else{ //单数据库模式
            tm = (TransactionManager) SpringContextManager.getBean("transactionManager_" + defaultDataSource);
        }
        return  tm;
    }

	/**
	 * 获取缺省的数据源
	 */
	public SessionManager getSessionManager() {	
		throw new  IllegalArgumentException("not supoort!");
	}


	public String getDefaultDataSource() {
		return defaultDataSource;
	}

	public void setDefaultDataSource(String defaultDataSource) {
		this.defaultDataSource = defaultDataSource;
	}
	
	/**
	 * 支持两种模式：取值:公共+地市库模式 COMMON_CITY_MODE， 平行库模式:BROTHERS_MODE
	 */
	public String getMultiDBMode() {
		return multiDBMode;
	}
	
	/**
	 * 支持两种模式：取值:公共+地市库模式 COMMON_CITY_MODE， 平行库模式:BROTHERS_MODE
	 * @param multiDBMode
	 */
	public void setMultiDBMode(String multiDBMode) {
		this.multiDBMode = multiDBMode;
	}

}
