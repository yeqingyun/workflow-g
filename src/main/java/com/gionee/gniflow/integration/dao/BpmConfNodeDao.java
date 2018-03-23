package com.gionee.gniflow.integration.dao;

import com.gionee.gniflow.biz.model.BpmConfNode;
import com.gionee.gniflow.dto.BpmConfNodeDto;

import java.util.List;
import java.util.Map;

public interface BpmConfNodeDao {

    int insert(BpmConfNode bpmConfNode);

    int delete(Integer id);

    int update(BpmConfNode bpmConfNode);

    List<BpmConfNode> getAll(Map<String, Object> param);

    List<BpmConfNode> getPage(Map<String, Object> param);
    
    int getPageCount(Map<String, Object> param);

    BpmConfNode get(Integer id);
    
    BpmConfNodeDto joinQueryBpmConfNode(Map<String, Object> param);
}
