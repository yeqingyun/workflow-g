package com.gionee.gniflow.biz.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.gionee.auth.dao.UserDao;
import com.gionee.auth.model.User;
import com.gionee.gnif.GnifException;
import com.gionee.gnif.dto.QueryMap;
import com.gionee.gnif.model.PageResult;
import com.gionee.gniflow.biz.service.BpmUserRoleService;
import com.gionee.gniflow.biz.model.BpmRole;
import com.gionee.gniflow.biz.model.BpmUserRole;
import com.gionee.gniflow.integration.dao.BpmRoleDao;
import com.gionee.gniflow.integration.dao.BpmUserRoleDao;
import com.gionee.gniflow.web.response.BpmUserRoleResponse;

@Service
public class BpmUserRoleServiceImpl implements BpmUserRoleService {

    @Autowired
    private BpmUserRoleDao bpmUserRoleDao;

    @Autowired
    private UserDao userDao;
    
    @Autowired
    private BpmRoleDao bpmRoleDao;
    
	@Override
	public void save(BpmUserRole bpmUserRole) {
		bpmUserRoleDao.insert(bpmUserRole);
	}

	@Override
	public void delete(QueryMap critera) {
		bpmUserRoleDao.delete(critera.getMap());
	}

	@Override
	public List<BpmUserRole> query(QueryMap critera) {
		return bpmUserRoleDao.getAll(critera.getMap());
	}

	@Override
	public List<String> getUserProcessDefKey(Integer userId) {
		return bpmUserRoleDao.getUserProcessDefKey(userId);
	}

	@Override
	public void handleBpmUserRole(List<Map<String,String>> lists) throws Exception{
		
		int index = 2;
		for (Map<String,String> element : lists) {
			//根据用户账户查出用户的ID
			User user = userDao.getByAccount(element.get("0"));
			//根据角色名称查出角色的ID
			BpmRole bpmRole = bpmRoleDao.getByRoleName(element.get("1"));
			
			if (user == null) {
				throw new GnifException("第"+ index +"行["+ element.get("0") +"]用户不存在，请检查导入文件中填写的数据是否正确！");
			}
			
			if (bpmRole == null) {
				throw new GnifException("第"+ index +"行["+ element.get("1") +"]角色不存在，请检查导入文件中填写的数据是否正确！");
			}
			
			BpmUserRole userRole = null;
			List<BpmUserRole> result = null;
			Map<String, Object> param = new HashMap<String,Object>();
			if (user != null && bpmRole != null) {
				userRole = new BpmUserRole(user.getId(), bpmRole.getId());
				param.clear();
				param.put("userId", user.getId());
				param.put("roleId", bpmRole.getId());
				result = bpmUserRoleDao.getAll(param);
				if (CollectionUtils.isEmpty(result)) {
					//验证是否已经在数据库存在，存在则不插入
					bpmUserRoleDao.insert(userRole);
				}
			}
		}
		index++;
	}

	@Override
	public PageResult<BpmUserRoleResponse> queryPage(QueryMap critera) {
		// TODO Auto-generated method stub
		PageResult<BpmUserRoleResponse> result = new PageResult<BpmUserRoleResponse>();
		result.setRows(bpmUserRoleDao.getPage(critera.getMap()));
		result.setTotal(bpmUserRoleDao.getPageCount(critera.getMap()));
		return result;
	}

}
