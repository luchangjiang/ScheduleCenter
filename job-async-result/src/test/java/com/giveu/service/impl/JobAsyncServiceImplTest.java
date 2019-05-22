package com.giveu.service.impl;

import com.giveu.job.common.vo.ResultModel;
import com.giveu.service.JobAsyncService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by fox on 2019/1/3.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class JobAsyncServiceImplTest {

	@Autowired
	JobAsyncService jobAsyncService;


	@Test
	public void queueHandler() throws Exception {

		while (true) {
			Thread.sleep(1000);
			ResultModel resultModel = jobAsyncService.queueHandler();
			System.out.println(resultModel);
		}

	}

}