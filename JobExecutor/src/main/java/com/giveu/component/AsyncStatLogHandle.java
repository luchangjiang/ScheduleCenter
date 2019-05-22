package com.giveu.component;


import com.giveu.common.CommonVar;
import com.giveu.job.common.entity.QrtzJobAsyncResult;
import com.giveu.job.common.entity.QrtzTriggerLog;
import com.giveu.service.JobAppTriggerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Created by fox on 2018/8/16.
 */
@Component
public class AsyncStatLogHandle {

	@Value("${spring.profiles.active}")
	String active;

	@Autowired
	JobAppTriggerService jobAppTriggerService;

	private static final Integer JOB_SYNC_TYPE = 1;
	private static final Integer JOB_ASYNC_TYPE = 2;

	public static Logger logger = LoggerFactory.getLogger(AsyncStatLogHandle.class);

	@Async("executor")
	public void record(QrtzTriggerLog qrtzTriggerLog) {
		String tableName = CommonVar.LOG_TABLE_NAME_PREFIX + qrtzTriggerLog.getAppKey();
		jobAppTriggerService.addTriggerToLog(qrtzTriggerLog);

		// 不是备环境才写子日志表
		if (!"bak".equals(active)) {
			jobAppTriggerService.addTriggerToLogByTable(tableName, qrtzTriggerLog);
		}

		if (JOB_SYNC_TYPE.equals(qrtzTriggerLog.getJobType())) {
			jobAppTriggerService.triggerCountPlus(qrtzTriggerLog.getJobId(), qrtzTriggerLog.getExecuteStatus());
		}


		// If the Job is a async job
		if (JOB_ASYNC_TYPE.equals(qrtzTriggerLog.getJobType())) {

			QrtzJobAsyncResult jobAsyncResult = new QrtzJobAsyncResult();

			jobAsyncResult.setJobId(qrtzTriggerLog.getJobId());
			jobAsyncResult.setJobName(qrtzTriggerLog.getJobName());
			jobAsyncResult.setJobCode(qrtzTriggerLog.getJobCode());

			jobAsyncResult.setAppKey(qrtzTriggerLog.getAppKey());

			jobAsyncResult.setTriggerTime(qrtzTriggerLog.getTriggerTime());

			jobAsyncResult.setLogId(qrtzTriggerLog.getId());
			jobAsyncResult.setSessionId(qrtzTriggerLog.getSessionId());

			jobAppTriggerService.addAsyncJobQueue(jobAsyncResult);

		}

	}

}
