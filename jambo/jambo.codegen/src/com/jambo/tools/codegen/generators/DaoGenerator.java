package com.jambo.tools.codegen.generators;

import java.io.IOException;
import java.util.Date;

import org.apache.velocity.VelocityContext;

import com.jambo.tools.codegen.util.TemplatesUtils;
import com.jambo.tools.codegen.util.ValueStore;
import com.jambo.tools.codegen.util.WriteFile;

public class DaoGenerator extends BaseGenerator {
	public DaoGenerator() {
		this.className = getDaoClassName();
		this.srcFolder = ValueStore.srcFolder;
		this.pkgName = getModelPkg();
		
	}

//	public VelocityContext getContext() {
//		VelocityContext context = new VelocityContext();
//		context.put("className", className) ;
//		if (null != pkgName){
//			context.put("pkgName", "package "+pkgName + ";") ;	
//		} else {
//			context.put("pkgName", "//null package") ;
//		}
//		context.put("poClassName", getPojoClassName()) ;
//		context.put("poClassCla", getPojoClassName()+".class") ;
//		context.put("date", new Date()) ;
//		context.put("author", ValueStore.author) ;
//		return context ;
//	}

	public String getTempleFile() throws IOException {
		String fileName = "DaoTemplet.templets" ;

		StringBuffer source = new StringBuffer(TemplatesUtils.getInstence().getString("DAO",fileName)) ;
		WriteFile.write("template/DaoTemplet.templets", source) ;

		return "template/DaoTemplet.templets" ;
	}
}
