package com.jambo.jop.common.commoncontrol;

import com.jambo.jop.common.persistent.CommonDAO;
import com.jambo.jop.infrastructure.control.AbstractBO;
import com.jambo.jop.infrastructure.db.BaseDAO;
import com.jambo.jop.infrastructure.db.BaseVO;
import com.jambo.jop.infrastructure.db.DAOFactory;
import com.jambo.jop.infrastructure.db.DataPackage;

import java.io.Serializable;

public class CommonBO extends AbstractBO {

    private Class voClass;

    @Override
    protected Class getDAOClasses() {
        return CommonDAO.class;
    }

    public Class getVoClass() {
        return voClass;
    }

    public void setVoClass(Class voClass) {
        this.voClass = voClass;
    }

    public Object doCreate(Object vo) throws Exception {
        BaseDAO ordinaryDAO = DAOFactory.buildCommonDAO(voClass, user);

        vo = ordinaryDAO.create(vo);
        return vo;
    }

    public void doRemoveByVO(Object vo) throws Exception {
        BaseDAO ordinaryDAO = DAOFactory.buildCommonDAO(voClass, user);
        ordinaryDAO.remove(vo);
    }

    public void doRemoveByPK(Serializable pk) throws Exception {
        BaseDAO ordinaryDAO = DAOFactory.buildCommonDAO(voClass, user);
        ordinaryDAO.removeByPk(pk);
    }

    public Object doUpdate(Object vo) throws Exception {
        BaseDAO ordinaryDAO = DAOFactory.buildCommonDAO(voClass, user);
        return ordinaryDAO.update(vo);
    }

    public BaseVO findByPk(Serializable pk) throws Exception {
        BaseDAO ordinaryDAO = DAOFactory.buildCommonDAO(voClass, user);
        return (BaseVO) ordinaryDAO.findByPk(pk);
    }

    public DataPackage doQuery(Object params) throws Exception {
        BaseDAO ordinaryDAO = DAOFactory.buildCommonDAO(voClass, user);
        return ordinaryDAO.query(params);
    }

}
