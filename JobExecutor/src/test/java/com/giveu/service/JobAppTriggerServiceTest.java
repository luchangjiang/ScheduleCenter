package com.giveu.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by fox on 2018/9/13.
 */

@SpringBootTest
@RunWith(SpringRunner.class)
public class JobAppTriggerServiceTest {

	@Autowired
	JobAppTriggerService jobAppTriggerService;

	@Test
	public void delJobByName() throws Exception {
		jobAppTriggerService.delJobByName("LogComponentTest8E345DA421CA47F5828C04A53E02B9EA");
	}

	@Test
	public void createTriggerLogTable() throws Exception {
		jobAppTriggerService.createTriggerLogTable();
	}

}