package com.stylefeng.guns.modular.system.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.giveu.job.common.vo.MonitorLogVo;
import com.giveu.job.common.vo.ResultModel;
import com.stylefeng.guns.modular.system.service.IMonitorLogListService;
import jodd.http.HttpMultiMap;
import jodd.http.HttpRequest;
import jodd.http.HttpResponse;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 监控日志Dao
 *
 * @author fengshuonan
 * @Date 2018-07-23 10:54:28
 */
@Service
public class MonitorLogListServiceImpl implements IMonitorLogListService {

	private static final int OK = 200;

	private static final String dataUrl = "http://localhost:9040/";

	@Override
	public void list(HttpServletRequest request, List<Map<String, Object>> list) throws IOException {

		String beginTime = request.getParameter("beginTime");
		String endTime = request.getParameter("endTime");
		String objName = request.getParameter("objName");

		HttpRequest httpRequest = HttpRequest.post(dataUrl+"monitor/log/list");
		HttpMultiMap multiMap = httpRequest.query();

		multiMap.add("beginTime", beginTime);
		multiMap.add("endTime", endTime);
		multiMap.add("objName", objName);

		HttpResponse response = httpRequest.send();

		ObjectMapper mapper = new ObjectMapper();
		String result = response.bodyText();
		ResultModel resultModel = mapper.readValue(result, ResultModel.class);
		List<MonitorLogVo> monitorLogVoList = null;
		if (resultModel.getCode() == OK) {
			monitorLogVoList = (ArrayList<MonitorLogVo>)resultModel.getData();

		}

		for (int i = 0; i < monitorLogVoList.size(); i++) {
			Map<String, Object> map = new HashMap<>();
			map.put("id", ((Map)monitorLogVoList.get(i)).get("id"));
			map.put("objId", ((Map)monitorLogVoList.get(i)).get("objId"));
			map.put("objName", ((Map)monitorLogVoList.get(i)).get("objName"));
			map.put("objCode", ((Map)monitorLogVoList.get(i)).get("objCode"));
			map.put("objDesc", ((Map)monitorLogVoList.get(i)).get("objDesc"));
			map.put("logCreateTime", ((Map)monitorLogVoList.get(i)).get("logCreateTime"));
			map.put("objPolicySettings", ((Map)monitorLogVoList.get(i)).get("objPolicySettings"));
			map.put("objReceiverSettings", ((Map)monitorLogVoList.get(i)).get("objReceiverSettings"));
			map.put("logContext", ((Map)monitorLogVoList.get(i)).get("logContext"));
			map.put("logExtend", ((Map)monitorLogVoList.get(i)).get("logExtend"));
			list.add(map);
		}

	}
}
