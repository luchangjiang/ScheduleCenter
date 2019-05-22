package com.stylefeng.guns.modular.system.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by fox on 2019/1/23.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class IDataSyncServiceTest {

	@Autowired
	IDataSyncService dataSyncService;


	@Test
	public void syncAppInfo() throws Exception {
		dataSyncService.syncAppInfo();
	}

	@Test
	public void syncJobInfo() throws Exception {
		dataSyncService.syncJobInfo();
	}

}