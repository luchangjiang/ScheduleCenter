package com.giveu.service.impl;

import com.giveu.common.CommLock;
import com.giveu.common.CommonVar;
import com.giveu.dao.AppDAO;
import com.giveu.dao.JobDAO;
import com.giveu.dao.LogDAO;
import com.giveu.dao.StatDAO;
import com.giveu.job.CallbackJob;
import com.giveu.job.common.dto.JobDetailDTO;
import com.giveu.job.common.entity.QrtzJobExtend;
import com.giveu.job.common.entity.QrtzTriggerLog;
import com.giveu.job.common.entity.QrtzTriggerStat;
import com.giveu.job.common.info.CommonMessage;
import com.giveu.job.common.util.CommonUtil;
import com.giveu.job.common.util.ObjectUtil;
import com.giveu.job.common.util.ValidUtil;
import com.giveu.job.common.vo.JobTriggerAppVo;
import com.giveu.job.common.vo.ResultModel;
import com.giveu.service.JobService;
import com.giveu.vo.JobParamVo;
import org.apache.commons.lang3.StringUtils;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

/**
 * 任务相关接口实现类
 * Created by fox on 2019/1/4.
 */
@Service
public class JobServiceImpl implements JobService {

	private static Logger logger = LoggerFactory.getLogger(JobServiceImpl.class);


	@Autowired
	JobDAO jobDAO;

	@Autowired
	StatDAO statDAO;

	@Autowired
	LogDAO logDAO;

	@Autowired
	AppDAO appDAO;

	// 加入Qulifier注解，通过名称注入bean
	@Autowired
	@Qualifier("Scheduler")
	private Scheduler scheduler;

	@Autowired
	CommLock lock;

	@Value("${job.lock.passwd}")
	String lockPasswd;

	// 执行成功状态
	private static final int EXECUTE_STATUS_SUCCESS = 1;

	// 执行失败状态
	private static final int EXECUTE_STATUS_FAILED = 2;

	private static final int SYNC_JOB_TYPE = 1;
	private static final int ASYNC_JOB_TYPE = 2;

	private static final int IS_PAUSE = 1;

	private static final String BOOLEAN_TRUE = "true";
	private static final String BOOLEAN_FALSE = "false";

