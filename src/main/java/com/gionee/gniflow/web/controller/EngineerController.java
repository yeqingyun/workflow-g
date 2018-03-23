package com.gionee.gniflow.web.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.activiti.engine.delegate.DelegateExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gionee.gnif.dto.MessageDto;
import com.gionee.gnif.dto.QueryMap;
import com.gionee.gnif.model.PageResult;
import com.gionee.gnif.web.util.EntryFactory;
import com.gionee.gniflow.biz.model.BpmConfProcessRole;
import com.gionee.gniflow.biz.model.Engineer;
import com.gionee.gniflow.biz.model.InterviewEntity;
import com.gionee.gniflow.biz.service.EngineerService;
import com.gionee.gniflow.biz.service.ProcessHelpService;
import com.gionee.gniflow.constant.WebReqConstant;
import com.gionee.gniflow.dto.HrUserDto;
import com.gionee.gniflow.integration.dao.BpmConfProcessRoleDao;
import com.gionee.gniflow.integration.dao.KeepRunTaskDao;
import com.gionee.gniflow.web.easyui.AjaxJson;
import com.gionee.gniflow.web.easyui.ComboBoxData;
import com.gionee.gniflow.web.util.PropertyHolder;

@Controller
@RequestMapping("/engineer")
public class EngineerController {
	
	@Autowired
	EngineerService engineerService;
	@Autowired
	ProcessHelpService processHelpService;
	@Autowired
	BpmConfProcessRoleDao bpmConfProcessRoleDao;
	@Autowired
	KeepRunTaskDao keepRunTaskDao;
	
	@RequestMapping("/getOnWork.json")
	@ResponseBody
	public Boolean getOnWork(String account) {
		Boolean workStatus = processHelpService.isOnWork(account);
		//System.out.println(workStatus);
		return workStatus;
	}
	
	@RequestMapping("/getAddress.json")
	@ResponseBody
	public List<ComboBoxData> getAddress() {
		List<ComboBoxData> comlist=new ArrayList<ComboBoxData>();
		List<Engineer> addresses=engineerService.getAddress();
		Map<String,Object> map=new HashMap<String,Object>();
		for(Engineer engineer:addresses){
			if(!map.containsKey(engineer.getAddress())){
				map.put(engineer.getAddress(),engineer.getId());
			}else{
				String code=String.valueOf(map.get(engineer.getAddress()))+","+engineer.getId();
				map.put(engineer.getAddress(),code);
			}
		}
		for(String address:map.keySet()){
			ComboBoxData com=new ComboBoxData();
			com.setId(String.valueOf(map.get(address)));
			com.setText(address);
			comlist.add(com);
		}
		return comlist;
	}
	
	@RequestMapping("/getEngineers.json")
	@ResponseBody
	public List<Engineer> getEngineers(QueryMap queryMap) {
		return engineerService.query(queryMap);
	}
	
	@RequestMapping("/get.json")
	@ResponseBody
	public Engineer get(Integer id) {
		return engineerService.getEngineer(id);
	}
	
	@RequestMapping("/sav.json")
	@ResponseBody
	public ResponseEntity<MessageDto> sav(Engineer engineer) {
		try {
			engineerService.save(engineer);
		} catch (Exception e) {
			return EntryFactory.create(new MessageDto("保存失败", false));
		}
		return EntryFactory.create(new MessageDto("保存成功"));
	}
	
	@RequestMapping("/del.json")
	@ResponseBody
	public ResponseEntity<MessageDto> del(Integer id) {
		try {
			engineerService.delete(id);
		} catch (Exception e) {
			return EntryFactory.create(new MessageDto("删除失败", false));
		}
		return EntryFactory.create(new MessageDto("删除成功"));
	}

	@RequestMapping("/queryPage.json")
	@ResponseBody
	public PageResult<Engineer> search(QueryMap critera) {
		return engineerService.queryPage(critera);
	}
	
