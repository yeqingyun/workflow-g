package com.gionee.gniflow.web.controller;


import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.gionee.gniflow.biz.model.ForeignTrainingDetail;
import com.gionee.gniflow.biz.model.MFixedAssetsDetraction;
import com.gionee.gniflow.biz.model.NewCompile;
import com.gionee.gniflow.biz.model.NonProductions;
import com.gionee.gniflow.biz.model.RapActivititiesDetail;
import com.gionee.gniflow.biz.model.TeamActivitiesDetail;
import com.gionee.gniflow.biz.model.Training;
import com.gionee.gniflow.biz.service.ActivitiHelpService;
import com.gionee.gniflow.biz.service.ProcessHelpService;
import com.gionee.gniflow.biz.service.RapInforService;
import com.gionee.gniflow.biz.service.TeamActivitiesDetailService;
import com.gionee.gniflow.dto.HrUserDto;
import com.gionee.gniflow.util.ExcelReadUtil;
import com.gionee.gniflow.util.ReflectionUtils;
import com.gionee.gniflow.web.easyui.AjaxJson;

@RequestMapping("/teamActivities")
@Controller
public class TeamActivitiesController {
	@Autowired
	private TeamActivitiesDetailService teamActivitiesDetailService;

	@Autowired
	private ProcessHelpService processHelpService;
	
	@Autowired
	private ActivitiHelpService activitiHelpService;
	
	@Autowired
	private RapInforService rapInforService;
	
	private final static int FEBRUARY = 1;
	private final static int MARCH = 2;

	private static final Date SimpleDateFormat = null;

	// 判断是否旅游过
	@SuppressWarnings("deprecation")
	public Boolean judgeHaveTraveled(String account) {
		
		long s1=System.currentTimeMillis();
		
		List<TeamActivitiesDetail> teamActivitiesDetails = teamActivitiesDetailService.getTeamActivitiesDetail(account);
		Date currentDate = new Date();
		int currentYear = currentDate.getYear();
		int currentMonth = currentDate.getMonth();
		int year = 0;
		int month = 0;
		for (TeamActivitiesDetail temp : teamActivitiesDetails) {
//			TeamActivitiesDetail temp=teamActivitiesDetailService.getByAccount(account);
			if (temp != null&&temp.getTravelDate()!=null) {
				year = temp.getTravelDate().getYear();
				month = temp.getTravelDate().getMonth();
				if (currentMonth >= month && year == currentYear && month > FEBRUARY) {
					return true;
				}
				if (currentMonth < MARCH
						&& ((year == currentYear && month <= currentMonth) || (year == currentYear - 1 && month > FEBRUARY))) {
					return true;
				}
			}
		}
		//long s2= System.currentTimeMillis();
		//long time1 = s2-s1;
		//System.out.print( "****1"+time1+"1***" );
		return false;
		
	}
	
	
	// 金立年度旅游活动名单导入
		@RequestMapping("/imporJinLiList.html")
		@ResponseBody
		public AjaxJson imporJinLiTravelList(@RequestParam("files") MultipartFile files) throws Exception{
			//System.out.println("--------"+System.currentTimeMillis());
			
			AjaxJson ajaxJson = new AjaxJson();
			ajaxJson.setMsg("文件读取成功");
			List<HashMap<String, Object>> rows = new ArrayList<>();
			
			String [] fileds = new String[]{"number","company","department","account","name","remark"};
			List<TeamActivitiesDetail> list = null;
			try {
				list = ExcelReadUtil.ReadExcel(files.getInputStream(), fileds, TeamActivitiesDetail.class, 1);
			} catch (Exception e) {
				ajaxJson.setMsg(e.getMessage());
				ajaxJson.setSuccess(false);
				return ajaxJson;
			}
		
			//校验数据
			if(list.size()>1){
				String str = IsExistAlike(list);
				if (!"success".equals(str)) {
					ajaxJson.setMsg(str);
					ajaxJson.setSuccess(false);
					return ajaxJson;
				}
			}
			int index = 0;
			
			StringBuilder accounts=new StringBuilder();
			for(TeamActivitiesDetail team:list){
				accounts.append(team.getAccount());
				accounts.append(",");
			}
			String empIds=accounts.toString();
			empIds=empIds.substring(0,empIds.length()-1);
			
			List<HrUserDto> users = processHelpService.getAllEmpInfo(empIds);//根据员工账号获取员工基本信息
			
			for(TeamActivitiesDetail teamList : list){
				HashMap<String, Object> map = new HashMap<>();
				index++;
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
				map.put("number", teamList.getNumber());
				map.put("company", teamList.getCompany().trim());
				map.put("department", teamList.getDepartment().trim());
				map.put("account", teamList.getAccount().trim());
				map.put("name", teamList.getName().trim());
				
				
				Object flag = IsNotNull(teamList, map);
				if(!flag.equals(-1)){
					ajaxJson.setMsg("第"+index+"行数据不完整！");
					ajaxJson.setSuccess(false);
					return ajaxJson;
				}
				String account = teamList.getAccount().trim();
				int count=0;
				for(HrUserDto user :users){
					if (user == null) {
						ajaxJson.setMsg("员工"
								+ (processHelpService.getUserName(account) == null ? "编码": processHelpService.getUserName(account))
								+ "[" + account + "]不存在！");
						ajaxJson.setSuccess(false);
						return ajaxJson;
					}
					if(user.getEmpId()==null||account==null)
						continue;
					if(user.getEmpId().intValue()!=Integer.parseInt(account)){
						count++;
						continue;
					}
					//根据编号判断职员是否离职
					String empId=processHelpService.getEmpAccount(account);
					if(empId==null || "".equals(empId)){
						ajaxJson.setMsg("员工"
								+ teamList.getName().trim()
								+ "[" + account + "]已离职！");
						ajaxJson.setSuccess(false);
						return ajaxJson;
					}
					
					String fname = user.getName().trim();//从系统中获取员工姓名
					String name= teamList.getName().replaceAll(" ", "");
					if(!(name.equals(fname))){
						ajaxJson.setMsg("您好，您所填写的员工姓名：【"+name+"】，与该员工编号：【"+account+"】，所对应的姓名：【"+fname+"】不匹配，请查核确认！");
						ajaxJson.setSuccess(false);
						return ajaxJson;
					}
					String fdate = sdf.format(user.getEntryDate());//从系统中获取入职日期 
					/*String ftime = sdf.format(teamList.getEntryDate());//获取导入电子表格中的入职日期
					if(ftime.compareTo(fdate)!=0){
						ajaxJson.setMsg("您好，您所填写的入职日期：【"+ftime+"】，与该员工编号：【"+account+"】，姓名：【"+fname+"】所对应的入职日期：【"+fdate+"】不匹配，请查核确认！");
						ajaxJson.setSuccess(false);
						return ajaxJson;
					}*/
					//判断入职是否满三个月
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(user.getEntryDate());
					calendar.add(Calendar.MONTH, 3);
					Date compDate = calendar.getTime();
					Date now = new Date();
					if(!now.after(compDate)){
						ajaxJson.setMsg("员工"+ fname + "["+ account + "]入职未满三个月，不能参加活动！");
						ajaxJson.setSuccess(false);
						return ajaxJson;
					}
					
					String company = user.getCompanyName();//从系统中获取公司信息
					String fcompany = teamList.getCompany().replaceAll(" ", "");//从列表中获取员工信息
					if("金立".equals(fcompany.replaceAll(" ", ""))){
						if(!(company.substring(3,5).equals(fcompany))){//判断金卓公司，东莞金卓通信科技有限公司
							ajaxJson.setMsg("您好，您所填写的员工:【"+account+","+name+"】公司：【"+fcompany+"】，与该员工所属公司：【"+company+"】不匹配，请查核确认！");
							ajaxJson.setSuccess(false);
							return ajaxJson;
						}
					}else if("海外事业部".equals(fcompany.replaceAll(" ", ""))){
						if(!(company.trim().equals(fcompany))){//判断金卓公司，东莞金卓通信科技有限公司
							ajaxJson.setMsg("您好，您所填写的员工:【"+account+","+name+"】公司：【"+fcompany+"】，与该员工所属公司：【"+company+"】不匹配，请查核确认！");
							ajaxJson.setSuccess(false);
							return ajaxJson;
						}
					}else{
						ajaxJson.setMsg("您好，您所填写的员工:【"+account+","+name+"】公司：【"+fcompany+"】，与该员工所属公司：【"+company+"】不匹配，请查核确认！");
						ajaxJson.setSuccess(false);
						return ajaxJson;
					}
				}
				if(count==users.size()){
					ajaxJson.setMsg("员工"
							+ teamList.getName().trim()
							+ "[" + account + "]不存在！");
					ajaxJson.setSuccess(false);
					return ajaxJson;
				}
				
				// 判断员工今年是否参加过旅游
				//if (judgeHaveTraveled((String) teamList.getAccount().replaceAll(" ", ""))) {
					
					/*ajaxJson.setMsg("员工"
							+ processHelpService
									.getUserName((String) teamList.getAccount().replaceAll(" ", "")) + "["
							+ teamList.getAccount() + "]今年已经参加过旅游！");*/
					/*ajaxJson.setMsg("员工"+teamList.getName()+"["+teamList.getAccount()+"]今年已经参加过旅游!");
					ajaxJson.setSuccess(false);
					return ajaxJson;
				}*/
				if ("1".equals(ifOffTraveled((String) teamList.getAccount().replaceAll(" ", "")))) {
					
					ajaxJson.setMsg("员工"
							+ teamList.getName() + "["
							+ teamList.getAccount() + "]今年的旅游申请在审核中！");
					ajaxJson.setSuccess(false);
					return ajaxJson;
				}else if("2".equals(ifOffTraveled((String) teamList.getAccount().replaceAll(" ", "")))){
					ajaxJson.setMsg("员工"
							+ teamList.getName() + "["
							+ teamList.getAccount() + "]今年已经参加过旅游！");
					ajaxJson.setSuccess(false);
					return ajaxJson;
				}
				
				map.put("remark", teamList.getRemark());
				rows.add(map);
			}
			ajaxJson.setObj(rows);
			//System.out.println("--------"+System.currentTimeMillis());
			return ajaxJson;
		}

