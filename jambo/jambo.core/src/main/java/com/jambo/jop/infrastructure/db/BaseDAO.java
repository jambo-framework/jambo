package com.jambo.jop.infrastructure.db;

import org.hibernate.Session;

import javax.transaction.TransactionManager;
import java.io.Serializable;
import java.util.Collection;


public interface BaseDAO {

	/**
	 * 查询类型,查询所有内容,即同时查询数据和查询分页所需要的数据总量
	 */
	public static final int QUERY_TYPE_COUNT_AND_DATA = 0;

    /**
     * 查询类型,只查询符合条件的数据总量
     */
	public static final int QUERY_TYPE_COUNT_ONLY = 10;

    /**
     * 查询类型,只查询数据，不查询总量，效率比较高
     */
	public static final int QUERY_TYPE_DATA_ONLY = 20;

    /**
     * 查询类型,给queryBySql用，查询时,直接使自定义sql查询，不再生成select a,b,c from () this
     */
    public static final int QUERY_TYPE_NATIVE_SQL = 30;

	public static final int MAX_QUERY_COUNT = 10000;

	public abstract Class getVoClass();

	public abstract void setVoClass(Class voClass);

	public abstract String getDbFlag();

	public abstract void setDbFlag(String dbFlag);
	
	/**
	 * 获取数据库会话管理器
	 */
    public SessionManager getSessionManager();

    /**
     * 设置数据库会话管理器,为支持其它ORM准备
     */
	public void setSessionManager(SessionManager sessionManager) ;


    /**
     * 取当前DAO的事务管理器
     */
    public TransactionManager getTransactionManager();

	/**
	 * 清除缓存中的内容
	 *
	 * @throws Exception
	 */
	public abstract void evict() throws Exception;

	public abstract void evict(Serializable id) throws Exception;

	

	public abstract Object create(Object vo) throws Exception;

	public abstract void removeByPk(Serializable pk) throws Exception;

	/**
	 * 删除记录
	 *
	 * @param vo
	 *            要删除的VO
	 * @throws Exception
	 *             删除失败
	 */
	public abstract void remove(Object vo) throws Exception;

	public abstract Object update(Object vo) throws Exception;

	public abstract Object findByPk(Serializable pk) throws Exception;

	public abstract Object findByUniqueKey(String uniqueKeyPropertyName,
			Serializable id) throws Exception;

	public abstract Collection findAll() throws Exception;

	/**
	 * 取得表中某一个字段的最大值，如表中无记录则返回null
	 */
	public abstract Object getMaxValue(String prop) throws Exception;

	/**
	 * 取得表中某一个字段的最大值，如表中无记录则返回null
	 */
	public abstract Object getMaxid(String id, String value, String prop)
			throws Exception;

	/**
	 * 根据表中的一个字段，按 prop = value，查询符合条件的第一条记录 但注意，这里的prop不适用于所有的集合属性，例如
	 * many-to-one 那些 除非这些属性用PK关联。
	 */
	public abstract Object findByProperty(String prop, Object value)
			throws Exception;

	/**
	 * 根据表中的一个字段，按 prop = value，查询符合条件的记录数 但注意，这里的prop不适用于所有的集合属性，例如 many-to-one
	 * 那些 除非这些属性用PK关联。
	 */
	public abstract int countByProperty(String prop, Object value)
			throws Exception;

	/**
	 * 根据网页的请求，生成相应查询的条件（where）子句。例如网页上字段 _se_name 的值为 gaven，则生成的条件子句为：name =
	 * 'gaven'。有多个字段的，则它们生成的条件是AND关系， 如果一个字段有多个值的，则这个字段生成的条件为OR关系。（这里还未实现OR的关系）
	 * 网页上的字段要求一定的命名规则，字段名：前缀 + '_' + 字段名，前缀：'_' + 类型 + 运算
	 *
	 * <pre>
	 *   前缀列表：
	 *   类型 空  非空  &lt;   &lt;=   =   &gt;=   &gt;   &lt;&gt;   in   not in
	 *   字串 _sn _snn _sl _snm _se _snl _sm _sne _sin _snin
	 *   数字 _nn _nnn _nl _nnm _ne _nnl _nm _nne _nin _nnin
	 *   日期 _dn _dnn _dl _dnm _de _dnl _dm _dne _din _dnin
	 *   字符串的特殊运算：
	 *   (like) (not like) (= 忽略大小写) (&lt;&gt; 忽略大小写) (like 忽略大小写) (not like 忽略大小写) (start with) (end with)
	 *   _sk    _snk       _sei          _snei         _ski             _snki                _ssw				_sew
	 *   直接的SQL条件：
	 *   _sql_
	 *   
	 *   params._queryexpress仅为true（不区分大小写）时，只查询数据，不查询数据数目总数。
	 * </pre>
	 */
	public abstract DataPackage query(Object params) throws Exception;

