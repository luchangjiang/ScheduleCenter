package com.giveu.job.common.dto;

/**
 * 公共HTTP header 数据传输类
 * Created by fox on 2018/6/30.
 */
public class HeaderDTO {

	private String xGiveuSign;

	private String xGiveuAppKey;

	private Long xGiveuTimestamp;

	public String getxGiveuSign() {
		return xGiveuSign;
	}

	public void setxGiveuSign(String xGiveuSign) {
		this.xGiveuSign = xGiveuSign;
	}

	public String getxGiveuAppKey() {
		return xGiveuAppKey;
	}

	public void setxGiveuAppKey(String xGiveuAppKey) {
		this.xGiveuAppKey = xGiveuAppKey;
	}

	public Long getxGiveuTimestamp() {
		return xGiveuTimestamp;
	}

	public void setxGiveuTimestamp(Long xGiveuTimestamp) {
		this.xGiveuTimestamp = xGiveuTimestamp;
	}
}
