package com.gf.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.demo.base.service.BaseService;
import com.tmsps.ne4spring.orm.param.NeParamList;

@Service
public class RegulationsService extends BaseService{
	
	public List<Map<String, Object>> list(Integer page, Integer limit, String searchText) {
		String sql = "select * from faculty  where status=0 and facultyName = ? order by created desc limit "
				+ page + "," + limit;
		NeParamList param = NeParamList.makeParams();
		param.add(searchText);
		List<Map<String, Object>> list = bs.findList(sql, param);
		return list;
	}

	public Integer pageCount(String searchText) {
		String sql = "select * from faculty  where status=0 and facultyName = ? order by created desc";
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
	
	public List<Map<String, Object>> findMaxByRegulationsDate(String type) {
		String sql = "select max(date) as date from regulations  where status=0  and type=? order by date desc ";
		NeParamList param = NeParamList.makeParams();
		param.add(type);
		List<Map<String, Object>> list = bs.findList(sql,param);
		return list;
		
	}
	
	public List<Map<String, Object>> findByRegulationsDate(String type) {
		String sql = "select distinct date from regulations  where status=0  and type=? order by date desc ";
		NeParamList param = NeParamList.makeParams();
		param.add(type);
		List<Map<String, Object>> list = bs.findList(sql,param);
		return list;
		
	}
	

}
