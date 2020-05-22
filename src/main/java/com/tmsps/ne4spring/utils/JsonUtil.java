package com.tmsps.ne4spring.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class JsonUtil {
	public static Map<String, Object> jsonStrToMap(String json) {
		Map<String, Object> map = new HashMap();
		if (ChkUtil.isNull(json)) {
			return map;
		}

		JSONObject jsonObject = JSON.parseObject(json);
		Set<String> keys = jsonObject.keySet();
		for (String key : keys) {
			map.put(key, jsonObject.get(key));
		}

		return map;
	}

	public static Object jsonStrToJsonObject(String json, Class<?> clazz) {
		if (clazz == String.class) {
			if (json.length() > 2) {
				json = json.substring(1, json.length() - 1);
			}
			return json;
		}
		JSONObject parse = JSON.parseObject(json);
		return JSON.toJavaObject(parse, clazz);
	}

	public static String toJson(Object obj) {
		return JSON.toJSONString(obj);
	}

	public static Map<String, Object> objToMap(Object obj) {
		String json = toJson(obj);
		Map<String, Object> map = jsonStrToMap(json);
		return map;
	}

	public static void main(String[] args) {
		String json = "{ \"firstName\": \"Brett\", \"lastName\":\"McLaughlin\", \"email\": \"aaaa\" }";
		Map<String, Object> map = jsonStrToMap(json);
		System.err.println(map);
	}

	public static JSONObject jsonStrToJsonObject(String json) {
		JSONObject parse = JSON.parseObject(json);
		return parse;
	}
}
