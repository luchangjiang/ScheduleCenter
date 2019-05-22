package com.giveu.controller;

import com.giveu.job.common.vo.MonitorObjectVo;
import com.giveu.job.common.vo.ResultModel;
import com.giveu.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 监控核心控制
 * Created by fox on 2018/7/21.
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/monitor")
public class MonitorController {

	@Autowired
	MonitorService monitorService;

	@Autowired
	MonitorCreditActiveService monitorCreditActiveService;

	@Autowired
	MonitorCheckOffService monitorCheckOffService;

	@Autowired
	MonitorReleaseCreditService monitorReleaseCreditService;

	@Autowired
	MonitorInstalmentService monitorInstalmentService;

	@Autowired
	MonitorOpenAccountService monitorOpenAccountService;

	/*************************核心业务监控接口********************************/
	/**
	 * 合同现行监控
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/credit/active/handle")
	public ResultModel creditActiveHandle() throws Exception {
		ResultModel resultModel = new ResultModel();
		monitorCreditActiveService.handle(resultModel);
		return resultModel;
	}

	/**
	 * 代扣监控
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/check/off/handle")
	public ResultModel checkOffHandle() throws Exception {
		ResultModel resultModel = new ResultModel();
		monitorCheckOffService.handle(resultModel);
		return resultModel;
	}

	/**
	 * 放款监控
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/release/credit/handle")
	public ResultModel releaseCreditHandle() throws Exception {
		ResultModel resultModel = new ResultModel();
		monitorReleaseCreditService.handle(resultModel);
		return resultModel;
	}

	/**
	 * 提前还款监控
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/instalment/handle")
	public ResultModel instalmentHandle() throws Exception {
		ResultModel resultModel = new ResultModel();
		monitorInstalmentService.handle(resultModel);
		return resultModel;
	}

	/**
	 * 静默开户监控
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/open/account/handle")
	public ResultModel openAccountHandle() throws Exception {

//		int i = 1/0;
		ResultModel resultModel = new ResultModel();
		monitorOpenAccountService.handle(resultModel);
		return resultModel;
	}



	/*****************************管理端接口***********************************/
	/**
	 * 查询监控对象列表
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/list")
	public ResultModel list(HttpServletRequest request) throws Exception {
		ResultModel resultModel = new ResultModel();
		monitorService.list(request, resultModel);
		return resultModel;
	}

	/**
	 * 查询监控日志列表
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/log/list")
	public ResultModel logList(HttpServletRequest request) throws Exception {
		ResultModel resultModel = new ResultModel();
		monitorService.logList(request, resultModel);
		return resultModel;
	}

	/**
	 * 根据id获取监控对象
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/get/id")
	public MonitorObjectVo getMonitorById(@RequestParam(value = "id") String id) throws Exception {
		return monitorService.getMonitorById(id);
	}

	/**
	 * 修改监控阈值配置
	 * @param id
	 * @param objPolicySettings
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/upd/sitting")
	public int updObjPolicySettings(@RequestParam(value = "id") String id, @RequestParam(value = "objPolicySettings") String objPolicySettings) throws Exception {
		return monitorService.updObjPolicySettings(id,objPolicySettings);
	}




}
