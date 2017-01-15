package com.jambo.exam.web.example.example1;

import com.jambo.exam.business.example.company.control.CompanyBO;
import com.jambo.exam.business.example.company.persistent.CompanyDBParam;
import com.jambo.exam.business.example.company.persistent.CompanyVO;
import com.jambo.jop.infrastructure.db.DataPackage;
import com.jambo.jop.ui.spring.BaseAction;
import com.jambo.jop.ui.worker.BaseMVCWorker;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 默认的BaxeAction只会映射 ModelAndView function(HttpServletRequest, HttpServletResponse)类似这样的方法，
 *     具体需要查 MultiActionController 的说明
 * 所以ajax的方法增加后，只能手工在方法加上映射关系，还得在类也加上
 */
@Controller
@RequestMapping("/example/example1/example1PartAction")
public class Example1PartAction extends BaseAction{
    public Example1PartAction() {
        super();

        setWorker(new BaseMVCWorker(this, CompanyBO.class, CompanyVO.class));
        setBasepath("/example/example1/");
//        this.setVOClass(CompanyVO.class);
//        this.setBOClass(CompanyBO.class);

        this.setForm(new Example1Form());
        this.setParam(new CompanyDBParam());
    }

    public ModelAndView list(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //通过指定查询参数中的SelectFields列表，来决定查询时，只查询哪些字段内容
        //比Example1Action少查询了account字段，可以在list页面上看到account列为空
        List fields = new ArrayList();
        fields.add("id");
        fields.add("companyname");
        fields.add("shortname");
        fields.add("address");
        this.bindQueryParam("selectFields", fields);

        return super.list(request, response);
    }
    public String getListPage() {
        return "listpart";
    }

    public String getContentPage() {
        return "contentpart";
    }

    @ResponseBody
    @RequestMapping("/listx")
    public DataPackage listx(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //MultiActionController不会自动给入参对象赋值，得自己调用bind
        bind(request, null);

        List fields = new ArrayList();
        fields.add("id");
        fields.add("companyname");
        fields.add("shortname");
        fields.add("address");
        getParam().setSelectFields(fields);

        return query(getParam());
    }
}