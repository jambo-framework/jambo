package com.jambo.exam.web.example.dubbo1;

import com.jambo.exam.business.example.company.control.CompanyBO;
import com.jambo.exam.business.example.company.persistent.CompanyVO;
import com.jambo.exam.service.ExampleService;
import com.jambo.jop.infrastructure.control.AbstractBO;
import com.jambo.jop.ui.spring.BaseAction;
import com.jambo.jop.ui.worker.BaseMVCWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * 如果不需要使用框架提供的增删改、查询这些功能，不需要使用BO来包装dubbo服务，可以直接把方法写到action里
 * jinbo 2015.2.10
 */
@Controller
public class Dubbo1Action extends BaseAction {
    @Autowired
    private ExampleService demoService;

    public Dubbo1Action() {
        super();

        setWorker(new BaseMVCWorker(this, CompanyBO.class, CompanyVO.class));
        setBasepath("/example/example2/");

//        this.setVOClass(CompanyVO.class);

//        this.basepath = "/example/dubbo1/";

        //指定主键数组，如果是复合主键，则需指定全部复合主键的字段名称
        //默认设置为id，如果主键名称是id，则不用设置
//        this.pkNameArray = new String[]{"id"};
    }

    public AbstractBO getBO() throws Exception {
        AbstractBO bo = new CompanyServiceBO(demoService, getDBAccessUser());
        return bo;
    }
}