package com.giveu;

import com.giveu.common.CommonVar;
import com.giveu.job.common.entity.QrtzTriggers;
import com.giveu.service.JobAppTriggerService;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 健康检测器-轮训策略
 * Created by fox on 2018/6/26.
 */
@Component
public class JobHealthApplicationRunner implements ApplicationRunner {

	private static Logger logger = LoggerFactory.getLogger(JobHealthApplicationRunner.class);

	@Autowired
	private DiscoveryClient discoveryClient;

	@Autowired
	private JobAppTriggerService jobAppTriggerService;

	@Autowired
	@Qualifier("Scheduler")
	private Scheduler scheduler;

	private static List<ServiceInstance> clientList = new ArrayList<>();

	@Override
	public void run(ApplicationArguments applicationArguments) throws Exception {

		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				List<ServiceInstance> list = discoveryClient.getInstances(CommonVar.JOB_EXECUTOR);
				List<QrtzTriggers> qzErrorList = jobAppTriggerService.getErrorTriggerList();
				if (qzErrorList != null && qzErrorList.size() > 0) {
					resumJob(qzErrorList);
				}
				if (list != null && list.size() > 0) {
					if (list.size() > clientList.size()) {
						clientList = list;
					} else if (list.size() == clientList.size()) {
						// TODO: 2018/7/3  
					} else {
						String address = getDisconnectAddress(clientList, list);
						logger.error("Client site disconnection:{}",address);
						List<QrtzTriggers> qzList = jobAppTriggerService.getActiveTriggerList();
						resumJob(qzList);
						clientList = list;
					}
				}
			}
		}, 0, CommonVar.EXECUTOR_HEALTH_PERIOD_TIME_MS);

		logger.info("Check the health of client site start...");
	}

	private void resumJob(List<QrtzTriggers> qzList) {
		for (QrtzTriggers q : qzList) {
			try {
				scheduler.resumeJob(JobKey.jobKey(q.getTriggerName(), q.getTriggerGroup()));
			} catch (SchedulerException e) {
				e.printStackTrace();
			}
		}
	}

	public String getDisconnectAddress(List<ServiceInstance> clientList, List<ServiceInstance> list) {
		Map<String, Boolean> map = new HashMap<>();
		for (ServiceInstance s : clientList) {
			map.put(s.getHost()+":"+s.getPort(),true);
		}
		for (ServiceInstance s : list) {
			if (map.get(s.getHost() + ":" + s.getPort()) != true) {
				return s.getHost() + ":" + s.getPort();
			}
		}
		return null;
	}
}
