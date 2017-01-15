//$Id: ClassMapping.java,v 1.1 2006/09/08 11:25:52 liaojinbo Exp $
package com.jambo.tools.codegen.hbm2java;


import org.apache.commons.collections.MultiMap;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.type.PrimitiveType;
import org.hibernate.type.Type;
import org.hibernate.type.TypeFactory;
import org.hibernate.usertype.UserType;
import org.hibernate.util.ReflectHelper;
import org.jdom.Attribute;
import org.jdom.Element;



import com.jambo.tools.codegen.util.StringHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;


public class ClassMapping {
    static private Log log = LogFactory.getLog(ClassMapping.class);
    private static final Map components = new HashMap();
    private ClassName name = null;
    private ClassName generatedName = null;
    private String superClass = null;
    private ClassMapping superClassMapping = null;
    private String proxyClass = null;
    private List fields = new ArrayList();
    private TreeSet imports = new TreeSet();
    private List subclasses = new ArrayList();
    private boolean mustImplementEquals = false;
    private MultiMap metaattribs;
    private boolean shouldBeAbstract = false;

    public ClassMapping(ClassName superClass, ClassMapping superClassMapping,
        Element classElement, MultiMap inheritedMeta) throws Exception {
        this(superClass, classElement, inheritedMeta);

        this.superClassMapping = superClassMapping;

        if (this.superClassMapping != null) {
            List l = this.superClassMapping.getAllFieldsForFullConstructor();

            for (Iterator iter = l.iterator(); iter.hasNext();) {
                Field element = (Field) iter.next();
                ClassName ct = element.getClassType();

                if (ct != null) { // add imports for superclasses possible fields.
                    addImport(ct);
                } else {
                    addImport(element.getType());
                }
            }
        }
    }

    public ClassMapping(ClassName superClass, Element classElement,
        MultiMap inheritedMeta) throws Exception {
        initWith(superClass, classElement, false, inheritedMeta);
    }

    public ClassMapping(Element classElement, MultiMap inheritedMeta)
        throws Exception {
        initWith(null, classElement, false, inheritedMeta);
    }

