package com.tmsps.ne4spring.orm.model;

import com.alibaba.fastjson.JSON;
import com.tmsps.ne4spring.orm.ClassUtil;
import java.io.Serializable;
import java.lang.reflect.Field;

public class DataModel implements Serializable {
	public String toJsonString() {
		return JSON.toJSONString(this);
	}

	public Object getPK() {
		Field idField = ClassUtil.getIdField(getClass());

		Object idVal = ClassUtil.getClassVal(idField, this);
		return idVal;
	}

	public String getTableName() {
		return ClassUtil.getClassName(getClass());
	}
}
