/**
 * @(#) BpmServiceImpl.java Created on 2014年9月11日
 *
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.gionee.gniflow.biz.service.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.HistoricDetailVariableInstanceUpdateEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.engine.task.Attachment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.gionee.gniflow.biz.service.BpmService;
import com.gionee.gniflow.constant.BPMConstant;
import com.gionee.gniflow.dto.MapFormElement;
import com.gionee.gniflow.integration.dao.ActivitiHelpDao;
import com.gionee.gniflow.web.bmp.ProcessHisVariable;
import com.gionee.gniflow.web.bmp.ProcessStep;
/**
 * The class <code>BpmServiceImpl</code>
 * 
 * @author lipw
 * @version 1.0
 */
@Service
public class BpmServiceImpl implements BpmService {

	@Autowired
	private HistoryService historyService;

	@Autowired
	private TaskService taskService;
	
	@Autowired
	private ActivitiHelpDao activitiHelpDao;
	
	@Autowired
	private RepositoryService repositoryService;
	
	@Autowired
	private RuntimeService runtimeService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gionee.gniflow.biz.service.BpmService#getHisProcesVariable(java.util
	 * .Map, java.lang.String)
	 */
	@Override
	public Map<String, Object> getHisProcesVariable(Map<String, Object> model,
			String processInstanceId, boolean onlySign) {
		List<ProcessStep> stepList = null;
		if (model == null) {
			model = new LinkedHashMap<String, Object>();
		}

		//查询流程的执行信息，排除"跳过的节点"
		List<HistoricActivityInstance> hisActivityInstanceList = activitiHelpDao.queryTrackHistoricActivityInstance(processInstanceId);
		
		if (!CollectionUtils.isEmpty(hisActivityInstanceList)) {
			// 循环List，找出连续ACT_ID_连续相同的ACTINST
			List<List<HistoricActivityInstance>> actList = mergeContinuousHisAct(hisActivityInstanceList);

			stepList = new ArrayList<ProcessStep>();
			ProcessStep step = null;
			// //
			for (List<HistoricActivityInstance> listEle : actList) {
				//当只有一个会签人员时，需要判断节点是不是以MultiInstTask结尾，是说明是会签，否则为普通任务节点
				if (listEle.size() > 1 || (listEle.get(0).getActivityId().endsWith(BPMConstant.BPMN_MULTIINSTTASK_SUFFIX))) {
					//多实例任务
					HistoricActivityInstance activitiInstance = listEle.get(0);
					
					step = new ProcessStep(activitiInstance.getActivityId(), new LinkedHashMap<String,Object>());
					step.setStartTime(activitiInstance.getStartTime());
					step.setEndTime(activitiInstance.getEndTime());
					step.setAssignee(activitiInstance.getAssignee());
					step.setDurationInMillis(activitiInstance.getDurationInMillis());
					step.setActivityName(activitiInstance.getActivityName());
					for (HistoricActivityInstance activityIns : listEle){
						ProcessStep muliStep = createProcessStep(activityIns, processInstanceId, true);
						step.getMultiInstances().add(muliStep);
					}
				} else {
					if (!onlySign) {
						//非多实例
						step = createProcessStep(listEle.get(0), processInstanceId, false);
					}
				}
				if (step != null && step.getVars() != null) {
					stepList.add(step);
				}
			}
		}

		model.put(BPMConstant.PROCESS_STEPS, stepList);
		return model;
	}
	
	@Override
	public Map<String, Object> getEntireProcesVariable(Map<String, Object> model, String processInstanceId,
			boolean onlySign) {
		// TODO Auto-generated method stub
		List<ProcessStep> stepList = new ArrayList<ProcessStep>();;
		List<String> stepNameList = new ArrayList<String>();
		if (model == null) {
			model = new LinkedHashMap<String, Object>();
		}
		//查询流程的执行信息，排除"跳过的节点"
		List<HistoricActivityInstance> hisActivityInstanceList = activitiHelpDao.queryTrackHistoricActivityInstance(processInstanceId);
		
		if (!CollectionUtils.isEmpty(hisActivityInstanceList)) {
			// 循环List，找出连续ACT_ID_连续相同的ACTINST
			List<List<HistoricActivityInstance>> actList = mergeContinuousHisAct(hisActivityInstanceList);
			ProcessStep step = null;
			// //
			for (List<HistoricActivityInstance> listEle : actList) {
				//当只有一个会签人员时，需要判断节点是不是以MultiInstTask结尾，是说明是会签，否则为普通任务节点
				if (listEle.size() > 1 || (listEle.get(0).getActivityId().endsWith(BPMConstant.BPMN_MULTIINSTTASK_SUFFIX))) {
					//多实例任务
					HistoricActivityInstance activitiInstance = listEle.get(0);
					
					step = new ProcessStep(activitiInstance.getActivityId(), new LinkedHashMap<String,Object>());
					step.setStartTime(activitiInstance.getStartTime());
					step.setEndTime(activitiInstance.getEndTime());
					step.setAssignee(activitiInstance.getAssignee());
					step.setDurationInMillis(activitiInstance.getDurationInMillis());
					step.setActivityName(activitiInstance.getActivityName());
					stepNameList.add(activitiInstance.getActivityName());
					for (HistoricActivityInstance activityIns : listEle){
						ProcessStep muliStep = createProcessStep(activityIns, processInstanceId, true);
						step.getMultiInstances().add(muliStep);
					}
				} else {
					if (!onlySign) {
						//非多实例
						step = createProcessStep(listEle.get(0), processInstanceId, false);
					}
				}
				if (step != null && step.getVars() != null) {
					stepList.add(step);
					stepNameList.add(step.getKey());
				}
			}
			List<TaskDefinition> TaskDefinitions = nextTaskDefinitions(processInstanceId);
			if(TaskDefinitions != null&&TaskDefinitions.size() > 0){
				for(TaskDefinition taskDef : TaskDefinitions){
					if(!stepNameList.contains(taskDef.getKey())){
						ProcessStep futureStep  =  new ProcessStep();
						futureStep.setKey(taskDef.getKey());
						String activityName = taskDef.getNameExpression().getExpressionText();
						futureStep.setActivityName(activityName);
						stepList.add(futureStep);
					}
				}
			}
		}
		model.put(BPMConstant.PROCESS_STEPS, stepList);
		return model;
	}
	/*
	 * (non-Javadoc)
	 * @see com.gionee.gniflow.biz.service.BpmService#getSignProcessStepList(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ProcessStep> getSignProcessStepList(String processInstanceId) {
		Map<String,Object> resultMap = this.getHisProcesVariable(new LinkedHashMap<String, Object>(), processInstanceId, true);
		List<ProcessStep> list = (List<ProcessStep>) resultMap.get(BPMConstant.PROCESS_STEPS);
		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public ProcessHisVariable getAllHisVariables(String processInstanceId) {
		
		ProcessHisVariable hisVariable = new ProcessHisVariable();
		Map<String,Object> hisVars = new HashMap<String,Object>();
		Map<String,List<List<MapFormElement>>> hisSers = new HashMap<String,List<List<MapFormElement>>>();
		
		List<HistoricDetail> processVars = historyService.createHistoricDetailQuery()
				.processInstanceId(processInstanceId).list();
		
		for (HistoricDetail hisDetail : processVars) {
     		HistoricDetailVariableInstanceUpdateEntity entity = (HistoricDetailVariableInstanceUpdateEntity)hisDetail;
     		//System.out.println(entity.getVariableTypeName());
     		if (entity.getVariableTypeName().equals("serializable")) {
     			//反序列化
     			byte[] bytes = entity.getBytes();
     	        try {        
     	            ByteArrayInputStream bis = new ByteArrayInputStream (bytes);        
     	            ObjectInputStream ois = new ObjectInputStream (bis);        
     	            List<List<MapFormElement>> obj = (List<List<MapFormElement>>) ois.readObject();   
     	            ois.close();   
     	            bis.close();   
     	            
     	           hisSers.put(entity.getName(), obj);
     	        } catch (IOException ex) {        
     	            ex.printStackTrace();   
     	        } catch (ClassNotFoundException ex) {        
     	            ex.printStackTrace();   
     	        }      
     		} else {
     			hisVars.put(entity.getName(), entity.getTextValue());
     		}
     	}
		
		List<HistoricActivityInstance> hisActivityInstanceList = activitiHelpDao.queryTrackHistoricActivityInstance(processInstanceId);
		// 处理附件
		List<Attachment> attachmentList = new ArrayList<Attachment>();
		attachmentList.addAll(taskService.getProcessInstanceAttachments(processInstanceId));
		
		if (!CollectionUtils.isEmpty(hisActivityInstanceList)) {
			for (HistoricActivityInstance hisAct : hisActivityInstanceList){
				attachmentList.addAll(taskService.getTaskAttachments(hisAct.getTaskId()));
			}
		}
		
		hisVariable.setVars(hisVars);
		hisVariable.setMapSers(hisSers);
		hisVariable.setAttachments(attachmentList);
		
		return hisVariable;
	}

	/**************************************************************************************/
	/**
	 * 根据ACT_ID处理连续性元素
	 * 当流程修改重新执行审批时,需要根据ACT_HI_ACTINST的ID_进行排序查询历史的流程节点(是流程流转的顺序数据)，
	 * 然后找出ACT_ID_连续的元素，进行处理，方便根据每个节点查找历史数据，尤其是会签数据。
	 * 
	 * @param srcMaterials
	 * @return
	 */
	private List<List<HistoricActivityInstance>> mergeContinuousHisAct(
			List<HistoricActivityInstance> srcHistoricActivityInstance) {

		List<List<HistoricActivityInstance>> resultSets = new ArrayList<List<HistoricActivityInstance>>();

		HistoricActivityInstance curHisInst = null;
		// 合并连续重复元素
		for (int i = 0; i < srcHistoricActivityInstance.size(); i++) {
			curHisInst = srcHistoricActivityInstance.get(i);
			// 存储连续的元素
			Set<HistoricActivityInstance> set = new LinkedHashSet<HistoricActivityInstance>();// 多个
			// 存储非连续的元素
			List<HistoricActivityInstance> singSet = new ArrayList<HistoricActivityInstance>();// 单个
			for (int j = i + 1; j < srcHistoricActivityInstance.size(); j++) {
				if (curHisInst.getActivityId().equals(
						srcHistoricActivityInstance.get(j).getActivityId())) {
					set.add(srcHistoricActivityInstance.get(i));
					set.add(srcHistoricActivityInstance.get(j));
				} else {
					break;
				}
			}
			if (set.size() > 0) {
				i = i + set.size() - 1;
				resultSets.add(set2List(set));
			} else {
				singSet.add(curHisInst);
				resultSets.add(singSet);
			}
		}
		return resultSets;
	}

	/**
	 * Set转List
	 * 
	 * @param userSet
	 * @return
	 */
	private static List<HistoricActivityInstance> set2List(
			Set<HistoricActivityInstance> userSet) {
		List<HistoricActivityInstance> users = new ArrayList<HistoricActivityInstance>();
		users.addAll(userSet);
		return users;
	}

	/**
	 * 创建历史表单对象ProcessStep
	 * 
	 * @param activitiInstance
	 * @param processInstanceId
	 * @return
	 */
	private ProcessStep createProcessStep(HistoricActivityInstance activitiInstance,
			String processInstanceId, boolean isMuliInst) {

		ProcessStep step = null;
		// 查询历史流程实例以及任务实例相关信息
		List<HistoricDetail> processVars = null;
		// List<HistoricDetail> processVars = historyService
		// .createHistoricDetailQuery().activityInstanceId(activitiInstance.getId()).list();
		if (isMuliInst) {
			processVars = historyService.createHistoricDetailQuery().executionId(processInstanceId)
					.activityInstanceId(activitiInstance.getId()).list();
			//ACT_INST_ID_字段值为空时,根据实例编号以创建时间为降序排列
			if(CollectionUtils.isEmpty(processVars)){
				processVars = historyService.createHistoricDetailQuery()
					.processInstanceId(processInstanceId).orderByTime().desc().list();
			}
			
			//流程文档会签流程反复遍历包含网关中的多人审核节点单独取数
			if(activitiInstance.getProcessDefinitionId().contains("L-Project-Human-Input-Account")) {
				Integer loopTimes = activitiHelpDao.getActHiDetailLoopTimesByProcInstId(processInstanceId);
				if(loopTimes != null && loopTimes > 1) {
					String lastFlowAuditId = activitiHelpDao.getActHiDetailLastFlowByProcInstId(processInstanceId,activitiInstance.getId());
					String beginId = activitiHelpDao.getActHiDetailBeginIdByProcInstId(processInstanceId,lastFlowAuditId);
					String nextFlowAuditId = "";
					String endId = "";
					nextFlowAuditId = activitiHelpDao.getActHiDetailNextFlowByProcInstId(processInstanceId,activitiInstance.getId());
					if(nextFlowAuditId != null && !nextFlowAuditId.equals("")) {
						endId = activitiHelpDao.getActHiDetailEndIdByProcInstId(processInstanceId,nextFlowAuditId);
					}
					
					List<HistoricDetail> processVarsTmp = new ArrayList<HistoricDetail>();
					if(endId.equals("")) {
						for(HistoricDetail hisDetail : processVars) {
							if(Long.parseLong(hisDetail.getId()) > Long.parseLong(beginId)) {
								processVarsTmp.add(hisDetail);
							}
						}
					} else {
						for(HistoricDetail hisDetail : processVars) {
							if(Long.parseLong(hisDetail.getId()) > Long.parseLong(beginId) && Long.parseLong(hisDetail.getId()) < Long.parseLong(endId)) {
								processVarsTmp.add(hisDetail);
							}
						}
					}
					
					if(processVarsTmp != null && processVarsTmp.size() > 0) {
						processVars = processVarsTmp;
					}
				}
			}
			
		} else {
			processVars = historyService.createHistoricDetailQuery()
					.activityInstanceId(activitiInstance.getId()).list();
			//并行及包含网关通过Activity_ID查询不到数据，只能从流程中查找--临时方案
			if(CollectionUtils.isEmpty(processVars)){
				processVars = historyService.createHistoricDetailQuery()
						.processInstanceId(processInstanceId).orderByTime().desc().list();
			}
			
			//配件定价流程反复遍历包含网关中的节点单独取数
			if(activitiInstance.getProcessDefinitionId().contains("L-Accessories-Pricing-Flow")) {
				Integer loopTimes = activitiHelpDao.getActHiDetailLoopTimesByProcInstId(processInstanceId);
				if(loopTimes != null && loopTimes > 1) {
					String lastId = activitiHelpDao.getActHiDetailLastIdByProcInstId(processInstanceId,activitiInstance.getId(),"MakeShipRetailPrice");
					if(lastId != null && !lastId.equals("")) {
						String beginId = activitiHelpDao.getActHiDetailBeginIdByProcInstId(processInstanceId,lastId);
						String nextId = "";
						String endId = "";
						nextId = activitiHelpDao.getActHiDetailNextIdByProcInstId(processInstanceId,activitiInstance.getId(),"MakeShipRetailPrice");
						if(nextId != null && !nextId.equals("")) {
							endId = activitiHelpDao.getActHiDetailEndIdByProcInstId(processInstanceId,nextId);
						}
						
						List<HistoricDetail> processVarsTmp = new ArrayList<HistoricDetail>();
						if(endId.equals("")) {
							for(HistoricDetail hisDetail : processVars) {
								if(Long.parseLong(hisDetail.getId()) > Long.parseLong(beginId)) {
									processVarsTmp.add(hisDetail);
								}
							}
						} else {
							for(HistoricDetail hisDetail : processVars) {
								if(Long.parseLong(hisDetail.getId()) > Long.parseLong(beginId) && Long.parseLong(hisDetail.getId()) < Long.parseLong(endId)) {
									processVarsTmp.add(hisDetail);
								}
							}
						}
						
						if(processVarsTmp != null && processVarsTmp.size() > 0) {
							processVars = processVarsTmp;
						}
					}
				}
			}
			
		}

		if (!CollectionUtils.isEmpty(processVars)) {
			step = new ProcessStep(activitiInstance.getActivityId(),
					new LinkedHashMap<String, Object>());
			step.setStartTime(activitiInstance.getStartTime());
			step.setEndTime(activitiInstance.getEndTime());
			step.setAssignee(activitiInstance.getAssignee());
			step.setDurationInMillis(activitiInstance.getDurationInMillis());
			step.setActivityName(activitiInstance.getActivityName());

			for (HistoricDetail hisDetail : processVars) {
				HistoricDetailVariableInstanceUpdateEntity entity = (HistoricDetailVariableInstanceUpdateEntity) hisDetail;
				step.getVars().put(entity.getName(), entity.getTextValue());
			}

			// 处理附件
			List<Attachment> attachmentList = null;
			if (activitiInstance.getActivityType().equals("startEvent")) {
				attachmentList = taskService
						.getProcessInstanceAttachments(processInstanceId);
			} else {
				attachmentList = taskService
						.getTaskAttachments(activitiInstance.getTaskId());
			}

			if (attachmentList != null && attachmentList.size() > 0) {
				step.setAttachments(attachmentList);
			} else {
				step.setAttachments(null);
			}
		}
		return step;
	}
	/**
	 * 获取流程的未完成的确定的节点
	 * @param procInstId
	 * @return
	 */
    private List<TaskDefinition> nextTaskDefinitions(String procInstId){  
    	List<TaskDefinition> tasksDefinitions = new ArrayList<TaskDefinition>();
        //流程标示  
        String processDefinitionId = historyService.createHistoricProcessInstanceQuery().processInstanceId(procInstId).singleResult().getProcessDefinitionId();  
          
        ProcessDefinitionEntity def = (ProcessDefinitionEntity) ((RepositoryServiceImpl)repositoryService).getDeployedProcessDefinition(processDefinitionId);  
        //执行实例  
        ExecutionEntity execution = (ExecutionEntity) runtimeService.createProcessInstanceQuery().processInstanceId(procInstId).active().singleResult();
        if(execution!=null){
        	//当前实例的执行到哪个节点  
        	String activitiId = execution.getActivityId();  
        	//获得当前任务的所有节点  
        	List<ActivityImpl> activitiList = def.getActivities();  
        	String id = null;  
        	for(ActivityImpl activityImpl:activitiList){    
        		id = activityImpl.getId();   
        		if(activitiId!=null&&activitiId.equals(id)){  
        			System.out.println("当前任务："+activityImpl.getProperty("name"));  
        			nextTaskDefinitions(activityImpl,tasksDefinitions,false);  
        		}  
        	}  
        }
        return tasksDefinitions;  
    }
    /**
     * 获取下一个确定的流程节点
     * @param activityImpl
     * @param tasksDefinitions
     */
    private void nextTaskDefinitions(ActivityImpl activityImpl, List<TaskDefinition> tasksDefinitions,boolean flag){
    	 	TaskDefinition taskDefinition ;
    	 	List<PvmTransition> outTransitions = activityImpl.getOutgoingTransitions();
    	 	PvmTransition suredPvmTransition = null;
	        if("userTask".equals(activityImpl.getProperty("type"))){  
	            taskDefinition = ((UserTaskActivityBehavior)activityImpl.getActivityBehavior()).getTaskDefinition(); 
	            System.out.println(taskDefinition.getKey());
	            tasksDefinitions.add(taskDefinition);
	        }   
            //默认流转方向
            if(outTransitions.size()>1){
	            	for(PvmTransition tr:outTransitions){
	            		if(activityImpl.getProperty("type")!="exclusiveGateway"&&activityImpl.getProperty("default")!=null&&activityImpl.getProperty("default").equals(tr.getId())){
	            			suredPvmTransition = tr;
	            			System.out.println("当前任务有默认的流转线"+activityImpl.getProperty("name")+"继续");  
	            		}
	            	}
            }else{
            	suredPvmTransition =  outTransitions.get(0);
            }
            //默认流转方向
            if("parallelGateway".equals(activityImpl.getProperty("type"))||"inclusiveGateway".equals(activityImpl.getProperty("type"))){
	            if(outTransitions.size()>1){
	            	for(int i=0;i<outTransitions.size()-1;i++){
	            		 	PvmActivity destination = outTransitions.get(i).getDestination();
	            		 	nextTaskDefinitions((ActivityImpl)destination,tasksDefinitions,flag);    
	            		 	System.out.println("当前任务有默认的流转线"+activityImpl.getProperty("name")+"继续");  
	            	}
	            	PvmActivity destination = outTransitions.get(outTransitions.size()-1).getDestination();
	            	nextTaskDefinitions((ActivityImpl)destination,tasksDefinitions,true);  
	            }else{
	            	if(!flag){
	            		return ;
	            	}else{
	            		System.out.println("当前任务只有一条流转线。继续");
	            		flag = false;
	            	}
	            }
            }

            if(suredPvmTransition != null){
                PvmActivity destination = suredPvmTransition.getDestination(); //获取线路的终点节点    
                if("exclusiveGateway".equals(destination.getProperty("type"))){
                	 nextTaskDefinitions((ActivityImpl)destination,tasksDefinitions,flag);    
                }else if("userTask".equals(destination.getProperty("type"))){ 
                	 nextTaskDefinitions((ActivityImpl)destination,tasksDefinitions,flag);  
                }else if("endEvent".equals(destination.getProperty("type"))){
                	System.out.println("结束");  
                }else if("serviceTask".equals(destination.getProperty("type"))){
                	System.out.println("跳过"); 
                	nextTaskDefinitions((ActivityImpl)destination,tasksDefinitions,flag);
                }else if("parallelGateway".equals(destination.getProperty("type"))){
                	nextTaskDefinitions((ActivityImpl)destination,tasksDefinitions,flag);
                }else if("inclusiveGateway".equals(destination.getProperty("type"))){
                	nextTaskDefinitions((ActivityImpl)destination,tasksDefinitions,flag);
                }else{
                	System.out.println("无法确定走向"); 
                }
            }else{
            	System.out.println("没有确定的走向");  
            }
 
    }
    
    
}
