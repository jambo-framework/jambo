/*
 * $Id: CodeGenerator.java,v 1.1 2006/09/08 11:25:52 liaojinbo Exp $
 */
package com.jambo.tools.codegen.hbm2java;


import org.hibernate.util.DTDEntityResolver;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.MultiHashMap;
import org.apache.commons.collections.MultiMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;


/**
 *
 */
public class CodeGenerator {

    static final Log log = LogFactory.getLog(CodeGenerator.class);
    
//      @SuppressWarnings("unchecked")
	public static void main(String[] args) {
        try {
            ArrayList mappingFiles = new ArrayList();
    
            SAXBuilder builder = new SAXBuilder();
            builder.setEntityResolver( new DTDEntityResolver() );
            
            builder.setErrorHandler( new ErrorHandler() {
    			public void error(SAXParseException error) {
    				log.error("Error parsing XML: " + error.getSystemId() + '(' + error.getLineNumber() + ')',error);
    			}
    			public void fatalError(SAXParseException error) { 
    				error(error); 
    			}
    			public void warning(SAXParseException error) {
    				log.warn("Warning parsing XML: " + error.getSystemId() + '(' + error.getLineNumber() + ')' );
    			}
            } );
            
            String outputDir = null;
    
            List generators = new ArrayList();
    
            MultiMap globalMetas = new MultiHashMap();
            // parse command line parameters
            for (int i = 0; i < args.length; i++) {
                if (args[i].startsWith("--")) {
    
                    if ( args[i].startsWith("--config=") ) {
                        // parse config xml file
                        Document document = builder.build( new File( args[i].substring(9) ) );
                        globalMetas = MetaAttributeHelper.loadAndMergeMetaMap(document.getRootElement(), null);
                        Iterator generateElements = document.getRootElement().getChildren("generate").iterator();
                        
                        while (generateElements.hasNext()) {
                            generators.add( new Generator( (Element) generateElements.next() ) );
                        }
                    }
                    else if ( args[i].startsWith("--output=") ) {
                    	outputDir = args[i].substring(9);
                    }
                    	
                } 
                else {
                    mappingFiles.add( args[i] );
                }
            }
    
            // if no config xml file, add a default generator
            if (generators.size() == 0) {
                generators.add( new Generator() );
            }
    		
            HashMap classMappings = new HashMap();
            builder.setValidation(true);
    		for ( Iterator iter = mappingFiles.iterator(); iter.hasNext(); ) {
            	// parse the mapping file
            	Document document = builder.build( new File( (String) iter.next() ) );
            	
            	Element rootElement = document.getRootElement();
            	Iterator classElements = rootElement.getChildren("class").iterator();
                MultiMap mm = MetaAttributeHelper.loadAndMergeMetaMap(rootElement, globalMetas);
                while ( classElements.hasNext() ) {
                    ClassMapping cmap = new ClassMapping( (Element) classElements.next(), mm);
                    classMappings.put(cmap.getCanonicalName(),cmap);
        	    }
    		}
    
            // generate source files
            for ( Iterator iterator = generators.iterator(); iterator.hasNext(); ) {
            	Generator g = (Generator) iterator.next();
            	g.setBaseDirName(outputDir);
                g.generate(classMappings);
            }
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}






