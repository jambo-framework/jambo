//$Id: BasicRenderer.java,v 1.1 2006/09/08 11:25:52 liaojinbo Exp $
package com.jambo.tools.codegen.hbm2java;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.jambo.tools.codegen.ddl2hbm.JDBCUtil;
import com.jambo.tools.codegen.util.StringHelper;



//@SuppressWarnings("unchecked")
public class BasicRenderer extends AbstractRenderer {
	
	static final protected int ORDINARY = 0;
	static final protected int BOUND = 1;
	static final protected int CONSTRAINT = 3;//any constraint properties are bound as well


 	public void render(String savedToPackage, String savedToClass, ClassMapping classMapping, Map class2classmap, PrintWriter mainwriter) throws Exception {
        if (savedToPackage!=null && !savedToPackage.trim().equals("")) {
                    mainwriter.println("package " + savedToPackage + ";");
        } else if ( classMapping.getGeneratedPackageName()!=null ) {
        	mainwriter.println("package " + classMapping.getGeneratedPackageName() + ";");
        }
        else {
        	mainwriter.println("// default package");
        }
        mainwriter.println();
    
        // switch to another writer to be able to insert the actually
        // used imports when whole class has been rendered. 
        StringWriter strWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(strWriter);
        
    
        // class declaration
        if(classMapping.getMeta("class-description")==null) {
            writer.println("/** @author Hibernate CodeGenerator */");
        } else {
            writer.println("/** \n" + toJavaDoc(classMapping.getMetaAsString("class-description"),0)  + "*/");   
        }
        
        String classScope = "public";
        if(classMapping.getMeta("scope-class")!=null) {
            classScope = classMapping.getMetaAsString("scope-class").trim();
        }
        if(classMapping.shouldBeAbstract() && (classScope.indexOf("abstract")==-1)) {
            writer.print( "abstract " + classScope + " class " + savedToClass );
        } else {
            writer.print( classScope + " class " + savedToClass );    
        }
    
        // subclass
        if (classMapping.getSuperClass() != null) {
            writer.print( " extends " + classMapping.getSuperClass() );
        } else if (classMapping.getMeta("extends")!=null) {
            writer.print( " extends " + classMapping.getMetaAsString("extends"));
        }
    
        // always implements Serializable
        writer.print(" extends BaseVO implements Serializable");
        
    	// implement proxy, but NOT if the proxy is the class it self!
    	if ( 
    		classMapping.getProxy()!=null && 
    		( !classMapping.getProxy().equals( classMapping.getCanonicalName() ) )
    	) {
        	writer.print(StringHelper.COMMA_SPACE);
        	writer.print( classMapping.getProxy() );
        }
        
        if(classMapping.getMeta("implements")!=null) {
        	writer.print(MetaAttributeHelper.getMetaAsString(classMapping.getMeta("implements"),", "));            
        }
        
        writer.println(" {");
        writer.println();
        
        // switch to another writer to be able to insert the 
        // veto- and changeSupport fields
        StringWriter strPropWriter = new StringWriter();
        PrintWriter propWriter = new PrintWriter(strPropWriter);

        doFields(classMapping, propWriter);
        
        doConstructors(savedToClass, classMapping, class2classmap, propWriter);
       
        String vetoSupport = makeSupportField("vetos", classMapping.getAllFields());
        String changeSupport = makeSupportField("changes", classMapping.getAllFields());    
        int fieldTypes = doFieldAccessors(classMapping, class2classmap, propWriter, vetoSupport, changeSupport);
        doSupportMethods(fieldTypes, vetoSupport, changeSupport, propWriter);
            
        doToString(classMapping, propWriter);
        
       	doEqualsAndHashCode(savedToClass,classMapping, propWriter);
    	
        propWriter.println("}");
        
        //insert change and VetoSupport
        doSupports(fieldTypes, classMapping, vetoSupport, changeSupport, writer);
        
        writer.print(strPropWriter.toString());
        
        // finally write the imports
        doImports(classMapping, mainwriter);
        mainwriter.print(strWriter.toString());
        
        
    }

