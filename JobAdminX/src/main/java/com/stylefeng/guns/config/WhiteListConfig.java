package com.stylefeng.guns.config;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fox on 2019/1/23.
 */
public class WhiteListConfig {

	public static List<String> list = new ArrayList<>();

	static {
		list.add("10.14.21.93");
		list.add("10.12.13.98");
		list.add("127.0.0.1");
	}
}
