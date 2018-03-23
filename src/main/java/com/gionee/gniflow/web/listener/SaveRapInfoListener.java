
package com.gionee.gniflow.web.listener;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gionee.gniflow.biz.model.RapInfo;
import com.gionee.gniflow.biz.service.ProcessHelpService;
import com.gionee.gniflow.biz.service.RapInforService;
import com.gionee.gniflow.web.support.AudiException;
import com.gionee.gniflow.web.util.SpringTools;

public class SaveRapInfoListener implements ExecutionListener {
	private Logger logger = LoggerFactory.getLogger(SaveEmpAndTeacherInfoListener.class);
	//private ProcessHelpService processHelpService;
	private ProcessHelpService processHelpService;
	
	private RapInforService rapInforService;
	
	private static final long serialVersionUID = -8591980217111953495L;

	@Override
	public void notify(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		RuntimeService runtimeService= execution.getEngineServices().getRuntimeService();
		  //获取奖惩信息
        String rapList=(String)runtimeService.getVariable(execution.getId(), "rapList");	
        processHelpService = (ProcessHelpService) SpringTools.getBean(ProcessHelpService.class);
        rapInforService = (RapInforService) SpringTools.getBean(RapInforService.class);
		if(StringUtils.isNotEmpty(rapList)){
			Map<String,Object> map=processHelpService.parseJsonToMap(rapList);
			List<Map<String,String>> assets=(List<Map<String,String>>)map.get("rapList");
			
			for(Map<String,String> asset:assets){
				  //RapInfo rapInfo=new RapInfo();
				
				asset.put("instanceID", execution.getId());
			     /* for(int i=0;i<asset.size();i++){
			    	  System.out.println(asset.get(i));
			      }*/
			      /* rapInfo.setAccount(asset.get("I_PERNR").toString());
				
				
				System.out.println( asset.get(Integer.toString(1)));
				rapInfo.setScore(asset.get("I_ZJCFS"));
				rapInfo.setCause(asset.get("I_ZJCYY").toString());
				rapInfo.setMoney(asset.get("I_ZJCJE").toString());
				rapInfo.setSort(asset.get("I_ZJCLB").toString());
				rapInfo.setRapDate(asset.get("I_BEGDA").toString());*/
				try {
					rapInforService.insert(asset);
				} catch (Exception e) {
					// TODO: handle exception
					throw new RuntimeException("运行出错");
				}
				
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
		   //将结果集存入map 插入rapInfo 
		   Map<String, String>map=new HashMap<String, String>();
		   map.put("I_ZJCLB", rank);
		   map.put("I_ZJCYY",rewardAndPenaltyDesc);
		   map.put("I_ZJCFS", rpScore);
		   map.put("I_ZJCJE", showMoneySum);
		   map.put("I_BEGDA", inTransferDate+" 00:00:00");
		   map.put("I_PERNR", applyUserAccount);
		   map.put("instanceID", execution.getId());
		   if(rapInforService.query(map)>0){
			   throw new AudiException("数据已经插入SAP或在审核中");
		   }
		   try {
				rapInforService.insert(map);
			} catch (Exception e) {
				// TODO: handle exception
				if(e instanceof AudiException)
					throw e;
				throw new RuntimeException("运行出错");
			}
    }
	}

}
