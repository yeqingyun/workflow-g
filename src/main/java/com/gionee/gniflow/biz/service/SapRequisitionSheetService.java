package com.gionee.gniflow.biz.service;

import java.util.List;

import com.gionee.gnif.dto.QueryMap;
import com.gionee.gniflow.biz.model.SapRequisitionSheet;

public interface SapRequisitionSheetService {

	void save(SapRequisitionSheet sapRequisitionSheet);

	SapRequisitionSheet getSapRequisitionSheet(String preqNo, Integer preqItem);
	
	SapRequisitionSheet getSapRequisitionSheet(String procInstId);
	
	List<SapRequisitionSheet> getPageSapRequisitionSheet(QueryMap querymap);
	
	Integer getPageCount(QueryMap querymap);
	
	List<SapRequisitionSheet> getAllSapRequisitionSheet(QueryMap querymap);

	void logicDelete(String preqNo, Integer preqItem);
	
	void update(SapRequisitionSheet sapRequisitionSheet);
	
	void updateSyncSapReqSheets(List<SapRequisitionSheet> sheets);
}
