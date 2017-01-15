package com.jambo.exam.business.example.employee;

import com.jambo.exam.business.example.company.persistent.CompanyVO;
import com.jambo.exam.business.example.employee.control.EmployeeBO;
import com.jambo.exam.business.example.employee.persistent.EmployeeDBParam;
import com.jambo.exam.business.example.employee.persistent.EmployeeVO;
import com.jambo.jop.infrastructure.control.BOFactory;
import com.jambo.jop.infrastructure.db.DataPackage;
import com.jambo.jop.test.jop.HibernateBOTestCase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Iterator;


/**
 * <p>Title: CompanyControlTest</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: jambo-framework Tech Ltd.</p>
 * @author jinbo
 * @version 1.0
 */
public class EmployeeBOTest extends HibernateBOTestCase {
    private static final Logger log = LogManager.getLogger(EmployeeBOTest.class);

    private EmployeeBO bo;
    private static final String COMPANY_DATAFILE = "com/jambo/exam/business/example/company/Company.data.xml";
    private static final String DATAFILE = "com/jambo/exam/business/example/employee/Employee.data.xml";

    public EmployeeBOTest() throws Exception {
        super("EmployeeBOTest");

        this.schema = "PUBLIC";
        this.testDB = "sessionFactory_DB_COMMON";
        bo = (EmployeeBO) BOFactory.build(EmployeeBO.class, getUser());
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * 新增,删除,修改测试
     */
    public void testCRUD() throws Exception{
        EmployeeVO vo = new EmployeeVO();
        vo.setEmpname("新员工");
        vo.setAddress("广州大道南368");
        vo.setBank("招商银行");
        vo.setAccount("893313441200435553");
        vo = (EmployeeVO) bo.doCreate(vo);
        log.info("新增记录ID=" + vo.getId());

        vo.setEmpname("老员工");
        vo = (EmployeeVO) bo.doUpdate(vo);
        log.info("emp_name=" + vo.getEmpname());

        EmployeeDBParam param = new EmployeeDBParam();
        param.set_sk_empname("员工");
        DataPackage data = bo.query(param);
        log.info("count =" + data.getRowCount());

        bo.doRemoveByVO(vo);
        log.info("delete...");
    }

    public void testUnionQuery() throws Exception{
        insertFileIntoDb(COMPANY_DATAFILE);
        insertFileIntoDb(DATAFILE);
        EmployeeDBParam params = new EmployeeDBParam();
        params.set_ne_companyid(new Long("1"));
        try {
            DataPackage dp = bo.queryUnion(params);
            assertNotNull(dp);
            assertNotNull(dp.getDatas());
            assertTrue(dp.getDatas().size() > 0);
            Iterator iter = dp.getDatas().iterator();
            while( iter != null && iter.hasNext() ) {
                Object[] objects=(Object[])iter.next();
                CompanyVO vo1 = (CompanyVO)objects[0];
                EmployeeVO vo2 = (EmployeeVO)objects[1];

                System.out.println("companyid = " + vo2.getCompanyid());
                System.out.println("companyname = " + vo1.getCompanyname());
                System.out.println("empname = " + vo2.getEmpname());
            }
        } catch (Exception e) {
            log.error(e);
            fail(e.getMessage());
        } finally {
            deleteFileFromDb(COMPANY_DATAFILE);
            deleteFileFromDb(DATAFILE);
        }
    }

    public void testDoQuery() throws Exception {
        insertFileIntoDb(DATAFILE);
        EmployeeDBParam params = new EmployeeDBParam();
        params.getQueryConditions().put("_sk_empname", "从兴");
        try {
            DataPackage dp = bo.query(params);
            assertNotNull(dp);
            log.info("testDoQuery count ={}", dp.getDatas().size());
            assertNotNull(dp.getDatas());
            assertTrue(dp.getDatas().size() > 0);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail(e.getMessage());
        } finally {
            deleteFileFromDb(DATAFILE);
        }
    }

}
