package com.jambo.tools.codegen.generators;

import java.io.IOException;
import java.util.Date;

import org.apache.velocity.VelocityContext;

import com.jambo.tools.codegen.util.Constants;
import com.jambo.tools.codegen.util.TemplatesUtils;
import com.jambo.tools.codegen.util.ValueStore;
import com.jambo.tools.codegen.util.WriteFile;

public class ServiceGenerator extends BaseGenerator {

    public ServiceGenerator() {
        this.className = getServiceClassName();
        this.srcFolder = ValueStore.srcFolder;
        this.pkgName = getServicePkg();
    }

	public String getTempleFile() throws IOException {
		String fileName = "ServiceTemplet.templets" ;

		StringBuffer source = new StringBuffer(TemplatesUtils.getInstence().getString("Service",fileName)) ;
		WriteFile.write("template/ServiceTemplet.templets", source) ;

		return "template/ServiceTemplet.templets" ;
	}
}
