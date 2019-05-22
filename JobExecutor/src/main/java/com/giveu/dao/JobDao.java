package com.giveu.dao;

import com.giveu.job.common.dto.JobDetailDTO;
import com.giveu.job.common.entity.QrtzJobExtend;
import com.giveu.job.common.entity.QrtzTriggerLog;
import com.giveu.job.common.vo.JobTriggerAppVo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

/**
 * Created by fox on 2019/1/4.
 */
@Mapper
@Component
public interface JobDAO {

	@Insert("INSERT into QRTZ_JOB_EXTEND(id, job_name, job_group, job_code, callback_url, app_key, job_desc, job_create_time, job_type) value(#{id}, #{jobName},#{jobGroup},#{jobCode},#{callbackUrl},#{appKey},#{jobDesc},#{jobCreateTime},#{jobType})")
	Integer addSyncJob(QrtzJobExtend qrtzJobExtend);

	@Insert("INSERT into QRTZ_JOB_EXTEND(id, job_name, job_group, job_code, callback_url, app_key, job_desc, job_create_time, job_type, result_wait_time, result_url) value(#{id}, #{jobName},#{jobGroup},#{jobCode},#{callbackUrl},#{appKey},#{jobDesc},#{jobCreateTime},#{jobType},#{resultWaitTime},#{resultUrl})")
	Integer addAsyncJob(QrtzJobExtend qrtzJobExtend);

	@Select("SELECT * FROM QRTZ_JOB_EXTEND WHERE job_name = #{jobName} limit 1")
	QrtzJobExtend getJobExtendByJobName(String jobName);

	@Select("SELECT * FROM QRTZ_JOB_EXTEND WHERE job_code = #{jobCode} limit 1")
	QrtzJobExtend getJobExtendByJobCode(String jobCode);

	@Delete("DELETE FROM QRTZ_JOB_EXTEND WHERE job_name = #{jobName}")
	Integer delJobExtendByJobName(String jobName);

	@Delete("DELETE FROM QRTZ_JOB_ASYNC_RESULT WHERE job_code = #{jobCode}")
	Integer delJobAsyncResultByJobCode(String jobCode);

	@Delete("delete FROM QRTZ_JOB_EXTEND WHERE job_code = #{jobCode}")
	Integer delJobExtendByJobCode(String jobCode);

//	@Update("UPDATE QRTZ_JOB_EXTEND SET callback_url = #{callbackUrl}, job_desc = #{jobDesc} WHERE job_code = #{jobCode}")
	@Update("<script> " +
		"UPDATE QRTZ_JOB_EXTEND  " +
		"<trim prefix='set' suffixOverrides=','>" +
			"<if test='callbackUrl!=null and callbackUrl.length > 0'>" +
				"callback_url=#{callbackUrl}," +
			"</if>" +
			"<if test='jobDesc!=null and jobDesc.length > 0'>" +
				"job_desc=#{jobDesc}," +
			"</if>" +
		"</trim>" +
		"WHERE job_code=#{jobCode}" +
		"</script>")
	Integer updSyncJob(QrtzJobExtend qrtzJobExtend);

//	@Update("UPDATE QRTZ_JOB_EXTEND SET callback_url = #{callbackUrl}, job_desc = #{jobDesc}, result_wait_time = #{resultWaitTime}, result_url = #{resultUrl} WHERE job_code = #{jobCode}")
	@Update("<script> " +
			"UPDATE QRTZ_JOB_EXTEND  " +
			"<trim prefix='set' suffixOverrides=','>" +
			"<if test='callbackUrl!=null and callbackUrl.length > 0'>" +
			"callback_url=#{callbackUrl}," +
			"</if>" +
			"<if test='jobDesc!=null and jobDesc.length > 0'>" +
			"job_desc=#{jobDesc}," +
			"</if>" +
			"<if test='resultWaitTime != null and resultWaitTime!=0'>" +
			"result_wait_time=#{resultWaitTime}," +
			"</if>" +
			"<if test='resultUrl!=null and jobCode.length > 0'>" +
			"result_url=#{resultUrl}," +
			"</if>" +
			"</trim>" +
			"WHERE job_code=#{jobCode}" +
			"</script>")
	Integer updAsyncJob(QrtzJobExtend qrtzJobExtend);

	@Select("SELECT d.*, t.trigger_state, c.cron_expression cron_expression FROM QRTZ_JOB_EXTEND d, QRTZ_TRIGGERS t, QRTZ_CRON_TRIGGERS c WHERE d.job_name = t.job_name AND d.job_group = t.job_group AND t.trigger_name = c.trigger_name AND t.trigger_group = c.trigger_group AND job_code = #{jobCode} limit 1")
	JobTriggerAppVo getJobByCode(String jobCode);

	@Select("SELECT d.*, t.trigger_state, c.cron_expression cron_expression FROM QRTZ_JOB_EXTEND d, QRTZ_TRIGGERS t, QRTZ_CRON_TRIGGERS c WHERE d.job_name = t.job_name AND d.job_group = t.job_group AND t.trigger_name = c.trigger_name AND t.trigger_group = c.trigger_group AND job_code = #{jobCode} limit 1")
	JobDetailDTO getJobDetailByCode(String jobCode);


	@Update("UPDATE `QRTZ_TRIGGER_LOG` SET execute_status = #{executeStatus}, result_update_time = now() WHERE id = #{sessionId} AND job_type = 2")
	Integer resultUpdate(@Param("sessionId") String sessionId, @Param("executeStatus") Integer executeStatus);

	@Update("UPDATE ${tableName} SET execute_status = #{executeStatus}, result_update_time = now() WHERE id = #{sessionId} AND job_type = 2")
	Integer resultUpdateSonLog(@Param("tableName") String tableName, @Param("sessionId") String sessionId, @Param("executeStatus") Integer executeStatus);

	@Update("UPDATE `QRTZ_TRIGGER_STAT` SET trigger_count = trigger_count+1, trigger_count_today = trigger_count_today+1 WHERE job_id = #{jobId} AND execute_status = #{executeStatus}")
	Integer triggerCountPlus(@Param("jobId") String jobId, @Param("executeStatus") Integer executeStatus);

	@Select("SELECT * FROM QRTZ_TRIGGER_LOG WHERE id = #{id}")
	QrtzTriggerLog getRecQrtzTriggerLogById(String id);

	@Delete("DELETE FROM `QRTZ_JOB_ASYNC_RESULT` WHERE log_id = #{logId}")
	Integer asyncResultDelByLogId(String logId);



}
