/**
 * @(#) BpmService.java Created on 2014年9月11日
 *
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.gionee.gniflow.biz.service;

import java.util.List;
import java.util.Map;

import com.gionee.gniflow.web.bmp.ProcessHisVariable;
import com.gionee.gniflow.web.bmp.ProcessStep;

/**
 * The class <code>BpmService</code>
 *
 * @author lipw
 * @version 1.0
 */
public interface BpmService {
	/**
	 * 获取历史表单对象及变量
	 * @param model
	 * 			流程变量
	 * @param processInstanceId
	 * 			流程实例
	 * @param onlySign
	 * 			是否只获取会签的历史节点列表
	 * @return
	 */
	public Map<String,Object> getHisProcesVariable(Map<String, Object> model, String processInstanceId,
			boolean onlySign);
	/**
	 * 获取打印表单整个节点
	 * @param model
	 * @param processInstanceId
	 * @param onlySign
	 * @return
	 */
	public Map<String,Object> getEntireProcesVariable(Map<String, Object> model, String processInstanceId,
			boolean onlySign);
	
	/**
	 * 获取会签的的历史节点列表
	 * @param processInstanceId
	 * @return
	 */
	public List<ProcessStep> getSignProcessStepList(String processInstanceId);
	
	/**
	 * 获取流程的所有运行时的流程变量
	 * @param processInstanceId
	 * @return
	 */
	public ProcessHisVariable getAllHisVariables(String processInstanceId);
}
