/**
* auto-generated code
* Sat Jul 29 16:54:43 CST 2006
*/
package com.jambo.exam.business.example.company.persistent;

import com.jambo.jop.infrastructure.db.AbstractDAO;

import java.util.List;

/**
 * <p>Title: CompanyDAO</p>
 * <p>Description: Data Access Object for CompanyVO</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: jambo-framework Tech Ltd.</p>
 * @author 
 * @version 1.0
 */
public class CompanyDAO extends AbstractDAO {

    /**
     * default constructor
     */
    public CompanyDAO(){
        super(CompanyVO.class);
    }

	public List queryCompanyNames(CompanyDBParam params) throws Exception {
		return queryByNamedSqlQuery("example.company.queryCompanyNames",params).getDatas();
	}

	public List queryCompanyCapicals(CompanyDBParam param) throws Exception {
		return queryByNamedSqlQuery("example.company.queryCompanyCapitals",param).getDatas();
	}

	public List queryIdAndAccounts(CompanyDBParam param) throws Exception {
		return queryByNamedSqlQuery("example.company.queryIdAndAccounts",param).getDatas();
	}
}
