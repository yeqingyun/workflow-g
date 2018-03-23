package com.gionee.gniflow.integration.dao;

import com.gionee.gniflow.biz.model.MLeaveApplicationInfo;

import java.util.List;
import java.util.Map;

public interface MLeaveApplicationInfoDao {

    int insert(MLeaveApplicationInfo mLeaveApplicationInfo);

    int update(MLeaveApplicationInfo mLeaveApplicationInfo);

    int delete(Integer id);

    List<MLeaveApplicationInfo> getAll(Map<String, Object> map);

    List<MLeaveApplicationInfo> getPage(Map<String, Object> map);
    
    int getPageCount(Map<String, Object> map);

    MLeaveApplicationInfo get(Integer id);
    
    void deleteObsoleteProcess(String proInstId);
}