	@Override
	@Transactional
	public ResultModel addJob(JobParamVo paramVo) {

		ResultModel resultModel = new ResultModel();
		resultModel.setCode(CommonMessage.ERROR_CODE);

		if (paramVo == null) {
			resultModel.setCode(CommonMessage.PAR_TYPE_ERROR_CODE);
			resultModel.setMessage(CommonMessage.PAR_TYPE_ERROR_DESC);
			return resultModel;
		}

		if (paramVo.getJobType() == null) {
			paramVo.setJobType(SYNC_JOB_TYPE);
		}

		if (!paramVo.getJobType().equals(SYNC_JOB_TYPE) && !paramVo.getJobType().equals(ASYNC_JOB_TYPE)) {
			resultModel.setCode(CommonMessage.PAR_TYPE_ERROR_CODE);
			resultModel.setMessage(CommonMessage.PAR_TYPE_ERROR_DESC + " JobType has to be 1 or 2.");
			return resultModel;
		}

		if (ObjectUtil.isEmptyBatch(paramVo.getJobName(), paramVo.getJobCode(), paramVo.getAppKey(), paramVo.getCronExpression(), paramVo.getCallbackUrl(), paramVo.getJobType())) {
			resultModel.setCode(CommonMessage.PAR_NOT_NULL_CODE);
			resultModel.setMessage(CommonMessage.PAR_NOT_NULL_DESC);
			return resultModel;
		}

		boolean b = ValidUtil.validField(paramVo.getJobCode());
		if (!b) {
			resultModel.setCode(CommonMessage.PAR_TYPE_ERROR_CODE);
			resultModel.setMessage(CommonMessage.PAR_TYPE_ERROR_DESC + " JobCode: " + paramVo.getJobCode());
			return resultModel;
		}

		if (paramVo.getJobType().equals(ASYNC_JOB_TYPE)) {
			if (ObjectUtil.isEmptyBatch(paramVo.getResultWaitTime(), paramVo.getResultUrl())) {
				resultModel.setCode(CommonMessage.PAR_NOT_NULL_CODE);
				resultModel.setMessage(CommonMessage.PAR_NOT_NULL_DESC);
				return resultModel;
			}

			if (paramVo.getResultWaitTime() < 10 || paramVo.getResultWaitTime() > 240) {
				resultModel.setCode(CommonMessage.PAR_TYPE_ERROR_CODE);
				resultModel.setMessage(CommonMessage.PAR_TYPE_ERROR_DESC);
				return resultModel;
			}

		} else {
			paramVo.setResultWaitTime(0);
			paramVo.setResultUrl("");
		}

		String cronExpression = paramVo.getCronExpression();

		if (StringUtils.isBlank(cronExpression) || !CronExpression.isValidExpression(cronExpression)) {
			resultModel.setCode(CommonMessage.CRON_EXPRESSION_ERROR_CODE);
			resultModel.setMessage(CommonMessage.CRON_EXPRESSION_ERROR_DESC);
			return resultModel;
		}

//		paramVo.setResultWaitTime(NumberUtils.toInt(String.valueOf(paramVo.getResultWaitTime()), 0));

//		if (paramVo.getResultUrl() == null) {
//			paramVo.setResultUrl("");
//		}

		QrtzJobExtend extend1 = jobDAO.getJobExtendByJobName(paramVo.getJobName());
		QrtzJobExtend extend2 = jobDAO.getJobExtendByJobCode(paramVo.getJobCode());
		if (extend1 != null) {
			String info = "Job Name already existed";
			resultModel.setCode(CommonMessage.ERROR_CODE);
			resultModel.setMessage(info);
			logger.info(info);
			return resultModel;
		}
		if (extend2 != null) {
			String info = "Job Code already existed";
			resultModel.setCode(CommonMessage.ERROR_CODE);
			resultModel.setMessage(info);
			logger.info(info);
			return resultModel;
		}

		QrtzJobExtend qrtzJobExtend = new QrtzJobExtend();
		BeanUtils.copyProperties(paramVo, qrtzJobExtend);

		// 构建job信息
		JobDetail jobDetail = JobBuilder.newJob(CallbackJob.class).withIdentity(paramVo.getJobName(), CommonVar.JOB_GROUP).build();
		// 表达式调度构建器(即任务执行的时间)
		CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(paramVo.getCronExpression());

		// 按新的cronExpression表达式构建一个新的trigger
		CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(paramVo.getJobName(), CommonVar.JOB_GROUP).withSchedule(scheduleBuilder).build();

		int affectedRows = 0;

		String uuid = CommonUtil.getUUID32();

		qrtzJobExtend.setId(uuid);
		qrtzJobExtend.setJobGroup(CommonVar.JOB_GROUP);
//		affectedRows = jobDAO.addJobExtend(qrtzJobExtend);

		if (paramVo.getJobType().equals(SYNC_JOB_TYPE)) {
			affectedRows = jobDAO.addSyncJob(qrtzJobExtend);
		}

		if (paramVo.getJobType().equals(ASYNC_JOB_TYPE)) {
			affectedRows = jobDAO.addAsyncJob(qrtzJobExtend);
		}

		if (affectedRows == 1) {
			QrtzTriggerStat stat = new QrtzTriggerStat();

			stat.setJobName(paramVo.getJobName());
			stat.setJobCode(paramVo.getJobCode());
			stat.setJobId(uuid);
			stat.setAppKey(paramVo.getAppKey());
			stat.setExecuteStatus(EXECUTE_STATUS_SUCCESS);
			statDAO.addStat(stat);
			stat.setExecuteStatus(EXECUTE_STATUS_FAILED);
			statDAO.addStat(stat);
			resultModel.setCode(CommonMessage.OK_CODE);
			resultModel.setMessage(CommonMessage.OK_DESC);

		}

		try {
			scheduler.scheduleJob(jobDetail, trigger);
		} catch (SchedulerException e) {
			e.printStackTrace();
			resultModel.setCode(CommonMessage.ERROR_CODE);
			resultModel.setMessage(e.getMessage());
		}


		if (paramVo.getIsPause() != null && paramVo.getIsPause().equals(IS_PAUSE)) {
			try {
				scheduler.pauseJob(JobKey.jobKey(qrtzJobExtend.getJobName(), CommonVar.JOB_GROUP));
				resultModel.setCode(CommonMessage.OK_CODE);
				resultModel.setMessage(CommonMessage.OK_DESC);
				logger.info("The JobName: " + qrtzJobExtend.getJobName() + " JobCode: " + qrtzJobExtend.getJobCode() + " default paused ");
			} catch (SchedulerException e) {
				e.printStackTrace();
				logger.error(e.getMessage(), e);
				resultModel.setCode(CommonMessage.SER_UNKNOW_ERROR_CODE);
				resultModel.setMessage(CommonMessage.SER_UNKNOW_ERROR_DESC);
			}
		}

		logger.info("The JobName: " + qrtzJobExtend.getJobName() + " JobCode: " + qrtzJobExtend.getJobCode() + "  added successfully ");

		return resultModel;
	}

