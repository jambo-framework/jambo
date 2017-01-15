package com.jambo.jop.infrastructure.component.impl;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.*;


public class Code2NameConfiger {

	
	private static final Logger logger = LoggerFactory.getLogger(Code2NameConfiger.class);
	
	public static CacheManager cacheManager;
	public static Cache code2nameCache;
	
	static public HashMap sysMap = new HashMap();
	static public HashMap localMap  = new HashMap();
	
	private static String CODE2NAME_CONFIG_PATH = "/data/code2name.xml";
	
	static {
		init();
	}

	public static String getCodeProperty( String definition ) throws Exception {
		Node node = (Node) sysMap.get(definition);
		return node.getCode();
	}
	
	public static String getNameProperty( String definition ) throws Exception {
		Node node = (Node) sysMap.get(definition);
		return node.getName();
	}

	// 下面注释描述的层是XML文件里面嵌套的层次
	final static String SYSCODE_CONFIG_0 = "syscode-config";// 第零层

	final static String SYSCODE_DYNAMIC_1 = "syscode-dynamic"; // 第一层

	final static String SYSCODE_LOCAL_1 = "syscode-local"; // 第一层

	final static String DEFINITION_2 = "definition"; // 第二层

	final static String VALUE_OBJECT_2 = "value-object"; // 第二层

	final static String CODE_2 = "code"; // 第二层

	final static String NAME_2 = "name"; // 第二层
	
	final static String ITEMS_2 = "items";// 第二层
	
	final static String ITEMVALUE_3 = "itemvalue";// 第三层

	final static String ITEMVALUE_3_CODE = "code";// 第三层itemvalue的属性
//	
	private static void init() {
		sysMap = new HashMap();

		SAXReader saxReader = new SAXReader();
		try {
			InputStream in = Code2NameConfiger.class.getResourceAsStream(CODE2NAME_CONFIG_PATH);
			if(in == null)
				throw new IllegalArgumentException("Can't find code2name.xml file, location: " + CODE2NAME_CONFIG_PATH);
			Document document = saxReader.read(in);

			List dynamicList = document.selectNodes(SYSCODE_CONFIG_0 + "/" + SYSCODE_DYNAMIC_1);
			Iterator iter = dynamicList.iterator();
			while (iter.hasNext()) {
				Element ele = (Element) iter.next();
				String key = ele.element(DEFINITION_2).getStringValue().trim(); //注意，要去除空格
				Node node = new Node();
				node.setValueObject(ele.element(VALUE_OBJECT_2).getStringValue().trim());
				node.setCode(ele.element(CODE_2).getStringValue().trim());
				node.setName(ele.element(NAME_2).getStringValue().trim());
				sysMap.put(key, node);
			}
			
			List locallist = document.selectNodes(SYSCODE_CONFIG_0 + "/" + SYSCODE_LOCAL_1);
			localMap = new HashMap();
	        Iterator iterl = locallist.iterator();
	        while(iterl.hasNext()){
	            Element localElement = (Element)iterl.next();
	            LocalNote local = new LocalNote();
	            Element definition = localElement.element(DEFINITION_2);
	            local.definition = definition.getTextTrim();//注意，要去除空格
	            Iterator iterItems = localElement.element(ITEMS_2).elementIterator(ITEMVALUE_3);
	            Map itemvalue = new LinkedHashMap();
	            while( iterItems.hasNext() ){
	            	Element item = (Element)iterItems.next();
	            	itemvalue.put( item.attribute(ITEMVALUE_3_CODE).getValue() ,item.getTextTrim() );
	            }
	            local.items = itemvalue;
	            localMap.put(local.definition, local);
	            logger.info("loaded local syscode :" + local.definition );
	        }

		} catch (DocumentException e) {
			logger.error("code2name config init error", e);
			e.printStackTrace();
		}
	}
}
