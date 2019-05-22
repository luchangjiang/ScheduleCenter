package com.giveu.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by fox on 2018/9/17.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class JobAppTriggerDaoTest {

	@Autowired
	JobAppTriggerDao jobAppTriggerDao;

	@Test
	public void createTriggerLogTable() throws Exception {
		jobAppTriggerDao.createTriggerLogTable("QRTZ_TRIGGER_LOG");
	}

}