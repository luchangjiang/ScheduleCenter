package com.giveu.job.common.entity;

/**
 * Created by fox on 2019/1/4.
 */
public class QrtzTriggerStat {

	private Integer id;

	private String jobId;

	private String jobName;

	private String jobCode;

	private Integer triggerCount;

	private Integer triggerCountToday;

	private String appKey;

	private String appName;

	private String statLogId;

	private Integer executeStatus;

	private String jobCreateTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

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

	public Integer getTriggerCount() {
		return triggerCount;
	}

	public void setTriggerCount(Integer triggerCount) {
		this.triggerCount = triggerCount;
	}

	public Integer getTriggerCountToday() {
		return triggerCountToday;
	}

	public void setTriggerCountToday(Integer triggerCountToday) {
		this.triggerCountToday = triggerCountToday;
	}

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getStatLogId() {
		return statLogId;
	}

	public void setStatLogId(String statLogId) {
		this.statLogId = statLogId;
	}

	public Integer getExecuteStatus() {
		return executeStatus;
	}

	public void setExecuteStatus(Integer executeStatus) {
		this.executeStatus = executeStatus;
	}

	public String getJobCreateTime() {
		return jobCreateTime;
	}

	public void setJobCreateTime(String jobCreateTime) {
		this.jobCreateTime = jobCreateTime;
	}
}
