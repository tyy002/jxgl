package com.example.demo.util.json;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

public class GsonUtil {

	public static final Gson GSON = new Gson();

	public static Map<String, Object> Object2Map(Object obj) {
		if (obj == null) {
			return Collections.emptyMap();
		}

		return GSON.fromJson(GSON.toJson(obj), Map.class);
	}

	public static Map<String, String> Object2MapString(Object obj) {
		if (obj == null) {
			return Collections.emptyMap();
		}
		return GSON.fromJson(GSON.toJson(obj), Map.class);
	}
}