	// 年度旅游活动名单导入
	@RequestMapping("/impor.html")
	@ResponseBody
	public AjaxJson impor(@RequestParam("files") MultipartFile files) throws Exception{
		//System.out.println("--------"+System.currentTimeMillis());
		
		AjaxJson ajaxJson = new AjaxJson();
		ajaxJson.setMsg("文件读取成功");
		List<HashMap<String, Object>> rows = new ArrayList<>();
		
		String [] fileds = new String[]{"number","company","department","account","name","entryDate","travelDate","remark"};
		List<TeamActivitiesDetail> list = null;
		try {
			list = ExcelReadUtil.ReadExcel(files.getInputStream(), fileds, TeamActivitiesDetail.class, 1);
		} catch (Exception e) {
			ajaxJson.setMsg(e.getMessage());
			ajaxJson.setSuccess(false);
			return ajaxJson;
		}
	
		//校验数据
		if(list.size()>1){
			String str = IsExistAlike(list);
			if (!"success".equals(str)) {
				ajaxJson.setMsg(str);
				ajaxJson.setSuccess(false);
				return ajaxJson;
			}
		}
		int index = 0;
		
		StringBuilder accounts=new StringBuilder();
		for(TeamActivitiesDetail team:list){
			accounts.append(team.getAccount());
			accounts.append(",");
		}
		String empIds=accounts.toString();
		empIds=empIds.substring(0,empIds.length()-1);
		
		List<HrUserDto> users = processHelpService.getAllEmpInfo(empIds);//根据员工账号获取员工基本信息
		for(TeamActivitiesDetail teamList : list){
			HashMap<String, Object> map = new HashMap<>();
			index++;
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
			map.put("number", teamList.getNumber());
			map.put("company", teamList.getCompany().trim());
			map.put("department", teamList.getDepartment().trim());
			map.put("account", teamList.getAccount().trim());
			map.put("name", teamList.getName().trim());
			map.put("entryDate", sdf.format(teamList.getEntryDate()));
			map.put("travelDate", sdf.format(teamList.getTravelDate()));
			
			
			Object flag = IsNotNull(teamList, map);
			if(!flag.equals(-1)){
				ajaxJson.setMsg("第"+index+"行数据不完整！");
				ajaxJson.setSuccess(false);
				return ajaxJson;
			}
			String account = teamList.getAccount().trim();
			
			int count=0;
			for(HrUserDto user :users){
				if (user == null) {
					ajaxJson.setMsg("员工"
							+ (processHelpService.getUserName(account) == null ? "编码": processHelpService.getUserName(account))
							+ "[" + account + "]不存在！");
					ajaxJson.setSuccess(false);
					return ajaxJson;
				}
				if(user.getEmpId()==null||account==null)
					continue;
				if(user.getEmpId().intValue()!=Integer.parseInt(account)){
					count++;
					continue;
				}
				//根据编号判断员工是否离职
				String empId=processHelpService.getEmpAccount(account);
				if(empId==null || "".equals(empId)){
					ajaxJson.setMsg("员工"
							+ teamList.getName().trim()
							+ "[" + account + "]已离职！");
					ajaxJson.setSuccess(false);
					return ajaxJson;
				}
				
				String fname = user.getName().trim();//从系统中获取员工姓名
				String name= teamList.getName().replaceAll(" ", "");
				if(!(name.equals(fname))){
					ajaxJson.setMsg("您好，您所填写的员工姓名：【"+name+"】，与该员工编号：【"+account+"】，所对应的姓名：【"+fname+"】不匹配，请查核确认！");
					ajaxJson.setSuccess(false);
					return ajaxJson;
				}
				String fdate = sdf.format(user.getEntryDate());//从系统中获取入职日期 
				String ftime = sdf.format(teamList.getEntryDate());//获取导入电子表格中的入职日期
				if(ftime.compareTo(fdate)!=0){
					ajaxJson.setMsg("您好，您所填写的入职日期：【"+ftime+"】，与该员工编号：【"+account+"】，姓名：【"+fname+"】所对应的入职日期：【"+fdate+"】不匹配，请查核确认！");
					ajaxJson.setSuccess(false);
					return ajaxJson;
				}
				//判断入职是否满三个月
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(user.getEntryDate());
				calendar.add(Calendar.MONTH, 3);
				Date compDate = calendar.getTime();
				Date now = new Date();
				if(!now.after(compDate)){
					ajaxJson.setMsg("员工"+ fname + "["+ account + "]入职未满三个月，不能参加活动！");
					ajaxJson.setSuccess(false);
					return ajaxJson;
				}
				
				String company = user.getCompanyDomain();//从系统中获取公司信息
				String fcompany = teamList.getCompany().replaceAll(" ", "");//从列表中获取员工信息
				if((fcompany.replaceAll(" ", "")).equals("金铭")){
					if(!(company.equals("金铭"))&&!company.equals("售后")){//判断金铭公司，东莞市金铭电子有限公司
						ajaxJson.setMsg("您好，您所填写的员工:【"+account+","+name+"】公司：【"+fcompany+"】，与该员工所属公司：【"+company+"】不匹配，请查核确认！");
						ajaxJson.setSuccess(false);
						return ajaxJson;
					}
				}else if("金卓".equals(fcompany.replaceAll(" ", ""))){
					if(!(company.equals("金卓"))){//判断金卓公司，东莞金卓通信科技有限公司
						ajaxJson.setMsg("您好，您所填写的员工:【"+account+","+name+"】公司：【"+fcompany+"】，与该员工所属公司：【"+company+"】不匹配，请查核确认！");
						ajaxJson.setSuccess(false);
						return ajaxJson;
					}
				}else if("金立".equals(fcompany.replaceAll(" ", ""))){
					if(!(company.equals("金立"))&&!company.equals("电商事业部")){//判断金卓公司，东莞金卓通信科技有限公司
						ajaxJson.setMsg("您好，您所填写的员工:【"+account+","+name+"】公司：【"+fcompany+"】，与该员工所属公司：【"+company+"】不匹配，请查核确认！");
						ajaxJson.setSuccess(false);
						return ajaxJson;
					}
				}else if("金尚".equals(fcompany.replaceAll(" ", ""))){
					if(!(company.equals("金尚"))){//判断金卓公司，东莞金卓通信科技有限公司
						ajaxJson.setMsg("您好，您所填写的员工:【"+account+","+name+"】公司：【"+fcompany+"】，与该员工所属公司：【"+company+"】不匹配，请查核确认！");
						ajaxJson.setSuccess(false);
						return ajaxJson;
					}
				}else if("诚壹".equals(fcompany.replaceAll(" ", ""))){
					if(!(company.equals("诚壹"))){//判断金卓公司，东莞金卓通信科技有限公司
						ajaxJson.setMsg("您好，您所填写的员工:【"+account+","+name+"】公司：【"+fcompany+"】，与该员工所属公司：【"+company+"】不匹配，请查核确认！");
						ajaxJson.setSuccess(false);
						return ajaxJson;
					}
				}else{
					ajaxJson.setMsg("您好，您所填写的员工:【"+account+","+name+"】公司：【"+fcompany+"】，与该员工所属公司：【"+company+"】不匹配，请查核确认！");
					ajaxJson.setSuccess(false);
					return ajaxJson;
				}
			}
			
			if(count==users.size()){
				ajaxJson.setMsg("员工"
						+ teamList.getName().trim()
						+ "[" + account + "]不存在！");
				ajaxJson.setSuccess(false);
				return ajaxJson;
			}
			
			// 判断员工今年是否参加过旅游
			if ("1".equals(ifOffTraveled((String) teamList.getAccount().replaceAll(" ", "")))) {
				
				ajaxJson.setMsg("员工"
						+ teamList.getName() + "["
						+ teamList.getAccount() + "]今年的旅游申请在审核中！");
				ajaxJson.setSuccess(false);
				return ajaxJson;
			}else if("2".equals(ifOffTraveled((String) teamList.getAccount().replaceAll(" ", "")))){
				ajaxJson.setMsg("员工"
						+ teamList.getName() + "["
						+ teamList.getAccount() + "]今年已经参加过旅游！");
				ajaxJson.setSuccess(false);
				return ajaxJson;
			}
			// 判断员工是否入职已满三个月
			/*if (!processHelpService.isThreeMonth((String) teamList.getAccount())) {
				ajaxJson.setMsg("员工"
						+ processHelpService
								.getUserName((String) teamList.getAccount()) + "["
						+ teamList.getAccount() + "]入职未满三个月，不能参加活动！");
				ajaxJson.setSuccess(false);
				return ajaxJson;
			}*/
			map.put("remark", teamList.getRemark());
			rows.add(map);
		}
		ajaxJson.setObj(rows);
		//System.out.println("--------"+System.currentTimeMillis());
		return ajaxJson;
	}
	
	private String ifOffTraveled(String account){
		List<TeamActivitiesDetail> teamActivitiesDetails = teamActivitiesDetailService.getTeamActivitiesDetail(account);
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Date date=new Date();
		
		int year=0;
		int day=0;
		int count=0;
		
		String[] times=sdf.format(date).split("-");
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(date);
		year=calendar.get(Calendar.YEAR);
		if(3<=Integer.parseInt(times[1]) && Integer.parseInt(times[1])<=12){
			count=1;
			calendar.add(Calendar.YEAR, +1);
			calendar.set(Calendar.MONTH, 1);
			day=calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		}else{
			count=2;
			calendar.set(Calendar.MONTH, 1);
			day=calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		}
		
		String result="0";
		TeamActivitiesDetail team=null;
		String[] oldDate=null;
		if(teamActivitiesDetails != null && teamActivitiesDetails.size() > 0){
			team=teamActivitiesDetails.get(0);
			if(team.getTravelDate()!=null && !"".equals(team.getTravelDate())){
				oldDate=sdf.format(team.getTravelDate()).split("-");
			}
		}
		
		if(oldDate!=null && oldDate.length>0){
			if((count==1 && ((Integer.parseInt(oldDate[0])==year && Integer.parseInt(oldDate[1]) >=3) || 
					(Integer.parseInt(oldDate[0])==year+1 && (Integer.parseInt(oldDate[1])<2 || 
					(Integer.parseInt(oldDate[1])==2 && Integer.parseInt(oldDate[2])<=day))))) || 
					(count==2 && ((Integer.parseInt(oldDate[0])==year-1 && Integer.parseInt(oldDate[1]) >=3) || 
							(Integer.parseInt(oldDate[0])==year && (Integer.parseInt(oldDate[1])<2 || 
							(Integer.parseInt(oldDate[1])==2 && Integer.parseInt(oldDate[2])<=day)))))){
				if(team.getStatus()==0){
					result="1";//今年年度旅游在申请审核中
				}else{
					result="2";//已经参加过今年的年度旅游
				}
			}
		}
		return result;
	}
	
