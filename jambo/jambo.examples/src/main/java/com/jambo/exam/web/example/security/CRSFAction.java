package com.jambo.exam.web.example.security;

import com.jambo.jop.security.common.SecurityUtils;
import com.jambo.jop.ui.spring.BaseSpringAction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value = "/crsfAction")
public class CRSFAction extends BaseSpringAction {
    public CRSFAction() {
        super();
    }

    @RequestMapping (value="test",method = RequestMethod.POST)
    public String welcome(HttpServletRequest request, HttpServletResponse response, String crsf_token, Model model) {
        model.addAttribute(SecurityUtils.REQUEST_CRSF_TOKEN_KEY, crsf_token); //名为SESSION_ATTRIBUTE_USER的属性放到Session属性列表中

        return "example/security/crsfTest";
    }

}
