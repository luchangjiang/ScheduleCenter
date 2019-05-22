package com.giveu.job.common.vo;

import java.io.Serializable;

/**
 * 数据响应模型
 * Created by fox on 2018/6/30.
 */
public class ResultModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer code;
	private String message;
	private Object data;

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public ResultModel() {
	}

	public ResultModel(Integer code, String message, Object data) {
		this.code = code;
		this.message = message;
		this.data = data;
	}

	@Override
	public String toString() {
		return "ResultModel{" +
				"code=" + code +
				", message='" + message + '\'' +
				", data=" + data +
				'}';
	}
}
