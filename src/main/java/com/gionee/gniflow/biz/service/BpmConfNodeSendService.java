package com.gionee.gniflow.biz.service;

import java.util.List;

import com.gionee.gnif.dto.QueryMap;
import com.gionee.gnif.model.PageResult;
import com.gionee.gniflow.biz.model.BpmConfNodeSend;
import com.gionee.gniflow.dto.BpmConfNodeSendDto;

public interface BpmConfNodeSendService {

	void save(BpmConfNodeSend bpmConfNodeSend);

	BpmConfNodeSend getBpmConfNodeSend(Integer id);

	void delete(Integer id);

	List<BpmConfNodeSend> query(QueryMap critera);

	PageResult<BpmConfNodeSend> queryPage(QueryMap critera);
	
	List<BpmConfNodeSendDto> joinQueryBpmConfNodeSend(QueryMap critera);
}
