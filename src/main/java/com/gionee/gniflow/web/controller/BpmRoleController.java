/**
 * @(#) BpmRoleController.java Created on 2014年5月28日
 *
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.gionee.gniflow.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.gionee.auth.UserService;
import com.gionee.auth.model.User;
import com.gionee.gnif.constant.TreeConstant;
import com.gionee.gnif.dto.QueryMap;
import com.gionee.gnif.model.PageResult;
import com.gionee.gniflow.biz.model.BpmRole;
import com.gionee.gniflow.biz.model.BpmRoleProcdef;
import com.gionee.gniflow.biz.model.BpmUserRole;
import com.gionee.gniflow.biz.model.Category;
import com.gionee.gniflow.biz.service.ActivitiHelpService;
import com.gionee.gniflow.biz.service.BpmRoleProcdefService;
import com.gionee.gniflow.biz.service.BpmRoleService;
import com.gionee.gniflow.biz.service.BpmUserRoleService;
import com.gionee.gniflow.biz.service.CategoryService;
import com.gionee.gniflow.biz.service.ProcessHelpService;
import com.gionee.gniflow.dto.BpmTreeDto;
import com.gionee.gniflow.web.easyui.AjaxJson;
import com.gionee.gniflow.web.response.BpmUserRoleResponse;
import com.gionee.gniflow.web.response.UserResponse;
import com.gionee.gniflow.web.util.PoiUtil;

/**
 * The class <code>BpmRoleController</code>
 * 
 * @author lipw
 * @version 1.0
 */
@RequestMapping("/bpmRole")
@Controller
public class BpmRoleController {
	
	private static final Logger logger = LoggerFactory.getLogger(BpmRoleController.class);
	
	@Autowired
	private BpmRoleService bpmRoleService;
	
	@Autowired
	private BpmRoleProcdefService bpmRoleProcdefService;
	
	@Autowired
	private BpmUserRoleService bpmUserRoleService;
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private ActivitiHelpService activitiHelpService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ProcessHelpService processHelpService;
	
    /** 加载流程角色*/
    @RequestMapping("/loadAllBpmRole.json")
    @ResponseBody
    public List<BpmRole> loadAllBpmRole(QueryMap queryMap){
    	List<BpmRole> bpmRoles = bpmRoleService.query(queryMap);
    	
    	QueryMap newQueryMap = new QueryMap();
    	if (!CollectionUtils.isEmpty(bpmRoles)) {
    		for (BpmRole bpmRole : bpmRoles) {
    			newQueryMap.getMap().clear();
    			newQueryMap.put("roleId", bpmRole.getId());
    			List<BpmRoleProcdef> result = bpmRoleProcdefService.query(newQueryMap);
    			bpmRole.setRelationDetail(handleRelationDetail(result));
    		}
    	}
    	return bpmRoles;
    }
    @RequestMapping("/loadBpmRole.json")
    @ResponseBody
    public PageResult<BpmRole> loadBpmRole(QueryMap queryMap){
    	PageResult<BpmRole> pageResult = bpmRoleService.queryPage(queryMap);
    	
    	QueryMap newQueryMap = new QueryMap();
    	if (!CollectionUtils.isEmpty(pageResult.getRows())) {
    		for (BpmRole bpmRole : pageResult.getRows()) {
    			newQueryMap.getMap().clear();
    			newQueryMap.put("roleId", bpmRole.getId());
    			List<BpmRoleProcdef> result = bpmRoleProcdefService.query(newQueryMap);
    			bpmRole.setRelationDetail(handleRelationDetail(result));
    		}
    	}
    	return pageResult;
    }
    /** 保存流程角色 及 角色-资源关系表 */
    @RequestMapping(value="/saveBpmRole.html")
    @ResponseBody
    public AjaxJson saveBpmRole(BpmRole bpmRole, @RequestParam("roleResources") String roleResources){
    	AjaxJson ajaxJson = null;
    	try {
    		if (bpmRole.getId() == null) {
    			QueryMap queryMap = new QueryMap();
    			queryMap.put("name", bpmRole.getName());
    			List<BpmRole> bpmRoles = bpmRoleService.query(queryMap);
    			if(bpmRoles!=null&&bpmRoles.size()>0){
    				ajaxJson = new AjaxJson(false, "流程角色不能重复!");
    			}else{
    				bpmRoleService.save(bpmRole, roleResources);
    				ajaxJson = new AjaxJson(true, "保存流程角色成功!");
    			}
    		}else{
    			bpmRoleService.save(bpmRole, roleResources);
    			ajaxJson = new AjaxJson(true, "保存流程角色成功!");
    		}
		} catch (Exception e) {
			e.printStackTrace();
			ajaxJson = new AjaxJson(false, "保存流程角色失败!");
		}
    	return ajaxJson;
    }
    
