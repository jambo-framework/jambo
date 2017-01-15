package com.jambo.jop.infrastructure.service;

import com.jambo.jop.exception.BusinessException;
import com.jambo.jop.infrastructure.control.AbstractBO;
import com.jambo.jop.infrastructure.control.BOFactory;
import com.jambo.jop.infrastructure.db.BaseVO;
import com.jambo.jop.infrastructure.db.DBAccessUser;
import com.jambo.jop.infrastructure.db.DBQueryParam;
import com.jambo.jop.infrastructure.db.DataPackage;
import org.apache.logging.log4j.LogManager;

import java.io.Serializable;
import java.util.UUID;

/**
 * 配合dubbo提供的基础服务实现类
 * User: jinbo
 */
public abstract class BaseServiceImpl {
    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(BaseServiceImpl.class);
    private static final DBAccessUser commonUser = DBAccessUser.getInnerUser();

    /**
     * 强制要求实现子类必须提供getBO方便
     */
    public abstract AbstractBO getBO() throws Exception;

    public AbstractBO  getBO(Class boClass) throws Exception {
        return BOFactory.build(boClass, getUser());
    }

    public DBAccessUser getUser() {
        return commonUser;
    }

    public String getImplClassname(){
        return getClass().getSimpleName();
    }

    /**
     * 子类可以通过覆盖这个方法来实现扩展operateType
     */
    protected void manageOtherType(int operateType) throws BusinessException {
        logger.error("{}.manager operation type not supported.", getImplClassname());
        throw new BusinessException("operation type not supported");
    }

    /**
     * 检查请求的参数是否正确，同时生成流水号，这个方法必须在每个发送请求前调用
     */
    private void checkRequest(DBQueryParam request, String method) throws BusinessException{
        logger.debug("{} request: {}", method, request);
        if (request == null) {
            StringBuffer msg = new StringBuffer(method).append("request is null");
            logger.error(msg.toString());
            throw new BusinessException(msg.toString());
        }

        //serialNo用于作为跟后台交互的的每次请求凭证，所以得每次发消息前再生成
//        request.setSerialNo(String.valueOf(UUID.randomUUID()));
    }

    public DataPackage query(AbstractBO bo , DBQueryParam request) throws BusinessException {
        String methodname = getImplClassname() + ".query";
        checkRequest(request, methodname);

        try {
            return bo.query(request);
        } catch (BusinessException ex) {
            logBusinessError(methodname, ex);
            throw ex;
        } catch (Exception ex) {
            logExceptionError(methodname, ex);
            throw new BusinessException(ex.getMessage(), ex);
        }
    }

    public DataPackage query(DBQueryParam request) throws BusinessException {
        try{
            return query(getBO(), request);
        } catch (Exception ex) {
            logExceptionError(getImplClassname() + ".query", ex);
            throw new BusinessException(ex.getMessage(), ex);
        }
    }

    /**
     * 简化接口的数量，这个方法合并了BaseDAO里的新增，删除，修改，主键查询4个方法
     */
    public BaseVO manager(AbstractBO bo , DBQueryParam request, BaseVO vo) throws BusinessException {
        String methodname = getImplClassname() + ".manager";
        checkRequest(request, methodname);

        BaseVO resultVO = null;
        try {
            switch (request.getOperateType()){
                case DBQueryParam.OPERATE_TYPE_CREATE :
                    resultVO = bo.doCreate(vo);
                    break;
                case DBQueryParam.OPERATE_TYPE_UPDATE :
                    resultVO = bo.doUpdate(vo);
                    break;
                case DBQueryParam.OPERATE_TYPE_DELETE :
                    bo.doRemoveByVO(vo);
                    break;
                default : {
                    manageOtherType(request.getOperateType());
                }
            }
        } catch (BusinessException ex) {
            logBusinessError(methodname, ex);
            throw ex;
        } catch (Exception ex) {
            logExceptionError(methodname, ex);
            throw new BusinessException(ex.getMessage(), ex);
        }
        return resultVO;
    }

    public BaseVO manager(DBQueryParam request, BaseVO vo) throws BusinessException {
        try{
            return manager(getBO(), request, vo);
        } catch (Exception ex) {
            logExceptionError(getImplClassname() + ".manager", ex);
            throw new BusinessException(ex.getMessage(), ex);
        }
    }

    public BaseVO findByPk(AbstractBO bo , DBQueryParam request, Serializable pk) throws Exception {
        String methodname = getImplClassname() + ".findByPk";
        checkRequest(request, methodname);
        BaseVO resultVO = null;
        try {
            resultVO = bo.findByPk(pk);
        } catch (BusinessException ex) {
            logBusinessError(methodname, ex);
            throw ex;
        } catch (Exception ex) {
            logExceptionError(methodname, ex);
            throw new BusinessException(ex.getMessage(), ex);
        }
        return resultVO;
    }

    public BaseVO findByPk(DBQueryParam request, Serializable pk) throws Exception {
        try{
            return findByPk(getBO(), request, pk);
        } catch (Exception ex) {
            logExceptionError(getImplClassname() + ".findByPk", ex);
            throw new BusinessException(ex.getMessage(), ex);
        }
    }

    private void logExceptionError(String methodname, Exception ex) {
        logger.error("{} Exception: {}", methodname, ex.getMessage());
    }

    private void logBusinessError(String methodname, BusinessException ex) {
        logger.error("{} BusinessException: {}", methodname, ex.getMessage());
    }
}