	@Override
	@Transactional
	public ResultModel delJob(String jobCode) {

		ResultModel resultModel = new ResultModel();
		resultModel.setCode(CommonMessage.ERROR_CODE);
		resultModel.setMessage(CommonMessage.ERROR_DESC);

		if (StringUtils.isBlank(jobCode)) {
			resultModel.setCode(CommonMessage.PAR_NOT_NULL_CODE);
			resultModel.setMessage(CommonMessage.PAR_NOT_NULL_DESC);
			return resultModel;
		}

		String jobName = getJobNameByCode(jobCode);
		if (StringUtils.isBlank(jobName)) {
			resultModel.setCode(CommonMessage.TABLE_NOT_FOUND_ERROR_CODE);
			resultModel.setMessage(CommonMessage.TABLE_NOT_FOUND_ERROR_DESC);
			return resultModel;
		}

		int affectedRows = 0;
		affectedRows = jobDAO.delJobExtendByJobName(jobName);
		if (affectedRows > 1) {
			String info = "The number of deletions is greater than 1";
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			logger.error(info);
			resultModel.setMessage(info);
			return resultModel;
		}
		jobDAO.delJobAsyncResultByJobCode(jobCode);

		try {
			scheduler.pauseTrigger(TriggerKey.triggerKey(jobName, CommonVar.JOB_GROUP));
			scheduler.unscheduleJob(TriggerKey.triggerKey(jobName, CommonVar.JOB_GROUP));
			scheduler.deleteJob(JobKey.jobKey(jobName, CommonVar.JOB_GROUP));
		} catch (Exception e) {
			e.printStackTrace();
			resultModel.setMessage(e.getMessage());
			return resultModel;
		}

		resultModel.setCode(CommonMessage.OK_CODE);
		resultModel.setMessage(CommonMessage.OK_DESC);
		resultModel.setData(jobName);
		logger.info("The JobName: " + jobName + " has deleted");
		return resultModel;
	}

