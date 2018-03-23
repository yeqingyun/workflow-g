/**
 * @(#) GridPageData.java Created on 2014年4月17日
 *
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.gionee.gniflow.web.easyui;

import java.util.List;

/**
 * The class <code>GridPageData</code>
 * 
 * @author lipw
 * @version 1.0
 */
public class GridPageData<T> {
	private List<T> rows;

	private Long total;

	/**
	 * @param rows
	 * @param total
	 */
	public GridPageData(List<T> rows, Long total) {
		super();
		this.rows = rows;
		this.total = total;
	}

	/**
	 * @return the rows
	 */
	public List<T> getRows() {
		return rows;
	}

	/**
	 * @param rows
	 *            the rows to set
	 */
	public void setRows(List<T> rows) {
		this.rows = rows;
	}

	/**
	 * @return the total
	 */
	public Long getTotal() {
		return total;
	}

	/**
	 * @param total
	 *            the total to set
	 */
	public void setTotal(Long total) {
		this.total = total;
	}

}
