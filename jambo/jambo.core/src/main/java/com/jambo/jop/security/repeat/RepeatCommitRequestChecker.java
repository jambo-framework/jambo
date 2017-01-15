package com.jambo.jop.security.repeat;

import com.jambo.jop.security.common.SecurityConfig;
import com.jambo.jop.security.common.SecurityLoggerUtils;
import com.jambo.jop.security.common.SecurityUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * 禁止操作员重复发请求的服务端控制.前端应该用JS再加强控制
 * User: jinbo
 * Date: 13-9-17
 * Time: 下午7:49
 */
public class RepeatCommitRequestChecker {
    protected static String [] ignores = null;

    public static void init(String ignore) {
        if (ignore != null){
            ignores = ignore.split(":");
        }
    }

    public static void check(HttpServletRequest hsr) throws RepeatRequestException {
        if (SecurityConfig.repeatInterval > 0 && hsr.getRequestURI() != null && !SecurityUtils.isIgnoreURI(hsr.getRequestURI(), ignores)) {
            Long begtime = System.currentTimeMillis();
            Object obj = hsr.getSession().getAttribute(SecurityUtils.SESSION_REPEAT_REQUEST_KEY);

            LastRequest lastReq = new LastRequest();
            if (obj != null) {
                lastReq = (LastRequest) obj;
                if (lastReq.url != null){
                    long interval = begtime - lastReq.time;
                    if (lastReq.equals(hsr) && (interval < SecurityConfig.repeatInterval)) {
                        //在指定的时间间隔内,如果来了同样URL的请求,则忽略
                        SecurityLoggerUtils.warn("ignore:" + lastReq.url);
                        throw new RepeatRequestException();
                    }
                }
            }

            lastReq.recordRequest(hsr, begtime);
            hsr.getSession().setAttribute(SecurityUtils.SESSION_REPEAT_REQUEST_KEY, lastReq);
        }
    }

}
