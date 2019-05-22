package com.giveu.controller;

import com.giveu.job.common.vo.ResultModel;
import com.giveu.service.JobService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by fox on 2018/12/1.
 */
@RestController
@RequestMapping(value = "/job")
@CrossOrigin
public class JobController {

	@Autowired
	JobService jobService;

	@RequestMapping(value = "/code")
	public ResultModel getJobByCode(@Param("jobCode") String jobCode) {
		return jobService.getJobByCode(jobCode);
	}

	@RequestMapping(value = "/log/list")
	@ResponseBody
	public ResultModel logList(HttpServletRequest request) throws Exception {
		ResultModel resultModel = new ResultModel();
		jobService.logList(request, resultModel);
		return resultModel;
	}
}
