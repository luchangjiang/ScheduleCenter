package com.giveu.service.impl;

import com.giveu.job.common.vo.ResultModel;
import com.giveu.service.MonitorCheckOffService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by fox on 2018/7/25.
 */

@SpringBootTest
@RunWith(SpringRunner.class)
public class MonitorCheckOffServiceImplTest {

	@Autowired
	MonitorCheckOffService monitorCheckOffService;


	@Test
	public void handle() throws Exception {
		ResultModel resultModel = new ResultModel();
		monitorCheckOffService.handle(resultModel);
		System.out.println(resultModel.getCode());
	}

}