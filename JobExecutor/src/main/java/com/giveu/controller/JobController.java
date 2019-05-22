package com.giveu.controller;

import com.giveu.job.common.info.CommonMessage;
import com.giveu.job.common.vo.AppVo;
import com.giveu.job.common.vo.JobTriggerAppVo;
import com.giveu.job.common.vo.ResultModel;
import com.giveu.service.JobAppTriggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

//@CrossOrigin
//@RestController
//@RequestMapping(value = "/job")
public class JobController {
	@Autowired
	private JobAppTriggerService jobAppTriggerService;

	@RequestMapping(value = "/list")
	@ResponseBody
	public ResultModel jobList(HttpServletRequest request) throws Exception {
		ResultModel resultModel = new ResultModel();
		jobAppTriggerService.list(request, resultModel);
		return resultModel;
	}
	@RequestMapping(value = "/log/list")
	@ResponseBody
	public ResultModel logList(HttpServletRequest request) throws Exception {
		ResultModel resultModel = new ResultModel();
		jobAppTriggerService.logList(request, resultModel);
		return resultModel;
	}
	@PostMapping(value = "/app/list")
	@ResponseBody
	public ResultModel appList(HttpServletRequest request) throws Exception {
		ResultModel resultModel = new ResultModel();
		jobAppTriggerService.appList(request, resultModel);
		return resultModel;
	}
	@PostMapping(value = "/app/list/account")
	@ResponseBody
	public ResultModel getListJsonByAccount(String account) throws Exception {
		ResultModel resultModel = new ResultModel();
		jobAppTriggerService.getListJsonByAccount(account, resultModel);
		return resultModel;
	}

	@RequestMapping(value = "/app/add")
	@ResponseBody
	public ResultModel addApp(HttpServletRequest request) throws Exception {
		ResultModel resultModel = new ResultModel();
		jobAppTriggerService.addApp(request, resultModel);
		return resultModel;
	}
	@RequestMapping(value = "/app/create/table")
	public ResultModel createTriggerLogTable() {
		ResultModel resultModel = new ResultModel();
		resultModel.setCode(CommonMessage.OK_CODE);
		jobAppTriggerService.createTriggerLogTable();
		return resultModel;
	}

	@RequestMapping(value = "/app/get/id")
	@ResponseBody
	public AppVo getAppVoById(@RequestParam(value = "appId") String appId) throws Exception {
		return jobAppTriggerService.getAppVoById(appId);
	}

	@RequestMapping(value = "/app/upd/id")
	@ResponseBody
	public ResultModel updAppById(HttpServletRequest request) throws Exception {
		ResultModel resultModel = new ResultModel();
		jobAppTriggerService.updAppById(request, resultModel);
		return resultModel;
	}

	@RequestMapping(value = "/app/del/id")
	@ResponseBody
	public Integer delAppById(@RequestParam(value = "appId") String appId) throws Exception {
		return jobAppTriggerService.delAppById(appId);
	}
	@RequestMapping(value = "/app/enable/id")
	@ResponseBody
	public Integer enableById(@RequestParam(value = "appId") String appId) throws Exception {
		return jobAppTriggerService.enableById(appId);
	}
	@RequestMapping(value = "/app/disable/id")
	@ResponseBody
	public Integer disableById(@RequestParam(value = "appId") String appId) throws Exception {
		return jobAppTriggerService.disableById(appId);
	}

	@PostMapping(value = "/add")
	@ResponseBody
	public ResultModel addJob(HttpServletRequest request) throws Exception {
		ResultModel resultModel = new ResultModel();
		Boolean verification = jobAppTriggerService.check(request, resultModel);
		if (verification) {
			jobAppTriggerService.addJob(request, resultModel);
		}
		return resultModel;
	}

