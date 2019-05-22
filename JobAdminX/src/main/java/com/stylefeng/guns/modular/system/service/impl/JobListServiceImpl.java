package com.stylefeng.guns.modular.system.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.giveu.job.common.info.CommonMessage;
import com.giveu.job.common.util.MD5Util;
import com.giveu.job.common.var.Secret;
import com.giveu.job.common.vo.JobTriggerAppVo;
import com.giveu.job.common.vo.ResultModel;
import com.stylefeng.guns.core.shiro.ShiroKit;
import com.stylefeng.guns.modular.system.dao.JobAppTriggerDao;
import com.stylefeng.guns.modular.system.service.IJobListService;
import jodd.http.HttpMultiMap;
import jodd.http.HttpRequest;
import jodd.http.HttpResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 任务列表Dao
 *
 * @author fengshuonan
 * @Date 2018-07-03 17:03:27
 */
@Service
public class JobListServiceImpl implements IJobListService {

	private static final int OK = 200;

//	private static final String dataUrl = "http://localhost:8765/";
//	private static final String dataUrl = "http://localhost:9060/";

	@Value("${data.url}")
	String dataUrl;

	@Autowired
	private JobAppTriggerDao jobAppTriggerDao;

	@Override
	public void list(HttpServletRequest request, List<Map<String, Object>> list) throws IOException {
		String id = request.getParameter("id");
		String jobCode = request.getParameter("jobCode");
		String jobName = request.getParameter("jobName");
		String appKey = request.getParameter("appKey");
		String triggerState = request.getParameter("triggerState");


		String userAccount = ShiroKit.getUser().getAccount();

		JobTriggerAppVo jobTriggerAppVo = new JobTriggerAppVo();
		if (StringUtils.isNotBlank(id)) {
			jobTriggerAppVo.setId(id);
		}
		if (StringUtils.isNotBlank(jobCode)) {
			jobTriggerAppVo.setJobCode(jobCode);
		}
		if (StringUtils.isNotBlank(appKey)) {
			jobTriggerAppVo.setAppKey(appKey);
		}
		if (StringUtils.isNotBlank(jobName)) {
			jobTriggerAppVo.setJobName(jobName);
		}
		if (StringUtils.isNotBlank(triggerState)) {
			jobTriggerAppVo.setTriggerState(triggerState);
		}
		if (StringUtils.isNotBlank(userAccount)) {
			jobTriggerAppVo.setUserAccount(userAccount);
		}

		List<JobTriggerAppVo> jobTriggerAppVoList = jobAppTriggerDao.getJobTriggerAppList(jobTriggerAppVo);

		for (JobTriggerAppVo obj : jobTriggerAppVoList) {
			String jobId = obj.getId();
			JobTriggerAppVo stat = jobAppTriggerDao.getTriggerCountByJobId(jobId);
			obj.setTriggerCount(stat.getTriggerCount());
			obj.setTriggerFailCount(stat.getTriggerFailCount());
		}

		for (JobTriggerAppVo vo : jobTriggerAppVoList) {
			Map<String, Object> map = new HashMap<>();
			map.put("id", vo.getId());
			map.put("jobName", vo.getJobName());
			map.put("jobDesc", vo.getJobDesc());
			map.put("jobCode", vo.getJobCode());
			map.put("callbackUrl", vo.getCallbackUrl());
			map.put("appKey", vo.getAppKey());
			map.put("cronExpression", vo.getCronExpression());
			map.put("jobCreateTime", vo.getJobCreateTime());
			map.put("triggerCount", vo.getTriggerCount());
			map.put("triggerFailCount", vo.getTriggerFailCount());
			map.put("triggerStateDesc", vo.getTriggerStateDesc());
			list.add(map);
		}

	}

	@Override
	public ResultModel list() {
		List<JobTriggerAppVo> list = jobAppTriggerDao.getJobList();
		ResultModel resultModel = new ResultModel();
		resultModel.setCode(CommonMessage.OK_CODE);
		resultModel.setMessage(CommonMessage.OK_DESC);
		resultModel.setData(list);
		return resultModel;
	}

