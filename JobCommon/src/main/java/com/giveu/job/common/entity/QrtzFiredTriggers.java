package com.giveu.job.common.entity;

/**
 * 
 * Created by fox on 2018/6/24.
 */
public class QrtzFiredTriggers {

	private String schedName;
	private String entryId;
	private String triggerName;
	private String triggerGroup;
	private String instanceName;
	private Long firedTime;
	private Long schedTime;
	private Integer priority;
	private String state;
	private String jobName;
	private String jobGroup;
	private String isNonconcurrent;
	private String requestsRecovery;

	public String getSchedName() {
		return schedName;
	}

	public void setSchedName(String schedName) {
		this.schedName = schedName;
	}

	public String getEntryId() {
		return entryId;
	}

	public void setEntryId(String entryId) {
		this.entryId = entryId;
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

	public String getInstanceName() {
		return instanceName;
	}

	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}

	public Long getFiredTime() {
		return firedTime;
	}

	public void setFiredTime(Long firedTime) {
		this.firedTime = firedTime;
	}

	public Long getSchedTime() {
		return schedTime;
	}

	public void setSchedTime(Long schedTime) {
		this.schedTime = schedTime;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
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

	public String getIsNonconcurrent() {
		return isNonconcurrent;
	}

	public void setIsNonconcurrent(String isNonconcurrent) {
		this.isNonconcurrent = isNonconcurrent;
	}

	public String getRequestsRecovery() {
		return requestsRecovery;
	}

	public void setRequestsRecovery(String requestsRecovery) {
		this.requestsRecovery = requestsRecovery;
	}
}
