/**
 * @(#) BpmConfNodeSendController.java Created on 2014年5月28日
 *
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.gionee.gniflow.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gionee.gnif.dto.QueryMap;
import com.gionee.gnif.model.PageResult;
import com.gionee.gnif.web.util.ResponseFactory;
import com.gionee.gniflow.biz.model.BpmEmailTemplate;
import com.gionee.gniflow.biz.service.BpmEmailTemplateService;
import com.gionee.gniflow.web.easyui.AjaxJson;
import com.gionee.gniflow.web.response.BpmEmailTemplateResponse;

/**
 * The class <code>BpmConfNodeSendController</code>
 * 
 * @author lipw
 * @version 1.0
 */
@RequestMapping("/bpmEmailTemplate")
@Controller
public class BpmEmailTemplateController {
	
	@Autowired
	private BpmEmailTemplateService bpmEmailTemplateService;
	
    @RequestMapping("/showAllEmailTemplate.json")
    @ResponseBody
    public List<BpmEmailTemplateResponse> showAllEmailTemplate(QueryMap queryMap) {
    	queryMap.put("status", BpmEmailTemplate.STATUS_NORMAL);
    	PageResult<BpmEmailTemplate> list = bpmEmailTemplateService.queryPage(queryMap);
        return ResponseFactory.convertList(list.getRows(), BpmEmailTemplateResponse.class);
    }
    
    @RequestMapping(value="/saveEmailTemplate.html",method=RequestMethod.POST)
    @ResponseBody
    public AjaxJson saveEmailTemplate(BpmEmailTemplate bpmEmailTemplate ){
    	AjaxJson ajaxJson = null;
    	try {
    		bpmEmailTemplateService.save(bpmEmailTemplate);
    		ajaxJson = new AjaxJson(true, "保存邮件模板成功!");
		} catch (Exception e) {
			e.printStackTrace();
			ajaxJson = new AjaxJson(false, "保存邮件模板失败!");
		}
    	return ajaxJson;
    }
    
    @RequestMapping(value="/deleteEmailTemplate.json",method=RequestMethod.POST)
    @ResponseBody
    public AjaxJson deleteEmailTemplate(@RequestParam("id") int id ){
    	AjaxJson ajaxJson = null;
    	try {
    		bpmEmailTemplateService.delete(id);
    		ajaxJson = new AjaxJson(true, "删除邮件模板成功!");
		} catch (Exception e) {
			e.printStackTrace();
			ajaxJson = new AjaxJson(false, "删除邮件模板失败!");
		}
    	return ajaxJson;
    }
}
