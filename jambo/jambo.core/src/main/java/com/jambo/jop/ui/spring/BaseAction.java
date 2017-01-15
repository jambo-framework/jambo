package com.jambo.jop.ui.spring;

import com.jambo.jop.infrastructure.control.AbstractBO;
import com.jambo.jop.infrastructure.control.BOFactory;
import com.jambo.jop.infrastructure.db.BaseVO;
import com.jambo.jop.infrastructure.db.DBAccessUser;
import com.jambo.jop.infrastructure.db.DBQueryParam;
import com.jambo.jop.infrastructure.db.DataPackage;
import com.jambo.jop.ui.worker.BaseMVCWorker;
import com.jambo.jop.ui.worker.MVCAction;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 2013-7-23 jinbo 从struts2改为springmvc，蛋痛
 * 继承MultiActionController,用于实现CoC方式的CRUD
 * 优点：所说中的CoC模式，几乎不用配置，子类需要写的方法也比较少，也可以指定springMVCURL映射规则
 * 缺点：参数，对象绑定有严格的要求，很多情况下需要自己调用BaseAction的bind方法来绑定http入参到自己的对象
 */
public abstract class BaseAction extends MultiActionController  implements MVCAction {
	private static final long serialVersionUID = 1L;
    private static Logger log = LogManager.getLogger(BaseAction.class);

//    private DBAccessUser user;
//    private static UserProvider provider;

//    public static final String WEB_RESULT_LIST = "list";
//	public static final String WEB_RESULT_CONTENT = "content";
//    public static final String WEB_RESULT_LIST_MODEL_NAME = "dp";
//    public static final String WEB_PAGE_PARAM = "param";
//    public static final String WEB_PAGE_FORM = "form";

//	protected DBQueryParam param;
//	private BaseVO form;

	//数据操作对象，必须赋值
//	private Class clsVO;
//    private Class boClass;
    // 主键的名字数组，在删除时会用到，如果只有查询导出不会用到，否则必须在子类赋值，
//    protected String[] pkNameArray = new String[]{"id"};

//    public static final String WEB_VALUE_CMD = "cmd";
//	public static final String WEB_CMD_NEW = "NEW";
//	public static final String WEB_CMD_EDIT = "EDIT";
//	public static final String WEB_CMD_SAVE = "SAVE";
	
	//保存是否成功标识
//	public String isSuccessful;
//	public static final String WEB_SUCCESS_YES = "YES";
//	public static final String WEB_SUCCESS_NO = "NO";

    private Map map = new HashMap();
    protected String basepath;
//    private ActionInfo ai = new ActionInfo();

//    BeanCopier copyFormToVO;
//    BeanCopier copyVOToForm;

    private BaseMVCWorker worker;


    public BaseAction(){

    }

    public BaseMVCWorker getWorker() {
        return worker;
    }

    public void setWorker(BaseMVCWorker worker) {
        this.worker = worker;
    }

    public DBQueryParam getParam() {
		return getWorker().getParam();
	}

	public void setParam(DBQueryParam param) {
        getWorker().setParam(param);
	}

	public BaseVO getForm() {
        return worker.getForm();
	}

	public void setForm(BaseVO form) {
        worker.setForm(form);
    }
    public String getBasepath(){
        return basepath;
    }

    public void setBasepath(String basepath){
        this.basepath = basepath;
    }

    public void setActionMessage(Model model, String message){
        getWorker().getAi().setMessage(message);
        model.addAttribute("ActionInfo", getWorker().getAi());
  }

    public void setPkNameArray(String[] pkNameArray) {
        worker.setPkNameArray(pkNameArray);
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new DateEditor());
    }

    /**
     * 坑爹的MultiActionController,只有子类的方法有三个参数时,才会调用这个方法,所以只有两个参数的,只能手工调
     */
    @Override
    protected void bind(HttpServletRequest request, Object command) throws Exception{
        if (command != null) {
            super.bind(request, command);
        }

        getWorker().reInitial();
        bindObject(request, getWorker().getParam());
        bindObject(request, getWorker().getForm());

        //再设置自定义的参数
        for(Object key : map.keySet()) {
            try {
                BeanUtils.setProperty(getWorker().getParam(), (String) key, map.get(key));
            } catch (Exception e) {
                log.warn("bind {} error!", key);
            }
        }
        map.clear();
    }

    /**
     * 把页面的值赋值给对象
     */
    protected void bindObject(HttpServletRequest request, Object o) {
        if (o != null) {
            RquestDataBinder binder = new RquestDataBinder(o);
            binder.setFieldMarkerPrefix(null);
            binder.bind(request);
        }
      }

    /**
     * 给action里的DBQueryParam param参数对象的指定属性名称(paramname)设置value值,失败只会在日志输出警告
     * 注意,调用这个方法后,不会直接对param对象赋值,需要在bind方法执行才有效
     * @param paramname DBQueryParam的属性名称
     * @param value DBQueryParam的属性值
     */
    public void bindQueryParam(String paramname, Object value){
        if (paramname != null){
            map.put(paramname, value);
        }
    }

