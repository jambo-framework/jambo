/**
 * 
 */
package com.jambo.jop.common.utils.lang;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author He Kun
 * Aug 25, 2008
 *
 */
public class TimeUtils {
	
	private static SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private static SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
	
	public static String formatDateTime(Date date) {
		return dateTimeFormat.format(date);
	}
	
	public static String formatDate(Date date) {
		return dateFormat.format(date);
	}
	
	public static String formatTime(Date date) {
		return timeFormat.format(date);
	}
}
