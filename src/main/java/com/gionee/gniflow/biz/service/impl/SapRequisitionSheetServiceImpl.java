package com.gionee.gniflow.biz.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gionee.gnif.dto.QueryMap;
import com.gionee.gniflow.biz.model.SapRequisitionSheet;
import com.gionee.gniflow.biz.service.SapRequisitionSheetService;
import com.gionee.gniflow.integration.dao.SapRequisitionSheetDao;

@Service
public class SapRequisitionSheetServiceImpl implements
		SapRequisitionSheetService {
	
	@Autowired
	private SapRequisitionSheetDao sapRequisitionSheetDao;
	
	@Override
	public void save(SapRequisitionSheet sapRequisitionSheet) {
		sapRequisitionSheetDao.insert(sapRequisitionSheet);
	}

	@Override
	public SapRequisitionSheet getSapRequisitionSheet(String preqNo,
			Integer preqItem) {
		return sapRequisitionSheetDao.get(preqNo, preqItem);
	}

	@Override
	public List<SapRequisitionSheet> getPageSapRequisitionSheet(
			QueryMap querymap) {
		return sapRequisitionSheetDao.getPage(querymap.getMap());
	}

	@Override
	public Integer getPageCount(QueryMap querymap) {
		return sapRequisitionSheetDao.getPageCount(querymap.getMap());
	}

	@Override
	public List<SapRequisitionSheet> getAllSapRequisitionSheet(QueryMap querymap) {
		return sapRequisitionSheetDao.getAll(querymap.getMap());
	}

	@Override
	public void logicDelete(String preqNo, Integer preqItem) {
		sapRequisitionSheetDao.logicDelete(preqNo, preqItem);
	}

	@Override
	public void update(SapRequisitionSheet sapRequisitionSheet) {
		sapRequisitionSheetDao.update(sapRequisitionSheet);
	}

	@Override
	public SapRequisitionSheet getSapRequisitionSheet(String procInstId) {
		return sapRequisitionSheetDao.queryByProcInstId(procInstId);
	}

	@Override
	public void updateSyncSapReqSheets(List<SapRequisitionSheet> sheets) {
		for (SapRequisitionSheet sheet : sheets) {
			sapRequisitionSheetDao.update(sheet);
		}
	}

}
