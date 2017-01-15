package com.jambo.jop.ui.spring;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.web.servlet.mvc.method.annotation.ExtendedServletRequestDataBinder;

import javax.servlet.ServletRequest;

/**
 * 从ExtendedServletRequestDataBinder.改造,保证页面的值能正常赋值给DBQueryParam的子类
 * User: jinbo
 * Date: 13-7-28
 * Time: 上午2:59
 */
public class RquestDataBinder extends ExtendedServletRequestDataBinder {
    public RquestDataBinder(Object target) {
        super(target);
    }

    protected void addBindValues(MutablePropertyValues mpvs, ServletRequest request) {
        PropertyValue value;
        for (int i = 0; i < mpvs.size(); i ++){
            value = mpvs.getPropertyValueList().get(i);
            mpvs.addPropertyValue(value.getName(), request.getParameterValues(value.getName()));
        }
    }

}
