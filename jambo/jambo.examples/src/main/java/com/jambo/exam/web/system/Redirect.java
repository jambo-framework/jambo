package com.jambo.exam.web.system;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 用来重定向JSP文件
 * LoginedUser: jinbo
 * Date: 13-7-24
 * Time: 上午11:13
 */
@Controller
@RequestMapping (value = "/redirect")
public class Redirect {

    @RequestMapping
    public String redirect(String url){
        return url;
    }
}
