package com.tmsps.ne4spring.orm;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.tmsps.ne4spring.orm.model.DataModel;
import com.tmsps.ne4spring.utils.ChkUtil;

public class ORMUtil {
	public static List<String> getColumnLables(ResultSet rs) {
		List<String> columnLables = new ArrayList<String>();
		ResultSetMetaData metaData = null;
		try {
			metaData = rs.getMetaData();
			int count = metaData.getColumnCount();
			for (int i = 0; i < count; i++) {
				columnLables.add(metaData.getColumnLabel(i + 1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return columnLables;
	}

	public static <T extends DataModel> T fillMapToBean(final Class<?> clazz, Map<String, Object> map) {
		if (ChkUtil.isNull(map)) {
			return null;
		}
		try {
			@SuppressWarnings("unchecked")
			T obj = (T) clazz.getDeclaredConstructor().newInstance();
			List<Field> fs = ClassUtil.getClassFields(clazz);
			for (Field field : fs) {
				Object val = map.get(field.getName());
				ClassUtil.setClassVal(field, obj, val);
			}
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String replaceFormatSqlOrderBy(String sql) {
		sql = sql.replaceAll("(\\s)+", " ");
		int index = sql.toLowerCase().lastIndexOf("order by");
		if (index > sql.toLowerCase().lastIndexOf(")")) {
			String sql1 = sql.substring(0, index);
			String sql2 = sql.substring(index);
			sql2 = sql2.replaceAll(
					"[oO][rR][dD][eE][rR] [bB][yY] [\u4e00-\u9fa5a-zA-Z0-9_.]+((\\s)+(([dD][eE][sS][cC])|([aA][sS][cC])))?(( )*,( )*[\u4e00-\u9fa5a-zA-Z0-9_.]+(( )+(([dD][eE][sS][cC])|([aA][sS][cC])))?)*",
					"");
			return sql1 + sql2;
		}
		return sql;
	}

}
