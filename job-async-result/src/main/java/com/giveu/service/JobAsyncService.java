package com.giveu.service;

import com.giveu.job.common.vo.ResultModel;

/**
 * Created by fox on 2019/1/3.
 */
public interface JobAsyncService {


	ResultModel queueHandler();

	ResultModel resultUpdate(String sessionId, Integer executeStatus);

}
