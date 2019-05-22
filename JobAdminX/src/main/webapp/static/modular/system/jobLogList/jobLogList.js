/**
 * 日志列表管理初始化
 */
var JobLogList = {
    id: "JobLogListTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
JobLogList.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
        {title: 'id', field: 'id', visible: false, align: 'center', valign: 'middle'},
        {title: '任务名称', field: 'jobName', align: 'center', valign: 'middle', sortable: true,width:'12%'},
        {title: '任务描述', field: 'jobDesc', align: 'left', valign: 'middle', sortable: true,width:'17%'},
        {title: '任务标识', field: 'jobCode', align: 'center', valign: 'middle', sortable: true},
        {title: 'AppKey', field: 'appKey', align: 'center', valign: 'middle', sortable: true},
        {title: '执行时间', field: 'triggerTime', align: 'center', valign: 'middle', sortable: true},
        {title: '执行耗时', field: 'leadTime', align: 'center', valign: 'middle', sortable: true},
        {title: '回调次数', field: 'callbackCount', align: 'center', valign: 'middle', sortable: true},
        {title: '回调地址', field: 'callbackUrl', align: 'left', valign: 'middle', sortable: true},
        {title: '执行结果', field: 'statusDesc', align: 'center', valign: 'middle', sortable: true}
    ];
};

/**
 * 检查是否选中
 */
JobLogList.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        JobLogList.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加日志列表
 */
JobLogList.openAddJobLogList = function () {
    var index = layer.open({
        type: 2,
        title: '添加日志列表',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/jobLogList/jobLogList_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看日志列表详情
 */
JobLogList.openJobLogListDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '日志列表详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/jobLogList/jobLogList_update/' + JobLogList.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除日志列表
 */
JobLogList.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/jobLogList/delete", function (data) {
            Feng.success("删除成功!");
            JobLogList.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("jobLogListId",this.seItem.id);

        ajax.start();
    }
};

/**
 * 手动触发
 */
JobLogList.trigger = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/jobList/trigger/log/id", function (data) {
            Feng.success("执行成功!");
            JobLogList.table.refresh();
        }, function (data) {
            Feng.error("执行失败!" + data.responseJSON.message + "!");
        });
        console.log(this.seItem.id);
        ajax.set("jobLogListId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询日志列表列表
 */
JobLogList.search = function () {
    var queryData = {};
    queryData['beginTime'] = $("#beginTime").val();
    queryData['endTime'] = $("#endTime").val();
    queryData['jobId'] = $("#jobId").val();
    queryData['jobCode'] = $("#jobCode").val();
    queryData['executeStatus'] = $("#executeStatus").val();
    queryData['appKey'] = $("#appKey").val();
    JobLogList.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = JobLogList.initColumn();
    var table = new BSTable(JobLogList.id, "/jobLogList/list", defaultColunms);
    table.setPaginationType("client");
    var triggerCount = "${triggerCount}";
    JobLogList.table = table.init();

    $("button[name='refresh']").hide();
});

