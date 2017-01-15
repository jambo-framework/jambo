package com.jambo.jop.infrastructure.db;

import com.jambo.jop.infrastructure.service.BaseResponse;
import org.springframework.cglib.beans.BeanCopier;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * <p>Description: 一般查询返回的结果集 </p>
 * <p/>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p/>
 * <p>Company: jambo-framework Tech Ltd.</p>
 *
 * @author HuangBaiming,jinbo
 */
public class DataPackage extends BaseResponse {
    private static final long serialVersionUID = 3085956436802464735L;

    private int rowCount;
    private int pageSize;
    private int pageNo;

    private List datas;

    public  DataPackage(){
        super();
    }

    public  DataPackage(String serialNo, String RespCode, String respMsg){
        super(serialNo, RespCode, respMsg);
    }

    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public boolean getIsFirst(){
        return (getPageNo() < 2);
    }

    public boolean getIsLast(){
        return !(getPageSize() >= 0 && getDatas() != null && getDatas().size() >= getPageSize());
    }

    public int getTotalPage(){
        //如果pagesize为0，表示不分页，只有1页。
        if( getPageSize() == 0)
            return 1;
        else
            return (int) Math.ceil(((double) getRowCount()) / ((double) getPageSize()));
    }

    public List getDatas() {
        return datas;
    }

    public void setDatas(List datas) {
        this.datas = datas;
    }

    /**
     * 将DataPackage的Data转换为指定的类型Class cls的List
     * @param cls 需转换为哪种类型的List
     */
    public List toList(Class cls) {
        List list = new ArrayList();
        BeanCopier beanCopier = BeanCopier.create(Object.class, cls,false);
        for (Iterator it = getDatas().iterator(); it.hasNext();) {
            Object o = (Object) it.next();
            try {
                Object obj = cls.newInstance();
//                BeanUtils.copyProperties(obj, o);
                beanCopier.copy(o, obj, null);

                list.add(obj);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }
}
