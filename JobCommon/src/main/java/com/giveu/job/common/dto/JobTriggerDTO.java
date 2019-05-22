package com.giveu.job.common.dto;

/**
 * JobTrigger 数据传输对象
 * Created by fox on 2018/6/29.
 */
public class JobTriggerDTO {

	private String jobName;

	private String jobGroup = "GiveuGroup";

	private String jobCode;

	private String jobDesc;

	private String jobClassName;

	private String jobCreateTime;

	private String callbackUrl;

	private String appKey = "GiveuTestKey";

	private String cronExpression;

	private String resultUrl;

	private Integer jobType;

	private Integer resultWaitTime;

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

	public String getJobClassName() {
		return jobClassName;
	}

	public void setJobClassName(String jobClassName) {
		this.jobClassName = jobClassName;
	}

	public String getJobCreateTime() {
		return jobCreateTime;
	}

	public void setJobCreateTime(String jobCreateTime) {
		this.jobCreateTime = jobCreateTime;
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

	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
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
}
