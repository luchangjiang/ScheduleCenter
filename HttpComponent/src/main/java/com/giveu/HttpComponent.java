package com.giveu;

import com.giveu.job.common.util.CommonUtil;
import com.giveu.job.common.util.DateUtil;
import com.giveu.job.common.util.PathUtil;
import jodd.http.HttpRequest;
import jodd.http.HttpResponse;

/**
 * Created by fox on 2018/8/24.
 */
public class HttpComponent {

	public static HttpRequest createPostHttpRequest(String url) {
		HttpRequest httpRequest = HttpRequest.post(url);
		setHeader(httpRequest);
		return httpRequest;
	}

	public static HttpRequest createGetHttpRequest(String url) {
		HttpRequest httpRequest = HttpRequest.get(url);
		setHeader(httpRequest);
		return httpRequest;
	}

	private static void setHeader(HttpRequest httpRequest) {
		String uuid = DateUtil.getStringDateShortYYMMDD() + CommonUtil.getUUID32();
		String systemName = PathUtil.getProjectName();

		httpRequest.header("request_id", uuid);
		httpRequest.header("system_name", systemName);
	}

	public static void main(String[] args) {
		HttpRequest httpRequest = HttpComponent.createGetHttpRequest("http://localhost:6088/foo1");
		HttpResponse response = httpRequest.send();
		System.out.println(response.bodyText());
	}

}
