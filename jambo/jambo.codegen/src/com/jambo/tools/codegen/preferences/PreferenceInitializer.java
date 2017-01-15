package com.jambo.tools.codegen.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import com.jambo.tools.codegen.CodegenPlugin;



public class PreferenceInitializer extends AbstractPreferenceInitializer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
	 */
	public void initializeDefaultPreferences() {
		IPreferenceStore store = CodegenPlugin.getDefault()
				.getPreferenceStore();
		store.setDefault(PreferenceConstants.DRIVER_CLASS, "oracle.jdbc.driver.OracleDriver");
		store.setDefault(PreferenceConstants.CONNECTION_URL, "jdbc:oracle:thin:@10.200.5.201:1521:BOSS15TEST");
		store.setDefault(PreferenceConstants.USER,
				"test");
		store.setDefault(PreferenceConstants.PASSWORD, "test");
		store.setDefault(PreferenceConstants.CATALOG, "");
		store.setDefault(PreferenceConstants.SCHEMA, "");
		store.setDefault(PreferenceConstants.PREFIX, 2);
		store.setDefault(PreferenceConstants.TABLEONLY, false);
		store.setDefault(PreferenceConstants.TGTFOLDER, "");
		store.setDefault(PreferenceConstants.SRCFOLDER, "");
		store.setDefault(PreferenceConstants.TESTFOLDER, "");
		store.setDefault(PreferenceConstants.WEBFOLDER, "");
		store.setDefault(PreferenceConstants.AUTHOR, "");
		store.setDefault(PreferenceConstants.FRAMEWORK, "Struts");
		
		store.setDefault(PreferenceConstants.PROJECT_NAME, "boss");
		store.setDefault(PreferenceConstants.TEMPLATE_PATH, "/");
	}
}
