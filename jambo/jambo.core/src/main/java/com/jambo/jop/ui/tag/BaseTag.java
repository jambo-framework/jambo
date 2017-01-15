package com.jambo.jop.ui.tag;

import com.jambo.jop.common.utils.lang.InterfaceUtils;
import com.jambo.jop.infrastructure.db.DBAccessUser;
import com.jambo.jop.ui.common.UserProvider;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * 给所有TAG提供user和dbflag
 * User: jinbo
 * Date: 13-8-6
 * Time: 上午9:19
 */
public class BaseTag extends BodyTagSupport {
    protected String dbFlag;
    protected String name;
    protected String style;
    protected String cssClass;
    protected String value;

    protected UserProvider provider;
    protected DBAccessUser user;

    StringBuffer results;

    public DBAccessUser getUser() {
        if (provider == null){
            provider = (UserProvider) InterfaceUtils.getInstance().createObject(UserProvider.class);
        }
        if (provider == null) {
            throw new IllegalArgumentException("Can't find or instant UserProvider's impl class! ");
        }

        user = provider.getUser();
        if (user == null) {
            user = DBAccessUser.getInnerUser();
//            throw new IllegalArgumentException("Can't find or instant UserProvider's impl class! ");
        }
        return user;
    }

    public void setUser(DBAccessUser user) {
        this.user = user;
    }

    public void setDbFlag(String dbFlag) {
        this.dbFlag = dbFlag;
    }

    public String getDbFlag() {
        String dbFlagValue = dbFlag;
        if (dbFlagValue == null) {
            dbFlagValue = getUser().getDbFlag();
        }

        return dbFlagValue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getCssClass() {
        return cssClass;
    }

    public void setCssClass(String cssClass) {
        this.cssClass = cssClass;
    }

    protected JspWriter getTagWriter() {
        return pageContext.getOut();
    }
}
