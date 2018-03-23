/**
 * @(#) SignFileController.java Created on 2014年5月28日
 *
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.gionee.gniflow.web.controller;

import java.io.BufferedOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.ManagementService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.impl.interceptor.Command;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gionee.auth.model.User;
import com.gionee.gnif.dto.QueryMap;
import com.gionee.gnif.web.util.ResponseFactory;
import com.gionee.gniflow.biz.model.SignFileInformation;
import com.gionee.gniflow.biz.model.SignOrgCode;
import com.gionee.gniflow.biz.service.SignFileInformationService;
import com.gionee.gniflow.constant.BPMConstant;
import com.gionee.gniflow.biz.service.SignOrgCodeService;
import com.gionee.gniflow.constant.WebReqConstant;
import com.gionee.gniflow.web.cmd.filesign.DrawTableWordPageCmd;
import com.gionee.gniflow.web.cmd.filesign.DrawWordPageCmd;
import com.gionee.gniflow.web.easyui.AjaxJson;
import com.gionee.gniflow.web.easyui.GridPageData;
import com.gionee.gniflow.web.response.SignFileInformationResponse;
import com.gionee.gniflow.web.util.DateUtil;
import com.gionee.gniflow.web.util.HttpClientUtil;
import com.gionee.gniflow.web.util.PropertyHolder;

/**
 * The class <code>SignFileController</code>
 * 
 * @author lipw
 * @version 1.0
 */
@RequestMapping("/signFile")
@Controller
public class SignFileController {
	
	@Autowired
	private SignFileInformationService signFileInformationService;
	
	@Autowired
	private  SignOrgCodeService signOrgCodeService;
	
	@Autowired
	private RuntimeService runtimeService;
	
	@Autowired
	private ManagementService managementService;