	/**
	 * Method doSupportMethods.
	 * @param fieldTypes
	 * @param vetoSupport
	 * @param changeSupport
	 * @param propWriter
	 */
	private void doSupportMethods(
		int fieldTypes,
		String vetoSupport,
		String changeSupport,
		PrintWriter writer) {
			if((fieldTypes&CONSTRAINT)==CONSTRAINT) {
				writer.println("    public void addVetoableChangeListener( VetoableChangeListener l ) {");
				writer.println("        "+vetoSupport+".addVetoableChangeListener(l);");
				writer.println("    }");
				writer.println("    public void removeVetoableChangeListener( VetoableChangeListener l ) {");
				writer.println("        "+vetoSupport+".removeVetoableChangeListener(l);");
				writer.println("    }");
				writer.println();
			}
			if((fieldTypes&BOUND)==BOUND) {
				writer.println("    public void addPropertyChangeListener( PropertyChangeListener l ) {");
				writer.println("        "+changeSupport+".addPropertyChangeListener(l);");
				writer.println("    }");
				writer.println("    public void removePropertyChangeListener( PropertyChangeListener l ) {");
				writer.println("        "+changeSupport+".removePropertyChangeListener(l);");
				writer.println("    }");
				writer.println();
			}
	}


	/**
	 * Method doSupports.
	 * @param vetoSupport
	 * @param changeSupport
	 * @param writer
	 */
//	@SuppressWarnings("unchecked")
	private void doSupports( int fieldTypes, 
											ClassMapping classMapping,
											String vetoSupport,
											String changeSupport,
											PrintWriter writer) {
			if((fieldTypes&CONSTRAINT)==CONSTRAINT) {
				writer.println( "    private VetoableChangeSupport "+vetoSupport+
					" = new VetoableChangeSupport(this);" );
				classMapping.getImports().add("java.beans.VetoableChangeSupport");
				classMapping.getImports().add("java.beans.PropertyVetoException");
				classMapping.getImports().add("java.beans.VetoableChangeListener");
			}
			if((fieldTypes&BOUND)==BOUND) {
				writer.println( "    private PropertyChangeSupport "+changeSupport+
					" = new PropertyChangeSupport(this);" );
				writer.println();
				classMapping.getImports().add("java.beans.PropertyChangeSupport");
				classMapping.getImports().add("java.beans.PropertyChangeListener");
			}
	}


