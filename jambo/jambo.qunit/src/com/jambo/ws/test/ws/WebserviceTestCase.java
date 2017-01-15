package com.jambo.ws.test.ws;

import com.jambo.ws.test.base.io.FileUtils;
import com.jambo.ws.test.base.io.net.HttpRequestUtils;
import com.jambo.ws.test.base.io.net.RequestException;
import junit.framework.TestCase;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: 彭宙硕
 * Date: 13-8-29
 * Time: 下午4:53
 */
public class WebserviceTestCase extends TestCase {

    /**
     * 通过HTTP请求发送Webservice请求报文并返回响应报文
     * @param url URL路径
     * @param message 请求报文
     * @return 响应报文
     */
    public static String getResponseXml(String url, String message){
       Map<String, String> params = new HashMap<String, String>();
       params.put("", message);
        try{
            return HttpRequestUtils.doPost(url, params);
        }catch (RequestException e){
            throw new RuntimeException(e);
        }
    }

    /**
     * 通过HTTP请求发送指定目录下的Webservice请求报文，并将返回结果与指定目录下的期望输出报文比较,一致时返回true，否则返回false
     * @param url URL路径
     * @param inputXmlPath 请求报文XML的绝对路径
     * @param expectOutputXmlPath 期望输出的XML报文绝对路径
     * @return
     */
    public void assertEqual(String url, String inputXmlPath, String expectOutputXmlPath){
        //请求报文
        String message = FileUtils.readFileToString(inputXmlPath, "UTF-8").toString();
        //实际响应报文
        String responseXml = getResponseXml(url, message);
        //期望响应报文
        String expectResponseXml = FileUtils.readFileToString(expectOutputXmlPath, "UTF-8").toString();
        if(responseXml != null){
            TestCase.assertEquals(expectResponseXml, responseXml);
        }
        TestCase.assertNotNull(responseXml);
     }
}
