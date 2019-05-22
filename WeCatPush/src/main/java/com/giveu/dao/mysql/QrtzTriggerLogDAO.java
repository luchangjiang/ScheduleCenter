package com.giveu.dao.mysql;

import com.giveu.entity.*;
import com.giveu.job.common.dto.QrtzTriggerLogDTO;
import com.giveu.job.common.vo.AppVo;
import com.giveu.job.common.vo.JobTriggerAppLogVo;
import com.giveu.job.common.vo.JobTriggerAppVo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

/**
 * Created by fox on 2018/8/22.
 */
public interface QrtzTriggerLogDAO {

	@Select("SELECT * FROM QRTZ_TRIGGER_LOG WHERE trigger_time > DATE_SUB(NOW(), INTERVAL 7 MINUTE) AND execute_status = 2 AND trigger_time > (SELECT IF(COUNT(*) = 1, trigger_time, 0) FROM QRTZ_TRIGGER_LOG WHERE id = #{id}) ORDER BY trigger_time DESC")
	List<QrtzTriggerLog> getErrorTriggerLog(String id);

	@Select("SELECT * FROM QRTZ_TRIGGER_LOG WHERE trigger_time > DATE_SUB(NOW(), INTERVAL 12 HOUR) AND execute_status = 2 ORDER BY trigger_time DESC")
	List<QrtzTriggerLog> getErrorTriggerLogx();

	@Select("SELECT * FROM QRTZ_TRIGGER_LOG WHERE id = #{id}")
	QrtzTriggerLog getQrtzTriggerLogById(String id);

	@Select("SELECT * FROM QRTZ_TRIGGER_LOG WHERE job_id = #{jobId} and execute_status = 2 ORDER BY trigger_time DESC limit 20")
	List<QrtzTriggerLogDTO> getErrorTriggerLogByJobId(String jobId);


	@Select("<script>" +
			"SELECT * FROM QRTZ_TRIGGER_STAT " +
			"WHERE true " +
			"<if test = 'appName != null and appName != \"\" '>" +
			" and app_name like CONCAT('%', #{appName}, '%') " +
			"</if>" +
			"<if test = 'jobName != null and jobName != \"\" '>" +
			" and job_name like CONCAT('%', #{jobName}, '%') " +
			"</if>" +
			"<if test = 'jobId != null and jobId != \"\" '>" +
			" and job_id like CONCAT('%', #{jobId}, '%') " +
			"</if>" +
			"<if test = 'jobIdList != null and jobIdList.size() > 0 '>" +
			" and job_id in <foreach item='item' index='index' collection='jobIdList' open='(' separator=',' close=')'>#{item}</foreach> " +
			"</if>" +
			"</script>")
	List<QrtzTriggerStat> getTriggerStatList(@Param("appName") String appName, @Param("jobName") String jobName, @Param("jobId") String jobId, @Param("jobIdList") List<String> jobIdList);

	@Select("SELECT * FROM QRTZ_TRIGGER_STAT")
	List<QrtzTriggerStat> getList();

	@Select("SELECT * FROM QRTZ_TRIGGER_LOG ORDER BY trigger_time DESC LIMIT 1")
	QrtzTriggerLog getEndLog();

	@Select("SELECT COUNT(*) FROM QRTZ_TRIGGER_LOG WHERE job_id = #{jobId} AND execute_status = #{executeStatus} AND trigger_time >= (SELECT IF(COUNT(*) = 1, trigger_time, 0) FROM QRTZ_TRIGGER_LOG WHERE id = #{startId}) and trigger_time < (SELECT IF(COUNT(*) = 1, trigger_time, 0) FROM QRTZ_TRIGGER_LOG WHERE id = #{endId}) ORDER BY trigger_time DESC")
	Integer getLogStatCount(@Param("jobId") String jobId, @Param("executeStatus") int executeStatus, @Param("startId") String startId, @Param("endId") String endId);

	@Select("SELECT COUNT(*) FROM QRTZ_TRIGGER_LOG WHERE job_id = #{jobId} AND execute_status = #{executeStatus} AND trigger_time >= FROM_UNIXTIME(UNIX_TIMESTAMP(CAST(SYSDATE()AS DATE))) AND trigger_time < (SELECT IF(COUNT(*) = 1, trigger_time, 0) FROM QRTZ_TRIGGER_LOG WHERE id = #{endId})")
	Integer getLogStatCountToday(@Param("jobId") String jobId, @Param("executeStatus") int executeStatus, @Param("endId") String endId);

