package com.gionee.gniflow.biz.model;

public class EngineerCollect {
	private String engineerName;
	private String perCount;
	private Double perTime;
	private String perSatisfaction;
	private String perSatisfactionRate;
	private String perAvgTime;
	
	public String getPerAvgTime() {
		return perAvgTime;
	}
	public void setPerAvgTime(String perAvgTime) {
		this.perAvgTime = perAvgTime;
	}
	public String getEngineerName() {
		return engineerName;
	}
	public void setEngineerName(String engineerName) {
		this.engineerName = engineerName;
	}
	public String getPerCount() {
		return perCount;
	}
	public void setPerCount(String perCount) {
		this.perCount = perCount;
	}
	public Double getPerTime() {
		return perTime;
	}
	public void setPerTime(Double perTime) {
		this.perTime = perTime;
	}
	public String getPerSatisfaction() {
		return perSatisfaction;
	}
	public void setPerSatisfaction(String perSatisfaction) {
		this.perSatisfaction = perSatisfaction;
	}
	public String getPerSatisfactionRate() {
		return perSatisfactionRate;
	}
	public void setPerSatisfactionRate(String perSatisfactionRate) {
		this.perSatisfactionRate = perSatisfactionRate;
	}
}
