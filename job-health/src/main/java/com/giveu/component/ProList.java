package com.giveu.component;

import com.giveu.job.common.entity.ProInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Vector;

/**
 * Created by fox on 2019/1/19.
 */
@Component
public class ProList {

	public List<ProInfo> proInfoList = new Vector<>();

//	static {
//		proInfoList.add(new ProInfo("调度管理端","job-admin-x-1.0.0-SNAPSHOT.jar", "8888", "test", "未知"));
//		proInfoList.add(new ProInfo("数据监控服务","monitor-1.0.0-SNAPSHOT.jar", "9040", "test", "未知"));
//		proInfoList.add(new ProInfo("微信统计服务","we-cat-push-1.0.0-SNAPSHOT.jar", "9050", "test", "未知"));
//		proInfoList.add(new ProInfo("调度执行器", "job-executor-1.0.0-SNAPSHOT.jar", "9060", "test", "未知"));
//		proInfoList.add(new ProInfo("调度执行器", "job-executor-1.0.0-SNAPSHOT.jar", "9061", "test", "未知"));
//		proInfoList.add(new ProInfo("调度执行器", "job-executor-1.0.0-SNAPSHOT.jar", "9062", "test", "未知"));
//		proInfoList.add(new ProInfo("注册中心", "job-eureka-1.0-SNAPSHOT.jar", "8761", "test", "未知"));
//		proInfoList.add(new ProInfo("异步结果服务", "job-async-result-1.0.0-SNAPSHOT.jar", "8010", "test", "未知"));
//	}

	@Value("${spring.profiles.active}")
	String active;

	@PostConstruct
	void init() {

		proInfoList.add(new ProInfo("调度管理端", "job-admin-x-1.0.0-SNAPSHOT.jar", "8888", active, "未知"));
		proInfoList.add(new ProInfo("数据监控服务", "monitor-1.0.0-SNAPSHOT.jar", "9040", active, "未知"));
		proInfoList.add(new ProInfo("微信统计服务", "we-cat-push-1.0.0-SNAPSHOT.jar", "9050", active, "未知"));
		proInfoList.add(new ProInfo("调度执行器", "job-executor-1.0.0-SNAPSHOT.jar", "9060", "", "未知"));
		proInfoList.add(new ProInfo("调度执行器", "job-executor-1.0.0-SNAPSHOT.jar", "9061", "", "未知"));
		proInfoList.add(new ProInfo("调度执行器", "job-executor-1.0.0-SNAPSHOT.jar", "9062", "", "未知"));
		proInfoList.add(new ProInfo("注册中心", "job-eureka-1.0.0-SNAPSHOT.jar", "8761", active, "未知"));
		proInfoList.add(new ProInfo("异步结果服务", "job-async-result-1.0.0-SNAPSHOT.jar", "8010", active, "未知"));
	}

	public List<ProInfo> getProInfoList() {
		return proInfoList;
	}

	public void setProInfoList(List<ProInfo> proInfoList) {
		this.proInfoList = proInfoList;
	}
}
