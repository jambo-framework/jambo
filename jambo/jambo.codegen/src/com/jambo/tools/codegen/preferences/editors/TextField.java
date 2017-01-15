package com.jambo.tools.codegen.preferences.editors;

import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class TextField extends TextCellEditor {
	public TextField(Composite parent, int style){
		super(parent, style);
		createControl(parent) ;
	}
	
	public Text createText(){
		return text ;
	}
}
