package com.giveu.dao;

import com.giveu.job.common.dto.JobAsyncDTO;
import com.giveu.job.common.entity.*;
import com.giveu.job.common.vo.AppUserVo;
import com.giveu.job.common.vo.AppVo;
import com.giveu.job.common.vo.JobTriggerAppLogVo;
import com.giveu.job.common.vo.JobTriggerAppVo;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

/**
 * 任务和触发相关DAO
 * 2018-6-28 create by fox
 */
public interface JobAppTriggerDao {

	@Select("SELECT NOW(3)")
	Date getNow();

	/**
	 * 查询任务列表信息
	 *
	 * @return
	 */
	@Select("SELECT * FROM QRTZ_JOB_DETAILS d, QRTZ_TRIGGERS t, QRTZ_CRON_TRIGGERS c WHERE d.job_name = t.job_name AND d.job_group = t.job_group AND t.trigger_name = c.trigger_name AND t.trigger_group = c.trigger_group")
	List<JobAndTrigger> getJobAndTriggerDetails();

	/**
	 * 查询所有开启的任务
	 *
	 * @return
	 */
	@Select("SELECT * FROM QRTZ_FIRED_TRIGGERS")
	List<QrtzFiredTriggers> getQrtzFiredTriggersList();

	/**
	 * 新增日志
	 *
	 * @param qrtzTriggerLog
	 * @return
	 */
	@Insert("INSERT into QRTZ_TRIGGER_LOG(id, job_id, job_name, job_code, job_desc, job_group, trigger_name, trigger_group, trigger_address, trigger_time, lead_time, callback_url, callback_count, session_id, execute_status, execute_desc, app_key, job_type, result_wait_time, result_url) " +
			"value(#{id}, #{jobId}, #{jobName}, #{jobCode}, #{jobDesc}, #{jobGroup},#{triggerName},#{triggerGroup},#{triggerAddress},#{triggerTime},#{leadTime},#{callbackUrl},#{callbackCount},#{sessionId},#{executeStatus},#{executeDesc}, #{appKey}, #{jobType}, #{resultWaitTime}, #{resultUrl})")
	Integer addTriggerToLog(QrtzTriggerLog qrtzTriggerLog);


	@Insert("INSERT into ${tableName}(id, job_id, job_name, job_code, job_desc, job_group, trigger_name, trigger_group, trigger_address, trigger_time, lead_time, callback_url, callback_count, session_id, execute_status, execute_desc, app_key, job_type, result_wait_time, result_url) " +
			"value(#{qrtzTriggerLog.id}, #{qrtzTriggerLog.jobId}, #{qrtzTriggerLog.jobName}, #{qrtzTriggerLog.jobCode}, #{qrtzTriggerLog.jobDesc}, #{qrtzTriggerLog.jobGroup},#{qrtzTriggerLog.triggerName},#{qrtzTriggerLog.triggerGroup},#{qrtzTriggerLog.triggerAddress},#{qrtzTriggerLog.triggerTime},#{qrtzTriggerLog.leadTime},#{qrtzTriggerLog.callbackUrl},#{qrtzTriggerLog.callbackCount},#{qrtzTriggerLog.sessionId},#{qrtzTriggerLog.executeStatus},#{qrtzTriggerLog.executeDesc}, #{qrtzTriggerLog.appKey}, #{qrtzTriggerLog.jobType}, #{qrtzTriggerLog.resultWaitTime}, #{qrtzTriggerLog.resultUrl})")
	Integer addTriggerToLogByTable(@Param("tableName") String tableName, @Param("qrtzTriggerLog") QrtzTriggerLog qrtzTriggerLog);


	/**
	 * 获取最新的三条日志记录
	 *
	 * @param qrtzTriggerLog
	 * @return
	 */
	@Select("SELECT * FROM QRTZ_TRIGGER_LOG WHERE job_name = #{jobName} and job_group = #{jobGroup} and trigger_name = #{triggerName} and trigger_group = #{triggerGroup} order by trigger_time desc limit 3")
	List<QrtzTriggerLog> getRecQrtzTriggerLog(QrtzTriggerLog qrtzTriggerLog);