    /** 删除流程角色 */
    @RequestMapping(value="/deleteBpmRole.json")
    @ResponseBody
    public AjaxJson deleteCategory(@RequestParam("id") int id) {
    	AjaxJson ajaxJson = null;
    	try {
    		bpmRoleService.delete(id);
    		ajaxJson = new AjaxJson(true, "删除流程角色成功!");
		} catch (Exception e) {
			e.printStackTrace();
			ajaxJson = new AjaxJson(false, "删除流程角色失败!");
		}
    	return  ajaxJson;
    }
    
    @RequestMapping("/saveUserRole.json")
	@ResponseBody
	public AjaxJson saveUserRole(@RequestParam("userIds") String userIds, @RequestParam("roleIds") String roleIds) {
    	AjaxJson ajaxJson = null;
    	try {
    		if (StringUtils.isNotEmpty(userIds) && StringUtils.isNotEmpty(roleIds)) {
    			String[] userIdsArr = userIds.split(",");
    			String[] roleIdsArr = roleIds.split(",");
    			
    			BpmUserRole userRole = null;
    			//先删除之前的关系
    			for (String userId : userIdsArr) {
    				QueryMap newQueryMap = new QueryMap();
    				newQueryMap.put("userId", Integer.parseInt(userId));
    				bpmUserRoleService.delete(newQueryMap);
    			}
    			
    			for (String userId : userIdsArr) {
    				for (String roleId : roleIdsArr) {
    					userRole = new BpmUserRole(Integer.parseInt(userId), Integer.parseInt(roleId));
    					bpmUserRoleService.save(userRole);
    				}
    			}
    		}
    		ajaxJson = new AjaxJson(true, "保存数据成功!");
		} catch (Exception e) {
			e.printStackTrace();
			ajaxJson = new AjaxJson(false, "保存数据失败!");
		}
    	return  ajaxJson;
	}
    
    /**
     * 插入角色用户关联关系
     * @param userIds
     * @param roleIds
     * @return
     */
    @RequestMapping("/insertUserRole.json")
	@ResponseBody
	public AjaxJson insertUserRole(@RequestParam("userIds") String userIds, @RequestParam("roleIds") String roleIds) {
    	AjaxJson ajaxJson = null;
    	try {
    		if (StringUtils.isNotEmpty(userIds) && StringUtils.isNotEmpty(roleIds)) {
    			String[] userIdsArr = userIds.split(",");
    			String[] roleIdsArr = roleIds.split(",");
    			
    			BpmUserRole userRole = null;
    			for (String userId : userIdsArr) {
    				for (String roleId : roleIdsArr) {
    					userRole = new BpmUserRole(Integer.parseInt(userId), Integer.parseInt(roleId));
    					QueryMap queryMap = new QueryMap();
    					queryMap.put("roleId", roleId);
    					queryMap.put("userId", userId);
    					List<BpmUserRole> list = bpmUserRoleService.query(queryMap);
    					if(list == null || list.isEmpty()){
    						bpmUserRoleService.save(userRole);
    					}
    				}
    			}
    		}
    		ajaxJson = new AjaxJson(true, "保存数据成功!");
		} catch (Exception e) {
			e.printStackTrace();
			ajaxJson = new AjaxJson(false, "保存数据失败!");
		}
    	return  ajaxJson;
	}
    /**
     * 删除角色中的某些用户
     * @param userIds
     * @param roleIds
     * @return
     */
    @RequestMapping("/deleteBpmRoleUser.json")
	@ResponseBody
	public AjaxJson deleteBpmRoleUser(@RequestParam("userIds") String userIds,@RequestParam("roleIds") String roleIds) {
    	AjaxJson ajaxJson = null;
    	try {
    		if (StringUtils.isNotEmpty(userIds) && StringUtils.isNotEmpty(roleIds)) {
    			String[] userIdsArr = userIds.split(",");
    			String[] roleIdsArr = roleIds.split(",");
    			
    			for (String userId : userIdsArr) {
    				for (String roleId : roleIdsArr) {
    					QueryMap queryMap = new QueryMap();
    					queryMap.put("roleId", roleId);
    					queryMap.put("userId", userId);
    					bpmUserRoleService.delete(queryMap);
    				}
    			}
    		}
    		ajaxJson = new AjaxJson(true, "删除数据成功!");
		} catch (Exception e) {
			e.printStackTrace();
			ajaxJson = new AjaxJson(false, "删除数据失败!");
		}
    	return  ajaxJson;
	}
    @RequestMapping("/bpmusers.json")
	@ResponseBody
	public  PageResult<BpmUserRoleResponse> bpmusers(QueryMap queryMap,Integer roleId, @RequestParam(value="userAccount",required=false) String userAccount,
			@RequestParam(value="userName",required=false) String userName) {
			if(roleId==0){
				return null;
			}
			queryMap.put("roleId", roleId);
			if (StringUtils.isNotEmpty(userAccount)) {
				queryMap.put("account", userAccount);
			}
			if (StringUtils.isNotEmpty(userName)) {
				queryMap.put("name", userName);
			}
			PageResult<BpmUserRoleResponse>  result = bpmUserRoleService.queryPage(queryMap);
		
			return result;
	}
    
    
    @RequestMapping("/users.json")
	@ResponseBody
	public List<UserResponse> users(Integer id, @RequestParam(value="userAccount",required=false) String userAccount,
			@RequestParam(value="userName",required=false) String userName,@RequestParam(value="orgId",required=false) Integer orgId) {
		List<com.gionee.gniflow.web.response.UserResponse> ret = new ArrayList<UserResponse>();
		List<User> users = null;
		if (id != null) {
			users = userService.getUsersByOrgId(id);
		} else {
			QueryMap queryMap = new QueryMap();
			if (StringUtils.isNotEmpty(userAccount)) {
				queryMap.put("account", userAccount);
			}
			if (StringUtils.isNotEmpty(userName)) {
				queryMap.put("nameLike", "%" + userName + "%");
			}
			if (StringUtils.isNotEmpty(String.valueOf(orgId))) {
				queryMap.put("orgId", orgId);
			}
			users = userService.queryUsers(queryMap.getMap());
		}
		
		for (User user:users) {
			UserResponse userResponse = new UserResponse(user);
			userResponse.setRoleEntities(bpmRoleService.getBpmRoles(user.getId()));
			ret.add(userResponse);
		}
		return ret;
	}
    
