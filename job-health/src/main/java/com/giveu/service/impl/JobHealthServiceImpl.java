package com.giveu.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.giveu.component.ProList;
import com.giveu.common.httpclient.component.HttpComponent;
import com.giveu.component.JobHealthRunner;
import com.giveu.dao.JobCommonDAO;
import com.giveu.job.common.entity.ProInfo;
import com.giveu.job.common.entity.QrtzSchedulerState;
import com.giveu.job.common.entity.QrtzTriggerLog;
import com.giveu.job.common.info.CommonMessage;
import com.giveu.job.common.util.DateUtil;
import com.giveu.job.common.vo.ResultModel;
import com.giveu.service.JobHealthService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;

/**
 * Created by fox on 2019/1/15.
 */
@Service
public class JobHealthServiceImpl implements JobHealthService {

	private static Logger logger = LoggerFactory.getLogger(JobHealthServiceImpl.class);

	@Autowired
	JobCommonDAO jobCommonDAO;

	@Autowired
	ProList proList;

	List<String> heartbeatList = new Vector<>();

	private int heartbeatListSize = 30;

	@Autowired
	HttpComponent httpComponent;

	@Value("${server.url}")
	String serverUrl;

	private String OP_START;
	private String OP_STOP;
	private String OP_RESTART;
	private String JAR_LIST_URL;

	@PostConstruct
	private void init() {
		OP_START = serverUrl + "/command/start";
		OP_STOP = serverUrl + "/command/stop";
		OP_RESTART = serverUrl + "/command/restart";
		JAR_LIST_URL = serverUrl + "/command/jar/list";
	}


	private static final String RUNNING = "运行中";
	private static final String STOP = "关闭";

	@Override
	public ResultModel heartbeat() {

		ResultModel resultModel = new ResultModel();
		resultModel.setCode(CommonMessage.OK_CODE);
		resultModel.setMessage(CommonMessage.OK_DESC);
		JobHealthRunner.last = System.currentTimeMillis();
		String time = DateUtil.getStringDateSSS();
		Date date = jobCommonDAO.getNow();
		String dateStr = "数据库时间：" + DateUtil.getStringDateSSS(date) + "  系统时间：" + time;
		logger.info(dateStr);
		if (heartbeatList.size() > heartbeatListSize) {
			heartbeatList.remove(0);
			heartbeatList.add(dateStr);
		} else {
			heartbeatList.add(dateStr);
		}
		return resultModel;
	}

	@Override
	public ResultModel proList() {

		List<ProInfo> proInfoList = proList.getProInfoList();

		ResultModel resultModel = null;
		try {
			resultModel = httpComponent.doPostFormResultObject(JAR_LIST_URL, ResultModel.class);
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(ExceptionUtils.getStackTrace(e));
		}

		Object obj = resultModel.getData();

		JSONArray array = null;

		if (obj instanceof JSONArray) {
			array = (JSONArray) resultModel.getData();
		}

		if (array == null || array.size() == 0) {
			resultModel.setCode(CommonMessage.OK_CODE);
			resultModel.setMessage(CommonMessage.OK_DESC);
			resultModel.setData(proInfoList);
			return resultModel;
		}


		for (ProInfo info : proInfoList) {
			info.setProStatus(STOP);
			for (Object arr : array) {
				if (arr.toString().indexOf("server.port") >= 0) {
					if (arr.toString().indexOf(info.getProPort()) >= 0) {
						info.setProStatus(RUNNING);
						break;
					}
				} else {
					if (arr.toString().indexOf(info.getProName()) >= 0) {
						info.setProStatus(RUNNING);
						break;
					}
				}
			}
		}

		resultModel.setData(proInfoList);
		return resultModel;
	}

	@Override
	public ResultModel start(String proName, String proPort, String proActive, String passwd) {
		return handle(proName, proPort, proActive, passwd, OP_START);
	}

	@Override
	public ResultModel stop(String proName, String proPort, String proActive, String passwd) {
		return handle(proName, proPort, proActive, passwd, OP_STOP);
	}

	@Override
	public ResultModel restart(String proName, String proPort, String proActive, String passwd) {
		return handle(proName, proPort, proActive, passwd, OP_RESTART);
	}

