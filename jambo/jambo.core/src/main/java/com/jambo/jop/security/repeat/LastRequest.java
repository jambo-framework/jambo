package com.jambo.jop.security.repeat;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 用于保存上次请求的URL与时间
 * User: jinbo
 * Date: 13-9-12
 * Time: 上午10:02
 */
public class LastRequest {
    public String url;
    public long time;
    public Map parameterMap;

    public boolean equals(HttpServletRequest request){
        if (request == null || url == null){
            return false;
        } else {
            return url.equals(request.getRequestURI() + "?" + request.getQueryString());
        }
    }

    public void recordRequest(HttpServletRequest request, long reqTime){
        url = request.getRequestURI() + "?" + request.getQueryString();
        time = reqTime;
    }
}
