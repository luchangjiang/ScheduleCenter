package com.giveu.job;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.giveu.common.CommLock;
import com.giveu.common.CommonVar;
import com.giveu.component.AsyncStatLogHandle;
import com.giveu.job.common.entity.QrtzAppInfo;
import com.giveu.job.common.entity.QrtzJobExtend;
import com.giveu.job.common.entity.QrtzTriggerLog;
import com.giveu.job.common.info.CommonMessage;
import com.giveu.job.common.util.CommonUtil;
import com.giveu.job.common.util.MD5Util;
import com.giveu.job.common.vo.ResultModel;
import com.giveu.service.JobAppTriggerService;
import com.giveu.util.ServiceInfoUtil;
import com.giveu.util.SpringUtil;
import jodd.http.HttpMultiMap;
import jodd.http.HttpRequest;
import jodd.http.HttpResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 回调执行类
 * Created by fox on 2018/6/29.
 */
public class CallbackJob implements BaseJob{

	public static Logger logger = LoggerFactory.getLogger(CallbackJob.class);

	// 注入Service
	JobAppTriggerService jobAppTriggerService = SpringUtil.getBean("jobAppTriggerServiceImpl", JobAppTriggerService.class);
	AsyncStatLogHandle asyncStatLogHandle = SpringUtil.getBean("asyncStatLogHandle", AsyncStatLogHandle.class);

	CommLock commLock = SpringUtil.getBean("commLock", CommLock.class);

//	@Autowired
//	AsyncStatLogHandle asyncStatLogHandle;

	// 超时时间 单位：毫秒
	public static final int TIME_OUT_MS = 60 * 1000;

	// 回调次数限制
	public static final int CALLBACK_COUNT = 3;

	// callbackUrl
	public static final String CALLBACK_URL = CommonVar.JOB_TEST_APP_URL;

