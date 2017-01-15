package com.jambo.tools.codegen.generators;

import java.io.IOException;

import org.apache.velocity.VelocityContext;

import com.jambo.tools.codegen.util.Constants;
import com.jambo.tools.codegen.util.TemplatesUtils;
import com.jambo.tools.codegen.util.ValueStore;
import com.jambo.tools.codegen.util.WriteFile;



public class TestGenerator extends BaseGenerator {

	public TestGenerator() {
        this.className = geBoClassName() + "Test";
        this.srcFolder = ValueStore.testFolder;
        this.pkgName = getControlPkg();
	}

/*	public VelocityContext getContext() {
		VelocityContext context = new VelocityContext();
		if (null != pkgName){
			context.put("pkgName", "package "+pkgName +";") ;	
		} else {
			context.put("pkgName", "//null package") ;
		}
		context.put("pkgForder", pkgName.replaceAll("\\.", "/")) ;
		context.put("className", className) ;
		context.put("isListVO", new Boolean(ValueStore.genOrNot[Constants.QUERYPARAM])) ;
		context.put("QueryParamName", getParamName()) ;
		context.put("modelPkg", getModelPkg()) ;
		context.put("poClassName", getPojoClassName()) ;
		context.put("conpkg", getControlPkg()) ;
//		context.put("ejbInterfaceName", getEjbInterfName()) ;
		context.put("BOClassName", geBoClassName()) ;
		
		context.put("author", ValueStore.author) ;
		return context ;
	} */

	public String getTempleFile() throws IOException {
		String fileName = "TestTemplet.templets" ;

		StringBuffer source = new StringBuffer(TemplatesUtils.getInstence().getString("Test",fileName)) ;
		WriteFile.write("template/TestTemplet.templets", source) ;

		return "template/TestTemplet.templets" ;
	}

}
