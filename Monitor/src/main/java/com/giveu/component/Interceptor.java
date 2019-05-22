package com.giveu.component;

import com.alibaba.fastjson.JSONObject;
import com.giveu.job.common.info.CommonMessage;
import com.giveu.job.common.util.MD5Util;
import com.giveu.job.common.vo.ResultModel;
import com.giveu.service.impl.MonitorBaseImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * 验签拦截器
 * Created by fox on 2018/8/10.
 */
@Component
public class Interceptor implements HandlerInterceptor {

	public static Logger logger = LoggerFactory.getLogger(MonitorBaseImpl.class);

	private static final String appSecret = "ADSFADSFADF";

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {

		Map<String, String> map = new HashMap<>();

		String xGiveuSign = request.getHeader("xGiveuSign");
		String xGiveuAppKey = request.getHeader("xGiveuAppKey");
		String xGiveuTimestamp = request.getHeader("xGiveuTimestamp");

		String jobSessionId = request.getParameter("jobSessionId");

		map.put("xGiveuSign",xGiveuSign);
		map.put("xGiveuAppKey",xGiveuAppKey);
		map.put("xGiveuTimestamp",xGiveuTimestamp);
		map.put("jobSessionId",jobSessionId);

		StringBuilder sb = new StringBuilder();
		// 遍历请求参数，判断是否有空字段，且生成部分原始加密字符串
		for (Map.Entry<String, String> entry : map.entrySet()) {
			if (entry.getKey().equals("xGiveuSign")) {
				continue;
			}
			sb.append(entry.getKey() + entry.getValue());
		}

		sb.append(appSecret);
		String originaly = sb.toString();

		String encrypted = MD5Util.sign(originaly);
		if (encrypted.equals(xGiveuSign)) {
			return true;
		}
		ResultModel resultModel = new ResultModel();
		resultModel.setCode(CommonMessage.SIGN_ERROR_CODE);
		resultModel.setMessage(CommonMessage.SIGN_ERROR_DESC);

		response.setCharacterEncoding("UTF-8");
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
	public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
	}
}
