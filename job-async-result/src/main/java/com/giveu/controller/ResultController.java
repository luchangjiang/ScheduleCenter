package com.giveu.controller;

import com.giveu.job.common.vo.ResultModel;
import com.giveu.service.JobAsyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by fox on 2019/1/7.
 */
@RestController
@RequestMapping("/job/result/")
public class ResultController {

	@Autowired
	JobAsyncService jobAsyncService;

	@RequestMapping("/queue")
	public ResultModel queueHandler() {
		return jobAsyncService.queueHandler();
	}

//	@RequestMapping("/update")
//	public ResultModel resultUpdate(String sessionId, Integer executeStatus) {
//		return jobAsyncService.resultUpdate(sessionId, executeStatus);
//	}
}
