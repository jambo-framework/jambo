package com.jambo.jop.infrastructure.control;

import com.jambo.jop.infrastructure.db.*;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.io.Serializable;

/**
 * <p>Title: 抽象ControlBean类</p>
 * <p/>
 * <p>Description: 封装了SessionBean、EJBLocalObject接口必须实现的方法</p>
 * <p/>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p/>
 * <p>Company: jambo-framework</p>
 *
 * @author DuHuazheng && HuangBaiming
 * @author He Kun
 * @version 1.2 He Kun 2007-12, 实现 Authorizable 接口，用于设置 user参数
 * 2013.7.23 jinbo 把dao放在基类里创建
 */
public abstract class AbstractBO implements Authorizable {

    protected DBAccessUser user;
    protected BaseDAO dao = null;

    protected abstract Class getDAOClasses();

    public DBAccessUser getUser() {
        return user;
    }

    public void setUser(DBAccessUser user) {
        this.user = user;
    }

    protected BaseDAO getBaseDAO() {
        if (dao == null) {
            dao = DAOFactory.build(getDAOClasses(), user);
        } else {
            //如果BO的user被变过，就重新创建DAO
            if (!dao.getDbFlag().equals(user.getDbFlag())){
                dao = DAOFactory.build(getDAOClasses(), user);
            }
        }
        return dao;
    }

    public BaseVO doCreate(BaseVO vo) throws Exception {
        return (BaseVO) getBaseDAO().create(vo);
    }

    public void doRemoveByVO(BaseVO vo) throws Exception {
        getBaseDAO().remove(vo);
    }

    public void doRemoveByPK(Serializable pk) throws Exception {
        getBaseDAO().removeByPk(pk);
    }

    public BaseVO doUpdate(BaseVO vo) throws Exception {
        return (BaseVO) getBaseDAO().update(vo);
    }

    public BaseVO findByPk(Serializable pk) throws Exception {
        return (BaseVO) getBaseDAO().findByPk(pk);
    }

    public DataPackage query(DBQueryParam params) throws Exception {
        return getBaseDAO().query(params);
    }

    /**
     * 手动要求spring回退事务，请谨慎使用
     */
    protected void rollback(){
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
    }
}
