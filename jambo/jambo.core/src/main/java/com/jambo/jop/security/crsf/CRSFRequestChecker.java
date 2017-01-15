package com.jambo.jop.security.crsf;

import com.jambo.jop.security.common.SecurityConfig;
import com.jambo.jop.security.common.SecurityUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * CRSF攻击防范,需要业务端提供支持,实现会比较复杂
 * <p/>
 * 参考资料:http://www.ibm.com/developerworks/cn/web/1102_niugang_csrf/
 * User: jinbo
 * Date: 13-9-17
 * Time: 下午7:49
 */
public class CRSFRequestChecker {
    protected static String [] ignores = null;

    public static void check(HttpServletRequest hsr) throws TokenMissException, RefererMissException {
        if (!SecurityUtils.isIgnoreURI(hsr.getRequestURI(), ignores)) {
            if (SecurityConfig.openCRSFCheckReferer) {
                // 从 HTTP 头中取得 Referer 值
                String referer = hsr.getHeader("Referer");

                // 判断 Referer 是否以 项目的WEB名称 crsfWebName 开头
                if ((referer == null) || !((SecurityConfig.crsfWebName != null) && referer.trim().startsWith(SecurityConfig.crsfWebName))) {
                    throw new RefererMissException();
                }

            }

            if (SecurityConfig.openCRSFCheckToken) {
                String sessionToken = SecurityUtils.getSessionToken(hsr);

                if (sessionToken == null) {

                    // 产生新的 token 放入 session 中
                    sessionToken = SecurityUtils.genToken();
                    hsr.getSession().setAttribute(SecurityUtils.SESSION_CRSF_TOKEN_KEY, sessionToken);
                } else {
                    // 从 HTTP 头中取得 token
                    String httpToken = hsr.getHeader(SecurityUtils.SESSION_CRSF_TOKEN_KEY);

                    // 从请求参数中取得 token
                    String reqToken = hsr.getParameter(SecurityUtils.REQUEST_CRSF_TOKEN_KEY);

                    //分别尝试从请求中获取 token 参数以及从 HTTP 头中获取 token 自定义属性并与 session 中的值进行比较，只要有一个地方带有有效 token，就判定请求合法
                    if (!(sessionToken.equals(reqToken) || sessionToken.equals(httpToken))) {
                        throw new TokenMissException();
                    }
                }
            }
        }
    }

    public static void init(String ignore) {
        if (ignore != null){
            ignores = ignore.split(":");
        }
    }
}
