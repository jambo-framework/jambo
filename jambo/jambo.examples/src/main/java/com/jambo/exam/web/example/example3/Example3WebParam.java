package com.jambo.exam.web.example.example3;

import com.jambo.exam.business.example.employee.persistent.EmployeeDBParam;

public class Example3WebParam extends EmployeeDBParam {
	
	private Long _ne_companyid;
	private String _sk_companyname;
	
    private String companyname;

	public String getCompanyname() {
		return companyname;
	}

	public void setCompanyname(String companyname) {
		this.companyname = companyname;
	}

	public Long get_ne_companyid() {
		return _ne_companyid;
	}

	public void set_ne_companyid(Long _ne_companyid) {
		this._ne_companyid = _ne_companyid;
	}

	public String get_sk_companyname() {
		return _sk_companyname;
	}

	public void set_sk_companyname(String _sk_companyname) {
		this._sk_companyname = _sk_companyname;
	}

	
}