	@Select("SELECT * FROM QRTZ_TRIGGER_LOG WHERE id = #{id}")
	QrtzTriggerLog getRecQrtzTriggerLogById(String id);

	@Select("<script> " +
			"SELECT count(*) FROM ${tableName} " +
			"WHERE 1 = 1 " +
			"<if test = 'jobId != null '>" +
			"and job_id = #{jobId} " +
			"</if>" +
			"<if test = 'jobCode != null '>" +
			"and job_code like CONCAT('%', #{jobCode}, '%') " +
			"</if>" +
			"<if test = 'executeStatus != 0 '>" +
			"and execute_status = #{executeStatus} " +
			"</if>" +
			"<if test = 'triggerBeginTime != null '>" +
			"and unix_timestamp(trigger_time)*1000 &gt;= #{triggerBeginTime} " +
			"</if>" +
			"<if test = 'triggerEndTime != null '>" +
			"and unix_timestamp(trigger_time)*1000 &lt;= #{triggerEndTime} " +
			"</if>" +
			"ORDER BY trigger_time DESC limit 100" +
			"</script>")
	Integer getRecQrtzTriggerLogCount(@Param("tableName") String tableName, @Param("triggerBeginTime") Long triggerBeginTime, @Param("triggerEndTime") Long triggerEndTime, @Param("jobId") String jobId, @Param("jobCode") String jobCode, @Param("executeStatus") Integer executeStatus);

	@Select("<script> " +
			"SELECT * FROM ${tableName} " +
			"WHERE 1 = 1 " +
			"<if test = 'jobId != null '>" +
			"and job_id = #{jobId} " +
			"</if>" +
			"<if test = 'jobCode != null '>" +
			"and job_code like CONCAT('%', #{jobCode}, '%') " +
			"</if>" +
			"<if test = 'executeStatus != 0 '>" +
			"and execute_status = #{executeStatus} " +
			"</if>" +
			"<if test = 'triggerBeginTime != null '>" +
			"and unix_timestamp(trigger_time)*1000 &gt;= #{triggerBeginTime} " +
			"</if>" +
			"<if test = 'triggerEndTime != null '>" +
			"and unix_timestamp(trigger_time)*1000 &lt;= #{triggerEndTime} " +
			"</if>" +
			"ORDER BY trigger_time DESC limit 100" +
			"</script>")
	List<JobTriggerAppLogVo> getRecQrtzTriggerLogList(@Param("tableName") String tableName, @Param("triggerBeginTime") Long triggerBeginTime, @Param("triggerEndTime") Long triggerEndTime, @Param("jobId") String jobId, @Param("jobCode") String jobCode, @Param("executeStatus") Integer executeStatus);

	@Insert("INSERT into QRTZ_TRIGGER_LOCK(id, job_name, job_group, trigger_name, trigger_group, trigger_time) value(#{id},#{jobName},#{jobGroup},#{triggerName},#{triggerGroup},#{triggerTime})")
	Integer addTriggerLock(QrtzTriggerLock qrtzTriggerLock);

	@Update("UPDATE QRTZ_TRIGGER_LOCK set trigger_time = #{triggerTime} WHERE job_name = #{jobName} and job_group = #{jobGroup} and trigger_name = #{triggerName} and trigger_group = #{triggerGroup}")
	Integer updTriggerLock(QrtzTriggerLog qrtzTriggerLog);

	@Select("SELECT * FROM QRTZ_TRIGGER_LOCK WHERE job_name = #{jobName} and job_group = #{jobGroup} and trigger_name = #{triggerName} and trigger_group = #{triggerGroup} order by trigger_time desc limit 1")
	QrtzTriggerLock getTriggerLock(QrtzTriggerLog qrtzTriggerLog);

