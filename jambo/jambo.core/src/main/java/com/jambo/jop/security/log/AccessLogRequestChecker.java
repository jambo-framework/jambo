package com.jambo.jop.security.log;

import com.jambo.jop.security.common.SecurityConfig;
import com.jambo.jop.security.common.SecurityLoggerUtils;
import com.jambo.jop.security.common.SecurityUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * WEB访问日志处理类,把操作员访问的URL记录下来,用于安全审计
 */
public class AccessLogRequestChecker {
    protected final int MAX_WAIT_TIME_SEC = 300;
    protected static String[] ignores = null;

    public AccessLogRequestChecker(String ignore) throws Exception {
        init(ignore);
    }

    public static void init(String ignore) {
        if (ignore != null) {
            ignores = ignore.split(":");
        }
    }

    public static boolean isIgnore(String uri) {
        return SecurityUtils.isIgnoreURI(uri, ignores);
    }

    public String getRequestInfo(HttpServletRequest request) {
        StringBuffer buffer = new StringBuffer();
        try {
            String currentURL = request.getRequestURI();
            if (request.getQueryString() != null) {
                currentURL = currentURL + "?" + request.getQueryString();
            }

            if (currentURL != null) {
                String contextPath = request.getContextPath();
                currentURL = currentURL.replaceFirst(contextPath, "");
            }

            String sessionid = null;
            String oprCode = null;

            HttpSession session = request.getSession();

            if (session != null) {
                sessionid = session.getId();
                oprCode = getOprCode(request);
            }

            buffer.append(oprCode).append("|").append(sessionid)
                    .append("|").append(SecurityUtils.getIpAddr(request)).append("|")
                    .append(currentURL);

        } catch (Throwable e1) {
            SecurityLoggerUtils.catching(e1);
        }

        return buffer.toString();
    }

    public String getOprCode(HttpServletRequest request) {
        String oprCode = null;
        if (SecurityConfig.operatorCodeProvider != null) {
            try {
                OperatorCodeProvider ocp = (OperatorCodeProvider) Class.forName(SecurityConfig.operatorCodeProvider).newInstance();
                oprCode = ocp.getOprcode(request);
            } catch (Exception e) {
                SecurityLoggerUtils.warn("operatorCodeProvider Error!");
            }
        }
        return oprCode;
    }

    public void log2file(String info, String type, Long ms) {
        if (SecurityConfig.openAccessLog) {
            StringBuffer buffer = new StringBuffer();
            buffer.append(type).append("|").append(info);
            if (ms != null) {
                double s = ms / 1000.0;
                buffer.append("|").append(s).append("s");
                if (s > MAX_WAIT_TIME_SEC)
                    buffer.append("|>").append(MAX_WAIT_TIME_SEC).append("s");
            }
            SecurityLoggerUtils.info(buffer.toString());
        }
    }

}