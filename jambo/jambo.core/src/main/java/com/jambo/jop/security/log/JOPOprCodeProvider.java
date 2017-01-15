package com.jambo.jop.security.log;

import com.jambo.jop.infrastructure.config.CoreConfigInfo;
import com.jambo.jop.infrastructure.db.DBAccessUser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created with IntelliJ IDEA.
 * User: jinbo
 * Date: 13-9-11
 * Time: 上午9:20
 */
public class JOPOprCodeProvider implements OperatorCodeProvider {

    public String getOprcode(HttpServletRequest request) {
        String opercode = null;
        HttpSession session = request.getSession();

        if (session != null) {
            DBAccessUser user = (DBAccessUser) session
                    .getAttribute(CoreConfigInfo.SESSION_ATTRIBUTE_USER);

            if (user != null) {
                opercode = user.getOprcode();
            }
        }
        return opercode;
    }
}
