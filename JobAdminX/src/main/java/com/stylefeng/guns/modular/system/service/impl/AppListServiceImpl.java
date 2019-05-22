package com.stylefeng.guns.modular.system.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.giveu.job.common.entity.QrtzAppInfo;
import com.giveu.job.common.info.CommonMessage;
import com.giveu.job.common.util.CommonUtil;
import com.giveu.job.common.util.StringUtil;
import com.giveu.job.common.util.ValidUtil;
import com.giveu.job.common.var.CommonVar;
import com.giveu.job.common.vo.AppUserVo;
import com.giveu.job.common.vo.AppVo;
import com.giveu.job.common.vo.ResultModel;
import com.stylefeng.guns.core.shiro.ShiroKit;
import com.stylefeng.guns.modular.system.dao.JobAppTriggerDao;
import com.stylefeng.guns.modular.system.dao.UserMgrDao;
import com.stylefeng.guns.modular.system.service.IAppListService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 应用列表Dao
 *
 * @author fengshuonan
 * @Date 2018-07-03 17:28:13
 */
@Service
public class AppListServiceImpl implements IAppListService {

	private static final int OK = 200;

//	private static final String dataUrl = "http://localhost:8765/";
	private static final String dataUrl = "http://localhost:9060/";

	@SuppressWarnings("all")
	@Autowired
	UserMgrDao userMgrDao;

	@Autowired
	private JobAppTriggerDao jobAppTriggerDao;


	@Override
	public void list(HttpServletRequest request, List<Map<String, Object>> list) throws IOException {

		String appName = request.getParameter("appName");
		String appKey = request.getParameter("appKey");
		String account = ShiroKit.getUser().getAccount();

		Integer appStatus = NumberUtils.toInt(request.getParameter("appStatus"), 0);

		List<AppVo> qrtzAppInfoList = jobAppTriggerDao.getAppInfoList(appName, appKey, appStatus, account);

		if (qrtzAppInfoList == null) {
			return;
		}

		for (AppVo vo : qrtzAppInfoList) {
			Map<String, Object> map = new HashMap<>();
			map.put("id", vo.getId());
			map.put("appName", vo.getAppName());
			map.put("appDesc", vo.getAppDesc());
			map.put("appStatus", vo.getAppStatus());
			map.put("appStatusDesc", vo.getAppStatusDesc());
			map.put("appCreateTime", vo.getAppCreateTime());
			map.put("appUpdateTime", vo.getAppUpdateTime());
			map.put("appAuthor", vo.getAppAuthor());
			map.put("appEmail", vo.getAppEmail());
			map.put("appPhone", vo.getAppPhone());
			map.put("appKey", vo.getAppKey());
			map.put("appSecret", vo.getAppSecret());
			list.add(map);
		}

	}

	@Override
	public ResultModel list() {
		ResultModel resultModel = new ResultModel();
		resultModel.setCode(CommonMessage.OK_CODE);
		resultModel.setMessage(CommonMessage.OK_DESC);
		List<AppVo> list = jobAppTriggerDao.getAppList();
		resultModel.setData(list);
		return resultModel;
	}

	@Override
	public String getListJson() {

		String account = ShiroKit.getUser().getAccount();
		List<AppVo> qrtzAppInfoList = jobAppTriggerDao.getAppInfoList(null, null, 0, account);

		ResultModel resultModel = new ResultModel();
		resultModel.setCode(CommonMessage.OK_CODE);
		resultModel.setMessage(CommonMessage.OK_DESC);
		resultModel.setData(qrtzAppInfoList);
		return JSONObject.toJSONString(resultModel);


	}

	@Override
	public String getListJsonByAccount() {

		String account = ShiroKit.getUser().getAccount();
		List<AppVo> qrtzAppInfoList = jobAppTriggerDao.getListJsonByAccount(account);

		ResultModel resultModel = new ResultModel();
		resultModel.setCode(CommonMessage.OK_CODE);
		resultModel.setMessage(CommonMessage.OK_DESC);
		resultModel.setData(qrtzAppInfoList);
		return JSONObject.toJSONString(resultModel);
	}

