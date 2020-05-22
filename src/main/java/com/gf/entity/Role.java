package com.gf.entity;


import org.springframework.security.core.GrantedAuthority;

import com.tmsps.ne4spring.annotation.PK;
import com.tmsps.ne4spring.orm.model.DataModel;

public class Role extends DataModel implements GrantedAuthority {
	
	
    private String name; //角色名称
    private String explanation; //角色描述

    @PK
	private String kid;
	private int status;
	private long created = System.currentTimeMillis();
 
	
	
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

	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    
    public String getExplanation() {
		return explanation;
	}

	public void setExplanation(String explanation) {
		this.explanation = explanation;
	}

	@Override
    public String getAuthority() {
        return name;
    }

	@Override
	public String toString() {
		return "Role [name=" + name + ", explanation=" + explanation + ", kid=" + kid + ", status=" + status
				+ ", created=" + created + "]";
	}

	

	

	

    
}
