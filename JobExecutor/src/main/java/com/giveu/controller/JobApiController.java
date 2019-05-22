package com.giveu.controller;

import com.giveu.component.JobCheckComponent;
import com.giveu.job.common.info.CommonMessage;
import com.giveu.job.common.vo.ResultModel;
import com.giveu.service.JobService;
import com.giveu.vo.JobParamVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by fox on 2019/1/4.
 */
@RestController
@RequestMapping(value = "/job")
public class JobApiController {

	@Autowired
	JobService jobService;

	@Autowired
	JobCheckComponent jobCheckComponent;

	@RequestMapping(value = "/add")
	public ResultModel addJob(JobParamVo paramVo, @RequestHeader("xGiveuAppKey") String xGiveuAppKey, HttpServletRequest request) {
		paramVo.setAppKey(xGiveuAppKey);
		ResultModel resultModel = jobCheckComponent.check(request);
		if (resultModel.getCode().equals(CommonMessage.OK_CODE)) {
			return jobService.addJob(paramVo);
		}
		return resultModel;
	}

	@RequestMapping(value = "/del/code")
	public ResultModel delJobByCode(String jobCode, HttpServletRequest request) {
		ResultModel resultModel = jobCheckComponent.check(request);
		if (resultModel.getCode().equals(CommonMessage.OK_CODE)) {
			return jobService.delJob(jobCode);
		}
		return resultModel;
	}

	@RequestMapping(value = "/upd/code")
	public ResultModel updJobByCode(JobParamVo paramVo, HttpServletRequest request) {
		ResultModel resultModel = jobCheckComponent.check(request);
		if (resultModel.getCode().equals(CommonMessage.OK_CODE)) {
			return jobService.updJob(paramVo);
		}
		return resultModel;
	}

	@RequestMapping(value = "/pause/code")
	public ResultModel pauseJobByCode(@RequestParam(value = "jobCode") String jobCode, HttpServletRequest request) {
		ResultModel resultModel = jobCheckComponent.check(request);
		if (resultModel.getCode().equals(CommonMessage.OK_CODE)) {
			return jobService.pause(jobCode);
		}
		return resultModel;
	}

	@RequestMapping(value = "/resume/code")
	public ResultModel resumeJobByCode(@RequestParam(value = "jobCode") String jobCode, HttpServletRequest request) {
		ResultModel resultModel = jobCheckComponent.check(request);
		if (resultModel.getCode().equals(CommonMessage.OK_CODE)) {
			return jobService.resume(jobCode);
		}
		return resultModel;
	}

	@RequestMapping(value = "/trigger/log/id")
	public ResultModel triggerByLogId(@RequestParam(value = "jobLogListId") String jobLogListId, HttpServletRequest request) {
//		ResultModel resultModel = jobCheckComponent.check(request);
//		if (resultModel.getCode().equals(CommonMessage.OK_CODE)) {
//			return jobService.triggerByLogId(jobLogListId);
//		}
//		return resultModel;

		return jobService.triggerByLogId(jobLogListId);
	}

	@RequestMapping(value = "/trigger/code")
	public ResultModel triggerByCode(@RequestParam(value = "jobCode") String jobCode, HttpServletRequest request) {
		ResultModel resultModel = jobCheckComponent.check(request);
		if (resultModel.getCode().equals(CommonMessage.OK_CODE)) {
			return jobService.triggerByCode(jobCode);
		}
		return resultModel;
	}

	@RequestMapping(value = "/result/update")
	public ResultModel resultUpdate(String sessionId, Integer executeStatus, HttpServletRequest request) {

		ResultModel resultModel = jobCheckComponent.check(request);
		if (resultModel.getCode().equals(CommonMessage.OK_CODE)) {
			return jobService.resultUpdate(sessionId, executeStatus);
		}
		return resultModel;

	}

	@RequestMapping(value = "/detail/code")
	public ResultModel getJobDetail(@RequestParam(value = "jobCode") String jobCode) {
		return jobService.getJobDetail(jobCode);
	}

	@RequestMapping(value = "/lock")
	public ResultModel lock(@RequestParam(value = "isOpen") String isOpen, @RequestParam(value = "passwd") String passwd) {
		return jobService.lock(isOpen, passwd);
	}

	@RequestMapping(value = "/lock/status")
	public ResultModel lockStatus() {
		return jobService.lockStatus();
	}



}
