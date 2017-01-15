package com.jambo.tools.codegen.wizards;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.jambo.tools.codegen.CodegenPlugin;
import com.jambo.tools.codegen.ddl2hbm.JDBCUtil;
import com.jambo.tools.codegen.preferences.PreferenceConstants;
import com.jambo.tools.codegen.util.Constants;
import com.jambo.tools.codegen.util.StringHelper;
import com.jambo.tools.codegen.util.ValueStore;



/**
 * <h3>向导页</h3>
 * <p>
 * 代码生成使用库表设置
 * </p>
 * 
 * @author Jerry Shang
 * @version 1.0
 * 
 * 
 */
public class SelTablePage extends WizardPage {
	private static final Log log = LogFactory.getLog(SelTablePage.class);

	private Table table;
	private boolean isPrevious = true ; //是否能点击上一页

	public SelTablePage(String pageName) {
		super(pageName);
		setTitle(Constants.WIZARDNAME);
		setDescription("请选择要生成代码的表名,并选择要自动生成的文件.");
	}

	public void createControl(Composite parent) {
		Composite pp = new Composite(parent, SWT.NULL);
		pp.setLayoutData(new GridData(GridData.FILL_BOTH));
		pp.setLayout(new FillLayout());
		createTableView(pp);
		setPageComplete(false);
		setControl(pp);
	}

	private void createTableView(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		gridLayout.marginHeight = gridLayout.marginWidth = 2;
		gridLayout.horizontalSpacing = gridLayout.verticalSpacing = 0;
		composite.setLayout(gridLayout);
		table = new Table(composite, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL
				| SWT.FULL_SELECTION);

		GridData gridData = new GridData(GridData.FILL_BOTH);
		table.setLayoutData(gridData);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		gridData.heightHint = 250;
		gridData.widthHint = 200;
		table.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				if (table.getSelection() != null){
					ValueStore.tableName = (String) ((TableItem[]) (table
							.getSelection()))[0].getData();
					ValueStore.baseClassName = StringHelper.camelName(
							ValueStore.tableName, ValueStore.numPrefix);
					((CodegenNewWizard) getWizard()).paramsPage.refresh();
					((CodegenNewWizard) getWizard()).foldersPage.refresh();
					setPageComplete(true);
				}
			}

			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		TableColumn tc1 = new TableColumn(table, SWT.LEFT);
		tc1.setText("表 名");
		tc1.setWidth(gridData.widthHint);

		fillTable();
	}

	private void fillTable() {
		try {
			IPreferenceStore store = CodegenPlugin.getDefault()
					.getPreferenceStore();
			String catalog = store.getString(PreferenceConstants.CATALOG);
			if ("".equals(catalog))
				catalog = null;
			String schemaPattern = store.getString(PreferenceConstants.SCHEMA);
			if ("".equals(schemaPattern))
				schemaPattern = null;
			boolean showTableOnly = store
					.getBoolean(PreferenceConstants.TABLEONLY);
			List tableNames = JDBCUtil.getTables(ValueStore.connection,
					catalog, schemaPattern, null,
					showTableOnly ? JDBCUtil.TABLE : JDBCUtil.ALL);
			log.debug("Tables:\t" + tableNames);

			for (Iterator iter = tableNames.iterator(); iter.hasNext();) {
				String tableName = (String) iter.next();
				TableItem ti = new TableItem(table, 0);
				ti.setText(0, tableName);
				ti.setData(tableName);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void setPreviousAble(boolean flag) {
		this.isPrevious = flag ;
	}
	
	public IWizardPage getPreviousPage(){
		if (isPrevious){
			return super.getPreviousPage() ;
		} else {
			return null ;
		}
	}

	public IWizardPage getNextPage() {
		SetNamesPage page = (SetNamesPage) getWizard().getNextPage(this);
		page.refresh();

		return page;
	}

	public String getTableName() {
		return (String) ((TableItem[]) (table.getSelection()))[0].getData();
	}
}