//	public Class getVOClass() {
//		return clsVO;
//	}

//    public void setVOClass(Class clsVO) {
//		this.clsVO = clsVO;
//
//        String clsname = getClass().getName();
//        clsname = clsname.replace("Action", "Form");
//        try {
//            this.setForm((BaseVO) Class.forName(clsname).newInstance());
//        } catch (Exception e) {
//            log.warn("BaseAction Auto setFrom({}) failed!", clsname);
//        }
//
//        clsname = clsVO.getName();
//        clsname = clsname.replace("VO", "DBParam");
//        try {
//            this.setParam((DBQueryParam) Class.forName(clsname).newInstance());
//        } catch (Exception e) {
//            log.warn("BaseAction Auto setParam({}) failed!", clsname);
//        }
//
//        if (form != null) {
//            copyFormToVO = BeanCopier.create(form.getClass(), clsVO, false);
//            copyVOToForm = BeanCopier.create(clsVO, form.getClass(), false);
//        }
//    }

    public DBAccessUser getDBAccessUser(){
        return getWorker().getDBAccessUser();
    }
//        if (provider == null){
//            provider = (UserProvider) InterfaceUtils.getInstance().createObject(UserProvider.class);
//        }
//
//        user = provider.getUser();
//		if(user == null){
//			throw new RuntimeException("BaseAction Can't get session user!");
//        } else {
//            return  user;
//        }
//    }

    public Class getBOClass() {
        return getWorker().getBOClass();
    }

    public void setBOClass(Class clsControl) {
        getWorker().setBOClass(clsControl);
    }

//    protected String getMapping(String page) {
//        return basepath + page;
//    }

    public ModelAndView add(HttpServletRequest request, HttpServletResponse response) throws Exception{
        bind(request, null);
        // 新建时设置form的默认值
        setForm(getWorker().getForm().getClass().newInstance());

        getWorker().add();

        ModelAndView model = new ModelAndView(getWorker().getContentUrl());
        model.addObject(WEB_PAGE_PARAM, getWorker().getParam());
        model.addObject(WEB_PAGE_FORM, this.getForm());
        model.addObject(WEB_VALUE_CMD, WEB_CMD_NEW);
        return model;
    }

    /**
     * DAO默认是不查询数据总数(不分页), 这里强制设定了querytype为查询总数,来实现默认是分页查询
     */
    public ModelAndView list(HttpServletRequest request, HttpServletResponse response) throws Exception{
        bind(request, null);

        ModelAndView model = new ModelAndView(getWorker().getListUrl());

        DataPackage dp = getWorker().list(getWorker().getParam());

        model.addObject(WEB_PAGE_PARAM, getWorker().getParam());
        model.addObject(WEB_RESULT_LIST_MODEL_NAME, dp);
		return model;
	}

    protected DataPackage query(DBQueryParam queryPararm) throws Exception{
            return getBO().query(queryPararm);
    }

    public AbstractBO getBO() throws Exception {
        AbstractBO bo = BOFactory.build(getBOClass(), getDBAccessUser());
        return bo;
    }

    public String getListPage() {
        return WEB_RESULT_LIST;
    }

    public String getContentPage() {
        return WEB_RESULT_CONTENT;
    }

    /**
	 * 按单一主键删除时，返回主键 用于List.jsp按删除按钮时，从参数中取出删除数据的主键列
	 */
