/**
 * @(#) HttpClientUtil.java Created on 2014年5月26日
 *
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.gionee.gniflow.web.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * The class <code>DateUtil</code>
 * 
 * @author lipw
 * @version 1.0
 */
public class DateUtil {
	
	private static final String EMPTY = "";
	/**
	 * Date time format: yyyy-MM-dd HH:mm:ss
	 */
	public static final String DATE_TIME_FORMAT_DEFAULT = "yyyy-MM-dd HH:mm:ss";

	public static final String DATE_TIME_FORMAT_DAY = "yyyy-MM-dd";

	public static final String DATE_TIME_FORMAT_YEAR = "yyyy";

	public static final String DATE_TIME_FORMAT_DAY_YYYYMMDD = "yyyyMMdd";

	/**
	 * Formatter, format: yyyy-MM-dd HH:mm:ss
	 */
	private static final SimpleDateFormat defaultFormatter = new SimpleDateFormat(
			DATE_TIME_FORMAT_DEFAULT);

	/**
	 * Date time format: yyyyMMddHHmmSS
	 */
	public static final String DATE_TIME_FORMAT_STAMP = "yyyyMMddHHmmSS";

	/**
	 * Formatter, format: yyyy-MM-dd
	 */
	private static final SimpleDateFormat stampDayFormatter = new SimpleDateFormat(
			DATE_TIME_FORMAT_DAY);

	/**
	 * Formatter, format: yyyyMMdd
	 */
	private static final SimpleDateFormat stampDayOtherFormatter = new SimpleDateFormat(
			DATE_TIME_FORMAT_DAY_YYYYMMDD);

	/**
	 * Formatter, format: yyyyMMddHHmmSS
	 */
	private static final SimpleDateFormat stampFormatter = new SimpleDateFormat(
			DATE_TIME_FORMAT_STAMP);

	/**
	 * Formatter, format: yyyy
	 */
	private static final SimpleDateFormat stampYearFormatter = new SimpleDateFormat(
			DATE_TIME_FORMAT_YEAR);

	/**
	 * Private constructor for avoiding instantiate.
	 */
	private DateUtil() {
	}

	/**
	 * Format date time to default format(yyyy-MM-dd HH:mm:ss).
	 * 
	 * @param dateTime
	 * @return
	 */
	public static String format(Date dateTime) {
		if (null == dateTime) {
			return EMPTY;
		}

		return defaultFormatter.format(dateTime);
	}

	/**
	 * Format date time to default format(yyyy-MM-dd).
	 * 
	 * @param dateTime
	 * @return
	 */
	public static String formatDay(Date dateTime) {
		if (null == dateTime) {
			return EMPTY;
		}

		return stampDayFormatter.format(dateTime);
	}

	/**
	 * Format date time to default format(yyyyMMdd).
	 * 
	 * @param dateTime
	 * @return
	 */
	public static String formatOtherDay(Date dateTime) {
		if (null == dateTime) {
			return EMPTY;
		}

		return stampDayOtherFormatter.format(dateTime);
	}

	/**
	 * Format date time to simple format(yyyyMMddHHmmSS).
	 * 
	 * @param dateTime
	 * @return
	 */
	public static String stamp(Date dateTime) {
		if (null == dateTime) {
			return EMPTY;
		}

		return stampFormatter.format(dateTime);
	}

	/**
	 * Format date time to specified format
	 * 
	 * @param dateTime
	 * @param format
	 * @return
	 */
	public static String format(Date dateTime, String format) {
		return new SimpleDateFormat(format).format(dateTime);
	}

