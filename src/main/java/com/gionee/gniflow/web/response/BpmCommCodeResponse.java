package com.gionee.gniflow.web.response;

import com.gionee.gniflow.biz.model.BpmCommCode;

public class BpmCommCodeResponse {

    private Integer id;
    private String category;
    private String description;
    private String code;
    private String name;
    private Integer sort;

    public BpmCommCodeResponse(BpmCommCode bpmCommCode) {
	    setId(bpmCommCode.getId());
	    setCategory(bpmCommCode.getCategory());
	    setDescription(bpmCommCode.getDescription());
	    setCode(bpmCommCode.getCode());
	    setName(bpmCommCode.getName());
	    setSort(bpmCommCode.getSort());
	}

    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getId() {
        return id;
    }
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
