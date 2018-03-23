package com.gionee.gniflow.web.listener.message;

import java.io.IOException;
import java.io.InputStream;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gionee.gniflow.web.util.SendMessageUtil;



public class LSendMessageToHardwareEngineer implements ExecutionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7489720084637302555L;
	private Logger logger = LoggerFactory.getLogger(LSendMessageToHardwareEngineer.class);
	//private ProcessHelpService processHelpService;
	@Override
	public void notify(DelegateExecution execution) throws Exception {
		logger.debug("Running in InsertLeaveAppInfoLister begin!");
		RuntimeService runtimeService = execution.getEngineServices().getRuntimeService();
		//String applyUserAccount = (String)runtimeService.getVariable(execution.getId(), "applyUserAccount");//申请人工号
		String applyUserName = (String)runtimeService.getVariable(execution.getId(), "userName");//申请人姓名
		String applyOrgName =(String)runtimeService.getVariable(execution.getId(), "orgName");//申请人部门
		String taskName= (String)runtimeService.getVariable(execution.getId(), "processName");//异常名称
		String taskDesc = (String)runtimeService.getVariable(execution.getId(), "taskDesc");//异常描述
		String engineerName = (String)runtimeService.getVariable(execution.getId(), "engineerName");//工程师姓名
		String engineerTel = (String)runtimeService.getVariable(execution.getId(), "engineerTel");//工程师电话
		//HrUserDto user = processHelpService.getHrUser4Account(applyUserAccount);//通过员工编号获取员工信息
		String contentMsg="";//短信内容
		//String applyUserTel = user.getTel();//申请人电话
		//if(null==applyUserTel||("").equals(applyUserTel)){
			contentMsg= engineerName+"同事，您好！"
					+ "由："+applyUserName+" 部门："+applyOrgName
					+"发起了一条【"+taskName+"】的任务，"
					+ "详细描述为【"+taskDesc+"】请您及时处理！"
					+ "此短信由 Gionee工作流平台(网址：flow.gionee.com) 自动发送，请勿回复!"
					+ "祝您工作顺利，生活愉快！";
		//}
		/*contentMsg= engineerName+"同事，您好！"
				+ "由"+applyUserName+"，部门："+applyOrgName+",电话："+engineerTel
				+"发起了一条"+taskName+"的任务，</br>"
				+ "详细描述为"+taskDesc+"。请及时处理！"
				+ "此短信由 Gionee工作流平台(网址：flow.gionee.com) 自动发送，请勿回复!感谢您的配合！"
				+ "祝您工作顺利，生活愉快！";*/
		
		
			/*String msgcontent = "尊敬的同事，您好：您有新任务需要处理，流程实例编号：" + delegateTask.getProcessInstanceId() + "，流程名称：" + proDef.getName() + "，任务名称：" 
					+ delegateTask.getName() + "。请登录Gionee工作流平台办理，任务办理地址：http://flow.gionee.com，"
					+ "此短信由Gionee工作流平台自动发出，请勿回复，祝工作愉快，谢谢！";
			String mobile = processHelpService.getUserMobileNumber(userAccount);
			if(mobile != null && !mobile.equals("")) {
				SendMessageUtil.sendMessage(msgcontent, mobile);
				
				
				String mobile = processHelpService.getUserMobileNumber(userAccount);

			}*/

			if(engineerTel != null && !engineerTel.equals("")) {
				SendMessageUtil.sendMessage(contentMsg, engineerTel);
				
				
				//String mobile = processHelpService.getUserMobileNumber(userAccount);

			}
			
		/*	String account = map.get("ASSIGNEE_").toString();
			//String mobile = processHelpService.getUserMobileNumber(account);
			String mobile = processHelpService.getUserMobileNumberNoLogin(account);
			if(mobile != null && !mobile.equals("")) {
				SendMessageUtil.sendMessage(content, mobile);
			}
			String mobile = processHelpService.getUserMobileNumberNoLogin(account);
*/
			
			
	/*	PostMethod post = new PostMethod("http://msg.gionee.com/send_message/send1.json");
		 NameValuePair[] data = {
		          new NameValuePair("account", "00001023"),
		          new NameValuePair("message_content", contentMsg),
		          new NameValuePair("destination_number", engineerTel)
		        };
	        post.setRequestBody(data);
	        post.setRequestHeader("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
	        HttpClient client = new HttpClient();
	        
	        try {
				System.out.println(client.executeMethod(post));
			} catch (HttpException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	        try {
				InputStream inStream   = post.getResponseBodyAsStream();
				 StringBuffer returnXml  =   new  StringBuffer( "" );
				 BufferedReader rd  =   new  BufferedReader( new  InputStreamReader(inStream,"UTF-8" ));
				for  (String line  =   null ; (line  =  rd.readLine())  !=   null ;)  {
				                  returnXml.append(line);
				} 
				System.out.println(returnXml.toString());
			    rd.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
		
		logger.debug("Running in InsertLeaveAppInfoLister end!");
	}

}
