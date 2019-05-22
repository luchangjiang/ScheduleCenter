package com.giveu.service;

import com.giveu.job.common.vo.ResultModel;

/**
 * Created by fox on 2018/9/5.
 */
public interface AppInfoService {

	void cacheAppInfoToRedis(ResultModel resultModel);
}