//	protected Serializable getSelectedPK(String pkValue) throws Exception {
//		Method[] methodArray = clsVO.getMethods();
//		Class pkType = null;
//		for (int i = 0; i < methodArray.length; i++) {
//			if (methodArray[i].getName().equalsIgnoreCase(
//					"get" + pkNameArray[0])) {
//				pkType = methodArray[i].getReturnType();
//			}
//		}
//		if (Integer.class == pkType) {
//			return new Integer(pkValue);
//		} else if (Long.class == pkType) {
//			return new Long(pkValue);
//		} else if (String.class == pkType) {
//			return pkValue;
//		} else {
//			throw new ActionException("action.err.wrongpk", pkValue, null);
////			throw new Exception("错误的主键类型");
//		}
//	}

	/**
	 * 按复合主键删除时，返回主键VO 用于List.jsp按删除按钮时，从参数中取出删除数据的主键列
	 */
//	protected Serializable getSelectedPkVO(String pkValue) throws Exception {
//		String[] pkValueArray = pkValue.split("\\|");
//		Serializable vo = (Serializable) clsVO.newInstance();
//		for (int j = 0; j < pkValueArray.length; j++) {
//			BeanUtils.setProperty(vo, pkNameArray[j], pkValueArray[j]);
//		}
//		return vo;
//	}

	public ModelAndView edit(HttpServletRequest request, HttpServletResponse response) throws Exception{
        bind(request, null);
        ModelAndView model = new ModelAndView(getWorker().getContentUrl());

        getWorker().edit(getWorker().getParam());

        model.addObject(WEB_PAGE_PARAM, getWorker().getParam());
        model.addObject(WEB_PAGE_FORM, this.getForm());
        model.addObject(WEB_VALUE_CMD, WEB_CMD_EDIT);
        return model;
    }

//	protected BaseVO findVOFromDB(ModelAndView model, String _pk) throws Exception {
////		String pk = getParam().get_pk();
//		if (_pk == null)
//			throw new ActionException("action.err.nullpk");
//
//		BaseVO contentVO = null;
//        AbstractBO bo = getBO();
//        try{
//			if (!_pk.trim().equalsIgnoreCase("")) { // 由list.jsp 传过来
//				if (_pk.indexOf("|") == -1) { // 单一主键
//                    contentVO = bo.findByPk(getSelectedPK(_pk));
//				} else { // 复合主键
//                    contentVO = bo.findByPk(getSelectedPkVO(_pk));
//				}
//			}
//			if (contentVO == null){
//				String [] param = {_pk, ClassUtils.getShortClassName(getForm().getClass())};
//				throw new ActionException("action.err.recordnotfound", param, null);
//			}
//		}catch (Exception e) {
//			this.processException(model, e);
//		}
//
//		return contentVO;
//	}

	public ModelAndView save(HttpServletRequest request, HttpServletResponse response) throws Exception{
        bind(request, null);
        //由于 DBQueryParam 存在 大量以_开头的属性，默认的spring绑定方法会忽略掉这些属性，所以得自己来
        getWorker().bindObject(request, getWorker().getParam());
        ModelAndView model = new ModelAndView(getWorker().getContentUrl());

        getWorker().save(getWorker().getParam(), getWorker().getForm());

        //save()修改过param,所以得取worker里的param
        model.addObject(WEB_PAGE_PARAM, getWorker().getParam());
        model.addObject(WEB_PAGE_FORM, getWorker().getForm());
        model.addObject(WEB_VALUE_CMD, getWorker().getParam().getCmd());
        model.addObject(MVCAction.WEB_ACTION_INFO, getWorker().getAi());
        return model;

//        ModelAndView model = new ModelAndView(getMapping(getContentPage()));
//        AbstractBO bo = getBO();
//		if (WEB_CMD_NEW.equals(param.getCmd())) {
//			try{
//				isSuccessful = WEB_SUCCESS_YES;
//				Object vo = null;
//				if (pkNameArray.length == 1) { // 单一主键
//					Object pk = PropertyUtils.getNestedProperty(form, pkNameArray[0]);
//					// 这里返回的是String类型
//					if (pk != null) {
//                        vo = bo.findByPk((Serializable) pk);
//					}
//				} else {// 复合主建
//                    vo = bo.findByPk(form);
//				}
//				if (vo == null) {
//                    BaseVO pojo = (BaseVO) clsVO.newInstance();
//                    copyFormToVO.copy(form, pojo, null);
//                    vo = bo.doCreate(pojo);
//
//                    copyVOToForm.copy(vo, form, null);
//
//                    setActionMessage(model, I18nMessage.getPublicString("msgSaveSuccess"));
//				} else {
//					onDuplicatePk(model);
//					isSuccessful = WEB_SUCCESS_NO;
//				}
//			}catch (Exception e) {
//				isSuccessful = WEB_SUCCESS_NO;
//				this.processException(model, e);
//			}
//            param.setCmd(WEB_CMD_SAVE);
//		} else {
//			try{
//                param.setCmd(WEB_CMD_SAVE);
//				isSuccessful = WEB_SUCCESS_YES;
//
//                BaseVO vo = (BaseVO) clsVO.newInstance();
//                copyFormToVO.copy(form, vo, null);
//                vo = bo.doUpdate(vo);
//
//                setActionMessage(model, I18nMessage.getPublicString("msgSaveSuccess"));
//			}catch (Exception e) {
//				isSuccessful = WEB_SUCCESS_NO;
//				this.processException(model, e);
//			}
//            param.setCmd(WEB_CMD_SAVE);
//		}
//
//        model.addObject(WEB_PAGE_PARAM, param);
//        model.addObject(WEB_PAGE_FORM, this.getForm());
//        model.addObject(WEB_VALUE_CMD, param.getCmd());
//		return model;
	}

