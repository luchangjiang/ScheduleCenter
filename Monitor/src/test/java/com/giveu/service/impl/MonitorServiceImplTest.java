package com.giveu.service.impl;

import com.giveu.dao.redis.RedisUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by fox on 2018/7/24.
 */

@SpringBootTest
@RunWith(SpringRunner.class)
public class MonitorServiceImplTest {

	String key = "giveu_test_xxxxxxyyyy";

	@Autowired
	RedisUtil redisUtil;

	@Test
	public void testRedis() {
		redisUtil.set(key, "89", 100000);
		System.out.println(redisUtil.get(key));

	}

}