    public ClassMapping(Element classElement, boolean component,
        MultiMap inheritedMeta) throws Exception {
        initWith(null, classElement, component, inheritedMeta);
    }

//    @SuppressWarnings("unchecked")
	protected void initWith(ClassName superClass, Element classElement,
        boolean component, MultiMap inheritedMeta) throws Exception {
        String fullyQualifiedName = classElement.getAttributeValue(component
                ? "class" : "name");

        log.debug("Processing mapping for class: " + fullyQualifiedName);

        setMetaAttribs(MetaAttributeHelper.loadAndMergeMetaMap(classElement,
                inheritedMeta));

        //    class & package names
        name = new ClassName();
        name.setFullyQualifiedName(fullyQualifiedName);

        if (getMeta("generated-class") != null) {
            generatedName = new ClassName();
            generatedName.setFullyQualifiedName(getMetaAsString(
                    "generated-class").trim());
            shouldBeAbstract = true;
            log.warn("Generating " + generatedName + " instead of " + name);
        } else {
            generatedName = name;
        }

        if (superClass != null) {
            this.superClass = superClass.getName();
            addImport(superClass); // can only be done AFTER this class gets its own name.
        }

        // get the properties defined for this class
        List propertyList = new ArrayList();
        propertyList.addAll(classElement.getChildren("property"));
        propertyList.addAll(classElement.getChildren("version"));
        propertyList.addAll(classElement.getChildren("timestamp"));
        propertyList.addAll(classElement.getChildren("key-property"));
        propertyList.addAll(classElement.getChildren("any"));

        // get all many-to-one associations defined for the class
        List manyToOneList = new ArrayList();
        manyToOneList.addAll(classElement.getChildren("many-to-one"));
        manyToOneList.addAll(classElement.getChildren("key-many-to-one"));

        Attribute att = classElement.getAttribute("proxy");

        if (att != null) {
            proxyClass = att.getValue();
        }

        Element id = classElement.getChild("id");

        if (id != null) {
            propertyList.add(0, id);
            implementEquals();
        }

        // composite id
        Element cmpid = classElement.getChild("composite-id");

        if (cmpid != null) {
            implementEquals();

            String cmpname = cmpid.getAttributeValue("name");
            String cmpclass = cmpid.getAttributeValue("class");

            if ((cmpclass == null) ||
                    cmpclass.equals(StringHelper.EMPTY_STRING)) {
                //Embedded composite id
                //implementEquals();
                propertyList.addAll(0, cmpid.getChildren("key-property"));
                manyToOneList.addAll(0, cmpid.getChildren("key-many-to-one"));
            } else {
                //Composite id class
                ClassMapping mapping = new ClassMapping(cmpid, true, metaattribs);
                MultiMap metaForCompositeid = MetaAttributeHelper.loadAndMergeMetaMap(cmpid,
                        metaattribs);
                mapping.implementEquals();

                ClassName classType = new ClassName();
                classType.setFullyQualifiedName(cmpclass);
                // add an import and field for this property
                addImport(classType);

                Field cmpidfield = new Field(cmpname, classType, false, true,
                        false, metaForCompositeid);
                fields.add(cmpidfield);
                components.put(mapping.getCanonicalName(), mapping);
            }
        }

        // derive the class imports and fields from the properties
        for (Iterator properties = propertyList.iterator();
                properties.hasNext();) {
            Element property = (Element) properties.next();

            MultiMap metaForProperty = MetaAttributeHelper.loadAndMergeMetaMap(property,
                    metaattribs);
            String name = property.getAttributeValue("name");

            if ((name == null) ||
                    name.trim().equals(StringHelper.EMPTY_STRING)) {
                continue; //since an id doesn't necessarily need a name
            }

            // ensure that the type is specified
            String type = property.getAttributeValue("type");

            if ((type == null) && (cmpid != null)) { // for composite-keys
                type = property.getAttributeValue("class");
            }

            if ("timestamp".equals(property.getName())) {
                type = "java.util.Date";
            }

            if ("any".equals(property.getName())) {
                type = "java.lang.Object";
            }

            if ((type == null) ||
                    type.trim().equals(StringHelper.EMPTY_STRING)) {
                log.warn("property \"" + name + "\" in class " + getName() +
                    " is missing a type attribute");

                continue;
            }

            // handle in a different way id and properties...
            // ids may be generated and may need to be of object type in order to support
            // the unsaved-value "null" value.
            // Properties may be nullable (ids may not)
            if (property == id) {
                Element generator = property.getChild("generator");
                String unsavedValue = property.getAttributeValue(
                        "unsaved-value");
                boolean needObject = ((unsavedValue != null) &&
                    unsavedValue.equals("null"));
                boolean generated = !generator.getAttributeValue("class")
                                              .equals("assigned");
                Field idField = new Field(name, getFieldType(type, needObject),
                        false, true, generated, metaForProperty);
                fields.add(idField);
            } else {
                String notnull = property.getAttributeValue("not-null");

                // if not-null property is missing lets see if it has been
                // defined at column level
                if (notnull == null) {
                    Element column = property.getChild("column");

                    if (column != null) {
                        notnull = column.getAttributeValue("not-null");
                    }
                }

                boolean nullable = ((notnull == null) ||
                    notnull.equals("false"));
                boolean key = property.getName().startsWith("key-"); //a composite id property
                Field stdField = new Field(name, getFieldType(type),
                        nullable && !key, key, false, metaForProperty);
                fields.add(stdField);
            }
        }

        // one to ones
        List onetooneList = classElement.getChildren("one-to-one");

        for (Iterator onetoones = onetooneList.iterator(); onetoones.hasNext();) {
            Element onetoone = (Element) onetoones.next();

            MultiMap metaForOneToOne = MetaAttributeHelper.loadAndMergeMetaMap(onetoone,
                    metaattribs);
            String name = onetoone.getAttributeValue("name");

            // ensure that the class is specified
            String clazz = onetoone.getAttributeValue("class");

            if (StringUtils.isEmpty(clazz)) {
                log.warn("one-to-one \"" + name + "\" in class " + getName() +
                    " is missing a class attribute");

                continue;
            }

            Field fm = new Field(name, getFieldType(clazz), true,
                    metaForOneToOne);
            fields.add(fm);
        }

        // many to ones - TODO: consolidate with code above
        for (Iterator manytoOnes = manyToOneList.iterator();
                manytoOnes.hasNext();) {
            Element manyToOne = (Element) manytoOnes.next();

            MultiMap metaForManyToOne = MetaAttributeHelper.loadAndMergeMetaMap(manyToOne,
                    metaattribs);
            String name = manyToOne.getAttributeValue("name");

            // ensure that the type is specified
            String type = manyToOne.getAttributeValue("class");

            if (StringUtils.isEmpty(type)) {
                log.warn("many-to-one \"" + name + "\" in class " + getName() +
                    " is missing a class attribute");

                continue;
            }

            ClassName classType = new ClassName();
            classType.setFullyQualifiedName(type);

            // is it nullable?
            String notnull = manyToOne.getAttributeValue("not-null");
            boolean nullable = ((notnull == null) || notnull.equals("false"));
            boolean key = manyToOne.getName().startsWith("key-"); //a composite id property

            // add an import and field for this property
            addImport(classType);

            Field f = new Field(name, classType, nullable && !key, key, false,
                    metaForManyToOne);
            fields.add(f);
        }

        // collections
        doCollections(classElement, "list", "java.util.List",
            "java.util.ArrayList", metaattribs);
        doCollections(classElement, "map", "java.util.Map",
            "java.util.HashMap", metaattribs);
        doCollections(classElement, "set", "java.util.Set",
            "java.util.HashSet", metaattribs);
        doCollections(classElement, "bag", "java.util.Collection",
            "java.util.ArrayList", metaattribs);
        doArrays(classElement, "array", metaattribs);
        doArrays(classElement, "primitive-array", metaattribs);

        //components
        for (Iterator iter = classElement.getChildren("component").iterator();
                iter.hasNext();) {
            Element cmpe = (Element) iter.next();
            MultiMap metaForComponent = MetaAttributeHelper.loadAndMergeMetaMap(cmpe,
                    metaattribs);
            String cmpname = cmpe.getAttributeValue("name");
            String cmpclass = cmpe.getAttributeValue("class");

            if ((cmpclass == null) ||
                    cmpclass.equals(StringHelper.EMPTY_STRING)) {
                log.warn("component \"" + cmpname + "\" in class " + getName() +
                    " does not specify a class");

                continue;
            }

            ClassMapping mapping = new ClassMapping(cmpe, true, metaattribs);

            ClassName classType = new ClassName();
            classType.setFullyQualifiedName(cmpclass);
            // add an import and field for this property
            addImport(classType);

            Field ff = new Field(cmpname, classType, false, metaForComponent);
            fields.add(ff);
            components.put(mapping.getCanonicalName(), mapping);
        }

        //    subclasses (done last so they can access this superclass for info)
        for (Iterator iter = classElement.getChildren("subclass").iterator();
                iter.hasNext();) {
            Element subclass = (Element) iter.next();
            ClassMapping subclassMapping = new ClassMapping(name, this,
                    subclass, metaattribs);
            subclasses.add(subclassMapping);
        }

        for (Iterator iter = classElement.getChildren("joined-subclass")
                                         .iterator(); iter.hasNext();) {
            Element subclass = (Element) iter.next();
            ClassMapping subclassMapping = new ClassMapping(name, this,
                    subclass, metaattribs);
            subclasses.add(subclassMapping);
        }

        validateMetaAttribs();
    }

