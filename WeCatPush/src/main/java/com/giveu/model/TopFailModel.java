package com.giveu.model;

/**
 * Created by fox on 2018/9/4.
 */
public class TopFailModel {

	private Integer jobCount;

	private Integer days;

	private Integer triggerCount;

	private Integer jobFailCount;

	private Integer triggerFailCount;

	private String triggerFailMaxName;

	private Integer triggerFailMaxCount;

	private Float jobFailPct;

	private Float triggerFailPct;

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

	public Integer getJobFailCount() {
		return jobFailCount;
	}

	public void setJobFailCount(Integer jobFailCount) {
		this.jobFailCount = jobFailCount;
	}

	public Integer getTriggerFailCount() {
		return triggerFailCount;
	}

	public void setTriggerFailCount(Integer triggerFailCount) {
		this.triggerFailCount = triggerFailCount;
	}

	public String getTriggerFailMaxName() {
		return triggerFailMaxName;
	}

	public void setTriggerFailMaxName(String triggerFailMaxName) {
		this.triggerFailMaxName = triggerFailMaxName;
	}

	public Integer getTriggerFailMaxCount() {
		return triggerFailMaxCount;
	}

	public void setTriggerFailMaxCount(Integer triggerFailMaxCount) {
		this.triggerFailMaxCount = triggerFailMaxCount;
	}

	public Float getJobFailPct() {
		return ((Float.valueOf(jobFailCount)) / jobCount) * 100;
	}

	public void setJobFailPct(Float jobFailPct) {
		this.jobFailPct = jobFailPct;
	}

	public Float getTriggerFailPct() {
		return (Float.valueOf(triggerFailCount)) / triggerCount;
	}

	public void setTriggerFailPct(Float triggerFailPct) {
		this.triggerFailPct = triggerFailPct;
	}
}