	//判断导入的数据中是否有重复数据
	 public String IsExistAlike(List<TeamActivitiesDetail> list){
		 String str ="success";
		  label:for(int index=0;index<list.size();index++){
				for(int y=index+1;y<list.size();y++){
					TeamActivitiesDetail current = list.get(index);
					TeamActivitiesDetail next = list.get(y);
					if(StringUtils.equals(current.getAccount(), next.getAccount())){
						str="员工"+current.getName()+"["+next.getAccount()+"]"+"被重复导入!";
						//str="员工"+processHelpService.getUserName(next.getAccount())+"["+next.getAccount()+"]"+"被重复导入！";
						break label;
					}
				}
			}
		  return str;
	  }
	 
	 
	 
	//判断导入的数据中是否有重复数据
		 public String IsRapExistAlike(List<RapActivititiesDetail> list){
			 String str ="success";
			  for(int index=0;index<list.size();index++){
					for(int y=index+1;y<list.size();y++){
						RapActivititiesDetail current = list.get(index);
						RapActivititiesDetail next = list.get(y);
						if(StringUtils.equals(current.getAccount(), next.getAccount())){
							str="员工"+current.getName()+"["+next.getAccount()+"]"+"被重复导入!";
							//str="员工"+processHelpService.getUserName(next.getAccount())+"["+next.getAccount()+"]"+"被重复导入！";
						}
					}
				}
			  return str;
		  }
	 //判断导入的数据中是否存在空数据
	public Object IsNotNull(TeamActivitiesDetail v, Map<String, Object> map)
			throws IllegalArgumentException, InvocationTargetException,
			IllegalAccessException {
		
		Iterator<String> iter = map.keySet().iterator();
		while (iter.hasNext()) {
			String key = iter.next();
			if (StringUtils.isEmpty(ReflectionUtils.invokeGetterMethod(v, key)
					.toString().trim())) {
				return map.get(key);
			}
		}
		return -1;
	}
     
	
	
	 //判断导入的数据中是否存在空数据
		public Object IsParNotNull(RapActivititiesDetail v, Map<String, Object> map)
				throws IllegalArgumentException, InvocationTargetException,
				IllegalAccessException {
			
			Iterator<String> iter = map.keySet().iterator();
			while (iter.hasNext()) {
				String key = iter.next();
				if (StringUtils.isEmpty(ReflectionUtils.invokeGetterMethod(v, key)
						.toString().trim())&&(!key.equals("remarks"))) {
					return map.get(key);
				}
			}
			return -1;
		}
		
	// 季度部门活动名单导入
	@RequestMapping("/imporTeamList")
	@ResponseBody
	public AjaxJson imporTeamList(@RequestParam("files") MultipartFile files) throws Exception {
		AjaxJson ajaxJson = new AjaxJson();
		ajaxJson.setMsg("文件读取成功");
		
		List<HashMap<String, Object>> rows = new ArrayList<>();
		String [] fileds = new String[]{"number","company","department","account","name","entryDate"};
		List<TeamActivitiesDetail> list = null;
		try {
			list = ExcelReadUtil.ReadExcel(files.getInputStream(), fileds, TeamActivitiesDetail.class, 1);
		} catch (Exception e) {
			ajaxJson.setMsg(e.getMessage());
			ajaxJson.setSuccess(false);
			return ajaxJson;
		}
		if(list.size()>1){
			String str = IsExistAlike(list);
			if (!"success".equals(str)) {
				ajaxJson.setMsg(str);
				ajaxJson.setSuccess(false);
				return ajaxJson;
			}
		}
		int index = 0;
		
		StringBuilder accounts=new StringBuilder();
		for(TeamActivitiesDetail team:list){
			accounts.append(team.getAccount());
			accounts.append(",");
		}
		String empIds=accounts.toString();
		empIds=empIds.substring(0,empIds.length()-1);
		
		List<HrUserDto> users = processHelpService.getAllEmpInfo(empIds);//根据员工账号获取员工基本信息
		for(TeamActivitiesDetail teamList : list){
			HashMap<String, Object> map = new HashMap<>();
			index++;
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
			map.put("number", teamList.getNumber());
			map.put("company", teamList.getCompany());
			map.put("department", teamList.getDepartment());
			map.put("account", teamList.getAccount());
			map.put("name", teamList.getName());
			map.put("entryDate", sdf.format(teamList.getEntryDate()));
			Object flag = IsNotNull(teamList, map);
			if(!flag.equals(-1)){
				ajaxJson.setMsg("第"+index+"行数据不完整！");
				ajaxJson.setSuccess(false);
				return ajaxJson;
			}
			//HrUserDto user = processHelpService.getHrUser4Account((String) teamList.getAccount());//根据员工账号获取员工基本信息
			//Object fcode = processHelpService.getHrUser4Account((String) teamList.getAccount());//获取员工账号
			//String fname = processHelpService.getUserName((String) teamList.getAccount());
			String account = teamList.getAccount();
			
			int count=0;
			for(HrUserDto user :users){
				
				if ( user == null ) {
					ajaxJson.setMsg("员工"
							+ (processHelpService.getUserName(account) == null ? "编码": processHelpService.getUserName(account))
							+ "[" + teamList.getAccount() + "]不存在,请查核确认,若为已离职人员请在备注项中添加已离职人员信息！");
					ajaxJson.setSuccess(false);
					return ajaxJson;
				}
				if(user.getEmpId()==null||account==null)
					continue;
				if(user.getEmpId().intValue()!=Integer.parseInt(account)){
					count++;
					continue;
				}
				String fname = user.getName();//从系统中获取员工姓名
				String name = teamList.getName();//从excel表格中获取员工姓名
				if(!(teamList.getName().equals(fname))){
					ajaxJson.setMsg("您好，您所填写的员工姓名：【"+name+"】，与该员工编号：【"+account+"】，所对应的姓名：【"+fname+"】不匹配，请查核确认！");
					ajaxJson.setSuccess(false);
					return ajaxJson;
				}
				String company = user.getCompanyName();//从系统中获取公司信息
				String fcompany = teamList.getCompany();//从列表中获取员工信息
				if((fcompany.replaceAll(" ", "")).equals("金铭")){
					if(!(company.substring(3,5).equals(fcompany))){//判断金铭公司，东莞市金铭电子有限公司
						//System.out.println(company.substring(3,5));
						ajaxJson.setMsg("您好，您所填写的员工:【"+account+","+name+"】公司：【"+fcompany+"】，与该员工所属公司：【"+company+"】不匹配，请查核确认！");
						ajaxJson.setSuccess(false);
						return ajaxJson;
					}
				}else if("金卓".equals(fcompany.replaceAll(" ", ""))){
					if(!(company.substring(2,4).equals(fcompany))){//判断金卓公司，东莞金卓通信科技有限公司
						ajaxJson.setMsg("您好，您所填写的员工:【"+account+","+name+"】公司：【"+fcompany+"】，与该员工所属公司：【"+company+"】不匹配，请查核确认！");
						ajaxJson.setSuccess(false);
						return ajaxJson;
					}
				}else if("金立".equals(fcompany.replaceAll(" ", ""))){
					if(!(company.substring(3,5).equals(fcompany))){//判断金卓公司，东莞金卓通信科技有限公司
						ajaxJson.setMsg("您好，您所填写的员工:【"+account+","+name+"】公司：【"+fcompany+"】，与该员工所属公司：【"+company+"】不匹配，请查核确认！");
						ajaxJson.setSuccess(false);
						return ajaxJson;
					}
				}else{
					ajaxJson.setMsg("您好，您所填写的员工:【"+account+","+name+"】公司：【"+fcompany+"】，与该员工所属公司：【"+company+"】不匹配，请查核确认！");
					ajaxJson.setSuccess(false);
					return ajaxJson;
				}
				
				String fdate = sdf.format(user.getEntryDate());//从系统中获取入职日期 
				//Date date = teamList.getEntryDate();
				String ftime = sdf.format(teamList.getEntryDate());//获取导入电子表格中的入职日期
				if(ftime.compareTo(fdate)!=0){
					ajaxJson.setMsg("您好，您所填写的入职日期：【"+ftime+"】，与该员工编号：【"+account+"】，姓名：【"+fname+"】所对应的入职日期：【"+fdate+"】不匹配，请查核确认！");
					ajaxJson.setSuccess(false);
					return ajaxJson;
				}
			}
			
			if(count==users.size()){
				ajaxJson.setMsg("员工"
						+ teamList.getName().trim()
						+ "[" + account + "]不存在！");
				ajaxJson.setSuccess(false);
				return ajaxJson;
			}
			//map.put("leaveDate",sdf.format(teamList.getLeaveDate()));
			rows.add(map);
		}
		ajaxJson.setObj(rows);
		return ajaxJson;
	}
	
	
	// 部门活动已离职人员名单导入
		@RequestMapping("/imporleavePersons")
		@ResponseBody
		public AjaxJson imporleavePersons(@RequestParam("files") MultipartFile files) throws Exception {
			AjaxJson ajaxJson = new AjaxJson();
			ajaxJson.setMsg("文件读取成功");
		
			List<HashMap<String, Object>> rows = new ArrayList<>();
			String [] fileds = new String[]{"number","account","name","company","job","entryDate","leaveDate","cost"};
			List<TeamActivitiesDetail> list = null;
			try {
				list = ExcelReadUtil.ReadExcel(files.getInputStream(), fileds, TeamActivitiesDetail.class, 1);
			} catch (Exception e) {
				ajaxJson.setMsg(e.getMessage());
				ajaxJson.setSuccess(false);
				return ajaxJson;
			}
			if(list.size()>1){
				String str = IsExistAlike(list);
				if (!"success".equals(str)) {
					ajaxJson.setMsg(str);
					ajaxJson.setSuccess(false);
					return ajaxJson;
				}
			}
			int index = 0;
			//"number","account","name","job","entryDate","leaveDate","cost"
			for(TeamActivitiesDetail teamList : list){
				HashMap<String, Object> map = new HashMap<>();
				index++;
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
				map.put("number", teamList.getNumber());
				map.put("account", teamList.getAccount());
				map.put("company", teamList.getCompany());
				map.put("name", teamList.getName());
				map.put("job", teamList.getJob());
				map.put("entryDate", sdf.format(teamList.getEntryDate()));
				map.put("leaveDate",sdf.format(teamList.getLeaveDate()));
				map.put("cost", teamList.getCost());
				Object flag = IsNotNull(teamList, map);
				if(!flag.equals(-1)){
					ajaxJson.setMsg("第"+index+"行数据不完整！");
					ajaxJson.setSuccess(false);
					return ajaxJson;
				}
				/*Date  fLeaveDate= teamList.getLeaveDate();//获取从电子表格中的离职日期
				int month = fLeaveDate.getMonth()+1;
				System.out.println(fLeaveDate);
				Integer cost = teamList.getCost();
				System.out.println(cost);*/
				rows.add(map);
			}
			ajaxJson.setObj(rows);
			return ajaxJson;
		}
		
