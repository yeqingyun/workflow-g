package com.gionee.gniflow.web.listener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.gionee.gnif.GnifException;
import com.gionee.gniflow.biz.service.ProcessHelpService;
import com.gionee.gniflow.sap.SapUtil;
import com.gionee.gniflow.web.support.AudiException;
import com.gionee.gniflow.web.util.SpringTools;
public class SaveRewardandpunishmentSapListener implements ExecutionListener{
    
	
	private static final long serialVersionUID = -4554160911262582345L;
	private Logger logger = LoggerFactory.getLogger(SaveRewardandpunishmentSapListener.class);
	private ProcessHelpService processHelpService;
	@Override
	public void notify(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		logger.debug("Running in SaveRewardandpunishmentSapListener begin!");
		RuntimeService runtimeService = execution.getEngineServices().getRuntimeService();
		        //获取奖惩信息
		         String rapList=(String)runtimeService.getVariable(execution.getId(), "rapList");
		     	processHelpService = (ProcessHelpService) SpringTools.getBean(ProcessHelpService.class);
				if(StringUtils.isNotEmpty(rapList)){
				try {
					Map<String,Object> map=processHelpService.parseJsonToMap(rapList);
					List<Map<String,String>> assets=(List<Map<String,String>>)map.get("rapList");
					for(Map<String,String> asset:assets){
							SapUtil sap=new SapUtil();
							asset.put("I_ZFLNR", execution.getId());
							String message=sap.writeToSapReturnStatusAndMessage("ZHR_OA42", asset);//ZHR_OA42为写入奖惩接口名
							String message1 = message.substring(0, 1);
							String message2 = message.substring(1,message.length());
							if(!("0".equals(message1))){
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
			}else if(StringUtils.isEmpty(rapList)){
		        //申请人
				String applyUserAccount = (String)runtimeService.getVariable(execution.getId(), "applyUserAccount");
				//姓名
				String userName = (String)runtimeService.getVariable(execution.getId(), "userName");
				//部门
				String orgName = (String)runtimeService.getVariable(execution.getId(), "orgName");//操作类型
				
				String reqChannels  = (String)runtimeService.getVariable(execution.getId(), "reqChannels");
				//奖惩类别
				String rank=reqChannels.equals("2")?"0002罚金":"0001奖金";
				//奖惩级别
				String money=reqChannels.equals("2")?"27罚金":"15奖金";
				//奖惩原因
				String rewardAndPenaltyDesc  = (String)runtimeService.getVariable(execution.getId(), "rewardAndPenaltyDesc");
				//奖惩分数
				String rpScore  = (String)runtimeService.getVariable(execution.getId(), "rpScore");
				//奖惩总金额
				String showMoneySum  = (String)runtimeService.getVariable(execution.getId(), "showMoneySum");
		        //备注
				String rewardAndPenaltySug  = (String)runtimeService.getVariable(execution.getId(), "rewardAndPenaltySug");
				//日期 
				String inTransferDate  = (String)runtimeService.getVariable(execution.getId(), "inTransferDate");
				//将结果集存入map
				Map<String,String> map = new HashMap<String,String>();
				try {
					map.put("I_PERNR", applyUserAccount);
					map.put("I_BEGDA", inTransferDate);
					map.put("I_ENDDA", inTransferDate);
					map.put("I_ZJCLB", rank);
					map.put("I_ZJCJC", money);
					map.put("I_ZJCYY", rewardAndPenaltyDesc);
					map.put("I_ZJCBM", orgName);
					map.put("I_ZJCFS", rpScore);
					map.put("I_ZJCJE", showMoneySum);
					if(StringUtils.isNotEmpty(rewardAndPenaltySug)){
						map.put("I_ZJCBZ", rewardAndPenaltySug);
					}
					SapUtil sap=new SapUtil();
					String message=sap.writeToSapReturnStatusAndMessage("ZHR_OA42", map);//ZHR_OA13为写入离职接口名
					String message1 = message.substring(0, 1);
					String message2 = message.substring(1,message.length());
					if(!("0".equals(message1))){
						throw new AudiException("SAP保存数据反馈信息："+message2);
					}

				} catch (Exception e) {
					e.printStackTrace();
					logger.error(e.getMessage());
					if(e instanceof AudiException)
						throw e;
					throw new GnifException("Failure of SaveInfoTo with SAP!");
				}
		    }
	}
}
