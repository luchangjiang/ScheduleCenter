package com.giveu.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.giveu.component.AsyncStatHandle;
import com.giveu.dao.mysql.MonitorLogDAO;
import com.giveu.dao.mysql.QrtzTriggerLogDAO;
import com.giveu.entity.QrtzTriggerDayStat;
import com.giveu.entity.QrtzTriggerLog;
import com.giveu.entity.QrtzTriggerStat;
import com.giveu.entity.QrtzTriggerTopFrequency;
import com.giveu.job.common.dto.QrtzTriggerLogDTO;
import com.giveu.job.common.info.CommonMessage;
import com.giveu.job.common.util.MD5Util;
import com.giveu.job.common.vo.AppVo;
import com.giveu.job.common.vo.JobTriggerAppVo;
import com.giveu.job.common.vo.MonitorLogVo;
import com.giveu.job.common.vo.ResultModel;
import com.giveu.model.*;
import com.giveu.service.WeCatService;
import com.giveu.vo.JobWarningStatVo;
import jodd.http.HttpMultiMap;
import jodd.http.HttpRequest;
import jodd.http.HttpResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by fox on 2018/8/22.
 */
@Service
public class WeCatServiceImpl implements WeCatService {

	public static Logger logger = LoggerFactory.getLogger(WeCatServiceImpl.class);

	@SuppressWarnings("all")
	@Autowired
	QrtzTriggerLogDAO qrtzTriggerLogDAO;

	@SuppressWarnings("all")
	@Autowired
	MonitorLogDAO monitorLogDAO;

	@Autowired
	AsyncStatHandle asyncStatHandle;

	private static String APP_KEY = "enterprise_wecat_access";
	private static String APP_SECRET = "FASF@_23412@!ADFAXYY_23TT";

	private static final String TOP_FREQUENCY = "frequency";
	private static final String TOP_FAIL = "fail";
	private static final String TOP_LEAD_TIME = "leadTime";

	private static String JOB_LOG_ID = "";
	private static String MONITOR_LOG_ID = "";

	private static final String JOB_TYPE = "JOB_TYPE";
	private static final String OBJ_TYPE = "OBJ_TYPE";

	@Override
	public synchronized void training() {
		List<QrtzTriggerLog> list = qrtzTriggerLogDAO.getErrorTriggerLog(JOB_LOG_ID);
		if (list == null || list.size() <= 0) {
			return;
		}
		JOB_LOG_ID = list.get(0).getId();
		for (QrtzTriggerLog obj : list) {
			push(obj.getJobId(), obj.getJobName(), "调度失败", JOB_TYPE);
		}
	}

	@Override
	public void getErrorLog(ResultModel resultModel) {
		List<QrtzTriggerLog> list = qrtzTriggerLogDAO.getErrorTriggerLogx();
		resultModel.setCode(CommonMessage.OK_CODE);
		resultModel.setMessage(CommonMessage.OK_DESC);
		resultModel.setData(list);
	}

	@Override
	public void getErrorLogByJobId(String jobId, ResultModel resultModel) {
		List<QrtzTriggerLogDTO> list = qrtzTriggerLogDAO.getErrorTriggerLogByJobId(jobId);
		resultModel.setCode(CommonMessage.OK_CODE);
		resultModel.setMessage(CommonMessage.OK_DESC);
		resultModel.setData(list);

	}

	@Override
	public void detail(String id, ResultModel resultModel) {
		QrtzTriggerLog qrtzTriggerLog = qrtzTriggerLogDAO.getQrtzTriggerLogById(id);
		resultModel.setCode(CommonMessage.OK_CODE);
		resultModel.setMessage(CommonMessage.OK_DESC);
		resultModel.setData(qrtzTriggerLog);

	}

	@Override
	public String triggerByLogId(String id) {
//		HttpRequest httpRequest = HttpRequest.post("http://10.10.11.52:9060/job/trigger/log/id");
		HttpRequest httpRequest = HttpRequest.post("http://10.11.13.30:9060/job/trigger/log/id");
		HttpMultiMap multiMap = httpRequest.query();
		multiMap.add("jobLogListId", id);
		HttpResponse response = httpRequest.send();
		String result = response.bodyText();
		return result;
	}

