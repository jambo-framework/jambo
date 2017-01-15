package com.jambo.tools.codegen.preferences.editors;

import org.eclipse.jface.preference.FieldEditor;
//import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;

public class ComboFieldEditor extends FieldEditor {
	private int widthInChars = -1;

//	@SuppressWarnings("unused")
//	private String errorMessage;
	/**
	 * Old text value.
	 */
//	@SuppressWarnings("unused")
//	private String oldValue;
	
	/**
	 * The text field, or <code>null</code> if none.
	 */
	Combo comboField;

	public ComboFieldEditor() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ComboFieldEditor(String name, String labelText,
			int width, String[] values, Composite parent){
		init(name, labelText);
		widthInChars = width;
//		errorMessage = JFaceResources
//				.getString("StringFieldEditor.errorMessage");//$NON-NLS-1$
		createControl(parent);
		for (int i = 0; i < values.length; i++) {
			comboField.add(values[i]);
		}
	}
	protected void adjustForNumColumns(int numColumns) {
		GridData gd = (GridData) comboField.getLayoutData();
		gd.horizontalSpan = numColumns - 1;
		// We only grab excess space if we have to
		// If another field editor has more columns then
		// we assume it is setting the width.
		gd.grabExcessHorizontalSpace = gd.horizontalSpan == 1;
	}

	protected void doFillIntoGrid(Composite parent, int numColumns) {
		getLabelControl(parent);

		comboField = getComboControl(parent);
		GridData gd = new GridData();
		gd.horizontalSpan = numColumns - 1;
		if (widthInChars != -1) {
			GC gc = new GC(comboField);
			try {
				Point extent = gc.textExtent("X");//$NON-NLS-1$
				gd.widthHint = widthInChars * extent.x;
			} finally {
				gc.dispose();
			}
		} else {
			gd.horizontalAlignment = GridData.FILL;
			gd.grabExcessHorizontalSpace = true;
		}
		comboField.setLayoutData(gd);
	}

	protected void doLoad() {
		if (comboField != null) {
			String value = getPreferenceStore().getString(getPreferenceName());
			comboField.setText(value);
//			oldValue = value;
		}
	}

	protected void doLoadDefault() {
		if (comboField != null) {
			String value = getPreferenceStore().getDefaultString(
					getPreferenceName());
			comboField.setText(value);
		}
	}

	protected void doStore() {
		getPreferenceStore().setValue(getPreferenceName(), comboField.getText());
	}

	public int getNumberOfControls() {
		return 2;
	}
	protected Combo getComboControl() {
		return comboField;
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
			comboField = new Combo(parent, SWT.SINGLE | SWT.BORDER );
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