	@Override
	@Transactional
	public ResultModel addApp(AppVo vo) {

		ResultModel resultModel = new ResultModel();

		String appName = vo.getAppName();
		String appKey = vo.getAppKey();
		String appSecret = vo.getAppSecret();
		Integer appStatus = vo.getAppStatus();

		String account = ShiroKit.getUser().getAccount();
		if (StringUtils.isBlank(account)) {
			resultModel.setCode(CommonMessage.NO_AUTHORITY_ERROR_CODE);
			resultModel.setMessage(CommonMessage.NO_AUTHORITY_ERROR_DESC);
			return resultModel;
		}

		boolean isEmptyBatch = StringUtil.isEmptyBatch(appName, appKey, appSecret);
		if (isEmptyBatch) {
			resultModel.setCode(CommonMessage.PAR_NOT_NULL_CODE);
			resultModel.setMessage(CommonMessage.PAR_NOT_NULL_DESC);
			return resultModel;
		}

		boolean b = ValidUtil.validField(appKey);
		if (!b) {
			resultModel.setCode(CommonMessage.PAR_TYPE_ERROR_CODE);
			resultModel.setMessage(CommonMessage.PAR_TYPE_ERROR_DESC + " appKey: " + appKey);
			return resultModel;
		}

		QrtzAppInfo appInfo = jobAppTriggerDao.getAppInfoByAppKey(appKey);
		if (appInfo != null) {
			resultModel.setCode(CommonMessage.APP_KEY_ALREADY_ERROR_CODE);
			resultModel.setMessage(CommonMessage.APP_KEY_ALREADY_ERROR_DESC);
			return resultModel;
		}

		QrtzAppInfo qrtzAppInfo = new QrtzAppInfo();
		qrtzAppInfo.setAppName(appName);
		qrtzAppInfo.setAppKey(appKey);
		qrtzAppInfo.setAppSecret(appSecret);
		qrtzAppInfo.setAppStatus(appStatus);
		qrtzAppInfo.setId(CommonUtil.getUUID32());
		int i = jobAppTriggerDao.addApp(qrtzAppInfo);
		jobAppTriggerDao.createTriggerLogTable(CommonVar.LOG_TABLE_NAME_PREFIX + appKey);
		if (i == 1) {
			AppUserVo appUserVo = new AppUserVo();
			appUserVo.setUserAccount(account);
			appUserVo.setAppKey(appKey);
			jobAppTriggerDao.bindAccount(appUserVo);
			if (!account.equals("admin")) {
				AppUserVo v = new AppUserVo();
				v.setUserAccount("admin");
				v.setAppKey(appKey);
				jobAppTriggerDao.bindAccount(v);
			}
			resultModel.setCode(CommonMessage.OK_CODE);
			resultModel.setMessage(CommonMessage.OK_DESC);
		}
		return resultModel;
	}

	@Override
	public Integer updAppById(AppVo appVo) throws IOException {
//		HttpRequest httpRequest = HttpRequest.post(dataUrl + "job/app/upd/id");
//		HttpMultiMap multiMap = httpRequest.query();
//		String appName = appVo.getAppName();
//		Integer appStatus = appVo.getAppStatus();
//		String appKey = appVo.getAppKey();
//		String appSecret = appVo.getAppSecret();
//		String id = appVo.getId();
//		multiMap.add("id", id);
//		multiMap.add("appName", appName);
//		multiMap.add("appStatus", appStatus);
//		multiMap.add("appKey", appKey);
//		multiMap.add("appSecret", appSecret);
//
//		HttpResponse response = httpRequest.send();
//		ObjectMapper mapper = new ObjectMapper();
//		String result = response.bodyText();
//		ResultModel resultModel = mapper.readValue(result, ResultModel.class);
//		return resultModel.getCode();

//		String id = request.getParameter("id");
//		String appName = request.getParameter("appName");
//		String appKey = request.getParameter("appKey");
//		String appSecret = request.getParameter("appSecret");
//		Integer appStatus = NumberUtils.toInt(request.getParameter("appStatus"),0);
		QrtzAppInfo qrtzAppInfo = new QrtzAppInfo();
		qrtzAppInfo.setId(appVo.getId());
		qrtzAppInfo.setAppName(appVo.getAppName());
		qrtzAppInfo.setAppKey(appVo.getAppKey());
		qrtzAppInfo.setAppSecret(appVo.getAppSecret());
		qrtzAppInfo.setAppStatus(appVo.getAppStatus());

		return jobAppTriggerDao.updAppById(qrtzAppInfo);
	}

