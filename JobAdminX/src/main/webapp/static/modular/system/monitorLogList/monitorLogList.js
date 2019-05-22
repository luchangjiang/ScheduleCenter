/**
 * 监控日志管理初始化
 */
var MonitorLogList = {
    id: "MonitorLogListTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
MonitorLogList.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
        {title: 'id', field: 'id', visible: false, align: 'center', valign: 'middle'},
        {title: '监控对象ID', field: 'objId', align: 'center', valign: 'middle', sortable: true,width:'12%'},
        {title: '监控名称', field: 'objName', align: 'center', valign: 'middle', sortable: true,width:'12%'},
        {title: '监控描述', field: 'objDesc', align: 'left', valign: 'middle', sortable: true,width:'17%'},
        {title: '监控标识', field: 'objCode', align: 'center', valign: 'middle', sortable: true},
        {title: '日志时间', field: 'logCreateTime', align: 'center', valign: 'middle', sortable: true},
        {title: '日志内容', field: 'logContext', align: 'center', valign: 'middle', sortable: true}
    ];
};

/**
 * 检查是否选中
 */
MonitorLogList.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        MonitorLogList.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加监控日志
 */
MonitorLogList.openAddMonitorLogList = function () {
    var index = layer.open({
        type: 2,
        title: '添加监控日志',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/monitorLogList/monitorLogList_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看监控日志详情
 */
MonitorLogList.openMonitorLogListDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '监控日志详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/monitorLogList/monitorLogList_update/' + MonitorLogList.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除监控日志
 */
MonitorLogList.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/monitorLogList/delete", function (data) {
            Feng.success("删除成功!");
            MonitorLogList.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("monitorLogListId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询监控日志列表
 */
MonitorLogList.search = function () {
    var queryData = {};
    queryData['beginTime'] = $("#beginTime").val();
    queryData['endTime'] = $("#endTime").val();
    queryData['objName'] = $("#objName").val();
    MonitorLogList.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = MonitorLogList.initColumn();
    var table = new BSTable(MonitorLogList.id, "/monitorLogList/list", defaultColunms);
    table.setPaginationType("client");
    MonitorLogList.table = table.init();
});
