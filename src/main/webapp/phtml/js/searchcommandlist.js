$(document).ready(function() {
	$("#pageNo").val("1");
	searchCommonList.query();
	funnames= 'searchCommonList.query()';
	//splitPage.pageSplitJz();
	//addBankCard.query();
	searchCommonList.getPendings("","commandcode","queryCallbacka");
});

var searchCommonList = {
		query:function(){
			var map = getUrlParams();
			var appId = map.get("app_id");
			var url = "/searchCommand/querySearchCommand.wap";
			var params="backdata=data&callback=searchCommonList.queryCallback&app_id="+appId+QueryMain.getJson();
			params = QueryMain.setParams(params);
			QueryMain.loadData(url,params);
		},
		queryCallback:function(data){
			//渲染模板
			QueryMain.processTemplateDataSplit("#result", "#nextPage", "templateContainer", data);
			initPagination(data.limit.pageNum,data.limit.totalNum,data.limit.rowDisplayed);
			$("#jisuanappcode").val(data.jisuanappcode);
		},
		getPendings : function(appcods,flag,callback) {
			
			setInterval(function() {
					searchCommonList.queryjisuan(appcods,flag,callback);
			}, 2000);
		},
		queryjisuan:function(appcods,flag,callback){
			if(""==$("#jisuanappcode").val()){
				return;
			}
			appcods = $("#jisuanappcode").val();
		    var url = "/cindexlog/jisuan.wap";                      
			var params="backdata=data&callback=searchCommonList."+callback+"&appcode="+appcods+"&flag="+flag;
			params = QueryMain.setParams(params);
			QueryMain.loadData(url,params);
		},
		queryCallbacka:function(data){
			for (var key in data) {  
				 $("#progress-bar-" + key).attr("style", "width:"+data[key]['jindu'] + "%");
				 $("#percent-" + key).html(data[key]['jindu'] + "%");
				 $("#percent-ku-" + key).html(data[key]['ku']);
				 $("#percent-haoshi-" + key).html(data[key]['haoshi']); 
				 $("#percent-date-" + key).html(data[key]['date']);
				 
				}   
		} 
		 
};