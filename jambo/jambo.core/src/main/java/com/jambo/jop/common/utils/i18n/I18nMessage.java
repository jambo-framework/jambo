package com.jambo.jop.common.utils.i18n;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * User: JinBo
 * Date: 2005-12-28
 * Time: 8:48:17
 * I18nMessage用于类的国际化语言处理。
 */                 
public  class I18nMessage {
	public static final String PUBLIC_RESOURCE_NAME = "i18n/public";
	
	
    /**
     * 取指定的语言信息
     *
     * @param bundleName 资源库名称
     * @param key        关键字
     * @return 语言信息
     */
    public static String getString(String bundleName, String key) {    	
        ResourceBundle rb = getResourceBundle(bundleName,null);

        return getString(rb, key);
    }
    
    /**
     * 
     * @param bundleName
     * @param key
     * @return
     */
    public static String getString(String bundleName, String key, Locale locale) {
        ResourceBundle rb = getResourceBundle(bundleName,locale);
        return getString(rb, key);
    }
    
    /**
     * 从public资源中取指定的信息，并用替换参数
     * @param key 关键字
     * @param args 参数值
     * @return 信息
     */
    public static String getString(String bundleName, String key, Object[] args){
    	return MessageFormat.format(getString(bundleName, key), args);
    }

    /**
     * 取资源文件里的指定信息
     *
     * @param rb  已经打开的资源文件
     * @param key 关键字
     * @return 语言信息
     */
    public static String getString(ResourceBundle rb, String key) {
        String result = key;
        if (rb == null) return key;
        if ((key != null) && (key.length() != 0)) {
            try {
                result = rb.getString(key);
            } catch (MissingResourceException e) {
                result = key;
            }
        }
        return result;
    }

    /**
     * 打开资源文件
     *
     * @param bundleName 资源文件名称
     * @return 资源文件
     */
    private static ResourceBundle getResourceBundle(String bundleName,Locale locale) {
    	if( locale != null ){
    		return ResourceBundle.getBundle(bundleName,locale);
    	}
        return ResourceBundle.getBundle(bundleName);
    }
    
    /**
     * Public资源是指放在根目录下，名称为Public的资源文件
     * @param key
     * @return
     */
    public static String getPublicString(String key){
    	return getString(PUBLIC_RESOURCE_NAME, key);
    }
}
