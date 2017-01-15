package com.jambo.jop.infrastructure.service;

import com.jambo.jop.infrastructure.db.DataPackage;

import java.util.List;

/**
 * 原来的datapackage不能被dubbo序列化，所以增加一个泛型子类包装
 * User: HeWenlang，jinbo
 *
 */
public class DataPackageWrap<T> extends DataPackage {

    private static final long serialVersionUID = 2558815832342580579L;
    protected List<T> voList;

    public DataPackageWrap(){
       super();
    }

    public DataPackageWrap(String serialNo, String RespCode, String respMsg){
        super(serialNo, RespCode, respMsg);
    }

    public List<T> getVoList() {
        return voList;
    }

    public void setVoList(List<T> voList) {
        this.voList = voList;
    }

    public void wrap(DataPackage dp){
        this.setRowCount(dp.getRowCount());
        this.setPageSize(dp.getPageSize());
        this.setPageNo(dp.getPageNo());

        voList = dp.getDatas();
    }

    public List getDatas(){
        return voList;
    }
}
