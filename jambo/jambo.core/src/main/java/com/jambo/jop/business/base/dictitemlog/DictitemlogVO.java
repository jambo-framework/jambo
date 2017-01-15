package com.jambo.jop.business.base.dictitemlog;

import com.jambo.jop.infrastructure.db.BaseVO;
import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class DictitemlogVO extends BaseVO implements Serializable {

    /** identifier field */
    private Long logid;

    /** nullable persistent field */
    private java.util.Date optime;

    /** nullable persistent field */
    private String oprcode;

    /** nullable persistent field */
    private String oprtype;

    /** nullable persistent field */
    private String success;

    /** nullable persistent field */
    private String dictid;

    /** nullable persistent field */
    private String dictname;

    /** nullable persistent field */
    private String groupid;

    /** nullable persistent field */
    private Short sortorder;

    /** nullable persistent field */
    private Byte status;

    /** nullable persistent field */
    private java.util.Date statusdate;

    /** nullable persistent field */
    private String description;

    /** full constructor */
    public DictitemlogVO(java.util.Date optime, java.lang.String oprcode, java.lang.String oprtype, java.lang.String success, java.lang.String dictid, java.lang.String dictname, java.lang.String groupid, java.lang.Short sortorder, java.lang.Byte status, java.util.Date statusdate, java.lang.String description) {
        this.optime = optime;
        this.oprcode = oprcode;
        this.oprtype = oprtype;
        this.success = success;
        this.dictid = dictid;
        this.dictname = dictname;
        this.groupid = groupid;
        this.sortorder = sortorder;
        this.status = status;
        this.statusdate = statusdate;
        this.description = description;
    }

    /** default constructor */
    public DictitemlogVO() {
    }

    public java.lang.Long getLogid() {
        return this.logid;
    }

    public void setLogid(java.lang.Long logid) {
        this.logid = logid;
    }

    public java.util.Date getOptime() {
        return this.optime;
    }

    public void setOptime(java.util.Date optime) {
        this.optime = optime;
    }

    public java.lang.String getOprcode() {
        return this.oprcode;
    }

    public void setOprcode(java.lang.String oprcode) {
        this.oprcode = oprcode;
    }

    public java.lang.String getOprtype() {
        return this.oprtype;
    }

    public void setOprtype(java.lang.String oprtype) {
        this.oprtype = oprtype;
    }

    public java.lang.String getSuccess() {
        return this.success;
    }

    public void setSuccess(java.lang.String success) {
        this.success = success;
    }

    public java.lang.String getDictid() {
        return this.dictid;
    }

    public void setDictid(java.lang.String dictid) {
        this.dictid = dictid;
    }

    public java.lang.String getDictname() {
        return this.dictname;
    }

    public void setDictname(java.lang.String dictname) {
        this.dictname = dictname;
    }

    public java.lang.String getGroupid() {
        return this.groupid;
    }

    public void setGroupid(java.lang.String groupid) {
        this.groupid = groupid;
    }

    public java.lang.Short getSortorder() {
        return this.sortorder;
    }

    public void setSortorder(java.lang.Short sortorder) {
        this.sortorder = sortorder;
    }

    public java.lang.Byte getStatus() {
        return this.status;
    }

    public void setStatus(java.lang.Byte status) {
        this.status = status;
    }

    public java.util.Date getStatusdate() {
        return this.statusdate;
    }

    public void setStatusdate(java.util.Date statusdate) {
        this.statusdate = statusdate;
    }

    public java.lang.String getDescription() {
        return this.description;
    }

    public void setDescription(java.lang.String description) {
        this.description = description;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("logid", getLogid())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof DictitemlogVO) ) return false;
        DictitemlogVO castOther = (DictitemlogVO) other;
        return new EqualsBuilder()
            .append(this.getLogid(), castOther.getLogid())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getLogid())
            .toHashCode();
    }

}
