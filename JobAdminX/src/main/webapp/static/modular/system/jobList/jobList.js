/**
 * 任务列表管理初始化
 */
var JobList = {
    id: "JobListTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
JobList.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
        {title: 'id', field: 'id', visible: false, align: 'center', valign: 'middle'},
        {title: '任务名称', field: 'jobName', align: 'center', valign: 'middle', sortable: true,width:'12%'},
        {title: '任务描述', field: 'jobDesc', align: 'left', valign: 'middle', sortable: true,width:'17%'},
        {title: '任务标识', field: 'jobCode', align: 'center', valign: 'middle', sortable: true},
        {title: 'AppKey', field: 'appKey', align: 'center', valign: 'middle', sortable: true},
        {title: 'CRON', field: 'cronExpression', align: 'left', valign: 'middle', sortable: true},
        {title: '回调地址', field: 'callbackUrl', align: 'left', valign: 'middle', sortable: true},
        {title: '失败次数', field: 'triggerFailCount', align: 'left', valign: 'middle', sortable: true},
        {title: '执行次数', field: 'triggerCount', align: 'left', valign: 'middle', sortable: true},
        {title: '执行状态', field: 'triggerStateDesc', align: 'left', valign: 'middle', sortable: true},
        {title: '创建时间', field: 'jobCreateTime', align: 'center', valign: 'middle', sortable: true}
    ];
};

/**
 * 检查是否选中
 */
JobList.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        JobList.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加任务列表
 */
JobList.openAddJobList = function () {
    var index = layer.open({
        type: 2,
        title: '添加任务列表',
        // area: ['800px', '520px'], //宽高
        area: ['1200px', '790px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/jobList/jobList_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看任务列表详情
 */
JobList.openJobListDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '任务列表详情',
            // area: ['800px', '520px'], //宽高
            area: ['1200px', '790px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/jobList/jobList_update/' + JobList.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除任务
 */
JobList.delJob = function () {
    if (this.check()) {
        var operation = function(){
            var obj = {};
            obj.jobCode = JobList.seItem.jobCode;
            obj.appKey = JobList.seItem.appKey;
            console.log(obj);
            var ajax = new $ax(Feng.ctxPath + "/jobList/delete", function (data) {
                Feng.success("删除成功!");
                // JobList.table.refresh();
                JobList.search();
            }, function (data) {
                console.log(obj);
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set(obj);
            ajax.start();
        };
        Feng.confirm("是否刪除该任务?", operation);
    }
};
/**
 * 暂停任务
 */
JobList.pauseJob = function () {
    if (this.check()) {
        var operation = function(){
            var obj = {};
            obj.jobCode = JobList.seItem.jobCode;
            obj.appKey = JobList.seItem.appKey;
            console.log(obj);
            var ajax = new $ax(Feng.ctxPath + "/jobList/pause", function (data) {
                Feng.success("暂停成功!");
                // JobList.table.refresh();
                JobList.search();
            }, function (data) {
                console.log(obj);
                Feng.error("暂停失败!" + data.responseJSON.message + "!");
            });
            ajax.set(obj);
            ajax.start();
        };
        Feng.confirm("是否暂停该任务?", operation);
    }
};
/**
 * 恢复任务
 */
JobList.resumeJob = function () {
    if (this.check()) {
        var operation = function(){
            var obj = {};
            obj.jobCode = JobList.seItem.jobCode;
            obj.appKey = JobList.seItem.appKey;
            console.log(obj);
            var ajax = new $ax(Feng.ctxPath + "/jobList/resume", function (data) {
                Feng.success("恢复成功!");
                // JobList.table.refresh();
                JobList.search();
            }, function (data) {
                console.log(obj);
                Feng.error("恢复失败!" + data.responseJSON.message + "!");
            });
            ajax.set(obj);
            ajax.start();
        };
        Feng.confirm("是否恢复该任务?", operation);
    }
};
/**
 * 根据JobCode手动执行
 */
JobList.triggerByJobCode = function () {
    if (this.check()) {
        var operation = function(){
            var obj = {};
            obj.jobCode = JobList.seItem.jobCode;
            obj.appKey = JobList.seItem.appKey;
            console.log(obj);
            var ajax = new $ax(Feng.ctxPath + "/jobList/trigger/code", function (data) {
                Feng.success("执行成功!");
                // JobList.table.refresh();
                JobList.search();
            }, function (data) {
                console.log(obj);
                Feng.error("执行失败!" + data.responseJSON.message + "!");
            });
            ajax.set(obj);
            ajax.start();
        };
        Feng.confirm("是否执行该任务?", operation);
    }
};

/**
 * 查看日志
 */
JobList.showLog = function () {
    if (this.check()) {
        localStorage.setItem("jobId", this.seItem.id);
        localStorage.setItem("appKey", this.seItem.appKey);
        localStorage.setItem("jobTime", new Date().getTime());
        var $lable = $('.nav-label', window.parent.document);
        for(var i = 0; i < $lable.size(); i++) {
            if($lable.eq(i).text() == "日志列表") {
                $lable.eq(i).click();
            }
        }
    }
};

/**
 * 查询任务列表列表
 */
JobList.search = function () {
    var queryData = {};
    queryData['id'] = $("#id").val();
    queryData['jobCode'] = $("#jobCode").val();
    queryData['appKey'] = $("#appKey").val();
    queryData['jobName'] = $("#jobName").val();
    queryData['triggerState'] = $("#triggerState").val();
    JobList.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = JobList.initColumn();
    var table = new BSTable(JobList.id, "/jobList/list", defaultColunms);
    table.setPaginationType("client");
    JobList.table = table.init();
});

$(function() {
    JobList.search();
    $("button[name='refresh']").hide();
});
