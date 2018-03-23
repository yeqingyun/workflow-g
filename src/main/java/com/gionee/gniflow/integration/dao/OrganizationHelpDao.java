package com.gionee.gniflow.integration.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.gionee.auth.model.Organization;

public interface OrganizationHelpDao {

    Organization queryRootNode(Integer orgId);
    
    Organization queryOrganizationByName(String orgName);

    Organization queryChildrenByRootNode(@Param(value="rootId")Integer rootId, @Param(value="orgName") String orgName);
    
    List<Organization> checkHasChild(String id);
}
