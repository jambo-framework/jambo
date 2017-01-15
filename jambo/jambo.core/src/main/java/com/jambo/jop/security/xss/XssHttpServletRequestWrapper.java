package com.jambo.jop.security.xss;

import com.jambo.jop.security.common.SecurityConfig;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.InputStream;
import java.util.*;
import java.util.regex.Pattern;

/**
 * XSS过滤器,将可能会导致XSS攻击的字符转换为全角字符,特殊页面可以通过配置来指定允许哪些字符
 *
 * @author jinbo
 */
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {
    public static final String REGEX_KEY_NAME = "__KEY__";
    HttpServletRequest orgRequest = null;

    private static List<Pattern> patternList = new ArrayList<Pattern>();

    //默认需要转换的XSS攻击字符与全角字符
    private static HashMap<String, String> defaultCharMap =  new HashMap<String, String>(){ {
            put(">", "＞");// 全角大于号
            put("<", "＜");// 全角小于号
            put("\'", "‘");// 全角单引号  //会导致参数无效，因为AJAX请求时会返回如{'key':'value'}格式的参数
            put("\"", "“");// 全角双引号  //会导致参数无效，因为AJAX请求时会返回如{"key":"value"}格式的参数
            put("&", "＆");// 全角&
            put("\\", "＼");// 全角斜线
            put("+", "＋");// 全角加号
            put("(", "〔");// 全角(号
            put(")", "〕");// 全角)号
            put("%", "％");// 全角%号
            put(";", "；");// 全角;号    //会导致无法导出文件，因为“;"用于分隔导出的每个字段
            put(".", "。");// 全角.号    //会导致无法下载文件，因为文件格式为filename.extension
        }
    };

	public XssHttpServletRequestWrapper(HttpServletRequest request) {
		super(request);
		orgRequest = request;
	}

    public static void init(String xssMapping){
        setXssMapping(xssMapping);

        patternList.clear();
        String[] keywords;
        String regex;
        if (SecurityConfig.xssMarks != null && SecurityConfig.xssMarksPattern != null){
            keywords = SecurityConfig.xssMarks.split(",");

            for (String keyword : keywords) {
                regex = SecurityConfig.xssMarksPattern.replace(REGEX_KEY_NAME, keyword);
                patternList.add(Pattern.compile(regex));
            }
        }

        if (SecurityConfig.xssAtts != null && SecurityConfig.xssAttsPattern != null){
            keywords = SecurityConfig.xssAtts.split(",");

            for (String keyword : keywords) {
                regex = SecurityConfig.xssAttsPattern.replace(REGEX_KEY_NAME, keyword);
                patternList.add(Pattern.compile(regex));
            }
        }

        if (SecurityConfig.xssWords != null && SecurityConfig.xssWordsPattern != null){
            keywords = SecurityConfig.xssWords.split(",");

            for (String keyword : keywords) {
                regex = SecurityConfig.xssWordsPattern.replace(REGEX_KEY_NAME, keyword);
                patternList.add(Pattern.compile(regex));
            }
        }
    }

    public static void setXssMapping(String xssMapping) {
        if (xssMapping != null && !xssMapping.isEmpty()){
            try {
                InputStream in = XssHttpServletRequestWrapper.class.getResourceAsStream(xssMapping);
                Properties properties = new Properties();
                properties.load(in);
                in.close();
                defaultCharMap = new HashMap(properties);
            } catch (Exception e){
                //出异常则忽略
                e.printStackTrace();
            }
        }
    }

    /**
	 * 覆盖getParameter方法，将参数名和参数值都做xss过滤。<br/>
	 * 如果需要获得原始的值，则通过super.getParameterValues(name)来获取<br/>
	 * getParameterNames,getParameterValues和getParameterMap也可能需要覆盖
	 */
	@Override
	public String getParameter(String name) {
		String value = super.getParameter(name);
		if (value != null) {
			value = xssEncode(value);
		}
		return value;
	}
	
	/**
	 * 覆盖getParameter方法，将参数名和参数值都做xss过滤。
	 */
	@Override
	public String[] getParameterValues(String name) {
		String[] newValues = null;
		String[] values = super.getParameterValues(name);
		if (null != values) {
			int size = values.length;
			newValues = new String[size];
			for (int i = 0; i < size; ++i) {
				newValues[i] = xssEncode(values[i]);
			}
		}
		return newValues;
	}
	
	/**
	 * 覆盖getParameter方法，将参数名和参数值都做xss过滤。
	 */
	@Override
	public Map getParameterMap() {
		final Map newParams = new HashMap();
		Map params = super.getParameterMap();
		if (null != params && !params.isEmpty()) {
			for (Object key : params.keySet()) {
				Object value = params.get(key);
				if (value instanceof String[]) {
					String[] values = (String[]) value;
					int size = values.length;
					String[] newValues = new String[size];
					for (int i = 0; i < size; ++i) {
						newValues[i] = xssEncode(values[i]);
					}
					newParams.put(key, newValues);
				}
			}
		}
		return newParams;
	}

	/**
	 * 覆盖getHeader方法，将参数名和参数值都做xss过滤。<br/>
	 * 如果需要获得原始的值，则通过super.getHeaders(name)来获取<br/>
	 * getHeaderNames 也可能需要覆盖
	 */
	@Override
	public String getHeader(String name) {
		String value = super.getHeader(name);
		if (value != null) {
			value = xssEncode(value);
		}
		return value;
	}

	/**
	 * 将容易引起xss漏洞的半角字符直接替换成全角字符
	 * 
	 */
	private String xssEncode(String s) {
		if (s == null || s.isEmpty()) {
			return s;
		}

        StringBuffer sb = new StringBuffer();
        boolean matched = false;
        for (Pattern p : patternList){
            matched = p.matcher(s).find();
            if (matched) {
                for (int i = 0; i < s.length(); i++) {
                    String c = String.valueOf(s.charAt(i));

                    if (defaultCharMap.containsKey(c)){
                        sb.append(defaultCharMap.get(c));
                    } else {
                        sb.append(c);
                    }
                }
                break;
            }
        }

        if (matched){
            return sb.toString();
        } else {
		    return s;
        }
	}

	/**
	 * 获取最原始的request
	 */
	public HttpServletRequest getOrgRequest() {
		return orgRequest;
	}

	/**
	 * 获取最原始的request的静态方法
	 */
	public static HttpServletRequest getOrgRequest(HttpServletRequest req) {
		if (req instanceof XssHttpServletRequestWrapper) {
			return ((XssHttpServletRequestWrapper) req).getOrgRequest();
		}

		return req;
	}
}
