package com.tmsps.ne4spring.utils;

public class StrUtil {
	public static String getTagsStr(String tags) {
		if (tags.length() == 0) {
			return tags;
		}
		String[] strs = tags.split(",");
		StringBuilder sb = new StringBuilder();
		for (String str : strs) {
			sb.append("'").append(str).append("',");
		}
		return sb.deleteCharAt(sb.length() - 1).toString();
	}

	public static String firstCharToLowerCase(String str) {
		char firstChar = str.charAt(0);
		if ((firstChar >= 'A') && (firstChar <= 'Z')) {
			char[] arr = str.toCharArray();
			int tmp25_24 = 0;
			char[] tmp25_23 = arr;
			tmp25_23[tmp25_24] = ((char) (tmp25_23[tmp25_24] + ' '));
			return new String(arr);
		}
		return str;
	}

	public static String firstCharToUpperCase(String str) {
		char firstChar = str.charAt(0);
		if ((firstChar >= 'a') && (firstChar <= 'z')) {
			char[] arr = str.toCharArray();
			int tmp25_24 = 0;
			char[] tmp25_23 = arr;
			tmp25_23[tmp25_24] = ((char) (tmp25_23[tmp25_24] - ' '));
			return new String(arr);
		}
		return str;
	}

	public static boolean isBlank(String str) {
		return (str == null) || ("".equals(str.trim()));
	}

	public static boolean notBlank(String str) {
		return (str != null) && (!"".equals(str.trim()));
	}

	public static boolean notBlank(String... strings) {
		if (strings == null)
			return false;
		for (String str : strings)
			if ((str == null) || ("".equals(str.trim())))
				return false;
		return true;
	}

	public static boolean notNull(Object... paras) {
		if (paras == null)
			return false;
		for (Object obj : paras)
			if (obj == null)
				return false;
		return true;
	}

	public static String toInStr(String strs) {
		if (ChkUtil.isNull(strs)) {
			return "";
		}
		String[] origialStr = strs.split(",");
		StringBuilder SB = new StringBuilder();
		for (String string : origialStr) {
			SB.append("'").append(string).append("'").append(",");
		}
		return SB.subSequence(0, SB.length() - 1).toString();
	}

	public static String toInStr(String... strs) {
		StringBuilder SB = new StringBuilder();
		for (String string : strs) {
			SB.append("'").append(string).append("'").append(",");
		}
		return SB.subSequence(0, SB.length() - 1).toString();
	}

	public static String toLikeOrStr(String strs) {
		String[] origialStr = strs.split(",");
		for (int i = 0; i < origialStr.length; i++) {
			origialStr[i] = ("\"%" + origialStr[i] + "%\"");
		}
		return String.join(" or ", origialStr);
	}
}
