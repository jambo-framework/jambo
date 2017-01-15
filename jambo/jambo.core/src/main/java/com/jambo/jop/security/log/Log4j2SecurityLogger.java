package com.jambo.jop.security.log;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Log4j2日志支持.
 * User: jinbo
 * Date: 13-9-17
 * Time: 下午9:37
 */
public class Log4j2SecurityLogger implements SecurityLogger{
    private static final Logger logger = LogManager.getLogger(AccessLogRequestChecker.class);

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
        logger.catching(e);
    }
}
