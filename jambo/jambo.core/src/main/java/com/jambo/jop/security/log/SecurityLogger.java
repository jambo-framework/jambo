package com.jambo.jop.security.log;

/**
 * 用来支持自己喜欢的日志输出类
 * User: jinbo
 * Date: 13-9-17
 * Time: 下午9:35
 */
public interface SecurityLogger {
    public void info(String msg);

    public void debug(String msg);

    public void warn(String msg);

    public void catching(Throwable e);
}
