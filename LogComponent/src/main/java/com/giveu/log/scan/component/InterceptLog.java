package com.giveu.log.scan.component;

import com.giveu.log.scan.common.ThreadLocalCommon;
import com.giveu.log.scan.util.*;
import com.giveu.log.scan.vo.MonitorLog;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.InetAddress;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by fox on 2018/8/13.
 */
@Component
@PropertySource(value = { "classpath:application-log.properties" }, ignoreResourceNotFound = true)
public class InterceptLog extends HandlerInterceptorAdapter {

	public static Logger logger = LoggerFactory.getLogger(InterceptLog.class);

	private static final String SCHEME = "http";

	private static final String errorCode = "500";

	private static final String SYSTEM_NAME = "system_name";

	private static final String REQUEST_ID = "request_id";

	@Value("${current.user}")
	private String currentUser;

	@Autowired
	AsyncLogHandle asyncLogHandle;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		MonitorLog monitorLog = new MonitorLog();
		monitorLog.setStartTime(System.currentTimeMillis());
		ThreadLocalCommon.threadLocalMonitorLog.set(monitorLog);

		return super.preHandle(request, response, handler);
	}

	@Override
	public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object o, Exception e) throws Exception {

//		if (!(request instanceof RequestFacade)) {
//			return;
//		}


		MonitorLog monitorLog = ThreadLocalCommon.threadLocalMonitorLog.get();
		ThreadLocalCommon.threadLocalMonitorLog.remove();

		Long startTime = monitorLog.getStartTime();
		Long endTime = System.currentTimeMillis();
		Long leadTime = endTime - startTime;
//		logger.debug(request.getRequestURL() + " Total Lead time： " + leadTime);
		monitorLog.setTotalMilliseconds(leadTime);
		Collection<String> collection = response.getHeaderNames();

		String _id = DateUtil.getStringDateShortYYMMDD() + CommonUtil.getUUID32();
		String scheme = SCHEME;
		String httpMethod = request.getMethod();
		String remoteIpAddress = NetUtil.getIPAddress(request);
		String localIpAddress = InetAddress.getLocalHost().getHostAddress();
		if (localIpAddress.equals("127.0.0.1")) {
			localIpAddress = request.getServerName();
		}

		Integer remotePort = request.getRemotePort();
		Integer localPort = request.getLocalPort();


		String path = request.getServletPath();
		String requestTime = DateUtil.getStringDateSSS();


		Enumeration headerNames = request.getHeaderNames();
		Map<String, String> headerMap = new HashMap<>();
		while (headerNames.hasMoreElements()) {
			String key = (String) headerNames.nextElement();
			String value = request.getHeader(key);
			headerMap.put(key, value);
		}

		String requestHeader = JsonUtil.toJson(headerMap);

		monitorLog.set_id(_id);
		monitorLog.setScheme(scheme);
		monitorLog.setHttpMethod(httpMethod);
		monitorLog.setRemoteIpAddress(remoteIpAddress);
		monitorLog.setLocalIpAddress(localIpAddress);
		monitorLog.setRemotePort(remotePort);
		monitorLog.setLocalPort(localPort);
		monitorLog.setPath(path);
		monitorLog.setRequestTime(requestTime);
		monitorLog.setRequestHeader(requestHeader);

		monitorLog.setCurrentUser(currentUser);
		monitorLog.setLocalSystemName(PathUtil.getProjectName());

		// Get request id
		String requestId =  request.getHeader(REQUEST_ID);
		if (requestId == null || requestId.equals("")) {
			requestId = CommonUtil.getUUID32();
		}
		if (StringUtils.isNotBlank(requestId)) {
			monitorLog.setRequestId(requestId);
		}
		// Get source system name
		String sourceSystemName =  request.getHeader(SYSTEM_NAME);
		if (StringUtils.isNotBlank(sourceSystemName)) {
			monitorLog.setSourceSystemName(sourceSystemName);
		}

		if (response == null) {
			monitorLog.setStatuCode(errorCode);
		}

		if (!errorCode.equals(monitorLog.getStatuCode())) {
			monitorLog.setStatuCode(String.valueOf(response.getStatus()));
		}

		monitorLog.setResponeTime(DateUtil.getStringDateSSS());

		Map<String, String> respHeaderMap = new HashMap();
		for (String c : collection) {
			respHeaderMap.put(c, response.getHeader(c));
		}
		monitorLog.setResponeHeader(JsonUtil.toJson(respHeaderMap));

		if (e != null) {
			StringWriter stringWriter = new StringWriter();
			PrintWriter printWriter = new PrintWriter(stringWriter);
			e.printStackTrace(printWriter);
			String statckTrace = printWriter.toString();
			monitorLog.setExceptionTraceStack(statckTrace);
			monitorLog.setExceptionMessage(e.getMessage());
		}

		RepeatedlyReadRequestWrapper requestWrapper = null;

		Enumeration paramNames = request.getParameterNames();
		Map paramMap = new HashMap();
		while (paramNames.hasMoreElements()) {
			String paramName = (String) paramNames.nextElement();
			String[] paramValues = request.getParameterValues(paramName);
			if (paramValues.length == 1) {
				String paramValue = paramValues[0];
				if (paramValue.length() != 0) {
					paramMap.put(paramName, paramValue);
				}
			}
		}

		String queryString = "";

		if (paramMap.size() == 0) {
			if (request instanceof RepeatedlyReadRequestWrapper) {
				requestWrapper = (RepeatedlyReadRequestWrapper) request;
				queryString = getBodyString(requestWrapper);
			}
		} else {
			queryString = JsonUtil.toJson(paramMap);
		}

		if (StringUtils.isBlank(queryString)) {

			StringBuilder sb = new StringBuilder();
			InputStream is = request.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String s = "";
			while ((s = br.readLine()) != null) {
				sb.append(s);
			}
			queryString = sb.toString();
		}


		monitorLog.setQueryString(queryString);
		monitorLog.setRequestBody(queryString);

		asyncLogHandle.record(monitorLog);

	}


	/**
	 * 获取请求Body
	 *
	 * @param request
	 *
	 * @return
	 */
	public static String getBodyString(final ServletRequest request) {
		StringBuilder sb = new StringBuilder();
		InputStream inputStream = null;
		BufferedReader reader = null;
		try {
			inputStream = cloneInputStream(request.getInputStream());
			reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
			String line = "";
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return sb.toString();
	}

	/**
	 * Description: 复制输入流</br>
	 *
	 * @param inputStream
	 *
	 * @return</br>
	 */
	public static InputStream cloneInputStream(ServletInputStream inputStream) {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len;
		try {
			while ((len = inputStream.read(buffer)) > -1) {
				byteArrayOutputStream.write(buffer, 0, len);
			}
			byteArrayOutputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		InputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
		return byteArrayInputStream;
	}
}
