package com.jambo.tools.codegen.generators;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.VelocityContext;

import com.jambo.tools.codegen.util.FieldsHelper;
import com.jambo.tools.codegen.util.StringHelper;
import com.jambo.tools.codegen.util.ValueStore;



public class I18nGenerator extends BaseGenerator{
	protected static final Log log = LogFactory.getLog(I18nGenerator.class);

	private String className;

	private String srcFolder;

	private String pkgName;

	public I18nGenerator() {
		this.className = ValueStore.baseClassName;
		this.srcFolder = ValueStore.srcFolder;
		this.pkgName = getViewPkg();
	}

	public void generate() throws IOException {
//		String pkgFolder = (pkgName == null) ? "" : (pkgName.replace(StringHelper.DOT, File.separatorChar));
		String pkgFolder = (pkgName == null) ? "" : "i18n/"+className;

		File dir = new File(srcFolder, pkgFolder);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File file = new File(dir, className + "Action_zh_CN.properties");
		log.debug("save file to: " + file.getAbsolutePath());
		PrintWriter writer = new PrintWriter(new FileOutputStream(file));
		doContent(writer);
		writer.close();
	}

	private void doContent(PrintWriter writer) {
		writer.println("titleList=" + ValueStore.baseClassName);
		writer.println("titleAdd=" + ValueStore.baseClassName);
		writer.println("titleView=" + ValueStore.baseClassName);
		writer.println("titleUpdate=" + ValueStore.baseClassName);
		FieldsHelper helper = new FieldsHelper(ValueStore.fields);
		for (int i = 0; i < helper.getParams().length; i++) {
			String tmp = helper.getParams()[i].toLowerCase();  //.substring(0,1).toUpperCase() + helper.getParams()[i].substring(1);
			writer.println(tmp + "=@"
					+ helper.getParams()[i].toUpperCase() + "@");
		}
		for (int i = 0; i < helper.getColumns().length; i++) {
			String tmp = helper.getColumns()[i].toLowerCase(); //.substring(0,1).toUpperCase() + helper.getColumns()[i].substring(1);
			writer.println(tmp + "=@"
					+ helper.getColumns()[i].toUpperCase() + "@");
		}
		writer.println();
	}

	public VelocityContext getContext() {
		return null ;
	}

	public String getTempleFile() throws IOException {
		return null ;
	}

}
