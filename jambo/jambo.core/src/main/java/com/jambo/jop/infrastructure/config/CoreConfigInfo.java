package com.jambo.jop.infrastructure.config;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.InetAddress;
import java.util.Properties;
import java.util.ResourceBundle;

public class CoreConfigInfo {
    public static String LOCAL_IP_ADDDRESS = "127.0.0.1";

    public static final String SESSION_ATTRIBUTE_USER = "JAMBO_SESSION_USER"; //放在Session里的操作员标识
    public static final String COMMON_CITY_MODE = "COMMON_CITY_MODE"; //公共+地市库模式 COMMON_CITY_MODE， 平行库模式:BROTHERS_MODE -->
    public static final String BROTHERS_MODE = "BROTHERS_MODE"; // 公共库

	/**
	 * --------核心，基本不变的配置------------------------------------------------------
	 */
	// 配置文件的根目录
	public static final String CFG_ROOT_PATH = "/data";
	// 配置文件的根目录
	public static final String I18N_ROOT_PATH = "i18n";
	// cityid mapping 配置文件
	public static final String DBFLAG_MAPPING_FILE_PATH = CFG_ROOT_PATH  + "/dbMapping.properties";
	// common DAO 配置文件
	public static final String COMMON_DAO_FILE_PATH = CFG_ROOT_PATH  + "/commonDBTable.properties";
	/**
	 * ------------------------------------------------------------------------
	 */

	/**
	 * 2011-8-24 jinbo 增加配置清单properties，用于指定JOP2使用的核心配置文件与其它配置文件 路径列表
	 * --------在程序启动路径找配置文件coreconfiginfo.properties,可随意改名--------------------------------
	 * --------提供给独立程序设置日志与配置路径的参数 ------------------------------------------------------
	 */
	// 配置文件清单MAP，可以存点任意的东西，前缀按业务组英文缩写标准
	private static Properties properties = new Properties();
	/**
	 * ------------------------------------------------------------------------
	 */

	private static ResourceBundle rs = null;

	/**
	 * --------Flag标识，布尔类型------------------------------------------------------
	 */
	// 是否在WebSphere下运行
	public static boolean RUN_BY_WEBSPHERE_FLAG = true;
	// 是否使用EJB
	public static boolean EJB_FLAG = false;
	// 是否在测试模式下运行
	public static boolean TEST_FLAG = false;
	// 是否使用缓存
	public static boolean USE_CACHE_FLAG = false;
	/**
	 * ------------------------------------------------------------------------
	 */

	// 公共库标识
	public static String COMMON_DB_NAME;
	// 默认页码
	public static String DEFAULT_PAGE;
	// 默认页大小
	public static String DEFAULT_PAGE_SIZE;

	// FTP参数
	public static String FTP_ADDRESS;
	public static String FTP_PORT;
	public static String FTP_USER;
	public static String FTP_PASSWORD;
	public static String FTP_WORK_DIR;

	// 上传、下载目录
	public static String UPLOAD_LOCATION;
	public static String DOWNLOAD_LOCATION;

	static {
		initial(null);
	}

    // 2011-8-27  jinbo 改造为读取到properties里，并提供给其它程序使用
	private static void initial(String path) {
		try {
            InetAddress addr = InetAddress.getLocalHost();
            LOCAL_IP_ADDDRESS = addr.getHostAddress().toString();//获得本机IP 　

			try {
				if (path != null && !path.equals("")){
					properties.load(new FileInputStream(path));
				} else {
					InputStream in = CoreConfigInfo.class.getResourceAsStream("/coreconfiginfo.properties"); 
					properties.load(in);
//					properties.load(ResourceBundle.class.getClassLoader().getResourceAsStream("coreconfiginfo.properties"));
				}
			} catch (Exception e) {
				System.out.println("coreconfiginfo load error...");
				throw e;
			}
			
			RUN_BY_WEBSPHERE_FLAG = getRuntimeParamString("run.by.websphere.flag").equalsIgnoreCase("yes");
			EJB_FLAG = getRuntimeParamString("ejb.flag").equalsIgnoreCase("yes");
			TEST_FLAG = getRuntimeParamString("test.flag").equalsIgnoreCase("yes");
			USE_CACHE_FLAG = getRuntimeParamString("use.cache.flag").equalsIgnoreCase("yes");
			COMMON_DB_NAME = getRuntimeParam("common.db.name");
			DEFAULT_PAGE = getRuntimeParam("default.page");
			DEFAULT_PAGE_SIZE = getRuntimeParam("default.page.size");

			FTP_ADDRESS = getRuntimeParam("ftp.address");
			FTP_PORT = getRuntimeParam("ftp.port");
			FTP_USER = getRuntimeParam("ftp.user");
			FTP_PASSWORD = getRuntimeParam("ftp.password");
			FTP_WORK_DIR = getRuntimeParam("ftp.work.dir");

			UPLOAD_LOCATION = getRuntimeParam("upload.location");
			DOWNLOAD_LOCATION = getRuntimeParam("download.location");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private CoreConfigInfo() {
	}
	
	/**
	 * 提供配置文件，强制CoreConfigInfo按这个配置文件进行重新刷新配置
	 * @param path 配置清单文件路径
	 */
	public static void setRuntime(String path){
		initial(path);
	}
	
	/**
	 * 用key来查询配置清单中指定的配置文件路径，没值则返回空
	 * @param key KEY值  
	 */
	public static String getRuntimeParam(String key) {
		String value = null;
		if (properties != null)
			value = properties.getProperty(key);
		return value;
	}

    /**
     * 用key来查询配置清单中指定的配置文件路径，没值则返回空字符串
     */
    public static String getRuntimeParamString(String key) {
        String v = getRuntimeParam(key);
        if (v == null) v = "";
        return v;
    }

	/**
	 * 直接将独立程序的启动参数全部传过来设置配置清单，如果有 runtime=/xxx,则设置配置清单文件，无则没反应
	 * @param args 独立程序启动参数
	 */
	public static void setRuntimeByArgs(String[] args){
		if (args != null){
			String runtimePath = null;
			for (int i = 0; i < args.length; i++){
				String param = args[i];
				
				int pos = param.indexOf("runtime=");
				if (pos >= 0){
					runtimePath = param.substring(8);
				}
			}
			setRuntime(runtimePath);
		}
	}
}