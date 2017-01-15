package com.jambo.jop.infrastructure.service;

import com.jambo.jop.infrastructure.control.AbstractBO;import java.lang.Class;import java.lang.Override;

/**
 * 为了适配旧的MVC框架，把DUBBO服务的基础功能包装成BO，
 * User: jinbo
 */
public class BaseServiceBO extends AbstractBO {
    @Override
    protected Class getDAOClasses() {
        return null;
    }

}
