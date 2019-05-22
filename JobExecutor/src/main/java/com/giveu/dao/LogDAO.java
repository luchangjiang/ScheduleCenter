package com.giveu.dao;

import com.giveu.job.common.entity.QrtzTriggerLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

/**
 * Created by fox on 2019/1/5.
 */
@Mapper
@Component
public interface LogDAO {

	@Select("SELECT * FROM QRTZ_TRIGGER_LOG WHERE id = #{id}")
	QrtzTriggerLog getLogById(String id);
}
