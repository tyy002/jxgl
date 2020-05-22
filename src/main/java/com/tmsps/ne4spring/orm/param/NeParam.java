package com.tmsps.ne4spring.orm.param;

import com.tmsps.ne4spring.utils.ChkUtil;
import java.io.Serializable;

public class NeParam implements Serializable {
	private static final long serialVersionUID = 1L;
	private boolean isNull;
	private Object value;

	public boolean getIsNull() {
		return this.isNull;
	}

	public void setIsNull(boolean isNull) {
		this.isNull = isNull;
	}

	public Object getValue() {
		return this.value;
	}

	public void setValue(Object value) {
		if (ChkUtil.isNull(value)) {
			setIsNull(true);
		}

		this.value = value;
	}
}
