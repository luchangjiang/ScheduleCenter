package com.giveu.dao.mysql;

import com.giveu.job.common.vo.MonitorLogVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by fox on 2018/8/22.
 */
public interface MonitorLogDAO {

	@Select("SELECT * FROM MONITOR_LOG WHERE log_create_time > DATE_SUB(NOW(), INTERVAL 5 MINUTE) AND log_create_time > (SELECT IF(COUNT(*) = 1, log_create_time, 0) FROM MONITOR_LOG WHERE id = #{id}) ORDER BY log_create_time DESC")
	List<MonitorLogVo> getErrorMonitorLog(String id);

	@Select("SELECT * FROM MONITOR_LOG WHERE id = #{id}")
	MonitorLogVo getMonitorLogById(String id);

	@Select("<script>" +
			"SELECT * FROM MONITOR_LOG where true " +
			"<if test = 'objName != null and objName != \"\" '>" +
				" and obj_name like CONCAT('%', #{objName}, '%') " +
			"</if>"+
			" ORDER BY log_create_time DESC limit 20 " +
			"</script>")
	List<MonitorLogVo> getMonitorErrorLog(@Param("objName") String objName);

	@Select("<script>" +
			"SELECT * FROM MONITOR_LOG where true " +
			"<if test = 'objIdList != null and objIdList.size() > 0 '>" +
			" and obj_id in <foreach item='item' index='index' collection='objIdList' open='(' separator=',' close=')'>#{item}</foreach> " +
			"</if>" +
			" ORDER BY log_create_time DESC limit 20 " +
			"</script>")
	List<MonitorLogVo> getMonitorLogByObjIdList(@Param("objIdList") List<String> objIdList);

	@Select("<script>" +
			"SELECT * FROM MONITOR_LOG where true " +
			" and obj_id in <foreach item='item' index='index' collection='objIdList' open='(' separator=',' close=')'>#{item}</foreach> " +
			" and log_create_time > DATE_SUB(NOW(), INTERVAL 30 DAY) " +
			"</script>")
	List<MonitorLogVo> getMonitorLogByObjIdListAndDays(@Param("objIdList") List<String> objIdList);
}
