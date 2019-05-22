package com.stylefeng.guns.modular.system.controller;

import com.alibaba.fastjson.JSONObject;
import com.giveu.job.common.info.CommonMessage;
import com.giveu.job.common.vo.AppVo;
import com.giveu.job.common.vo.ResultModel;
import com.stylefeng.guns.common.controller.BaseController;
import com.stylefeng.guns.common.exception.BizExceptionEnum;
import com.stylefeng.guns.common.exception.BussinessException;
import com.stylefeng.guns.config.WhiteListConfig;
import com.stylefeng.guns.modular.system.service.IAppListService;
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
 * 应用列表控制器
 *
 * @author fengshuonan
 * @Date 2018-07-03 17:28:13
 */
@Controller
@RequestMapping("/appList")
public class AppListController extends BaseController {

    private static Logger logger = Logger.getLogger(AppListController.class);

    private String PREFIX = "/system/appList/";

    @Autowired
    IAppListService appListService;

    /**
     * 跳转到应用列表首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "appList.html";
    }

    /**
     * 跳转到添加应用列表
     */
    @RequestMapping("/appList_add")
    public String appListAdd() {
        return PREFIX + "appList_add.html";
    }

    /**
     * 跳转到修改应用列表
     */
    @RequestMapping("/appList_update/{appListId}")
    public String appListUpdate(@PathVariable String appListId, Model model) throws IOException {
        AppVo appList = appListService.getAppVoById(appListId);
        model.addAttribute("appList", appList);
        return PREFIX + "appList_edit.html";
    }

    @RequestMapping("/bind/{appKey}")
    public String appListBindUser(@PathVariable String appKey, Model model) throws IOException {
        List<String> accountList = appListService.getAccountList();
        String bindAccountListJson = appListService.getBindAccountList(appKey);
        String accountListJson = JSONObject.toJSONString(accountList);
        model.addAttribute("appKey", appKey);
        model.addAttribute("bindAccountListJson", bindAccountListJson);
        model.addAttribute("accountListJson", accountListJson);
        return PREFIX + "appList_bind.html";
    }

    /**
     * 获取应用列表列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(HttpServletRequest request) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        appListService.list(request, list);

        List<Map<String, Object>> menus = list;
        return super.warpObject(new MenuWarpper(menus));
    }

    /**
     * 新增应用列表
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(@Valid AppVo appVo, BindingResult result) throws Exception {
        if (result.hasErrors()) {
            throw new BussinessException(BizExceptionEnum.REQUEST_NULL);
        }
        ResultModel resultModel = appListService.addApp(appVo);

//        if (code == 519 || code.equals(519)) {
//            throw new Exception(" APPKEY 已存在");
//
//        }

        if (!CommonMessage.OK_CODE.equals(resultModel.getCode())) {
            throw new Exception(resultModel.getMessage());
        }

        return super.SUCCESS_TIP;
    }

    /**
     * 删除应用列表
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam(value = "id") String id) {
        appListService.delAppById(id);
        return SUCCESS_TIP;
    }
    /**
     * 删除应用列表
     */
    @RequestMapping(value = "/enable")
    @ResponseBody
    public Object enable(@RequestParam(value = "id") String id) {
        appListService.enableById(id);
        return SUCCESS_TIP;
    }
    /**
     * 删除应用列表
     */
    @RequestMapping(value = "/disable")
    @ResponseBody
    public Object disable(@RequestParam(value = "id") String id) {
        appListService.disableById(id);
        return SUCCESS_TIP;
    }


    /**
     * 修改应用列表
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(@Valid AppVo appVo, BindingResult result) throws IOException {
        if (result.hasErrors()) {
            throw new BussinessException(BizExceptionEnum.REQUEST_NULL);
        }
        appListService.updAppById(appVo);
        return super.SUCCESS_TIP;
    }
    @RequestMapping(value = "/bind")
    @ResponseBody
    public Object bindAccount(String accountListJson, String appKey) throws IOException {
        appListService.bindAccount(accountListJson, appKey);
        return super.SUCCESS_TIP;
    }

    /**
     * 应用列表详情
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
            resultModel = appListService.list();

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
