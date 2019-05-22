package com.giveu.dao.redis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by fox on 2018/7/20.
 */

@SpringBootTest
@RunWith(SpringRunner.class)
public class RedisUtilTest {

	@Autowired
	RedisUtil redisUtil;

	String key = "giveu_test_giveu_tset_xxxxx";

	@Test
	public void set() {
		redisUtil.set("monitor_test_name", 33, 1000);
	}

	@Test
	public void getExpire() throws Exception {
		redisUtil.set("monitor_test_name", 33, 1000);
		Long time = redisUtil.getExpire("monitor_test_name");
		System.out.println(time);
	}

	@Test
	public void get() {
		redisUtil.set(key, "test11", 10000);
		System.out.println(redisUtil.get(key));
	}

}