	@Override
	public void monitorTraining() {

		List<MonitorLogVo> list = monitorLogDAO.getErrorMonitorLog(MONITOR_LOG_ID);
		if (list == null || list.size() <= 0) {
			return;
		}
		MONITOR_LOG_ID = list.get(0).getId();

		logger.info("*****************************");
		for (MonitorLogVo q : list) {
			logger.info(q.getLogCreateTime());
		}
		logger.info("*****************************");

		Map<String, Object> map = new HashMap<>();
		for (MonitorLogVo obj : list) {
			if (map.get(obj.getId()) != null) {
				continue;
			}
			map.put(obj.getId(), new Object());
			String id = obj.getObjId();
			push(obj.getObjId(), obj.getObjName(), "数据异常", OBJ_TYPE);
		}

	}

	@Override
	public void monitorDetail(String id, ResultModel resultModel) {
		MonitorLogVo monitorLogVo = monitorLogDAO.getMonitorLogById(id);
		resultModel.setCode(CommonMessage.OK_CODE);
		resultModel.setMessage(CommonMessage.OK_DESC);
		resultModel.setData(monitorLogVo);

	}

	@Override
	public void monitorErrorLog(String objName, ResultModel resultModel) {
		List<MonitorLogVo> list = monitorLogDAO.getMonitorErrorLog(objName);
		resultModel.setCode(CommonMessage.OK_CODE);
		resultModel.setMessage(CommonMessage.OK_DESC);
		resultModel.setData(list);

	}

	@Override
	public void getMonitorLogByObjIdList(String objIdListJson, ResultModel resultModel) {
		List<String> objIdList = JSONObject.parseArray(objIdListJson, String.class);
		resultModel.setCode(CommonMessage.OK_CODE);
		if (objIdList.size() == 0) {
			resultModel.setData(objIdListJson);
			return;
		}
		List<MonitorLogVo> list = monitorLogDAO.getMonitorLogByObjIdList(objIdList);

		resultModel.setMessage(CommonMessage.OK_DESC);
		resultModel.setData(list);
	}

	@Override
	public void getMonitorLogByObjIdListAndDays(String objIdListJson, ResultModel resultModel) {
		List<String> objIdList = JSONObject.parseArray(objIdListJson, String.class);
		if (objIdList.size() == 0) {
			resultModel.setData(objIdListJson);
			return;
		}
		List<MonitorLogVo> list = monitorLogDAO.getMonitorLogByObjIdListAndDays(objIdList);

		Map<String, Integer> map = new HashMap<>();

		for (MonitorLogVo m : list) {
			String objName = m.getObjName();
			Integer count = map.get(objName);
			if (count == null) {
				map.put(objName, 1);
			} else {
				map.put(objName, ++count);
			}
		}

		String maxObjName = "";
		int maxCount = 0;
		for (Map.Entry<String, Integer> entry : map.entrySet()) {
			if (entry.getValue() > maxCount) {
				maxObjName = entry.getKey();
				maxCount = entry.getValue();
			}
		}

		TopObjFailModel topObjFailModel = new TopObjFailModel();
		topObjFailModel.setDays(30);
		topObjFailModel.setObjCount(objIdList.size());
		topObjFailModel.setLogCount(list.size());
		topObjFailModel.setObjFailCount(map.size());
		topObjFailModel.setLogMaxCount(maxCount);
		topObjFailModel.setLogMaxName(maxObjName);

		TopModel topModel = new TopModel();
		topModel.setTopObjFailModel(topObjFailModel);
		resultModel.setCode(CommonMessage.OK_CODE);
		resultModel.setMessage(CommonMessage.OK_DESC);
		resultModel.setData(topModel);
	}

	@Override
	public void logStatTraining() {
		asyncStatHandle.recordLogStatTraining();
	}

