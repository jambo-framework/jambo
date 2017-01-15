package com.jambo.jop.common.utils.lang;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: JinBo Date: 2006-4-6 Time: 11:27:00
 */
public class PublicUtils {

	/**
	 * 将java.sql.Date类型的日期转换为yyyy-MM-dd格式日期字符串 注：只有日期
	 *
	 * @author mys
	 */
	public static String sqlDateToStr(java.sql.Date date) {
		return formatSqlDate(date, "yyyy-MM-dd");
	}

	/**
	 * 将java.sql.Date类型的日期转换为pattern格式的日期字符串
	 *
	 * @author mys
	 */
	public static String formatSqlDate(java.sql.Date date, String pattern) {
		if (date == null)
			return "";
		DateFormat format = new SimpleDateFormat(pattern);
		return format.format(date);
	}

	/**
	 * 将yyyy-MM-dd格式的日期字符串转换为java.sql.Date类型
	 *
	 * @author mys
	 * @throws Exception
	 */
	public static java.sql.Date strToSqlDate(String datetime) throws Exception {
		return strToSqlDate(datetime, "yyyy-MM-dd");
	}

	/**
	 * 将pattern格式的日期字符串转换为java.sql.Date类型
	 *
	 * @author mys
	 * @throws Exception
	 */
	public static java.sql.Date strToSqlDate(String datetime, String pattern)
			throws Exception {
		java.sql.Date result = null;
		try {
			if ((datetime != null) && (datetime.length() > 0)) {
				SimpleDateFormat format = new SimpleDateFormat(pattern);
				result = new java.sql.Date(format.parse(datetime).getTime());
			}
		} catch (Exception exp) {
			throw new Exception("have a NG date format");
		}
		return result;
	}