//    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView delete(HttpServletRequest request, HttpServletResponse response) throws Exception{
        bind(request, null);
        //由于 DBQueryParam 存在 大量以_开头的属性，默认的spring绑定方法会忽略掉这些属性，所以得自己来
        getWorker().bindObject(request, getWorker().getParam());
        ModelAndView model = new ModelAndView("redirect:" + MVCAction.WEB_RESULT_LIST + ".do");

        getWorker().delete(getWorker().getParam());

        model.addObject(WEB_PAGE_PARAM, getWorker().getParam());
        model.addObject(MVCAction.WEB_ACTION_INFO, getWorker().getAi());
        return  model;

//        isSuccessful = WEB_SUCCESS_YES;
//        String[] selectArray = (getParam()).get_selectitem();
//
//        ModelAndView model = new ModelAndView("redirect:" + getListPage() + ".do");
//        if(selectArray == null) {
//            setActionMessage(model, I18nMessage.getPublicString("msgNoSelect"));
//        } else {
//            try{
//                AbstractBO bo = getBO();
//
//                for (int i = 0; i < selectArray.length; i++) {
//                    if (selectArray[0].indexOf('|') == -1) { // 单一主键
//                        bo.doRemoveByPK(getSelectedPK(selectArray[i]));
//                    } else { // 复合主键
//                        bo.doRemoveByVO((BaseVO) getSelectedPkVO(selectArray[i]));
//                    }
//                }
//            }catch (Exception e) {
//                isSuccessful = WEB_SUCCESS_NO;
//                this.processException(model, e);
//            }
//        }
//        model.addObject(WEB_PAGE_PARAM, param);
//        setActionMessage(model, I18nMessage.getPublicString("msgSaveSuccess"));
//        return  model;
    }

    /*
     * 模板方法, 允许子类设置主键重复时的错误消息 @param actionMapping @param actionForm @param
     */
//    protected void onDuplicatePk(ModelAndView model) {
//        setActionMessage(model, I18nMessage.getPublicString("msgDuplicatePk"));
//    }

//    protected void processException(ModelAndView model, Throwable e){
//        log.catching(e);
//		String msg = "";
//		if(e instanceof InvocationTargetException) {
//			InvocationTargetException ie = (InvocationTargetException) e;
//			Throwable t = ie.getTargetException();
//			if( null != t.getCause()){
//				if(null != t.getCause().getMessage())
//					msg = 	t.getCause().getMessage();
//			}else{
//				msg = t.getMessage()!=null ? t.getMessage() : t.toString();
//			}
//		}else if(e instanceof UndeclaredThrowableException) {
//			UndeclaredThrowableException ue = (UndeclaredThrowableException)e;
//			Throwable t = ue.getUndeclaredThrowable();
//			if( null != t.getCause()){
//				if(null != t.getCause().getMessage())
//					msg = 	t.getCause().getMessage();
//			}else{
//				msg = t.getMessage()!=null ? t.getMessage() : t.toString();
//			}
//
//		}else {
//			if( null != e.getCause()){
//				if(null != e.getCause().getMessage())
//					msg = 	e.getCause().getMessage();
//			}else{
//				msg = e.getMessage()!=null ? e.getMessage() : e.toString();
//			}
//		}
//
//        setActionMessage(model, msg);
//	}
}
