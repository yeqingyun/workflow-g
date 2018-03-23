/**
 * @(#) SapMessageUtil.java Created on 2014年7月31日
 *
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.gionee.gniflow.web.util;

import java.util.List;
import java.util.Map;

/**
 * The class <code>SapMessageUtil</code>
 *
 * @author lipw
 * @version 1.0
 */
public final class SapMessageUtil {
	
	private SapMessageUtil(){
		
	}
	/**
	 * 将Map中的错误信息拼成一行
	 * @param sapMsgMap
	 * @return
	 */
	public static String formatSapMessage(Map<String, String> sapMsgMap){
		StringBuffer sb = new StringBuffer();
		for (Map.Entry<String, String> entry : sapMsgMap.entrySet()) {
			sb.append(entry.getKey()).append(":")
			.append(entry.getValue()).append("!")
			.append("   ");
		}
		return sb.toString();
	}
	
	/**
	 * List中的元素转换为字符串
	 * @param list
	 * @return
	 */
	public static String listToString(List<String> list){
		StringBuffer sb = new StringBuffer();
		for (String temp: list) {
			sb.append(temp).append(",");
		}
		sb = sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}
}
