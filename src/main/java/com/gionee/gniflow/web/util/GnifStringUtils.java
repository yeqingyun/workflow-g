/**
 * @(#) GnifStringUtils.java Created on 2014年12月29日
 *
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.gionee.gniflow.web.util;

import java.util.regex.Pattern;

/**
 * The class <code>GnifStringUtils</code>
 * 
 * @author lipw
 * @version 1.0
 */
public final class GnifStringUtils {
	/**
	 * 数组元素转换为字符串
	 * 
	 * @param join
	 * @param strAry
	 * @return
	 */
	public static String join(String join, String[] strAry) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < strAry.length; i++) {
			if (i == (strAry.length - 1)) {
				sb.append(strAry[i]);
			} else {
				sb.append(strAry[i]).append(join);
			}
		}

		return new String(sb);
	}

	/**
	 * 判断字符串在不在数组中
	 * 
	 * @param tempStr
	 * @param arr
	 * @return
	 */
	public static Boolean inArr(String tempStr, String[] arr) {
		boolean result = false;
		for (String str : arr) {
			if (str.equals(tempStr)) {
				result = true;
				break;
			}
		}
		return result;
	}
	
	/**
	 * 判断字符串是不是数字
	 * 
	 * @param type
	 * @param value
	 * @return
	 */
	public static boolean isNumer(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(str).matches();
	}
}
