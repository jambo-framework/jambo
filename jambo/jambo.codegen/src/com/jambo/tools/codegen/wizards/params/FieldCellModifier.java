package com.jambo.tools.codegen.wizards.params;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.swt.widgets.TableItem;

import com.jambo.tools.codegen.wizards.ParamsObjPage;



public class FieldCellModifier implements ICellModifier {
	private ParamsObjPage page;

	/**
	 * Constructor
	 * 
	 * @param FieldCellModifier
	 *            an instance of a FieldCellModifier
	 */
	public FieldCellModifier(ParamsObjPage page) {
		super();
		this.page = page;
	}

	public boolean canModify(Object element, String property) {
		// Find the index of the column
		int columnIndex = page.getColumnNames().indexOf(property);

		boolean result = true;
		switch (columnIndex) {
		case 0:
			result = false;
			break;
		default:
		}
		return result;
	}

	public Object getValue(Object element, String property) {
		// Find the index of the column
		int columnIndex = page.getColumnNames().indexOf(property);

		Object result = null;
		Field field = (Field) element;

		switch (columnIndex) {
		case 0:
			result = field.getName();
			break;
//		case 1:
//			result = new Boolean(field.isN());
//			break;
//		case 2:
//			result = new Boolean(field.isNn());
//			break;
//		case 3:
//			result = new Boolean(field.isL());
//			break;
//		case 4:
//			result = new Boolean(field.isNm());
//			break;
//		case 5:
//			result = new Boolean(field.isE());
//			break;
//		case 6:
//			result = new Boolean(field.isNl());
//			break;
//		case 7:
//			result = new Boolean(field.isM());
//			break;
//		case 8:
//			result = new Boolean(field.isNe());
//			break;
//		case 9:
//			result = new Boolean(field.isIn());
//			break;
//		case 10:
//			result = new Boolean(field.isNin());
//			break;
//		case 11:
//			result = new Boolean(field.isK());
//			break;
//		case 12:
//			result = new Boolean(field.isNk());
//			break;
		default:
			result = new Boolean(field.isSelected(columnIndex - 1));
		}
		return result;
	}

	public void modify(Object element, String property, Object value) {
		int columnIndex = page.getColumnNames().indexOf(property);
		TableItem item = (TableItem) element;
		Field field = (Field) item.getData();

		switch (columnIndex) {
		case 0:
			field.setName((String) value);
			break;
//		case 1:
//			field.setN(((Boolean) value).booleanValue());
//			break;
//		case 2:
//			field.setNn(((Boolean) value).booleanValue());
//			break;
//		case 3:
//			field.setL(((Boolean) value).booleanValue());
//			break;
//		case 4:
//			field.setNm(((Boolean) value).booleanValue());
//			break;
//		case 5:
//			field.setE(((Boolean) value).booleanValue());
//			break;
//		case 6:
//			field.setNl(((Boolean) value).booleanValue());
//			break;
//		case 7:
//			field.setM(((Boolean) value).booleanValue());
//			break;
//		case 8:
//			field.setNe(((Boolean) value).booleanValue());
//			break;
//		case 9:
//			field.setIn(((Boolean) value).booleanValue());
//			break;
//		case 10:
//			field.setNin(((Boolean) value).booleanValue());
//			break;
//		case 11:
//			field.setK(((Boolean) value).booleanValue());
//			break;
//		case 12:
//			field.setNk(((Boolean) value).booleanValue());
//			break;
		default:
			field.setSelected(columnIndex - 1, ((Boolean) value).booleanValue());
		}
		page.getFieldList().fieldChanged(field);
	}

}
