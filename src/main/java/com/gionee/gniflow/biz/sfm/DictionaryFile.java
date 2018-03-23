package com.gionee.gniflow.biz.sfm;

public class DictionaryFile {

	private String id;
	private String pid;
	private String name;
	private Boolean isParent;
	private Boolean open;
	private String path;
	private String dispPath;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Boolean getIsParent() {
		return isParent;
	}
	public void setIsParent(Boolean isParent) {
		this.isParent = isParent;
	}
	public Boolean getOpen() {
		return open;
	}
	public void setOpen(Boolean open) {
		this.open = open;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getDispPath() {
		return dispPath;
	}
	public void setDispPath(String dispPath) {
		this.dispPath = dispPath;
	}
	
	
}