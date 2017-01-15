package com.jambo.tools.codegen.wizards.params;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.jambo.tools.codegen.wizards.ParamsObjPage;



public class FieldsLabelProvider extends LabelProvider implements
		ITableLabelProvider {
	// Names of images used to represent checkboxes
	public static final String CHECKED_IMAGE = "checked";

	public static final String UNCHECKED_IMAGE = "unchecked";

	// For the checkbox images
	private static ImageRegistry imageRegistry = new ImageRegistry();

	/**
	 * Note: An image registry owns all of the image objects registered with it,
	 * and automatically disposes of them the SWT Display is disposed.
	 */
	static {
		String iconPath = "icons/";
		imageRegistry.put(CHECKED_IMAGE, ImageDescriptor.createFromFile(
				ParamsObjPage.class, iconPath + CHECKED_IMAGE + ".gif"));
		imageRegistry.put(UNCHECKED_IMAGE, ImageDescriptor.createFromFile(
				ParamsObjPage.class, iconPath + UNCHECKED_IMAGE + ".gif"));
	}

	public Image getColumnImage(Object element, int columnIndex) {
		if(columnIndex ==0) return null;
		Field field = (Field) element;
		boolean isSelected = field.isSelected(columnIndex - 1);
//		switch(columnIndex){
//		case 1:
//			isSelected = field.isN();
//			break;
//		case 2:
//			isSelected = field.isNn();
//			break;
//		case 3:
//			isSelected = field.isL();
//			break;
//		case 4:
//			isSelected = field.isNm();
//			break;
//		case 5:
//			isSelected = field.isE();
//			break;
//		case 6:
//			isSelected = field.isNl();
//			break;
//		case 7:
//			isSelected = field.isM();
//			break;
//		case 8:
//			isSelected = field.isNe();
//			break;
//		case 9:
//			isSelected = field.isIn();
//			break;
//		case 10:
//			isSelected = field.isNin();
//			break;
//		case 11:
//			isSelected = field.isK();
//			break;
//		case 12:
//			isSelected = field.isNk();
//			break;
//		default:
//			isSelected = false;
//		}
		String key = isSelected ? CHECKED_IMAGE : UNCHECKED_IMAGE;
		return  imageRegistry.get(key);
	}

	public String getColumnText(Object element, int columnIndex) {
		String result = "";
		Field field = (Field) element;
		switch (columnIndex) {
		case 0:
			result = field.getName();
			break;
		default:
			break;
		}
		return result;
	}

}
