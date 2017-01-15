package com.jambo.tools.codegen.generators;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.collections.MultiHashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.jambo.tools.codegen.ddl2hbm.JDBCUtil;
import com.jambo.tools.codegen.hbm2java.ClassMapping;
import com.jambo.tools.codegen.hbm2java.ClassName;
import com.jambo.tools.codegen.hbm2java.Generator;
import com.jambo.tools.codegen.util.Constants;
import com.jambo.tools.codegen.util.StringHelper;
import com.jambo.tools.codegen.util.ValueStore;

/**
 * 生成 hibernate的pojo与hbm
 * @author jinbo
 */
public class MapGenerator {
	private static Log logger = LogFactory.getLog(MapGenerator.class);
	private String tableName;
	private String hbmPkg;
	private String hbmFileName;
	private String daoPkg;
	private String poClassName;
	private boolean javaTypes;
	private boolean reservePrefix;
	private String generator;
	private String idType;
	private String[] generatorParameters;
	private String catalog;
	private String schemaPattern;
	private String[] tableTypes = Constants.tableTypes;
	private File srcFolder;
	private String idName;
	private DocumentBuilder docBuilder;
	private TransformerFactory tFactory;
	private boolean generateSource = true;
	private String baseClass;
	private org.jdom.input.DOMBuilder jdomBuilder;
	private final boolean singleMapFile = true;
	//2013-4-28 jinbo 保存字段列表,在写VO时读取注释内容
	List columnList = null;

