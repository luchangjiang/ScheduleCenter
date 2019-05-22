package com.giveu.service.impl;

import com.giveu.job.common.vo.ResultModel;
import com.giveu.model.ItemModel;
import com.giveu.service.MonitorOpenAccountService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 静默开户监控实现类
 * Created by fox on 2018/8/3.
 */
@Service
public class MonitorOpenAccountServiceImpl extends MonitorBaseImpl implements MonitorOpenAccountService {

	// 日志
	public static Logger logger = LoggerFactory.getLogger(MonitorOpenAccountServiceImpl.class);

	// 静默开户监控代码
	private static final String JOB_CODE = "0005";

	// 静默开户等待
	private static final String OPEN_ACCOUNT_WAIT = "openAccountWait";
	// 静默开户失败
	private static final String OPEN_ACCOUNT_FAILED = "openAccountFailed";

	public MonitorOpenAccountServiceImpl() {
		super.jobCode = JOB_CODE;
	}

	@Override
	public synchronized void handle(ResultModel resultModel) {
		resultModel.setCode(OK_CODE);
		List<ItemModel> itemModelList = getItemModelList();

		List<Long> openAccountWaitList = dafySales2DAO.getOpenAccountWaitList();
		List<Long> openAccountFailedList = dafySales2DAO.getOpenAccountFailedList();

		Integer openAccountWait = openAccountWaitList.size();
		Integer openAccountFailed = openAccountFailedList.size();

		if (openAccountWait != null && openAccountWait > 0) {
			checkItem(itemModelList, OPEN_ACCOUNT_WAIT, openAccountWait, resultModel);
			String info = "静默开户等待处理超时异常，ID号：" + StringUtils.join(openAccountWaitList, ", ");
			logger.error(info);
		}
		if (openAccountFailed != null && openAccountFailed > 0) {
			checkItem(itemModelList, OPEN_ACCOUNT_FAILED, openAccountFailed, resultModel);
			String info = "静默开户失败处理超时异常，ID号：" + StringUtils.join(openAccountFailedList, ", ");
			logger.error(info);
		}
	}
}
