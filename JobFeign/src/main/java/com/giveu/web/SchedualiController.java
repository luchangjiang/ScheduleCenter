package com.giveu.web;

import com.giveu.job.common.vo.AppVo;
import com.giveu.job.common.vo.ResultModel;
import com.giveu.service.SchedualService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/job")
public class SchedualiController {

    @Autowired
    private SchedualService schedualService;

    @RequestMapping(value = "/resumejob",method = RequestMethod.GET)
    public String resumejob(@RequestParam(value = "jobClassName") String jobClassName, @RequestParam(value = "jobGroupName") String jobGroupName) throws Exception {
        return schedualService.resumejob(jobClassName,jobGroupName);
    }

    @RequestMapping(value = "/pausejob",method = RequestMethod.GET)
    public String pausejob(@RequestParam(value = "jobClassName") String jobClassName, @RequestParam(value = "jobGroupName") String jobGroupName) throws Exception{
        return schedualService.pausejob(jobClassName,jobGroupName);
    }

    @RequestMapping(value = "/resumerunjob",method = RequestMethod.GET)
    public String resumerunjob(){
        return schedualService.resumerunjob();
    }

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
    public ResultModel jobList(@RequestParam(value = "id") String id, @RequestParam(value = "appKey") String appKey, @RequestParam(value = "jobCode") String jobCode) throws Exception {
        return schedualService.jobList(id, jobCode, appKey);
    }

    @RequestMapping(value = "/log/list", method = RequestMethod.POST)
    @ResponseBody
    public ResultModel logList(HttpServletRequest request) throws Exception {
        String beginTime = request.getParameter("beginTime");
        String endTime = request.getParameter("endTime");
        String jobId = request.getParameter("jobId");
        String jobCode = request.getParameter("jobCode");
        String executeStatus = request.getParameter("executeStatus");

        return schedualService.logList(beginTime, endTime, jobId, jobCode, executeStatus);
    }

    @RequestMapping(value = "/app/list", method = RequestMethod.POST)
    @ResponseBody
    public ResultModel appList(@RequestParam(value = "appName") String appName, @RequestParam(value = "appKey") String appKey, @RequestParam(value = "appStatus") String appStatus) throws Exception {
        return schedualService.appList(appName, appKey, appStatus);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public ResultModel addJob(@RequestParam(value = "jobName") String jobName, @RequestParam(value = "jobCode") String jobCode, @RequestParam(value = "jobDesc") String jobDesc, @RequestParam(value = "callbackUrl") String callbackUrl, @RequestParam(value = "cronExpression") String cronExpression,
                              @RequestHeader(value = "xGiveuSign") String xGiveuSign, @RequestHeader(value = "xGiveuAppKey") String xGiveuAppKey, @RequestHeader(value = "xGiveuTimestamp") String xGiveuTimestamp) throws Exception {
        return schedualService.addJob(jobName, jobCode, jobDesc, callbackUrl, cronExpression, xGiveuSign, xGiveuAppKey, xGiveuTimestamp);
    }

    @RequestMapping(value = "/del/code", method = RequestMethod.POST)
    @ResponseBody
    public ResultModel delJobByCode(@RequestParam(value = "jobCode") String jobCode, @RequestHeader(value = "xGiveuSign") String xGiveuSign, @RequestHeader(value = "xGiveuAppKey") String xGiveuAppKey, @RequestHeader(value = "xGiveuTimestamp") String xGiveuTimestamp) throws Exception {
        return schedualService.delJobByCode(jobCode,xGiveuSign, xGiveuAppKey, xGiveuTimestamp);
    }

    @RequestMapping(value = "/app/add", method = RequestMethod.POST)
    @ResponseBody
    public ResultModel addApp(@RequestParam(value = "appName") String appName, @RequestParam(value = "appStatus") String appStatus, @RequestParam(value = "appKey") String appKey, @RequestParam(value = "appSecret") String appSecret) throws Exception {
        return schedualService.addApp(appName,appStatus, appKey, appSecret);
    }

    @RequestMapping(value = "/app/get/id", method = RequestMethod.POST)
    @ResponseBody
    public AppVo getAppVoById(@RequestParam(value = "appId") String appId) throws Exception {
        return schedualService.getAppVoById(appId);
    }

    @RequestMapping(value = "/app/upd/id", method = RequestMethod.POST)
    @ResponseBody
    public ResultModel updAppById(@RequestParam(value = "id") String id, @RequestParam(value = "appName") String appName, @RequestParam(value = "appStatus") String appStatus, @RequestParam(value = "appKey") String appKey, @RequestParam(value = "appSecret") String appSecret) throws Exception {
        return schedualService.updAppById(id, appName,appStatus, appKey, appSecret);
    }

    @RequestMapping(value = "/app/del/id", method = RequestMethod.POST)
    @ResponseBody
    public Integer delAppById(@RequestParam(value = "appId") String appId) throws Exception {
        return schedualService.delAppById(appId);
    }

    @RequestMapping(value = "/trigger/log/id", method = RequestMethod.POST)
    @ResponseBody
    public ResultModel triggerByLogId(@RequestParam(value = "jobLogListId") String jobLogListId) throws Exception {
        return schedualService.triggerByLogId(jobLogListId);
    }

    @RequestMapping(value = "/app/enable/id", method = RequestMethod.POST)
    @ResponseBody
    public Integer enableById(@RequestParam(value = "appId") String appId) throws Exception {
        return schedualService.enableById(appId);
    }

    @RequestMapping(value = "/app/disable/id", method = RequestMethod.POST)
    @ResponseBody
    public Integer disableById(@RequestParam(value = "appId") String appId) throws Exception {
        return schedualService.disableById(appId);
    }


}