	@Select("SELECT * FROM QRTZ_TRIGGERS WHERE trigger_state = 'acquired' or trigger_state = 'error'")
	List<QrtzTriggers> getActiveTriggerList();

	@Select("SELECT * FROM QRTZ_TRIGGERS WHERE trigger_state = 'error'")
	List<QrtzTriggers> getErrorTriggerList();

	/**
	 * 新增job扩展
	 *
	 * @param qrtzJobExtend
	 * @return
	 */
	@Insert("INSERT into QRTZ_JOB_EXTEND(id, job_name, job_group, job_code, callback_url, app_key, job_desc, job_create_time, job_type, result_wait_time, result_url) value(#{id}, #{jobName},#{jobGroup},#{jobCode},#{callbackUrl},#{appKey},#{jobDesc},#{jobCreateTime},#{jobType},#{resultWaitTime},#{resultUrl})")
	Integer addJobExtend(QrtzJobExtend qrtzJobExtend);

	@Delete("delete FROM QRTZ_JOB_EXTEND WHERE job_name = #{jobName}")
	Integer delJobExtendByJobName(String jobName);

	@Select("SELECT * FROM QRTZ_JOB_EXTEND WHERE job_code = #{jobCode} limit 1")
	QrtzJobExtend getJobExtendByJobCode(String jobCode);

	@Select("SELECT * FROM QRTZ_JOB_EXTEND WHERE job_name = #{jobName} limit 1")
	QrtzJobExtend getJobExtendByJobName(String jobName);

	@Select("SELECT * FROM QRTZ_JOB_EXTEND WHERE id = #{id} limit 1")
	QrtzJobExtend getJobExtendByJobId(String id);

	@Select("SELECT d.*, t.trigger_state, c.cron_expression cron_expression FROM QRTZ_JOB_EXTEND d, QRTZ_TRIGGERS t, QRTZ_CRON_TRIGGERS c WHERE d.job_name = t.job_name AND d.job_group = t.job_group AND t.trigger_name = c.trigger_name AND t.trigger_group = c.trigger_group AND id = #{id} limit 1")
	JobTriggerAppVo getJobById(String id);

	@Update("UPDATE QRTZ_JOB_EXTEND SET callback_url = #{callbackUrl}, job_desc = #{jobDesc}, result_wait_time = #{resultWaitTime}, result_url = #{resultUrl} WHERE job_code = #{jobCode}")
	Integer updJob(JobTriggerAppVo jobTriggerAppVo);

	@Select("<script> " +
			"SELECT d.id id, d.job_name job_name, d.job_code job_code, d.job_desc job_desc, d.job_create_time job_create_time, d.app_key app_key, c.cron_expression cron_expression, d.callback_url, t.trigger_state  FROM QRTZ_JOB_EXTEND d, QRTZ_TRIGGERS t, QRTZ_CRON_TRIGGERS c " +
			"WHERE d.job_name = t.job_name and d.job_group = t.job_group and t.trigger_name = c.trigger_name and t.trigger_group = c.trigger_group " +
			"<if test = 'id != null '>" +
			"and id = #{id} " +
			"</if>" +
			"<if test = 'jobCode != null '>" +
			"and job_code like CONCAT('%', #{jobCode}, '%') " +
			"</if>" +
			"<if test = 'appKey != null '>" +
			"and app_key like CONCAT('%', #{appKey} '%') " +
			"</if>" +
			"<if test = 'jobName != null '>" +
			"and d.job_name like CONCAT('%', #{jobName} '%') " +
			"</if>" +
			"<if test = 'triggerState != null '>" +
			"and t.trigger_state like CONCAT('%', #{triggerState} '%') " +
			"</if>" +
			"and app_key IN (SELECT app_key FROM `QRTZ_APP_USER` WHERE user_account = #{userAccount}) " +
			"order by job_create_time desc limit 100 " +
			"</script>")
	List<JobTriggerAppVo> getJobTriggerAppList(JobTriggerAppVo jobTriggerAppVo);

