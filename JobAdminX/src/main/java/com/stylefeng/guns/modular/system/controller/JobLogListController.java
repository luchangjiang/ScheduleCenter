package com.stylefeng.guns.modular.system.controller;

import com.giveu.job.common.var.CommonVar;
import com.giveu.job.common.vo.JobTriggerAppLogVo;
import com.stylefeng.guns.common.controller.BaseController;
import com.stylefeng.guns.core.shiro.ShiroKit;
import com.stylefeng.guns.modular.system.dao.JobAppTriggerDao;
import com.stylefeng.guns.modular.system.service.IAppListService;
import com.stylefeng.guns.modular.system.warpper.MenuWarpper;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.giveu.job.common.util.DateUtil.dateToStamp;

/**
 * 日志列表控制器
 *
 * @author fengshuonan
 * @Date 2018-07-03 14:35:10
 */
@Controller
@RequestMapping("/jobLogList")
public class JobLogListController extends BaseController {

    private String PREFIX = "/system/jobLogList/";

    private static final int OK = 200;

    @Autowired
    private JobAppTriggerDao jobAppTriggerDao;

//    private static final String dataUrl = "http://localhost:8765/job/log/list";
//    private static final String dataUrl = "http://localhost:9060/job/log/list";
    private static final String dataUrl = "http://localhost:9050/job/log/list";

    @Autowired
    IAppListService appListService;

    /**
     * 跳转到日志列表首页
     */
    @RequestMapping("")
    public String index(Model model) {
        String appListJson = appListService.getListJsonByAccount();
        model.addAttribute("appListJson", appListJson);
        String account = ShiroKit.getUser().getAccount();
        model.addAttribute("account", account);
        model.addAttribute("triggerCount", 0);
        return PREFIX + "jobLogList.html";
    }

    /**
     * 跳转到添加日志列表
     */
    @RequestMapping("/jobLogList_add")
    public String jobLogListAdd() {
        return PREFIX + "jobLogList_add.html";
    }

    /**
     * 跳转到修改日志列表
     */
    @RequestMapping("/jobLogList_update/{jobLogListId}")
    public String jobLogListUpdate(@PathVariable Integer jobLogListId, Model model) {
        return PREFIX + "jobLogList_edit.html";
    }