	@Override
	public ResultModel updJob(JobParamVo paramVo) {

		ResultModel resultModel = new ResultModel();
		resultModel.setCode(CommonMessage.ERROR_CODE);

		if (paramVo.getJobType() == null) {
			String info = "JobType is null";
			resultModel.setMessage(info);
			logger.info(info);
			return resultModel;
		}

		if (StringUtils.isBlank(paramVo.getJobCode())) {
			resultModel.setCode(CommonMessage.PAR_NOT_NULL_CODE);
			resultModel.setMessage(CommonMessage.PAR_NOT_NULL_DESC);
			return resultModel;
		}

		String cronExpression = paramVo.getCronExpression();

		if (StringUtils.isNotBlank(cronExpression) && !CronExpression.isValidExpression(cronExpression)) {
			resultModel.setCode(CommonMessage.CRON_EXPRESSION_ERROR_CODE);
			resultModel.setMessage(CommonMessage.CRON_EXPRESSION_ERROR_DESC);
			return resultModel;
		}

		QrtzJobExtend qrtzJobExtend = new QrtzJobExtend();
		BeanUtils.copyProperties(paramVo, qrtzJobExtend);

//		if (StringUtils.isBlank(paramVo.getCallbackUrl()) || StringUtils.isBlank(paramVo.getJobDesc())) {
//			resultModel.setCode(CommonMessage.PAR_NOT_NULL_CODE);
//			resultModel.setMessage(CommonMessage.PAR_NOT_NULL_DESC);
//			return resultModel;
//		}
//		QrtzJobExtend extend = jobDAO.getJobExtendByJobCode(paramVo.getJobCode());
//		if (extend == null) {
//			resultModel.setCode(CommonMessage.TABLE_NOT_FOUND_ERROR_CODE);
//			resultModel.setMessage(CommonMessage.TABLE_NOT_FOUND_ERROR_DESC);
//			return resultModel;
//		}

		JobTriggerAppVo jobVo = jobDAO.getJobByCode(paramVo.getJobCode());
		if (jobVo == null) {
			resultModel.setCode(CommonMessage.TABLE_NOT_FOUND_ERROR_CODE);
			resultModel.setMessage(CommonMessage.TABLE_NOT_FOUND_ERROR_DESC);
			return resultModel;
		}

		Integer affect = 0;

		if (jobVo.getJobType().equals(SYNC_JOB_TYPE)) {
			affect = jobDAO.updSyncJob(qrtzJobExtend);
		}

		if (jobVo.getJobType().equals(ASYNC_JOB_TYPE)) {
			Integer waitTime = qrtzJobExtend.getResultWaitTime();
			if (waitTime == null || waitTime < 10 || waitTime > 240) {
				resultModel.setCode(CommonMessage.PAR_TYPE_ERROR_CODE);
				resultModel.setMessage(CommonMessage.PAR_TYPE_ERROR_DESC + " WaitTime: " + waitTime);
				return resultModel;
			}
			affect = jobDAO.updAsyncJob(qrtzJobExtend);
		}

		if (affect > 0) {
			resultModel.setCode(CommonMessage.OK_CODE);
			resultModel.setMessage(CommonMessage.OK_DESC);
			resultModel.setData(affect);
		}

		if (paramVo.getCronExpression() != null && !jobVo.getCronExpression().equals(paramVo.getCronExpression())) {
			try {
				TriggerKey triggerKey = TriggerKey.triggerKey(jobVo.getJobName(), CommonVar.JOB_GROUP);
				// 表达式调度构建器
				CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(paramVo.getCronExpression());
				CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
				// 按新的cronExpression表达式重新构建trigger
				trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
				// 按新的trigger重新设置job执行
				scheduler.rescheduleJob(triggerKey, trigger);
			} catch (SchedulerException e) {
				e.printStackTrace();
				resultModel.setCode(CommonMessage.SER_UNKNOW_ERROR_CODE);
				resultModel.setMessage(CommonMessage.SER_UNKNOW_ERROR_DESC);
				return resultModel;
			}
		}
		logger.info("JobName: " + jobVo.getJobName() + " JobCode: " + jobVo.getJobCode() + " The Job update done...");
		return resultModel;
	}

