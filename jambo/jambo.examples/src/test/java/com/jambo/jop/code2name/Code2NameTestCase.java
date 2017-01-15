package com.jambo.jop.code2name;

import com.jambo.jop.common.utils.lang.Code2NameUtils;
import com.jambo.jop.infrastructure.db.DBQueryParam;
import com.jambo.jop.infrastructure.db.DataPackage;
import com.jambo.jop.test.jop.HibernateBOTestCase;

import java.util.Map;

public class Code2NameTestCase extends HibernateBOTestCase {
//	private Code2Name testObj;

	public Code2NameTestCase() {
        super("Code2Name Test");
	}

	
    protected void setUp() throws Exception {
        super.setUp();
//        testObj = new DefaultCode2Name();
    }

    protected void tearDown() throws Exception {
    	super.tearDown();
    }
    

    public void testCode2NameByField(){
    	String value = Code2NameUtils.code2Name("TARGET_SYSTEM", "EJB_DEMO_SYS", "COMMON");
    	System.out.println(value);
    }

    public void testCode2NameBySql(){
    	String value = Code2NameUtils.code2Name("#COMPANY", "10", "COMMON");
    	
    	System.out.println(value);
    }

    public void testCode2NameByCache(){
        String value = Code2NameUtils.code2Name("TARGET_SYSTEM", "OUT", "COMMON");
    	System.out.println(value);

        //测试是否用到了缓存
        value = Code2NameUtils.code2Name("TARGET_SYSTEM", "OUT", "COMMON");
    	System.out.println(value);
    }

    public void testCode2NameListByCache(){
        DBQueryParam param = new DBQueryParam();
        param.set_pagesize("30");

        Map map = Code2NameUtils.valueList("TARGET_SYSTEM", "", param, "COMMON");

        System.out.println("size : " + map.size());

        map = null;
        //测试是否用到了缓存
        map = Code2NameUtils.valueList("TARGET_SYSTEM", "", param, "COMMON");

        System.out.println("size : " + map.size());
    }

    public void testCode2NameValueListPackage() {
        DBQueryParam param = new DBQueryParam();
        DataPackage dp = Code2NameUtils.valueListPackage("#COMPANY", "", param, "COMMON");
        System.out.println("size : " + dp.getRowCount());

    }
}
