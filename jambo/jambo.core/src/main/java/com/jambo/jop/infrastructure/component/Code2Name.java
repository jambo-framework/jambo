package com.jambo.jop.infrastructure.component;

import java.util.*;

import com.jambo.jop.infrastructure.db.*;

/**
 * 
 * @author He Kun
 *
 */
public interface Code2Name {

    /**
     * 转换编码值为名称
     * @param definition 数据字典定义
     * @param codeName key值
     * @param nameName value值
     * @param codeValue 返回映射后的value值
     */
    public String code2Name(String definition,String codeValue,String dbFlag) ;

    /**
     * 获取指定编码的名称列表
     * @param definition 数据字典定义
     */
    public Map valueList(String definition,String dbFlag) ;

    /**
     * 获取指定编码的名称列表
     * @param definition 数据字典定义
     */
    public Map valueList(String definition,String condition,String dbFlag) ;

    /**
     * 从数据库中查询数据字典，结果以map返回
     * @param def 数据字典定义
     * @param condition 查询条件
     * @param param 查询条件对象
     * @param dbFlag 数据库标识
     */
    public Map valueList(String def, String condition,DBQueryParam param, String dbFlag) ;

    /**
     * 从单表中 获取指定编码的名称列表。由于数据量过大，采用分页的方式获取，一次一页，必须指定页号。
     * @param definition 数据字典定义
     * @param condition 查询条件
     * @param param 查询条件对象
     * @param dbFlag 数据库标识
     */
    public DataPackage valueListPackage(String definition,String condition, DBQueryParam param,  String dbFlag) ;

    public String castConditionValue(String code2nameQryGroup, String code2nameCondition, String code2nameQryCode, String code2nameQryName);
}
