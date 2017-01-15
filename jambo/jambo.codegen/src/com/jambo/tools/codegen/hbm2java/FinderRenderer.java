
package com.jambo.tools.codegen.hbm2java;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * <p>Title: Basic Finder Generator for Hibernate 2</p>
 * <p>Description: Generate basic finders for hibernate properties.
 * This requires two things in the hbm.xml files.
 *
 * The first is an indication of which fields you want to generate finders for.
 * You indicate that with a meta block inside a property tag such as
 *
 *  <property name="name" column="name" type="string">
 *    <meta attribute="finder-method">findByName</meta>
 *  </property>
 *
 * The finder method name will be the text enclosed in the meta tags.
 *
 * The second is to create a config file for hbm2java of the format:
 *
 *  <codegen>
 *    <generate renderer="net.sf.hibernate.tool.hbm2java.BasicRenderer"/>
 *    <generate suffix="Finder" renderer="net.sf.hibernate.tool.hbm2java.FinderRenderer"/>
 *  </codegen>
 *
 * And then use the param to hbm2java --config=xxx.xml where xxx.xml is the config file you
 * just created.
 *
 * An optional parameter is meta tag at the class level of the format:
 *
 * <meta attribute="session-method">com.whatever.SessionTable.getSessionTable().getSession();</meta>
 *
 * Which would be the way in which you get sessions if you use the Thread Local Session pattern
 * like I do.
 * </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * @author Matt Hall (matt2k(at)users.sf.net)
 * @author Max Rydahl Andersen (small adjustments and bugfixes)
 * @version 1.0
 */
//@SuppressWarnings("unchecked")
public class FinderRenderer extends AbstractRenderer {

    private static final String MT_FINDERMETHOD = "finder";

    /**
     * Render finder classes.
     *@param  classMapping
     *@param  class2classmap
     *@param  mainwriter
     *@exception  Exception
     */
    public void render(String savedToPackage, String savedToClass, ClassMapping classMapping, Map class2classmap, PrintWriter mainwriter) throws Exception {
        
        if (savedToPackage!=null && !savedToPackage.trim().equals("")) {
            mainwriter.println("package " + savedToPackage +";");
        } else if (classMapping.getGeneratedPackageName() != null) {
            mainwriter.println("package " + classMapping.getGeneratedPackageName() + ";");
        } else {
            mainwriter.println("// default package");
        }
        mainwriter.println();

        // switch to another writer to be able to insert the actually
        // used imports when whole class has been rendered.
        StringWriter strWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(strWriter);
    
        writer.println("/** Automatically generated Finder class for " + savedToClass  + ".\n" +
                       " * @author Hibernate FinderGenerator " +
                       " **/");
 
        String classScope = "public";
        writer.print(classScope + " class " + savedToClass);

        // always implements Serializable
        writer.print(" implements Serializable");

        writer.println(" {");
        writer.println();

        // switch to another writer to be able to insert the
        // veto- and changeSupport fields
        StringWriter strPropWriter = new StringWriter();
        PrintWriter propWriter = new PrintWriter(strPropWriter);

        doFinders(classMapping, class2classmap, propWriter);

        propWriter.println("}");

        writer.print(strPropWriter.toString());

        // finally write the imports
        doImports(classMapping, mainwriter);
        mainwriter.print(strWriter.toString());

    }


