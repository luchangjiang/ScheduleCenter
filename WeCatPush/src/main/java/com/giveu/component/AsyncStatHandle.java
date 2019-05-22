package com.giveu.component;

import com.giveu.dao.mysql.MonitorLogDAO;
import com.giveu.dao.redis.RedisUtil;
import com.giveu.entity.QrtzTriggerLog;
import com.giveu.entity.QrtzTriggerStat;
import com.giveu.dao.mysql.QrtzTriggerLogDAO;
import com.giveu.job.common.vo.JobTriggerAppVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by fox on 2018/8/16.
 */
@Component
public class AsyncStatHandle {
	public static Logger logger = LoggerFactory.getLogger(AsyncStatHandle.class);

	@SuppressWarnings("all")
	@Autowired
	QrtzTriggerLogDAO qrtzTriggerLogDAO;

	@SuppressWarnings("all")
	@Autowired
	MonitorLogDAO monitorLogDAO;

	@Autowired
	RedisUtil redisUtil;

	@Async("executor")
	public void recordInit() {
		logger.info("Initialization is start...");
		Long time = System.currentTimeMillis();
		List<JobTriggerAppVo> list = qrtzTriggerLogDAO.getJobList();
		for (JobTriggerAppVo job : list) {
			int count = qrtzTriggerLogDAO.getStatCountByJobId(job.getId());
			if (count >= 1) {
				continue;
			}
			String appName = qrtzTriggerLogDAO.getAppNameByAppKey(job.getAppKey());
			QrtzTriggerStat stat = new QrtzTriggerStat();
			stat.setJobId(job.getId());
			stat.setJobName(job.getJobName());
			stat.setJobCode(job.getJobCode());
			stat.setAppKey(job.getAppKey());
			stat.setAppName(appName);
			stat.setExecuteStatus(1);
			qrtzTriggerLogDAO.addStat(stat);
			stat.setExecuteStatus(2);
			qrtzTriggerLogDAO.addStat(stat);
		}
		logger.info("Initialization is completed... LeadTime : " + (System.currentTimeMillis() - time));
	}

	@Async("executor")
	public void recordLogStatTraining() {
		logger.info("LogStatTraining is start...");
		Long time = System.currentTimeMillis();
		QrtzTriggerLog qrtzTriggerLog = qrtzTriggerLogDAO.getEndLog();
		List<QrtzTriggerStat> list = qrtzTriggerLogDAO.getList();
		for (QrtzTriggerStat stat : list) {
			String startId = stat.getStatLogId();
			String endId = qrtzTriggerLog.getId();
			Integer logStatCount = qrtzTriggerLogDAO.getLogStatCount(stat.getJobId(), stat.getExecuteStatus(), startId, endId);
			Integer logStatCountToday = qrtzTriggerLogDAO.getLogStatCountToday(stat.getJobId(), stat.getExecuteStatus(), endId);
			qrtzTriggerLogDAO.updLogStatCount(stat.getId(), logStatCount);
			qrtzTriggerLogDAO.updLogStatId(stat.getId(), qrtzTriggerLog.getId());
			qrtzTriggerLogDAO.updLogStatCountToday(stat.getId(), logStatCountToday);
		}
		logger.info("LogStatTraining is completed...  LeadTime : " + (System.currentTimeMillis() - time));
	}
}
