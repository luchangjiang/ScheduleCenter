/**
 * 初始化日志列表详情对话框
 */
var JobLogListInfoDlg = {
    jobLogListInfoData : {}
};

/**
 * 清除数据
 */
JobLogListInfoDlg.clearData = function() {
    this.jobLogListInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
JobLogListInfoDlg.set = function(key, val) {
    this.jobLogListInfoData[key] = (typeof value == "undefined") ? $("#" + key).val() : value;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
JobLogListInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
JobLogListInfoDlg.close = function() {
    parent.layer.close(window.parent.JobLogList.layerIndex);
}

/**
 * 收集数据
 */
JobLogListInfoDlg.collectData = function() {
    this.set('id');
}

/**
 * 提交添加
 */
JobLogListInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/jobLogList/add", function(data){
        Feng.success("添加成功!");
        window.parent.JobLogList.table.refresh();
        JobLogListInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.jobLogListInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
JobLogListInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/jobLogList/update", function(data){
        Feng.success("修改成功!");
        window.parent.JobLogList.table.refresh();
        JobLogListInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.jobLogListInfoData);
    ajax.start();
}

$(function() {

});
