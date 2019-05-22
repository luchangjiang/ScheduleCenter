package com.giveu.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.giveu.dao.mysql.JobCenterDAO;
import com.giveu.dao.oracle.DafySales2DAO;
import com.giveu.job.common.var.Symbolic;
import com.giveu.job.common.vo.ResultModel;
import com.giveu.dao.redis.RedisUtil;
import com.giveu.job.common.util.DateUtil;
import com.giveu.model.ItemModel;
import com.giveu.service.MonitorCreditActiveService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * 合同现行监控服务类
 * Created by fox on 2018/7/21.
 */
@Service
public class MonitorCreditActiveServiceImpl extends MonitorBaseImpl implements MonitorCreditActiveService {

	// 日志
	public static Logger logger = LoggerFactory.getLogger(MonitorCreditActiveServiceImpl.class);


	/************************* Redis key ****************************/
	// 执行步骤KEY
	private String MONITOR_XX_CONTRACT_ACTIVE_STEP = "";
	// 注册合同ID
	private String MONITOR_XX_REGISTER_ID_LIST = "";
	// 获得已现行合同的重试次数
	private String MONITOR_XX_GET_CREDIT_COUNT = "";


	// 合同现行监控代码
	private static final String JOB_CODE = "0001";

	// 合同上限
	private static final String CREDIT_AMOUNT_MAX = "creditAmountMax";
	// 合同下限
	private static final String CREDIT_AMOUNT_MIN = "creditAmountMin";
	// 耗时
	private static final String LEAD_TIME = "leadTime";

	// 重试次数
	private static final Integer REPEAT_COUNT = 24;


	// 未开始
	private int STEP0 = 0;

	// 第一步在合同现行之前拿到已注册的数据
	private int STEP1 = 1;

	// 第二步在合同开始后拿到已经成功现行的数据，如果第一次没拿到会重复3次
	private int STEP2 = 2;

	// 第三步在合同预期完成的时间拿到已经成功的数据，做对比，如果成功合同ID和注册合同ID数量且内容相等那么就是成功，反之则报警
	private int STEP3 = 3;

	// 第四步在合同完成后比较长的时间再次去查询一遍，如果第三步没成功而现在查询后对比成功那么就判断只是处理时间慢并不是有失败
	private int STEP4 = 4;

	// 执行完毕
	private int STEP5 = 5;


	// 有效时间（单位：秒）
	private long EXPIRE_TIME = 40 * 60;

	private static int hour = 0;

	// 第一步执行的时间
	private int STPE1_HOUR_OF_DAY = hour;
	private int STPE1_MINUTE = 40;

	// 第二步执行的时间
	private int STPE2_HOUR_OF_DAY = hour;
	private int STPE2_MINUTE = 48;

	// 第三步执行的时间
	private int STPE3_HOUR_OF_DAY = hour;
	private int STPE3_MINUTE = 50;

	// 第四步执行的时间
	private int STPE4_HOUR_OF_DAY = hour;
	private int STPE4_MINUTE = 58;

	@Autowired
	@SuppressWarnings("all")
	DafySales2DAO dafySales2DAO;


	@Autowired
	@SuppressWarnings("all")
	JobCenterDAO jobCenterDAO;

	@Autowired
	RedisUtil redisUtil;

	public MonitorCreditActiveServiceImpl() {
		super.jobCode = JOB_CODE;
		super.expireTime = EXPIRE_TIME;
	}

