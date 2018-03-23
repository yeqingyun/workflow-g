package com.gionee.gniflow.integration.dao;

import com.gionee.gniflow.biz.model.SignOrgCode;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface SignOrgCodeDao {

    int insert(SignOrgCode signOrgCode);

    int update(SignOrgCode signOrgCode);

    int delete(Integer id);

    List<SignOrgCode> getAll(Map<String, Object> param);

    List<SignOrgCode> getPage(Map<String, Object> param);

    int getPageCount(Map<String, Object> param);

    SignOrgCode get(Integer id);
    
    SignOrgCode getOrgCode4OrgId(@Param("orgId") Integer orgId);

}
