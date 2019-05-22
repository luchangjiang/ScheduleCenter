package com.giveu.model;

import com.giveu.vo.JobWarningStatVo;

import java.util.List;

/**
 * Created by fox on 2018/9/11.
 */
public class KeyValueList {
	private String appName;

	private List<JobWarningStatVo> list;

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public List<JobWarningStatVo> getList() {
		return list;
	}

	public void setList(List<JobWarningStatVo> list) {
		this.list = list;
	}
}
