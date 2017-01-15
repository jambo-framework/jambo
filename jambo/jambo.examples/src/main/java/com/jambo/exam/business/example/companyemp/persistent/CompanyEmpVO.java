package com.jambo.exam.business.example.companyemp.persistent;

import com.jambo.jop.infrastructure.db.BaseVO;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 复杂查询demo VO， 公司职员人数统计。职员人数empNumber是公司表所没有的字段，需要单独作为视图来映射。
 * @author He Kun */
public class CompanyEmpVO extends BaseVO {
    /** identifier field */
    private Long id;

    /** nullable persistent field */
    private String companyname;

    /** nullable persistent field */
    private String shortname;

    private Integer empNumber;

    public CompanyEmpVO() {
    }

    /** full constructor */
    public CompanyEmpVO(  Long id, String companyname, String shortname, Integer empNumber ) {
        super();
        this.id = id;
        this.companyname = companyname;
        this.shortname = shortname;
        this.empNumber = empNumber;
    }


    public String toString() {
        return new ToStringBuilder(this)
                .append("id", getId())
                .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof CompanyEmpVO) ) return false;
        CompanyEmpVO castOther = (CompanyEmpVO) other;
        return new EqualsBuilder()
                .append(this.getId(), castOther.getId())
                .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
                .append(getId())
                .toHashCode();
    }


    public String getCompanyname() {
        return companyname;
    }


    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }


    public Integer getEmpNumber() {
        return empNumber;
    }


    public void setEmpNumber(Integer empNumber) {
        this.empNumber = empNumber;
    }


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public String getShortname() {
        return shortname;
    }


    public void setShortname(String shortname) {
        this.shortname = shortname;
    }

}
