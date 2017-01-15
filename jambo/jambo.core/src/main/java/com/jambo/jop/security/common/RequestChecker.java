package com.jambo.jop.security.common;

/**
 * 提供忽略列表的统一处理
 * User: jinbo
 * Date: 13-9-17
 * Time: 下午10:42
 */
public class RequestChecker {
    protected static String [] ignores = null;

    public static void init(String ignore) {
        if (ignore != null){
            ignores = ignore.split("|");
        }
    }

    public static boolean isIgnore(String uri){
        if (uri != null && ignores != null) {
            for (String ext : ignores){
                if (uri.endsWith(ext)) {
                    return true;
                }
            }
        }
        return false;
    }
}
