package com.jambo.tools.codegen.util;

import org.apache.commons.lang.StringUtils;

public class StringHelper {
	public static final String EMPTY_STRING = "";

	public static final char DOT = '.';

	public static final char UNDERSCORE = '_';

	public static final String COMMA_SPACE = ", ";

	public static final String COMMA = ",";

	public static final String OPEN_PAREN = "(";

	public static final String CLOSE_PAREN = ")";

	public static final char SINGLE_QUOTE = '\'';

	public static String makeMemberName(String name, boolean reservePrefix) {
		String memberName = name.toLowerCase();
		int i;

		while ((i = memberName.indexOf(StringHelper.UNDERSCORE)) != -1) {
			java.lang.String tmp1 = memberName.substring(0, i);

			if ((i + 1) < memberName.length()) {
				if (reservePrefix) {
					tmp1 += memberName.substring(i + 1, i + 2).toUpperCase();
				} else {
					tmp1 = memberName.substring(i + 1, i + 2);
				}
			}

			if ((i + 2) < memberName.length()) {
				tmp1 += memberName.substring(i + 2);
			}

			memberName = tmp1;
		}

		return memberName;
	}

	public static String makeMemberName(String name, int menber) {
		String memberName = name.toLowerCase();
		int i;

		while ((i = memberName.indexOf(StringHelper.UNDERSCORE)) != -1) {
			java.lang.String tmp1 = memberName.substring(0, i);

			if ((i + 1) < memberName.length()) {
				if ((menber--)>0) {
					tmp1 = memberName.substring(i + 1, i + 2);
				} else {
					tmp1 += memberName.substring(i + 1, i + 2).toUpperCase();
				}
			}

			if ((i + 2) < memberName.length()) {
				tmp1 += memberName.substring(i + 2);
			}

			memberName = tmp1;
		}

		return memberName;
	}

	public static String camelName(String name, int menber) {
		String tmp = makeMemberName(name, menber) ;
		return tmp.substring(0, 1).toUpperCase()+tmp.substring(1) ;
	}

	public static String camelName(String name, boolean reservePrefix) {
		if(name.equals("")) return name;
		String tmpName = name.toLowerCase();
		int i;
		if ((i = tmpName.indexOf(StringHelper.UNDERSCORE)) != -1) {
			tmpName = reservePrefix ? tmpName : tmpName
					.substring(i + 1);
		}
		while ((i = tmpName.indexOf(StringHelper.UNDERSCORE)) != -1) {
			String tmp1 = tmpName.substring(0, i);

			if ((i + 1) < tmpName.length()) {
				tmp1 += tmpName.substring(i + 1, i + 2).toUpperCase();
			}

			if ((i + 2) < tmpName.length()) {
				tmp1 += tmpName.substring(i + 2);
			}

			tmpName = tmp1;
		}
		tmpName = tmpName.substring(0, 1).toUpperCase()
		+ tmpName.substring(1);
//		System.out.println("table name=[" + name + "]\tclassname=[" + tmpName + "]");
		return tmpName;

	}
	public static void main(String[] args){
		System.out.println(makeMemberName("b_sys_paramsetlog", 0));
	}
	public static String shortName(String nameWithPkg){
		return nameWithPkg.substring(nameWithPkg.lastIndexOf(".") + 1);
	}
	/**
	 * Convert string into something that can be rendered nicely into a javadoc
	 * comment.
	 * Prefix each line with a star ('*').
	 * @param string
	 */
	protected String toJavaDoc(String string, int indent) {
	    StringBuffer result = new StringBuffer();
	    
	    if(string!=null) {
	        String[] lines = StringUtils.split(string, "\n\r\f");
	        for (int i = 0; i < lines.length; i++) {
	            String docline = " * " + lines[i] + "\n";
	            result.append(StringUtils.leftPad(docline, docline.length() + indent));
	        }
	    }
	    
	    return result.toString();
	}
}
