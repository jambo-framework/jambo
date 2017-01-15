package com.jambo.jop.infrastructure.db;

import javax.transaction.TransactionManager;

/**
 * 多数据库转发路由器接口
 * @author 1.0 Huang Baiming 
 * @author 1.1 He Kun (Sunrise,Guangzhou, CN)
 *
 */
public interface SessionFactoryRouter {	

	public void setSessionFactoryConfig(String configFile);
	
	public SessionManager getSessionManager(Class voClass,String dbFlag);
	
	public SessionManager getSessionManager();

    public TransactionManager getTransactionManager(String dbFlag);
}
