package com.gionee;

import java.util.List;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gionee.gnif.dto.QueryMap;
import com.gionee.gniflow.biz.model.SapRequisitionSheet;
import com.gionee.gniflow.biz.service.SapFlowService;
import com.gionee.gniflow.biz.service.SapRequisitionSheetService;
import com.gionee.gniflow.constant.WebReqConstant;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:biz-context.xml"})
public class SapServiceTest {

	@Autowired
	private SapFlowService sapFlowService;
	
	@Autowired
	private SapRequisitionSheetService sapRequisitionSheetService;
	
	@Test
	@Ignore
	public void testGetSapAccountRelation() {
		String hrAccount = sapFlowService.getHrAccount4SapAccount("005");
		Assert.assertNotNull(hrAccount);
		Assert.assertEquals("00001111", hrAccount);
	}
	
	@Test
	@Ignore
	public void testGetSapApprovalRelation() {
		String hrAccount = sapFlowService.getHrAccount4SapApprovalCode("05");
		Assert.assertNotNull(hrAccount);
		Assert.assertEquals("00001234", hrAccount);
	}
	
	@Test
	@Ignore
	public void testGetProjectLeader(){
		String preqNo = "1200000061";
		Integer preqItem = 10;
		String leaderHrAccount = sapFlowService.getProjectLeader(preqNo, preqItem);
		System.out.println("leaderHrAccount-->" + leaderHrAccount);
		Assert.assertEquals("00001234", leaderHrAccount);
	}
	
	@Test
	@Ignore
	public void testGetGeneralManager(){
		String preqNo = "1200000061";
		Integer preqItem = 10;
		String generalManager = sapFlowService.getGeneralManager(preqNo, preqItem);
		System.out.println("generalManager-->" + generalManager);
		Assert.assertEquals("00001111", generalManager);
	}
	
	@Test
	@Ignore
	public void testSapReqSheetQuery(){
		QueryMap queryMap = new QueryMap();
		queryMap.put("status", WebReqConstant.PROCURE_SHEET_TOBEAUDI);
		queryMap.put("delflag", WebReqConstant.DEL_NO);
		
		Integer total = sapRequisitionSheetService.getPageCount(queryMap);
		
		Assert.assertEquals(19, total.intValue());
	}
	
	@Test
	@Ignore
	public void testSapReqSheet4HisProcessQuery(){
		QueryMap queryMap = new QueryMap();
		queryMap.put("curUserAccount", "ali");
		queryMap.put("status", "");
		queryMap.put("delflag", WebReqConstant.DEL_NO);
		queryMap.put("firstRow", 1);
		queryMap.put("lastRow", 20);
		queryMap.put("procDefId", "purchase-requisition");
		
		List<SapRequisitionSheet> list = sapFlowService.queryPageSapRequisitionSheet4HisProcess(queryMap);
		
		Integer total = sapFlowService.countSapRequisitionSheet4HisProcess(queryMap);
		
		System.out.println(list.size());
		
		System.out.println(total);
	}
	
	@Test
	@Ignore
	public void testGetSapApprovalCode(){
		
		String approvalCode = sapFlowService.getSapApprovalCode4HrAccount("00000065");
		
		Assert.assertEquals("05", approvalCode);
	}
	
}
