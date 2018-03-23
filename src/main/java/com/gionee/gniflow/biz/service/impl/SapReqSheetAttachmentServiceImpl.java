package com.gionee.gniflow.biz.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gionee.gnif.dto.QueryMap;
import com.gionee.gniflow.biz.model.SapReqSheetAttachment;
import com.gionee.gniflow.biz.service.SapReqSheetAttachmentService;
import com.gionee.gniflow.constant.WebReqConstant;
import com.gionee.gniflow.integration.dao.SapReqSheetAttachmentDao;

@Service
public class SapReqSheetAttachmentServiceImpl implements SapReqSheetAttachmentService {

    @Autowired
    private SapReqSheetAttachmentDao sapReqSheetAttachmentDao;

	@Override
	public void save(SapReqSheetAttachment sapReqSheetAttachment) {
		sapReqSheetAttachmentDao.insert(sapReqSheetAttachment);
	}

	@Override
	public SapReqSheetAttachment getSapReqSheetAttachment(String preqNo,
			Integer preqItem) {
		return sapReqSheetAttachmentDao.get(preqNo, preqItem);
	}

	@Override
	public List<SapReqSheetAttachment> getPageSapReqSheetAttachment(
			QueryMap querymap) {
		return sapReqSheetAttachmentDao.getPage(querymap.getMap());
	}

	@Override
	public int getPageCount(QueryMap querymap) {
		return sapReqSheetAttachmentDao.getPageCount(querymap.getMap());
	}

	@Override
	public List<SapReqSheetAttachment> getAllSapReqSheetAttachment(
			QueryMap querymap) {
		return sapReqSheetAttachmentDao.getAll(querymap.getMap());
	}

	@Override
	public void logicDelete(String preqNo, Integer preqItem,String fileNo) {
		sapReqSheetAttachmentDao.logicDelete(preqNo, preqItem, fileNo, WebReqConstant.DEL_YES);
	}

}
