package com.gionee.gniflow.web.listener.TeamActivitiesDetail;

import java.util.List;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.gionee.gnif.dto.QueryMap;
import com.gionee.gniflow.biz.model.TeamActivitiesDetail;
import com.gionee.gniflow.biz.service.TeamActivitiesDetailService;
import com.gionee.gniflow.constant.WebReqConstant;
import com.gionee.gniflow.web.util.SpringTools;

public class UpdateTeamActivitiesStatusLister implements ExecutionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1267535620187077556L;
	private Logger logger = LoggerFactory.getLogger(UpdateTeamActivitiesDetailLister.class);

	@Override
	public void notify(DelegateExecution execution) throws Exception {
		logger.debug("Running in UpdateTeamActivitiesStatusLister begin!");
		TeamActivitiesDetailService sfService=(TeamActivitiesDetailService)SpringTools.getBean(TeamActivitiesDetailService.class);
		List<TeamActivitiesDetail> list = null;
		String processInstanceId = execution.getProcessInstanceId();
		QueryMap queryMap= new QueryMap();
		queryMap.put("procInstId", processInstanceId);
		
		list = sfService.query(queryMap);
		
		if(!CollectionUtils.isEmpty(list)){
			for(int i=0;i<list.size();i++){
				TeamActivitiesDetail sfInfo=list.get(i);
				sfInfo.setStatus(WebReqConstant.HAVE_PASSED);
				sfService.save(sfInfo);
			}
		}
		logger.debug("Running in UpdateTeamActivitiesStatusLister end!");
		
	}

}
