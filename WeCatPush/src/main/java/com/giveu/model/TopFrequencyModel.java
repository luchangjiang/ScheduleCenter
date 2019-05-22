package com.giveu.model;

/**
 * Created by fox on 2018/9/4.
 */
public class TopFrequencyModel {

	private Integer jobCount;

	private Integer days;

	private Integer triggerCount;

	private String jobMaxName;

	private String jobMinName;

	private Integer jobMaxCount;

	private Integer jobMinCount;

	private Float triggerAvg;

	public Integer getJobCount() {
		return jobCount;
	}

	public void setJobCount(Integer jobCount) {
		this.jobCount = jobCount;
	}

	public Integer getDays() {
		return days;
	}

	public void setDays(Integer days) {
		this.days = days;
	}

	public Integer getTriggerCount() {
		return triggerCount;
	}

	public void setTriggerCount(Integer triggerCount) {
		this.triggerCount = triggerCount;
	}

	public String getJobMaxName() {
		return jobMaxName;
	}

	public void setJobMaxName(String jobMaxName) {
		this.jobMaxName = jobMaxName;
	}

	public String getJobMinName() {
		return jobMinName;
	}

	public void setJobMinName(String jobMinName) {
		this.jobMinName = jobMinName;
	}

	public Integer getJobMaxCount() {
		return jobMaxCount;
	}

	public void setJobMaxCount(Integer jobMaxCount) {
		this.jobMaxCount = jobMaxCount;
	}

	public Integer getJobMinCount() {
		return jobMinCount;
	}

	public void setJobMinCount(Integer jobMinCount) {
		this.jobMinCount = jobMinCount;
	}

	public Float getTriggerAvg() {
		return Float.valueOf(triggerCount / jobCount);
	}

	public void setTriggerAvg(Float triggerAvg) {
		this.triggerAvg = triggerAvg;
	}
}
