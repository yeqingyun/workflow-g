package com.gionee.gniflow.web.listener;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gionee.auth.dao.UserDao;
import com.gionee.gniflow.biz.service.ProcessHelpService;
import com.gionee.gniflow.web.util.SendMailTool;
import com.gionee.gniflow.web.util.SpringTools;

public class AutoUpdateThreeSendmailListener implements ExecutionListener {
	private static final long serialVersionUID = 1L;
	private Logger logger = LoggerFactory
			.getLogger(AutoUpdateThreeSendmailListener.class);
	private ProcessHelpService processHelpService;
	private UserDao userDao;

	@Override
	public void notify(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		logger.debug("Running in AutoUpdateThreeSendmailListener begin!");

		processHelpService = (ProcessHelpService) SpringTools
				.getBean(ProcessHelpService.class);
		userDao = (UserDao) SpringTools.getBean(UserDao.class);
		RuntimeService runtimeService = execution.getEngineServices()
				.getRuntimeService();
		String SqaAffirmAudi = (String) runtimeService.getVariable(
				execution.getId(), "SqaAffirmAudi");
		if (SqaAffirmAudi.equals("1")) {
			// 软件名称
			String softwareName = (String) runtimeService.getVariable(
					execution.getId(), "softwareName");
			// 软件版本号
			String versionNumber = (String) runtimeService.getVariable(
					execution.getId(), "versionNumber");
			// 适配机型
			String equipmentType = (String) runtimeService.getVariable(
					execution.getId(), "equipmentType");

			// 应用升级管理方式
			String AppUpgradeManage = (String) runtimeService.getVariable(
					execution.getId(), "AppUpgradeManage");

			AppUpgradeManage = AppUpgradeManage.equals("0")?"无":(AppUpgradeManage.equals("1")?"APK发布":(AppUpgradeManage.equals("2")?"推荐升级":"强制升级（静默安装升级）"));

			// 应用升级管理编号
			String AppUpgradeManageNumber = (String) runtimeService
					.getVariable(execution.getId(), "AppUpgradeManageNumber");

			// 预上线时间点
			String expectDate = (String) runtimeService.getVariable(
					execution.getId(), "expectDate");

			// 适配平台
			String equipmentPlatform = (String) runtimeService.getVariable(
					execution.getId(), "equipmentPlatform");

			// 适配渠道
			String equipmentDitch = (String) runtimeService.getVariable(
					execution.getId(), "equipmentDitch");

			// 适配过的分辨率
			String equipmentVGA = (String) runtimeService.getVariable(
					execution.getId(), "equipmentVGA");

			// 适配过的Android版本
			String androidVersion = (String) runtimeService.getVariable(
					execution.getId(), "androidVersion");

			// 关联耦合模块功能的验证情况
			String modulesValidate = (String) runtimeService.getVariable(
					execution.getId(), "modulesValidate");

			// 数据库上下兼容的验证情况
			String dbValidate = (String) runtimeService.getVariable(
					execution.getId(), "dbValidate");

			// 跨旧版本升级的验证情况
			String oldVersionValidate = (String) runtimeService.getVariable(
					execution.getId(), "oldVersionValidate");
			
			//申请自升级原因
		    String applyReason = (String)runtimeService.getVariable(execution.getId(), "applyReason");
		    applyReason = applyReason.equals("1")?"《运营需求》":(applyReason.equals("2")?"《Debug迭代》":"《维护迭代》");

			// 测试类型
			String testType = (String) runtimeService.getVariable(
					execution.getId(), "testType");

			testType = testType.equals("1") ? "基本功能测试" : "全面测试";

			// 测试结果
			String testResult = (String) runtimeService.getVariable(
					execution.getId(), "testResult");

			testResult = testResult.equals("1") ? "通过" : "不通过";

			// 风险
			String risk = (String) runtimeService.getVariable(
					execution.getId(), "risk");

			// 功能变更说明
			String functionalChange = (String) runtimeService.getVariable(
					execution.getId(), "functionalChange");

			// 修改问题说明
			String modifyExplain = (String) runtimeService.getVariable(
					execution.getId(), "modifyExplain");

			// 新增问题
			String newIssue = (String) runtimeService.getVariable(
					execution.getId(), "newIssue");

			// 遗留问题
			String leaveBehindIssue = (String) runtimeService.getVariable(
					execution.getId(), "leaveBehindIssue");

			// 备注
			String remark = (String) runtimeService.getVariable(
					execution.getId(), "remark");
			if(StringUtils.isEmpty(remark)){
				remark="";
			}

			// 申请人
			String applyUserName = (String) runtimeService.getVariable(
					execution.getId(), "applyUserName");

			// 申请时间
			String applyDate = (String) runtimeService.getVariable(
					execution.getId(), "applyDate");

			// SQA审批人
			String SQAName = (String) runtimeService.getVariable(
					execution.getId(), "SQAName");

			// SQA审批时间
			String SQASignDate = (String) runtimeService.getVariable(
					execution.getId(), "SQASignDate");

			// 是否通过
			String SqaAudi = (String) runtimeService.getVariable(
					execution.getId(), "SqaAudi");
			SqaAudi = SqaAudi.equals("0") ? "通过" : "不通过";

			// 处理意见
			String SqaAdvice = (String) runtimeService.getVariable(
					execution.getId(), "SqaAdvice");

			// 运营经理评审人
			String OperationManagerName = (String) runtimeService.getVariable(
					execution.getId(), "OperationManagerName");

			// 运营经理审批时间
			String OperationManagerSignDate = (String) runtimeService
					.getVariable(execution.getId(), "OperationManagerSignDate");

			// 处理意见
			String operationManagerAdvice = (String) runtimeService
					.getVariable(execution.getId(), "operationManagerAdvice");

			/*
			 * // 反馈人 String feedbackAudiName = (String)
			 * runtimeService.getVariable( execution.getId(),
			 * "feedbackAudiName");
			 * 
			 * // 反馈时间 String feedbackAudiSignDate = (String)
			 * runtimeService.getVariable( execution.getId(),
			 * "feedbackAudiSignDate");
			 * 
			 * // 用户反馈系统 String userFeedbackAudiAdvice = (String)
			 * runtimeService.getVariable( execution.getId(),
			 * "userFeedbackAudiAdvice");
			 * 
			 * // 售后 String afterService = (String) runtimeService.getVariable(
			 * execution.getId(), "afterService");
			 * 
			 * // 论坛 String feedbackAudiAdvice = (String)
			 * runtimeService.getVariable( execution.getId(),
			 * "feedbackAudiAdvice");
			 * 
			 * // 报错 String error = (String)
			 * runtimeService.getVariable(execution.getId(), "error");
			 */

			// 审核人
			String SqaAffirmName = (String) runtimeService.getVariable(
					execution.getId(), "SqaAffirmName");

			// 审核时间
			String SqaAffirmSignDate = (String) runtimeService.getVariable(
					execution.getId(), "SqaAffirmSignDate");

			// 处理结果
			String SqaAffirmAdvice = (String) runtimeService.getVariable(
					execution.getId(), "SqaAffirmAdvice");

			String titel = "APK自升级流程《" + softwareName + "》" + versionNumber
					+ "适配机型" + equipmentType + "正式发布不通过";
			/*
			 * String content = "APK是否正式上线:          审核人:" + SqaAffirmName +
			 * "      审核时间:" + SqaAffirmSignDate +
			 * "      是否同意上线:否                 处理结果:" + SqaAffirmAdvice +
			 * "用户反馈专员:     反馈人 :" + feedbackAudiName + "         反馈时间  :" +
			 * feedbackAudiSignDate + "      用户反馈系统:" + userFeedbackAudiAdvice +
			 * "     售后:" + afterService + "     论坛:" + feedbackAudiAdvice +
			 * "     报错:" + error + "     处理意见:" + SqaAdvice +
			 * "运营经理审批:     运营经理评审人:" + OperationManagerName +
			 * "         运营经理审批时间 :" + OperationManagerSignDate +
			 * "      是否通过:通过" + "     处理意见:" + operationManagerAudi +
			 * "SQA审批:     SQA审批人 :" + SQAName + "         SQA审批时间  :" +
			 * SQASignDate + "      是否通过:通过" + "     处理意见:" + SqaAdvice +
			 * "     开始表单           " + "应用升级管理方式:" + AppUpgradeManage +
			 * "            应用升级管理编号:" + AppUpgradeManageNumber +
			 * "            预上线时间点:" + expectDate + "            适配平台" +
			 * equipmentPlatform + "            适配渠道" + equipmentDitch +
			 * "            适配过的分辨率:" + equipmentVGA +
			 * "            适配过的Android版本:" + androidVersion +
			 * "            关联耦合模块功能的验证情况:" + modulesValidate +
			 * "            数据库上下兼容的验证情况:" + modulesValidate +
			 * "            跨旧版本升级的验证情况:" + oldVersionValidate +
			 * "            测试类型:" + testType + "            测试结果:" + testResult
			 * + "            风险:" + risk + "            功能变更说明:" +
			 * functionalChange + "            修改问题说明:" + modifyExplain +
			 * "            新增问题:" + newIssue + "            遗留问题:" +
			 * leaveBehindIssue + "            申请人：" + applyUserName +
			 * "            申请时间:" + applyDate + "            备注:" + remark;
			 */

			String table = "";
			// 修改表单
			String updateSoftwareName = (String) runtimeService.getVariable(
					execution.getId(), "updateSoftwareName");
			if (StringUtils.isNotEmpty(updateSoftwareName)) {
				// 软件版本号
				String updateVersionNumber = (String) runtimeService
						.getVariable(execution.getId(), "updateVersionNumber");
				// 适配机型
				String updateEquipmentType = (String) runtimeService
						.getVariable(execution.getId(), "updateEquipmentType");

				// 应用升级管理方式
				String updateAppUpgradeManage = (String) runtimeService
						.getVariable(execution.getId(),
								"updateAppUpgradeManage");

				updateAppUpgradeManage = updateAppUpgradeManage.equals("0")?"无":(updateAppUpgradeManage.equals("1")?"APK发布":(updateAppUpgradeManage.equals("2")?"推荐升级":"强制升级（静默安装升级）"));

				// 应用升级管理编号
				String updateAppUpgradeManageNumber = (String) runtimeService
						.getVariable(execution.getId(),
								"updateAppUpgradeManageNumber");

				// 预上线时间点
				String updateExpectDate = (String) runtimeService.getVariable(
						execution.getId(), "updateExpectDate");

				// 适配平台
				String updateEquipmentPlatform = (String) runtimeService
						.getVariable(execution.getId(),
								"updateEquipmentPlatform");

				// 适配渠道
				String updateEquipmentDitch = (String) runtimeService
						.getVariable(execution.getId(), "updateEquipmentDitch");

				// 适配过的分辨率
				String updateEquipmentVGA = (String) runtimeService
						.getVariable(execution.getId(), "updateEquipmentVGA");

				// 适配过的Android版本
				String updateAndroidVersion = (String) runtimeService
						.getVariable(execution.getId(), "updateAndroidVersion");

				// 关联耦合模块功能的验证情况
				String updateModulesValidate = (String) runtimeService
						.getVariable(execution.getId(), "updateModulesValidate");

				// 数据库上下兼容的验证情况
				String updateDbValidate = (String) runtimeService.getVariable(
						execution.getId(), "updateDbValidate");

				// 跨旧版本升级的验证情况
				String updateOldVersionValidate = (String) runtimeService
						.getVariable(execution.getId(),
								"updateOldVersionValidate");
				//申请自升级原因
			    String updateApplyReason = (String)runtimeService.getVariable(execution.getId(), "updateApplyReason");
			    updateApplyReason=updateApplyReason.equals("1")?"《运营需求》":(updateApplyReason.equals("2")?"《Debug迭代》":"《维护迭代》");

				// 测试类型
				String updateTestType = (String) runtimeService.getVariable(
						execution.getId(), "updateTestType");

				updateTestType = updateTestType.equals("1") ? "基本功能测试" : "全面测试";

				// 测试结果
				String updateTestResult = (String) runtimeService.getVariable(
						execution.getId(), "updateTestResult");

				updateTestResult = updateTestResult.equals("1") ? "通过" : "不通过";

				// 风险
				String updateRisk = (String) runtimeService.getVariable(
						execution.getId(), "updateRisk");

				// 功能变更说明
				String updateFunctionalChange = (String) runtimeService
						.getVariable(execution.getId(),
								"updateFunctionalChange");

				// 修改问题说明
				String updateModifyExplain = (String) runtimeService
						.getVariable(execution.getId(), "updateModifyExplain");

				// 新增问题
				String updateNewIssue = (String) runtimeService.getVariable(
						execution.getId(), "updateNewIssue");

				// 遗留问题
				String updateLeaveBehindIssue = (String) runtimeService
						.getVariable(execution.getId(),
								"updateLeaveBehindIssue");

				// 备注
				String updateRemark = (String) runtimeService.getVariable(
						execution.getId(), "updateRemark");
				if(StringUtils.isEmpty(updateRemark)){
					updateRemark="";
				}

				// 申请人
				String updateApplyUserName = (String) runtimeService
						.getVariable(execution.getId(), "updateApplyUserName");

				// 申请时间
				String updateApplyDate = (String) runtimeService.getVariable(
						execution.getId(), "updateApplyDate");

				table = "<table width=100%  border=1 align=center cellpadding=0 cellspacing=0>"
						+ "<tr ><td  colspan=4 align=center><font size=5px color=red>修改表单</font></td></tr>"
						+ "<tr><td colspan=2>软件名称:"
						+ updateSoftwareName
						+ "</td><td colspan=2>软件版本号:"
						+ updateVersionNumber
						+ "</td></tr>"
						+ "<tr><td colspan=2>适配机型:"
						+ updateEquipmentType
						+ "</td><td colspan=2>应用升级管理方式:"
						+ updateAppUpgradeManage
						+ "</td></tr>"
						+ "<tr><td colspan=2>应用升级管理编号:"
						+ updateAppUpgradeManageNumber
						+ "</td><td colspan=2>预上线时间点:"
						+ updateExpectDate
						+ "</td></tr>"
						+ "<tr><td colspan=2>适配平台:"
						+ updateEquipmentPlatform
						+ "</td><td colspan=2>适配渠道:"
						+ updateEquipmentDitch
						+ "</td></tr>"
						+ "<tr><td colspan=2>适配过的Android版本:"
						+ updateAndroidVersion
						+ "</td><td colspan=2>适配过的分辨率:"
						+ updateEquipmentVGA
						+ "</td></tr>"
						+ "<tr><td colspan=2>关联耦合模块功能的验证情况:"
						+ updateModulesValidate
						+ "</td><td colspan=2>数据库上下兼容的验证情况:"
						+ updateDbValidate
						+ "</td></tr>"
						+ "<tr><td colspan=2>跨旧版本升级的验证情况:"
						+ updateOldVersionValidate
						+ "</td><td colspan=2>申请自升级原因:"+updateApplyReason+"</td></tr>"
						+ "<tr></td><td colspan=2>测试类型:"
						+ updateTestType
						+ "</td><td colspan=2>测试结果:"
						+ updateTestResult+"</tr>"
						+ "<tr><td colspan=4>风险:"
						+ updateRisk.replaceAll("\r\n", "<br/>")
						+ "</td></tr>"
						+ "<tr><td colspan=4>功能变更说明:"
						+ updateFunctionalChange.replaceAll("\r\n", "<br/>")
						+ "</td></tr>"
						+ "<tr><td colspan=4>修改问题说明:"
						+ updateModifyExplain.replaceAll("\r\n", "<br/>")
						+ "</td></tr>"
						+ "<tr><td colspan=4>新增问题:"
						+ updateNewIssue.replaceAll("\r\n", "<br/>")
						+ "</td></tr>"
						+ "<tr><td colspan=4>遗留问题:"
						+ updateLeaveBehindIssue.replaceAll("\r\n", "<br/>")
						+ "</td></tr>"
						+ "<tr><td colspan=4>备注:"
						+ updateRemark.replaceAll("\r\n", "<br/>")
						+ "</td></tr>"
						+ "<tr><td colspan=2>申请人:"
						+ updateApplyUserName
						+ "</td><td colspan=2>申请时间:"
						+ updateApplyDate
						+ "</td></tr></table></br>";

			}
			StringBuilder content = new StringBuilder();
			
			
			content.append("<table width=100%  border=1 align=center cellpadding=0 cellspacing=0>"
					+ "<tr ><td  colspan=4 align=center><font size=5px color=red>正式发布SQA评审结果</font></td></tr>"
					+ "<tr><td colspan=2>审核人:"
					+ SqaAffirmName
					+ "</td><td colspan=2>审核时间:"
					+ SqaAffirmSignDate
					+ "</td></tr>"
					+ "<tr><td colspan=2>评审结果:不通过</td><td colspan=2>备注:"
					+ SqaAffirmAdvice.replaceAll("\r\n", "<br/>")+ "</td></tr></table></br>");

			/*
			 * content.append(
			 * "<table width=100%  border=1 align=center cellpadding=0 cellspacing=0>"
			 * +
			 * "<tr ><td  colspan=4 align=center><font size=5px color=red>正式发布运营评审结果</font></td></tr>"
			 * + "<tr><td colspan=2>反馈人:" + feedbackAudiName +
			 * "</td><td colspan=2>反馈时间:" + feedbackAudiSignDate + "</td></tr>"
			 * // + //
			 * "<tr><td>运营经理评审人:"+OperationManagerName+"</td><td>运营经理审批时间:"
			 * +OperationManagerSignDate
			 * +"</td><td>是否通过:通过</td><td>处理意见:"+operationManagerAudi
			 * +"</td></tr></table></br>" + "<tr><td colspan=2>用户反馈系统:" +
			 * userFeedbackAudiAdvice + "</td><td colspan=2>售后:" + afterService
			 * + "</td></tr>" + "<tr><td colspan=2>论坛:" + feedbackAudiAdvice +
			 * "</td><td colspan=2>报错:" + error + "</td></tr></table></br>");
			 */

			content.append("<table width=100%  border=1 align=center cellpadding=0 cellspacing=0>"
					+ "<tr ><td  colspan=4 align=center><font size=5px color=red>预发布运营评审结果</font></td></tr>"
					+ "<tr><td colspan=2>审核人:"
					+ OperationManagerName
					+ "</td><td colspan=2>审核时间:"
					+ OperationManagerSignDate
					+ "</td></tr>"
					+ "<tr><td colspan=2>评审结果:通过</td><td colspan=2>备注:"
					+ operationManagerAdvice.replaceAll("\r\n", "<br/>")+ "</td></tr></table></br>");

			content.append("<table width=100%  border=1 align=center cellpadding=0 cellspacing=0>"
					+ "<tr ><td  colspan=4 align=center><font size=5px color=red>预发布SQA评审结果</font></td></tr>"
					+ "<tr><td colspan=2>审核人:"
					+ SQAName
					+ "</td><td colspan=2>审核时间 :"
					+ SQASignDate
					+ "</td></tr>"
					+ "<tr><td colspan=2>评审结果:通过</td><td colspan=2>备注:"
					+ SqaAdvice.replaceAll("\r\n", "<br/>")+ "</td></tr></table></br>");
			if (StringUtils.isNotEmpty(table)) {
				content.append(table);
			}
			content.append("<table width=100%  border=1 align=center cellpadding=0 cellspacing=0>"
					+ "<tr ><td  colspan=4 align=center><font size=5px color=red>开始表单</font></td></tr>"
					+ "<tr><td colspan=2>软件名称:"
					+ softwareName
					+ "</td><td colspan=2>软件版本号:"
					+ versionNumber
					+ "</td></tr>"
					+ "<tr><td colspan=2>适配机型:"
					+ equipmentType
					+ "</td><td colspan=2>应用升级管理方式:"
					+ AppUpgradeManage
					+ "</td></tr>"
					+ "<tr><td colspan=2>应用升级管理编号:"
					+ AppUpgradeManageNumber
					+ "</td><td colspan=2>预上线时间点:"
					+ expectDate
					+ "</td></tr>"
					+ "<tr><td colspan=2>适配平台:"
					+ equipmentPlatform
					+ "</td><td colspan=2>适配渠道:"
					+ equipmentDitch
					+ "</td></tr>"
					+ "<tr><td colspan=2>适配过的Android版本:"
					+ androidVersion
					+ "</td><td colspan=2>适配过的分辨率:"
					+ equipmentVGA
					+ "</td></tr>"
					+ "<tr><td colspan=2>关联耦合模块功能的验证情况:"
					+ modulesValidate
					+ "</td><td colspan=2>数据库上下兼容的验证情况:"
					+ dbValidate
					+ "</td></tr>"
					+ "<tr><td colspan=2>跨旧版本升级的验证情况:"
					+ oldVersionValidate
					+ "</td><td colspan=2>申请自升级原因:"+applyReason+"</td></tr>"
					+ "<tr></td><td colspan=2>测试类型:"
					+ testType
					+ "</td><td colspan=2>测试结果:"
					+ testResult
					+ "</td></tr>"
					+ "<tr><td colspan=4>风险:"
					+ risk.replaceAll("\r\n", "<br/>")
					+ "</td></tr>"
					+ "<tr><td colspan=4>功能变更说明:"
					+ functionalChange.replaceAll("\r\n", "<br/>")
					+ "</td></tr>"
					+ "<tr><td colspan=4>修改问题说明:"
					+ modifyExplain.replaceAll("\r\n", "<br/>")
					+ "</td></tr>"
					+ "<tr><td colspan=4>新增问题:"
					+ newIssue.replaceAll("\r\n", "<br/>")
					+ "</td></tr>"
					+ "<tr><td colspan=4>遗留问题:"
					+ leaveBehindIssue.replaceAll("\r\n", "<br/>")
					+ "</td></tr>"
					+ "<tr><td colspan=4>备注:"
					+ remark.replaceAll("\r\n", "<br/>")
					+ "</td></tr>"
					+ "<tr><td colspan=2>申请人:"
					+ applyUserName
					+ "</td><td colspan=2>申请时间:"
					+ applyDate
					+ "</td></tr></table></br>");

			String str = "zhongyanyun@gionee.com";

			// 获取登陆的账号
			String applyUserId = (String) runtimeService.getVariable(
					execution.getId(), "applyUserId");

			// 给部门所有人发送邮件
			if (!StringUtils.isEmpty(applyUserId)) {
				processHelpService.sendEmail4Orgs(
						processHelpService.getOrgId(applyUserId), titel,
						content.toString());
			}

			// 设置固定收件人邮箱
			String[] Emails = { "dujq@gionee.com", "zhuying@gionee.com",
					"xiangmj@gionee.com", "caoht@gionee.com"
					,"shijy<shijy@gionee.com>","liaojh@gionee.com", "sw_user@gionee.com",
					"znyf_testleader@gionee.com",
					"wuyq@gionee.com", "zhangni@gionee.com" };
			// 发送邮箱到固定收件人
			//SendMailTool.sendMailByAddress(titel, content.toString(), "huangfuxiong@gionee.com");
			for (String email : Emails) {
				SendMailTool.sendMailByAddress(titel, content.toString(), email);
			}
			/*TestEmail.sendMailByAddress(titel, content.toString(), "zhangshilin@gionee.com");*/
		}
		
	}

}