		//导入固定资产报废清单
		@RequestMapping("/imporFixedAssets.json")
		@ResponseBody
		public AjaxJson imporFixedAssets(@RequestParam("files") MultipartFile files) throws Exception {
			AjaxJson ajaxJson = new AjaxJson();
			ajaxJson.setMsg("文件读取成功");
		
			List<HashMap<String, Object>> rows = new ArrayList<>();
			String [] fileds = new String[]{"number","assetsNo","fault"};
			List<MFixedAssetsDetraction> list = null;
			try {
				list = ExcelReadUtil.ReadExcel(files.getInputStream(), fileds, MFixedAssetsDetraction.class, 1);
			} catch (Exception e) {
				ajaxJson.setMsg(e.getMessage());
				ajaxJson.setSuccess(false);
				return ajaxJson;
			}
			
			//判断故障说明是否为空
			for(int i=0;i<list.size();i++){
				String fixed=list.get(i).getFault().trim();
				if("".equals(fixed)){
					ajaxJson.setSuccess(false);
					ajaxJson.setMsg("第"+(i+2)+"行，第3列的值为空，请填写再上传!");
					return ajaxJson;
				}
			}
			
			//判断编号是否重复
			AjaxJson ajas=noIsOffRevert(list);
			if(!ajas.isSuccess()){
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg(Arrays.asList(ajas.getMsg().split(",")).toString()+"编号有重复，请修改再上传!");
				return ajaxJson;
			}
			
			//判断编号是否在申请中
			List<String> applyList=assetsNoisOffApply(list);
			if(applyList!=null&&applyList.size()>0){
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg(applyList.toString()+"编号的固定资产正在申请报废中!");
				return ajaxJson;
			}
			
			//判断编号在SAP是否存在或已报废
			String assetInfo=getAssetInfo(list);
			if(assetInfo!=null&&assetInfo.length()>0){
				if(assetInfo.indexOf("#")>0 && assetInfo.indexOf("@")==-1){
					ajaxJson.setSuccess(false);
					ajaxJson.setMsg("不存在"+Arrays.asList(assetInfo.split("#")).toString()+"编号的固定资产!");
					return ajaxJson;
				}else if(assetInfo.indexOf("#")==-1 && assetInfo.indexOf("@")>0){
					ajaxJson.setSuccess(false);
					ajaxJson.setMsg(Arrays.asList(assetInfo.split("@")).toString()+"编号的固定资产已报废，不可重新发起申请!");
					return ajaxJson;
				}else{
					ajaxJson.setSuccess(false);
					String info1=assetInfo.substring(0,assetInfo.lastIndexOf("#"));
					String info2=assetInfo.substring(assetInfo.lastIndexOf("#")+1,assetInfo.length());
					ajaxJson.setMsg("不存在"+Arrays.asList(info1.split("#")).toString()+"编号的固定资产；"+Arrays.asList(info2.split("@")).toString()+"编号的固定资产已报废，不可重新发起申请!");
					return ajaxJson;
				}
			}
			
			
			for(MFixedAssetsDetraction assets : list){
				Map<String,Object> param=getOneAsset(assets.getAssetsNo());
				
				HashMap<String, Object> map = new HashMap<>();
				map.put("number",assets.getNumber());
				map.put("assetsNo",assets.getAssetsNo());
				map.put("fault",assets.getFault());
				map.put("monad",param.get("GROES_B"));
				map.put("count",param.get("GROES_A"));
				map.put("type",param.get("TYPBZ"));
				map.put("repertoryNo",param.get("INVNR"));
				map.put("name",param.get("EQKTX"));
				map.put("intakeDate",param.get("ANSDT"));
				rows.add(map);
			}
			ajaxJson.setObj(rows);
			return ajaxJson;
		}
		
		public Map<String,Object> getOneAsset(String assetNo) throws Exception{
			Map<String,String> inputMap=new HashMap<String,String>();
			String[] exportVars="GROES_B_NEXT_GROES_A_NEXT_TYPBZ_NEXT_INVNR_NEXT_EQKTX_NEXT_ANSDT_NEXT_ANSWT_NEXT_STORT".split("_NEXT_");
			inputMap.put("I_TYPE","S");
			inputMap.put("I_EQUNR", assetNo);
			String str=processHelpService.getSapAssetsInfoForJson("ZHR_OA44",inputMap,"OT_EQUI",exportVars);
			return processHelpService.parseJsonToMap(str);
		}
		
		public String getAssetInfo(List<MFixedAssetsDetraction> assets) throws Exception{
			StringBuilder builder1=new StringBuilder();
			StringBuilder builder2=new StringBuilder();
			Map<String,String> inputMap=new HashMap<String,String>();
			String[] exportVars="GROES_B_NEXT_GROES_A_NEXT_TYPBZ_NEXT_INVNR_NEXT_EQKTX_NEXT_ANSDT_NEXT_ANSWT_NEXT_STORT".split("_NEXT_");
			for(MFixedAssetsDetraction asset:assets){
				inputMap.put("I_TYPE","S");
				inputMap.put("I_EQUNR", asset.getAssetsNo());
				String str1=processHelpService.getSapAssetsInfoForJson("ZHR_OA44",inputMap,"OT_EQUI",exportVars);
				if(str1==null||"".equals(str1)){
					builder1.append(asset.getAssetsNo()+"#");
				}else if(processHelpService.parseJsonToMap(str1).get("STORT").equals("A3")){
					builder2.append(asset.getAssetsNo()+"@");
				}
			}
			return builder1.toString()+builder2.toString();
		}
		
		public List<String> assetsNoisOffApply(List<MFixedAssetsDetraction> assets){
			List<String> list1=new ArrayList<String>();
			for(MFixedAssetsDetraction asset:assets){
				list1.add(asset.getAssetsNo());
			}
			List<String> list2=new ArrayList<String>();
			List<String> strs=activitiHelpService.assetsNoisOffApply();
			boolean flag=false;
			for(String str:strs){
				if(!"".equals(str)&&str!=null){
					List<Map<String,Object>> list=(List<Map<String,Object>>)processHelpService.parseJsonToMap(str).get("accessoryData");
					for(Map<String,Object> map:list){
						String assetNo=(String)map.get("assetsNo");
						list2.add(assetNo);
					}
				}
			}
			list1.retainAll(list2);
			return list1;
		}
		
