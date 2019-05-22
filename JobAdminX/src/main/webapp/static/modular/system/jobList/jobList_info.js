/**
 * 初始化任务列表详情对话框
 */
var JobListInfoDlg = {
    jobListInfoData : {}
};

/**
 * 清除数据
 */
JobListInfoDlg.clearData = function() {
    this.jobListInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
JobListInfoDlg.set = function(key, val) {
    this.jobListInfoData[key] = (typeof value == "undefined") ? $("#" + key).val() : value;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
JobListInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
JobListInfoDlg.close = function() {
    parent.layer.close(window.parent.JobList.layerIndex);
}

/**
 * 收集数据
 */
JobListInfoDlg.collectData = function() {
    this.set('id').set('jobName').set('jobDesc').set('jobCode').set('appKey').set('cronExpression').set('jobCreateTime').set('callbackUrl').set('appKey').set('jobType').set('resultWaitTime').set('resultUrl');
}

function isRealNum(val){
    // isNaN()函数 把空串 空格 以及NUll 按照0来处理 所以先去除
    if(val === "" || val ==null){
        return false;
    }
    if(!isNaN(val)){
        return true;
    }else{
        return false;
    }
}


var checkInput = function(jobListInfoData) {
    var jobName = jobListInfoData.jobName;
    var jobDesc = jobListInfoData.jobDesc;
    var jobCode = jobListInfoData.jobCode;
    var cronExpression = jobListInfoData.cronExpression;
    var callbackUrl = jobListInfoData.callbackUrl;
    var appKey = jobListInfoData.appKey;
    var jobType = jobListInfoData.jobType;
    var resultUrl = jobListInfoData.resultUrl;
    var resultWaitTime = jobListInfoData.resultWaitTime;

    if(jobName.trim() == "") {
        alert("任务名称不能为空");
        return false;
    }
    if(jobDesc.trim() == "") {
        alert("任务描述不能为空");
        return false;
    }
    if(jobCode.trim() == "") {
        alert("任务编号不能为空");
        return false;
    }
    if(cronExpression.trim() == "") {
        alert("任务CRON表达式不能为空");
        return false;
    }
    if(callbackUrl.trim() == "") {
        alert("任务回调地址不能为空");
        return false;
    }
    if(appKey.trim() == "") {
        alert("请选择应用");
        return false;
    }
    console.log(jobType);
    if(jobType == 2) {
        if(resultUrl.trim() == "") {
            alert("结果地址不能为空");
            return false;
        }
        if(resultWaitTime.trim() == "") {
            alert("最大处理时间不能为空");
            return false;
        }
        if (!isRealNum(resultWaitTime)) {
            alert("最大处理时间必须为整数");
            return false;
        }
        if (resultWaitTime < 10 || resultWaitTime > 240) {
            alert("最大处理时间范围10分钟 ~ 240分钟");
            return false;
        }

    }
    return true;
};

/**
 * 提交添加
 */
JobListInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    var isSubmit = checkInput(this.jobListInfoData);

    console.log(this.jobListInfoData);
    if(!isSubmit) {
        return false;
    }

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/jobList/add", function(data){
        Feng.success("添加成功!");
        // window.parent.JobList.table.refresh();
        window.parent.JobList.search();
        JobListInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.jobListInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
JobListInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    var isSubmit = checkInput(this.jobListInfoData);
    if(!isSubmit) {
        return false;
    }

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/jobList/update", function(data){
        Feng.success("修改成功!");
        // window.parent.JobList.table.refresh();
        window.parent.JobList.search();
        JobListInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.jobListInfoData);
    ajax.start();
}

$(function() {

    $(".btn-primary").click(function() {
        var cronStr = $("#croninfo").val();
        var cronArr = cronStr.split(" ");
        var indexStr0 = cronArr[0];
        var indexStr1 = cronArr[1];
        var indexStr2 = cronArr[2];
        var indexStr3 = cronArr[3];
        var indexStr4 = cronArr[4];
        if (indexStr0 == "*") {
            cronArr[0] = 0;
        }
        if (indexStr0 == "*" && indexStr1 == "*") {
            cronArr[1] = 0;
        }
        if (indexStr0 == "*" && indexStr1 == "*" && indexStr2 == "*") {
            cronArr[2] = 0;
        }
        if (indexStr0 == "*" && indexStr1 == "*" && indexStr2 == "*" && indexStr3 == "*") {
            cronArr[3] = 0;
        }
        if (indexStr0 == "*" && indexStr1 == "*" && indexStr2 == "*" && indexStr3 == "*" && indexStr4 == "*") {
            cronArr[4] = 0;
        }
        cronStr = cronArr.join(" ");
        $("#croninfo").val(cronStr);
    });

});
