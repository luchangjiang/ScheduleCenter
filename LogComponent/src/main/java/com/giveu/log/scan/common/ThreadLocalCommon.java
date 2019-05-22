package com.giveu.log.scan.common;


import com.giveu.log.scan.vo.MonitorLog;

/**
 * Created by fox on 2018/8/21.
 */
public class ThreadLocalCommon {

	public static ThreadLocal<MonitorLog> threadLocalMonitorLog = new ThreadLocal<>();

}
