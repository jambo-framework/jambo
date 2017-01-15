package com.jambo.tools.codegen.util;

import java.sql.Connection;
import java.util.List;

public class ValueStore {
	public static Connection connection; // 连接

	public static String tableName; // 表名

	public static String baseClassName; // 基本类名,其它类都根据它来变

	public static String moduleName; // 模块名称,各层的子目录都是相同的

	// 各部分路径
	public static String srcFolder;

	public static String testFolder;

	public static String webFolder;

	// 各类文件是否生成,及生成的文件
	public static boolean[] genOrNot = new boolean[10];

	// Hibernate文件是否保留字段前缀
	public static boolean reservePrefix = false;
	
	public static int numPrefix ;

	// 表的字段列表,通过它生成VO的字段和ListVO的字段
	public static List fields;

	public static String author;

	// 使用JSF还是使用struts
	public static String framework;

	public static String pkFields;

	//2013-4-26 jinbo 增加模板目录,用于灵活指定模板的位置
    public static String templatePath;
    //项目名称
    public static String projname;

	// 是否改为生成注解类型的POJO
    public static boolean isAnnotation = true;
}
