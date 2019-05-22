package com.giveu.log.scan.util;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;

/**
 * RabbitMQ 链接工具组件
 * Created by fox on 2018/8/19.
 */
@Component
@PropertySource(value = { "classpath:application-log.properties" })
public class ConnectionUtil {

	public static Logger logger = LoggerFactory.getLogger(ConnectionUtil.class);

	@Value("${mq.host}")
	private String host;

	@Value("${mq.port}")
	private Integer port;

	@Value("${mq.virtual.host}")
	private String virtualHost;

	@Value("${mq.username}")
	private String username;

	@Value("${mq.password}")
	private String password;

	private boolean flag = true;

	private Connection connection = null;

	@PostConstruct
	public void createConnection() {
		synchronized (ConnectionUtil.class) {
			if (flag) {
				ConnectionFactory factory = new ConnectionFactory();
				factory.setHost(host);
				factory.setPort(port);
				factory.setVirtualHost(virtualHost);
				factory.setUsername(username);
				factory.setPassword(password);

				try {
					connection = factory.newConnection();
				} catch (IOException e) {
					e.printStackTrace();
				}

				if (connection != null) {
					logger.debug("RabbitMQ connection create success...");
				} else {
					logger.error("RabbitMQ connection create failed...");
				}
				flag = false;
			}
		}
	}

	public Connection getConnection() {
		return connection;
	}
//	public Connection getConnection() {
//		ConnectionFactory factory = new ConnectionFactory();
//		factory.setHost(host);
//		factory.setPort(port);
//		factory.setVirtualHost(virtualHost);
//		factory.setUsername(username);
//		factory.setPassword(password);
//
//		try {
//			connection = factory.newConnection();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return connection;
//	}


	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}
}