	@Update("UPDATE QRTZ_TRIGGER_STAT SET trigger_count = trigger_count + #{count} WHERE id = #{id}")
	Integer updLogStatCount(@Param("id") int id, @Param("count") int count);

	@Update("UPDATE QRTZ_TRIGGER_STAT SET trigger_count_today = #{count} WHERE id = #{id}")
	Integer updLogStatCountToday(@Param("id") int id, @Param("count") int count);

	@Update("UPDATE QRTZ_TRIGGER_STAT SET stat_log_id = #{logId} WHERE id = #{id}")
	Integer updLogStatId(@Param("id") int id, @Param("logId") String logId);

	@Select("SELECT * FROM QRTZ_JOB_EXTEND")
	List<JobTriggerAppVo> getJobList();

	@Select("SELECT * FROM QRTZ_JOB_EXTEND where job_code = #{jobCode} limit 1")
	JobTriggerAppVo getJobByCode(String jobCode);

	@Select("SELECT COUNT(*) FROM QRTZ_TRIGGER_STAT WHERE job_id = #{jobId}")
	Integer getStatCountByJobId(String jobId);

	@Insert("INSERT INTO QRTZ_TRIGGER_STAT(job_id, job_name, job_code, app_key, app_name, execute_status) VALUE(#{jobId}, #{jobName}, #{jobCode}, #{appKey}, #{appName}, #{executeStatus})")
	Integer addStat(QrtzTriggerStat stat);

	@Select("SELECT app_name FROM QRTZ_APP_INFO WHERE app_key = #{appKey}")
	String getAppNameByAppKey(String appKey);

	@Update("UPDATE QRTZ_TRIGGER_STAT SET app_name = #{appName} where id = #{id}")
	Integer updAppName(@Param("id") int id, @Param("appName") String appName);


	@Select("SELECT job_id, job_name, COUNT(*) trigger_count, SUM(lead_time) lead_time_sum FROM QRTZ_TRIGGER_LOG WHERE trigger_time >= FROM_UNIXTIME(UNIX_TIMESTAMP(CAST(SYSDATE()AS DATE) - INTERVAL #{days} DAY)) GROUP BY job_id, job_name ORDER BY trigger_count DESC")
	List<QrtzTriggerTopFrequency> getQrtzTriggerTopFrequency(Integer days);

	@Select("SELECT job_id, job_name, COUNT(*) trigger_count, SUM(lead_time) lead_time_sum FROM QRTZ_TRIGGER_LOG WHERE execute_status = 2 AND trigger_time >= FROM_UNIXTIME(UNIX_TIMESTAMP(CAST(SYSDATE()AS DATE) - INTERVAL #{days} DAY)) GROUP BY job_id, job_name ORDER BY trigger_count DESC")
	List<QrtzTriggerTopFrequency> getQrtzTriggerTopFail(Integer days);

	@Update("UPDATE QRTZ_TRIGGER_STAT_TOP SET content = #{content} WHERE top = #{top}")
	Integer updTop(@Param("top") String top, @Param("content") String content);

	@Select("SELECT * FROM QRTZ_TRIGGER_STAT_TOP")
	List<JobTop> getJobTopStat();


	@Select("SELECT * FROM QRTZ_APP_INFO")
	List<AppVo> getAppList();

	@Select("SELECT MAX(lead_time) max_lead_time, MIN(lead_time) min_lead_time, SUM(lead_time) lead_time_sum, COUNT(*) trigger_day_count FROM QRTZ_TRIGGER_LOG WHERE trigger_time > DATE_SUB(NOW(), INTERVAL 1 DAY)")
	Map<String, Object> getLeadTimeTopBaseData();

	@Select("SELECT COUNT(*) FROM QRTZ_TRIGGER_LOG WHERE lead_time >= 10000 AND trigger_time > DATE_SUB(NOW(), INTERVAL 1 DAY)")
	Integer getMoreThanCount();

