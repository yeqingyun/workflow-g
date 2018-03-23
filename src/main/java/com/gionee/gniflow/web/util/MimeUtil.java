/**
 * @(#) MimeUtil.java Created on 2015年3月20日
 *
 * Copyright (c) 2015 GIONEE. All Rights Reserved
 */
package com.gionee.gniflow.web.util;

import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;

/**
 * The class <code>MimeUtil</code>
 *
 * @author lipw
 * @version 1.0
 */
public class MimeUtil {
	
	/**
	 * 处理浏览器不同，导出时名字乱码问题
	 * @param request
	 * @param srcFileName
	 * @return
	 */
	public static String encodeFilename(HttpServletRequest request, String srcFileName){
   	 	String agent = request.getHeader("USER-AGENT");   
   	 	String targetFileName = "";
        try {    
          if ((agent != null) && (agent.toUpperCase().indexOf("MSIE") > 0)) {
        	  targetFileName = URLEncoder.encode(srcFileName, "UTF-8");    
          } else if ((agent != null) && (-1 != agent.toUpperCase().indexOf("MOZILLA"))){
        	  targetFileName = new String(srcFileName.getBytes("UTF-8"),"iso-8859-1");
          } 
          return targetFileName;
        } catch (Exception ex) {    
          return srcFileName;    
        }
	}
}
