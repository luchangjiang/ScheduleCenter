package com.giveu;

import com.giveu.job.common.util.CommonUtil;
import com.giveu.job.common.util.MD5Util;
import jodd.http.HttpMultiMap;
import jodd.http.HttpRequest;
import jodd.http.HttpResponse;

import java.util.Date;

/**
 * Created by fox on 2018/7/11.
 */
public class CreateJobTest {

	private static final String dataUrl = "http://10.10.11.52:8765/";

	public static void main(String[] args) throws InterruptedException {
		HttpRequest httpRequest = HttpRequest.post(dataUrl+"job/add");
		String xGiveuAppKey = "giveu_file_push";
		String xGiveuTimestamp = String.valueOf(new Date().getTime());
		String jobName = "GiveuFilePush2";
		String jobDesc = "Test";
		String callbackUrl = "http://www.baidu.com";
		String cronExpression = "*/5 * * * * ?";
		String jobCode = CommonUtil.getUUID32();

		httpRequest.header("xGiveuAppKey", xGiveuAppKey);
		httpRequest.header("xGiveuTimestamp", xGiveuTimestamp);

		HttpMultiMap multiMap = httpRequest.query();
		multiMap.add("jobName", jobName);
		multiMap.add("jobDesc", jobDesc);
		multiMap.add("callbackUrl", callbackUrl);
		multiMap.add("cronExpression", cronExpression);
		multiMap.add("jobCode", jobCode);

		StringBuilder sb = new StringBuilder();
		sb.append("xGiveuAppKey").append(xGiveuAppKey);
		sb.append("xGiveuTimestamp").append(xGiveuTimestamp);
		sb.append("jobName").append(jobName);
		sb.append("jobDesc").append(jobDesc);
		sb.append("callbackUrl").append(callbackUrl);
		sb.append("cronExpression").append(cronExpression);
		sb.append("jobCode").append(jobCode);
		sb.append("@#4234YYSDF**234");

		String original = sb.toString();
		String encrypted = MD5Util.sign(original);

		httpRequest.header("xGiveuSign", encrypted);

		HttpResponse response = httpRequest.send();
		System.out.println(response.bodyText());

	}
}
