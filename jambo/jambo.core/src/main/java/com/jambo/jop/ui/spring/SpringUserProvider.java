/**
 * 
 */
package com.jambo.jop.ui.spring;

import com.jambo.jop.infrastructure.config.CoreConfigInfo;
import com.jambo.jop.infrastructure.db.DBAccessUser;
import com.jambo.jop.ui.common.UserProvider;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpSession;

/**
 * @author jinbo
 *
 */
public class SpringUserProvider implements UserProvider {
    @Autowired
    private HttpSession session;

//    @Autowired
//    private javax.servlet.http.HttpServletRequest request;

    /**
	 * 根据Struts2 的方式获取User
	 */
	public DBAccessUser getUser() {				
		DBAccessUser user = (DBAccessUser)session.getAttribute(CoreConfigInfo.SESSION_ATTRIBUTE_USER);
//        user = (DBAccessUser)ActionContext.getContext().getSession().get(WebConstant.SESSION_ATTRIBUTE_USER);
        return user;
	}
}
