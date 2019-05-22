package com.stylefeng.guns.modular.system.component;

import com.stylefeng.guns.modular.system.service.IDataSyncService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 健康检测器-轮训策略
 * Created by fox on 2019.01.12.
 */
@Component
public class DataSyncRunner implements ApplicationRunner {

	private static Logger logger = LoggerFactory.getLogger(DataSyncRunner.class);

	@Autowired
	IDataSyncService dataSyncService;

	public volatile static long last = System.currentTimeMillis();

	@Value("${spring.profiles.active}")
	String active;

	@Override
	public void run(ApplicationArguments applicationArguments) throws Exception {

		if (!"bak".equals(active)) {
			return;
		}

		logger.info("Sync the App&JobInfo to bak...");
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {


				synchronized (this) {
					Long start = System.currentTimeMillis();
					logger.info("SyncInfo start...");
					dataSyncService.syncAppInfo();
					dataSyncService.syncJobInfo();
					Long leadTime = System.currentTimeMillis() - start;
					logger.info("SyncInfo end... LeadTime: " + leadTime);
				}



			}
		}, 0, 10000);


	}

}
