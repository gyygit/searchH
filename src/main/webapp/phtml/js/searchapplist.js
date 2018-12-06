$(document).ready(function() {
	
	$("#pageNo").val("1");
	searchAppList.query();
	funnames= 'searchAppList.query()';
	//splitPage.pageSplitJz();
	//addBankCard.query();
	searchAppList.getPendings("","appcode","queryCallbacka");
});

var searchAppList = {
		query:function(){
			var url = "/searchApp/querySearchApp.wap";
			var params="backdata=data&callback=searchAppList.queryCallback"+QueryMain.getJson();
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
				$("#jisuanappcode").val(data.jisuanappcode);
				$("#verspan").load("ver.html");
			}
		},
		start:function(id){ 
			var fun = function() {
				$("#astart").css("cursor", "default");
				$("#astart").css("opacity", "0.2");
				 $("#astart").attr('href', '#'); 
				 $("#astart").attr("disabled", "disabled");
				var url = "/searchApp/startApp.wap";
				var params="backdata=data&callback=searchAppList.callBack&id="+id;
				QueryMain.loadData(url,params);
			}
			DialogTemplate.showConfirm("确定要重启吗",fun,"");
		},
		stop:function(id){
			var funstop = function() {
				$("#astop").css("cursor", "default");
				$("#astop").css("opacity", "0.2");
				 $("#astop").attr('href', '#'); 
				 $("#astop").attr("disabled", "disabled");
				var url = "/searchApp/stopApp.wap";
				var params="backdata=data&callback=searchAppList.callBack&id="+id;
				QueryMain.loadData(url,params);
			}
			DialogTemplate.showConfirm("确定要停止吗",funstop,"");
			
		},
		reset:function(id){
			
			var funreset = function() {
				$("#rstart").css("cursor", "default");
				$("#rstart").css("opacity", "0.2");
				 $("#rstart").attr('href', '#'); 
				 $("#rstart").attr("disabled", "disabled");
				var url = "/searchApp/resetApp.wap";
				var params="backdata=data&callback=searchAppList.callBack&id="+id;
				QueryMain.loadData(url,params);
			}
			DialogTemplate.showConfirm("确定要重新应用吗",funreset,"");
		},
		callBack:function(data){
			if(data.flag == "OK"){
				alert("操作成功");
				window.location.reload();//刷新当前页
			}else{
				alert(data.flag);
			}
		},
		erroinfo:function(infoid){
			alert($("#percent-error-" + infoid).val());
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
		},
		getPendings : function(appcods,flag,callback) {
			
			setInterval(function() {
					searchAppList.queryjisuan(appcods,flag,callback);
			}, 2000);
		},
		queryjisuan:function(appcods,flag,callback){
			if(""==$("#jisuanappcode").val()){
				return;
			}
			appcods = $("#jisuanappcode").val();
		    var url = "/cindexlog/jisuan.wap";                      
			var params="backdata=data&callback=searchAppList."+callback+"&appcode="+appcods+"&flag="+flag;
			params = QueryMain.setParams(params);
			QueryMain.loadData(url,params);
		},
		queryCallbacka:function(data){
			for (var key in data) {  
				 $("#progress-bar-" + key).attr("style", "width:"+data[key]['jindu'] + "%");
				 $("#percent-" + key).html(data[key]['jindu'] + "%");
				 if(data[key]['flag']==2)
				 {
				    $("#percent-" + key).html("<font color='red' size='4'>error:click</font>");  
				    $("#progress-bar-" + key).attr("style", "width:0" + "%");
				    $("#percent-error-" + key).val(data[key]['errorinfo']);
				    $("#percent-" + key).attr("onclick","searchAppList.erroinfo('"+key+"');"); 
				 }else{
					$("#percent-error-" + key).val="";
					$("#percent-" + key).removeAttr("onclick");
				 }
				 $("#percent-ku-" + key).html(data[key]['ku']);
				 $("#percent-haoshi-" + key).html(data[key]['haoshi']); 
				 $("#percent-date-" + key).html(data[key]['date']);
				 
				}   
			
		} 
};