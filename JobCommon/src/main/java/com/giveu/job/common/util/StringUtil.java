package com.giveu.job.common.util;

/**
 * Created by fox on 2018/9/13.
 */
public class StringUtil {

	public static boolean isEmptyBatch(Object... objects){
		for (Object object : objects) {
			if (object == null) {
				return true;
			}
			if (object instanceof String && "".equals(object)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isNotEmptyBatch(Object... objects){
		for (Object object : objects) {
			if (object == null) {
				return false;
			}
			if (object instanceof String && "".equals(object)) {
				return false;
			}
		}
		return true;
	}
}
