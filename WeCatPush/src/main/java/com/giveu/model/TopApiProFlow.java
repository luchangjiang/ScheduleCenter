package com.giveu.model;

/**
 * 系统流量
 * Created by fox on 2018/11/5.
 */
public class TopApiProFlow {

	private Integer proCout;

	private Integer apiCount;

	private Integer days;

	private Integer invCount;

	private String apiMaxName;

	private Integer apiMaxCount;

	private String apiInvMaxName;

	private Integer apiInvMaxCount;

	private String apiMinName;

	private Integer apiMinCount;

	private String apiInvMinName;

	private Integer apiInvMinCount;


	public String getApiInvMaxName() {
		return apiInvMaxName;
	}

	public void setApiInvMaxName(String apiInvMaxName) {
		this.apiInvMaxName = apiInvMaxName;
	}

	public String getApiInvMinName() {
		return apiInvMinName;
	}

	public void setApiInvMinName(String apiInvMinName) {
		this.apiInvMinName = apiInvMinName;
	}

	public Integer getProCout() {
		return proCout;
	}

	public void setProCout(Integer proCout) {
		this.proCout = proCout;
	}

	public Integer getApiCount() {
		return apiCount;
	}

	public void setApiCount(Integer apiCount) {
		this.apiCount = apiCount;
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

	public String getApiMaxName() {
		return apiMaxName;
	}

	public void setApiMaxName(String apiMaxName) {
		this.apiMaxName = apiMaxName;
	}

	public Integer getApiMaxCount() {
		return apiMaxCount;
	}

	public void setApiMaxCount(Integer apiMaxCount) {
		this.apiMaxCount = apiMaxCount;
	}

	public Integer getApiInvMaxCount() {
		return apiInvMaxCount;
	}

	public void setApiInvMaxCount(Integer apiInvMaxCount) {
		this.apiInvMaxCount = apiInvMaxCount;
	}

	public String getApiMinName() {
		return apiMinName;
	}

	public void setApiMinName(String apiMinName) {
		this.apiMinName = apiMinName;
	}

	public Integer getApiMinCount() {
		return apiMinCount;
	}

	public void setApiMinCount(Integer apiMinCount) {
		this.apiMinCount = apiMinCount;
	}

	public Integer getApiInvMinCount() {
		return apiInvMinCount;
	}

	public void setApiInvMinCount(Integer apiInvMinCount) {
		this.apiInvMinCount = apiInvMinCount;
	}
}
