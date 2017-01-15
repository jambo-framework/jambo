package com.jambo.exam.business.example.company.persistent;

import com.jambo.jop.infrastructure.db.BaseVO;
import com.jambo.jop.infrastructure.sysadmin.ManageLog;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class CompanyVO extends BaseVO implements ManageLog {

    /** identifier field */
    private Long id;

    /** nullable persistent field */
    private String companyname;

    /** nullable persistent field */
    private String shortname;

    /** nullable persistent field */
    private String address;

    /** nullable persistent field */
    private Long capital;

    /** nullable persistent field */
    private java.util.Date createdate;

    /** nullable persistent field */
    private String bank;

    /** nullable persistent field */
    private String account;

    /** nullable persistent field */
    private Byte state;

    /** full constructor */
    public CompanyVO(Long id, String companyname, String shortname, String address, Long capital, java.util.Date createdate, String bank, String account, Byte state) {
        this.id = id;
        this.companyname = companyname;
        this.shortname = shortname;
        this.address = address;
        this.capital = capital;
        this.createdate = createdate;
        this.bank = bank;
        this.account = account;
        this.state = state;
    }

    public CompanyVO(Long id, String companyname, String shortname, String address) {
        this.id = id;
        this.companyname = companyname;
        this.shortname = shortname;
        this.address = address;
    }

    public CompanyVO(Long id, String companyname) {
        this.id = id;
        this.companyname = companyname;
    }

    /**����̬����ת���õĹ��췽��*/
    public CompanyVO(String shortname, String companyname) {
        this.shortname = shortname;
        this.companyname = companyname;
    }

    /** default constructor */
    public CompanyVO() {
    }

    /** minimal constructor */
    public CompanyVO(Long id) {
        this.id = id;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompanyname() {
        return this.companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }

    public String getShortname() {
        return this.shortname;
    }

    public void setShortname(String shortname) {
        this.shortname = shortname;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getCapital() {
        return this.capital;
    }

    public void setCapital(Long capital) {
        this.capital = capital;
    }

    public java.util.Date getCreatedate() {
        return this.createdate;
    }

    public void setCreatedate(java.util.Date createdate) {
        this.createdate = createdate;
    }

    public String getBank() {
        return this.bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getAccount() {
        return this.account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Byte getState() {
        return this.state;
    }

    public void setState(Byte state) {
        this.state = state;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("id", getId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof CompanyVO) ) return false;
        CompanyVO castOther = (CompanyVO) other;
        return new EqualsBuilder()
            .append(this.getId(), castOther.getId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getId())
            .toHashCode();
    }

}