    /**
     * Method setMetaAttribs.
     * @param multiMap
     */
    private void setMetaAttribs(MultiMap multiMap) {
        metaattribs = multiMap;
    }

    public void implementEquals() {
        mustImplementEquals = true;
    }

    public boolean mustImplementEquals() {
        return mustImplementEquals;
    }

    public List getFields() {
        return fields;
    }

    public TreeSet getImports() {
        return imports;
    }

    public String getCanonicalName() {
        return name.getFullyQualifiedName();
    }

    public String getName() {
        return name.getName();
    }

    public String getGeneratedName() {
        return generatedName.getName();
    }

    public String getProxy() {
        return proxyClass;
    }

    public String getPackageName() {
        return name.getPackageName();
    }

    public String getGeneratedPackageName() {
        return generatedName.getPackageName();
    }

    public List getSubclasses() {
        return subclasses;
    }

    public String getSuperClass() {
        return superClass;
    }

    // We need a minimal constructor only if it's different from
    // the full constructor or the no-arg constructor.
    // A minimal construtor is one that lets
    // you specify only the required fields.
    public boolean needsMinimalConstructor() {
        boolean generatedId = true;
        boolean missingId = true;
        int countNull = 0;

        for (Iterator it = fields.iterator(); it.hasNext();) {
            Field f = (Field) it.next();

            if (f.isIdentifier()) {
                generatedId = f.isGenerated();
                missingId = false;
            } else if (f.isNullable()) {
                countNull++;
            }
        }

        return !((countNull == 0) ||
        ((countNull == (fields.size() - 1)) && generatedId) ||
        ((countNull == fields.size()) && missingId));
    }

//    @SuppressWarnings("unchecked")
	public List getLocalFieldsForFullConstructor() {
        List result = new ArrayList();

        for (Iterator fields = getFields().iterator(); fields.hasNext();) {
            Field field = (Field) fields.next();

            if (!field.isIdentifier() ||
                    (field.isIdentifier() && !field.isGenerated())) {
                result.add(field);
            }
        }

        return result;
    }

//    @SuppressWarnings("unchecked")
	public List getFieldsForSupersFullConstructor() {
        List result = new ArrayList();

        if (getSuperClassMapping() != null) {
            // The correct sequence is vital here, as the subclass should be
            // able to invoke the fullconstructor based on the sequence returned
            // by this method!
            result.addAll(getSuperClassMapping()
                              .getFieldsForSupersFullConstructor());
            result.addAll(getSuperClassMapping()
                              .getLocalFieldsForFullConstructor());
        }

        return result;
    }

//    @SuppressWarnings("unchecked")
	public List getLocalFieldsForMinimalConstructor() {
        List result = new ArrayList();

        for (Iterator fields = getFields().iterator(); fields.hasNext();) {
            Field field = (Field) fields.next();

            if ((!field.isIdentifier() && !field.isNullable()) ||
                    (field.isIdentifier() && !field.isGenerated())) {
                result.add(field);
            }
        }

        return result;
    }

//    @SuppressWarnings("unchecked")
	public List getAllFields() {
        List result = new ArrayList();

        if (getSuperClassMapping() != null) {
            result.addAll(getSuperClassMapping().getAllFields());
        } else {
            result.addAll(getFields());
        }

        return result;
    }

//    @SuppressWarnings("unchecked")
	public List getAllFieldsForFullConstructor() {
        List result = getFieldsForSupersFullConstructor();
        result.addAll(getLocalFieldsForFullConstructor());

        return result;
    }

//    @SuppressWarnings("unchecked")
	public List getFieldsForSupersMinimalConstructor() {
        List result = new ArrayList();

        if (getSuperClassMapping() != null) {
            // The correct sequence is vital here, as the subclass should be
            // able to invoke the fullconstructor based on the sequence returned
            // by this method!
            result.addAll(getSuperClassMapping()
                              .getFieldsForSupersMinimalConstructor());
            result.addAll(getSuperClassMapping()
                              .getLocalFieldsForMinimalConstructor());
        }

        return result;
    }

//    @SuppressWarnings("unchecked")
	public List getAllFieldsForMinimalConstructor() {
        List result = getFieldsForSupersMinimalConstructor();
        result.addAll(getLocalFieldsForMinimalConstructor());

        return result;
    }

//    @SuppressWarnings("unchecked")
	public void addImport(ClassName className) {
        // if the package is java.lang or our own package don't add
        if (!className.inJavaLang() && !className.inSamePackage(generatedName) &&
                !className.isPrimitive()) {
            if (className.isArray()) {
                imports.add(className.getFullyQualifiedName()
                                     .substring(0,
                        className.getFullyQualifiedName().length() - 2)); // remove []
            } else {
                imports.add(className.getFullyQualifiedName());
            }
        }
    }

