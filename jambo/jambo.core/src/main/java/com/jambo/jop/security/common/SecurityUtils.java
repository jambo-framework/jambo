package com.jambo.jop.security.common;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * 安全组件公共工具类
 * User: jinbo
 * Date: 13-9-16
 * Time: 下午11:43
 */
public class SecurityUtils {
    public static final String SESSION_CRSF_TOKEN_KEY = "SECURITY_CRSF_TOKEN_KEY";
    public static final String REQUEST_CRSF_TOKEN_KEY = "crsf_token";
    public static final String SESSION_REPEAT_REQUEST_KEY = "SESSION_REPEAT_REQUEST_KEY";
    //在session读取调用处理的状态(成功,失败),无则不记录.这个功能需要调用的类来提供支持
    public static String SESSION_INVOKE_STATUS_KEY = "SECURITY_AUDIT_INVOKE_STATUS";

    //生成基于UUID的随机字符串,用来给CRSF防范用
    public static String genToken() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String getIpAddr(HttpServletRequest request) {
        String unknown = "unknown";
        String localIP = "127.0.0.1";
        String ip = request.getHeader("x-forwarded-for");
        if(ip == null || ip.length() == 0 || (localIP.equalsIgnoreCase(ip)) || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || (localIP.equalsIgnoreCase(ip)) || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");//
        }
        if(ip == null || ip.length() == 0 || (localIP.equalsIgnoreCase(ip)) || unknown.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    public static boolean isIgnoreURI(String uri, String[] ignores){
        if (uri != null && ignores != null) {
            for (String ext : ignores){
                if (uri.toLowerCase().endsWith(ext)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static String genSessionToken(HttpServletRequest request) {
        String token = genToken();
        request.setAttribute(SESSION_CRSF_TOKEN_KEY, token);
        return token;
    }

    public static String getSessionToken(HttpServletRequest request) {
        Object token = request.getAttribute(SESSION_CRSF_TOKEN_KEY);
        if (token != null){
            return (String)token ;
        } else {
            return null;
        }
    }
}
