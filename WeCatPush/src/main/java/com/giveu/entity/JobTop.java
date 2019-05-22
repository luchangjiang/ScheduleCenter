package com.giveu.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by fox on 2018/9/29.
 */
@JsonIgnoreProperties(value="content", allowGetters = true, allowSetters = false)
public class JobTop {

	private String top;

	private String content;

	public String getTop() {
		return top;
	}

	public void setTop(String top) {
		this.top = top;
	}

	public String getContent() {
		return content;
	}

}
