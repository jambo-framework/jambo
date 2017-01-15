package com.jambo.tools.codegen.hbm2java;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;



import org.apache.commons.collections.MultiHashMap;
import org.apache.commons.collections.MultiMap;
import org.jdom.Element;

import com.jambo.tools.codegen.util.StringHelper;


/**
 * Helper for loading, merging  and accessing <meta> tags.
 * 
 * @author max
 *
 * 
 */
public class MetaAttributeHelper {

	static class MetaAttribute {
		String value;
		boolean inheritable = true;

		MetaAttribute(String value, boolean inherit) {
			this.value = value;
			this.inheritable = inherit;
		}

		public String toString() {
			return value;
		}
	}

	/**
	 * Load meta attributes from jdom element into a MultiMap.
	 * 
	 * @param element
	 * @return MultiMap
	 */
//	@SuppressWarnings("unchecked")
	static protected MultiMap loadMetaMap(Element element) {
		MultiMap result = new MultiHashMap();
		List metaAttributeList = new ArrayList();
		metaAttributeList.addAll(element.getChildren("meta"));

		for (Iterator iter = metaAttributeList.iterator(); iter.hasNext();) {
			Element metaAttrib = (Element) iter.next();
			// does not use getTextNormalize() or getTextTrim() as that would remove the formatting in new lines in items like description for javadocs.
			String attribute = metaAttrib.getAttribute("attribute").getValue();
			String value = metaAttrib.getText();
			boolean inherit =
				Boolean
					.valueOf(metaAttrib.getAttributeValue("inherit"))
					.booleanValue();
			MetaAttribute ma = new MetaAttribute(value, inherit);
			result.put(attribute, ma);
		}
		return result;

	}

	/**
	 * Merges a Multimap with inherited maps.
	 * Values specified always overrules/replaces the inherited values.
	 * 
	 * @param local
	 * @param inherited
	 * @return a MultiMap with all values from local and extra values
	 * from inherited
	 */
	static public MultiMap mergeMetaMaps(MultiMap local, MultiMap inherited) {
		MultiHashMap result = new MultiHashMap();

		if (inherited != null) {
			result.putAll(local);
			for (Iterator iter = inherited.keySet().iterator();
				iter.hasNext();
				) {
				String key = (String) iter.next();

				if (!local.containsKey(key)) {
					// inheriting a meta attribute only if it is inheritable
					ArrayList ml = (ArrayList) inherited.get(key);
					for (Iterator iterator = ml.iterator();
						iterator.hasNext();
						) {
						MetaAttribute element = (MetaAttribute) iterator.next();
						if (element.inheritable) {
							result.put(key, element);
						}
					}
				}
			}
		}

		return result;

	}
	/**
	 * Method loadAndMergeMetaMap.
	 * @param classElement
	 * @param inheritedMeta
	 * @return MultiMap
	 */
	public static MultiMap loadAndMergeMetaMap(
		Element classElement,
		MultiMap inheritedMeta) {
		return mergeMetaMaps(loadMetaMap(classElement), inheritedMeta);
	}

	/**
	 * @param collection
	 * @param string
	 */
	public static String getMetaAsString(Collection meta, String seperator) {
		StringBuffer buf = new StringBuffer();
			for (Iterator iter = meta.iterator(); iter.hasNext();) {				
				buf.append(seperator);				
				buf.append(iter.next());
			}
		return buf.toString();
	}

	static	boolean getMetaAsBool(Collection c, boolean defaultValue) {
			if(c==null || c.isEmpty()) {
				return defaultValue;
			} else {
				return Boolean.valueOf(c.iterator().next().toString()).booleanValue();
			}
		}

	static String getMetaAsString(Collection c) {
			if(c==null || c.isEmpty()) {
		        return StringHelper.EMPTY_STRING;
		    } else {
		    StringBuffer sb = new StringBuffer();
		  for (Iterator iter = c.iterator(); iter.hasNext();) {
		    Object element = (Object) iter.next();
		    sb.append(element.toString());
		}   
		    return sb.toString();
		}
	}

}
