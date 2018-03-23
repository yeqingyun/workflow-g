package com.gionee.gniflow.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gionee.gnif.dto.QueryMap;
import com.gionee.gnif.model.PageResult;
import com.gionee.gniflow.biz.model.BpmProcessConf;
import com.gionee.gniflow.biz.service.BpmProcessConfService;
import com.gionee.gniflow.web.easyui.AjaxJson;

@RequestMapping("/bpmProcessConf")
@Controller
public class BpmProcessConfController {
	

	
	@Autowired
	private BpmProcessConfService bpmProcessConfService;
	
	  /** 加载数据 */
    @RequestMapping("/loadAllProcessConf.json")
    @ResponseBody
    public PageResult<BpmProcessConf> loadAllDictionary(QueryMap queryMap){
    	PageResult<BpmProcessConf> pageResult = bpmProcessConfService.queryPage(queryMap);
    	return pageResult;
    }
    
    /** 保存流程配置信息 */
    @RequestMapping(value="/saveProcessConf.html", method=RequestMethod.POST)
    @ResponseBody
    public AjaxJson saveDictionary(BpmProcessConf bpmProcessConf){
    	AjaxJson ajaxJson = null;
    	try {
    		bpmProcessConfService.save(bpmProcessConf);
    		ajaxJson = new AjaxJson(true, "保存流程配置成功!");
		} catch (Exception e) {
			e.printStackTrace();
			ajaxJson = new AjaxJson(false, "保存流程配置失败!");
		}
    	return ajaxJson;
    }
    
    
    

}
