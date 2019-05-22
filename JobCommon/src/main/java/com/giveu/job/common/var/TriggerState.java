package com.giveu.job.common.var;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by fox on 2018/9/27.
 */
public class TriggerState {

	public static Map<String, String> triggerStateMap = new HashMap<>();
	static {
		triggerStateMap.put("WAITING", "等待");
		triggerStateMap.put("PAUSED", "暂停");
		triggerStateMap.put("ACQUIRED", "正常执行");
		triggerStateMap.put("BLOCKED", "阻塞");
		triggerStateMap.put("ERROR", "错误");
	}
}

