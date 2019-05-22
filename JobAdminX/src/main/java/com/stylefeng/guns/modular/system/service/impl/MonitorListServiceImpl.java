package com.stylefeng.guns.modular.system.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.giveu.job.common.vo.MonitorObjectVo;
import com.giveu.job.common.vo.ResultModel;
import com.stylefeng.guns.modular.system.service.IMonitorListService;
import jodd.http.HttpMultiMap;
import jodd.http.HttpRequest;
import jodd.http.HttpResponse;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 监控列表Dao
 *
 * @author fengshuonan
 * @Date 2018-07-23 10:18:15
 */
@Service
public class MonitorListServiceImpl implements IMonitorListService {

	private static final int OK = 200;

	private static final String dataUrl = "http://localhost:9040/";

	@Override
	public void list(HttpServletRequest request, List<Map<String, Object>> list) throws IOException {

		String objName = request.getParameter("objName");
		HttpRequest httpRequest = HttpRequest.post(dataUrl+"monitor/list");
		HttpMultiMap multiMap = httpRequest.query();
		multiMap.add("objName", objName);
		HttpResponse response = httpRequest.send();

		ObjectMapper mapper = new ObjectMapper();
		String result = response.bodyText();
		ResultModel resultModel = mapper.readValue(result, ResultModel.class);
		List<MonitorObjectVo> monitorObjectVoList = null;
		if (resultModel.getCode() == OK) {
			monitorObjectVoList = (ArrayList<MonitorObjectVo>)resultModel.getData();

		}

		for (int i = 0; i < monitorObjectVoList.size(); i++) {
			Map<String, Object> map = new HashMap<>();
			map.put("id", ((Map)monitorObjectVoList.get(i)).get("id"));
			map.put("objName", ((Map)monitorObjectVoList.get(i)).get("objName"));
			map.put("objDesc", ((Map)monitorObjectVoList.get(i)).get("objDesc"));
			map.put("objCode", ((Map)monitorObjectVoList.get(i)).get("objCode"));
			map.put("objStatus", ((Map)monitorObjectVoList.get(i)).get("objStatus"));
			map.put("objCreateTime", ((Map)monitorObjectVoList.get(i)).get("objCreateTime"));
			map.put("objPolicySettings", ((Map)monitorObjectVoList.get(i)).get("objPolicySettings"));
			map.put("objReceiverSettings", ((Map)monitorObjectVoList.get(i)).get("objReceiverSettings"));
			list.add(map);
		}

	}

	@Override
	public MonitorObjectVo getMonitorObjectVoById(String id) throws IOException {
		HttpRequest httpRequest = HttpRequest.post(dataUrl + "monitor/get/id");
		HttpMultiMap multiMap = httpRequest.query();
		multiMap.add("id", id);
		HttpResponse response = httpRequest.send();
		ObjectMapper mapper = new ObjectMapper();
		String result = response.bodyText();
		MonitorObjectVo monitorObjectVo = mapper.readValue(result, MonitorObjectVo.class);
		return monitorObjectVo;
	}

	@Override
	public int updObjPolicySettings(String id, String objPolicySettings) {
		HttpRequest httpRequest = HttpRequest.post(dataUrl + "monitor/upd/sitting");
		HttpMultiMap multiMap = httpRequest.query();
		multiMap.add("id", id);
		multiMap.add("objPolicySettings", objPolicySettings);
		HttpResponse response = httpRequest.send();
		return NumberUtils.toInt(response.bodyText(), 0);
	}
}
