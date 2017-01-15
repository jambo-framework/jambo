package com.jambo.tools.codegen.wizards.params;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class FieldList {
//	private final int COUNT = 10;
	private ArrayList fields = new ArrayList();
	private Set changeListeners = new HashSet();
	
	public ArrayList getFields() {
		return fields;
	}
	public void setFields(ArrayList fields) {
		this.fields = fields;
	}
	public void addField(String name){
		addField(name, -1, String.class, -1);
	}
	public void addField(String name, int sqlType){
		addField(name, sqlType, String.class, -1);		
	}
	public void addField(String name, int sqlType, Class javaType, int sqlLength){
		Field field = new Field(name, sqlType, javaType, sqlLength);
		fields.add(field);
		Iterator iterator = changeListeners.iterator();
		while (iterator.hasNext())
			((IFieldListViewer) iterator.next()).addField(field);
		
	}
	public void removeField(Field field) {
		fields.remove(field);
		Iterator iterator = changeListeners.iterator();
		while (iterator.hasNext())
			((IFieldListViewer) iterator.next()).removeField(field);
	}

	/**
	 * @param task
	 */
	public void fieldChanged(Field field) {
		Iterator iterator = changeListeners.iterator();
		while (iterator.hasNext())
			((IFieldListViewer) iterator.next()).updateField(field);
	}

	/**
	 * @param viewer
	 */
	public void removeChangeListener(IFieldListViewer viewer) {
		changeListeners.remove(viewer);
	}

	/**
	 * @param viewer
	 */
	public void addChangeListener(IFieldListViewer viewer) {
		changeListeners.add(viewer);
	}
	public void clear(){
		fields.clear();
	}
}
