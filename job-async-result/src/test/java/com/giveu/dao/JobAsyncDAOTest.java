package com.giveu.dao;

import com.giveu.dto.JobAsyncDTO;
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
public class JobAsyncDAOTest {

	@Autowired
	JobAsyncDAO jobAsyncDAO;

	@Test
	public void getJobAsyncDTO() throws Exception {
		JobAsyncDTO dto = jobAsyncDAO.getJobAsyncDTO();
		System.out.println(dto);

	}

}