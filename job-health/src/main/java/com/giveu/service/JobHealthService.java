package com.giveu.service;

import com.giveu.job.common.vo.ResultModel;

/**
 * Created by fox on 2019/1/15.
 */
public interface JobHealthService {


	ResultModel heartbeat();

	ResultModel proList();

	ResultModel start(String proName, String proPort, String proActive, String passwd);

	ResultModel stop(String proName, String proPort, String proActive, String passwd);

	ResultModel restart(String proName, String proPort, String proActive, String passwd);


	ResultModel triggerLog();

	ResultModel heartbeatLog();

	ResultModel runningList();

	ResultModel scheduleState();

	ResultModel lock(String isOpen, String passwd);

	ResultModel lockStatus();

}
