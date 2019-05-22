package com.giveu.service;

import com.giveu.job.common.vo.ResultModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by fox on 2018/9/5.
 */

@SpringBootTest
@RunWith(SpringRunner.class)
public class AppInfoServiceTest {

	@Autowired
	AppInfoService appInfoService;

	@Test
	public void cacheAppInfoToRedis() throws Exception {

		ResultModel resultModel = new ResultModel();

		appInfoService.cacheAppInfoToRedis(resultModel);
		System.out.println(resultModel);
	}

}