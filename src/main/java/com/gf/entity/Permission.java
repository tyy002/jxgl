package com.gf.entity;

import com.tmsps.ne4spring.annotation.NotMap;
import com.tmsps.ne4spring.annotation.PK;
import com.tmsps.ne4spring.orm.model.DataModel;

public class Permission extends DataModel{
	
	private String mid;// 编码 每3位一级
    private String code;// 编码 每3位一级
	private String url;// 地址 data-param 中的值 |
	private String name;
	private String icon;// 图标 class中的值
	private String type;// 类型: 1 父级 2 子级
	
	@PK
	private String kid;
	private int status;
	private long created = System.currentTimeMillis();

	@NotMap
	private static final long serialVersionUID = 1L;
	   
	
	
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
	public String getMid() {
		return mid;
	}
	public void setMid(String mid) {
		this.mid = mid;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@Override
	public String toString() {
		return "Permisson [mid=" + mid + ", code=" + code + ", url=" + url + ", name=" + name + ", icon=" + icon
				+ ", type=" + type + ", kid=" + kid + ", status=" + status + ", created=" + created + "]";
	}

	
	


    
    
    
}
