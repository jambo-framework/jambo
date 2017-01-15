/**
 * 
 */
package com.jambo.jop.ui.component;

import javax.servlet.http.HttpServletRequest;

import com.jambo.jop.common.spring.SpringContextManager;

/**
 * 界面方案选择器，支持用户在界面选择显示方案。 一个方案名对应一个css目录下的子目录
 * @author He Kun
 *
 */
public class ThemeSelector {
	
	private String[] themes;
	
	private String defaultTheme = "default"; //dir: css/default/
	
	public String[] getThemes() {
		return themes;
	}

	public void setThemes(String[] themes) {
		this.themes = themes;
	}

	public String getDefaultTheme() {
		return defaultTheme;
	}

	public void setDefaultTheme(String defaultTheme) {
		this.defaultTheme = defaultTheme;
	}
	
	public static String getCurrentTheme(HttpServletRequest request) {
		String currentTheme = "default";
		try {
			ThemeSelector themeSelector = (ThemeSelector)SpringContextManager.getBean("ThemeSelector");
			currentTheme = themeSelector.getDefaultTheme();
		}catch(Exception e) {
			
		}
		
		if( request.getParameter("theme")!=null)  {
			currentTheme = request.getParameter("theme");
			//Cookie[] cookies = request. 设置到cookie
			
			request.getSession().setAttribute("theme",currentTheme);
			
		}else if(request.getSession().getAttribute("theme")!=null) {
			currentTheme = (String)request.getSession().getAttribute("theme");
		}
		return currentTheme;
	}
}
