/**
 * 
 */
package com.jambo.jop.web.common;

import com.jambo.jop.common.utils.lang.Code2NameUtils;
import com.jambo.jop.infrastructure.db.DataPackage;
import com.jambo.jop.ui.common.UserProvider;
import com.jambo.jop.ui.spring.BaseAction;
import com.jambo.jop.ui.worker.BaseMVCWorker;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author He Kun, jinbo
 *
 */
@Controller
@RequestMapping(value = "/pickerAction")
public class PickerAction extends BaseAction {
    private static Logger log = LogManager.getLogger(PickerAction.class);
    private static UserProvider provider;

//	private String definition,condition, dbFlag;
//	private String code, name; //查询用的编码和名称
	/**
	 * 
	 */
	public PickerAction() {
        setWorker(new BaseMVCWorker(this, null, null));

        setParam(new PickerParam());
	}

    public String getDbFlag() {
        return super.getWorker().getInnerUser().getDbFlag();
    }

    public ModelAndView list(HttpServletRequest request, HttpServletResponse response) throws Exception{
        bind(request, null);

        ModelAndView model = new ModelAndView("inc/picker");
        DataPackage dp = null;
		try {
            PickerParam p = (PickerParam)getWorker().getParam();

			//使用Cache拦截器管理缓存，拦截器配置在 WebViewInterceptorHandler 中，参见 /jop-aop.xml 
			String queryDBFlag = getDbFlag();
			if(StringUtils.isNotBlank(p.getDbFlag())) {
				queryDBFlag = p.getDbFlag();
			}

            String definition = p.getDefinition();
			if(definition.startsWith("$")){
				;	//直接将字典表用picker形式展现
			}else if(definition.startsWith("#")){
				;
			}else{
                definition = "#" + definition;
			}

            String condition = "";

			if(StringUtils.isNotBlank(p.getCode()))	//添加用户输入的code，name作为新的查询条件。
				condition = p.getCondition() + ";" + "CODE:" + p.getCode();
			
			if(StringUtils.isNotBlank(p.getName()))
				condition = condition + ";" + "NAME:" + p.getName();
			
			if(condition.startsWith(";"))
				condition = condition.substring(1);
			
            dp = Code2NameUtils.valueListPackage(definition, condition, getWorker().getParam(), queryDBFlag);
//			setDp(dp);
				
		} catch (Exception e) {			 
			log.catching(e);
		}

        model.addObject(WEB_PAGE_PARAM, getWorker().getParam());
        model.addObject(WEB_RESULT_LIST_MODEL_NAME, dp);
        return model;
	}

    @RequestMapping("/listx")
    @ResponseBody
    public DataPackage listx(HttpServletRequest request, HttpServletResponse response) throws Exception {
        DataPackage dp = null;
        try {
            bind(request, null);

            PickerParam req = (PickerParam)getWorker().getParam();

            //使用Cache拦截器管理缓存，拦截器配置在 WebViewInterceptorHandler 中，参见 /jop-aop.xml
            String queryDBFlag = getDbFlag();
            if(StringUtils.isNotBlank(req.getDbFlag())) {
                queryDBFlag = req.getDbFlag();
            }

            String definition = req.getDefinition();
            if(definition.startsWith("$")){
                ;	//直接将字典表用picker形式展现
            }else if(definition.startsWith("#")){
                ;
            }else{
                definition = "#" + definition;
            }

            String condition = null;
            if(req.getCondition() == null)
                condition = "";

            if(StringUtils.isNotBlank(req.getCode()))	//添加用户输入的code，name作为新的查询条件。
                condition = req.getCondition() + ";" + "CODE:" + req.getCode();

            if(StringUtils.isNotBlank(req.getName()))
                condition = condition + ";" + "NAME:" + req.getName();

            if(condition != null && condition.startsWith(";"))
                condition = condition.substring(1);

            dp = Code2NameUtils.valueListPackage(definition, condition, getWorker().getParam(), queryDBFlag);

        } catch (Exception e) {
            log.catching(e);
        }

        return dp ;
    }

}
