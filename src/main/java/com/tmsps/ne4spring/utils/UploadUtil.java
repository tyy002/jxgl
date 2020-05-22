package com.tmsps.ne4spring.utils;

import java.io.File;
import java.util.Date;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UploadUtil {
	static Logger log = LoggerFactory.getLogger(UploadUtil.class);

	public static String _MEDIA_IMG = ".jpg,.jpeg,.png,.gif,.bmp";
	public static String _MEDIA_VIDEO = ".flv,.avi,.mp4";
	public static String _MEDIA_TXT = ".txt";
	public static String BASEPATH = File.separator;

	public static String getRealPath(HttpServletRequest request) {
		return request.getServletContext().getRealPath("/");
	}

	public static String getUploadPath(String savePath) {
		StringBuffer SB = new StringBuffer(savePath);
		SB.append(TimeUtil.getCurrentTime("yyyy/MM/dd/"));
		return SB.toString();
	}

	public static String getUploadPath(String fileName, Date date) {
		StringBuffer SB = new StringBuffer(BASEPATH);
		String fileSuffix = checkSuffix(fileName);
		Integer type = MatchType(fileSuffix);
		String _timestamp = TimeUtil.getTiemModle(date, "yyyy/MM/dd");
		switch (type.intValue()) {
		case 0:
			SB.append("pic/").append(_timestamp).append("/");
			break;
		case 1:
			SB.append("video/").append(_timestamp).append("/");
			break;
		case 2:
			SB.append("txt/").append(_timestamp).append("/");
			break;
		default:
			SB.append("other/").append(_timestamp).append("/");
		}

		return SB.toString();
	}

	public static Integer MatchType(String type) {
		if (type == null)
			return Integer.valueOf(9);
		if (_MEDIA_IMG.contains(type.toLowerCase()))
			return Integer.valueOf(0);
		if (_MEDIA_VIDEO.contains(type.toLowerCase()))
			return Integer.valueOf(1);
		if (_MEDIA_TXT.contains(type.toLowerCase())) {
			return Integer.valueOf(2);
		}
		return Integer.valueOf(9);
	}

	protected static String reFileName() {
		return PKUtil.getPK();
	}

	public static String getNewFileName(String fileName) {
		StringBuffer SB = new StringBuffer(reFileName());
		SB.append(checkSuffix(fileName));
		return SB.toString();
	}

	public static String checkSuffix(String fileName) {
		int no = fileName.lastIndexOf(".");
		if ((no == -1) || (no == fileName.length() - 1)) {
			return ".nosuffix";
		}
		return fileName.substring(no);
	}

	public static boolean moveFile(String sourceFile, String targetPath, String newFileName) {
		File file = new File(sourceFile);
		if (!file.exists()) {
			return false;
		}
		File dir = new File(targetPath);
		FileUtil.createDirs(targetPath);
		boolean b = file.renameTo(new File(dir, newFileName));
		return b;
	}

	public static String getSuffixType(String fileName) {
		String suffix = checkSuffix(fileName);
		Integer type = MatchType(suffix);
		switch (type.intValue()) {
		case 0:
			return "image";
		case 1:
			return "video";
		case 2:
			return "txt";
		}
		return "other";
	}
}
