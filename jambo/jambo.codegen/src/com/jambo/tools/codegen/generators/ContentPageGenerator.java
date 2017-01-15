package com.jambo.tools.codegen.generators;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.velocity.VelocityContext;

import com.jambo.tools.codegen.util.FieldsHelper;
import com.jambo.tools.codegen.util.TemplatesUtils;
import com.jambo.tools.codegen.util.ValueStore;
import com.jambo.tools.codegen.util.WriteFile;



public class ContentPageGenerator extends BasePageGenerator {
    private FieldsHelper helper;

    public ContentPageGenerator() {
        super();
        webFolder = ValueStore.webFolder;
        pkgName = getWebPkg();
        pageName = "content";
//        helper = new FieldsHelper(ValueStore.fields);
    }
    
    /*	public VelocityContext getContext() {
		VelocityContext context = new VelocityContext();
		context.put("includeFile", getIncludeFile()) ;
		context.put("helper", helper) ;
		context.put("moduleName", ValueStore.moduleName) ;
		context.put("baseClassName", ValueStore.baseClassName.toLowerCase()) ;
		if (framework.equals("JSF")){
			context.put("faceName", getFaceName()) ;
		} else {
			List len = new ArrayList() ;
			List type = new ArrayList() ;
            Collections.addAll(len, helper.getLength()) ;
            Collections.addAll(type, helper.getJSType()) ;
            context.put("lens", len) ;
            context.put("types", type) ;
            context.put("pk", getPK()) ;
		}
    	return context ;
    }*/
    
    public String getTempleFile() throws IOException {
		String fileName = "ContentTemplet.templets" ;
//		if (framework.equals("JSF")){
//			fileName = "JsfContentTemplet.templets" ;
//		}

		StringBuffer source = new StringBuffer(TemplatesUtils.getInstence().getString("Content",fileName)) ;
		WriteFile.write("template/ContentTemplet.templets", source) ;

		return "template/ContentTemplet.templets" ;
    }
    
    protected String getPK() {
        String pks = ValueStore.pkFields;
        if (pks==null)
        	return null;
		pks = pks.replaceAll("\"", "");
		if (pks.indexOf(",") < 0)
			return pks.trim();
		String[] pkArray = pks.split(",");
		StringBuffer buffer = new StringBuffer().append(pkArray[0]
				.trim());
		for (int i = 1; i < pkArray.length; i++) {
			buffer.append(" + '|' + ").append(pkArray[i].trim());
		}
		return buffer.toString();
	}
    
    public static void main(String[] args){
    	System.out.println(1<<4) ;
    }
}
