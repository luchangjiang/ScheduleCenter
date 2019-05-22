package com.giveu.job.scan.service;

import com.giveu.job.scan.model.ResultModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by fox on 2018/12/24.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class JobSdkServiceTest {

	@Autowired
	JobSdkService jobSdkService;

	@Test
	public void addSyncJob() throws Exception {

		ResultModel resultModel =
				jobSdkService.addSyncJob("JobSdkTest133",
						"job_sdk_test1233",
						"Test...xxx",
						"*/30 * * * * ?",
						"http://localhost:6089/sign",false);

		System.out.println(resultModel);

	}

	@Test
	public void addAsyncJob() throws Exception {

		ResultModel resultModel =
				jobSdkService.addAsyncJob("JobSdkTest112323xf",
						"job_sdk_test112323xf",
						"Test...",
						"*/30 * * * * ?",
						"http://localhost:6089/sign", 30, "http://127.0.0.1:6089/async/result", false);

		System.out.println(resultModel);

	}

	@Test
	public void updSyncJob() throws Exception {
//		ResultModel resultModel =
//				jobSdkService.updSyncJob(
//						"cc",
//						"Test...xxxx",
//						"*/31 * * * * ?",
//						"http://localhost:6089/sign");

		ResultModel resultModel =
				jobSdkService.updSyncJob(
						"ddd",
						"ddd",
						"xx",
						"xx");

		System.out.println(resultModel);
	}

	@Test
	public void updAsyncJob() throws Exception {
		ResultModel resultModel =
				jobSdkService.updAsyncJob(
						"eee",
						"",
						null,
						"http://localhost:6089/signxff", null, "http://127.0.0.1:6089/async/resultxff");

		System.out.println(resultModel);

	}

	@Test
	public void delJob() throws Exception {
		ResultModel resultModel = jobSdkService.delJob("job_sdk_test123");
		System.out.println(resultModel);
	}

	@Test
	public void pause() throws Exception {
		ResultModel resultModel = jobSdkService.pause("x");
		System.out.println(resultModel);
	}

	@Test
	public void resume() throws Exception {
		ResultModel resultModel = jobSdkService.resume("");
		System.out.println(resultModel);
	}

	@Test
	public void trigger() throws Exception {
		ResultModel resultModel = jobSdkService.trigger("eefe");
		System.out.println(resultModel);
	}

	@Test
	public void resultUpdate() throws Exception {
		ResultModel resultModel = jobSdkService.resultUpdate("A17362744B29488BB5B611368FD053F2x", 2);
		System.out.println(resultModel);
	}
	@Test
	public void getJobDetail() throws Exception {
		ResultModel resultModel = jobSdkService.getJobDetail("eee");
		System.out.println(resultModel);
	}

}