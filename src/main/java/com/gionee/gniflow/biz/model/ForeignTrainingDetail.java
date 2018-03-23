package com.gionee.gniflow.biz.model;

import java.util.Date;

import com.gionee.gnif.model.base.BusinessObject;

public class ForeignTrainingDetail extends BusinessObject{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	
	  private Integer number;//序号
	  private String account;//员工编号
	  private String name;//员工姓名
	  private String courseName;//课程名称
	  private String courseContent;//课程内容
	  private String  ojtSite;//培训地点
	  private String  ojtOrg;//培训机构
	  private String  ojtTeacher;//培训讲师
	  private String  ojtResult;//培训结果
	  private Integer ojtScore;//培训分数
	  private Integer ojtCost;//培训费用
	  private Integer ojtPercentage;//培训支付百分比
	  private Date startDate;//开始日期
	  private Date endDate;//结束日期
	  private Date ojtStartDate;//培训开始日期
	  private Date ojtEndDate;//培训结束日期
	  public Date getOjtStartDate() {
		return ojtStartDate;
	}
	public void setOjtStartDate(Date ojtStartDate) {
		this.ojtStartDate = ojtStartDate;
	}
	public Date getOjtEndDate() {
		return ojtEndDate;
	}
	public void setOjtEndDate(Date ojtEndDate) {
		this.ojtEndDate = ojtEndDate;
	}
	
		public Integer getNumber() {
			return number;
		}
		public void setNumber(Integer number) {
			this.number = number;
		}
		public String getAccount() {
			return account;
		}
		public void setAccount(String account) {
			this.account = account;
		}
		public String getCourseName() {
			return courseName;
		}
		public void setCourseName(String courseName) {
			this.courseName = courseName;
		}
		public String getCourseContent() {
			return courseContent;
		}
		public void setCourseContent(String courseContent) {
			this.courseContent = courseContent;
		}
		public String getOjtSite() {
			return ojtSite;
		}
		public void setOjtSite(String ojtSite) {
			this.ojtSite = ojtSite;
		}
		public String getOjtOrg() {
			return ojtOrg;
		}
		public void setOjtOrg(String ojtOrg) {
			this.ojtOrg = ojtOrg;
		}
		public String getOjtTeacher() {
			return ojtTeacher;
		}
		public void setOjtTeacher(String ojtTeacher) {
			this.ojtTeacher = ojtTeacher;
		}
		public String getOjtResult() {
			return ojtResult;
		}
		public void setOjtResult(String ojtResult) {
			this.ojtResult = ojtResult;
		}
		public Integer getOjtScore() {
			return ojtScore;
		}
		public void setOjtScore(Integer ojtScore) {
			this.ojtScore = ojtScore;
		}
		public Integer getOjtCost() {
			return ojtCost;
		}
		public void setOjtCost(Integer ojtCost) {
			this.ojtCost = ojtCost;
		}
		public Integer getOjtPercentage() {
			return ojtPercentage;
		}
		public void setOjtPercentage(Integer ojtPercentage) {
			this.ojtPercentage = ojtPercentage;
		}
		public Date getStartDate() {
			return startDate;
		}
		public void setStartDate(Date startDate) {
			this.startDate = startDate;
		}
		public Date getEndDate() {
			return endDate;
		}
		public void setEndDate(Date endDate) {
			this.endDate = endDate;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
	 
}
