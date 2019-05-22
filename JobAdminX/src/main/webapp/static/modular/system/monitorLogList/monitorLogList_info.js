/**
 * 初始化监控日志详情对话框
 */
var MonitorLogListInfoDlg = {
    monitorLogListInfoData : {}
};

/**
 * 清除数据
 */
MonitorLogListInfoDlg.clearData = function() {
    this.monitorLogListInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
MonitorLogListInfoDlg.set = function(key, val) {
    this.monitorLogListInfoData[key] = (typeof value == "undefined") ? $("#" + key).val() : value;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
MonitorLogListInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
MonitorLogListInfoDlg.close = function() {
    parent.layer.close(window.parent.MonitorLogList.layerIndex);
}

/**
 * 收集数据
 */
MonitorLogListInfoDlg.collectData = function() {
    this.set('id');
}

/**
 * 提交添加
 */
MonitorLogListInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/monitorLogList/add", function(data){
        Feng.success("添加成功!");
        window.parent.MonitorLogList.table.refresh();
        MonitorLogListInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.monitorLogListInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
MonitorLogListInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/monitorLogList/update", function(data){
        Feng.success("修改成功!");
        window.parent.MonitorLogList.table.refresh();
        MonitorLogListInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.monitorLogListInfoData);
    ajax.start();
}

$(function() {

});
