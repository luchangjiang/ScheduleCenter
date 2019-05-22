package com.giveu.service.impl;

import com.giveu.job.common.util.DateUtil;
import com.giveu.job.common.vo.ResultModel;
import com.giveu.model.ItemModel;
import com.giveu.service.MonitorCheckOffService;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 代扣监控服务类
 * Created by fox on 2018/7/21.
 */
@Service
public class MonitorCheckOffServiceImpl extends MonitorBaseImpl implements MonitorCheckOffService {

	// 日志
	public static Logger logger = LoggerFactory.getLogger(MonitorCheckOffServiceImpl.class);


	/************************* Redis key ****************************/
	// 执行步骤KEY
	private static String MONITOR_XX_CHECK_OFF_STEP = "MONITOR_XX_CHECK_OFF_STEP";
	// 代扣数量
	private static String MONITOR_XX_CHECK_OFF_COUNT = "MONITOR_XX_CHECK_OFF_COUNT";
	// 获得已现行合同的重试次数
	private static String MONITOR_XX_GET_CHECK_OFF_REPEAT_COUNT = "MONITOR_XX_GET_CHECK_OFF_REPEAT_COUNT";

	// 监控代码
	private static final String JOB_CODE = "0002";

	// 总数上限
	private static final String CHECK_OFF_COUNT_MAX = "checkOffCountMax";
	// 总数下限
	private static final String CHECK_OFF_COUNT_MIN = "checkOffCountMin";

	// 成功的数量
	private static final String CHECK_OFF_SUCCEEDED_COUNT = "succeededCount";

	// 未处理
	private static final String CHECK_OFF_UNTREATED_COUNT = "untreatedCount";

	// 重试次数
	private static final Integer REPEAT_COUNT = 24;

	// 错误代码
	private static final Integer ERROR_CODE = 500;

	private static final Integer OK_CODE = 200;



	// 未开始
	private int STEP0 = 0;

	// 第一步在代扣任务开始之前拿到今日的代扣数量
	private int STEP1 = 1;

	// 第二步在代扣开始后拿到已经成功现行的数据，如果第一次没拿到会重复N次
	private int STEP2 = 2;

	// 第三步在代扣预期完成的时间拿到已经成功的数据，与设定的阈值做对比，符合条件就报警
	private int STEP3 = 3;

	// 第四步在合同完成后比较长的时间再次去查询一遍
	private int STEP4 = 4;

	// 执行完毕
	private int STEP5 = 5;


	// 有效时间（单位：秒）
	private int EXPIRE_TIME = 20 * 60 * 60;

	// 第一步执行的时间
	private int STPE1_HOUR_OF_DAY = 9;
	private int STPE1_MINUTE = 45;

	// 第二步执行的时间
	private int STPE2_HOUR_OF_DAY = 10;
	private int STPE2_MINUTE = 10;

	// 第三步执行的时间
	private int STPE3_HOUR_OF_DAY = 15;
	private int STPE3_MINUTE = 10;

	// 第四步执行的时间
	private int STPE4_HOUR_OF_DAY = 23;
	private int STPE4_MINUTE = 10;

	public MonitorCheckOffServiceImpl() {
		super.jobCode = JOB_CODE;
	}

//	void checkItem(List<ItemModel> itemModelList, String item, Integer value, ResultModel resultModel) {
//		for (ItemModel itemModel : itemModelList) {
//			if (item.equals(itemModel.getKey())) {
//				if (isOutOfBounds(itemModel, value)) {
//					String info = "Warning: 阈值限制异常！ " + itemModel.getName() + Symbolic.symbolicMap.get(itemModel.getCompare()) + " 限定数: " + itemModel.getValue() + " 当前数: " + value;
//					logger.info(info);
//					addMonitorLog(info);
//					resultModel.setCode(ERROR_CODE);
//					resultModel.setMessage(info);
//				}
//			}
//		}
//	}

