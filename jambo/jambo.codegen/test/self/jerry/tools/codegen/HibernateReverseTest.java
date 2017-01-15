package self.jerry.tools.codegen;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.jdom.input.SAXBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.jambo.tools.codegen.generators.GeneratorAnnotation;
import com.jambo.tools.codegen.generators.MapGenerator;
import com.jambo.tools.codegen.preferences.PreferenceConstants;
import com.jambo.tools.codegen.util.StringHelper;
import com.jambo.tools.codegen.util.ValueStore;


public class HibernateReverseTest {

	private static Connection c;
	public HibernateReverseTest() {
		super();
	}
	
	private static void connect() {
//		String driverName = "oracle.jdbc.driver.OracleDriver";
//		String url = "jdbc:oracle:thin:@180.200.3.22:1521:test";
//		String user = "boss";
//		String pswd ="boss";

		String driverName = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://127.0.0.1/COMMON";
		String user = "root";
		String pswd ="";

		try {
			Class.forName(driverName);
			c = DriverManager.getConnection(url, user, pswd);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void testMapGenerator() throws Exception{
		connect();
        
		try {
			ValueStore.projname = "ng3";
			ValueStore.moduleName = "a";
			ValueStore.baseClassName = "Errcode";
			
			MapGenerator mg = new MapGenerator();
			mg.setTableName("sa_db_errcode");
			mg.setSrcFolder(new File("d:/download/src"));
	
			mg.setBaseClass(ValueStore.baseClassName);
			mg.setHbmFileName("errcode.hbm.xml");		
			mg.setPoClassName("ErrcodeVO");
			mg.setGenerator("assigned");
			mg.setIdType("int");
			mg.setJavaTypes(true);
			mg.setReservePrefix(false);
			mg.setSchemaPattern("") ;
			mg.setCatalog("");
			
			mg.generate(c);
		}catch (Exception e){
			e.printStackTrace();
		} finally {
			if (c != null) c.close();
		}
	}
	
	public static Document readXML(File xml) {
	    Document doc = null;
	    try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
	    	doc = db.parse(xml);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return doc;
	}
	
	public static void testAnnotationGenerator() throws Exception{
		File srcFolder = new File("D:/Projects/NGCRM/NGCRM/src-product/");
		String hbmPkg = "com.sunrise.ngcrm.business.product.solutionprods";
		String poClassName = "SolutionprodsVO";
		
		File hbmFile = new File(srcFolder, hbmPkg.replace(StringHelper.DOT,
				File.separatorChar) + "\\Solutionprods.hbm.xml");

		Document d = readXML(hbmFile);
		
		GeneratorAnnotation AnnoGen = new GeneratorAnnotation();
		
		AnnoGen.generateAnnotation(d, srcFolder, hbmPkg, poClassName);
	}
	
	public static void main(String[] args) throws Exception{
		testAnnotationGenerator();
	}
}
