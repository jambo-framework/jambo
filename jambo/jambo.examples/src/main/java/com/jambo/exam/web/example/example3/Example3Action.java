package com.jambo.exam.web.example.example3;

import com.jambo.exam.business.example.company.control.CompanyBO;
import com.jambo.exam.business.example.company.persistent.CompanyVO;
import com.jambo.exam.business.example.employee.control.EmployeeBO;
import com.jambo.exam.business.example.employee.persistent.EmployeeDBParam;
import com.jambo.exam.business.example.employee.persistent.EmployeeVO;
import com.jambo.jop.infrastructure.control.BOFactory;
import com.jambo.jop.infrastructure.db.DataPackage;
import com.jambo.jop.ui.spring.BaseAction;
import com.jambo.jop.ui.worker.BaseMVCWorker;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Controller
public class Example3Action extends BaseAction{
    public Example3Action() {
        super();

        setWorker(new BaseMVCWorker(this, CompanyBO.class, CompanyVO.class));
        setBasepath("/example/example3/");

//        this.setVOClass(EmployeeVO.class);
        //BaseAction将把VO名称替换为DBParam，如果命名符合规范，则不用设置
//        this.setBOClass(EmployeeBO.class);
        this.setParam(new Example3WebParam());

//        this.basepath = "/example/example2/";
    }

    //注：关键属性companyid必须有值，否则查询为空
    /**
     * 复杂查询1：两表关联查询
     */
    public ModelAndView xQuery1(HttpServletRequest request, HttpServletResponse response) throws Exception{
        bind(request, null);

        DataPackage dp =new DataPackage();
        EmployeeBO control = (EmployeeBO) BOFactory.build(EmployeeBO.class, getDBAccessUser());
        EmployeeDBParam param = (EmployeeDBParam)getParam();


        DataPackage qrydp = control.queryUnion(param);
        Iterator iter = qrydp.getDatas().iterator();
        List list = new ArrayList();
        while(iter.hasNext()) {
            Object[] objects=(Object[])iter.next();
            CompanyVO vo1 = (CompanyVO)objects[0];
            EmployeeVO vo2 = (EmployeeVO)objects[1];

            Example3Form form = new Example3Form();
//            BeanUtils.copyProperties(form, vo2);
            form.setCompanyname(vo1.getCompanyname());
            list.add(form);
        }
        dp.setDatas(list);

//        setDp(dp);

        ModelAndView model = new ModelAndView("xquery1");//getMapping(
        model.addObject(WEB_PAGE_PARAM, param);
        model.addObject(WEB_RESULT_LIST_MODEL_NAME, dp);
        return model;
    }

    /**
     * 复杂查询2：可对应集合example2 复杂SQL查询
     * @return
     * @throws Exception
     */
    public ModelAndView xQuery2(HttpServletRequest request, HttpServletResponse response) throws Exception{
        bind(request, null);

        EmployeeBO control = (EmployeeBO) BOFactory.build(EmployeeBO.class, getDBAccessUser());
//        DataPackage dp = control.queryByNamedSqlQuery("queryCompanyEmp", param);
//        setDp(dp);

        ModelAndView model = new ModelAndView("xquery2");//getMapping(
//        model.addObject(WEB_PAGE_PARAM, param);
//        model.addObject(WEB_RESULT_LIST_MODEL_NAME, dp);
        return model;
    }

    /**
     * 复杂查询2：无对应集合复杂SQL查询
     * @return
     * @throws Exception
     */
    public ModelAndView xQuery3(HttpServletRequest request, HttpServletResponse response) throws Exception{
        bind(request, null);

        EmployeeBO control = (EmployeeBO) BOFactory.build(EmployeeBO.class, getDBAccessUser());
//        param.setQueryType(BaseDAO.QUERY_TYPE_DATA_ONLY);
//        DataPackage dp = control.queryCompanySum(param);
//        setDp(dp);
        ModelAndView model = new ModelAndView("xquery3");//getMapping(
//        model.addObject(WEB_PAGE_PARAM, param);
//        model.addObject(WEB_RESULT_LIST_MODEL_NAME, dp);
        return model;
    }

    public ModelAndView save(HttpServletRequest request, HttpServletResponse response) throws Exception{
        bind(request, null);

        EmployeeDBParam param = (EmployeeDBParam)getParam();
        Example3Form form = (Example3Form) getForm();
        form.setCompanyid(param.get_ne_companyid());
        return super.save(request, response);
    }

}
