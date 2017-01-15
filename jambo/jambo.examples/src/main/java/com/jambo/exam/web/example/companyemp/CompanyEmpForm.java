package com.jambo.exam.web.example.companyemp;

import com.jambo.exam.business.example.companyemp.persistent.CompanyEmpVO;

/**
 * /** 
 * 复杂查询demo Form， 公司职员人数统计。职员人数empNumber是公司表所没有的字段，需要单独作为视图来映射。
 * @author He Kun
 *
 */
public class CompanyEmpForm extends CompanyEmpVO {

    private Long _ne_companyid;
    private String _sk_companyname;

    public CompanyEmpForm() {
        super();
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
