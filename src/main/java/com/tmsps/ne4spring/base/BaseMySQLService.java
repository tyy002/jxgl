package com.tmsps.ne4spring.base;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.tmsps.ne4spring.exception.NEServiceException;
import com.tmsps.ne4spring.orm.ClassUtil;
import com.tmsps.ne4spring.orm.MySQLUtil;
import com.tmsps.ne4spring.orm.ORMUtil;
import com.tmsps.ne4spring.orm.model.DataModel;
import com.tmsps.ne4spring.orm.param.NeParamList;
import com.tmsps.ne4spring.orm.param.NeParamTools;
import com.tmsps.ne4spring.page.Page;
import com.tmsps.ne4spring.utils.ChkUtil;
import com.tmsps.ne4spring.utils.GenerateUtil;
import com.tmsps.ne4spring.utils.StrUtil;

@Service
public class BaseMySQLService implements IBaseService {
	@Autowired
	//@Qualifier("primaryJdbcTemplate") // 多数据源配置
	private JdbcTemplate jt;

	@Override
	public JdbcTemplate getJdbcTemplate() {
		return this.jt;
	}

	@Override
	public int saveObj(DataModel model) {
		log.debug("save the model" + model.toJsonString());
		if (ChkUtil.isNull(model)) {
			log.error("保存的对象不能为空");
			throw new NEServiceException("保存的对象不能为空");
		}

		// 获取对象中ID属性字段
		Field idField = ClassUtil.getIdField(model.getClass());
		// 获取ID的值
		Object idVal = ClassUtil.getClassVal(idField, model);
		// 如果ID字段为空，则生成一个主键
		if (ChkUtil.isNull(idVal)) {
			if (idField.getType() == String.class) {
				idVal = GenerateUtil.getBase58ID();
				ClassUtil.setClassVal(idField, model, idVal);
			}
		}
		// 获取Bean中的属性值
		final List<Object> vals = ClassUtil.getValuesPar(model);
		// 根据Bean生成插入语句
		String sql = MySQLUtil.getInsSQL(model.getClass());
		log.debug(sql);
		return jt.update(sql, vals.toArray());
	}

	@Override
	public int saveObj(DataModel model, boolean sync) {
		log.debug("save the model" + model.toJsonString());
		if (sync) {
			int flag = this.saveObj(model);
			model = findObjById(model.getPK(), model.getClass());
			return flag;
		} else {
			return this.saveObj(model);
		}
	}

	@Override
	public int save(String tableName, Record record) {
		final String insSQL = MySQLUtil.getInsSQL(record, tableName);
		if (!StrUtil.notBlank(insSQL)) {
			return 0;
		} else {
			return jt.update(insSQL, record.getValues().toArray());
		}
	}

	@Override
	@Transactional
	public void saveObjs(List<DataModel> objs) {
		if (ChkUtil.listIsNull(objs)) {
			log.error("保存的对象集合不能为空");
			throw new NEServiceException("保存的对象集合不能为空");
		}
		objs.forEach(model -> {
			this.saveObj(model);
		});
	}

	@Override
	@Deprecated
	/**
	 * since 1.4.8 use findById(idVal, clazz);
	 */
	public <T extends DataModel> T findObjById(Object idVal, Class<? extends DataModel> clazz) {
		return findById(idVal, clazz);
	}

	@Override
	public <T extends DataModel> T findById(Object idVal, Class<? extends DataModel> clazz) {
		// 主键为空，无法查询，返回null
		if (ChkUtil.isNull(idVal)) {
			log.warn("主键为null,无法查询,返回null");
			return null;
		}
		String sql = MySQLUtil.getSelectSQL(clazz, false);
		log.debug(sql);
		return jt.query(sql, new Object[] { idVal }, new ResultSetExtractor<T>() {
			@SuppressWarnings("unchecked")
			public T extractData(ResultSet rs) throws SQLException, DataAccessException {
				while (rs.next()) {
					T obj = ((T) MySQLUtil.fillPojoByResultSet(rs, clazz));
					// 只取第一个
					return (T) obj;
				}
				return null;
			}
		});
	}

	@Override
	public Map<String, Object> findById(String tableName, String pkName, String pkVal) {
		List<Map<String, Object>> list = jt.queryForList(MySQLUtil.getSelectByIdSQL(tableName, pkName), pkVal);
		return list.size() == 1 ? list.get(0) : null;
	}

