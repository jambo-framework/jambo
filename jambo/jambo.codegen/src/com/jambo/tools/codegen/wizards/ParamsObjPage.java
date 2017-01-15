package com.jambo.tools.codegen.wizards;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.jambo.tools.codegen.CodegenPlugin;
import com.jambo.tools.codegen.ddl2hbm.JDBCUtil;
import com.jambo.tools.codegen.preferences.PreferenceConstants;
import com.jambo.tools.codegen.util.Constants;
import com.jambo.tools.codegen.util.ValueStore;
import com.jambo.tools.codegen.wizards.params.Field;
import com.jambo.tools.codegen.wizards.params.FieldCellModifier;
import com.jambo.tools.codegen.wizards.params.FieldList;
import com.jambo.tools.codegen.wizards.params.FieldSorter;
import com.jambo.tools.codegen.wizards.params.FieldsLabelProvider;
import com.jambo.tools.codegen.wizards.params.IFieldListViewer;



public class ParamsObjPage extends WizardPage {
	private static final Log log = LogFactory.getLog(ParamsObjPage.class);

	private Table table;

	private TableViewer tableViewer;

	// Create a FieldList and assign it to an instance variable
	private FieldList fieldList;

	public ParamsObjPage(String pageName) {
		super(pageName);
		log.debug("init params page");
		setTitle(Constants.WIZARDNAME);
		setDescription("请选择要生成代码的字段名,并选择要执行的查询条件.");
	}

	public void createControl(Composite parent) {
		Composite pp = new Composite(parent, SWT.NULL);
		pp.setLayoutData(new GridData(GridData.FILL_BOTH));
		pp.setLayout(new FillLayout());
		// Create the table
		createTable(pp);

		// Create and setup the TableViewer
		createTableViewer();
		tableViewer.setContentProvider(new FieldContentProvider());
		tableViewer.setLabelProvider(new FieldsLabelProvider());
		// The input for the table viewer is the instance of ExampleTaskList
		fieldList = new FieldList();
		tableViewer.setInput(fieldList);
		// setPageComplete(false);
		setControl(pp);
	}

	/**
	 * Create the Table
	 */
	private void createTable(Composite parent) {
		int style = SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL
				| SWT.FULL_SELECTION | SWT.HIDE_SELECTION;

		// final int NUMBER_COLUMNS = 2;

		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		gridLayout.marginHeight = gridLayout.marginWidth = 2;
		gridLayout.horizontalSpacing = gridLayout.verticalSpacing = 0;
		composite.setLayout(gridLayout);
		table = new Table(composite, style);

		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalSpan = 3;
		table.setLayoutData(gridData);

		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		TableColumn column = new TableColumn(table, SWT.LEFT, 0);
		column.setText(Constants.columnHeaders[0]);
		column.setWidth(100);
		// Add listener to column so tasks are sorted by description when
		// clicked
		column.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				tableViewer.setSorter(new FieldSorter(FieldSorter.NAME));
			}
		});
		for (int i = 1; i < 13; i++) {
			column = new TableColumn(table, SWT.CENTER, i);
			column.setText(Constants.columnHeaders[i]);
			column.setWidth(30);
		}

	}

	/**
	 * Create the TableViewer
	 */
	private void createTableViewer() {

		tableViewer = new TableViewer(table);
		tableViewer.setUseHashlookup(true);

		tableViewer.setColumnProperties(Constants.columnNames);

		// Create the cell editors
		CellEditor[] editors = new CellEditor[Constants.columnNames.length];

		// Column 2 :
		for (int i = 1; i < 13; i++) {
			editors[i] = new CheckboxCellEditor(table);
		}

		// // Column 1 :
		// TextCellEditor textEditor = new TextCellEditor(table);
		// ((Text) textEditor.getControl()).setTextLimit(60);
		// editors[0] = textEditor;

		// Assign the cell editors to the viewer
		tableViewer.setCellEditors(editors);
		// Set the cell modifier for the viewer
		tableViewer.setCellModifier(new FieldCellModifier(this));
		// Set the default sorter for the viewer
		tableViewer.setSorter(new FieldSorter(FieldSorter.NAME));
	}

	public void refresh() {
		if (ValueStore.tableName != null)
			fillTable();
	}

	private void fillTable() {
		try {
			IPreferenceStore store = CodegenPlugin.getDefault()
					.getPreferenceStore();
			String catalog = store.getString(PreferenceConstants.CATALOG);
			String schemaPattern = store.getString(PreferenceConstants.SCHEMA);
			List columnNames = JDBCUtil.getTableColumns(ValueStore.connection,
					catalog, schemaPattern, ValueStore.tableName, null);
			// log.debug("Columns:\t" + columnNames);
			fieldList.clear();
			tableViewer.refresh(true);
			for (Iterator iter = columnNames.iterator(); iter.hasNext();) {
				JDBCUtil.Column column = (JDBCUtil.Column) iter.next();
				fieldList.addField(column.name, column.sqlType, column.javaType, column.sqlColumnLength);
			}
			ValueStore.fields = fieldList.getFields();
			List pkColumns = JDBCUtil.getPrimaryKeyColumns(ValueStore.connection,
					catalog, schemaPattern, ValueStore.tableName);
			StringBuffer buffer = new StringBuffer();
			for (Iterator it = pkColumns.iterator(); it.hasNext();) {
				JDBCUtil.Column column = (JDBCUtil.Column) it.next();
				buffer.append("\"").append(column.name).append("\",");
			}
			if(buffer.length() > 0){
				buffer.deleteCharAt(buffer.length() - 1);
				ValueStore.pkFields = buffer.toString();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
//	public IWizardPage getNextPage() {
//		LastPage page = (LastPage)super.getNextPage() ;
//		page.setCreateFlag(true) ;
//		return page ;
//	}

	/**
	 * Return the column names in a collection
	 * 
	 * @return List containing column names
	 */
	public java.util.List getColumnNames() {
		return Arrays.asList(Constants.columnNames);
	}

	public FieldList getFieldList() {
		return fieldList;
	}

	class FieldContentProvider implements IStructuredContentProvider,
			IFieldListViewer {
		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
			if (newInput != null)
				((FieldList) newInput).addChangeListener(this);
			if (oldInput != null)
				((FieldList) oldInput).removeChangeListener(this);
		}

		public void dispose() {
			fieldList.removeChangeListener(this);
		}

		// Return the tasks as an array of Objects
		public Object[] getElements(Object parent) {
			return fieldList.getFields().toArray();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see ITaskListViewer#addTask(ExampleTask)
		 */
		public void addField(Field field) {
			tableViewer.add(field);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see ITaskListViewer#removeTask(ExampleTask)
		 */
		public void removeField(Field field) {
			tableViewer.remove(field);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see ITaskListViewer#updateTask(ExampleTask)
		 */
		public void updateField(Field field) {
			tableViewer.update(field, null);
		}
	}

}