    @RequestMapping("/getDepartPrincipal.json")
	@ResponseBody
	public List<UserResponse> getDepartPrincipal(String id, @RequestParam(value="userAccount",required=false) String userAccount,
			@RequestParam(value="userName",required=false) String userName) {
		List<com.gionee.gniflow.web.response.UserResponse> ret = new ArrayList<UserResponse>();
		List<User> users = null;
		if (id != null) {
			users=new ArrayList<User>();
			List<User> user=new ArrayList<User>();
			String[] ids=id.split(",");
			for(int i=0;i<ids.length;i++){
				user.addAll(userService.getUsersByOrgId(Integer.parseInt(ids[i])));
			}
			//users = userService.getUsersByOrgId(id);
			for(int i=0;i<user.size();i++){
				if(processHelpService.isMiddleManager(user.get(i).getAccount())){
					for(User u:users){
						if(user.get(i).getAccount().equals(u.getAccount())){
							continue;
						}
					}
					users.add(user.get(i));
				}
			}
		} else {
			QueryMap queryMap = new QueryMap();
			if (StringUtils.isNotEmpty(userAccount)) {
				queryMap.put("account", userAccount);
			}
			if (StringUtils.isNotEmpty(userName)) {
				queryMap.put("nameLike", "%" + userName + "%");
			}
			users = userService.queryUsers(queryMap.getMap());
		}
		
		for (User user:users) {
			UserResponse userResponse = new UserResponse(user);
			userResponse.setRoleEntities(bpmRoleService.getBpmRoles(user.getId()));
			ret.add(userResponse);
		}
		return ret;
	}
    
    @RequestMapping("/bpmRoleTree.json")
	@ResponseBody
	public List<BpmTreeDto> getMenu() {
		List<Category> categoryList = categoryService.query4Pid(0L);
		//Catetory转换为BpmTreeDto
		List<BpmTreeDto> trees = transToListBpmTreeDto(categoryList);
		if (trees != null && trees.size() > 0) {
			for (int i=trees.size()-1; i>=0; i--) {
				if (resolveMenu(trees.get(i))) {
					trees.remove(i);
				}
			}
		}
		return trees;
	}
	
