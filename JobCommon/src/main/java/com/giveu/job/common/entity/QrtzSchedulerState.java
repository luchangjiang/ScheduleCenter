package com.giveu.job.common.entity;

import com.giveu.job.common.util.DateUtil;

/**
 * Created by fox on 2019/1/17.
 */
public class QrtzSchedulerState {

	// 调度器名称
	private String schedName;
	// 实例名称
	private String instanceName;
	// 最后检查时间
	private String lastCheckinTime;

	private String lastCheckinTimeDesc;
	// 检查频率
	private String checkinInterval;

	// 实例启动时间
	private String instanceTime;

	// 实例IP地址
	private String instanceIp;

	public String getSchedName() {
		return schedName;
	}

	public void setSchedName(String schedName) {
		this.schedName = schedName;
	}

	public String getInstanceName() {
		return instanceName;
	}

	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}

	public String getLastCheckinTime() {
		return lastCheckinTime;
	}

	public void setLastCheckinTime(String lastCheckinTime) {
		this.lastCheckinTime = lastCheckinTime;
	}

	public String getCheckinInterval() {
		return checkinInterval;
	}

	public void setCheckinInterval(String checkinInterval) {
		this.checkinInterval = checkinInterval;
	}

	public String getInstanceTime() {
		return instanceTime;
	}

	public void setInstanceTime(String instanceTime) {
		this.instanceTime = instanceTime;
	}

	public String getInstanceIp() {
		return instanceIp;
	}

	public void setInstanceIp(String instanceIp) {
		this.instanceIp = instanceIp;
	}

	public String getLastCheckinTimeDesc() {
		return DateUtil.stampToDateTime(lastCheckinTime);
	}

	public void setLastCheckinTimeDesc(String lastCheckinTimeDesc) {
		this.lastCheckinTimeDesc = lastCheckinTimeDesc;
	}
}
