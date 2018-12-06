$(document).ready(function() {
    $("#pageNo").val("1");
    synonymWordList.query();
    funnames= 'synonymWordList.query()';
});

var synonymWordList = {
	query : function() {
		var url = "/synonymWord/querySynonymWord.wap";
		var params = "backdata=data&callback=synonymWordList.queryCallback"
				+ QueryMain.getJson();
		params = QueryMain.setParams(params);
		QueryMain.loadData(url, params);
	},
	queryCallback : function(data) {
		var name = data.username;
		if(name == null || name == 'null' || name == ''){
			location.href="/searchH/";
		}else{
			// 渲染模板
			QueryMain.processTemplateDataSplit("#result", "#nextPage",
					"templateContainer", data);
			initPagination(data.limit.pageNum, data.limit.totalNum,
					data.limit.rowDisplayed);
			$("#verspan").load("ver.html"); 
		}
	},
	start : function(id) {
		var url = "/synonymWord/startApp.wap";
		var params = "backdata=data&callback=synonymWordList.callBack&id=" + id;
		QueryMain.loadData(url, params);
	},
	stop : function(id) {
		var url = "/synonymWord/stopApp.wap";
		var params = "backdata=data&callback=synonymWordList.callBack&id=" + id;
		QueryMain.loadData(url, params);
	},
	reset : function(id) {
		var url = "/synonymWord/resetApp.wap";
		var params = "backdata=data&callback=synonymWordList.callBack&id=" + id;
		QueryMain.loadData(url, params);
	},
	callBack : function(data) {
		if (data.flag == "OK") {
			alert("操作成功");
			window.location.reload();// 刷新当前页
		} else {
			alert(data.flag);
		}
	},

	exit : function(id) {
		$.ajax({
			type : "post",
			url : "/searchH/user/exit",
			dataType : "json",
			success : function(res) {
				if (res.success) {
					location.href = "/searchH/";
				}
			}
		});
	}
};