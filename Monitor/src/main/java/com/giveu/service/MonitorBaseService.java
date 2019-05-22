package com.giveu.service;

import com.giveu.job.common.vo.ResultModel;

/**
 * 监控基类接口
 * Created by fox on 2018/8/3.
 */
public interface MonitorBaseService {

	/**
	 * 核心处理
	 * @param resultModel
	 */
	void handle(ResultModel resultModel);
}
