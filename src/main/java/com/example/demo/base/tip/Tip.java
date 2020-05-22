package com.example.demo.base.tip;

/**
 * 提示信息 封装类
 * 
 * @author shenwei
 * 
 * @date :
 */

public class Tip {

	// 提示信息
	public String msg;
	public Type type;

	public Tip() {
		super();
		this.msg = "";
		this.type = Type.info;
	}

	public Tip(String msg, Type type) {
		super();
		this.msg = msg;
		this.type = type;
	}

	// 提示类别
	public enum Type {
		success("success"), warning("warning"), info("info"), error("error");
		private Type(String type) {
			this.type = type;
		}

		public String toString() {
			return type.toString();
		}

		private String type;

	}

	// ====== get / set ()============
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

}
