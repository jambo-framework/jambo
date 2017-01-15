package com.jambo.tools.codegen.generators;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.velocity.VelocityContext;

import com.jambo.tools.codegen.util.FieldsHelper;
import com.jambo.tools.codegen.util.ParamCreate;
import com.jambo.tools.codegen.util.TemplatesUtils;
import com.jambo.tools.codegen.util.ValueStore;
import com.jambo.tools.codegen.wizards.params.FieldList;
import com.jambo.tools.codegen.util.WriteFile;

public class FormGenerator extends BaseGenerator {
	private List fields;
	private FieldsHelper helper;

	public FormGenerator(FieldList fieldList){
        this.className = getFormClassName();
        this.srcFolder = ValueStore.srcFolder;
        this.pkgName = getViewPkg();

        fields = ValueStore.fields;
        helper = new FieldsHelper(fieldList.getFields());
	}

	public VelocityContext getContext() {
//		VelocityContext context = new VelocityContext();
//		context.put("pkField", ValueStore.pkFields) ;
//		context.put("className", className) ;
//		if (null != pkgName){
//			context.put("pkgName", "package "+pkgName+";") ;	
//		} else {
//			context.put("pkgName", "//null package") ;
//		}
//		context.put("pojoClassName", getPojoClassName()) ;
//		context.put("modelPkg", getModelPkg()) ;
//		context.put("date", new Date()) ;
//		context.put("author", ValueStore.author) ;
//		context.put("shd", new ParamCreate()) ;
    	VelocityContext context = super.getContext();
		context.put("fields", fields) ;
		context.put("helper", helper) ;
		return context ;
	}

	public String getTempleFile() throws IOException {
		String fileName = "ActionFormTemplet.templets" ;
//		if (framework.equals("JSF")){
//			fileName = "JsfBeanTemplet.templets" ;
//		}

		StringBuffer source = new StringBuffer(TemplatesUtils.getInstence().getString("ActionForm",fileName)) ;
		WriteFile.write("template/ActionFormTemplet.templets", source) ;

		return "template/ActionFormTemplet.templets" ;
	}
}
