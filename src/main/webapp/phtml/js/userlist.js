$(document).ready(function() {
	$("#pageNo").val("1");
	userlist.query();
	funnames= 'userlist.query()';
});

var userlist = {
		query:function(){
			var url = "/user/queryUser.wap";
			var params="backdata=data&callback=userlist.queryCallback"+QueryMain.getJson();
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
				$("#verspan").load("ver.html");
			}
		},
		
		start:function(id){
			var url = "/user/changeStart.wap";
			var params="backdata=data&callback=userlist.callBack&id="+id;
			QueryMain.loadData(url,params);
		},
		stop:function(id){
			var url = "/user/changeStop.wap";
			var params="backdata=data&callback=userlist.callBack&id="+id;
			QueryMain.loadData(url,params);
		},
		callBack:function(data){
			if(data.flag == "OK"){
				alert("操作成功");
				window.location.reload();//刷新当前页
			}else{
				alert(data.flag);
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
