package com.giveu.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.giveu.common.CommonVar;
import com.giveu.dao.JobAppTriggerDao;
import com.giveu.job.CallbackJob;
import com.giveu.job.common.dto.JobTriggerDTO;
import com.giveu.job.common.entity.*;
import com.giveu.job.common.info.CommonMessage;
import com.giveu.job.common.util.CommonUtil;
import com.giveu.job.common.util.MD5Util;
import com.giveu.job.common.util.ObjectUtil;
import com.giveu.job.common.util.StringUtil;
import com.giveu.job.common.var.Secret;
import com.giveu.job.common.vo.*;
import com.giveu.service.JobAppTriggerService;
import jodd.http.HttpRequest;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.quartz.*;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class JobAppTriggerServiceImpl implements JobAppTriggerService {

	private static Logger logger = LoggerFactory.getLogger(JobAppTriggerServiceImpl.class);

	@Autowired
	@SuppressWarnings("all")
	private JobAppTriggerDao jobAppTriggerDao;

	// 加入Qulifier注解，通过名称注入bean
	@Autowired
	@Qualifier("Scheduler")
	private Scheduler scheduler;

	// 间隔时间
	private static final int INTERVAL_TIME = 500;

	// 参数长度限制
	private static final int PAR_LENGTH = 128;

	// 新增任务的URL地址
	private static final String ADD_NEW_JOB_URL = "/job/add";

	// 删除任务的URL地址
	private static final String DEL_JOB_URL = "/job/del/code";

	// 暂停任务的URL地址
	private static final String PAUSE_JOB_URL = "/job/pause/code";

	// 恢复任务的URL地址
	private static final String RESUME_JOB_URL = "/job/resume/code";

	private static final String UPDATE_STAT_LIST_URL = "http://localhost:9050/we/cat/push/job/log/stat/init";

//	public PageInfo<JobAndTrigger> getJobAndTriggerDetails(int pageNum, int pageSize) {
//		PageHelper.startPage(pageNum, pageSize);
//		List<JobAndTrigger> list = jobAppTriggerDao.getJobAndTriggerDetails();
//		PageInfo<JobAndTrigger> page = new PageInfo(list);
//		return page;
//	}

	@Override
	public Integer addTriggerToLog(QrtzTriggerLog qrtzTriggerLog) {
		return jobAppTriggerDao.addTriggerToLog(qrtzTriggerLog);
	}

	@Override
	public Integer addTriggerToLogByTable(String tableName, QrtzTriggerLog qrtzTriggerLog) {
		return jobAppTriggerDao.addTriggerToLogByTable(tableName, qrtzTriggerLog);
	}

	@Override
	public Boolean isExecut(QrtzTriggerLog qrtzTriggerLog) {
		QrtzTriggerLock qrtzTriggerLock = jobAppTriggerDao.getTriggerLock(qrtzTriggerLog);
		if (qrtzTriggerLock == null) {
			qrtzTriggerLock = new QrtzTriggerLock();
			BeanUtils.copyProperties(qrtzTriggerLog,qrtzTriggerLock);
			qrtzTriggerLock.setId(CommonUtil.getUUID32());
			jobAppTriggerDao.addTriggerLock(qrtzTriggerLock);
			return true;
		}
		if (qrtzTriggerLock != null) {
			if ((qrtzTriggerLog.getTriggerTime().getTime() - qrtzTriggerLock.getTriggerTime().getTime()) < INTERVAL_TIME) {
				logger.info("重复执行，JobName: {}", qrtzTriggerLog.getJobName());
				return false;
			}
			jobAppTriggerDao.updTriggerLock(qrtzTriggerLog);
			return true;
		}
		return false;
	}

	@Override
	public Date getNow() {
		return jobAppTriggerDao.getNow();
	}

	@Override
	public List<QrtzTriggers> getActiveTriggerList() {
		return jobAppTriggerDao.getActiveTriggerList();
	}
	@Override
	public List<QrtzTriggers> getErrorTriggerList() {
		return jobAppTriggerDao.getErrorTriggerList();
	}

	@Override
	public Integer addJob(JobTriggerDTO jobTriggerDTO) throws Exception {

		// 启动调度器
		scheduler.start();

		// 构建job信息
		JobDetail jobDetail = JobBuilder.newJob(CallbackJob.class).withIdentity(jobTriggerDTO.getJobName(), jobTriggerDTO.getJobGroup()).build();
		// 表达式调度构建器(即任务执行的时间)
		CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(jobTriggerDTO.getCronExpression());

		// 按新的cronExpression表达式构建一个新的trigger
		CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(jobTriggerDTO.getJobName(), jobTriggerDTO.getJobGroup())
				.withSchedule(scheduleBuilder).build();

		int affectedRows = 0;
		QrtzJobExtend qrtzJobExtend = new QrtzJobExtend();
		BeanUtils.copyProperties(jobTriggerDTO, qrtzJobExtend);
		try {
			scheduler.scheduleJob(jobDetail, trigger);
			affectedRows = jobAppTriggerDao.addJobExtend(qrtzJobExtend);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		return affectedRows;
	}

	@Override
	public Integer delJobByName(String jobName) {
		return delJobByNameHandler(jobName);
	}

	@Override
	public Integer delJobByCode(HttpServletRequest request, ResultModel resultModel) {
		String jobCode = request.getParameter("jobCode");
//		QrtzJobExtend qrtzJobExtend = jobAppTriggerDao.getJobExtendByJobCode(jobCode);
//		if (qrtzJobExtend == null) {
//			logger.error("Method:delJobByCode. Not found job by {}", jobCode);
//			resultModel.setCode(CommonMessage.REQ_RES_ERROR_CODE);
//			resultModel.setMessage(CommonMessage.REQ_RES_ERROR_DESC);
//			return 0;
//		}
//		String jobName = qrtzJobExtend.getJobName();
		String jobName = getJobNameByCode(jobCode);
		if (StringUtils.isBlank(jobName)) {
			resultModel.setCode(CommonMessage.REQ_RES_ERROR_CODE);
			resultModel.setMessage(CommonMessage.REQ_RES_ERROR_DESC);
			return 0;
		}

		int affectedRows = delJobByNameHandler(jobName);

		if (affectedRows == 1) {
			resultModel.setCode(CommonMessage.OK_CODE);
			resultModel.setMessage(CommonMessage.OK_DESC);
			resultModel.setData(jobName);
		}

		return affectedRows;
	}

	@Override
	public void pauseJob(String jobName) throws SchedulerException {
		scheduler.pauseJob(JobKey.jobKey(jobName, CommonVar.JOB_GROUP));
	}

	@Override
	public void pauseJobByCode(String jobCode, ResultModel resultModel) {
		String jobName = getJobNameByCode(jobCode);
		try {
			scheduler.pauseJob(JobKey.jobKey(jobName, CommonVar.JOB_GROUP));
			resultModel.setCode(CommonMessage.OK_CODE);
			resultModel.setMessage(CommonMessage.OK_DESC);
		} catch (SchedulerException e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			resultModel.setCode(CommonMessage.SER_UNKNOW_ERROR_CODE);
			resultModel.setMessage(CommonMessage.SER_UNKNOW_ERROR_DESC);
		}
	}

	@Override
	public void resumeJobByCode(String jobCode, ResultModel resultModel) {
		String jobName = getJobNameByCode(jobCode);
		if (jobName == null) {
			resultModel.setCode(CommonMessage.TABLE_NOT_FOUND_ERROR_CODE);
			resultModel.setMessage(CommonMessage.TABLE_NOT_FOUND_ERROR_DESC);
			return;
		}
		try {
			scheduler.resumeJob(JobKey.jobKey(jobName, CommonVar.JOB_GROUP));
			resultModel.setCode(CommonMessage.OK_CODE);
			resultModel.setMessage(CommonMessage.OK_DESC);
		} catch (SchedulerException e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			resultModel.setCode(CommonMessage.SER_UNKNOW_ERROR_CODE);
			resultModel.setMessage(CommonMessage.SER_UNKNOW_ERROR_DESC);
		}

	}

	@Override
	public void resumeJob(String jobName) throws SchedulerException {
//		TriggerKey triggerKey = TriggerKey.triggerKey(jobName, CommonVar.JOB_GROUP);
//		boolean result = false;
//		if (checkExists(jobName, CommonVar.JOB_GROUP)) {
//			scheduler.resumeTrigger(triggerKey);
//			result = true;
//			logger.info(">>>>>>>>>>> resumeJob success, triggerKey:{}", triggerKey);
//		} else {
//			logger.info(">>>>>>>>>>> resumeJob fail, triggerKey:{}", triggerKey);
//		}
		scheduler.resumeJob(JobKey.jobKey(jobName, CommonVar.JOB_GROUP));
	}
	public boolean checkExists(String jobName, String jobGroup) throws SchedulerException{
		TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
		return scheduler.checkExists(triggerKey);
	}

	public static boolean isValidExpression(final String cronExpression){
		CronTriggerImpl trigger = new CronTriggerImpl();
		try {
			trigger.setCronExpression(cronExpression);
			Date date = trigger.computeFirstFireTime(null);
			return date != null;
		} catch (Exception e) {
			logger.error("[TaskUtils.isValidExpression]:failed. throw ex:" , e);
			return false;
		}
	}

	@Override
	public Boolean check(HttpServletRequest request, ResultModel resultModel) {

		Map<String, String> map = new HashMap<>();
		StringBuilder sb = new StringBuilder();

		// 判断是否为新增任务的请求
		if (request.getServletPath ().equals(ADD_NEW_JOB_URL)) {
			// 获取提交过来新增JOB的参数内容
			String jobName = request.getParameter("jobName");
			String jobCode = request.getParameter("jobCode");
			String jobDesc = request.getParameter("jobDesc");
			String callbackUrl = request.getParameter("callbackUrl");
			String cronExpression = request.getParameter("cronExpression");


			String jobType = request.getParameter("jobType");
			String resultWaitTime = request.getParameter("resultWaitTime");
			String resultUrl = request.getParameter("resultUrl");

			if (!isValidExpression(cronExpression)) {
				resultModel.setCode(CommonMessage.CRON_EXPRESSION_ERROR_CODE);
				resultModel.setMessage(CommonMessage.CRON_EXPRESSION_ERROR_DESC);
				return false;
			}

			QrtzJobExtend qrtzJobExtend = jobAppTriggerDao.getJobExtendByJobName(jobName);
			if (qrtzJobExtend != null) {
				resultModel.setCode(CommonMessage.JOB_NAME_ALREADY_ERROR_CODE);
				resultModel.setMessage(CommonMessage.JOB_NAME_ALREADY_ERROR_DESC);
				return false;
			}
			map.put("jobName",jobName);
			map.put("jobCode",jobCode);
			map.put("jobDesc",jobDesc);
			map.put("callbackUrl",callbackUrl);
			map.put("cronExpression",cronExpression);

			map.put("jobType",jobType);

			Integer AsyncType = 2;
			if (AsyncType.equals(NumberUtils.toInt(jobType, 1))) {
				map.put("resultWaitTime", resultWaitTime);
				map.put("resultUrl", resultUrl);
			}
		}

		// 判断是否为删除任务的请求
		else if (request.getServletPath ().equals(DEL_JOB_URL)) {
			String jobCode = request.getParameter("jobCode");
			map.put("jobCode",jobCode);
		}

		// 判断是否为暂停任务的请求
		else if (request.getServletPath ().equals(PAUSE_JOB_URL)) {
			String jobCode = request.getParameter("jobCode");
			map.put("jobCode",jobCode);
		}

		// 判断是否为恢复任务的请求
		else if (request.getServletPath ().equals(RESUME_JOB_URL)) {
			String jobCode = request.getParameter("jobCode");
			map.put("jobCode",jobCode);
		}

		else {
			resultModel.setCode(CommonMessage.NO_AUTHORITY_ERROR_CODE);
			resultModel.setMessage(CommonMessage.NO_AUTHORITY_ERROR_DESC);
			return false;
		}

		// 获取提交过来的公共参数内容
		String xGiveuSign = request.getHeader("xGiveuSign");
		String xGiveuAppKey = request.getHeader("xGiveuAppKey");
		String xGiveuTimestamp = request.getHeader("xGiveuTimestamp");


		Boolean isEmpty = ObjectUtil.isEmptyBatch(xGiveuSign, xGiveuAppKey, xGiveuTimestamp);
		if (isEmpty) {
			resultModel.setCode(CommonMessage.PAR_NOT_NULL_CODE);
			resultModel.setMessage(CommonMessage.PAR_NOT_NULL_DESC);
			return false;
		}

		QrtzJobExtend qrtzJobExtend = jobAppTriggerDao.getJobExtendByJobCode(map.get("jobCode"));

		if (qrtzJobExtend != null && (!CommonVar.ADMIN_APP_KEY.equals(xGiveuAppKey)) && (!xGiveuAppKey.equals(qrtzJobExtend.getAppKey()))) {
			resultModel.setCode(CommonMessage.NO_AUTHORITY_ERROR_CODE);
			resultModel.setMessage(CommonMessage.NO_AUTHORITY_ERROR_DESC);
			return false;
		}

		map.put("xGiveuSign",xGiveuSign);
		map.put("xGiveuAppKey",xGiveuAppKey);
		map.put("xGiveuTimestamp",xGiveuTimestamp);

		// 遍历请求参数，判断是否有空字段，且生成部分原始加密字符串
		for (Map.Entry<String, String> entry : map.entrySet()) {
			if (StringUtils.isBlank(entry.getValue()) && entry.getValue().length() <= PAR_LENGTH) {
				resultModel.setCode(CommonMessage.PAR_NOT_NULL_CODE);
				resultModel.setMessage(CommonMessage.PAR_NOT_NULL_DESC + entry.getKey());
				return false;
			}
			if (entry.getKey().equals("xGiveuSign")) {
				continue;
			}
			sb.append(entry.getKey() + entry.getValue());
		}

		// 判断传过来的时间戳是否类型错误
		Long timestamp = NumberUtils.toLong(xGiveuTimestamp, 0);
		if (timestamp == 0) {
			resultModel.setCode(CommonMessage.PAR_TYPE_ERROR_CODE);
			resultModel.setMessage(CommonMessage.PAR_TYPE_ERROR_DESC);
			return false;
		}

		String originalx = "";
		String originaly = "";
		String parStr = sb.toString();

		QrtzAppInfo qrtzAppInfo = jobAppTriggerDao.getAppInfoByAppKey(xGiveuAppKey);
		if (qrtzAppInfo == null) {
			originalx = parStr + Secret.APP_SECRET;
		}else {
			originalx = parStr + Secret.APP_SECRET;
			originaly = parStr + qrtzAppInfo.getAppSecret();
		}

		String encrypted1 = MD5Util.sign(originalx);
		String encrypted2 = MD5Util.sign(originaly);
		if (!encrypted1.equals(xGiveuSign) && !encrypted2.equals(xGiveuSign)) {
			resultModel.setCode(CommonMessage.SIGN_ERROR_CODE);
			resultModel.setMessage(CommonMessage.SIGN_ERROR_DESC);
			logger.info(CommonMessage.SIGN_ERROR_DESC);
			return false;
		}
		logger.info("Sign check success...");
		return true;
	}



	@Override
	public Integer addJob(HttpServletRequest request, ResultModel resultModel) {

		String jobName = request.getParameter("jobName");
		String jobCode = request.getParameter("jobCode");
		String jobDesc = request.getParameter("jobDesc");
		String callbackUrl = request.getParameter("callbackUrl");
		String cronExpression = request.getParameter("cronExpression");

		Integer jobType = NumberUtils.toInt(String.valueOf(request.getParameter("jobType")), 1);
		String resultUrl = request.getParameter("resultUrl");
		Integer resultWaitTime = NumberUtils.toInt(String.valueOf(request.getParameter("resultWaitTime")), 0);

		String xGiveuAppKey = request.getHeader("xGiveuAppKey");

		JobTriggerDTO jobTriggerDTO = new JobTriggerDTO();
		jobTriggerDTO.setJobName(jobName);
		jobTriggerDTO.setJobCode(jobCode);
		jobTriggerDTO.setJobDesc(jobDesc);
		jobTriggerDTO.setCallbackUrl(callbackUrl);
		jobTriggerDTO.setCronExpression(cronExpression);

		jobTriggerDTO.setJobType(jobType);
		jobTriggerDTO.setResultUrl(resultUrl);
		jobTriggerDTO.setResultWaitTime(resultWaitTime);

		jobTriggerDTO.setAppKey(xGiveuAppKey);

		jobTriggerDTO.setJobClassName(CallbackJob.class.toString());

		// 启动调度器
		try {
			scheduler.start();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}

//		Random random = new Random();
//		Integer x = random.nextInt(10);
//		String jobClassStr = "com.giveu.job.CallbackJob00" + x;
//		Class clazz = null;
//		try {
//			clazz = Class.forName(jobClassStr);
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		}

		// 构建job信息
		JobDetail jobDetail = JobBuilder.newJob(CallbackJob.class).withIdentity(jobTriggerDTO.getJobName(), jobTriggerDTO.getJobGroup()).build();
		// 表达式调度构建器(即任务执行的时间)
		CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(jobTriggerDTO.getCronExpression());

		// 按新的cronExpression表达式构建一个新的trigger
		CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(jobTriggerDTO.getJobName(), jobTriggerDTO.getJobGroup()).withSchedule(scheduleBuilder).build();

		int affectedRows = 0;
		QrtzJobExtend qrtzJobExtend = new QrtzJobExtend();
		BeanUtils.copyProperties(jobTriggerDTO, qrtzJobExtend);
		try {
			scheduler.scheduleJob(jobDetail, trigger);
			qrtzJobExtend.setId(CommonUtil.getUUID32());
			affectedRows = jobAppTriggerDao.addJobExtend(qrtzJobExtend);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		if (affectedRows == 1) {
			resultModel.setCode(CommonMessage.OK_CODE);
			resultModel.setMessage(CommonMessage.OK_DESC);

			try {
				// Update the statistics list
				HttpRequest wecatRequest = HttpRequest.post(UPDATE_STAT_LIST_URL);
				wecatRequest.send();
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("Update the statistics list is failed. Check the statistics server is started.");
			}
		}
		return affectedRows;

	}

	@Override
	public void getParToMap(HttpServletRequest request, Map<String, String> map) {

	}

	@Override
	public QrtzJobExtend getJobExtendByJobName(String jobName) {
		return jobAppTriggerDao.getJobExtendByJobName(jobName);
	}

	@Override
	public void getJobById(String id, ResultModel resultModel) {
		JobTriggerAppVo jobTriggerAppVo = jobAppTriggerDao.getJobById(id);
		if (jobTriggerAppVo == null) {
			resultModel.setCode(CommonMessage.TABLE_NOT_FOUND_ERROR_CODE);
			resultModel.setMessage(CommonMessage.TABLE_NOT_FOUND_ERROR_DESC);
		} else {
			resultModel.setCode(CommonMessage.OK_CODE);
			resultModel.setMessage(CommonMessage.OK_DESC);
			resultModel.setData(jobTriggerAppVo);
		}
	}

	@Override
	public void updJob(JobTriggerAppVo jobTriggerAppVo, ResultModel resultModel) {
		if (StringUtils.isBlank(jobTriggerAppVo.getCallbackUrl()) || StringUtils.isBlank(jobTriggerAppVo.getJobDesc())) {
			resultModel.setCode(CommonMessage.PAR_NOT_NULL_CODE);
			resultModel.setMessage(CommonMessage.PAR_NOT_NULL_DESC);
			return;
		}
		JobTriggerAppVo jobVo = jobAppTriggerDao.getJobById(jobTriggerAppVo.getId());
		if (jobVo == null) {
			resultModel.setCode(CommonMessage.TABLE_NOT_FOUND_ERROR_CODE);
			resultModel.setMessage(CommonMessage.TABLE_NOT_FOUND_ERROR_DESC);
			return;
		}
		if (!jobVo.getCronExpression().equals(jobTriggerAppVo.getCronExpression())) {
			try {
				TriggerKey triggerKey = TriggerKey.triggerKey(jobVo.getJobName(), CommonVar.JOB_GROUP);
				// 表达式调度构建器
				CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(jobTriggerAppVo.getCronExpression());
				CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
				// 按新的cronExpression表达式重新构建trigger
				trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
				// 按新的trigger重新设置job执行
				scheduler.rescheduleJob(triggerKey, trigger);
				// 非暂停状态重新回复任务
				if (jobVo.getTriggerState() != null && !jobVo.getTriggerState().equals("PAUSED")) {
					// 恢复任务
					scheduler.resumeJob(JobKey.jobKey(jobVo.getJobName(), CommonVar.JOB_GROUP));
				}
			} catch (SchedulerException e) {
				e.printStackTrace();
				resultModel.setCode(CommonMessage.SER_UNKNOW_ERROR_CODE);
				resultModel.setMessage(CommonMessage.SER_UNKNOW_ERROR_DESC);
				return;
			}
		}
		Integer affect = jobAppTriggerDao.updJob(jobTriggerAppVo);
		if (affect > 0) {
			resultModel.setCode(CommonMessage.OK_CODE);
			resultModel.setMessage(CommonMessage.OK_DESC);
			resultModel.setData(affect);
		}

		logger.info("Job update done...");
	}

	@Override
	public void list(HttpServletRequest request, ResultModel resultModel) {

		String id = request.getParameter("id");
		String jobCode = request.getParameter("jobCode");
		String appKey = request.getParameter("appKey");
		String jobName = request.getParameter("jobName");
		String triggerState = request.getParameter("triggerState");
		String userAccount = request.getParameter("userAccount");

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

		resultModel.setCode(CommonMessage.OK_CODE);
		resultModel.setMessage(CommonMessage.OK_DESC);
		resultModel.setData(jobTriggerAppVoList);
	}

	/*
     * 将时间转换为时间戳
     */
	public static Long dateToStamp(String s) throws ParseException{
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = simpleDateFormat.parse(s);
		return date.getTime();

	}

	@Override
	public void logList(HttpServletRequest request, ResultModel resultModel) {

		String beginTime = request.getParameter("beginTime");
		String endTime = request.getParameter("endTime");
		String jobId = request.getParameter("jobId");
		String jobCode = request.getParameter("jobCode");
		String appKey = request.getParameter("appKey");
		String userAccount = request.getParameter("userAccount");
		Integer executeStatus = NumberUtils.toInt(request.getParameter("executeStatus"), 0);

		Long triggerBeginTime = null;
		Long triggerEndTime = null;


		if (StringUtils.isNotBlank(beginTime)) {
			try {
				triggerBeginTime = dateToStamp(beginTime);

			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if (StringUtils.isNotBlank(endTime)) {
			try {
				triggerEndTime = dateToStamp(endTime) + (86400000 - 1);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		if (StringUtils.isBlank(jobId)) {
			jobId = null;
		}
		if (StringUtils.isBlank(jobCode)) {
			jobCode = null;
		}

		List<String> appKeyList = jobAppTriggerDao.getAppKeyByAccount(userAccount);

		if (appKeyList == null && appKeyList.size() > 0) {
			resultModel.setCode(CommonMessage.NO_AUTHORITY_ERROR_CODE);
			resultModel.setMessage(CommonMessage.NO_AUTHORITY_ERROR_DESC);
			return;
		}


		if (StringUtils.isBlank(appKey)) {
//			List<AppVo> appVoList = jobAppTriggerDao.getAppList();
//			if (appVoList == null && appVoList.size() > 0) {
//				resultModel.setCode(CommonMessage.TABLE_NOT_FOUND_ERROR_CODE);
//				resultModel.setMessage(CommonMessage.TABLE_NOT_FOUND_ERROR_DESC);
//				return;
//			}
//			appKey = appVoList.get(0).getAppKey();
			appKey = appKeyList.get(0);
			QrtzAppInfo qrtzAppInfo = jobAppTriggerDao.getAppInfoByAppKey(appKey);
			if (qrtzAppInfo == null) {
				resultModel.setCode(CommonMessage.TABLE_NOT_FOUND_ERROR_CODE);
				resultModel.setMessage(CommonMessage.TABLE_NOT_FOUND_ERROR_DESC);
				return;
			}
		} else {
			boolean isContains = appKeyList.contains(appKey);
			if (!isContains) {
				resultModel.setCode(CommonMessage.NO_AUTHORITY_ERROR_CODE);
				resultModel.setMessage(CommonMessage.NO_AUTHORITY_ERROR_DESC);
				return;
			}
		}

		String tableName = CommonVar.LOG_TABLE_NAME_PREFIX + appKey;

		Integer count = jobAppTriggerDao.getRecQrtzTriggerLogCount(tableName, triggerBeginTime, triggerEndTime, jobId, jobCode, executeStatus);
		List<JobTriggerAppLogVo> qrtzTriggerLogList = jobAppTriggerDao.getRecQrtzTriggerLogList(tableName, triggerBeginTime, triggerEndTime, jobId, jobCode, executeStatus);

		resultModel.setCode(CommonMessage.OK_CODE);
		resultModel.setMessage(String.valueOf(count));
		resultModel.setData(qrtzTriggerLogList);
	}

	@Override
	public void appList(HttpServletRequest request, ResultModel resultModel) {

		String appName = request.getParameter("appName");
		String appKey = request.getParameter("appKey");
		String account = request.getParameter("account");

		Integer appStatus = NumberUtils.toInt(request.getParameter("appStatus"), 0);

		List<AppVo> qrtzAppInfoList = jobAppTriggerDao.getAppInfoList(appName, appKey, appStatus, account);

		if (qrtzAppInfoList == null || qrtzAppInfoList.size() <= 0) {
			resultModel.setCode(CommonMessage.SER_UNKNOW_ERROR_CODE);
			resultModel.setMessage(CommonMessage.SER_UNKNOW_ERROR_DESC);
		}
		resultModel.setCode(CommonMessage.OK_CODE);
		resultModel.setMessage(CommonMessage.OK_DESC);
		resultModel.setData(qrtzAppInfoList);

	}

	@Override
	public void getListJsonByAccount(String account, ResultModel resultModel) {
		List<AppVo> qrtzAppInfoList = jobAppTriggerDao.getListJsonByAccount(account);
		resultModel.setCode(CommonMessage.OK_CODE);
		resultModel.setMessage(CommonMessage.OK_DESC);
		resultModel.setData(qrtzAppInfoList);
	}


	@Override
	public QrtzAppInfo getAppInfoByAppKey(String appKey) {
		return jobAppTriggerDao.getAppInfoByAppKey(appKey);
	}

	@Override
	public Integer addApp(HttpServletRequest request, ResultModel resultModel) {
		String appName = request.getParameter("appName");
		String appKey = request.getParameter("appKey");
		String appSecret = request.getParameter("appSecret");
		Integer appStatus = NumberUtils.toInt(request.getParameter("appStatus"),1);
		String account = request.getParameter("account");
		if (StringUtils.isBlank(account)) {
			resultModel.setCode(CommonMessage.NO_AUTHORITY_ERROR_CODE);
			resultModel.setMessage(CommonMessage.NO_AUTHORITY_ERROR_DESC);
			return 0;
		}

		boolean isEmptyBatch = StringUtil.isEmptyBatch(appName, appKey, appSecret);
		if (isEmptyBatch) {
			resultModel.setCode(CommonMessage.PAR_NOT_NULL_CODE);
			resultModel.setMessage(CommonMessage.PAR_NOT_NULL_DESC);
			return 0;
		}

		QrtzAppInfo appInfo = jobAppTriggerDao.getAppInfoByAppKey(appKey);
		if (appInfo != null) {
			resultModel.setCode(CommonMessage.APP_KEY_ALREADY_ERROR_CODE);
			resultModel.setMessage(CommonMessage.APP_KEY_ALREADY_ERROR_DESC);
			return 0;
		}

		QrtzAppInfo qrtzAppInfo = new QrtzAppInfo();
		qrtzAppInfo.setAppName(appName);
		qrtzAppInfo.setAppKey(appKey);
		qrtzAppInfo.setAppSecret(appSecret);
		qrtzAppInfo.setAppStatus(appStatus);
		qrtzAppInfo.setId(CommonUtil.getUUID32());
		int i = jobAppTriggerDao.addApp(qrtzAppInfo);
		jobAppTriggerDao.createTriggerLogTable(CommonVar.LOG_TABLE_NAME_PREFIX + appKey);
		if (i == 1) {
			AppUserVo appUserVo = new AppUserVo();
			appUserVo.setUserAccount(account);
			appUserVo.setAppKey(appKey);
			jobAppTriggerDao.bindAccount(appUserVo);
			resultModel.setCode(CommonMessage.OK_CODE);
			resultModel.setMessage(CommonMessage.OK_DESC);
		}
		return i;
	}

	@Override
	public AppVo getAppVoById(String appId) {
		QrtzAppInfo qrtzAppInfo = jobAppTriggerDao.getQrtzAppInfoById(appId);
		if (qrtzAppInfo == null) {
			return null;
		}
		AppVo appVo = new AppVo();
		BeanUtils.copyProperties(qrtzAppInfo, appVo);
		appVo.setAppCreateTime(qrtzAppInfo.getAppCreateTime().toString());
		appVo.setAppUpdateTime(qrtzAppInfo.getAppUpdateTime().toString());
		return appVo;
	}

	private Integer delJobByNameHandler(String jobName) {
		int affectedRows = 0;
		try {
			scheduler.pauseTrigger(TriggerKey.triggerKey(jobName, CommonVar.JOB_GROUP));
			scheduler.unscheduleJob(TriggerKey.triggerKey(jobName, CommonVar.JOB_GROUP));
			scheduler.deleteJob(JobKey.jobKey(jobName, CommonVar.JOB_GROUP));
			affectedRows = jobAppTriggerDao.delJobExtendByJobName(jobName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return affectedRows;
	}

	@Override
	public void updAppById(HttpServletRequest request, ResultModel resultModel) {

		String id = request.getParameter("id");
		String appName = request.getParameter("appName");
		String appKey = request.getParameter("appKey");
		String appSecret = request.getParameter("appSecret");
		Integer appStatus = NumberUtils.toInt(request.getParameter("appStatus"),0);
		QrtzAppInfo qrtzAppInfo = new QrtzAppInfo();
		qrtzAppInfo.setId(id);
		qrtzAppInfo.setAppName(appName);
		qrtzAppInfo.setAppKey(appKey);
		qrtzAppInfo.setAppSecret(appSecret);
		qrtzAppInfo.setAppStatus(appStatus);

		jobAppTriggerDao.updAppById(qrtzAppInfo);
	}

	@Override
	public Integer delAppById(String appId) {
		QrtzAppInfo qrtzAppInfo = jobAppTriggerDao.getQrtzAppInfoById(appId);
		String appKey = qrtzAppInfo.getAppKey();
		jobAppTriggerDao.unbindAccountByAppKey(appKey);
		return jobAppTriggerDao.delAppById(appId);
	}

	@Override
	public Integer enableById(String appId) {
		return jobAppTriggerDao.enableAppById(appId);
	}

	@Override
	public Integer disableById(String appId) {
		return jobAppTriggerDao.disableAppById(appId);
	}

	@Override
	public void triggerByLogId(ResultModel resultModel, String jobLogListId) {
		QrtzTriggerLog qrtzTriggerLog = jobAppTriggerDao.getRecQrtzTriggerLogById(jobLogListId);
		if (qrtzTriggerLog == null) {
			resultModel.setCode(CommonMessage.TABLE_NOT_FOUND_ERROR_CODE);
			resultModel.setMessage(CommonMessage.TABLE_NOT_FOUND_ERROR_DESC);
		}
		String jobId = qrtzTriggerLog.getJobId();
		QrtzJobExtend qrtzJobExtend = jobAppTriggerDao.getJobExtendByJobId(jobId);
		if (qrtzJobExtend == null) {
			resultModel.setCode(CommonMessage.TABLE_NOT_FOUND_ERROR_CODE);
			resultModel.setMessage(CommonMessage.TABLE_NOT_FOUND_ERROR_DESC);
		}
		String jobName = qrtzJobExtend.getJobName();
		String jobGroup = qrtzJobExtend.getJobGroup();

		triggerJob(resultModel, jobName, jobGroup);
	}

	@Override
	public void triggerByJobCode(ResultModel resultModel, String jobCode) {
		QrtzJobExtend qrtzJobExtend = jobAppTriggerDao.getJobExtendByJobCode(jobCode);
		String jobName = qrtzJobExtend.getJobName();
		String jobGroup = qrtzJobExtend.getJobGroup();
		triggerJob(resultModel, jobName, jobGroup);

	}

	@Override
	public void createTriggerLogTable() {
		List<AppVo> list = jobAppTriggerDao.getAppList();
		for (AppVo app : list) {
			String tableName = CommonVar.LOG_TABLE_NAME_PREFIX + app.getAppKey();
			logger.info("Table name:" + tableName);
			jobAppTriggerDao.createTriggerLogTable(tableName);
		}

	}

	@Override
	public void bindAccount(ResultModel resultModel, String accountListJson, String appKey) {

		jobAppTriggerDao.unbindAccountByAppKey(appKey);
		List<String> list = JSONObject.parseArray(accountListJson, String.class);
		for (String account : list) {
			AppUserVo appUserVo = new AppUserVo();
			appUserVo.setAppKey(appKey);
			appUserVo.setUserAccount(account);
			jobAppTriggerDao.bindAccount(appUserVo);
		}
		resultModel.setCode(CommonMessage.OK_CODE);

	}

	@Override
	public void getBindAccount(ResultModel resultModel, String appKey) {
		resultModel.setCode(CommonMessage.OK_CODE);
		resultModel.setData(jobAppTriggerDao.getBindAccount(appKey));
	}

	@Override
	public Integer triggerCountPlus(String jobId, Integer executeStatus) {
		return jobAppTriggerDao.triggerCountPlus(jobId, executeStatus);
	}

	@Override
	public Integer addAsyncJobQueue(QrtzJobAsyncResult jobAsyncResult) {
		return jobAppTriggerDao.addAsyncJobQueue(jobAsyncResult);
	}

	@Override
	@Transactional
	public ResultModel resultUpdate(String sessionId, Integer executeStatus) {
		ResultModel resultModel = new ResultModel();
		resultModel.setCode(CommonMessage.OK_CODE);
		if (executeStatus != 1 && executeStatus != 2) {
			resultModel.setCode(CommonMessage.NO_AUTHORITY_ERROR_CODE);
			resultModel.setMessage(CommonMessage.NO_AUTHORITY_ERROR_DESC);
			return resultModel;
		}
		Integer affect = jobAppTriggerDao.resultUpdate(sessionId, executeStatus);
		if (affect != 1) {
			return resultModel;
		}
		QrtzTriggerLog log = jobAppTriggerDao.getRecQrtzTriggerLogById(sessionId);
		String appKey = log.getAppKey();
		String tableName = CommonVar.LOG_TABLE_NAME_PREFIX + appKey;
		jobAppTriggerDao.resultUpdateSonLog(tableName, sessionId, executeStatus);
		jobAppTriggerDao.asyncResultDelByLogId(sessionId);
		jobAppTriggerDao.triggerCountPlus(log.getJobId(), executeStatus);
		return resultModel;
	}

	String getJobNameByCode(String jobCode) {
		QrtzJobExtend qrtzJobExtend = jobAppTriggerDao.getJobExtendByJobCode(jobCode);
		if (qrtzJobExtend == null) {
			return null;
		}
		return qrtzJobExtend.getJobName();
	}

	void triggerJob(ResultModel resultModel, String jobName, String jobGroup) {
		try {
			scheduler.triggerJob(JobKey.jobKey(jobName, jobGroup));
			resultModel.setCode(CommonMessage.OK_CODE);
			resultModel.setMessage(CommonMessage.OK_DESC);
		} catch (SchedulerException e) {
			e.printStackTrace();
			resultModel.setCode(CommonMessage.JOB_EXE_ERROR_CODE);
			resultModel.setMessage(CommonMessage.JOB_EXE_ERROR_DESC);
		}

	}
}