	public void doConstructors(String savedToClass, ClassMapping classMapping, Map class2classmap, PrintWriter writer) {
		// full constructor
		List allFieldsForFullConstructor = classMapping.getAllFieldsForFullConstructor();
		
		writer.println("    /** full constructor */");
		String fullCons = "    public " + savedToClass + StringHelper.OPEN_PAREN;
		
		
		for(Iterator fields = allFieldsForFullConstructor.iterator(); fields.hasNext();) {
		    Field field = (Field) fields.next();
		        fullCons = fullCons + shortenType(getTrueTypeName(field, class2classmap), classMapping.getImports()) + " " + field.getName();
		        if(fields.hasNext()) {
		          fullCons = fullCons + ", ";
		        }
		}
		
		writer.println(fullCons + ") {");
		//invoke super to initialize superclass...
		List supersConstructorFields = classMapping.getFieldsForSupersFullConstructor();
		if (!supersConstructorFields.isEmpty()) {
		    writer.print("        super(");
		    for (Iterator fields = supersConstructorFields.iterator(); fields.hasNext();) {
		        Field field = (Field) fields.next();
		        writer.print(field.getName());
		        if(fields.hasNext()) {
		            writer.print(", ");
		        }
		    }
		    writer.println(");");
		}
		
		// initialisation of localfields
		for(Iterator fields = classMapping.getLocalFieldsForFullConstructor().iterator(); fields.hasNext();) {
		    Field field = (Field) fields.next();
		    if(field.isGeneratedAsProperty()) {
		    	writer.println("        this." + field.getName() + " = " + field.getName() + ";");
		    }
		}
		writer.println("    }");
		writer.println();
		
		// no args constructor (if fullconstructor had any arguments!)
		if (allFieldsForFullConstructor.size() > 0) {
		    writer.println("    /** default constructor */");
			writer.println("    public " + savedToClass + "() {");
			writer.println("    }");
			writer.println();
		}
		
		// minimal constructor (only if the fullconstructor had any arguments)
		if ((allFieldsForFullConstructor.size() > 0) && classMapping.needsMinimalConstructor()) {
		
		    List allFieldsForMinimalConstructor = classMapping.getAllFieldsForMinimalConstructor();
		    writer.println("    /** minimal constructor */"); 
		
		    String minCons = "    public " + savedToClass + "(";
		    for (Iterator fields = allFieldsForMinimalConstructor.iterator(); fields.hasNext();) {
		        Field field = (Field) fields.next();
		        minCons = minCons + shortenType(getTrueTypeName(field, class2classmap), classMapping.getImports()) + " " + field.getName();
		        if (fields.hasNext()) {
		            minCons = minCons + ", ";
		        }
		    }
		
		    writer.println(minCons + ") {");
		    // invoke super to initialize superclass...
		          List supersMinConstructorFields = classMapping.getFieldsForSupersMinimalConstructor();
		          if (!supersMinConstructorFields.isEmpty()) {
		              writer.print("      super(");
		              for (Iterator fields = supersMinConstructorFields.iterator(); fields.hasNext();) {
		                  Field field = (Field) fields.next();
		                  writer.print(field.getName());
		                  if(fields.hasNext()) {
		                      writer.print(StringHelper.COMMA_SPACE);
		                  }
		              }
		              writer.println(");");
		          }
		
		    // initialisation of localfields
		    for (Iterator fields = classMapping.getLocalFieldsForMinimalConstructor().iterator(); fields.hasNext();) {
		        Field field = (Field) fields.next();
		        if(field.isGeneratedAsProperty()) {
		        	writer.println("        this." + field.getName() + " = " + field.getName() + ";");
		        }
		    }
		    writer.println("    }");
		    writer.println();
		}
	}

	public void doFields(ClassMapping classMapping, PrintWriter writer) {
		int i = 0;
		// fields
		for ( Iterator fields = classMapping.getFields().iterator(); fields.hasNext(); ) {
		    Field field = (Field) fields.next();
		    
			JDBCUtil.Column column = (JDBCUtil.Column) this.columnList.get(i);
		    String comm = column.comments;
		    if (comm==null) comm = "";
		    
		    if(field.isGeneratedAsProperty()) {
		    String fieldScope = getFieldScope(field, "scope-field", "private");
		    writer.println( 
		    	"    /** " + 
		    	( field.isNullable() && !field.isIdentifier() ? "nullable " : StringHelper.EMPTY_STRING ) +
		    	( field.isIdentifier() ? "identifier" : "persistent " ) + comm 
		    	+ " field */");
		    writer.println(
		    	"    " + fieldScope + " " + 
		    	shortenType( field.getType(), classMapping.getImports() ) + 
		    	' ' + 
		    	field.getName() +
		    	';'
		    );
		    }
			writer.println();
			
			i = i +1;
		}
	}

