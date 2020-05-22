package com.tmsps.ne4spring.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.sql.Timestamp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileUtil {
	private static final Logger log = LoggerFactory.getLogger(FileUtil.class);

	/************************************************************
	 * 
	 * 
	 * 文件夹工具
	 * 
	 * 
	 * 
	 *************************************************************/
	/**
	 * 创建文件夹，含不存在的父目录
	 */
	public static boolean createDirs(String Dir_Path) {
		File file = new File(Dir_Path);
		if (file.isDirectory()) {
			log.debug("文件夹已经存在无需创建");
			return true;
		} else {
			try {
				file.mkdirs();
				log.debug("文件夹创建完毕");
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				log.warn("文件夹创建失败:{}", e.toString());
				return false;
			}
		}
	}

	/**
	 * 删除文件夹
	 * 
	 * @param Dir_Path
	 * @return
	 */
	public static boolean DelDir(String Dir_Path) {
		File file = new File(Dir_Path);
		if (file.isDirectory()) {
			return file.delete();
		} else {
			return true;
		}
	}

	/************************************************************
	 * 
	 * 
	 * 文件工具
	 * 
	 * 
	 * 
	 *************************************************************/
	/**
	 * 创建文件
	 * 
	 * @param File_Path
	 * @return
	 */
	public static boolean CreateFile(String File_Path) {
		File f = new File(File_Path);
		if (f.exists()) {
			return true;
		} else {
			try {
				return f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				log.warn("文件夹创建失败:{}", e.toString());
				return false;
			}
		}
	}

	/**
	 * 写文件
	 */
	public static void WriteFile(String File_Path, String txtValue) {
		File file = new File(File_Path);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				log.warn("文件夹创建失败:{}", e.toString());
			}
		}
		/**
		 * 开始写文件
		 */
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
			out.write(txtValue);
			out.flush();
			log.debug("文件写入完成");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * 写追加文件
	 * 
	 * @param File_Path
	 * @param txtValue
	 */
	public static void WriteAddFile(String File_Path, String txtValue) {
		File file = new File(File_Path);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				log.debug("文件创建失败 :" + e);
			}
		}
		/**
		 * 开始写文件
		 */
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true)));
			out.write(txtValue);
			out.write("\r\n");
			out.flush();
			log.debug("文件写入完成");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 文件转换String
	 * 
	 * @return
	 */
	public static String FileToString(String File_Path) {
		File file = new File(File_Path);
		BufferedReader reader = null;
		StringBuilder sb = new StringBuilder();
		String tmpString = null;
		log.debug("开始读取文件：" + file.getName());
		try {
			reader = new BufferedReader(new FileReader(file));
			while ((tmpString = reader.readLine()) != null) {
				sb.append(tmpString);
			}
		} catch (Exception e) {
			log.debug("文件[" + file.getName() + "]读取失败" + e);
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	/************************************************************
	 * 
	 * 
	 * 文件工具
	 * 
	 * 
	 * 
	 *************************************************************/
	/**
	 * 删除文件
	 * 
	 * @param File_Path
	 * @return
	 */
	public static boolean DelFile(String File_Path) {
		File file = new File(File_Path);
		if (file.exists()) {
			return file.delete();
		} else {
			log.debug("文件不存在");
			return true;
		}

	}

	/**
	 * 删除文件夹下所有文件以及文件
	 * 
	 * @param Dir_Path
	 * @return
	 */
	public static boolean DelAllFilesOnDir(String Dir_Path) {
		File file = new File(Dir_Path);
		try {
			new FileUtil().DleALLFiles(file);
		} catch (Exception e) {
			log.debug("删除文件夹失败 :" + e);
			return false;
		}
		return true;
	}

	/**
	 * 删除所有文件 威力巨大，慎用！
	 * 
	 * @param file
	 */
	public void DleALLFiles(File file) {
		if (file.exists()) {
			if (file.isFile()) {
				file.delete();
				log.debug("删除" + file.getName());
			} else if (file.isDirectory()) {
				File files[] = file.listFiles();
				for (int i = 0; i < files.length; i++) {
					this.DleALLFiles(files[i]);
				}
			}
			file.delete();
			log.debug("删除" + file.getName());
		}
	}

	/**
	 * 获取文件大小 返回的是文件byte大小，可以使用 long/1024D 获取KB
	 * 
	 * @param File_Path
	 * @return
	 */
	public static long GetFileSize(String File_Path) {
		File file = new File(File_Path);
		return file.length();
	}

	/**
	 * 获取文件名,不加后缀
	 * 
	 * @param fname
	 * @return
	 */
	public static String GetFileNameNotSuffix(String fname) {
		if (ChkUtil.isNull(fname)) {
			return "";
		}
		int i = fname.lastIndexOf('.');
		if (i == -1 || i == 0) {
			return fname;
		}
		return fname.substring(0, i);
	}

	/**
	 * 
	 * @param date
	 * @param video_id
	 * @return
	 */
	public static File getTdCode(Timestamp date, String video_id) {
		String fileName = video_id + ".png";
		String path = UploadUtil.getUploadPath(fileName, date);

		File f = new File(path);
		if (!f.exists()) {
			f.mkdirs();
		}
		f = new File(path, fileName);
		try {
			if (!f.exists()) {
				f.createNewFile();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return f;
	}

	public static void close(OutputStream op) {
		if (op != null) {
			try {
				op.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void close(InputStream is) {
		if (is != null) {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

}
