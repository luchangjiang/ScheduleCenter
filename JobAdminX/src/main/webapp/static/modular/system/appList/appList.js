/**
 * 应用列表管理初始化
 */
var AppList = {
    id: "AppListTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
AppList.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
        {title: 'id', field: 'id', visible: false, align: 'center', valign: 'middle'},
        {title: '应用名称', field: 'appName', align: 'center', valign: 'middle', sortable: true,width:'17%'},
        {title: '应用编号', field: 'appKey', align: 'center', valign: 'middle', sortable: true,width:'12%'},
        {title: '应用秘钥', field: 'appSecret', align: 'center', valign: 'middle', sortable: true},
        {title: '应用状态', field: 'appStatusDesc', align: 'center', valign: 'middle', sortable: true}
    ];
};

/**
 * 检查是否选中
 */
AppList.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        AppList.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加应用列表
 */
AppList.openAddAppList = function () {
    var index = layer.open({
        type: 2,
        title: '添加应用列表',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/appList/appList_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看应用列表详情
 */
AppList.openAppListDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '应用列表详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/appList/appList_update/' + AppList.seItem.id
        });
        this.layerIndex = index;
    }
};
AppList.bindUser = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '绑定用户',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/appList/bind/' + AppList.seItem.appKey
        });
        this.layerIndex = index;
    }
};

/**
 * 删除应用列表
 */
AppList.delete = function () {

    if (this.check()) {
        var operation = function(){
            var ajax = new $ax(Feng.ctxPath + "/appList/delete", function (data) {
                Feng.success("删除成功!");
                // AppList.table.refresh();
                AppList.search();
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("id",AppList.seItem.id);
            ajax.start();
        };

        Feng.confirm("是否刪除该应用?", operation);
    }
};
/**
 * 禁用
 */
AppList.disable = function () {

    if (this.check()) {
        var operation = function(){
            var ajax = new $ax(Feng.ctxPath + "/appList/disable", function (data) {
                Feng.success("成功!");
                // AppList.table.refresh();
                AppList.search();
            }, function (data) {
                Feng.error("失败!" + data.responseJSON.message + "!");
            });
            ajax.set("id",AppList.seItem.id);
            ajax.start();
        };

        Feng.confirm("是否禁用该应用?", operation);
    }
};
/**
 * 启用
 */
AppList.enable = function () {

    if (this.check()) {
        var operation = function(){
            var ajax = new $ax(Feng.ctxPath + "/appList/enable", function (data) {
                Feng.success("成功!");
                // AppList.table.refresh();
                AppList.search();
            }, function (data) {
                Feng.error("失败!" + data.responseJSON.message + "!");
            });
            ajax.set("id",AppList.seItem.id);
            ajax.start();
        };

        Feng.confirm("是否启用该应用?", operation);
    }
};

/**
 * 查询应用列表列表
 */
AppList.search = function () {
    var queryData = {};
    queryData['appName'] = $("#appName").val();
    queryData['appKey'] = $("#appKey").val();
    queryData['appStatus'] = $("#appStatus").val();
    AppList.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = AppList.initColumn();
    var table = new BSTable(AppList.id, "/appList/list", defaultColunms);
    table.setPaginationType("client");
    AppList.table = table.init();
});

$(function() {
    $("button[name='refresh']").hide();
});

