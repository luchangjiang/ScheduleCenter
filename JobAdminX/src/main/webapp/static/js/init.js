/**
 * Created by LinJ on 2015/11/5. 初始化页面模块js
 */

// 初始化各类元素以及监听
$(function() {
	inition();
});

// 页面所有的页签名称
var itemList = [ "second", "min", "hour", "day", "month", "week" ];

// 初始化各个页签自主选择的最大值
var max_second = 59;
var max_min = 59;
var max_hour = 23;
var max_day = 31;
var max_month = 12;
var max_week = 7;

function inition() {
	// 初始化checkbox
	initCheckBox("l_second", 0, 59);
	initCheckBox("l_min", 0, 59);
	initCheckBox("l_hour", 0, 23);
	initCheckBox("l_day", 1, 31);
	initCheckBox("l_month", 1, 12);
	initCheckBoxZhou("l_week", 1, 7);

	// 初始化单选、复选框
	$('input').iCheck({
		checkboxClass : 'icheckbox_square-blue',
		radioClass : 'iradio_square-blue',
		increaseArea : '20%' // optional
	});

	// 解析按钮绑定函数
	$('#explain').click(function() {
		reverseExp();
	});

	initFirstRadio();
	initNoConfirmRadio();
	initRadio();
	initRadioCheckDiv();
	initSpinner();
	initCronFromTable();
	initListChange();

	// 如果已有传输过来的值，那么放入cron字段并且反解析
	var instr = $("#transCron").val();
	if (instr) {
		$("#croninfo").val(instr);
		reverseExp();
	}

}

// 初始化大量的复选框 根据div的id以及数量进行初始化 每个复选框占1/12宽度
function initCheckBox(divid, start, cnt) {
	if (cnt != null && cnt > 0) {
		for (dc = start; dc <= cnt; dc++) {
			$("#" + divid).append(
					" <div class='col-xs-1 col-sm-1 col-md-1'> <label><input type='checkbox' value="
							+ dc + "> &nbsp;" + dc + "</label></div>");
		}
	}
}

// 初始化大量的复选框 根据div的id以及数量进行初始化 每个复选框占1/12宽度
function initCheckBoxZhou(divid, start, cnt) {
	if (cnt != null && cnt > 0) {
		for (dc = start; dc <= cnt; dc++) {
			switch (dc) {
			case 1:
				$("#" + divid).append(
						"<div class='col-xs-1 col-sm-1 col-md-1'> <label><input type='checkbox' value="
								+ dc + "> &nbsp;周日 </label></div>");
				break;
			case 2:
				$("#" + divid).append(
						" <div class='col-xs-1 col-sm-1 col-md-1'> <label><input type='checkbox' value="
								+ dc + "> &nbsp;周一 </label></div>");
				break;
			case 3:
				$("#" + divid).append(
						" <div class='col-xs-1 col-sm-1 col-md-1'> <label><input type='checkbox' value="
								+ dc + "> &nbsp;周二</label></div>");
				break;
			case 4:
				$("#" + divid).append(
						" <div class='col-xs-1 col-sm-1 col-md-1'> <label><input type='checkbox' value="
								+ dc + "> &nbsp;周三 </label></div>");
				break;
			case 5:
				$("#" + divid).append(
						" <div class='col-xs-1 col-sm-1 col-md-1'> <label><input type='checkbox' value="
								+ dc + "> &nbsp;周四 </label></div>");
				break;
			case 6:
				$("#" + divid).append(
						" <div class='col-xs-1 col-sm-1 col-md-1'> <label><input type='checkbox' value="
								+ dc + "> &nbsp;周五 </label></div>");
				break;
			default:
				$("#" + divid).append(
						" <div class='col-xs-1 col-sm-1 col-md-1'> <label><input type='checkbox' value="
								+ dc + "> &nbsp;周六 </label></div>");
				break;
			}
		}
	}
}

