package com.gionee.gniflow.biz.model;

import java.util.Date;

import com.gionee.gnif.model.base.BusinessObject;

public class RapActivititiesDetail extends BusinessObject{

	   /**
		 * 
		 */
		private static final long serialVersionUID = 1285817177933402158L;
		

		private String department;//部门
	    private String name;//姓名
	    private Date RapDate;//奖惩日期
	    private String account;//员工编号
	    private Integer number;//序号
	    private String job;//岗位
	    private Integer cost;//费用 
	    private String applyAccount;//申请人编号
	    private String applyName;//申请人名字
	    private String applyDepa;//申请人部门
	    private String sort;//类别
	    private String rank;//级别
        private String cause;//原因
        private Integer score;//分数
        private Integer money;//金额
        private String remarks;//备注
        
        
		public String getDepartment() {
			return department;
		}
		public void setDepartment(String department) {
			this.department = department;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public Date getRapDate() {
			return RapDate;
		}
		public void setRapDate(Date rapDate) {
			RapDate = rapDate;
		}
		public String getAccount() {
			return account;
		}
		public void setAccount(String account) {
			this.account = account;
		}
		public Integer getNumber() {
			return number;
		}
		public void setNumber(Integer number) {
			this.number = number;
		}
		public String getJob() {
			return job;
		}
		public void setJob(String job) {
			this.job = job;
		}
		public Integer getCost() {
			return cost;
		}
		public void setCost(Integer cost) {
			this.cost = cost;
		}
		public String getApplyAccount() {
			return applyAccount;
		}
		public void setApplyAccount(String applyAccount) {
			this.applyAccount = applyAccount;
		}
		public String getApplyName() {
			return applyName;
		}
		public void setApplyName(String applyName) {
			this.applyName = applyName;
		}
		public String getApplyDepa() {
			return applyDepa;
		}
		public void setApplyDepa(String applyDepa) {
			this.applyDepa = applyDepa;
		}
		public String getSort() {
			return sort;
		}
		public void setSort(String sort) {
			this.sort = sort;
		}
		public String getRank() {
			return rank;
		}
		public void setRank(String rank) {
			this.rank = rank;
		}
		public String getCause() {
			return cause;
		}
		public void setCause(String cause) {
			this.cause = cause;
		}
		public Integer getScore() {
			return score;
		}
		public void setScore(Integer score) {
			this.score = score;
		}
		public Integer getMoney() {
			return money;
		}
		public void setMoney(Integer money) {
			this.money = money;
		}
		public String getRemarks() {
			return remarks;
		}
		public void setRemarks(String remarks) {
			this.remarks = remarks;
		}
		public static long getSerialversionuid() {
			return serialVersionUID;
		}
}
