package com.giveu.dao.mysql;

import com.giveu.entity.MonitorLog;
import com.giveu.entity.MonitorObject;
import com.giveu.job.common.vo.MonitorObjectVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by fox on 2018/7/18.
 */
public interface JobCenterDAO {

	List<MonitorObject> getMonitorObject();

	MonitorObject getMonitorObjectByCode(@Param(value="objCode")String objCode);

	Integer addMonitorLog(MonitorLog monitorLog);

	List<MonitorObject> list(MonitorObjectVo monitorObjectVo);

	List<MonitorLog> logList(@Param(value="objName")String objName, @Param(value="logBeginTime")Long logBeginTime, @Param(value="logEndTime")Long logEndTime);

	MonitorObject getMonitorById(String id);

	int updObjPolicySettings(@Param(value = "id") String id, @Param(value = "objPolicySettings") String objPolicySettings);

}
