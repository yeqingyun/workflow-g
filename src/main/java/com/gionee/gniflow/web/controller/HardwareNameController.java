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
import com.gionee.gniflow.biz.model.TypeDetail;
import com.gionee.gniflow.biz.service.TypeDetailService;
import com.gionee.gniflow.web.easyui.AjaxJson;
import com.gionee.gniflow.web.easyui.GridPageData;
import com.gionee.gniflow.web.response.TypeDetailResponse;


@Controller
@RequestMapping("/type")
public class HardwareNameController {
	@Autowired
	private TypeDetailService typeDetailService;
	
	 /** 加载问题类型 */
    @RequestMapping("/query.json")
    @ResponseBody
    public GridPageData<TypeDetailResponse> loadHardwareName(QueryMap queryMap){
    	PageResult<TypeDetail> pageResult = typeDetailService.queryPage(queryMap);
    	GridPageData<TypeDetailResponse> gridPageData = 
    			new GridPageData<TypeDetailResponse>(ResponseFactory.convertList(pageResult.getRows(), TypeDetailResponse.class),pageResult.getTotal().longValue());
    	return gridPageData;
    }
    
	/**保存问题类型*/
	@RequestMapping(value="/save.json", method=RequestMethod.POST)
	@ResponseBody
	public AjaxJson saveHardwareName(TypeDetail typeDetail) {
		AjaxJson ajaxJson= null;
		 try {
			 	typeDetailService.save(typeDetail);
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
    public AjaxJson deleteHardwareName(@RequestParam("id") int id) {
    	AjaxJson ajaxJson = null;
    	try {
    		typeDetailService.delete(id);
    		ajaxJson = new AjaxJson(true, "删除问题类型成功!");
		} catch (Exception e) {
			e.printStackTrace();
			ajaxJson = new AjaxJson(false, "删除问题类型失败!");
		}
    	return  ajaxJson;
    }
	
}
