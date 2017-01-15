package com.jambo.jop.security.log;

import javax.servlet.http.HttpServletRequest;

/**
 * 用于获取当前操作的工号
 * User: jinbo
 * Date: 13-9-11
 * Time: 上午9:12
 */
public interface OperatorCodeProvider {
    public String getOprcode(HttpServletRequest request);
}
