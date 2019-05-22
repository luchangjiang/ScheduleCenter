package com.giveu.util;

import org.springframework.boot.context.embedded.EmbeddedServletContainerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 服务信息工具类
 * Created by fox on 2018/6/27.
 */
@Configuration
public class ServiceInfoUtil implements ApplicationListener<EmbeddedServletContainerInitializedEvent> {
	private static EmbeddedServletContainerInitializedEvent event;

	@Override
	public void onApplicationEvent(EmbeddedServletContainerInitializedEvent event) {
		ServiceInfoUtil.event = event;
	}

	/**
	 * 获取运行时端口
	 * @return
	 */
	public static int getPort() {
		Assert.notNull(event, "event not null.");
		int port = event.getEmbeddedServletContainer().getPort();
		Assert.state(port != -1, "端口号获取失败");
		return port;
	}

	/**
	 * 获取运行时IP
	 * @return
	 */
	public static String getHost(){
		String host = "";
		try {
			host = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return host;
	}

	/**
	 * 获取完整地址
	 * @return
	 */
	public static String getAddress() {
		return getHost() + ":" + getPort();
	}
}
