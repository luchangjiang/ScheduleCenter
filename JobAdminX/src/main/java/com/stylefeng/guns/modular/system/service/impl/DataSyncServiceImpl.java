package com.stylefeng.guns.modular.system.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.giveu.job.common.vo.AppUserVo;
import com.giveu.job.common.vo.AppVo;
import com.giveu.job.common.vo.JobTriggerAppVo;
import com.giveu.job.common.vo.ResultModel;
import com.stylefeng.guns.modular.system.dao.JobAppTriggerDao;
import com.stylefeng.guns.modular.system.service.IAppListService;
import com.stylefeng.guns.modular.system.service.IDataSyncService;
import com.stylefeng.guns.modular.system.service.IJobListService;
import jodd.http.HttpRequest;
import jodd.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by fox on 2019/1/23.
 */
@Service
public class DataSyncServiceImpl implements IDataSyncService {

	@Autowired
	JobAppTriggerDao dao;


	@Autowired
	IJobListService jobListService;

	@Autowired
	IAppListService appListService;

	@Override
	public void syncAppInfo() {
		HttpRequest httpRequest = HttpRequest.post("http://10.11.13.30:8888/appList/get/list");
		HttpResponse response = httpRequest.send();
		String result = response.bodyText();

		ResultModel resultModel = JSONObject.parseObject(result, ResultModel.class);

		List<AppVo> appList = null;
		if (resultModel != null && resultModel.getData() != null) {
			String dataStr = resultModel.getData().toString();
			appList = JSONObject.parseArray(dataStr, AppVo.class);
		}
		if (appList == null || appList.size() == 0) {
			return;
		}
		List<AppVo> list = dao.getAppList();

		out: for (AppVo vo : list) {
			for (AppVo avo : appList) {
				if (vo.getAppKey().equals(avo.getAppKey())) {
					if (!vo.getAppName().equals(avo.getAppName()) || !vo.getAppSecret().equals(avo.getAppSecret()) || !vo.getAppStatus().equals(avo.getAppStatus())) {
						dao.updAppByAppKey(avo);
					}
					continue out;

				}
			}
			appListService.delAppById(vo.getId());
		}

		out: for (AppVo avo : appList) {
			for (AppVo vo : list) {
				if (vo.getAppKey().equals(avo.getAppKey())) {
					continue out;
				}
			}
			AppUserVo v = new AppUserVo();
			v.setUserAccount("admin");
			v.setAppKey(avo.getAppKey());
			dao.bindAccount(v);
			dao.addAppVo(avo);
		}

	}

	@Override
	public void syncJobInfo() {

		HttpRequest httpRequest = HttpRequest.post("http://10.11.13.30:8888/jobList/get/list");
		HttpResponse response = httpRequest.send();
		String result = response.bodyText();

		ResultModel resultModel = JSONObject.parseObject(result, ResultModel.class);

		List<JobTriggerAppVo> jobList = null;
		if (resultModel != null && resultModel.getData() != null) {
			String dataStr = resultModel.getData().toString();
			jobList = JSONObject.parseArray(dataStr, JobTriggerAppVo.class);
		}
		if (jobList == null || jobList.size() == 0) {
			return;
		}

		List<JobTriggerAppVo> list = dao.getJobList();

		out: for (JobTriggerAppVo vo : list) {
			for (JobTriggerAppVo jvo : jobList) {
				if (vo.getJobCode().equals(jvo.getJobCode())) {
					if (!vo.getCronExpression().equals(jvo.getCronExpression()) ||
							!vo.getCallbackUrl().equals(jvo.getCallbackUrl()) ||
							!vo.getResultWaitTime().equals(jvo.getResultWaitTime()) ||
							!vo.getResultUrl().equals(jvo.getResultUrl()) ||
							!vo.getJobDesc().equals(jvo.getJobDesc())) {
						jobListService.updJob(jvo);
					}
					continue out;

				}
			}
			jobListService.delJobByCode(vo.getJobCode());
		}

		out: for (JobTriggerAppVo jvo : jobList) {
			for (JobTriggerAppVo vo : list) {
				if (vo.getJobCode().equals(jvo.getJobCode())) {
					continue out;
				}
			}
			jobListService.addJob(jvo);
		}

	}
}
