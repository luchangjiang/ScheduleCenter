package com.giveu.job.common.dto;

import com.giveu.job.common.var.TriggerState;

/**
 * Created by fox on 2019/1/8.
 */
public class JobDetailDTO {

	private String id;
	private String jobName;
	private String jobDesc;
	private String jobCode;
	private String appKey;
	private String cronExpression;
	private String jobCreateTime;
	private String callbackUrl;
	private String triggerState;
	private String triggerStateDesc;

	private Integer jobType;
	private Integer resultWaitTime;
	private String resultUrl;

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

	public String getResultUrl() {
		return resultUrl;
	}

	public void setResultUrl(String resultUrl) {
		this.resultUrl = resultUrl;
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

	public String getJobDesc() {
		return jobDesc;
	}

	public void setJobDesc(String jobDesc) {
		this.jobDesc = jobDesc;
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

	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
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


	public String getTriggerState() {
		return triggerState;
	}

	public void setTriggerState(String triggerState) {
		this.triggerState = triggerState;
	}

	public String getTriggerStateDesc() {
		return TriggerState.triggerStateMap.get(triggerState);
	}

	public void setTriggerStateDesc(String triggerStateDesc) {
		this.triggerStateDesc = triggerStateDesc;
	}

}
