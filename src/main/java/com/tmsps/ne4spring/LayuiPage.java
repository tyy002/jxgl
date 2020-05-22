package com.tmsps.ne4spring;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.tmsps.ne4spring.annotation.NotMap;
import com.tmsps.ne4spring.annotation.PK;
import com.tmsps.ne4spring.annotation.Table;
import com.tmsps.ne4spring.orm.model.DataModel;

/**
 * layUI返回封装
 */

@Table
public class LayuiPage<T> extends DataModel {

	private int code;// 0查询成功1失败
	private String msg;// 返回消息
	private int count;// 总数
	private List<?> data;// 查询数据

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<?> getData() {
		return data;
	}

	public void setData(List<?> data) {
		this.data = data;
	}

}