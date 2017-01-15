package com.jambo.jop.infrastructure.db;

import com.jambo.jop.infrastructure.config.CoreConfigInfo;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import java.io.Serializable;

/**
 * <p>
 * Title: BOSS
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * 
 * <p>
 * Company: jambo-framework Tech Ltd.
 * </p>
 * 
 * @author HuangBaiming
 * @author He Kun
 * 
 * @version 1.0
 * @version 1.1 增加IP参数
 */
public class DBAccessUser implements Serializable {

	private static final long serialVersionUID = 3628497740671279410L;

	private String oprcode;  //操作员编号
	private String dbFlag;  //数据所在地市ID(字母)
	private String ip;  //访问的来源IP
	
	
	//危险操作,一旦修改了innerUser内置用户的属性给修改了,所有用户的登录状态都会受到影响
	//比如本来innerUser为999
	//A用户如果是由 static块获取用户的话, 修改user的dbFlag为A会把innerUser内置用户的dbFlag刷为A
	//B用户登录如果也是由 static块获取用户的话, 获取dbFlag亦为A
	//详细见main方法实现
	
//	private static final DBAccessUser innerUser;
//	
//	static  {
//		//内置的用户， 用于code2name, 登录检查，权限检查等内部操作，如果需要，可以修改默认用户的dbFlag等属性。
//		innerUser = new DBAccessUser();
//		innerUser.setDbFlag("DB_COMMON");
//		innerUser.setOprcode("root");
//		innerUser.setIp("127.0.0.1");
//	}
	
	public String getOprcode() {
		return oprcode;
	}

	public void setOprcode(String oprcode) {
		this.oprcode = oprcode;
	}

	public String getDbFlag() {
		return dbFlag;
	}

	public void setDbFlag(String dbFlag) {
		this.dbFlag = dbFlag;
	}
	
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
	
	public String toString(){
		return ReflectionToStringBuilder.toString(this);
	}

    /**
     * 返回公共库用户对象，用于没登录用户的场景，opcode会指定为jop，IP会返回本机的IP
     */
	public static DBAccessUser getInnerUser() {
		DBAccessUser innerUser = new DBAccessUser();
		innerUser.setDbFlag(CoreConfigInfo.COMMON_DB_NAME);
		innerUser.setOprcode("jop");
		innerUser.setIp(CoreConfigInfo.LOCAL_IP_ADDDRESS);
		return innerUser;
	}
	
    /**
     * 2013-5-29 jinbo 增加使用登录user获取公共库user的方法
     * @param user 当前的User
     * @return dbFlag被转换为公共库的dbFlag
     */
	public static DBAccessUser getCommonUser(DBAccessUser user) throws Exception{
		DBAccessUser commUser = new DBAccessUser();
		commUser.setDbFlag(CoreConfigInfo.COMMON_DB_NAME);
		if (user != null){
			commUser.setOprcode(user.getOprcode());
			commUser.setIp(user.getIp());
		}
		return commUser;
	}
	
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
}
