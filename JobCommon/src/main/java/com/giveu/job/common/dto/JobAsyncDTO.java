package com.giveu.job.common.dto;

import java.util.Date;

/**
 * Created by fox on 2019/1/3.
 */
public class JobAsyncDTO {

	private String jobId;
	private String jobName;
	private String jobCode;
	private String appKey;
	private Date triggerTime;
	private String logId;
	private String sessionId;

	private String resultUrl;

	private Integer resultWaitTime;

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getJobCode() {
		return jobCode;
	}

	public void setJobCode(String jobCode) {
		this.jobCode = jobCode;
	}

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public Date getTriggerTime() {
		return triggerTime;
	}

	public void setTriggerTime(Date triggerTime) {
		this.triggerTime = triggerTime;
	}

	public String getLogId() {
		return logId;
	}

	public void setLogId(String logId) {
		this.logId = logId;
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

	public Integer getResultWaitTime() {
		return resultWaitTime;
	}

	public void setResultWaitTime(Integer resultWaitTime) {
		this.resultWaitTime = resultWaitTime;
	}
}
