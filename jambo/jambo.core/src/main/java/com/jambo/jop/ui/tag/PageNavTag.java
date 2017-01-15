package com.jambo.jop.ui.tag;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

import com.jambo.jop.infrastructure.db.DataPackage;

/**
 * <p>
 * Title: PageNavTag
 * </p>
 * <p>
 * Description: 分页标签处理器
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: jambo-framework Tech Ltd.
 * </p>
 * 
 * @author WODEN
 * @version 1.0
 */
public class PageNavTag extends TagSupport {
	private String dpName;

	private static final String PAGE_NAV_JSP_PATH = "/common/pageNav.jsp";

	private static final String TOTAL_PAGE_NAME = "totalPage";

	private static final String CURRENT_PAGE_NAME = "currentPage";

	public void setDpName(String dpName) {
		this.dpName = dpName;
	}

	public int doStartTag() throws JspTagException {

		try {
			DataPackage bean = (DataPackage) pageContext.getRequest()
					.getAttribute(dpName);
			
			if (null == bean)
				return SKIP_BODY;

			int currentPage = bean.getPageNo();
			int totalPage = (int) Math.ceil(((double) bean.getRowCount())
					/ ((double) bean.getPageSize()));

			pageContext.getRequest().setAttribute(TOTAL_PAGE_NAME,
					new Integer(totalPage));
			pageContext.getRequest().setAttribute(CURRENT_PAGE_NAME,
					new Integer(currentPage));
			pageContext.include(PAGE_NAV_JSP_PATH);
		} catch (Exception ex) {
			// 如捕获异常,则不显示分页信息,不会导致任何破坏性的问题
			// 这里不需要做任何事情.
		}

		return SKIP_BODY;
	}
}
