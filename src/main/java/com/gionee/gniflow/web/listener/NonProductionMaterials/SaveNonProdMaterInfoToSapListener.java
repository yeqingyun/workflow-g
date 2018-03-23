package com.gionee.gniflow.web.listener.NonProductionMaterials;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gionee.gniflow.biz.service.ProcessHelpService;
import com.gionee.gniflow.integration.dao.KeepRunTaskDao;
import com.gionee.gniflow.sap.SapUtil;
import com.gionee.gniflow.web.support.AudiException;
import com.gionee.gniflow.web.util.SpringTools;

public class SaveNonProdMaterInfoToSapListener implements ExecutionListener {
	
	private static final long serialVersionUID = -4554160257262582345L;
	private Logger logger = LoggerFactory.getLogger(SaveNonProdMaterInfoToSapListener.class);
	private ProcessHelpService processHelpService;

	@Override
	public void notify(DelegateExecution execution) throws Exception {
		logger.debug("Running in SaveNonProdMaterInfoToSapListener begin!");
		RuntimeService runtimeService = execution.getEngineServices().getRuntimeService();
		processHelpService = (ProcessHelpService) SpringTools.getBean(ProcessHelpService.class);
		KeepRunTaskDao keepRunTaskDao=(KeepRunTaskDao)SpringTools.getBean(KeepRunTaskDao.class);
		//获取非生产物资信息
		String proofType = (String)runtimeService.getVariable(execution.getId(), "proofType");//凭证类型
		String updateProofType=(String)runtimeService.getVariable(execution.getId(), "updateProofType");
		String factory=(String)runtimeService.getVariable(execution.getId(), "factory");
		String updateFactory=(String)runtimeService.getVariable(execution.getId(), "updateFactory");
		String scrapWay=(String)runtimeService.getVariable(execution.getId(), "scrapWay");//申请方式
		
		String accessoriesJsonStr = null;
		if("1".equals(scrapWay)){
			accessoriesJsonStr=(String)runtimeService.getVariable(execution.getId(), "MFixedAssetsDetraction");//申请清单
		}else{
			accessoriesJsonStr=(String)runtimeService.getVariable(execution.getId(), "scrapWay2JsonStr");//申请清单
		}
		String updateAccessoriesJsonStr = (String)runtimeService.getVariable(execution.getId(), "updateMFixedAssetsDetraction");
		if((accessoriesJsonStr==null||"".equals(accessoriesJsonStr.trim())) && (updateAccessoriesJsonStr==null||"".equals(updateAccessoriesJsonStr.trim()))){
			return;
		}
		Map<String,Object> map=null;
		if(updateAccessoriesJsonStr==null){
			map=processHelpService.parseJsonToMap(accessoriesJsonStr);
		}else{
			map=processHelpService.parseJsonToMap(updateAccessoriesJsonStr);
		}
		List<Map<String,Object>> assets=(List<Map<String,Object>>)map.get("accessoryData");
		if(assets.size()<=0){
			return;
		}
		Map<String,String> param=new HashMap<String,String>();
		param.put("I_SUBMIT","T");
		
		if(updateProofType==null){
			param.put("I_BSART",proofType);
		}else{
			param.put("I_BSART",updateProofType);
		}
		
		if(updateFactory!=null&&!"".equals(updateFactory.trim())){
			factory=updateFactory;
		}
		
		SapUtil sap=new SapUtil();
		
		List<Map<String,String>> list=new ArrayList<Map<String,String>>();
		//写入清单
		for(Map<String,Object> asset:assets){
			String courseAllot=(String)asset.get("courseAllot");
			if(courseAllot==null){
				courseAllot="";
			}
			String stockNumber=(String)asset.get("stockNumber");
			if(stockNumber==null||"".equals(stockNumber.trim())){
				stockNumber="";
			}else{
				stockNumber="000000000"+stockNumber;
			}
			String stockNameAndStandard=(String)asset.get("stockNameAndStandard");
			if(stockNameAndStandard==null){
				stockNameAndStandard="";
			}
			String applyNumber=(String)asset.get("applyNumber");
			String monad=(String)asset.get("monad");
			if(monad==null){
				monad="";
			}
			String companyName="";
			if(factory!=null&&!"".equals(factory)){
				companyName=factory;
			}else{
				companyName=(String)asset.get("factory");
				if(companyName.equals("东莞市金铭电子有限公司")||companyName.equals("东莞金卓通信科技有限公司")){
					if(companyName.equals("东莞市金铭电子有限公司")){
						companyName="2000";
					}else{
						companyName="3000";
					}
				}
			}
			
			String reckoning=(String)asset.get("reckoning");
			if(reckoning==null){
				reckoning="";
			}
			String costCenter=(String)asset.get("costCenter");
			if(costCenter!=null && !"".equals(costCenter.trim())){
				if(costCenter.indexOf("-")==-1){
					costCenter="00"+costCenter;
				}else{
					costCenter="00"+costCenter.substring(0,costCenter.indexOf("-"));
				}
			}
			String unitPrice=(String)asset.get("unitPrice");
			if(unitPrice==null||"".equals(unitPrice.trim())){
				unitPrice="";
			}else{
				unitPrice=String.valueOf(Double.parseDouble(unitPrice)*1000);
				unitPrice=unitPrice.substring(0,unitPrice.indexOf("."));
			}
			String deliveryDate=(String)asset.get("deliveryDate");
			deliveryDate=deliveryDate.replaceAll("-","");
			String procurementSection=(String)asset.get("procurementSection");
			if(procurementSection==null){
				procurementSection="";
			}
			String itemGroup=(String)asset.get("itemGroup");
			if(itemGroup==null){
				itemGroup="";
			}
			String applyName=(String)asset.get("applyName");
			Map<String,String> param1 = new HashMap<String,String>();
			//param1.put("BSART",proofType);
			param1.put("KNTTP",courseAllot);
			param1.put("MATNR",stockNumber);
			param1.put("TXZ01",stockNameAndStandard);
			param1.put("MENGE",applyNumber);
			param1.put("MEINS",monad);
			//param1.put("MEINS","PC");
			param1.put("LFDAT",deliveryDate);
			param1.put("MATKL",itemGroup);
			param1.put("WERKS",companyName);
			param1.put("EKGRP",procurementSection);
			param1.put("SAKTO",reckoning);
			param1.put("KOSTL",costCenter);
			param1.put("PREIS",unitPrice);
			param1.put("PEINH","1000");
			param1.put("AFNAM",applyName);
			param1.put("BEDNR",execution.getId().substring(execution.getId().length()-9,execution.getId().length()));
			
			list.add(param1);
		}
			String message=sap.writeToSapWithInputTableAndBanfn("ZHR_OA45", param, "OT_EBAN", list);//ZHR_OA45为写入非生产物资的接口名
			String[] arr=message.split("@");
			if("E".equals(arr[1].trim())){
				logger.debug("SAP保存数据反馈信息："+arr[2]);
				throw new AudiException("SAP保存数据反馈信息："+arr[2]);
			}
			Map<String,Object> map1=new HashMap<String,Object>();
			map1.put("processId",execution.getId());
			map1.put("proofNo",arr[0]);
			keepRunTaskDao.saveProofNo(map1);
	}
}
