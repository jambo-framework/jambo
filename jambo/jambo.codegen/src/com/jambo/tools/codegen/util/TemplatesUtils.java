package com.jambo.tools.codegen.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import com.jambo.tools.codegen.CodegenPlugin;
import com.jambo.tools.codegen.preferences.PreferenceConstants;
import com.jambo.tools.codegen.util.WriteFile;

public class TemplatesUtils {
	private static TemplatesUtils in ;
	
	public String getString(String key) {
		return CodegenPlugin.getDefault().getPreferenceStore().getString(key) ;
	}
	
	public String getFileContent(String filename) throws RuntimeException {
		InputStream in = null;
		String path = getString(PreferenceConstants.TEMPLATE_PATH);
		if (path == null || path.equals("/") || path.equals("\\")){
			in = this.getClass().getClassLoader().getResourceAsStream("template\\jambo\\" + filename) ;
		} else{
			int pos = filename.lastIndexOf("/");
			filename = filename.substring(pos+1);
			filename = path + "\\" + filename;
			
			File f = new File(filename);
//			in = this.getClass().getClassLoader().getResourceAsStream(filename) ;
			try {
				in = new FileInputStream(f);
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException(e) ;
			}
		}
			
		try {
			return WriteFile.read(in).toString() ;
		} catch (IOException e) {
			throw new RuntimeException(e) ;
		}
	}
	
	public String getString(String key, String filename) {
		String template = getString(key) ;
		if (null == template||template.trim().length()==0){
			template = getFileContent(filename) ;
		}
		return template ;
	}
	
	public static TemplatesUtils getInstence(){
		if (null == in){
			in = new TemplatesUtils() ;
		}
		return in ;
	}
}