    /**
     *  Create finders for properties that have the <meta atttribute="finder-method">
     *  finderName</meta> block defined. Also, create a findAll(Session) method.
     *
     *@param  classMapping
     *@param  class2classmap
     *@param  writer
     */
    public void doFinders(ClassMapping classMapping, Map class2classmap, PrintWriter writer) {
        // Find out of there is a system wide way to get sessions defined
        String sessionMethod = classMapping.getMetaAsString("session-method").trim();

        // fields
        for (Iterator fields = classMapping.getFields().iterator(); fields.hasNext(); ) {
            Field field = (Field) fields.next();

            if (field.getMeta(MT_FINDERMETHOD) != null) {

                String finderName = field.getMetaAsString(MT_FINDERMETHOD);

                if ("".equals(sessionMethod)) {
                    // Make the method signature require a session to be passed in
                    writer.println("    public static List " + finderName +
                            "(Session session, " + getTrueTypeName(field, class2classmap) + " " + field.getName() + ") "
                            + "throws SQLException, HibernateException {");
                } else {
                    // Use the session method to get the session to execute the query
                    writer.println("    public static List " + finderName +
                            "(" + getTrueTypeName(field, class2classmap) + " " + field.getName() + ") "
                            + "throws SQLException, HibernateException {");
                    writer.println("        Session session = " + sessionMethod);
                }

                writer.println("        List finds = session.find(\"from " + classMapping.getName()
                        + " in class " + classMapping.getPackageName() + "." + classMapping.getName()
                        + " where " + classMapping.getName() + "." + field.getName() + "=?\", " + getFieldAsObject(false, field)
                        + ", " + getFieldAsHibernateType(false, field) + ");");
                writer.println("        return finds;");
                writer.println("    }");
            }
            writer.println();
        }

        // Create the findAll() method
        if ("".equals(sessionMethod)) {
            writer.println("    public static List findAll"
                    + "(Session session) "
                    + "throws SQLException, HibernateException {");

        } else {
            writer.println("    public static List findAll() "
                    + "throws SQLException, HibernateException {");
            writer.println("        Session session = " + sessionMethod);
        }
        writer.println("        List finds = session.find(\"from " + classMapping.getName()
                + " in class " + classMapping.getPackageName() + "." + classMapping.getName() + "\");");
        writer.println("        return finds;");
        writer.println("    }");
        writer.println();

    }


    static Map primitiveToObject = new HashMap();
    {
        primitiveToObject.put("char", "Character");

        primitiveToObject.put("byte", "Byte");
        primitiveToObject.put("short", "Short");
        primitiveToObject.put("int", "Integer");
        primitiveToObject.put("long", "Long");

        primitiveToObject.put("boolean", "Boolean");

        primitiveToObject.put("float", "Float");
        primitiveToObject.put("double", "Double");

    }


    /**
     *  Generate the imports for the finder class.
     *
     *@param  classMapping
     *@param  writer
     */
    public void doImports(ClassMapping classMapping, PrintWriter writer) {
        // imports is not included from the class it self as this is a separate generated class.
     /*   classMapping.getImports().add("java.io.Serializable");

        for (Iterator imports = classMapping.getImports().iterator(); imports.hasNext(); ) {
            writer.println("import " + imports.next() + ";");
        }
*/
        // Imports for finders
        writer.println("import java.io.Serializable;");
        writer.println("import java.util.List;");
        writer.println("import java.sql.SQLException;");
        writer.println();
        // * import is bad style. But better than importing classing that we don't necesarrily uses...
        writer.println("import net.sf.hibernate.*;");
//        writer.println("import net.sf.hibernate.Hibernate;");
//        writer.println("import net.sf.hibernate.HibernateException;");

        writer.println();
    }


    /**
     *  Gets the fieldAsObject attribute of the FinderRenderer object
     *
     *@param  prependThis
     *@param  field
     *@return
     */
    private String getFieldAsObject(boolean prependThis, Field field) {
        ClassName type = field.getClassType();
        if (type != null && type.isPrimitive() && !type.isArray()) {
            String typeName = (String) primitiveToObject.get(type.getName());
            typeName = "new " + typeName + "( ";
            typeName += prependThis ? "this." : "";
            return typeName + field.getName() + " )";
        }
        return field.getName();
    }


    /**
     *  Coversion map for field types to Hibernate types, might be good to move
     *  this to some other more general class
     */
    static Map hibType = new HashMap();
    {
        hibType.put("char", "Hibernate.CHARACTER");

        hibType.put("byte", "Hibernate.BYTE");
        hibType.put("short", "Hibernate.SHORT");
        hibType.put("int", "Hibernate.INTEGER");
        hibType.put("long", "Hibernate.LONG");

        hibType.put("boolean", "Hibernate.BOOLEAN");

        hibType.put("float", "Hibernate.FLOAT");
        hibType.put("double", "Hibernate.DOUBLE");

        hibType.put("String", "Hibernate.STRING");

    }


    /**
     *  Return the hibernate type string for the given field
     *
     *@param  prependThis
     *@param  field
     *@return
     */
    private String getFieldAsHibernateType(boolean prependThis, Field field) {
        ClassName type = field.getClassType();
        String hibTypeString = (String) hibType.get(type.getName());
        if (hibType != null) {
            return hibTypeString;
        } else {
            return "Hibernate.OBJECT";
        }
    }

}
