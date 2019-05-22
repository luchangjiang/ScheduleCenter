package com.giveu.controller;

import com.giveu.job.common.vo.ResultModel;
import com.giveu.service.JobHealthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by fox on 2019/1/12.
 */
@RestController
@RequestMapping("/health")
public class JobHealthController {

	private static Logger logger = LoggerFactory.getLogger(JobHealthController.class);

	@Autowired
	JobHealthService jobHealthService;

//	@JobCallBackSignValid
	@RequestMapping("/heartbeat")
	public ResultModel heartbeat() {
		return jobHealthService.heartbeat();
	}

	@RequestMapping("/pro/list")
	public ResultModel proList() {
		return jobHealthService.proList();
	}

	@RequestMapping("/start")
	public ResultModel start(String proName, String proPort, String proActive, String passwd) {
		return jobHealthService.start(proName, proPort, proActive, passwd);
	}
	@RequestMapping("/stop")
	public ResultModel stop(String proName, String proPort, String proActive, String passwd) {
		return jobHealthService.stop(proName, proPort, proActive, passwd);
	}
	@RequestMapping("/restart")
	public ResultModel restart(String proName, String proPort, String proActive, String passwd) {
		return jobHealthService.restart(proName, proPort, proActive, passwd);
	}

	@RequestMapping("/trigger/log")
	public ResultModel triggerLog() {
		return jobHealthService.triggerLog();
	}

	@RequestMapping("/heartbeat/log")
	public ResultModel heartbeatLog() {
		return jobHealthService.heartbeatLog();
	}

	@RequestMapping("/running/list")
	public ResultModel runningList() {
		return jobHealthService.runningList();
	}

	@RequestMapping("/schedule/state")
	public ResultModel scheduleState() {
		return jobHealthService.scheduleState();
	}

	@RequestMapping(value = "/lock")
	public ResultModel lock(@RequestParam(value = "isOpen") String isOpen, @RequestParam(value = "passwd") String passwd) {
		return jobHealthService.lock(isOpen, passwd);
	}

	@RequestMapping(value = "/lock/status")
	public ResultModel lockStatus() {
		return jobHealthService.lockStatus();
	}


}
