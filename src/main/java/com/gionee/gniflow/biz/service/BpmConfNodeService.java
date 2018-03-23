package com.gionee.gniflow.biz.service;

import java.util.List;

import com.gionee.gnif.dto.QueryMap;
import com.gionee.gnif.model.PageResult;
import com.gionee.gniflow.biz.model.BpmConfNode;
import com.gionee.gniflow.dto.BpmConfNodeDto;

public interface BpmConfNodeService {

	Integer save(BpmConfNode bpmConfNode);

	BpmConfNode getBpmConfNode(Integer id);

	void delete(Integer id);

	List<BpmConfNode> query(QueryMap critera);

	PageResult<BpmConfNode> queryPage(QueryMap critera);
	
	BpmConfNodeDto joinQueryBpmConfNode(QueryMap critera);
}
