package com.jambo.tools.codegen.util;

import com.jambo.tools.codegen.wizards.params.Field;

public class ParamCreate {
	public String getMethodName(Field field) {
		String element = StringHelper.makeMemberName(field.getName(), ValueStore.numPrefix);
		String methodName = element.substring(0,1).toUpperCase() + element.substring(1);
		return methodName ;
	}
	
	public String getRealName(Field field) {
		String realName = StringHelper.makeMemberName(field.getName(), ValueStore.numPrefix);
		return realName ;
	}
}
