package com.jambo.jop.common.businesslog;

import com.jambo.jop.common.commoncontrol.CommonBO;
import com.jambo.jop.exception.JOPException;
import com.jambo.jop.infrastructure.db.BaseDAO;
import com.jambo.jop.infrastructure.db.DAOFactory;

public class BusinessLogBO extends CommonBO implements BusinessLogControl {
	
	public BusinessLogBO(){
	}
	
	public Object doLogCreate(Object vo) throws Exception {
		BaseDAO ordinaryDAO = DAOFactory.buildCommonDAO(voClass, user);
		try {
			vo = ordinaryDAO.create(vo);
			return vo;
		} catch (Exception ex) {
			throw new JOPException(ex);
		}
	}

	public Class getVoClass() {
		return voClass;
	}

	public void setVoClass(Class voClass) {
		this.voClass = voClass;
	}

	private Class voClass;
	
}
