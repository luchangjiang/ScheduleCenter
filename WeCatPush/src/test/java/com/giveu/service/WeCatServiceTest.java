package com.giveu.service;

import com.alibaba.fastjson.JSONObject;
import com.giveu.job.common.vo.ResultModel;
import com.giveu.model.TopFrequencyModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fox on 2018/9/3.
 */

@SpringBootTest
@RunWith(SpringRunner.class)
public class WeCatServiceTest {





	@Autowired
	WeCatService weCatService;

	@Test
	public void training() throws Exception {
		weCatService.training();
	}

	@Test
	public void logStatTraining() throws Exception {
		weCatService.logStatTraining();
	}

	@Test
	public void logStatInit() throws Exception {
		weCatService.logStatInit();
	}

	@Test
	public void topStatTraining() throws Exception {
		weCatService.topStatTraining();
	}

	@Test
	public void dayStatTraining() {
		weCatService.dayStatTraining();
	}

	@Test
	public void getQrtzTriggerDayStat() {
//		List<QrtzTriggerDayStat> list = weCatService.getQrtzTriggerDayStat("1537718400000", "1537804800000");
//		for (QrtzTriggerDayStat q : list) {
//			System.out.println(q.getJobCode());
//		}
	}

	@Test
	public void testtt() {

		String json = "{\"days\":30,\"jobCount\":50,\"jobMaxCount\":1412957,\"jobMaxName\":\"日志存储服务& #40;开发、测试& #41;\",\"jobMinCount\":1,\"jobMinName\":\"静默开户监控\",\"triggerAvg\":40025,\"triggerCount\":2001254}";

		TopFrequencyModel topFrequencyModel = JSONObject.parseObject(json, TopFrequencyModel.class);

		System.out.println(topFrequencyModel.getDays());

	}


	@Test
	public void getJobTopStat() throws Exception {

		List<String> list = new ArrayList<>();
		list.add("1053011B41B148C28E996946458698E0");
		list.add("22A48686D7E64004A627559C1F6410C3");
		list.add("3A15DB99A68F48F99A5702FC94B4EAA1");
		list.add("3FE6F723CC244CEBB555A0CBEB8B8224");
		list.add("43D5FE9691894841A84C140513A8A827");
		list.add("5A916778FC2C47FBBC262EA2C054CFD2");
		list.add("5F5897ECB75745E2A88C5221F8A18A64");
		list.add("607D9DDB72B04A3ABD880293DE07BA90");
		list.add("6974ACDD99A04C6798EBA0FBB4B07079");

		String jobIdListJson = JSONObject.toJSONString(list);

		ResultModel resultModel = new ResultModel();

		weCatService.getJobTopStat(jobIdListJson, resultModel);

		String json = JSONObject.toJSON(resultModel.getData()).toString();
		System.out.println(json);

	}
}