	@Select("SELECT * FROM QRTZ_APP_INFO WHERE app_key = #{appKey} and app_status = 1")
	QrtzAppInfo getAppInfoByAppKey(String appKey);

	@Select("<script> " +
			"SELECT * FROM QRTZ_APP_INFO " +
			"WHERE 1 = 1 " +
			"<if test = 'appName != null '>" +
			"and app_name like CONCAT('%', #{appName}, '%') " +
			"</if>" +
			"<if test = 'appKey != null '>" +
			"and app_key like CONCAT('%', #{appKey}, '%') " +
			"</if>" +
			"<if test = 'appStatus != 0 '>" +
			"and app_status = #{appStatus} " +
			"</if>" +
			"<if test = 'account != \"admin\" '>" +
			"and app_key IN(SELECT app_key FROM `QRTZ_APP_USER` WHERE user_account = #{account}) " +
			"</if>" +
			"order by app_create_time desc " +
			"</script>")
	List<AppVo> getAppInfoList(@Param("appName") String appName, @Param("appKey") String appKey, @Param("appStatus") Integer appStatus, @Param("account") String account);

	@Select("SELECT * FROM `QRTZ_APP_INFO` WHERE app_key IN(SELECT app_key FROM `QRTZ_APP_USER` WHERE user_account = #{account})")
	List<AppVo> getListJsonByAccount(String account);

	@Select("SELECT * FROM QRTZ_APP_INFO")
	List<AppVo> getAppList();

	@Insert("INSERT INTO QRTZ_APP_INFO(id, app_name, app_key, app_secret, app_status) value(#{id},#{appName},#{appKey},#{appSecret},#{appStatus})")
	Integer addApp(QrtzAppInfo qrtzAppInfo);

	@Select("SELECT * FROM QRTZ_APP_INFO where id = #{id}")
	QrtzAppInfo getQrtzAppInfoById(String id);

	@Update("UPDATE QRTZ_APP_INFO SET app_name = #{appName}, app_secret = #{appSecret}, app_status = #{appStatus} WHERE id = #{id}")
	Integer updAppById(QrtzAppInfo qrtzAppInfo);

	@Delete("DELETE FROM QRTZ_APP_INFO WHERE id = #{id}")
	Integer delAppById(String id);

	@Update("UPDATE QRTZ_APP_INFO SET app_status = 1 where id = #{id}")
	Integer enableAppById(String id);

	@Update("UPDATE QRTZ_APP_INFO SET app_status = 2 where id = #{id}")
	Integer disableAppById(String id);

	@Update("CREATE TABLE IF NOT EXISTS ${tableName} (\n" +
			"  `id` varchar(36) NOT NULL COMMENT '32位uuid字符串做主键',\n" +
			"  `job_id` varchar(36) NOT NULL COMMENT '任务ID',\n" +
			"  `job_name` varchar(200) NOT NULL COMMENT '任务名称',\n" +
			"  `job_code` varchar(200) NOT NULL COMMENT '任务标识',\n" +
			"  `job_desc` varchar(128) NOT NULL COMMENT '任务描述',\n" +
			"  `job_group` varchar(200) NOT NULL COMMENT '任务组',\n" +
			"  `app_key` varchar(200) NOT NULL COMMENT 'APPKEY',\n" +
			"  `trigger_name` varchar(200) NOT NULL COMMENT '触发器名称',\n" +
			"  `trigger_group` varchar(200) NOT NULL COMMENT '触发器组',\n" +
			"  `trigger_address` varchar(128) NOT NULL COMMENT 'ip地址加端口号，如192.168.0.1:8080',\n" +
			"  `trigger_time` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '触发时间，精确到毫秒',\n" +
			"  `lead_time` bigint(20) NOT NULL COMMENT '耗时',\n" +
			"  `callback_url` varchar(4096) NOT NULL COMMENT '回调地址',\n" +
			"  `callback_count` tinyint(4) NOT NULL COMMENT '回调次数',\n" +
			"  `session_id` varchar(64) NOT NULL COMMENT 'SessionId',\n" +
			"  `execute_status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '执行状态：1.成功；2.失败；3.重复',\n" +
			"  `execute_desc` varchar(4096) NOT NULL DEFAULT '' COMMENT '执行描述',\n" +
			"  `job_type` TINYINT NOT NULL DEFAULT '1' COMMENT '任务类型：1.同步任务；2.异步任务；',\n" +
			"  `result_url` VARCHAR(512) NOT NULL DEFAULT '' COMMENT '结果地址',\n" +
			"  `result_time` INT(11) UNSIGNED NOT NULL DEFAULT '0' COMMENT '结果时间',\n" +
			"  PRIMARY KEY (`id`),\n" +
			"  KEY `qrtz_trigger_log_index_trigger_time` (`trigger_time`),\n" +
			"  KEY `qrtz_trigger_log_index_execute_status` (`execute_status`),\n" +
			"  KEY `qrtz_trigger_log_index_job_id` (`job_id`)\n" +
			") ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='任务触发记录表 \\r\\n 记录每次触发执行的日志记录'")
	void createTriggerLogTable(@Param("tableName") String tableName);


