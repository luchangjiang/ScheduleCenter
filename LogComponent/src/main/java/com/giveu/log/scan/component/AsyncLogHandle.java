package com.giveu.log.scan.component;

import com.giveu.log.scan.util.ConnectionUtil;
import com.giveu.log.scan.util.JsonUtil;
import com.giveu.log.scan.vo.MonitorLog;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Created by fox on 2018/8/16.
 */
@Component
public class AsyncLogHandle {

	@Autowired
	ConnectionUtil connectionUtil;

	public static Logger logger = LoggerFactory.getLogger(AsyncLogHandle.class);

	private final static String EXCHANGE_NAME = "GiveU.Monitor.Log.Exchange";
	private final static String QUEUE_NAME = "GiveU.Monitor.Log.QueueName";
	private final static String ROUTING_KEY = "GiveU.Monitor.Log.QueueName";

	@Async("executor")
	public void record(MonitorLog monitorLog) throws IOException {
		String monitorLogJson = JsonUtil.toCapitalizeJson(monitorLog);
		Connection connection = connectionUtil.getConnection();
		Channel channel = null;
		try {
			channel = connection.createChannel();
			channel.exchangeDeclare(EXCHANGE_NAME, "topic");
			channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, null, monitorLogJson.getBytes());
		} catch (Exception e) {
			connectionUtil.setFlag(true);
			connectionUtil.createConnection();

		}finally {
			if (channel != null) {
				try {
					channel.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
//		logger.debug("Send MQ: {}", monitorLogJson);
	}

}