	@Override
	public synchronized void handle(ResultModel resultModel) {

		// 给redis key 加上批次
		MONITOR_XX_CONTRACT_ACTIVE_STEP = "MONITOR_XX_CONTRACT_ACTIVE_STEP_" + DateUtil.getStringDateShortYYMMDD();
		MONITOR_XX_REGISTER_ID_LIST = "MONITOR_XX_REGISTER_ID_LIST_" + DateUtil.getStringDateShortYYMMDD();
		MONITOR_XX_GET_CREDIT_COUNT = "MONITOR_XX_GET_CREDIT_COUNT_" + DateUtil.getStringDateShortYYMMDD();

		// 获取当前执行的步骤
		int step = NumberUtils.toInt(String.valueOf(redisUtil.get(MONITOR_XX_CONTRACT_ACTIVE_STEP)), 1);

		// 设置默认返回数据
		resultModel.setCode(OK_CODE);
		resultModel.setMessage("Waiting...");


		// 监控结束：重试次数过多，或者最终步骤执行完毕但是合同现行没有成功执行完毕
		if (step == STEP0) {
			String info = "监控计划完成，但是合同现行未完成或者指定时间内未处理任何一条数据";
			resultModel.setCode(ERROR_CODE);
			resultModel.setMessage(info);
			logger.info(info);
			return;
		}

		// 第一步在合同现行之前拿到已注册的数据
		if (step == STEP1) {
			if (!isReachedTime(STPE1_HOUR_OF_DAY, STPE1_MINUTE)) {
				String info = "Step1. 执行时间：" + STPE1_HOUR_OF_DAY + ": " + STPE1_MINUTE + " 正在等待获取注册数据...";
				logger.info(info);
				resultModel.setCode(OK_CODE);
				resultModel.setMessage(info);
				return;
			}
			List<Integer> registerIdList = dafySales2DAO.getContractRegisterOnlyId();
			if (registerIdList == null || registerIdList.size() == 0) {
				String info = "Error: 合同注册数量为空!";
				logger.info(info);
				addMonitorLog(info);
				resultModel.setCode(ERROR_CODE);
				resultModel.setMessage(info);
				return;
			}
			List<ItemModel> itemModelList = getItemModelList();
			for (ItemModel itemModel : itemModelList) {
				if (CREDIT_AMOUNT_MAX.equals(itemModel.getKey()) || CREDIT_AMOUNT_MIN.equals(itemModel.getKey())) {
					if (isOutOfBounds(itemModel, registerIdList.size())) {
						String info = "Warning: "+ Symbolic.symbolicMap.get(itemModel.getCompare()) +"阈值. " + itemModel.getName() + " 限定: " + itemModel.getValue() + " 当前: " + registerIdList.size();
						logger.info(info);
						addMonitorLog(info);
						resultModel.setCode(ERROR_CODE);
						resultModel.setMessage(info);
					}
				}
			}

			String json = JSONObject.toJSONString(registerIdList);
			Boolean b = redisUtil.set(MONITOR_XX_REGISTER_ID_LIST, json, EXPIRE_TIME);
			if (b) {
				String info = "Notice: 获取合同注册列表完成.";
				logger.info("获取到的注册ID " + json);
				logger.info(info);
				resultModel.setCode(OK_CODE);
				resultModel.setMessage(info);
				redisUtil.set(MONITOR_XX_CONTRACT_ACTIVE_STEP, STEP2, EXPIRE_TIME);
			}
			return;
		}

		// 第二步在合同开始后拿到已经成功现行的数据，如果第一次没拿到会重复N次
		if (step == STEP2) {
			if (!isReachedTime(STPE2_HOUR_OF_DAY, STPE2_MINUTE)) {
				String info = "Step2. 执行时间：" + STPE2_HOUR_OF_DAY + ": " + STPE2_MINUTE + " 正在等待验证是否已经在处理...";
				logger.info(info);
				resultModel.setCode(OK_CODE);
				resultModel.setMessage(info);
				return;
			}
			List<Integer> activeIdList = dafySales2DAO.getContractActiveOnlyId();
			if (activeIdList != null && activeIdList.size() > 0) {
				redisUtil.set(MONITOR_XX_CONTRACT_ACTIVE_STEP, STEP3, EXPIRE_TIME);
				String info = "Notice: 合同现行已经开始...";
				logger.info(info);
				resultModel.setCode(OK_CODE);
				resultModel.setMessage(info);

			} else {
				int count = NumberUtils.toInt(String.valueOf(redisUtil.get(MONITOR_XX_GET_CREDIT_COUNT)), 0);
				if (count >= REPEAT_COUNT) {
					String info = "Error: 检查合同已现行列表，但是返回为空，重复检查次数: " + REPEAT_COUNT;
					addMonitorLog(info);
					resultModel.setCode(ERROR_CODE);
					resultModel.setMessage(info);
					redisUtil.set(MONITOR_XX_CONTRACT_ACTIVE_STEP, STEP0, EXPIRE_TIME);
					redisUtil.set(MONITOR_XX_GET_CREDIT_COUNT, 0, EXPIRE_TIME);
				}
				int tempCount = ++count;
				String info = "Warning: 没有任何一个合同完成现行. 将重复检测. 重复第N次: " + tempCount;
				logger.info(info);
				redisUtil.set(MONITOR_XX_GET_CREDIT_COUNT, tempCount, EXPIRE_TIME);
				resultModel.setCode(OK_CODE);
				resultModel.setMessage(info);
			}
			return;
		}


		// 第三步在合同预期完成的时间拿到已经成功的数据，做对比，如果成功合同ID和注册合同ID数量且内容相等那么就是成功，反之则报警
		if (step == STEP3) {

			// 检查是否到达第三步执行的时间，如果没到时间则不执行
			if (!isReachedTime(STPE3_HOUR_OF_DAY, STPE3_MINUTE)) {
				String info = "Step3. 执行时间：" + STPE3_HOUR_OF_DAY + ": " + STPE3_MINUTE + " 正在等待获取是否完成...";
				logger.info(info);
				resultModel.setCode(OK_CODE);
				resultModel.setMessage(info);
				return;
			}

			List<Integer> notRegister = new ArrayList<>();
			List<Integer> notActive = new ArrayList<>();
			compareContract(notRegister, notActive, resultModel);


			if (notActive.size() == 0) {
				String info = "Step3. 合同现行全部执行完成. ";
				logger.info(info);
				resultModel.setCode(OK_CODE);
				resultModel.setMessage(info);
				redisUtil.set(MONITOR_XX_CONTRACT_ACTIVE_STEP, STEP5, EXPIRE_TIME);
				checkLeadTime(resultModel);
			} else {
				String info = "合同现行在制定时间内未完成。进入下一步...";
				logger.info(info);
				addMonitorLog(info);
				resultModel.setCode(ERROR_CODE);
				resultModel.setMessage(info);
				redisUtil.set(MONITOR_XX_CONTRACT_ACTIVE_STEP, STEP4, EXPIRE_TIME);
			}
			return;
		}

		// 第四步在合同完成后比较长的时间再次去查询一遍，如果第三步没成功而现在查询后对比成功那么就判断只是处理时间慢并不是有失败
		if (step == STEP4) {
			// 检查是否到达第四步执行的时间，如果没到时间则不执行
			if (!isReachedTime(STPE4_HOUR_OF_DAY, STPE4_MINUTE)) {
				String info = "Step4. 执行时间：" + STPE4_HOUR_OF_DAY + ": " + STPE4_MINUTE + " 正在等待获取是否完成...";
				logger.info(info);
				resultModel.setCode(OK_CODE);
				resultModel.setMessage(info);
				return;
			}

			List<Integer> notRegister = new ArrayList<>();
			List<Integer> notActive = new ArrayList<>();
			compareContract(notRegister, notActive, resultModel);

			if (notActive.size() == 0) {
				String info = "Step 4. 合同现行全部完成";
				logger.info(info);
				resultModel.setCode(OK_CODE);
				resultModel.setMessage(info);
				redisUtil.set(MONITOR_XX_CONTRACT_ACTIVE_STEP, STEP5, EXPIRE_TIME);
				checkLeadTime(resultModel);
			} else {
				String info = "Error: Step 4. 监控计划完成, 但是合同现行未完成！";
				addMonitorLog(info);
				resultModel.setCode(ERROR_CODE);
				resultModel.setMessage(info);
				logger.error(info);
				redisUtil.set(MONITOR_XX_CONTRACT_ACTIVE_STEP, STEP0, EXPIRE_TIME);
			}
		}
		// 第一步在合同现行之前拿到已注册的数据
		if (step == STEP5) {
			resultModel.setCode(OK_CODE);
			resultModel.setMessage("合同现行完成...");
			return;
		}
	}