		public AjaxJson noIsOffRevert(List<MFixedAssetsDetraction> assets){
			AjaxJson ajaxJson = new AjaxJson();
			StringBuilder builder=new StringBuilder("");
			
			Map<String,Integer> map=new HashMap<String,Integer>();
			
			for(MFixedAssetsDetraction asset:assets){
				if(map.keySet().contains(asset.getAssetsNo())){
					map.put(asset.getAssetsNo(),map.get(asset.getAssetsNo())+1);
				}else{
					map.put(asset.getAssetsNo(),1);
				}
			}
			
			Set<Entry<String,Integer>> entrys=map.entrySet();
			
			for(Entry<String,Integer> entry:entrys){
				if(entry.getValue()>=2){
					ajaxJson.setSuccess(false);
					builder.append(entry.getKey()+",");
				}
			}
			
			ajaxJson.setMsg(builder.toString());
			return ajaxJson;
		}
		//导入非生产物资清单
		@RequestMapping("/importNonProduc.json")
		@ResponseBody
		public AjaxJson importNonProduc(@RequestParam("files") MultipartFile files) throws Exception{
			AjaxJson ajaxJson = new AjaxJson();
			ajaxJson.setMsg("文件读取成功");
		
			List<HashMap<String, Object>> rows = new ArrayList<>();
			String [] fileds = new String[]{"number","courseAllot","stockNumber","stockNameAndStandard","applyNumber","monad","factory","reckoning","costCenter",
					"unitPrice","deliveryDate","procurementSection","itemGroup","applyName","remark"};
			List<NonProductions> list = null;
			try {
				list = ExcelReadUtil.ReadExcel(files.getInputStream(), fileds, NonProductions.class, 2);
			} catch (Exception e) {
				ajaxJson.setMsg(e.getMessage());
				ajaxJson.setSuccess(false);
				return ajaxJson;
			}
			Date date=new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Pattern pattern = Pattern.compile("^[0-9]+(.[0-9]{0,3})?$"); 
			DecimalFormat df3  = new DecimalFormat("##.000");
			int count=2;
			for(NonProductions nonProdus : list){
				count++;
				HashMap<String, Object> map = new HashMap<>();
				if(!"".equals(nonProdus.getCourseAllot().trim())&&!"K".equals(nonProdus.getCourseAllot().trim())){
					ajaxJson.setMsg("第"+count+"行,第2列需要的值是空或K!");
					ajaxJson.setSuccess(false);
					return ajaxJson;
				}
				if("".equals(nonProdus.getCourseAllot().trim())){
					if("".equals(nonProdus.getStockNumber().trim())){
						ajaxJson.setMsg("第"+count+"行,科目分配为空，第3列为必填项!");
						ajaxJson.setSuccess(false);
						return ajaxJson;
					}
				}else{
					if("".equals(nonProdus.getReckoning().trim())){
						ajaxJson.setMsg("第"+count+"行,科目分配为K,第8列为必填项!");
						ajaxJson.setSuccess(false);
						return ajaxJson;
					}
					if("".equals(nonProdus.getCostCenter().trim())){
						ajaxJson.setMsg("第"+count+"行,科目分配为K,第9列为必填项!");
						ajaxJson.setSuccess(false);
						return ajaxJson;
					}
				}
				if(!"".equals(nonProdus.getStockNumber().trim())){
					if(!pattern.matcher(nonProdus.getStockNumber()).matches()){
						ajaxJson.setMsg("第"+count+"行,第3列需要的类型是数字，并且为9位数!");
						ajaxJson.setSuccess(false);
						return ajaxJson;
					}
					if(nonProdus.getStockNumber().length()!=9){
						ajaxJson.setMsg("第"+count+"行,第3列需要的类型是数字，并且为9位数!");
						ajaxJson.setSuccess(false);
						return ajaxJson;
					}
				}
				if(!"2000".equals(nonProdus.getFactory())&&!"3000".equals(nonProdus.getFactory())){
					ajaxJson.setMsg("第"+count+"行,第7列工厂字段只可以填写2000或3000!");
					ajaxJson.setSuccess(false);
					return ajaxJson;
				}
				if("".equals(nonProdus.getStockNameAndStandard().trim())){
					ajaxJson.setMsg("第"+count+"行,第4列物料名称字段不可为空!");
					ajaxJson.setSuccess(false);
					return ajaxJson;
				}else if(nonProdus.getStockNameAndStandard().trim().length()>40){
					ajaxJson.setMsg("第"+count+"行,第4列物料名称字段不可超过40个字符!");
					ajaxJson.setSuccess(false);
					return ajaxJson;
				}
				if("".equals(nonProdus.getMonad().trim())){
					ajaxJson.setMsg("第"+count+"行,第6列单位字段不可为空!");
					ajaxJson.setSuccess(false);
					return ajaxJson;
				}else if(nonProdus.getMonad().trim().length()>3){
					ajaxJson.setMsg("第"+count+"行,第6列单位字段不可超过3个字符!");
					ajaxJson.setSuccess(false);
					return ajaxJson;
				}
				if(!pattern.matcher(nonProdus.getApplyNumber()).matches()){
					ajaxJson.setMsg("第"+count+"行,第5列需要的类型是数字!");
					ajaxJson.setSuccess(false);
					return ajaxJson;
				}
				if(!"".equals(nonProdus.getReckoning().trim())){
					if(!pattern.matcher(nonProdus.getReckoning().trim()).matches()||nonProdus.getReckoning().trim().length()!=10){
						ajaxJson.setMsg("第"+count+"行,第8列需要的类型是数字且长度为10!");
						ajaxJson.setSuccess(false);
						return ajaxJson;
					}
				}
				if(!"".equals(nonProdus.getCostCenter().trim())){
					if(!pattern.matcher(nonProdus.getCostCenter().trim()).matches()||nonProdus.getCostCenter().trim().length()!=8){
						ajaxJson.setMsg("第"+count+"行,第9列需要的类型是数字且长度为8!");
						ajaxJson.setSuccess(false);
						return ajaxJson;
					}
				}
				if(!pattern.matcher(nonProdus.getUnitPrice()).matches()){
					ajaxJson.setMsg("第"+count+"行,第10列需要的类型是数字!");
					ajaxJson.setSuccess(false);
					return ajaxJson;
				}
				/*if(!pattern.matcher(nonProdus.getAmountInTotal()).matches()){
					ajaxJson.setMsg("第"+count+"行,第11列需要的类型是数字!");
					ajaxJson.setSuccess(false);
					return ajaxJson;
				}*/
				if(nonProdus.getDeliveryDate().getTime()<(date.getTime()-24*60*60*1000)){
					ajaxJson.setMsg("第"+count+"行,第11列日期不可以小于当前日期!");
					ajaxJson.setSuccess(false);
					return ajaxJson;
				}
				if(!pattern.matcher(nonProdus.getProcurementSection()).matches()||nonProdus.getProcurementSection().length()!=3){
					ajaxJson.setMsg("第"+count+"行,第12列需要的类型是数字,长度为3位!");
					ajaxJson.setSuccess(false);
					return ajaxJson;
				}
				if(!pattern.matcher(nonProdus.getItemGroup()).matches()||nonProdus.getItemGroup().length()!=4){
					ajaxJson.setMsg("第"+count+"行,第13列需要的类型是数字,长度为4位!");
					ajaxJson.setSuccess(false);
					return ajaxJson;
				}
				if("".equals(nonProdus.getApplyName().trim())||pattern.matcher(nonProdus.getApplyName().trim()).matches()){
					ajaxJson.setMsg("第"+count+"行,第14列不可为空且为字符!");
					ajaxJson.setSuccess(false);
					return ajaxJson;
				}
				double totalCost=(Integer.parseInt(nonProdus.getApplyNumber()))*(Double.parseDouble(nonProdus.getUnitPrice()));
				map.put("number", nonProdus.getNumber());
				map.put("courseAllot",nonProdus.getCourseAllot());
				map.put("stockNumber",nonProdus.getStockNumber());
				map.put("stockNameAndStandard",nonProdus.getStockNameAndStandard());
				map.put("applyNumber",nonProdus.getApplyNumber());
				map.put("monad",nonProdus.getMonad());
				map.put("factory",nonProdus.getFactory());
				map.put("reckoning",nonProdus.getReckoning());
				map.put("costCenter",nonProdus.getCostCenter());
				map.put("unitPrice",nonProdus.getUnitPrice());
				map.put("amountInTotal",df3.format(totalCost));
				map.put("deliveryDate",sdf.format(nonProdus.getDeliveryDate()));
				map.put("procurementSection",nonProdus.getProcurementSection());
				map.put("itemGroup",nonProdus.getItemGroup());
				map.put("applyName",nonProdus.getApplyName());
				map.put("remark",nonProdus.getRemark()==null?"":nonProdus.getRemark());
				
				rows.add(map);
			}
			ajaxJson.setObj(rows);
			return ajaxJson;
		}
		
