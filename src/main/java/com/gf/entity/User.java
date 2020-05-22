package com.gf.entity;


import org.springframework.security.core.userdetails.UserDetails;

import com.tmsps.ne4spring.annotation.NotMap;
import com.tmsps.ne4spring.annotation.PK;
import com.tmsps.ne4spring.orm.model.DataModel;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class User extends DataModel implements UserDetails,Serializable{

    private String username;
    private String password;
    private String createDate;
    private String description; //描述
    private String phone_number; //手机号
    private String online_status; //指派状态
    
    private List<Role> authorities;
    
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
	

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPhone_number() {
		return phone_number;
	}

	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}

	public String getOnline_status() {
		return online_status;
	}

	public void setOnline_status(String online_status) {
		this.online_status = online_status;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	@Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public List<Role> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<Role> authorities) {
        this.authorities = authorities;
    }

    /**
     * 用户账号是否过期
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 用户账号是否被锁定
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 用户密码是否过期
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 用户是否可用
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

	@Override
	public String toString() {
		return "User [username=" + username + ", password=" + password + ", createDate=" + createDate + ", description="
				+ description + ", phone_number=" + phone_number + ", online_status=" + online_status + ", authorities="
				+ authorities + ", kid=" + kid + ", status=" + status + ", created=" + created + "]";
	}

	

	

    
}
