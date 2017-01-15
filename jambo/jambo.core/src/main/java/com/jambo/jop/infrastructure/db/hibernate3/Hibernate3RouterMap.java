package com.jambo.jop.infrastructure.db.hibernate3;

import com.jambo.jop.infrastructure.config.CoreConfigInfo;
import com.jambo.jop.infrastructure.db.DBAccessUser;
import org.apache.commons.collections.BidiMap;
import org.apache.commons.collections.bidimap.DualHashBidiMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

/**
 * 对应sessionFactoryRouter里面的名字,用于spring bean的多数据源注册
 * @author Canigar
 *	
 */
public class Hibernate3RouterMap {
	
	private static Logger logger = LoggerFactory.getLogger(Hibernate3RouterMap.class); 
	
	private static String dpMapConfig;
	private static String commonDaoMapConfig;
	
	private static BidiMap dbMap;
	private static HashMap<String,String> commonDaoMap = new HashMap<String, String>();
	
	static{
		try {
			InputStream in = Hibernate3RouterMap.class.getResourceAsStream(CoreConfigInfo.DBFLAG_MAPPING_FILE_PATH);
			Properties properties = new Properties();
			properties.load(in);
			in.close();
			dbMap = new DualHashBidiMap(properties);
        } catch (Exception ex) {
            logger.error("SessionFactoryRouter init " + CoreConfigInfo.DBFLAG_MAPPING_FILE_PATH + " error!", ex);
        }

        try {
            InputStream in = Hibernate3RouterMap.class.getResourceAsStream(CoreConfigInfo.COMMON_DAO_FILE_PATH);
            Properties properties = new Properties();
			properties.load(in);
			in.close();
			commonDaoMap = new HashMap(properties);			
		} catch (Exception ex) {
            logger.error("SessionFactoryRouter init " + CoreConfigInfo.COMMON_DAO_FILE_PATH + " error!", ex);
		}

	}
	
	public static String getRouter(String dbFlag){
		return (String) dbMap.get(dbFlag);
	}

    public static String getAlias(String dbflag) throws Exception{
        return dbMap.get(dbflag)==null?"000":dbMap.get(dbflag).toString();
    }
    public static String getDbFlagByAlias(String alias) throws Exception{
        return dbMap.inverseBidiMap().get(alias)==null? CoreConfigInfo.COMMON_DB_NAME:dbMap.inverseBidiMap().get(alias).toString();
    }

	/*
	 * 判断是否包含COMMON库里面的VO
	 */
	public static boolean containsCommonVO(String vo){
		return commonDaoMap.containsKey(vo);
	}

	/**
	 * 判断voClass是否对应COMMON库里面的库表,如何是的话返回"DB_COMMON",否则的话返回dbFlag
	 */
	public static String checkIsCommonDB(Class voClass, String dbFlag) {
		return checkIsCommonDB(voClass.getName(),dbFlag);
	}
	
	/**
	 * 判断voName是否对应COMMON库里面的库表,如何是的话返回"DB_COMMON",否则的话返回dbFlag
	 */
	public static String checkIsCommonDB(String voName, String dbFlag) {
		if(containsCommonVO(voName)){
			return CoreConfigInfo.COMMON_DB_NAME;
		}else{
			return dbFlag;
		}
	}
	
	/**
	 * 2013-6-19 jinbo
	 * 判断voName是否对应COMMON库里面的库表,如果是就返回公共库的user
	 * @throws Exception 
	 */
	public static DBAccessUser checkVOForUser(String voName, DBAccessUser user)  {
		try {
			if (containsCommonVO(voName)) {
				user = DBAccessUser.getCommonUser(user);
			}
		} catch (Exception e) {
		}

		return user;
	}

	public String getDpMapConfig() {
		return dpMapConfig;
	}

	public void setDpMapConfig(String dpMapConfig) {
		this.dpMapConfig = dpMapConfig;
	}

	public String getCommonDaoMapConfig() {
		return commonDaoMapConfig;
	}

	public void setCommonDaoMapConfig(String commonDaoMapConfig) {
		this.commonDaoMapConfig = commonDaoMapConfig;
	}
}



