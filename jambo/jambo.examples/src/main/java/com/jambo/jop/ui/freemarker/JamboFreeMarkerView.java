package com.jambo.jop.ui.freemarker;

import org.springframework.web.servlet.view.freemarker.FreeMarkerView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 给页面提供自定义的变量，简化页面开发
 */
public class JamboFreeMarkerView extends FreeMarkerView {
    private static final String CONTEXT_PATH = "contextPath";

    @Override
    protected void exposeHelpers(Map<String, Object> model,
                                 HttpServletRequest request) throws Exception {
        model.put(CONTEXT_PATH, request.getContextPath());
        super.exposeHelpers(model, request);
    }

}
