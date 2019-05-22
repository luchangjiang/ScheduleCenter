package com.giveu.entity;

import java.util.Date;

/**
 * 监控对象实体类
 * Created by fox on 2018/7/18.
 */
public class MonitorObject {

	private String id;

	private String objName;

	private String objDesc;

	private String objCode;

	private Integer objStatus;

	private Date objCreateTime;

	private String objPolicySettings;

	private String objReceiverSettings;

	private Integer objShowOrder;

	private String objExtend;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getObjName() {
		return objName;
	}

	public void setObjName(String objName) {
		this.objName = objName;
	}

	public String getObjDesc() {
		return objDesc;
	}

	public void setObjDesc(String objDesc) {
		this.objDesc = objDesc;
	}

	public String getObjCode() {
		return objCode;
	}

	public void setObjCode(String objCode) {
		this.objCode = objCode;
	}

	public Integer getObjStatus() {
		return objStatus;
	}

	public void setObjStatus(Integer objStatus) {
		this.objStatus = objStatus;
	}

	public Date getObjCreateTime() {
		return objCreateTime;
	}

	public void setObjCreateTime(Date objCreateTime) {
		this.objCreateTime = objCreateTime;
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

	public Integer getObjShowOrder() {
		return objShowOrder;
	}

	public void setObjShowOrder(Integer objShowOrder) {
		this.objShowOrder = objShowOrder;
	}

	public String getObjExtend() {
		return objExtend;
	}

	public void setObjExtend(String objExtend) {
		this.objExtend = objExtend;
	}
}
