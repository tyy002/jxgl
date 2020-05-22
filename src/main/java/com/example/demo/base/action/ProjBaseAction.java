package com.example.demo.base.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.base.tip.Tip;
import com.tmsps.ne4spring.base.IBaseService;
import com.tmsps.ne4spring.page.Page;

public class ProjBaseAction {

	@Autowired
	protected IBaseService bs;

	protected Logger logger = LoggerFactory.getLogger(getClass());

	protected HttpServletRequest req;
	protected HttpServletResponse resp;

	// 操作结果提示与说明(例如:操作成功)
	public Tip tip = new Tip();

	// 查询字段
	public JSONObject srh = new JSONObject();
	// 排序字段
	public Map<String, String> sort_params = new HashMap<String, String>();
	// 返回结果
	public Map<String, Object> result = new HashMap<String, Object>();

	public Page page = new Page();

	// ============= request and response ========================
	@ModelAttribute
	public void setReqAndResp(HttpServletRequest req, HttpServletResponse resp) {
		this.req = req;
		this.resp = resp;

	}

	// ========= get / set ()===================

	public void setTipMsg(String msg) {
		this.setTipMsg(msg, Tip.Type.info);
	}

	public void setTipMsg(String msg, Tip.Type type) {
		this.setTipMsg(true, msg, type);
	}

	public void setTipMsg(boolean b, String msg, Tip.Type type) {
		tip.setMsg(msg);
		tip.setType(type);
		result.put("success", b);
		result.put("loginVar", "");
		result.put("tip", tip);
	}

}
