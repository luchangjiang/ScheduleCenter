package com.giveu;

import com.giveu.job.common.util.CommonUtil;
import com.giveu.job.common.util.MD5Util;
import jodd.http.HttpMultiMap;
import jodd.http.HttpRequest;
import jodd.http.HttpResponse;

/**
 * Created by fox on 2018/7/11.
 */
public class CallbackTest {

	private static String appKey = "giveu_store";
	private static String secret = "123456";
	private static final String CALL_BACK_URL = "http://10.12.11.110:10020/v1/task/order/orderSelfSendTask/sendSelfMail";

	public static void main(String[] args) throws InterruptedException {
		HttpRequest httpRequest = HttpRequest.post(CALL_BACK_URL);

		String xGiveuAppKey = appKey;
		String xGiveuTimestamp = String.valueOf(System.currentTimeMillis());
		String jobSessionId = CommonUtil.getUUID32();


		httpRequest.header("xGiveuAppKey", xGiveuAppKey);
		httpRequest.header("xGiveuTimestamp", xGiveuTimestamp);


		HttpMultiMap multiMap = httpRequest.query();
		multiMap.add("jobSessionId", jobSessionId);

		StringBuilder sb = new StringBuilder();
		sb.append("xGiveuAppKey").append(xGiveuAppKey);
		sb.append("xGiveuTimestamp").append(xGiveuTimestamp);
		sb.append("jobSessionId").append(jobSessionId);
		sb.append(secret);

		String original = sb.toString();
		String encrypted = MD5Util.sign(original);

		httpRequest.header("xGiveuSign", encrypted);

		HttpResponse response = httpRequest.send();
		System.out.println(response.bodyText());

		System.out.println("SessionId: " + jobSessionId);
		System.out.println("Timestamp: " + xGiveuTimestamp);
		System.out.println("Sign: " + encrypted);
	}
}
