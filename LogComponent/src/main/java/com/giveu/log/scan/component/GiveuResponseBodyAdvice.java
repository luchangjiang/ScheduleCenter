package com.giveu.log.scan.component;

import com.giveu.log.scan.common.ThreadLocalCommon;
import com.giveu.log.scan.vo.MonitorLog;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * Created by fox on 2018/8/16.
 */
//@ControllerAdvice(annotations = RestController.class)
@ControllerAdvice
public class GiveuResponseBodyAdvice implements ResponseBodyAdvice {

	private static final int MAX_BODY_LENGTH = 15000;



	@Override
	public boolean supports(MethodParameter returnType, Class converterType) {
		return true;
	}

	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {

		MonitorLog monitorLog = ThreadLocalCommon.threadLocalMonitorLog.get();
		if (monitorLog == null) {
			return body;
		}

		String bodyStr = String.valueOf(body);

		if (bodyStr != null && bodyStr.length() > MAX_BODY_LENGTH) {
			bodyStr = bodyStr.substring(0, MAX_BODY_LENGTH);
		}

		monitorLog.setResponseBody(bodyStr);
		if (bodyStr.indexOf("requestId") >= 0 && bodyStr.indexOf("status") >= 0 && bodyStr.indexOf("-100") >= 0) {
			monitorLog.setStatuCode("500");
			return body;
		}
		if (bodyStr.indexOf("requestId") < 0 && bodyStr.indexOf("code=-1") >= 0) {
			monitorLog.setStatuCode("500");
			return body;
		}
		return body;
	}
}
