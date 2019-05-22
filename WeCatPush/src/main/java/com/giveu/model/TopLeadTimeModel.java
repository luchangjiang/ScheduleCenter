package com.giveu.model;

/**
 * Created by fox on 2018/9/4.
 */
public class TopLeadTimeModel {

	private Integer jobCount;

	private Integer days;

	private Integer triggerCount;

	private Integer maxLeadTime;

	private Integer minLeadTime;

	private Integer moreThanSeconds;

	private Long leadTimeAvg;

	private Float moreThanAvg;

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

	public Integer getMaxLeadTime() {
		return maxLeadTime;
	}

	public void setMaxLeadTime(Integer maxLeadTime) {
		this.maxLeadTime = maxLeadTime;
	}

	public Integer getMinLeadTime() {
		return minLeadTime;
	}

	public void setMinLeadTime(Integer minLeadTime) {
		this.minLeadTime = minLeadTime;
	}

	public Integer getMoreThanSeconds() {
		return moreThanSeconds;
	}

	public void setMoreThanSeconds(Integer moreThanSeconds) {
		this.moreThanSeconds = moreThanSeconds;
	}

	public Long getLeadTimeAvg() {
		return leadTimeAvg;
	}

	public void setLeadTimeAvg(Long leadTimeAvg) {
		this.leadTimeAvg = leadTimeAvg;
	}

	public Float getMoreThanAvg() {
		return moreThanAvg;
	}

	public void setMoreThanAvg(Float moreThanAvg) {
		this.moreThanAvg = moreThanAvg;
	}
}
