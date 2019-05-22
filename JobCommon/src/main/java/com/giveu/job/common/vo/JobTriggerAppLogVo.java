package com.giveu.job.common.vo;

/**
 * Created by fox on 2018/7/2.
 */
public class JobTriggerAppLogVo {

	private String id;
	private String jobName;
	private String jobDesc;
	private String jobCode;
	private String appKey;
	private String triggerTime;
	private Long leadTime;
	private String callbackUrl;
	private Integer callbackCount;
	private Integer executeStatus;
	private String statusDesc;
	private String executeDesc;

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


	public Long getLeadTime() {
		return leadTime;
	}

	public void setLeadTime(Long leadTime) {
		this.leadTime = leadTime;
	}

	public String getCallbackUrl() {
		return callbackUrl;
	}

	public void setCallbackUrl(String callbackUrl) {
		this.callbackUrl = callbackUrl;
	}

	public Integer getCallbackCount() {
		return callbackCount;
	}

	public void setCallbackCount(Integer callbackCount) {
		this.callbackCount = callbackCount;
	}

	public Integer getExecuteStatus() {
		return executeStatus;
	}

	public void setExecuteStatus(Integer executeStatus) {
		this.executeStatus = executeStatus;
	}

	public String getExecuteDesc() {
		return executeDesc;
	}

	public void setExecuteDesc(String executeDesc) {
		this.executeDesc = executeDesc;
	}

	public String getTriggerTime() {
		return triggerTime;
	}

	public void setTriggerTime(String triggerTime) {
		this.triggerTime = triggerTime;
	}

	public String getStatusDesc() {
		if (this.executeStatus == 1) {
			statusDesc = "成功";
		}else {
			statusDesc = "失败";
		}
		return statusDesc;
	}

	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}
}
