package com.tmsps.ne4spring.orm;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tmsps.ne4spring.annotation.PK;
import com.tmsps.ne4spring.base.Record;
import com.tmsps.ne4spring.orm.model.DataModel;
import com.tmsps.ne4spring.utils.StrUtil;

public class MySQLUtil {
	static Logger log = LoggerFactory.getLogger(MySQLUtil.class);

	public static String getInsSQL(final Class<? extends DataModel> clazz) {
		int i = 0;
		StringBuilder sb = new StringBuilder("insert into ");
		// 获取表名称
		sb.append(ClassUtil.getTableName(clazz)).append("(");
		// 获取class中需要映射的属性名
		List<String> names = ClassUtil.getPropertyName(clazz);
		for (i = 0; i < names.size() - 1; i++) {
			// 生成映射的insert 语句
			sb.append('`').append(names.get(i)).append("`,");
		}
		sb.append('`').append(names.get(names.size() - 1)).append('`');
		sb.append(") values (");
		for (i = 0; i < names.size() - 1; i++) {
			// 生成映射的占位符
			sb.append("? , ");
		}
		sb.append("?)");
		log.debug(sb.toString());
		return sb.toString();
	}

	public static String getTemplateInsSQL(final String tableName, final List<Object> propertys) {
		StringBuilder sb = new StringBuilder("insert into ").append(tableName).append("(");

		// 需要映射的属性名
		propertys.forEach(name -> {
			sb.append("`").append(name).append("`,");
		});
		sb.deleteCharAt(sb.length() - 1);
		sb.append(") values (");
		for (int i = 0; i < propertys.size() - 1; i++) {
			sb.append("?").append(",");
		}
		sb.append("?)");

		log.debug(sb.toString());
		return sb.toString();
	}

	public static String getInsSQL(final Record record, final String tableName) {
		List<String> columnName = record.getColumnnName();
		if (columnName.size() == 0) {
			return null;
		}
		StringBuilder sb = new StringBuilder("insert into ").append(tableName).append(" (");
		for (String column : columnName) {
			sb.append(column).append(",");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append(") values (");
		for (int i = 0; i < columnName.size(); i++) {
			sb.append("?,");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append(")");
		log.debug(sb.toString());
		return sb.toString();
	}

	public static String getUpdateSQL(final Class<?> clazz) {
		StringBuilder sb = new StringBuilder("update ");
		// 获取表名称
		sb.append(ClassUtil.getClassName(clazz)).append(" set ");
		// 获取字段名称
		List<Field> names = ClassUtil.getClassFields(clazz);
		for (Field name : names) {
			if (name.isAnnotationPresent((Class<? extends Annotation>) PK.class)) {
				continue;
			}
			sb.append('`').append(name.getName() + "`= ?,");
		}
		// 去掉 最后的逗号
		sb.deleteCharAt(sb.length() - 1);
		sb.append(" where ");
		sb.append(ClassUtil.getIdField(clazz).getName()).append(" = ?");
		log.debug(sb.toString());
		return sb.toString();
	}

	/**
	 * 生成变动的update SQL 语句，方便处理变动的字段，不变动的字段不处理，节省工作量
	 * 
	 * @author
	 * 
	 * @param obj
	 * @return
	 */
	public static String getChangeUpdateSQL(final Object obj) {
		StringBuilder sb = new StringBuilder("update ");
		String idFileld = ClassUtil.getIdField(obj.getClass()).getName();
		// 获取表名称
		sb.append(ClassUtil.getClassName(obj.getClass())).append(" set ");
		Map<Object, Object> beanKeyVals = ClassUtil.getClassKeyValNotNull(obj);
		for (Map.Entry<Object, Object> entry : beanKeyVals.entrySet()) {
			if (idFileld.equals(entry.getKey())) {
				continue;
			}
			sb.append('`').append(entry.getKey() + "`= ?,");
		}
		// 去掉 最后的逗号
		sb.deleteCharAt(sb.length() - 1);
		sb.append(" where ");
		sb.append(ClassUtil.getIdField(obj.getClass()).getName()).append(" = ?");
		log.debug(sb.toString());
		return sb.toString();
	}

	public static String getDelRealSQL(final Class<?> clazz) {
		StringBuilder sb = new StringBuilder("delete from  ");
		// 获取表名称
		sb.append(ClassUtil.getClassName(clazz)).append(" where ");
		Field id = ClassUtil.getIdField(clazz);
		sb.append(id.getName()).append(" = ?");
		log.debug(sb.toString());
		return sb.toString();
	}

	public static String getDelRealSQL(final String tableName, final String pkColumn) {
		StringBuilder sb = new StringBuilder("delete from ");
		// 获取表名称
		sb.append(tableName).append(" where ");
		sb.append(pkColumn).append(" = ?");
		log.debug(sb.toString());
		return sb.toString();
	}

	public static String getSelectSQL(final Class<?> clazz, boolean status) {
		StringBuilder sb = new StringBuilder("select * from  ");
		// 获取表名
		sb.append(ClassUtil.getClassName(clazz)).append(" where ");
		Field id = ClassUtil.getIdField(clazz);
		sb.append(id.getName()).append(" = ?");
		if (status) {// 是否需要标识位 status
			sb.append(" and status = 0");
		}
		log.debug(sb.toString());
		return sb.toString();
	}

	public static String getSelectByIdSQL(String tableName, String pkName) {
		if (StrUtil.isBlank(pkName)) {// 主键为空则使用约定主键 _id
			return getSelectByIdSQL(tableName);
		} else if (StrUtil.notBlank(tableName)) {
			StringBuilder sb = new StringBuilder("select * from ").append(tableName).append(" t");
			sb.append(" where t.").append(pkName).append(" = ?");
			log.debug(sb.toString());
			return sb.toString();
		} else {
			log.error("tableName and pkName is not null !!!");
		}
		return null;
	}

	public static String getSelectByIdSQL(String tableName) {
		if (StrUtil.notBlank(tableName)) {
			StringBuilder sb = new StringBuilder("select * from ").append(tableName).append(" t");
			sb.append(" where t._id = ?");
			log.debug(sb.toString());
			return sb.toString();
		} else {
			log.error("tableName is not null !!!");
		}
		return null;
	}

	public static String getCntSql(String sql) {
		if (sql == null) {
			return null;
		}
		StringBuilder sizeSql = new StringBuilder("SELECT COUNT(1) ");
		sizeSql.append(sql.substring(sql.toUpperCase().lastIndexOf("FROM")));
		log.debug(sizeSql.toString());
		return sizeSql.toString();
	}

	public static <T extends DataModel> T fillPojoByResultSet(ResultSet rs, final Class<T> clazz) {
		try {
			T obj = (T) clazz.getDeclaredConstructor().newInstance();
			List<Field> fs = ClassUtil.getClassFields(clazz);
			for (Field field : fs) {
				Object val = rs.getObject(field.getName());
				ClassUtil.setClassVal(field, obj, val);
			}
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getPageSQL(String select, String sqlExceptSelect) {
		StringBuilder sb = new StringBuilder(select);
		sb.append(" ").append(sqlExceptSelect);
		sb.append(" ").append("LIMIT ?,?");
		return sb.toString();
	}
}
