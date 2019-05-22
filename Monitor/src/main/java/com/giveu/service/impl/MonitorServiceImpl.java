package com.giveu.service.impl;

import com.giveu.job.common.info.CommonMessage;
import com.giveu.dao.mysql.JobCenterDAO;
import com.giveu.dao.oracle.DafySales2DAO;
import com.giveu.dao.redis.RedisUtil;
import com.giveu.entity.MonitorLog;
import com.giveu.entity.MonitorObject;
import com.giveu.job.common.util.DateUtil;
import com.giveu.job.common.vo.MonitorLogVo;
import com.giveu.job.common.vo.MonitorObjectVo;
import com.giveu.job.common.vo.ResultModel;
import com.giveu.service.MonitorService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 监控服务类
 * Created by fox on 2018/7/21.
 */
@Service("monitorService")
public class MonitorServiceImpl implements MonitorService {

	@Autowired
	@SuppressWarnings("all")
	DafySales2DAO dafySales2DAO;


	@Autowired
	@SuppressWarnings("all")
	JobCenterDAO jobCenterDAO;

	@Autowired
	RedisUtil redisUtil;

	@Override
	public void list(HttpServletRequest request, ResultModel resultModel) {

		String objName = request.getParameter("objName");
		if (StringUtils.isBlank(objName)) {
			objName = null;
		}
		MonitorObjectVo monitorObjectVo = new MonitorObjectVo();
		monitorObjectVo.setObjName(objName);
		List<MonitorObject> list = jobCenterDAO.list(monitorObjectVo);
		List<MonitorObjectVo> listVo = new ArrayList<>();

		for (MonitorObject m : list) {
			MonitorObjectVo v = new MonitorObjectVo();
			BeanUtils.copyProperties(m,v);
			Date d = m.getObjCreateTime();
			v.setObjCreateTime(DateUtil.dateToStrLong(d));
			listVo.add(v);
		}

		resultModel.setCode(CommonMessage.OK_CODE);
		resultModel.setMessage(CommonMessage.OK_DESC);
		resultModel.setData(listVo);

	}

	@Override
	public void logList(HttpServletRequest request, ResultModel resultModel) {
		String beginTime = request.getParameter("beginTime");
		String endTime = request.getParameter("endTime");
		String objName = request.getParameter("objName");

		Long logBeginTime = null;
		Long logEndTime = null;

		if (StringUtils.isNotBlank(beginTime)) {
			try {
				logBeginTime = DateUtil.dateToStamp(beginTime);

			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if (StringUtils.isNotBlank(endTime)) {
			try {
				logEndTime = DateUtil.dateToStamp(endTime) + (86400000 - 1);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		if (StringUtils.isBlank(objName)) {
			objName = null;
		}

		List<MonitorLog> list = jobCenterDAO.logList(objName, logBeginTime, logEndTime);
		List<MonitorLogVo> listVo = new ArrayList<>();
		for (MonitorLog m : list) {
			MonitorLogVo v = new MonitorLogVo();
			BeanUtils.copyProperties(m,v);
			Date d = m.getLogCreateTime();
			v.setLogCreateTime(DateUtil.dateToStrLong(d));
			listVo.add(v);
		}
		resultModel.setCode(CommonMessage.OK_CODE);
		resultModel.setMessage(CommonMessage.OK_DESC);
		resultModel.setData(listVo);

	}

	@Override
	public MonitorObjectVo getMonitorById(String id) {
		MonitorObject monitorObject = jobCenterDAO.getMonitorById(id);
		MonitorObjectVo monitorObjectVo = new MonitorObjectVo();
		BeanUtils.copyProperties(monitorObject, monitorObjectVo);
		return monitorObjectVo;
	}

	@Override
	public int updObjPolicySettings(String id, String objPolicySettings) {
		return jobCenterDAO.updObjPolicySettings(id, objPolicySettings);
	}


}
