package com.gionee;

import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gionee.gniflow.biz.service.BpmConfProcessRoleService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:biz-context.xml")
public class BpmConfProcessRoleServiceTest {

	@Autowired
	private BpmConfProcessRoleService bpmConfProcessRoleService;
	
	@Test
	@Ignore
	public void testGetSepicalRoleMaster(){
		ExecutionEntity execution = new ExecutionEntity();
		execution.setProcessDefinitionId("Test-Handle:108:21423");
		String assignee = bpmConfProcessRoleService.getSpecialRoleMaster("考勤专员", execution);
		System.out.println("assignee-->" + assignee);
	}
	
	@Test
	@Ignore
	public void testGetSepicalAreaRoleMaster(){
		ExecutionEntity execution = new ExecutionEntity();
		execution.setProcessDefinitionId("Test-Handle:108:21423");
		String assignee = bpmConfProcessRoleService.getSpecialAreaRoleMaster("绩效专员", "00001222", execution);
		System.out.println("assignee-->" + assignee);
	}
	
	@Test
	@Ignore
	public void testGetSepicalDeptMaster(){
		ExecutionEntity execution = new ExecutionEntity();
		execution.setProcessDefinitionId("Test-Handle:108:21423");
		String assignee = bpmConfProcessRoleService.getSpecialDeptMaster("薪酬专员", 56905, execution);
		System.out.println("assignee-->" + assignee);
	}
	
	@Test
	@Ignore
	public void testGetSepicalAreaDeptMaster(){
		ExecutionEntity execution = new ExecutionEntity();
		execution.setProcessDefinitionId("L-Personnel-Requirement:108:21423");
		String assignee = bpmConfProcessRoleService.getSpecialAreaDeptMaster("招聘专员", 74878, "00001021", execution);
		System.out.println("assignee-->" + assignee);
	}
	
	@Test
//	@Ignore
	public void testGetSepicalAreaFloorMaster(){
		ExecutionEntity execution = new ExecutionEntity();
		execution.setProcessDefinitionId("Test-Handle:108:21423");
		String assignee = bpmConfProcessRoleService.getSpecialAreaFloorMaster("考勤专员", 1, "00001021", execution);
		System.out.println("assignee-->" + assignee);
	}
}
