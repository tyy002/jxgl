package com.gf.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.demo.base.service.BaseService;
import com.tmsps.ne4spring.orm.param.NeParamList;

@Service
public class FileUploadService extends BaseService{
	
	
	public List<Map<String, Object>> list(Integer page, Integer limit, String searchText) {
		String sql = "select *  from uploadFile  where status=0 and name = ? order by created desc limit "
				+ page + "," + limit;
		NeParamList param = NeParamList.makeParams();
		param.add(searchText);
		List<Map<String, Object>> list = bs.findList(sql, param);
		return list;
	}

	public Integer pageCount(String searchText) {
		String sql = "select *  from uploadFile  where status=0 and name = ? order by created desc";
		NeParamList param = NeParamList.makeParams();
		param.add(searchText);
		List<Map<String, Object>> list = bs.findList(sql, param);
		return list.size();
	}

	public List<Map<String, Object>> findAllAdmin() {
		String sql = "select *  from uploadFile  where status=0 and userName = 'admin' order by created desc";

		List<Map<String, Object>> list = bs.findList(sql);
		return list;
	}

	public List<Map<String, Object>> downlList(Integer page, Integer limit, String searchText) {
		String sql = "select *  from uploadFile  where status=0 and name = ? and userName = 'admin' order by created desc limit "
				+ page + "," + limit;
		NeParamList param = NeParamList.makeParams();
		param.add(searchText);
		List<Map<String, Object>> list = bs.findList(sql, param);
		return list;
	}

	public Integer pageCountDownlList(String searchText) {
		String sql = "select *  from uploadFile  where status=0 and name = ? and userName = 'admin' order by created desc";
		NeParamList param = NeParamList.makeParams();
		param.add(searchText);
		List<Map<String, Object>> list = bs.findList(sql, param);
		return list.size();
	}
}
