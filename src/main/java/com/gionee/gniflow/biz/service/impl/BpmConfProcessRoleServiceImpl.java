package com.gionee.gniflow.biz.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.delegate.DelegateExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gionee.gnif.dto.QueryMap;
import com.gionee.gnif.model.PageResult;
import com.gionee.gniflow.biz.service.BpmConfProcessRoleService;
import com.gionee.gniflow.biz.model.BpmConfProcessRole;
import com.gionee.gniflow.constant.BPMConstant;
import com.gionee.gniflow.constant.WebReqConstant;
import com.gionee.gniflow.integration.dao.BpmConfProcessRoleDao;
import com.gionee.gniflow.web.util.HttpClientUtil;
import com.gionee.gniflow.web.util.PropertyHolder;

@Service("bpmConfProcessRoleService")
public class BpmConfProcessRoleServiceImpl implements BpmConfProcessRoleService {
	
	private static final Logger logger = LoggerFactory.getLogger(BpmConfProcessRoleServiceImpl.class);

    @Autowired
    private BpmConfProcessRoleDao bpmConfProcessRoleDao;

	@Override
	public void save(BpmConfProcessRole bpmConfProcessRole) {
		bpmConfProcessRoleDao.insert(bpmConfProcessRole);
	}
	
	@Override
	public void update(BpmConfProcessRole bpmConfProcessRole) {
		bpmConfProcessRoleDao.update(bpmConfProcessRole);
	}

	@Override
	public BpmConfProcessRole getBpmConfProcessRole(Integer id) {
		return bpmConfProcessRoleDao.get(id);
	}

	@Override
	public void delete(Integer id) {
		bpmConfProcessRoleDao.delete(id);
	}

	@Override
	public List<BpmConfProcessRole> query(QueryMap critera) {
		return bpmConfProcessRoleDao.getAll(critera.getMap());
	}

	@Override
	public PageResult<BpmConfProcessRole> queryPage(QueryMap critera) {
		PageResult<BpmConfProcessRole> result = new PageResult<BpmConfProcessRole>();
		result.setRows(bpmConfProcessRoleDao.getPage(critera.getMap()));
		result.setTotal(bpmConfProcessRoleDao.getPageCount(critera.getMap()));
		return result;
	}

	@Override
	public String getSpecialRoleMaster(String roleName, DelegateExecution execution) {
		String proDefKey = getProcessDefKey(execution.getProcessDefinitionId());
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("processDefKey", proDefKey);
		param.put("roleName", roleName);
		param.put("method", "getSpecialRoleMaster");
		BpmConfProcessRole confProRole = bpmConfProcessRoleDao.getSingleBpmConfProcessRole(param);
		if (confProRole != null) {
			return confProRole.getAssignee();
		}
		
		return null;
	}

	@Override
	public String getSpecialAreaRoleMaster(String roleName, String account, DelegateExecution execution) {
		try {
	        String area = getArea(account);
			
			String proDefKey = getProcessDefKey(execution.getProcessDefinitionId());
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("processDefKey", proDefKey);
			param.put("roleName", roleName);
			param.put("area", area);
			param.put("method", "getSpecialAreaRoleMaster");
			
			BpmConfProcessRole confProRole = bpmConfProcessRoleDao.getSingleBpmConfProcessRole(param);
			if (confProRole != null) {
				return confProRole.getAssignee();
			}
		} catch (Exception e) {
			logger.debug(e.getMessage());
		}
		return null;
	}

	@Override
	public String getSpecialDeptMaster(String roleName, Integer orgId,
			DelegateExecution execution) {
		String proDefKey = getProcessDefKey(execution.getProcessDefinitionId());
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("processDefKey", proDefKey);
		param.put("roleName", roleName);
		param.put("details", String.valueOf(orgId));
		param.put("method", "getSpecialDeptMaster");
		
		BpmConfProcessRole confProRole = bpmConfProcessRoleDao.getSingleBpmConfProcessRole(param);
		if (confProRole != null) {
			return confProRole.getAssignee();
		}
		
		return null;
	}

	@Override
	public String getSpecialAreaDeptMaster(String roleName, Integer orgId,
			String account, DelegateExecution execution) {
		
		String area = getArea(account);
		
		String proDefKey = getProcessDefKey(execution.getProcessDefinitionId());
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("processDefKey", proDefKey);
		param.put("roleName", roleName);
		param.put("area", area);
		param.put("details", String.valueOf(orgId));
		param.put("method", "getSpecialAreaDeptMaster");
		
		BpmConfProcessRole confProRole = bpmConfProcessRoleDao.getSingleBpmConfProcessRole(param);
		if (confProRole != null) {
			return confProRole.getAssignee();
		}
		
		return null;
	}
	

	@Override
	public String getSpecialAreaFloorMaster(String roleName, Integer floor,
			String account, DelegateExecution execution) {
		String area = getArea(account);
		
		String proDefKey = getProcessDefKey(execution.getProcessDefinitionId());
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("processDefKey", proDefKey);
		param.put("roleName", roleName);
		param.put("area", area);
		param.put("details", String.valueOf(floor));
		param.put("method", "getSpecialAreaFloorMaster");
		
		BpmConfProcessRole confProRole = bpmConfProcessRoleDao.getSingleBpmConfProcessRole(param);
		if (confProRole != null) {
			return confProRole.getAssignee();
		}
		
		return null;
	}
	
	/**
	 * 获取流程定义的Key
	 * @param processDefId
	 * @return
	 */
	private String getProcessDefKey(String processDefId){
		return processDefId.split(":")[0];
	}
	/**
	 * 根据账号获取区域
	 * @param account
	 * @return
	 */
	private String getArea(String account){
		//调用OA的接口判断 是深圳，北京，还是海外
		String area = null;
		try {
			String url = PropertyHolder.getContextProperty(WebReqConstant.HR_EMP_EMPINFO_KEY);
			Map<String,String> reqData = new HashMap<String,String>();
			reqData.put("emp.empId", account);//必须
			
			String result = HttpClientUtil.sendPostRequest(url, reqData,"UTF-8");
			
			JSONObject jsonObj = JSONArray.parseObject(result);
			//人事范围
			String personRange = jsonObj.getString("scoNm");
			//人事子范围
	        Integer personSubRange = jsonObj.getInteger("scoChrNo");
	        if (personSubRange == 1000) {
	        	area = BPMConstant.HR_PERSONAL_SUB_RANGE[0];
	        } else if (personSubRange == 2000){
	        	area = BPMConstant.HR_PERSONAL_SUB_RANGE[1];
	        } else if (personSubRange == 3000) {
	        	area = BPMConstant.HR_PERSONAL_SUB_RANGE[2];
	        }
	        
	        if (personRange.trim().equals(BPMConstant.HR_PERSONAL_RANGE[2])) {
	        	area = "海外";
	        }
		} catch (Exception e) {
			logger.debug(e.getMessage());
		}
		return area;
	}

	@Override
	public BpmConfProcessRole querySingle(QueryMap critera) {
		return bpmConfProcessRoleDao.getSingleBpmConfProcessRole(critera.getMap());
	}

}
