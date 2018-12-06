$(document).ready(function() {
	$("#pageNo").val("1");
	dictionarylist.query();
	funnames= 'dictionarylist.query()';
	//splitPage.pageSplitJz();
	//addBankCard.query();
});

var dictionarylist = {
		query:function(){
			var map = getUrlParams();
			var indexId = map.get("index_id");
			var url = "/dictionary/queryDictionary.wap";
			var params="backdata=data&callback=dictionarylist.queryCallback&index_id="+indexId+QueryMain.getJson();
			params = QueryMain.setParams(params);
			QueryMain.loadData(url,params);
		},
		queryCallback:function(data){
			//渲染模板
			QueryMain.processTemplateDataSplit("#result", "#nextPage", "templateContainer",
					data);
			initPagination(data.limit.pageNum,data.limit.totalNum,data.limit.rowDisplayed);
		}
		
};