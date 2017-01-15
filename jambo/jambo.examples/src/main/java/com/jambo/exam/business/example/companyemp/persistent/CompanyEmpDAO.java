/**
* auto-generated code
* Sat Jul 29 16:54:43 CST 2006
*/
package com.jambo.exam.business.example.companyemp.persistent;

import com.jambo.jop.infrastructure.db.AbstractDAO;
import com.jambo.jop.infrastructure.db.DBQueryParam;
import com.jambo.jop.infrastructure.db.DataPackage;

/**
 * <p>Title: CompanyEmpDAO</p>
 * <p>Description: Data Access Object for CompanyVO�� ���Ӳ�ѯdemo</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: jambo-framework Tech Ltd.</p>
 * @author He Kun 
 * @version 1.0
 */
public class CompanyEmpDAO extends AbstractDAO {

    /**
     * default constructor
     */
    public CompanyEmpDAO(){
        super(CompanyEmpVO.class);
    }
    
    /**
     * ��ѯ��˾�Ĺ�Ա��ͳ�ƽ��
     * @param param
     * @return
     * @throws Exception
     */
    public DataPackage queryCompanySum(DBQueryParam param) throws Exception {
    	return queryByNamedSqlQuery("queryCompanySum", param);
    }
}
