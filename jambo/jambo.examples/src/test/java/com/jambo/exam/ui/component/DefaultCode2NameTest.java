package com.jambo.exam.ui.component;


import com.jambo.jop.infrastructure.component.Code2Name;
import com.jambo.jop.infrastructure.component.impl.DefaultCode2Name;
import com.jambo.jop.test.jop.HibernateBOTestCase;

public class DefaultCode2NameTest extends HibernateBOTestCase {
	private Code2Name testObj;

	public DefaultCode2NameTest() {
        super("Code2Name Test");
	}

	
    protected void setUp() throws Exception {
        super.setUp();
        testObj = new DefaultCode2Name();
    }

    protected void tearDown() throws Exception {
    	testObj  = null;
    	
    	super.tearDown();
    }
    

    public void testCode2Name(){
        String value = testObj.code2Name("TARGET_SYSTEM", "EJB_DEMO_SYS", "COMMON");
        System.out.println(value);

        value = testObj.code2Name("#COMPANY", "1", "COMMON");
    	
    	System.out.println(value);
    }

    public void testCode2NameByField(){
    	String value = testObj.code2Name("#COMPANY_FIELD", "999", "COMMON");

    	System.out.println(value);
    }

    public void testCode2NameBySql(){
    	String value = testObj.code2Name("#COMPANY_SQL", "999", "COMMON");
    	
    	System.out.println(value);
    }
}
