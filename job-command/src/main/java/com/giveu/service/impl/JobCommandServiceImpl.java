package com.giveu.service.impl;

import com.giveu.job.common.info.CommonMessage;
import com.giveu.job.common.vo.ResultModel;
import com.giveu.service.JobCommandService;
import com.giveu.util.CommandUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fox on 2019/1/18.
 */
@Service
public class JobCommandServiceImpl implements JobCommandService {

	private static Logger logger = LoggerFactory.getLogger(JobCommandServiceImpl.class);

	@Value("${mgr.passwd}")
	String mgrPasswd;

	@Override
	public ResultModel jarList() {
		ResultModel resultModel = new ResultModel();
		resultModel.setCode(CommonMessage.OK_CODE);
		resultModel.setMessage(CommonMessage.OK_DESC);
//		String command = "ps -ef|grep SNAPSHOT.jar";
//		String command = "ifconfig";
		List<String> list = null;
		List<String> commandList = new ArrayList<>();
		commandList.add("/bin/sh");
		commandList.add("-c");
		commandList.add("ps -ef|grep jar");
		try {
			list = CommandUtil.run(commandList.toArray(new String[commandList.size()]));
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(ExceptionUtils.getStackTrace(e));
			resultModel.setCode(CommonMessage.ERROR_CODE);
			resultModel.setMessage(e.getMessage());
		}
		resultModel.setData(list);
		return resultModel;
	}

	@Override
	public ResultModel start(String proName, String proPort, String proActive, String passwd) {
		ResultModel resultModel = new ResultModel();
		if (!mgrPasswd.equals(passwd)) {
			resultModel.setCode(CommonMessage.NO_AUTHORITY_ERROR_CODE);
			resultModel.setMessage(CommonMessage.NO_AUTHORITY_ERROR_DESC);
			return resultModel;
		}
		if (StringUtils.isBlank(proName)) {
			resultModel.setCode(CommonMessage.PAR_NOT_NULL_CODE);
			resultModel.setMessage(CommonMessage.PAR_NOT_NULL_DESC);
			return resultModel;
		}
		if (StringUtils.isBlank(proName)) {
			proName = "null";
		}
		if (StringUtils.isBlank(proPort)) {
			proPort = "null";
		}
		if (StringUtils.isBlank(proActive)) {
			proActive = "null";
		}
		String command = "sh ./health.sh " + proName + " " + proPort + " " + proActive + " " + "start";
		return exe(command);
	}

	@Override
	public ResultModel stop(String proName, String proPort, String proActive, String passwd) {
		ResultModel resultModel = new ResultModel();
		if (!mgrPasswd.equals(passwd)) {
			resultModel.setCode(CommonMessage.NO_AUTHORITY_ERROR_CODE);
			resultModel.setMessage(CommonMessage.NO_AUTHORITY_ERROR_DESC);
			return resultModel;
		}
		if (StringUtils.isBlank(proName) && StringUtils.isBlank(proPort) && StringUtils.isBlank(proActive)) {
			resultModel.setCode(CommonMessage.PAR_NOT_NULL_CODE);
			resultModel.setMessage(CommonMessage.PAR_NOT_NULL_DESC);
			return resultModel;
		}
		if (StringUtils.isBlank(proName)) {
			proName = "null";
		}
		if (StringUtils.isBlank(proPort)) {
			proPort = "null";
		}
//		if (StringUtils.isBlank(proActive)) {
//			proActive = "null";
//		}
		String command = "sh ./health.sh " + proName + " " + proPort + " null " + "stop";
		return exe(command);
	}

	@Override
	public ResultModel restart(String proName, String proPort, String proActive, String passwd) {
		ResultModel resultModel = new ResultModel();
		if (!mgrPasswd.equals(passwd)) {
			resultModel.setCode(CommonMessage.NO_AUTHORITY_ERROR_CODE);
			resultModel.setMessage(CommonMessage.NO_AUTHORITY_ERROR_DESC);
			return resultModel;
		}
		if (StringUtils.isBlank(proName) && StringUtils.isBlank(proPort) && StringUtils.isBlank(proActive)) {
			resultModel.setCode(CommonMessage.PAR_NOT_NULL_CODE);
			resultModel.setMessage(CommonMessage.PAR_NOT_NULL_DESC);
			return resultModel;
		}
		if (StringUtils.isBlank(proName)) {
			proName = "null";
		}
		if (StringUtils.isBlank(proPort)) {
			proPort = "null";
		}
		if (StringUtils.isBlank(proActive)) {
			proActive = "null";
		}
		String command = "sh ./health.sh " + proName + " " + proPort + " " + proActive + " " + "restart";
		return exe(command);
	}

	@Override
	public ResultModel status(String proName, String proPort, String proActive) {
		String command = "sh ./health.sh " + proName + " " + proPort + " " + proActive + " " + "status";
		return exe(command);
	}

	private ResultModel exe(String command) {
		System.out.println(command);
		ResultModel resultModel = new ResultModel();
		resultModel.setCode(CommonMessage.OK_CODE);
		resultModel.setMessage(CommonMessage.OK_DESC);
		try {
			Process ps = Runtime.getRuntime().exec(command);
			ps.waitFor();

			BufferedReader br = new BufferedReader(new InputStreamReader(ps.getInputStream()));
			StringBuffer sb = new StringBuffer();
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line).append("\n");
			}
			String result = sb.toString();
			System.out.println(result);
			resultModel.setData(result);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			e.printStackTrace();
			resultModel.setCode(CommonMessage.ERROR_CODE);
			resultModel.setMessage(CommonMessage.ERROR_DESC);
			resultModel.setData(e.getMessage());
		}
		return resultModel;
	}
}
