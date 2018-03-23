package com.gionee.gniflow.biz.model;

import com.gionee.gnif.model.base.BusinessObject;
import java.util.Date;


public class Training extends BusinessObject {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1285817177933402158L;
	
	private String content;//内容
    private String date;//培训日期
    private String hour;//培训时数
    private String teacher;//培训讲师
    private String type;//考核方式
    private String result;//培训结果
    private String number;//序号
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getHour() {
		return hour;
	}
	public void setHour(String hour) {
		this.hour = hour;
	}
	public String getTeacher() {
		return teacher;
	}
	public void setTeacher(String teacher) {
		this.teacher = teacher;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
   
    

}
