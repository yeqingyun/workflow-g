package com.gionee.gniflow.biz.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gionee.gnif.dto.QueryMap;
import com.gionee.gnif.model.PageResult;
import com.gionee.gniflow.biz.service.BpmRoleService;
import com.gionee.gniflow.biz.model.BpmRole;
import com.gionee.gniflow.biz.model.BpmRoleProcdef;
import com.gionee.gniflow.integration.dao.BpmRoleDao;
import com.gionee.gniflow.integration.dao.BpmRoleProcdefDao;
import com.gionee.gniflow.integration.dao.BpmUserRoleDao;

@Service
public class BpmRoleServiceImpl implements BpmRoleService {

    @Autowired
    private BpmRoleDao bpmRoleDao;
    
    @Autowired
    private BpmRoleProcdefDao bpmRoleProcdefDao;
    
    @Autowired
    private BpmUserRoleDao bpmUserRoleDao;

	@Override
	public void save(BpmRole bpmRole, String roleProDefIds) {
		if (bpmRole.getId() == null) {
			bpmRoleDao.insert(bpmRole);
			
			//插入BPM_ROLE_PROCDEF表
			if (StringUtils.isNotEmpty(roleProDefIds)) {
				String[] idsArr = roleProDefIds.split(",");
				BpmRoleProcdef bpmRoleProcdef = null;
				for (String tempId : idsArr) {
					bpmRoleProcdef = new BpmRoleProcdef(bpmRole.getId(), tempId);
					bpmRoleProcdefDao.insert(bpmRoleProcdef);
				}
			}
		} else {
			bpmRoleDao.update(bpmRole);
			//删除后在插入
			bpmRoleProcdefDao.delete(bpmRole.getId());
			
			//插入BPM_ROLE_PROCDEF表
			if (StringUtils.isNotEmpty(roleProDefIds)) {
				String[] idsArr = roleProDefIds.split(",");
				BpmRoleProcdef bpmRoleProcdef = null;
				for (String tempId : idsArr) {
					bpmRoleProcdef = new BpmRoleProcdef(bpmRole.getId(), tempId);
					bpmRoleProcdefDao.insert(bpmRoleProcdef);
				}
			}
		}
	}

	@Override
	public BpmRole getBpmRole(Integer id) {
		return bpmRoleDao.get(id);
	}

	@Override
	public void delete(Integer id) {
		bpmRoleDao.delete(id);
		//删除BPM_ROLE_PROCDEFKEY表数据
		bpmRoleProcdefDao.delete(id);
		//删除BPM_USER_ROLE表数据
		bpmUserRoleDao.deleteByRoleId(id);
	}

	@Override
	public List<BpmRole> query(QueryMap critera) {
		return bpmRoleDao.getAll(critera.getMap());
	}

	@Override
	public PageResult<BpmRole> queryPage(QueryMap critera) {
		PageResult<BpmRole> result = new PageResult<BpmRole>();
		result.setRows(bpmRoleDao.getPage(critera.getMap()));
		result.setTotal(bpmRoleDao.getPageCount(critera.getMap()));
		return result;
	}

	@Override
	public List<BpmRole> getBpmRoles(Integer id) {
		return bpmRoleDao.getUserRoles(id);
	}

	@Override
	public List<BpmRole> getReportRoles(Integer id) {
		return bpmRoleDao.getReportUserRoles(id);
	}

}