		//导入设备物料清单
		@RequestMapping("/importEquipmentMaintain.json")
		@ResponseBody
		public AjaxJson importEquipmentMaintain(@RequestParam("files") MultipartFile files) throws Exception{
			AjaxJson ajaxJson = new AjaxJson();
			ajaxJson.setMsg("文件读取成功");
				
			List<HashMap<String, Object>> rows = new ArrayList<>();
			String [] fileds = new String[]{"number","courseAllot","stockNumber","stockNameAndStandard","applyNumber","monad","reckoning","costCenter",
				"unitPrice","deliveryDate","procurementSection","itemGroup","applyName","remark"};
			List<NonProductions> list = null;
			try {
				list = ExcelReadUtil.ReadExcel(files.getInputStream(), fileds, NonProductions.class, 2);
			} catch (Exception e) {
				ajaxJson.setMsg(e.getMessage());
				ajaxJson.setSuccess(false);
				return ajaxJson;
			}
			Date date=new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Pattern pattern = Pattern.compile("^[0-9]+(.[0-9]{0,3})?$"); 
			DecimalFormat df3  = new DecimalFormat("##.000");
			int count=2;
			for(NonProductions nonProdus : list){
				count++;
				HashMap<String, Object> map = new HashMap<>();
				if(!"".equals(nonProdus.getCourseAllot().trim())&&!"K".equals(nonProdus.getCourseAllot().trim())){
					ajaxJson.setMsg("第"+count+"行,第2列需要的值是空或K!");
					ajaxJson.setSuccess(false);
					return ajaxJson;
				}
				if("".equals(nonProdus.getCourseAllot().trim())){
					if("".equals(nonProdus.getStockNumber().trim())){
						ajaxJson.setMsg("第"+count+"行,科目分配为空，第3列为必填项!");
						ajaxJson.setSuccess(false);
						return ajaxJson;
					}
				}else{
					if("".equals(nonProdus.getReckoning().trim())){
						ajaxJson.setMsg("第"+count+"行,科目分配为K,第7列为必填项!");
						ajaxJson.setSuccess(false);
						return ajaxJson;
					}
					if("".equals(nonProdus.getCostCenter().trim())){
						ajaxJson.setMsg("第"+count+"行,科目分配为K,第8列为必填项!");
						ajaxJson.setSuccess(false);
						return ajaxJson;
					}
				}
				if(!"".equals(nonProdus.getStockNumber().trim())){
					if(!pattern.matcher(nonProdus.getStockNumber()).matches()){
						ajaxJson.setMsg("第"+count+"行,第3列需要的类型是数字，并且为9位数!");
						ajaxJson.setSuccess(false);
						return ajaxJson;
					}
					if(nonProdus.getStockNumber().length()!=9){
						ajaxJson.setMsg("第"+count+"行,第3列需要的类型是数字，并且为9位数!");
						ajaxJson.setSuccess(false);
						return ajaxJson;
					}
				}
				if("".equals(nonProdus.getStockNameAndStandard().trim())){
					ajaxJson.setMsg("第"+count+"行,第4列物料名称字段不可为空!");
					ajaxJson.setSuccess(false);
					return ajaxJson;
				}else if(nonProdus.getStockNameAndStandard().trim().length()>40){
					ajaxJson.setMsg("第"+count+"行,第4列物料名称字段不可超过40个字符!");
					ajaxJson.setSuccess(false);
					return ajaxJson;
				}
				if("".equals(nonProdus.getMonad().trim())){
					ajaxJson.setMsg("第"+count+"行,第6列单位字段不可为空!");
					ajaxJson.setSuccess(false);
					return ajaxJson;
				}else if(nonProdus.getMonad().trim().length()>3){
					ajaxJson.setMsg("第"+count+"行,第6列单位字段不可超过3个字符!");
					ajaxJson.setSuccess(false);
					return ajaxJson;
				}
				if(!pattern.matcher(nonProdus.getApplyNumber()).matches()){
					ajaxJson.setMsg("第"+count+"行,第5列需要的类型是数字!");
					ajaxJson.setSuccess(false);
					return ajaxJson;
				}
				if(!"".equals(nonProdus.getReckoning().trim())){
					if(!pattern.matcher(nonProdus.getReckoning().trim()).matches()||nonProdus.getReckoning().trim().length()!=10){
						ajaxJson.setMsg("第"+count+"行,第7列需要的类型是数字且长度为10!");
						ajaxJson.setSuccess(false);
						return ajaxJson;
					}
				}
				if(!"".equals(nonProdus.getCostCenter().trim())){
					if(!pattern.matcher(nonProdus.getCostCenter().trim()).matches()||nonProdus.getCostCenter().trim().length()!=8){
						ajaxJson.setMsg("第"+count+"行,第8列需要的类型是数字且长度为8!");
						ajaxJson.setSuccess(false);
						return ajaxJson;
					}
				}
				if(!pattern.matcher(nonProdus.getUnitPrice()).matches()){
					ajaxJson.setMsg("第"+count+"行,第9列需要的类型是数字!");
					ajaxJson.setSuccess(false);
					return ajaxJson;
				}
				/*if(!pattern.matcher(nonProdus.getAmountInTotal()).matches()){
					ajaxJson.setMsg("第"+count+"行,第10列需要的类型是数字!");
					ajaxJson.setSuccess(false);
					return ajaxJson;
				}*/
				if(nonProdus.getDeliveryDate().getTime()<(date.getTime()-24*60*60*1)){
					ajaxJson.setMsg("第"+count+"行,第10列日期不可以小于当前日期!");
					ajaxJson.setSuccess(false);
					return ajaxJson;
				}
				if(!pattern.matcher(nonProdus.getProcurementSection()).matches()||nonProdus.getProcurementSection().length()!=3){
					ajaxJson.setMsg("第"+count+"行,第11列需要的类型是数字,长度为3位!");
					ajaxJson.setSuccess(false);
					return ajaxJson;
				}
				if(!pattern.matcher(nonProdus.getItemGroup()).matches()||nonProdus.getItemGroup().length()!=4){
					ajaxJson.setMsg("第"+count+"行,第12列需要的类型是数字,长度为4位!");
					ajaxJson.setSuccess(false);
					return ajaxJson;
				}
				if("".equals(nonProdus.getApplyName().trim())||pattern.matcher(nonProdus.getApplyName().trim()).matches()){
					ajaxJson.setMsg("第"+count+"行,第13列不可为空且为字符!");
					ajaxJson.setSuccess(false);
					return ajaxJson;
				}
				double totalCost=(Integer.parseInt(nonProdus.getApplyNumber()))*(Double.parseDouble(nonProdus.getUnitPrice()));
				map.put("number", nonProdus.getNumber());
				map.put("courseAllot",nonProdus.getCourseAllot());
				map.put("stockNumber",nonProdus.getStockNumber());
				map.put("stockNameAndStandard",nonProdus.getStockNameAndStandard());
				map.put("applyNumber",nonProdus.getApplyNumber());
				map.put("monad",nonProdus.getMonad());
				map.put("reckoning",nonProdus.getReckoning());
				map.put("costCenter",nonProdus.getCostCenter());
				map.put("unitPrice",nonProdus.getUnitPrice());
				map.put("amountInTotal",df3.format(totalCost));
				map.put("deliveryDate",sdf.format(nonProdus.getDeliveryDate()));
				map.put("procurementSection",nonProdus.getProcurementSection());
				map.put("itemGroup",nonProdus.getItemGroup());
				map.put("applyName",nonProdus.getApplyName());
				map.put("remark",nonProdus.getRemark()==null?"":nonProdus.getRemark());
						
				rows.add(map);
			}
			ajaxJson.setObj(rows);
			return ajaxJson;
		}
		
		// 导入培训记录表
			@RequestMapping("/importTraining.json")
			@ResponseBody
			public AjaxJson importTraining(@RequestParam("files") MultipartFile files) throws Exception {
				AjaxJson ajaxJson = new AjaxJson();
				ajaxJson.setMsg("文件读取成功");
			
				List<HashMap<String, Object>> rows = new ArrayList<>();
				String [] fileds = new String[]{"number","content","date","hour","teacher","type","result"};
				List<Training> list = null;
				try {
					list = ExcelReadUtil.ReadExcelForTraining(files.getInputStream(), fileds, Training.class, 2);
				} catch (Exception e) {
				}

				int index = 0;
				//"number","content","date","hour","teacher","type","result"
				
				
				for(Training trainList : list){
					HashMap<String, Object> map = new HashMap<>();
					index++;
					map.put("number", trainList.getNumber());
					map.put("content", trainList.getContent());
					map.put("date", trainList.getDate());
					map.put("hour", trainList.getHour());
					map.put("teacher", trainList.getTeacher());
					map.put("type",trainList.getType());
					map.put("result", trainList.getResult());
					
					rows.add(map);
				}
				ajaxJson.setObj(rows);
				return ajaxJson;
			}
			
			// 导入金立培训计划表
			@RequestMapping("/importTrainPlan")
			@ResponseBody
			public AjaxJson importTrainPlan(@RequestParam("files") MultipartFile files) throws Exception {
					AjaxJson ajaxJson = new AjaxJson();
					ajaxJson.setMsg("文件读取成功");
						
			List<HashMap<String, Object>> rows = new ArrayList<>();
				String [] fileds = new String[]{"number","content","date","type","result"};
				List<Training> list = null;
					try {
						list = ExcelReadUtil.ReadExcelForTraining(files.getInputStream(), fileds, Training.class, 2);
					} catch (Exception e) {
						
					}

				int index = 0;
				//"number","content","date","hour","teacher","type","result"
							
							
				for(Training trainList : list){
				HashMap<String, Object> map = new HashMap<>();
					index++;
					map.put("number", trainList.getNumber());//序号
					map.put("content", trainList.getContent());//培训内容
					map.put("date", trainList.getDate());//培训时间
					map.put("type",trainList.getType());//培训目的
					map.put("result", trainList.getResult());//备注
								
					rows.add(map);
				}
				ajaxJson.setObj(rows);
				return ajaxJson;
			}
			
