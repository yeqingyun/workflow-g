package com.gionee.gniflow.web.listener.TeamActivitiesDetail;

import java.util.Date;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gionee.gniflow.biz.model.TeamActivitiesDetail;
import com.gionee.gniflow.biz.service.TeamActivitiesDetailService;
import com.gionee.gniflow.constant.BPMConstant;
import com.gionee.gniflow.web.util.DateUtil;
import com.gionee.gniflow.web.util.SpringTools;

public class UpdateTeamActivitiesDetailLister implements ExecutionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6040840716957154734L;
	private Logger logger = LoggerFactory.getLogger(UpdateTeamActivitiesDetailLister.class);

	@Override
	public void notify(DelegateExecution execution) throws Exception {
		logger.debug("Running in UpdateTeamActivitiesDetailLister begin!");
		TeamActivitiesDetailService sdService = (TeamActivitiesDetailService)SpringTools.getBean(TeamActivitiesDetailService.class);
		String processInstanceId=execution.getProcessInstanceId();
		sdService.delete(processInstanceId);
		
		TeamActivitiesDetailService sfService = (TeamActivitiesDetailService)SpringTools.getBean(TeamActivitiesDetailService.class);
		RuntimeService runtimeService = execution.getEngineServices().getRuntimeService();
		String participantList4Hidden = (String) runtimeService.getVariable(execution.getId(), "participantList4Hidden");
		String travelDate=(String) runtimeService.getVariable(execution.getId(), "timeTo");
		String[] participantList = participantList4Hidden.split(":");
		
		for(int i = 0;i<participantList.length;i++){
			if(!participantList[i].isEmpty()){
				TeamActivitiesDetail sf = new TeamActivitiesDetail();
				String temp = participantList[i].substring(1,participantList[i].length()-1);
				String[] participant = temp.split(",");
				sf.setCompany(participant[1]);
				sf.setDepartment(participant[2]);
				sf.setAccount(participant[3]);
				sf.setName(participant[4]);
				sf.setTravelDate(DateUtil.parse(travelDate,"yyyy-MM-dd"));
				//sf.setRemark(participant[6]);
				sf.setCreateBy((String) runtimeService.getVariable(execution.getId(), "userName"));
				sf.setCreateTime(new Date());
				//sf.setEntryDate(DateUtil.parse(participant[5],"yy/MM/dd"));
				sf.setProcInstId(execution.getProcessInstanceId());
				sf.setStatus(BPMConstant.STATUS_NORMAL);
//				sf.setUpdateBy(updateBy);
//				sf.setUpdateTime(updateTime);
				sfService.save(sf);
			}
		}
		logger.debug("Running in UpdateTeamActivitiesDetailLister end!");
	}

}
