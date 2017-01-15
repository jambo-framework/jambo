package com.jambo.tools.codegen.wizards.params;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

public class FieldSorter extends ViewerSorter {
	public static final int NAME = 0;
	private int criteria;
	
	public FieldSorter(int criteria){
		this.criteria = criteria;
	}
	public int compare(Viewer viewer, Object o1, Object o2) {

		Field field1 = (Field) o1;
		Field field2 = (Field) o2;

		switch (criteria) {
			case NAME :
				return field1.getName().compareTo(field2.getName());
			default:
				return 0;
		}
	}

}
