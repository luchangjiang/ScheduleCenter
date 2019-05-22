package com.giveu.dao;

import com.giveu.dto.JobAsyncDTO;
import com.giveu.job.common.entity.QrtzTriggerLog;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

/**
 * Created by fox on 2019/1/3.
 */

@Mapper
@Component
public interface JobAsyncDAO {

	@Select("SELECT * FROM QRTZ_JOB_ASYNC_RESULT a, QRTZ_JOB_EXTEND e WHERE e.id = a.job_id AND e.result_wait_time * 60 < (NOW() - a.trigger_time) LIMIT 1")
	JobAsyncDTO getJobAsyncDTO();

	@Delete("DELETE FROM `QRTZ_JOB_ASYNC_RESULT` WHERE log_id = #{logId}")
	Integer asyncResultDelByLogId(String logId);


	@Update("UPDATE `QRTZ_TRIGGER_LOG` SET execute_status = #{executeStatus}, result_update_time = now() WHERE id = #{sessionId} AND job_type = 2")
	Integer resultUpdate(@Param("sessionId") String sessionId, @Param("executeStatus") Integer executeStatus);

	@Update("UPDATE ${tableName} SET execute_status = #{executeStatus}, result_update_time = now() WHERE id = #{sessionId} AND job_type = 2")
	Integer resultUpdateSonLog(@Param("tableName") String tableName, @Param("sessionId") String sessionId, @Param("executeStatus") Integer executeStatus);

	@Update("UPDATE `QRTZ_TRIGGER_STAT` SET trigger_count = trigger_count+1, trigger_count_today = trigger_count_today+1 WHERE job_id = #{jobId} AND execute_status = #{executeStatus}")
	Integer triggerCountPlus(@Param("jobId") String jobId, @Param("executeStatus") Integer executeStatus);

	@Select("SELECT * FROM QRTZ_TRIGGER_LOG WHERE id = #{id}")
	QrtzTriggerLog getRecQrtzTriggerLogById(String id);


}
