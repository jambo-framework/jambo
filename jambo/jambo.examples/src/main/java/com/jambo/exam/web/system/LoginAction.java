package com.jambo.exam.web.system;

import com.jambo.exam.web.common.LoginedUser;
import com.jambo.jop.infrastructure.config.CoreConfigInfo;
import com.jambo.jop.ui.spring.BaseSpringAction;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @author jinbo
 * 登录控制器
 */
@Controller
@RequestMapping (value = "system/loginAction")
@SessionAttributes(CoreConfigInfo.SESSION_ATTRIBUTE_USER)
public class LoginAction extends BaseSpringAction {
	private static Logger log = LogManager.getLogger(LoginAction.class);

    @RequestMapping (value="welcome",method = RequestMethod.GET)
	public String welcome(HttpServletRequest request, HttpServletResponse response) {
        return "/jframe/login";
//        return "login";
	}

    @RequestMapping (value="login",method = RequestMethod.POST)
	public String login(HttpServletRequest request, HttpServletResponse response, @ModelAttribute("currentUser") LoginedUser user, Model model) {
		try {
			if(user == null) {
				return "error";
			}else	if(StringUtils.isBlank(user.getOprcode()) || StringUtils.isBlank(user.getPassword())) {
				return "error";
			}else {
				
//				Operator operator = (Operator) BOFactory.build(OperatorBO.class, DBAccessUser.getInnerUser());
//				OperatorVO vo = operator.findByPk(user.getOprcode());
//				if(vo == null) {
//					return "error";
//				}else {
//					if(!user.getPassword().equals(vo.getPssword())) {
//						return "error";
//					}
//					
//					user.setDbFlag(DBAccessUser.getInnerUser().getDbFlag());
//				}
                user.setOprcode("admin");
                user.setOpername("administrator");
                user.setDbFlag("COMMON");
                user.setPassword("admin");
			}

			user.setSessionID( request.getSession().getId() );
			user.setIp( request.getRemoteHost());
			user.setLogintime(new Date());

            model.addAttribute(CoreConfigInfo.SESSION_ATTRIBUTE_USER, user); //名为SESSION_ATTRIBUTE_USER的属性放到Session属性列表中
			return "/jframe/main";
//			return "main";
		}catch(Exception e) {
            this.setActionMessage(model, "登录错误");
			log.catching(e);
			return "/jframe/login";
//            return "login";
		}
	}

    @RequestMapping (value="logout",method = RequestMethod.GET)
	public String logout(HttpServletRequest request, HttpServletResponse responset, SessionStatus sessionStatus) {
        request.getSession().setAttribute(CoreConfigInfo.SESSION_ATTRIBUTE_USER, null);
        sessionStatus.setComplete();
		return "/jframe/login";
//		return "logout";
	}

    @RequestMapping (value="modifypassword",method = RequestMethod.POST)
	public String modifyPassword(HttpServletRequest request, HttpServletResponse response) {
		return "modifyPassword";
	}
}
