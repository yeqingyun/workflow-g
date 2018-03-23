/**
 * @(#) ProcessConvertUtil.java Created on 2014年10月21日
 *
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.gionee.gniflow.web.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.task.Task;
import org.springframework.beans.BeanUtils;

import com.gionee.gniflow.dto.TaskDto;
import com.gionee.gniflow.web.bmp.BpmTaskEntity;

/**
 * The class <code>ProcessConvertUtil</code>
 * 
 * @author lipw
 * @version 1.0
 */
public class ProcessConvertUtil {

	public static List<TaskDto> convert2TaskDto(List<BpmTaskEntity> tasks) {
		List<TaskDto> dtoList = new ArrayList<TaskDto>();
		TaskDto dto = null;
		for (Task task : tasks) {
			dto = new TaskDto();
			BeanUtils.copyProperties(task, dto);
			dto.setProStartUserName(dto.getProStartUserName() );
			//dto.setProStartUserName("[" + dto.getProStartUserId() + "]" + dto.getProStartUserName() );
			dto.setStartTime(task.getCreateTime());
			dtoList.add(dto);
		}
		return dtoList;
	}

	/**
	 * 将模板对象输出到客户端
	 * 
	 * @param response
	 * @param obj
	 * @throws IOException
	 */
	public static void processObj2ClientString(HttpServletResponse response,
			Object obj) throws IOException {
		response.setContentType("text/html; charset=utf-8");
		response.setCharacterEncoding("utf-8");
		response.getWriter().write(obj.toString());
	}
}
