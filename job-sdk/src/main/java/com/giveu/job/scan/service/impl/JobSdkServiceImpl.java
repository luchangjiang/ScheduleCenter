package com.giveu.job.scan.service.impl;

import com.giveu.common.httpclient.component.HttpComponent;
import com.giveu.job.scan.info.CommonMessage;
import com.giveu.job.scan.model.ResultModel;
import com.giveu.job.scan.service.JobSdkService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 调度中心SDK实现
 * Created by fox on 2018/12/24.
 */
@Service
public class JobSdkServiceImpl implements JobSdkService {


	@Value("${http.client.sign.app.key}")
	private String appKey;

	@Value("${http.client.sign.app.secret}")
	private String secret;

	@Value("${job.center.api.url}")
	private String url;

	private String ADD_JOB_PATH;
	private String UPD_JOB_PATH;
	private String DEL_JOB_PATH;
	private String PAUSE_JOB_PATH;
	private String RESUME_JOB_PATH;
	private String TRIGGER_JOB_PATH;
	private String RESULT_UPDATE_JOB_PATH;
	private String DETAIL_JOB_PATH;

	@PostConstruct
	private void init() {
		ADD_JOB_PATH = url + "/job/add";
		UPD_JOB_PATH = url + "/job/upd/code";
		DEL_JOB_PATH = url + "/job/del/code";
		PAUSE_JOB_PATH = url + "/job/pause/code";
		RESUME_JOB_PATH = url + "/job/resume/code";
		TRIGGER_JOB_PATH = url + "/job/trigger/code";
		RESULT_UPDATE_JOB_PATH = url + "/job/result/update";
		DETAIL_JOB_PATH = url + "/job/detail/code";
	}

	@Autowired
	HttpComponent httpComponent;

