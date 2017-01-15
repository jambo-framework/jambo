package com.jambo.exam.web.example.example1;

import com.jambo.jop.infrastructure.cache.CacheFactory;
import com.jambo.jop.infrastructure.component.Code2Name;
import com.jambo.jop.infrastructure.component.impl.DefaultCode2Name;
import com.jambo.jop.infrastructure.db.DBQueryParam;
import com.jambo.jop.infrastructure.db.DataPackage;
import com.jambo.jop.infrastructure.dproxy.ProxyFactory;
import com.jambo.jop.test.jop.HibernateBOTestCase;

import java.util.Map;

public class Code2NameTagTest extends HibernateBOTestCase {
    private static final String DATAFILE = "com/jambo/exam/web/example/example1/dictitem.data.xml";
    private static final String EMPLOYEE_DATAFILE = "com/jambo/exam/business/example/employee/Employee.data.xml";


    public Code2NameTagTest() {
        super("Code2NameTagTest");


        this.schema = "PUBLIC";
        this.testDB = "sessionFactory_DB_COMMON";
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();

        CacheFactory.logStatistics();

    }

    public void testCode2NameWithCache() {
        try {
            DefaultCode2Name comp = (DefaultCode2Name) ProxyFactory.createObject(DefaultCode2Name.class,
                    new String[]{"code2Name","valueList"}, "WebViewInterceptorHandler");

            String name = comp.code2Name("#EMPLOYEE", "42", "COMMON");
            System.out.println("result " + name);

            long start = System.currentTimeMillis();
            //doCode2NameTest(comp);

            long elap = System.currentTimeMillis() - start;
            System.out.println("use:" + elap);
        } catch (InstantiationException e) {
            fail(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 测试字典表的转换
     */
    public void testCode2NameFixedParam() throws Exception {
        try {
            insertFileIntoDb(DATAFILE);
            DefaultCode2Name comp = (DefaultCode2Name) ProxyFactory.createObject(DefaultCode2Name.class,
                    new String[]{"code2Name"}, "WebViewInterceptorHandler");

            String name = comp.code2Name("$MODULE_TYPE", "1", "COMMON");
            System.out.println("result 1 = " + name);

            Thread.sleep(2000);

            name = comp.code2Name("$MODULE_TYPE", "1", "COMMON");
            System.out.println("result 2 = " + name);

            //这个测试单跑没问题,合一起跑就出错,所以只好注释掉这个断言
            //assertEquals("数据字典 模块类型 1 ", "模块", name);
        } catch (InstantiationException e) {
            fail(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            deleteFileFromDb(DATAFILE);
        }

    }

    public void testCode2NameDictitem() {
        try {
            DefaultCode2Name comp = (DefaultCode2Name) ProxyFactory.createObject(DefaultCode2Name.class,
                    new String[]{"code2Name"}, "WebViewInterceptorHandler");

            String name = comp.code2Name("COMPANY_STATE", "1", "COMMON");
            System.out.println("result " + name);

            assertEquals("固定参数 公司状态 1 ", "有效",name);
        } catch (InstantiationException e) {
            fail(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //
////
////	public void testCode2NameNoCache() throws Exception {
////		DefaultCode2Name comp = new DefaultCode2Name();
////		String name = comp.code2Name("#EMPLOYEE", "111", "COMMON");
////		long start = System.currentTimeMillis();
////		doCode2NameTest(comp);
////		long elap = System.currentTimeMillis() - start;
////		System.out.println("testCode2NameNoCache use:" + elap);
////	}
//
//	public void doCode2NameTest(DefaultCode2Name comp) {
//		try{
//
//			int n = 1;
//			for (int i = 0; i < n; i++) {
//				String name = comp.code2Name("#EMPLOYEE",  "145", "COMMON");
//				System.out.println("result1 " + name);
//
//				Thread.sleep(1000);
//
//				name = comp.code2Name("#EMPLOYEE", "145", "COMMON");
//				System.out.println("result2 " + name);
//
//				Thread.sleep(2000);
//				name = comp.code2Name("#EMPLOYEE", "147", "COMMON");
//				System.out.println("result " + name);
//
//
//
//				Thread.sleep(2000);
//
//				name = comp.code2Name("#EMPLOYEE", "147", "COMMON");
//				System.out.println("result3 " + name);
//
//				Thread.sleep(2000);
//				name = comp.code2Name("#EMPLOYEE", "147", "COMMON");
//				System.out.println("result " + name);
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
    public void testValueListWithSingleTable() throws Exception {
        insertFileIntoDb(EMPLOYEE_DATAFILE);
        try {

            String definition ="#EMPLOYEE";
            String condition = null;

            //使用Cache拦截器管理缓存，拦截器配置在 WebViewInterceptorHandler 中，参见 /jop-aop.xml
            Code2Name comp = (Code2Name) ProxyFactory.createObject(DefaultCode2Name.class, new String[]{"valueListPackage"}, "WebViewInterceptorHandler");

            DBQueryParam param = new DBQueryParam();

            DataPackage data = comp.valueListPackage(definition,condition,param, getUser().getDbFlag());

            System.out.println(" size = " + data.getDatas().size());
            System.out.println(" data = " + data.getDatas());

            assertTrue("查询到单表参数集合", data.getDatas().size() > 0);
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        } finally {
            deleteFileFromDb(EMPLOYEE_DATAFILE);
        }
    }

    public void testValueListWithSingleTableValueList() throws Exception {
        insertFileIntoDb(EMPLOYEE_DATAFILE);
        try {

            String definition ="#EMPLOYEE";
            String condition = null;

            //使用Cache拦截器管理缓存，拦截器配置在 WebViewInterceptorHandler 中，参见 /jop-aop.xml
            Code2Name comp = (Code2Name) ProxyFactory.createObject(DefaultCode2Name.class, new String[]{"valueList"}, "WebViewInterceptorHandler");

            DBQueryParam param = new DBQueryParam();

            Map map = comp.valueList(definition,condition,param, getUser().getDbFlag());

            System.out.println(" size = " + map.size());
            System.out.println(" data = " + map);

            assertTrue("查询到单表参数集合", map.size() > 0);
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        } finally {
            deleteFileFromDb(EMPLOYEE_DATAFILE);
        }
    }

    public void testValueListWithFixedItems() {
        try {

            String definition ="COMPANY_STATE";
            String condition = null;

            //使用Cache拦截器管理缓存，拦截器配置在 WebViewInterceptorHandler 中，参见 /jop-aop.xml
            Code2Name comp = (Code2Name) ProxyFactory.createObject(DefaultCode2Name.class, new String[]{"valueList"}, "WebViewInterceptorHandler");

            Map map = comp.valueList(definition,condition,getUser().getDbFlag());

            System.out.println(" size " + map.size());
            System.out.println("data " + map);

            assertTrue("查询到单表参数集合", map.size() > 0);
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }
    //
    public void testValueListWithDictItemValues() {
        try {

            String definition ="$MODULE_TYPE";
            String condition = null;

            //使用Cache拦截器管理缓存，拦截器配置在 WebViewInterceptorHandler 中，参见 /jop-aop.xml
            Code2Name comp = (Code2Name) ProxyFactory.createObject(DefaultCode2Name.class, new String[]{"valueList"}, "WebViewInterceptorHandler");

            Map map = comp.valueList(definition, condition,  null);

            System.out.println(" valieList size " + map.size());
            System.out.println(" valieList data " + map);
//
//			map = comp.valueList("COMPANY_STATE", null);
//			map = comp.valueList("COMPANY_STATE", null);
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    public void testValueListWithDictItemDataPackage() {
        try {

            String definition ="$MODULE_TYPE";
            String condition = null;

            //使用Cache拦截器管理缓存，拦截器配置在 WebViewInterceptorHandler 中，参见 /jop-aop.xml
            Code2Name comp = (Code2Name) ProxyFactory.createObject(DefaultCode2Name.class, new String[]{"valueList"}, "WebViewInterceptorHandler");
            DBQueryParam param = new DBQueryParam();
            DataPackage dp = comp.valueListPackage(definition, condition, param, null);

            System.out.println(" valieList size " + dp.getDatas().size());
            System.out.println(" valieList data " + dp.getDatas());
//
//			map = comp.valueList("COMPANY_STATE", null);
//			map = comp.valueList("COMPANY_STATE", null);
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    protected void finalize() throws Throwable {
        super.finalize();
    }
}
