package com.jambo.exam.business.example.employee.persistent;

import com.jambo.jop.infrastructure.db.BaseVO;
import com.jambo.jop.infrastructure.sysadmin.ManageLog;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class EmployeeVO extends BaseVO implements ManageLog {

    /** identifier field */
    private Long id;

    private Long companyid;

    /** nullable persistent field */
    private String empname;

    /** nullable persistent field */
    private String address;

    /** nullable persistent field */
    private String bank;

    /** nullable persistent field */
    private String account;
    
    private Object pk = "id";
    
    private String tableName = "EXAM_EMPLOYEE";
    
    /** default constructor */
    public EmployeeVO() {    	
    }

    public EmployeeVO(Long id,String empname) {
    	this.id = id;
    	this.empname = empname;
    }

    public Long getCompanyid() {
		return companyid;
	}

	public void setCompanyid(Long companyid) {
		this.companyid = companyid;
	}

	public String getEmpname() {
		return empname;
	}

	public void setEmpname(String empname) {
		this.empname = empname;
	}

 
    /** minimal constructor */
    public EmployeeVO(Long id) {
        this.id = id;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String toString() {
        return new ToStringBuilder(this)
            .append("id", getId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof EmployeeVO) ) return false;
        EmployeeVO castOther = (EmployeeVO) other;
        return new EqualsBuilder()
            .append(this.getId(), castOther.getId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getId())
            .toHashCode();
    }

	public Object getPk() {
		return pk;
	}

	public void setPk(Object pk) {
		this.pk = pk;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

}
