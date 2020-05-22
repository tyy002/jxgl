package com.tmsps.ne4spring.orm.param;

import com.tmsps.ne4spring.utils.ChkUtil;
import java.util.ArrayList;
import java.util.List;

public class NeParamList {
	private List<NeParam> paramList = new ArrayList();

	private List<Object> paramValueList = new ArrayList();

	public NeParamList add(Object value) {
		NeParam p = new NeParam();
		p.setValue(value);
		this.paramList.add(p);

		if (!p.getIsNull()) {
			this.paramValueList.add(value);
		}
		return this;
	}

	public NeParamList addLikeL(String paramValue) {
		if (ChkUtil.isNotNull(paramValue)) {
			paramValue = "%" + paramValue;
		}
		return add(paramValue);
	}

	public NeParamList addLikeR(String paramValue) {
		if (ChkUtil.isNotNull(paramValue)) {
			paramValue = paramValue + "%";
		}
		return add(paramValue);
	}

	public NeParamList addLike(String paramValue) {
		if (ChkUtil.isNotNull(paramValue)) {
			paramValue = "%" + paramValue + "%";
		}
		return add(paramValue);
	}

	public Object[] getParamValues() {
		return this.paramValueList.toArray();
	}

	public List<NeParam> getParamList() {
		return this.paramList;
	}

	public static NeParamList makeParams() {
		NeParamList params = new NeParamList();
		return params;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[ ");
		for (Object value : this.paramValueList) {
			sb.append(value).append(" ");
		}
		sb.append("]");
		return sb.toString();
	}

	public static void main(String[] args) {
	}
}