	@Override
	public ResultModel addJob(JobTriggerAppVo jobTriggerAppVo) {

		HttpRequest httpRequest = HttpRequest.post(dataUrl+"job/add");
		String xGiveuAppKey = jobTriggerAppVo.getAppKey();
		String xGiveuTimestamp = String.valueOf(new Date().getTime());
		String jobName = jobTriggerAppVo.getJobName();
		String jobDesc = jobTriggerAppVo.getJobDesc();
		String callbackUrl = jobTriggerAppVo.getCallbackUrl();
		String cronExpression = jobTriggerAppVo.getCronExpression();
		String jobCode = jobTriggerAppVo.getJobCode();

		Integer jobType = jobTriggerAppVo.getJobType();
		Integer resultWaitTime = jobTriggerAppVo.getResultWaitTime();
		String resultUrl = jobTriggerAppVo.getResultUrl();

		httpRequest.header("xGiveuAppKey", xGiveuAppKey);
		httpRequest.header("xGiveuTimestamp", xGiveuTimestamp);

		HttpMultiMap multiMap = httpRequest.query();
		multiMap.add("jobName", jobName);
		multiMap.add("jobDesc", jobDesc);
		multiMap.add("callbackUrl", callbackUrl);
		multiMap.add("cronExpression", cronExpression);
		multiMap.add("jobCode", jobCode);

		multiMap.add("jobType", jobType);
		multiMap.add("resultWaitTime", resultWaitTime);
		multiMap.add("resultUrl", resultUrl);

		StringBuilder sb = new StringBuilder();
		sb.append("xGiveuAppKey").append(xGiveuAppKey);
		sb.append("xGiveuTimestamp").append(xGiveuTimestamp);
		sb.append("jobName").append(jobName);
		sb.append("jobDesc").append(jobDesc);
		sb.append("callbackUrl").append(callbackUrl);
		sb.append("cronExpression").append(cronExpression);
		sb.append("jobCode").append(jobCode);

		sb.append("jobType").append(jobType);

		Integer AsyncType = 2;
		if (AsyncType.equals(jobType)) {
			sb.append("resultWaitTime").append(resultWaitTime);
			sb.append("resultUrl").append(resultUrl);
		}

		sb.append(Secret.APP_SECRET);

		String original = sb.toString();
		String encrypted = MD5Util.sign(original);

		httpRequest.header("xGiveuSign", encrypted);

		HttpResponse response = httpRequest.send();
		String result = response.bodyText();
		ResultModel resultModel = JSONObject.parseObject(result, ResultModel.class);

		return resultModel;
	}

	@Override
	public ResultModel delJobByCode(String jobCode) {
		HttpRequest httpRequest = HttpRequest.post(dataUrl+"job/del/code");
		return requestHandle(jobCode, httpRequest);
	}

	@Override
	public ResultModel triggerByLogId(String jobLogListId) throws IOException {
		HttpRequest httpRequest = HttpRequest.post(dataUrl+"job/trigger/log/id");
		HttpMultiMap multiMap = httpRequest.query();
		multiMap.add("jobLogListId", jobLogListId);
		HttpResponse response = httpRequest.send();
		String result = response.bodyText();
		ObjectMapper mapper = new ObjectMapper();
		ResultModel resultModel = mapper.readValue(result, ResultModel.class);
		return resultModel;
	}

	@Override
	public ResultModel triggerByJobCode(String jobCode) {
		HttpRequest httpRequest = HttpRequest.post(dataUrl+"job/trigger/code");
		return requestHandle(jobCode, httpRequest);
	}

	@Override
	public ResultModel pauseJob(String jobCode) {
		HttpRequest httpRequest = HttpRequest.post(dataUrl+"job/pause/code");
		return requestHandle(jobCode, httpRequest);
	}

	@Override
	public ResultModel resumeJob(String jobCode) {
		HttpRequest httpRequest = HttpRequest.post(dataUrl+"job/resume/code");
		return requestHandle(jobCode, httpRequest);
	}

	@Override
	public JobTriggerAppVo getJobTriggerAppVo(String jobListId) {
		return jobAppTriggerDao.getJobById(jobListId);
	}

