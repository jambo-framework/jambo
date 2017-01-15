package com.jambo.jop.infrastructure.db.hibernate3;

import com.jambo.jop.common.utils.bean.BeanTools;
import com.jambo.jop.exception.JOPException;
import com.jambo.jop.infrastructure.db.*;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.*;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.type.CompositeType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;

import javax.transaction.TransactionManager;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * Title: BaseDAO for Hibernate, Hibernate版的DAO基类
 * </p>
 * <p>
 * Description: 封装数据库的操作，提供基本的增、删、改、查的方法
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: jambo-framework Tech Ltd.
 * </p>
 *
 * @author Gaven
 * @version 1.5 He Kun 2008-1-30 复杂查询功能完善，部分字段查询功能简化。无对应集合查询不再需要配置映射和新建VO，返回数据为Map类型.
 */
public class Hibernate3BaseDAO implements BaseDAO {
    private SessionManager sessionManager;  // 非static 
    private static Logger log = LogManager.getLogger(Hibernate3BaseDAO.class);

    //缺省的 Hibernate 查询缓存名，所有查询使用一个缓存region 和同样的缓存策略
    private static final String DEFAULT_HIBERNATE_QUERY_CACHE = "QUERY_DEFAULT_HIBERNATE_QUERY_CACHE";
    /**
     * java类型和Hibernate类型映射关系，queryBySql(),查询部分字段时需要
     */
    private static final Map javaTypeHibernateTypeMapping = new HashMap(12);

    static {
        javaTypeHibernateTypeMapping.put(String.class, StandardBasicTypes.STRING);
        javaTypeHibernateTypeMapping.put(Character.class, StandardBasicTypes.CHARACTER);

        javaTypeHibernateTypeMapping.put(Integer.class, StandardBasicTypes.INTEGER);
        javaTypeHibernateTypeMapping.put(Long.class, StandardBasicTypes.LONG);
        javaTypeHibernateTypeMapping.put(Double.class, StandardBasicTypes.DOUBLE);
        javaTypeHibernateTypeMapping.put(Float.class, StandardBasicTypes.FLOAT);
        javaTypeHibernateTypeMapping.put(Short.class, StandardBasicTypes.SHORT);
        javaTypeHibernateTypeMapping.put(Byte.class, StandardBasicTypes.BYTE);
        javaTypeHibernateTypeMapping.put(java.util.Date.class, StandardBasicTypes.DATE);
        javaTypeHibernateTypeMapping.put(java.sql.Timestamp.class, StandardBasicTypes.TIMESTAMP);
        javaTypeHibernateTypeMapping.put(BigDecimal.class, StandardBasicTypes.BIG_DECIMAL);
        //其他类型需要时再添加
    }

    public Hibernate3BaseDAO() {
    }

    /**
     * @todo 做一个查询量的限制，以后需要改进
     */

    protected Class voClass;

    /* (non-Javadoc)
     * @see com.jambo.jop.infrastructure.db.hibernate3.BaseDAO#getVoClass()
     */
    public Class getVoClass() {
        return voClass;
    }

    /* (non-Javadoc)
     * @see com.jambo.jop.infrastructure.db.hibernate3.BaseDAO#setVoClass(java.lang.Class)
     */
    public void setVoClass(Class voClass) {
        this.voClass = voClass;
    }

    /**
     * 调用该方法构造的DAO，必需先setDbFlag（），才能调用方法。
     */
    protected Hibernate3BaseDAO(Class voClass) {
        this.voClass = voClass;
    }

    private String dbFlag;

    /* (non-Javadoc)
     * @see com.jambo.jop.infrastructure.db.hibernate3.BaseDAO#getDbFlag()
     */
    public String getDbFlag() {
        return dbFlag;
    }

    /* (non-Javadoc)
     * @see com.jambo.jop.infrastructure.db.hibernate3.BaseDAO#setDbFlag(java.lang.String)
     */
    public void setDbFlag(String dbFlag) {
        this.dbFlag = dbFlag;
    }


    public Hibernate3BaseDAO(Class voClass, String dbFlag) {
        this.voClass = voClass;
        this.dbFlag = dbFlag;
    }

    /**
     * 使用Hibernate3的 SessionManager实例, 这是整个DAO层接口框架的总入口
     */
    public SessionManager getSessionManager() {
        return sessionManager;
    }

    public void setSessionManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public TransactionManager getTransactionManager(){
        return getSessionManager().getSessionFactoryRouter().getTransactionManager(dbFlag);
    }

    /**
     * 获取Hibernate SessionFactory , 基类接口无此方法，Hibernate特定。
     */
    public SessionFactory getSessionFactory() {
        return ((Hibernate3SessionManager) getSessionManager()).getSessionFactory();
    }

    /**
     * 获取Hibernate 当前 SessionFactory的Session , 基类接口无此方法，Hibernate特定。
     */
    public Object getCurrentSession() {
        if (!(sessionManager instanceof SessionGetter)) {
            throw new JOPException("BaseDAO impl class must implement interface SessionGetter, or session can't be gotten." + this.getClass().getName());
        } else {
            SessionGetter getter = (SessionGetter) sessionManager;
            return getter.getCurrentSession();
        }
    }

    private Session currentSession() {
        return (Session) getCurrentSession();
    }

    /* (non-Javadoc)
     * @see com.jambo.jop.infrastructure.db.hibernate3.BaseDAO#evict()
     */
    public void evict() throws Exception {
        try {
            SessionFactory sessionFactory = getSessionFactory();
            sessionFactory.getCache().evictEntityRegion(voClass);
//			sessionFactory.evict(voClass);
        } catch (HibernateException ex) {
            if (ex.getCause() != null) {
                throw new Exception(ex.getCause());
            } else {
                throw ex;
            }
        }
    }

    /* (non-Javadoc)
     * @see com.jambo.jop.infrastructure.db.hibernate3.BaseDAO#evict(java.io.Serializable)
     */
    public void evict(Serializable id) throws Exception {
        try {
            SessionFactory sessionFactory = getSessionFactory();
            sessionFactory.getCache().evictEntity(voClass, id);
//            sessionFactory.evict(voClass, id);
        } catch (HibernateException ex) {
            if (ex.getCause() != null) {
                throw new Exception(ex.getCause());
            } else {
                throw ex;
            }
        }
    }

    /* (non-Javadoc)
     * @see com.jambo.jop.infrastructure.db.hibernate3.BaseDAO#clearSession()
     */
    public void clearSession() throws Exception {
        Session session = currentSession();
        session.clear();
    }

    /* (non-Javadoc)
      * @see com.jambo.jop.infrastructure.db.hibernate3.BaseDAO#getSession()
      */
    public Session getSession() throws Exception {
        return currentSession();
    }

    /* (non-Javadoc)
      * @see com.jambo.jop.infrastructure.db.hibernate3.BaseDAO#create(java.lang.Object)
      */
    public Object create(Object vo) throws Exception {
        Session session = currentSession();
        try {
            session.save(vo);

        } catch (HibernateException ex) {
            /**
             * 一般来说，Hibernate处理异常时都用HibernateException包装之后抛出，
             * 这里尝试解开HibernateException的包装，出示真正的错误原因。
             */
            if (ex.getCause() != null) {
                throw new Exception(ex.getCause());
            } else {
                throw ex;
            }
        }
        return vo;
    }

    /* (non-Javadoc)
     * @see com.jambo.jop.infrastructure.db.hibernate3.BaseDAO#removeByPk(java.io.Serializable)
     */
    public void removeByPk(Serializable pk) throws Exception {
        Session session = currentSession();

        try {
            Object entity = session.get(voClass, pk);
            if (entity != null) {
                session.delete(entity);

            }
        } catch (HibernateException ex) {
            if (ex.getCause() != null) {
                throw new Exception(ex.getCause());
            } else {
                throw ex;
            }
        }
    }

    /* (non-Javadoc)
     * @see com.jambo.jop.infrastructure.db.hibernate3.BaseDAO#remove(java.lang.Object)
     */
    public void remove(Object vo) throws Exception {
        Session session = currentSession();

        try {
            if (vo != null) {
                session.delete(vo);

            }
        } catch (HibernateException ex) {
            if (ex.getCause() != null) {
                throw new Exception(ex.getCause());
            } else {
                throw ex;
            }
        }
    }

    /* (non-Javadoc)
     * @see com.jambo.jop.infrastructure.db.hibernate3.BaseDAO#update(java.lang.Object)
     */
    public Object update(Object vo) throws Exception {
        Session session = currentSession();
        try {
            session.update(vo);
        } catch (HibernateException ex) {
            if (ex.getCause() != null) {
                throw new Exception(ex.getCause());
            } else {
                throw ex;
            }
        }
        return vo;
    }

