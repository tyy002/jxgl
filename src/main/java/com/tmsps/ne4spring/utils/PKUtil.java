package com.tmsps.ne4spring.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.lang3.RandomStringUtils;

public class PKUtil {
	public static void main(String[] args) {
		String orderNO = getOrderNO("yyyyMMddHHmmssSSS", 10);
		System.out.println(orderNO);
	}
	
	public static String getPK() {

		return GenerateUtil.getBase58ID();
	}

	public static String getOrderNO() {
		return getOrderNO("yyyyMMddHHmmssSSS", 10);
	}

	public static synchronized String getOrderNO(String datePartten, int randomNumeric) {
		try {
			Thread.sleep(1L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		SimpleDateFormat sd = new SimpleDateFormat(datePartten);
		return sd.format(new Date()) + RandomStringUtils.randomNumeric(randomNumeric);
	}
}
