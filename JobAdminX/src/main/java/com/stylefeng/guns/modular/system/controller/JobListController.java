package com.stylefeng.guns.modular.system.controller;

import com.alibaba.fastjson.JSONObject;
import com.giveu.job.common.info.CommonMessage;
import com.giveu.job.common.vo.JobTriggerAppVo;
import com.giveu.job.common.vo.ResultModel;
import com.stylefeng.guns.common.controller.BaseController;
import com.stylefeng.guns.common.exception.BizExceptionEnum;
import com.stylefeng.guns.common.exception.BussinessException;
import com.stylefeng.guns.config.WhiteListConfig;
import com.stylefeng.guns.core.shiro.ShiroKit;
import com.stylefeng.guns.modular.system.service.IAppListService;
import com.stylefeng.guns.modular.system.service.IJobListService;
import com.stylefeng.guns.modular.system.warpper.MenuWarpper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 任务列表控制器
 *
 * @author fengshuonan
 * @Date 2018-07-03 17:03:27
 */
@Controller
@RequestMapping("/jobList")
public class JobListController extends BaseController {

    private static Logger logger = Logger.getLogger(JobListController.class);

    private String PREFIX = "/system/jobList/";

    @Autowired
    IAppListService appListService;

    @Autowired
    IJobListService jobListService;


    /**
     * 跳转到任务列表首页
     */
    @RequestMapping("")
    public String index(Model model) {
        String appListJson = appListService.getListJsonByAccount();
        String account = ShiroKit.getUser().getAccount();
        model.addAttribute("account", account);
        model.addAttribute("appListJson", appListJson);
        return PREFIX + "jobList.html";
    }

//    /**
//     * 跳转到任务列表首页
//     */
//    @RequestMapping("/show/log")
//    public String showLog() {
//        return PREFIX + "../jobLogList/jobLogList.html";
//    }



    /**
     * 跳转到添加任务列表
     */
    @RequestMapping("/jobList_add")
    public String jobListAdd(Model model) throws IOException {
//        String appListJson = appListService.getListJson();
        String appListJson = appListService.getListJsonByAccount();
        model.addAttribute("appListJson", appListJson);
        return PREFIX + "jobList_add.html";
    }

    /**
     * 跳转到修改任务列表
     */
    @RequestMapping("/jobList_update/{jobListId}")
    public String jobListUpdate(@PathVariable String jobListId, Model model) {
        JobTriggerAppVo jobTriggerAppVo = jobListService.getJobTriggerAppVo(jobListId);
        model.addAttribute("jobTriggerAppVo", jobTriggerAppVo);
        return PREFIX + "jobList_edit.html";
    }

    /**
     * 获取任务列表列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(HttpServletRequest request) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();

        jobListService.list(request, list);

        List<Map<String, Object>> menus = list;

        return super.warpObject(new MenuWarpper(menus));
    }

    /**
     * 新增任务列表
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(@Valid JobTriggerAppVo jobTriggerAppVo, BindingResult result) throws Exception {
        if (result.hasErrors()) {
            throw new BussinessException(BizExceptionEnum.REQUEST_NULL);
        }
        ResultModel resultModel = jobListService.addJob(jobTriggerAppVo);
        if (!CommonMessage.OK_CODE.equals(resultModel.getCode())) {
            throw new Exception(resultModel.getMessage());
        }
        return super.SUCCESS_TIP;
    }

    /**
     * 删除任务列表
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam(value = "jobCode") String jobCode) throws Exception {
        ResultModel resultModel = jobListService.delJobByCode(jobCode);
        if (!CommonMessage.OK_CODE.equals(resultModel.getCode())) {
            throw new Exception(resultModel.getMessage());
        }
        return SUCCESS_TIP;
    }

    /**
     * 暂停任务
     */
    @RequestMapping(value = "/pause")
    @ResponseBody
    public Object pause(@RequestParam(value = "jobCode") String jobCode) throws Exception {
//        jobListService.pauseJob(jobCode);
//        return SUCCESS_TIP;

        ResultModel resultModel = jobListService.pauseJob(jobCode);
        if (!CommonMessage.OK_CODE.equals(resultModel.getCode())) {
            throw new Exception(resultModel.getMessage());
        }
        return SUCCESS_TIP;
    }
    /**
     * 恢复任务
     */
    @RequestMapping(value = "/resume")
    @ResponseBody
    public Object resume(@RequestParam(value = "jobCode") String jobCode) throws Exception {
//        jobListService.resumeJob(jobCode);
//        return SUCCESS_TIP;

        ResultModel resultModel = jobListService.resumeJob(jobCode);
        if (!CommonMessage.OK_CODE.equals(resultModel.getCode())) {
            throw new Exception(resultModel.getMessage());
        }
        return SUCCESS_TIP;
    }
    /**
     * 手动执行根据jobCode
     */
    @RequestMapping(value = "/trigger/code")
    @ResponseBody
    public Object triggerByJobCode(@RequestParam(value = "jobCode") String jobCode) throws Exception {
//        jobListService.triggerByJobCode(jobCode);
//        return SUCCESS_TIP;

        ResultModel resultModel = jobListService.triggerByJobCode(jobCode);
        if (!CommonMessage.OK_CODE.equals(resultModel.getCode())) {
            throw new Exception(resultModel.getMessage());
        }
        return SUCCESS_TIP;
    }


    /**
     * 手动触发
     */
    @RequestMapping(value = "/trigger/log/id")
    @ResponseBody
    public Object triggerByLogId(@RequestParam(value = "jobLogListId") String jobLogListId) throws Exception {
//        jobListService.triggerByLogId(jobLogListId);
//        return SUCCESS_TIP;

        ResultModel resultModel = jobListService.triggerByLogId(jobLogListId);
        if (!CommonMessage.OK_CODE.equals(resultModel.getCode())) {
            throw new Exception(resultModel.getMessage());
        }
        return SUCCESS_TIP;
    }


    /**
     * 修改任务列表
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(@Valid JobTriggerAppVo jobTriggerAppVo, BindingResult result) throws Exception {
//        if (result.hasErrors()) {
//            throw new BussinessException(BizExceptionEnum.REQUEST_NULL);
//        }
//        jobListService.updJob(jobTriggerAppVo);
//        return super.SUCCESS_TIP;

        ResultModel resultModel = jobListService.updJob(jobTriggerAppVo);
        if (!CommonMessage.OK_CODE.equals(resultModel.getCode())) {
            throw new Exception(resultModel.getMessage());
        }
        return SUCCESS_TIP;
    }

    /**
     * 任务列表详情
     */
    @RequestMapping(value = "/detail")
    @ResponseBody
    public Object detail() {
        return null;
    }


    @RequestMapping(value = "/get/list")
    public void getList(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String address = request.getRemoteAddr();
        logger.info("Request remoteAddress: " + address);
        List<String> list = WhiteListConfig.list;
        boolean flag = false;
        for (String li : list) {
            if (address.indexOf(li) >= 0) {
                flag = true;
                break;
            }
        }

        ResultModel resultModel = null;
        if (flag) {
            resultModel = jobListService.list();

        } else {
            resultModel = new ResultModel();
            resultModel.setCode(CommonMessage.NO_AUTHORITY_ERROR_CODE);
            resultModel.setMessage(CommonMessage.NO_AUTHORITY_ERROR_DESC);

        }

        response.setContentType("application/json;charset=utf-8");
        Writer writer = response.getWriter();
        String jsonStr = JSONObject.toJSONString(resultModel);
        writer.write(jsonStr);
        writer.flush();
        writer.close();

    }

}
