package com.gionee.gniflow.web.listener.TeamActivitiesDetail;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gionee.gniflow.biz.model.TeamActivitiesDetail;
import com.gionee.gniflow.biz.service.TeamActivitiesDetailService;
import com.gionee.gniflow.constant.WebReqConstant;
import com.gionee.gniflow.web.util.DateUtil;
import com.gionee.gniflow.web.util.SpringTools;

public class InsertTeamActivitiesDetailLister implements ExecutionListener{
	/**
	 * 插入参加活动人员名单
	 */
	private static final long serialVersionUID = 4358779360859450714L;
	private Logger logger = LoggerFactory.getLogger(InsertTeamActivitiesDetailLister.class);
	@Override
	public void notify(DelegateExecution execution) throws Exception {
		logger.debug("Running in InsetTeamActivitiesDetailLister begin!");
		TeamActivitiesDetailService sfService = (TeamActivitiesDetailService)SpringTools.getBean(TeamActivitiesDetailService.class);
		RuntimeService runtimeService = execution.getEngineServices().getRuntimeService();
		String participantList4Hidden = (String) runtimeService.getVariable(execution.getId(), "participantList4Hidden");
		String travelDate=(String)runtimeService.getVariable(execution.getId(), "timeTo");//旅游结束时间
		String[] participantList = participantList4Hidden.split(":");
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		for(int i = 0;i<participantList.length;i++){
			if(!participantList[i].isEmpty()){
				TeamActivitiesDetail sf = new TeamActivitiesDetail();
				String temp = participantList[i].substring(1,participantList[i].length()-1);
				String[] participant = temp.split(",");
				sf.setCompany(participant[1]);
				sf.setDepartment(participant[2]);
				sf.setAccount(participant[3]);
				sf.setName(participant[4]);
				//sf.setRemark(participant[5]);
				sf.setCreateBy((String) runtimeService.getVariable(execution.getId(), "userName"));
				sf.setCreateTime(new Date());
				sf.setProcInstId(execution.getProcessInstanceId());
				sf.setStatus(WebReqConstant.NOT_PASSED);
				sf.setTravelDate(sdf.parse(travelDate));
//				sf.setUpdateBy(updateBy);
//				sf.setUpdateTime(updateTime);
				sfService.save(sf);
			}
		}
		logger.debug("Running in InsetTeamActivitiesDetailLister end!");
	}
}
