package com.gf.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.demo.base.service.BaseService;
import com.tmsps.ne4spring.orm.param.NeParamList;

@Service
public class AnnouncementService extends BaseService{

	public List<Map<String, Object>> list(Integer page, Integer limit, String searchText) {
		String sql = "select * from announcement  where status=0 and title = ? order by created desc limit "
				+ page + "," + limit;
		NeParamList param = NeParamList.makeParams();
		param.add(searchText);
		List<Map<String, Object>> list = bs.findList(sql, param);
		return list;
	}

	public Integer pageCount(String searchText) {
		String sql =  "select * from announcement  where status=0 and title = ? order by created desc";
		NeParamList param = NeParamList.makeParams();
		param.add(searchText);
		List<Map<String, Object>> list = bs.findList(sql, param);
		return list.size();
	}
	
	public List<Map<String, Object>> announcementIndex() {
		String sql = "select * from announcement  where status=0 and announcement_status = 1 order by created desc limit 0,5";

		List<Map<String, Object>> list = bs.findList(sql);
		return list;
	}
}
