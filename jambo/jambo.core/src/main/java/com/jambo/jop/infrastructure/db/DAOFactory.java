package com.jambo.jop.infrastructure.db;

import org.apache.commons.lang.StringUtils;

import com.jambo.jop.common.persistent.*;
import com.jambo.jop.common.spring.*;
import com.jambo.jop.exception.*;


/**
 * 
 * <p>
 * Title: DAOFactory
 * </p>
 * 
 * <p>
 * Description:DAO工厂, 构造委托的DAO实例.
 * 使用 build(Class daoClazz,User user) 构造的DAO实例, 将会对create,update,remove方法自动生成管理日志或操作日志.
 * <br> 其中, 对应VO必须实现 ManageLog 接口,或者 OperationLog 接口. 
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * 
 * <p>
 * Company: jambo-framework Tech Ltd.
 * </p>
 * 
 * @author J, He Kun 
 * @version 1.0
 * @version 1.1 He Kun 代码重构。用Spring容器构造DAO实例。
 */
public class DAOFactory {	
	/**
	 * 构造DAO实例
	 * @param daoClazz
	 * @param param
	 * @return
	 */
	public static BaseDAO build(Class daoClazz, DBAccessUser user) {
		return build(daoClazz, null, user);		
	}
	
	/**
	 * 构造 CommonDAO 的实例
	 * @param voClass
	 * @param user
	 * @return
	 */
	public static BaseDAO buildCommonDAO(Class voClass, DBAccessUser user) {
        return build(CommonDAO.class, voClass, user);
	}
	
	/**
	 * 采用Spring bean容器的方式构造到实例，以便为dao增加切面功能。具体配置在 applicationContext.xml中
	 * @param daoClazz
	 * @param user
	 * @return
	 */
	public static BaseDAO build(Class daoClazz, Class voClass, DBAccessUser user) {
		return build(daoClazz, voClass, user.getDbFlag());
	}	

	
	/**
	 * 构造dao实例。
	 * @param daoClazz
	 * @param dbFlag 数据源标识
	 * @return
	 */
	public static BaseDAO build(Class daoClazz, String dbFlag) {
		return build(daoClazz, null, dbFlag);
	}
	
	/**
	 * 构造 CommonDAO 的实例
	 * @param voClass
	 * @param dbFlag 数据源标识
	 * @return
	 */
	public static BaseDAO buildCommonDAO(Class voClass, String dbFlag) {
        return build(CommonDAO.class, voClass, dbFlag);
	}

	/**
	 * 采用Spring bean容器的方式构造到实例，以便为dao增加切面功能。具体配置在 applicationContext.xml中
	 * @param daoClazz
	 * @param voClass
	 * @param dbFlag
	 * @return
	 */
	public static BaseDAO build(Class daoClazz, Class voClass, String dbFlag) {
		AbstractDAO dao = null;
		try {
			SpringContextManager.registerBean(daoClazz.getName(), daoClazz);
			dao = (AbstractDAO)SpringContextManager.getBean(daoClazz.getName());
			
			SessionFactoryRouter router = (SessionFactoryRouter) SpringContextManager.getBean(SessionFactoryRouter.class.getName());

//			if(dao.getVoClass()==null) {
//				if(voClass==null)
//					throw new IllegalArgumentException("voClass is empty when build DAO " + daoClazz.getName());
//				else
//					dao.setVoClass(voClass);
//			}
			
			if( StringUtils.isBlank(dbFlag)) {
				throw new IllegalArgumentException("dbFlag( user.dbFlag ) is missing!" );
			}
			SessionManager sessionManager = router.getSessionManager(dao.getVoClass(), dbFlag);
			
			BaseDAO delegateDAO = sessionManager.newDAO();
			//要先设置委托dao，再设置其他属性。
			
			dao.setDelegateDAO(delegateDAO);
			
			dao.setDbFlag(dbFlag); //设置user， 业务bean需要	
            if (daoClazz == CommonDAO.class) {
                dao.setVoClass(voClass);
                dao.getDelegateDAO().setVoClass(voClass);
            }else if(dao.getVoClass()==null) {
				if(voClass == null)
					throw new IllegalArgumentException("voClass is empty when build DAO " + daoClazz.getName());
				else
					dao.setVoClass(voClass);
			}
		} catch (Exception e) {	
			e.printStackTrace();
			throw new JOPException("Can't build dao of " + daoClazz.getName()+", cause:" + e.getMessage(),e);
		}
		return  dao;	
	}	
}
