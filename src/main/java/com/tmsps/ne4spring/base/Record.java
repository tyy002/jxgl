package com.tmsps.ne4spring.base;

import com.alibaba.fastjson.JSON;
import com.tmsps.ne4spring.orm.model.DataModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Record extends DataModel {
	private static final long serialVersionUID = 1L;
	private Map<String, Object> columns;

	void setColumnsMap(Map<String, Object> columns) {
		this.columns = columns;
	}

	public Map<String, Object> getColumns() {
		if (this.columns == null) {
			this.columns = new HashMap();
		}
		return this.columns;
	}

	public Record setColumns(Map<String, Object> columns) {
		getColumns().putAll(columns);
		return this;
	}

	public Record remove(String column) {
		getColumns().remove(column);
		return this;
	}

	public Record clear() {
		getColumns().clear();
		return this;
	}

	public Record set(String column, Object value) {
		getColumns().put(column, value);
		return this;
	}

	public <T> T get(String column) {
		return (T) getColumns().get(column);
	}

	public List<String> getColumnnName() {
		if (this.columns == null) {
			return null;
		}
		return new ArrayList(this.columns.keySet());
	}

	public List<Object> getValues() {
		List<Object> values = new ArrayList();
		Iterator<Map.Entry<String, Object>> it = this.columns.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, Object> entry = (Map.Entry) it.next();
			values.add(entry.getValue());
		}
		return values;
	}

	public String toJson() {
		return JSON.toJSONString(this.columns);
	}
}