    public void addImport(String className) {
        ClassName cn = new ClassName();
        cn.setFullyQualifiedName(className);
        addImport(cn);
    }

    public static Iterator getComponents() {
        return components.values().iterator();
    }

//    @SuppressWarnings("unchecked")
	private void doCollections(Element classElement, String xmlName,
        String interfaceClass, String implementingClass, MultiMap inheritedMeta) {
        ClassName interfaceClassName = new ClassName();
        ClassName implementingClassName = new ClassName();

        interfaceClassName.setFullyQualifiedName(interfaceClass);
        implementingClassName.setFullyQualifiedName(implementingClass);

        for (Iterator collections = classElement.getChildren(xmlName).iterator();
                collections.hasNext();) {
            Element collection = (Element) collections.next();
            MultiMap metaForCollection = MetaAttributeHelper.loadAndMergeMetaMap(collection,
                    inheritedMeta);
            String name = collection.getAttributeValue("name");

            // add an import and field for this collection
            addImport(interfaceClassName);

            // import implementingClassName should only be 
            // added if the initialisaiton code of the field 
            // is actually used - and currently it isn't!
            //addImport(implementingClassName);
            ClassName foreignClass = null;
            Set foreignKeys = null;

            // Collect bidirectional data
            if (collection.getChildren("one-to-many").size() != 0) {
                foreignClass = new ClassName();
                foreignClass.setFullyQualifiedName(collection.getChild(
                        "one-to-many").getAttributeValue("class"));
            } else if (collection.getChildren("many-to-many").size() != 0) {
                foreignClass = new ClassName();
                foreignClass.setFullyQualifiedName(collection.getChild(
                        "many-to-many").getAttributeValue("class"));
            }

            // Do the foreign keys and import
            if (foreignClass != null) {
                // Collect the keys
                foreignKeys = new HashSet();
                foreignKeys.add(collection.getChild("key")
                                          .getAttributeValue("column"));

                for (Iterator iter = collection.getChild("key")
                                               .getChildren("column").iterator();
                        iter.hasNext();) {
                    foreignKeys.add(((Element) iter.next()).getAttributeValue(
                            "name"));
                }

                addImport(foreignClass);
            }

            Field cf = new Field(name, interfaceClassName,
                    "new " + implementingClassName.getName() + "()", false,
                    foreignClass, foreignKeys, metaForCollection);
            fields.add(cf);

            if (collection.getChildren("composite-element") != null) {
                for (Iterator compositeElements = collection.getChildren(
                            "composite-element").iterator();
                        compositeElements.hasNext();) {
                    Element compositeElement = (Element) compositeElements.next();
                    String compClass = compositeElement.getAttributeValue(
                            "class");

                    try {
                        ClassMapping mapping = new ClassMapping(compositeElement,
                                true, metaattribs);
                        ClassName classType = new ClassName();
                        classType.setFullyQualifiedName(compClass);
                        // add an import and field for this property
                        addImport(classType);
                        components.put(mapping.getCanonicalName(), mapping);
                    } catch (Exception e) {
                        log.error("Error building composite-element " +
                            compClass, e);
                    }
                }
            }
        }
    }

//    @SuppressWarnings("unchecked")
	private void doArrays(Element classElement, String type,
        MultiMap inheritedMeta) {
        for (Iterator arrays = classElement.getChildren(type).iterator();
                arrays.hasNext();) {
            Element array = (Element) arrays.next();
            MultiMap metaForArray = MetaAttributeHelper.loadAndMergeMetaMap(array,
                    inheritedMeta);
            String role = array.getAttributeValue("name");
            String elementClass = array.getAttributeValue("element-class");

            if (elementClass == null) {
                Element elt = array.getChild("element");

                if (elt == null) {
                    elt = array.getChild("one-to-many");
                }

                if (elt == null) {
                    elt = array.getChild("many-to-many");
                }

                if (elt == null) {
                    elt = array.getChild("composite-element");
                }

                if (elt == null) {
                    log.warn("skipping collection with subcollections");

                    continue;
                }

                elementClass = elt.getAttributeValue("type");

                if (elementClass == null) {
                    elementClass = elt.getAttributeValue("class");
                }
            }

            ClassName cn = getFieldType(elementClass);
            cn.setFullyQualifiedName(cn.getFullyQualifiedName() + "[]",
                cn.isPrimitive());
            cn.setIsArray(true);

            Field af = new Field(role, cn, false, metaForArray);
            fields.add(af);
        }
    }

