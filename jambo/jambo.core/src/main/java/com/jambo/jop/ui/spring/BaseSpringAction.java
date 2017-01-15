package com.jambo.jop.ui.spring;

import com.jambo.jop.infrastructure.control.AbstractBO;
import com.jambo.jop.infrastructure.control.BOFactory;
import com.jambo.jop.infrastructure.db.BaseVO;
import com.jambo.jop.infrastructure.db.DBAccessUser;
import com.jambo.jop.infrastructure.db.DBQueryParam;
import com.jambo.jop.infrastructure.db.DataPackage;
import com.jambo.jop.ui.worker.BaseMVCWorker;
import com.jambo.jop.ui.worker.MVCAction;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * 基于BaseSpringAction实现，如果不需要使用框架提供的CRUD功能，也可以不从BaseSpringAction继承
 * 优点：可以使用springMVC各种奇葩的URL映射规则，可以随意绑定各种参数，对象，是springMVC比较推荐的使用方式
 * 缺点：每个方法，每个类都需要写映射路径，容易造成项目整体目录混乱
 * jinbo 2015.2.1
 */
public abstract class BaseSpringAction implements MVCAction {
    private BaseMVCWorker worker;
    private String basepath = null;

    public BaseMVCWorker getWorker() {
        return worker;
    }

    public void setWorker(BaseMVCWorker worker) {
        this.worker = worker;
    }

    public String getBasepath(){
        return basepath;
    }

    public void setBasepath(String basepath){
        this.basepath = basepath;
    }

    public void setForm(BaseVO form) {
        worker.setForm(form);
    }

    public void setParam(DBQueryParam param) {
        worker.setParam(param);
    }

    public void setPkNameArray(String[] pkNameArray) {
        worker.setPkNameArray(pkNameArray);
    }

    public DBAccessUser getDBAccessUser(){
        return getWorker().getDBAccessUser();
    }

    public void setActionMessage(Model model, String message){
        getWorker().getAi().setMessage(message);
        model.addAttribute("ActionInfo", getWorker().getAi());
    }

    public Class getBOClass() {
        return getWorker().getBOClass();
    }

    public AbstractBO getBO() throws Exception {
        AbstractBO bo = BOFactory.build(getBOClass(), getDBAccessUser());
        return bo;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new DateEditor());
    }

    public ModelAndView add() throws Exception {
        ModelAndView model = new ModelAndView(getWorker().getContentUrl());

        getWorker().add();

        model.addObject(MVCAction.WEB_PAGE_PARAM, getWorker().getParam());
        model.addObject(MVCAction.WEB_PAGE_FORM, getWorker().getForm());
        model.addObject(MVCAction.WEB_VALUE_CMD, MVCAction.WEB_CMD_NEW);
        model.addObject(MVCAction.WEB_ACTION_INFO, getWorker().getAi());

        return model;
    }

    public ModelAndView list(HttpServletRequest request, DBQueryParam param) throws Exception {
        //由于 DBQueryParam 存在 大量以_开头的属性，默认的spring绑定方法会忽略掉这些属性，所以得自己来
        getWorker().bindObject(request, param);
        ModelAndView model = new ModelAndView(getWorker().getListUrl());

        DataPackage dp = getWorker().list(param);

        //param需要将传入的数据再传递回页面，所以得给model设置
        model.addObject(WEB_PAGE_PARAM, param);
        model.addObject(WEB_RESULT_LIST_MODEL_NAME, dp);
        model.addObject(MVCAction.WEB_ACTION_INFO, getWorker().getAi());
        return model;
    }

    public ModelAndView edit(HttpServletRequest request, DBQueryParam param) throws Exception{
        //由于 DBQueryParam 存在 大量以_开头的属性，默认的spring绑定方法会忽略掉这些属性，所以得自己来
        getWorker().bindObject(request, param);
        ModelAndView model = new ModelAndView(getWorker().getContentUrl());

        getWorker().edit(param);

        model.addObject(WEB_PAGE_PARAM, param);
        model.addObject(WEB_PAGE_FORM, getWorker().getForm());
        model.addObject(WEB_VALUE_CMD, WEB_CMD_EDIT);
        model.addObject(MVCAction.WEB_ACTION_INFO, getWorker().getAi());
        return model;
    }

    public ModelAndView save(HttpServletRequest request, DBQueryParam param, BaseVO form) throws Exception{
        //由于 DBQueryParam 存在 大量以_开头的属性，默认的spring绑定方法会忽略掉这些属性，所以得自己来
        getWorker().bindObject(request, param);
        ModelAndView model = new ModelAndView(getWorker().getContentUrl());

        getWorker().save(param, form);

        //save()修改过param,所以得取worker里的param
        model.addObject(WEB_PAGE_PARAM, getWorker().getParam());
        model.addObject(WEB_PAGE_FORM, getWorker().getForm());
        model.addObject(WEB_VALUE_CMD, getWorker().getParam().getCmd());
        model.addObject(MVCAction.WEB_ACTION_INFO, getWorker().getAi());
        return model;
    }

    public ModelAndView delete(HttpServletRequest request, DBQueryParam param) throws Exception{
        //由于 DBQueryParam 存在 大量以_开头的属性，默认的spring绑定方法会忽略掉这些属性，所以得自己来
        getWorker().bindObject(request, param);
        ModelAndView model = new ModelAndView("redirect:" + MVCAction.WEB_RESULT_LIST + ".do");

        getWorker().delete(param);

        model.addObject(WEB_PAGE_PARAM, param);
        model.addObject(MVCAction.WEB_ACTION_INFO, getWorker().getAi());
        return  model;
    }

    public String getListPage() {
        return WEB_RESULT_LIST;
    }

    public String getContentPage() {
        return WEB_RESULT_CONTENT;
    }


}