	/**
	 * 将java.utils.Date类型的日期转换为“yyyy-MM-dd HH:mm:ss" 格式日期字符串<br>
	 * 注：只有日期，时，分，秒为零
	 */
	public static String utilDateToStr(java.util.Date date) {
		return formatUtilDate(date, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 将java.utils.Date类型的日期转换为pattern格式的日期字符串
	 */
	public static String formatUtilDate(java.util.Date date, String pattern) {
		if (date == null)
			return "";
		DateFormat format = new SimpleDateFormat(pattern);
		return format.format(date);
	}

	/**
	 * xqy 将“yyyy-MM-dd HH:mm:ss"格式的日期字符串转换为java.utils.Date类型
	 *
	 * @throws Exception
	 */
	public static java.util.Date UtilStrToDate(String datetime)
			throws Exception {
		return UtilStrToDate(datetime, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * xqy 将pattern格式的日期字符串转换为java.utils.Date类型
	 *
	 * @throws Exception
	 */
	public static java.util.Date UtilStrToDate(String datetime, String pattern)
			throws Exception {
		java.util.Date result;
		try {
			if ((datetime != null) && (datetime.length() > 0)) {
				SimpleDateFormat format = new SimpleDateFormat(pattern);
				result = format.parse(datetime);
			} else {
				result = null;
			}
		} catch (Exception exp) {
			// 指定的日期字符串格式不对
			throw new Exception("have a NG date format");
		}
		return result;
	}

	/**
	 * 将对象强制转换为字符串，空则返回""
	 */
	public static String objToStr(Object obj) {
		if (obj != null)
			return obj.toString();
		return "";
	}

	/**
	 * 字段串是否为空
	 */
	public static boolean isBlankString(String str) {
		return (str == null || str.trim().equals("") || str.length() < 0);
	}

	/**
	 * 比较两个日期相差多少个月，用d2 - d1
	 */
	public static int compareMonth(java.util.Date d1, java.util.Date d2) {
		java.util.Calendar c1 = java.util.Calendar.getInstance();
		c1.setTime(d1);
		java.util.Calendar c2 = java.util.Calendar.getInstance();
		c2.setTime(d2);
		// int m1 = c1.get(java.utils.Calendar.YEAR) * 12 +
		// c1.get(java.utils.Calendar.MONTH);
		// int m2 = c2.get(java.utils.Calendar.YEAR) * 12 +
		// c2.get(java.utils.Calendar.MONTH);

		return (c1.get(java.util.Calendar.YEAR) - c2
				.get(java.util.Calendar.YEAR))
				* 12
				+ (c1.get(java.util.Calendar.MONTH) - c2
				.get(java.util.Calendar.MONTH));
	}
	/**
	 * 比较两个日期相差多少天，用d2 - d1
	 */
	public static int compareDate(java.util.Date d1, java.util.Date d2) {

		return (int)((d2.getTime()-d1.getTime())/(1000*60*60*24));
	}

	/**
	 * 比较两个日期相差多少个月，用d2 - d1
	 *
	 * @author mys
	 */
	public static int compareMonth(java.sql.Date d1, java.sql.Date d2) {
		java.util.Calendar c1 = java.util.Calendar.getInstance();
		c1.setTime(d1);
		java.util.Calendar c2 = java.util.Calendar.getInstance();
		c2.setTime(d2);

		return (c1.get(java.util.Calendar.YEAR) - c2
				.get(java.util.Calendar.YEAR))
				* 12
				+ (c1.get(java.util.Calendar.MONTH) - c2
				.get(java.util.Calendar.MONTH));
	}

	/**
	 * 验证目标字符串是否整数
	 * <p>
	 * PublicUtils.isInteger(null) = false
	 * <p>
	 * PublicUtils.isInteger("") = false
	 * <p>
	 * PublicUtils.isInteger(" ") = false
	 * <p>
	 * PublicUtils.isInteger("02007") = false
	 * <p>
	 * PublicUtils.isInteger("2 007") = false
	 * <p>
	 * PublicUtils.isInteger(" 2007 ") = true
	 * <p>
	 *
	 * @param str
	 *            要验证的字符串
	 * @return
	 */
	public static boolean isInteger(String str) {
		Pattern pattern = Pattern.compile("([1-9]+[0-9]*|0)");
		Matcher matcher = pattern.matcher(org.apache.commons.lang.StringUtils
				.trimToEmpty(str));
		return matcher.matches();
	}

	/**
	 * 验证目标字符串是否整数
	 * <p>
	 * PublicUtils.isInteger(null) = false
	 * <p>
	 * PublicUtils.isInteger("") = false
	 * <p>
	 * PublicUtils.isInteger(" ") = false
	 * <p>
	 * PublicUtils.isInteger("02007") = true
	 * <p>
	 * PublicUtils.isInteger("2 007") = false
	 * <p>
	 * PublicUtils.isInteger(" 2007 ") = true
	 * <p>
	 *
	 * @param str
	 *            要验证的字符串
	 * @return
	 */
	public static boolean isIntegerWithZero(String str) {
		Pattern pattern = Pattern.compile("([0-9]+)");
		Matcher matcher = pattern.matcher(org.apache.commons.lang.StringUtils
				.trimToEmpty(str));
		return matcher.matches();
	}

	/**
	 * 验证目标字符串是否小数
	 *
	 * @param str
	 *            要验证的字符串
	 * @param scale
	 *            精度，即小数点后几位
	 * @param maximum
	 *            最大值（小于maxinum）
	 * @return
	 */
	public static boolean isDecimal(String str, int scale, double maximum) {
		Pattern pattern = Pattern.compile("([1-9]+[0-9]*|0)(\\.[0-9]{1,"
				+ scale + "}|)");
		Matcher matcher = pattern.matcher(org.apache.commons.lang.StringUtils
				.trimToEmpty(str));
		return matcher.matches() && Double.parseDouble(str) <= maximum;
	}

	/**
	 * 用于将小写金额转换成大写，最大数据为亿
	 * @param value
	 * @return
	 * @see #changeToBig(String)
	 */
	public static String changeToBig(Double value) {
		DecimalFormat df = new DecimalFormat("#.00");
		return changeToBig(df.format(value));
	}

	/**
	 * 用于将小写金额转换成大写，最大数据为亿
	 *
	 * @param value
	 *            小写金额（精确到小数点后两位）
	 * @return 大写金额
	 * @throws IllegalArgumentException
	 *             当传入参数为空或者不是有效数字类型时，抛出该例外
	 */
	public static String changeToBig(String value) {

		// 校验参数value，不允许为空
		if (org.apache.commons.lang.StringUtils.isBlank(value)) {
			throw new IllegalArgumentException("传入方法 PublicUtils.changeToBig("
					+ "String value)的参数不允许为空：" + value + "。");
		}

		// 校验参数value精度及最大值，不允许超过指定数值
		double max = 9999999999d;
		if (!isDecimal(value, 2, max)) {
			throw new IllegalArgumentException("传入方法 PublicUtils.changeToBig("
					+ "String value)的参数不是有效数字：" + value + "。");
		}

		// 将参数value转换为大十进制，防止数值转换时，出现舍入问题
		BigDecimal d = new BigDecimal(value);
		BigDecimal d2 = new BigDecimal("100");

		// 定义相关段内，段名及数字翻译
		char[] hunit = { '拾', '佰', '仟' };
		char[] vunit = { '万', '亿' };
		char[] digit = { '零', '壹', '贰', '叁', '肆', '伍', '陆', '柒', '捌', '玖' };
		long midVal = 0;
		String valStr = "";
		String head = "";
		String rail = "";

		// 对只有小数部分进行特殊处理
		if (d.compareTo(new BigDecimal("1")) < 0) {
			head = "0";
			rail = d.multiply(new BigDecimal("100")).compareTo(
					new BigDecimal("10")) < 0 ? "0"
					+ (d.multiply(d2).toString()) : d.multiply(d2).toString();
		} else {
			midVal = d.multiply(new BigDecimal("100")).longValue();
			valStr = String.valueOf(midVal);
			head = valStr.substring(0, valStr.length() - 2);
			rail = valStr.substring(valStr.length() - 2);
		}

		String prefix = "";
		String suffix = "";

		// 处理小数部分
		if (rail.equals("00")) {
			suffix = "整";
		} else {
			suffix = digit[rail.charAt(0) - '0'] + "角"
					+ digit[rail.charAt(1) - '0'] + "分";
		}

		// 处理整数部分
		char[] chDig = head.toCharArray();
		char zero = '0';
		byte zeroSerNum = 0;

		// 对整数部分循环处理
		for (int i = 0; i < chDig.length; i++) {
			int idx = (chDig.length - i - 1) % 4;
			int vidx = (chDig.length - i - 1) / 4;

			// 如段（即个十百千为一个段）内相邻的零的个数大于1，也只显示一个零
			if (chDig[i] == '0') {
				zeroSerNum++;
				if (zero == '0') {
					zero = digit[0];
				}
				if (idx == 0 && vidx > 0 && zeroSerNum < 4) {
					prefix += vunit[vidx - 1];
					zero = '0';
				}
				continue;
			}
			zeroSerNum = 0;
			if (zero != '0') {
				prefix += zero;
				zero = '0';
			}
			prefix += digit[chDig[i] - '0'];
			if (idx > 0)
				prefix += hunit[idx - 1];
			if (idx == 0 && vidx > 0) {
				prefix += vunit[vidx - 1];
			}
		}

		if (prefix.length() > 0)
			prefix += '圆';
		return prefix + suffix;
	}

	/**
	 * 将整数或者小数转换成字符串
	 * @param value
	 * @return
	 * @see #changeToArray(String)
	 */
	public static char[] changeToArray(Double value) {
		DecimalFormat df = new DecimalFormat("#.00");
		return changeToArray(df.format(value));
	}

	/**
	 * 将整数或者小数转换成字符串
	 *
	 * @param value
	 *            待转换的有效数字
	 * @return
	 * @throws IllegalArgumentException
	 *             当传入参数为空或者不是有效数字类型时，抛出该例外
	 */
	public static char[] changeToArray(String value) {
		if (org.apache.commons.lang.StringUtils.isBlank(value)) {
			throw new IllegalArgumentException(
					"传入方法 PublicUtils.changeToArray("
							+ "String value)的参数不允许为空：" + value + "。");
		}
		if (!isDecimal(value, 2, 99999999)) {
			throw new IllegalArgumentException(
					"传入方法 PublicUtils.changeToArray("
							+ "String value)的参数不是有效数字：" + value + "。");
		}
		BigDecimal d = new BigDecimal(value);
		long midVal = d.multiply(new BigDecimal("100")).longValue();
		String tmp = String.valueOf(midVal);
		char[] chs = tmp.toCharArray();
		return chs;
	}

	/**
	 * @param year
	 * @param month
	 * @return 返回指定年月的开始时间 （yyyy-MM-dd 00:00:00）
	 */
	public static String getMonthBegin(int year, int month) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month - 1, 1);
		return formatUtilDate(calendar.getTime(), "yyyy-MM-dd") + " 00:00:00";
	}

	/**
	 * @param year
	 * @param month
	 * @return 返回指定年月的结束时间 （yyyy-MM-dd 23:59:59）
	 */
	public static String getMonthEnd(int year, int month) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month, 0);
		return formatUtilDate(calendar.getTime(), "yyyy-MM-dd") + " 23:59:59";
	}

