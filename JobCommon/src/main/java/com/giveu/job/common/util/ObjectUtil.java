package com.giveu.job.common.util;

/**
 * Created by fox on 2018/9/13.
 */
public class ObjectUtil {

	public static boolean isEmptyBatch(Object... objects){
		for (Object object : objects) {
			if(object == null)
				return true;
		}
		return false;
	}

	public static boolean isNotEmptyBatch(Object... objects){
		for (Object object : objects) {
			if(object == null)
				return false;
		}
		return true;
	}
}
