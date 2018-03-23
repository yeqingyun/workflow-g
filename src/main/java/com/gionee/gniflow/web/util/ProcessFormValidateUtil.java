package com.gionee.gniflow.web.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.gionee.gnif.util.AppContext;
import com.gionee.gniflow.groovy.ProcessValidateUtil;
import com.gionee.gniflow.groovy.SimpleValidateProcessEngine;
import com.gionee.gniflow.groovy.Step;
import com.gionee.gniflow.web.easyui.AjaxJson;

public class ProcessFormValidateUtil {
	private static Logger logger = Logger.getLogger(ProcessFormValidateUtil.class);
	/**
	 * 验证表单信息并返回结果
	 * @param processDefinetionId
	 * @param taskDefinetionId
	 * @param formProperties
	 * @return
	 */
	public static AjaxJson validate(String processDefinetionId,String taskDefinetionId,Map<String, Object> formProperties){
		Set<Entry<String,Object>>  formEntrySet = formProperties.entrySet();
		Map<String,Object> result = null;
		for(Entry<String,Object> formEntry:formEntrySet){
			String fieldName = formEntry.getKey();
			//获取验证规则
			String validTypes = validType(processDefinetionId, taskDefinetionId, fieldName, formProperties);
			//无则进行下一次循环
			if(validTypes.trim().equals("")){
				continue;
			}
			//获取对应的值
			Object valueObject = formEntry.getValue();
			if(valueObject instanceof String){
				String fieldValue = (String)valueObject;
				String[] validTypeGroup = validTypes.trim().split(" ");
				for(String validType:validTypeGroup){
					//判断是否存在参数
					try{
						if(validType.indexOf("(")!=-1 && validType.indexOf(")")!=-1){
							String validTypeParams = validType.substring(validType.indexOf("(")+1, validType.indexOf(")"));
							String[] args =  validTypeParams.split(",");
							validType = validType.substring(0,validType.indexOf("("));
							result = ProcessValidateUtil.validate(validType.toLowerCase(), fieldValue, args);
						}else{
							result = ProcessValidateUtil.validate(validType.toLowerCase(), fieldValue);
						}						
					}catch(Exception e ){
						//捕获数据类型转换错误
						result = new HashMap<String,Object>();
						result.put(ProcessValidateUtil.IS_VALID, false);
						result.put(ProcessValidateUtil.MESSAGE, "格式转换失败");
					}finally{
						//验证是否成功，确保用户有收到交互信息
						if(!(Boolean)result.get(ProcessValidateUtil.IS_VALID)){
							String message = (String)result.get(ProcessValidateUtil.MESSAGE);
							String label = SimpleValidateProcessEngine.getFieldLabel(processDefinetionId, taskDefinetionId, fieldName);
							if(StringUtils.isNotBlank(label)){
								fieldName = label;
							}
							return new AjaxJson(false,fieldName+ message);
						}
					}
				}
			}
		}
		return new AjaxJson();
	}
	/**
	 * 获取对应字段的验证类型
	 * @param processDefinetionId
	 * @param taskDefinetionId
	 * @param fieldName
	 * @param formProperties
	 * @return
	 */
	private static String validType(String processDefinetionId,String taskDefinetionId,String fieldName,Map<String, Object> formProperties){
		//特殊节点验证类型获取
		String validTypes = "";
		Step currentStep = SimpleValidateProcessEngine.getCurrentStep(processDefinetionId, taskDefinetionId);
		if(currentStep != null){
			if(currentStep.getType().equals("approvalTask") || currentStep.getType().equals("verifyTask")){
				//获取当前用户账号，并截取关键字
				String account = AppContext.getCurrentAccount();
				String sortedfieldName = fieldName.substring(0, fieldName.indexOf(account));
				if(currentStep.getType().equals("approvalTask")){
					String key = currentStep.getKey();
					if(sortedfieldName.equals(key+"AudiAdvice") && ((String)formProperties.get(key+"Audi"+account)).equals("1")){
						validTypes = "NotBlank";
					}else if(sortedfieldName.equals(key+"AudiAdvice") && ((String)formProperties.get(key+"Audi"+account)).equals("0")){
						validTypes = "";
					}
					else{
						validTypes = "NotBlank";
					}
				}else{
					String key = currentStep.getKey();
					if(sortedfieldName.equals(key+"AudiAdvice")){
						validTypes = "";
					}else{
						validTypes = "NotBlank";
					}
				}
			}else{
				validTypes = SimpleValidateProcessEngine.getValidType(processDefinetionId, taskDefinetionId, fieldName);
			}
		}
		return validTypes;
	}
}
