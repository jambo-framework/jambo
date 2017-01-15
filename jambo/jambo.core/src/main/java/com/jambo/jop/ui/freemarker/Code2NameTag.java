package com.jambo.jop.ui.freemarker;

import com.jambo.jop.common.utils.lang.Code2NameUtils;
import com.jambo.jop.infrastructure.config.CoreConfigInfo;
import freemarker.core.Environment;
import freemarker.template.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Map;

/**
 * 用来给Freemarker提供多国语言支持
 * 有2种使用方式
 * 范例1：<@message bundle="login" key="username"/>
 * 范例2：${message('userName',‘login')
 */
public class Code2NameTag implements TemplateDirectiveModel, TemplateMethodModelEx{
    public static final String TAG_DEFINITION = "definition";
    public static final String TAG_CODE= "code";

    private Logger log = LogManager.getLogger(Code2NameTag.class);

    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
        String definition = null;
        if(params.containsKey(TAG_DEFINITION)){
            definition = params.get(TAG_DEFINITION).toString();
        }
        String code = null;
        if(params.containsKey(TAG_CODE)){
            code = params.get(TAG_CODE).toString();
        }

        String msg = Code2NameUtils.code2Name(definition, code, CoreConfigInfo.COMMON_DB_NAME);

        Writer out = env.getOut();
        out.write(msg);
    }

    public Object exec(List arguments) throws TemplateModelException {
        String definition = null;
        if (arguments.size() > 0)
            definition = arguments.get(0).toString();
        String code = null;
        if (arguments.size() > 1)
            code = arguments.get(1).toString();

        return Code2NameUtils.code2Name(definition, code, CoreConfigInfo.COMMON_DB_NAME);
    }

}
