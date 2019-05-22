package com.stylefeng.guns.modular.system.controller;

import com.alibaba.fastjson.JSONObject;
import com.giveu.job.common.vo.ResultModel;
import com.stylefeng.guns.modular.system.service.IJobListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;

/**
 * Created by fox on 2019/1/12.
 */
@RestController
@RequestMapping(value = "/job")
public class JobAdminController {

	@Autowired
	IJobListService jobListService;

	/**
	 *
	 * @param id
	 * @param jobCode
	 * @param appKey
	 * @param jobName
	 * @param triggerState
	 * @param userAccount
	 * @return
	 */
	@RequestMapping(value = "/list")
	public void getJobList(String id, String jobCode, String appKey, String jobName, String triggerState, String userAccount, HttpServletResponse response) throws IOException {

		ResultModel resultModel = jobListService.getJobList(id, jobCode, appKey, jobName, triggerState, userAccount);

		String json = JSONObject.toJSONString(resultModel);

		response.setContentType("application/json; charset=utf-8");

		Writer writer = response.getWriter();
		writer.write(json);
		writer.flush();
		writer.close();
	}
}
