package com.giveu.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.giveu.dao.AppInfoDAO;
import com.giveu.job.common.info.CommonMessage;
import com.giveu.job.common.vo.ResultModel;
import com.giveu.job.common.vo.AppVo;
import com.giveu.service.AppInfoService;
import com.giveu.util.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 秘钥服务类
 * Created by fox on 2018/9/5.
 */
@Service
public class AppInfoServiceImpl implements AppInfoService {

	public static Logger logger = LoggerFactory.getLogger(AppInfoServiceImpl.class);

	public static final String GIVEU_APP_SECRET_INFO = "GIVEU_APP_SECRET_INFO";

	@Autowired
	AppInfoDAO appInfoDAO;

	@Autowired
	RedisUtil redisUtil;

	@Override
	public void cacheAppInfoToRedis(ResultModel resultModel) {

		List<AppVo> list = appInfoDAO.getAppInfoList();
		String appInfoJson = JSONObject.toJSONString(list);
		boolean b = redisUtil.set(GIVEU_APP_SECRET_INFO, appInfoJson);
		if (b) {
			resultModel.setCode(CommonMessage.OK_CODE);
			resultModel.setMessage(CommonMessage.OK_DESC);
			logger.info("Cache the AppKeyInfo to Redis is done.");
		} else {
			resultModel.setCode(CommonMessage.ERROR_CODE);
			resultModel.setMessage(CommonMessage.ERROR_DESC);
			logger.info("Cache the AppKeyInfo to Redis is failed.");
		}
	}
}
