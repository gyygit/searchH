var i = 2;
$(document).ready(function() {
	$("#pageNo").val("1");
	luceneSearchhot.query();
	funnames = 'luceneSearchhot.query()';
	//splitPage.pageSplitJz();
		//addBankCard.query();
});

var luceneSearchhot = {
	query : function() {
		var map = getUrlParams();
		var commandId = map.get("commandId");
    var app_id = map.get("app_id");
		var url = "/luceneSearch/searchhot.wap";
		var params = "backdata=data&callback=luceneSearchhot.queryCallback&commandId=" + commandId+"&app_id=" + app_id+ QueryMain.getJson();
		params = QueryMain.setParams(params);
		QueryMain.loadData(url, params);
	},
	queryCallback : function(data) {
		//渲染模板
		QueryMain.processTemplateDataSplit("#result", "#nextPage", "templateContainer", data);
		initPagination(data.limit.pageNum, data.limit.totalNum, data.limit.rowDisplayed);
	} 
	 
};