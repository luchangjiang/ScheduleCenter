package com.giveu.vo;

/**
 * Created by fox on 2018/9/4.
 */
public class JobWarningStatVo {

	private String jobId;

	private String jobName;

	private String jobCreateTime;

	private Integer successCount;

	private Integer failCount;

	private Integer todaySuccessCount;

	private Integer todayFailCount;

	private Double failureRate;

	private Double todayFailureRate;

	private String appKey;

	private String appName;

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

	public String getJobCreateTime() {
		return jobCreateTime;
	}

	public void setJobCreateTime(String jobCreateTime) {
		this.jobCreateTime = jobCreateTime;
	}

	public Integer getSuccessCount() {
		return successCount;
	}

	public void setSuccessCount(Integer successCount) {
		this.successCount = successCount;
	}

	public Integer getFailCount() {
		return failCount;
	}

	public void setFailCount(Integer failCount) {
		this.failCount = failCount;
	}

	public Integer getTodaySuccessCount() {
		return todaySuccessCount;
	}

	public void setTodaySuccessCount(Integer todaySuccessCount) {
		this.todaySuccessCount = todaySuccessCount;
	}

	public Integer getTodayFailCount() {
		return todayFailCount;
	}

	public void setTodayFailCount(Integer todayFailCount) {
		this.todayFailCount = todayFailCount;
	}

	public Double getFailureRate() {
		if ((failCount + successCount) == 0) {
			return 0.0;
		}
		return Double.valueOf(failCount / (failCount + successCount));
	}

	public void setFailureRate(Double failureRate) {
		this.failureRate = failureRate;
	}

	public Double getTodayFailureRate() {
		int count = todayFailCount + todaySuccessCount;
		if (count == 0) {
			return 0.0;
		}
		return Double.valueOf(todayFailCount / count);
	}

	public void setTodayFailureRate(Double todayFailureRate) {
		this.todayFailureRate = todayFailureRate;
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
}
