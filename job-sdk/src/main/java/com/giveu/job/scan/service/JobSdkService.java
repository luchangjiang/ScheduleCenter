package com.giveu.job.scan.service;

import com.giveu.job.scan.model.ResultModel;

/**
 * 调度中心SDK接口
 * Created by fox on 2018/12/24.
 */
public interface JobSdkService {

	/**
	 * 新增同步任务（可设置默认是否为暂停状态）
	 * @param jobName
	 * @param jobCode
	 * @param jobDesc
	 * @param cronExpression
	 * @param callbackUrl
	 * @param isPause
	 * @return
	 */
	ResultModel addSyncJob(String jobName, String jobCode, String jobDesc, String cronExpression, String callbackUrl, Boolean isPause);

	/**
	 *
	 * @param jobName
	 * @param jobCode
	 * @param jobDesc
	 * @param cronExpression
	 * @param callbackUrl
	 * @param resultWaitTime
	 * @param resultUrl
	 * @param isPause
	 * @return
	 */
	ResultModel addAsyncJob(String jobName, String jobCode, String jobDesc, String cronExpression, String callbackUrl, Integer resultWaitTime, String resultUrl, Boolean isPause);

	/**
	 * 更新同步任务
	 * @param jobCode
	 * @param jobDesc
	 * @param cronExpression
	 * @param callbackUrl
	 * @return
	 */
	ResultModel updSyncJob(String jobCode, String jobDesc, String cronExpression, String callbackUrl);

	/**
	 * 更新异步任务
	 * @param jobCode
	 * @param jobDesc
	 * @param cronExpression
	 * @param callbackUrl
	 * @param resultWaitTime
	 * @param resultUrl
	 * @return
	 */
	ResultModel updAsyncJob(String jobCode, String jobDesc, String cronExpression, String callbackUrl, Integer resultWaitTime, String resultUrl);

	/**
	 * 删除任务
	 * @param jobCode
	 * @return
	 */
	ResultModel delJob(String jobCode);

	/**
	 * 暂停任务
	 * @param jobCode
	 * @return
	 */
	ResultModel pause(String jobCode);

	/**
	 * 恢复任务
	 * @param jobCode
	 * @return
	 */
	ResultModel resume(String jobCode);

	/**
	 * 手动执行
	 * @param jobCode
	 * @return
	 */
	ResultModel trigger(String jobCode);

	/**
	 * 异步任务结果更新
	 * @param sessionId
	 * @param executeStatus
	 * @return
	 */
	ResultModel resultUpdate(String sessionId, Integer executeStatus);

	/**
	 * 获取任务详情
	 * @param jobCode
	 * @return
	 */
	ResultModel getJobDetail(String jobCode);



}
