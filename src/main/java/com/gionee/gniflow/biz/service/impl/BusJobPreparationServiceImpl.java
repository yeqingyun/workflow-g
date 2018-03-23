package com.gionee.gniflow.biz.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.gionee.auth.model.Organization;
import com.gionee.gnif.GnifException;
import com.gionee.gnif.dto.QueryMap;
import com.gionee.gnif.model.PageResult;
import com.gionee.gniflow.biz.service.BusJobPreparationService;
import com.gionee.gniflow.biz.model.BusJobPreparation;
import com.gionee.gniflow.integration.dao.BusJobPreparationDao;
import com.gionee.gniflow.integration.dao.OrganizationHelpDao;
import com.gionee.gniflow.web.easyui.ComboBoxData;

@Service
public class BusJobPreparationServiceImpl implements BusJobPreparationService {

    @Autowired
    private BusJobPreparationDao busJobPreparationDao;
    
    @Autowired
    private OrganizationHelpDao organizationHelpDao;
    
	@Override
	public void save(BusJobPreparation busJobPreparation) {
		//设置该部门的顶层部门
		Organization rootOrg = organizationHelpDao.queryRootNode(busJobPreparation.getOrgId());
		if (rootOrg == null) {
			throw new GnifException("Couldn't found root organization for org id " + busJobPreparation.getOrgId());
		}
		busJobPreparation.setRootOrgId(rootOrg.getId());
		busJobPreparation.setRootOrgName(rootOrg.getName());
		
		if (busJobPreparation.getId() == null) {
			busJobPreparationDao.insert(busJobPreparation);
		}
		else {
			busJobPreparationDao.update(busJobPreparation);
		}
	}

	@Override
	public BusJobPreparation getBusJobPreparation(Integer id) {
		return busJobPreparationDao.get(id);
	}

	@Override
	public void delete(Integer id) {
		busJobPreparationDao.delete(id);
	}

	@Override
	public List<BusJobPreparation> query(QueryMap critera) {
		return busJobPreparationDao.getAll(critera.getMap());
	}

	@Override
	public PageResult<BusJobPreparation> queryPage(QueryMap critera) {
		PageResult<BusJobPreparation> result = new PageResult<BusJobPreparation>();
		result.setRows(busJobPreparationDao.getPage(critera.getMap()));
		result.setTotal(busJobPreparationDao.getPageCount(critera.getMap()));
		return result;
	}

	@Override
	public void handleBusJobPrepara(List<Map<String,String>> lists) throws Exception{
		
		int index = 2;
		for (Map<String,String> element : lists) {
			//根据所属公司名称查出公司ID
			Organization rootOrg = organizationHelpDao.queryOrganizationByName(element.get("4"));
			if (rootOrg == null) {
				throw new GnifException("第"+ index +"行["+ element.get("4") +"]所属公司不存在，请检查导入文件中填写的数据是否正确！");
			}
			//根据角色名称查出角色的ID
			Organization curOrg = organizationHelpDao.queryChildrenByRootNode(rootOrg.getId(), element.get("0"));
			if (curOrg == null) {
				throw new GnifException("第"+ index +"行["+ element.get("0") +"]所属公司下的部门不存在，请检查导入文件中填写的数据是否正确！");
			}
			
			BusJobPreparation jobPreparation = null;
			List<BusJobPreparation> result = null;
			Map<String, Object> param = new HashMap<String,Object>();
			if (rootOrg != null && curOrg != null) {
				jobPreparation = new BusJobPreparation();
				jobPreparation.setOrgId(curOrg.getId());
				jobPreparation.setOrgName(curOrg.getName());
				jobPreparation.setPositions(element.get("1"));
				jobPreparation.setGrade(element.get("2"));
				jobPreparation.setPlait(Integer.parseInt(element.get("3")));
//				jobPreparation.setExistNum(Integer.parseInt(element.get("4")));
				jobPreparation.setRootOrgId(rootOrg.getId());
				jobPreparation.setRootOrgName(rootOrg.getName());
				jobPreparation.setSalaryRange(element.get("5"));
				
				param.clear();
				param.put("orgId", jobPreparation.getOrgId());
				param.put("rootOrgId", jobPreparation.getRootOrgId());
				param.put("positions", element.get("1"));
				result = busJobPreparationDao.getAll(param);
				//验证是否已经在数据库存在，存在则不插入
				if (CollectionUtils.isEmpty(result)) {
					busJobPreparationDao.insert(jobPreparation);
				} else {
					//更新
					BusJobPreparation updateJobPrepara = result.get(0);
					jobPreparation.setId(updateJobPrepara.getId());
					busJobPreparationDao.update(jobPreparation);
				}
			}
		}
		index++;
	}

	@Override
	public List<BusJobPreparation> statBusJobPreparation(Integer orgId) {
		return busJobPreparationDao.statBusJobPreparation(orgId);
	}

	@Override
	public List<ComboBoxData> getJobInfoFromDept(String deptId){
		List<ComboBoxData> dataList = new ArrayList<ComboBoxData>();
		List<BusJobPreparation> reqData = busJobPreparationDao.getJob(deptId);
		for(BusJobPreparation temp : reqData){
			dataList.add(new ComboBoxData(temp.getId()+"", temp.getPositions()));
		}
		return dataList;
	}
}
