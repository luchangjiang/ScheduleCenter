package com.giveu.controller;

import com.giveu.job.common.vo.ResultModel;
import com.giveu.service.JobCommandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by fox on 2019/1/18.
 */
@RestController
@RequestMapping("/command")
public class JobCommandController {

	@Autowired
	JobCommandService jobCommandService;

	@RequestMapping("/jar/list")
	public ResultModel list() {
		return jobCommandService.jarList();
	}
	@RequestMapping("/start")
	public ResultModel start(String proName, String proPort, String proActive, String passwd) {
		return jobCommandService.start(proName, proPort, proActive, passwd);
	}
	@RequestMapping("/stop")
	public ResultModel stop(String proName, String proPort, String proActive, String passwd) {
		return jobCommandService.stop(proName, proPort, proActive, passwd);
	}
	@RequestMapping("/restart")
	public ResultModel restart(String proName, String proPort, String proActive, String passwd) {
		return jobCommandService.restart(proName, proPort, proActive, passwd);
	}
	@RequestMapping("/status")
	public ResultModel status(String proName, String proPort, String proActive) {
		return jobCommandService.status(proName, proPort, proActive);
	}
}
