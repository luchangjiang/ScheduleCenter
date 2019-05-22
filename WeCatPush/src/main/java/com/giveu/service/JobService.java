package com.giveu.service;

import com.giveu.job.common.vo.ResultModel;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by fox on 2018/12/1.
 */
public interface JobService {

	ResultModel getJobByCode(String jobCode);

	void logList(HttpServletRequest request, ResultModel resultModel);
}
