package com.jambo.exam.service;

import com.jambo.exam.business.example.company.persistent.CompanyDBParam;
import com.jambo.exam.business.example.company.persistent.CompanyVO;
import com.jambo.jop.exception.BusinessException;
import com.jambo.jop.infrastructure.service.BaseResponse;
import com.jambo.jop.infrastructure.service.DataPackageWrap;

import java.io.Serializable;

/**
 * MVC基本页面范例,
 * 测试用的服务定义，真正的项目应该把服务定义与POJO，PARAM抽出来单独建一个项目，不然会形成循环引用
 * User: jinbo
 * Date: 15-1-26
 */
public interface ExampleService {
    public String sayHello(String name);

    public DataPackageWrap<CompanyVO> query(CompanyDBParam request) throws BusinessException;

    public BaseResponse<CompanyVO> findByPk(CompanyDBParam request, Serializable pk) throws Exception;

    public BaseResponse<CompanyVO> manager(CompanyDBParam request, CompanyVO vo) throws BusinessException;

}