	@Override
	public void logStatInit() {
		asyncStatHandle.recordInit();
	}

	@Override
	public void jobLogStat(HttpServletRequest request, ResultModel resultModel) {

		resultModel.setCode(CommonMessage.OK_CODE);
		resultModel.setMessage(CommonMessage.OK_DESC);

		String appName = request.getParameter("appName");
		String jobName = request.getParameter("jobName");
		String jobId = request.getParameter("jobId");
		String jobIdListJson = request.getParameter("jobIdListJson");

		List<String> jobIdList = JSONObject.parseArray(jobIdListJson, String.class);

		if (jobIdList.size() == 0) {
			resultModel.setData(jobIdListJson);
			return;
		}


		List<QrtzTriggerStat> list = qrtzTriggerLogDAO.getTriggerStatList(appName, jobName, jobId, jobIdList);


		Map<String, JobWarningStatVo> map = new HashMap<>();

		List<JobWarningStatVo> jobWarningStatVoList = new ArrayList<>();

		for (QrtzTriggerStat stat : list) {
			JobWarningStatVo vo = map.get(stat.getJobId());
			if (vo == null) {
				vo = new JobWarningStatVo();
				map.put(stat.getJobId(), vo);

				vo.setJobId(stat.getJobId());
				vo.setJobName(stat.getJobName());
				vo.setJobCreateTime(stat.getJobCreateTime());
				vo.setAppKey(stat.getAppKey());
				vo.setAppName(stat.getAppName());
			}

			if (stat.getExecuteStatus() == 1) {
				vo.setSuccessCount(stat.getTriggerCount());
				vo.setTodaySuccessCount(stat.getTriggerCountToday());
			} else {
				vo.setFailCount(stat.getTriggerCount());
				vo.setTodayFailCount(stat.getTriggerCountToday());
			}

		}

		for (Map.Entry<String, JobWarningStatVo> entry : map.entrySet()) {
			jobWarningStatVoList.add(entry.getValue());
		}


		List<KeyValueList> keyValueLists = new ArrayList<>();
		Map<String, List<JobWarningStatVo>> listMap = new HashMap<>();
		for (JobWarningStatVo vo : jobWarningStatVoList) {
			List<JobWarningStatVo> jobWarningStatVos = listMap.get(vo.getAppName());
			if (jobWarningStatVos == null) {
				jobWarningStatVos = new ArrayList<>();
				listMap.put(vo.getAppName(), jobWarningStatVos);
			}
			jobWarningStatVos.add(vo);
		}

		for (Map.Entry<String, List<JobWarningStatVo>> entry : listMap.entrySet()) {
			KeyValueList keyValueList = new KeyValueList();
			keyValueList.setAppName(entry.getKey());
			keyValueList.setList(entry.getValue());
			keyValueLists.add(keyValueList);
		}

		if (keyValueLists != null && keyValueLists.size() > 0) {
			resultModel.setData(keyValueLists);
		}
	}

