package com.giveu.log.scan.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fox on 2018/8/21.
 */
public class MonitorLog {

	@JsonIgnore
	private Long startTime;
	@JsonIgnore
	private Long startBusTime;

	private String _id;
	private String statuCode = "";
	private String currentUser = "";
	private String requestId = "";
	private String scheme = "";
	private String httpMethod = "";
	private String remoteIpAddress = "";
	private String localIpAddress = "";
	private Integer remotePort = 0;
	private Integer localPort = 0;
	private String path = "";
	private String requestTime = "";
	private String requestHeader = "";
	private String queryString = "";
	private String requestBody = "";
	private String responeTime = "";
	private String responeHeader = "";
	private String responseBody = "";
	private Long businessMilliseconds = 0L;
	private Long totalMilliseconds = 0L;
	private List Steps = new ArrayList();
	private String localSystemName = "";
	private String sourceSystemName = "";
	private String exceptionMessage = "";
	private String exceptionTraceStack = "";

	public List getSteps() {
		return Steps;
	}

	public void setSteps(List steps) {
		Steps = steps;
	}

	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	public Long getStartBusTime() {
		return startBusTime;
	}

	public void setStartBusTime(Long startBusTime) {
		this.startBusTime = startBusTime;
	}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getStatuCode() {
		return statuCode;
	}

	public void setStatuCode(String statuCode) {
		this.statuCode = statuCode;
	}

	public String getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(String currentUser) {
		this.currentUser = currentUser;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getScheme() {
		return scheme;
	}

	public void setScheme(String scheme) {
		this.scheme = scheme;
	}

	public String getHttpMethod() {
		return httpMethod;
	}

	public void setHttpMethod(String httpMethod) {
		this.httpMethod = httpMethod;
	}

	public String getRemoteIpAddress() {
		return remoteIpAddress;
	}

	public void setRemoteIpAddress(String remoteIpAddress) {
		this.remoteIpAddress = remoteIpAddress;
	}

	public String getLocalIpAddress() {
		return localIpAddress;
	}

	public void setLocalIpAddress(String localIpAddress) {
		this.localIpAddress = localIpAddress;
	}

	public Integer getRemotePort() {
		return remotePort;
	}

	public void setRemotePort(Integer remotePort) {
		this.remotePort = remotePort;
	}

	public Integer getLocalPort() {
		return localPort;
	}

	public void setLocalPort(Integer localPort) {
		this.localPort = localPort;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(String requestTime) {
		this.requestTime = requestTime;
	}

	public String getRequestHeader() {
		return requestHeader;
	}

	public void setRequestHeader(String requestHeader) {
		this.requestHeader = requestHeader;
	}

	public String getQueryString() {
		return queryString;
	}

	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	public String getRequestBody() {
		return requestBody;
	}

	public void setRequestBody(String requestBody) {
		this.requestBody = requestBody;
	}

	public String getResponeTime() {
		return responeTime;
	}

	public void setResponeTime(String responeTime) {
		this.responeTime = responeTime;
	}

	public String getResponeHeader() {
		return responeHeader;
	}

	public void setResponeHeader(String responeHeader) {
		this.responeHeader = responeHeader;
	}

	public String getResponseBody() {
		return responseBody;
	}

	public void setResponseBody(String responseBody) {
		this.responseBody = responseBody;
	}

	public Long getBusinessMilliseconds() {
		return businessMilliseconds;
	}

	public void setBusinessMilliseconds(Long businessMilliseconds) {
		this.businessMilliseconds = businessMilliseconds;
	}

	public Long getTotalMilliseconds() {
		return totalMilliseconds;
	}

	public void setTotalMilliseconds(Long totalMilliseconds) {
		this.totalMilliseconds = totalMilliseconds;
	}

	public String getLocalSystemName() {
		return localSystemName;
	}

	public void setLocalSystemName(String localSystemName) {
		this.localSystemName = localSystemName;
	}

	public String getSourceSystemName() {
		return sourceSystemName;
	}

	public void setSourceSystemName(String sourceSystemName) {
		this.sourceSystemName = sourceSystemName;
	}

	public String getExceptionMessage() {
		return exceptionMessage;
	}

	public void setExceptionMessage(String exceptionMessage) {
		this.exceptionMessage = exceptionMessage;
	}

	public String getExceptionTraceStack() {
		return exceptionTraceStack;
	}

	public void setExceptionTraceStack(String exceptionTraceStack) {
		this.exceptionTraceStack = exceptionTraceStack;
	}

	@Override
	public String toString() {
		return "MonitorLog{" +
				"startTime=" + startTime +
				", startBusTime=" + startBusTime +
				", _id='" + _id + '\'' +
				", statuCode='" + statuCode + '\'' +
				", currentUser='" + currentUser + '\'' +
				", requestId='" + requestId + '\'' +
				", scheme='" + scheme + '\'' +
				", httpMethod='" + httpMethod + '\'' +
				", remoteIpAddress='" + remoteIpAddress + '\'' +
				", localIpAddress='" + localIpAddress + '\'' +
				", remotePort=" + remotePort +
				", localPort=" + localPort +
				", path='" + path + '\'' +
				", requestTime='" + requestTime + '\'' +
				", requestHeader='" + requestHeader + '\'' +
				", queryString='" + queryString + '\'' +
				", requestBody='" + requestBody + '\'' +
				", responeTime='" + responeTime + '\'' +
				", responeHeader='" + responeHeader + '\'' +
				", responseBody='" + responseBody + '\'' +
				", businessMilliseconds=" + businessMilliseconds +
				", totalMilliseconds=" + totalMilliseconds +
				", Steps=" + Steps +
				", localSystemName='" + localSystemName + '\'' +
				", sourceSystemName='" + sourceSystemName + '\'' +
				", exceptionMessage='" + exceptionMessage + '\'' +
				", exceptionTraceStack='" + exceptionTraceStack + '\'' +
				'}';
	}
}
