package com.giveu.service;

import com.giveu.job.common.vo.ResultModel;

import javax.servlet.http.HttpServletRequest;

/**
 * 微信推送服务接口
 * Created by fox on 2018/8/22.
 */
public interface WeCatService {

	void training();

	void getErrorLog(ResultModel resultModel);

	void getErrorLogByJobId(String id, ResultModel resultModel);

	void detail(String id, ResultModel resultModel);
	String triggerByLogId(String id);

	void monitorTraining();

	void monitorDetail(String id, ResultModel resultModel);

	void monitorErrorLog(String objName, ResultModel resultModel);


	void getMonitorLogByObjIdList(String objIdListJson, ResultModel resultModel);

	void getMonitorLogByObjIdListAndDays(String objIdListJson, ResultModel resultModel);

	void logStatTraining();

	void logStatInit();

	/**
	 * 任务日志统计
	 * @param resultModel
	 */
	void jobLogStat(HttpServletRequest request, ResultModel resultModel);

	/**
	 * 主题轮训统计
	 */
	void topStatTraining();

	/**
	 * 按日统计
	 */
	void dayStatTraining();

	void getQrtzTriggerDayStat(String startTime, String endTime, String account, ResultModel resultModel);

//	List<QrtzTriggerDayStat> getQrtzTriggerDayStatList(String account);

	void getJobTopStat(String jobIdListJson, ResultModel resultModel);
}
