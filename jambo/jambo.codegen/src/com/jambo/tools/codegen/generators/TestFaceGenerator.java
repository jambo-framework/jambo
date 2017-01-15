package com.jambo.tools.codegen.generators;

import java.io.IOException;

import org.apache.velocity.VelocityContext;

import com.jambo.tools.codegen.util.ValueStore;



public class TestFaceGenerator extends BaseGenerator {

	public TestFaceGenerator() {
        this.className = "" + getActionClassName() + "Test";
        this.srcFolder = ValueStore.testFolder;
        this.pkgName = getViewPkg();
	}

	public void generate() throws IOException {}

	public VelocityContext getContext() {
		return null ;
	}

	public String getTempleFile() throws IOException {
		return null ;
	}

}
