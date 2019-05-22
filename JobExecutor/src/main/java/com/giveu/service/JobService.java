package com.giveu.service;

import com.giveu.job.common.vo.ResultModel;
import com.giveu.vo.JobParamVo;

/**
 * 任务Api接口
 * Created by fox on 2019/1/4.
 */
public interface JobService {

	/**
	 * 添加任务
	 * @param paramVo
	 * @return
	 */
	ResultModel addJob(JobParamVo paramVo);

	/**
	 * 删除任务
	 * @param jobCode
	 * @return
	 */
	ResultModel delJob(String jobCode);

	/**
	 * 更新任务
	 * @param paramVo
	 * @return
	 */
	ResultModel updJob(JobParamVo paramVo);

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
	 * 触发任务
	 * @param jobCode
	 * @return
	 */
	ResultModel triggerByCode(String jobCode);

	ResultModel triggerByLogId(String logId);

	ResultModel resultUpdate(String sessionId, Integer executeStatus);

	ResultModel getJobDetail(String jobCode);

	ResultModel lock(String isOpen, String passwd);

	ResultModel lockStatus();


}
