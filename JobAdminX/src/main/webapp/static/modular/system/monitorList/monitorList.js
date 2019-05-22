/**
 * 监控列表管理初始化
 */
var MonitorList = {
    id: "MonitorListTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
MonitorList.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
        {title: 'id', field: 'id', visible: false, align: 'center', valign: 'middle'},
        {title: '监控名称', field: 'objName', align: 'center', valign: 'middle', sortable: true,width:'12%'},
        {title: '监控描述', field: 'objDesc', align: 'left', valign: 'middle', sortable: true,width:'17%'},
        {title: '监控标识', field: 'objCode', align: 'center', valign: 'middle', sortable: true},
        {title: '监控状态', field: 'objStatus', align: 'center', valign: 'middle', sortable: true},
        {title: '创建时间', field: 'objCreateTime', align: 'center', valign: 'middle', sortable: true}
    ];
};

/**
 * 检查是否选中
 */
MonitorList.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        MonitorList.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加监控列表
 */
MonitorList.openAddMonitorList = function () {
    var index = layer.open({
        type: 2,
        title: '添加监控列表',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/monitorList/monitorList_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看监控列表详情
 */
MonitorList.openMonitorListDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '监控列表详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/monitorList/monitorList_update/' + MonitorList.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除监控列表
 */
MonitorList.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/monitorList/delete", function (data) {
            Feng.success("删除成功!");
            MonitorList.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("monitorListId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询监控列表列表
 */
MonitorList.search = function () {
    var queryData = {};
    queryData['objName'] = $("#objName").val();
    MonitorList.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = MonitorList.initColumn();
    var table = new BSTable(MonitorList.id, "/monitorList/list", defaultColunms);
    table.setPaginationType("client");
    MonitorList.table = table.init();
});
