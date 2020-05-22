package com.gf.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.demo.base.service.BaseService;
import com.tmsps.ne4spring.orm.param.NeParamList;

@Service
public class WithdrawService extends BaseService{
	
	public List<Map<String, Object>> list(Integer page, Integer limit, String searchText) {
		String sql = "select * from withdraw  where status=0 and faculty_name = ? order by created desc limit "
				+ page + "," + limit;
		NeParamList param = NeParamList.makeParams();
		param.add(searchText);
		List<Map<String, Object>> list = bs.findList(sql, param);
		return list;
	}

	public Integer pageCount(String searchText) {
		String sql = "select * from withdraw  where status=0 and faculty_name = ? order by created desc";
		NeParamList param = NeParamList.makeParams();
		param.add(searchText);
		List<Map<String, Object>> list = bs.findList(sql, param);
		return list.size();
	}

	public List<Map<String, Object>> findFilesByRegulationsId(String kid) {
		String sql = "select * from uploadFile  where status=0 and regulations_id = ? order by created desc ";
		NeParamList param = NeParamList.makeParams();
		param.add(kid);
		List<Map<String, Object>> list = bs.findList(sql,param);
		return list;
		
	}


	public Integer findWithdrawByPrincipalPageCount(String searchText,String user_kid) {
		String sql = "select * from withdraw  where status=0 and faculty_name = ? and user_kid=? order by created desc";
		NeParamList param = NeParamList.makeParams();
		param.add(searchText);
		param.add(user_kid);
		List<Map<String, Object>> list = bs.findList(sql, param);
		return list.size();
	}

	public List<Map<String, Object>> findWithdrawByPrincipal(Integer page, Integer limit, String searchText,
			String user_kid) {
		String sql = "select * from withdraw  where status=0 and faculty_name = ? and user_kid=? order by created desc limit "
				+ page + "," + limit;
		NeParamList param = NeParamList.makeParams();
		param.add(searchText);
		param.add(user_kid);
		List<Map<String, Object>> list = bs.findList(sql, param);
		return list;
	}
	

}