    /* (non-Javadoc)
     * @see com.jambo.jop.infrastructure.db.hibernate3.BaseDAO#findByPk(java.io.Serializable)
     */
    public Object findByPk(Serializable pk) throws Exception {
        if (pk == null) throw new NullPointerException("findByPk method: Pk is null.");
        Session session = currentSession();
        Object vo;
        try {
            vo = session.get(voClass, pk);
        } catch (HibernateException ex) {
            if (ex.getCause() != null) {
                throw new Exception(ex.getCause());
            } else {
                throw ex;
            }
        }
        return vo;
    }

    /* (non-Javadoc)
     * @see com.jambo.jop.infrastructure.db.hibernate3.BaseDAO#findByUniqueKey(java.lang.String, java.io.Serializable)
     */
    public Object findByUniqueKey(String uniqueKeyPropertyName, Serializable id)
            throws Exception {
        return findByProperty(uniqueKeyPropertyName, id);
    }

    /* (non-Javadoc)
     * @see com.jambo.jop.infrastructure.db.hibernate3.BaseDAO#findAll()
     */
    public Collection findAll() throws Exception {
        Session session = currentSession();

        Collection result = null;
        try {
            result = session.createCriteria(voClass).list();
        } catch (HibernateException ex) {
            if (ex.getCause() != null) {
                throw new Exception(ex.getCause());
            } else {
                throw ex;
            }
        }
        return result;
    }

    /* (non-Javadoc)
     * @see com.jambo.jop.infrastructure.db.hibernate3.BaseDAO#getMaxValue(java.lang.String)
     */
    public Object getMaxValue(String prop) throws Exception {
        if (prop != null && prop.trim().length() > 0) {
            StringBuilder sql = new StringBuilder("SELECT max(this.")
                    .append(prop).append(") FROM ").append(voClass.getName())
                    .append(" this");

            Session session = null;
            try {
                session = currentSession();

                Query query = session.createQuery(sql.toString());
                Iterator iter = query.iterate();
                if (iter != null && iter.hasNext()) {
                    return iter.next();
                }
            } catch (HibernateException ex) {
                if (ex.getCause() != null) {
                    throw new Exception(ex.getCause());
                } else {
                    throw ex;
                }
            }
        }
        return null;
    }

    /* (non-Javadoc)
     * @see com.jambo.jop.infrastructure.db.hibernate3.BaseDAO#getMaxid(java.lang.String, java.lang.String, java.lang.String)
     */
    public Object getMaxid(String id, String value, String prop)
            throws Exception {
        if (prop != null && prop.trim().length() > 0) {
            StringBuffer sql = new StringBuffer("SELECT max(this.")
                    .append(prop).append(") FROM ").append(voClass.getName())
                    .append(" this where this.").append(id).append("='")
                    .append(value).append("'");

            Session session = null;
            try {
                session = currentSession();

                Query query = session.createQuery(sql.toString());
                Iterator iter = query.iterate();
                if (iter != null && iter.hasNext()) {
                    return iter.next();
                }
            } catch (HibernateException ex) {
                if (ex.getCause() != null) {
                    throw new Exception(ex.getCause());
                } else {
                    throw ex;
                }
            }
        }

        return null;
    }

    /* (non-Javadoc)
     * @see com.jambo.jop.infrastructure.db.hibernate3.BaseDAO#findByProperty(java.lang.String, java.lang.Object)
     */
    public Object findByProperty(String prop, Object value) throws Exception {
        if (prop != null && prop.trim().length() > 0 && value != null) {
            StringBuffer selectHQL = new StringBuffer("FROM ").append(
                    voClass.getName()).append(" this ").append(
                    "where this.").append(prop).append(" = '").append(value.toString()).append("'");

            Session session = currentSession();

            Query query = session.createQuery(selectHQL.toString());

            queryCacheSet(query); //设置缓存属性

            List list = query.list();
            if (list != null && list.size() > 0) {
                return list.get(0);
            }
        }
        return null;
    }

    /* (non-Javadoc)
     * @see com.jambo.jop.infrastructure.db.hibernate3.BaseDAO#countByProperty(java.lang.String, java.lang.Object)
     */
    public int countByProperty(String prop, Object value) throws Exception {
        int count = 0;
        if (prop != null && prop.trim().length() > 0 && value != null) {
            StringBuffer countSQL = new StringBuffer("SELECT  ");
            countSQL.append(buildCountClause(true));
            countSQL.append(" FROM ")
                    .append(voClass.getName()).append(" this WHERE this.")
                    .append(prop).append(" = :prop ");

            Session session = null;
            try {
                session = currentSession();

                Query query = session.createQuery(countSQL.toString());
                query.setParameter("prop", value);

                Iterator iter = query.iterate();
                if (iter != null && iter.hasNext()) {
                    return ((Number) iter.next()).intValue();
                }
            } catch (HibernateException ex) {
                if (ex.getCause() != null) {
                    throw new Exception(ex.getCause());
                } else {
                    throw ex;
                }
            }
        }
        return count;
    }

    /**
     * 根据参数查询
     */
    public DataPackage query(Object param) throws Exception {
        DBQueryParam dbparam = (DBQueryParam) param; //只计数
        return query(param, dbparam.getQueryType());
    }

    /* (non-Javadoc)
     * @see com.jambo.jop.infrastructure.db.hibernate3.BaseDAO#count(java.lang.Object)
     */
    public int count(Object param) throws Exception {
        DataPackage dp = query(param, QUERY_TYPE_COUNT_ONLY);
        if (dp != null) {
            return dp.getRowCount();
        } else {
            return -1;
        }
    }

    /* (non-Javadoc)
     * @see com.jambo.jop.infrastructure.db.hibernate3.BaseDAO#query(java.lang.Object, int)
     */
    public DataPackage query(Object param, int type) throws Exception {
        StringBuffer countHQL = new StringBuffer("SELECT  ");
        countHQL.append(buildCountClause(true));
        countHQL.append(" FROM ")
                .append(voClass.getName()).append(" this ");

        int _pagesize = 20, _pageno = 1;
        String _orderby = null, _desc = null;
        try {
            _pagesize = Integer.parseInt(BeanUtils.getProperty(param,
                    "_pagesize"));
        } catch (Exception ex) {
            _pagesize = Integer.MAX_VALUE;
        }
        if (_pagesize > MAX_QUERY_COUNT) {
            _pagesize = MAX_QUERY_COUNT; /** @todo 做一个查询量的限制，以后需要修改 */
        }

        try {
            _pageno = Integer.parseInt(BeanUtils.getProperty(param,
                    "_pageno"));
        } catch (Exception ex) {
            _pageno = 1;
        }

        try {
            _orderby = BeanUtils.getProperty(param, "_orderby");
        } catch (Exception ex) {
            _orderby = "";
        }

        try {
            _desc = BeanUtils.getProperty(param, "_desc");
        } catch (Exception ex) {
            _desc = "";
        }
        if (_desc == null) {
            _desc = "0"; // 默认递增
        }

        Map placeholders = new HashMap();
        List dateParamList = new ArrayList();
        StringBuffer whereClause = new StringBuffer(buildHQL(param, "this",
                placeholders, dateParamList));
        if (whereClause.length() > 4) {
            countHQL = countHQL.append(" WHERE ").append(whereClause);
        }

        // 查询结果
        DataPackage result = new DataPackage();
        result.setPageNo(_pageno);
        result.setPageSize(_pagesize);

        Session session = currentSession();

        try {
            // 取总记录数
            if (type == QUERY_TYPE_COUNT_AND_DATA || type == QUERY_TYPE_COUNT_ONLY) {

                Query countQuery = session.createQuery(countHQL.toString());
                //缓存
                queryCacheSet("QUERY_COUNT_" + voClass.getName(), countQuery); //设置缓存属性


                if (log.isDebugEnabled())
                    log.debug("count HQL:" + countHQL.toString());
                if (placeholders != null && placeholders.size() > 0) {
                    Set keyset = placeholders.keySet();
                    for (Iterator it = keyset.iterator(); it.hasNext(); ) {
                        String key = (String) it.next();
                        Object value = placeholders.get(key);
                        // 针对日期做特殊判断
                        if (dateParamList.indexOf(key) != -1) {
                            if (value.toString().length() == 10) {// yyyy-MM-dd
                                countQuery.setDate(key, (new SimpleDateFormat(
                                        "yyyy-MM-dd")).parse(value.toString()));
                            } else {// yyyy-MM-dd HH:mm:ss
                                java.util.Date date = new SimpleDateFormat(
                                        "yyyy-MM-dd HH:mm:ss").parse(value
                                        .toString());
                                Timestamp ts = new Timestamp(date.getTime());
                                countQuery.setTimestamp(key, ts);
                            }
                        } else {
                            countQuery.setParameter(key, value);
                        }
                    }
                }

                List resultList = countQuery.list();

                // use Number type compatible with Integer and Long, because hibernate 3.3.0 use long. 
                Number countInteger = (Number) resultList.get(0);
                result.setRowCount(countInteger != null ? countInteger.intValue() : 0);
                //判断当前页不为首页时，页数超过数据最大页数时，默认查询最后一页。---LJX
                if (_pageno > 1 && countInteger.intValue() <= _pagesize * (_pageno - 1)) {
                    if (countInteger.intValue() / _pagesize == 0) {
                        _pageno = 1;
                    } else {
                        _pageno = countInteger.intValue() / _pagesize;
                    }
                    result.setPageNo(_pageno);
                }
            }

            // 取指定页的数据
            if (type == QUERY_TYPE_COUNT_AND_DATA || type == QUERY_TYPE_DATA_ONLY) {
                StringBuffer selectHQL = new StringBuffer(buildSelectFileds(param));
                selectHQL.append(" FROM ").append(
                        voClass.getName()).append(" this ");
                if (whereClause.toString().trim().length() > 0) {
                    selectHQL = selectHQL.append(" WHERE ").append(whereClause);
                }

                if (_orderby != null && _orderby.trim().length() > 0
                        && _desc != null) {
                    selectHQL = selectHQL.append(" order by ").append(getNewOrderby(_orderby, _desc, "this."));
                }

                Query query = session.createQuery(selectHQL.toString());
//				缓存
                queryCacheSet("QUERY_" + voClass.getName(), query); //设置缓存属性

                if (placeholders != null && placeholders.size() > 0) {
                    Set keyset = placeholders.keySet();
                    for (Iterator it = keyset.iterator(); it.hasNext(); ) {
                        String key = (String) it.next();
                        Object value = placeholders.get(key);
                        // 针对日期做特殊判断
                        if (dateParamList.indexOf(key) != -1) {
                            if (value.toString().length() == 10) {// yyyy-MM-dd
                                query.setDate(key, (new SimpleDateFormat(
                                        "yyyy-MM-dd")).parse(value.toString()));
                            } else {// yyyy-MM-dd HH:mm:ss
                                java.util.Date date = new SimpleDateFormat(
                                        "yyyy-MM-dd HH:mm:ss").parse(value
                                        .toString());
                                Timestamp ts = new Timestamp(date.getTime());
                                query.setTimestamp(key, ts);
                            }
                        } else {
                            query.setParameter(key, value);
                        }
                    }
                }

                if (_pagesize != 0) {
                    query.setMaxResults(_pagesize);
                    query.setFirstResult(_pagesize * (_pageno - 1));
                }

                List list = query.list();
                if (list == null) {
                    list = new ArrayList();
                }
                result.setDatas(list);
                result.setPageSize(_pagesize);
            }
        } catch (HibernateException ex) {
            if (ex.getCause() != null) {
                throw new Exception(ex.getCause());
            } else {
                throw ex;
            }
        }
        return result;
    }

