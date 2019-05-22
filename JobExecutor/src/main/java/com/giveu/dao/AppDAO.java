package com.giveu.dao;

import com.giveu.job.common.entity.QrtzAppInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

/**
 * Created by fox on 2019/1/7.
 */
@Mapper
@Component
public interface AppDAO {

	@Select("SELECT * FROM QRTZ_APP_INFO WHERE app_key = #{appKey} and app_status = 1")
	QrtzAppInfo getAppInfoByAppKey(String appKey);

}
