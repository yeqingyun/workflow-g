package com.gionee.gniflow.web.controller;


import java.io.BufferedOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.ManagementService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.impl.interceptor.Command;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gionee.gnif.dto.QueryMap;
import com.gionee.gniflow.biz.model.QuitApplicationInfo;
import com.gionee.gniflow.biz.service.QuitApplicationInfoService;
import com.gionee.gniflow.constant.WebReqConstant;
import com.gionee.gniflow.web.cmd.quit.DrawWordTemplateCmd;
import com.gionee.gniflow.web.easyui.AjaxJson;



@RequestMapping("/quitAudi")
@Controller
public class QuitAudiController {
	@Autowired
	private QuitApplicationInfoService quitService;
	
	@Autowired
	private RuntimeService runtimeService;
	
	@Autowired
	private ManagementService managementService;
 	/** 查找申请离职时间**/
	@RequestMapping("/applationQuitDate.json")
	@ResponseBody
	public AjaxJson getApplicationQuitDate(String account,String state){
		QuitApplicationInfo quitInfo = null;
		AjaxJson ajaxJson = new AjaxJson(true, null);
		QueryMap query =new QueryMap();
		query.put("account", account);
		query.put("state", state);
		List<QuitApplicationInfo> list =null;
		list=quitService.query(query);
		String applicationdate=null;
		if(!CollectionUtils.isEmpty(list)){
			quitInfo=list.get(0);
			Date date=quitInfo.getApplicationDate();
			SimpleDateFormat sdate = new SimpleDateFormat("yyyy-MM-dd");
			applicationdate=sdate.format(date);
			ajaxJson=new AjaxJson(true, "");
			ajaxJson.setObj(applicationdate);
		}else{
			ajaxJson=new AjaxJson(false, "你还没有走离职申请流程！");
		}
	
		return ajaxJson;
	}
	
	/** 查找申请离职信息**/
	@RequestMapping("/getApplationQuitInfo.json")
	@ResponseBody
	public AjaxJson getApplicationQuitInfo(String account,String state){
		QuitApplicationInfo quitInfo = null;
		AjaxJson ajaxJson = new AjaxJson(true, null);
		QueryMap query =new QueryMap();
		query.put("account", account);
		query.put("state", state);
		List<QuitApplicationInfo> list =null;
		list=quitService.query(query);
		if(!CollectionUtils.isEmpty(list)){
			quitInfo=list.get(0);
			ajaxJson=new AjaxJson(true, "");
			ajaxJson.setObj(quitInfo);
		}else{
			ajaxJson=new AjaxJson(false, "你还没有走申请流程！");
		}
	
		return ajaxJson;
	}
	/** 下载离职证明模板 */
	@RequestMapping("/downLoadTemplatePage.html")
    public void downLoadWordPage(@RequestParam("processInstanceId") String processInstanceId,HttpServletRequest request,
            HttpServletResponse response) throws Exception {
		String appPath =  request.getSession().getServletContext().getRealPath("/");
		String filePath = WebReqConstant.TEMPLATE_PATH;
		String fileName = "";
		String fullpath = appPath + filePath;
		//String fullpath = "D:\\";
		Command<XWPFDocument> cmd = null;
			fileName = "Resignation.docx";
			fullpath = fullpath + fileName;
			cmd = new DrawWordTemplateCmd(processInstanceId, fullpath);
		XWPFDocument document = managementService.executeCommand(cmd);

		response.setContentType("application/x-download; charset=utf-8");
        response.addHeader("Content-Disposition", "attachment;filename="
        		+new String("离职证明.docx".getBytes("gbk"),"iso-8859-1"));
		BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
		document.write(out);
		out.flush();
		out.close();
    }


}
