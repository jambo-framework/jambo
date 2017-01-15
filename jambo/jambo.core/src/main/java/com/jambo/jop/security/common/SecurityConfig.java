package com.jambo.jop.security.common;

import java.io.InputStream;
import java.util.Properties;

/**
 * 用于从配置文件中读取参数,如果读不到文件或者出现错误,则使用默认值,即关闭所有安全选项
 * User: jinbo
 * Date: 13-9-16
 * Time: 下午11:42
 */
public class SecurityConfig {
    public static final String CONFIG_FILE_PATH = "/data/securityconf.properties";

    public static String crsfWebName;
    public static String crsfIgnore;
    public static boolean openCRSFCheckReferer;
    public static boolean openCRSFCheckToken;

    public static long repeatInterval  = 0;
    public static String repeatIgnore;

    public static boolean openAccessLog;
    public static boolean logAccessBegin; //是否记录访问请求接入的日志
    public static String logIgnore;
    public static String operatorCodeProvider;
    public static String loggerProvider;

    public static boolean openXSSWrapper;
    public static String xssMapping;

    public static String xssMarks;
    public static String xssAtts;
    public static String xssWords;
    public static String xssMarksPattern;
    public static String xssAttsPattern;
    public static String xssWordsPattern;

    static {
        initial(null);
    }


    private static void initial(String path) {
        InputStream in = SecurityConfig.class.getResourceAsStream(CONFIG_FILE_PATH);
        Properties properties = new Properties();
        try {
            properties.load(in);

            openCRSFCheckReferer = getParamString(properties, "open.crsf.check.referer").equalsIgnoreCase("true");
            openCRSFCheckToken = getParamString(properties, "open.crsf.check.token").equalsIgnoreCase("true");
            crsfWebName = getParam(properties, "crsf.web.name");
            crsfIgnore = getParam(properties, "crsf.check.ignore");

            try {
                repeatInterval = Long.parseLong(getParamString(properties, "repeat.commit.interval"));
            } catch (NumberFormatException e) {
                repeatInterval = 0;
            }
            repeatIgnore = getParam(properties, "repeat.commit.ignore");

            openAccessLog = getParamString(properties, "open.access.log").equalsIgnoreCase("true");
            logAccessBegin = getParamString(properties, "open.access.log.begin").equalsIgnoreCase("true");
            operatorCodeProvider = getParam(properties, "open.access.log.operator.provider");
            logIgnore = getParam(properties, "open.access.log.ignore");
            loggerProvider = getParam(properties, "open.access.logger.provider");

            openXSSWrapper = getParamString(properties, "open.xss.wrapper").equalsIgnoreCase("true");
            xssMapping = getParam(properties, "xss.mapping");
            xssMarks = getParam(properties, "xss.filter.marks");
            xssAtts = getParam(properties, "xss.filter.attributes");
            xssWords = getParam(properties, "xss.filter.keywords");
            xssMarksPattern = getParam(properties, "xss.filter.marks.pattern");
            xssAttsPattern = getParam(properties, "xss.filter.attributes.pattern");
            xssWordsPattern = getParam(properties, "xss.filter.keywords.pattern");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 用key来查询配置清单中指定的配置文件路径，没值则返回空
     * @param key KEY值
     */
    public static String getParam(Properties prop, String key) {
        String value = null;
        if (prop != null)
            value = prop.getProperty(key);
        return value;
    }

    /**
     * 用key来查询配置清单中指定的配置文件路径，没值则返回空字符串
     */
    public static String getParamString(Properties prop, String key) {
        String v = getParam(prop, key);
        if (v == null) v = "";
        return v;
    }
}
