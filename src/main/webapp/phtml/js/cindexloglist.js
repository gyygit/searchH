$(document).ready(function() {
	$("#pageNo").val("1");
	cindexlogList.query();
	funnames= 'cindexlogList.query()';
	//splitPage.pageSplitJz();
	//addBankCard.query();
	
});

var cindexlogList = {
		query:function(){
		    var url = "/cindexlog/queryCindexLog.wap";                      
			var params="backdata=data&callback=cindexlogList.queryCallback"+QueryMain.getJson();
			params = QueryMain.setParams(params);
			QueryMain.loadData(url,params);
		},
		getPendings : function(appcods,flag,callback) {
			setInterval(function() {
					cindexlogList.queryjisuan(appcods,flag,callback);
			}, 2000);
		},
		queryCallback:function(data){
				//渲染模板
				QueryMain.processTemplateDataSplit("#result", "#nextPage", "templateContainer",
						data);
				initPagination(data.limit.pageNum,data.limit.totalNum,data.limit.rowDisplayed);
				$("#verspan").load("ver.html");
		},
		queryjisuan:function(appcods,flag,callback){
		    var url = "/cindexlog/jisuan.wap";                      
			var params="backdata=data&callback=cindexlogList."+callback+"&appcode="+appcods+"&flag="+flag;
			params = QueryMain.setParams(params);
			QueryMain.loadData(url,params);
		},
		queryCallbacka:function(data){
			
			for (var key in data) {  
				  var a = map[key];
				  alert(key);
					for (var k in a) {  
						alert(k+'---'+a[k]);
					}   
					
				}   
			//$("#progress-bar-" + tid).attr("style", "width:"+response.percent + "%");
			//$("#percent-" + tid).html(response.percent + "%");
		} 
		 
		 
};