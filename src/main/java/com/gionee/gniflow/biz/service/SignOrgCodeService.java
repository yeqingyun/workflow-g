package com.gionee.gniflow.biz.service;

import com.gionee.gniflow.biz.model.SignOrgCode;

public interface SignOrgCodeService {

	void save(SignOrgCode signOrgCode);

	SignOrgCode getSignOrgCode(Integer id);

	void delete(Integer id);
	
	SignOrgCode getSignOrgCode4OrgId(Integer orgId);

}
