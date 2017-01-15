package com.jambo.jop.ui.tag;

import com.jambo.jop.common.utils.lang.InterfaceUtils;
import com.jambo.jop.infrastructure.config.CoreConfigInfo;
import com.jambo.jop.infrastructure.db.DBAccessUser;
import com.jambo.jop.ui.filter.PermissionChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;
import java.io.IOException;

/**
 * 该标签有2个属性：
 * permissionId			权限控制点属性
 * disableChild		子元素disabled属性
 * 
 * 该标签作用：判断用户是否有权限，根据2属性有3种不同显示
 * 1.用户有permid的权限：显示标签体所有内容
 * 2.用户没permid的权限，disableChild属性为true：标签体所有内容都添加disabled属性，链接变成不可用
 * 3.用户没permid的权限，disableChild属性不为true：不显示标签体内容
 * 
 * ----------------------------
 * 需要在描述文件*.tld里添加下面代码
 *
	<tag>
		<name>purChk</name>
		<tag-class>PurChkTag</tag-class>
		<body-content>JSP</body-content>
		<description>Examine that if the user have the permission.</description>
		<attribute>
			<name>permissionId</name>
			<required>true</required>
			<rtexprvalue>true</rtexprvalue>
		</attribute>
		<attribute>
			<name>disableChild</name>
			<required>false</required>
			<rtexprvalue>false</rtexprvalue>
		</attribute>
	</tag>
 * 
 * ----------------------------
 * @author hbm
 *
 */
public class PurChkTag extends BodyTagSupport {
	final static String DISABLED = " disabled ";
	
	private static Logger log = LoggerFactory.getLogger(PurChkTag.class);
	private String permissionId;

	private String disableChild = "true"; //默认无权限时禁用子元素

	public String getPermid() {
		return permissionId;
	}

	public void setPermid(String permissionId) {
		this.permissionId = permissionId;
	}

	public String getDisableChild() {
		return disableChild;
	}

	public void setDisableChild(String disableChild) {
		this.disableChild = disableChild;
	}

	public PurChkTag() throws Exception {
	}

	/**
	 * start tag
	 */
	public int doStartTag() throws JspException {
		return EVAL_BODY_BUFFERED;
	}

	/**
	 * end tag
	 */
	public int doEndTag() throws JspException {
		boolean isDisabled = checkDisabled();
		boolean hasPermission = checkPermission(permissionId);
		if (isDisabled) { //如果禁用无权限的控件
			if (bodyContent != null) {
				String oldBody = bodyContent.getString();
				JspWriter out = this.getPreviousOut();
				try {
					StringBuffer newBody = new StringBuffer(oldBody.trim());
					if (!hasPermission) {// 没权限						
						if(newBody.indexOf("<input" ) >=0)  {
							addDisabled(newBody, "<input");// 替<input>添加disabled属性
							out.print(newBody);
							
						}else if(newBody.indexOf("<select" ) >=0 ) {
							addDisabled(newBody, "<select");// 替<select>添加disabled属性	
							out.print(newBody);
							
						}else if( newBody.indexOf("<a" ) >=0) {
							deleteATag(newBody);  // 把<a>删除，只留下内容
							out.print(newBody);
							
						} //else //其他html标签，不显示
							
					}else	//有权限则显示
						out.print(newBody);
					
				} catch (IOException e) {
					throw new JspException(e);
				}
			}
		} else {
			if (hasPermission) {// 有权限
				if (bodyContent != null) {
					JspWriter out = this.getPreviousOut();
					try {
						out.print(bodyContent.getString());
					} catch (IOException e) {
						throw new JspException(e);
					}
				}
			}
		}

		return SKIP_BODY;
	}

	
	/**
	 * 检查disableChild是否为true
	 * @return
	 */
	private boolean checkDisabled() {
		if (disableChild != null && disableChild.equalsIgnoreCase("true")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断用户是否有权限
	 * @param permissionId
	 * @return
	 */
	private boolean checkPermission(String permissionId) {
		DBAccessUser user = (DBAccessUser) pageContext.getSession().getAttribute(CoreConfigInfo.SESSION_ATTRIBUTE_USER);
		String oprcode = user.getOprcode();
		boolean hasPermission = false;
		try {
			PermissionChecker checker = (PermissionChecker) InterfaceUtils.getInstance().createImplObject(PermissionChecker.class);
			hasPermission = checker.checkPermission( oprcode , permissionId, user);
		
		} catch (Exception e) {
			if(log.isWarnEnabled())
				log.warn("PurChkTag 鉴权失败:" + user.getOprcode() +"," + permissionId, e);
			return false;
		}
		if(log.isDebugEnabled())
			log.debug("检查权限:oprcode: " + oprcode + ",permissionId:" + permissionId + ", hasPermission? " + hasPermission );
		
		return hasPermission;
	}

	/**
	 * 对于<a href="xxx">test</a>,删除<a href="xxx"></a>只留下test
	 * 
	 * @param body
	 */
	private void deleteATag(StringBuffer body) {
		boolean needLoop = true;
		while (needLoop) {
			needLoop = false;
			int firstPoint = body.toString().toLowerCase().indexOf("<a ");
			if (firstPoint >= 0) {
				int secondPoint = body.indexOf(">", firstPoint + 3) + 1;
				int thirdPoint = body.toString().toLowerCase().indexOf("</a>",
						firstPoint + 3);
				body.delete(thirdPoint, thirdPoint + 4); // 先删除后面的字符
				body.delete(firstPoint, secondPoint); // 再删除前面的字符				
				needLoop = true;
			}
		}
	}

	/**
	 * 替目标标签添加disabled属性
	 * 
	 * @param body
	 * @param target
	 *            目标标签
	 */
	private void addDisabled(StringBuffer body, String target) {
		boolean needLoop = true;
		int currentPoint = 0;
		while (needLoop) {
			needLoop = false;
			int firstPoint = findPoint(body.toString(), target, currentPoint);
			if (firstPoint >= 0) {
				body.insert(firstPoint, DISABLED);
				currentPoint = firstPoint;
				needLoop = true;
			}
		}
	}

	/**
	 * 查找target所在位置
	 * 
	 * @param body
	 * @param target
	 * @param startPoint
	 * @return
	 */
	private int findPoint(String body, String target, int startPoint) {
		int point = body.toLowerCase().indexOf(target, startPoint);
		if (point >= 0) {
			point += target.length();
		}
		return point;
	}
	
	public static void main(String[] args) {
		
		StringBuffer body =new StringBuffer( "<a href=' \r\n /web/system/module_edit.do?param._pk=ACCTMONITOR_MAG'> \r\nACCTMONITOR_MAG\r\n  </a>");
		int firstPoint = body.toString().toLowerCase().indexOf("<a ");
		if (firstPoint >= 0) {
			int secondPoint = body.indexOf(">", firstPoint + 3) + 1;
			int thirdPoint = body.toString().toLowerCase().indexOf("</a>",
					firstPoint + 3);
			//body.delete(thirdPoint, thirdPoint + 4); // 先删除后面的字符
			//body.delete(firstPoint, secondPoint); // 再删除前面的字符
			
			body = new StringBuffer( body.substring(secondPoint, thirdPoint).trim());
			
		}
		
		System.out.println(body );
	}
}
