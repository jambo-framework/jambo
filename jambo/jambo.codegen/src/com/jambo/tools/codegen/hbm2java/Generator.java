//$Id: Generator.java,v 1.1 2006/09/08 11:25:52 liaojinbo Exp $
package com.jambo.tools.codegen.hbm2java;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Element;



import com.jambo.tools.codegen.util.StringHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 *
 */
public class Generator {
    private Log log = LogFactory.getLog(Generator.class);
    private String rendererClass = "com.jambo.tools.codegen.hbm2java.BasicRenderer";
    private String baseDirName = "generated";
    private String packageName = null;
    private String suffix = StringHelper.EMPTY_STRING;
    private String extension = "java";
    private boolean lowerFirstLetter = false;

	//2013-4-28 jinbo 保存字段列表,在写VO时读取注释内容
	public List columnList = null;

    /**
     * Constructs a new Generator using the defaults.
     */
    public Generator() {
    }

    /**
     * Constructs a new Generator, configured from XML.
     */
    public Generator(Element generateElement) throws Exception {
        String value = null;

        // set rendererClass field
        if ((this.rendererClass = generateElement.getAttributeValue("renderer")) == null) {
            throw new Exception("attribute renderer is required.");
        }

        // set dirName field
        if ((value = generateElement.getAttributeValue("dir")) != null) {
            this.baseDirName = value;
        }

        // set packageName field
        this.packageName = generateElement.getAttributeValue("package");

        // set suffix
        if ((value = generateElement.getAttributeValue("suffix")) != null) {
            this.suffix = value;
        }

        // set extension
        if ((value = generateElement.getAttributeValue("extension")) != null) {
            this.extension = value;
        }

        // set lowerFirstLetter
        value = generateElement.getAttributeValue("lowerFirstLetter");
        this.lowerFirstLetter = Boolean.valueOf(value).booleanValue();
    }

    /**
     *
     */
    public void generate(Map classMappingsCol) throws Exception {
        Renderer renderer = (Renderer) Class.forName(this.rendererClass)
                                            .newInstance();
        
		//2013-4-28 jinbo 为了不影响原来的代码,只好把字段列表这样一路传进去
        ((AbstractRenderer)renderer).columnList = this.columnList;

        /** Running through actual classes */
        for (Iterator classMappings = classMappingsCol.values().iterator();
                classMappings.hasNext();) {
            ClassMapping classMapping = (ClassMapping) classMappings.next();
            writeRecur(classMapping, classMappingsCol, renderer);
        }

        /** Running through components */
        for (Iterator cmpMappings = ClassMapping.getComponents();
                cmpMappings.hasNext();) {
            ClassMapping mapping = (ClassMapping) cmpMappings.next();
            write(mapping, classMappingsCol, renderer);
        }
    }

    private void writeRecur(ClassMapping classMapping, Map class2classmap,
        Renderer renderer) throws Exception {
        write(classMapping, class2classmap, renderer);

        if (!classMapping.getSubclasses().isEmpty()) {
            Iterator it = classMapping.getSubclasses().iterator();

            while (it.hasNext()) {
                writeRecur((ClassMapping) it.next(), class2classmap, renderer);
            }
        }
    }

    /**
     *
     */
    private void write(ClassMapping classMapping, Map class2classmap,
        Renderer renderer) throws Exception {
        File dir = this.getDir(classMapping.getGeneratedPackageName());
        File file = new File(dir,
                this.getFileName(classMapping.getGeneratedName()));
        log.debug("Writing " + file);

        PrintWriter writer = new PrintWriter(new FileOutputStream(file));

        renderer.render(this.packageName,
            getName(classMapping.getGeneratedName()), classMapping,
            class2classmap, writer);
        writer.close();
    }

    /**
     *
     */
    private String getFileName(String className) {
        return this.getName(className) + "." + this.extension;
    }

    /**
     *
     */
    private String getName(String className) {
        String name = null;

        if (this.lowerFirstLetter) {
            name = className.substring(0, 1).toLowerCase() +
                className.substring(1, className.length());
        } else {
            name = className;
        }

        return name + this.suffix;
    }

    /**
     *
     */
    private File getDir(String packageName) throws Exception {
        File baseDir = new File(this.baseDirName);
        File dir = null;

        if (this.packageName == null) {
            dir = new File(baseDir,
                    (packageName == null) ? StringHelper.EMPTY_STRING
                                          : packageName.replace(
                        StringHelper.DOT, File.separatorChar));
        } else {
            dir = new File(baseDir,
                    this.packageName.replace(StringHelper.DOT,
                        File.separatorChar));
        }

        // if the directory exists, make sure it is a directory
        if (dir.exists()) {
            if (!dir.isDirectory()) {
                throw new Exception("The path: " + dir.getCanonicalPath() +
                    " exists, but is not a directory");
            }
        } // else make the directory and any non-existent parent directories
        else {
            if (!dir.mkdirs()) {
                throw new Exception("unable to create directory: " +
                    dir.getCanonicalPath());
            }
        }

        return dir;
    }

    public String getBaseDirName() {
        return baseDirName;
    }

    public void setBaseDirName(String baseDirName) {
        if (baseDirName != null) {
            this.baseDirName = baseDirName;
        }
    }
}
