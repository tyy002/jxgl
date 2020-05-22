package com.gf.entity;

import com.tmsps.ne4spring.annotation.NotMap;
import com.tmsps.ne4spring.annotation.PK;
import com.tmsps.ne4spring.orm.model.DataModel;

public class UploadFile extends DataModel{
	
	private String name;//名称
	private String firstName;//原始名称
	private String endName;//最终名称
	private String url;//路径
	private String size;//大小
	private String type;//类型
	private String date;//时间
	private String year;//年度
	private String regulations_id; //条列kid
	private String userName;//上传用户
	
	
	@PK
	private String kid;
	private int status;
	private long created = System.currentTimeMillis();

	@NotMap
	private static final long serialVersionUID = 1L;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	
	public String getEndName() {
		return endName;
	}

	public void setEndName(String endName) {
		this.endName = endName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	

	public String getRegulations_id() {
		return regulations_id;
	}

	public void setRegulations_id(String regulations_id) {
		this.regulations_id = regulations_id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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
		return "UploadFile [name=" + name + ", firstName=" + firstName + ", endName=" + endName + ", url=" + url
				+ ", size=" + size + ", type=" + type + ", date=" + date + ", year=" + year + ", regulations_id="
				+ regulations_id + ", userName=" + userName + ", kid=" + kid + ", status=" + status + ", created="
				+ created + "]";
	}


	

	
	
	
	
	
	
	

}
