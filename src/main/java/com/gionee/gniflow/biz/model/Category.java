package com.gionee.gniflow.biz.model;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gionee.gnif.biz.model.TreeModel;
import com.gionee.gnif.util.AppContext;

public class Category extends TreeModel {

	private static final long serialVersionUID = -5397783886648598815L;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public final static Integer STATUS_NORMAL = 0;
	public final static Integer STATUS_DELETED = -1;
	
	private Long id;
	private Integer status;
	private String remark;
	private String create_by;
	private Date create_time;
	private String update_by;
	private Date update_time;
	private String code;
	private String name;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCreate_by() {
		return create_by;
	}

	public void setCreate_by(String create_by) {
		this.create_by = create_by;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public String getUpdate_by() {
		return update_by;
	}

	public void setUpdate_by(String update_by) {
		this.update_by = update_by;
	}

	public Date getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(Date update_time) {
		this.update_time = update_time;
	}
	
	public String getCurrentAccount() {
		try {
			return AppContext.getCurrentAccount();
		}
		catch (Exception ex) {
			logger.info("can not get current account: " + ex.getMessage());
		}
		return null;
	}
	
	public Date getCurrentTime() {
		return new Date();
	}
	
	public Integer getNormalStatus() {
		return STATUS_NORMAL;
	}
	
	public Long getNextId() {
		id = AppContext.getIdGenerator().getId(this.getClass().getName()).longValue();
		return id;
	}
}
