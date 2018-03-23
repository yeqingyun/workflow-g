package com.gionee.gniflow.biz.service;

import java.util.List;

import com.gionee.gnif.dto.QueryMap;
import com.gionee.gnif.model.PageResult;
import com.gionee.gniflow.biz.model.MLeaveApplicationInfo;

public interface MLeaveApplicationInfoService {

	void save(MLeaveApplicationInfo mLeaveApplicationInfo);

	MLeaveApplicationInfo getMLeaveApplicationInfo(Integer id);

	List<MLeaveApplicationInfo> query(QueryMap critera);

	PageResult<MLeaveApplicationInfo> queryPage(QueryMap critera);

	void delete(String proInstId);
	/**
	 * 删除已废止的流程
	 * @param status
	 * @param processInstId
	 */
	void deleteObsoleteProcess(String proInstId);
}
