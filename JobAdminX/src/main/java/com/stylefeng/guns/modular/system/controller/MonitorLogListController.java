package com.stylefeng.guns.modular.system.controller;

import com.stylefeng.guns.common.controller.BaseController;
import com.stylefeng.guns.modular.system.service.IMonitorLogListService;
import com.stylefeng.guns.modular.system.warpper.MenuWarpper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 监控日志控制器
 *
 * @author fengshuonan
 * @Date 2018-07-23 10:54:28
 */
@Controller
@RequestMapping("/monitorLogList")
public class MonitorLogListController extends BaseController {

    @Autowired
    IMonitorLogListService monitorLogListService;

    private String PREFIX = "/system/monitorLogList/";

    /**
     * 跳转到监控日志首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "monitorLogList.html";
    }

    /**
     * 跳转到添加监控日志
     */
    @RequestMapping("/monitorLogList_add")
    public String monitorLogListAdd() {
        return PREFIX + "monitorLogList_add.html";
    }

    /**
     * 跳转到修改监控日志
     */
    @RequestMapping("/monitorLogList_update/{monitorLogListId}")
    public String monitorLogListUpdate(@PathVariable Integer monitorLogListId, Model model) {
        return PREFIX + "monitorLogList_edit.html";
    }

    /**
     * 获取监控日志列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(HttpServletRequest request) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();

        monitorLogListService.list(request, list);

        List<Map<String, Object>> menus = list;

        return super.warpObject(new MenuWarpper(menus));
    }

    /**
     * 新增监控日志
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add() {
        return super.SUCCESS_TIP;
    }

    /**
     * 删除监控日志
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete() {
        return SUCCESS_TIP;
    }


    /**
     * 修改监控日志
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update() {
        return super.SUCCESS_TIP;
    }

    /**
     * 监控日志详情
     */
    @RequestMapping(value = "/detail")
    @ResponseBody
    public Object detail() {
        return null;
    }
}
