package com.giveu.service.impl;

import com.giveu.job.common.vo.ResultModel;
import com.giveu.service.MonitorCreditActiveService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by fox on 2018/7/26.
 */

@SpringBootTest
@RunWith(SpringRunner.class)
public class MonitorCreditActiveServiceImplTest {

	@Autowired
	MonitorCreditActiveService monitorCreditActiveService;


	@Test
	public void handle() throws Exception {

		while (true) {

			ResultModel resultModel = new ResultModel();
			monitorCreditActiveService.handle(resultModel);
			System.out.println(resultModel.getCode());
			System.out.println(resultModel.getMessage());
			Thread.sleep(5000);
		}

	}

}