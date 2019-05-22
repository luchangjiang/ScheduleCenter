package com.giveu.component;

import com.giveu.common.CommonVar;
import com.giveu.dao.AppDAO;
import com.giveu.dao.JobDAO;
import com.giveu.job.common.entity.QrtzAppInfo;
import com.giveu.job.common.entity.QrtzJobExtend;
import com.giveu.job.common.info.CommonMessage;
import com.giveu.job.common.util.MD5Util;
import com.giveu.job.common.util.ObjectUtil;
import com.giveu.job.common.var.Secret;
import com.giveu.job.common.vo.ResultModel;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by fox on 2019/1/7.
 */
@Service
public class JobCheckComponent {

	private static Logger logger = LoggerFactory.getLogger(JobCheckComponent.class);

	@Autowired
	JobDAO jobDAO;

	@Autowired
	AppDAO appDAO;

	// 新增任务的URL地址
	private static final String ADD_NEW_JOB_URL = "/job/add";

	// 删除任务的URL地址
	private static final String DEL_JOB_URL = "/job/del/code";

	// 更新任务的URL地址
	private static final String UPD_JOB_URL = "/job/upd/code";

	// 暂停任务的URL地址
	private static final String PAUSE_JOB_URL = "/job/pause/code";

	// 恢复任务的URL地址
	private static final String RESUME_JOB_URL = "/job/resume/code";

	private static final String TRIGGER_JOB_URL = "/job/trigger/code";

	private static final String RESULT_UPDATE_JOB_URL = "/job/result/update";