	@Select("SELECT job_name, job_id, job_code, app_key, COUNT(*) trigger_count, COUNT(IF(execute_status = 2, 1, NULL)) trigger_fail_count, MAX(lead_time) max_lead_time, MIN(lead_time) min_lead_time, SUM(lead_time) lead_time_sum, DATE_SUB(CURDATE(),INTERVAL 1 DAY) trigger_date FROM QRTZ_TRIGGER_LOG WHERE  DATE(trigger_time) = DATE_SUB(CURDATE(),INTERVAL 1 DAY) GROUP BY job_name, job_id, job_code, app_key")
	List<QrtzTriggerDayStat> getQrtzTriggerDayStatInfo();

	@Insert("INSERT INTO QRTZ_TRIGGER_STAT_DAY(job_id, job_name, job_code, job_create_time, app_key, app_name, trigger_count, trigger_fail_count, max_lead_time, min_lead_time, lead_time_sum, trigger_date) VALUE(#{jobId}, #{jobName}, #{jobCode}, #{jobCreateTime}, #{appKey}, #{appName}, #{triggerCount}, #{triggerFailCount}, #{maxLeadTime}, #{minLeadTime}, #{leadTimeSum}, #{triggerDate})")
	Integer addStatDay(QrtzTriggerDayStat qrtzTriggerDayStat);

	@Select("SELECT job_name, job_id, job_code, max(app_key) app_key, max(app_name) app_name, max(job_create_time) job_create_time, SUM(trigger_count) trigger_count, SUM(trigger_fail_count) trigger_fail_count, MAX(max_lead_time) max_lead_time, MIN(min_lead_time) min_lead_time, SUM(lead_time_sum) lead_time_sum FROM QRTZ_TRIGGER_STAT_DAY WHERE app_key IN (SELECT app_key FROM `QRTZ_APP_USER` WHERE user_account = #{userAccount}) and trigger_date >= FROM_UNIXTIME(#{startTime}) AND trigger_date < FROM_UNIXTIME(#{endTime}) GROUP BY job_name, job_id, job_code, app_key")
	List<QrtzTriggerDayStat> getQrtzTriggerDayStatByJobName(@Param("startTime") Long startTime, @Param("endTime") Long endTime, @Param("userAccount") String userAccount);

	@Select("SELECT SUM(trigger_count) trigger_count, SUM(trigger_fail_count) trigger_fail_count, trigger_date FROM QRTZ_TRIGGER_STAT_DAY WHERE app_key IN (SELECT app_key FROM `QRTZ_APP_USER` WHERE user_account = #{userAccount}) and trigger_date >= FROM_UNIXTIME(#{startTime}) AND trigger_date < FROM_UNIXTIME(#{endTime}) GROUP BY trigger_date ORDER BY trigger_date ASC")
	List<QrtzTriggerDayStat> getQrtzTriggerDayStatByTriggerDate(@Param("startTime") Long startTime, @Param("endTime") Long endTime, @Param("userAccount") String userAccount);

	@Select("SELECT SUM(trigger_count) trigger_count, SUM(trigger_fail_count) trigger_fail_count, app_key FROM QRTZ_TRIGGER_STAT_DAY WHERE app_key IN (SELECT app_key FROM `QRTZ_APP_USER` WHERE user_account = #{userAccount}) and trigger_date >= FROM_UNIXTIME(#{startTime}) AND trigger_date < FROM_UNIXTIME(#{endTime}) GROUP BY app_key ORDER BY app_key ASC")
	List<QrtzTriggerDayStat> getQrtzTriggerDayStatByAppKey(@Param("startTime") Long startTime, @Param("endTime") Long endTime, @Param("userAccount") String userAccount);

	@Select("<script> SELECT * FROM QRTZ_TRIGGER_STAT_DAY WHERE trigger_date > DATE_SUB(NOW(), INTERVAL 30 DAY) and job_id in <foreach item='item' index='index' collection='jobIdList' open='(' separator=',' close=')'>#{item}</foreach> </script>")
	List<QrtzTriggerDayStat> getQrtzTriggerDayStat(@Param("jobIdList") List<String> jobIdList);


	@Select("SELECT app_key FROM `QRTZ_APP_USER` WHERE user_account = #{account}")
	List<String> getAppKeyByAccount(String account);

	@Select("SELECT * FROM QRTZ_APP_INFO WHERE app_key = #{appKey} and app_status = 1")
	QrtzAppInfo getAppInfoByAppKey(String appKey);


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

}