	/**
	 * 执行主程序
	 * @param context
	 * @throws JobExecutionException
	 */
	public void execute(JobExecutionContext context)
			throws JobExecutionException {

		String jobName = context.getTrigger().getJobKey().getName();

		// 调度执行开关（备份环境默认关闭）
		if (!commLock.isOpen) {
			return;
		}

		QrtzTriggerLog qrtzTriggerLog = new QrtzTriggerLog();

		HttpRequest httpRequest = AssembleObject(qrtzTriggerLog, context);
		if (httpRequest == null) {
			return;
		}
		Boolean b = jobAppTriggerService.isExecut(qrtzTriggerLog);
		if (b) {
			Map<String, String> logDescMap = new HashMap<>();
			Long startTime = System.currentTimeMillis();
			recursionSend(httpRequest, qrtzTriggerLog, logDescMap);
			Long endTime = System.currentTimeMillis();
			Long leadTime = endTime - startTime;
			qrtzTriggerLog.setLeadTime(leadTime);
			ObjectMapper mapper = new ObjectMapper();
			String descJson = "";
			try {
				descJson = mapper.writeValueAsString(logDescMap);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
			qrtzTriggerLog.setExecuteDesc(descJson);
			asyncStatLogHandle.record(qrtzTriggerLog);
		}
	}

	/**
	 * 组装日志数据
	 * @param qrtzTriggerLog
	 * @param context
	 */
	private HttpRequest AssembleObject(QrtzTriggerLog qrtzTriggerLog, JobExecutionContext context) {
		Date date = jobAppTriggerService.getNow();
		String uuid = CommonUtil.getUUID32();

		String triggerName = context.getTrigger().getKey().getName();
		String triggerGroup = context.getTrigger().getKey().getGroup();
		String jobName = context.getTrigger().getJobKey().getName();
		String jobGroup = context.getTrigger().getJobKey().getGroup();
		qrtzTriggerLog.setTriggerName(triggerName);
		qrtzTriggerLog.setTriggerGroup(triggerGroup);
		qrtzTriggerLog.setJobName(jobName);
		qrtzTriggerLog.setJobGroup(jobGroup);
		qrtzTriggerLog.setExecuteStatus(1);
		String address = ServiceInfoUtil.getAddress();
		qrtzTriggerLog.setTriggerAddress(address);
		qrtzTriggerLog.setTriggerTime(date);
		qrtzTriggerLog.setCallbackCount(0);
		qrtzTriggerLog.setCallbackUrl(CALLBACK_URL);
		qrtzTriggerLog.setSessionId(uuid);
		qrtzTriggerLog.setId(uuid);

		// 检查JOB 是否绑定客户端应用
		QrtzJobExtend qrtzJobExtend = jobAppTriggerService.getJobExtendByJobName(jobName);
		if (qrtzJobExtend == null) {
			return null;
//			throw new RuntimeException("The job not bind app. JobName : " + jobName);
		}
		QrtzAppInfo qrtzAppInfo = jobAppTriggerService.getAppInfoByAppKey(qrtzJobExtend.getAppKey());
		if (qrtzAppInfo == null) {
			return null;
//			throw new RuntimeException("The app is not found. AppKey : " + qrtzJobExtend.getAppKey());
		}

		qrtzTriggerLog.setCallbackUrl(qrtzJobExtend.getCallbackUrl());
		qrtzTriggerLog.setAppKey(qrtzJobExtend.getAppKey());

		qrtzTriggerLog.setJobId(qrtzJobExtend.getId());
		qrtzTriggerLog.setJobCode(qrtzJobExtend.getJobCode());
		qrtzTriggerLog.setJobDesc(qrtzJobExtend.getJobDesc());

		qrtzTriggerLog.setJobType(qrtzJobExtend.getJobType());
		qrtzTriggerLog.setResultWaitTime(qrtzJobExtend.getResultWaitTime());
		qrtzTriggerLog.setResultUrl(qrtzJobExtend.getResultUrl());

		HttpRequest httpRequest = HttpRequest.post(qrtzJobExtend.getCallbackUrl());
		httpRequest.timeout(TIME_OUT_MS);
		httpRequest.header("xGiveuAppKey", qrtzJobExtend.getAppKey());
		httpRequest.header("xGiveuTimestamp", date.getTime() + "");

		HttpMultiMap multiMap = httpRequest.query();
		multiMap.add("jobSessionId", qrtzTriggerLog.getSessionId());

		StringBuilder sb = new StringBuilder();
		sb.append("xGiveuAppKey").append(qrtzJobExtend.getAppKey());
		sb.append("xGiveuTimestamp").append(date.getTime());
		sb.append("jobSessionId").append(qrtzTriggerLog.getSessionId());
		sb.append(qrtzAppInfo.getAppSecret());
		String original = sb.toString();

		String encrypted = MD5Util.sign(original);

		httpRequest.header("xGiveuSign", encrypted);
		return httpRequest;
	}

	/**
	 * 递归回调客户端
	 * @param qrtzTriggerLog
	 */
	private void recursionSend(HttpRequest httpRequest, QrtzTriggerLog qrtzTriggerLog, Map<String, String> logDescMap) {
		Integer count = qrtzTriggerLog.getCallbackCount() + 1;
		String countKey = String.valueOf(count);
		qrtzTriggerLog.setCallbackCount(count);
		HttpResponse response = null;
		try {
			response = httpRequest.send();
			httpRequest.keepAlive(response,false);
		} catch (Exception e) {
			String errorInfo = "JobName: " + qrtzTriggerLog.getJobName() + "; ErrorInfo: " + e.getCause() + "; URL: " + qrtzTriggerLog.getCallbackUrl();
			logger.error(errorInfo);
			logDescMap.put(countKey, errorInfo);
		}
		if (response == null) {
			qrtzTriggerLog.setExecuteStatus(2);
		}
		if (response != null && response.statusCode()!= 200) {
			qrtzTriggerLog.setExecuteStatus(2);
			String errorInfo = "JobName: " + qrtzTriggerLog.getJobName() + "; URL: " + qrtzTriggerLog.getCallbackUrl() + " ; Request error. Response status code : " + response.statusCode();
			logger.error(errorInfo);
			logDescMap.put(countKey, errorInfo);

		}
		if (response != null && response.statusCode() == 200) {
			qrtzTriggerLog.setExecuteStatus(0);

//			ObjectMapper mapper = new ObjectMapper();
			String result = response.bodyText();
			ResultModel resultModel = new ResultModel();
			try {
//				resultModel = mapper.readValue(result, ResultModel.class);
				if (StringUtils.isBlank(result)) {
					String errorInfo = "The result cannot be null!";
					logDescMap.put(countKey, errorInfo);
				} else {
					JSONObject jsonObject = JSONObject.parseObject(result);
					Object objCode = jsonObject.get("code");
					Object objMessage = jsonObject.get("message");
					Object objData = jsonObject.get("data");
					if (objCode == null) {
						resultModel.setCode(null);
						logger.error("The code cannot be null!. JobName: " + qrtzTriggerLog.getJobName() + " JobCode: " + qrtzTriggerLog.getJobCode() + " LogId: " + qrtzTriggerLog.getId());
					} else {
						String strCode = String.valueOf(objCode);
						Integer code = NumberUtils.toInt(strCode, 500);
						resultModel.setCode(code);
					}
					if (objMessage == null) {
						resultModel.setMessage("");
					} else {
						resultModel.setMessage(String.valueOf(objMessage));
					}
					if (objData == null) {
						resultModel.setData("");
					} else {
						resultModel.setData(String.valueOf(objData));
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
				String errorInfo = "JobName: " + qrtzTriggerLog.getJobName() + "; URL: " + qrtzTriggerLog.getCallbackUrl() + " ; Parse error. BodyText: " + result;
				logger.error(errorInfo);
				logDescMap.put(countKey, errorInfo);
				return;
			}

			if (resultModel == null || resultModel.getCode() == null) {
				qrtzTriggerLog.setExecuteStatus(2);
				String errorInfo = "JobName: " + qrtzTriggerLog.getJobName() + "; URL: " + qrtzTriggerLog.getCallbackUrl();
				logger.error(errorInfo);
				logDescMap.put(countKey, errorInfo);
				return;
			}

			if (resultModel.getCode() != null && resultModel.getCode().equals(500)) {
				qrtzTriggerLog.setExecuteStatus(2);
				String errorInfo = "JobName: " + qrtzTriggerLog.getJobName() + "; Return code error: " + resultModel.getCode() + "; URL: " + qrtzTriggerLog.getCallbackUrl() + "; Info: " + resultModel.getMessage();
				logger.error(errorInfo);
				logDescMap.put(countKey, errorInfo);
				return;
			}

			if (resultModel.getCode() != null && !resultModel.getCode().equals(CommonMessage.OK_CODE)) {
				qrtzTriggerLog.setExecuteStatus(2);
				String errorInfo = "JobName: " + qrtzTriggerLog.getJobName() + "; Return code error: " + resultModel.getCode() + "; URL: " + qrtzTriggerLog.getCallbackUrl();
				logger.error(errorInfo);
				logDescMap.put(countKey, errorInfo);
				return;
			}

//			String sessionId = resultModel.getData().toString();
//			if (!sessionId.equals(qrtzTriggerLog.getSessionId())) {
//				// SessionId 不匹配
//				qrtzTriggerLog.setExecuteStatus(4);
//				String errorInfo = "JobName: " + qrtzTriggerLog.getJobName() + "; Mismatching! AppSessionId: " + sessionId + ", ServiceSessionId:" + qrtzTriggerLog.getSessionId() + "; URL: " + qrtzTriggerLog.getCallbackUrl();
//				logger.error(errorInfo);
//				logDescMap.put(countKey, errorInfo);
//				return;
//			}

			if (resultModel != null && resultModel.getCode().equals(CommonMessage.OK_CODE)) {
				qrtzTriggerLog.setExecuteStatus(1);
				logDescMap.put(countKey, response.statusCode() + " : " + result);
			}

			return;
		}
		if (qrtzTriggerLog.getCallbackCount() >= CALLBACK_COUNT) {
			qrtzTriggerLog.setExecuteStatus(2);
			return;
		}
		recursionSend(httpRequest, qrtzTriggerLog, logDescMap);
	}
}
