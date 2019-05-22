package com.giveu.job.common.vo;

/**
 * 应用用户关联
 * Created by fox on 2018/10/9.
 */
public class AppUserVo {

	private Integer id;

	private String userAccount;

	private String appKey;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}
}
