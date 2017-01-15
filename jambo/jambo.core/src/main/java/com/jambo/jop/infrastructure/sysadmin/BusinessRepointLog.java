package com.jambo.jop.infrastructure.sysadmin;


/**
 * ExtendOperationLog
 * <br> Description: 操作日志接口. 需要登记操作日志的VO类而原表里面又存在日志表的属性,需要重新指定属性的,需要实现此接口.
 * <br> Company: Sunrise,Guangzhou</br>
 * @author Linli
 * @since 1.0
 * @version 1.0
 * 2009年11月24日16:46:36
 */
public interface BusinessRepointLog extends BusinessLog{

	/**
	 * 添加logProperties属性,当logvo属性指定有所改变的时候,记住改变的logvo属性顺序一定要跟这5个属性的顺序一致
	 */
	public static final String[] logProperties = new String[]{"logid","optime","oprcode","oprtype","success"};

	public static final int logid = 0;
	public static final int optime = 1;
	public static final int oprcode = 2;
	public static final int oprtype = 3;
	public static final int success = 4;
	
	public String[] repointLogProperites();
 	
}
