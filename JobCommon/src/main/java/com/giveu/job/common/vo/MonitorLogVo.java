package com.giveu.job.common.vo;

/**
 * Created by fox on 2018/7/23.
 */
public class MonitorLogVo {

	private String id;

	private String objId;
	private String objName;
	private String objCode;
	private String objDesc;
	private String logCreateTime;
	private String objPolicySettings;
	private String objReceiverSettings;
	private String logContext;
	private String logExtend;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getObjId() {
		return objId;
	}

	public void setObjId(String objId) {
		this.objId = objId;
	}

	public String getObjName() {
		return objName;
	}

	public void setObjName(String objName) {
		this.objName = objName;
	}

	public String getObjCode() {
		return objCode;
	}

	public void setObjCode(String objCode) {
		this.objCode = objCode;
	}

	public String getObjDesc() {
		return objDesc;
	}

	public void setObjDesc(String objDesc) {
		this.objDesc = objDesc;
	}

	public String getLogCreateTime() {
		return logCreateTime;
	}

	public void setLogCreateTime(String logCreateTime) {
		this.logCreateTime = logCreateTime;
	}

	public String getObjPolicySettings() {
		return objPolicySettings;
	}

	public void setObjPolicySettings(String objPolicySettings) {
		this.objPolicySettings = objPolicySettings;
	}

	public String getObjReceiverSettings() {
		return objReceiverSettings;
	}

	public void setObjReceiverSettings(String objReceiverSettings) {
		this.objReceiverSettings = objReceiverSettings;
	}

	public String getLogContext() {
		return logContext;
	}

	public void setLogContext(String logContext) {
		this.logContext = logContext;
	}

	public String getLogExtend() {
		return logExtend;
	}

	public void setLogExtend(String logExtend) {
		this.logExtend = logExtend;
	}
}
