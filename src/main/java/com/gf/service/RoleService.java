package com.gf.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.demo.base.service.BaseService;
import com.tmsps.ne4spring.orm.param.NeParamList;

@Service
public class RoleService extends BaseService{
	
	public List<Map<String, Object>> getRolesByUserId(String kid) {
		String sql = "SELECT A.kid,A.name FROM role A LEFT JOIN user_role B ON A.kid=B.role_id WHERE B.user_id =?";
		NeParamList param = NeParamList.makeParams();
		param.add(kid);
		List<Map<String, Object>> list = bs.findList(sql, param);
		return list;
	}
	
	public List<Map<String, Object>> list(Integer page, Integer limit, String searchText) {
		String sql = "select * from role  where status=0 and name = ? order by created desc limit "
				+ page + "," + limit;
		NeParamList param = NeParamList.makeParams();
		param.add(searchText);
		List<Map<String, Object>> list = bs.findList(sql, param);
		return list;
	}

	public Integer pageCount(String searchText) {
		String sql = "select * from role  where status=0 and name like ? order by created desc";
		NeParamList param = NeParamList.makeParams();
		param.add(searchText);
		List<Map<String, Object>> list = bs.findList(sql, param);
		return list.size();
	}

	public List<Map<String, Object>> findPermissionList(String kid) {
		String sql = "SELECT p.* from role as r LEFT JOIN role_permission as rps on r.kid=rps.role_id LEFT JOIN permission as p on rps.permission_id=p.kid   where p.status=0  and r.kid = ? order by p.created desc ";
		NeParamList param = NeParamList.makeParams();
		param.add(kid);
		List<Map<String, Object>> list = bs.findList(sql, param);
		return list;
	}

	public List<Map<String, Object>> findAllpermissionByRole(String rolekid) {
		String sql = "SELECT * FROM  role_permission  as rps where rps.role_id=?  and rps.status=0 ";
		NeParamList param = NeParamList.makeParams();
		param.add(rolekid);
		List<Map<String, Object>> list = bs.findList(sql, param);
		return list;
		
	}
}
