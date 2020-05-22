package com.gf.entity;

import com.tmsps.ne4spring.annotation.NotMap;
import com.tmsps.ne4spring.annotation.PK;
import com.tmsps.ne4spring.orm.model.DataModel;

public class Announcement extends DataModel{
	
	private String title;  //标题
	private String context; //内容
	private String date;  //发布时间
	private String userName; //发布人
	private int announcement_status; //1上架  0 下架
	private int is_top; //是否顶置  1顶置 0 不顶置
	
	
	@PK
	private String kid;
	private int status;
	private long created = System.currentTimeMillis();

	@NotMap
	private static final long serialVersionUID = 1L;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	
	public int getAnnouncement_status() {
		return announcement_status;
	}

	public void setAnnouncement_status(int announcement_status) {
		this.announcement_status = announcement_status;
	}

	public int getIs_top() {
		return is_top;
	}

	public void setIs_top(int is_top) {
		this.is_top = is_top;
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
		return "Announcement [title=" + title + ", context=" + context + ", date=" + date + ", userName=" + userName
				+ ", announcement_status=" + announcement_status + ", is_top=" + is_top + ", kid=" + kid + ", status="
				+ status + ", created=" + created + "]";
	}

	

	
	
}