	public abstract int count(Object params) throws Exception;

	/**
	 * 查询，当type为10时，只查记录数，不查具体的数据 而type为0时，则两样都查
	 */
	public abstract DataPackage query(Object params, int type) throws Exception;

	/**
	 * 多表查询方法
	 */
	public abstract DataPackage unionQuery(Object params[], Class vo[],
			String joins[][]) throws Exception;

	/**
	 * 多表查询方法
	 */
	public abstract DataPackage unionQuery(Object params[], Class vo[],
			String joins[][], int type) throws Exception;


	/**
	 * 取数据库当前日期
	 */
	public abstract java.util.Date getCurrentTime() throws Exception;

	public abstract Object queryUniqueByNamedSqlQuery(String name, Object params)
			throws Exception;

	/**
	 * 按配置的named Sql Query 进行查询，查询唯一结果
	 * @param name  hbm.xml 中配置的 Sql Query 的名称
	 * @return
	 * @throws Exception
	 */
	public abstract Object queryUniqueByNamedSqlQuery(String name,
			Object params, Class returnType) throws Exception;

	/**
	 * 按配置的named Sql Query 进行查询, 只查询记录总数。获取方法 DataPackage.getRowCount()
	 * @param name
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public abstract DataPackage queryCountByNamedSqlQuery(String name,
			Object params) throws Exception;

	/**
	 * 按配置的named Sql Query 进行查询。 记录数，记录集合都查询, 无附加查询条件，记录集合不分页，全部查处。
	 * @param name  hbm.xml 中配置的 Sql Query 的名称
	 * @return
	 * @throws Exception
	 */
	public abstract DataPackage queryByNamedSqlQuery(String name)
			throws Exception;

	/**
	 * 按配置的named Sql Query 进行查询。 记录数，记录集合都查询。按params中的条件进行分页，排序。
	 * @param name  hbm.xml 中配置的 Sql Query 的名称
	 * @param params 符合ListVO规范的查询参数对象。
	 * @return
	 * @throws Exception
	 */
	public abstract DataPackage queryByNamedSqlQuery(String name, Object params)
			throws Exception;

	/**
	 * 按配置的named Sql Query 进行查询。
	 * @param name hbm.xml 中配置的 Sql Query 的名称
	 * @param params 符合ListVO规范的查询参数对象。
	 * @param type 查询类型：查询记录数(QUERY_TYPE_COUNT_ONLY),查询记录(QUERY_TYPE_DATA_ONLY),两者都查询(QUERY_TYPE_COUNT_AND_DATA)
	 * @return
	 * @throws Exception
	 */
	public abstract DataPackage queryByNamedSqlQuery(String name,
			Object params, int type) throws Exception;

	/**
	 * 根据sql进行查询. 查询唯一结果。
	 * @param sql 要查询的sql语句
	 * @return
	 * @throws Exception
	 */
	public Object queryUniqueBySql(String queryString, Object params,Class returnType) throws Exception;
	
	
	/* (non-Javadoc)
	 * @see com.jambo.jop.infrastructure.db.hibernate3.BaseDAO#queryUniqueBySql(java.lang.String, java.lang.Object, java.lang.Class)
	 */
	public Object queryUniqueBySql(String queryString, Object param,Class returnType,String queryName) throws Exception ;
	
	/**
	 * 根据sql进行查询. 记录数，记录集合两者都查询
	 * @param sql 要查询的sql语句
	 * @return
	 * @throws Exception
	 */
	public DataPackage queryBySql(String queryString) throws Exception;

	/**
	 * 根据sql进行查询. 记录数，记录集合两者都查询
	 * @param sql 要查询的sql语句
	 * @param params  符合ListVO规范的查询参数对象。 
	 * @return
	 * @throws Exception
	 */
	public DataPackage queryBySql(String queryString, Object params)	throws Exception;

	/**
	 * 根据SEQUENCE名称取得seq nextval的值
	 * @param seq
	 */
	public Object getSequence(String seqname) throws Exception;

    /**
     * 清理hibernate的session缓存
     */
    public void clearSession() throws Exception;

    /**
     * 获取当前使用的Session
     */
    public abstract Session getSession() throws Exception;
}