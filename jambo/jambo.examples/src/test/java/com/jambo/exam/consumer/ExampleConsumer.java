package com.jambo.exam.consumer;

import com.jambo.exam.business.example.company.control.CompanyBO;
import com.jambo.exam.business.example.company.persistent.CompanyDBParam;
import com.jambo.exam.business.example.company.persistent.CompanyVO;
import com.jambo.exam.service.ExampleService;
import com.jambo.jop.common.spring.SpringContextManager;
import com.jambo.jop.infrastructure.control.BOFactory;
import com.jambo.jop.infrastructure.db.DBAccessUser;
import com.jambo.jop.infrastructure.db.DBQueryParam;
import com.jambo.jop.infrastructure.db.DataPackage;

public class ExampleConsumer {
	
    public static void main(String[] args) throws Exception {
//        localNBFTest();
        dubboInitial();
        dubboTest1();
//        dubboTest2();
     }

    public static void dubboInitial(){
        SpringContextManager.init("/spring/example-client.xml");
    }

    public static void dubboTest1(){
        ExampleService demoService = (ExampleService) SpringContextManager.getBean("demoService"); // 获取远程服务代理
        String hello = demoService.sayHello("world"); // 执行远程方法
        System.out.println( hello ); // 显示调用结果

    }

    public static void dubboTest2(){
        ExampleService demoService = (ExampleService) SpringContextManager.getBean("demoService"); // 获取远程服务代理
        CompanyDBParam param = new CompanyDBParam();
//        param.set_sk_companyname("test");
        //兼容Datapackage，方便与页面框架整合
        DataPackage dp = demoService.query(param);
        if (dp != null){
            for (Object obj : dp.getDatas()){
                CompanyVO vo = (CompanyVO) obj;
                System.out.println("id =" + vo.getId() + "; companyname = " + vo.getCompanyname());
            }
        }else System.out.println(dp);
    }

    public static void localNBFTest(){
        SpringContextManager.init("/spring/ng3-system.xml");

        DBAccessUser user = new DBAccessUser();
        user.setDbFlag("COMMON");

        DBQueryParam param = new DBQueryParam();

        try {
            CompanyBO bo = (CompanyBO) BOFactory.build(CompanyBO.class, user);
            DataPackage dp = bo.query(param);

            if (dp != null){
                System.out.println(dp.getDatas().size());
            }else System.out.println(dp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}