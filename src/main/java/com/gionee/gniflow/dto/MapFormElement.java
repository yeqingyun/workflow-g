/**
 * @(#) MapFormElement.java Created on 2015年1月28日
 *
 * Copyright (c) 2015 GIONEE. All Rights Reserved
 */
package com.gionee.gniflow.dto;

import java.io.Serializable;

/**
 * The class <code>MapFormElement</code>
 * 处理表单提交的name形如：map_part1up_KPI_2_1	aaa
 * 变成==> key=part1up,name=map_part1up_KPI_1,value=aaa,colIndex=2,rowIndex=1
 * @author lipw
 * @version 1.0
 */
public class MapFormElement implements Serializable,Comparable<MapFormElement> {
	
	private static final long serialVersionUID = 1288041776660088262L;
	//存储的key
	private String key;
	//inut的name
	private String name;
	//值
	private String value;
	//行标
	private String rowIndex;
	//列标
	private String colIndex;

	public MapFormElement() {
		super();
	}

	public MapFormElement(String key, String name, String value, String colIndex,
			String rowIndex) {
		super();
		this.key = key;
		this.name = name;
		this.value = value;
		this.colIndex = colIndex;
		this.rowIndex = rowIndex;
	}



	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getRowIndex() {
		return rowIndex;
	}

	public void setRowIndex(String rowIndex) {
		this.rowIndex = rowIndex;
	}

	public String getColIndex() {
		return colIndex;
	}

	public void setColIndex(String colIndex) {
		this.colIndex = colIndex;
	}

	@Override
	public int compareTo(MapFormElement o) {
		//如果行标相同，则根据列标排序
		if (this.rowIndex.compareTo(o.getRowIndex()) == 0) {
			return this.colIndex.compareTo(o.getColIndex());
		} else {
			return this.rowIndex.compareTo(o.getRowIndex());
		}
	}

	
}
