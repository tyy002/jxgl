package com.gf.entity;


public class RolePermission {//临时表

    private String url;
    private String roleName;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    
	@Override
	public String toString() {
		return "RolePermisson [url=" + url + ", roleName=" + roleName + "]";
	}


}
