package com.giveu.vo;

/**
 * Created by fox on 2019/1/4.
 */
public class JobParamVo {

	private String jobName;

	private String jobCode;

	private String jobDesc;

	private Integer jobType;

	private String cronExpression;

	private String appKey;

	private String callbackUrl;

	private String resultUrl;

	private Integer resultWaitTime;

	private Integer isPause;

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

	public String getJobDesc() {
		return jobDesc;
	}

	public void setJobDesc(String jobDesc) {
		this.jobDesc = jobDesc;
	}

	public Integer getJobType() {
		return jobType;
	}

	public void setJobType(Integer jobType) {
		this.jobType = jobType;
	}

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public String getCallbackUrl() {
		return callbackUrl;
	}

	public void setCallbackUrl(String callbackUrl) {
		this.callbackUrl = callbackUrl;
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

	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	public Integer getIsPause() {
		return isPause;
	}

	public void setIsPause(Integer isPause) {
		this.isPause = isPause;
	}
}
