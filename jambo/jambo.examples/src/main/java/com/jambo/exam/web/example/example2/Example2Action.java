package com.jambo.exam.web.example.example2;

import com.jambo.exam.business.example.company.control.CompanyBO;
import com.jambo.exam.business.example.company.persistent.CompanyDBParam;
import com.jambo.exam.business.example.company.persistent.CompanyVO;
import com.jambo.jop.ui.spring.BaseSpringAction;
import com.jambo.jop.ui.worker.BaseMVCWorker;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * 基于BaseSpringAction实现，如果不需要使用框架提供的CRUD功能，也可以不从BaseSpringAction继承
 * 优点：可以使用springMVC各种奇葩的URL映射规则，可以随意绑定各种参数，对象，是springMVC比较推荐的使用方式
 * 缺点：每个方法，每个类都需要写映射路径，容易造成项目整体目录混乱
 */
@Controller
@RequestMapping(value = "example/example2/example2Action")
public class Example2Action extends BaseSpringAction {

    public Example2Action() {
        super();

        setWorker(new BaseMVCWorker(this, CompanyBO.class, CompanyVO.class));
        setBasepath("/example/example2/");

        //BaseAction将把Action的这个词替换为Form，如果命名符合规范，则不用设置
//        this.setForm(new CompanyVO());
        //BaseAction将把VO名称替换为DBParam，如果命名符合规范，则不用设置
//        this.setParam(new CompanyDBParam());

        //指定主键数组，如果是复合主键，则需指定全部复合主键的字段名称
        //默认设置为id，如果主键名称是id，则不用设置
//        this.setPkNameArray(new String[]{"id"});
    }

    //不带入参的请求，使用GET可以效率高一点
    @RequestMapping (value="add",method = RequestMethod.GET)
    public ModelAndView add() throws Exception {
        return super.add();
    }

    //由于DBQueryParam存在大量以_开头的属性，默认的spring绑定方法会忽略掉这些属性，所以得自己来,也就是入参必须有HttpServletRequest
    @RequestMapping (value="list",method = RequestMethod.GET)
    public ModelAndView list(HttpServletRequest request, CompanyDBParam param) throws Exception {
        return super.list(request, param);
    }

    @RequestMapping (value="edit",method = RequestMethod.GET)
    public ModelAndView edit(HttpServletRequest request, CompanyDBParam param) throws Exception{
        return super.edit(request, param);
    }

    //使用POST，可以避免参数出现在URL中，安全性更高
    @RequestMapping (value="save",method = RequestMethod.POST)
    public ModelAndView save(HttpServletRequest request, CompanyDBParam param, Example2Form form) throws Exception{
        return super.save(request, param, form);
    }

    //使用POST，可以避免参数出现在URL中，安全性更高
    @RequestMapping (value="delete",method = RequestMethod.POST)
    public ModelAndView delete(HttpServletRequest request, CompanyDBParam param) throws Exception{
        return super.delete(request, param);
    }

}