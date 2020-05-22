package com.tmsps.ne4spring.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtil {
	public static String getTiemModle(Date date, String modle) {
		SimpleDateFormat sf = new SimpleDateFormat(modle);
		return sf.format(date);
	}

	public static Timestamp getCurrentTimestamp() {
		return Timestamp.valueOf(getCurrentTime("yyyy-MM-dd HH:mm:ss"));
	}

	public static String getCurrentTime(String model) {
		return getTiemModle(new Date(), model);
	}

	public static String getNextTime(String model) {
		return getTiemModle(new Date(new Date().getTime() + 86400000L), model);
	}

	public static long getDiffTime(String frmtime) throws Exception {
		long currnt_time = new Date().getTime();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long des_time = 0L;
		long diff_time = 0L;
		try {
			des_time = sf.parse(frmtime).getTime();
		} catch (ParseException e) {
			throw new Exception(e.toString());
		}
		if (des_time > currnt_time) {
			diff_time = (des_time - currnt_time) / 1000L;
		}
		return diff_time;
	}

	public static String getTimeaddMonth(int addmonth, String model) {
		Calendar cal = Calendar.getInstance();
		cal.add(2, addmonth);
		return getTiemModle(cal.getTime(), model);
	}

	public static String getTimeaddMinute(int count, String model) {
		Calendar cal = Calendar.getInstance();
		cal.add(12, count);
		return getTiemModle(cal.getTime(), model);
	}

	public static String getTimeaddDay(int addday, String model) {
		Calendar cal = Calendar.getInstance();
		cal.add(5, addday);
		return getTiemModle(cal.getTime(), model);
	}

	public static String getTimeaddMonth(String special_date, int addmonth, String model) throws ParseException {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		cal.setTime(sf.parse(special_date));
		cal.add(2, addmonth);
		return getTiemModle(cal.getTime(), model);
	}

	public static String getTimeaddDay(String special_date, int addday, String model) throws ParseException {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		cal.setTime(sf.parse(special_date));
		cal.add(5, addday);
		return getTiemModle(cal.getTime(), model);
	}
}