	@Override
	public synchronized void handle(ResultModel resultModel) {

		// 给redis key 加上批次
		MONITOR_XX_CHECK_OFF_STEP = "MONITOR_XX_CHECK_OFF_STEP_" + DateUtil.getStringDateShortYYMMDD();
		MONITOR_XX_CHECK_OFF_COUNT = "MONITOR_XX_CHECK_OFF_COUNT_" + DateUtil.getStringDateShortYYMMDD();
		MONITOR_XX_GET_CHECK_OFF_REPEAT_COUNT = "MONITOR_XX_GET_CHECK_OFF_REPEAT_COUNT_" + DateUtil.getStringDateShortYYMMDD();

		int step = NumberUtils.toInt(String.valueOf(redisUtil.get(MONITOR_XX_CHECK_OFF_STEP)), 1);

		resultModel.setCode(OK_CODE);
		resultModel.setMessage("Waiting...");

		// 监控结束：重试次数过多，或者最终步骤执行完毕但是合同现行没有成功执行完毕
		if (step == STEP0) {
			String info = "代扣监控计划结束或指定时间内未处理任何一条代扣";
			resultModel.setCode(ERROR_CODE);
			resultModel.setMessage(info);
			logger.info(info);
			return;
		}

		if (step == STEP1) {
			if (!isReachedTime(STPE1_HOUR_OF_DAY, STPE1_MINUTE)) {
				String info = "等待获取未代扣数据...";
				logger.info(info);
				resultModel.setCode(OK_CODE);
				resultModel.setMessage(info);
				return;
			}
			int checkOffCount = dafySales2DAO.getCheckOffCount();
			if (checkOffCount <= 0) {
				String info = "Error: 未代扣数据为空!";
				logger.info(info);
				addMonitorLog(info);
				resultModel.setCode(ERROR_CODE);
				resultModel.setMessage(info);
				return;
			}
			List<ItemModel> itemModelList = getItemModelList();
			checkItem(itemModelList, CHECK_OFF_COUNT_MAX, checkOffCount, resultModel);
			checkItem(itemModelList, CHECK_OFF_COUNT_MIN, checkOffCount, resultModel);

			Boolean b = redisUtil.set(MONITOR_XX_CHECK_OFF_COUNT, checkOffCount, EXPIRE_TIME);
			if (b) {
				String info = "Notice: 获得未代扣数据总数完成.";
				logger.info(info);
				resultModel.setCode(OK_CODE);
				resultModel.setMessage(info);
				redisUtil.set(MONITOR_XX_CHECK_OFF_STEP, STEP2, EXPIRE_TIME);
			}
			return;
		}

		if (step == STEP2) {
			if (!isReachedTime(STPE2_HOUR_OF_DAY, STPE2_MINUTE)) {
				String info = "检测是否在处理未代扣数据，等待中...";
				logger.info(info);
				resultModel.setCode(OK_CODE);
				resultModel.setMessage(info);
				return;
			}
			int checkOffSucceededCount = dafySales2DAO.getCheckOffSucceededCount();
			if (checkOffSucceededCount > 0) {
				redisUtil.set(MONITOR_XX_CHECK_OFF_STEP, STEP3, EXPIRE_TIME);
				String info = "Notice: 代扣已经开始处理... 进入下一步...";
				logger.info(info);
				resultModel.setCode(OK_CODE);
				resultModel.setMessage(info);

			} else {
				int count = NumberUtils.toInt(String.valueOf(redisUtil.get(MONITOR_XX_GET_CHECK_OFF_REPEAT_COUNT)), 0);
				if (count >= REPEAT_COUNT) {
					String info = "Error: 检查代扣成功列表，但是返回空数据. 总共重试次数: " + REPEAT_COUNT;
					addMonitorLog(info);
					resultModel.setCode(ERROR_CODE);
					resultModel.setMessage(info);
					redisUtil.set(MONITOR_XX_CHECK_OFF_STEP, STEP0, EXPIRE_TIME);
					redisUtil.set(MONITOR_XX_GET_CHECK_OFF_REPEAT_COUNT, 0, EXPIRE_TIME);
				}
				int tempCount = ++count;
				String info = "Warning: 没有任何一条代扣数据执行. 将重复检测. 第N次: " + tempCount;
				logger.info(info);
				redisUtil.set(MONITOR_XX_GET_CHECK_OFF_REPEAT_COUNT, tempCount, EXPIRE_TIME);
				resultModel.setCode(OK_CODE);
				resultModel.setMessage(info);
			}
			return;
		}


		if (step == STEP3) {

			// 检查是否到达第三步执行的时间，如果没到时间则不执行
			if (!isReachedTime(STPE3_HOUR_OF_DAY, STPE3_MINUTE)) {
				String info = "Step3. 等待检测代扣是否已经完成...";
				logger.info(info);
				resultModel.setCode(OK_CODE);
				resultModel.setMessage(info);
				return;
			}

			int checkOffCount = dafySales2DAO.getCheckOffCount();
			List<ItemModel> itemModelList = getItemModelList();
			checkItem(itemModelList, CHECK_OFF_UNTREATED_COUNT, checkOffCount, resultModel);
			redisUtil.set(MONITOR_XX_CHECK_OFF_STEP, STEP4, EXPIRE_TIME);

			return;
		}

		if (step == STEP4) {

			// 检查是否到达第四步执行的时间，如果没到时间则不执行
			if (!isReachedTime(STPE4_HOUR_OF_DAY, STPE4_MINUTE)) {
				String info = "Step4. 等待检测代扣是否已经完成...";
				logger.info(info);
				resultModel.setCode(OK_CODE);
				resultModel.setMessage(info);
				return;
			}

			int checkOffCount = dafySales2DAO.getCheckOffCount();
			List<ItemModel> itemModelList = getItemModelList();
			checkItem(itemModelList, CHECK_OFF_UNTREATED_COUNT, checkOffCount, resultModel);
			redisUtil.set(MONITOR_XX_CHECK_OFF_STEP, STEP5, EXPIRE_TIME);

		}

		// 第一步在合同现行之前拿到已注册的数据
		if (step == STEP5) {
			resultModel.setCode(OK_CODE);
			resultModel.setMessage("代扣完成...");
			return;
		}
	}
}
