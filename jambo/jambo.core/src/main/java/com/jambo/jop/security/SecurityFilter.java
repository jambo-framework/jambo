package com.jambo.jop.security;

import com.jambo.jop.security.common.SecurityConfig;
import com.jambo.jop.security.common.SecurityUtils;
import com.jambo.jop.security.crsf.CRSFRequestChecker;
import com.jambo.jop.security.log.AccessLogRequestChecker;
import com.jambo.jop.security.repeat.RepeatCommitRequestChecker;
import com.jambo.jop.security.xss.XssHttpServletRequestWrapper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * JavaEE网络安全组件(JWS)
 * 访问日志:开关,记录是否成功(需要action配合写状态到session对象)
 * XSS字符处理:开关,url,过滤URL=字符配置文件
 * CRSF防范:开关,检查session里的TOKEN值
 * 重复访问:开关（-1秒为不控制,默认1秒内不允许连续同一个URL请求）
 * @author jinbo
 */
public class SecurityFilter implements Filter {
    private AccessLogRequestChecker accessLogger;

    public void doFilter(ServletRequest req, ServletResponse response,
			FilterChain filterChain) throws IOException, ServletException {
		/*
		 * 非http请求不予处理
		 */
		if(!(req instanceof HttpServletRequest)){
			filterChain.doFilter(req, response);
			return;
		}

        String info = "";
        Long begtime = System.currentTimeMillis();
        HttpServletRequest hsr = (HttpServletRequest) req;

        //CRSF处理
        CRSFRequestChecker.check(hsr);

        //重复请求屏蔽
        RepeatCommitRequestChecker.check(hsr);

        boolean isIgnoreLog = false;
        if (SecurityConfig.openAccessLog) {
            isIgnoreLog = accessLogger.isIgnore(hsr.getRequestURI());
            if (!isIgnoreLog){
                info = accessLogger.getRequestInfo(hsr);

                //清理调用处理状态
                hsr.getSession().setAttribute(SecurityUtils.SESSION_INVOKE_STATUS_KEY, null);
            }
        }
		try {
            //必须是同时配置 LogAccessBegin和 tOpenAccessLog为true才会记录begin
            if (SecurityConfig.logAccessBegin) {
                if (!isIgnoreLog){
                    accessLogger.log2file(info, "Begin", null);
                }
            }

            //XSS字符过滤
            if (SecurityConfig.openXSSWrapper) {
                XssHttpServletRequestWrapper xssRequest = new XssHttpServletRequestWrapper(hsr);
                filterChain.doFilter(xssRequest, response);
            } else{
                filterChain.doFilter(req, response);
            }

            //检查URL请求的处理状态
            if (!isIgnoreLog){
                Object status = hsr.getSession().getAttribute(SecurityUtils.SESSION_INVOKE_STATUS_KEY);
                if (status != null) {
                    accessLogger.log2file(info, (String) status, System.currentTimeMillis() - begtime);
                } else {
                    accessLogger.log2file(info, "End", System.currentTimeMillis() - begtime);
                }
            }
		} catch (ServletException sx) {
            accessLogger.log2file(info, "End-ExpServ", System.currentTimeMillis() - begtime);
			throw sx;
		} catch (IOException iox) {
            accessLogger.log2file(info, "End-ExpIO", System.currentTimeMillis() - begtime);
			throw iox;
		}
	}

	public void init(FilterConfig filterConfig) throws ServletException {
        try {
            if (SecurityConfig.openAccessLog){
                System.out.println("JWS:open Logger.");
                accessLogger = new AccessLogRequestChecker(SecurityConfig.logIgnore);
            }

            if (SecurityConfig.openCRSFCheckReferer || SecurityConfig.openCRSFCheckToken){
                System.out.println("JWS:open CRSF.");
                CRSFRequestChecker.init(SecurityConfig.crsfIgnore);
            }

            if (SecurityConfig.repeatInterval > 0){
                System.out.println("JWS:open RC.");
                RepeatCommitRequestChecker.init(SecurityConfig.repeatIgnore);
            }

            if (SecurityConfig.openXSSWrapper){
                System.out.println("JWS:open XSS.");
                XssHttpServletRequestWrapper.init(SecurityConfig.xssMapping);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	public void destroy() {
	}

}