	@Override
	public void topStatTraining() {

		Long start = System.currentTimeMillis();

		int days = 30;

		TopFrequencyModel top = new TopFrequencyModel();

		List<QrtzTriggerTopFrequency> list = qrtzTriggerLogDAO.getQrtzTriggerTopFrequency(days);
		if (list == null || list.size() == 0) {
			return;
		}

		Integer jobCount = list.size();

		top.setJobCount(jobCount);
		top.setDays(days);
		top.setJobMaxName(list.get(0).getJobName());
		top.setJobMaxCount(list.get(0).getTriggerCount());
		top.setJobMinName(list.get(list.size() - 1).getJobName());
		top.setJobMinCount(list.get(list.size() - 1).getTriggerCount());

		int triggerCount = 0;
		for (QrtzTriggerTopFrequency q : list) {
			triggerCount += q.getTriggerCount();

		}
		top.setTriggerCount(triggerCount);

		String json = JSONObject.toJSONString(top);

		qrtzTriggerLogDAO.updTop(TOP_FREQUENCY, json);

		TopFailModel topFailModel = new TopFailModel();
		List<QrtzTriggerTopFrequency> failList = qrtzTriggerLogDAO.getQrtzTriggerTopFail(days);
		topFailModel.setJobCount(jobCount);
		topFailModel.setDays(days);
		topFailModel.setTriggerCount(triggerCount);

		if (failList == null || failList.size() == 0) {
			return;
		}

		topFailModel.setJobFailCount(failList.size());
		topFailModel.setTriggerFailMaxName(failList.get(0).getJobName());
		topFailModel.setTriggerFailMaxCount(failList.get(0).getTriggerCount());

		int triggerFailCount = 0;
		for (QrtzTriggerTopFrequency q : failList) {
			triggerFailCount += q.getTriggerCount();
		}
		topFailModel.setTriggerFailCount(triggerFailCount);
		String topFailModelJson = JSONObject.toJSONString(topFailModel);

		qrtzTriggerLogDAO.updTop(TOP_FAIL, topFailModelJson);


		Map<String, Object> map = qrtzTriggerLogDAO.getLeadTimeTopBaseData();
		Integer moreThanCount = qrtzTriggerLogDAO.getMoreThanCount();
		int maxLeadTime = NumberUtils.toInt(String.valueOf(map.get("max_lead_time")), 0);
		int minLeadTime = NumberUtils.toInt(String.valueOf(map.get("min_lead_time")), 0);
		int leadTimeSum = NumberUtils.toInt(String.valueOf(map.get("lead_time_sum")), 0);
		int triggerDayCount = NumberUtils.toInt(String.valueOf(map.get("trigger_day_count")), 0);
//		moreThanCount = moreThanCount*days;

		TopLeadTimeModel topLeadTimeModel = new TopLeadTimeModel();
		topLeadTimeModel.setDays(days);
		topLeadTimeModel.setJobCount(jobCount);
		topLeadTimeModel.setTriggerCount(triggerCount);
		topLeadTimeModel.setMaxLeadTime(maxLeadTime);
//		topLeadTimeModel.setMixLeadTime(minLeadTime);
//		topLeadTimeModel.setLeadTimeAvg(Float.valueOf(leadTimeSum) / triggerDayCount);
		topLeadTimeModel.setMoreThanSeconds(moreThanCount);
		topLeadTimeModel.setMoreThanAvg(Float.valueOf(moreThanCount) / triggerDayCount);

		String topLeadTimeModelJson = JSONObject.toJSONString(topLeadTimeModel);
		qrtzTriggerLogDAO.updTop(TOP_LEAD_TIME, topLeadTimeModelJson);

	}

	@Override
	public void dayStatTraining() {
		List<QrtzTriggerDayStat> qrtzTriggerDayStatList = qrtzTriggerLogDAO.getQrtzTriggerDayStatInfo();
		List<JobTriggerAppVo> jobTriggerAppVoList = qrtzTriggerLogDAO.getJobList();
		List<AppVo> appVoList = qrtzTriggerLogDAO.getAppList();

		for (QrtzTriggerDayStat q : qrtzTriggerDayStatList) {
			for (JobTriggerAppVo job : jobTriggerAppVoList) {
				if (job.getId().equals(q.getJobId())) {
					q.setJobCreateTime(job.getJobCreateTime());
					break;
				}
			}
			for (AppVo a : appVoList) {
				if (q.getAppKey().equals(a.getAppKey())) {
					q.setAppName(a.getAppName());
					break;
				}
			}
			qrtzTriggerLogDAO.addStatDay(q);
		}
	}

	@Override
	public void getQrtzTriggerDayStat(String startTime, String endTime, String account, ResultModel resultModel) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		Date today = calendar.getTime();
		Calendar calc =Calendar.getInstance();
		calc.add(calc.DATE, - 30);
		Date before = calc.getTime();

