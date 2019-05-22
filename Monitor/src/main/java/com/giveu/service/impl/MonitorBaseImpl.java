package com.giveu.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.giveu.job.common.var.Symbolic;
import com.giveu.dao.mysql.JobCenterDAO;
import com.giveu.dao.oracle.DafySales2DAO;
import com.giveu.dao.redis.RedisUtil;
import com.giveu.entity.MonitorLog;
import com.giveu.entity.MonitorObject;
import com.giveu.job.common.util.CommonUtil;
import com.giveu.job.common.vo.ResultModel;
import com.giveu.model.ContractActiveModel;
import com.giveu.model.Guardian;
import com.giveu.model.ItemModel;
import com.giveu.utils.EmailUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Calendar;
import java.util.List;

/**
 * 监控基类
 * Created by fox on 2018/7/25.
 */
public class MonitorBaseImpl {

	// 日志
	public static Logger logger = LoggerFactory.getLogger(MonitorBaseImpl.class);

	@Autowired
	@SuppressWarnings("all")
	DafySales2DAO dafySales2DAO;


	@Autowired
	@SuppressWarnings("all")
	JobCenterDAO jobCenterDAO;

	@Autowired
	RedisUtil redisUtil;

	// 错误代码
	static final Integer ERROR_CODE = 500;

	// 成功代码
	static final Integer OK_CODE = 200;

	String jobCode;

	long expireTime;

	boolean isOutOfBounds(ItemModel itemModel, int value) {
		int bound = itemModel.getValue();
		String symbolic = itemModel.getCompare();

		if (Symbolic.EQUAL.equals(symbolic)) {
			return value == bound;
		}
		if (Symbolic.NOT_EQUAL.equals(symbolic)) {
			return value != bound;
		}
		if (Symbolic.GREATER_THAN.equals(symbolic)) {
			return value > bound;
		}
		if (Symbolic.LESS_THAN.equals(symbolic)) {
			return value < bound;
		}
		if (Symbolic.GREATER_THAN_OR_EQUAL.equals(symbolic)) {
			return value >= bound;
		}
		if (Symbolic.LESS_THAN_OR_EQUAL.equals(symbolic)) {
			return value <= bound;
		}
		return false;
	}

	List<ItemModel> getItemModelList() {
		MonitorObject monitorObject = jobCenterDAO.getMonitorObjectByCode(jobCode);
		String policySettingsJson = monitorObject.getObjPolicySettings();
		JSONObject jsonObject = JSONObject.parseObject(policySettingsJson);
		ContractActiveModel contractActiveModel = JSONObject.toJavaObject(jsonObject, ContractActiveModel.class);
		List<ItemModel> itemModelList = contractActiveModel.getItems();
		return itemModelList;
	}

	int addMonitorLog(String info) {
		MonitorObject monitorObject = jobCenterDAO.getMonitorObjectByCode(jobCode);
		MonitorLog monitorLog = new MonitorLog();
		monitorLog.setId(CommonUtil.getUUID32());
		monitorLog.setObjId(monitorObject.getId());
		monitorLog.setObjName(monitorObject.getObjName());
		monitorLog.setObjCode(monitorObject.getObjCode());
		monitorLog.setObjDesc(monitorObject.getObjDesc());
		monitorLog.setObjPolicySettings(monitorObject.getObjPolicySettings());
		monitorLog.setObjReceiverSettings(monitorObject.getObjReceiverSettings());
		monitorLog.setLogContext(info);
		sendEmail(info, monitorLog.getObjName());
		return jobCenterDAO.addMonitorLog(monitorLog);
	}

	void sendEmail(String info, String objName) {
		MonitorObject monitorObject = jobCenterDAO.getMonitorObjectByCode(jobCode);
		String objReceiverSettings = monitorObject.getObjReceiverSettings();
		List<Guardian> guardianList = JSONObject.parseArray(objReceiverSettings, Guardian.class);
		StringBuilder sb = new StringBuilder();
		for (Guardian guardian : guardianList) {
			sb.append(guardian.getEmail());
			sb.append(";");
		}
		String emailList = sb.toString();
		String mailSubject = objName + "预警";
		boolean b = EmailUtil.sendMail(emailList, mailSubject, info);
		if (b) {
			logger.info("Sending email is successful...");
		}else {
			logger.error("Sending email is failing...");
		}
	}

	boolean isReachedTime(int hourOfDay, int minute) {
		Calendar ncalendar = Calendar.getInstance();
		if (hourOfDay == ncalendar.get(Calendar.HOUR_OF_DAY) && minute <= ncalendar.get(Calendar.MINUTE)) {
			return true;
		}
		return false;
	}

	void checkItem(List<ItemModel> itemModelList, String item, Integer value, ResultModel resultModel) {
		boolean b = isEmptyBatch(itemModelList, item, value, resultModel);
		if (b) {
			String info = "必要条件为空";
			logger.info(info);
			resultModel.setCode(ERROR_CODE);
			resultModel.setMessage(info);
			return;
		}
		for (ItemModel itemModel : itemModelList) {
			if (item.equals(itemModel.getKey())) {
				if (isOutOfBounds(itemModel, value)) {
					String info = "Warning: 阈值限制异常！ " + itemModel.getName() + " " + Symbolic.symbolicMap.get(itemModel.getCompare()) + " 限定数: " + itemModel.getValue() + " 当前数: " + value;
					logger.info(info);
					addMonitorLog(info);
					resultModel.setCode(ERROR_CODE);
					resultModel.setMessage(info);
				}
			}
		}
	}

	public static boolean isEmptyBatch(Object... objects){
		for (Object object : objects) {
			if(object == null)
				return true;
		}
		return false;
	}

	public static boolean isNotEmptyBatch(Object... objects){
		for (Object object : objects) {
			if(object == null)
				return false;
		}
		return true;
	}

}
