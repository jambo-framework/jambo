package com.jambo.jop.ui.freemarker;

import com.jambo.jop.common.utils.i18n.I18nMessage;
import com.jambo.jop.common.utils.lang.PublicUtils;
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
public class MessageTag implements TemplateDirectiveModel, TemplateMethodModelEx{
    public static final String TAG_RESOURCE_ROOT = "/i18n/";
    public static final String TAG_RESOURCE_BUNDLE = "bundle";
    public static final String TAG_RESOURCE_KEY = "key";

    private Logger log = LogManager.getLogger(MessageTag.class);

    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
        String bundle = null;
        if(params.containsKey(TAG_RESOURCE_BUNDLE)){
            bundle = params.get(TAG_RESOURCE_BUNDLE).toString();
        }
        String key = null;
        if(params.containsKey(TAG_RESOURCE_KEY)){
            key = params.get(TAG_RESOURCE_KEY).toString();
        }

        String msg = toMessage(bundle, key);

        Writer out = env.getOut();
        out.write(msg);
    }

    protected String toMessage(String bundle, String key) {
        String msg = "";
        if (!PublicUtils.isBlankString(key)) {
            try {
                if (PublicUtils.isBlankString(bundle)) msg = I18nMessage.getPublicString(key);
                else msg = I18nMessage.getString(TAG_RESOURCE_ROOT + bundle, key);

            } catch (Exception e) {
                log.warn("Can't find bundle for base name {}, locale zh_CN", TAG_RESOURCE_ROOT + bundle);
            }
        }
        return msg;
    }

    public Object exec(List arguments) throws TemplateModelException {
        String key = null;
        if (arguments.size() > 0)
            key = arguments.get(0).toString();
        String bundle = null;
        if (arguments.size() > 1)
            bundle = arguments.get(1).toString();

        return toMessage(bundle, key);
    }
}
