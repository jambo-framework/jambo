package com.jambo.tools.codegen.util;

public class OpinionUtils {
	public static boolean isNull(String s) {
		return (null==s||s.trim().length()==0) ;
	}
	
	public static String getUpperString(String s) {
		return isNull(s) ? null : s.toUpperCase() ;
	}
}
