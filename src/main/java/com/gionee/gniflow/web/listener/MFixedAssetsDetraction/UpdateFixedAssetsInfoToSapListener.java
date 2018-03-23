package com.gionee.gniflow.web.listener.MFixedAssetsDetraction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gionee.gniflow.biz.service.ProcessHelpService;
import com.gionee.gniflow.sap.SapUtil;
import com.gionee.gniflow.web.listener.MLeaveApplication.InsertLeaveAppInfoToSapLister;
import com.gionee.gniflow.web.util.SpringTools;
import com.gionee.gniflow.web.support.AudiException;

public class UpdateFixedAssetsInfoToSapListener implements ExecutionListener {
	
	private static final long serialVersionUID = -4554160911262582345L;
	private Logger logger = LoggerFactory.getLogger(UpdateFixedAssetsInfoToSapListener.class);
	private ProcessHelpService processHelpService;

	@Override
	public void notify(DelegateExecution execution) throws Exception {
		logger.debug("Running in UpdateFixedAssetsInfoToSapListener begin!");
		RuntimeService runtimeService = execution.getEngineServices().getRuntimeService();
		processHelpService = (ProcessHelpService) SpringTools.getBean(ProcessHelpService.class);
		//获取资产信息
		String fixedAssetsInfo = (String)runtimeService.getVariable(execution.getId(), "assetMFixedAssetsDetraction");
		String scrapWay2JsonStr = (String)runtimeService.getVariable(execution.getId(), "scrapWay2JsonStr");
		String updateScrapWay2JsonStr = (String)runtimeService.getVariable(execution.getId(), "updateScrapWay2JsonStr");
		String scrapWay=(String)runtimeService.getVariable(execution.getId(),"scrapWay");
		
		Map<String,Object> map=null;
		if("1".equals(scrapWay)){
			map=processHelpService.parseJsonToMap(fixedAssetsInfo);
		}else if(updateScrapWay2JsonStr==null){
			map=processHelpService.parseJsonToMap(scrapWay2JsonStr);
		}else{
			map=processHelpService.parseJsonToMap(updateScrapWay2JsonStr);
		}
		List<Map<String,Object>> assets=(List<Map<String,Object>>)map.get("accessoryData");
		
		if("2".equals(scrapWay)){
			for(Map<String,Object> asset:assets){
				String assetsNo="0000000000"+(String)asset.get("assetsNo");
				Map<String,String> param = new HashMap<String,String>();
				param.put("I_EQUNR",assetsNo);
				param.put("I_BUKRS", "");
				param.put("I_TYPE","G");
				SapUtil sap=new SapUtil();
				String message=sap.writeToSapReturnStatusAndMessage("ZHR_OA44", param);//ZHR_OA44为更新固定资产状态的接口名
				String message1 = message.substring(0, 1);
				String message2 = message.substring(1,message.length());
				if("4".equals(message1)){
					logger.debug("SAP保存数据反馈信息："+message2);
					throw new AudiException("SAP保存数据反馈信息："+message2);
				}else if("".equals(message1) || "S".equals(message1)){
					logger.debug("SAP处理请求成功并返回");
				}
			}
		}else{
			for(Map<String,Object> asset:assets){
				String isScrap=(String)asset.get("isScrap");
				String assetsNo="0000000000"+(String)asset.get("assetsNo");
				if("是".equals(isScrap)){
					Map<String,String> param = new HashMap<String,String>();
					param.put("I_EQUNR",assetsNo);
					param.put("I_BUKRS", "");
					param.put("I_TYPE","G");
					SapUtil sap=new SapUtil();
					String message=sap.writeToSapReturnStatusAndMessage("ZHR_OA44", param);//ZHR_OA44为更新固定资产状态的接口名
					String message1 = message.substring(0, 1);
					String message2 = message.substring(1,message.length());
					if("4".equals(message1)){
						logger.debug("SAP保存数据反馈信息："+message2);
						throw new AudiException("SAP保存数据反馈信息："+message2);
					}else if("".equals(message1) || "S".equals(message1)){
						logger.debug("SAP处理请求成功并返回");
					}
				}
			}
		}
	}

}
