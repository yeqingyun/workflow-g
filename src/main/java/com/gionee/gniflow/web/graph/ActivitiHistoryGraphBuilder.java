package com.gionee.gniflow.web.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.impl.HistoricActivityInstanceQueryImpl;
import org.activiti.engine.impl.Page;
import org.activiti.engine.impl.cmd.GetDeploymentProcessDefinitionCmd;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.gionee.gniflow.biz.service.ActivitiHelpService;
import com.gionee.gniflow.web.util.SpringTools;

public class ActivitiHistoryGraphBuilder {
    private static Logger logger = LoggerFactory.getLogger(ActivitiHistoryGraphBuilder.class);
    private String processInstanceId;
    private ProcessDefinitionEntity processDefinitionEntity;
    private List<HistoricActivityInstance> historicActivityInstances;
    private List<HistoricActivityInstance> visitedHistoricActivityInstances = new ArrayList<HistoricActivityInstance>();
    private Map<String, Node> nodeMap = new HashMap<String, Node>();
    
    private static List<String> boundaryEventList = new ArrayList<String>();

    public ActivitiHistoryGraphBuilder(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }
    
    static{
    	//初始化边界事件
    	boundaryEventList.add("boundaryTimer");
    	boundaryEventList.add("boundaryMessage");
    	boundaryEventList.add("boundarySignal");
    	
    }
    
    public Graph build() {
        this.fetchProcessDefinitionEntity();
        this.fetchHistoricActivityInstances();

        Graph graph = new Graph();

        for (HistoricActivityInstance historicActivityInstance : historicActivityInstances) {
            Node currentNode = new Node();
            currentNode.setId(historicActivityInstance.getId());
            currentNode.setName(historicActivityInstance.getActivityId());
            currentNode.setType(historicActivityInstance.getActivityType());
            currentNode.setActive(historicActivityInstance.getEndTime() == null);
            currentNode.setProInstId(historicActivityInstance.getProcessInstanceId());
            logger.debug("currentNode : {}:{}", currentNode.getName(),
                    currentNode.getId());
            //找到前一个元素
            Edge previousEdge = this.findPreviousEdge(currentNode,
                    historicActivityInstance.getStartTime().getTime());

            if (previousEdge == null) {
                if (graph.getInitial() != null) {
                    throw new IllegalStateException("already set an initial.");
                }

                graph.setInitial(currentNode);
            } else {
                logger.debug("previousEdge : {}", previousEdge.getName());
            }

            nodeMap.put(currentNode.getId(), currentNode);
            visitedHistoricActivityInstances.add(historicActivityInstance);
        }

        if (graph.getInitial() == null) {
            throw new IllegalStateException("cannot find initial.");
        }
        return graph;
    }

    public void fetchProcessDefinitionEntity() {
        String processDefinitionId = Context.getCommandContext()
                .getHistoricProcessInstanceEntityManager()
                .findHistoricProcessInstance(processInstanceId)
                .getProcessDefinitionId();
        GetDeploymentProcessDefinitionCmd cmd = new GetDeploymentProcessDefinitionCmd(
                processDefinitionId);
        processDefinitionEntity = cmd.execute(Context.getCommandContext());
    }

    public void fetchHistoricActivityInstances() {
        HistoricActivityInstanceQueryImpl historicActivityInstanceQueryImpl = new HistoricActivityInstanceQueryImpl();
        // TODO: 如果用了uuid会造成这样排序出问题
        // 但是如果用startTime，可能出现因为处理速度太快，时间一样，导致次序颠倒的问题
        historicActivityInstanceQueryImpl.processInstanceId(processInstanceId)
                .orderByHistoricActivityInstanceId().asc();
        
//      historicActivityInstanceQueryImpl.processInstanceId(processInstanceId)
// 		.orderByHistoricActivityInstanceStartTime().asc();

        Page page = new Page(0, 100);
        historicActivityInstances = Context
                .getCommandContext()
                .getHistoricActivityInstanceEntityManager()
                .findHistoricActivityInstancesByQueryCriteria(
                        historicActivityInstanceQueryImpl, page);
    }

