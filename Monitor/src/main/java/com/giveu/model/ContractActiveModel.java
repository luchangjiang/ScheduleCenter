package com.giveu.model;

import java.util.List;

/**
 * 合同现行模型
 * Created by fox on 2018/7/20.
 */
public class ContractActiveModel {

	private String code;

	private String messageTemplate;

	private List<ItemModel> items;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessageTemplate() {
		return messageTemplate;
	}

	public void setMessageTemplate(String messageTemplate) {
		this.messageTemplate = messageTemplate;
	}

	public List<ItemModel> getItems() {
		return items;
	}

	public void setItems(List<ItemModel> items) {
		this.items = items;
	}
}
