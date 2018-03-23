/**
 * @(#) ProcessStatusConverter.java Created on 2014年12月8日
 *
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.gionee.gniflow.web.util.json;

import com.fasterxml.jackson.databind.util.StdConverter;

/**
 * The class <code>ProcessStatusConverter</code>
 *
 * @author lipw
 * @version 1.0
 */
public class ProcessStatusConverter extends StdConverter<Integer, String>{

	@Override
	public String convert(Integer value) {
		switch (value) {
		case 1:
			return "<span style=\"color:red\">运行中</span>";
		case 2:
			return "<span style=\"color:blue\">已完成</span>";
		case 3:
			return "<span style=\"color:#005831\">已挂起</span>";
		case 4 :
			return "<span style=\"color:#c88400\">已激活</span>";
		case 5:
			return "<span style=\"color:#840228\">已作废</span>";
		default:
			return "<span style=\"color:#008792\">未知状态</span>";
		}
	}
}