	/**
	 * 获得当前帐务周期
	 *
	 * @return valBillCyc 当前有效帐务周期 add by xff
	 */
	public static Long getCurBillCyc() {
		Calendar cal = Calendar.getInstance();
		String curdate = PublicUtils.formatUtilDate(cal.getTime(),
				"yyyyMMdd HH:mm:ss");
		String billcyc = curdate.substring(0, 6) + "00";
		Long valBillCyc = null;
		if (billcyc != null && !"".equals(billcyc)) {
			valBillCyc = new Long(billcyc);
		}
		return valBillCyc;
	}

	/**
	 * 增加导入时候对日期检查的支持,修复JDK的SimpleDataFormat.parse方法对[20081301]这种数据的通过
	 * 对于闰年的判断也有计算在方法里面,时间由1000-01-01 00:00:00到9999-12-31 23:59:59
	 * @param s
	 * @return
	 * @author linli
	 */
	public static boolean checkDateTime(String s){
		return checkDateTime(4, s, "-", ":", " ");
	}

	/**
	 * 增加导入时候对日期检查的支持,修复JDK的SimpleDataFormat.parse方法对[20081301]这种数据的通过
	 * 对于闰年的判断也有计算在方法里面,时间由1000-01-01 00:00:00到9999-12-31 23:59:59
	 * @param type<br>
	 *   参数 1 支持到检查格式yyyy-MM,如2009-01<br>
	 *   参数 2 支持到检查格式yyyy-MM-dd,如2009-01-01<br>
	 *   参数 3 支持到检查格式HH:mm:ss,如00:00:00<br>
	 *   参数 4 支持到检查格式yyyy-MM-dd HH:mm:ss,如2009-01-01 00:00:00<br>
	 * @param s
	 * @return
	 * @author linli
	 */
	public static boolean checkDateTime(int type, String s){
		return checkDateTime(type, s, "-", ":", " ");
	}

