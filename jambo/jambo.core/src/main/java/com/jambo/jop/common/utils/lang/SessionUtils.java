package com.jambo.jop.common.utils.lang;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.jambo.jop.infrastructure.db.SessionGetter;
import com.jambo.jop.infrastructure.db.SessionManager;
import com.jambo.jop.infrastructure.db.hibernate3.Hibernate3SessionFactoryRouter;
import com.jambo.jop.infrastructure.db.hibernate3.Hibernate3SessionManager;

public class SessionUtils {

    public static Session currentSession(String dbFlag) throws HibernateException {
        SessionManager sessionManager = Hibernate3SessionFactoryRouter.getInstance().getSessionManager(null, dbFlag);
        SessionGetter getter = (SessionGetter)sessionManager;
        return (Session) getter.getCurrentSession();
    }

    /**
     * 以前的版本用传进来的clazz识别是不是在公共库，后来已经废掉这个机制，这个参数已经不需要
     */
    public static Session currentSession(Class clazz,String dbFlag) throws HibernateException {
		SessionManager sessionManager = Hibernate3SessionFactoryRouter.getInstance().getSessionManager(clazz,dbFlag);
		SessionGetter getter = (SessionGetter)sessionManager;
		return (Session) getter.getCurrentSession();
	}
	
	public static SessionFactory getSessionFactory(Class clazz,String dbFlag) throws HibernateException{
		Hibernate3SessionManager sessionManager = (Hibernate3SessionManager)Hibernate3SessionFactoryRouter.getInstance().getSessionManager(clazz,dbFlag);
		SessionFactory sessionFactory = sessionManager.getSessionFactory();
		return sessionFactory;
	}
}