// 初始化每个页签第一排的radio，即*条件
function initFirstRadio() {
	$('.firstradio').on('ifChecked', function() {
		everyTime(this);
	});
	$('.unselectradio').on('ifChecked', function() {
		clearSpan(this);
	});
}

// 初始化不指定的radio 即？条件 以及最后一日条件
function initNoConfirmRadio() {
	$('.noconfirmradio').on('ifChecked', function() {
		unAppoint(this);
	});
	$('.lastdayradio').on('ifChecked', function() {
		lastDay(this);
	});

}

// 初始化每个页签选择的的radio
function initRadio() {
	// 周期选择的radio
	$('.cycleradio').on('ifChecked', function() {
		writeStartAndEnd(this, "-");
	});
	// 循环选择的radio
	$('.loopradio').on('ifChecked', function() {
		writeStartAndEnd(this, "/");
	});

	// 指定选择的radio 即#
	$('.designradio').on('ifChecked', function() {
		writeStartAndEnd(this, "#");
	});

	// 自主选择的radio
	$('.choiceradio').on('ifChecked', function() {

		var spanname = this.name;
		var theList = $("." + spanname + "List").find(':checkbox');
		var maxvalue = eval("max_" + this.name);
		changeSpanFromCheckList(theList, maxvalue, spanname);
	});

	// 最近工作日的radio
	$('.nearradio').on('ifChecked', function() {
		writeEnd(this, "W");
	});

	// 最后一个周几的radio
	$('.lastradio').on('ifChecked', function() {
		writeEnd(this, "L");
	});

}

// 初始化div内容监听绑定函数，即点击div，对应radio被选中
function initRadioCheckDiv() {
	$('.radiocheck').click(function() {
		radioCheckByClick(this);

		/***************create by fox*****************/
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
        /********************************/
	});

	$.each(itemList, function(n, value) {
		var checkList = $("." + value + "List");
		checkList.click(function() {
			var theRadio = this.closest('.radiocheck').find(':radio');
			theRadio.eq(0).iCheck('check');
		});
	});
}

// 绑定数字微调器
function initSpinner() {
	// 绑定数字微调器
	$(".cyclespin").spinner('changing', function(e, newVal, oldVal) {
		// trigger immediately
		writeStartAndEnd(this, "-");
	});
	$(".loopspin").spinner('changing', function(e, newVal, oldVal) {
		// trigger immediately
		writeStartAndEnd(this, "/");
	});
	$(".designspin").spinner('changing', function(e, newVal, oldVal) {
		// trigger immediately
		writeStartAndEnd(this, "#");
	});
	$(".nearspin").spinner('changing', function(e, newVal, oldVal) {
		// trigger immediately
		writeEnd(this, "W");
	});
	$(".lastspin").spinner('changing', function(e, newVal, oldVal) {
		// trigger immediately
		writeEnd(this, "L");
	});
}

// 表达式结果由表格生成到cron表达式
function initCronFromTable() {
	// 查找所有name以v_开头的span元素
	var vals = $("span[name^='v_']");
	var cron = $("#croninfo");
	vals.change(function() {
		var item = [];
		vals.each(function() {
			item.push(this.innerHTML);
		});
		cron.val(item.join(" "));
	});
}

// 定义checkbox在被点击的时候的绑定函数 循环遍历页签数组
function initListChange() {
	$.each(itemList, function(n, value) {
		var checkList = $("." + value + "List").find(':checkbox');
		var maxvalue = eval("max_" + value);
		initChangeOnCheckboxList(checkList, maxvalue, value);
	});
}

// 给list的checkbox绑定点击方法 入参是list，最大值，指定输出的span名称
function initChangeOnCheckboxList(checkList, maxvalue, spanname) {
	checkList.on('ifChanged', function() {
		// 模拟div被点击过一次
		$(this).closest('.radiocheck').click();
		changeSpanFromCheckList(checkList, maxvalue, spanname);
	});
}


// 清除选中状态
function unselectAll(listId) {
	for ( var i = 1; i <= 12; i++) {
		$("#l_quarter input[value='" + i + "']").iCheck('uncheck');
	}
}