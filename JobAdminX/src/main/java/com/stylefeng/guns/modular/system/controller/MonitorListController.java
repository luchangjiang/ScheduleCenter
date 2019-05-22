package com.stylefeng.guns.modular.system.controller;

import com.giveu.job.common.vo.MonitorObjectVo;
import com.stylefeng.guns.common.controller.BaseController;
import com.stylefeng.guns.modular.system.service.IMonitorListService;
import com.stylefeng.guns.modular.system.warpper.MenuWarpper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 监控列表控制器
 *
 * @author fengshuonan
 * @Date 2018-07-23 10:18:15
 */
@Controller
@RequestMapping("/monitorList")
public class MonitorListController extends BaseController {

    @Autowired
    IMonitorListService monitorListService;

    private String PREFIX = "/system/monitorList/";

    /**
     * 跳转到监控列表首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "monitorList.html";
    }

    /**
     * 跳转到添加监控列表
     */
    @RequestMapping("/monitorList_add")
    public String monitorListAdd() {
        return PREFIX + "monitorList_add.html";
    }

    /**
     * 跳转到修改监控列表
     */
    @RequestMapping("/monitorList_update/{monitorListId}")
    public String monitorListUpdate(@PathVariable String monitorListId, Model model) throws IOException {
		MonitorObjectVo monitorObjectVo = monitorListService.getMonitorObjectVoById(monitorListId);
		model.addAttribute("monitorObjectVo", monitorObjectVo);
		return PREFIX + "monitorList_edit.html";
    }

    /**
     * 获取监控列表列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(HttpServletRequest request) throws Exception {
        List<Map<String, Object>> list = new ArrayList<>();

        monitorListService.list(request, list);

        List<Map<String, Object>> menus = list;

        return super.warpObject(new MenuWarpper(menus));
    }

    /**
     * 新增监控列表
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add() {
        return super.SUCCESS_TIP;
    }

    /**
     * 删除监控列表
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete() {
        return SUCCESS_TIP;
    }


    /**
     * 修改监控列表
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(@RequestParam(value = "id") String id, @RequestParam(value = "objPolicySettings") String objPolicySettings) {
		monitorListService.updObjPolicySettings(id, objPolicySettings);
		return super.SUCCESS_TIP;
    }

    /**
     * 监控列表详情
     */
    @RequestMapping(value = "/detail")
    @ResponseBody
    public Object detail() {
        return null;
    }
}