    public Edge findPreviousEdge(Node currentNode, long currentStartTime) {
        String activityId = currentNode.getName();
        ActivityImpl activityImpl = processDefinitionEntity
                .findActivity(activityId);
        HistoricActivityInstance nestestHistoricActivityInstance = null;
        String temporaryPvmTransitionId = null;
        
        //修改主要部分
        List<PvmTransition> activityImpList = activityImpl.getIncomingTransitions();
        //边界事件没有进入当前的节点的连接线，需要特殊处理，开始节点除外
        if (CollectionUtils.isEmpty(activityImpList) && boundaryEventList.contains(currentNode.getType())){
        	//找边界事件的上一个节点
        	HistoricActivityInstance previousHisActInstance = findPreviousHisActInstance4BoundaryId(currentNode.getName());
        	if (previousHisActInstance != null) {
        		Node previousNode = nodeMap.get(previousHisActInstance.getId());
            	
            	Edge edge = new Edge();
            	edge.setType(currentNode.getType());
                edge.setName(activityId);
                previousNode.getEdges().add(edge);//前一个节点将当前的连线加入进去了（衔接元素）
                edge.setSrc(previousNode);
                edge.setDest(currentNode);
                return edge;
        	} else {
        		throw new IllegalStateException("cannot find previous historicActivityInstance.");
        	}
        } else {
            // 遍历进入当前节点的所有连线
            for (PvmTransition pvmTransition : activityImpList) {
                PvmActivity source = pvmTransition.getSource();

                String previousActivityId = source.getId();
               
                HistoricActivityInstance visitiedHistoryActivityInstance = this
                        .findVisitedHistoricActivityInstance(previousActivityId);
                
                if (visitiedHistoryActivityInstance == null) {
                    continue;
                }
                
                //update by lipw-解决包含网关跟踪报错问题
                String actType = (String) source.getProperty("type");
                Date visitiedHisActivityEndTime = visitiedHistoryActivityInstance.getEndTime();
                if (actType.equals("inclusiveGateway") && visitiedHisActivityEndTime == null){
                	//如果前一个节点是包含关口，需查询ACT_HI_ACTINST表，相同节点名离该条记录最近的且
                	
                    HistoricActivityInstanceQueryImpl historicActivityInstanceQueryImpl = new HistoricActivityInstanceQueryImpl();
                    historicActivityInstanceQueryImpl.processInstanceId(processInstanceId).activityId(previousActivityId);
                	
                	List<HistoricActivityInstance> activityInstances = Context.getCommandContext()
                			.getHistoricActivityInstanceEntityManager()
                            .findHistoricActivityInstancesByQueryCriteria(
                                     historicActivityInstanceQueryImpl, new Page(0, 100));
                	
                	if (CollectionUtils.isEmpty(activityInstances)) {
                		throw new IllegalStateException("cannot find history inclusiveGateway activity.");
                	} else {            		
                		String isMore = ""; //判断包含网关多分支是否多人审核
                		ActivitiHelpService activitiHelpService = (ActivitiHelpService) SpringTools.getBean(ActivitiHelpService.class);
                		for (HistoricActivityInstance hisActInst : activityInstances) {
                			//找到与包含网关相同ExecutionID相同的非包含网关的节点的结束时间
                			//保险起见，采用查询一次，跟新一次的方式，而非一条SQL完成，避免ACT_HI_ACTINST的END_TIME_被全部更新
                			HistoricActivityInstance perHisActInst = activitiHelpService
                					.queryPreviousNotInclusivegatewayHisActInst4CurNode(
                							hisActInst.getProcessInstanceId(), hisActInst.getExecutionId(), "inclusiveGateway");
                			if (perHisActInst != null) {
                				//跟新包含网关的endtime为进入该网关的节点的endtime
                				activitiHelpService.updateHisInclusivegatewayEndTime(
                						hisActInst.getProcessInstanceId(), 
                						hisActInst.getExecutionId(), perHisActInst.getEndTime());
                				
                				visitiedHisActivityEndTime = perHisActInst.getEndTime();
                			} else {
                				isMore = "yes";
                				break;
                			}      			
                		}
                		 			
                		//多分支多人审核查找最近节点
                		if(isMore.equals("yes")) { 
                			int posIndex = activityInstances.size() - 1;
                			String lastId = activityInstances.get(posIndex).getId();                			
                			String instanceId = activityInstances.get(posIndex).getProcessInstanceId();
                			String maxId = activitiHelpService.queryInclusivegatewayHisActInstMaxId(instanceId,lastId);
                			String minId = activitiHelpService.queryInclusivegatewayHisActInstMinId(instanceId,maxId);
                			List<HistoricActivityInstance> inclusivegatewayBetNode = activitiHelpService.queryInclusivegatewayBetNode(instanceId,minId,maxId);
                			int i = 0;
                			for (HistoricActivityInstance hisActInst : activityInstances) {
                				if(Long.parseLong(hisActInst.getId()) > Long.parseLong(maxId) && i < inclusivegatewayBetNode.size()) {
	                				HistoricActivityInstance perHisActInst = inclusivegatewayBetNode.get(i);
	                				i++;
	                				
	                				if (perHisActInst != null) {
	                    				//跟新包含网关的endtime为进入该网关的节点的endtime
	                    				activitiHelpService.updateHisInclusivegatewayEndTime(
	                    						hisActInst.getProcessInstanceId(), 
	                    						hisActInst.getExecutionId(), perHisActInst.getEndTime());
	                    				
	                    				visitiedHisActivityEndTime = perHisActInst.getEndTime();
	                    			}
                				} else {
	                				HistoricActivityInstance perHisActInst = inclusivegatewayBetNode.get(inclusivegatewayBetNode.size() - 1);
	                				
	                				if (perHisActInst != null) {
	                    				//跟新包含网关的endtime为进入该网关的节点的endtime
	                    				activitiHelpService.updateHisInclusivegatewayEndTime(
	                    						hisActInst.getProcessInstanceId(), 
	                    						hisActInst.getExecutionId(), perHisActInst.getEndTime());
	                    				
	                    				visitiedHisActivityEndTime = perHisActInst.getEndTime();
	                    			}
                				}
                			}              			
                		}
                	}
                } 
                //update end
                
                
                // 如果上一个节点还未完成，说明不可能是从这个节点过来的，跳过
                if (visitiedHisActivityEndTime == null) {
                    continue;
                }

                logger.debug("current activity start time : {}", new Date(
                        currentStartTime));
                logger.debug("nestest activity end time : {}",
                		visitiedHisActivityEndTime);

                // 如果当前节点的开始时间，比上一个节点的结束时间要早，跳过
                if (currentStartTime < visitiedHisActivityEndTime.getTime()) {
                    continue;
                }

                if (nestestHistoricActivityInstance == null) {
                    nestestHistoricActivityInstance = visitiedHistoryActivityInstance;
                    temporaryPvmTransitionId = pvmTransition.getId();
                } else if ((currentStartTime - nestestHistoricActivityInstance
                        .getEndTime().getTime()) > (currentStartTime - visitiedHistoryActivityInstance
                        .getEndTime().getTime())) {
                    // 寻找离当前节点最近的上一个节点
                    // 比较上一个节点的endTime与当前节点startTime的差
                    nestestHistoricActivityInstance = visitiedHistoryActivityInstance;
                    temporaryPvmTransitionId = pvmTransition.getId();
                }
            }
        }

        // 没找到上一个节点，就返回null
        if (nestestHistoricActivityInstance == null) {
            return null;
        }

        Node previousNode = nodeMap
                .get(nestestHistoricActivityInstance.getId());

        if (previousNode == null) {
            return null;
        }

        logger.debug("previousNode : {}:{}", previousNode.getName(),
                previousNode.getId());

        Edge edge = new Edge();
        edge.setType(currentNode.getType());
        edge.setName(temporaryPvmTransitionId);
        previousNode.getEdges().add(edge);
        edge.setSrc(previousNode);
        edge.setDest(currentNode);

        return edge;
    }

    public HistoricActivityInstance findVisitedHistoricActivityInstance(
            String activityId) {
        for (int i = visitedHistoricActivityInstances.size() - 1; i >= 0; i--) {
            HistoricActivityInstance historicActivityInstance = visitedHistoricActivityInstances
                    .get(i);

            if (activityId.equals(historicActivityInstance.getActivityId())) {
                return historicActivityInstance;
            }
        }

        return null;
    }
    
    /**
     * 根据边界事件的ID找到上一个历史节点
     * @param boundaryId
     * @return
     */
    private HistoricActivityInstance findPreviousHisActInstance4BoundaryId(String boundaryId){
    	Integer index = null;
    	for (int i = 0; i < historicActivityInstances.size(); i++) {
			if (historicActivityInstances.get(i).getActivityId().equals(boundaryId)){
				index = i;
			}
		}
    	if (index != null && index - 1 >= 0) {
    		return historicActivityInstances.get(index - 1);
    	}
    	return null;
    }
}
