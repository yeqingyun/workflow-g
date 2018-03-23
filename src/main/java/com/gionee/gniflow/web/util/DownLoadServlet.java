/**
 * @(#) DownLoadServlet.java Created on 2014年4月14日
 *
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.gionee.gniflow.web.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.TaskService;
import org.activiti.engine.task.Attachment;

/**
 * The class <code>DownLoadServlet</code>
 * 
 * @author lipw
 * @version 1.0
 */
public class DownLoadServlet extends HttpServlet {

	private static final long serialVersionUID = -5664421270038051541L;

	public static final int BUFFER_SIZE = 8096;

	public static final String CONTENT_DOWNLOAD_TYPE = "application/x-download; charset=utf-8";

	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String attachmentId = request.getParameter("attachmentId");
		
		TaskService taskService = (TaskService) SpringTools.getBean(TaskService.class);
		Attachment attachment = taskService.getAttachment(attachmentId);

		response.setContentType(attachment.getType()+ "; charset=UTF-8");
		response.setHeader("Content-disposition", "attachment; filename="
				+ URLEncoder.encode(attachment.getDescription(), "UTF-8"));
		BufferedInputStream ins = new BufferedInputStream(taskService.getAttachmentContent(attachmentId));
		BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
		int i = 0;
		byte[] b = new byte[8096];
		while ((i = ins.read(b)) != -1)
			out.write(b, 0, i);
		out.flush();
		out.close();
		ins.close();
	}

}
