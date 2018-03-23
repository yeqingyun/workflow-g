package com.gionee.gniflow.dto.sfm;

import java.util.List;

import com.gionee.gniflow.biz.service.impl.sfm.Supplier;

public class SuppliersDto {

	private int total;
	private List<Supplier> rows;
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public List<Supplier> getRows() {
		return rows;
	}
	public void setRows(List<Supplier> rows) {
		this.rows = rows;
	}
	
	
	
	
}