	public ResultModel pausejob(@RequestParam(value = "jobName") String jobName, HttpServletRequest request) throws Exception {
		ResultModel resultModel = new ResultModel();
		Boolean verification = jobAppTriggerService.check(request, resultModel);
		if (verification) {
			jobAppTriggerService.pauseJob(jobName);
		}
		return resultModel;
	}
	@RequestMapping(value = "/pause/code")
	public ResultModel pauseJobByCode(@RequestParam(value = "jobCode") String jobCode, HttpServletRequest request) throws Exception {
		ResultModel resultModel = new ResultModel();
		Boolean verification = jobAppTriggerService.check(request, resultModel);
		if (verification) {
			jobAppTriggerService.pauseJobByCode(jobCode, resultModel);
		}
		return resultModel;
	}

	@RequestMapping(value = "/resume/code")
	public ResultModel resumeJobByCode(@RequestParam(value = "jobCode") String jobCode, HttpServletRequest request) throws Exception {
		ResultModel resultModel = new ResultModel();
		Boolean verification = jobAppTriggerService.check(request, resultModel);
		if (verification) {
			jobAppTriggerService.resumeJobByCode(jobCode, resultModel);
		}
		return resultModel;
	}

	public ResultModel resumejob(@RequestParam(value = "jobName") String jobName, HttpServletRequest request) throws Exception {
		ResultModel resultModel = new ResultModel();
		Boolean verification = jobAppTriggerService.check(request, resultModel);
		if (verification) {
			jobAppTriggerService.resumeJob(jobName);
		}
		return resultModel;
	}

	@PostMapping(value = "/del/name")
	public void delJobByName(@RequestParam(value = "jobName") String jobName) throws Exception {
		jobAppTriggerService.delJobByName(jobName);
	}

	@PostMapping(value = "/del/code")
	@ResponseBody
	public ResultModel delJobByCode(HttpServletRequest request) throws Exception {
		ResultModel resultModel = new ResultModel();
		Boolean verification = jobAppTriggerService.check(request, resultModel);
		if (verification) {
			jobAppTriggerService.delJobByCode(request, resultModel);
		}
		return resultModel;
	}
	@PostMapping(value = "/trigger/log/id")
	@ResponseBody
	public ResultModel triggerByLogId(String jobLogListId) throws Exception {
		ResultModel resultModel = new ResultModel();
		jobAppTriggerService.triggerByLogId(resultModel, jobLogListId);
		return resultModel;
	}

	@PostMapping(value = "/trigger/code")
	@ResponseBody
	public ResultModel triggerByJobCode(String jobCode) throws Exception {
		ResultModel resultModel = new ResultModel();
		jobAppTriggerService.triggerByJobCode(resultModel, jobCode);
		return resultModel;
	}

	@PostMapping(value = "/get/id")
	@ResponseBody
	public ResultModel getJobById(String id) throws Exception {
		ResultModel resultModel = new ResultModel();
		jobAppTriggerService.getJobById(id, resultModel);
		return resultModel;
	}
	@PostMapping(value = "/upd")
	@ResponseBody
	public ResultModel updJobById(JobTriggerAppVo jobTriggerAppVo) throws Exception {
		ResultModel resultModel = new ResultModel();
		jobAppTriggerService.updJob(jobTriggerAppVo, resultModel);
		return resultModel;
	}


	@PostMapping(value = "/bind/account")
	@ResponseBody
	public ResultModel bindAccount(String accountListJson, String appKey) throws Exception {
		ResultModel resultModel = new ResultModel();
		jobAppTriggerService.bindAccount(resultModel, accountListJson, appKey);
		return resultModel;
	}
	@PostMapping(value = "/get/bind/account")
	@ResponseBody
	public ResultModel getBindAccount(String appKey) throws Exception {
		ResultModel resultModel = new ResultModel();
		jobAppTriggerService.getBindAccount(resultModel, appKey);
		return resultModel;
	}

	@PostMapping(value = "/result/update")
	public ResultModel resultUpdate(String sessionId, Integer executeStatus) throws Exception {
		return jobAppTriggerService.resultUpdate(sessionId, executeStatus);
	}



	@RequestMapping("/health")
	public String isHealth(){
		return "success";
	}

}
