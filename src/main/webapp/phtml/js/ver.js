var ver="版本:1.1";
var ver="版本:1.2";
var ver="版本:1.3";
var ver="版本:1.4";
var ver="版本:1.5";
var ver="版本:1.6";
var ver="版本:1.7";
var ver="版本:1.8";
var ver="版本:1.8.1";
var ver="版本:1.8.2";
var ver="版本:1.9.0.0";
var ver="版本:2.0.0.0";
var ver="版本:2.0.1.0";
var ver="版本:2.0.2.0";
var verc = {
		query:function(){
			var url = "/searchApp/queryuser.wap";
			var params="backdata=data&callback=verc.queryCallback"+QueryMain.getJson();
			params = QueryMain.setParams(params);
			QueryMain.loadData(url,params);
		},
		queryCallback:function(data){
			var name = data.username;
			if(name == null || name == 'null' || name == ''){
				location.href="/searchH/";
			}else{
				$("#user").text(data.username);
				$("#ver").html(ver);
			}
		}
};

verc.query();