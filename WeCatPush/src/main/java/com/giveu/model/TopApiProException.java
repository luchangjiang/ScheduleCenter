package com.giveu.model;

/**
 * 系统异常
 * Created by fox on 2018/11/5.
 */
public class TopApiProException {

	private Integer proCout;

	private Integer days;

	private Integer invCount;

	private Integer excCount;

	private Float excRate;

	private String proExcMaxName;

	private Integer proExcMaxCount;

	private String proExcMinName;

	private String apiInvMaxName;

	private Integer apiInvMaxCount;

	private Integer apiInvExcMaxCount;

	private Float apiInvExcMaxRate;

	public Integer getProCout() {
		return proCout;
	}

	public void setProCout(Integer proCout) {
		this.proCout = proCout;
	}

	public Integer getDays() {
		return days;
	}

	public void setDays(Integer days) {
		this.days = days;
	}

	public Integer getInvCount() {
		return invCount;
	}

	public void setInvCount(Integer invCount) {
		this.invCount = invCount;
	}

	public Integer getExcCount() {
		return excCount;
	}

	public void setExcCount(Integer excCount) {
		this.excCount = excCount;
	}

	public Float getExcRate() {
		return excRate;
	}

	public void setExcRate(Float excRate) {
		this.excRate = excRate;
	}

	public String getProExcMaxName() {
		return proExcMaxName;
	}

	public void setProExcMaxName(String proExcMaxName) {
		this.proExcMaxName = proExcMaxName;
	}

	public Integer getProExcMaxCount() {
		return proExcMaxCount;
	}

	public void setProExcMaxCount(Integer proExcMaxCount) {
		this.proExcMaxCount = proExcMaxCount;
	}

	public String getProExcMinName() {
		return proExcMinName;
	}

	public void setProExcMinName(String proExcMinName) {
		this.proExcMinName = proExcMinName;
	}

	public String getApiInvMaxName() {
		return apiInvMaxName;
	}

	public void setApiInvMaxName(String apiInvMaxName) {
		this.apiInvMaxName = apiInvMaxName;
	}

	public Integer getApiInvMaxCount() {
		return apiInvMaxCount;
	}

	public void setApiInvMaxCount(Integer apiInvMaxCount) {
		this.apiInvMaxCount = apiInvMaxCount;
	}

	public Integer getApiInvExcMaxCount() {
		return apiInvExcMaxCount;
	}

	public void setApiInvExcMaxCount(Integer apiInvExcMaxCount) {
		this.apiInvExcMaxCount = apiInvExcMaxCount;
	}

	public Float getApiInvExcMaxRate() {
		return apiInvExcMaxRate;
	}

	public void setApiInvExcMaxRate(Float apiInvExcMaxRate) {
		this.apiInvExcMaxRate = apiInvExcMaxRate;
	}
}