			// 外训活动导入
			@RequestMapping("/trainingList.html")
			@ResponseBody
			public AjaxJson trainingList(@RequestParam("files") MultipartFile files) throws Exception{
				//System.out.println("--------"+System.currentTimeMillis());
				
				AjaxJson ajaxJson = new AjaxJson();
				ajaxJson.setMsg("文件读取成功");
				List<HashMap<String, Object>> rows = new ArrayList<>();
				
				String [] fileds = new String[]{"number","department","job","account","name","remark"};
				List<TeamActivitiesDetail> list = null;
				try {
					list = ExcelReadUtil.ReadExcel(files.getInputStream(), fileds, TeamActivitiesDetail.class, 1);
				} catch (Exception e) {
					ajaxJson.setMsg(e.getMessage());
					ajaxJson.setSuccess(false);
					return ajaxJson;
				}
			
				//校验数据
				if(list.size()>1){
					String str = IsExistAlike(list);
					if (!"success".equals(str)) {
						ajaxJson.setMsg(str);
						ajaxJson.setSuccess(false);
						return ajaxJson;
					}
				}
				int index = 0;
				
				StringBuilder accounts=new StringBuilder();
				for(TeamActivitiesDetail team:list){
					accounts.append(team.getAccount());
					accounts.append(",");
				}
				String empIds=accounts.toString();
				empIds=empIds.substring(0,empIds.length()-1);
				
				List<HrUserDto> users = processHelpService.getAllEmpInfo(empIds);//根据员工账号获取员工基本信息
				
				for(TeamActivitiesDetail teamList : list){
					HashMap<String, Object> map = new HashMap<>();
					index++;
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
					map.put("number", teamList.getNumber());
			     	map.put("department", teamList.getDepartment().trim());
					map.put("name", teamList.getName().trim());
					map.put("account", teamList.getAccount());
					map.put("job", teamList.getJob().trim());
					
					Object flag = IsNotNull(teamList, map);
					if(!flag.equals(-1)){
						ajaxJson.setMsg("第"+index+"行数据不完整！");
						ajaxJson.setSuccess(false);
						return ajaxJson;
					}
					String account = teamList.getAccount().trim();
					int count=0;
					for(HrUserDto user :users){
						if (user == null) {
							ajaxJson.setMsg("员工"
									+ (processHelpService.getUserName(account) == null ? "编码": processHelpService.getUserName(account))
									+ "[" + account + "]不存在！");
							ajaxJson.setSuccess(false);
							return ajaxJson;
						}
						if(user.getEmpId()==null||account==null)
							continue;
						if(user.getEmpId().intValue()!=Integer.parseInt(account)){
							count++;
							continue;
						}
					/*	String fname = user.getName().trim();//从系统中获取员工姓名
						String name= teamList.getName().replaceAll(" ", "");
						if(!(name.equals(fname))){
							ajaxJson.setMsg("您好，您所填写的员工姓名：【"+name+"】，与该员工编号：【"+account+"】，所对应的姓名：【"+fname+"】不匹配，请查核确认！");
							ajaxJson.setSuccess(false);
							return ajaxJson;
						}
						String company = user.getCompanyName();//从系统中获取公司信息
						String fcompany = teamList.getCompany().replaceAll(" ", "");//从列表中获取员工信息
						if("金立".equals(fcompany.replaceAll(" ", ""))){
							if(!(company.substring(3,5).equals(fcompany))){//判断金卓公司，东莞金卓通信科技有限公司
								ajaxJson.setMsg("您好，您所填写的员工:【"+account+","+name+"】公司：【"+fcompany+"】，与该员工所属公司：【"+company+"】不匹配，请查核确认！");
								ajaxJson.setSuccess(false);
								return ajaxJson;
							}
						}else if("海外事业部".equals(fcompany.replaceAll(" ", ""))){
							if(!(company.trim().equals(fcompany))){//判断金卓公司，东莞金卓通信科技有限公司
								ajaxJson.setMsg("您好，您所填写的员工:【"+account+","+name+"】公司：【"+fcompany+"】，与该员工所属公司：【"+company+"】不匹配，请查核确认！");
								ajaxJson.setSuccess(false);
								return ajaxJson;
							}
						}else{
							ajaxJson.setMsg("您好，您所填写的员工:【"+account+","+name+"】公司：【"+fcompany+"】，与该员工所属公司：【"+company+"】不匹配，请查核确认！");
							ajaxJson.setSuccess(false);
							return ajaxJson;
						}*/
					}
					if(count==users.size()){
						ajaxJson.setMsg("员工"
								+ teamList.getName().trim()
								+ "[" + account + "]不存在！");
						ajaxJson.setSuccess(false);
						return ajaxJson;
					}
					map.put("remark", teamList.getRemark());
					rows.add(map);
				}
				ajaxJson.setObj(rows);
				//System.out.println("--------"+System.currentTimeMillis());
				return ajaxJson;
			}
		   //判断是否当前月
			public  boolean checkMonth(String time){
				boolean bl = true;
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
				
					try {
						
						String now = sdf.format(new Date());//当前时间  
						if(Integer.parseInt(now.split("-")[2])<=10){
							Date date = sdf.parse(now);
						    Calendar calendar = Calendar.getInstance();
							calendar.setTime(date);
							calendar.add(Calendar.MONTH, -1);
							Date compDate = calendar.getTime();
							String lastDate=sdf.format(compDate);
							if(time.equals(now.substring(0,7))||time.equals(lastDate.substring(0,7))){
								return true;
							}
						}else {
							if(time.equals(now.substring(0,7))){
								return true;
							}
						}
					
					/*	String param = sdf.format(date);**////参数时间  
						
						/*bl = param.equals(now)?true:false;*/
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
				   
				return bl;
			}
			
			
			// 奖惩活动名单导入
			@RequestMapping("/rewardandpunishUpload.html")
			@ResponseBody
			public AjaxJson rewardandpunishUpload(@RequestParam("files") MultipartFile files) {
				//System.out.println("--------"+System.currentTimeMillis());
			AjaxJson ajaxJson = new AjaxJson();
			try{
				ajaxJson.setMsg("文件读取成功");
				List<HashMap<String, Object>> rows = new ArrayList<>();
				
				String [] fileds = new String[]{"number","account","name","department","job","RapDate","applyAccount","applyName","applyDepa","sort","rank","cause","score","remarks"};
				List<RapActivititiesDetail> list = null;
				try {
					list = ExcelReadUtil.ReadExcel(files.getInputStream(), fileds, RapActivititiesDetail.class, 1);
				} catch (Exception e) {
					ajaxJson.setMsg(e.getMessage());
					ajaxJson.setSuccess(false);
					return ajaxJson;
				}
				Map<String, String>rapInfoMap=new HashMap<String, String>();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
				for(int i = 0; i < list.size() ;i++){
					rapInfoMap.put("I_ZJCLB", list.get(i).getSort());
					rapInfoMap.put("I_ZJCYY", list.get(i).getCause());
					rapInfoMap.put("I_ZJCFS", String.valueOf(list.get(i).getScore()));
					rapInfoMap.put("I_ZJCJE", String.valueOf(list.get(i).getScore()*5));
					rapInfoMap.put("I_BEGDA", sdf.format(list.get(i).getRapDate()));
					rapInfoMap.put("I_PERNR", list.get(i).getAccount());
					if(rapInforService.query(rapInfoMap)>0){
						ajaxJson.setMsg("第"+(i+2)+"行数据已经存入SAP或在审核中");
						ajaxJson.setSuccess(false);
						return ajaxJson;
					}
				}
				
				Set<Integer>set=new HashSet<Integer>();
				for (int i = 0; i < list.size() - 1;i++)
		        {
		            for (int j = i + 1; j < list.size(); j++)
		            {
		            	
						if(list.get(i).getNumber()==list.get(j).getNumber())
		                		{
		                	set.add(i+2);
		                	set.add(j+2);
		                }
		            }
		        }
				String str="";
				for(Integer i:set){
					str+=i+",";
				}
				if(StringUtils.isNotEmpty(str)){
					ajaxJson.setMsg(str+"行序号出现重复");
					ajaxJson.setSuccess(false);
					return ajaxJson;
				}
				
				
				//判断编号是否重复
				AjaxJson ajas=noIsOffRap(list);
				if(!ajas.isSuccess()){
					ajaxJson.setSuccess(false);
					ajaxJson.setMsg(Arrays.asList(ajas.getMsg()).toString()+"请修改再上传!");
					return ajaxJson;
				}
				
				
				for (int i = 0; i < list.size() - 1;i++)
		        {
		            for (int j = i + 1; j < list.size(); j++)
		            {
		            	
						if(list.get(i).getSort().equals(list.get(j).getSort())&&
		                		list.get(i).getCause().equals(list.get(j).getCause())&&
		                		list.get(i).getScore().equals(list.get(j).getScore())&&
		                		list.get(i).getAccount().equals(list.get(j).getAccount())&&
		                		checkDate(list.get(i).getRapDate(),list.get(j).getRapDate()))
		                		{
		                	
		                	
		                	set.add(i+2);
		                	set.add(j+2);
		                }
		            }
		        }
				for(Integer i:set){
					str+=i+",";
				}
				if(StringUtils.isNotEmpty(str)){
					ajaxJson.setMsg(str+"行数据出现重复");
					ajaxJson.setSuccess(false);
					return ajaxJson;
				}
				
				//校验数据
				/*if(list.size()>1){
					String str = IsRapExistAlike(list);
					if (!"success".equals(str)) {
						ajaxJson.setMsg(str);
						ajaxJson.setSuccess(false);
						return ajaxJson;
					}
				}*/
				int index = 0;
				
				StringBuilder accounts=new StringBuilder();
				StringBuilder appAccounts=new StringBuilder();
				for(RapActivititiesDetail team:list){
					accounts.append(team.getAccount());
					accounts.append(",");
					appAccounts.append(team.getApplyAccount());
					appAccounts.append(",");
				}
				String empIds=accounts.toString();
				empIds=empIds.substring(0,empIds.length()-1);
				
				
				String appEmpIds=appAccounts.toString();
				appEmpIds=appEmpIds.substring(0,appEmpIds.length()-1);
				
				List<HrUserDto> users = processHelpService.getAllEmpInfo(empIds);//根据员工账号获取员工基本信息
				
				List<HrUserDto> users2 = processHelpService.getAllEmpInfo(appEmpIds);//根据员工账号获取员工基本信息
				
				for(RapActivititiesDetail teamList : list){
					HashMap<String, Object> map = new HashMap<>();
					index++;
					map.put("number", teamList.getNumber());
					map.put("account", teamList.getAccount().trim());
					map.put("name", teamList.getName().trim());
					map.put("department", teamList.getDepartment().trim());
					map.put("job", teamList.getJob().trim());
					map.put("rapDate", teamList.getRapDate());
					map.put("applyAccount", teamList.getApplyAccount().trim());
					map.put("applyName", teamList.getApplyName().trim());
					map.put("applyDepa", teamList.getApplyDepa().trim());
					map.put("sort", teamList.getSort());
					map.put("rank", teamList.getRank().trim());
					map.put("cause", teamList.getCause().trim());
					map.put("score", teamList.getScore());
					map.put("remarks", teamList.getRemarks());
					Object flag;
					try {
						flag = IsParNotNull(teamList, map);
						if(!flag.equals(-1)){
							ajaxJson.setMsg("第"+index+"行数据不完整！");
							ajaxJson.setSuccess(false);
							return ajaxJson;
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					map.put("money", teamList.getScore()*5);
					String account = teamList.getAccount().trim();
					
					String appAccount = teamList.getApplyAccount().trim();
					int count=0;
					int count2=0;
					for(HrUserDto user :users){
						if (user == null) {
							ajaxJson.setMsg("员工"
									+ (processHelpService.getUserName(account) == null ? "编码": processHelpService.getUserName(account))
									+ "[" + account + "]不存在！");
							ajaxJson.setSuccess(false);
							return ajaxJson;
						}
						if(user.getEmpId()==null||account==null)
							continue;
						int accountInt = 0;
						try{
							accountInt = Integer.parseInt(account);
						}catch(Exception e){
							ajaxJson.setMsg("第"+index+"行的员工编号数据存在问题！");
							ajaxJson.setSuccess(false);
							return ajaxJson;
						}
						if(user.getEmpId().intValue()!=accountInt){
							count++;
							continue;
						}
						String fname = user.getName().trim();//从系统中获取员工姓名
						String name= teamList.getName().replaceAll(" ", "");
						if(!(name.equals(fname))){
							ajaxJson.setMsg("您好，您所填写的员工姓名：【"+name+"】，与该员工编号：【"+account+"】，所对应的姓名：【"+fname+"】不匹配，请查核确认！");
							ajaxJson.setSuccess(false);
							return ajaxJson;
						}
						if(!user.getCompanyName().contains("金卓") && !user.getCompanyName().contains("金铭")){
							ajaxJson.setMsg("您好，第"+teamList.getNumber()+"行受奖惩员工所属的人事范围不是金卓或金铭，请修改再上传!");
							ajaxJson.setSuccess(false);
							return ajaxJson;
						}
					}
					
					SimpleDateFormat sd=new SimpleDateFormat("yyyy-MM");
			        String rapDate=sd.format(teamList.getRapDate());
			        if(!checkMonth(rapDate)){
			        	ajaxJson.setMsg(rapDate+"该日期不在奖惩范围内");
						ajaxJson.setSuccess(false);
						return ajaxJson;
			        }
			        if(teamList.getScore()>40){
						ajaxJson.setMsg("分数大于40请单独处理");
						ajaxJson.setSuccess(false);
						return ajaxJson;
					}
			        if(!(teamList.getSort().equals("0001奖类")||teamList.getSort().equals("0002罚类"))){
			        	ajaxJson.setMsg("提示：第"+(index+1)+"行奖惩类别必须是0001奖类或者0002罚类！");
						ajaxJson.setSuccess(false);
						return ajaxJson;
			        }else if(!checkType(teamList.getSort(),teamList.getRank())){
			        	ajaxJson.setMsg("提示：第"+(index+1)+"行奖惩内容有错");
						ajaxJson.setSuccess(false);
						return ajaxJson;
			        }
					for(HrUserDto user :users2){
						if (user == null) {
							ajaxJson.setMsg("员工"
									+ (processHelpService.getUserName(appAccount) == null ? "编码": processHelpService.getUserName(appAccount))
									+ "[" + account + "]不存在！");
							ajaxJson.setSuccess(false);
							return ajaxJson;
						}
						if(user.getEmpId()==null||appAccount==null)
							continue;
						int appAccountInt = 0;
						try{
							appAccountInt = Integer.parseInt(appAccount);
						}catch(Exception e){
							ajaxJson.setMsg("第"+index+"行的申请人员工编号数据存在问题！");
							ajaxJson.setSuccess(false);
							return ajaxJson;
						}
						if(user.getEmpId().intValue()!=appAccountInt){
							count2++;
							continue;
						}
						String fname = user.getName().trim();//从系统中获取员工姓名
						String name= teamList.getApplyName().replaceAll(" ", "");
						if(!(name.equals(fname))){
							ajaxJson.setMsg("您好，您所填写的员工姓名：【"+name+"】，与该员工编号：【"+appAccount+"】，所对应的姓名：【"+fname+"】不匹配，请查核确认！");
							ajaxJson.setSuccess(false);
							return ajaxJson;
						}
					}
					if(count==users.size()){
						ajaxJson.setMsg("员工"
								+ teamList.getName().trim()
								+ "[" + account + "]不存在！");
						ajaxJson.setSuccess(false);
						return ajaxJson;
					}
					if(count2==users2.size()){
						ajaxJson.setMsg("员工"
								+ teamList.getApplyName().trim()
								+ "[" + appAccount + "]不存在！");
						ajaxJson.setSuccess(false);
						return ajaxJson;
					}
					
					rows.add(map);
				}
				ajaxJson.setObj(rows);
				//System.out.println("--------"+System.currentTimeMillis());
				return ajaxJson;
			}catch(Exception e){
				ajaxJson.setMsg("文件内容解析发生错误，请先确认模板中的内容是否完全正确，再联系管理员。。");
				ajaxJson.setSuccess(false);
				return ajaxJson;
			}
			}
			
			public AjaxJson noIsOffRap(List<RapActivititiesDetail> assets){
				AjaxJson ajaxJson = new AjaxJson();
				StringBuilder builder=new StringBuilder("");
				
				Map<String,String> map=new HashMap<String,String>();
				int i=1;
				for(RapActivititiesDetail asset:assets){
					i++;
					if(map.keySet().contains(asset.getAccount()+","+asset.getSort()+","+asset.getCause()+","+asset.getScore()+","+asset.getScore()*5+","+asset.getRapDate())){
						map.put(asset.getAccount()+","+asset.getSort()+","+asset.getCause()+","+asset.getScore()+","+asset.getScore()*5+","+asset.getRapDate(),map.get(asset.getAccount()+","+asset.getSort()+","+asset.getCause()+","+asset.getScore()+","+asset.getScore()*5+","+asset.getRapDate())+String.valueOf(i)+",");
					}else{
						map.put(asset.getAccount()+","+asset.getSort()+","+asset.getCause()+","+asset.getScore()+","+asset.getScore()*5+","+asset.getRapDate(),String.valueOf((i))+",");
					}
				}
				
				Set<Entry<String,String>> entrys=map.entrySet();
				
				for(Entry<String,String> entry:entrys){
					if(entry.getValue().split(",").length>=2){
						ajaxJson.setSuccess(false);
						builder.append(entry.getValue().substring(0, entry.getValue().length()-1)+"行重复");
					}
				}
				
				ajaxJson.setMsg(builder.toString());
				return ajaxJson;
			}	
			// 外训活动导入
		@RequestMapping("/trainingResultList.html")
		@ResponseBody
		public AjaxJson trainingResultList(@RequestParam("files") MultipartFile files) throws Exception{
			//System.out.println("--------"+System.currentTimeMillis());
			
			AjaxJson ajaxJson = new AjaxJson();
			ajaxJson.setMsg("文件读取成功");
			List<HashMap<String, Object>> rows = new ArrayList<>();
			String [] fileds = new String[]{"number","account","name","startDate","endDate","courseName","courseContent","ojtSite","ojtOrg","ojtTeacher","ojtResult","ojtScore","ojtCost","ojtPercentage","ojtStartDate","ojtEndDate"};
			List<ForeignTrainingDetail> list = null;
			try {
				list = ExcelReadUtil.ReadExcelForForeign(files.getInputStream(), fileds, ForeignTrainingDetail.class, 1);
			} catch (Exception e) {
				ajaxJson.setMsg(e.getMessage());
				ajaxJson.setSuccess(false);
				return ajaxJson;
			}
		
			//校验数据
			/*if(list.size()>1){
				String str = IsExistAlike(list);
				if (!"success".equals(str)) {
					ajaxJson.setMsg(str);
					ajaxJson.setSuccess(false);
					return ajaxJson;
				}
			}*/
			int index = 0;
			
			StringBuilder accounts=new StringBuilder();
			for(ForeignTrainingDetail team:list){
				accounts.append(team.getAccount());
				accounts.append(",");
			}
			String empIds=accounts.toString();
			empIds=empIds.substring(0,empIds.length()-1);
			
			List<HrUserDto> users = processHelpService.getAllEmpInfo(empIds);//根据员工账号获取员工基本信息
			
			for(ForeignTrainingDetail teamList : list){
				HashMap<String, Object> map = new HashMap<>();
				index++;
				/*SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");*/
				map.put("number", teamList.getNumber());
		     	map.put("account", teamList.getAccount().trim());
		     	map.put("name", teamList.getName().trim());
				map.put("courseName", teamList.getCourseName().trim());
				map.put("courseContent", teamList.getCourseContent().trim());
				map.put("ojtSite", teamList.getOjtSite().trim());
				map.put("ojtOrg", teamList.getOjtOrg().trim());
				map.put("ojtTeacher", teamList.getOjtTeacher().trim());
				map.put("ojtResult", teamList.getOjtResult().trim());
				map.put("ojtScore", teamList.getOjtScore());
				map.put("ojtCost", teamList.getOjtCost());
				map.put("ojtPercentage", teamList.getOjtPercentage());
				map.put("startDate", teamList.getStartDate());
				map.put("endDate", teamList.getEndDate());
				map.put("ojtStartDate", teamList.getOjtStartDate());
				map.put("ojtEndDate", teamList.getOjtEndDate());
				Object flag = IsNotNullToTraining(teamList, map);
				if(!flag.equals(-1)){
					ajaxJson.setMsg("第"+index+"行数据不完整！");
					ajaxJson.setSuccess(false);
					return ajaxJson;
				}
				String account = teamList.getAccount().trim();
				int count=0;
				for(HrUserDto user :users){
					if (user == null) {
						ajaxJson.setMsg("员工"
								+ (processHelpService.getUserName(account) == null ? "编码": processHelpService.getUserName(account))
								+ "[" + account + "]不存在！");
						ajaxJson.setSuccess(false);
						return ajaxJson;
					}
					if(user.getEmpId()==null||account==null)
						continue;
					if(user.getEmpId().intValue()!=Integer.parseInt(account)){
						count++;
						continue;
					}
				}
				if(count==users.size()){
					ajaxJson.setMsg("员工"
							+ teamList.getName().trim()
							+ "[" + account + "]不存在！");
					ajaxJson.setSuccess(false);
					return ajaxJson;
				}
				rows.add(map);
			}
			ajaxJson.setObj(rows);
			//System.out.println("--------"+System.currentTimeMillis());
			return ajaxJson;
		}


		 //判断导入的数据中是否存在空数据
		public Object IsNotNullToTraining(ForeignTrainingDetail v, Map<String, Object> map)
				throws IllegalArgumentException, InvocationTargetException,
				IllegalAccessException {
			
			Iterator<String> iter = map.keySet().iterator();
			while (iter.hasNext()) {
				String key = iter.next();
				if (StringUtils.isEmpty(ReflectionUtils.invokeGetterMethod(v, key)
						.toString().trim())) {
					return map.get(key);
				}
			}
			return -1;
		}
		
		//判断2个日期是否一致
		public Boolean checkDate(Date d1,Date d2){
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
			String str=sdf.format(d1);  
			SimpleDateFormat sdf2=new SimpleDateFormat("yyyy-MM-dd");  
			String str2=sdf2.format(d2);  
			if(str.equals(str2)){
				return true;
			}else{
				return false;
			}
		}
		//判断奖惩类型是否一致
		public Boolean checkType(String sort,String rank){
			Boolean boo=false;
			if(sort.equals("0001奖类")){
				boo= rank.equals("15奖金");
			}else if(sort.equals("0002罚类")){
				boo=rank.equals("27罚款");
			}
			return boo;
		}
		
		
		// 新增编制说明
		@RequestMapping("/newCompile.html")
		@ResponseBody
		public AjaxJson newCompile(@RequestParam("files") MultipartFile files) throws Exception{
			
			AjaxJson ajaxJson = new AjaxJson();
			ajaxJson.setMsg("文件读取成功");
			List<HashMap<String, Object>> rows = new ArrayList<>();
			
			String [] fileds = new String[]{"content"};
			List<NewCompile> list = null;
			try {
				list = ExcelReadUtil.newCompileReadExcel(files.getInputStream(), fileds, NewCompile.class, 1);
			} catch (Exception e) {
				ajaxJson.setMsg(e.getMessage());
				ajaxJson.setSuccess(false);
				return ajaxJson;
			}
			for (NewCompile newCompile : list) {
				HashMap<String, Object> map = new HashMap<>();
				map.put("content", newCompile.getContent());
				rows.add(map);
			}
			ajaxJson.setObj(rows);
			return ajaxJson;
		}
}
