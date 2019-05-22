package com.giveu.service.impl;

import com.giveu.job.common.vo.ResultModel;
import com.giveu.model.ItemModel;
import com.giveu.service.MonitorReleaseCreditService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 放款监控实现类
 * Created by fox on 2018/8/3.
 */
@Service
public class MonitorReleaseCreditServiceImpl extends MonitorBaseImpl implements MonitorReleaseCreditService {

	// 日志
	public static Logger logger = LoggerFactory.getLogger(MonitorReleaseCreditServiceImpl.class);

	// 放款监控代码
	private static final String JOB_CODE = "0003";
	// 现金贷未放款
	private static final String LARGE_COUNT = "largeCount";
	// 零花钱未放款
	private static final String PETTY_COUNT = "pettyCount";
	// 商户未放款
	private static final String MERCH_COUNT = "merchCount";
	// 默认逾期天数
	private static final Integer DEFAULT_DAYS_OVERDUE = 30;


	// 逾期天数
	private static final String DAYS_OVERDUE = "daysOverdue";



	public MonitorReleaseCreditServiceImpl() {
		super.jobCode = JOB_CODE;
	}

	@Override
	public synchronized void handle(ResultModel resultModel) {
		resultModel.setCode(OK_CODE);
		List<ItemModel> itemModelList = getItemModelList();

		int daysOverdue = DEFAULT_DAYS_OVERDUE;
		for (ItemModel itemModel : itemModelList) {
			if (DAYS_OVERDUE.equals(itemModel.getKey())) {
				daysOverdue = itemModel.getValue();
				break;
			}
		}

		Integer largeCount = dafySales2DAO.getLargeReleaseCreditCount(daysOverdue);
		Integer pettyCount = dafySales2DAO.getPettyReleaseCreditCount(daysOverdue);
		Integer merchCount = dafySales2DAO.getMerchReleaseCreditCount(daysOverdue);

		if (largeCount != null && largeCount > 0) {
			checkItem(itemModelList, LARGE_COUNT, largeCount, resultModel);
		}
		if (pettyCount != null && pettyCount > 0) {
			checkItem(itemModelList, PETTY_COUNT, pettyCount, resultModel);
		}
		if (merchCount != null && merchCount > 0) {
			checkItem(itemModelList, MERCH_COUNT, merchCount, resultModel);
		}




	}
}
