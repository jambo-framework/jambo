package com.jambo.tools.codegen.generators;

import java.io.IOException;
import java.util.Date;

import org.apache.velocity.VelocityContext;

import com.jambo.tools.codegen.util.Constants;
import com.jambo.tools.codegen.util.TemplatesUtils;
import com.jambo.tools.codegen.util.ValueStore;
import com.jambo.tools.codegen.util.WriteFile;

public class ServiceImplGenerator extends BaseGenerator {

    public ServiceImplGenerator() {
        this.className = getServiceImplClassName();
        this.srcFolder = ValueStore.srcFolder;
        this.pkgName = getServiceImplPkg();
    }

	public String getTempleFile() throws IOException {
		String fileName = "ServiceImplTemplet.templets" ;

		StringBuffer source = new StringBuffer(TemplatesUtils.getInstence().getString("ServiceImpl",fileName)) ;
		WriteFile.write("template/ServiceImplTemplet.templets", source) ;

		return "template/ServiceImplTemplet.templets" ;
	}
}