	/**
	 * 增加导入时候对日期检查的支持,修复JDK的SimpleDataFormat.parse方法对[20081301]这种数据的通过
	 * 对于闰年的判断也有计算在方法里面,时间由1000-01-01 00:00:00到9999-12-31 23:59:59
	 * @param type<br>
	 *   参数 1 支持到检查格式yyyy-MM,如2009-01<br>
	 *   参数 2 支持到检查格式yyyy-MM-dd,如2009-01-01<br>
	 *   参数 3 支持到检查格式HH:mm:ss,如00:00:00<br>
	 *   参数 4 支持到检查格式yyyy-MM-dd HH:mm:ss,如2009-01-01 00:00:00<br>
	 * @param s
	 * @param datePrefix
	 * 	 区分日期之间的分隔符 默认 -
	 * @param timePrefix
	 * 	 区分时间之间的分隔符 默认 :
	 * @param blank
	 * 	 区分日期与时间之间的分隔符 默认 空格
	 * @return
	 * @author linli
	 */
	public static boolean checkDateTime(int type, String s, String datePrefix, String timePrefix, String blank){

		if(null == datePrefix)datePrefix="";
		if(null == timePrefix)timePrefix="";
		if(null == blank)blank="";

		StringBuffer regex = new StringBuffer();
		switch(type){
			case 1:
				regex.append("([1-9][0-9]{3}").append(datePrefix).append("[0][1-9])").	//年跟前9个月
						append("|").
						append("([1-9][0-9]{3}").append(datePrefix).append("[1][0-2])");	//年跟后3个月
				break;
			case 2:
				regex.append("(").
						append("([1-9][0-9]{3})").append(datePrefix).
						append("(").
						append("((0[13578]|1[02])").append(datePrefix).append("(0[1-9]|[12][0-9]|3[01]))").  //1 3 5 7 8 10 12月为31天
						append("|").
						append("((0[469]|11)").append(datePrefix).append("(0[1-9]|[12][0-9]|30))").		//4 6 9 11月为30天
						append("|").
						append("(02").append(datePrefix).append("(0[1-9]|[1][0-9]|2[0-8]))").	//默认情况下设2月为28天
						append(")").
						append(")").
						append("|").
						append("((([1-9][0-9])(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))").append(datePrefix).append("02").append(datePrefix).append("29)");   //闰年情况下为29天
				break;
			case 3:
				regex.append("((20|21|22|23|[0-1]?\\d)").append(timePrefix).append("[0-5]?\\d").append(timePrefix).append("[0-5]?\\d)");
				break;
			case 4:
				String date = s.substring(0, 8 + 2 * datePrefix.length());
				String time = s.substring(8 + 2 * datePrefix.length() + blank.length());
				return checkDateTime(2, date, datePrefix, timePrefix, blank) && checkDateTime(3, time, datePrefix, timePrefix, blank) ;
			default:
				return false;
		}

		Pattern p = Pattern.compile(regex.toString());
		Matcher m = p.matcher(s);
		return m.matches();
	}

}
