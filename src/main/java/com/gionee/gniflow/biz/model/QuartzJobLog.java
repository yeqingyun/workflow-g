package com.gionee.gniflow.biz.model;

import java.io.Serializable;
import java.util.Date;

public class QuartzJobLog implements Serializable {

	private static final long serialVersionUID = 1346590688432030491L;
	
	private Integer id;
	private String taskname;
	private Date startetime;
	private Date endtime;
	private Integer intervaltime;

	public void setTaskname(String taskname) {
		this.taskname = taskname;
	}

	public String getTaskname() {
		return taskname;
	}

	public void setStartetime(Date startetime) {
		this.startetime = startetime;
	}

	public Date getStartetime() {
		return startetime;
	}

	public void setEndtime(Date endtime) {
		this.endtime = endtime;
	}

	public Date getEndtime() {
		return endtime;
	}

	public void setIntervaltime(Integer intervaltime) {
		this.intervaltime = intervaltime;
	}

	public Integer getIntervaltime() {
		return intervaltime;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
