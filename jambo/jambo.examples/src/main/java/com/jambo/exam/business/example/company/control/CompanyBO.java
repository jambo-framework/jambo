package com.jambo.exam.business.example.company.control;

import com.jambo.exam.business.example.company.persistent.CompanyDAO;
import com.jambo.exam.business.example.company.persistent.CompanyDBParam;
import com.jambo.exam.business.example.company.persistent.CompanyVO;
import com.jambo.jop.infrastructure.control.AbstractBO;
import com.jambo.jop.infrastructure.db.DAOFactory;
import com.jambo.jop.infrastructure.db.DBAccessUser;

import java.util.List;

/**
 * @version 1.0 Jinbo
 */
public class CompanyBO extends AbstractBO {

    @Override
    protected Class getDAOClasses() {
        return CompanyDAO.class;
    }

    public CompanyDAO getDAO() throws Exception {
        return (CompanyDAO)getBaseDAO();
    }

    /**
     * 转移资本金。用于演示和测试两数据源操作
     */
    public void doTransferCapital(DBAccessUser user1,Long id1,DBAccessUser user2,Long id2,Long value) throws Exception  {
        CompanyDAO companyDAO1 = (CompanyDAO) DAOFactory.build(CompanyDAO.class, user1);
        CompanyDAO companyDAO2 = (CompanyDAO) DAOFactory.build(CompanyDAO.class, user2);

        CompanyVO companyVO1 = (CompanyVO) companyDAO1.findByPk(id1);
        companyVO1.setCapital( new Long(companyVO1.getCapital().longValue() - value.longValue()));
        companyDAO1.update(companyVO1);

        CompanyVO companyVO2 = (CompanyVO) companyDAO2.findByPk(id2);
        companyVO2.setCapital( new Long(companyVO2.getCapital().longValue() + value.longValue()));
        companyDAO2.update(companyVO2);
    }

    public List queryCompanyNames(CompanyDBParam params) throws Exception  {
        return getDAO().queryCompanyNames(params);
    }

    public List queryCompanyCapicals(CompanyDBParam param) throws Exception {
        return getDAO().queryCompanyCapicals(param);
    }

    public List queryIdAndAccounts(CompanyDBParam param) throws Exception {
        return getDAO().queryIdAndAccounts(param);
    }

}



