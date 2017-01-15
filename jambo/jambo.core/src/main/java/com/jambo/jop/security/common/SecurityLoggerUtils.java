package com.jambo.jop.security.common;

import com.jambo.jop.security.log.Log4j2SecurityLogger;
import com.jambo.jop.security.log.SecurityLogger;

/**
 * Created with IntelliJ IDEA.
 * User: jinbo
 * Date: 13-9-18
 * Time: 上午10:20
 */
public class SecurityLoggerUtils {
    private static SecurityLogger logger ;

    private SecurityLoggerUtils(){
    }

    protected static SecurityLogger getLogger() throws Exception {
        if (logger == null){
            if (SecurityConfig.loggerProvider != null) {
                logger = (SecurityLogger) Class.forName(SecurityConfig.loggerProvider).newInstance();
            } else {
                logger = new Log4j2SecurityLogger();
            }
        }
        return logger;
    }

    public static void info(String msg)  {
        try {
            getLogger().info(msg);
        } catch (Exception e) {
            e.printStackTrace();  
        }
    }

    public static void debug(String msg)  {
        try {
            getLogger().debug(msg);
        } catch (Exception e) {
            e.printStackTrace();  
        }
    }

    public static void warn(String msg) {
        try {
            getLogger().warn(msg);
        } catch (Exception e) {
            e.printStackTrace();  
        }
    }

    public static void catching(Throwable e){
        try {
            getLogger().catching(e);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

}
