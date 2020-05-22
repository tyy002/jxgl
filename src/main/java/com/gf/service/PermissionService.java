package com.gf.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.base.service.BaseService;
import com.tmsps.ne4spring.orm.param.NeParamList;

@Service
public class PermissionService extends BaseService{
//	 @Autowired
//	 PermissionMapper permissionMapper;
	
//	 public List<Permisson> getMenusByUserId(Integer userId) {
//	    return permissionMapper.getMenusByUserId(userId);
//	 }
	 
	 public List<Map<String, Object>> getMenusByUserId(String kid) {
			String sql = "SELECT DISTINCT  m.* FROM  ( SELECT * FROM USER ) u \r\n" + 
					" 					INNER JOIN ( SELECT * FROM user_role ) ur ON u.kid = ur.user_id AND u.kid = ?" + 
					" 					INNER JOIN ( SELECT * FROM role ) r ON r.kid = ur.role_id \r\n" + 
					" 					INNER JOIN ( SELECT * FROM role_permission ) rm ON rm.role_id = r.kid\r\n" + 
					" 					INNER JOIN ( SELECT * FROM permission ) m ON rm.permission_id = m.kid where m.status=0 order by m.created desc";
			NeParamList param = NeParamList.makeParams();
			param.add(kid);
			List<Map<String, Object>> list = bs.findList(sql, param);
			return list;
		}
	 public List<Map<String, Object>> getRolePermissions() {
		 String sql = "SELECT A.NAME AS roleName,C.url FROM role AS A LEFT JOIN role_permission B ON A.kid=B.role_id LEFT JOIN permission AS C ON B.permission_id=C.kid where A.status=0 order by A.created desc";
		 List<Map<String, Object>> list = bs.findList(sql);
		 return list;
	 }
	public List<Map<String, Object>> getAllpermisson() {
		 String sql = "SELECT * FROM  permission where status=0 order by created desc";
		 List<Map<String, Object>> list = bs.findList(sql);
		 return list;
	}

}
