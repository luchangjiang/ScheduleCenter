package com.giveu.service;

import com.giveu.job.common.vo.ResultModel;
import com.giveu.job.common.vo.AppVo;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "job-executor")
public interface SchedualService {
    @RequestMapping(value = "/job/resumejob")
    String resumejob(@RequestParam(value = "jobClassName") String jobClassName, @RequestParam(value = "jobGroupName") String jobGroupName) throws Exception;

    @RequestMapping(value = "/job/pausejob")
    String pausejob(@RequestParam(value = "jobClassName") String jobClassName, @RequestParam(value = "jobGroupName") String jobGroupName) throws Exception;

    @RequestMapping(value = "/job/resumerunjob")
    String resumerunjob();

    @RequestMapping(value = "/job/list")
	ResultModel jobList(@RequestParam(value = "id") String id, @RequestParam(value = "jobCode") String jobCode, @RequestParam(value = "appKey") String appKey);

    @RequestMapping(value = "/job/log/list")
    ResultModel logList(@RequestParam(value = "beginTime") String beginTime,@RequestParam(value = "endTime") String endTime,@RequestParam(value = "jobId") String jobId,@RequestParam(value = "jobCode") String jobCode,@RequestParam(value = "executeStatus") String executeStatus);

    @PostMapping(value = "/job/app/list")
    ResultModel appList(@RequestParam(value = "appName") String appName, @RequestParam(value = "appKey") String appKey, @RequestParam(value = "appStatus") String appStatus);

    @PostMapping(value = "/job/add")
    ResultModel addJob(@RequestParam(value = "jobName") String jobName, @RequestParam(value = "jobCode") String jobCode, @RequestParam(value = "jobDesc") String jobDesc, @RequestParam(value = "callbackUrl") String callbackUrl, @RequestParam(value = "cronExpression") String cronExpression,
                       @RequestHeader(value = "xGiveuSign") String xGiveuSign, @RequestHeader(value = "xGiveuAppKey") String xGiveuAppKey, @RequestHeader(value = "xGiveuTimestamp") String xGiveuTimestamp);

    @PostMapping(value = "/job/del/code")
    ResultModel delJobByCode(@RequestParam(value = "jobCode") String jobCode, @RequestHeader(value = "xGiveuSign") String xGiveuSign, @RequestHeader(value = "xGiveuAppKey") String xGiveuAppKey, @RequestHeader(value = "xGiveuTimestamp") String xGiveuTimestamp);

    @PostMapping(value = "/job/app/add")
    ResultModel addApp(@RequestParam(value = "appName") String appName, @RequestParam(value = "appStatus") String appStatus, @RequestParam(value = "appKey") String appKey, @RequestParam(value = "appSecret") String appSecret);

    @PostMapping(value = "/job/app/upd/id")
    ResultModel updAppById(@RequestParam(value = "id") String id, @RequestParam(value = "appName") String appName, @RequestParam(value = "appStatus") String appStatus, @RequestParam(value = "appKey") String appKey, @RequestParam(value = "appSecret") String appSecret);

    @PostMapping(value = "/job/app/get/id")
    AppVo getAppVoById(@RequestParam(value = "appId") String appName);

    @PostMapping(value = "/job/app/del/id")
    Integer delAppById(@RequestParam(value = "appId") String appId);

    @PostMapping(value = "/job/trigger/log/id")
    ResultModel triggerByLogId(@RequestParam(value = "jobLogListId") String jobLogListId);

    @PostMapping(value = "/job/app/enable/id")
    Integer enableById(@RequestParam(value = "appId") String appId);

    @PostMapping(value = "/job/app/disable/id")
    Integer disableById(@RequestParam(value = "appId") String appId);


}
