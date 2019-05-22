/**
 * 初始化监控列表详情对话框
 */
var MonitorListInfoDlg = {
    monitorListInfoData : {}
};

/**
 * 清除数据
 */
MonitorListInfoDlg.clearData = function() {
    this.monitorListInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
MonitorListInfoDlg.set = function(key, val) {

    // create by fox
    // 根据配置自动获取值转为JSON 提交
    var objPolicySettings = $("#objPolicySettings").text();
    objPolicySettings = JSON.parse(objPolicySettings);
    var items = objPolicySettings.items;
    for (var i = 0; i < items.length; i++) {
        items[i].value = $("#" + items[i].key).val();
        items[i].compare = $("#" + items[i].key + '-compare').val();
    }
    this.monitorListInfoData.id = (typeof value == "undefined") ? $("#id").val() : value;
    this.monitorListInfoData.objPolicySettings = JSON.stringify(objPolicySettings);
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
MonitorListInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
MonitorListInfoDlg.close = function() {
    parent.layer.close(window.parent.MonitorList.layerIndex);
}

/**
 * 收集数据
 */
MonitorListInfoDlg.collectData = function() {
    this.set('id');
}

/**
 * 提交添加
 */
MonitorListInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/monitorList/add", function(data){
        Feng.success("添加成功!");
        window.parent.MonitorList.table.refresh();
        MonitorListInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.monitorListInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
MonitorListInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/monitorList/update", function(data){
        Feng.success("修改成功!");
        window.parent.MonitorList.table.refresh();
        MonitorListInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.monitorListInfoData);
    ajax.start();
}

$(function() {

});
