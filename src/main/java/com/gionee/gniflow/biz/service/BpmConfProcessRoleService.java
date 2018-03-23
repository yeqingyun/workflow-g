package com.gionee.gniflow.biz.service;

import java.util.List;

import org.activiti.engine.delegate.DelegateExecution;

import com.gionee.gnif.dto.QueryMap;
import com.gionee.gnif.model.PageResult;
import com.gionee.gniflow.biz.model.BpmConfProcessRole;

public interface BpmConfProcessRoleService {

	void save(BpmConfProcessRole bpmConfProcessRole);
	
	void update(BpmConfProcessRole bpmConfProcessRole);

	BpmConfProcessRole getBpmConfProcessRole(Integer id);

	void delete(Integer id);

	List<BpmConfProcessRole> query(QueryMap critera);

	PageResult<BpmConfProcessRole> queryPage(QueryMap critera);
	
	BpmConfProcessRole querySingle(QueryMap critera);
	
	/**
	 * 根据角色获取特殊用户
	 * @param roleName
	 * @return
	 */
	String getSpecialRoleMaster(String roleName, DelegateExecution execution);
	/**
	 * 根据角色，账户获取特定区域特殊用户
	 * @param roleName
	 * @param account
	 * @return
	 */
	String getSpecialAreaRoleMaster(String roleName, String account, DelegateExecution execution);
	/**
	 * 根据角色名称，部门Id获取特殊账户
	 * @param roleName
	 * @param orgId
	 * @return
	 */
	String getSpecialDeptMaster(String roleName, Integer orgId, DelegateExecution execution);
	/**
	 * 根据角色，部门ID，账户获取特定区域的特殊账户
	 * @param roleName
	 * @param orgId
	 * @param account
	 * @return
	 */
	String getSpecialAreaDeptMaster(String roleName, Integer orgId, String account, DelegateExecution execution);
	/**
	 * 根据角色，部门ID，账户获取特定区域的特定楼层的账户
	 * @param roleName
	 * @param orgId
	 * @param account
	 * @return
	 */
	String getSpecialAreaFloorMaster(String roleName, Integer floor, String account, DelegateExecution execution);
}
