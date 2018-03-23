package com.gionee.gniflow.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gionee.gnif.dto.QueryMap;
import com.gionee.gnif.model.PageResult;
import com.gionee.gnif.web.util.ResponseFactory;
import com.gionee.gniflow.biz.model.HardwareType;
import com.gionee.gniflow.biz.service.HardwareTypeService;
import com.gionee.gniflow.web.easyui.AjaxJson;
import com.gionee.gniflow.web.easyui.GridPageData;
import com.gionee.gniflow.web.response.HardwareTypeResponse;


@Controller
@RequestMapping("/hardware")
public class HardwareTypeController {

	//private static final Logger logger = LoggerFactory.getLogger(HardwardTypeController.class);
	
	@Autowired
	private HardwareTypeService hardwareTypeService;

	/**查询问题类型*//*
	@RequestMapping("/queryPage.json")
	@ResponseBody
	public PageResult<HardwareType> loadHardwareType(QueryMap critera) {
		return hardwareTypeService.queryPage(critera);
	}*/
	  /** 加载问题类型 */
    @RequestMapping("/query.json")
    @ResponseBody
    public GridPageData<HardwareTypeResponse> loadHardwareType(QueryMap queryMap){
    	PageResult<HardwareType> pageResult = hardwareTypeService.queryPage(queryMap);
    	GridPageData<HardwareTypeResponse> gridPageData = 
    			new GridPageData<HardwareTypeResponse>(ResponseFactory.convertList(pageResult.getRows(), HardwareTypeResponse.class),pageResult.getTotal().longValue());
    	return gridPageData;
    }
	/**保存问题类型*/
	@RequestMapping(value="/save.json", method=RequestMethod.POST)
	@ResponseBody
	public AjaxJson saveHardwareType(HardwareType hardwareType) {
		AjaxJson ajaxJson= null;
		 try {
			 	hardwareTypeService.save(hardwareType);
	    		ajaxJson = new AjaxJson(true, "保存问题类型成功!");
	    		//System.out.println(ajaxJson);
			} catch (Exception e) {
				e.printStackTrace();
				ajaxJson = new AjaxJson(false, "保存问题类型失败!");
			}
		 return ajaxJson;
	}
	 /** 删除问题类型*/
    @RequestMapping(value="/delete.json", method=RequestMethod.POST)
    @ResponseBody
    public AjaxJson deleteHardwareType(@RequestParam("id") int id) {
    	AjaxJson ajaxJson = null;
    	try {
    		hardwareTypeService.delete(id);
    		ajaxJson = new AjaxJson(true, "删除问题类型成功!");
		} catch (Exception e) {
			e.printStackTrace();
			ajaxJson = new AjaxJson(false, "删除问题类型失败!");
		}
    	return  ajaxJson;
    }
}
