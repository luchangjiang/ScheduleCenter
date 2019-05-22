package com.giveu.controller;

import com.giveu.job.common.vo.ResultModel;
import com.giveu.service.WeCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by fox on 2018/8/22.
 */
@RestController
@RequestMapping(value = "/we/cat/push/")
@CrossOrigin
public class WeCatController {

	@Autowired
	WeCatService weCatService;

	@RequestMapping(value = "job/training")
	public ResultModel training() {
		ResultModel resultModel = new ResultModel();
		resultModel.setCode(200);
		weCatService.training();
		return resultModel;
	}

	@RequestMapping(value = "error/log")
	public ResultModel errorLog() {
		ResultModel resultModel = new ResultModel();
		weCatService.getErrorLog(resultModel);
		return resultModel;
	}
	@RequestMapping(value = "error/log/job")
	public ResultModel errorLogByJobId(@RequestParam(value = "jobId") String jobId) {
		ResultModel resultModel = new ResultModel();
		weCatService.getErrorLogByJobId(jobId, resultModel);
		return resultModel;
	}

	@RequestMapping(value = "detail")
	public ResultModel detail(@RequestParam(value = "id") String id) {
		ResultModel resultModel = new ResultModel();
		weCatService.detail(id, resultModel);
		return resultModel;
	}

	@PostMapping(value = "/trigger/log/id")
	public String triggerByLogId(@RequestParam(value = "id") String id) {
		return weCatService.triggerByLogId(id);
	}



	@RequestMapping(value = "obj/training")
	public ResultModel monitorTraining() {
		ResultModel resultModel = new ResultModel();
		resultModel.setCode(200);
		weCatService.monitorTraining();
		return resultModel;
	}

	@RequestMapping(value = "monitor/detail")
	public ResultModel monitorDetail(@RequestParam(value = "id") String id) {
		ResultModel resultModel = new ResultModel();
		weCatService.monitorDetail(id, resultModel);
		return resultModel;
	}

	@RequestMapping(value = "monitor/error/log")
	public ResultModel monitorErrorLog(HttpServletRequest request) {
		String objIdListJson = request.getParameter("objIdListJson");
		ResultModel resultModel = new ResultModel();
		weCatService.getMonitorLogByObjIdList(objIdListJson, resultModel);
		return resultModel;
	}

	@RequestMapping(value = "monitor/error/log/top")
	public ResultModel monitorLogByObjIdListAndDays(HttpServletRequest request) {
		String objIdListJson = request.getParameter("objIdListJson");
		ResultModel resultModel = new ResultModel();
		weCatService.getMonitorLogByObjIdListAndDays(objIdListJson, resultModel);
		return resultModel;
	}

	@RequestMapping(value = "job/log/stat")
	public ResultModel jobLogStat(HttpServletRequest request) {
		ResultModel resultModel = new ResultModel();
		weCatService.jobLogStat(request, resultModel);
		return resultModel;
	}

	@RequestMapping(value = "job/log/stat/training")
	public ResultModel logStatTraining() {
		ResultModel resultModel = new ResultModel();
		resultModel.setCode(200);
		weCatService.logStatTraining();
		return resultModel;
	}

	@RequestMapping(value = "job/log/stat/init")
	public ResultModel logStatInit() {
		ResultModel resultModel = new ResultModel();
		resultModel.setCode(200);
		weCatService.logStatInit();
		return resultModel;
	}

	@RequestMapping(value = "job/day/stat/training")
	public ResultModel dayStatTraining() {
		ResultModel resultModel = new ResultModel();
		resultModel.setCode(200);
		weCatService.dayStatTraining();
		return resultModel;
	}

	@RequestMapping(value = "trigger/day/stat")
	public ResultModel getQrtzTriggerDayStat(String startTime, String endTime, String account) {
		ResultModel resultModel = new ResultModel();
		resultModel.setCode(200);
		weCatService.getQrtzTriggerDayStat(startTime, endTime, account, resultModel);
//		resultModel.setData(weCatService.getQrtzTriggerDayStat(startTime, endTime, account));
		return resultModel;
	}
//	@RequestMapping(value = "trigger/day/stat/list")
//	public ResultModel getQrtzTriggerDayStatList(String account) {
//		ResultModel resultModel = new ResultModel();
//		resultModel.setCode(200);
//		resultModel.setData(weCatService.getQrtzTriggerDayStatList(account));
//		return resultModel;
//	}

	@RequestMapping(value = "job/top/stat")
	public ResultModel getJobTopStat(@RequestParam("jobIdListJson") String jobIdListJson) {
		ResultModel resultModel = new ResultModel();
		resultModel.setCode(200);
		weCatService.getJobTopStat(jobIdListJson, resultModel);
		return resultModel;
	}
}
