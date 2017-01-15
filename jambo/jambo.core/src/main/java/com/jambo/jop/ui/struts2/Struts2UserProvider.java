/**
 * 
 */
package com.jambo.jop.ui.struts2;

import com.jambo.jop.infrastructure.db.DBAccessUser;
import com.jambo.jop.ui.common.UserProvider;

/**
 * @author He Kun
 *
 */
public class Struts2UserProvider implements UserProvider {

	/**
	 * 根据Struts2 的方式获取User
	 */
	public DBAccessUser getUser() {				
		DBAccessUser user=null;
//        user = (DBAccessUser)ActionContext.getContext().getSession().get(WebConstant.SESSION_ATTRIBUTE_USER);
        return user;
	}
}