    private ClassName getFieldType(String hibernateType) {
        return getFieldType(hibernateType, false);
    }

    private ClassName getFieldType(String hibernateType, boolean needObject) {
        // deal with hibernate binary type
        ClassName cn = new ClassName();

        if (hibernateType.equals("binary")) {
            cn.setFullyQualifiedName("byte[]", true);
            cn.setIsArray(true);

            return cn;
        } else {
            Type basicType = TypeFactory.basic(hibernateType);

            if (basicType != null) {
                if ((basicType instanceof PrimitiveType) &&
                        !hibernateType.trim()
                                          .equals(basicType.getReturnedClass()
                                                               .getName()) &&
                        !needObject) {
                    cn.setFullyQualifiedName(((PrimitiveType) basicType).getPrimitiveClass()
                                              .getName(), true);

                    return cn;
                } else {
                    cn.setFullyQualifiedName(basicType.getReturnedClass()
                                                      .getName());

                    return cn;
                }
            } else {
                // check and resolve correct type if it is an usertype
                hibernateType = getTypeForUserType(hibernateType);

                ClassName classType = new ClassName();
                classType.setFullyQualifiedName(hibernateType);
                // add an import and field for this property
                addImport(classType);

                return classType;
            }
        }
    }

    /** Returns name of returnedclass if type is an UserType **/
    private String getTypeForUserType(String type) {
        Class clazz = null;

        try {
            clazz = ReflectHelper.classForName(type);

            if (UserType.class.isAssignableFrom(clazz)) {
                UserType ut = (UserType) clazz.newInstance();
                log.debug("Resolved usertype: " + type + " to " +
                    ut.returnedClass().getName());

                String t = clazzToName(ut.returnedClass());

                return t;
            }
        } catch (ClassNotFoundException e) {
            log.warn("Could not find UserType: " + type + ". Using the type '" +
                type + "' directly instead. (" + e.toString() + ")");
        } catch (IllegalAccessException iae) {
            log.warn("Error while trying to resolve UserType. Using the type '" +
                type + "' directly instead. (" + iae.toString() + ")");
        } catch (InstantiationException e) {
            log.warn("Error while trying to resolve UserType. Using the type '" +
                type + "' directly instead. (" + e.toString() + ")");
        }

        return type;
    }

