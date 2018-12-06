$(document).ready(function() {
	$("#pageNo").val("1");
	dictionaryindexlist.query();
	funnames= 'dictionaryindexlist.query()';
	//splitPage.pageSplitJz();
	//addBankCard.query();
});

var dictionaryindexlist = {
		query:function(){
			var url = "/dictionaryindex/queryDictionaryindex.wap";
			var params="backdata=data&callback=dictionaryindexlist.queryCallback"+QueryMain.getJson();
			params = QueryMain.setParams(params);
			QueryMain.loadData(url,params);
		},
		queryCallback:function(data){
			var name = data.username;
			if(name == null || name == 'null' || name == ''){
				location.href="/searchH/";
			}else{
				//渲染模板
				QueryMain.processTemplateDataSplit("#result", "#nextPage", "templateContainer",
						data);
				initPagination(data.limit.pageNum,data.limit.totalNum,data.limit.rowDisplayed);
				//$("#user").text(data.username);
				$("#verspan").load("ver.html");
			}
		},
		
		exit:function(id){
			$.ajax({
				type:"post",
				url : "/searchH/user/exit",
				dataType : "json",
				success:function(res){
					if(res.success){
						location.href="/searchH/";
					}
				}
			});
		}
		
};