	@Select("SELECT IFNULL(SUM(trigger_count), 0) triggerCount, SUM(IF(execute_status = 2, trigger_count, 0)) triggerFailCount FROM `QRTZ_TRIGGER_STAT` WHERE job_id = #{jobId}")
	JobTriggerAppVo getTriggerCountByJobId(String jobId);

	@Select("SELECT user_account FROM `QRTZ_APP_USER` WHERE app_key = #{appKey}")
	List<String> getBindAccount(String appKey);

	@Select("SELECT app_key FROM `QRTZ_APP_USER` WHERE user_account = #{account}")
	List<String> getAppKeyByAccount(String account);

	@Insert({"INSERT INTO QRTZ_APP_USER(user_account, app_key) VALUE(#{userAccount},#{appKey})"})
	Integer bindAccount(AppUserVo appUserVo);

	@Delete("DELETE FROM QRTZ_APP_USER WHERE app_key = #{appKey}")
	Integer unbindAccountByAppKey(String appKey);

	@Update("UPDATE `QRTZ_TRIGGER_STAT` SET trigger_count = trigger_count+1, trigger_count_today = trigger_count_today+1 WHERE job_id = #{jobId} AND execute_status = #{executeStatus}")
	Integer triggerCountPlus(@Param("jobId") String jobId, @Param("executeStatus") Integer executeStatus);


	@Insert("INSERT INTO QRTZ_JOB_ASYNC_RESULT(job_id, job_name, job_code, app_key, trigger_time, log_id, session_id) VALUE(#{jobId}, #{jobName}, #{jobCode}, #{appKey}, #{triggerTime}, #{logId}, #{sessionId})")
	Integer addAsyncJobQueue(QrtzJobAsyncResult jobAsyncResult);


	@Update("UPDATE `QRTZ_TRIGGER_LOG` SET execute_status = #{executeStatus}, result_update_time = now() WHERE id = #{sessionId} AND job_type = 2")
	Integer resultUpdate(@Param("sessionId") String sessionId, @Param("executeStatus") Integer executeStatus);

	@Update("UPDATE ${tableName} SET execute_status = #{executeStatus}, result_update_time = now() WHERE id = #{sessionId} AND job_type = 2")
	Integer resultUpdateSonLog(@Param("tableName") String tableName, @Param("sessionId") String sessionId, @Param("executeStatus") Integer executeStatus);


	@Delete("DELETE FROM `QRTZ_JOB_ASYNC_RESULT` WHERE log_id = #{logId}")
	Integer asyncResultDelByLogId(String logId);


	@Select("SELECT * FROM QRTZ_JOB_ASYNC_RESULT a, QRTZ_JOB_EXTEND e WHERE e.id = a.job_id AND e.result_wait_time * 60 < (NOW() - a.trigger_time) LIMIT 1")
	JobAsyncDTO getJobAsyncDTO();

}
