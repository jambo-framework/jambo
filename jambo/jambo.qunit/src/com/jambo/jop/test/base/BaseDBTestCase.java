package com.jambo.jop.test.base;

import com.jambo.jop.infrastructure.db.DBAccessUser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dbunit.DatabaseTestCase;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DefaultDataSet;
import org.dbunit.dataset.DefaultTable;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;

import java.io.File;
import java.net.URL;
import java.sql.Connection;


/**
 * <p>Title: 数据库测试用例基类</p>
 * <p/>
 * <p>Description: 执行DAO测试的准备工作以及提供准备数据及恢复数据的方法
 * <br>
 * 来源于<a href="http://www-128.ibm.com/developerworks/cn/java/j-dbunit/index.html">
 * 用 DbUnit 和 Anthill 控制测试环境
 * </a>
 * </p>
 * <p/>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p/>
 * <p>Company: jambo-framework Tech Ltd.</p>
 *
 * @author Jerry Shang, jinbo
 * @version 1.1 modify by chenshijie 20130611 修改为抽象类 getConnection方法修改成抽象方法。
 */
public abstract class BaseDBTestCase extends DatabaseTestCase {
    private DBAccessUser user;
    public String schema;
    protected String testDB;

    /**
     * 用于数据操作的连接
     */
    protected IDatabaseConnection connection;
    protected static final Logger log = LogManager.getLogger(BaseDBTestCase.class);

    public BaseDBTestCase(String s) {
        super(s);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    protected IDataSet getDataSet() throws Exception {
        return new DefaultDataSet();
    }


    /**
     * 返回一个 DbUnit的数据库连接
     *
     * @return IDatabaseConnection DBUnit数据库连接
     * @throws Exception
     */
    protected abstract IDatabaseConnection getConnection() throws Exception;

    /**
     * 用beanname从spring里获取一个连接用来填充数据
     */
    protected abstract IDatabaseConnection getConnection(String dbBean, String schema);

    public DBAccessUser getUser() {
        if (user == null) {
            user = DBAccessUser.getInnerUser();
        }
        return user;
    }

    private IDataSet getFlatXmlDataSet(String tableName) throws Exception {
        URL url = BaseDBTestCase.class.getResource("/" + tableName);
        if (url == null) {
            throw new Exception("找不到数据文件[" + "/" + tableName + "]");
        }

        File file = new File(url.getPath());
        return new FlatXmlDataSetBuilder().build(file);
    }

    /**
     * 插入指定的表数据,如果相同主键的数据已经存在,则更新数据
     *
     * @param fileName String 带路径的数据文件名(不含后缀)
     * @throws Exception
     */
    protected void insertFileIntoDb(String fileName) throws Exception {
        insertFileIntoDb(testDB, fileName);
    }

    protected void insertFileIntoDb(String dbBean, String fileName) throws Exception {
        Connection conn = null;
        try {
            DatabaseOperation.REFRESH.execute(getConnection(dbBean, schema), getFlatXmlDataSet(fileName));
        } catch (Exception e) {
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(false);
                connection = null;
                conn.close();
            }
        }
    }

    /**
     * 删除指定的表数据
     *
     * @param fileName String 带路径的数据文件名(不含后缀)
     * @throws Exception
     */
    protected void deleteFileFromDb(String fileName) throws Exception {
        deleteFileFromDb(testDB, fileName);
    }

    protected void deleteFileFromDb(String dbBean, String fileName) throws Exception {
        Connection conn = null;
        try {
            DatabaseOperation.DELETE.execute(getConnection(dbBean, schema), getFlatXmlDataSet(fileName));
        } catch (Exception e) {
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(false);
                connection = null;
                conn.close();
            }
        }
    }

    /**
     * 清空表中所有数据
     *
     * @param tableName String 表名
     * @param dbBean
     * @throws Exception
     */
    protected void emptyTable(String tableName) throws Exception {
        emptyTable(testDB, tableName);
    }

    protected void emptyTable(String dbBean, String tableName) throws Exception {
        Connection conn = null;
        try {
            IDataSet dataSet = new DefaultDataSet(new DefaultTable(tableName));
            DatabaseOperation.DELETE_ALL.execute(getConnection(dbBean, schema), dataSet);
        } catch (Exception e) {
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(false);
                connection = null;
                conn.close();
            }
        }
    }

}
