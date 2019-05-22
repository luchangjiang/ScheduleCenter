package com.stylefeng.guns.modular.system.controller;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.giveu.job.common.vo.ResultModel;
import com.stylefeng.guns.common.controller.BaseController;
import com.stylefeng.guns.core.shiro.ShiroKit;
import com.stylefeng.guns.modular.system.dao.NoticeDao;
import jodd.http.HttpMultiMap;
import jodd.http.HttpRequest;
import jodd.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 总览信息
 *
 * @author fengshuonan
 * @Date 2017年3月4日23:05:54
 */
@Controller
@RequestMapping("/blackboard")
@PropertySource(value = { "classpath:application.yml" }, ignoreResourceNotFound = true)
public class BlackboardController extends BaseController {

    @Autowired
    NoticeDao noticeDao;

    @Value("${wecaturl}")
    String wecatUrl;
    @Value("${wecatport}")
    String wecatport;

    /**
     * 跳转到黑板
     */
    @RequestMapping("")
    public String blackboard(Model model) throws IOException {
        List<Map<String, Object>> notices = noticeDao.list(null);
        model.addAttribute("noticeList",notices);
        HttpRequest httpRequest = HttpRequest.post(wecatUrl + ":" + wecatport + "/we/cat/push/trigger/day/stat");
        HttpMultiMap multiMap = httpRequest.query();
        String account = ShiroKit.getUser().getAccount();
        multiMap.add("account", account);
        HttpResponse response = httpRequest.send();
        ObjectMapper mapper = new ObjectMapper();
        String result = response.bodyText();
        ResultModel resultModel = JSONObject.parseObject(result, ResultModel.class);
        String statListJson = resultModel.getData() + "";
        model.addAttribute("statListJson",statListJson);
        return "/blackboard.html";
    }
}
