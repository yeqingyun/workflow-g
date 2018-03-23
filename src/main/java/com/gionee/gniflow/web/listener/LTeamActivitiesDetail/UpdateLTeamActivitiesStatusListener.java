package com.gionee.gniflow.web.listener.LTeamActivitiesDetail;

import java.util.List;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.gionee.gniflow.biz.model.LTeamActivitiesDetail;
import com.gionee.gniflow.biz.service.LTeamActivitiesDetailService;
import com.gionee.gniflow.constant.WebReqConstant;
import com.gionee.gniflow.web.util.SpringTools;

public class UpdateLTeamActivitiesStatusListener implements ExecutionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1267535620187077556L;
	private Logger logger = LoggerFactory.getLogger(UpdateLTeamActivitiesDetailListener.class);

	@Override
	public void notify(DelegateExecution execution) throws Exception {
		logger.debug("Running in UpdateLTeamActivitiesStatusLister begin!");
		LTeamActivitiesDetailService lTeamActivitiesDetailService = (LTeamActivitiesDetailService)SpringTools.getBean(LTeamActivitiesDetailService.class);
		String processInstanceId = execution.getProcessInstanceId();
		List<LTeamActivitiesDetail> list = lTeamActivitiesDetailService.query(processInstanceId);
		
		if(!CollectionUtils.isEmpty(list)){
			for(int i=0;i<list.size();i++){
				LTeamActivitiesDetail sfInfo=list.get(i);
				sfInfo.setStatus(WebReqConstant.HAVE_PASSED);
				lTeamActivitiesDetailService.update(sfInfo);
			}
		}
		logger.debug("Running in UpdateLTeamActivitiesStatusLister end!");
		
	}

}
