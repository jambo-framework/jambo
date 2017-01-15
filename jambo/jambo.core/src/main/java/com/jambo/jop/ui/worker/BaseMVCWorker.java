package com.jambo.jop.ui.worker;

import com.jambo.jop.common.utils.i18n.I18nMessage;
import com.jambo.jop.common.utils.lang.InterfaceUtils;
import com.jambo.jop.exception.system.ActionException;
import com.jambo.jop.infrastructure.control.AbstractBO;
import com.jambo.jop.infrastructure.db.BaseVO;
import com.jambo.jop.infrastructure.db.DBAccessUser;
import com.jambo.jop.infrastructure.db.DBQueryParam;
import com.jambo.jop.infrastructure.db.DataPackage;
import com.jambo.jop.ui.common.UserProvider;
import com.jambo.jop.ui.spring.ActionInfo;
import com.jambo.jop.ui.spring.RquestDataBinder;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.ClassUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.cglib.beans.BeanCopier;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;

/**
 * 抽象BaseAction的实现，使action能适合不同的MVC框架
 */
public class BaseMVCWorker {
    private static Logger log = LogManager.getLogger(BaseMVCWorker.class);

    private DBAccessUser user;
    private static UserProvider provider;


	protected DBQueryParam param;
	private BaseVO form;

	//数据操作对象，必须赋值
	private Class clsVO;
    private Class boClass;

    // 主键的名字数组，在删除时会用到，如果只有查询导出不会用到，否则必须在子类赋值，
    protected String[] pkNameArray = new String[]{"id"};

    //保存是否成功标识
    public String isSuccessful;

    private ActionInfo ai = new ActionInfo();

    BeanCopier copyFormToVO;
    BeanCopier copyVOToForm;

    private MVCAction action;

    public BaseMVCWorker(MVCAction action, Class boClass, Class voClass){
        this.action = action;
        setBOClass(boClass);
        setVOClass(voClass);
    }

    public String[] getPkNameArray() {
        return pkNameArray;
    }

    public void setPkNameArray(String[] pkNameArray) {
        this.pkNameArray = pkNameArray;
    }

    public ActionInfo getAi() {
        return ai;
    }

    public DBQueryParam getParam() {
		return param;
	}

	public void setParam(DBQueryParam param) {
		this.param = param;
	}

	public BaseVO getForm() {
		return form;
	}

	public void setForm(BaseVO form) {
		this.form = form;

        if (form != null) {
            copyFormToVO = BeanCopier.create(form.getClass(), clsVO, false);
            copyVOToForm = BeanCopier.create(clsVO, form.getClass(), false);
        }
    }

	public Class getVOClass() {
		return clsVO;
	}

    public void setVOClass(Class clsVO) {
		this.clsVO = clsVO;

        if (clsVO != null) {
            String clsname = action.getClass().getName();
            clsname = clsname.replace("Action", "Form");
            try {
                this.setForm((BaseVO) Class.forName(clsname).newInstance());
            } catch (Exception e) {
                log.warn("BaseAction Auto setFrom({}) failed!", clsname);
            }

            clsname = clsVO.getName();
            clsname = clsname.replace("VO", "DBParam");
            try {
                this.setParam((DBQueryParam) Class.forName(clsname).newInstance());
            } catch (Exception e) {
                log.warn("BaseAction Auto setParam({}) failed!", clsname);
            }

            if (form != null) {
                copyFormToVO = BeanCopier.create(form.getClass(), clsVO, false);
                copyVOToForm = BeanCopier.create(clsVO, form.getClass(), false);
            }
        }
    }

    /**
     * 取session里的user，一般也就是指当前操作员
     */
    public DBAccessUser getDBAccessUser(){
        if (provider == null){
            provider = (UserProvider) InterfaceUtils.getInstance().createObject(UserProvider.class);
        }

        user = provider.getUser();
		if(user == null){
			throw new RuntimeException("BaseAction Can't get session user!");
        } else {
            return  user;
        }
    }

    /**
     * 先取session里的user，如果没有就返回公共库，这个方法是给框架内部使用
     */
    public DBAccessUser getInnerUser(){
        if (provider == null){
            provider = (UserProvider) InterfaceUtils.getInstance().createObject(UserProvider.class);
        }

        user = provider.getUser();
		if(user == null){
            user = DBAccessUser.getInnerUser();
        }
        return  user;
    }

