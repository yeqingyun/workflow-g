package com.gionee.gniflow.integration.dao;

import com.gionee.gniflow.biz.model.SignFileInformation;

import java.util.List;
import java.util.Map;

public interface SignFileInformationDao {

    int insert(SignFileInformation signFileInformation);

    int update(SignFileInformation signFileInformation);

    List<SignFileInformation> getAll(Map<String, Object> param);

    List<SignFileInformation> getPage(Map<String, Object> param);
    
    int getPageCount(Map<String, Object> param);

    SignFileInformation get(Integer id);
    
    SignFileInformation getMaxFileCodeInfo(Map<String, Object> param);

}
