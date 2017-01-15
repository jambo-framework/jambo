package com.jambo.jop.common.utils.bean;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaProperty;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 从apache BeanUtils修改过来，了为避免和Apache冲突，改名叫BeanTools
 * <br> Description: class BeanUtils
 * <br> Company: jambo-framework,Guangzhou</br>
 * @author He Kun， jinbo
 * @since 1.0
 * @version 1.0
 * 2006-6-6
 */
public class BeanTools {
	private static Logger log = LoggerFactory.getLogger(BeanTools.class);
	
	public static void copyProperties(Object dest, Object orig) throws IllegalAccessException, InvocationTargetException {
		copyProperties(dest,orig,false);
	}

	public static void copyProperties(Object dest, Object orig,boolean copyNullValue) throws IllegalAccessException, InvocationTargetException {
		  // Validate existence of the specified beans
      if (dest == null) {
          throw new IllegalArgumentException
                  ("No destination bean specified");
      }
      if (orig == null) {
          throw new IllegalArgumentException("No origin bean specified");
      }
      if (log.isDebugEnabled()) {
          log.debug("BeanUtils.copyProperties(" + dest + ", " +
                    orig + ")");
      }

      // Copy the properties, converting as necessary
      if (orig instanceof DynaBean) {
          DynaProperty origDescriptors[] =
              ((DynaBean) orig).getDynaClass().getDynaProperties();
          for (int i = 0; i < origDescriptors.length; i++) {
              String name = origDescriptors[i].getName();
              if (PropertyUtils.isWriteable(dest, name)) {
                  Object value = ((DynaBean) orig).get(name);
                  
                  /*add by hekun & hbm, don't copy null value*/
                  if(copyNullValue==false){
                	  if(value==null||value.toString().trim().length()==0){
                		  continue;
                	  }
                  }
                  /*add by hekun*/
                  org.apache.commons.beanutils.BeanUtils.copyProperty(dest, name, value);
              }
          }
      } else if (orig instanceof Map) {
          Iterator names = ((Map) orig).keySet().iterator();
          while (names.hasNext()) {
              String name = (String) names.next();
              if (PropertyUtils.isWriteable(dest, name)) {
                  Object value = ((Map) orig).get(name);
                  
                  /*add by hekun & hbm, don't copy null value*/
                  if(copyNullValue==false){
                	  if(value==null||value.toString().trim().length()==0){
                		  continue;
                	  }
                  }
                  /*add by hekun*/
                  
                  org.apache.commons.beanutils.BeanUtils.copyProperty(dest, name, value);
              }
          }
      } else /* if (orig is a standard JavaBean) */ {
          PropertyDescriptor origDescriptors[] =
              PropertyUtils.getPropertyDescriptors(orig);
          for (int i = 0; i < origDescriptors.length; i++) {
              String name = origDescriptors[i].getName();
              if ("class".equals(name)) {
                  continue; // No point in trying to set an object's class
              }
              if (PropertyUtils.isReadable(orig, name) &&
                  PropertyUtils.isWriteable(dest, name)) {
                  try {
                      Object value =
                          PropertyUtils.getSimpleProperty(orig, name);
                      if(log.isDebugEnabled())
                    	  log.debug("orig: property name:" + name + " value:" + value);
                      /*add by hekun & hbm, don't copy null value*/
                      if(copyNullValue==false){
                    	  if(value==null||value.toString().trim().length()==0){
                    		  continue;
                    	  }
                      }
                      /*add by hekun*/
                      org.apache.commons.beanutils.BeanUtils.copyProperty(dest, name, value);
                  } catch (NoSuchMethodException e) {
                      ; // Should not happen
                  }
              }
          }
      }
	}
//	 可以使用null进行赴值。""也将示为null
	// xiangyin
	public static void copyvaluewithNULL(Object ob, String property,
			Object value){
		Method[] method = ob.getClass().getMethods();
		try{
		String getmethodname = "set"+capitalize(property);
		Object values[] = new Object[1];
		if(value==null || "".equals(value)) value=null;
        values[0] = value;
		for(int i=0;i<method.length;i++){
			if(getmethodname.equals(method[i].getName())){
				method[i].invoke(ob, values);
				break;
			}
		}
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	public static String capitalize(String name) {
		if (name == null || name.length() == 0) {
			return name;
		}
		return name.substring(0, 1).toUpperCase() + name.substring(1);
	}


	public static void setProperty(Serializable bean, String name, Object value) throws IllegalAccessException, InvocationTargetException {
		org.apache.commons.beanutils.BeanUtils.setProperty(bean, name, value);
	}
	
	 /**
     * Return the entire set of properties for which the specified bean
     * provides a read method.  This map can be fed back to a call to
     * <code>BeanUtils.populate()</code> to reconsitute the same set of
     * properties, modulo differences for read-only and write-only
     * properties, but only if there are no indexed properties.
     *
     * @param bean Bean whose properties are to be extracted
     *
     * @exception IllegalAccessException if the caller does not have
     *  access to the property accessor method
     * @exception java.lang.reflect.InvocationTargetException if the property accessor method
     *  throws an exception
     * @exception NoSuchMethodException if an accessor method for this
     *  property cannot be found
     */
    public static Map describe(Object bean)
            throws IllegalAccessException, InvocationTargetException,
            NoSuchMethodException {

        if (bean == null) {
        //            return (Collections.EMPTY_MAP);
            return (new HashMap());
        }
        
        if (log.isDebugEnabled()) {
            log.debug("Describing bean: " + bean.getClass().getName());
        }
            
        Map description = new HashMap();
        if (bean instanceof DynaBean) {
            DynaProperty descriptors[] =
                ((DynaBean) bean).getDynaClass().getDynaProperties();
            for (int i = 0; i < descriptors.length; i++) {
                String name = descriptors[i].getName();
                description.put(name, getProperty(bean, name));
            }
        } else {
            PropertyDescriptor descriptors[] =
                PropertyUtils.getPropertyDescriptors(bean);
            for (int i = 0; i < descriptors.length; i++) {
                String name = descriptors[i].getName();
                if (descriptors[i].getReadMethod() != null)
                    description.put(name, getProperty(bean, name));
            }
        }
        return (description);

    }
    
    /**
     * Return the value of the specified property of the specified bean,
     * no matter which property reference format is used, as a String.
     *
     * @param bean Bean whose property is to be extracted
     * @param name Possibly indexed and/or nested name of the property
     *  to be extracted
     *
     * @exception IllegalAccessException if the caller does not have
     *  access to the property accessor method
     * @exception java.lang.reflect.InvocationTargetException if the property accessor method
     *  throws an exception
     * @exception NoSuchMethodException if an accessor method for this
     *  property cannot be found
     */
    public static Object getProperty(Object bean, String name)
            throws IllegalAccessException, InvocationTargetException,
            NoSuchMethodException {

        return PropertyUtils.getNestedProperty(bean, name);
    }
    
    /**
     * companyName 转换为 company_name, 
     * @param javaPropertyName
     * @return
     */
    private String toDBStyleName(String javaPropertyName) {
    	if(javaPropertyName== null || javaPropertyName.length() < 2)
    		return javaPropertyName;
    	
    	StringBuffer buffer = new StringBuffer(javaPropertyName.length());
    	
    	buffer.append(javaPropertyName.charAt(0));
    	for (int i = 1; i < javaPropertyName.length(); i++) {
    		char ch = javaPropertyName.charAt(i);
			if( ch >= 'A' && ch <= 'Z') {
				buffer.append("_").append( Character.toLowerCase(ch));
			}else
				buffer.append(ch);
		}
    	return buffer.toString();
    }
}
