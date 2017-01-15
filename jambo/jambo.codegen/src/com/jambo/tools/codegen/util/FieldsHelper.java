package com.jambo.tools.codegen.util;

import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.jambo.tools.codegen.wizards.params.Field;



public class FieldsHelper {
	private String[] params;

	private String[] columns;

	private String[] jsType;

	private String[] length;
	
	public HashMap paramColumn;
	public HashMap paramJsType;
	public HashMap paramLength;

	public String[] getColumns() {
		return columns;
	}

	public String[] getParams() {
		return params;
	}

	public String[] getJSType(){
		return jsType;
	}
	
	public String[] getLength(){
		return length;
	}
	
	public FieldsHelper(List fields) {
		//** 奇怪，为什么不直接用这些List作为返回的内容，而要再建立数组。JinBo
		ArrayList paramList = new ArrayList();
		ArrayList columnList = new ArrayList();
		ArrayList lenList  = new ArrayList();
		ArrayList typeList  = new ArrayList();
		
		paramColumn = new HashMap();
		paramJsType = new HashMap();
		paramLength = new HashMap();
		for (Iterator iter = fields.iterator(); iter.hasNext();) {
			Field element = (Field) iter.next();
			String type = "";
			String jsType = "";
			String paramName; 

			switch (element.getSqlType()) {
			case Types.CHAR:
			case Types.VARCHAR:
				type = "s";
				jsType = "c";
				break;

			case Types.TIME:
			case Types.TIMESTAMP:
			case Types.DATE:
				type = "d";
				jsType = "t";
				break;

			default:
				type = "n";
				jsType = "f";
			}
			String columnName = StringHelper.makeMemberName(element.getName(),
					0);
			for (int i = 0; i < 12; i++) {
				if (element.isSelected(i)) {
					paramName = "_" + type + Constants.columnNames[i + 1] + "_" + columnName;
					paramList.add(paramName);
					paramColumn.put(paramName, columnName);
					paramJsType.put(paramName, jsType);
					paramLength.put(paramName, Integer.toString(element.getSqlLength()));
				}
			}
			columnList.add(columnName);
			lenList.add(Integer.toString(element.getSqlLength()));
			typeList.add(jsType);
		}
		
		params = new String[paramList.size()];
		for(int i = 0; i < params.length; i++){
			params[i] = (String)paramList.get(i);
		}
		columns = new String[columnList.size()];
		for(int i = 0; i < columns.length; i++){
			columns[i] = (String)columnList.get(i);
		}
		
		jsType = new String[typeList.size()];
		for(int i = 0; i < jsType.length; i++){
			jsType[i] = (String)typeList.get(i);
		}
		length = new String[lenList.size()];
		for(int i = 0; i < length.length; i++){
			length[i] = (String)lenList.get(i);
		}
	}
}
