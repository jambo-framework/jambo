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

public class WebParamGenerator extends BaseGenerator {
	private List fields;
	private FieldsHelper helper;

	public WebParamGenerator(FieldList fieldList){
//        this.className = getWebParamName();
        this.srcFolder = ValueStore.srcFolder;
        this.pkgName = getViewPkg();

        fields = ValueStore.fields;
        helper = new FieldsHelper(fieldList.getFields());
	}

	public VelocityContext getContext() {
		VelocityContext context = new VelocityContext();
		context.put("pkField", ValueStore.pkFields) ;
		context.put("className", className) ;
		if (null != pkgName){
			context.put("pkgName", "package "+pkgName +";") ;	
		} else {
			context.put("pkgName", "//null package") ;
		}
		context.put("ListVOName", getParamClassName()) ;
		context.put("modelPkg", getModelPkg()) ;
		context.put("fields", fields) ;
		context.put("shd", new ParamCreate()) ;
		context.put("helper", helper) ;
		context.put("date", new Date()) ;
		context.put("author", ValueStore.author) ;
		return context ;
	}

	public String getTempleFile() throws IOException {
		String fileName = "WebParamTemplet.templets" ;

		StringBuffer source = new StringBuffer(TemplatesUtils.getInstence().getString("WebParam",fileName)) ;
		WriteFile.write("template/WebParamTemplet.templets", source) ;

		return "template/WebParamTemplet.templets" ;
	}
}
