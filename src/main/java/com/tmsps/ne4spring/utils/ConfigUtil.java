package com.tmsps.ne4spring.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigUtil extends Properties {
	private static final long serialVersionUID = 1L;

	private ConfigUtil() {
		InputStream in = getClass().getClassLoader().getResourceAsStream("config.properties");
		try {
			load(in);
			return;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static ConfigUtil _instance = new ConfigUtil();

	public static ConfigUtil getConfigReader() {
		return _instance;
	}

	public String get(String key) {
		return getProperty(key);
	}
}