	public ResultModel check(HttpServletRequest request) {

		ResultModel resultModel = new ResultModel();

		resultModel.setCode(CommonMessage.ERROR_CODE);
		resultModel.setMessage(CommonMessage.ERROR_DESC);

		Map<String, String> map = new HashMap<>();
		StringBuilder sb = new StringBuilder();

		// 判断是否为新增任务的请求
		if (request.getServletPath ().equals(ADD_NEW_JOB_URL)) {
			// 获取提交过来新增JOB的参数内容
			String jobName = request.getParameter("jobName");
			String jobCode = request.getParameter("jobCode");
			String jobDesc = request.getParameter("jobDesc");
			String callbackUrl = request.getParameter("callbackUrl");
			String cronExpression = request.getParameter("cronExpression");


			String jobType = request.getParameter("jobType");
			String resultWaitTime = request.getParameter("resultWaitTime");
			String resultUrl = request.getParameter("resultUrl");
			String isPause = request.getParameter("isPause");


			map.put("jobName",jobName);
			map.put("jobCode",jobCode);
			map.put("jobDesc",jobDesc);
			map.put("callbackUrl",callbackUrl);
			map.put("cronExpression",cronExpression);

			map.put("jobType",jobType);
			map.put("isPause",isPause);

			Integer AsyncType = 2;
			if (AsyncType.equals(NumberUtils.toInt(jobType, 1))) {
				map.put("resultWaitTime", resultWaitTime);
				map.put("resultUrl", resultUrl);
			}
		}

		// 判断是否为删除任务的请求
		else if (request.getServletPath ().equals(DEL_JOB_URL)) {
			String jobCode = request.getParameter("jobCode");
			map.put("jobCode",jobCode);
		}

		// 判断是否为删除任务的请求
		else if (request.getServletPath ().equals(RESULT_UPDATE_JOB_URL)) {
			String sessionId = request.getParameter("sessionId");
			String executeStatus = request.getParameter("executeStatus");
			map.put("sessionId",sessionId);
			map.put("executeStatus",executeStatus);
		}

		// 判断是否为更新任务的请求
		else if (request.getServletPath ().equals(UPD_JOB_URL)) {
			String jobCode = request.getParameter("jobCode");
			String jobDesc = request.getParameter("jobDesc");
			String callbackUrl = request.getParameter("callbackUrl");
			String cronExpression = request.getParameter("cronExpression");


			String jobType = request.getParameter("jobType");
			String resultWaitTime = request.getParameter("resultWaitTime");
			String resultUrl = request.getParameter("resultUrl");


			map.put("jobCode",jobCode);
			map.put("jobDesc",jobDesc);
			map.put("callbackUrl",callbackUrl);
			map.put("cronExpression",cronExpression);

			map.put("jobType",jobType);

			Integer AsyncType = 2;
			if (AsyncType.equals(NumberUtils.toInt(jobType, 1))) {
				map.put("resultWaitTime", resultWaitTime);
				map.put("resultUrl", resultUrl);
			}
		}

		// 判断是否为暂停任务的请求
		else if (request.getServletPath ().equals(PAUSE_JOB_URL)) {
			String jobCode = request.getParameter("jobCode");
			map.put("jobCode",jobCode);
		}

		// 判断是否为恢复任务的请求
		else if (request.getServletPath ().equals(RESUME_JOB_URL)) {
			String jobCode = request.getParameter("jobCode");
			map.put("jobCode",jobCode);
		}

		else if (request.getServletPath ().equals(TRIGGER_JOB_URL)) {
			String jobCode = request.getParameter("jobCode");
			map.put("jobCode",jobCode);
		}

		else {
			resultModel.setCode(CommonMessage.NO_AUTHORITY_ERROR_CODE);
			resultModel.setMessage(CommonMessage.NO_AUTHORITY_ERROR_DESC);
			return resultModel;
		}

		// 获取提交过来的公共参数内容
		String xGiveuSign = request.getHeader("xGiveuSign");
		String xGiveuAppKey = request.getHeader("xGiveuAppKey");
		String xGiveuTimestamp = request.getHeader("xGiveuTimestamp");


		Boolean isEmpty = ObjectUtil.isEmptyBatch(xGiveuSign, xGiveuAppKey, xGiveuTimestamp);
		if (isEmpty) {
			resultModel.setCode(CommonMessage.PAR_NOT_NULL_CODE);
			resultModel.setMessage(CommonMessage.PAR_NOT_NULL_DESC);
			return resultModel;
		}

		QrtzJobExtend qrtzJobExtend = jobDAO.getJobExtendByJobCode(map.get("jobCode"));

		if (qrtzJobExtend != null && (!CommonVar.ADMIN_APP_KEY.equals(xGiveuAppKey)) && (!xGiveuAppKey.equals(qrtzJobExtend.getAppKey()))) {
			resultModel.setCode(CommonMessage.NO_AUTHORITY_ERROR_CODE);
			resultModel.setMessage(CommonMessage.NO_AUTHORITY_ERROR_DESC);
			return resultModel;
		}

//		map.put("xGiveuSign",xGiveuSign);
		map.put("xGiveuAppKey",xGiveuAppKey);
		map.put("xGiveuTimestamp",xGiveuTimestamp);

		// 遍历请求参数，判断是否有空字段，且生成部分原始加密字符串
		for (Map.Entry<String, String> entry : map.entrySet()) {
			if (StringUtils.isBlank(entry.getValue())) {
				continue;
			}
			sb.append(entry.getKey() + entry.getValue());
		}

		// 判断传过来的时间戳是否类型错误
		Long timestamp = NumberUtils.toLong(xGiveuTimestamp, 0);
		if (timestamp == 0) {
			resultModel.setCode(CommonMessage.PAR_TYPE_ERROR_CODE);
			resultModel.setMessage(CommonMessage.PAR_TYPE_ERROR_DESC);
			return resultModel;
		}

		String originalx = "";
		String originaly = "";
		String parStr = sb.toString();

		QrtzAppInfo qrtzAppInfo = appDAO.getAppInfoByAppKey(xGiveuAppKey);
		if (qrtzAppInfo == null) {
			originalx = parStr + Secret.APP_SECRET;
		}else {
			originalx = parStr + Secret.APP_SECRET;
			originaly = parStr + qrtzAppInfo.getAppSecret();
		}

		String encrypted1 = MD5Util.sign(originalx);
		String encrypted2 = MD5Util.sign(originaly);
		if (!encrypted1.equals(xGiveuSign) && !encrypted2.equals(xGiveuSign)) {
			resultModel.setCode(CommonMessage.SIGN_ERROR_CODE);
			resultModel.setMessage(CommonMessage.SIGN_ERROR_DESC);
			logger.info(CommonMessage.SIGN_ERROR_DESC);
			return resultModel;
		}
		logger.info("Sign check success...");
		resultModel.setCode(CommonMessage.OK_CODE);
		resultModel.setMessage(CommonMessage.OK_DESC);
		return resultModel;
	}
}
