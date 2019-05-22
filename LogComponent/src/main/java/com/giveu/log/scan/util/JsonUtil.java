package com.giveu.log.scan.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;

/**
 * JSON 工具类
 * Created by fox on 2018/8/21.
 */
public class JsonUtil {

	/**
	 * 对象转首字母大写Json
	 * @param object
	 * @return
	 */
	public static String toCapitalizeJson(Object object) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setPropertyNamingStrategy(new PropertyNamingStrategy() {
			private static final long serialVersionUID = 1L;
			// 反序列化时调用
			@Override
			public String nameForSetterMethod(MapperConfig<?> config,
											  AnnotatedMethod method, String defaultName) {
				return method.getName().substring(3);
			}
			// 序列化时调用
			@Override
			public String nameForGetterMethod(MapperConfig<?> config,
											  AnnotatedMethod method, String defaultName) {
				return method.getName().substring(3);
			}
		});

		try {
			return mapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return "{}";
	}

	/**
	 * 对象转Json
	 * @param object
	 * @return
	 */
	public static String toJson(Object object) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return "{}";
	}
}
