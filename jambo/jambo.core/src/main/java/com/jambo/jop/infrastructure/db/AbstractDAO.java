package com.jambo.jop.infrastructure.db;

import com.jambo.jop.common.spring.SpringContextManager;
import org.hibernate.Session;

import javax.transaction.TransactionManager;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

/**
 * AbstractDAO, 将dao行为委托到具体的DAO实现类，以便根据配置灵活切换不同的OR框架
 * @author He Kun (Sunrise,Guangzhou, CN)
 *
 */
public abstract class AbstractDAO implements BaseDAO {

	private Class voClass;
	private BaseDAO delegateDAO;

	public AbstractDAO(Class voClass) {
		//委托到具体的DAO实现类，以便根据配置灵活切换不同的OR框架
		this.setVoClass(voClass);
	}

	public AbstractDAO() {
		this(null);
	}

	/**
	 * 具体方案的子类必须提供SessionManager的具体实现类实例
	 */
	public SessionManager getSessionManager() {
		return getSessionFactoryRouter().getSessionManager(delegateDAO.getVoClass(),delegateDAO.getDbFlag());
	}
	
	public void setSessionManager(SessionManager sessionManager) {		
	}

    public TransactionManager getTransactionManager(){
        return getSessionFactoryRouter().getTransactionManager(delegateDAO.getDbFlag());
    }

    public SessionFactoryRouter getSessionFactoryRouter() {
		return (SessionFactoryRouter)SpringContextManager.getBean(SessionFactoryRouter.class.getName());
	}
	
	public int count(Object param) throws Exception {
		return delegateDAO.count(param);
	}

	public int countByProperty(String prop, Object value) throws Exception {
		return delegateDAO.countByProperty(prop, value);
	}

	public Object create(Object vo) throws Exception {
		return delegateDAO.create(vo);
	}

	public void evict() throws Exception {
		delegateDAO.evict();
	}

	public void evict(Serializable id) throws Exception {
		delegateDAO.evict(id);
	}

	public Collection findAll() throws Exception {
		return delegateDAO.findAll();
	}

	public Object findByPk(Serializable pk) throws Exception {
		return delegateDAO.findByPk(pk);
	}

	public Object findByProperty(String prop, Object value) throws Exception {
		return delegateDAO.findByProperty(prop, value);
	}

	public Object findByUniqueKey(String uniqueKeyPropertyName, Serializable id) throws Exception {
		return delegateDAO.findByUniqueKey(uniqueKeyPropertyName, id);
	}

	public Date getCurrentTime() throws Exception {
		return delegateDAO.getCurrentTime();
	}

	public String getDbFlag() {
		return delegateDAO.getDbFlag();
	}

	public Object getMaxid(String id, String value, String prop) throws Exception {
		return delegateDAO.getMaxid(id, value, prop);
	}

	public Object getMaxValue(String prop) throws Exception {
		return delegateDAO.getMaxValue(prop);
	}

	public Object getSequence(String seqname) throws Exception {
		return delegateDAO.getSequence(seqname);
	}

	public Class getVoClass() {
//		if(delegateDAO ==null)
			return this.voClass;
//		else
//			return delegateDAO.getVoClass();
	}

	public DataPackage query(Object param, int type) throws Exception {
		return delegateDAO.query(param, type);
	}

	public DataPackage query(Object param) throws Exception {
		return delegateDAO.query(param);
	}

	public DataPackage queryByNamedSqlQuery(String name, Object param, int type) throws Exception {
		return delegateDAO.queryByNamedSqlQuery(name, param, type);
	}

	public DataPackage queryByNamedSqlQuery(String name, Object param) throws Exception {
		return delegateDAO.queryByNamedSqlQuery(name, param);
	}

	public DataPackage queryByNamedSqlQuery(String name) throws Exception {
		return delegateDAO.queryByNamedSqlQuery(name);
	}

//	public DataPackage queryBySql(String queryString, Object param) throws Exception {
//		return delegateDAO.queryBySql(queryString, param);
//	}

//	public DataPackage queryBySql(String queryString) throws Exception {
//		return delegateDAO.queryBySql(queryString);
//	}

	public DataPackage queryCountByNamedSqlQuery(String name, Object param) throws Exception {
		return delegateDAO.queryCountByNamedSqlQuery(name, param);
	}

	public Object queryUniqueByNamedSqlQuery(String name, Object param, Class returnType) throws Exception {
		return delegateDAO.queryUniqueByNamedSqlQuery(name, param, returnType);
	}

	public Object queryUniqueByNamedSqlQuery(String name, Object param) throws Exception {
		return delegateDAO.queryUniqueByNamedSqlQuery(name, param);
	}

//	public Object queryUniqueBySql(String queryString, Object param, Class returnType) throws Exception {
//		return delegateDAO.queryUniqueBySql(queryString, param, returnType);
//	}

	public void remove(Object vo) throws Exception {
		delegateDAO.remove(vo);
	}

	public void removeByPk(Serializable pk) throws Exception {
		delegateDAO.removeByPk(pk);
	}

	public void setDbFlag(String dbFlag) {
		delegateDAO.setDbFlag(dbFlag);
	}

	public void setVoClass(Class voClass) {
//		if(delegateDAO == null)
			this.voClass = voClass;
//		else
//			delegateDAO.setVoClass(voClass);
	}

	public DataPackage unionQuery(Object[] param, Class[] vo, String[][] joins, int type) throws Exception {
		return delegateDAO.unionQuery(param, vo, joins, type);
	}

	public DataPackage unionQuery(Object[] param, Class[] vo, String[][] joins) throws Exception {
		return delegateDAO.unionQuery(param, vo, joins);
	}

	public Object update(Object vo) throws Exception {
		return delegateDAO.update(vo);
	}

	public DataPackage queryBySql(String queryString) throws Exception {
		return delegateDAO.queryBySql(queryString);
	}
	
	public DataPackage queryBySql(String queryString, Object params) throws Exception {
		return delegateDAO.queryBySql(queryString,params);
	}
	
	
	public Object queryUniqueBySql(String queryString, Object param, Class returnType) throws Exception {
		return delegateDAO.queryUniqueBySql(queryString, param, returnType);
	}
	
	public Object queryUniqueBySql(String queryString, Object param, Class returnType, String queryName) throws Exception {
		return delegateDAO.queryUniqueBySql(queryString, param, returnType,queryName);		
	}
	
	public BaseDAO getDelegateDAO() {
		return delegateDAO;
	}

	public void setDelegateDAO(BaseDAO delegateDAO) {
		this.delegateDAO = delegateDAO;
		this.delegateDAO.setVoClass(this.voClass);
	}

    public void clearSession() throws Exception{
        delegateDAO.clearSession();
    }

    public Session getSession() throws Exception{
        return delegateDAO.getSession();
    }
}
