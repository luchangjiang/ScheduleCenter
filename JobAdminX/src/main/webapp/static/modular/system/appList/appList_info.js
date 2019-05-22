/**
 * 初始化应用列表详情对话框
 */
var AppListInfoDlg = {
    appListInfoData : {}
};

/**
 * 清除数据
 */
AppListInfoDlg.clearData = function() {
    this.appListInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
AppListInfoDlg.set = function(key, val) {
    this.appListInfoData[key] = (typeof value == "undefined") ? $("#" + key).val() : value;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
AppListInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
AppListInfoDlg.close = function() {
    parent.layer.close(window.parent.AppList.layerIndex);
}

/**
 * 收集数据
 */
AppListInfoDlg.collectData = function() {
    this.set('appName').set('appKey').set('appSecret').set('appStatus').set('id');
}


var checkInput = function(appListInfoData) {

    var appName = appListInfoData.appName;
    var appKey = appListInfoData.appKey;
    var appSecret = appListInfoData.appSecret;

    if(appName.trim() == "") {
        alert("应用名称不能为空");
        return false;
    }
    if(appKey.trim() == "") {
        alert("应用KEY不能为空");
        return false;
    }
    if(appSecret.trim() == "") {
        alert("应用秘钥不能为空");
        return false;
    }

    return true;

};

/**
 * 提交添加
 */
AppListInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    var isSubmit = checkInput(this.appListInfoData);
    if(!isSubmit) {
        return false;
    }

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/appList/add", function(data){
        Feng.success("添加成功!");
        // window.parent.AppList.table.refresh();
        window.parent.AppList.search();
        AppListInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.appListInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
AppListInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    var isSubmit = checkInput(this.appListInfoData);

    if(!isSubmit) {
        return false;
    }

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/appList/update", function(data){
        Feng.success("修改成功!");
        // window.parent.AppList.table.refresh();
        window.parent.AppList.search();
        AppListInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.appListInfoData);
    ajax.start();
}

AppListInfoDlg.bindSubmit = function() {

    this.clearData();
    this.collectData();

    // var isSubmit = checkInput(this.appListInfoData);
    //
    // if(!isSubmit) {
    //     return false;
    // }

    var accountArr = [];
    $("#check-list").find("input[name='account-list']:checked").each(function() {
        accountArr.push($(this).val());
    });

    var accountListJson = JSON.stringify(accountArr);

    var appKey = $("#app-key").val();

    $.ajax({
        url: "/appList/bind",
        method: "post",
        data: {"accountListJson":accountListJson, "appKey": appKey},
        success: function(data) {
            Feng.success("修改成功!");
            // window.parent.AppList.table.refresh();
            window.parent.AppList.search();
            AppListInfoDlg.close();
            console.log(data);
        }
    });

    //提交信息
    // var ajax = new $ax(Feng.ctxPath + "/appList/bind", function(data){
    //     Feng.success("修改成功!");
    //     window.parent.AppList.table.refresh();
    //     AppListInfoDlg.close();
    // },function(data){
    //     Feng.error("修改失败!" + data.responseJSON.message + "!");
    // });
    // ajax.set(this.appListInfoData);
    // ajax.start();
}

$(function() {

});