	/** 检查文档会签流程*/
	@RequestMapping("/checkSignFileProcess.json")
	@ResponseBody
	public AjaxJson checkSignFileProcess(Integer operateType, String fileNo) {
		AjaxJson ajaxJson = new AjaxJson(true, null);
		List<SignFileInformation> list = null;
		try {
			QueryMap queryMap = new QueryMap();
			if (operateType == WebReqConstant.SIGN_FILE_OPERATE_TYPE_MODIFY ||
					operateType == WebReqConstant.SIGN_FILE_OPERATE_TYPE_ABOLISH) {
				queryMap.put("audiStatus", WebReqConstant.SIGN_FILE_PRO_AUDIING);//审核中
				queryMap.put("fileNo", fileNo);//文件编号
				queryMap.put("fileOperateType", WebReqConstant.SIGN_FILE_OPERATE_TYPE_MODIFY);//修订操作
				queryMap.put("status", BPMConstant.STATUS_NORMAL);
				list = signFileInformationService.querySignFileInformation(queryMap);
				if (!CollectionUtils.isEmpty(list)) {
					if (operateType == WebReqConstant.SIGN_FILE_OPERATE_TYPE_MODIFY) {
						ajaxJson = new AjaxJson(false,"此文档已在修订中，未审批完成时，不能再次修订此文档!");
					} else if (operateType == WebReqConstant.SIGN_FILE_OPERATE_TYPE_ABOLISH) {
						ajaxJson = new AjaxJson(false,"此文档已在修订中，未审批完成时，不能废止此文档!");
					}
					
				}
				
				queryMap.put("fileOperateType", WebReqConstant.SIGN_FILE_OPERATE_TYPE_ABOLISH);//废止操作
				list = signFileInformationService.querySignFileInformation(queryMap);
				if (!CollectionUtils.isEmpty(list)) {
					if (operateType == WebReqConstant.SIGN_FILE_OPERATE_TYPE_MODIFY) {
						ajaxJson = new AjaxJson(false,"此文档已经在废止中，未审批完成时，不能修订此文档!");
					} else if (operateType == WebReqConstant.SIGN_FILE_OPERATE_TYPE_ABOLISH) {
						ajaxJson = new AjaxJson(false,"此文档已经在废止中，未审批完成时，不能再次废止此文档!");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			ajaxJson = new AjaxJson(true, "服务器请求数据错误!");
		}
		return ajaxJson;
	}
	/** 查找可修订的文件**/
	@RequestMapping("/enablefiles.json")
	@ResponseBody
	public GridPageData<SignFileInformationResponse> getEnablefiles() {
		QueryMap queryMap = new QueryMap();
		List<SignFileInformation> files = null;
		queryMap.put("delFlag", WebReqConstant.DEL_NO);
		files = signFileInformationService.querySignFileInformation(queryMap);
		
		return new GridPageData<SignFileInformationResponse>(
        		ResponseFactory.convertList(files, SignFileInformationResponse.class),(long)files.size());
	}
	
	@RequestMapping("/queryFileForOA.json")
	@ResponseBody
	public GridPageData<SignFileInformationResponse> queryFileForOA(QueryMap queryMap) {
		String postUrl = PropertyHolder.getContextProperty(WebReqConstant.HR_DOCUEMNT_DOCINFO_KEY);
		Map<String,String> reqData = new HashMap<String, String>();
		reqData.put("document.docName", "t");//必须
		String result = HttpClientUtil.sendPostRequest(postUrl, reqData, WebReqConstant.DEFAULT_CHAR_SET);
//		return signFileInformationService.querySignFileInformation(queryMap);
		
		//OA接口
//		String result = null;
		List<SignFileInformation> fileList = new ArrayList<SignFileInformation>();
		JSONArray myArray =JSONArray.parseArray(result);
		JSONObject jsonObj = null;
		SignFileInformation signFileInfo = null;
        for(int i=0;i<myArray.size();i++) {
        	jsonObj = myArray.getJSONObject(i);
        	signFileInfo = new SignFileInformation();
	    	signFileInfo.setCompanyId(jsonObj.getInteger("comId"));
	    	signFileInfo.setCompanyName(jsonObj.getString("comNm"));
	    	signFileInfo.setOrgId(jsonObj.getInteger("deptId"));
	    	signFileInfo.setOrgName(jsonObj.getString("deptNm"));
	    	signFileInfo.setCreateBy(jsonObj.getString("createByNo"));
	    	signFileInfo.setCreateTime(DateUtil.parse(jsonObj.getString("createDate"), "yyyy-MM-dd HH:mm"));
	    	signFileInfo.setCreaterName(jsonObj.getString("createByNm"));
	    	
	    	signFileInfo.setFileName(jsonObj.getString("docName"));
	    	signFileInfo.setFileNo(jsonObj.getString("docNo"));
	    	signFileInfo.setFileType(jsonObj.getString("categoryCode"));
        	String oaVersion = jsonObj.getString("version");
        	signFileInfo.setFileVersion(oaVersion.substring(0,1));
        	signFileInfo.setFileVersionState(oaVersion.substring(1, oaVersion.length()));
        	
        	fileList.add(signFileInfo);
        }
        return new GridPageData<SignFileInformationResponse>(
        		ResponseFactory.convertList(fileList, SignFileInformationResponse.class),(long)fileList.size());
	}
	
	/** 获取部门缩写编号**/
	@RequestMapping("/getOrgCode.json")
	@ResponseBody
	public AjaxJson getoOrgCode(Integer orgId) {
		AjaxJson ajaxJson = new AjaxJson(true, null);
		try{
	    SignOrgCode orgCode = signOrgCodeService.getSignOrgCode4OrgId(orgId);
	    if(null!=orgCode){
	    	ajaxJson=new AjaxJson(true, "");
	    	ajaxJson.setObj(orgCode);
	    }else{
	    	ajaxJson=new AjaxJson(false, "你所选择的部门没有编号缩写");
	    }
		}catch (Exception e) {
			e.printStackTrace();
			ajaxJson = new AjaxJson(false, "服务器请求数据错误!");
		}
	     return ajaxJson;
	}
	
	/** 查看流程定义图片 */
	@RequestMapping("/downLoadWordPage.html")
    public void downLoadWordPage(@RequestParam("processInstanceId") String processInstanceId,HttpServletRequest request,
            HttpServletResponse response) throws Exception {
		String appPath =  request.getSession().getServletContext().getRealPath("/");
		String filePath = WebReqConstant.TEMPLATE_PATH;
		String fileName = "";
		String fullpath = appPath + filePath;
		
		Object obj = runtimeService.getVariable(processInstanceId, "assigneeList");
		Command<XWPFDocument> cmd = null;
		if (obj == null) {
			fileName = "SimpleDocCover.docx";
			fullpath = fullpath + fileName;
			cmd = new DrawWordPageCmd(processInstanceId, fullpath);
		} else {
			fileName = "TableDocCover.docx";
			fullpath = fullpath + fileName;
			cmd = new DrawTableWordPageCmd(processInstanceId, fullpath);
		}
		XWPFDocument document = managementService.executeCommand(cmd);

		response.setContentType("application/x-download; charset=utf-8");
        response.addHeader("Content-Disposition", "attachment;filename="
        		+new String("文档封面.docx".getBytes("gbk"),"iso-8859-1"));
		BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
		document.write(out);
		out.flush();
		out.close();
    }
	/** 获取分管领导**/
	@RequestMapping("/getFGprsident.json")
	@ResponseBody
	public AjaxJson getFGprsident(@RequestParam String userAccount, @RequestParam String type) {
		AjaxJson ajaxJson = new AjaxJson(true, null);
		User user=new User();
		String reqUrl = PropertyHolder
				.getContextProperty(WebReqConstant.HR_ACCOUNT_ORGLEADER_KEY);

		Map<String, String> reqData = new HashMap<String, String>();
		reqData.put("usr.empId", userAccount);
		reqData.put("usr.wfRule", type);
		try{
		String result = HttpClientUtil.sendPostRequest(reqUrl, reqData, "UTF-8");
		if (StringUtils.isNotEmpty(result)) {
			ajaxJson=new AjaxJson(true, "");
			user.setAccount(result.split("_")[0]);
			user.setName(result.split("_")[1]);
			ajaxJson.setObj(user);
		}else{
			ajaxJson=new AjaxJson(false, "找不到对应的分管副总裁！");
		}
		}
		catch (Exception e) {
			e.printStackTrace();
			ajaxJson = new AjaxJson(false, "服务器请求数据错误!");
		}
		return ajaxJson;
	}
}
