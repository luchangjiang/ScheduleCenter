package com.giveu.job.common.vo;

import com.giveu.job.common.var.TriggerState;

/**
 *
 * Created by fox on 2018/7/2.
 */
public class JobTriggerAppVo {

	private String id;
	private String jobName;
	private String jobDesc;
	private String jobCode;
	private String appKey;
	private String cronExpression;
	private String jobCreateTime;
	private String callbackUrl;
	private Integer triggerCount;
	private Integer triggerFailCount;
	private String triggerState;
	private String triggerStateDesc;
	private String userAccount;

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

	public Integer getTriggerCount() {
		return triggerCount;
	}

	public void setTriggerCount(Integer triggerCount) {
		this.triggerCount = triggerCount;
	}

	public Integer getTriggerFailCount() {
		return triggerFailCount;
	}

	public void setTriggerFailCount(Integer triggerFailCount) {
		this.triggerFailCount = triggerFailCount;
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

	public String getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}
}
