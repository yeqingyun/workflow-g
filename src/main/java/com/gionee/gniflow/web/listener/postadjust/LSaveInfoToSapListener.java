package com.gionee.gniflow.web.listener.postadjust;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gionee.gnif.GnifException;
import com.gionee.gniflow.biz.service.ProcessHelpService;
import com.gionee.gniflow.sap.SapUtil;
import com.gionee.gniflow.web.support.AudiException;
import com.gionee.gniflow.web.util.SpringTools;


public class LSaveInfoToSapListener implements ExecutionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8871027786340018518L;
	private Logger logger = LoggerFactory.getLogger(LSaveInfoToSapListener.class);

	@Override
	public void notify(DelegateExecution execution) throws Exception {
		logger.debug("Running in InsertLeaveAppInfoLister begin!");
		RuntimeService runtimeService= execution.getEngineServices().getRuntimeService();
		ProcessHelpService processHelpService= (ProcessHelpService) SpringTools.getBean(ProcessHelpService.class);
		String userAccount=(String)runtimeService.getVariable(execution.getId(), "account");//人员编号
		String date=(String)runtimeService.getVariable(execution.getId(), "inTransferDate");//调动日期
		String finalMonth = date;
		date = finalMonth.substring(0, 4)+finalMonth.substring(5, 7)+finalMonth.substring(8, 10);
		String optionType=(String)runtimeService.getVariable(execution.getId(), "oprationTypevalue");//操作类型
		String reasion=(String)runtimeService.getVariable(execution.getId(), "oprationReasionvalue");//操作原因
		String newJobId = (String)runtimeService.getVariable(execution.getId(), "newJobValue");//调整后的岗位Id

		
		String employeeGroup=(String)runtimeService.getVariable(execution.getId(), "employeeGroupvalue");//员工组
		String childemployeeGroup=(String)runtimeService.getVariable(execution.getId(), "childemployeeGroupvalue");//员工子组
		String hrGroup=(String)runtimeService.getVariable(execution.getId(), "hrScopevalue");//人事范围
		String childhrGroup=(String)runtimeService.getVariable(execution.getId(), "childhrScopevalue");//人事子范围
		String saryGroup=(String)runtimeService.getVariable(execution.getId(), "saleScopevalue");//工资范围
		String jobAgreement=(String)runtimeService.getVariable(execution.getId(), "jobAgreementvalue");//工作合同
		
		String accepterOrg=(String)runtimeService.getVariable(execution.getId(), "accepterOrgId");//接受部门编号
		String acceptOrgId=processHelpService.getSapOrgCode4DeptId(accepterOrg);
		if(null==jobAgreement||"".equals(jobAgreement)){//当工作合同为空的时候，不传入SAP
			//将参数放入map对象
			Map<String,String> map=new HashMap<String,String>();
			if("A3".equals(optionType)){//部门内部调动
				map.put("P_PERNR", userAccount);//人员编号
				map.put("P_DATE", date);//系统日期
				map.put("P_MASSG", reasion);//操作原因 
				map.put("P_PLANS", newJobId);//职位
				map.put("P_PERSG", employeeGroup);//员工组
				map.put("P_PERSK", childemployeeGroup);//员工子组
				map.put("P_ABKRS", saryGroup);//工资范围
				try {
					SapUtil sap=new SapUtil();
					String message=sap.writeToSapReturnStatusAndMessage("ZHR_OA10", map);//ZHR_OA10部门内部调动接口
					String message1 = message.substring(0, 1);
					String message2 = message.substring(1,message.length());
					if(!("0".equals(message1))){
						//System.out.println("SAP保存数据反馈信息："+message2);
						throw new AudiException("SAP保存数据反馈信息："+message2);
					}
				
				} catch (Exception e) {
					e.printStackTrace();
					logger.error(e.getMessage());
					if(e instanceof AudiException)
						throw e;
					throw new GnifException("Failure of SaveInfoTo with SAP!");
				}
			}else if("A4".equals(optionType)){//部门间调动
				map.put("P_PERNR", userAccount);//人员编号
				map.put("P_DATE", date);//系统日期
				map.put("P_MASSG", reasion);//操作原因
				map.put("P_PLANS", newJobId);//职位
				map.put("P_ORGEH", acceptOrgId);//组织单位
				map.put("P_WERKS", hrGroup);//人事范围
				map.put("P_BTRTL", childhrGroup);//人事子范围
				map.put("P_PERSG", employeeGroup);//员工组
				map.put("P_PERSK", childemployeeGroup);//员工子组
				map.put("P_ABKRS", saryGroup);//工资范围
				try {
					SapUtil sap=new SapUtil();
					String message=sap.writeToSapReturnStatusAndMessage("ZHR_OA11", map);//ZHR_OA11部门间调动接口
					String message1 = message.substring(0, 1);
					String message2 = message.substring(1,message.length());
					if(!("0".equals(message1))){
						//System.out.println("SAP保存数据反馈信息："+message2);
						throw new AudiException("SAP保存数据反馈信息："+message2);
					}
				} catch (Exception e) {
					e.printStackTrace();
					logger.error(e.getMessage());
					if(e instanceof AudiException)
						throw e;
					throw new GnifException("Failure of SaveInfoTo with SAP!");
				}
			}else if("A5".equals(optionType)){//跨公司调动
				
				map.put("P_PERNR", userAccount);//人员编号
				map.put("P_DATE", date);//系统日期
				map.put("P_MASSG", reasion);//操作原因
				map.put("P_PLANS", newJobId);//职位
				map.put("P_WERKS", hrGroup);//人事范围
				map.put("P_BTRTL", childhrGroup);//人事子范围
				map.put("P_PERSG", employeeGroup);//员工组
				map.put("P_PERSK", childemployeeGroup);//员工子组
				map.put("P_ABKRS", saryGroup);//工资范围
				try {
					SapUtil sap=new SapUtil();
					String message=sap.writeToSapReturnStatusAndMessage("ZHR_OA12", map);//ZHR_OA12公司间调动接口
					String message1 = message.substring(0, 1);
					String message2 = message.substring(1,message.length());
					if(!("0".equals(message1))){
						//System.out.println("SAP保存数据反馈信息："+message2);
						throw new AudiException("SAP保存数据反馈信息："+message2);
					}
				} catch (Exception e) {
					e.printStackTrace();
					logger.error(e.getMessage());
					if(e instanceof AudiException)
						throw e;
					throw new GnifException("Failure of SaveInfoTo with SAP!");
				}
			}else{
				//System.out.println("没有相关操作类型！");
			}
		}else{
		
		//将参数放入map对象
		Map<String,String> map=new HashMap<String,String>();
		if("A3".equals(optionType)){//部门内部调动
			map.put("P_PERNR", userAccount);//人员编号
			map.put("P_DATE", date);//系统日期
			map.put("P_MASSG", reasion);//操作原因 
			map.put("P_PLANS", newJobId);//职位
			map.put("P_PERSG", employeeGroup);//员工组
			map.put("P_PERSK", childemployeeGroup);//员工子组
			map.put("P_ABKRS", saryGroup);//工资范围
			map.put("P_ANSVH", jobAgreement);//工作合同
			try {
				SapUtil sap=new SapUtil();
				String message=sap.writeToSapReturnStatusAndMessage("ZHR_OA10", map);//ZHR_OA10部门内部调动接口
				String message1 = message.substring(0, 1);
				String message2 = message.substring(1,message.length());
				if(!("0".equals(message1))){
					//System.out.println("SAP保存数据反馈信息："+message2);
					throw new AudiException("SAP保存数据反馈信息："+message2);
				}
			
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				if(e instanceof AudiException)
					throw e;
				throw new GnifException("Failure of SaveInfoTo with SAP!");
			}
		}else if("A4".equals(optionType)){//部门间调动
			map.put("P_PERNR", userAccount);//人员编号
			map.put("P_DATE", date);//系统日期
			map.put("P_MASSG", reasion);//操作原因
			map.put("P_PLANS", newJobId);//职位
			map.put("P_ORGEH", acceptOrgId);//组织单位
			map.put("P_WERKS", hrGroup);//人事范围
			map.put("P_BTRTL", childhrGroup);//人事子范围
			map.put("P_PERSG", employeeGroup);//员工组
			map.put("P_PERSK", childemployeeGroup);//员工子组
			map.put("P_ABKRS", saryGroup);//工资范围
			map.put("P_ANSVH", jobAgreement);//工作合同
			try {
				SapUtil sap=new SapUtil();
				String message=sap.writeToSapReturnStatusAndMessage("ZHR_OA11", map);//ZHR_OA11部门间调动接口
				String message1 = message.substring(0, 1);
				String message2 = message.substring(1,message.length());
				if(!("0".equals(message1))){
					//System.out.println("SAP保存数据反馈信息："+message2);
					throw new AudiException("SAP保存数据反馈信息："+message2);
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				if(e instanceof AudiException)
					throw e;
				throw new GnifException("Failure of SaveInfoTo with SAP!");
			}
		}else if("A5".equals(optionType)){//跨公司调动
			
			map.put("P_PERNR", userAccount);//人员编号
			map.put("P_DATE", date);//系统日期
			map.put("P_MASSG", reasion);//操作原因
			map.put("P_PLANS", newJobId);//职位
			map.put("P_WERKS", hrGroup);//人事范围
			map.put("P_BTRTL", childhrGroup);//人事子范围
			map.put("P_PERSG", employeeGroup);//员工组
			map.put("P_PERSK", childemployeeGroup);//员工子组
			map.put("P_ABKRS", saryGroup);//工资范围
			map.put("P_ANSVH", jobAgreement);//工作合同
			try {
				SapUtil sap=new SapUtil();
				String message=sap.writeToSapReturnStatusAndMessage("ZHR_OA12", map);//ZHR_OA12公司间调动接口
				String message1 = message.substring(0, 1);
				String message2 = message.substring(1,message.length());
				if(!("0".equals(message1))){
					//System.out.println("SAP保存数据反馈信息："+message2);
					throw new AudiException("SAP保存数据反馈信息："+message2);
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				if(e instanceof AudiException)
					throw e;
				throw new GnifException("Failure of SaveInfoTo with SAP!");
			}
		}else{
			//System.out.println("没有相关操作类型！");
		}
	}
		
		logger.debug("Running in InsertLeaveAppInfoLister end!");
	}
}