    /**
     * 以DBQueryParam中SelectFields指定的字段，构造查询的字段，空则查询全部
     *
     * @param param DBQueryParam
     */
    private String buildSelectFileds(Object param) {
        StringBuffer selectHQL = new StringBuffer("");
        try {
            DBQueryParam dpparam = (DBQueryParam) param;
            if (param != null && dpparam.getSelectFields() != null) {
                for (Object o : dpparam.getSelectFields()) {
                    if (selectHQL.length() > 0) selectHQL.append(",");
                    selectHQL.append(" this.").append(o);
                }
            }
            String head = "SELECT NEW " + voClass.getName() + "(";
            if (selectHQL.length() > 0) selectHQL.insert(0, head).append(")");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return selectHQL.toString();
    }

    /**
     * 查询，只查具体的数据
     */
    private DataPackage queryOnlyRcd(Object param) throws Exception {

        int _pagesize = 20, _pageno = 1;
        String _orderby = null, _desc = null;
        try {
            _pagesize = Integer.parseInt(BeanUtils.getProperty(param,
                    "_pagesize"));
        } catch (Exception ex) {
            _pagesize = Integer.MAX_VALUE;
        }
        if (_pagesize > MAX_QUERY_COUNT) {
            _pagesize = MAX_QUERY_COUNT; /** @todo 做一个查询量的限制，以后需要修改 */
        }

        try {
            _pageno = Integer.parseInt(BeanUtils.getProperty(param,
                    "_pageno"));
        } catch (Exception ex) {
            _pageno = 1;
        }

        try {
            _orderby = BeanUtils.getProperty(param, "_orderby");
        } catch (Exception ex) {
            _orderby = "";
        }

        try {
            _desc = BeanUtils.getProperty(param, "_desc");
        } catch (Exception ex) {
            _desc = "";
        }
        if (_desc == null) {
            _desc = "0"; // 默认递增
        }

        Map placeholders = new HashMap();
        List dateParamList = new ArrayList();
        StringBuffer whereClause = new StringBuffer(buildHQL(param, "this", placeholders, dateParamList));

        // 查询结果
        DataPackage result = new DataPackage();
        result.setPageNo(_pageno);
        result.setPageSize(_pagesize);

        Session session = currentSession();

        try {
            // 不取总记录数
            // modifyed by Ge Aiping on 25-Oct-2006
            result.setRowCount(0);
            // 取指定页的数据
            StringBuffer selectHQL = new StringBuffer("FROM ").append(
                    voClass.getName()).append(" this ");
            if (whereClause.toString().trim().length() > 0) {
                selectHQL = selectHQL.append(" WHERE ").append(whereClause);
            }

            if (_orderby != null && _orderby.trim().length() > 0
                    && _desc != null) {
                selectHQL = selectHQL.append(" order by ").append(getNewOrderby(_orderby, _desc, "this."));
            }

            Query query = session.createQuery(selectHQL.toString());
            //缓存设置
            queryCacheSet(query); //设置缓存属性


            if (placeholders != null && placeholders.size() > 0) {
                Set keyset = placeholders.keySet();
                for (Iterator it = keyset.iterator(); it.hasNext(); ) {
                    String key = (String) it.next();
                    Object value = placeholders.get(key);
                    // 针对日期做特殊判断
                    if (dateParamList.indexOf(key) != -1) {
                        if (value.toString().length() == 10) {// yyyy-MM-dd
                            query.setDate(key, (new SimpleDateFormat(
                                    "yyyy-MM-dd")).parse(value.toString()));
                        } else {// yyyy-MM-dd HH:mm:ss
                            java.util.Date date = new SimpleDateFormat(
                                    "yyyy-MM-dd HH:mm:ss").parse(value
                                    .toString());
                            Timestamp ts = new Timestamp(date.getTime());
                            query.setTimestamp(key, ts);
                        }
                    } else {
                        query.setParameter(key, value);
                    }
                }
            }

            if (_pagesize != 0) {
                query.setMaxResults(_pagesize);
                query.setFirstResult(_pagesize * (_pageno - 1));
            }

            List list = query.list();
            if (list == null) {
                list = new ArrayList();
            }

            result.setDatas(list);
        } catch (HibernateException ex) {
            if (ex.getCause() != null) {
                throw new Exception(ex.getCause());
            } else {
                throw ex;
            }
        }
        return result;
    }

    /* (non-Javadoc)
     * @see com.jambo.jop.infrastructure.db.hibernate3.BaseDAO#query2(java.lang.Object[], java.lang.Class[], java.lang.String[][])
	 */
    public DataPackage unionQuery(Object param[], Class vo[], String joins[][])
            throws Exception {
        return unionQuery(param, vo, joins, QUERY_TYPE_COUNT_AND_DATA);
    }

    /* (non-Javadoc)
     * @see com.jambo.jop.infrastructure.db.hibernate3.BaseDAO#query2(java.lang.Object[], java.lang.Class[], java.lang.String[][], int)
     */
    public DataPackage unionQuery(Object param[], Class vos[], String joins[][],
                                  int type) throws Exception {

        int _pagesize = 20, _pageno = 1;
        String _orderby = null, _desc = null;

        if (param == null || vos == null || joins == null) {
            throw new Exception("error paramters !");
        }
        if (param.length != vos.length || joins[0].length != vos.length) {
            throw new Exception("error paramters num!");
        }

        try {
            _pagesize = Integer.parseInt(BeanUtils.getProperty(
                    param[0], "_pagesize"));
        } catch (Exception ex) {
            _pagesize = Integer.MAX_VALUE;
        }
        if (_pagesize > MAX_QUERY_COUNT) {
            _pagesize = MAX_QUERY_COUNT; /** @todo 做一个查询量的限制，以后需要修改 */
        }

        try {
            _pageno = Integer.parseInt(BeanUtils.getProperty(
                    param[0], "_pageno"));
        } catch (Exception ex) {
            _pageno = 1;
        }

        // 将orderby的字段不限于param[0],推广到所有param都可以排序
        try {
            StringBuffer sb_orderby = new StringBuffer(" ");
            for (int i = 0; i < param.length; i++) {
                String orderby = BeanUtils.getProperty(param[i], "_orderby");
                if (StringUtils.isEmpty(orderby)) continue;
                String[] _orderbys = StringUtils.split(orderby, ",");
                for (int j = 0; j < _orderbys.length; j++) {
                    sb_orderby.append("t").append(i).append(".").append(_orderbys[j]).append(",");
                }
            }
            _orderby = sb_orderby.substring(0, sb_orderby.length() - 1);
        } catch (Exception ex) {
            _orderby = "";
        }
        try {
            for (int i = 0; i < param.length; i++) {
                _desc = BeanUtils.getProperty(param[i], "_desc");
                if (!StringUtils.isEmpty(_desc)) {
                    break;
                }
            }
        } catch (Exception ex) {
            _desc = "";
        }

        Map placeholders = new HashMap();
        List dateParamList = new ArrayList();
        StringBuffer condition = new StringBuffer();
        StringBuffer strjoins = new StringBuffer();
        StringBuffer fromClause = new StringBuffer("FROM ");
        for (int i = 0; i < vos.length; i++) {
            fromClause.append(vos[i].getName()).append(" t").append(i).append(
                    ",");
            if (param[i] != null && !"".equals(param[i])) {
                String where = buildHQL(param[i], "t" + i, placeholders,
                        dateParamList, placeholders.size() + 1);
                if (where != null && !"".equals(where)) {
                    condition.append(" and ").append(where).append(" ");
                }
            }
        }
        if (fromClause.length() > 1) {
            fromClause.deleteCharAt(fromClause.length() - 1);
        }
        if (condition.length() > 1) {
            condition.deleteCharAt(condition.length() - 1);
        }
        for (int i = 0; i < joins.length; i++) {
            strjoins.append(" and ");
            for (int j = 0; j < joins[i].length; j++) {
                if (joins[i][j] != null) {
                    strjoins.append("t").append(j).append(".").append(
                            joins[i][j]).append("=");
                }
            }
            if (strjoins.length() > 1) {
                strjoins.deleteCharAt(strjoins.length() - 1);
                strjoins.append(" ");
            }
        }
        fromClause.append(" where 1=1 ").append(strjoins).append(condition);
        // System.out.println(fromClause.toString());

        if (_desc == null) {
            _desc = "0"; // 默认递增
        }

        DataPackage result = new DataPackage();
        result.setPageNo(_pageno);
        result.setPageSize(_pagesize);

        Session session = currentSession();

        try {
            // 取总记录数
            if (type == QUERY_TYPE_COUNT_AND_DATA || type == QUERY_TYPE_COUNT_ONLY) {
                String countHQL = "select " + buildCountClause(true) + fromClause;
                Query query = session.createQuery(countHQL.toString());
                //缓存设置
                queryCacheSet(query); //设置缓存属性


                if (placeholders != null && placeholders.size() > 0) {
                    Set keyset = placeholders.keySet();
                    for (Iterator it = keyset.iterator(); it.hasNext(); ) {
                        String key = (String) it.next();
                        Object value = placeholders.get(key);
                        // 针对日期做特殊判断
                        if (dateParamList.indexOf(key) != -1) {
                            if (value.toString().length() == 10) {// yyyy-MM-dd
                                query.setDate(key, (new SimpleDateFormat(
                                        "yyyy-MM-dd")).parse(value.toString()));
                            } else {// yyyy-MM-dd HH:mm:ss
                                java.util.Date date = new SimpleDateFormat(
                                        "yyyy-MM-dd HH:mm:ss").parse(value
                                        .toString());
                                Timestamp ts = new Timestamp(date.getTime());
                                query.setTimestamp(key, ts);
                            }
                        } else {
                            query.setParameter(key, value);
                        }
                    }
                }

                Iterator iter = query.iterate(); // hibernate3的写法
                if (iter != null && iter.hasNext()) {
                    result.setRowCount(((Number) iter.next()).intValue());
                } else {
                    result.setRowCount(0);
                }
            }

            // 取指定页的数据
            // 将orderby的字段不限于param[0],推广到所有param都可以排序
            if (type == QUERY_TYPE_COUNT_AND_DATA || type == QUERY_TYPE_DATA_ONLY) {

                if (_orderby != null && _orderby.trim().length() > 0
                        && _desc != null) {
                    fromClause = fromClause.append(" order by ").append(getNewOrderby(_orderby, _desc, null));
                }

                Query query = session.createQuery(fromClause.toString());
                //hekun 2008-9-23, 启用查询缓存功能
                queryCacheSet(query); //设置缓存属性

                if (placeholders != null && placeholders.size() > 0) {
                    Set keyset = placeholders.keySet();
                    for (Iterator it = keyset.iterator(); it.hasNext(); ) {
                        String key = (String) it.next();
                        Object value = placeholders.get(key);
                        // 针对日期做特殊判断
                        if (dateParamList.indexOf(key) != -1) {
                            if (value.toString().length() == 10) {// yyyy-MM-dd
                                query.setDate(key, (new SimpleDateFormat(
                                        "yyyy-MM-dd")).parse(value.toString()));
                            } else {// yyyy-MM-dd HH:mm:ss
                                java.util.Date date = new SimpleDateFormat(
                                        "yyyy-MM-dd HH:mm:ss").parse(value
                                        .toString());
                                Timestamp ts = new Timestamp(date.getTime());
                                query.setTimestamp(key, ts);
                            }
                        } else {
                            query.setParameter(key, value);
                        }
                    }
                }

                if (_pagesize != 0) {
                    query.setMaxResults(_pagesize);
                    query.setFirstResult(_pagesize * (_pageno - 1));
                }

                List list = query.list();
                if (list == null) {
                    list = new ArrayList();
                }

                result.setDatas(list);
            }
        } catch (HibernateException ex) {
            ex.printStackTrace();
            if (ex.getCause() != null) {
                throw new Exception(ex.getCause());
            } else {
                throw ex;
            }
        }
        return result;
    }

    /* (non-Javadoc)
     * @see com.jambo.jop.infrastructure.db.hibernate3.BaseDAO#buildHQL(java.lang.Object, java.lang.String, java.util.Map, java.util.List)
     */
    public String buildHQL(Object param, String prefix, Map placeholders,
                           List dateParamList) throws Exception {
        return buildHQL(param, prefix, placeholders, dateParamList, 1);
    }

    /* (non-Javadoc)
     * @see com.jambo.jop.infrastructure.db.hibernate3.BaseDAO#buildHQL(java.lang.Object, java.lang.String, java.util.Map, java.util.List, int)
     */
    public String buildHQL(Object param, String prefix, Map placeholders,
                           List dateParamList, int ph) throws Exception {

//		He Kun 2008-1-28 判断，如果只使用 queryConditions 中的固定参数，则忽略动态参数，并且不生成where字句。
        if (param instanceof DBQueryParam) {
            DBQueryParam queryParam = (DBQueryParam) param;
            if (queryParam.isUseFixedParamOnly())
                return "";
        }

        if (prefix == null) {
            prefix = "";
        }

        if (prefix.trim().length() > 0) {
            prefix = prefix + ".";
        }

        StringBuffer whereClause = new StringBuffer();

        String key, field;
        Object value;

        Map props = getConditions(param);

        // 为查询条件排序，使用方法是设置ListVO的_firstitems属性，比如
        // listVO.set_firstitems("_ne_xxx,_se_yyy");
        // 那么生成的sql语句中关于_ne_xxx、_se_yyy的条件（假如有）就会排在前两位。
        // ----add by lwl
        List orderedKeys = getOrderedKeyset(props.keySet(), param);

        BaseVO voIns = (BaseVO) voClass.newInstance(); //to check property type use

        for (Iterator iter = orderedKeys.iterator(); iter.hasNext(); ) {
            key = (String) iter.next();
            // value = props.get(key)!=null ? String.valueOf( props.get(key) ) :
            // null;
            if (props.get(key) != null && props.get(key) instanceof Collection) {
                value = props.get(key);
            } else {
                value = props.get(key) != null ? props.get(key) : null;
            }

            // 忽略无值的参数
            if (value == null) {
                continue;
            }

            // 对于余下的检查是否符合规则
            try {
                field = key.substring(key.indexOf('_', 1) + 1);
            } catch (Exception ex) {
                continue;
            }

            // null条件的处理
            if (key.startsWith("_sn_") || key.startsWith("_dn_")
                    || key.startsWith("_nn_")) {
                whereClause = whereClause.append("(" + prefix + field
                        + " is null) and ");
            } else if (key.startsWith("_snn_") || key.startsWith("_dnn_")
                    || key.startsWith("_nnn_")) {
                whereClause = whereClause.append("(" + prefix + field
                        + " is not null) and ");
            } else {
                // 非 null条件的，参数必须有值
                if (value instanceof Collection) {
                    if (((Collection) value).size() <= 0) {
                        continue;
                    }
                } else {
                    if (value.toString().trim().length() <= 0) {
                        continue;
                    }
                }
                // 对日期类型做特殊处理，所以先保存下对应的参数名，例如k1，k2等
                // _dn _dnn _dl _dnm _de _dnl _dm _dne _din _dnin
                if (key.startsWith("_d")) {
                    if (key.startsWith("_dnl_") || key.startsWith("_dnm_")
                            || key.startsWith("_de_")
                            || key.startsWith("_dnn_")
                            || key.startsWith("_dl_") || key.startsWith("_dn_")
                            || key.startsWith("_dm_")
                            || key.startsWith("_dne_")
                            || key.startsWith("_din_")
                            || key.startsWith("_dnin_")) {
                        dateParamList.add("k" + ph);
                    }
                }
                // 只有向placeholders添加元素，才累加ph; 
                // ----- modified by zhangsiwei
                if (key.startsWith("_sql_")) {
                    whereClause = whereClause.append("(" + value + ") and ");
                } else if (key.startsWith("_sl_") || key.startsWith("_dl_")
                        || key.startsWith("_nl_")) {
                    whereClause = whereClause.append("(" + prefix + field
                            + " < :k" + ph + ") and ");
                    placeholders.put(String.valueOf("k" + ph++), value);
                } else if (key.startsWith("_snm_") || key.startsWith("_dnm_")
                        || key.startsWith("_nnm_")) {
                    whereClause = whereClause.append("(" + prefix + field
                            + " <= :k" + ph + ") and ");
                    placeholders.put(String.valueOf("k" + ph++), value);
                } else if (key.startsWith("_se_") || key.startsWith("_de_")
                        || key.startsWith("_ne_")) {
                    whereClause = whereClause.append("(" + prefix + field
                            + " = :k" + ph + ") and ");
                    placeholders.put(String.valueOf("k" + ph++), value);
                } else if (key.startsWith("_snl_") || key.startsWith("_dnl_")
                        || key.startsWith("_nnl_")) {
                    whereClause = whereClause.append("(" + prefix + field
                            + " >= :k" + ph + ") and ");
                    placeholders.put(String.valueOf("k" + ph++), value);
                } else if (key.startsWith("_sm_") || key.startsWith("_dm_")
                        || key.startsWith("_nm_")) {
                    whereClause = whereClause.append("(" + prefix + field
                            + " > :k" + ph + ") and ");
                    placeholders.put(String.valueOf("k" + ph++), value);
                } else if (key.startsWith("_sne_") || key.startsWith("_dne_")
                        || key.startsWith("_nne_")) {
                    whereClause = whereClause.append("(" + prefix + field
                            + " <> :k" + ph + ") and ");
                    placeholders.put(String.valueOf("k" + ph++), value);
                } else if (key.startsWith("_sin_") || key.startsWith("_din_")
                        || key.startsWith("_nin_")) {
                    ph = buildSQLInOrNotInCondition(value, whereClause, prefix,
                            field, ph, placeholders, true);
                } else if (key.startsWith("_snin_") || key.startsWith("_dnin_")
                        || key.startsWith("_nnin_")) {
                    ph = buildSQLInOrNotInCondition(value, whereClause, prefix,
                            field, ph, placeholders, false);
                } else if (key.startsWith("_sei_")) {
                    whereClause = whereClause.append("( lower(" + prefix
                            + field + ") = lower( :k" + ph + ")) and ");
                    placeholders.put(String.valueOf("k" + ph++), value);
                } else if (key.startsWith("_snei_")) {
                    whereClause = whereClause.append("( lower(" + prefix
                            + field + ") <> lower( :k" + ph + ")) and ");
                    placeholders.put(String.valueOf("k" + ph++), value);
                } else if (key.startsWith("_sk_")) {
                    value = "%" + value + "%";
                    whereClause = whereClause.append("(" + prefix + field
                            + " like :k" + ph + ") and ");
                    placeholders.put(String.valueOf("k" + ph++), value);
                } else if (key.startsWith("_ssw_")) {
                    value = value + "%";
                    whereClause = whereClause.append("(" + prefix + field
                            + " like :k" + ph + ") and ");
                    placeholders.put(String.valueOf("k" + ph++), value);
                } else if (key.startsWith("_sew_")) {
                    value = "%" + value;
                    whereClause = whereClause.append("(" + prefix + field
                            + " like :k" + ph + ") and ");
                    placeholders.put(String.valueOf("k" + ph++), value);
                } else if (key.startsWith("_snk_")) {
                    value = "%" + value + "%";
                    whereClause = whereClause.append("(" + prefix + field
                            + " not like :k" + ph + ") and ");
                    placeholders.put(String.valueOf("k" + ph++), value);
                } else if (key.startsWith("_ski_")) {
                    value = "%" + value + "%";
                    whereClause = whereClause.append("( lower(" + prefix
                            + field + ") like lower( :k" + ph + ")) and ");
                    placeholders.put(String.valueOf("k" + ph++), value);
                } else if (key.startsWith("_snki_")) {
                    value = "%" + value + "%";
                    whereClause = whereClause.append("( lower(" + prefix
                            + field + ") not like lower( :k" + ph + ")) and ");
                    placeholders.put(String.valueOf("k" + ph++), value);
                }
            }

            if (value != null && value.getClass() == String.class) {  //只对string类型做类型兼容型判断，避免给数值字段传入string的值
//				for this key
                PropertyDescriptor pd = PropertyUtils.getPropertyDescriptor(voIns, field);
//				处理数值类型。
                Object ovalue = null;
                try {
                    if (pd != null && pd.getPropertyType() != String.class) {
                        ph--;
                        ovalue = placeholders.get(String.valueOf("k" + ph));
                        if (ovalue != null)
                            if (pd.getPropertyType() == Long.class) {

                                Long lvalue = Long.valueOf((String) ovalue);
                                placeholders.put(String.valueOf("k" + ph), lvalue);
                            } else if (pd.getPropertyType() == Integer.class) {

                                Integer lvalue = Integer.valueOf((String) ovalue);
                                placeholders.put(String.valueOf("k" + ph), lvalue);

                            } else if (pd.getPropertyType() == Short.class) {

                                Short lvalue = Short.valueOf((String) ovalue);
                                placeholders.put(String.valueOf("k" + ph), lvalue);

                            } else if (pd.getPropertyType() == Byte.class) {

                                Byte lvalue = Byte.valueOf((String) ovalue);
                                placeholders.put(String.valueOf("k" + ph), lvalue);
                            }
                        ph++;
                    }
                } catch (NumberFormatException t) {
                    throw new NumberFormatException("Field type not match：field type " + pd.getPropertyType() + ", field value:" + ovalue);
                }
            }
//			ph++;   //下一个
        }

        if (whereClause.length() > 4) {
            whereClause = whereClause.delete(whereClause.length() - 4,
                    whereClause.length() - 1);
        }

        return whereClause.toString();
    }

    protected Map getConditions(Object param) throws IllegalAccessException,
            InvocationTargetException, NoSuchMethodException {

        // 静态条件部分
        Map props = BeanTools.describe(param);

        // new 动态条件部分 add by hekun
        if (param instanceof DBQueryParam) {
            DBQueryParam listVO = (DBQueryParam) param;
            Map queryConditions = listVO.getQueryConditions();

            if (queryConditions != null && queryConditions.size() > 0) {
                // 将静态条件加入动态条件中，重复的动态条件及其值将被覆盖。
                for (Iterator keys = props.keySet().iterator(); keys.hasNext(); ) {
                    String key = (String) keys.next();
                    Object value = props.get(key);
                    if (key.startsWith("_") && value != null)
                        queryConditions.put(key, value);
                }
                props = queryConditions;
            }
        }
        return props;
    }

    /* (non-Javadoc)
     * @see com.jambo.jop.infrastructure.db.hibernate3.BaseDAO#getCurrentTime()
     */
    public java.util.Date getCurrentTime() throws Exception {
        String sql = "select sysdate from dual";
        Session session = currentSession();

        SQLQuery query = session.createSQLQuery(sql);
        query.addScalar("sysdate", StandardBasicTypes.TIMESTAMP);
        // Hibernate.
        List ret = query.list();
        if (ret.size() > 0) {
            return (java.util.Date) ret.get(0);
        }
        return null;
    }

    protected List getOrderedKeyset(Set keys, Object param) throws Exception {
        List orderedKeyset = new ArrayList();
        if (keys.size() > 0) {
            String firstitems = BeanUtils.getProperty(param,
                    "_firstitems");
            String firstitemname = null;
            if (firstitems != null) {
                for (StringTokenizer st = new StringTokenizer(firstitems, ","); st
                        .hasMoreTokens(); ) {
                    firstitemname = st.nextToken();
                    if (keys.contains(firstitemname)) {
                        orderedKeyset.add(firstitemname);
                    }
                }
            }
            for (Iterator it = keys.iterator(); it.hasNext(); ) {
                String key = (String) it.next();
                if (!orderedKeyset.contains(key))
                    orderedKeyset.add(key);
            }
        }
        return orderedKeyset;
    }

    /**
     * add by Hanny Yeung
     *
     * @param inOrNotIn    true代表SQL中的in，false代表SQL中的not in

     */
    private int buildSQLInOrNotInCondition(Object value,
                                           StringBuffer whereClause, String prefix, String field, int ph,
                                           Map placeholders, boolean inOrNotIn) {
        Collection tmpList = (Collection) value;
        int tmpSize = tmpList.size();
        if (inOrNotIn) {
            whereClause = whereClause.append("( " + prefix + field + " in ( ");
        } else {
            whereClause = whereClause.append("( " + prefix + field
                    + " not in ( ");
        }
        Iterator iter = tmpList.iterator();
        int i = 0;
        while (iter.hasNext()) {
            Object obj = iter.next();
            if (i == tmpSize - 1) {
                whereClause.append(" :k" + ph + ")) and ");
            } else {
                whereClause.append(" :k" + ph + ",");
            }
            placeholders.put(String.valueOf("k" + ph++), obj);
            i++;
        }
        return ph;
    }

//	public static void main(String[] args) throws Exception {
    // 测试keys的排序用
    // com.jambo.boss.business.fee.billing.fixfee.persistent.FixFeeListVO
    // listvo = new
    // com.jambo.boss.business.fee.billing.fixfee.persistent.FixFeeListVO();
    // listvo.set_datasize("datasize");
    // listvo.set_desc("desc");
    // listvo.set_ne_acctid("_ne_acctid");
    // listvo.set_ne_subsid("_ne_subsid");
    // listvo.set_ne_validbillcyc("_ne_validbillcyc");
    // listvo.set_orderby("_orderby");
    // listvo.set_pageno("_pageno");
    // listvo.set_pagesize("_pagesize");
    // listvo.set_sk_mobileno("_sk_mobileno");
    // listvo.setAscending(true);
    // listvo.setDatas("datas");
    // listvo.setMobileno("mobileno");
    // listvo.setQueryConditions(null);
    // listvo.set_firstitems("_orderby,_ne_validbillcyc,class");
    // Map props = getConditions(listvo);
    // Set keys = props.keySet();
    // System.out.println("======Before Order " + keys.size() +"======");
    // for(Iterator it = keys.iterator();it.hasNext();){
    // String key = (String)it.next();
    // System.out.println(key);
    // }
    // List orderedKeys = getOrderedKeyset(keys,listvo);
    // System.out.println("======After Order " + orderedKeys.size()
    // +"======");
    // for(Iterator it = orderedKeys.iterator();it.hasNext();){
    // String key = (String)it.next();
    // System.out.println(key);
    // }
    // System.out.println("====== End ========");
//	}


    /* (non-Javadoc)
     * @see com.jambo.jop.infrastructure.db.hibernate3.BaseDAO#queryUniqueByNamedSqlQuery(java.lang.String, java.lang.Object)
     */
    public Object queryUniqueByNamedSqlQuery(String name, Object param) throws Exception {
        return queryUniqueByNamedSqlQuery(name, param, Integer.class);
    }

    /* (non-Javadoc)
     * @see com.jambo.jop.infrastructure.db.hibernate3.BaseDAO#queryUniqueByNamedSqlQuery(java.lang.String, java.lang.Object, java.lang.Class)
     */
    public Object queryUniqueByNamedSqlQuery(String name, Object param, Class returnType) throws Exception {
        Session session = currentSession();
        SQLQuery query = (SQLQuery) session.getNamedQuery(name);
        String queryString = query.getQueryString();

        return queryUniqueBySql(queryString, param, returnType, name);
    }

    /* (non-Javadoc)
     * @see com.jambo.jop.infrastructure.db.hibernate3.BaseDAO#queryCountByNamedSqlQuery(java.lang.String, java.lang.Object)
     */
    public DataPackage queryCountByNamedSqlQuery(String name, Object param) throws Exception {
        return queryByNamedSqlQuery(name, param, QUERY_TYPE_COUNT_ONLY);
    }

    /* (non-Javadoc)
     * @see com.jambo.jop.infrastructure.db.hibernate3.BaseDAO#queryByNamedSqlQuery(java.lang.String)
     */
    public DataPackage queryByNamedSqlQuery(String name) throws Exception {
        return queryByNamedSqlQuery(name, new DBQueryParam());
    }

    /* (non-Javadoc)
     * @see com.jambo.jop.infrastructure.db.hibernate3.BaseDAO#queryByNamedSqlQuery(java.lang.String, java.lang.Object)
     */
    public DataPackage queryByNamedSqlQuery(String name, Object param) throws Exception {
        return queryByNamedSqlQuery(name, param, ((DBQueryParam) param).getQueryType());
    }

    /* (non-Javadoc)
     * @see com.jambo.jop.infrastructure.db.hibernate3.BaseDAO#queryByNamedSqlQuery(java.lang.String, java.lang.Object, int)
     */
    public DataPackage queryByNamedSqlQuery(String name, Object param, int type) throws Exception {
        Session session = currentSession();

        SQLQuery query = (SQLQuery) session.getNamedQuery(name);

        String queryString = query.getQueryString();

        return queryBySql(queryString, param, type, name);
    }

    public Object queryUniqueBySql(String queryString, Object param, Class returnType) throws Exception {
        return queryUniqueBySql(queryString, param, returnType, "QUERY_NoNameUniqueQuery");
    }

    /* (non-Javadoc)
     * @see com.jambo.jop.infrastructure.db.hibernate3.BaseDAO#queryUniqueBySql(java.lang.String, java.lang.Object, java.lang.Class)
     */
    public Object queryUniqueBySql(String queryString, Object param, Class returnType, String queryName) throws Exception {

        Session session = currentSession();

        Type hibernateType = toHibernateType(returnType);
        SQLQuery query = session.createSQLQuery(queryString).addScalar("val", hibernateType);
        //缓存
        queryCacheSet(queryName, query); //设置缓存属性

        if (param instanceof DBQueryParam) {
            DBQueryParam listVO = (DBQueryParam) param;
            listVO.set_pagesize("0");
            listVO.set_orderby(null);
        }
        setQueryNamedParameters(query, param);

        List list = query.list();
        if (list.size() > 1)
            throw new NonUniqueResultException(list.size());
        if (list.size() > 0)
            return list.get(0);
        else
            throw new NonUniqueResultException(0);
    }

    private void setQueryNamedParameters(Query query, Object param) throws Exception {

        String[] parameters = query.getNamedParameters();
        Map parametersMap = new HashMap();
        for (String parameter : parameters) {
            parametersMap.put(parameter, null);
        }

        Map props = null;
        if (param instanceof DBQueryParam) {
            props = getConditions(param);
        } else if (param instanceof Map) {
            props = (Map) param;
        }

        if (props == null) return;

        Set orderedKeys = props.keySet();
        String key = null;
        Object value = null;
        for (Iterator iter = orderedKeys.iterator(); iter.hasNext(); ) {
            key = (String) iter.next();

            if (!parametersMap.containsKey(key))
                continue;

            value = props.get(key);
            if (value == null) continue;
            // added by He Kun, 2007-11-28 , 增加对绑定集合参数的支持			
            if (value instanceof Collection) {
                query.setParameterList(key, (Collection) value);
            } else if (value.getClass().isArray()) {
                query.setParameterList(key, (Object[]) value);
            } else {
                query.setParameter(key, value);
            }

        }
    }

    private Type toHibernateType(Class returnType) {

        if (returnType.equals(String.class)) {
            return StandardBasicTypes.STRING;
        } else if (returnType.equals(Long.class)) {
            return StandardBasicTypes.LONG;
        } else if (returnType.equals(Integer.class)) {
            return StandardBasicTypes.INTEGER;
        } else if (returnType.equals(Short.class)) {
            return StandardBasicTypes.SHORT;
        } else if (returnType.equals(Byte.class)) {
            return StandardBasicTypes.BYTE;
        } else if (returnType.equals(java.sql.Timestamp.class)) {
            return StandardBasicTypes.TIMESTAMP;
        } else if (returnType.isAssignableFrom(java.util.Date.class)) {
            return StandardBasicTypes.DATE;
        } else {
            throw new UnsupportedOperationException("Unsupported type " + returnType.getName());
        }
    }

    /* (non-Javadoc)
     * @see com.jambo.jop.infrastructure.db.hibernate3.BaseDAO#queryBySql(java.lang.String)
     */
    public DataPackage queryBySql(String queryString) throws Exception {
        return queryBySql(queryString, new DBQueryParam());

    }

    /* (non-Javadoc)
     * @see com.jambo.jop.infrastructure.db.hibernate3.BaseDAO#queryBySql(java.lang.String, java.lang.Object)
     */
    public DataPackage queryBySql(String queryString, Object param) throws Exception {
        DBQueryParam p = (DBQueryParam) param;

        return queryBySql(queryString, param, p.getQueryType());
    }

    protected DataPackage queryBySql(String queryString, Object param, int type) throws Exception {
        return queryBySql(queryString, param, type, "QUERY_NoNameQuery");
    }

    /**
     * 根据sql进行查询
     *
     * @param sql   要查询的sql语句
     * @param param 符合ListVO规范的查询参数对象。
     * @param type  查询类型：查询记录数(QUERY_TYPE_COUNT_ONLY),查询记录(QUERY_TYPE_DATA_ONLY),两者都查询(QUERY_TYPE_COUNT_AND_DATA)

     * @throws Exception
     */
    protected DataPackage queryBySql(String queryString, Object param, int type, String queryName) throws Exception {
        Session session = currentSession();
        // 查询结果
        DataPackage result = new DataPackage();
        StringBuffer countSQL = new StringBuffer();
        Map placeholders = new HashMap();
        List dateParamList = new ArrayList();
        StringBuffer whereClause = new StringBuffer();
        int _pagesize = 20, _pageno = 1;
        String _orderby = null, _desc = null;

        if (type != QUERY_TYPE_NATIVE_SQL){
//            StringBuffer countSQL = new StringBuffer("SELECT ");
            countSQL.append("SELECT ");
            countSQL.append(buildCountClause());
            countSQL.append(" total FROM (").append(queryString).append(") this ");

            try {
                _pagesize = Integer.parseInt(BeanUtils.getProperty(param, "_pagesize"));
            } catch (Exception ex) {
                _pagesize = Integer.MAX_VALUE;
            }
            if (_pagesize > MAX_QUERY_COUNT) {
                _pagesize = MAX_QUERY_COUNT; /** @todo 做一个查询量的限制，以后需要修改 */
            }

            try {
                _pageno = Integer.parseInt(BeanUtils.getProperty(param, "_pageno"));
            } catch (Exception ex) {
                _pageno = 1;
            }

            try {
                _orderby = BeanUtils.getProperty(param, "_orderby");
            } catch (Exception ex) {
                _orderby = "";
            }

            try {
                _desc = BeanUtils.getProperty(param, "_desc");
            } catch (Exception ex) {
                _desc = "";
            }
            if (_desc == null) {
                _desc = "0"; // 默认递增
            }

            whereClause = new StringBuffer(buildHQL(param, "this",
                    placeholders, dateParamList));
            if (whereClause.length() > 4) {
                countSQL = countSQL.append(" WHERE ").append(whereClause);
            }

            result.setPageNo(_pageno);
            result.setPageSize(_pagesize);
        }

        try {
            // 取总记录数
            if (type == QUERY_TYPE_COUNT_AND_DATA || type == QUERY_TYPE_COUNT_ONLY) {

                SQLQuery countQuery = session.createSQLQuery(countSQL.toString());
                //启用查询缓存
                queryCacheSet("QUERY_COUNT_" + queryName, countQuery); //设置缓存属性


                if (placeholders != null && placeholders.size() > 0) {
                    Set keyset = placeholders.keySet();
                    for (Iterator it = keyset.iterator(); it.hasNext(); ) {
                        String key = (String) it.next();
                        Object value = placeholders.get(key);
                        // 针对日期做特殊判断
                        if (dateParamList.indexOf(key) != -1) {
                            if (value.toString().length() == 10) {// yyyy-MM-dd
                                countQuery.setDate(key, (new SimpleDateFormat(
                                        "yyyy-MM-dd")).parse(value.toString()));
                            } else {// yyyy-MM-dd HH:mm:ss
                                java.util.Date date = new SimpleDateFormat(
                                        "yyyy-MM-dd HH:mm:ss").parse(value
                                        .toString());
                                Timestamp ts = new Timestamp(date.getTime());
                                countQuery.setTimestamp(key, ts);
                            }
                        } else {
                            countQuery.setParameter(key, value);
                        }
                    }
                }

                setQueryNamedParameters(countQuery, param);

                countQuery.addScalar("total", new IntegerType());
                result.setRowCount(((Number) countQuery.uniqueResult()).intValue());
            }


            /**
             * 修改逻辑，全部查找使用*号，部分查找
             */

            // 取指定页的数据
            if (type == QUERY_TYPE_COUNT_AND_DATA || type == QUERY_TYPE_DATA_ONLY || type == QUERY_TYPE_NATIVE_SQL) {
                SQLQuery query = null;
                boolean selectPartFiels = false;
                List selectFields = null;
                Map selectFieldsClass = null;
                boolean isSelectFieldsUseVOType = false;
                StringBuffer selectSQL = new StringBuffer(300);

                if (type != QUERY_TYPE_NATIVE_SQL) {
    //				StringBuffer selectSQL = new StringBuffer("select * from ( ").append( queryString).append(") this ");


                    selectSQL.append("select ");

    //                ClassMetadata class1 = session.getSessionFactory().getClassMetadata(voClass.getName());
    //                String[] pks = getPKs();
                    //如果设置了有选择查询，则按设置的字段集合进行查询，否则按所有字段查询。
                    if (param instanceof DBQueryParam) {
                        DBQueryParam dbparam = (DBQueryParam) param;
                        if (dbparam.getSelectFields() != null) {
                            selectFields = dbparam.getSelectFields();

                            if (dbparam.getSelectFieldsClass() != null) {
                                selectFieldsClass = dbparam.getSelectFieldsClass();
                            }
                        }

                        if (selectFields != null && selectFields.size() > 0) {
                            selectPartFiels = true;
                        }
                        isSelectFieldsUseVOType = dbparam.isSelectFieldsUseVOType();
                    }

                    if (!selectPartFiels) {
    //					//如果是所有字段，则先构造主键
    //					for(int i=0;i<pks.length; i++) {
    //						selectSQL.append(" this.").append(pks[i].toUpperCase()).append(" as ").append(" {ttt.").append(pks[i]).append("}");
    //						if(i < pks.length -1)	selectSQL.append(",");
    //					}
    //					selectFields = Arrays.asList(class1.getPropertyNames()); //除主键外地其他字段
    //					if(selectFields.size() > 0) selectSQL.append(", ");
    //
    //
    //					for (int i = 0; i < selectFields.size(); i++) {
    //						String colName = (String)selectFields.get(i);
    //
    //						selectSQL.append(" this.").append(colName.toUpperCase());
    //						if(!selectPartFiels) //如果只查询部分字段，则不需要设置别名，否则需要设置别名
    //							selectSQL.append(" as ").append(" {ttt.").append(colName).append("}");
    //
    //
    //						if(i < (selectFields.size() - 1))
    //							selectSQL.append(", ");
    //					}
                        selectSQL.append(" * ");

                    } else {
                        for (int i = 0; i < selectFields.size(); i++) {
                            String colName = (String) selectFields.get(i);

                            selectSQL.append(" this.").append(colName.toUpperCase());
                            if (!selectPartFiels) //如果只查询部分字段，则不需要设置别名，否则需要设置别名
                                selectSQL.append(" as ").append(" {ttt.").append(colName).append("}");


                            if (i < (selectFields.size() - 1))
                                selectSQL.append(", ");
                        }
                    }

                    selectSQL.append(" from (").append(queryString).append(" ) this  ");

                    if (whereClause.toString().trim().length() > 0) {
                        selectSQL = selectSQL.append(" WHERE ").append(whereClause);
                    }

                    if (_orderby != null && _orderby.trim().length() > 0 && _desc != null) {
                        selectSQL = selectSQL.append(" order by ").append(getNewOrderby(_orderby, _desc, "this."));
                    }
                } else {
                    selectSQL.append(queryString);
                }

                query = session.createSQLQuery(selectSQL.toString());
                queryCacheSet("QUERY_" + queryName, query); //设置缓存属性

                /**
                 * 设置字段映射
                 * 1) 如果只有1个字段，则按字段类型返回数据，例如：capital, 则按vo的属性类型 Long 返回集合，如果字段不是vo的属性，则按string类型返回
                 * 2) 如果有多个字段，并且字段都是vo的属性，则返回 vo的集合
                 * 3) 如果有多个字段，但字段不全是vo的属性，则返回 HashMap 的集合
                 */
                if (type != QUERY_TYPE_NATIVE_SQL) {
                    if (!selectPartFiels) { //按实体查询
                        query.addEntity("ttt", voClass);

                    } else { //设置字段映射，部分字段映射名称

                        for (int i = 0; i < selectFields.size(); i++) {
                            //设置查询结果中列的对应Hibernate类型
                            Type hibernateType = StandardBasicTypes.STRING;
                            String fieldName = (String) selectFields.get(i);
                            Class clazz = null;

                            if (selectFieldsClass != null)
                                clazz = (Class) selectFieldsClass.get(fieldName);

                            hibernateType = convert2HibernateType(fieldName, clazz);
                            //	按vo对应属性获取对应的类型
                            query.addScalar((String) selectFields.get(i), hibernateType);
                        }
                    }
                }

                setQueryNamedParameters(query, param);

                if (placeholders != null && placeholders.size() > 0) {
                    Set keyset = placeholders.keySet();
                    for (Iterator it = keyset.iterator(); it.hasNext(); ) {
                        String key = (String) it.next();
                        Object value = placeholders.get(key);
                        // 针对日期做特殊判断
                        if (dateParamList.indexOf(key) != -1) {
                            if (value.toString().length() == 10) {// yyyy-MM-dd
                                query.setDate(key, (new SimpleDateFormat("yyyy-MM-dd")).parse(value.toString()));
                            } else {// yyyy-MM-dd HH:mm:ss
                                java.util.Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(value.toString());
                                Timestamp ts = new Timestamp(date.getTime());
                                query.setTimestamp(key, ts);
                            }
                        } else {
                            query.setParameter(key, value);
                        }
                    }
                }

                if (_pagesize != 0 && type != QUERY_TYPE_NATIVE_SQL){
                    query.setMaxResults(_pagesize);
                    query.setFirstResult(_pagesize * (_pageno - 1));
                }

                List list = query.list();

                //2008-1-9,He Kun: 如果是只查部分字段，则需要对结果中没行的 Object[] 封装为 Map，方便操作。
                if (selectPartFiels)
                    list = wrapDataList(list, selectFields, isSelectFieldsUseVOType);

                if (list == null) {
                    list = new ArrayList();
                }

                result.setDatas(list);
            }
        } catch (HibernateException ex) {
            if (ex.getCause() != null) {
                throw new Exception(ex.getCause());
            } else {
                throw ex;
            }
        }
        return result;
    }

    /**
     * 将制定字段的类型的指定类型转换为Hibernate类型
     *
     * @param fieldClass 如果不指定时，将按 vo中对应属性的类型判断。
     */
    private Type convert2HibernateType(String fieldName, Class fieldClass) {
        Class fClass = fieldClass;
        Type type = null;

        if (fClass == null) {
            try {
                Object object = voClass.newInstance();
                PropertyDescriptor pd = PropertyUtils.getPropertyDescriptor(object, fieldName);
                if (pd != null)
                    fClass = pd.getPropertyType();
            } catch (Exception e) {
                if (log.isErrorEnabled())
                    log.error("无法创建vo 实例，" + e.getMessage(), e);
            }
        }
        //return Hibernate.STRING;
        if (fClass != null)
            type = (Type) javaTypeHibernateTypeMapping.get(fClass);

        if (type == null || fClass == null) {
            type = StandardBasicTypes.STRING;
        }

        return type;
    }

    /**
     * 将部分字段查询结果数据结构转换为易用的数据结构， VO，或 Map
     * 1) 如果只有1个字段，则按字段类型返回数据，例如：capital, 则按vo的属性类型 Long 返回集合，如果字段不是vo的属性，则按string类型返回
     * 2) 如果有多个字段，则返回 HashMap 的集合
     * 3) 如果有多个字段，并且字段都是vo的属性，并设置selectFieldsUseVOType 属性为true， 则返回VO的集合。
     */
    private List wrapDataList(List list, List selectFields, boolean isSelectFieldsUseVOType) {
        List list0 = new ArrayList(list.size());

        for (int i = 0; i < list.size(); i++) {


            if (selectFields.size() == 1) { //只有1列				
                list0.add(list.get(i));

            } else if (isSelectFieldsUseVOType && isAllVOFields(selectFields)) { //多列,并且字段全部是vo的字段

                Object[] objects = (Object[]) list.get(i);
                BaseVO vo = convert2VO(objects, selectFields);
                list0.add(vo);

            } else { //多列但字段不全部是vo的字段， 转换为Map
                Object[] objects = (Object[]) list.get(i);
                list0.add(convert2Map(objects, selectFields));
            }

        }
        return list0;
    }

    /**
     * 检查selectFields 中的字段是否都是vo对属性
     */
    private boolean isAllVOFields(List selectFields) {

        Map pd;
        try {
            pd = (Map) PropertyUtils.describe(voClass.newInstance());
            for (int i = 0; i < selectFields.size(); i++) {
                if (!pd.containsKey(
                        selectFields.get(i)))
                    return false;
                //包含非vo字段
            }
        } catch (Exception e) {
            if (log.isErrorEnabled())
                log.error("无法创建vo 实例, " + e.getMessage(), e);
            return false;
        }
        return true;
    }

    /**
     * 将object[]数据转换为VO
     *
     */
    private BaseVO convert2VO(Object[] objects, List selectFields) {
        try {
            if (objects.length > selectFields.size())
                log.warn("警告：结果集合字段数比 selectFields 中设定的字段数多" + objects.length + "> " + selectFields.size() + " 这可能会造成性能浪费，请保持他们的一致性。");

            BaseVO vo = (BaseVO) voClass.newInstance();
            for (int i = 0; i < selectFields.size(); i++) {
                BeanUtils.setProperty(vo, (String) selectFields.get(i), objects[i]);
            }
            return vo;
        } catch (Exception e) {
            if (log.isErrorEnabled())
                log.error("无法将结果集合按字段列表 " + selectFields + " 转换为对应的VO " + voClass.getName() + ", " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * 将一条 Object[] 型的记录转化为Map
     */
    private Map convert2Map(Object[] objects, List selectFields) {
        Map map = new HashMap((int) (objects.length * 1.34));
        for (int i = 0; i < objects.length; i++) {
            map.put((String) selectFields.get(i), objects[i]);
        }
        return map;
    }

    private String buildCountClause() {
        return buildCountClause(false);
    }

    private String buildCountClause(boolean isHQL) {
        //return " count(*) "; 

        //由于新项目库表采用 company_name 式样的名字，而属性名为 companyName，两者不一致，
        //所以 hql中无法支持,暂时采用 count(*).
        //解决方法， 获取voClass对应的表名， 然后采用count(1) 字句进行计数。
        String pks[] = null;
        StringBuffer buffer = null;
        try {
            buffer = new StringBuffer(16);
            buffer.append(" count(");
            pks = getPKs();

            if (pks.length > 1 || isHQL)
                buffer.append(" * ");
            else {
                if (pks.length == 1) {
//					if(isHQL ) 
//						buffer.append( pks[0] ); // pks[0]);
//					else
                    buffer.append("*"); //为避免跨数据库问题，改count(1)为都使用count(*)
                } else {
                    buffer.append("*");
                }
            }

            buffer.append(") ");

        } catch (Exception e) {
            return " count(*) ";
        }
        return buffer.toString();

    }

    /**
     * 获取实体类的主键名称，支持多主键
     *
     */
    protected String[] getPKs() throws Exception {
        Session session = currentSession();

        ClassMetadata metadata = session.getSessionFactory().getClassMetadata(this.voClass);

        if (metadata == null) {
            throw new NullPointerException("voClass " + voClass.getName() + " not configed in hibernate config!");
        }
        Type type = metadata.getIdentifierType();

        String[] pkNames = null;
        if (!(type instanceof CompositeType)) {
            String pkName = metadata.getIdentifierPropertyName();
            pkNames = new String[1];
            pkNames[0] = pkName;

        } else {
            CompositeType aType = (CompositeType) type;
            pkNames = aType.getPropertyNames();
        }
        return pkNames;
    }

    /* (non-Javadoc)
     * @see com.jambo.jop.infrastructure.db.hibernate3.BaseDAO#getSequence(java.lang.String)
     */
    public Object getSequence(String seqname) throws Exception {
        if (StringUtils.isBlank(seqname)) return null;
        StringBuffer sql = new StringBuffer("SELECT ").append(seqname).append(".NEXTVAL val FROM dual");
        Long seq = (Long) queryUniqueBySql(sql.toString(), null, Long.class);
        return seq;
    }

    protected void queryCacheSet(Query squery) {
        queryCacheSet(DEFAULT_HIBERNATE_QUERY_CACHE, squery);
    }

    /**
     * 为查询设置缓存属性。<br/>
     * 默认启用缓存，<br/>
     * 缓存区域名：HIBERNATE_QUERY_CACHE。<br/>
     * <p/>
     * 以后可以根据配置为不同的query设置不同的cache region，以便使用不同的缓存策略。<br/>
     */
    protected void queryCacheSet(String queryCacheRegion, Query squery) {
        squery.setCacheable(true);
        squery.setCacheRegion(queryCacheRegion);

        if (log.isDebugEnabled())
            log.debug("query cache region:" + queryCacheRegion);
    }

    /**
     * 修正多个orderby字段的bug
     *
     * @param _orderby
     * @param prefix
     */
    private String getNewOrderby(String _orderby,String _desc, String prefix) {
        if (prefix == null) {
            prefix = "";
        }
        String[] columns =
                StringUtils.split(_orderby, ",");
        String[] orderTypes =
                StringUtils.split(_desc, ",");

        String orderType = null;
        if(columns.length != orderTypes.length){
            orderType = orderTypes[0];
        }

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < columns.length; i++) {
            sb.append(prefix).append(columns[i]);
            if(orderType != null){
               if("1".equals(orderType)){
                   sb.append(" desc");
               }else{
                   sb.append(" asc");
               }
            }else{
               if("1".equals(orderTypes[i])){
                   sb.append(" desc");
               }else{
                   sb.append(" asc");
               }
            }

            if (i != columns.length - 1) {
                sb.append(",");
            }
        }
        return sb.toString();
    }
}
