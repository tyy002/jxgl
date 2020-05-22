package com.gf.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.base.service.BaseService;
import com.gf.entity.User;
import com.tmsps.ne4spring.orm.param.NeParamList;
import com.tmsps.ne4spring.page.Page;

@Service
public class UserService extends BaseService{
	
	
	public List<Map<String, Object>> list(Integer page, Integer limit, String searchText) {
		String sql = "select u.kid,u.username,r.name from user as u LEFT JOIN user_role as ur on u.kid=ur.user_id LEFT JOIN role as r on r.kid=ur.role_id  where u.status=0 and u.username = ? order by u.created desc limit "
				+ page + "," + limit;
		NeParamList param = NeParamList.makeParams();
		param.add(searchText);
		List<Map<String, Object>> list = bs.findList(sql, param);
		return list;
	}

	public Integer pageCount(String searchText) {
		String sql = "select u.kid,u.username,r.name from user as u LEFT JOIN user_role as ur on u.kid=ur.user_id LEFT JOIN role as r on r.kid=ur.role_id   where u.status=0 and u.username = ? order by u.created desc";
		NeParamList param = NeParamList.makeParams();
		param.add(searchText);
		List<Map<String, Object>> list = bs.findList(sql, param);
		return list.size();
	}
	public List<Map<String, Object>> loadUserByUsername(String userName) {
		String sql = "select kid , username , password from user where username =? and status=0";
		NeParamList param = NeParamList.makeParams();
		param.add(userName);
		List<Map<String, Object>> list = bs.findList(sql, param);
		return list;
	}
	public List<Map<String, Object>> findAllUser() {
		String sql = "SELECT * from USER as u LEFT JOIN user_role as ur on u.kid=ur.user_id LEFT JOIN role as r on ur.role_id =r.kid where r.`name`='ROLE_USER' and u.status=0 ";
		List<Map<String, Object>> list = bs.findList(sql);
		return list;
	}

	public List<Map<String, Object>> findAllRoles() {
		String sql = "SELECT * from role where status=0  ";
		List<Map<String, Object>> list = bs.findList(sql);
		return list;
	}
	
	public List<Map<String, Object>> findAll() {
		String sql = "SELECT * from user where status=0  ";
		List<Map<String, Object>> list = bs.findList(sql);
		return list;
	}



	
	

}
