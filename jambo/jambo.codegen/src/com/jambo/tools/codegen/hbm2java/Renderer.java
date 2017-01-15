//$Id: Renderer.java,v 1.1 2006/09/08 11:25:52 liaojinbo Exp $
package com.jambo.tools.codegen.hbm2java;


import java.io.PrintWriter;
import java.util.List;
import java.util.Map;


public interface Renderer {
	public void render(String savedToPackage, String savedToClass, ClassMapping classMapping, Map class2classmap, PrintWriter writer) throws Exception;
}