    public Class getBOClass() {
        return boClass;
    }

    public void setBOClass(Class clsControl) {
        this.boClass = clsControl;
    }

    /**
     * 把页面的值赋值给对象
     */
    public void bindObject(HttpServletRequest request, Object o) {
        if (o != null) {
            RquestDataBinder binder = new RquestDataBinder(o);
            binder.setFieldMarkerPrefix(null);
            binder.bind(request);
        }
    }

    /**
     * 用来清空form,param，避免这2个对象的值一直保留
     */
    public void reInitial() throws Exception{
        if (form != null) {
            setForm(form.getClass().newInstance());
        }
        if (param != null) {
            setParam(param.getClass().newInstance());
        }
    }

    public void add() throws Exception{
        // 新建时设置form的默认值
        setForm(form.getClass().newInstance());

        param.setCmd(MVCAction.WEB_CMD_NEW);
    }

    /**
     * DAO默认是不查询数据总数(不分页), 这里强制设定了querytype为查询总数,来实现默认是分页查询
     */
    public DataPackage list(DBQueryParam param) throws Exception{
        setActionMessage(null);
        DataPackage dp = null;
        try{
            dp = query(param);
        }catch (Exception e) {
            this.processException(e);
        }

		return dp;
	}

    protected DataPackage query(DBQueryParam queryPararm) throws Exception{
            return getBO().query(queryPararm);
    }

    protected AbstractBO getBO() throws Exception {
        return action.getBO();
    }

    public String getListUrl() {
        return action.getBasepath() + action.getListPage();
    }

    public String getContentUrl() {
        return action.getBasepath() + action.getContentPage();
    }

    /**
	 * 按单一主键删除时，返回主键 用于List.jsp按删除按钮时，从参数中取出删除数据的主键列
	 */
	protected Serializable getSelectedPK(String pkValue) throws Exception {
		Method[] methodArray = clsVO.getMethods();
		Class pkType = null;
		for (int i = 0; i < methodArray.length; i++) {
			if (methodArray[i].getName().equalsIgnoreCase(
					"get" + pkNameArray[0])) {
				pkType = methodArray[i].getReturnType();
			}
		}
		if (Integer.class == pkType) {
			return new Integer(pkValue);
		} else if (Long.class == pkType) {
			return new Long(pkValue);
		} else if (String.class == pkType) {
			return pkValue;
		} else {
			throw new ActionException("action.err.wrongpk", pkValue, null);
		}
	}

	/**
	 * 按复合主键删除时，返回主键VO 用于List.jsp按删除按钮时，从参数中取出删除数据的主键列
	 */
	protected Serializable getSelectedPkVO(String pkValue) throws Exception {
		String[] pkValueArray = pkValue.split("\\|");
		Serializable vo = (Serializable) clsVO.newInstance();
		for (int j = 0; j < pkValueArray.length; j++) {
			BeanUtils.setProperty(vo, pkNameArray[j], pkValueArray[j]);
		}
		return vo;
	}

	public void edit(DBQueryParam param) throws Exception{
        setActionMessage(null);
        BaseVO vo = findVOFromDB(param.get_pk());

        copyVOToForm.copy(vo, form, null);

        param.setCmd(MVCAction.WEB_CMD_EDIT);
	}

	protected BaseVO findVOFromDB(String _pk) throws Exception {
		if (_pk == null)
			throw new ActionException("action.err.nullpk");

		BaseVO contentVO = null;
        AbstractBO bo = getBO();
        try{
			if (!_pk.trim().equalsIgnoreCase("")) { // 由list.jsp 传过来
				if (_pk.indexOf("|") == -1) { // 单一主键
                    contentVO = bo.findByPk(getSelectedPK(_pk));
				} else { // 复合主键
                    contentVO = bo.findByPk(getSelectedPkVO(_pk));
				}
			}
			if (contentVO == null){
				String [] param = {_pk, ClassUtils.getShortClassName(getForm().getClass())};
				throw new ActionException("action.err.recordnotfound", param, null);
			}
		}catch (Exception e) {
			this.processException(e);
		}

		return contentVO;
	}

