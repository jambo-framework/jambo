package com.jambo.jop.business.base.dictitem;

import com.jambo.jop.infrastructure.db.DBQueryParam;

/**
 * <p>Title: DictitemDBParam </p>;
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: jambo-framework Tech Ltd.</p>
 * @author Yedaoe
 * @version 1.0
 */
public class DictitemDBParam extends DBQueryParam {
    private String _se_dictid;
    private String _se_dictname;
    private String _se_groupid;
    private String _sne_dictid;
    private String _sk_dictname;

	public String get_sne_dictid() {
		return _sne_dictid;
	}
	public void set_sne_dictid(String _sne_dictid) {
		this._sne_dictid = _sne_dictid;
	}
	/**
     * @return Returns the _se_dictid.
     */
    public String get_se_dictid() {
        return this._se_dictid;
    }
    /**
     * @param _sk_companyname The _se_dictid to set.
     */
    public void set_se_dictid(String _se_dictid) {
        this._se_dictid = _se_dictid;
    }

	/**
     * @return Returns the _se_dictname.
     */
    public String get_se_dictname() {
        return this._se_dictname;
    }
    /**
     * @param _sk_companyname The _se_dictname to set.
     */
    public void set_se_dictname(String _se_dictname) {
        this._se_dictname = _se_dictname;
    }

	/**
     * @return Returns the _se_groupid.
     */
    public String get_se_groupid() {
        return this._se_groupid;
    }
    /**
     * @param _sk_companyname The _se_groupid to set.
     */
    public void set_se_groupid(String _se_groupid) {
        this._se_groupid = _se_groupid;
    }
	public String get_sk_dictname() {
		return _sk_dictname;
	}
	public void set_sk_dictname(String _sk_dictname) {
		this._sk_dictname = _sk_dictname;
	}

}
