package com.jambo.tools.codegen.wizards;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import com.jambo.tools.codegen.util.Constants;

public class LastPage extends WizardPage {
	private static final Log log = LogFactory.getLog(LastPage.class);
	private boolean createFlag = false ; //是否允许生成代码
	public LastPage(String pageName){
		super(pageName);
		log.debug("init last page");
		setTitle(Constants.WIZARDNAME);
		setDescription("向导已经收集了代码生成必须的信息,如果要代码生成后继续对新的表生成代码,请点击next,如果全部完成,请点Finish按钮.并刷新项目");	}
	public void createControl(Composite parent) {
		Composite pp = new Composite(parent, SWT.NULL);
		pp.setLayoutData(new GridData(GridData.FILL_BOTH));
        pp.setLayout(new FillLayout());
        createReviewArea(pp);
		setControl(pp);
	}
	public IWizardPage getNextPage() {
		SelTablePage page = (SelTablePage) getWizard().getStartingPage() ;
		if (createFlag){
			CodegenNewWizard wizard = (CodegenNewWizard)getWizard() ;
			wizard.setCloseConnFlag(false) ;
			wizard.performFinish() ;
			log.debug("代码生成中") ;
			createFlag = false ;
			page.setPreviousAble(createFlag) ;
			wizard.setCloseConnFlag(true) ;
			return page ;
		}
		log.debug("等待生成中");
		createFlag = true ;

        return page;
    }
	public void setCreateFlag(boolean flag){
		this.createFlag = flag ;
	}
	public void createReviewArea(Composite composite){
		
	}
}
