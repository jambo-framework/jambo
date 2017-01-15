package com.jambo.jop.business.base.dictitem;

import com.jambo.jop.infrastructure.db.BaseVO;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;

/** @author Hibernate CodeGenerator */
public class DictitemVO extends BaseVO implements Serializable ,IDictitemVO{

    /** identifier field */
    private String dictid;

    /** identifier field */
    private String groupid;

    /** nullable persistent field */
    private String dictname;

    /** nullable persistent field */
    private Short sortorder;

    /** nullable persistent field */
    private Byte status;

    /** nullable persistent field */
    private java.util.Date statusdate;

    /** nullable persistent field */
    private String description;

    /** full constructor */
    public DictitemVO(java.lang.String dictid, java.lang.String groupid, java.lang.String dictname, java.lang.Short sortorder, java.lang.Byte status, java.util.Date statusdate, java.lang.String description) {
        this.dictid = dictid;
        this.groupid = groupid;
        this.dictname = dictname;
        this.sortorder = sortorder;
        this.status = status;
        this.statusdate = statusdate;
        this.description = description;
    }

    /** default constructor */
    public DictitemVO() {
    }

    /** minimal constructor */
    public DictitemVO(java.lang.String dictid, java.lang.String groupid) {
        this.dictid = dictid;
        this.groupid = groupid;
    }

    public java.lang.String getDictid() {
        return this.dictid;
    }

    public void setDictid(java.lang.String dictid) {
        this.dictid = dictid;
    }

    public java.lang.String getGroupid() {
        return this.groupid;
    }

    public void setGroupid(java.lang.String groupid) {
        this.groupid = groupid;
    }

    public java.lang.String getDictname() {
        return this.dictname;
    }

    public void setDictname(java.lang.String dictname) {
        this.dictname = dictname;
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
            .append("dictid", getDictid())
            .append("groupid", getGroupid())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof DictitemVO) ) return false;
        DictitemVO castOther = (DictitemVO) other;
        return new EqualsBuilder()
            .append(this.getDictid(), castOther.getDictid())
            .append(this.getGroupid(), castOther.getGroupid())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getDictid())
            .append(getGroupid())
            .toHashCode();
    }
}