	public void doEqualsAndHashCode(String savedToClass, ClassMapping classMapping, PrintWriter writer) {
		 if ( classMapping.mustImplementEquals() ) {
		    	writer.println("    public boolean equals(Object other) {");
		    	writer.println("        if ( !(other instanceof " + savedToClass + ") ) return false;");
		    	writer.println("        " + savedToClass + " castOther = (" + savedToClass + ") other;");
		    	writer.println("        return new EqualsBuilder()");
		    	for (Iterator fields = classMapping.getFields().iterator(); fields.hasNext();) {
		    		Field field = (Field) fields.next();
		    		if ( field.isIdentifier() ) {
		    			writer.println("            .append(this." + field.getGetterSignature() + ", castOther." + field.getGetterSignature() + StringHelper.CLOSE_PAREN);
		    		}
		    	}
		    	writer.println("            .isEquals();");
		    	writer.println("    }");
				writer.println();
				
		    	writer.println("    public int hashCode() {");
		    	writer.println("        return new HashCodeBuilder()");
		    	for (Iterator fields = classMapping.getFields().iterator(); fields.hasNext();) {
		    		Field field = (Field) fields.next();
		    		if ( field.isIdentifier() ) {
		    			writer.println("            .append(" + field.getGetterSignature() + StringHelper.CLOSE_PAREN);
		    		}
		    	}
		    	writer.println("            .toHashCode();");
		    	writer.println("    }");
				writer.println();
		    }
	}

	public void doToString(ClassMapping classMapping, PrintWriter writer) {
		
		writer.println("    public String toString() {");
		writer.println("        return new ToStringBuilder(this)");
		for (Iterator fields = classMapping.getAllFields().iterator(); fields.hasNext();) {
			Field field = (Field) fields.next();
			if (field.isIdentifier() || field.getMetaAsBool("use-in-tostring")) {
				writer.println("            .append(\"" + field.getName() + "\", " + field.getGetterSignature() + ")");
			}
		}
		writer.println("            .toString();");
		writer.println("    }");
		writer.println();
		
	}
	
	static Map primitiveToObject = new HashMap();
	{
	  primitiveToObject.put("char", "Character");
	  
	  primitiveToObject.put("byte", "Byte");
	  primitiveToObject.put("short", "Short");
	  primitiveToObject.put("int",  "Integer");
	  primitiveToObject.put("long", "Long");
	  
	  primitiveToObject.put("boolean", "Boolean");
	  
	  primitiveToObject.put("float", "Float");
	  primitiveToObject.put("double", "Double");
	    	
	}
	
