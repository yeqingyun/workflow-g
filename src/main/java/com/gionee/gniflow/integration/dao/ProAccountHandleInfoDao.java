package com.gionee.gniflow.integration.dao;

import com.gionee.gniflow.biz.model.ProAccountHandleInfo;

import java.util.List;
import java.util.Map;

public interface ProAccountHandleInfoDao {

    int insert(ProAccountHandleInfo proAccountHandleInfo);

    int update(ProAccountHandleInfo proAccountHandleInfo);

    int delete(Integer id);

    List<ProAccountHandleInfo> getAll(Map<String, Object> param);

    List<ProAccountHandleInfo> getPage(Map<String, Object> param);
    int getPageCount(Map<String, Object> param);

    ProAccountHandleInfo get(Integer id);

}
