package com.jambo.jop.infrastructure.db;

/**
 * 原来的SessionUtil，改为接口，实现为Hibernate3SessionManager
 * @author bo
 *
 */
public interface SessionManager{

	public BaseDAO newDAO();
	
	public SessionFactoryRouter getSessionFactoryRouter();

    public Object getCurrentSession();

}