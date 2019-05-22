package com.giveu.dao;

import com.giveu.job.common.entity.QrtzSchedulerState;
import com.giveu.job.common.entity.QrtzTriggerLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * Created by fox on 2019/1/17.
 */
@Mapper
@Component
public interface JobCommonDAO {


	@Select("SELECT job_name, job_code, execute_status, trigger_time FROM QRTZ_TRIGGER_LOG order by trigger_time desc limit 40")
	List<QrtzTriggerLog> getTriggerLog();

	@Select("select * from QRTZ_SCHEDULER_STATE")
	List<QrtzSchedulerState> getSchedulerState();

	@Select("SELECT NOW(3)")
	Date getNow();


}
