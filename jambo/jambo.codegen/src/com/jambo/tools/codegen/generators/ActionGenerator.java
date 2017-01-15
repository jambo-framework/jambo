package com.jambo.tools.codegen.generators;

import java.io.IOException;
import java.util.Date;

import org.apache.velocity.VelocityContext;

//import com.maywide.tools.codegen.util.StringHelper;


import com.jambo.tools.codegen.util.StringHelper;
import com.jambo.tools.codegen.util.TemplatesUtils;
import com.jambo.tools.codegen.util.ValueStore;
import com.jambo.tools.codegen.util.WriteFile;

public class ActionGenerator extends BaseGenerator {

    public ActionGenerator() {
        this.className = getActionClassName();
        this.srcFolder = ValueStore.srcFolder;
        this.pkgName = getViewPkg();
    }

    public VelocityContext getContext() {
    	VelocityContext context = super.getContext();
        context.put("pkField", getFormatPK());
        return context;
    }

    public String getTempleFile() throws IOException {
        String fileName = "ActionTemplet.templets";
//        if (framework.equals("JSF")) {
//            fileName = "JsfBeanTemplet.templets";
//        }

        StringBuffer source = new StringBuffer(TemplatesUtils.getInstence()
                .getString("Action", fileName));
        WriteFile.write("template/ActionTemplet.templets", source);

        return "template/ActionTemplet.templets";
    }

   /**
    * 将数据库中的主键名更改为java字段的命名规则，如CUST_ID 转为custId
    * @author Yu Xinhua(Levin)
    * @return
    */
    private String getFormatPK() {
        String pks = ValueStore.pkFields;
        if (pks == null) {
            return null;
        }
        pks = pks.replaceAll("\"", "");
        if (pks.indexOf(",") < 0) {
            return "\""+StringHelper.makeMemberName(pks, 0)+"\"";
        }
        String[] pkArray = pks.split(",");
        StringBuffer pkBuffer = new StringBuffer();
        for (int i = 0; i < pkArray.length; i++) {
            pkBuffer.append("\"").append(StringHelper.makeMemberName(pkArray[i], 0)).append("\"");
            if(i<pkArray.length-1){
                pkBuffer.append(",");
            }
        }
        return pkBuffer.toString();

    }
}