	@Override
	public ResultModel pause(String jobCode) {

		ResultModel resultModel = new ResultModel();
		resultModel.setCode(CommonMessage.ERROR_CODE);
		resultModel.setMessage(CommonMessage.ERROR_DESC);

		if (StringUtils.isBlank(jobCode)) {
			resultModel.setCode(CommonMessage.PAR_NOT_NULL_CODE);
			resultModel.setMessage(CommonMessage.PAR_NOT_NULL_DESC);
			return resultModel;
		}

		String jobName = getJobNameByCode(jobCode);
		if (StringUtils.isBlank(jobName)) {
			resultModel.setCode(CommonMessage.TABLE_NOT_FOUND_ERROR_CODE);
			resultModel.setMessage(CommonMessage.TABLE_NOT_FOUND_ERROR_DESC);
			return resultModel;
		}
		try {
			scheduler.pauseJob(JobKey.jobKey(jobName, CommonVar.JOB_GROUP));
			resultModel.setCode(CommonMessage.OK_CODE);
			resultModel.setMessage(CommonMessage.OK_DESC);
			logger.info("The JobName: " + jobName + " JobCode: " + jobCode + " has paused ");
		} catch (SchedulerException e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			resultModel.setCode(CommonMessage.SER_UNKNOW_ERROR_CODE);
			resultModel.setMessage(CommonMessage.SER_UNKNOW_ERROR_DESC);
		}

		return resultModel;
	}

	@Override
	public ResultModel resume(String jobCode) {
		ResultModel resultModel = new ResultModel();
		resultModel.setCode(CommonMessage.ERROR_CODE);
		resultModel.setMessage(CommonMessage.ERROR_DESC);

		if (StringUtils.isBlank(jobCode)) {
			resultModel.setCode(CommonMessage.PAR_NOT_NULL_CODE);
			resultModel.setMessage(CommonMessage.PAR_NOT_NULL_DESC);
			return resultModel;
		}
		String jobName = getJobNameByCode(jobCode);
		if (StringUtils.isBlank(jobName)) {
			resultModel.setCode(CommonMessage.TABLE_NOT_FOUND_ERROR_CODE);
			resultModel.setMessage(CommonMessage.TABLE_NOT_FOUND_ERROR_DESC);
			return resultModel;
		}
		try {
			scheduler.resumeJob(JobKey.jobKey(jobName, CommonVar.JOB_GROUP));
			resultModel.setCode(CommonMessage.OK_CODE);
			resultModel.setMessage(CommonMessage.OK_DESC);
			logger.info("The JobName: " + jobName + " JobCode: " + jobCode + " is resuming ");
		} catch (SchedulerException e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			resultModel.setCode(CommonMessage.SER_UNKNOW_ERROR_CODE);
			resultModel.setMessage(CommonMessage.SER_UNKNOW_ERROR_DESC);
		}
		return resultModel;
	}

	@Override
	public ResultModel triggerByCode(String jobCode) {
		ResultModel resultModel = new ResultModel();
		resultModel.setCode(CommonMessage.ERROR_CODE);
		resultModel.setMessage(CommonMessage.ERROR_DESC);

		if (StringUtils.isBlank(jobCode)) {
			resultModel.setCode(CommonMessage.PAR_NOT_NULL_CODE);
			resultModel.setMessage(CommonMessage.PAR_NOT_NULL_DESC);
			return resultModel;
		}

		QrtzJobExtend qrtzJobExtend = jobDAO.getJobExtendByJobCode(jobCode);
		if (qrtzJobExtend == null) {
			resultModel.setCode(CommonMessage.TABLE_NOT_FOUND_ERROR_CODE);
			resultModel.setMessage(CommonMessage.TABLE_NOT_FOUND_ERROR_DESC);
			return resultModel;
		}
		String jobName = qrtzJobExtend.getJobName();
		triggerJob(jobName, resultModel);
		return resultModel;
	}

	@Override
	public ResultModel triggerByLogId(String logId) {
		ResultModel resultModel = new ResultModel();
		resultModel.setCode(CommonMessage.ERROR_CODE);
		resultModel.setMessage(CommonMessage.ERROR_DESC);

		if (StringUtils.isBlank(logId)) {
			resultModel.setCode(CommonMessage.PAR_NOT_NULL_CODE);
			resultModel.setMessage(CommonMessage.PAR_NOT_NULL_DESC);
			return resultModel;
		}
		QrtzTriggerLog log = logDAO.getLogById(logId);
		if (log == null) {
			resultModel.setCode(CommonMessage.TABLE_NOT_FOUND_ERROR_CODE);
			resultModel.setMessage(CommonMessage.TABLE_NOT_FOUND_ERROR_DESC);
			return resultModel;
		}
		String jobName = log.getJobName();
		triggerJob(jobName, resultModel);
		return resultModel;
	}

