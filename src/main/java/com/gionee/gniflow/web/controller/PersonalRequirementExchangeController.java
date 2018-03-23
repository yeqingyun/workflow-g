package com.gionee.gniflow.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gionee.auth.UserService;
import com.gionee.auth.model.User;
import com.gionee.gniflow.biz.service.PersonalRequirementExchangeService;
import com.gionee.gniflow.web.easyui.ComboBoxData;

@RequestMapping("/personalRequirement")
@Controller
public class PersonalRequirementExchangeController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PersonalRequirementExchangeService personalRequirementExchangeService;
	
	@RequestMapping("/getUserByOrgId.json")
	@ResponseBody
	public List<ComboBoxData> getUserByOrgId(@RequestParam("orgId") Integer orgId) {
		List<ComboBoxData> comboBoxData = new ArrayList<ComboBoxData>();
		List<User> users = userService.getUsersByOrgId(orgId);
		for(User user : users){
			comboBoxData.add(new ComboBoxData(String.valueOf(user.getId()), user.getName()+" ["+user.getAccount()+"]"));
		}
		return comboBoxData;
	}
	
	@RequestMapping("/selectedOnce.json")
	@ResponseBody
	private boolean selectedOnce(@RequestParam("account") String account) {
		if(!personalRequirementExchangeService.getSelectedOnce(account).isEmpty()){
			return true;
		}
		return false;
	}

}
