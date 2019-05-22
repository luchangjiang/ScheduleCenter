package com.giveu.job.common.vo;

import java.io.Serializable;

/**
 * Created by fox on 2018/7/2.
 */
public class AppVo implements Serializable{

	private static final long serialVersionUID = -5414391138065567138L;

	private String id;
	private String appName;
	private String appDesc;
	private Integer appStatus;
	private String appStatusDesc;
	private String appCreateTime;
	private String appUpdateTime;
	private String appAuthor;
	private String appEmail;
	private String appPhone;
	private String appKey;
	private String appSecret;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getAppDesc() {
		return appDesc;
	}

	public void setAppDesc(String appDesc) {
		this.appDesc = appDesc;
	}

	public Integer getAppStatus() {
		return appStatus;
	}

	public void setAppStatus(Integer appStatus) {
		this.appStatus = appStatus;
	}

	public String getAppStatusDesc() {
		if (this.appStatus == 1) {
			appStatusDesc = "启用";
		}else {
			appStatusDesc = "禁用";
		}
		return appStatusDesc;
	}

	public void setAppStatusDesc(String appStatusDesc) {
		this.appStatusDesc = appStatusDesc;
	}

	public String getAppCreateTime() {
		return appCreateTime;
	}

	public void setAppCreateTime(String appCreateTime) {
		this.appCreateTime = appCreateTime;
	}

	public String getAppUpdateTime() {
		return appUpdateTime;
	}

	public void setAppUpdateTime(String appUpdateTime) {
		this.appUpdateTime = appUpdateTime;
	}

	public String getAppAuthor() {
		return appAuthor;
	}

	public void setAppAuthor(String appAuthor) {
		this.appAuthor = appAuthor;
	}

	public String getAppEmail() {
		return appEmail;
	}

	public void setAppEmail(String appEmail) {
		this.appEmail = appEmail;
	}

	public String getAppPhone() {
		return appPhone;
	}

	public void setAppPhone(String appPhone) {
		this.appPhone = appPhone;
	}

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public String getAppSecret() {
		return appSecret;
	}

	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}
}
