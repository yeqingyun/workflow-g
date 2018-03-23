package com.gionee.gniflow.biz.model;

import com.gionee.gnif.model.base.BusinessObject;

public class BpmCommCode extends BusinessObject {

	private static final long serialVersionUID = 5897869001637581015L;

	private String category;
    private String description;
    private String code;
    private String name;
    private Integer sort;

    public void setCategory(String category) {
        this.category = category;
    }
    public String getCategory() {
        return category;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getDescription() {
        return description;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getCode() {
        return code;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public void setSort(Integer sort) {
        this.sort = sort;
    }
    public Integer getSort() {
        return sort;
    }

}
