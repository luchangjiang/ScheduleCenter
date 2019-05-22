package com.giveu.com.giveu.service.impl;

import com.giveu.com.giveu.service.LogTestService;
import org.springframework.stereotype.Service;

/**
 * Created by fox on 2018/8/22.
 */
@Service
public class LogTestServiceImpl implements LogTestService {

	@Override
	public void foo1() {

		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void foo2() {

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
	@Override
	public void foo3() {

		int i = 1/0;
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
}
