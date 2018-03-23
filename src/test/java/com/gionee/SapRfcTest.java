package com.gionee;

import java.util.List;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gionee.gniflow.biz.model.SapRequisitionSheet;
import com.gionee.gniflow.biz.service.SapRequisitionSheetService;
import com.gionee.gniflow.constant.WebReqConstant;
import com.gionee.gniflow.sap.rfc.RfcSapReqRelease;
import com.gionee.gniflow.sap.rfc.RfcSapReqRestRelease;
import com.gionee.gniflow.sap.rfc.RfcSapReqSheet;
import com.gionee.gniflow.sap.rfc.RfcSyncSapReqSheet;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:biz-context.xml"})
public class SapRfcTest {
	
	@Autowired
	private SapRequisitionSheetService sapRequisitionSheetService;

	@Test
	@Ignore
	public void testRfcSapReqRelease() {
		
		RfcSapReqRelease release = new RfcSapReqRelease();
		
		Map<String,String> sapResult = release.sapReqSheetAudi("1200000201", "00010", "05");
		
		if (sapResult == null) {
			System.out.println("Success!");
		} else {
			for (Map.Entry<String, String> entry : sapResult.entrySet()) {
				System.out.println(entry.getKey() + "--" + entry.getValue());
			}
			
		}
		
	}
	
	@Test
	@Ignore
	public void testRfcSapReqRestRelease() {
		
		RfcSapReqRestRelease release = new RfcSapReqRestRelease();
		
		Map<String,String> sapResult = release.sapReqSheetResetAudi("1200000201", "00010", "05");
		if (sapResult == null) {
			System.out.println("Success!");
		} else {
			for (Map.Entry<String, String> entry : sapResult.entrySet()) {
				System.out.println(entry.getKey() + "--" + entry.getValue());
			}
			
		}
	}
	
	@Test
	@Ignore
	public void testGetSapReqSheet() {
		RfcSapReqSheet sapReqSheet = new RfcSapReqSheet();
		List<SapRequisitionSheet>  saqReqListOne = sapReqSheet.getSapReqSheets4ReqDate();
		List<SapRequisitionSheet>  saqReqListTwo = sapReqSheet.getSapReqSheets4ChangeDate();
		
		//合并两个List去掉preqNo，preqItem重复的数据
		List<SapRequisitionSheet> resultList = sapReqSheet.removeDuplicateItem(saqReqListOne, saqReqListTwo);
		
		//保存数据
		SapRequisitionSheet curSapReqSheet = null;
		for (SapRequisitionSheet saoReqSheet : resultList) {
			//判断当前库中的单据是否存在状态为待修改的数据，如果存在更新状态为待审核，并将流程实例置空
			curSapReqSheet = sapRequisitionSheetService.getSapRequisitionSheet(saoReqSheet.getPreqNo(), saoReqSheet.getPreqItem());
			if (curSapReqSheet == null) {
				sapRequisitionSheetService.save(saoReqSheet);
			} else if (curSapReqSheet.getStatus() == WebReqConstant.PROCURE_SHEET_TOBEMODEFY) {
				saoReqSheet.setStatus(WebReqConstant.PROCURE_SHEET_TOBEAUDI);
				saoReqSheet.setProcInstId(null);
				
				sapRequisitionSheetService.update(saoReqSheet);
			} else {
				sapRequisitionSheetService.update(saoReqSheet);
			}
		}
		
	}
	
	@Test
	//@Ignore
	public void testSyncSapReqSheet() {
		RfcSyncSapReqSheet syncSheet = new RfcSyncSapReqSheet();
		
		List<SapRequisitionSheet> sheets = syncSheet.getSyncSapReqSheets("1200000225", 10);
		//保存数据
		sapRequisitionSheetService.updateSyncSapReqSheets(sheets);
		
	}
	
}