	private ResultModel handle(String proName, String proPort, String proActive, String passwd, String url) {
		ResultModel resultModel = new ResultModel();
		if (StringUtils.isBlank(passwd)) {
			resultModel.setCode(CommonMessage.NO_AUTHORITY_ERROR_CODE);
			resultModel.setMessage(CommonMessage.NO_AUTHORITY_ERROR_DESC);
			return resultModel;
		}
		if (StringUtils.isBlank(proName)) {
			resultModel.setCode(CommonMessage.PAR_NOT_NULL_CODE);
			resultModel.setMessage(CommonMessage.PAR_NOT_NULL_DESC);
			return resultModel;
		}
		Map<String, String> map = new HashMap<>();
		map.put("proName", proName);
		map.put("proPort", proPort);
		map.put("proActive", proActive);
		map.put("passwd", passwd);

		try {
			resultModel = httpComponent.doPostFormResultObject(url, ResultModel.class, map);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(ExceptionUtils.getStackTrace(e));
			resultModel.setCode(CommonMessage.ERROR_CODE);
			resultModel.setMessage(CommonMessage.ERROR_DESC);
			resultModel.setData(e.getMessage());
		}
		return resultModel;
	}

	@Override
	public ResultModel triggerLog() {
		ResultModel resultModel = new ResultModel();
		resultModel.setCode(CommonMessage.OK_CODE);
		resultModel.setMessage(CommonMessage.OK_DESC);
		List<QrtzTriggerLog> list = jobCommonDAO.getTriggerLog();
		resultModel.setData(list);
		return resultModel;

	}

	@Override
	public ResultModel heartbeatLog() {
		ResultModel resultModel = new ResultModel();
		resultModel.setCode(CommonMessage.OK_CODE);
		resultModel.setMessage(CommonMessage.OK_DESC);
		resultModel.setData(heartbeatList);
		return resultModel;
	}

	@Override
	public ResultModel runningList() {
//		ResultModel resultModel = new ResultModel();
//		resultModel.setCode(CommonMessage.OK_CODE);
//		resultModel.setMessage(CommonMessage.OK_DESC);
//		List<String> list = null;
//		//			List<String> commandList = new ArrayList<>();
////			commandList.add("/bin/sh");
////			commandList.add("-c");
////			commandList.add("ps -ef|grep jar");
////			list = CommandUtil.run(commandList.toArray(new String[commandList.size()]));
////			System.out.println(list.toArray());
//
////		String hostname = "10.11.13.26";
//		String hostname = "10.11.13.30";
//		String username = "giro";
//		String password = "giro";
//		try {
////			System.out.println(SSHUtil.execRemoteCommand(hostname, username, password,
////					"ps -ef|grep SNAPSHOT"));
//
//			String str = SSHUtil.execRemoteCommand(hostname, username, password,
//					"ps -ef|grep SNAPSHOT");
//			String[] arr = str.split("\r\n");
////			System.out.println(Arrays.toString(arr));
//			list = Arrays.asList(arr);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		resultModel.setData(list);
//		return resultModel;


		ResultModel resultModel = null;
		try {
			resultModel = httpComponent.doPostFormResultObject(JAR_LIST_URL, ResultModel.class);
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(ExceptionUtils.getStackTrace(e));
			resultModel.setCode(CommonMessage.ERROR_CODE);
			resultModel.setMessage(e.getMessage());
		}

		return resultModel;
	}

	@Override
	public ResultModel scheduleState() {
		ResultModel resultModel = new ResultModel();
		resultModel.setCode(CommonMessage.OK_CODE);
		resultModel.setMessage(CommonMessage.OK_DESC);
		List<QrtzSchedulerState> list = jobCommonDAO.getSchedulerState();
		resultModel.setData(list);
		return resultModel;
	}

	@Override
	public ResultModel lock(String isOpen, String passwd) {
		ResultModel resultModel = new ResultModel();
		resultModel.setCode(CommonMessage.OK_CODE);
		resultModel.setMessage(CommonMessage.OK_DESC);
		Map<String, String> map = new HashMap<>();
		map.put("isOpen", isOpen);
		map.put("passwd", passwd);
		try {
			resultModel = httpComponent.doPostFormResultObject("http://localhost:9060/job/lock", ResultModel.class, map);
		} catch (IOException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			e.printStackTrace();
			resultModel.setCode(CommonMessage.ERROR_CODE);
			resultModel.setMessage(CommonMessage.ERROR_DESC);
		}
		return resultModel;
	}

	@Override
	public ResultModel lockStatus() {
		ResultModel resultModel = new ResultModel();
		resultModel.setCode(CommonMessage.OK_CODE);
		resultModel.setMessage(CommonMessage.OK_DESC);
		try {
			resultModel = httpComponent.doPostFormResultObject("http://localhost:9060/job/lock/status", ResultModel.class);
		} catch (IOException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			e.printStackTrace();
			resultModel.setCode(CommonMessage.ERROR_CODE);
			resultModel.setMessage(CommonMessage.ERROR_DESC);
		}
		return resultModel;
	}
}
