package com.tmsps.ne4spring.utils;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5Util {
	private static final String MD5_SALT = "NE_CORE";

	public static String MD5(String str, String salt) {
		return DigestUtils.md5Hex(str.concat(salt));
	}

	public static String MD5(String str) {
		return MD5(str, "NE_CORE");
	}

	public static String MD5Normal(String str) {
		return DigestUtils.md5Hex(str);
	}
}