    /**
     * 获取日志列表列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(HttpServletRequest request, HttpServletResponse httpServletResponse) throws IOException {
//        List<Map<String, Object>> list = new ArrayList<>();
//
//        int triggerCount = 0;
//
//        String beginTime = request.getParameter("beginTime");
//        String endTime = request.getParameter("endTime");
//        String jobId = request.getParameter("jobId");
//        String jobCode = request.getParameter("jobCode");
//        String executeStatus = request.getParameter("executeStatus");
//        String appKey = request.getParameter("appKey");
//
//        String account = ShiroKit.getUser().getAccount();
//
//
//
//        HttpRequest httpRequest = HttpRequest.post(dataUrl);
//        HttpMultiMap multiMap = httpRequest.query();
//        multiMap.add("beginTime", beginTime);
//        multiMap.add("endTime", endTime);
//        multiMap.add("jobId", jobId);
//        multiMap.add("jobCode", jobCode);
//        multiMap.add("executeStatus", executeStatus);
//        multiMap.add("appKey", appKey);
//        multiMap.add("userAccount", account);
//
//        HttpResponse response = httpRequest.send();
//
//        ObjectMapper mapper = new ObjectMapper();
//        String result = response.bodyText();
//        ResultModel resultModel = mapper.readValue(result, ResultModel.class);
//        List<JobTriggerAppLogVo> jobTriggerAppLogVoList = null;
//        if (resultModel.getCode() == OK) {
//            jobTriggerAppLogVoList = (ArrayList<JobTriggerAppLogVo>)resultModel.getData();
//            triggerCount = NumberUtils.toInt(resultModel.getMessage(), 0);
//
//        }
//
//        Cookie cookie = new Cookie("triggerCount", String.valueOf(triggerCount));
//        httpServletResponse.addCookie(cookie);
//
//        for (int i = 0; i < jobTriggerAppLogVoList.size(); i++) {
//            Map<String, Object> map = new HashMap<>();
//            map.put("id", ((Map)jobTriggerAppLogVoList.get(i)).get("id"));
//            map.put("jobName", ((Map)jobTriggerAppLogVoList.get(i)).get("jobName"));
//            map.put("jobDesc", ((Map)jobTriggerAppLogVoList.get(i)).get("jobDesc"));
//            map.put("jobCode", ((Map)jobTriggerAppLogVoList.get(i)).get("jobCode"));
//            map.put("appKey", ((Map)jobTriggerAppLogVoList.get(i)).get("appKey"));
//            map.put("triggerTime", ((Map)jobTriggerAppLogVoList.get(i)).get("triggerTime"));
//            map.put("leadTime", ((Map)jobTriggerAppLogVoList.get(i)).get("leadTime"));
//            map.put("callbackUrl", ((Map)jobTriggerAppLogVoList.get(i)).get("callbackUrl"));
//            map.put("callbackCount", ((Map)jobTriggerAppLogVoList.get(i)).get("callbackCount"));
//            map.put("callbackCount", ((Map)jobTriggerAppLogVoList.get(i)).get("callbackCount"));
//            map.put("statusDesc", ((Map)jobTriggerAppLogVoList.get(i)).get("statusDesc"));
//            map.put("executeDesc", ((Map)jobTriggerAppLogVoList.get(i)).get("executeDesc"));
//            list.add(map);
//        }
//
//        List<Map<String, Object>> menus = list;
//        return super.warpObject(new MenuWarpper(menus));








        String beginTime = request.getParameter("beginTime");
        String endTime = request.getParameter("endTime");
        String jobId = request.getParameter("jobId");
        String jobCode = request.getParameter("jobCode");
//        String executeStatus = request.getParameter("executeStatus");
        Integer executeStatus = NumberUtils.toInt(request.getParameter("executeStatus"), 0);
        String appKey = request.getParameter("appKey");

        String userAccount = ShiroKit.getUser().getAccount();


        Long triggerBeginTime = null;
        Long triggerEndTime = null;


        if (StringUtils.isNotBlank(beginTime)) {
            try {
                triggerBeginTime = dateToStamp(beginTime);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (StringUtils.isNotBlank(endTime)) {
            try {
                triggerEndTime = dateToStamp(endTime) + (86400000 - 1);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (StringUtils.isBlank(jobId)) {
            jobId = null;
        }
        if (StringUtils.isBlank(jobCode)) {
            jobCode = null;
        }

        List<String> appKeyList = jobAppTriggerDao.getAppKeyByAccount(userAccount);

//        if (appKeyList == null && appKeyList.size() > 0) {
//            resultModel.setCode(CommonMessage.NO_AUTHORITY_ERROR_CODE);
//            resultModel.setMessage(CommonMessage.NO_AUTHORITY_ERROR_DESC);
//            return;
//        }


        if (StringUtils.isBlank(appKey)) {
            return super.warpObject(new MenuWarpper(null));
//            appKey = appKeyList.get(0);
//            QrtzAppInfo qrtzAppInfo = jobAppTriggerDao.getAppInfoByAppKey(appKey);
//            if (qrtzAppInfo == null) {
////                resultModel.setCode(CommonMessage.TABLE_NOT_FOUND_ERROR_CODE);
////                resultModel.setMessage(CommonMessage.TABLE_NOT_FOUND_ERROR_DESC);
////                return;
//            }
        } else {
            boolean isContains = appKeyList.contains(appKey);
            if (!isContains) {
//                resultModel.setCode(CommonMessage.NO_AUTHORITY_ERROR_CODE);
//                resultModel.setMessage(CommonMessage.NO_AUTHORITY_ERROR_DESC);
//                return;
                return super.warpObject(new MenuWarpper(null));
            }
        }

        String tableName = CommonVar.LOG_TABLE_NAME_PREFIX + appKey;

        Integer triggerCount = jobAppTriggerDao.getRecQrtzTriggerLogCount(tableName, triggerBeginTime, triggerEndTime, jobId, jobCode, executeStatus);
        List<JobTriggerAppLogVo> jobTriggerAppLogVoList = jobAppTriggerDao.getRecQrtzTriggerLogList(tableName, triggerBeginTime, triggerEndTime, jobId, jobCode, executeStatus);

//        resultModel.setCode(CommonMessage.OK_CODE);
//        resultModel.setMessage(String.valueOf(count));
//        resultModel.setData(qrtzTriggerLogList);

        List<Map<String, Object>> list = new ArrayList<>();

//        for (int i = 0; i < jobTriggerAppLogVoList.size(); i++) {
//            Map<String, Object> map = new HashMap<>();
//            map.put("id", ((Map)jobTriggerAppLogVoList.get(i)).get("id"));
//            map.put("jobName", ((Map)jobTriggerAppLogVoList.get(i)).get("jobName"));
//            map.put("jobDesc", ((Map)jobTriggerAppLogVoList.get(i)).get("jobDesc"));
//            map.put("jobCode", ((Map)jobTriggerAppLogVoList.get(i)).get("jobCode"));
//            map.put("appKey", ((Map)jobTriggerAppLogVoList.get(i)).get("appKey"));
//            map.put("triggerTime", ((Map)jobTriggerAppLogVoList.get(i)).get("triggerTime"));
//            map.put("leadTime", ((Map)jobTriggerAppLogVoList.get(i)).get("leadTime"));
//            map.put("callbackUrl", ((Map)jobTriggerAppLogVoList.get(i)).get("callbackUrl"));
//            map.put("callbackCount", ((Map)jobTriggerAppLogVoList.get(i)).get("callbackCount"));
//            map.put("callbackCount", ((Map)jobTriggerAppLogVoList.get(i)).get("callbackCount"));
//            map.put("statusDesc", ((Map)jobTriggerAppLogVoList.get(i)).get("statusDesc"));
//            map.put("executeDesc", ((Map)jobTriggerAppLogVoList.get(i)).get("executeDesc"));
//            list.add(map);
//        }

        Cookie cookie = new Cookie("triggerCount", String.valueOf(triggerCount));
        httpServletResponse.addCookie(cookie);

        for (JobTriggerAppLogVo vo : jobTriggerAppLogVoList) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", vo.getId());
            map.put("jobName", vo.getJobName());
            map.put("jobDesc", vo.getJobDesc());
            map.put("jobCode", vo.getJobCode());
            map.put("appKey", vo.getAppKey());
            map.put("triggerTime", vo.getTriggerTime());
            map.put("leadTime", vo.getLeadTime().intValue());
            map.put("callbackUrl", vo.getCallbackUrl());
            map.put("callbackCount", vo.getCallbackCount());
            map.put("statusDesc", vo.getStatusDesc());
            map.put("executeDesc", vo.getExecuteDesc());
            list.add(map);
        }

        List<Map<String, Object>> menus = list;
        return super.warpObject(new MenuWarpper(menus));
    }

    /**
     * 新增日志列表
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add() {
        return super.SUCCESS_TIP;
    }

    /**
     * 删除日志列表
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete() {
        return SUCCESS_TIP;
    }




    /**
     * 修改日志列表
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update() {
        return super.SUCCESS_TIP;
    }

    /**
     * 日志列表详情
     */
    @RequestMapping(value = "/detail")
    @ResponseBody
    public Object detail() {
        return null;
    }
}
