package com.giveu.job.common.util;

/**
 * Created by fox on 2019/1/9.
 */
public class ValidUtil {

	public static boolean validField(String field) {
		String regex = "^[0-9a-zA-Z_]{1,}$";
		return field.matches(regex);
	}
}
