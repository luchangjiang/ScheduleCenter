package com.stylefeng.guns.modular.system.service;

import com.giveu.job.common.vo.JobTriggerAppVo;
import com.giveu.job.common.vo.ResultModel;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 任务列表Service
 *
 * @author fengshuonan
 * @Date 2018-07-03 17:03:27
 */
public interface IJobListService {

	void list(HttpServletRequest request, List<Map<String, Object>> list) throws IOException;

	ResultModel list();

	ResultModel addJob(JobTriggerAppVo jobTriggerAppVo);

	ResultModel delJobByCode(String jobCode);

	ResultModel triggerByLogId(String jobLogListId) throws IOException;

	ResultModel triggerByJobCode(String jobCode);

	ResultModel pauseJob(String jobCode);

	ResultModel resumeJob(String jobCode);

	JobTriggerAppVo getJobTriggerAppVo(String jobListId);

	ResultModel updJob(JobTriggerAppVo jobTriggerAppVo);

	ResultModel getJobList(String id, String jobCode, String appKey, String jobName, String triggerState, String userAccount);

}
