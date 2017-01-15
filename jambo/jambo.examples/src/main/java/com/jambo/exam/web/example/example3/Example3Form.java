package com.jambo.exam.web.example.example3;

import com.jambo.exam.business.example.employee.persistent.EmployeeVO;

public class Example3Form extends EmployeeVO {
    private String companyname;

	public String getCompanyname() {
		return companyname;
	}

	public void setCompanyname(String companyname) {
		this.companyname = companyname;
	}

	public Example3Form(){
	}
}
