package com.gionee.gniflow.biz.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gionee.gnif.dto.QueryMap;
import com.gionee.gniflow.biz.model.SapAccountRelation;
import com.gionee.gniflow.biz.model.SapApprovalRelation;
import com.gionee.gniflow.biz.model.SapRequisitionSheet;
import com.gionee.gniflow.biz.service.SapFlowService;
import com.gionee.gniflow.dto.SapRequisitionTaskDto;
import com.gionee.gniflow.integration.dao.SapAccountRelationDao;
import com.gionee.gniflow.integration.dao.SapApprovalRelationDao;
import com.gionee.gniflow.integration.dao.SapRequisitionSheetDao;

@Service(value="sapFlowService")
public class SapFlowServiceImpl implements SapFlowService {
	
	@Autowired
	private SapRequisitionSheetDao sapRequisitionSheetDao;

    @Autowired
    private SapAccountRelationDao sapAccountRelationDao;
    
    @Autowired
    private SapApprovalRelationDao sapApprovalRelationDao;

	@Override
	public String getHrAccount4SapAccount(String sapAccount) {
		SapAccountRelation sapAccountRelation = sapAccountRelationDao.get(sapAccount);
		if (sapAccountRelation != null) {
			return sapAccountRelation.getHrAccount();
		}
		return null;
	}

	@Override
	public String getHrAccount4SapApprovalCode(String approvalCode) {
		SapApprovalRelation sapApprovalRelation = sapApprovalRelationDao.get(approvalCode);
		if (sapApprovalRelation != null) {
			return sapApprovalRelation.getHrAccount();
		}
		return null;
	}

	@Override
	public String getProjectLeader(String preqNo, Integer preqItem) {
		SapRequisitionSheet sapRequisitionSheet = sapRequisitionSheetDao.get(preqNo, preqItem);
		if (sapRequisitionSheet != null) {
			return this.getHrAccount4SapApprovalCode(sapRequisitionSheet.getFrgc1());
		}
		return null;
	}

	@Override
	public String getGeneralManager(String preqNo, Integer preqItem) {
		SapRequisitionSheet sapRequisitionSheet = sapRequisitionSheetDao.get(preqNo, preqItem);
		if (sapRequisitionSheet != null) {
			return this.getHrAccount4SapApprovalCode(sapRequisitionSheet.getFrgc2());
		}
		return null;
	}

	@Override
	public String getSapAccount4CurUserAccount(String hrAccount) {
		SapAccountRelation sapAccountRelation = sapAccountRelationDao.find(hrAccount);
		if (sapAccountRelation != null) {
			return sapAccountRelation.getSapAccount();
		}
		return null;
	}

	@Override
	public List<SapRequisitionSheet> queryPageSapRequisitionSheet4HisProcess(
			QueryMap queryMap) {
		return sapRequisitionSheetDao.queryPageSapReqSheet4HisProcess(queryMap.getMap());
	}

	@Override
	public Integer countSapRequisitionSheet4HisProcess(QueryMap queryMap) {
		return sapRequisitionSheetDao.countSapReqSheet4HisProcess(queryMap.getMap());
	}

	@Override
	public String getSapApprovalCode4HrAccount(String hrAccount) {
		return sapApprovalRelationDao.getApprovalCode(hrAccount);
	}

	@Override
	public List<SapRequisitionTaskDto> queryPageSapRequisitionSheet4UserTask(
			QueryMap queryMap) {
		return sapRequisitionSheetDao.queryPageSapRequisitionSheet4UserTask(queryMap.getMap());
	}

	@Override
	public Integer countSapRequisitionSheet4UserTask(QueryMap queryMap) {
		return sapRequisitionSheetDao.countSapRequisitionSheet4UserTask(queryMap.getMap());
	}

	@Override
	public List<SapRequisitionSheet> querySapReqSheet4ProcurementInquiry(
			QueryMap queryMap) {
		return sapRequisitionSheetDao.querySapReqSheet4ProcurementInquiry(queryMap.getMap());
	}

	@Override
	public Integer countSapReqSheet4ProcurementInquiry(QueryMap queryMap) {
		return sapRequisitionSheetDao.countSapReqSheet4ProcurementInquiry(queryMap.getMap());
	}


}
