package com.giveu.service;

import com.giveu.job.common.vo.MonitorObjectVo;
import com.giveu.job.common.vo.ResultModel;

import javax.servlet.http.HttpServletRequest;

/**
 * 监控服务类
 * Created by fox on 2018/7/21.
 */
public interface MonitorService {

	/**
	 * 获取监控对象列表
	 * @param request
	 * @param resultModel
	 */
	void list(HttpServletRequest request, ResultModel resultModel);

	/**
	 * 获取监控日志列表
	 * @param request
	 * @param resultModel
	 */
	void logList(HttpServletRequest request, ResultModel resultModel);

	/**
	 * 获取监控对象VO
	 * @param id
	 * @return
	 */
	MonitorObjectVo getMonitorById(String id);

	/**
	 * 更新监控配置
	 * @param id
	 * @param objPolicySettings
	 * @return
	 */
	int updObjPolicySettings(String id, String objPolicySettings);

}
