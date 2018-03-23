/**
 * @(#) BpmCommCodeController.java Created on 2014年5月28日
 *
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.gionee.gniflow.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gionee.gnif.dto.QueryMap;
import com.gionee.gnif.model.PageResult;
import com.gionee.gnif.web.util.ResponseFactory;
import com.gionee.gniflow.biz.model.BpmCommCode;
import com.gionee.gniflow.biz.service.BpmCommCodeService;
import com.gionee.gniflow.web.easyui.AjaxJson;
import com.gionee.gniflow.web.easyui.ComboBoxData;
import com.gionee.gniflow.web.response.BpmCommCodeResponse;

/**
 * The class <code>BpmCommCodeController</code>
 * 
 * @author lipw
 * @version 1.0
 */
@RequestMapping("/bpmCommCode")
@Controller
public class BpmCommCodeController {
	
	private static final Logger logger = LoggerFactory.getLogger(BpmCommCodeController.class);

	@Autowired
	private BpmCommCodeService bpmCommCodeService;

	
	/** 根据分类加载数据 */
    @RequestMapping("/loadDateForCategory.json")
    @ResponseBody
    public List<ComboBoxData> loadDateForCategory(@RequestParam("catagory") String catagory) {
    	QueryMap queryMap = new QueryMap();
    	queryMap.put("catagory", catagory); 
    	queryMap.put("status", BpmCommCode.STATUS_NORMAL);
    	queryMap.put("order", "asc");
    	
    	List<ComboBoxData> comboData = new ArrayList<ComboBoxData>();
    	try {
    		List<BpmCommCode> bpmCommCodes = bpmCommCodeService.query(queryMap);
        	
        	for (BpmCommCode code : bpmCommCodes) {
        		comboData.add(new ComboBoxData(code.getCode(), code.getName()));
        	}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
        return comboData;
    }
    
    @RequestMapping("/loadSysLike.json")
    @ResponseBody
    public List<ComboBoxData> loadSysLike(@RequestParam("catagory") String catagory) {
    	QueryMap queryMap = new QueryMap();
    	queryMap.put("catagory", catagory); 
    	queryMap.put("order", "asc");
    	List<ComboBoxData> comboData = new ArrayList<ComboBoxData>();
    	try {
    		List<BpmCommCode> bpmCommCodes = bpmCommCodeService.loadSysLike(queryMap);
        	
        	for (BpmCommCode code : bpmCommCodes) {
        		comboData.add(new ComboBoxData(code.getCode(), code.getName()));
        	}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
        return comboData;
    }
    
    
    
    
    /** 根据分类加载数据 */
    @RequestMapping("/loadAllDictionary.json")
    @ResponseBody
    public List<BpmCommCodeResponse> loadAllDictionary(QueryMap queryMap){
    	PageResult<BpmCommCode> pageResult = bpmCommCodeService.queryPage(queryMap);
    	return ResponseFactory.convertList(pageResult.getRows(), BpmCommCodeResponse.class);
    }
    /** 保存业务字典 */
    @RequestMapping(value="/saveDictionary.html", method=RequestMethod.POST)
    @ResponseBody
    public AjaxJson saveDictionary(BpmCommCode bpmCommCode){
    	AjaxJson ajaxJson = null;
    	try {
    		bpmCommCodeService.save(bpmCommCode);
    		ajaxJson = new AjaxJson(true, "保存业务字典成功!");
		} catch (Exception e) {
			e.printStackTrace();
			ajaxJson = new AjaxJson(false, "保存业务字典失败!");
		}
    	return ajaxJson;
    }
    
    /** 删除业务字典 */
    @RequestMapping(value="/deleteDictionary.json", method=RequestMethod.POST)
    @ResponseBody
    public AjaxJson deleteCategory(@RequestParam("id") int id) {
    	AjaxJson ajaxJson = null;
    	try {
    		bpmCommCodeService.deleteDictionary(id);
    		ajaxJson = new AjaxJson(true, "删除业务字典成功!");
		} catch (Exception e) {
			e.printStackTrace();
			ajaxJson = new AjaxJson(false, "删除业务字典失败!");
		}
    	return  ajaxJson;
    }
    
}