	@Override
	public Map<String, Object> findById(String tableName, String pkVal) {
		return jt.queryForMap(MySQLUtil.getSelectByIdSQL(tableName), pkVal);
	}

	@Override
	public List<Map<String, Object>> findList(String sql, Object[] vals) {
		log.debug("{},{}", sql, JSON.toJSONString(vals));
		return jt.queryForList(sql, vals);
	}

	@Override
	public Map<String, Object> findObj(String sql, Object[] vals) {
		log.debug("{},{}", sql, JSON.toJSONString(vals));
		List<Map<String, Object>> list = this.findList(sql, vals);
		if (ChkUtil.listIsNotNull(list)) {
			return list.get(0);
		} else {
			return null;
		}
	}

	@Override
	public Map<String, Object> findObj(String sql) {
		List<Map<String, Object>> list = findList(sql);
		if (ChkUtil.listIsNotNull(list)) {
			return list.get(0);
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends DataModel> T findObj(String sql, Class<? extends DataModel> clazz) {
		List<Map<String, Object>> list = findList(sql);
		if (ChkUtil.listIsNotNull(list)) {
			return (T) ORMUtil.fillMapToBean(clazz, list.get(0));
		} else {
			return null;
		}
	}

	@Override
	public <T extends DataModel> T findForObj(String sql, Class<? extends DataModel> clasz) {
		return findObj(sql, clasz);
	}

	@Override
	public List<Map<String, Object>> findList(String sql) {
		log.debug(sql);
		return jt.queryForList(sql);
	}

	@Override
	public <T extends DataModel> T findObj(String sql, Object[] vals, Class<? extends DataModel> modelClass) {
		return ORMUtil.fillMapToBean(modelClass, findObj(sql, vals));
	}

	@Override
	public int deleteObjById(String id, Class<? extends DataModel> clazz) {
		final String sql = MySQLUtil.getDelRealSQL(clazz);
		return jt.update(sql, id);
	}

	@Override
	public int deleteByID(String pkColumn, String id, String tableName) {
		return jt.update(MySQLUtil.getDelRealSQL(tableName, pkColumn), id);
	}

	@Override
	public int updateObj(DataModel obj) {
		if (ChkUtil.isNull(obj)) {
			log.error("update faile , the obj can not null !");
			return 0;
		} else {
			String sql = MySQLUtil.getUpdateSQL(obj.getClass());
			final LinkedHashMap<Object, Object> clsky = ClassUtil.getClassKeyVal(obj);
			List<Object> vals = new ArrayList<Object>();
			clsky.entrySet().forEach(entry -> {
				vals.add(entry.getValue());
			});
			String pk = ClassUtil.getIdField(obj.getClass()).getName();
			Object pkVal = clsky.get(pk);
			vals.remove(pkVal);
			vals.add(pkVal);
			log.debug("sql:{} and paras is {}", sql, vals.toString());
			return jt.update(sql, vals.toArray());
		}
	}

	@Override
	public int updateChangeObj(DataModel obj) {
		return updateTemplateObj(obj);
	}

	@Override
	public int updateTemplateObj(DataModel obj) {
		if (ChkUtil.isNull(obj)) {
			log.error("修改失败！对象不允许为空！");
			return 0;
		} else {
			String sql = MySQLUtil.getChangeUpdateSQL(obj);
			final LinkedHashMap<Object, Object> clsky = ClassUtil.getClassKeyValNotNull(obj);
			List<Object> vals = new ArrayList<Object>();
			clsky.entrySet().forEach(entry -> {
				vals.add(entry.getValue());
			});

			String pk = ClassUtil.getIdField(obj.getClass()).getName();
			Object pkVal = clsky.get(pk);
			vals.remove(pkVal);
			vals.add(pkVal);
			log.debug("sql:{} and paras is {}", sql, vals.toString());
			return jt.update(sql, vals.toArray());
		}
	}

	@Override
	public List<Map<String, Object>> findList(String sql, String sql_cnt, Object[] vals, Page page) {
		log.debug("{},{}", sql, sql_cnt);
		log.debug("{},{}", JSON.toJSONString(vals), JSON.toJSONString(page));
		List<Map<String, Object>> ret = null;
		int cnt = jt.queryForObject(sql_cnt, vals, Integer.class);
		page.setTotalRow(cnt);
		page.setTotalPage((page.getTotalRow() - 1) / page.getPageSize() + 1);
		if (page.getPageNumber() > page.getTotalPage()) {
			page.setPageNumber(page.getTotalPage());
		}
		if (page.getPageNumber() <= 0) {
			page.setPageNumber(1);
		}
		// 分页
		sql += " limit :start,:pageSize";
		sql = sql.replace(":start", page.getPageSize() * (page.getPageNumber() - 1) + "");
		sql = sql.replace(":pageSize", page.getPageSize() + "");
		ret = jt.queryForList(sql, vals);
		return ret;
	}

	@Override
	public List<Map<String, Object>> findList(String sql, Object[] vals, Page page) {
		return findList(sql, vals, null, page);
	}

	@Override
	public List<Map<String, Object>> findList(String sql, Object[] vals, Map<String, String> sort_params, Page page) {
		String sql_cnt = MySQLUtil.getCntSql(sql);

		// 排序
		if (ChkUtil.isNotNull(sort_params)) {
			StringBuilder sb = new StringBuilder();
			for (String key : sort_params.keySet()) {
				if ("table".equals(key)) {
					continue;
				}
				String table = sort_params.get("table");
				table = ChkUtil.isNull(table) ? "" : table + ".";
				sb.append(" ").append(table + key).append(" ").append(sort_params.get(key)).append(",");
			}
			String ob = "order by";
			int orderIndex = sql.toLowerCase().indexOf(ob);
			if (orderIndex == -1) {
				sb.deleteCharAt(sb.length() - 1);
				sql += ob + sb.toString();
			}
			/*
			 * else { System.out.println(sql); sql = sql.substring(0,orderIndex);
			 * sb.deleteCharAt(sb.length() - 1); sql += ob + sb.toString(); //sql = new
			 * StringBuilder(sql).insert(orderIndex + ob.length(), sb).toString(); }
			 */
		} // #if a
		return findList(sql, sql_cnt, vals, page);
	}

	@Override
	public List<Map<String, Object>> findList(String sql, Page page) {
		return findList(sql, null, page);
	}

	@Override
	public int updObj(Class<?> clazz, Map<String, Object> parm, Map<String, Object> whereparm) {
		StringBuffer sb = new StringBuffer(" update ");
		sb.append(" " + ClassUtil.getClassName(clazz) + " ");
		sb.append(" set ");
		List<Object> cxparm = new ArrayList<Object>();
		int i = 0;
		for (String key : parm.keySet()) {
			if (i > 0) {
				sb.append(",");
			}
			sb.append(" " + key + " = ? ");
			i++;
			cxparm.add(parm.get(key));
		}
		sb.append(" where 1=1 ");
		if (ChkUtil.isNotNull(whereparm)) {
			for (String key : whereparm.keySet()) {
				sb.append(" and " + key + " = ?  ");
				cxparm.add(whereparm.get(key));
			}
		}
		return this.jt.update(sb.toString(), cxparm.toArray());
	}

	@Override
	public Map<String, Object> getMap(String key, Object val) {
		return getMap(new String[] { key }, new Object[] { val });
	}

	@Override
	public Map<String, List<Object>> getSearchMap() {
		Map<String, List<Object>> cxparms = new HashMap<String, List<Object>>();
		cxparms.put("cname", new ArrayList<Object>());
		cxparms.put("cwhere", new ArrayList<Object>());
		cxparms.put("cval", new ArrayList<Object>());
		return cxparms;
	}

	@Override
	public Map<String, Object> getMap(String[] key, Object[] val) {
		Map<String, Object> mapobj = new HashMap<String, Object>();
		for (int i = 0; i < key.length; i++) {
			mapobj.put(key[i], val[i]);
		}
		return mapobj;
	}

	@Override
	public Page pageinate(int pageNumber, int pageSize, String select, String sqlExceptSelect, Object... paras) {
		if (pageNumber < 1 || pageSize < 1) {
			log.error("当前页面页数和页面记录展示条数不能小于1");
			return new Page(pageNumber, pageSize);
		}
		long totalRow = 0;
		int totalPage = 0;
		if (!StrUtil.notNull(paras)) {
			return pageinate(pageNumber, pageSize, select, sqlExceptSelect);
		}
		totalRow = jt.queryForObject("SELECT COUNT(1) " + sqlExceptSelect, paras, Long.class);
		if (totalRow % pageSize == 0) {
			totalPage = (int) (totalRow / pageSize);
		} else {
			totalPage = (int) (totalRow / pageSize) + 1;
		}
		log.debug("The TotalPage is :" + totalPage);
		String sql = MySQLUtil.getPageSQL(select, sqlExceptSelect);
		log.debug("SQL: " + sql);
		// 判断当前页面是否大于最大页面
		pageNumber = pageNumber >= totalPage ? totalPage : pageNumber;
		List<Object> objs = new ArrayList<Object>(Arrays.asList(paras));
		objs.add((pageNumber - 1) * pageSize < 0 ? 0 : (pageNumber - 1) * pageSize);
		objs.add(pageSize);

		List<Map<String, Object>> result = jt.queryForList(sql, objs.toArray());
		return new Page(result, pageNumber, pageSize, totalPage, ((Number) totalRow).intValue());
	}

	@Override
	public Page pageinate(int pageNumber, int pageSize, String select, String sqlExceptSelect) {
		if (pageNumber < 1 || pageSize < 1) {
			log.error("当前页面页数和页面记录展示条数不能小于1");
			return new Page(pageNumber, pageSize);
		}
		long totalRow = 0;
		int totalPage = 0;
		totalRow = jt.queryForObject("SELECT COUNT(1) " + sqlExceptSelect, Long.class);
		if (totalRow % pageSize == 0) {
			totalPage = (int) (totalRow / pageSize);
		} else {
			totalPage = (int) (totalRow / pageSize) + 1;
		}
		log.debug("The TotalPage is :" + totalPage);
		String sql = MySQLUtil.getPageSQL(select, sqlExceptSelect);
		log.debug("SQL: {}", sql);

		// 判断当前页面是否大于最大页面
		pageNumber = pageNumber >= totalPage ? totalPage : pageNumber;
		List<Map<String, Object>> result = jt.queryForList(sql,
				(pageNumber - 1) * pageSize < 0 ? 0 : (pageNumber - 1) * pageSize, pageSize);
		return new Page(result, pageNumber, pageSize, totalPage, ((Number) totalRow).intValue());
	}

	// 新版本的查询
	@Override
	public List<Map<String, Object>> findList(String sql, NeParamList params, Map<String, String> sort_params,
			Page page) {
		String endSql = NeParamTools.handleSql(sql, params);
		log.debug("{},--->{}", endSql, params.toString());
		return this.findList(endSql, params.getParamValues(), sort_params, page);
	}

	@Override
	public List<Map<String, Object>> findList(String sql, NeParamList params) {
		String endSql = NeParamTools.handleSql(sql, params);
		log.debug("{},--->{}", endSql, params.toString());
		return jt.queryForList(endSql, params.getParamValues());
	}

	// 模版保存,只保存字段不为空的属性
	@Override
	public int saveTemplateObj(DataModel model) {
		log.debug("save the model by template:{}", model.toJsonString());
		if (ChkUtil.isNull(model)) {
			log.error("保存的对象不能为空");
			throw new NEServiceException("保存的对象不能为空");
		}
		// 获取对象中ID属性字段
		Field idField = ClassUtil.getIdField(model.getClass());
		// 获取ID的值
		Object idVal = ClassUtil.getClassVal(idField, model);
		// 如果ID字段为空，则生成一个主键
		if (ChkUtil.isNull(idVal)) {
			if (idField.getType() == String.class) {
				idVal = GenerateUtil.getBase58ID();
				ClassUtil.setClassVal(idField, model, idVal);
			}
		}

		LinkedHashMap<Object, Object> modelKeyVal = ClassUtil.getClassKeyValNotNull(model);
		// 获取属性名称
		final List<Object> propertys = ClassUtil.getKeyList(modelKeyVal);
		// 获取属性值
		final List<Object> vals = ClassUtil.getValList(modelKeyVal);
		// 根据Bean生成插入语句
		String sql = MySQLUtil.getTemplateInsSQL(ClassUtil.getTableName(model.getClass()), propertys);
		log.debug(sql);
		return jt.update(sql, vals.toArray());
	}

	@Override
	public int saveTemplateObj(DataModel model, boolean sync) {
		log.debug("save the model by template:{} with {}", model.toJsonString(), String.valueOf(sync));
		int result = this.saveTemplateObj(model);
		if (sync) {
			model = this.findById(model.getPK(), model.getClass());
		}
		return result;
	}
}