	/** Creates a new instance of SchemaExport */
	public MapGenerator() {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			docBuilder = dbf.newDocumentBuilder();
			tFactory = TransformerFactory.newInstance();
			// jdk1.4不支持indent-number属性,因此去掉,
			// TODO: 以后版本改用其它的xml解析器试试
			// tFactory.setAttribute("indent-number", new Integer(2));
			jdomBuilder = new org.jdom.input.DOMBuilder();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	public void generate(Connection c) throws IOException, SQLException,
			Exception {
		Document d = getMap(c);
		writeMapping(d);
		generateCode(d);
		
		//2015.3.25 生成POJO的注解
		if (ValueStore.isAnnotation){
			GeneratorAnnotation AnnoGen = new GeneratorAnnotation(); 
			AnnoGen.generateAnnotation(d, getSrcFolder(), getHbmPkg(), this.getPoClassName());
		}
	}

	public Document getMap(Connection c) throws IOException, SQLException {
		Document hbm = createMappingDoc();

		// 这里应该是从数据库中读取全部的table,应该是修改过的代码,连log都没改.
		// String[] tableNames = getTableNames();
		//
		// for (int i = 0; (tableNames != null) && (i < tableNames.length); i++)
		// {
		// //添加表的所有信息到Document
		addHibernateClass(c, tableName, hbm.getDocumentElement());

		// }
		return hbm;
	}

	public void generateCode(Document doc) throws Exception {
		Map map = new HashMap();
		NodeList classes = doc.getElementsByTagName("class");

		if ((classes == null) || (classes.getLength() == 0)) {
			return;
		}

		for (int i = 0; i < classes.getLength(); i++) {
			Element classElement = (Element) classes.item(i);
			ClassMapping cmap = null;

			if (getBaseClass() != null) {
				ClassName cName = new ClassName();
				cName.setFullyQualifiedName(getBaseClass());
				cmap = new ClassMapping(cName, jdomBuilder.build(classElement),
						new MultiHashMap());
			} else {
				cmap = new ClassMapping(jdomBuilder.build(classElement),
						new MultiHashMap());
			}

			map.put(cmap.getCanonicalName(), cmap);
		}

		// generate source files
		Generator g = new Generator();
		g.setBaseDirName(getSrcFolder().getAbsolutePath());
		//2013-4-28 jinbo 为了不影响原来的代码,只好把字段列表这样一路传进去
		g.columnList = this.columnList;
		g.generate(map);
	}

	protected void writeMapping(Document mapping) throws IOException {
		hbmPkg = getHbmPkg();
		File hbmFolder = new File(srcFolder, hbmPkg.replace(StringHelper.DOT,
				File.separatorChar));

		if (!hbmFolder.exists()) {
			hbmFolder.mkdirs();
		}

		if (singleMapFile) {
			File mappingFile = new File(hbmFolder, hbmFileName);
			FileOutputStream fos = new FileOutputStream(mappingFile);

			try {
				logger.info("writing to file " + mappingFile);
				writeHbm(mapping, fos);
			} finally {
				fos.close();
			}
		} else {
			NodeList classes = mapping.getElementsByTagName("class");

			for (int i = 0; i < classes.getLength(); i++) {
				Element classElement = (Element) classes.item(i);

				// 新建一个同样的document,只把一个class元素的内容导入
				Document hibDoc = createMappingDoc();
				classElement = (Element) hibDoc.importNode(classElement, true);
				hibDoc.getDocumentElement().appendChild(classElement);

				File mappingFile = new File(hbmFolder,
						makeEntityName(classElement.getAttribute("class"))
								+ "hbm.xml");
				logger.info("writing to file " + mappingFile);

				FileOutputStream fos = new FileOutputStream(mappingFile);

				try {
					writeHbm(hibDoc, fos);
				} finally {
					fos.close();
				}
			}
		}
	}

	protected void writeHbm(Node node, OutputStream os) {
		try {
			// Use a Transformer for output
			Transformer transformer = tFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer
					.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM,
							"http://hibernate.sourceforge.net/hibernate-mapping-3.0.dtd");
			transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC,
					"-//Hibernate/Hibernate Mapping DTD 3.0//EN");

			DOMSource source = new DOMSource(node);
			StreamResult result = new StreamResult(new OutputStreamWriter(os));
			transformer.transform(source, result);
		} catch (TransformerConfigurationException tce) {
			logger.error("Transformation configuration error", tce);
			throw new RuntimeException(tce.getMessage());
		} catch (TransformerException te) {
			// Error generated by the parser
			logger.error("Transformation error", te);
			throw new RuntimeException(te.getMessage());
		}
	}

	protected String getXml(Node node) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			writeNode(node, baos);
			baos.close();

			return baos.toString();
		} catch (IOException ioe) {
			throw new RuntimeException(ioe.getMessage());
		}
	}

	protected void writeNode(Node node, OutputStream os) {
		try {
			// Use a Transformer for output
			Transformer transformer = tFactory.newTransformer();
			DOMSource source = new DOMSource(node);
			StreamResult result = new StreamResult(os);
			transformer.transform(source, result);
		} catch (TransformerConfigurationException tce) {
			logger.error("Transformation configuration error", tce);
			throw new RuntimeException(tce.getMessage());
		} catch (TransformerException te) {
			// Error generated by the parser
			logger.error("Transformation error", te);
			throw new RuntimeException(te.getMessage());
		}
	}

	/**
	 * 创建Hibernate mapping的Document<br>
	 * 只创建了一个hibernate-mapping的元素.
	 * 
	 * @return
	 */
	protected Document createMappingDoc() {
		Document hbm = docBuilder.newDocument();
		Element hibernate = hbm.createElement("hibernate-mapping");
		hbm.appendChild(hibernate);
		logger.info(getXml(hbm));

		return hbm;
	}

	/**
	 * 添加vo class元素到document
	 * 
	 * @param c
	 * @param tableName
	 * @param mappingElement
	 * @throws SQLException
	 */
	protected void addHibernateClass(Connection c, String tableName,
			Element mappingElement) throws SQLException {
		logger.info("adding hibernate class for table " + tableName);

		Document hbm = mappingElement.getOwnerDocument();
		Element classElement = hbm.createElement("class");

		classElement.setAttribute("name", getDaoPkg() + "." + poClassName);
		classElement.setAttribute("table", tableName);

		// if (getSchemaPattern() != null) {
		// classElement.setAttribute("schema", getSchemaPattern());
		// }

		List pkColumns = JDBCUtil.getPrimaryKeyColumns(c, getCatalog(),
				getSchemaPattern(), tableName);
		Set fkColumns = JDBCUtil.getForeignKeyColumns(c, getCatalog(),
				getSchemaPattern(), tableName);
		List columns = JDBCUtil.getTableColumns(c, getCatalog(),
				getSchemaPattern(), tableName);

		//2013-4-28 jinbo 保存字段列表,在写VO时读取注释内容
		this.columnList = columns;
		
		if (pkColumns.size() == 1) {
			addId(classElement, (JDBCUtil.Column) pkColumns.iterator().next());
		} else if (pkColumns.size() > 1) {
			addCompositeId(classElement, pkColumns, fkColumns);
		}

		addProperties(classElement, pkColumns, fkColumns, columns);

		logger.info("class element: " + getXml(classElement));
		mappingElement.appendChild(classElement);
	}

	/**
	 * 添加listvo class元素到document
	 * 
	 * @param c
	 * @param tableName
	 * @param mappingElement
	 * @throws SQLException
	 */
	protected void addParamsClass(Connection c, String tableName,
			Element mappingElement) throws SQLException {
		logger.info("adding hibernate class for table " + tableName);

		Document hbm = mappingElement.getOwnerDocument();
		Element classElement = hbm.createElement("paramsClass");

		/* @todo voPackageName should be daoPackageName */
		String className = getDaoPkg() + "." + makeParamsObjName(tableName);
		classElement.setAttribute("name", className);
		classElement.setAttribute("table", tableName);

		// if (getSchemaPattern() != null) {
		// classElement.setAttribute("schema", getSchemaPattern());
		// }

		List pkColumns = JDBCUtil.getPrimaryKeyColumns(c, getCatalog(),
				getSchemaPattern(), tableName);
		Set fkColumns = JDBCUtil.getForeignKeyColumns(c, getCatalog(),
				getSchemaPattern(), tableName);
		List columns = JDBCUtil.getTableColumns(c, getCatalog(),
				getSchemaPattern(), tableName);

		if (pkColumns.size() == 1) {
			addId(classElement, (JDBCUtil.Column) pkColumns.iterator().next());
		} else if (pkColumns.size() > 1) {
			addCompositeId(classElement, pkColumns, fkColumns);
		}

		addProperties(classElement, pkColumns, fkColumns, columns);

		logger.info("class element: " + getXml(classElement));
		mappingElement.appendChild(classElement);
	}

	protected void addId(Element classElement, JDBCUtil.Column pkColumn) {
		Document hbm = classElement.getOwnerDocument();

		Element identifierElement = hbm.createElement("id");
		String idName = getIdName();

		if (idName == null) {
			// idName = makeMemberName(pkColumn.name);
			idName = StringHelper.makeMemberName(pkColumn.name, 0);
		}

		identifierElement.setAttribute("name", idName);
		identifierElement.setAttribute("column", pkColumn.name);

		if (getIdType() != null) {
			identifierElement.setAttribute("type", getIdType());
		} else {
			identifierElement.setAttribute("type", getColumnType(pkColumn));

			if (pkColumn.sqlColumnLength > 0) {
				identifierElement.setAttribute("length",
						StringHelper.EMPTY_STRING + pkColumn.sqlColumnLength);
			}
		}

		addGenerator(identifierElement);
		classElement.appendChild(identifierElement);
	}

	protected void addCompositeId(Element classElement, Collection primaryKeys,
			Collection foreignKeys) {
		Document hbm = classElement.getOwnerDocument();

		Element identifierElement = hbm.createElement("composite-id");

		// ciElement.setAttribute("class", getPackageName() + "." +
		// makeEntityName(tableName) + "Key");
		// String compositeId = this.getIdName();
		// if (compositeId == null) compositeId = makeMemberName(tableName) +
		// "Id";
		// ciElement.setAttribute("name", compositeId);
		for (Iterator it = primaryKeys.iterator(); it.hasNext();) {
			JDBCUtil.Column pkColumn = (JDBCUtil.Column) it.next();
			Element property = hbm.createElement("key-property");
			// property.setAttribute("name", makeMemberName(pkColumn.name));
			property.setAttribute("name",
					StringHelper.makeMemberName(pkColumn.name, 0));

			if ((getIdType() != null) && foreignKeys.contains(pkColumn)) {
				// if the primary key column is also a foreign key, use the
				// specified keyType.
				property.setAttribute("type", getIdType());
			} else {
				property.setAttribute("column", pkColumn.name);

				property.setAttribute("type", getColumnType(pkColumn));

				if (pkColumn.sqlColumnLength > 0) {
					property.setAttribute("length", StringHelper.EMPTY_STRING
							+ pkColumn.sqlColumnLength);
				}
			}

			identifierElement.appendChild(property);
		}

		classElement.appendChild(identifierElement);
	}

	protected void addProperties(Element classElement, Collection pkColumns,
			Collection fkColumns, Collection columns) {
		Document hbm = classElement.getOwnerDocument();

		for (Iterator it = columns.iterator(); it.hasNext();) {
			JDBCUtil.Column column = (JDBCUtil.Column) it.next();

			if (!pkColumns.contains(column)) {
				Element propertyElement = hbm.createElement("property");
				// propertyElement.setAttribute("name",
				// makeMemberName(column.name));
				propertyElement.setAttribute("name",
						StringHelper.makeMemberName(column.name, 0));
				propertyElement.setAttribute("column", column.name);

				if ((getIdType() != null) && fkColumns.contains(column)) {
					propertyElement.setAttribute("type", getIdType());
				} else {
					propertyElement.setAttribute("type", getColumnType(column));

					if (column.sqlColumnLength > 0) {
						propertyElement.setAttribute("length",
								StringHelper.EMPTY_STRING
										+ column.sqlColumnLength);
					}
				}

				if (column.sqlNotNull) {
					propertyElement.setAttribute("not-null",
							StringHelper.EMPTY_STRING + column.sqlNotNull);
				}

				logger.info("column info: " + getXml(propertyElement));
				classElement.appendChild(propertyElement);
			}
		}
	}

	protected void addGenerator(Element idElement) {
		Document hbm = idElement.getOwnerDocument();
		Element generator = hbm.createElement("generator");
		generator.setAttribute("class", getGenerator());

		if (generatorParameters != null) {
			for (int i = 0; i < generatorParameters.length; i++) {
				Element param = hbm.createElement("param");
				param.appendChild(hbm.createTextNode(generatorParameters[i]));
				generator.appendChild(param);
			}
		}

		idElement.appendChild(generator);
	}

	protected String makeEntityName(String name) {
		// String tmp = makeMemberName(name);
		String tmp = StringHelper.makeMemberName(name, ValueStore.numPrefix);
		tmp = tmp.substring(0, 1).toUpperCase() + tmp.substring(1);

		return tmp;
	}

	protected String makeParamsObjName(String tableName) {
		/* @todo should get name from wizzard */
		return "SampleListVO";
	}

	/**
	 * Getter for property schemaPattern.
	 * 
	 * @return Value of property schemaPattern.
	 */
	public java.lang.String getSchemaPattern() {
		return schemaPattern;
	}

	/**
	 * Setter for property schemaPattern.
	 * 
	 * @param schemaPattern
	 *            New value of property schemaPattern.
	 */
	public void setSchemaPattern(java.lang.String schemaPattern) {
		this.schemaPattern = schemaPattern;
	}

	/**
	 * Getter for property catalog.
	 * 
	 * @return Value of property catalog.
	 */
	public java.lang.String getCatalog() {
		return catalog;
	}

	/**
	 * Setter for property catalog.
	 * 
	 * @param catalog
	 *            New value of property catalog.
	 */
	public void setCatalog(java.lang.String catalog) {
		this.catalog = catalog;
	}

	/**
	 * Getter for property tableTypes.
	 * 
	 * @return Value of property tableTypes.
	 */
	public java.lang.String[] getTableTypes() {
		return this.tableTypes;
	}

	/**
	 * Setter for property tableTypes.
	 * 
	 * @param tableTypes
	 *            New value of property tableTypes.
	 */
	public void setTableTypes(java.lang.String[] tableTypes) {
		this.tableTypes = tableTypes;
	}

	/**
	 * Getter for property voPackageName.
	 * 
	 * @return Value of property voPackageName.
	 */
	public java.lang.String getDaoPkg() {
		return daoPkg != null ? daoPkg : "com." + ValueStore.projname
				+ ".business." + ValueStore.moduleName + "."
				+ ValueStore.baseClassName.toLowerCase() + "." + "persistent";
	}

	/**
	 * Setter for property voPackageName.
	 * 
	 * @param voPackageName
	 *            New value of property voPackageName.
	 */
	public void setDaoPkg(java.lang.String daoPkg) {
		this.daoPkg = daoPkg;
	}

	/**
	 * Getter for property hbmPackageName.
	 * 
	 * @return Value of property hbmPackageName.
	 */
	public java.lang.String getHbmPkg() {
		return hbmPkg != null ? hbmPkg : getDaoPkg();
	}

	/**
	 * Setter for property hbmPackageName.
	 * 
	 * @param hbmPackageName
	 *            New value of property hbmPackageName.
	 */
	public void setHbmPkg(java.lang.String hbmPackageName) {
		this.hbmPkg = hbmPackageName;
	}

	/**
	 * Getter for property outputDirectory.
	 * 
	 * @return Value of property outputDirectory.
	 */
	public java.io.File getSrcFolder() {
		return srcFolder;
	}

	/**
	 * Setter for property outputDirectory.
	 * 
	 * @param outputDirectory
	 *            New value of property outputDirectory.
	 */
	public void setSrcFolder(java.io.File outputDirectory) {
		this.srcFolder = outputDirectory;
		if (!outputDirectory.exists()) {
			outputDirectory.mkdirs();
		}
		if (!outputDirectory.exists() || !outputDirectory.isDirectory()) {
			throw new RuntimeException("Invalid directory " + outputDirectory);
		}
	}

	/**
	 * Getter for property idName.
	 * 
	 * @return Value of property idName.
	 */
	public java.lang.String getIdName() {
		return idName;
	}

	/**
	 * Setter for property idName.
	 * 
	 * @param idName
	 *            New value of property idName.
	 */
	public void setIdName(java.lang.String idName) {
		this.idName = idName;
	}

	/**
	 * Getter for property generator.
	 * 
	 * @return Value of property generator.
	 */
	public java.lang.String getGenerator() {
		return generator;
	}

	/**
	 * Setter for property generator.
	 * 
	 * @param generator
	 *            New value of property generator.
	 */
	public void setGenerator(java.lang.String generator) {
		this.generator = generator;
	}

	/**
	 * Getter for property generatorParameters.
	 * 
	 * @return Value of property generatorParameters.
	 */
	public java.lang.String[] getGeneratorParameters() {
		return this.generatorParameters;
	}

	/**
	 * Setter for property generatorParameters.
	 * 
	 * @param generatorParameters
	 *            New value of property generatorParameters.
	 */
	public void setGeneratorParameters(java.lang.String[] generatorParameters) {
		this.generatorParameters = generatorParameters;
	}

	/**
	 * Getter for property idType.
	 * 
	 * @return Value of property idType.
	 */
	public java.lang.String getIdType() {
		return idType;
	}

	/**
	 * Setter for property idType.
	 * 
	 * @param idType
	 *            New value of property idType.
	 */
	public void setIdType(java.lang.String idType) {
		this.idType = idType;
	}

	/**
	 * Getter for property baseClass.
	 * 
	 * @return Value of property baseClass.
	 */
	public java.lang.String getBaseClass() {
		return baseClass;
	}

	/**
	 * Setter for property baseClass.
	 * 
	 * @param baseClass
	 *            New value of property baseClass.
	 */
	public void setBaseClass(java.lang.String baseClass) {
		this.baseClass = baseClass;
	}

	/**
	 * Getter for property generateSource.
	 * 
	 * @return Value of property generateSource.
	 */
	public boolean isGenerateSource() {
		return generateSource;
	}

	/**
	 * Setter for property generateSource.
	 * 
	 * @param generateSource
	 *            New value of property generateSource.
	 */
	public void setGenerateSource(boolean generateSource) {
		this.generateSource = generateSource;
	}

	/**
	 * Setter for property javaTypes.
	 * 
	 * @param javaTypes
	 *            New value of property javaTypes.
	 */
	public void setJavaTypes(boolean javaTypes) {
		this.javaTypes = javaTypes;
	}

	public boolean getReservePrefix() {
		return this.reservePrefix;
	}

	/**
	 * 设置是否保留字段前缀
	 * 
	 * @param reservePrefix
	 *            是否保留字段前缀
	 */
	public void setReservePrefix(boolean reservePrefix) {
		this.reservePrefix = reservePrefix;
	}

	private String getColumnType(JDBCUtil.Column column) {
		return (javaTypes) ? column.javaType.getName() : column.hibernateType
				.getName();
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public void setPoClassName(String poClassName) {
		this.poClassName = poClassName;
	}

	public String getPoClassName() {
		return this.poClassName;
	}

	public void setHbmFileName(String hbmFileName) {
		this.hbmFileName = hbmFileName;
	}
}

