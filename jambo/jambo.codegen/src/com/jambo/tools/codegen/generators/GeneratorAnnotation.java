package com.jambo.tools.codegen.generators;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.jambo.tools.codegen.util.StringHelper;
import com.jambo.tools.codegen.util.WriteFile;

/**
 * 使用HBM的XML，重新修改java文件，生成POJO的注解
 * 
 * @author jinbo
 */
public class GeneratorAnnotation {
	private File hbmFolder;
	private String pojoClassName;
	
	/**
	 * 使用HBM的XML，重新修改java文件，生成POJO的注解
	 * @param d
	 */
	public void generateAnnotation(Document mapping, File srcFolder, String hbmPkg, String pojoClassName) {
		this.pojoClassName = pojoClassName;
		hbmFolder = new File(srcFolder, hbmPkg.replace(StringHelper.DOT,
				File.separatorChar));
		
		try {
			File pojo = new File(hbmFolder, pojoClassName + ".java");
			StringBuffer sb = WriteFile.read(new FileInputStream(pojo));

			addImport(sb);
			addEntity(mapping, sb);
			System.out.println("....");
			addID(mapping, sb);
			System.out.println("....");
			addProps(mapping, sb);
			System.out.println("....");
			
			if (pojo.exists()) pojo.delete();
			WriteFile.write(pojo.getPath(), sb);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void addImport(StringBuffer sb) {
		int idx = sb.indexOf("import java.io.Serializable;");
		if (idx > 0){
			sb = sb.insert(idx, "import javax.persistence.*;\n");
		}		
	}

	private void addEntity(Document mapping, StringBuffer sb) {
		NodeList classes = mapping.getElementsByTagName("class");
		if (classes != null){
			//只能处理一个classes
			Element cls = (Element) classes.item(0);
			
			String name = pojoClassName;
			String table = cls.getAttribute("table");
			
			int idx = sb.indexOf("public class " + name);
			if (idx > 0){
				sb = sb.insert(idx, "@Table (name=\""+table+"\")\n");
				sb = sb.insert(idx, "@Entity\n");
			}
		}
	}

	private void addProps(Document mapping, StringBuffer sb) {
		NodeList props = mapping.getElementsByTagName("property");
		//只能处理一个id
		for (int i = 0; i < props.getLength(); i++) {
			Element element = (Element) props.item(i);

			addMapping(sb, element, false);
		}
	}

	private int addMapping(StringBuffer sb, Element element, boolean isKey) {
		String name = element.getAttribute("name");
		String type = element.getAttribute("type");
		if (!type.equals("java.util.Date")){
			String[] types = type.split("\\.");
			if (types.length > 1)
				type = types[types.length - 1];
			else 
				type = element.getAttribute("type");
		}
		String len = element.getAttribute("length");
		String notNull = element.getAttribute("not-null");
			
		
		int idx = sb.indexOf("private " + type + " " + name);
		if (idx > 0){
			String str = "@Column (name=\""+element.getAttribute("column") + "\"";

			if (len != null && len.length() > 1){
				str = str + ", length = " + len;
			} 
			
			if (notNull != null && notNull.length() > 1){
				str = str + ", nullable  = " + notNull.equals("false");
			} 
			
			if (isKey){
				str = str + ", unique  = true";
			} 
			
			str = str + ")\n\t\t";
		
			sb = sb.insert(idx,str); 
		}
		return idx;
	}

	private Element findFistNode(Document mapping, String name){
		Element node = null;
		NodeList list = mapping.getElementsByTagName(name);
		if (list != null){
			node = (Element)list.item(0);
		}
		return node;
	}
	
	private void addID(Document mapping, StringBuffer sb) {
		Element id = findFistNode(mapping, "id");
		if (id != null){
			int idx = addMapping(sb, id, true);
			if (idx > 0){
				Element gen = findFistNode(mapping, "generator");
				if (gen != null){
					String genClass = gen.getAttribute("class");
					
					if (genClass != null) {
						if (genClass.equals("sequence")){
							Element param = findFistNode(mapping, "param");
							if (param != null) {
								String seq = param.getTextContent();
							
								sb = sb.insert(idx, "@SequenceGenerator (name=\""+ seq +"\", sequenceName=\""+ seq +"\")\n\t\t");
								sb = sb.insert(idx, "@GeneratedValue (strategy = GenerationType.SEQUENCE,generator=\""+ seq +"\"\n\t\t");
							}
						} else if (genClass.equals("assigned")) {
							//额，这个不需要设置注解
						}
					}
				}
				sb = sb.insert(idx, "@Id\n\t\t");
			}
		}

		//复合主键
		id = findFistNode(mapping, "composite-id");
		if (id != null){
			NodeList ids = mapping.getElementsByTagName("key-property");
			for (int i = 0; i < ids.getLength(); i++){
				id = (Element)ids.item(i);
				
				int idx = addMapping(sb, id, true);
				if (idx > 0){
					sb = sb.insert(idx, "@Id\n\t\t");
				}
			}
		}
	}

}