	@Override
	public ResultModel addSyncJob(String jobName, String jobCode, String jobDesc, String cronExpression, String callbackUrl, Boolean isPause) {
		Map<String, String> map = new HashMap<>();
		map.put("jobName", jobName);
		map.put("jobCode", jobCode);
		map.put("jobDesc", jobDesc);
		map.put("cronExpression", cronExpression);
		map.put("callbackUrl", callbackUrl);
		map.put("jobType", "1");
		if (isPause == null) {
			isPause = false;
		}

		String isPausex;
		if (isPause) {
			isPausex = "1";
		} else {
			isPausex = "0";
		}

		map.put("isPause", isPausex);

		ResultModel resultModel = new ResultModel();
		resultModel.setCode(CommonMessage.ERROR_CODE);
		resultModel.setMessage(CommonMessage.ERROR_DESC);

		try {
			resultModel = httpComponent.doPostFormResultObject(ADD_JOB_PATH, ResultModel.class, map);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return resultModel;
	}

	@Override
	public ResultModel addAsyncJob(String jobName, String jobCode, String jobDesc, String cronExpression, String callbackUrl, Integer resultWaitTime, String resultUrl, Boolean isPause) {
		Map<String, String> map = new HashMap<>();
		map.put("jobName", jobName);
		map.put("jobCode", jobCode);
		map.put("jobDesc", jobDesc);
		map.put("cronExpression", cronExpression);
		map.put("callbackUrl", callbackUrl);
		map.put("resultWaitTime", String.valueOf(resultWaitTime));
		map.put("resultUrl", resultUrl);
		map.put("jobType", "2");
		if (isPause == null) {
			isPause = false;
		}
		String isPausex;
		if (isPause) {
			isPausex = "1";
		} else {
			isPausex = "0";
		}
		map.put("isPause", isPausex);

		ResultModel resultModel = new ResultModel();
		resultModel.setCode(CommonMessage.ERROR_CODE);
		resultModel.setMessage(CommonMessage.ERROR_DESC);

		try {
			resultModel = httpComponent.doPostFormResultObject(ADD_JOB_PATH, ResultModel.class, map);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return resultModel;
	}

	@Override
	public ResultModel updSyncJob(String jobCode, String jobDesc, String cronExpression, String callbackUrl) {

		Map<String, String> map = new HashMap<>();
		if (StringUtils.isNotBlank(jobCode)) {
			map.put("jobCode", jobCode);
		}
		if (StringUtils.isNotBlank(jobDesc)) {
			map.put("jobDesc", jobDesc);
		}
		if (StringUtils.isNotBlank(cronExpression)) {
			map.put("cronExpression", cronExpression);
		}
		if (StringUtils.isNotBlank(callbackUrl)) {
			map.put("callbackUrl", callbackUrl);
		}

		map.put("jobType", "1");

		ResultModel resultModel = new ResultModel();
		resultModel.setCode(CommonMessage.ERROR_CODE);
		resultModel.setMessage(CommonMessage.ERROR_DESC);

		try {
			resultModel = httpComponent.doPostFormResultObject(UPD_JOB_PATH, ResultModel.class, map);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return resultModel;
	}

	@Override
	public ResultModel updAsyncJob(String jobCode, String jobDesc, String cronExpression, String callbackUrl, Integer resultWaitTime, String resultUrl) {
		Map<String, String> map = new HashMap<>();
		if (StringUtils.isNotBlank(jobCode)) {
			map.put("jobCode", jobCode);
		}
		if (StringUtils.isNotBlank(jobDesc)) {
			map.put("jobDesc", jobDesc);
		}
		if (StringUtils.isNotBlank(cronExpression)) {
			map.put("cronExpression", cronExpression);
		}
		if (StringUtils.isNotBlank(callbackUrl)) {
			map.put("callbackUrl", callbackUrl);
		}
		if (resultWaitTime != null && !resultWaitTime.equals(0)) {
			map.put("resultWaitTime", resultWaitTime + "");
		}
		if (StringUtils.isNotBlank(resultUrl)) {
			map.put("resultUrl", resultUrl);
		}
		map.put("jobType", "2");

		ResultModel resultModel = new ResultModel();
		resultModel.setCode(CommonMessage.ERROR_CODE);
		resultModel.setMessage(CommonMessage.ERROR_DESC);

		try {
			resultModel = httpComponent.doPostFormResultObject(UPD_JOB_PATH, ResultModel.class, map);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return resultModel;
	}

	@Override
	public ResultModel delJob(String jobCode) {
		ResultModel resultModel = new ResultModel();
		resultModel.setCode(CommonMessage.ERROR_CODE);
		resultModel.setMessage(CommonMessage.ERROR_DESC);

		Map<String, String> map = new HashMap<>();
		map.put("jobCode", jobCode);

		try {
			resultModel = httpComponent.doPostFormResultObject(DEL_JOB_PATH, ResultModel.class, map);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return resultModel;
	}

	@Override
	public ResultModel pause(String jobCode) {
		Map<String, String> map = new HashMap<>();
		map.put("jobCode", jobCode);

		ResultModel resultModel = new ResultModel();
		resultModel.setCode(CommonMessage.ERROR_CODE);
		resultModel.setMessage(CommonMessage.ERROR_DESC);

		try {
			resultModel = httpComponent.doPostFormResultObject(PAUSE_JOB_PATH, ResultModel.class, map);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return resultModel;
	}

	@Override
	public ResultModel resume(String jobCode) {

		Map<String, String> map = new HashMap<>();
		map.put("jobCode", jobCode);

		ResultModel resultModel = new ResultModel();
		resultModel.setCode(CommonMessage.ERROR_CODE);
		resultModel.setMessage(CommonMessage.ERROR_DESC);

		try {
			resultModel = httpComponent.doPostFormResultObject(RESUME_JOB_PATH, ResultModel.class, map);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return resultModel;
	}

	@Override
	public ResultModel trigger(String jobCode) {
		Map<String, String> map = new HashMap<>();
		map.put("jobCode", jobCode);

		ResultModel resultModel = new ResultModel();
		resultModel.setCode(CommonMessage.ERROR_CODE);
		resultModel.setMessage(CommonMessage.ERROR_DESC);

		try {
			resultModel = httpComponent.doPostFormResultObject(TRIGGER_JOB_PATH, ResultModel.class, map);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return resultModel;
	}

	@Override
	public ResultModel resultUpdate(String sessionId, Integer executeStatus) {

		Map<String, String> map = new HashMap<>();
		map.put("sessionId", sessionId);
		map.put("executeStatus", String.valueOf(executeStatus));

		ResultModel resultModel = new ResultModel();
		resultModel.setCode(CommonMessage.ERROR_CODE);
		resultModel.setMessage(CommonMessage.ERROR_DESC);

		try {
			resultModel = httpComponent.doPostFormResultObject(RESULT_UPDATE_JOB_PATH, ResultModel.class, map);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return resultModel;
	}

	@Override
	public ResultModel getJobDetail(String jobCode) {
		Map<String, String> map = new HashMap<>();
		map.put("jobCode", jobCode);

		ResultModel resultModel = new ResultModel();
		resultModel.setCode(CommonMessage.ERROR_CODE);
		resultModel.setMessage(CommonMessage.ERROR_DESC);

		try {
			resultModel = httpComponent.doPostFormResultObject(DETAIL_JOB_PATH, ResultModel.class, map);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return resultModel;
	}
}
