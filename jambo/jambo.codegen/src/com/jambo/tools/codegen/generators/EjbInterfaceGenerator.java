package com.jambo.tools.codegen.generators;

import java.io.IOException;
import java.util.Date;

import org.apache.velocity.VelocityContext;

import com.jambo.tools.codegen.util.Constants;
import com.jambo.tools.codegen.util.TemplatesUtils;
import com.jambo.tools.codegen.util.ValueStore;
import com.jambo.tools.codegen.util.WriteFile;

public class EjbInterfaceGenerator extends BaseGenerator {
    public EjbInterfaceGenerator() {
        this.className = getEjbInterfName();
        this.srcFolder = ValueStore.srcFolder;
        this.pkgName = getControlPkg();
    }

	public VelocityContext getContext() {
		VelocityContext context = new VelocityContext();
		context.put("className", className) ;
		if (null != pkgName){
			context.put("pkgName", "package "+pkgName +";") ;	
		} else {
			context.put("pkgName", "//null package") ;
		}
		context.put("poClassName", getPojoClassName()) ;
		context.put("modelPkg", getModelPkg()) ;
		context.put("isListVO", new Boolean(ValueStore.genOrNot[Constants.QUERYPARAM])) ;
		context.put("listVOClassName", getParamClassName()) ;
		context.put("daoClassName", getDaoClassName()) ;
		context.put("date", new Date()) ;
		context.put("author", ValueStore.author) ;
		return context ;
	}

	public String getTempleFile() throws IOException {
		String fileName = "EjbInterfaceTemplet.templets" ;

		StringBuffer source = new StringBuffer(TemplatesUtils.getInstence().getString("Ejbinterface",fileName)) ;
		WriteFile.write("template/EjbInterfaceTemplet.templets", source) ;

		return "template/EjbInterfaceTemplet.templets" ;
	}
}