    public int doFieldAccessors(ClassMapping classMapping, 
    											Map class2classmap, 
    											PrintWriter writer,
    											String vetoSupport,
    											String changeSupport) {
    	int fieldTypes=ORDINARY;
        // field accessors
        for (Iterator fields = classMapping.getFields().iterator(); fields.hasNext();) {
            Field field = (Field) fields.next();
            if(field.isGeneratedAsProperty()) {
        
            // getter
            String getAccessScope = getFieldScope(field, "scope-get", "public");
            
        
            if(field.getMeta("field-description")!=null) {
            writer.println("    /** \n" + toJavaDoc(field.getMetaAsString("field-description"), 4) + "     */"); 
            }
            writer.println("    " + getAccessScope + " " + getTrueTypeName(field, class2classmap) + " " + field.getGetterSignature() + " {");
            writer.println("        return this." + field.getName() + ";");
            writer.println("    }");
            writer.println();
        
            // setter
            int fieldType=0;
            if(field.getMeta("beans-property-type")!=null) {
            	String beansPropertyType = field.getMetaAsString("beans-property-type").trim().toLowerCase();
            	if(beansPropertyType.equals("constraint") ) {
            		fieldTypes = (fieldTypes | CONSTRAINT);
            		fieldType = CONSTRAINT;
            	}
            	else if(beansPropertyType.equals("bound") ) {
            		fieldTypes = (fieldTypes | BOUND);
            		fieldType = BOUND;
            	}
            }
            String setAccessScope = getFieldScope(field, "scope-set", "public");
            writer.print("    " + setAccessScope + " void set" + field.getAsSuffix() + StringHelper.OPEN_PAREN + getTrueTypeName(field, class2classmap) + " " + field.getName() + ")");
            writer.println((fieldType&CONSTRAINT)==CONSTRAINT ? " throws PropertyVetoException {" : " {");
			if((fieldType&CONSTRAINT)==CONSTRAINT) {
            		writer.println("        "+vetoSupport+".fireVetoableChange(\""+field.getName()+"\",");
            		writer.println("                "+getFieldAsObject(true, field)+",");
            		writer.println("                "+getFieldAsObject(false, field)+");");
			}
            writer.println("        this." + field.getName() + " = " + field.getName() + ";");
            if((fieldType&BOUND)==BOUND) {
            		writer.println("        "+changeSupport+".firePropertyChange(\""+field.getName()+"\",");
            		writer.println("                "+getFieldAsObject(true, field)+",");
            		writer.println("                "+getFieldAsObject(false, field)+");");
            }
            writer.println("    }");
            writer.println();
            
            // add/remove'rs (commented out for now)
            /* 
            if(field.getForeignClass()!=null) { 
                ClassName foreignClass = field.getForeignClass();
                
                String trueforeign = getTrueTypeName(foreignClass, class2classmap);
                classMapping.addImport(trueforeign);
                
                // Try to identify the matching set method on the child.
                ClassMapping forignMap = (ClassMapping) class2classmap.get(foreignClass.getFullyQualifiedName());
                
                if(forignMap!=null) {
                  Iterator foreignFields = forignMap.getFields().iterator();
                  while (foreignFields.hasNext()) {
                    Field ffield = (Field) foreignFields.next();
                    if(ffield.isIdentifier()) {
                       log.debug("Trying to match " + ffield.getName() + " with " + field.getForeignKeys());   
                    }
                }
                  
                } else {
                  log.error("Could not find foreign class's mapping - cannot provide bidirectional setters!");   
                }
                
                String addAccessScope = getFieldScope(field, "scope", "scope-add");
                writer.println("    " + setAccessScope + " void add" + field.getAsSuffix() + StringHelper.OPEN + shortenType(trueforeign, classMapping.getImports()) + " a" + field.getName() + ") {");
                writer.println("        this." + getterType + field.getAsSuffix() + "().add(a" + field.getName() + ");");
                writer.println("        a" + field.getName() + ".setXXX(this);");
                writer.println("    }");
                writer.println();
            
            
            }
            */
        }
        }
        return fieldTypes;
        
    }

    public void doImports(ClassMapping classMapping, PrintWriter writer) {
           // imports
            classMapping.getImports().add("java.io.Serializable");
            classMapping.getImports().add("org.apache.commons.lang.builder.ToStringBuilder");
            if ( classMapping.mustImplementEquals() ) {
            	classMapping.getImports().add("org.apache.commons.lang.builder.EqualsBuilder");
            	classMapping.getImports().add("org.apache.commons.lang.builder.HashCodeBuilder");
            }
            classMapping.getImports().add("com.jambo.jop.infrastructure.db.BaseVO");
        
            for ( Iterator imports = classMapping.getImports().iterator(); imports.hasNext(); ) {
                writer.println("import " + imports.next() + ";");
            }
            writer.println();
    }
    
    protected String makeSupportField(String fieldName, List fieldList) {
		String suffix = "";
		boolean needSuffix = false;
		for (Iterator fields = fieldList.iterator(); fields.hasNext();) {
			String name = ((Field) fields.next()).getName();
			if (name.equals(fieldName))
				needSuffix = true;
			suffix += name;
		}
		return needSuffix ? fieldName + "_" + suffix : fieldName;
	}
	
	private String getFieldAsObject(boolean prependThis, Field field) {
				ClassName type = field.getClassType();			
				if(type != null && type.isPrimitive() && !type.isArray()) {
						String typeName = (String) primitiveToObject.get(type.getName());
						typeName = "new "+typeName+"( ";
						typeName += prependThis ? "this." : "";
						return typeName+field.getName()+" )";        			
				}
				return field.getName();
		}

}