		Long st = null;
		Long et = null;
		if (StringUtils.isBlank(startTime)) {
			st = before.getTime();
		} else {
			st = NumberUtils.toLong(startTime, System.currentTimeMillis());
		}
		st = st / 1000;
		if (StringUtils.isBlank(endTime)) {
			et = today.getTime();
		} else {
			et = NumberUtils.toLong(endTime, System.currentTimeMillis());
		}
		et = et / 1000;

		List<QrtzTriggerDayStat> statByJobList = qrtzTriggerLogDAO.getQrtzTriggerDayStatByJobName(st, et, account);
		List<QrtzTriggerDayStat> statByTriggerDateList = qrtzTriggerLogDAO.getQrtzTriggerDayStatByTriggerDate(st, et, account);
		List<QrtzTriggerDayStat> statByAppKeyList = qrtzTriggerLogDAO.getQrtzTriggerDayStatByAppKey(st, et, account);

		Map<String, List<QrtzTriggerDayStat>> map = new HashMap<>();
		map.put("statByJobList", statByJobList);
		map.put("statByTriggerDateList", statByTriggerDateList);
		map.put("statByAppKeyList", statByAppKeyList);

		resultModel.setData(map);
	}

//	@Override
//	public List<QrtzTriggerDayStat> getQrtzTriggerDayStatList(String account) {
//		return qrtzTriggerLogDAO.getQrtzTriggerDayStatList(account);
//	}

	@Override
	public void getJobTopStat(String jobIdListJson, ResultModel resultModel) {

		List<String> jobIdList = JSONObject.parseArray(jobIdListJson, String.class);
		int jobCount =  jobIdList.size();
		if (jobIdList.size() == 0) {
			resultModel.setData(null);
			return;
		}

		List<QrtzTriggerDayStat> qrtzTriggerDayStatList = qrtzTriggerLogDAO.getQrtzTriggerDayStat(jobIdList);

		int topTriggerCount = 0;
		int topFailCount = 0;
		int jobFailCount = 0;
		long leadTimeSum = 0;
		long leadTimeAvg = 0;



		// 最多执行
		QrtzTriggerDayStat qrtzTriggerDayStat1 = new QrtzTriggerDayStat();
		// 最少执行
		QrtzTriggerDayStat qrtzTriggerDayStat2 = new QrtzTriggerDayStat();
		// 失败最多
		QrtzTriggerDayStat qrtzTriggerDayStat3 = new QrtzTriggerDayStat();
		// 最大耗时
		QrtzTriggerDayStat qrtzTriggerDayStat4 = new QrtzTriggerDayStat();
		// 最小耗时
		QrtzTriggerDayStat qrtzTriggerDayStat5 = new QrtzTriggerDayStat();

		qrtzTriggerDayStat1.setTriggerCount(0L);
		qrtzTriggerDayStat2.setTriggerCount(999999999L);
		qrtzTriggerDayStat3.setTriggerFailCount(0L);
		qrtzTriggerDayStat4.setMaxLeadTime(0L);
		qrtzTriggerDayStat5.setMinLeadTime(999999999L);


		Map<String, Boolean> map = new HashMap<>();


		for (QrtzTriggerDayStat q : qrtzTriggerDayStatList) {
			topTriggerCount += q.getTriggerCount();
			topFailCount += q.getTriggerFailCount();
			leadTimeSum += q.getLeadTimeSum();
			if (q.getTriggerCount() > qrtzTriggerDayStat1.getTriggerCount()) {
				qrtzTriggerDayStat1 = q;
			}
			if (q.getTriggerCount() < qrtzTriggerDayStat2.getTriggerCount()) {
				qrtzTriggerDayStat2 = q;
			}
			if (q.getTriggerFailCount() > qrtzTriggerDayStat3.getTriggerFailCount()) {
				qrtzTriggerDayStat3 = q;
			}
			if (q.getMaxLeadTime() > qrtzTriggerDayStat4.getMaxLeadTime()) {
				qrtzTriggerDayStat4 = q;
			}
			if (q.getMinLeadTime() < qrtzTriggerDayStat5.getMinLeadTime()) {
				qrtzTriggerDayStat5 = q;
			}

			if (map.get(q.getJobId()) == null) {
				map.put(q.getJobId(), true);
			}

		}

		jobFailCount = map.size();



		TopFrequencyModel topFrequencyModel = new TopFrequencyModel();
		topFrequencyModel.setJobCount(jobCount);
		topFrequencyModel.setDays(30);
		topFrequencyModel.setTriggerCount(topTriggerCount);
		topFrequencyModel.setJobMaxCount(qrtzTriggerDayStat1.getTriggerCount().intValue());
		topFrequencyModel.setJobMinCount(qrtzTriggerDayStat2.getTriggerCount().intValue());
		topFrequencyModel.setJobMaxName(qrtzTriggerDayStat1.getJobName());
		topFrequencyModel.setJobMinName(qrtzTriggerDayStat2.getJobName());


		TopFailModel topFailModel = new TopFailModel();

		topFailModel.setJobCount(jobCount);
		topFailModel.setDays(30);
		topFailModel.setTriggerCount(topTriggerCount);
		topFailModel.setTriggerFailCount(topFailCount);
		topFailModel.setTriggerFailMaxCount(qrtzTriggerDayStat3.getTriggerFailCount().intValue());
		topFailModel.setTriggerFailMaxName(qrtzTriggerDayStat3.getJobName());
		topFailModel.setJobFailCount(jobFailCount);

		TopLeadTimeModel topLeadTimeModel = new TopLeadTimeModel();
		topLeadTimeModel.setJobCount(jobCount);
		topLeadTimeModel.setDays(30);
		topLeadTimeModel.setTriggerCount(topTriggerCount);
		topLeadTimeModel.setMaxLeadTime(qrtzTriggerDayStat4.getMaxLeadTime().intValue());
		topLeadTimeModel.setMinLeadTime(qrtzTriggerDayStat5.getMinLeadTime().intValue());
		if (leadTimeSum != 0 && topTriggerCount != 0) {
			leadTimeAvg = leadTimeSum / topTriggerCount;
		}
		topLeadTimeModel.setLeadTimeAvg(leadTimeAvg);

		TopModel topModel = new TopModel();
		topModel.setTopFrequencyModel(topFrequencyModel);
		topModel.setTopFailModel(topFailModel);
		topModel.setTopLeadTimeModel(topLeadTimeModel);
		resultModel.setData(topModel);
	}


	private void push(String id, String name, String message, String type) {

		String pushUrlJob = "http://noticecenter.jyfq.com/message/push/job";
		String pushUrlObj = "http://noticecenter.jyfq.com/message/push/obj";

//		String pushUrlJob = "http://localhost/message/push/job";
//		String pushUrlObj = "http://localhost/message/push/obj";

		String url = "";

		if (JOB_TYPE.equals(type)) {
			url = pushUrlJob;
		}

		if (OBJ_TYPE.equals(type)) {
			url = pushUrlObj;
		}

		HttpRequest httpRequest = HttpRequest.post(url);

		String xGiveuTimestamp = String.valueOf(System.currentTimeMillis());
		httpRequest.header("xGiveuAppKey", APP_KEY);
		httpRequest.header("xGiveuTimestamp", xGiveuTimestamp);

		HttpMultiMap multiMap = httpRequest.query();
		multiMap.add("id", id);
		multiMap.add("name", name);
		multiMap.add("message", message);

		StringBuilder sb = new StringBuilder();
		sb.append("xGiveuAppKey").append(APP_KEY);
		sb.append("xGiveuTimestamp").append(xGiveuTimestamp);
		sb.append("id").append(id);
		sb.append("name").append(name);
		sb.append("message").append(message);
		sb.append(APP_SECRET);
		String original = sb.toString();
		String encrypted = MD5Util.sign(original);
		httpRequest.header("xGiveuSign", encrypted);
		HttpResponse response = httpRequest.send();
		logger.info(response.bodyText());

	}

}
