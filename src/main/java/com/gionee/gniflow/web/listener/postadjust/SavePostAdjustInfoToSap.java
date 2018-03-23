package com.gionee.gniflow.web.listener.postadjust;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gionee.gnif.GnifException;
import com.gionee.gniflow.biz.service.ProcessHelpService;
import com.gionee.gniflow.sap.SapUtil;
import com.gionee.gniflow.web.support.AudiException;
import com.gionee.gniflow.web.util.SpringTools;

public class SavePostAdjustInfoToSap implements ExecutionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8871027786340018518L;
	private Logger logger = LoggerFactory.getLogger(SavePostAdjustInfoToSap.class);

	@Override
	public void notify(DelegateExecution execution) throws Exception {
		logger.debug("Running in InsertLeaveAppInfoLister begin!");
		RuntimeService runtimeService= execution.getEngineServices().getRuntimeService();
		ProcessHelpService processHelpService= (ProcessHelpService) SpringTools.getBean(ProcessHelpService.class);
		
		String isUploadSap=(String)runtimeService.getVariable(execution.getId(), "isUploadSap");//是否上传Sap
		
		//判断是否是要上传SAP 0代表是  1代表不是
		if(isUploadSap==null || ("0").equals(isUploadSap)){
		
		String userAccount=(String)runtimeService.getVariable(execution.getId(), "account");//人员编号
		String date=(String)runtimeService.getVariable(execution.getId(), "hrleaderCheckenableTime");//日期
		String finalMonth = date;
		date = finalMonth.substring(0, 4)+finalMonth.substring(5, 7)+"01";
		
		//String jobOption=(String)runtimeService.getVariable(execution.getId(), "newjobvalue");//职位
		
		String newCompanyIdStart = (String)runtimeService.getVariable(execution.getId(), "incompanyId");//调整后的公司Id
		String newDeptIdStart = (String)runtimeService.getVariable(execution.getId(), "accepterOrgId");//调整后的部门Id
		String newJobIdStart = (String)runtimeService.getVariable(execution.getId(), "newjobvalue");//调整后的岗位Id

		String newCompanyIdStaff = (String)runtimeService.getVariable(execution.getId(), "updateCompanyIdStaff");//确认员工调整后的公司Id
		String newCompanyIdOfficeWorker = (String)runtimeService.getVariable(execution.getId(), "updateCompanyIdOfficeWorker");//确认职员调整后的公司Id
		String newDeptIdStaff = (String)runtimeService.getVariable(execution.getId(), "updateOrgIdStaff");//确认员工调整后的部门Id
		String newDeptIdOfficeWorker = (String)runtimeService.getVariable(execution.getId(), "updateOrgIdOfficeWorker");//确认职员调整后的部门Id
		String newJobIdStaff = (String)runtimeService.getVariable(execution.getId(), "updateJobIdStaff");//确认员工调整后的岗位Id
		String newJobIdOfficeWorker = (String)runtimeService.getVariable(execution.getId(), "updateJobIdOfficeWorker");//确认职员调整后的岗位Id
		
		String costType = (String)runtimeService.getVariable(execution.getId(), "costType");//确认职员调整后的岗位Id
		if(StringUtils.isNotBlank(costType)){
			if(costType.equals("1")){
				costType = "1产品导入";
			}else if(costType.equals("2")){
				costType = "2纯加工制造";
			}else if(costType.equals("3")){
				costType = "3售后服务支持";
			}else{
				costType = "";
			}
		}else{
			costType = "";
		}
		String newCompanyId = null;
		String newDeptId = null;
		String newJobId = null;
		
		//判断调整后公司信息
		if(null==newCompanyIdOfficeWorker){
			if("".equals(newCompanyIdStaff)||null==newCompanyIdStaff){
				newCompanyId = newCompanyIdStart;
				newDeptId = newDeptIdStart;
				newJobId = newJobIdStart;
				
			}else {
				newCompanyId = newCompanyIdStaff;
				newDeptId = newDeptIdStaff;
				newJobId = newJobIdStaff;
			}
		}
		if(null==newCompanyIdStaff){
			if("".equals(newCompanyIdOfficeWorker)||null==newCompanyIdOfficeWorker){
				newCompanyId = newCompanyIdStart;
				newDeptId = newDeptIdStart;
				newJobId = newJobIdStart;
			}else{
				newCompanyId = newCompanyIdOfficeWorker;
				newDeptId = newDeptIdOfficeWorker;
				newJobId = newJobIdOfficeWorker;
			}
		}
		
		String isUpdate=(String)runtimeService.getVariable(execution.getId(), "isUpdate");//是否有修改数据
		String employeeGroup=null;
		String childemployeeGroup=null;
		String hrGroup=null;
		String childhrGroup=null;
		String saryGroup=null;
		String jobAgreement=null;
		String companyCode=null;
		String optionType=null;
		String reasion=null;
		//判断最后一步是否有修改数据如果有就取最后一步修改的数据  1代表没有  0代表有
		if(isUpdate==null || ("1").equals(isUpdate)){
		 employeeGroup=(String)runtimeService.getVariable(execution.getId(), "employeeGroupvalue");//员工组
		 childemployeeGroup=(String)runtimeService.getVariable(execution.getId(), "childemployeeGroupvalue");//员工子组
		 hrGroup=(String)runtimeService.getVariable(execution.getId(), "hrScopevalue");//人事范围
		 childhrGroup=(String)runtimeService.getVariable(execution.getId(), "childhrScopevalue");//人事子范围
		 saryGroup=(String)runtimeService.getVariable(execution.getId(), "saleScopevalue");//工资范围
		 jobAgreement=(String)runtimeService.getVariable(execution.getId(), "jobAgreementvalue");//工作合同
		 companyCode=(String)runtimeService.getVariable(execution.getId(), "companyCodevalue");//公司代码
		 optionType=(String)runtimeService.getVariable(execution.getId(), "oprationTypevalue");//操作类型
		 reasion=(String)runtimeService.getVariable(execution.getId(), "oprationReasionvalue");//操作原因
		}else{
			employeeGroup=(String)runtimeService.getVariable(execution.getId(), "employeeGroupvalueTo");//员工组
			 childemployeeGroup=(String)runtimeService.getVariable(execution.getId(), "childemployeeGroupvalueTo");//员工子组
			 hrGroup=(String)runtimeService.getVariable(execution.getId(), "hrScopevalueTo");//人事范围
			 childhrGroup=(String)runtimeService.getVariable(execution.getId(), "childhrScopevalueTo");//人事子范围
			 saryGroup=(String)runtimeService.getVariable(execution.getId(), "saleScopevalueTo");//工资范围
			 jobAgreement=(String)runtimeService.getVariable(execution.getId(), "jobAgreementvalueTo");//工作合同
			 companyCode=(String)runtimeService.getVariable(execution.getId(), "companyCodevalueTo");//公司代码
			 optionType=(String)runtimeService.getVariable(execution.getId(), "oprationTypevalueTo");//操作类型
			 reasion=(String)runtimeService.getVariable(execution.getId(), "oprationReasionvalueTo");//操作原因
		}
		String str=(String)runtimeService.getVariable(execution.getId(), "accepterOrgId");//接受部门编号
		String acceptorgid=processHelpService.getSapOrgCode4DeptId(str);
		//薪资数据
		String changeReasion=(String)runtimeService.getVariable(execution.getId(), "changeReasonValue");//更改原因
		String type=(String)runtimeService.getVariable(execution.getId(), "typeValue");//类型
		String area=(String)runtimeService.getVariable(execution.getId(), "areaValue");//区域
		String level=(String)runtimeService.getVariable(execution.getId(), "fuSalaryGroup");//级别
		String fuSalaryLevel=(String)runtimeService.getVariable(execution.getId(), "fuSalaryLevel");//档次
		String fuSalaryOpt1=(String)runtimeService.getVariable(execution.getId(), "fuSalaryOpt1");//工资项
		String fuSalary1=(String)runtimeService.getVariable(execution.getId(), "fuSalary1");//工资项
		String fuSalaryOpt2=(String)runtimeService.getVariable(execution.getId(), "fuSalaryOpt2");//支付的工资项金额
		String fuSalary2=(String)runtimeService.getVariable(execution.getId(), "fuSalary2");//支付的工资项金额
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
			map.put("P_ZCBLX", costType);//成本类型
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
			map.put("P_ORGEH", acceptorgid);//组织单位
			map.put("P_WERKS", hrGroup);//人事范围
			map.put("P_BTRTL", childhrGroup);//人事子范围
			map.put("P_PERSG", employeeGroup);//员工组
			map.put("P_PERSK", childemployeeGroup);//员工子组
			map.put("P_ABKRS", saryGroup);//工资范围
			map.put("P_ANSVH", jobAgreement);//工作合同
			map.put("P_ZCBLX", costType);//成本类型
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
			map.put("P_ZCBLX", costType);//成本类型
			try {
				SapUtil sap=new SapUtil();
				String message=sap.writeToSapReturnStatusAndMessage("ZHR_OA12", map);//ZHR_OA12公司间调动接口
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
		
		logger.debug("Running in InsertLeaveAppInfoLister end!");
	}
}
