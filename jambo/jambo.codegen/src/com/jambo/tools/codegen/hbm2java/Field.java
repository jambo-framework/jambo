//$Id: Field.java,v 1.1 2006/09/08 11:25:52 liaojinbo Exp $
package com.jambo.tools.codegen.hbm2java;

import java.util.Collection;
import java.util.Set;


import org.apache.commons.collections.MultiMap;


public class Field {
	private String name = null;
	// private String type = null;
	private String initialisation = null;
	private String asSuffix = null;
	private boolean id = false;
	private boolean generated = false;
	private boolean nullable = true;
	private ClassName classType;
    private MultiMap metaattribs = null;
    private ClassName foreignClass;
    private Set foreignKeys;
    
    public Field(String name, ClassName type, boolean nullable, MultiMap metaattribs) {
        initWith(name, type, name, nullable, id, generated, null, null, metaattribs);
    }
    
    public Field(String name, ClassName type, boolean nullable, boolean id, boolean generated, MultiMap metaattribs) {
        initWith(name, type, initialisation, nullable, id, generated, null, null, metaattribs);
    }
    
    public Field(String name, ClassName type, String initialisation, boolean nullable, ClassName foreignClass, Set foreignKeys, MultiMap metaattribs) {
        initWith(name, type, initialisation, nullable, id, generated, foreignClass, foreignKeys, metaattribs);
    }
    
    protected void initWith(String name, ClassName type, String initialisation, boolean nullable, boolean id, boolean generated, ClassName foreignClass, Set foreignKeys, MultiMap metaattribs) {
        this.name = name;
        setType(type);
        this.initialisation = initialisation;
        this.nullable = nullable;
        this.id = id;
        this.generated = generated;
        this.asSuffix = name.substring(0, 1).toUpperCase() + name.substring(1);
        this.foreignClass = foreignClass;
        this.foreignKeys = foreignKeys;
        setMeta(metaattribs);
        
    }
    
    public String getInitialisation() {
        return this.initialisation;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getAsSuffix() {
        return this.asSuffix;
    }
    
    public String getGetterType() {
		return (getType().toLowerCase().equals("boolean") ) ? "is" : "get";
    }
    
    public String getType() {
        String type = classType.getFullyQualifiedName();
        int loc = type.indexOf("java.lang.");
        if ( loc<0 ) {
            return type;
        }
        else {
            return type.substring(10);
        }
    }
    
    public boolean isIdentifier() {
        return id;
    }
    
    public boolean isNullable() {
        return nullable;
    }
    
    public boolean isGenerated() {
        return generated;
    }

    public String toString() {
        return getType() + ":" + getName();    
    }    
	
	/**
	 * Returns the classType. Can be null as it is not always possible to get
	 * all info for a field class.
	 * @return ClassName
	 */
	public ClassName getClassType() {
		return classType;
	}
	
    private void setType(ClassName type) {
        this.classType = type;
    }
    /**
     * Method setMeta.
     * @param metaForProperty
     */
     private void setMeta(MultiMap multiMap) {
        metaattribs = multiMap;
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
        Collection c= getMeta(attribute);
 
        return MetaAttributeHelper.getMetaAsString(c);
    }

    public boolean getMetaAsBool(String attribute) {
    	return getMetaAsBool(attribute,false);
    }
    
	public boolean getMetaAsBool(String attribute, boolean defaultValue) {
			Collection c= getMeta(attribute);
		
			return MetaAttributeHelper.getMetaAsBool(c,defaultValue);
		}

    /**
     * Returns the foreignClass.
     * @return ClassName
     */
    public ClassName getForeignClass() {
        return foreignClass;
    }

    /**
     * Sets the foreignClass.
     * @param foreignClass The foreignClass to set
     */
    public void setForeignClass(ClassName foreignClass) {
        this.foreignClass = foreignClass;
    }

    /**
     * Returns the foreignKeys.
     * @return Set
     */
    public Set getForeignKeys() {
        return foreignKeys;
    }

	/**
	 * Method getGetterSignature.
	 * @return String
	 */
	public String getGetterSignature() {
		return getGetterType() + getAsSuffix() + "()";
	}

	public boolean isGeneratedAsProperty() {
		return getMetaAsBool("gen-property", true);
	}
}