	@Override
	public ResultModel updJob(JobTriggerAppVo jobTriggerAppVo) {
		HttpRequest httpRequest = HttpRequest.post(dataUrl+"job/upd/code");

		String xGiveuAppKey = jobTriggerAppVo.getAppKey();
		String xGiveuTimestamp = String.valueOf(new Date().getTime());
		String jobDesc = jobTriggerAppVo.getJobDesc();
		String callbackUrl = jobTriggerAppVo.getCallbackUrl();
		String cronExpression = jobTriggerAppVo.getCronExpression();
		String jobCode = jobTriggerAppVo.getJobCode();

		Integer jobType = jobTriggerAppVo.getJobType();
		Integer resultWaitTime = jobTriggerAppVo.getResultWaitTime();
		String resultUrl = jobTriggerAppVo.getResultUrl();

		httpRequest.header("xGiveuAppKey", xGiveuAppKey);
		httpRequest.header("xGiveuTimestamp", xGiveuTimestamp);

		HttpMultiMap multiMap = httpRequest.query();
		multiMap.add("jobDesc", jobDesc);
		multiMap.add("callbackUrl", callbackUrl);
		multiMap.add("cronExpression", cronExpression);
		multiMap.add("jobCode", jobCode);

		multiMap.add("jobType", jobType);
		multiMap.add("resultWaitTime", resultWaitTime);
		multiMap.add("resultUrl", resultUrl);

		StringBuilder sb = new StringBuilder();
		sb.append("xGiveuAppKey").append(xGiveuAppKey);
		sb.append("xGiveuTimestamp").append(xGiveuTimestamp);
		sb.append("jobDesc").append(jobDesc);
		sb.append("callbackUrl").append(callbackUrl);
		sb.append("cronExpression").append(cronExpression);
		sb.append("jobCode").append(jobCode);

		sb.append("jobType").append(jobType);

		Integer AsyncType = 2;
		if (AsyncType.equals(jobType)) {
			sb.append("resultWaitTime").append(resultWaitTime);
			sb.append("resultUrl").append(resultUrl);
		}

		sb.append(Secret.APP_SECRET);

		String original = sb.toString();
		String encrypted = MD5Util.sign(original);

		httpRequest.header("xGiveuSign", encrypted);

		HttpResponse response = httpRequest.send();
		String result = response.bodyText();
		ResultModel resultModel = JSONObject.parseObject(result, ResultModel.class);
		return resultModel;
	}

	@Override
	public ResultModel getJobList(String id, String jobCode, String appKey, String jobName, String triggerState, String userAccount) {

		ResultModel resultModel = new ResultModel();
		resultModel.setCode(CommonMessage.OK_CODE);
		resultModel.setMessage(CommonMessage.OK_DESC);
		JobTriggerAppVo jobTriggerAppVo = new JobTriggerAppVo();
		if (StringUtils.isNotBlank(id)) {
			jobTriggerAppVo.setId(id);
		}
		if (StringUtils.isNotBlank(jobCode)) {
			jobTriggerAppVo.setJobCode(jobCode);
		}
		if (StringUtils.isNotBlank(appKey)) {
			jobTriggerAppVo.setAppKey(appKey);
		}
		if (StringUtils.isNotBlank(jobName)) {
			jobTriggerAppVo.setJobName(jobName);
		}
		if (StringUtils.isNotBlank(triggerState)) {
			jobTriggerAppVo.setTriggerState(triggerState);
		}
		if (StringUtils.isNotBlank(userAccount)) {
			jobTriggerAppVo.setUserAccount(userAccount);
		}

		List<JobTriggerAppVo> jobTriggerAppVoList = jobAppTriggerDao.getJobTriggerAppList(jobTriggerAppVo);
		resultModel.setData(jobTriggerAppVoList);
		return resultModel;

	}

	private ResultModel requestHandle(String jobCode, HttpRequest httpRequest) {
		String xGiveuAppKey = "admin";
		String xGiveuTimestamp = String.valueOf(new Date().getTime());

		httpRequest.header("xGiveuAppKey", xGiveuAppKey);
		httpRequest.header("xGiveuTimestamp", xGiveuTimestamp);

		HttpMultiMap multiMap = httpRequest.query();
		multiMap.add("jobCode", jobCode);

		StringBuilder sb = new StringBuilder();
		sb.append("xGiveuAppKey").append(xGiveuAppKey);
		sb.append("xGiveuTimestamp").append(xGiveuTimestamp);
		sb.append("jobCode").append(jobCode);
		sb.append(Secret.APP_SECRET);

		String original = sb.toString();
		String encrypted = MD5Util.sign(original);

		httpRequest.header("xGiveuSign", encrypted);
		ObjectMapper mapper = new ObjectMapper();
		HttpResponse response = httpRequest.send();
		String result = response.bodyText();
		ResultModel resultModel = null;
		try {
			resultModel = mapper.readValue(result, ResultModel.class);
		} catch (IOException e) {
			resultModel = new ResultModel();
			resultModel.setCode(CommonMessage.ERROR_CODE);
			e.printStackTrace();
		}

		return resultModel;
	}



}