    private String clazzToName(Class cl) {
        String s = null;

        if (cl.isArray()) {
            s = clazzToName(cl.getComponentType()) + "[]";
        } else {
            s = cl.getName();
        }

        return s;
    }

    /**
     * Returns the superClassMapping.
     * @return ClassMapping
     */
    public ClassMapping getSuperClassMapping() {
        return superClassMapping;
    }

    Collection getMeta(String attribute) {
        return (Collection) metaattribs.get(attribute);
    }

    /**
     * Method getMetaAsString.
     * @param string
     * @return String
     */
    public String getMetaAsString(String attribute) {
        Collection c = getMeta(attribute);

        return MetaAttributeHelper.getMetaAsString(c);
    }

    /**
     * Method shouldBeAbstract.
     * @return boolean
     */
    public boolean shouldBeAbstract() {
        return shouldBeAbstract;
    }

    // Based on some raw heuristics the following method validates the provided metaattribs.
    void validateMetaAttribs() {
        // Inform that "extends" is not used if this one is a genuine subclass
        if ((getSuperClass() != null) && (getMeta("extends") != null)) {
            log.warn("Warning: meta attribute extends='" +
                getMetaAsString("extends") + "' will be ignored for subclass " +
                name);
        }
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return "ClassMapping: " + name.getFullyQualifiedName();
    }
}
