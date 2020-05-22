package com.tmsps.ne4spring.utils;

import java.io.PrintStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.codec.digest.DigestUtils;

public class SHAEncrypt {
	public static String shax(String shax, String strSrc) {
		MessageDigest md = null;
		String strDes = null;
		byte[] bt = strSrc.getBytes();
		try {
			md = MessageDigest.getInstance(shax);
			md.update(bt);
			strDes = bytes2Hex(md.digest());
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
		return strDes;
	}

	public static String bytes2Hex(byte[] bts) {
		String des = "";
		String tmp = null;
		for (int i = 0; i < bts.length; i++) {
			tmp = Integer.toHexString(bts[i] & 0xFF);
			if (tmp.length() == 1) {
				des = des + "0";
			}
			des = des + tmp;
		}
		return des;
	}

	public static void main(String[] args) {
		System.err.println(DigestUtils.sha256Hex("admin"));
		System.err.println(shax("SHA-256", "admin"));
	}
}
