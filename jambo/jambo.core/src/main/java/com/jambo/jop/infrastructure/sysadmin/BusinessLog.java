/**
 * @author He Kun (Henry He), Guangzhou, China
 * 2006-9-15
 */
package com.jambo.jop.infrastructure.sysadmin;

import java.io.*;

/**
 * DefaultOperationLog
 * <br> Description: 操作日志接口. 需要登记操作日志的VO类,需要实现此接口.
 * 一个表对应一张日志表,表结构必须符合操作日志表设计规范.
 * <br> Company: Sunrise,Guangzhou</br>
 * @author He Kun
 * @since 1.0
 * @version 1.0
 * 2006-9-15
 */
public interface BusinessLog extends Serializable {
	
	public static final Byte STATE_SUCCESS = Byte.valueOf("0");
	public static final Byte STATE_FAIL = Byte.valueOf("-1");
	
	public static final Short ACTION_CREATE = Short.valueOf("0"); 
	public static final Short ACTION_UPDATE = Short.valueOf("1"); 
	public static final Short ACTION_REMOVE = Short.valueOf("2");
	public static final Short ACTION_UNKNOWN = Short.valueOf("-1"); 
	
	/**
	 * 返回对应的操作日志VO类.
	 * @return
	 */
	Class logVOClass();
}
