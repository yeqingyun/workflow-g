package com.gionee.gniflow.biz.model;


public class RapInfo {
	 private String account;//员工编号
	 private String sort;//类别
     private String cause;//原因
     private String score;//分数
     private String money;//金额
     private String RapDate;//奖惩日期
     
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getRapDate() {
		return RapDate;
	}
	public void setRapDate(String rapDate) {
		RapDate = rapDate;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	
	public String getCause() {
		return cause;
	}
	public void setCause(String cause) {
		this.cause = cause;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	
}
