package com.jambo.jop.business.base.dictitem;

import java.io.Serializable;

public interface IDictitemVO extends Serializable{
	
	public java.lang.String getDictid();
    public void setDictid(java.lang.String dictid);
    public java.lang.String getGroupid();
    public void setGroupid(java.lang.String groupid);
    public java.lang.String getDictname();
    public void setDictname(java.lang.String dictname);
    public java.lang.Short getSortorder();
    public void setSortorder(java.lang.Short sortorder);
    public java.lang.Byte getStatus();
    public void setStatus(java.lang.Byte status);
    public java.util.Date getStatusdate();
    public void setStatusdate(java.util.Date statusdate);
    public java.lang.String getDescription();
    public void setDescription(java.lang.String description);
	
}
