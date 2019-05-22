package com.giveu.component;

import com.giveu.common.httpclient.component.HttpComponent;
import com.giveu.job.common.vo.ResultModel;
import com.giveu.mail.component.MailSendComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 健康检测器-轮训策略
 * Created by fox on 2019.01.12.
 */
@Component
public class JobHealthRunner implements ApplicationRunner {

	private static Logger logger = LoggerFactory.getLogger(JobHealthRunner.class);

	public volatile static long last = System.currentTimeMillis();

//	private static final int HEALTH_TIME = 30000;

	@Value("${health.time}")
	private int healthTime;

	@Value("${spring.profiles.active}")
	String active;

	@Autowired
	HttpComponent httpComponent;

	@Autowired
	MailSendComponent mailSendComponent;

	private static final String NOTICE_URL = "http://10.11.13.26/message/push/form";

	String mailList = "hanbin@dafycredit.com,zhangyu6@dafycredit.com";

	@Override
	public void run(ApplicationArguments applicationArguments) throws Exception {

		if (!"prod".equals(active)) {
			return;
		}

		logger.info("Check the health of client site start...");
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				long current = System.currentTimeMillis();

				if ((current - last) > healthTime) {

					Map<String, String> map = new HashMap<>();
					String receiver = "538306,536989";
					String category = "心跳检测预警";
					String message = "调度中心异常报警尽快检测调度中心是否运行正常";
					map.put("receiver", receiver);
					map.put("category", category);
					map.put("message", message);

					String errorInfo = "调度中心心跳异常，尽快处理";
					logger.error(errorInfo);
					logger.error("The JobCenter is exceptional! Check the server as soon as possible.");

					String info = mailSendComponent.send(null, "EmailEngine", null, errorInfo, "心跳检测预警（调度中心）", null, null, mailList, null).toString();
					logger.info(info);
					try {
						ResultModel resultModel = httpComponent.doPostFormResultObject(NOTICE_URL, ResultModel.class, map);
						System.out.println(resultModel);
					} catch (IOException e) {
						e.printStackTrace();
					}

				}
			}
		}, 0, 5000);


	}

}
