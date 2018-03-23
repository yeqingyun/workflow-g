package com.gionee;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.activiti.engine.HistoryService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.persistence.entity.HistoricProcessInstanceEntity;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gionee.gnif.dto.QueryMap;
import com.gionee.gnif.util.AppContext;
import com.gionee.gniflow.biz.model.BpmProcessRun;
import com.gionee.gniflow.biz.service.ActivitiHelpService;
import com.gionee.gniflow.biz.service.BpmProcessRunService;
import com.gionee.gniflow.web.bmp.BpmHisProcessInstanceEntity;
import com.gionee.gniflow.web.bmp.BpmHisTaskEntity;
import com.gionee.gniflow.web.bmp.BpmTaskEntity;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:biz-context.xml")
public class ActivitiHelpServiceTest {

	@Autowired
	private ActivitiHelpService activitiHelpService;
	
	@Autowired
	private HistoryService historyService;
	
	@Autowired
	private BpmProcessRunService bpmProcessRunService;
	
	@Test
	@Ignore
	public void testQueryAssigneeTask(){
		List<Object> list = new ArrayList<Object>();
		list.add("purchase-requisition");
		
		QueryMap queryMap = new QueryMap();
		queryMap.put("assignee", "00001021");
		queryMap.put("suspensionState", 1);
		queryMap.put("sapProcessIds", list);
		queryMap.put("firstRow", 1);
		queryMap.put("lastRow", 20);
		
		List<BpmTaskEntity> dtos = activitiHelpService.page4AssigneeTask(queryMap);
		System.out.println("size-->" + dtos.size());
		
		System.out.println(activitiHelpService.count4AssigneeTask(queryMap));
	}
	
	@Test
	@Ignore
	public void testQueryCompletedTask(){
		List<Object> list = new ArrayList<Object>();
		list.add("purchase-requisition");
		
		QueryMap queryMap = new QueryMap();
		queryMap.put("assignee", "00001021");
		queryMap.put("sapProcessIds", list);
		queryMap.put("firstRow", 1);
		queryMap.put("lastRow", 20);
		
		List<BpmHisTaskEntity> dtos = activitiHelpService.page4CompletedTask(queryMap);
		System.out.println("size-->" + dtos.size());
		
		System.out.println(activitiHelpService.count4CompletedTask(queryMap));
	}
	
	@Test
	@Ignore
	public void testQueryStartProcess(){
		List<Object> list = new ArrayList<Object>();
		list.add("purchase-requisition");
		
		QueryMap queryMap = new QueryMap();
		queryMap.put("assignee", "ali");
		queryMap.put("sapProcessIds", list);
		queryMap.put("finished", true);
		queryMap.put("firstRow", 1);
		queryMap.put("lastRow", 20);
		
		List<BpmHisProcessInstanceEntity> dtos = activitiHelpService.page4StartProcess(queryMap);
		System.out.println("size-->" + dtos.size());
		
		System.out.println(activitiHelpService.count4StartProcess(queryMap));
	}
	
	@Test
	@Ignore
	public void testQueryInvolvedProcess(){
		List<Object> list = new ArrayList<Object>();
		list.add("purchase-requisition");
		
		QueryMap queryMap = new QueryMap();
		queryMap.put("assignee", "ali");
		queryMap.put("sapProcessIds", list);
		queryMap.put("firstRow", 1);
		queryMap.put("lastRow", 20);
		
		List<BpmHisProcessInstanceEntity> dtos = activitiHelpService.page4InvolvedUserProcess(queryMap);
		System.out.println("size-->" + dtos.size());
		
		System.out.println(activitiHelpService.count4InvolvedUserProcess(queryMap));
	}
	
	@Test
	public void testInsert(){
		List<HistoricProcessInstance> list = historyService.createHistoricProcessInstanceQuery().list();
		for (HistoricProcessInstance his : list) {
			HistoricProcessInstanceEntity en = (HistoricProcessInstanceEntity) his;
			BpmProcessRun bpmProcessRun = new BpmProcessRun();
			bpmProcessRun.setProcessDefKey(en.getProcessDefinitionId());
			bpmProcessRun.setProcessInstId(en.getProcessInstanceId());
			bpmProcessRun.setStartTime(en.getStartTime());
			bpmProcessRun.setStatus(BpmProcessRun.STATUS_RUN);
			bpmProcessRun.setReason("流程正常启动");
			bpmProcessRun.setStartUserAccount(en.getStartUserId());
			bpmProcessRunService.save(bpmProcessRun);
		}
	}
	
}
