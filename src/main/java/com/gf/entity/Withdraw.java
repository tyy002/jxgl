package com.gf.entity;

import com.tmsps.ne4spring.annotation.NotMap;
import com.tmsps.ne4spring.annotation.PK;
import com.tmsps.ne4spring.orm.model.DataModel;

public class Withdraw extends DataModel{
	
	private String faculty_name; //院系
	private String regulation_kid; //条例
	private String regulation_name; //条例
	private String type; //类型
	private String user_kid; //退回给用户
	private String reason; //原因
	private String dateTime; //时间
	private String deal_with; //处理状态
	
	
	@PK
	private String kid;
	private int status;
	private long created = System.currentTimeMillis();

	@NotMap
	private static final long serialVersionUID = 1L;

	public String getFaculty_name() {
		return faculty_name;
	}

	public void setFaculty_name(String faculty_name) {
		this.faculty_name = faculty_name;
	}

	public String getRegulation_kid() {
		return regulation_kid;
	}

	public void setRegulation_kid(String regulation_kid) {
		this.regulation_kid = regulation_kid;
	}

	
	public String getRegulation_name() {
		return regulation_name;
	}

	public void setRegulation_name(String regulation_name) {
		this.regulation_name = regulation_name;
	}

	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUser_kid() {
		return user_kid;
	}

	public void setUser_kid(String user_kid) {
		this.user_kid = user_kid;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	
	

	public String getDeal_with() {
		return deal_with;
	}

	public void setDeal_with(String deal_with) {
		this.deal_with = deal_with;
	}

	public String getKid() {
		return kid;
	}

	public void setKid(String kid) {
		this.kid = kid;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public long getCreated() {
		return created;
	}

	public void setCreated(long created) {
		this.created = created;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "Withdraw [faculty_name=" + faculty_name + ", regulation_kid=" + regulation_kid + ", regulation_name="
				+ regulation_name + ", type=" + type + ", user_kid=" + user_kid + ", reason=" + reason + ", dateTime="
				+ dateTime + ", deal_with=" + deal_with + ", kid=" + kid + ", status=" + status + ", created=" + created
				+ "]";
	}

	

	

	   
	

}
