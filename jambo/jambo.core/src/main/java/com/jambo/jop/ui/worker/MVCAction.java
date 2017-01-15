package com.jambo.jop.ui.worker;

import com.jambo.jop.infrastructure.control.AbstractBO;
import com.jambo.jop.infrastructure.db.BaseVO;
import com.jambo.jop.infrastructure.db.DBAccessUser;
import com.jambo.jop.infrastructure.db.DBQueryParam;
import org.springframework.ui.Model;

/**
 * Created with IntelliJ IDEA.
 * User: jinbo
 * Date: 15-2-12
 * Time: 上午11:30
 */
public interface MVCAction {
    public static final String WEB_RESULT_LIST = "list";
    public static final String WEB_RESULT_CONTENT = "content";
    public static final String WEB_RESULT_LIST_MODEL_NAME = "dp";
    public static final String WEB_PAGE_PARAM = "param";
    public static final String WEB_PAGE_FORM = "form";

    public static final String WEB_VALUE_CMD = "cmd";
    public static final String WEB_CMD_NEW = "NEW";
    public static final String WEB_CMD_EDIT = "EDIT";
    public static final String WEB_CMD_SAVE = "SAVE";

    public static final String WEB_SUCCESS_YES = "YES";
    public static final String WEB_SUCCESS_NO = "NO";

    public static final String WEB_ACTION_INFO = "ActionInfo";

    public String getBasepath();

    public void setBasepath(String basepath);

    public void setForm(BaseVO form);

    public void setParam(DBQueryParam param);

    public void setPkNameArray(String[] pkNameArray);

    public void setActionMessage(Model model, String message);

    public DBAccessUser getDBAccessUser();

    public AbstractBO getBO() throws Exception;

    public String getListPage();

    public String getContentPage();
}
