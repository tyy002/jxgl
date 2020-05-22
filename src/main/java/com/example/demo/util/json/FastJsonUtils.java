package com.example.demo.util.json;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.JSONLibDataFormatSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;


public class FastJsonUtils {

	private static final SerializeConfig config;

	static {
		config = new SerializeConfig();
		config.put(java.util.Date.class, new JSONLibDataFormatSerializer()); // 使用和json-lib兼容的日期输出格式
		config.put(java.sql.Date.class, new JSONLibDataFormatSerializer()); // 使用和json-lib兼容的日期输出格式
	}

	private static final SerializerFeature[] features = { SerializerFeature.WriteMapNullValue, // 输出空置字段
			SerializerFeature.WriteNullListAsEmpty, // list字段如果为null，输出为[]，而不是null
			SerializerFeature.WriteNullNumberAsZero, // 数值字段如果为null，输出为0，而不是null
			SerializerFeature.WriteNullBooleanAsFalse, // Boolean字段如果为null，输出为false，而不是null
			SerializerFeature.WriteNullStringAsEmpty // 字符类型字段如果为null，输出为""，而不是null
	};

	public static String convertObjectToJSON(Object object) {
		return JSON.toJSONString(object, config, features);
	}

	public static String toJSONNoFeatures(Object object) {
		return JSON.toJSONString(object, config);
	}

	public static Object toBean(String text) {
		return JSON.parse(text);
	}

	public static <T> T toBean(String text, Class<T> clazz) {
		return JSON.parseObject(text, clazz);
	}

	// 转换为数组
	public static <T> Object[] toArray(String text) {
		return toArray(text, null);
	}

	// 转换为数组
	public static <T> Object[] toArray(String text, Class<T> clazz) {
		return JSON.parseArray(text, clazz).toArray();
	}

	// 转换为List
	public static <T> List<T> toList(String text, Class<T> clazz) {
		return JSON.parseArray(text, clazz);
	}


	/**
	 * 将string转化为序列化的json字符串
	 * 
	 * @param keyvalue
	 * @return
	 */
	public static Object textToJson(String text) {
		Object objectJson = JSON.parse(text);
		return objectJson;
	}

	/**
	 * json字符串转化为map
	 * 
	 * @param s
	 * @return
	 */
	public static <K, V> Map<K, V> stringToCollect(String s) {
		Map<K, V> m = (Map<K, V>) JSONObject.parseObject(s);
		return m;
	}

	/**
	 * 转换JSON字符串为对象
	 * 
	 * @param jsonData
	 * @param clazz
	 * @return
	 */
	public static Object convertJsonToObject(String jsonData, Class<?> clazz) {
		return JSONObject.parseObject(jsonData, clazz);
	}

	public static Object convertJSONToObject(String content, Class<?> clazz) {
		return JSONObject.parseObject(content, clazz);
	}

	/**
	 * 将map转化为string
	 * 
	 * @param m
	 * @return
	 */
	public static <K, V> String collectToString(Map<K, V> m) {
		String s = JSONObject.toJSONString(m);
		return s;
	}

	/**
	 * 用fastjson 将jsonString 解析成 List<Map<String,Object>>
	 *
	 * @param jsonString
	 * @return
	 */
	public static List<Map<String, Object>> getListMap(String jsonString) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			// 两种写法
			// list = JSON.parseObject(jsonString, new
			// TypeReference<List<Map<String, Object>>>(){}.getType());

			list = JSON.parseObject(jsonString, new TypeReference<List<Map<String, Object>>>() {
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 将Map转换为对象
	 * 
	 * @param paramMap
	 * @param cls
	 * @return
	 */
	public static <T> T parseMap2Object(Map<String, Object> paramMap, Class<T> cls) {
		return JSONObject.parseObject(JSONObject.toJSONString(paramMap), cls);
	}

}
