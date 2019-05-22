package com.giveu.service.impl;

import com.giveu.dao.mysql.QrtzTriggerLogDAO;
import com.giveu.entity.QrtzAppInfo;
import com.giveu.job.common.info.CommonMessage;
import com.giveu.job.common.vo.JobTriggerAppLogVo;
import com.giveu.job.common.vo.ResultModel;
import com.giveu.service.JobService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by fox on 2018/12/1.
 */
@Service
public class JobServiceImpl implements JobService {


	@SuppressWarnings("all")
	@Autowired
	QrtzTriggerLogDAO qrtzTriggerLogDAO;


	@Override
	public ResultModel getJobByCode(String jobCode) {
		ResultModel resultModel = new ResultModel();
		resultModel.setCode(CommonMessage.OK_CODE);
		resultModel.setMessage(CommonMessage.OK_DESC);
		resultModel.setData(qrtzTriggerLogDAO.getJobByCode(jobCode));
		return resultModel;
	}

	public void logList(HttpServletRequest request, ResultModel resultModel) {

		String beginTime = request.getParameter("beginTime");
		String endTime = request.getParameter("endTime");
		String jobId = request.getParameter("jobId");
		String jobCode = request.getParameter("jobCode");
		String appKey = request.getParameter("appKey");
		String userAccount = request.getParameter("userAccount");
		Integer executeStatus = NumberUtils.toInt(request.getParameter("executeStatus"), 0);

		Long triggerBeginTime = null;
		Long triggerEndTime = null;


		if (StringUtils.isNotBlank(beginTime)) {
			try {
				triggerBeginTime = dateToStamp(beginTime);

			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if (StringUtils.isNotBlank(endTime)) {
			try {
				triggerEndTime = dateToStamp(endTime) + (86400000 - 1);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		if (StringUtils.isBlank(jobId)) {
			jobId = null;
		}
		if (StringUtils.isBlank(jobCode)) {
			jobCode = null;
		}

		List<String> appKeyList = qrtzTriggerLogDAO.getAppKeyByAccount(userAccount);

		if (appKeyList == null && appKeyList.size() > 0) {
			resultModel.setCode(CommonMessage.NO_AUTHORITY_ERROR_CODE);
			resultModel.setMessage(CommonMessage.NO_AUTHORITY_ERROR_DESC);
			return;
		}


		if (StringUtils.isBlank(appKey)) {
			appKey = appKeyList.get(0);
			QrtzAppInfo qrtzAppInfo = qrtzTriggerLogDAO.getAppInfoByAppKey(appKey);
			if (qrtzAppInfo == null) {
				resultModel.setCode(CommonMessage.TABLE_NOT_FOUND_ERROR_CODE);
				resultModel.setMessage(CommonMessage.TABLE_NOT_FOUND_ERROR_DESC);
				return;
			}
		} else {
			boolean isContains = appKeyList.contains(appKey);
			if (!isContains) {
				resultModel.setCode(CommonMessage.NO_AUTHORITY_ERROR_CODE);
				resultModel.setMessage(CommonMessage.NO_AUTHORITY_ERROR_DESC);
				return;
			}
		}

		String tableName = "QRTZ_TRIGGER_LOG_" + appKey;

		Integer count = qrtzTriggerLogDAO.getRecQrtzTriggerLogCount(tableName, triggerBeginTime, triggerEndTime, jobId, jobCode, executeStatus);
		List<JobTriggerAppLogVo> qrtzTriggerLogList = qrtzTriggerLogDAO.getRecQrtzTriggerLogList(tableName, triggerBeginTime, triggerEndTime, jobId, jobCode, executeStatus);

		resultModel.setCode(CommonMessage.OK_CODE);
		resultModel.setMessage(String.valueOf(count));
		resultModel.setData(qrtzTriggerLogList);
	}

	/*
     * 将时间转换为时间戳
     */
	public static Long dateToStamp(String s) throws ParseException{
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = simpleDateFormat.parse(s);
		return date.getTime();

	}
}
