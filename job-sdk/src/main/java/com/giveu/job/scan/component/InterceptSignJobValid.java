package com.giveu.job.scan.component;

import com.alibaba.fastjson.JSONObject;
import com.giveu.job.scan.annotation.JobCallBackSignValid;
import com.giveu.job.scan.info.CommonMessage;
import com.giveu.job.scan.model.ResultModel;
import com.giveu.job.scan.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Writer;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by fox on 2018/11/26.
 */
@Component
@PropertySource(value = {"classpath:application-job-sdk.properties"}, ignoreResourceNotFound = true)
public class InterceptSignJobValid implements HandlerInterceptor {

	public static Logger logger = LoggerFactory.getLogger(InterceptSignJobValid.class);

	@Value("${job.callback.sign.app.key}")
	private String appKey;

	@Value("${job.callback.sign.app.secret}")
	private String secret;

	// 10分钟
	public static final long DIFF_MILLISECOND = 10 * 60 * 1000;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {

		if (o instanceof HandlerMethod) {
			JobCallBackSignValid jobCallBackSignValid = ((HandlerMethod) o).getMethodAnnotation(JobCallBackSignValid.class);
			if (jobCallBackSignValid == null) {
				return true;
			}
		} else {
			return true;
		}


		Map<String, String> map = new HashMap<>();

		String xGiveuSign = request.getHeader("xGiveuSign");
		String xGiveuAppKey = request.getHeader("xGiveuAppKey");
		String xGiveuTimestamp = request.getHeader("xGiveuTimestamp");

		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json;charset=UTF-8");

		Long timestamp = NumberUtils.toLong(xGiveuTimestamp, 0);
		boolean b = validTimestamp(timestamp);
		if (!b) {
			logger.info("Timestamp error...");
			ResultModel resultModel = new ResultModel();
			resultModel.setCode(CommonMessage.SIGN_ERROR_CODE);
			resultModel.setMessage(CommonMessage.SIGN_ERROR_DESC);

			String resultModelJson = JSONObject.toJSONString(resultModel);
			Writer writer = response.getWriter();
			writer.write(resultModelJson);
			writer.flush();
			writer.close();
			logger.info(resultModelJson);
			return false;
		}

		StringBuilder sb = new StringBuilder();

		String jobSessionId = request.getParameter("jobSessionId");
		map.put("xGiveuAppKey",xGiveuAppKey);
		map.put("xGiveuTimestamp",xGiveuTimestamp);

		map.put("jobSessionId",jobSessionId);

		// 遍历请求参数，判断是否有空字段，且生成部分原始加密字符串
		for (Map.Entry<String, String> entry : map.entrySet()) {
			if (StringUtils.isBlank(entry.getValue())) {
				continue;
			}
			sb.append(entry.getKey() + entry.getValue());
		}

		sb.append(secret);
		String originaly = sb.toString();

		String encrypted = MD5Util.sign(originaly);
		if (encrypted.equals(xGiveuSign)) {
			return true;
		}
		ResultModel resultModel = new ResultModel();
		resultModel.setCode(CommonMessage.SIGN_ERROR_CODE);
		resultModel.setMessage(CommonMessage.SIGN_ERROR_DESC);

		String resultModelJson = JSONObject.toJSONString(resultModel);
		Writer writer = response.getWriter();
		writer.write(resultModelJson);
		writer.flush();
		writer.close();
		logger.info(resultModelJson);

		return false;

	}

	@Override
	public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object o, Exception e) throws Exception {


	}


	public static Boolean validTimestamp(long timestamp) {
		Boolean result = false;
		Date date = new Date();
		long diff = date.getTime() - timestamp;
		if (Math.abs(diff) < DIFF_MILLISECOND) {
			result = true;
		}
		return result;
	}

}