    /**
     * 导入人员配置
     * @param file
     * @return
     */
    @RequestMapping(value = "/importUserAssignment.html")
    @ResponseBody
    public AjaxJson importUserAssignment(@RequestParam(value = "file") MultipartFile file) {
    	AjaxJson ajaxJson = null;
		String fileName = file.getOriginalFilename();
		
		String[] excleTitle = new String[]{"员工账号","角色名称"};
		//存储错误信息
		Map<Integer,String> errors = new TreeMap<Integer,String>();
		StringBuffer sb = null;
		if(fileName.endsWith(".xls") || fileName.endsWith(".xlsx") && file.getSize() < 10*1024*1024) {
			try {
				int i= 0;
				List<Map<String,String>> lists = PoiUtil.importExcelToMap(file.getInputStream(), fileName,excleTitle.length, 1);
				//循环验证格式，为空，数字
				if (!CollectionUtils.isEmpty(lists)) {
					for(Map<String, String> element : lists){
						sb = new StringBuffer();
						String userAccount = element.get("0");
						String roleName = element.get("1");
						if(StringUtils.isEmpty(userAccount)){
							sb.append("用户账号为空！");
						}
						if(StringUtils.isEmpty(roleName)){
							sb.append("角色名称为空！");
						}
						if(sb.toString().length()>0){
							errors.put(i+1, sb.toString());
						}
						i++;
					}
				} else {
					errors.put(2, "Excel文件中未包含导入数据!");
				}
				//有错，则输出相关信息
				if(errors.size() > 0){
					sb = new StringBuffer();
					for(Object o: errors.keySet()){
						sb.append("第" + o + "行").append(errors.get(o)).append("<br/>");
			        }  
					ajaxJson = new AjaxJson(false, sb.toString());
				} else {
					try {
						bpmUserRoleService.handleBpmUserRole(lists);
						ajaxJson = new AjaxJson(true, "导入成功！");
					} catch (Exception e) {
						ajaxJson = new AjaxJson(false, e.getMessage());
					}
				}
			} catch (Exception e) {
				logger.error("BpmRoleController Exception", e);
				ajaxJson = new AjaxJson(false, "导入失败,请详细检查导入文件格式是否正确!");
			} 
		} else {
			ajaxJson = new AjaxJson(false, "导入失败,请导入Excel文件!");
		}
		
		return ajaxJson;
    }
    
	private boolean resolveMenu(BpmTreeDto tree) {
		//设置子节点
		List<Category> subCategorys = categoryService.query4Pid(Long.parseLong(tree.getId()));
		List<BpmTreeDto> trees = transToListBpmTreeDto(subCategorys);
		tree.setChildren(trees);

		List<ProcessDefinition> list = activitiHelpService.queryLastVersionAndCategoryBySort(tree.getId().toString());
		
		if ((trees == null || trees.size() == 0) && list.size() == 0) {
			return true; // tell parent to remove it.
		}
		
		boolean needRemove = true;
		if (trees != null && trees.size() > 0) {
			for (int i=trees.size()-1; i>=0; i--) {
				if (resolveMenu(trees.get(i))) {
					trees.remove(i);
				}
				else {
					needRemove = false;
				}
			}
		}
		if (list.size() > 0) {
			for (ProcessDefinition process:list) {
				BpmTreeDto processNode = new BpmTreeDto();
				processNode.setId(process.getKey());
				processNode.setText(process.getName());
				processNode.setState(TreeConstant.STATE_OPEN);
				tree.getChildren().add(processNode);
			}
			needRemove = false;
		}
		return needRemove;
	}
    
    /**
     * 获取BPM_ROLE_PROCDEF
     * @param bpmRoleProcDefList
     * @return
     */
    private String handleRelationDetail(List<BpmRoleProcdef> bpmRoleProcDefList){
    	StringBuffer sb = new StringBuffer();
    	for (BpmRoleProcdef def : bpmRoleProcDefList) {
    		sb.append(def.getProcdefKey()).append(",");
    	}
    	if (sb.length() > 0) {
    		sb = sb.delete(sb.length() - 1, sb.length());
    	}
    	
    	return sb.toString();
    }
    
    private BpmTreeDto transToBpmTreeDto(Category category){
    	BpmTreeDto treeDto = new BpmTreeDto();
    	treeDto.setId(String.valueOf(category.getId()));
    	treeDto.setPid(String.valueOf(category.getPid()));
    	treeDto.setText(category.getName());
    	treeDto.setState(TreeConstant.STATE_OPEN);
    	treeDto.setChecked(false);
    	return treeDto;
    }
    
    private List<BpmTreeDto> transToListBpmTreeDto(List<Category> categorys){
    	List<BpmTreeDto> result = new ArrayList<BpmTreeDto>();
    	for (Category cate : categorys) {
    		result.add(transToBpmTreeDto(cate));
    	}
    	return result;
    }
    
    //根据工号查找账号
    @RequestMapping("/getEmpAccount.json")
	@ResponseBody
	public String getEmpAccount(String empNo) {
    	try {
			return processHelpService.getEmpAccount(empNo);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
    @RequestMapping("/findById.json")
    @ResponseBody
    public String findById(String account){
    	return processHelpService.findById(account);
    }
}
