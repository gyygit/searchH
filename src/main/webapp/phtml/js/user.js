var flag = true;
$(document).ready(function() {
	user.query();
	funnames = 'user.query()';
});

var user = {
		query:function(){
			var map = getUrlParams();
			var id = map.get("id");
			//id为空：新增，不为空：修改。
			var url = "/user/beforeEditUser.wap";
			var params="backdata=data&callback=user.queryCallback&id="+id;
			params = QueryMain.setParams(params);
			QueryMain.loadData(url,params);
		},
		queryCallback:function(data){
			//渲染模板
			jQuery("#content").setTemplateElement("user_template",null,{filter_data: false}).processTemplate(data);
			if(data.m.id==""){
				user.getSearchAppBeforeAdd();
			}
		},
		save:function(){
			if(flag) {
				var url = "";
				var app = user.getCheckedAppCode();
				//id为空：新增，不为空：修改。
				var inputId = $("#tab_id").val();
				if(inputId == ""){
					if(user.validDate()) {
						if(user.verify(app)){
							flag = false;
							url = "/user/addUser.wap";
						}
					}
				}else{
					if(user.validDate1()) {
						if(user.verify(app)){
							flag = false;
							url = "/user/editUser.wap";
						}
					}
				}
				var params="backdata=data&callback=user.processData"+QueryMain.getJson()+"&appcode="+app;
				QueryMain.loadData(url,params);
			}
		},
		processData:function(data){
			if(data.flag=="OK"){
				alert("保存成功");
				flag = true;
				location.href="/searchH/phtml/searchapp/userlb.html";
			}
			else if(data.flag=="ReLogin") {
				alert("保存成功");
				flag = true;
				location.href="/searchH/phtml/searchapp/userlb.html";
				alert("你修改了业务应用名称，请重新登录！");
				location.href="/searchH/";
			}else{
				alert(data.flag);
				flag = true;
			}
		},
		
		verify : function(app){
			if(app.length==0){
				alert("业务应用名称不能为空!");
				return false;
			}
			return true;
		},
		
		validDate:function(){
			if($("#tab_code").val()==""){
				alert("登录名不能为空!");
				return false;
			}
			if($("#tab_code").val()=="admin"){
				alert("admin为超级管理员用户，您不能添加！");
				return false;
			}
			if($("#tab_name").val()==""){
				alert("昵称不能为空!");
				return false;
			}
			if($("#tab_password").val()==""){
				alert("密码不能为空!");
				return false;
			}
			if($("#tab_verify_password").val()==""){
				alert("确认密码不能为空!");
				return false;
			}
			if($("#tab_password").val()!= $("#tab_verify_password").val()){
				alert("密码和确认密码不一致!");
				return false;
			}
			var pwd = $("#tab_password").val();
			if(pwd.length<6 || pwd.length>15){
				alert("密码长度在6-15之间！");
				return false;
			}
			var patrn=/^(\w){6,15}$/;
			if (!patrn.exec(pwd)){
				alert("密码只能输入6-15个字母、数字、下划线！");
				return false 
			}
			return true;
		},
		
		validDate1:function(){
			if($("#tab_name").val()==""){
				alert("昵称不能为空!");
				return false;
			}
			return true;
		},
		
		getSearchAppBeforeAdd:function(){
			$.ajax({
				type : "post",
				url : "/searchH/user/beforeAddUser",
				dataType : "json",
				success:function(res){
					if(res.success){
						$("#addAppCode").val("");
						var list = res.data.apps;
						for(var i=0; i<list.length;i++){
							var code = list[i].appCode;
							var name = list[i].appName;
							var app = "<label><input name='tab_appcode' type='checkbox' value='"+ code+"' />"+name+"</label>"
							$("#addAppCode").append(app);
						}
					}
				},
				error : function(res){
					alert("瞅啥瞅，是你出错了呗");
				}
			});
		},
		
		getCheckedAppCode : function(){
			var app = "";
			$("input:checkbox[name='tab_appcode']:checked").each(function() {
	            app += $(this).val() + ",";
	        });
			return app.substr(0,app.length-1);
		}
};