package com.giveu.service.impl;

import com.giveu.common.httpclient.component.HttpComponent;
import com.giveu.dao.JobAsyncDAO;
import com.giveu.dto.JobAsyncDTO;
import com.giveu.job.common.entity.QrtzTriggerLog;
import com.giveu.job.common.info.CommonMessage;
import com.giveu.job.common.var.CommonVar;
import com.giveu.job.common.vo.ResultModel;
import com.giveu.service.JobAsyncService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

/**
 * Created by fox on 2019/1/3.
 */
@Service
public class JobAsyncServiceImpl implements JobAsyncService {

	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(JobAsyncServiceImpl.class);

	@Autowired
	JobAsyncDAO jobAsyncDAO;

	@Autowired
	HttpComponent httpComponent;


	@Override
	public ResultModel queueHandler() {


		ResultModel resultModel = new ResultModel();
		resultModel.setCode(CommonMessage.OK_CODE);

		JobAsyncDTO jobAsyncDTO = jobAsyncDAO.getJobAsyncDTO();
		if (jobAsyncDTO == null) {
			return resultModel;
		}

		String logId = jobAsyncDTO.getLogId();
		if (StringUtils.isBlank(logId)) {
			logger.error("LogId is null...");
			logger.error(jobAsyncDTO.toString());
			resultModel.setCode(CommonMessage.ERROR_CODE);
			return resultModel;
		}

		String tableName = CommonVar.LOG_TABLE_NAME_PREFIX + jobAsyncDTO.getAppKey();

		jobAsyncDAO.asyncResultDelByLogId(logId);

		ResultModel result = new ResultModel();

		try {
			result = httpComponent.doPostFormResultObject(jobAsyncDTO.getResultUrl(), ResultModel.class);
		} catch (IOException e) {
			e.printStackTrace();
			result.setCode(CommonMessage.ERROR_CODE);
			result.setMessage(e.getMessage());
		}

		int executeStatus;
		if (result.getCode() != null && result.getCode().equals(CommonMessage.OK_CODE)) {
			executeStatus = 1;
		} else {
			executeStatus = 2;
		}

		jobAsyncDAO.resultUpdate(logId, executeStatus);
		jobAsyncDAO.resultUpdateSonLog(tableName, logId, executeStatus);

		jobAsyncDAO.triggerCountPlus(jobAsyncDTO.getJobId(), executeStatus);


		return resultModel;
	}


	@Override
	@Transactional
	public ResultModel resultUpdate(String sessionId, Integer executeStatus) {
		ResultModel resultModel = new ResultModel();
		resultModel.setCode(CommonMessage.OK_CODE);
		if (executeStatus != 1 && executeStatus != 2) {
			resultModel.setCode(CommonMessage.NO_AUTHORITY_ERROR_CODE);
			resultModel.setMessage(CommonMessage.NO_AUTHORITY_ERROR_DESC);
			return resultModel;
		}
		Integer affect = jobAsyncDAO.resultUpdate(sessionId, executeStatus);
		if (affect != 1) {
			return resultModel;
		}
		QrtzTriggerLog log = jobAsyncDAO.getRecQrtzTriggerLogById(sessionId);
		String appKey = log.getAppKey();
		String tableName = CommonVar.LOG_TABLE_NAME_PREFIX + appKey;
		jobAsyncDAO.resultUpdateSonLog(tableName, sessionId, executeStatus);
		jobAsyncDAO.asyncResultDelByLogId(sessionId);
		jobAsyncDAO.triggerCountPlus(log.getJobId(), executeStatus);
		return resultModel;
	}
}
