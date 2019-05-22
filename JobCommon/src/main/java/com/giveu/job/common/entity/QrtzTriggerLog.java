package com.giveu.job.common.entity;

import com.giveu.job.common.util.DateUtil;

import java.util.Date;

/**
 * 触发日志
 * Created by fox on 2018/6/27.
 */
public class QrtzTriggerLog {

	private String id;
	private String jobId;
	private String jobName;
	private String jobCode;
	private String jobDesc;
	private String jobGroup;
	private String triggerName;
	private String triggerGroup;
	private String triggerAddress;
	private Date triggerTime;
	private String triggerTimeDesc;
	private Long leadTime;
	private String callbackUrl;
	private Integer callbackCount;
	private String executeDesc;
	private String appKey;
	private Integer executeStatus;
	private String executeStatusDesc;
	private String sessionId;

	private String resultUrl;

	private Integer jobType;

	private Integer resultWaitTime;

	private Date resultUpdateTime;



	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public String getJobCode() {
		return jobCode;
	}

	public void setJobCode(String jobCode) {
		this.jobCode = jobCode;
	}

	public String getJobDesc() {
		return jobDesc;
	}

	public void setJobDesc(String jobDesc) {
		this.jobDesc = jobDesc;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getJobGroup() {
		return jobGroup;
	}

	public void setJobGroup(String jobGroup) {
		this.jobGroup = jobGroup;
	}

	public String getTriggerName() {
		return triggerName;
	}

	public void setTriggerName(String triggerName) {
		this.triggerName = triggerName;
	}

	public String getTriggerGroup() {
		return triggerGroup;
	}

	public void setTriggerGroup(String triggerGroup) {
		this.triggerGroup = triggerGroup;
	}

	public String getTriggerAddress() {
		return triggerAddress;
	}

	public void setTriggerAddress(String triggerAddress) {
		this.triggerAddress = triggerAddress;
	}

	public Date getTriggerTime() {
		return triggerTime;
	}

	public void setTriggerTime(Date triggerTime) {
		this.triggerTime = triggerTime;
	}

	public Long getLeadTime() {
		return leadTime;
	}

	public void setLeadTime(Long leadTime) {
		this.leadTime = leadTime;
	}

	public Integer getCallbackCount() {
		return callbackCount;
	}

	public void setCallbackCount(Integer callbackCount) {
		this.callbackCount = callbackCount;
	}

	public String getExecuteDesc() {
		return executeDesc;
	}

	public void setExecuteDesc(String executeDesc) {
		this.executeDesc = executeDesc;
	}

	public String getCallbackUrl() {
		return callbackUrl;
	}

	public void setCallbackUrl(String callbackUrl) {
		this.callbackUrl = callbackUrl;
	}

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public Integer getExecuteStatus() {
		return executeStatus;
	}

	public void setExecuteStatus(Integer executeStatus) {
		this.executeStatus = executeStatus;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getResultUrl() {
		return resultUrl;
	}

	public void setResultUrl(String resultUrl) {
		this.resultUrl = resultUrl;
	}

	public Integer getJobType() {
		return jobType;
	}

	public void setJobType(Integer jobType) {
		this.jobType = jobType;
	}

	public Integer getResultWaitTime() {
		return resultWaitTime;
	}

	public void setResultWaitTime(Integer resultWaitTime) {
		this.resultWaitTime = resultWaitTime;
	}

	public Date getResultUpdateTime() {
		return resultUpdateTime;
	}

	public void setResultUpdateTime(Date resultUpdateTime) {
		this.resultUpdateTime = resultUpdateTime;
	}

	public String getTriggerTimeDesc() {
		return DateUtil.dateToStrLong(triggerTime);
	}

	public void setTriggerTimeDesc(String triggerTimeDesc) {
		this.triggerTimeDesc = triggerTimeDesc;
	}

	public String getExecuteStatusDesc() {
		if (this.executeStatus == 1) {
			executeStatusDesc = "成功";
		}else {
			executeStatusDesc = "失败";
		}
		return executeStatusDesc;
	}

	public void setExecuteStatusDesc(String executeStatusDesc) {
		this.executeStatusDesc = executeStatusDesc;
	}
}
