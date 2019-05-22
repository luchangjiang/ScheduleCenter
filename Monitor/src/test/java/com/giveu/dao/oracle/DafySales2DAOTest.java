package com.giveu.dao.oracle;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * Created by fox on 2018/7/23.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class DafySales2DAOTest {

	@Autowired
	@SuppressWarnings("all")
	DafySales2DAO dafySales2DAO;

	@Test
	public void getContractActiveOnlyId() {
		List<Integer> list = dafySales2DAO.getContractActiveOnlyId();
		System.out.println(ArrayUtils.toString(list));

	}

}