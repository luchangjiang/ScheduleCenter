package com.giveu.entity;

/**
 * Created by fox on 2018/9/4.
 */
public class QrtzTriggerTopFrequency {

	private String jobId;

	private String jobName;

	private Integer triggerCount;

	private Long leadTimeSum;

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

	public Integer getTriggerCount() {
		return triggerCount;
	}

	public void setTriggerCount(Integer triggerCount) {
		this.triggerCount = triggerCount;
	}

	public Long getLeadTimeSum() {
		return leadTimeSum;
	}

	public void setLeadTimeSum(Long leadTimeSum) {
		this.leadTimeSum = leadTimeSum;
	}
}
