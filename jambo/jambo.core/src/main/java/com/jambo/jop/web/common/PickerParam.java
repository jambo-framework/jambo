package com.jambo.jop.web.common;

import com.jambo.jop.infrastructure.db.DBQueryParam;

/**
 * <p>Title: PickerParam </p>;
 * <p>Description : code2name 弹出窗口查询参数  </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: jambo-framework Tech Ltd.</p>
 * @author jinbo
 */
public class PickerParam extends DBQueryParam {
    private String definition,condition, dbFlag;
    private String code, name; //查询用的编码和名称
    private String btnid; //点击弹出窗口的那个按钮id

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getCondition() {
        if (condition == null)
            return "";
        else  if (condition.toLowerCase().equals("null"))
            return "";
        else{
            return condition;
        }
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getDbFlag() {
        return dbFlag;
    }

    public void setDbFlag(String dbFlag) {
        this.dbFlag = dbFlag;
    }

    public String getCode() {
//        if (code == null)
//            return "";
//        else
            return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
//        if (name == null)
//            return "";
//        else
            return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBtnid() {
        return btnid;
    }

    public void setBtnid(String btnid) {
        this.btnid = btnid;
    }
}
