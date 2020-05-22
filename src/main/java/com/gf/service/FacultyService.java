package com.gf.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.demo.base.service.BaseService;
import com.tmsps.ne4spring.orm.param.NeParamList;
@Service
public class FacultyService extends BaseService{

	public List<Map<String, Object>> employment_list(Integer page, Integer limit, String searchText) {
		String sql;
		List<Map<String, Object>> clearResult = clearResult("就业");
		if(clearResult.size()==0) {
			sql = "SELECT * from faculty as f where NOT EXISTS (SELECT * FROM result as r WHERE  r.faculty_kid=f.kid and r.type='就业') and f.`status`=0 and f.facultyName = ? order by f.created desc limit "
					+ page + "," + limit;
		}else {
			sql = "SELECT * from faculty as f LEFT JOIN result as r ON f.kid=r.faculty_kid where r.type='就业' and f.`status`=0 and f.facultyName = ? order by f.created desc limit "
					+ page + "," + limit;
		}
		NeParamList param = NeParamList.makeParams();
		param.add(searchText);
		List<Map<String, Object>> list = bs.findList(sql, param);
		return list;
	}
	
	public List<Map<String, Object>> enrollment_list(Integer page, Integer limit, String searchText) {
		String sql;
		List<Map<String, Object>> clearResult = clearResult("招生");
		if(clearResult.size()==0) {
			sql = "SELECT * from faculty as f where NOT EXISTS (SELECT * FROM result as r WHERE  r.faculty_kid=f.kid and r.type='招生') and f.`status`=0 and f.facultyName = ? order by f.created desc limit "
					+ page + "," + limit;
		}else {
			sql = "SELECT * from faculty as f LEFT JOIN result as r ON f.kid=r.faculty_kid where r.type='招生' and f.`status`=0 and f.facultyName = ? order by f.created desc limit "
					+ page + "," + limit;
		}
		NeParamList param = NeParamList.makeParams();
		param.add(searchText);
		List<Map<String, Object>> list = bs.findList(sql, param);
		return list;
	}
	
	public List<Map<String, Object>> findFacultyByPrincipal_employment(String kid) {
		String sql;
		List<Map<String, Object>> clearResult = clearResult("就业");
		if(clearResult.size()==0) {
			sql = "SELECT * from faculty as f where NOT EXISTS (SELECT * FROM result as r WHERE  r.faculty_kid=f.kid and r.type='就业') and f.`status`=0 and f.principal = ?";
		}else {
			sql = "SELECT * from faculty as f LEFT JOIN result as r ON f.kid=r.faculty_kid where r.type='就业' and f.`status`=0 and f.principal = ?";
		}
		NeParamList param = NeParamList.makeParams();
		param.add(kid);
		List<Map<String, Object>> list = bs.findList(sql, param);
		return list;
		
	}
	
	public List<Map<String, Object>> findFacultyByPrincipal_enrollment(String kid) {
		String sql;
		List<Map<String, Object>> clearResult = clearResult("招生");
		if(clearResult.size()==0) {
		 sql = "SELECT * from faculty as f where NOT EXISTS (SELECT * FROM result as r WHERE  r.faculty_kid=f.kid and r.type='招生') and f.`status`=0 and f.principal = ?";
		}else {
			 sql = "SELECT * from faculty as f LEFT JOIN result as r ON f.kid=r.faculty_kid where r.type='招生' and f.`status`=0 and f.principal = ?";
		}
		NeParamList param = NeParamList.makeParams();
		param.add(kid);
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

	public List<Map<String, Object>> findRegulationListByFacultyId(String kid,String type) {
		String sql = "select * from regulations  where status=0 and faculty_id = ? and type=? order by created asc ";
		NeParamList param = NeParamList.makeParams();
		param.add(kid);
		param.add(type);
		List<Map<String, Object>> list = bs.findList(sql, param);
		return list;
	}
	
	
	public List<Map<String, Object>> findRegulationListByFacultyIdAndYear(String kid,String type,String year) {
		String sql = "select * from regulations  where status=0 and faculty_id = ? and type=? and date=? order by created desc ";
		NeParamList param = NeParamList.makeParams();
		param.add(kid);
		param.add(type);
		param.add(year);
		List<Map<String, Object>> list = bs.findList(sql, param);
		return list;
	}

	public List<Map<String, Object>> findAll() {
		String sql = "select * from faculty  where status=0 order by created desc ";
		List<Map<String, Object>> list = bs.findList(sql);
		return list;
	}

	public Integer findFacultyByPrincipalPageCount(String kid) {
		String sql = "select * from faculty  where status=0 and principal = ? order by created desc";
		NeParamList param = NeParamList.makeParams();
		param.add(kid);
		List<Map<String, Object>> list = bs.findList(sql, param);
		return list.size();
	}
	
	public List<Map<String, Object>> clearResult(String type) {
		String sql = "select * from result where type=?";
		NeParamList param = NeParamList.makeParams();
		param.add(type);
		List<Map<String, Object>> list = bs.findList(sql,param);
		return list;
	}

	public List<Map<String, Object>> findRegulationsByFacultyId(String faculty_id, String type, String year) {
		String sql = "select * from regulations  where status=0 and faculty_id = ? and type=? and date=?";
		NeParamList param = NeParamList.makeParams();
		param.add(faculty_id);
		param.add(type);
		param.add(year);
		List<Map<String, Object>> list = bs.findList(sql, param);
		return list;
	}
	

}
