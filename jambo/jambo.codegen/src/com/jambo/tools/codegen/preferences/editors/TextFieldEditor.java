package com.jambo.tools.codegen.preferences.editors;

import java.util.Iterator;
import java.util.Map;

import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import com.jambo.tools.codegen.listener.TemplateModifyListener;
import com.jambo.tools.codegen.util.TemplatesUtils;

public class TextFieldEditor extends FieldEditor {
	Text text ;
	Combo comboField;
	Map map ;
	
	public TextFieldEditor(){
		super() ;
	}
	
	public TextFieldEditor(String name, String label, Map combo, Composite parent){
		map = combo ;
		init(name, label) ;
		createControl(parent);
		Iterator iterator = combo.keySet().iterator() ;
		while (iterator.hasNext()){
			comboField.add((String)iterator.next()) ;
		}
		addModifyListener(new TemplateModifyListener(text,combo)) ;
	}

	protected void adjustForNumColumns(int numColumns) {
		// TODO Auto-generated method stub
	}

	protected void doFillIntoGrid(Composite parent, int numColumns) {
		// TODO Auto-generated method stub
		getLabelControl(parent);
		comboField = getComboControl(parent);
		GC gc1 = new GC(comboField);
		gc1.dispose();
		text = getTextControl(parent) ;
		text.setLayoutData(new GridData(GridData.FILL_BOTH)) ;
		GC gc = new GC(text) ;
		gc.dispose() ;
	}

	protected void doLoad() {
		// TODO Auto-generated method stub
		if (comboField != null) {
			String value = getPreferenceStore().getString(getPreferenceName());
			comboField.setText(value);
		}
	}

	protected void doLoadDefault() {
		// TODO Auto-generated method stub
		String value = comboField.getText() ;
		if (null!=value&&value.trim().length()>0){
			text.setText(TemplatesUtils.getInstence().getFileContent((String)map.get(value))) ;
		}
	}

	protected void doStore() {
		// TODO Auto-generated method stub
		getPreferenceStore().setValue(getPreferenceName(), comboField.getText()) ;
		getPreferenceStore().setValue(comboField.getText(), text.getText()) ;
	}

	public int getNumberOfControls() {
		// TODO Auto-generated method stub
		return 1;
	}
	
	public Text getTextControl(Composite parent){
		if (null == text){
			TextField tce = new TextField(parent,SWT.MULTI|SWT.BORDER|SWT.WRAP|SWT.V_SCROLL) ;
			text = tce.createText() ;
			text.setFont(parent.getFont()) ;
		} else {
			checkParent(text,parent) ;
		}
		return text ;
	}

	/**
	 * Returns this field editor's text control.
	 * <p>
	 * The control is created if it does not yet exist
	 * </p>
	 * 
	 * @param parent
	 *            the parent
	 * @return the text control
	 */
	public Combo getComboControl(Composite parent) {
		if (comboField == null) {
			comboField = new Combo(parent, SWT.SINGLE | SWT.BORDER | SWT.READ_ONLY);
			comboField.setFont(parent.getFont());
		} else {
			checkParent(comboField, parent);
		}
		return comboField;
	}
	
	public void addModifyListener(ModifyListener listener) {
		comboField.addModifyListener(listener) ;
	}

}