	@RequestMapping("/getLoginNo.json")
	@ResponseBody
	public String getLoginNo(String engineerId){
		try {
			return processHelpService.getEmpAccount(engineerId);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@RequestMapping("/getUserInfo.json")
	@ResponseBody
	public Engineer getUserById(String userId){
		return engineerService.getUserById(userId);
	}
	
	@RequestMapping("/getCountByAddress.json")
	@ResponseBody
	public String getCountByAddress(QueryMap queryMap){
		int count=engineerService.getCountByAddress(queryMap);
		return String.valueOf(count);
	}
	/**
	 * 根据第三层部门名称获取相应的招聘专员
	 * @param departName
	 * @return
	 */
	@RequestMapping("/getDepartRecruiterAccount.json")
	@ResponseBody
	public String getDepartRecruiterAccount(String departName){
		try {
			return processHelpService.getDepartRecruiterAccount(departName);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 根据部门id获取第三层部门的名称
	 * @param orgId
	 * @return
	 */
	@RequestMapping("/getThreeDepartByOrgId.json")
	@ResponseBody
	public AjaxJson getThreeDepartByOrgId(String orgId,String account){
		AjaxJson ajaxJson=new AjaxJson();
		String empId=processHelpService.loadEmpByUsrAccount(account);
		String recruiters = PropertyHolder
				.getContextProperty(WebReqConstant.DEPART_RECRUITER_EMPID);
		Map<String,Object> map=processHelpService.parseJsonToMap(recruiters);
		Set<String> empIds=map.keySet();
		String depart=processHelpService.getThreeDepartByOrgId(orgId);
		//判断招聘专员是否有权限申请此部门的流程
		if(empIds.contains(empId)){
			String departs=(String)map.get(empId);
			if(!departs.contains(depart)){
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("你没权限申请此部门的流程！");
				return ajaxJson;
			}
		}
		ajaxJson.setMsg(depart);
		return ajaxJson;
	}
	/**
	 * 根据部门id判断是否是研发
	 * @param orgId
	 * @return
	 */
	@RequestMapping("/ifOffThreeDepartById.json")
	@ResponseBody
	public boolean ifOffThreeDepartById(String orgId){
		return processHelpService.ifOffThreeDepartById(orgId,"50011894,50033233,10100081");
	}
	/**
	 * 根据角色名称、实例名称获取相应的角色账号
	 * @param roleName
	 * @param processDefKey
	 * @return
	 */
	@RequestMapping("/getUserRecruiterAccount.json")
	@ResponseBody
	public String getUserRecruiterAccount(String roleName,String processDefKey){
		String roleAccount="";
		String[] roleS=roleName.split(",");
		for(int i=0;i<roleS.length;i++){
			Map<String,Object> param=new HashMap<String,Object>();
			param.put("processDefKey",processDefKey);
			param.put("roleName", roleS[i]);
			param.put("method", "getSpecialRoleMaster");
			BpmConfProcessRole confProRole = bpmConfProcessRoleDao.getSingleBpmConfProcessRole(param);
			roleAccount+=confProRole.getAssignee()+",";
		}
		if (!"".equals(roleAccount)) {
			roleAccount=roleAccount.substring(0,roleAccount.length());
		}
		
		return roleAccount;
	}
	
	/**
	 * 获取录用人员的薪级及薪资
	 * @param queryMap
	 * @return
	 */
	@RequestMapping("/getSararyAndGroupFromIter.json")
	@ResponseBody
	public AjaxJson getSararyAndGroupFromIter(QueryMap queryMap){
		AjaxJson ajaxJson=new AjaxJson();
		try{
			List<InterviewEntity> groupSalary=keepRunTaskDao.getSararyAndGroupFromIter(queryMap.getMap());
			if(groupSalary != null && groupSalary.size() > 0){
				ajaxJson.setObj(groupSalary.get(groupSalary.size()-1));
			}else{
				ajaxJson.setSuccess(false);
			}
		}catch(Exception e){
			e.printStackTrace();
			ajaxJson.setSuccess(false);
		}
		return ajaxJson;
	}
	
	/**
	 * 判断该职员的试用期是否在申请中
	 * @param empId
	 * @return
	 */
	@RequestMapping("/getEmpIdProbationApply.json")
	@ResponseBody
	public boolean getEmpIdProbationApply(String empId){
		boolean flag=true;
		String account=keepRunTaskDao.getEmpIdProbationApply(empId);
		if(account==null || "".equals(account.trim()) || !empId.equals(account)){
			flag=false;
		}
		return flag;
	}
}
