package com.gionee.gniflow.biz.service;

import java.util.List;

import com.gionee.gnif.dto.QueryMap;
import com.gionee.gniflow.biz.model.SapReqSheetAttachment;

public interface SapReqSheetAttachmentService {

	void save(SapReqSheetAttachment sapReqSheetAttachment);

	SapReqSheetAttachment getSapReqSheetAttachment(String preqNo, Integer preqItem);
	
	List<SapReqSheetAttachment> getPageSapReqSheetAttachment(QueryMap querymap);
	
	int getPageCount(QueryMap querymap);
	
	List<SapReqSheetAttachment> getAllSapReqSheetAttachment(QueryMap querymap);

	void logicDelete(String preqNo, Integer preqItem, String fileNo);

}
