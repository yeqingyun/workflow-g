package com.gionee.gniflow.integration.dao;

import com.gionee.gniflow.biz.model.BpmConfNodeSend;
import com.gionee.gniflow.dto.BpmConfNodeSendDto;

import java.util.List;
import java.util.Map;

public interface BpmConfNodeSendDao {

    int insert(BpmConfNodeSend bpmConfNodeSend);

    int update(BpmConfNodeSend bpmConfNodeSend);

    int delete(Integer id);

    List<BpmConfNodeSend> getAll(Map<String, Object> param);

    List<BpmConfNodeSend> getPage(Map<String, Object> param);
    
    int getPageCount(Map<String, Object> param);

    BpmConfNodeSend get(Integer id);

    List<BpmConfNodeSendDto> joinQueryBpmConfNodeSend(Map<String, Object> param);
}
