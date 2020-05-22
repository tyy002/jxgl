package com.tmsps.ne4spring.utils;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;




public class ChkUtil {
	public static boolean isNull(Integer num) {
		if (num == null || num == 0) {
			return true;
		} else {
			return false;
		}
	}// #isNull

	public static boolean isNull(CharSequence str) {
		if (str == null || "".equals(str.toString().trim())) {
			return true;
		} else {
			return false;
		}
	}// #isNull

	public static boolean isNull(Map<?, ?> map) {
		if (map == null || map.isEmpty()) {
			return true;
		} else {
			return false;
		}
	}// #isNull

	// 判断对象是否为空
	// 修正之前 object 如果为空字符串情况
	public static boolean isNull(Object obj) {
		return Objects.isNull(obj) ? true : isNull(obj.toString());
	}// #isNull

	public static boolean isNull(Object... strs) {
		if (strs == null || strs.length == 0) {
			return true;
		} else {
			return false;
		}
	}// #isNull

	public static boolean isNull(String str) {
		return StringUtils.isBlank(str);
	}

	public static boolean isNull(List<?> list) {
		if (list == null || list.size() == 0) {
			return true;
		} else {
			return false;
		}
	}// #isNull

	public static boolean isNotNull(List<?> list) {
		return !isNull(list);
	}// #isNotNull

	public static boolean isNotNull(Object str) {
		return !isNull(str);
	}// #isNotNull

	public static boolean isNotNull(Object... str) {
		return !isNull(str);
	}// #isNotNull

	public static boolean isNotNull(Integer num) {
		return !isNull(num);
	}// #isNotNull

	public static boolean isNotNull(Map<?, ?> map) {
		return !isNull(map);
	}// #isNotNull

	public static boolean isNotNull(String str) {
		return !isNull(str);
	}

	public static boolean listIsNull(List<?> list) {
		return ChkUtil.isNull(list) || list.isEmpty();
	}// #判断集合为空

	public static boolean listIsNotNull(List<?> list) {
		return !listIsNull(list);
	}

	/**
	 * 验证是否为邮箱
	 * 
	 * @see isEMail
	 */
	@Deprecated
	public static boolean isMail(String acc) {
		/**
		 * 判断帐号是否为Email 鉴于现在Email帐号前缀和后缀复杂性，所以判断 @ 和 .} 是否存在以及其的位置
		 * 
		 * @param acc
		 * @return
		 */
		if (acc == null || acc.length() < 5) {
			// #如果帐号小于5位，则肯定不可能为邮箱帐号eg: x@x.x
			return false;
		}
		if (!acc.contains("@")) {// 判断是否含有@符号
			return false;// 没有@则肯定不是邮箱
		}
		String[] sAcc = acc.split("@");
		if (sAcc.length != 2) {// # 数组长度不为2则包含2个以上的@符号，不为邮箱帐号
			return false;
		}
		if (sAcc[0].length() <= 0) {// #@前段为邮箱用户名，自定义的话至少长度为1，其他暂不验证
			return false;
		}
		if (sAcc[1].length() < 3 || !sAcc[1].contains(".")) {
			// # @后面为域名，位数小于3位则不为有效的域名信息
			// #如果后端不包含.则肯定不是邮箱的域名信息
			return false;
		} else {
			if (sAcc[1].substring(sAcc[1].length() - 1).equals(".")) {
				// # 最后一位不能为.结束
				return false;
			}
			String[] sDomain = sAcc[1].split("\\.");
			// #将域名拆分 coowhy.com/ 或者 .com.cn.xxx
			for (String s : sDomain) {
				if (s.length() <= 0) {
					System.err.println(s);
					return false;
				}
			}

		}
		return true;
	}

	/**
	 * 判断是否为电子邮箱
	 * 
	 * @param mail 传入的参数不能为空
	 * @return 是否为合格的邮箱
	 */
	public static boolean isEMail(String mail) {
		if (isNull(mail)) {
			return false;
		} else
			return mail.matches("^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$");
	}

	/**
	 * 验证密码
	 * 
	 * @param arg
	 * @return
	 */
	public static boolean isPassword(String arg) {
		if (isNull(arg)) {
			return false;
		}
		return arg.matches("\\w{6,16}");
	}

	/**
	 * 验证手机号码
	 * 
	 * @param arg
	 * @return
	 */
	public static boolean isMobile(String arg) {
		if (isNull(arg)) {
			return false;
		}
		return arg.matches("13[0-9]{9}$|14[0-9]{9}|15[0-9]{9}$|17[0-9]{9}$|18[0-9]{9}");
	}

	public static boolean isIngteger(String arg) {
		if (isNull(arg)) {
			return false;
		}
		return arg.matches("-{0,1}\\d+");
	}

	public static Integer getInteger(String arg) {
		return getInteger(arg, 0);
	}

	public static Integer getInteger(String arg, Integer defaultVal) {
		if (isIngteger(arg)) {
			return new Integer(arg);
		} else {
			return defaultVal;
		}
	}

	// 如果为空，返回默认值
	public static <T extends CharSequence> T defaultIfNull(final T str, final T defaultVal) {
		return isNull(str) ? defaultVal : str;
	}

}
