package com.gionee.gniflow.web.listener;

import java.util.List;
import java.util.Map;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gionee.gnif.GnifException;
import com.gionee.gniflow.biz.service.ProcessHelpService;
import com.gionee.gniflow.sap.SapUtil;
import com.gionee.gniflow.web.support.AudiException;
import com.gionee.gniflow.web.util.SpringTools;

public class SaveTrainingToSapListener implements ExecutionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Logger logger = LoggerFactory.getLogger(SaveTrainingToSapListener.class);
	private ProcessHelpService processHelpService;
	@Override
	public void notify(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		logger.debug("Running in SaveTrainingToSapListener begin!");
		RuntimeService runtimeService = execution.getEngineServices().getRuntimeService();
        //获取外训信息
         String trainingList=(String)runtimeService.getVariable(execution.getId(), "trainingList");
     	processHelpService = (ProcessHelpService) SpringTools.getBean(ProcessHelpService.class);
			if(StringUtils.isNotEmpty(trainingList)){
			try {
				Map<String,Object> map=processHelpService.parseJsonToMap(trainingList);
				List<Map<String,String>> assets=(List<Map<String,String>>)map.get("trainingList");
				for(Map<String,String> asset:assets){
						SapUtil sap=new SapUtil();
						String message=sap.writeToSapReturnStatusAndMessage("ZHR_OA43", asset);//ZHR_OA43为写外训接口名
						String message1 = message.substring(0, 1);
						String message2 = message.substring(1,message.length());
						if(!("0".equals(message1))){
							//System.out.println("SAP保存数据反馈信息："+message2);
							throw new AudiException("SAP保存数据反馈信息："+message2);
						}
					}
			} catch (Exception e1) {
				e1.printStackTrace();
				logger.error(e1.getMessage());
				if(e1 instanceof AudiException)
					throw e1;
				throw new GnifException("Failure of SaveInfoTo with SAP!");
			}
		}
	}

}
