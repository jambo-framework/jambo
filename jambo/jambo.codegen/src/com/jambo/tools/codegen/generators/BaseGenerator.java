package com.jambo.tools.codegen.generators;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import com.jambo.tools.codegen.util.Constants;
import com.jambo.tools.codegen.util.StringHelper;
import com.jambo.tools.codegen.util.ValueStore;

public abstract class BaseGenerator {
	protected static final Log log = LogFactory.getLog(BaseGenerator.class);

	private String baseClassName = ValueStore.baseClassName;
	protected String author = ValueStore.author;
	
	protected String srcFolder;

	protected String pkgName;

	protected String className;

	protected String framework = ValueStore.framework;
	
	public void generate() throws Exception {
		String pkgFolder = (pkgName == null) ? "" : (pkgName.replace(
				StringHelper.DOT, File.separatorChar));

		File dir = new File(srcFolder, pkgFolder);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File file = new File(dir, className + ".java");
		log.debug("save file to: " + file.getAbsolutePath());

        Template template =  null;
        Writer writer = null;
        String temp = getTempleFile();;
		try {
			template = Velocity.getTemplate(temp);
			writer = new PrintWriter(new FileOutputStream(file));
			if ( template != null){
			    template.merge(getContext(), writer);
			}
			writer.flush() ;
		} catch (Exception e) {
			log.error(e);
		} finally{
            if (writer != null) writer.close();
			File f = new File(temp) ;
			f.delete() ;
		}
	}
	
	protected abstract String getTempleFile() throws IOException ;

	protected String getBaseClassName() {
		return baseClassName;
	}

	protected String getPojoClassName() {
		return baseClassName + "VO";
	}

	protected String getDaoClassName() {
		return baseClassName + "DAO";
	}

	protected String getParamClassName() {
		return baseClassName + "DBParam";
	}

	protected String getFormClassName() {
		return baseClassName + "Form";
	}

//	protected String getWebParamName() {
//		return baseClassName + "WebParam";
//	}

	protected String geBoClassName() {
		return baseClassName + "BO";
	}

	protected String getEjbInterfName() {
		return baseClassName;
	}

	protected String getServiceClassName() {
		return baseClassName + "Service";
	}

	protected String getServiceImplClassName() {
		return baseClassName + "ServiceImpl";
	}

	protected String getActionClassName() {
//		if (framework.equals("JSF"))
//			return baseClassName + "Face";
//		else return baseClassName + "Action";
		return baseClassName + "Action";
	}

	protected String getTestDlgName() {
		return baseClassName + "Test";
	}

//	protected String getTestFaceName() {
//		return baseClassName + "FaceTest";
//	}

	protected String getHbmPkg() {
		return getModelPkg();
	}

	protected String getModelPkg() {
		return "com." + ValueStore.projname + ".business." 
		+ ValueStore.moduleName 
		+"." 
		+ baseClassName.toLowerCase()
		+ "." 
	    + "persistent";
	}

	protected String getControlPkg() {
		return "com." + ValueStore.projname + ".business." 
		+ ValueStore.moduleName 
		+"." 
		+ baseClassName.toLowerCase()
		+ "." 
	    + "control";
	}
	
	protected String getServicePkg() {
		return "com." + ValueStore.projname + "." 
		+ ValueStore.moduleName 
		+"." 
		+ baseClassName.toLowerCase()
		+ ".service";
	}

	protected String getServiceImplPkg() {
		return "com." + ValueStore.projname + "." 
		+ ValueStore.moduleName 
		+"." 
		+ baseClassName.toLowerCase()
		+ ".business";
	}

	protected String getViewPkg() {
		return "com." + ValueStore.projname + ".web." + ValueStore.moduleName + "."
		+ baseClassName.toLowerCase();
	}

	protected String getI18nPkg() {
		return "com." + ValueStore.projname + ".resource.i18n." + ValueStore.moduleName;
	}
	
	protected String getDataFile(){
		String projname = ValueStore.projname.replaceAll(".", "/");
		
		return "com/" + projname + "/business/" +  ValueStore.moduleName + "/data/" + baseClassName + ".data";
	}
	
    public VelocityContext getContext() {
        VelocityContext context = new VelocityContext();
        context.put("className", className);
        if (null != pkgName) {
            context.put("pkgName", "package " + pkgName + ";");
        } else {
            context.put("pkgName", "//null package");
        }
		context.put("pkgForder", pkgName.replaceAll("\\.", "/")) ;
        context.put("modelPkg", getModelPkg());
        context.put("controlPkg", getControlPkg());
        
        context.put("paramClassName", getParamClassName());
        context.put("boClassName", geBoClassName());
        context.put("pojoClassName", getPojoClassName());
        context.put("pojoClasses", getPojoClassName() + ".class");
        context.put("formClassName", getFormClassName());
		context.put("daoClassName", getDaoClassName()) ;
        context.put("baseClassName", getBaseClassName()) ;
		context.put("moduleName",ValueStore.moduleName.replaceAll("\\.", "/")) ;

        context.put("servicePkg", getServicePkg()) ;
        context.put("serviceImplPkg", getServiceImplPkg()) ;
        context.put("serviceClassName", getServiceClassName()) ;
        context.put("serviceImplClassName", getServiceImplClassName()) ;

        context.put("isParam", new Boolean(ValueStore.genOrNot[Constants.QUERYPARAM])) ;
        context.put("date", new Date());
        context.put("author", ValueStore.author);

//		context.put("ejbInterfName", getEjbInterfName()) ;
//      context.put("delegatePkg", getDelegatePkg());
//      context.put("dlgClassCla", getDlgClassName() + ".class");
//      context.put("webParamName", getWebParamName());

        return context;
    }

}
