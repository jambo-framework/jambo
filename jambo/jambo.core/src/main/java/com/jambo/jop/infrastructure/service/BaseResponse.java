package com.jambo.jop.infrastructure.service;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * <p>Description: 数据操作结果返回状态对象 </p>
 * <p/>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p/>
 * <p>Company: jambo-framework Tech Ltd.</p>
 *
 * @author jinbo
 */
public class BaseResponse<T> implements Serializable {
    private static final long serialVersionUID = 3085976486835465235L;

    //服务调用成功0、失败1，其它错误码需要查询错误代码表
    public static final String RESPONSE_CODE_SUCCESS = "0";
    public static final String RESPONSE_CODE_FAILURE = "1";

    protected String serialNo;
    protected String RespCode; //0：成功  , 其它失败
    protected String respMsg;

    private T vo;

    public BaseResponse() {
    }

    public BaseResponse(String serialNo, String RespCode, String respMsg){
        this.setSerialNo(serialNo);
        this.setRespCode(RespCode);
        this.setRespMsg(respMsg);
    }

    public T getVo() {
        return vo;
    }

    public void setVo(T vo) {
        this.vo = vo;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getRespCode() {
        return RespCode;
    }

    public void setRespCode(String respCode) {
        RespCode = respCode;
    }

    public String getRespMsg() {
        return respMsg;
    }

    public void setRespMsg(String respMsg) {
        this.respMsg = respMsg;
    }

    public void setErrorRespMsg(String respCode, String respMsg){
        this.setRespCode(respCode);
        this.setRespMsg(respMsg);
    }

    public void setErrorRespMsg(String respMsg){
        this.setRespCode("-1");
        this.setRespMsg(respMsg);
    }

    public boolean isSuccess(){
        return RESPONSE_CODE_SUCCESS.equals(getRespCode());
    }

    public String toString(){
        return ToStringBuilder.reflectionToString(this);
    }
}
