package com.jambo.exam.web.example.example1;

import com.jambo.exam.business.example.company.control.CompanyBO;
import com.jambo.exam.business.example.company.persistent.CompanyVO;
import com.jambo.jop.ui.spring.BaseAction;
import com.jambo.jop.ui.worker.BaseMVCWorker;
import org.springframework.stereotype.Controller;

/**
 * 基于MultiActionController实现，提供的默认的CRUD功能
 * 优点：所说中的CoC模式，几乎不用配置，子类需要写的方法也比较少，也可以指定springMVCURL映射规则
 * 缺点：参数，对象绑定有严格的要求，很多情况下需要自己调用BaseAction的bind方法来绑定http入参到自己的对象
 */
@Controller
public class Example1Action extends BaseAction{

    public Example1Action() {
        super();

        setWorker(new BaseMVCWorker(this, CompanyBO.class, CompanyVO.class));
        setBasepath("/example/example1/");

        //以下几个方法是必须的
        //vo,form,param，action命名规范：xxx.action,xxxVO，XXXForm,xxxDBParam，即前面完全一样，就是后面的词不同
        //BaseAction将把Action的这个词替换为Form，如果命名符合规范，则不用设置
//		this.setForm(new ExampleCompanyForm());
//        this.setVOClass(CompanyVO.class);
        //BaseAction将把VO名称替换为DBParam，如果命名符合规范，则不用设置
//        this.setParam(new CompanyDBParam());
//        this.setBOClass(CompanyBO.class);

//        this.basepath = "/example/example1/";

        //指定主键数组，如果是复合主键，则需指定全部复合主键的字段名称
        //默认设置为id，如果主键名称是id，则不用设置
//        this.pkNameArray = new String[]{"id"};
    }

}
