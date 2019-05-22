package com.giveu.dao;

import com.giveu.job.common.entity.QrtzTriggerStat;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * Created by fox on 2019/1/4.
 */
@Mapper
@Component
public interface StatDAO {

	@Insert("INSERT INTO QRTZ_TRIGGER_STAT(job_id, job_name, job_code, app_key, execute_status) VALUE(#{jobId}, #{jobName}, #{jobCode}, #{appKey}, #{executeStatus})")
	Integer addStat(QrtzTriggerStat stat);

}
