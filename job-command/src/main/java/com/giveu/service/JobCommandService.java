package com.giveu.service;

import com.giveu.job.common.vo.ResultModel;

/**
 * Created by fox on 2019/1/18.
 */
public interface JobCommandService {

	ResultModel jarList();

	ResultModel start(String proName, String proPort, String proActive, String passwd);

	ResultModel stop(String proName, String proPort, String proActive, String passwd);

	ResultModel restart(String proName, String proPort, String proActive, String passwd);

	ResultModel status(String proName, String proPort, String proActive);
}
