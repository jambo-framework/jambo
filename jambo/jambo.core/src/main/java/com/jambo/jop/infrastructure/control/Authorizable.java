package com.jambo.jop.infrastructure.control;

import com.jambo.jop.infrastructure.db.*;

/**
 * 
 * @author He Kun
 *
 */
public interface Authorizable {
	
	void setUser(DBAccessUser user);
	
	DBAccessUser getUser();
}
