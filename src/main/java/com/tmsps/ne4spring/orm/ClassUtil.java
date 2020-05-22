package com.tmsps.ne4spring.orm;

import com.tmsps.ne4spring.annotation.NotMap;
import com.tmsps.ne4spring.annotation.PK;
import com.tmsps.ne4spring.annotation.Table;
import com.tmsps.ne4spring.orm.model.DataModel;
import com.tmsps.ne4spring.utils.ChkUtil;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class ClassUtil {
	public static String getClassName(Class clazz) {
		if (!clazz.isAnnotationPresent(Table.class))
			return clazz.getSimpleName().toString();
		if (!"".equals(((Table) clazz.getAnnotation(Table.class)).value().toString()))
			return ((Table) clazz.getAnnotation(Table.class)).value().toString();
		if ("".equals(((Table) clazz.getAnnotation(Table.class)).TableName().toString())) {
			return clazz.getSimpleName().toString();
		}
		return ((Table) clazz.getAnnotation(Table.class)).TableName().toString();
	}

	public static String getTableName(Class<? extends DataModel> clazz) {
		if (!clazz.isAnnotationPresent(Table.class))
			return clazz.getSimpleName().toString();
		if (!"".equals(((Table) clazz.getAnnotation(Table.class)).value().toString()))
			return ((Table) clazz.getAnnotation(Table.class)).value().toString();
		if ("".equals(((Table) clazz.getAnnotation(Table.class)).TableName().toString())) {
			return clazz.getSimpleName().toString();
		}
		return ((Table) clazz.getAnnotation(Table.class)).TableName().toString();
	}

	public static List<String> getPropertyName(Class<?> clazz) {
		List<String> list = new ArrayList();
		List<Field> fields = getClassFields(clazz);
		for (Field field : fields) {
			if (!field.isAnnotationPresent(NotMap.class)) {

				list.add(field.getName());
			}
		}
		return list;
	}

	public static List<Field> getClassFields(Class<?> clazz) {
		List<Field> clazzField = new ArrayList();
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			if (!field.isAnnotationPresent(NotMap.class)) {

				clazzField.add(field);
			}
		}
		return clazzField;
	}

	public static List<Object> getValuesPar(Object obj) {
		List<Object> list = new ArrayList();
		List<Field> fields = getClassFields(obj.getClass());
		for (Field field : fields)
			if (!field.isAnnotationPresent(NotMap.class)) {

				boolean acc = field.isAccessible();
				field.setAccessible(true);
				try {
					Object value = field.get(obj);
					list.add(value);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					field.setAccessible(acc);
				}
			}
		return list;
	}

	public static LinkedHashMap<Object, Object> getClassKeyVal(Object obj) {
		LinkedHashMap<Object, Object> classMap = new LinkedHashMap();
		List<Field> fields = getClassFields(obj.getClass());
		for (Field field : fields)
			if (!field.isAnnotationPresent(NotMap.class)) {

				boolean acc = field.isAccessible();
				field.setAccessible(true);
				try {
					classMap.put(field.getName(), field.get(obj));
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					field.setAccessible(acc);
				}
			}
		return classMap;
	}

	public static LinkedHashMap<Object, Object> getClassKeyValNotNull(Object obj) {
		LinkedHashMap<Object, Object> classMap = getClassKeyVal(obj);
		Iterator<Map.Entry<Object, Object>> entries = classMap.entrySet().iterator();
		while (entries.hasNext()) {
			Map.Entry<Object, Object> entry = (Map.Entry) entries.next();
			if (entry.getValue() == null) {
				entries.remove();
			}
		}
		return classMap;
	}

	public static LinkedHashMap<Object, Object> getClassKeyValNotNullAndPK(Object obj) {
		LinkedHashMap<Object, Object> classMap = new LinkedHashMap();
		List<Field> fields = getClassFields(obj.getClass());
		for (Field field : fields)
			if (!field.isAnnotationPresent(NotMap.class)) {

				boolean acc = field.isAccessible();
				field.setAccessible(true);
				try {
					if (field.isAnnotationPresent(PK.class)) {
						classMap.put(field.getName(), field.get(obj));
					} else if (ChkUtil.isNotNull(field.get(obj))) {
						classMap.put(field.getName(), field.get(obj));
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					field.setAccessible(acc);
				}
			}
		return classMap;
	}

	public static List<Object> getKeyList(Map<Object, Object> map) {
		Set<Object> keys = map.keySet();
		return new ArrayList(keys);
	}

	public static List<Object> getValList(Map<Object, Object> map) {
		return new ArrayList(map.values());
	}

	public static Field getIdField(Class<?> clazz) {
		Field id = null;
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			if (field.isAnnotationPresent(PK.class)) {
				id = field;
				break;
			}
		}

		if (id == null) {
			for (Field field : fields) {
				if (!field.isAnnotationPresent(NotMap.class)) {
					id = field;
					break;
				}
			}
		}
		return id;
	}

	public static Object getClassVal(Field field, Object obj) {
		boolean acc = field.isAccessible();
		field.setAccessible(true);
		try {
			return field.get(obj);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			field.setAccessible(acc);
		}
	}

	public static void setClassVal(Field field, Object obj, Object val) {
		boolean acc = field.isAccessible();
		field.setAccessible(true);
		try {
			field.set(obj, val);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			field.setAccessible(acc);
		}
	}
}