	public void save(DBQueryParam param, BaseVO form) throws Exception{
        AbstractBO bo = getBO();
        BaseVO vo = null;
		if (MVCAction.WEB_CMD_NEW.equals(param.getCmd())) {
			try{
				isSuccessful = MVCAction.WEB_SUCCESS_YES;
				if (pkNameArray.length == 1) { // 单一主键
					Object pk = PropertyUtils.getNestedProperty(form, pkNameArray[0]);
					// 这里返回的是String类型
					if (pk != null) {
                        vo = bo.findByPk((Serializable) pk);
					}
				} else {// 复合主建
                    vo = bo.findByPk(form);
				}
				if (vo == null) {
                    BaseVO pojo = (BaseVO) clsVO.newInstance();
                    copyFormToVO.copy(form, pojo, null);
                    vo = bo.doCreate(pojo);

                    copyVOToForm.copy(vo, form, null);

                    setActionMessage(I18nMessage.getPublicString("msgSaveSuccess"));
				} else {
					onDuplicatePk();
					isSuccessful = MVCAction.WEB_SUCCESS_NO;
				}
			}catch (Exception e) {
				isSuccessful = MVCAction.WEB_SUCCESS_NO;
				this.processException(e);
			}
            param.setCmd(MVCAction.WEB_CMD_SAVE);
		} else {
			try{
                param.setCmd(MVCAction.WEB_CMD_SAVE);
				isSuccessful = MVCAction.WEB_SUCCESS_YES;

                vo = (BaseVO) clsVO.newInstance();
                copyFormToVO.copy(form, vo, null);
                vo = bo.doUpdate(vo);

                setActionMessage(I18nMessage.getPublicString("msgSaveSuccess"));
			}catch (Exception e) {
				isSuccessful = MVCAction.WEB_SUCCESS_NO;
				this.processException(e);
			}
            param.setCmd(MVCAction.WEB_CMD_SAVE);
		}
        setParam(param);
        setForm(vo);
	}

    public void delete(DBQueryParam param) throws Exception{
//        isSuccessful = MVCAction.WEB_SUCCESS_YES;
        String[] selectArray = param.get_selectitem();

        if(selectArray == null) {
            setActionMessage(I18nMessage.getPublicString("msgNoSelect"));
        } else {
            try{
                AbstractBO bo = getBO();

                for (int i = 0; i < selectArray.length; i++) {
                    if (selectArray[0].indexOf('|') == -1) { // 单一主键
                        bo.doRemoveByPK(getSelectedPK(selectArray[i]));
                    } else { // 复合主键
                        bo.doRemoveByVO((BaseVO) getSelectedPkVO(selectArray[i]));
                    }
                }
            }catch (Exception e) {
//                isSuccessful = MVCAction.WEB_SUCCESS_NO;
                this.processException(e);
            }
        }
    }

    /*
     * 模板方法, 允许子类设置主键重复时的错误消息 @param actionMapping @param actionForm @param
     */
    protected void onDuplicatePk() {
        setActionMessage(I18nMessage.getPublicString("msgDuplicatePk"));
    }

    public void processException(Throwable e){
        log.catching(e);
		String msg = "";
		if(e instanceof InvocationTargetException) {
			InvocationTargetException ie = (InvocationTargetException) e;
			Throwable t = ie.getTargetException();
			if( null != t.getCause()){
				if(null != t.getCause().getMessage())
					msg = 	t.getCause().getMessage();
			}else{
				msg = t.getMessage()!=null ? t.getMessage() : t.toString();
			}
		}else if(e instanceof UndeclaredThrowableException) {
			UndeclaredThrowableException ue = (UndeclaredThrowableException)e;
			Throwable t = ue.getUndeclaredThrowable();
			if( null != t.getCause()){
				if(null != t.getCause().getMessage())
					msg = 	t.getCause().getMessage();
			}else{
				msg = t.getMessage()!=null ? t.getMessage() : t.toString();
			}

		}else {
			if( null != e.getCause()){
				if(null != e.getCause().getMessage())
					msg = 	e.getCause().getMessage();
			}else{
				msg = e.getMessage()!=null ? e.getMessage() : e.toString();
			}
		}

        setActionMessage(msg);
	}

    private void setActionMessage(String msg) {
        ai.setMessage(msg);
    }
}
