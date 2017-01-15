package com.jambo.tools.codegen.wizards;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.jambo.tools.codegen.util.Constants;
import com.jambo.tools.codegen.util.ValueStore;

public class SetNamesPage extends WizardPage {
    private static final Log log = LogFactory.getLog(SetNamesPage.class);
    private Text txtBaseName;
    private Button[] chks = new Button[10];

    protected SetNamesPage(String pageName) {
        super(pageName);
		log.debug("init last page");
        setTitle(Constants.WIZARDNAME);
        setDescription("请修改要生成的文件名.");
    }

    public void refresh() {
    	String baseName = ValueStore.baseClassName;    	
    	int index = baseName.lastIndexOf('_');
    	baseName = index > 0 ?  baseName.substring(index + 1) : baseName;
    	System.out.println("---- baseName " + baseName);
    	txtBaseName.setText(baseName);
    }

    public void createControl(Composite parent) {
        Composite pp = new Composite(parent, SWT.NULL);
        pp.setLayoutData(new GridData(GridData.FILL_BOTH));
        pp.setLayout(new GridLayout(1, true));
        createNamesView(pp);
        setControl(pp);
    }

    private void createNamesView(Composite parent) {
        
        Composite top = new Composite(parent, SWT.NONE);
        top.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        top.setLayout(new GridLayout(2, false));
       
        Label lblTitle = new Label(top, SWT.BOLD);
        lblTitle.setText("生成文件设置:");

        txtBaseName = new Text(top, SWT.SINGLE | SWT.BORDER);
        txtBaseName.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        txtBaseName.addModifyListener(new ModifyListener() {
                public void modifyText(ModifyEvent e) {
                    ValueStore.baseClassName = txtBaseName.getText();
                }
            });
        
        createChecks(parent);
        
    }

    private void createChecks(Composite parent) {
        Composite composite = new Composite(parent, SWT.NONE);
        composite.setLayoutData(new GridData(GridData.FILL_BOTH));
        composite.setLayout(new GridLayout(2, true));
        for(int i = 0; i < Constants.MAX_CHECK_NUMBER; i++){
        	chks[i] = new Button(composite, SWT.CHECK);
        	chks[i].setText(Constants.labelsWhich[i]);
        	chks[i].setSelection(true);
        	chks[i].setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        	if(i == Constants.HBM || i == Constants.VO)
        		chks[i].setEnabled(false);
        }
	}

	public IWizardPage getNextPage() {
        SetFoldersPage page = (SetFoldersPage) getWizard().getNextPage(this);
        for (int i = 0; i < Constants.MAX_CHECK_NUMBER; i++) {
            ValueStore.genOrNot[i] = chks[i].getSelection();
		}
        ValueStore.baseClassName = txtBaseName.getText().trim();
        page.refresh();

        return page;
    }
    public String getBaseClassName(){
    	return txtBaseName.getText().trim();
    }
    public boolean[] getNeedGenerate(){
    	boolean[] checks = new boolean[]{true,true,true,true,true,true,true,true,true,true};
    	for(int i = 0; i < Constants.MAX_CHECK_NUMBER; i++){
        	checks[i] = chks[i].getSelection();
        }
    	return checks;
    }

}
