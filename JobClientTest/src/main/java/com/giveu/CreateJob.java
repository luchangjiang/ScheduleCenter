package com.giveu;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.giveu.job.common.util.MD5Util;
import com.giveu.job.common.var.Secret;
import com.giveu.job.common.util.CommonUtil;
import jodd.http.HttpMultiMap;
import jodd.http.HttpRequest;
import jodd.http.HttpResponse;

import java.util.Date;

/**
 * Created by fox on 2018/7/11.
 */
public class CreateJob {

//	private static final String dataUrl = "http://localhost:8765/";
	private static final String dataUrl = "http://localhost:9060/";
//	private static final String dataUrl = "http://10.12.11.196:8765/";
//	private static final String dataUrl = "http://10.10.11.52:8765/";

	public static void main(String[] args) throws InterruptedException {

		for (int  i = 0; i < 50; i++) {
			Thread.sleep(1);
			HttpRequest httpRequest = HttpRequest.post(dataUrl+"job/add");
			String xGiveuAppKey = "monitor";
			String xGiveuTimestamp = String.valueOf(new Date().getTime());
//			String jobName = "JOB" + CommonUtil.getUUID32();
			String jobName = "job" + CommonUtil.getUUID32();
			String jobDesc = "Test";
			String callbackUrl = "http://localhost:6088/job";
//			String callbackUrl = "http://localhost:9040/monitor/open/account/handle";
//			String callbackUrl = "http://10.14.21.100:6088/foo3";
			String cronExpression = "*/1 * * * * ?";
//			String cronExpression = "0 12 * * * ? ";
//			String cronExpression = "*/5 * * * * ?";
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
			sb.append(Secret.APP_SECRET);

			String original = sb.toString();
			String encrypted = MD5Util.sign(original);

			httpRequest.header("xGiveuSign", encrypted);

			ObjectMapper mapper = new ObjectMapper();
			HttpResponse response = httpRequest.send();
			System.out.println(response);
		}

//		String result = response.bodyText();
	}
}
