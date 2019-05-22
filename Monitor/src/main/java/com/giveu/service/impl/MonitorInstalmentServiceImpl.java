package com.giveu.service.impl;

import com.giveu.job.common.vo.ResultModel;
import com.giveu.model.ItemModel;
import com.giveu.service.MonitorInstalmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 提前还款实现类
 * Created by fox on 2018/8/3.
 */
@Service
public class MonitorInstalmentServiceImpl extends MonitorBaseImpl implements MonitorInstalmentService {

	// 提前还款日志
	public static Logger logger = LoggerFactory.getLogger(MonitorInstalmentServiceImpl.class);

	// 提前还款监控代码
	private static final String JOB_CODE = "0004";

	// 提前代扣
	private static final String PAY_TYPE_2_COUNT = "payType2Count";
	// 15天还款
	private static final String PAY_TYPE_4_COUNT = "payType4Count";
	// 提前取消
	private static final String PAY_TYPE_6_COUNT = "payType6Count";



	public MonitorInstalmentServiceImpl() {
		super.jobCode = JOB_CODE;
	}

	@Override
	public synchronized void handle(ResultModel resultModel) {
		resultModel.setCode(OK_CODE);
		List<ItemModel> itemModelList = getItemModelList();


		Integer payType2Count = dafySales2DAO.getInstalmentCountByPayType2();
		Integer payType4Count = dafySales2DAO.getInstalmentCountByPayType4();
		Integer payType6Count = dafySales2DAO.getInstalmentCountByPayType6();


		if (payType2Count != null && payType2Count > 0) {
			checkItem(itemModelList, PAY_TYPE_2_COUNT, payType2Count, resultModel);
		}
		if (payType4Count != null && payType4Count > 0) {
			checkItem(itemModelList, PAY_TYPE_4_COUNT, payType4Count, resultModel);
		}
		if (payType6Count != null && payType6Count > 0) {
			checkItem(itemModelList, PAY_TYPE_6_COUNT, payType6Count, resultModel);
		}


	}
}
