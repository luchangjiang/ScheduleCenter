package com.giveu.controller;

import com.giveu.job.common.vo.ResultModel;
import com.giveu.service.AppInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by fox on 2018/9/5.
 */
@RestController
@RequestMapping("/appkey")
public class AppKeyController {

	@Autowired
	AppInfoService appInfoService;

	@RequestMapping("/cache")
	public ResultModel cacheToRedis() {
		ResultModel resultModel = new ResultModel();
		appInfoService.cacheAppInfoToRedis(resultModel);
		return resultModel;
	}
}
