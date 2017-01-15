/**
 * auto-generated code
 * Sat Jul 29 16:54:43 CST 2006
 */
package com.jambo.exam.business.example.employee.control;

import com.jambo.exam.business.example.company.persistent.CompanyDBParam;
import com.jambo.exam.business.example.company.persistent.CompanyVO;
import com.jambo.exam.business.example.companyemp.persistent.CompanyEmpDAO;
import com.jambo.exam.business.example.employee.persistent.EmployeeDAO;
import com.jambo.exam.business.example.employee.persistent.EmployeeDBParam;
import com.jambo.exam.business.example.employee.persistent.EmployeeVO;
import com.jambo.jop.infrastructure.control.AbstractBO;
import com.jambo.jop.infrastructure.db.DAOFactory;
import com.jambo.jop.infrastructure.db.DBQueryParam;
import com.jambo.jop.infrastructure.db.DataPackage;

/**
 * @version 1.0 Jinbo
 * @version 2.0 baiming
 */
public class EmployeeBO extends AbstractBO {

    @Override
    protected Class getDAOClasses() {
        return EmployeeDAO.class;
    }

	public DataPackage queryUnion(EmployeeDBParam param) throws Exception {
		CompanyDBParam param1 = new CompanyDBParam();
		
		EmployeeDBParam param2 = param;
		
        Object[] params={param1, param2};
        Class[] vo={CompanyVO.class, EmployeeVO.class};
		String[][] joins={{"id", "companyid"}};

		EmployeeDAO dao = (EmployeeDAO) DAOFactory.build(EmployeeDAO.class, user);
		return dao.unionQuery(params, vo , joins , EmployeeDAO.QUERY_TYPE_COUNT_AND_DATA);
	}

	public DataPackage queryByNamedSqlQuery(String queryName, DBQueryParam params) throws Exception {
		EmployeeDAO dao = (EmployeeDAO) DAOFactory.build(EmployeeDAO.class, user);
		return dao.queryByNamedSqlQuery(queryName, params);		
	}
	
	public DataPackage queryCompanySum(DBQueryParam params) throws Exception {
		CompanyEmpDAO dao = (CompanyEmpDAO) DAOFactory.build(CompanyEmpDAO.class, user);
		return dao.queryCompanySum(params);		
	}
}



