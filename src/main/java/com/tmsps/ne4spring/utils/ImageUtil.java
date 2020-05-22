package com.tmsps.ne4spring.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageUtil {
	public byte[] getImageBinary(String filePath) throws IOException {
		File file = new File(filePath);

		FileInputStream fis = new FileInputStream(file);

		byte[] b = new byte[fis.available()];
		fis.read(b);
		return b;
	}

	public String getImageBinaryString(String filePath) throws IOException {
		return byte2hex(getImageBinary(filePath));
	}

	public void saveImageBinary(byte[] fileData, String saveFilePath) throws IOException {
		File file = new File(saveFilePath);
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(fileData);
		fos.flush();
		fos.close();
	}

	public void saveImageBinaryString(String fileData, String saveFilePath) throws IOException {
		saveImageBinary(hex2byte(fileData), saveFilePath);
	}

	public static String byte2hex(byte[] b) {
		StringBuffer sb = new StringBuffer();
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = Integer.toHexString(b[n] & 0xFF);
			if (stmp.length() == 1) {
				sb.append("0" + stmp);
			} else {
				sb.append(stmp);
			}
		}

		return sb.toString();
	}

	public static byte[] hex2byte(String str) {
		if (str == null)
			return null;
		str = str.trim();
		int len = str.length();
		if ((len == 0) || (len % 2 == 1))
			return null;
		byte[] b = new byte[len / 2];
		try {
			for (int i = 0; i < str.length(); i += 2) {
				b[(i / 2)] = ((byte) Integer.decode("0X" + str.substring(i, i + 2)).intValue());
			}
			return b;
		} catch (Exception e) {
		}
		return null;
	}
}