	/**
	 * Parse string to date time to
	 * 
	 * @param dateString
	 * @param format
	 * @return
	 */
	public static Date parse(String dateString, String format) {
		try {
			return new SimpleDateFormat(format).parse(dateString);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * Parse string to date time to
	 * 
	 * @param dateString
	 * @param format
	 * @return
	 */
	public static Date parse(String dateString) {
		try {
			return defaultFormatter.parse(dateString);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * Format date time to simple format(yyyy).
	 * 
	 * @param dateTime
	 * @return
	 */
	public static String formatYear(Date dateTime) {
		if (null == dateTime) {
			return EMPTY;
		}
		return stampYearFormatter.format(new Date());
	}

	/**
	 * Get the start of a day
	 * 
	 * @param date
	 * @return
	 */
	public static Date getStartOfDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	/**
	 * Get the start of next day
	 * 
	 * @param date
	 * @return
	 */
	public static Date getStartOfNextDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	/**
	 * 获取所给时间点所在月的第一天的0点0分0秒。
	 * 
	 */
	public static Date getStartOfMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DATE, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	/**
	 * 获取所给时间点所在月的下一个月的第一天的0点0分0秒。
	 */
	public static Date getStartOfNextMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, 1);
		calendar.set(Calendar.DATE, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	/**
	 * 获取本周日0点0分0秒
	 * 
	 * @return
	 */
	public static Date getStartOfWeek(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_WEEK, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	/**
	 * 获取 下一周周日0点0分0秒
	 * 
	 * @return
	 */
	public static Date getStartOfNextWeek(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.WEEK_OF_YEAR, 1);
		calendar.set(Calendar.DAY_OF_WEEK, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	/**
	 * 获取上周周日0点0分0秒
	 * 
	 * @return
	 */
	public static Date getStartOfLastWeek(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_WEEK, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.add(Calendar.WEEK_OF_YEAR, -1);
		return calendar.getTime();
	}

	/**
	 * 调整日期
	 * 
	 * @param date
	 * @param dayCount
	 *            增加（正数）或减少（负数）的小时数
	 * @return
	 */
	public static Date addHour(Date date, int hourCount) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.HOUR_OF_DAY, hourCount);
		return calendar.getTime();
	}

	/**
	 * 调整日期
	 * 
	 * @param date
	 * @param dayCount
	 *            增加（正数）或减少（负数）的日数
	 * @return
	 */
	public static Date addDay(Date date, int dayCount) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_YEAR, dayCount);
		return calendar.getTime();
	}

	/**
	 * 调整日期
	 * 
	 * @param date
	 * @param weekCount
	 *            增加（正数）或减少（负数）的周数
	 * @return
	 */
	public static Date addWeek(Date date, int weekCount) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.WEEK_OF_YEAR, weekCount);
		return calendar.getTime();
	}

	/**
	 * 调整日期
	 * 
	 * @param date
	 * @param monthCount
	 *            增加（正数）或减少（负数）的周数
	 * @return
	 */
	public static Date addMonth(Date date, int monthCount) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, monthCount);
		return calendar.getTime();
	}

	/**
	 * 获取当前时间，但是需要设置时，分。妙和毫秒设置的都是零
	 * 
	 * @param hour
	 * @param minute
	 * @return
	 */
	public static Date getDate(Date date, Integer dayOfMonth, int hour,
			int minute) {
		Calendar calendar = Calendar.getInstance();
		if (date == null) {
			calendar.setTime(new Date());
		} else {
			calendar.setTime(date);
		}

		if (dayOfMonth != null) {
			calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth.intValue());
		}

		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	/**
	 * 获取当前时间为星期几，周一为1，周二为2......周日为7
	 * 
	 * @param date
	 * @return
	 */
	public static int getDayOfWeek(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		if (dayOfWeek == 0) {
			return 7;
		}
		return dayOfWeek;
	}
	
	/**
	 * 获取昨天的开始时间
	 * @return
	 */
	public static Date getYesterdayStartTime(){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DAY_OF_YEAR, -1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);  
		calendar.set(Calendar.MINUTE, 0);  
		calendar.set(Calendar.SECOND, 0);
		return calendar.getTime();
	}
	
	/**
	 * 获取昨天的结束时间
	 * @return
	 */
	public static Date getYesterdayEndTime(){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DAY_OF_YEAR, -1);
		calendar.set(Calendar.HOUR_OF_DAY, 23);  
		calendar.set(Calendar.MINUTE, 59);  
		calendar.set(Calendar.SECOND, 59);
		return calendar.getTime();
	}
}
