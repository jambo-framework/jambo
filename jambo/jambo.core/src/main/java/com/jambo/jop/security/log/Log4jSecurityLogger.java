package com.jambo.jop.security.log;

import org.apache.log4j.Logger;

/**
 * Log4j日志支持
 * User: jinbo
 * Date: 13-9-17
 * Time: 下午9:37
 */
public class Log4jSecurityLogger implements SecurityLogger{
    private static final Logger logger = Logger.getLogger(AccessLogRequestChecker.class);

    public void info(String msg){
        logger.info(msg);
    }

    public void debug(String msg){
        logger.debug(msg);
    }

    public void warn(String msg){
        logger.warn(msg);
    }

    public void catching(Throwable e) {
        logger.error(e.getMessage(), e);
    }
}
