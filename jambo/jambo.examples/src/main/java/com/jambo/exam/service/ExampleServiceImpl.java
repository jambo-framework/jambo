package com.jambo.exam.service;

import com.alibaba.dubbo.rpc.RpcContext;
import com.jambo.exam.business.example.company.control.CompanyBO;
import com.jambo.exam.business.example.company.persistent.CompanyDBParam;
import com.jambo.exam.business.example.company.persistent.CompanyVO;
import com.jambo.jop.exception.BusinessException;
import com.jambo.jop.infrastructure.control.AbstractBO;
import com.jambo.jop.infrastructure.service.BaseResponse;
import com.jambo.jop.infrastructure.service.BaseServiceImpl;
import com.jambo.jop.infrastructure.service.DataPackageWrap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 由于dubbo需要确定的类型来实现接口和实体的序列化，所以无法在基类里把下面这些方法也提取出来，需要每个实现类都自己调用并进行类型转换
 */
public class ExampleServiceImpl extends BaseServiceImpl implements ExampleService {
    private static final Logger logger = LogManager.getLogger(ExampleServiceImpl.class);
    public CompanyBO bo;

    @Override
    public AbstractBO getBO() throws Exception {
        if (bo == null) bo = (CompanyBO) getBO(CompanyBO.class);
        return bo;
    }

    public String sayHello(String name) {
        System.out.println("[" + new SimpleDateFormat("HH:mm:ss").format(new Date()) + "] Hello " + name + ", request from consumer: " + RpcContext.getContext().getRemoteAddress());
        return "Hello " + name + ", response form provider: " + RpcContext.getContext().getLocalAddress();
    }

    public DataPackageWrap<CompanyVO> query(CompanyDBParam request) throws BusinessException {
        DataPackageWrap response = new DataPackageWrap<CompanyVO>(request.getSerialNo(), "0", null);
        response.wrap(super.query(request));
        return response;
    }

    public BaseResponse<CompanyVO> manager(CompanyDBParam request, CompanyVO vo) throws BusinessException {
        BaseResponse response = new BaseResponse(request.getSerialNo(), "0", null);
        response.setVo(super.manager(request, vo));
        return response;
    }

    public BaseResponse<CompanyVO> findByPk(CompanyDBParam request, Serializable pk) throws Exception {
        BaseResponse response = new BaseResponse(request.getSerialNo(), "0", null);
        response.setVo(super.findByPk(request, pk));
        return response;
    }

}