package com.jambo.exam.comm;

import com.jambo.jop.common.utils.i18n.I18nMessage;
import com.jambo.jop.exception.JOPException;
import junit.framework.TestCase;

/**
 * User: jinbo
 * Date: 13-7-28
 * Time: 下午11:12
 */
public class JOPUtilsTest extends TestCase{

    public JOPUtilsTest() throws Exception {
        super("JOPUtilsTest");


    }

    public void testPublicMsg(){
        assertEquals(I18nMessage.getPublicString("button_build"), "构建");

        Exception e = new JOPException("delegate.common");
        e.printStackTrace();
    }

    public void testMessageFormat(){
        assertEquals(I18nMessage.getString("i18n.public", "test.msg", new String[]{"testmsg001", JOPUtilsTest.class.getName()}), "测试KEY[testmsg001] 测试消息对象[com.jambo.exam.comm.JOPUtilsTest]");
    }

}
