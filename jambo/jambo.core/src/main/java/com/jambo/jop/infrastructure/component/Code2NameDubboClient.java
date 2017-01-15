package com.jambo.jop.infrastructure.component;

import com.jambo.jop.common.spring.SpringContextManager;
import com.jambo.jop.infrastructure.component.impl.Node;
import com.jambo.jop.infrastructure.db.DBQueryParam;
import com.jambo.jop.infrastructure.service.DataPackageWrap;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * Created by jinbo on 2015/7/13.
 */
public class Code2NameDubboClient implements Code2Name{
    @Autowired
    private Code2NameService service;

    public static void main(String[] args) {
        String str = Code2NameService.class.getSimpleName();
        System.out.println(str.replaceFirst(str.substring(0, 1),str.substring(0, 1).toLowerCase()));
    }

    public Code2NameService getService() {
        if (service == null) {
            String str = Code2NameService.class.getSimpleName();
            str = str.replaceFirst(str.substring(0, 1),str.substring(0, 1).toLowerCase());
            service = (Code2NameService) SpringContextManager.getBean(str);
        }
        return service;
    }

    public void setService(Code2NameService service) {
        this.service = service;
    }

    /**
     * 转换编码值为名称
     * @param definition 数据字典定义
     * @param codeName key字段名称
     * @param nameName value字段名称
     * @param codeValue key的值
     */
    public String code2Name(String definition, String codeValue, String dbFlag){
        return  getService().code2Name(definition, codeValue, dbFlag);
    }

    /**
     * 获取指定编码的名称列表
     * @param definition 数据字典定义
     */
    public Map<String, String> valueList(String definition, String dbFlag) {
        return  getService().valueList(definition, dbFlag);
    }

    /**
     * 获取指定编码的名称列表
     * @param definition 数据字典定义
     */
    public Map<String, String> valueList(String definition, String condition, String dbFlag) {
        return  getService().valueList(definition, condition, dbFlag);
    }

    /**
     * 从数据库中查询数据字典，结果以map返回
     * @param def 数据字典定义
     * @param condition 查询条件
     * @param param 查询条件对象
     * @param dbFlag 数据库标识
     */
    public Map<String, String> valueList(String def, String condition, DBQueryParam param, String dbFlag) {
        return  getService().valueList(def, condition, param, dbFlag);
    }

    /**
     * 从单表中 获取指定编码的名称列表。由于数据量过大，采用分页的方式获取，一次一页，必须指定页号。
     * @param definition 数据字典定义
     * @param condition 查询条件
     * @param param 查询条件对象
     * @param dbFlag 数据库标识
     */
    public DataPackageWrap<Node> valueListPackage(String definition, String condition, DBQueryParam param, String dbFlag) {
        return getService().valueListPackage(definition, condition, param, dbFlag);
    }

    public String castConditionValue(String code2nameQryGroup, String code2nameCondition, String code2nameQryCode, String code2nameQryName) {
        return getService().castConditionValue(code2nameQryGroup, code2nameCondition, code2nameQryCode, code2nameQryName);
    }
}
