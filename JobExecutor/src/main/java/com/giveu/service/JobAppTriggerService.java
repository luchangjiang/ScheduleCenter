package com.giveu.service;


import com.giveu.job.common.dto.JobTriggerDTO;
import com.giveu.job.common.entity.*;
import com.giveu.job.common.vo.AppVo;
import com.giveu.job.common.vo.JobTriggerAppVo;
import com.giveu.job.common.vo.ResultModel;
import org.quartz.SchedulerException;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 任务调度核心接口
 */
public interface JobAppTriggerService {

	Integer addTriggerToLog(QrtzTriggerLog qrtzTriggerLog);

	Integer addTriggerToLogByTable(String tableName, QrtzTriggerLog qrtzTriggerLog);


	/**
	 * 是否需要执行
	 * @param qrtzTriggerLog
	 * @return
	 */
	Boolean isExecut(QrtzTriggerLog qrtzTriggerLog);

	/**
	 * 获得数据库时间
	 * @return
	 */
	Date getNow();

	/**
	 * 获取触发器列表（acquired 和 error）
	 * @return
	 */
	List<QrtzTriggers> getActiveTriggerList();

	/**
	 * 获取触发器列表（error）
	 * @return
	 */
	List<QrtzTriggers> getErrorTriggerList();

	/**
	 * 新增任务
	 * @param jobTriggerDTO
	 * @return
	 * @throws Exception
	 */
	Integer addJob(JobTriggerDTO jobTriggerDTO) throws Exception;

	/**
	 * 删除任务根据名称
	 * @param jobName
	 * @return
	 * @throws SchedulerException
	 */
	Integer delJobByName(String jobName) throws SchedulerException;

	/**
	 * 删除任务根据JobCode
	 * @param request
	 * @param resultModel
	 * @return
	 */
	Integer delJobByCode(HttpServletRequest request, ResultModel resultModel);

	/**
	 * 暂停任务
	 * @param jobName
	 * @throws SchedulerException
	 */
	void pauseJob(String jobName) throws SchedulerException;

	/**
	 * 根据JobCode暂停任务
	 * @param jobCode
	 */
	void pauseJobByCode(String jobCode, ResultModel resultModel);


	void resumeJobByCode(String jobCode, ResultModel resultModel);

	/**
	 * 恢复任务
	 * @param jobName
	 * @throws SchedulerException
	 */
	void resumeJob(String jobName) throws SchedulerException;

	/**
	 * 校验
	 * @param request
	 * @param resultModel
	 * @return
	 */
	Boolean check(HttpServletRequest request, ResultModel resultModel);

	/**
	 * 新增任务
	 * @param request
	 * @param resultModel
	 * @return
	 */
	Integer addJob(HttpServletRequest request, ResultModel resultModel);

	void getParToMap(HttpServletRequest request, Map<String, String> map);

	/**
	 * 获取任务拓展信息
	 * @param jobName
	 * @return
	 */
	QrtzJobExtend getJobExtendByJobName(String jobName);

	/**
	 * 获取任务信息根据ID
	 * @param id
	 * @return
	 */
	void getJobById(String id, ResultModel resultModel);

	void updJob(JobTriggerAppVo jobTriggerAppVo, ResultModel resultModel);

	/**
	 * 获取任务列表
	 * @param request
	 * @param resultModel
	 */
	void list(HttpServletRequest request, ResultModel resultModel);

	/**
	 * 获取日志列表
	 * @param request
	 * @param resultModel
	 */
	void logList(HttpServletRequest request, ResultModel resultModel);

	/**
	 * 获取应用列表
	 * @param request
	 * @param resultModel
	 */
	void appList(HttpServletRequest request, ResultModel resultModel);

	/**
	 * 根据account获取应用列表
	 * @param account
	 * @param resultModel
	 */
	void getListJsonByAccount(String account, ResultModel resultModel);

	/**
	 * 获得应用根据appkey
	 * @param appKey
	 * @return
	 */
	QrtzAppInfo getAppInfoByAppKey(String appKey);

	/**
	 * 新增app
	 * @param request
	 * @param resultModel
	 * @return
	 */
	Integer addApp(HttpServletRequest request, ResultModel resultModel);

	/**
	 * 更具appid获取app
	 * @param appId
	 * @return
	 */
	AppVo getAppVoById(String appId);

	/**
	 * 更新app
	 * @param request
	 * @param resultModel
	 */
	void updAppById(HttpServletRequest request, ResultModel resultModel);

	/**
	 * 删除app
	 * @param appId
	 * @return
	 */
	Integer delAppById(String appId);

	/**
	 * 启用app
	 * @param appId
	 * @return
	 */
	Integer enableById(String appId);

	/**
	 * 禁用app
	 * @param appId
	 * @return
	 */
	Integer disableById(String appId);

	/**
	 * 手动调用执行一次
	 * @param resultModel
	 * @param jobLogListId
	 */
	void triggerByLogId(ResultModel resultModel, String jobLogListId);

	/**
	 * 手动调用执行一次根据jobCode
	 * @param resultModel
	 * @param jobCode
	 */
	void triggerByJobCode(ResultModel resultModel, String jobCode);

	/**
	 * 根据AppKey创建日志表
	 */
	void createTriggerLogTable();


	void bindAccount(ResultModel resultModel, String accountListJson, String appKey);

	void getBindAccount(ResultModel resultModel, String appKey);

	/**
	 * 统计叠加
	 * @param jobId
	 * @param executeStatus
	 * @return
	 */
	Integer triggerCountPlus(String jobId, Integer executeStatus);

	/**
	 * Add Async Job Queue
	 * @param jobAsyncResult
	 * @return
	 */
	Integer addAsyncJobQueue(QrtzJobAsyncResult jobAsyncResult);


	ResultModel resultUpdate(String sessionId, Integer executeStatus);

	



}
