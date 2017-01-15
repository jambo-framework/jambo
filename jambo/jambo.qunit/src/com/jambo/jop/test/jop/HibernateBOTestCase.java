package com.jambo.jop.test.jop;

import com.jambo.jop.common.spring.SpringContextManager;
import com.jambo.jop.test.base.BaseDBTestCase;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.internal.SessionFactoryImpl;

/**
 * <p>Title: BO测试用例基类,依赖JOP</p>
 * <p/>
 * <p>Description: 执行BO测试的准备工作以及提供准备数据及恢复数据的方法
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
 * @author  jinbo
 * @version 1.0
 * @version  modify by chenshijie 20130611 DBUnit增加支持公共库
 */
public class HibernateBOTestCase extends BaseDBTestCase {
    private static Logger log = LogManager.getLogger(HibernateBOTestCase.class);

    static {
        SpringContextManager.init("/spring/applicationContextTest.xml");

    }

    public HibernateBOTestCase(String s) {
        super(s);
    }

    protected void setUp() throws Exception {
//        connection=getConnection();
    }

    protected void tearDown() throws Exception {
//        connection.close();
//        connection = null;
    }

    protected IDatabaseConnection getConnection(){
        return  null;
    }

    @Override
    protected IDatabaseConnection getConnection(String dbBean, String schema) {
        IDatabaseConnection cn = null;
        Object o = SpringContextManager.getBean(dbBean);
        if (o instanceof SessionFactoryImpl) {
            try {
                if (schema != null){
                    cn = new DatabaseConnection(((SessionFactoryImpl)o).getConnectionProvider().getConnection(), schema);
                }else{
                    cn = new DatabaseConnection(((SessionFactoryImpl)o).getConnectionProvider().getConnection());
                }
                cn.getConnection().setAutoCommit(true);
            } catch (Exception e) {
                log.error("Can't get Connection from hibernate.", e);
            }
        }
        return cn;
    }
}
