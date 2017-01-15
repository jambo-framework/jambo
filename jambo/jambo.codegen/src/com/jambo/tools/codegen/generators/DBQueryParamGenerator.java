package com.jambo.tools.codegen.generators;

import java.io.IOException;
import java.util.Date;

import org.apache.velocity.VelocityContext;

import com.jambo.tools.codegen.util.FieldsHelper;
import com.jambo.tools.codegen.util.TemplatesUtils;
import com.jambo.tools.codegen.util.ValueStore;
import com.jambo.tools.codegen.wizards.params.FieldList;
import com.jambo.tools.codegen.util.WriteFile;

public class DBQueryParamGenerator extends BaseGenerator {
	private FieldsHelper helper;

    public DBQueryParamGenerator(FieldList fieldList) {
        this.className = getParamClassName();
        this.srcFolder = ValueStore.srcFolder;
        this.pkgName = getModelPkg();
    	helper = new FieldsHelper(fieldList.getFields());
    }

	public VelocityContext getContext() {
//		VelocityContext context = new VelocityContext();
//		context.put("className", className) ;
//		if (null != pkgName){
//			context.put("pkgName", "package "+pkgName +";") ;	
//		} else {
//			context.put("pkgName", "//null package") ;
//		}
//		context.put("helper", helper) ;
//		context.put("date", new Date()) ;
//		context.put("author", ValueStore.author) ;
//		context.put("modelPkg", getModelPkg()) ;
//		context.put("poClassName", getPojoClassName()) ;
		VelocityContext context = super.getContext();
		context.put("helper", helper) ;
		return context ;
	}

	public String getTempleFile() throws IOException {
		String fileName = "QueryParamTemplet.templets" ;
//		if (framework.equals("JSF")){
//			fileName = "JsfBeanTemplet.templets" ;
//		}

		StringBuffer source = new StringBuffer(TemplatesUtils.getInstence().getString("QueryParam",fileName)) ;
		WriteFile.write("template/QueryParamTemplet.templets", source) ;

		return "template/QueryParamTemplet.templets" ;
	}
}
