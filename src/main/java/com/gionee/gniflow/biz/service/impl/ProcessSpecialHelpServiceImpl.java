/**
 * @(#) ProcessSpecialHelpServiceImpl.java Created on 2014年12月11日
 *
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.gionee.gniflow.biz.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gionee.gniflow.biz.service.ProcessHelpService;
import com.gionee.gniflow.biz.service.ProcessSpecialHelpService;
import com.gionee.gniflow.constant.BPMConstant;

/**
 * The class <code>ProcessSpecialHelpServiceImpl</code>
 *
 * @author lipw
 * @version 1.0
 */
@Service("processSpecialHelpService")
public class ProcessSpecialHelpServiceImpl implements ProcessSpecialHelpService {
	
	@Autowired
	private ProcessHelpService processHelpService;
	
	/* (non-Javadoc)
	 * @see com.gionee.gniflow.biz.service.ProcessSpecialHelpService#isLevelOneTransfer(java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public Boolean isLevelOneTransfer(String outOrgId, String inOrgId) {
		//调出部门的总监
		String outDirector = processHelpService.getOtherOrgLeader(outOrgId, BPMConstant.LEADER_TYPE_DIRECTOR);
		String inDirector = processHelpService.getOtherOrgLeader(inOrgId, BPMConstant.LEADER_TYPE_DIRECTOR);
		if (StringUtils.isNotEmpty(outDirector) && StringUtils.isNotEmpty(inDirector)
				&& outDirector.equals(inDirector)) {
			return true;
		}
		return false;
	}

}