	private void checkLeadTime(ResultModel resultModel) {
		Calendar ncalendar = Calendar.getInstance();
		int leadTimeMinutes = ncalendar.get(Calendar.MINUTE) - STPE2_MINUTE;
		List<ItemModel> itemModelList = getItemModelList();
		for (ItemModel itemModel : itemModelList) {
			if (LEAD_TIME.equals(itemModel.getKey())) {
				if (isOutOfBounds(itemModel, leadTimeMinutes)) {
					String info = "Warning: 耗时过长: " + leadTimeMinutes;
					logger.info(info);
					addMonitorLog(info);
					resultModel.setCode(ERROR_CODE);
					resultModel.setMessage(info);
				}
			}
		}
	}

	void compareContract(List<Integer> notRegister, List<Integer> notActive, ResultModel resultModel) {
		Object obj = redisUtil.get(MONITOR_XX_REGISTER_ID_LIST);
		List<Integer> activeIdList = dafySales2DAO.getContractActiveOnlyId();
		if (obj != null && activeIdList != null && activeIdList.size() > 0) {
			JSONArray jsonArray = JSONObject.parseArray(obj.toString());
			Integer[] registerList = jsonArray.toArray(new Integer[jsonArray.size()]);
			Integer[] activeList = activeIdList.toArray(new Integer[activeIdList.size()]);
			compare(registerList, activeList, notRegister); // 合同现行成功有 但是 Redis 中没有
			compare(activeList, registerList, notActive); // Redis 中有 但是合同现行中没有

			String infoMessage = "";
			if (notRegister.size() > 0) {
				String unregistered = StringUtils.join(notRegister, ",");
				String info = " Unregistered: " + unregistered;
				logger.info(info);
				infoMessage += info;
			}
			if (notActive.size() > 0) {
				String nonactivated = StringUtils.join(notActive, ",");
				String info = " Nonactivated: " + nonactivated;
				logger.info(info);
				infoMessage += info;
			}

			if (StringUtils.isNotBlank(infoMessage)) {
				String error = "Error: ";
				infoMessage = error + infoMessage;
				resultModel.setCode(ERROR_CODE);
				resultModel.setMessage(infoMessage);
			}
		}
	}

	public static void compare(Integer[] t1, Integer[] t2, List<Integer> list) {
		List<Integer> list1 = Arrays.asList(t1);
		for (Integer t : t2) {
			if (!list1.contains(t)) {
				list.add(t);
			}
		}
	}
}
