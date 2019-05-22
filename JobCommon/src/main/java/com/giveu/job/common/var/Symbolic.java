package com.giveu.job.common.var;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by fox on 2018/7/21.
 */
public class Symbolic {

	public static final String EQUAL = "0";
	public static final String NOT_EQUAL = "-11";
	public static final String GREATER_THAN = "1";
	public static final String LESS_THAN = "-1";
	public static final String GREATER_THAN_OR_EQUAL = "10";
	public static final String LESS_THAN_OR_EQUAL = "-10";

	public static Map<String, String> symbolicMap = new HashMap<>();

	static {
		symbolicMap.put(EQUAL, "等于");
		symbolicMap.put(NOT_EQUAL, "不等于");
		symbolicMap.put(GREATER_THAN, "大于");
		symbolicMap.put(LESS_THAN, "小于");
		symbolicMap.put(GREATER_THAN_OR_EQUAL, "大于等于");
		symbolicMap.put(LESS_THAN_OR_EQUAL, "小于等于");
	}
}
