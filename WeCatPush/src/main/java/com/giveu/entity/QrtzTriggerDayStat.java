package com.giveu.entity;

import java.util.Date;

/**
 * Created by fox on 2018/9/25.
 */
public class QrtzTriggerDayStat {

	private String jobId;
	private String jobName;
	private String jobCode;
	private String jobCreateTime;
	private String appKey;
	private String appName;
	private Long triggerCount;
	private Long triggerFailCount;
	private Float triggerFailRate;
	private Long maxLeadTime;
	private Long minLeadTime;
	private Long leadTimeSum;
	private Float leadTimeAvg;
	private Date triggerDate;

	public Long getLeadTimeSum() {
		return leadTimeSum;
	}

	public void setLeadTimeSum(Long leadTimeSum) {
		this.leadTimeSum = leadTimeSum;
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

	public String getJobCreateTime() {
		return jobCreateTime;
	}

	public void setJobCreateTime(String jobCreateTime) {
		this.jobCreateTime = jobCreateTime;
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

	public Long getTriggerCount() {
		return triggerCount;
	}

	public void setTriggerCount(Long triggerCount) {
		this.triggerCount = triggerCount;
	}

	public Long getTriggerFailCount() {
		return triggerFailCount;
	}

	public void setTriggerFailCount(Long triggerFailCount) {
		this.triggerFailCount = triggerFailCount;
	}

	public Float getTriggerFailRate() {
		if (triggerCount == 0 || triggerFailCount == null) {
			return 0F;
		}
		return Float.valueOf(triggerFailCount) / triggerCount;
	}

	public void setTriggerFailRate(Float triggerFailRate) {
		this.triggerFailRate = triggerFailRate;
	}

	public Float getLeadTimeAvg() {
		if (triggerCount == 0 || leadTimeSum == null) {
			return 0F;
		}
		return Float.valueOf(leadTimeSum) / triggerCount;
	}

	public void setLeadTimeAvg(Float leadTimeAvg) {
		this.leadTimeAvg = leadTimeAvg;
	}

	public String getJobCode() {
		return jobCode;
	}

	public void setJobCode(String jobCode) {
		this.jobCode = jobCode;
	}

	public Long getMaxLeadTime() {
		return maxLeadTime;
	}

	public void setMaxLeadTime(Long maxLeadTime) {
		this.maxLeadTime = maxLeadTime;
	}

	public Long getMinLeadTime() {
		return minLeadTime;
	}

	public void setMinLeadTime(Long minLeadTime) {
		this.minLeadTime = minLeadTime;
	}

	public Date getTriggerDate() {
		return triggerDate;
	}

	public void setTriggerDate(Date triggerDate) {
		this.triggerDate = triggerDate;
	}


}