	String getJobNameByCode(String jobCode) {
		QrtzJobExtend qrtzJobExtend = jobDAO.getJobExtendByJobCode(jobCode);
		if (qrtzJobExtend == null) {
			return null;
		}
		return qrtzJobExtend.getJobName();
	}

	void triggerJob(String jobName, ResultModel resultModel) {
		try {
			scheduler.triggerJob(JobKey.jobKey(jobName, CommonVar.JOB_GROUP));
			resultModel.setCode(CommonMessage.OK_CODE);
			resultModel.setMessage(CommonMessage.OK_DESC);
			logger.info("Trigger the task is successful. JobName: " + jobName);
		} catch (SchedulerException e) {
			e.printStackTrace();
			resultModel.setCode(CommonMessage.ERROR_CODE);
			resultModel.setMessage(CommonMessage.ERROR_DESC);
			logger.info("The trigger has failed. Contact the developer Fox as soon as possible. JobName: " + jobName);
		}
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
		Integer affect = jobDAO.resultUpdate(sessionId, executeStatus);
		if (affect != 1) {
			return resultModel;
		}
		QrtzTriggerLog log = jobDAO.getRecQrtzTriggerLogById(sessionId);
		String appKey = log.getAppKey();
		String tableName = CommonVar.LOG_TABLE_NAME_PREFIX + appKey;
		jobDAO.resultUpdateSonLog(tableName, sessionId, executeStatus);
		jobDAO.asyncResultDelByLogId(sessionId);
		jobDAO.triggerCountPlus(log.getJobId(), executeStatus);
		return resultModel;
	}

	@Override
	public ResultModel getJobDetail(String jobCode) {
		ResultModel resultModel = new ResultModel();
		resultModel.setCode(CommonMessage.OK_CODE);
		resultModel.setMessage(CommonMessage.OK_DESC);
		JobDetailDTO dto = jobDAO.getJobDetailByCode(jobCode);
		resultModel.setData(dto);
		return resultModel;
	}

	@Override
	public ResultModel lock(String isOpen, String passwd) {
		ResultModel resultModel = new ResultModel();
		resultModel.setCode(CommonMessage.OK_CODE);
		resultModel.setMessage(CommonMessage.OK_DESC);

		if (StringUtils.isBlank(isOpen) || StringUtils.isBlank(passwd)) {
			resultModel.setCode(CommonMessage.PAR_NOT_NULL_CODE);
			resultModel.setMessage(CommonMessage.PAR_NOT_NULL_DESC);
			return resultModel;
		}

		if (!passwd.equals(lockPasswd)) {
			resultModel.setCode(CommonMessage.NO_AUTHORITY_ERROR_CODE);
			resultModel.setMessage(CommonMessage.NO_AUTHORITY_ERROR_DESC);
			return resultModel;
		}

		if (!BOOLEAN_TRUE.equals(isOpen) && !BOOLEAN_FALSE.equals(isOpen)) {
			resultModel.setCode(CommonMessage.PAR_TYPE_ERROR_CODE);
			resultModel.setMessage(CommonMessage.PAR_TYPE_ERROR_DESC);
			return resultModel;
		}

		if (BOOLEAN_TRUE.equals(isOpen)) {
			lock.isOpen = true;
		}
		if (BOOLEAN_FALSE.equals(isOpen)) {
			lock.isOpen = false;
		}

		return resultModel;
	}

	@Override
	public ResultModel lockStatus() {
		ResultModel resultModel = new ResultModel();
		resultModel.setCode(CommonMessage.OK_CODE);
		resultModel.setMessage(CommonMessage.OK_DESC);
		resultModel.setData(lock.isOpen);
		return resultModel;
	}


}
