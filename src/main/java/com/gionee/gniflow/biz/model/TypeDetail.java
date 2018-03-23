package com.gionee.gniflow.biz.model;

import com.gionee.gnif.model.base.BusinessObject;

public class TypeDetail extends BusinessObject {

    /**
	 * 
	 */
	private static final long serialVersionUID = 4719918232901300832L;
	
	private String name;
	private String typeName;
    private String description;

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getDescription() {
        return description;
    }
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

}
