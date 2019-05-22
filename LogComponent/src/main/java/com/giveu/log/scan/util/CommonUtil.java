package com.giveu.log.scan.util;

import java.util.UUID;

/**
 * Created by fox on 2018/6/28.
 */
public class CommonUtil {

	public static String getUUID32(){
		String uuid = UUID.randomUUID().toString().replace("-", "").toUpperCase();
		return uuid;
	}

	public static void main(String[] args) {
		System.out.println(getUUID32());
	}
}
