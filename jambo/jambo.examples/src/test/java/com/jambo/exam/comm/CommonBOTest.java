package com.jambo.exam.comm;

import com.jambo.exam.business.example.company.persistent.CompanyDBParam;
import com.jambo.exam.business.example.company.persistent.CompanyVO;
import com.jambo.jop.common.commoncontrol.CommonBO;
import com.jambo.jop.infrastructure.control.BOFactory;
import com.jambo.jop.test.jop.HibernateBOTestCase;
import junit.textui.TestRunner;
import java.util.Date;

/**
 * <p>Title: CommonBOTest</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: sunrise Tech Ltd.</p>
 * @author jinbo
 * @version 1.0
 */
public class CommonBOTest extends HibernateBOTestCase {

    //    private Company company;
    private CommonBO bo;

    public CommonBOTest() throws Exception {
        super("CommonBOTest");
        bo = (CommonBO) BOFactory.build(CommonBO.class, getUser());
        bo.setVoClass(CompanyVO.class);
    }

    public static void main(String[] args) {
        TestRunner.run(CommonBOTest.class);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * 新增，删，修改的测试合在一起比较方便
     */
    public void testCRUD() throws Exception {
        CompanyVO vo = new CompanyVO();
        vo.setCompanyname("E电子公司");
        vo.setShortname("ER");
        vo.setCapital(new Long(91000000));
        vo.setCreatedate(new Date());
        vo.setAddress("广州大道南368号");
        vo.setBank("中国工商银行客村支行");
        vo.setAccount("893313441200435553");
        vo.setState((byte) 1);
        bo.doCreate(vo);

        vo.setShortname("jambo");

        CompanyDBParam param = new CompanyDBParam();
        param.set_sk_companyname("jambo");
        bo.doQuery(param);

        bo.doUpdate(vo);
    }

    public void testFindByPk() throws Exception {
        CompanyVO vo = (CompanyVO) bo.findByPk(Long.valueOf(1));
        System.out.println(vo.getCompanyname());
    }
}