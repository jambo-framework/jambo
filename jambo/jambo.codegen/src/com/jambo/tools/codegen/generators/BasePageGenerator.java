package com.jambo.tools.codegen.generators;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import com.jambo.tools.codegen.util.FieldsHelper;
import com.jambo.tools.codegen.util.StringHelper;
import com.jambo.tools.codegen.util.ValueStore;


public abstract class BasePageGenerator {
    protected static final Log log = LogFactory.getLog(BasePageGenerator.class);
    protected String baseClassName = ValueStore.baseClassName.toLowerCase();
    protected String webFolder;
    protected String pkgName;
    protected String pageName;
	protected String framework = ValueStore.framework;
	protected VelocityContext context = new VelocityContext();
    private FieldsHelper helper;

    public BasePageGenerator() {
        helper = new FieldsHelper(ValueStore.fields);
    }

    public void generate() throws Exception {
        String pkgFolder = (pkgName == null) ? ""
                                             : (pkgName.replace(StringHelper.DOT,
                File.separatorChar));

        File dir = new File(webFolder, pkgFolder);

        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(dir, pageName + ".jsp");
        log.debug("save file to: " + file.getAbsolutePath());

        String temp = getTempleFile() ;
        Template template =  null;
        VelocityContext context = getContext() ;
        PrintWriter writer = null;
        try {
	        template = Velocity.getTemplate(temp);
			writer = new PrintWriter(new FileOutputStream(file));
	        if ( template != null){
	            template.merge(context, writer);
	        }
	        writer.flush() ;
        }catch (Exception e){
        	log.error(e);
        } finally{
            if (writer != null) writer.close();
    		File f = new File(temp) ;
    		f.delete() ;
        }
    }
	
	protected VelocityContext getContext(){
		VelocityContext context = new VelocityContext();
		context.put("moduleName", ValueStore.moduleName) ;
		context.put("className", getClassName()) ;
		context.put("baseClassName", ValueStore.baseClassName) ;
		context.put("actionName", getActionClassName()) ;

		List len = new ArrayList() ;
		List type = new ArrayList() ;
        
        context.put("helper", helper) ;
		Collections.addAll(len, helper.getLength()) ;
        Collections.addAll(type, helper.getJSType()) ;
        
        context.put("lens", len) ;
        context.put("types", type) ;
        context.put("paramColumn", helper.paramColumn) ;
        context.put("pk", getPK()) ;

        //提供转义词
        context.put("ELStart", "${") ;
        context.put("ELEnd", "}") ;
        context.put("EEStart", "#{") ;

    	return context ;
	}
	
	protected abstract  Object getPK();

	protected abstract String getTempleFile() throws IOException ;
	
	protected String getActionClassName() {
		return baseClassName + "Action";
	}

	protected String getClassName() {
		return baseClassName;
	}

	protected String getWebPkg() {
		return ValueStore.baseClassName.toLowerCase();
	}
	protected String getFaceName(){
		return baseClassName.substring(0,1).toLowerCase() + baseClassName.substring(1) + "Face";
	}
}
