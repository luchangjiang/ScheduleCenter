package com.giveu.dao;

import com.giveu.job.common.vo.AppVo;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by fox on 2018/9/5.
 */
public interface AppInfoDAO {

	@Select("SELECT * FROM QRTZ_APP_INFO")
	List<AppVo> getAppInfoList();

}
