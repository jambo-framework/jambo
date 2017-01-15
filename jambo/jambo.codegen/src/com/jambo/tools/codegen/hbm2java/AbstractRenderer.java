/*
 * Created on 25-03-2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code Template
 */
package com.jambo.tools.codegen.hbm2java;

import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;

import com.jambo.tools.codegen.util.StringHelper;



/**
 * @author max
 */
public abstract class AbstractRenderer implements Renderer {
	//2013-4-28 jinbo 保存字段列表,在写VO时读取注释内容
	public List columnList = null;

	/**
	 * Returns the true name for the given class name. By true name is
	 * that it will return the Proxy for the class name if the class was
	 * defined with a proxy attribute.
	 * @param field class name that we use to serach in class2classmap
	 * @param class2classmap a map from classname to classmappings
	 * @return String return either name or the proxy name of the classmap
	 */
	static protected String getTrueTypeName(Field field, Map class2classmap) {
		String name =
			(field.getClassType() != null)
				? field.getClassType().getFullyQualifiedName()
				: field.getType();
		ClassMapping cmap = (ClassMapping) class2classmap.get(name);

		if (cmap != null) {
			if (cmap.getProxy() != null) {
				return cmap.getProxy();
			}
		}
		return name;
	}

	static protected String getTrueTypeName(ClassName cn, Map class2classmap) {
		String name = cn.getFullyQualifiedName();
		ClassMapping cmap = (ClassMapping) class2classmap.get(name);

		if (cmap != null) {
			if (cmap.getProxy() != null) {
				return cmap.getProxy();
			}
		}
		return name;
	}

	/**
	 * Returns the last part of type if it is in the set of imports.
	 * e.g. java.util.Date would become Date, if imports contains 
	 * java.util.Date.
	 * 
	 * @param type
	 * @param imports
	 * @return String
	 */
	protected String shortenType(String type, TreeSet imports) {
	    if( imports.contains(type) ) {
	      return type.substring( type.lastIndexOf(StringHelper.DOT)+1 );
	    } 
	    else {
	      if( type.endsWith("[]") ) {
	        return shortenType( type.substring(0, type.length()-2), imports ) + "[]";    
	      } 
	      else {
	        return type;   
	      }
	    }
	}

	/**
	 * Convert string into something that can be rendered nicely into a javadoc
	 * comment.
	 * Prefix each line with a star ('*').
	 * @param string
	 */
	protected String toJavaDoc(String string, int indent) {
	    StringBuffer result = new StringBuffer();
	    
	    if(string!=null) {
	        String[] lines = StringUtils.split(string, "\n\r\f");
	        for (int i = 0; i < lines.length; i++) {
	            String docline = " * " + lines[i] + "\n";
	            result.append(StringUtils.leftPad(docline, docline.length() + indent));
	        }
	    }
	    
	    return result.toString();
	}

	public String getFieldScope(Field field, String localScopeName, String defaultScope) {
	    if (defaultScope==null) defaultScope = "private";
	    return ( field.getMeta(localScopeName)==null )? defaultScope : field.getMetaAsString(localScopeName);
	}

}
