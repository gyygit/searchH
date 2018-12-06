$(document).ready(function() {
	$("#pageNo").val("1");
	searchRuleDateList.beforeQuery();
	funnames= 'searchRuleDateList.queryList()';
	//splitPage.pageSplitJz();
	//addBankCard.query();
});

var searchRuleDateList = {
		beforeQuery:function(){
			var map = getUrlParams();
			var appId = map.get("app_id");
			var url = "/searchRuleDate/beforeQuerySearchRuleDate.wap";
			var params="backdata=data&callback=searchRuleDateList.queryCallback&app_id="+appId+QueryMain.getJson();
			params = QueryMain.setParams(params);
			QueryMain.loadData(url,params);
			
		},
		queryCallback:function(data){
			if(data.flag == "OK"){
				searchRuleDateList.queryList();
			}else{
				alert(data.flag);
				location.href="/searchH/phtml/searchapp/searchapplb.html";
			}
		},
		queryList:function(){
			var map = getUrlParams();
			var appId = map.get("app_id");
			var url = "/searchRuleDate/querySearchRuleDate.wap";
			var params="backdata=data&callback=searchRuleDateList.queryListCallBack&app_id="+appId+QueryMain.getJson();
			params = QueryMain.setParams(params);
			QueryMain.loadData(url,params);
		},
		queryListCallBack:function(data){
			//渲染模板
			QueryMain.processTemplateDataSplit("#result", "#nextPage", "templateContainer", data);
			initPagination(data.limit.pageNum,data.limit.totalNum,data.limit.rowDisplayed);
		},
		
		updateSearchRule : function(){
			$.ajax({
				type:"post",
				url : "/searchH/searchRuleDate/updateSyncSearchRule",
				dataType : "json",
				success:function(res){
					if(res.success){
						if(res.data.message=='OK'){
							alert("操作成功！");
							alert("注意：请删除该路径下 "+res.data.path+"的SearchRuleDateDAO文件夹," +
									"然后把文件夹SearchRuleDateDAO1重命名为SearchRuleDateDAO，最后重启服务！");
						}else{
							alert(res.data.message);
						}
					}else{
						alert("出现问题了");
					}
				},
				error : function(res){
					alert("请求出错了");
				}
			});
		}
};