package com.jambo.tools.codegen.wizards;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.jambo.tools.codegen.CodegenPlugin;
import com.jambo.tools.codegen.preferences.PreferenceConstants;
import com.jambo.tools.codegen.util.Constants;
import com.jambo.tools.codegen.util.ValueStore;




/**
 * <h3>向导页</h3>
 * <p>文件存放路径及包命名设置</p>
 *
 * @author Jerry Shang
 * @version 1.0
 */
public class SetFoldersPage extends WizardPage {
    private static final Log log = LogFactory.getLog(SetFoldersPage.class);
    private Text txtModuleName;
    private Text txtSrcFolder;
    private Button btnSrcFolder;
    private Text txtTestFolder;
    private Button btnTestFolder;
    private Text txtWebFolder;
    private Button btnWebFolder;
	private Label lblHbmPackage;
	private Label lblModelPackage;
	private Label lblControlPackage;
	private Label lblViewPackage;
	private Label lblI18nPackage;

    /**
     * @param pageName
     */
    public SetFoldersPage(String pageName) {
        super(pageName);
		log.debug("init last page");
        setTitle(Constants.WIZARDNAME);
        setDescription("请选择源文件目录以及各文件所在包目录.");
        this.setPageComplete(false);
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
     */
    public void createControl(Composite parent) {
        Composite pp = new Composite(parent, SWT.NULL);
        pp.setLayoutData(new GridData(GridData.FILL_BOTH));
        pp.setLayout(new FillLayout());
        createFoldersView(pp);
        initTextValues();
        setControl(pp);
    }

    private void initTextValues() {
        IPreferenceStore store = CodegenPlugin.getDefault().getPreferenceStore();
        txtSrcFolder.setText(store.getString(
                PreferenceConstants.SRCFOLDER));
        txtTestFolder.setText(store.getString(
                PreferenceConstants.TESTFOLDER));
        txtWebFolder.setText(store.getString(
                PreferenceConstants.WEBFOLDER));
    }

    private void createFoldersView(final Composite parent) {
        Composite composite = new Composite(parent, SWT.NONE);
        composite.setLayout(new GridLayout(2, false));

        ModifyListener ml = new ModifyListener() {
                public void modifyText(ModifyEvent e) {
                    setPageComplete(isComplete());
                }
            };
        
        Label lblSrcFolder = new Label(composite, SWT.NONE);
        lblSrcFolder.setText("源文件目录");

        Composite txtAndBtn = new Composite(composite, SWT.NONE);
        txtAndBtn.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        txtAndBtn.setLayout(new GridLayout(2, false));
        txtSrcFolder = new Text(txtAndBtn, SWT.BORDER | SWT.SINGLE);
        txtSrcFolder.addModifyListener(ml);
        txtSrcFolder.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        btnSrcFolder = new Button(txtAndBtn, SWT.FLAT);
        btnSrcFolder.setText("...");
        btnSrcFolder.addSelectionListener(new SelectionListener() {
                public void widgetSelected(SelectionEvent e) {
                    DirectoryDialog dd = new DirectoryDialog(parent.getShell());
                    dd.setText("源文件目录");

                    String path = dd.open();

                    if (path != null) {
                        txtSrcFolder.setText(path);
                    }
                }

                public void widgetDefaultSelected(SelectionEvent e) {
                }
            });

        Label lblTestFolder = new Label(composite, SWT.NONE);
        lblTestFolder.setText("测试文件目录");
        Composite txtAndBtn2 = new Composite(composite, SWT.NONE);
        txtAndBtn2.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        txtAndBtn2.setLayout(new GridLayout(2, false));
        txtTestFolder = new Text(txtAndBtn2, SWT.BORDER | SWT.SINGLE);
        txtTestFolder.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        btnTestFolder = new Button(txtAndBtn2, SWT.FLAT);
        btnTestFolder.setText("...");
        btnTestFolder.addSelectionListener(new SelectionListener() {
                public void widgetSelected(SelectionEvent e) {
                    DirectoryDialog dd = new DirectoryDialog(parent.getShell());
                    dd.setText("测试文件目录");
                    String path = dd.open();
                    if (path != null) {
                        txtTestFolder.setText(path);
                    }
                }
                public void widgetDefaultSelected(SelectionEvent e) {
                }
            });

        txtTestFolder.addModifyListener(ml);

        Label lblWebFolder = new Label(composite, SWT.NONE);
        lblWebFolder.setText("Web模块目录");
        Composite txtAndBtn3 = new Composite(composite, SWT.NONE);
        txtAndBtn3.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        txtAndBtn3.setLayout(new GridLayout(2, false));
        txtWebFolder = new Text(txtAndBtn3, SWT.BORDER | SWT.SINGLE);
        txtWebFolder.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        btnWebFolder = new Button(txtAndBtn3, SWT.FLAT);
        btnWebFolder.setText("...");
        btnWebFolder.addSelectionListener(new SelectionListener() {
                public void widgetSelected(SelectionEvent e) {
                    DirectoryDialog dd = new DirectoryDialog(parent.getShell());
                    dd.setText("Web模块目录");
                    String path = dd.open();
                    if (path != null) {
                        txtWebFolder.setText(path);
                    }
                }
                public void widgetDefaultSelected(SelectionEvent e) {
                }
            });
        txtWebFolder.addModifyListener(ml);

        Label lblModuleName = new Label(composite, SWT.NONE);
        lblModuleName.setText("模块名称");
        txtModuleName = new Text(composite, SWT.BORDER | SWT.SINGLE);
        txtModuleName.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        txtModuleName.addModifyListener(new ModifyListener() {
                public void modifyText(ModifyEvent e) {
                    ValueStore.moduleName = txtModuleName.getText();
                      
                    lblHbmPackage.setText("com." + ValueStore.projname + ".business." +
                        txtModuleName.getText() + "." +
                        ValueStore.baseClassName.toLowerCase()
                        + "." 
                        + "persistent");
                    lblControlPackage.setText("com." + ValueStore.projname + ".business." +
                            txtModuleName.getText() + "." +
                            ValueStore.baseClassName.toLowerCase()
                            + "." 
                            + "control");
                    
                    lblModelPackage.setText("com." + ValueStore.projname + ".business." +
                        txtModuleName.getText() + "." +
                        ValueStore.baseClassName.toLowerCase()
                        + "." 
                        + "persistent");
                    
                    lblI18nPackage.setText("com." + ValueStore.projname + ".web." +
                    		txtModuleName.getText() + "." +
                    		ValueStore.baseClassName.toLowerCase());
                    lblViewPackage.setText("com." + ValueStore.projname + ".web." +
                            txtModuleName.getText() + "." +
                            ValueStore.baseClassName.toLowerCase());
                    setPageComplete(isComplete());
                }
            });

        Label lblHbmFolder = new Label(composite, SWT.NONE);
        lblHbmFolder.setText("hbm包名");
        lblHbmPackage = new Label(composite, SWT.NONE);
		lblHbmPackage.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        Label lblModelFolder = new Label(composite, SWT.NONE);
        lblModelFolder.setText("Model包名");
        lblModelPackage = new Label(composite, SWT.NONE);
		lblModelPackage.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        Label lblControlFolder = new Label(composite, SWT.NONE);
        lblControlFolder.setText("Control包名");
        lblControlPackage = new Label(composite, SWT.NONE);
		lblControlPackage.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        Label lblViewFolder = new Label(composite, SWT.NONE);
        lblViewFolder.setText("View包名");
        lblViewPackage = new Label(composite, SWT.NONE);
		lblViewPackage.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        Label lblI18nFolder = new Label(composite, SWT.NONE);
        lblI18nFolder.setText("资源文件包名");
        lblI18nPackage = new Label(composite, SWT.NONE);
		lblI18nPackage.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

    }

    private boolean isComplete() {
        if (txtModuleName.getText().trim().equals("")) {
            return false;
        }

        if (txtSrcFolder.getText().trim().equals("")) {
        	return false;
        }

        if (txtTestFolder.getEnabled() &&
                txtTestFolder.getText().trim().equals("")) {
            return false;
        }
        
        if (txtWebFolder.getEnabled() &&
        		txtWebFolder.getText().trim().equals("")) {
        	return false;
        }
        return true;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.wizard.IWizardPage#getNextPage()
     */
    public IWizardPage getNextPage() {
        if (ValueStore.genOrNot[Constants.HBM]) {
            return ((CodegenNewWizard) getWizard()).hbmPage;
        } else if (ValueStore.genOrNot[Constants.QUERYPARAM]) {
            return ((CodegenNewWizard) getWizard()).paramsPage;
        }
        return ((CodegenNewWizard) getWizard()).lastPage;
    }
    /**
     * @return 测试代码存放路径
     */
    public String getSrcFolder() {
        return txtSrcFolder.getText().trim();
    }

    /**
     * @return 测试源代码存放路径
     */
    public String getTestFolder() {
        return txtTestFolder.getText().trim();
    }
    
    public String getWebFolder(){
    	return txtWebFolder.getText().trim();
    }
    public String getModuleName(){
    	return txtModuleName.getText().trim().toLowerCase();
    }
    /**
     * 刷新文本框内容,设置控件是否可用
     */
    public void refresh() {
        if (ValueStore.moduleName == null) {
            txtModuleName.setText(" ");
            txtModuleName.setText("");
        } else if (txtModuleName.getText().trim().equals("")) {
            txtModuleName.setText(ValueStore.moduleName);
        }

        lblHbmPackage.setEnabled(ValueStore.genOrNot[Constants.HBM]);
        //&&ValueStore.genOrNot[Constants.DELEGATE]
        lblControlPackage.setEnabled(ValueStore.genOrNot[Constants.EJB]);
        lblModelPackage.setEnabled(ValueStore.genOrNot[Constants.VO] &&
            ValueStore.genOrNot[Constants.QUERYPARAM] &&
            ValueStore.genOrNot[Constants.DAO]);
        txtTestFolder.setEnabled(ValueStore.genOrNot[Constants.TEST]);
        btnTestFolder.setEnabled(ValueStore.genOrNot[Constants.TEST]);
    }
}