	@Override
	public AppVo getAppVoById(String appId) throws IOException {
//		HttpRequest httpRequest = HttpRequest.post(dataUrl + "job/app/get/id");
//		HttpMultiMap multiMap = httpRequest.query();
//		multiMap.add("appId", appId);
//		HttpResponse response = httpRequest.send();
//		ObjectMapper mapper = new ObjectMapper();
//		String result = response.bodyText();
//		AppVo appVo = mapper.readValue(result, AppVo.class);
//
//		return appVo;

		QrtzAppInfo qrtzAppInfo = jobAppTriggerDao.getQrtzAppInfoById(appId);
		if (qrtzAppInfo == null) {
			return null;
		}
		AppVo appVo = new AppVo();
		BeanUtils.copyProperties(qrtzAppInfo, appVo);
		appVo.setAppCreateTime(qrtzAppInfo.getAppCreateTime().toString());
		appVo.setAppUpdateTime(qrtzAppInfo.getAppUpdateTime().toString());
		return appVo;
	}

	@Override
	public Integer delAppById(String appId) {
//		HttpRequest httpRequest = HttpRequest.post(dataUrl + "job/app/del/id");
//		HttpMultiMap multiMap = httpRequest.query();
//		multiMap.add("appId", id);
//		HttpResponse response = httpRequest.send();
//		return null;

		QrtzAppInfo qrtzAppInfo = jobAppTriggerDao.getQrtzAppInfoById(appId);
		String appKey = qrtzAppInfo.getAppKey();
		jobAppTriggerDao.unbindAccountByAppKey(appKey);
		return jobAppTriggerDao.delAppById(appId);
	}

	@Override
	public Integer enableById(String appId) {
//		HttpRequest httpRequest = HttpRequest.post(dataUrl + "job/app/enable/id");
//		HttpMultiMap multiMap = httpRequest.query();
//		multiMap.add("appId", id);
//		HttpResponse response = httpRequest.send();
//		return null;

		return jobAppTriggerDao.enableAppById(appId);
	}

	@Override
	public Integer disableById(String appId) {
//		HttpRequest httpRequest = HttpRequest.post(dataUrl + "job/app/disable/id");
//		HttpMultiMap multiMap = httpRequest.query();
//		multiMap.add("appId", id);
//		HttpResponse response = httpRequest.send();
//		return null;

		return jobAppTriggerDao.disableAppById(appId);
	}

	@Override
	public List<String> getAccountList() {
		return userMgrDao.getAccountList();
	}

	@Override
	public String getBindAccountList(String appKey) {
//		HttpRequest httpRequest = HttpRequest.post(dataUrl + "job/get/bind/account");
//		HttpMultiMap multiMap = httpRequest.query();
//		multiMap.add("appKey", appKey);
//		HttpResponse response = httpRequest.send();
//		ObjectMapper mapper = new ObjectMapper();
//		String result = response.bodyText();
//
//		ResultModel resultModel = JSONObject.parseObject(result, ResultModel.class);
//
//		return String.valueOf(resultModel.getData());


//		resultModel.setCode(CommonMessage.OK_CODE);
//		resultModel.setData(jobAppTriggerDao.getBindAccount(appKey));

		return JSONObject.toJSONString(jobAppTriggerDao.getBindAccount(appKey));
	}

	@Override
	public void bindAccount(String accountListJson, String appKey) {
//		HttpRequest httpRequest = HttpRequest.post(dataUrl + "job/bind/account");
//		HttpMultiMap multiMap = httpRequest.query();
//		multiMap.add("accountListJson", accountListJson);
//		multiMap.add("appKey", appKey);
//		HttpResponse response = httpRequest.send();

		jobAppTriggerDao.unbindAccountByAppKey(appKey);
		List<String> list = JSONObject.parseArray(accountListJson, String.class);
		for (String account : list) {
			AppUserVo appUserVo = new AppUserVo();
			appUserVo.setAppKey(appKey);
			appUserVo.setUserAccount(account);
			jobAppTriggerDao.bindAccount(appUserVo);
		}
//		resultModel.setCode(CommonMessage.OK_CODE);
	}

}
