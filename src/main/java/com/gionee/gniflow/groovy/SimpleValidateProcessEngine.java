package com.gionee.gniflow.groovy;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.springframework.beans.factory.InitializingBean;

import groovy.lang.Closure;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;

public class SimpleValidateProcessEngine implements InitializingBean{
	private static ScriptEngine engine;
	//存入解析后的Process对象
	private static Map<String, Process> processMap = new HashMap<String,Process>();
	//存入从Process->Step->Field的Map对象
	private static Map<String,Object> validateProcessMap =  new HashMap<String,Object>();
	/**
	 * 通过脚本文件创建Process对象，并存入内存中
	 * @param path
	 */
	@SuppressWarnings({ "rawtypes", "resource" })
	public void buildProcess(String path){
		File file = null;
		InputStream  is = null;
		BufferedInputStream bis = null;
		try{
			file = new File(path);
			is = new FileInputStream(file);
			bis = new BufferedInputStream(is);
			byte[] buffer=new byte[1024];
			int flag=0;
			String groovyCode = "def processDefinition={ ";
			while((flag = bis.read(buffer,0,buffer.length))!=-1){
				groovyCode+=new String(buffer, 0, flag,"utf-8");
			}
			groovyCode+=" }";
			Bindings binding=engine.createBindings();
	        try {  
	        	Closure closure = (Closure)engine.eval(groovyCode,binding);
	        	ClassLoader cl = this.getClass().getClassLoader();  
	        	GroovyClassLoader groovyCl = new GroovyClassLoader(cl);  
	            Class groovyClass = groovyCl.parseClass(new File(this.getClass().getResource("/com/gionee/gniflow/groovy/ProcessGroovy.groovy").toURI()));  
	            GroovyObject groovyObject = (GroovyObject) groovyClass.newInstance();  
	            Process process = (Process)groovyObject.invokeMethod("make", closure);   
	            processMap.put(process.getId(), process);
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        } 
	        
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(is!=null){
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(bis!=null){
				try {
					bis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * 通过流程定义、任务ID，表单域获取验证信息
	 * @param processDefinetionId
	 * @param taskDefinetionId
	 * @param fieldName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String  getValidType(String processDefinetionId,String taskDefinetionId,String fieldName){
		Map<String,Object> validateProcess = (Map<String,Object>)validateProcessMap.get(processDefinetionId);
		if(validateProcess != null){
			Map<String,Object> validateStep = (Map<String,Object>)validateProcess.get(taskDefinetionId);
			if(validateStep != null){
				Field field = (Field)validateStep.get(fieldName);
				if(field == null){
					return "";
				}
				return field.getValidType();
			}
		}
		//如果没有，则缓存这个流程的验证信息
		String fieldValidType = "";
		Process process = processMap.get(processDefinetionId);
		if(process != null){
			List<Step> steps = process.getStep();
			if(steps != null && steps.size()>0){
				Map<String,Object> validateStepMap = new HashMap<String,Object>();
				for(Step step:steps){
					Map<String,Field> validateFieldMap = new HashMap<String,Field>();
					List<Field> fields = step.getField();
					if(fields != null && fields.size()>0){
						for(Field field:fields){
							String name = field.getName();
							String validType = field.getValidType();
							validateFieldMap.put(name, field);
							if(name.equals(fieldName)){
								fieldValidType = validType;
							}
						}
					}
					validateStepMap.put(step.getId(), validateFieldMap);
				}
				validateProcessMap.put(processDefinetionId, validateStepMap);
			}
		}
		return fieldValidType;
		
	}
	/**
	 * 获取任务的类型是自定义还是审核还是确认
	 * @param processDefinetionId
	 * @param taskDefinetionId
	 * @return
	 */
	public static Step getCurrentStep(String processDefinetionId,String taskDefinetionId){
		Step currentStep = null;
		Process process = processMap.get(processDefinetionId);
		if(process != null){
			List<Step> steps = process.getStep();
			if(steps != null && steps.size()>0){
				for(Step step:steps){
					String stepId = step.getId();
					if(stepId.endsWith(taskDefinetionId)){
						currentStep = step;
					}
				}
			}
		}
		return currentStep;
	}
	/**
	 * 获取表单域对应的label
	 * @param processDefinetionId
	 * @param taskDefinetionId
	 * @param fieldName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public  static String getFieldLabel(String processDefinetionId,String taskDefinetionId,String fieldName){
		Map<String,Object> validateProcess = (Map<String,Object>)validateProcessMap.get(processDefinetionId);
		if(validateProcess != null){
			Map<String,Field> validateStep = (Map<String,Field>)validateProcess.get(taskDefinetionId);
			if(validateStep != null){
				Field field = validateStep.get(fieldName);
				if(field == null){
					return null;
				}
				return field.getLabel();
			}
		}
		return null;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		ScriptEngineManager factory=new ScriptEngineManager();
		engine = factory.getEngineByName("groovy");
	}
 }
