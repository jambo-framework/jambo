package com.jambo.tools.codegen.generators;

import java.io.IOException;
import java.util.Date;

import org.apache.velocity.VelocityContext;

import com.jambo.tools.codegen.util.Constants;
import com.jambo.tools.codegen.util.TemplatesUtils;
import com.jambo.tools.codegen.util.ValueStore;
import com.jambo.tools.codegen.util.WriteFile;

public class BOClassGenerator extends BaseGenerator {
    public BOClassGenerator() {
        this.className = geBoClassName();
        this.srcFolder = ValueStore.srcFolder;
        this.pkgName = getControlPkg();
    }

//	public VelocityContext getContext() {
//		VelocityContext context = new VelocityContext();
//		context.put("className", className) ;
//		if (null != pkgName){
//			context.put("pkgName", "package "+pkgName +";") ;	
//		} else {
//			context.put("pkgName", "//null package") ;
//		}
//		context.put("poClassName", getPojoClassName()) ;
//		context.put("modelPkg", getModelPkg()) ;
//		context.put("isListVO", new Boolean(ValueStore.genOrNot[Constants.QUERYPARAM])) ;
//		context.put("listVOClassName", getParamName()) ;
//		context.put("daoClassName", getDaoClassName()) ;
//		context.put("daoClassCla", getDaoClassName()+".class") ;
//		context.put("ejbInterfName", getEjbInterfName()) ;
//		context.put("date", new Date()) ;
//		context.put("author", ValueStore.author) ;
//		context.put("baseClassName", getBaseClassName().toLowerCase()) ;
//		context.put("moduleName",ValueStore.moduleName.replaceAll("\\.", "/")) ;
//		return context ;
//	}

	public String getTempleFile() throws IOException {
		String fileName = "BOClassTemplet.templets" ;

		StringBuffer source = new StringBuffer(TemplatesUtils.getInstence().getString("BOClass",fileName)) ;
		WriteFile.write("template/BOClassTemplet.templets", source) ;

		return "template/BOClassTemplet.templets" ;
	}
}
