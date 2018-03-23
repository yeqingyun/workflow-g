package com.gionee.gniflow.biz.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gionee.gnif.dto.QueryMap;
import com.gionee.gnif.model.PageResult;
import com.gionee.gniflow.biz.service.ReportRoleService;
import com.gionee.gniflow.biz.model.ReportRole;
import com.gionee.gniflow.biz.model.ReportRoleProcdef;
import com.gionee.gniflow.integration.dao.ReportRoleDao;
import com.gionee.gniflow.integration.dao.ReportRoleProcdefDao;
import com.gionee.gniflow.integration.dao.BpmUserRoleDao;
import com.gionee.gniflow.integration.dao.ReportUserRoleDao;

@Service
public class ReportRoleServiceImpl implements ReportRoleService {

    @Autowired
    private ReportRoleDao reportRoleDao;
    
    @Autowired
    private ReportRoleProcdefDao reportRoleProcdefDao;
    
    @Autowired
    private ReportUserRoleDao reportUserRoleDao;

	@Override
	public void save(ReportRole reportRole, String roleProDefIds) {
		if (reportRole.getId() == null) {
			reportRoleDao.insert(reportRole);
			
			//插入REPORT_ROLE_PROCDEF表
			if (StringUtils.isNotEmpty(roleProDefIds)) {
				String[] idsArr = roleProDefIds.split(",");
				ReportRoleProcdef reportRoleProcdef = null;
				for (String tempId : idsArr) {
					reportRoleProcdef = new ReportRoleProcdef(reportRole.getId(), tempId);
					reportRoleProcdefDao.insert(reportRoleProcdef);
				}
			}
		} else {
			reportRoleDao.update(reportRole);
			//删除后在插入
			reportRoleProcdefDao.delete(reportRole.getId());
			
			//插入BPM_ROLE_PROCDEF表
			if (StringUtils.isNotEmpty(roleProDefIds)) {
				String[] idsArr = roleProDefIds.split(",");
				ReportRoleProcdef reportRoleProcdef = null;
				for (String tempId : idsArr) {
					reportRoleProcdef = new ReportRoleProcdef(reportRole.getId(), tempId);
					reportRoleProcdefDao.insert(reportRoleProcdef);
				}
			}
		}
	}

	@Override
	public ReportRole getReportRole(Integer id) {
		return reportRoleDao.get(id);
	}

	@Override
	public void delete(Integer id) {
		reportRoleDao.delete(id);
		//删除BPM_ROLE_PROCDEFKEY表数据
		reportRoleProcdefDao.delete(id);
		//删除BPM_USER_ROLE表数据
		reportUserRoleDao.deleteByRoleId(id);
	}

	@Override
	public List<ReportRole> query(QueryMap critera) {
		return reportRoleDao.getAll(critera.getMap());
	}

	@Override
	public PageResult<ReportRole> queryPage(QueryMap critera) {
		PageResult<ReportRole> result = new PageResult<ReportRole>();
		result.setRows(reportRoleDao.getPage(critera.getMap()));
		result.setTotal(reportRoleDao.getPageCount(critera.getMap()));
		return result;
	}

	@Override
	public List<ReportRole> getReportRoles(Integer id) {
		return reportRoleDao.getUserRoles(id);
	}

}
