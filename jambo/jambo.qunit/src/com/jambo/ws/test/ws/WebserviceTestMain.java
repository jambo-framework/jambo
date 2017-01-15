package com.jambo.ws.test.ws;


import com.jambo.ws.test.base.io.FileUtils;
import com.jambo.ws.test.base.io.net.HttpRequestUtils;
import com.jambo.ws.test.base.io.net.RequestException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: 彭宙硕
 * Date: 13-8-30
 * Time: 上午11:44
 */
public class WebserviceTestMain {

    private static final Logger log = LogManager.getLogger(WebserviceTestMain.class);

    public static void main(String[] args){
        System.out.println("Please input testing root path");
        String rootPathInput = new Scanner(System.in).nextLine();
        File file = new File(rootPathInput);
        while(!file.exists()){
            System.out.println("File " + rootPathInput + " not exists, please input another path again");
            rootPathInput = new Scanner(System.in).nextLine();
            file = new File(rootPathInput);
        }

        //目录结构为： 根目录/项目/模块/子模块
        //子模块下面为目录结构为 input目录 output目录 conf.properties文件
        System.out.println("WebService自动测试...");
        String rootDir = rootPathInput;
        File rootFile = new File(rootDir);
        if(rootFile.exists()){
            System.out.println("根目录为" + rootDir);
            File[] projectFiles = rootFile.listFiles();
            if(projectFiles!=null && projectFiles.length > 0){
                //遍历每个项目目录
                for(File projectFile : projectFiles){
                    if(projectFile.isDirectory()){
                        System.out.println("进入项目目录：" + projectFile.getName());
                        handleProject(projectFile);
                    }
                }
            }else{
                System.out.println("未找到项目目录");
            }
        }else{
            System.err.println("根目录" + rootDir + "不存在");
        }
        System.out.println("Webservice自动测试结束。");
    }

    /**
     * 处理项目目录
     * @param projectFileDir
     */
    public static void handleProject(File projectFileDir){
        //遍历每个模块目录
        File[] modelFiles = projectFileDir.listFiles();
        System.out.println("正在测试项目: " + projectFileDir.getName() + " 的Webservice接口......");
        //每个子模块的统计结果列表
        List<WebServiceTestResultStatis> resultStatisList = new ArrayList<WebServiceTestResultStatis>();
        for(File modelFile : modelFiles){
            if(modelFile.isDirectory()){
                System.out.println("正在测试模块：" + modelFile.getName() + "...");
                File[] subModelFiles = modelFile.listFiles();
                //遍历每个子模块记录
                for(File subModelFile : subModelFiles){
                    if(subModelFile.isDirectory()){
                        System.out.println("正在测试子模块: "+ subModelFile.getName() + "...");
                        //处理子模块
                        WebServiceTestResultStatis resultStatis = handleSubModel(subModelFile);
                        resultStatis.setModelName(subModelFile.getName());
                        resultStatisList.add(resultStatis);
                    }
                }
            }
        }
        System.out.println("*****************项目：" +  projectFileDir.getName() + "的统计报告******************************");
        System.out.println("模块名称\t\t" + "总数\t\t" + "成功\t\t" + "失败\t\t");
        for(WebServiceTestResultStatis resultStatis : resultStatisList){
            System.out.println(resultStatis.getModelName() + "\t\t" + resultStatis.getTotalCount() + "\t\t" + resultStatis.getSuccessCount() + "\t\t" + resultStatis.getFailureCount() + "\t\t");
        }
        System.out.println("******************************************************************************************");
    }

    /**
     * 处理子模块
     * @param subModelFileDir
     */
    public static WebServiceTestResultStatis handleSubModel(File subModelFileDir){
        File confFile = new File(subModelFileDir.getPath() + File.separator + "conf.properties");
        if(confFile.exists()){
            Properties propertie = new Properties();
            try {
                propertie.load(new BufferedInputStream(new FileInputStream(confFile)));
            } catch (IOException e) {
                log.error(e);
            }
            String url = propertie.getProperty("url");
            File inputDirPath = new File(subModelFileDir.getPath() + File.separator + "input");
            File[] inputFiles = inputDirPath.listFiles();
            File outputDirPath = new File(subModelFileDir.getPath() + File.separator + "output");
            File[] outputFiles = outputDirPath.listFiles();
            if(url!=null && url.length() > 0 && inputFiles!=null && outputFiles.length >  0 && inputFiles.length == outputFiles.length){
                return handleItem(url, inputFiles, outputFiles);
            }
        }else{
            System.out.println("未找到配置文件conf.properties");
        }
        return new WebServiceTestResultStatis();
    }

    /**
     * 处理子模块下的所有测试任务
     * @param url
     * @param inputFiles
     * @param outputFiles
     */
    public static WebServiceTestResultStatis handleItem(String url, File[] inputFiles, File[] outputFiles){
        int successCount = 0;
        int failureCount = 0;
        for(int i=0; i<inputFiles.length; i++){
            if(assertEqual(url, inputFiles[i].getPath(), outputFiles[i].getPath()) == 1){
                successCount++;
            }else{
                failureCount++;
            }
        }
        WebServiceTestResultStatis resultStatis = new WebServiceTestResultStatis();
        resultStatis.setSuccessCount(successCount);
        resultStatis.setFailureCount(failureCount);
        return  resultStatis;
    }

    /**
     * 发送SOAP发送指定的请求报文，将响应报文与期望报文比对，成功则返回1，否则返回0
     * @param url
     * @param inputXmlPath
     * @param expectOutputXmlPath
     * @return
     */
    public static int assertEqual(String url, String inputXmlPath, String expectOutputXmlPath){
        try{
            //请求报文
            String message = FileUtils.readFileToString(inputXmlPath, "UTF-8").toString();
            //实际响应报文
            String responseXml = getResponseXml(url, message);
            //期望响应报文
            String expectResponseXml = FileUtils.readFileToString(expectOutputXmlPath, "UTF-8").toString();
            if(responseXml != null){
                if(expectResponseXml.equals(responseXml)){
                    System.out.println("Test WebService: " + url + " SUCCESS");
                    return 1;
                }
            }
        }catch (Exception e){
            System.out.println("Test Webservice: " + url + " FAILURE(NETWORK ERROR) ");
            return 0;
        }
        System.out.println("Test Webservice: " + url + " FAILURE");
        return 0;
    }

    /**
     * 发送报文并取得响应
     * @param url
     * @param message
     * @return
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
}
