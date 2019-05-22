package com.giveu.job.common.entity;

/**
 * Created by fox on 2019/1/19.
 */
public class ProInfo {

	private String proDesc;
	private String proName;

	private String proPort;

	private String proActive;

	private String proStatus;

	public String getProName() {
		return proName;
	}

	public void setProName(String proName) {
		this.proName = proName;
	}

	public String getProPort() {
		return proPort;
	}

	public void setProPort(String proPort) {
		this.proPort = proPort;
	}

	public String getProStatus() {
		return proStatus;
	}

	public void setProStatus(String proStatus) {
		this.proStatus = proStatus;
	}

	public String getProDesc() {
		return proDesc;
	}

	public void setProDesc(String proDesc) {
		this.proDesc = proDesc;
	}

	public String getProActive() {
		return proActive;
	}

	public void setProActive(String proActive) {
		this.proActive = proActive;
	}

	public ProInfo(String proDesc, String proName, String proPort, String proActive, String proStatus) {
		this.proDesc = proDesc;
		this.proName = proName;
		this.proPort = proPort;
		this.proActive = proActive;
		this.proStatus = proStatus;
	}
}
